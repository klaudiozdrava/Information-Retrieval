package Controller;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queries.mlt.MoreLikeThis;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.flexible.core.builders.QueryBuilder;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.TextFragment;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.search.vectorhighlight.BaseFragmentsBuilder;
import org.apache.lucene.search.vectorhighlight.FastVectorHighlighter;
import org.apache.lucene.search.vectorhighlight.FieldQuery;
import org.apache.lucene.search.vectorhighlight.FragListBuilder;
import org.apache.lucene.search.vectorhighlight.FragmentsBuilder;
import org.apache.lucene.search.vectorhighlight.ScoreOrderFragmentsBuilder;
import org.apache.lucene.search.vectorhighlight.SimpleFragListBuilder;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.lucene.util.fst.Builder;

public class Searcher {
	
	 IndexSearcher indexSearcher;
	 QueryParser queryParser;
	 Query query;
	 DirectoryReader reader;
	 Directory indexDirectory;
     Analyzer analyzer = new MyOwnAnalyzer();
     
	 
	 public Searcher(String indexDirectoryPath,Similarity sim) throws IOException {
		 
		  indexDirectory = FSDirectory.open(new File(indexDirectoryPath));
	      reader = DirectoryReader.open(indexDirectory);
	      indexSearcher = new IndexSearcher(reader);
	      indexSearcher.setSimilarity(sim);
	      
	      queryParser = new MultiFieldQueryParser(Version.LUCENE_41,
	    		  new String[] {"title","authors","content"},analyzer);
	      
	      queryParser.setPhraseSlop(3);
	     
	      
	   }
	 
	 private String GetDocNumber(String docId) {
		 int positionA = docId.indexOf("doc");
		 int positionB=docId.indexOf(".");
		 String doc =docId.substring(positionA + 3, positionB);
		 return doc;
		 
	 }
	 
	 private List<MyDocument> Top(TopDocs tops) throws CorruptIndexException, IOException{
		 
		  ReadFiles reading = new ReadFiles();
		  List<MyDocument> All_Documents = new ArrayList<>();
		  for (ScoreDoc scoreDoc : tops.scoreDocs) {
	    	  Document doc = getDocument(scoreDoc);
              String id=doc.get("id");
              String get_docNumber=GetDocNumber(id);
         
              File file = new File(id);
              if(file.isFile()) {
            	  Map<String, String> myMap = reading.Extract(file);
            	  String title = myMap.get("Title");
            	  String authors = myMap.get("Authors");
            	  String words = myMap.get("Words");
           
            	  All_Documents.add( new MyDocument(scoreDoc.doc,title,words,authors,title+"-"+authors,get_docNumber));
              }
	      }
		  
		  return All_Documents;
	 }
	 
	 
	 private SimpleHTMLFormatter htmlFormatter() {
		 SimpleHTMLFormatter formatter =
				 new SimpleHTMLFormatter("<span style=\"background-color: #80ff80\">","</span>");
		 return formatter;
	 }
	 
	 //highlight the content field of documents
	 private String ContentHighlighter(String field,String text) throws IOException, InvalidTokenOffsetsException {
		 Highlighter highlighter = new Highlighter(htmlFormatter(), new QueryScorer(query));
		 TokenStream tokenStream = TokenSources.getTokenStream(field, text, analyzer) ;
		 TextFragment[] frag = highlighter.getBestTextFragments(tokenStream, text, false, 4);
		 String highlightedContents="";
		 for (int j = 0; j < frag.length; j++) {
             if ((frag[j] != null) && (frag[j].getScore() > 0)) {             
            	 highlightedContents+=frag[j].toString();
             }
		 }
		 return highlightedContents;
	 }
	 
	 
	 //search documents for query object
	 public List<MyDocument> search( String searchQuery) 
	      throws IOException, ParseException, InvalidTokenOffsetsException {
	      query = queryParser.parse(searchQuery);//parse query
	      TopDocs topDocs = indexSearcher.search(query, 50);
	      List<MyDocument> Search_Documents = new ArrayList<>();
	      ReadFiles reading = new ReadFiles();
		  for (ScoreDoc scoreDoc : topDocs.scoreDocs) {//foreach document that has retrieved
	    	  Document doc = getDocument(scoreDoc);//document id
              String id=doc.get("id");//file name
              String get_docNumber=GetDocNumber(id);
              File file = new File(id);
              if(file.isFile()) {
            	  Map<String, String> myMap = reading.Extract(file);
            	  String title = myMap.get("Title");
            	  String authors = myMap.get("Authors");
            	  String words = myMap.get("Words");
            	  if (words.equals("")) {
            		  String title_highlight=ContentHighlighter("title",title);
            		  String authors_highlight=ContentHighlighter("authors",authors);
            		  
            		  title_highlight+=authors_highlight;
        			  Search_Documents.add( new MyDocument(scoreDoc.doc,title,words,authors,title_highlight,get_docNumber));

            	  }else {
            		  String content_highlight=ContentHighlighter("content",words);
            		  String authors_highlight=ContentHighlighter("authors",authors);
            		  
            		  content_highlight+=authors_highlight;
            		  if(content_highlight.equals("")) {
            			  String title_highlight=ContentHighlighter("title",title);
            			  content_highlight+=title_highlight;
            		  }
            		  Search_Documents.add( new MyDocument(scoreDoc.doc,title,words,authors,content_highlight,get_docNumber));
                		 
            	  }
              }
	      }
		  
	      return Search_Documents;
	 }
	 
	

	public void CloseEverything() throws IOException {
    	 
    	 reader.close();
    	 indexDirectory.close();
     }
     
     //Find similar documents based on id
     public List<MyDocument> FindSimilarDocs(int id) throws IOException {
    	 MoreLikeThis mlt = new MoreLikeThis(reader);
    	 mlt.setFieldNames(new String[] {"title", "authors","content"});
    	 mlt.setMinTermFreq(1);
    	 mlt.setMinDocFreq(1);
    	 mlt.setAnalyzer(new MyOwnAnalyzer());
    	 
    	 Query query = mlt.like(id);
    	 
    	 TopDocs similarDocs = indexSearcher.search(query, 50);
    	 List<MyDocument> similar_docs = Top(similarDocs);
    	 
    	 return similar_docs;
    	 
     }

	 public Document getDocument(ScoreDoc scoreDoc) 
	      throws CorruptIndexException, IOException {
	      return indexSearcher.doc(scoreDoc.doc);	
	 }
	 

}

package Controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Index {
	

	private  IndexWriter writer; 
	
	//Create index
	public Index(String indexDirectoryPath,Similarity sim) throws IOException {
		
		 
	      //this directory will contain the indexes
		  Directory indexDirectory = 
		         FSDirectory.open(new File(indexDirectoryPath));
	      
		  Analyzer analyzer = new MyOwnAnalyzer();
	      //create the indexer
		  IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_41,analyzer);
		  config.setSimilarity(sim);
	      writer = new IndexWriter(indexDirectory, config);
	   }

	   public void close() throws CorruptIndexException, IOException {
		 
	      writer.close();
	   }
	   
	   //Create document and fields objects.Add fields to document with their data
	   private Document getDocument(File file,Map<String, String> mapping) throws IOException {
	      Document document = new Document();
	      
	      TextField id = new TextField("id",file.getCanonicalPath(),Field.Store.YES);
	      
	      TextField title = new TextField("title",
	         (String) mapping.get("Title"),Field.Store.YES);
	     
	      TextField authors = new TextField("authors",
	         (String) mapping.get("Authors"),Field.Store.YES);
	      
	      TextField contentField = new TextField("content", (String) mapping.get("Words"), Field.Store.NO);
	      
          contentField.setBoost(1.4F);//.W field is more important for questions
	      authors.setBoost(20.2F);
	      document.add(id);
	      document.add(title);
	      document.add(contentField);
	      document.add(authors);
	      
		  
	      return document;
	   }   
	   

		
	   
	   private void indexFile(File file,Map<String, String> map) throws IOException {
		      //System.out.println("Indexing "+file.getCanonicalPath());
		      Document document = getDocument(file,map);
		      writer.addDocument(document);
		   }
	   
	   
	   public int CreateIndex (String folder) throws IOException {
		   
		   File[] listOfFiles = new File(folder).listFiles();
		   ReadFiles file_read = new ReadFiles();
		   
		   for (File file : listOfFiles) {
			    if (file.isFile()) {
			    	Map<String, String> myMap = file_read.Extract(file);
			        indexFile(file,myMap);
			    }
			}
		   		return writer.numDocs();
		} 

}
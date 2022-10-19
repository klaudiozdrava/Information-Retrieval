package Controller;



import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.miscellaneous.CapitalizationFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.analysis.util.WordlistLoader;
import org.apache.lucene.util.Version;







public class MyOwnAnalyzer extends Analyzer {
	
	 Reader myStopfileReader;
	 
	
	 
	 @Override
	    protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
	        StandardTokenizer src = new StandardTokenizer(Version.LUCENE_41, reader);
	        TokenStream result = new StandardFilter(Version.LUCENE_41,src);
	        result = new LowerCaseFilter(Version.LUCENE_41,result);
	        try {
				myStopfileReader = new FileReader("D:\\Lucene-Texts\\common_words.txt");
				CharArraySet stopset = WordlistLoader.getWordSet(myStopfileReader,Version.LUCENE_41);
				result = new StopFilter(Version.LUCENE_41,result,stopset);
		        result = new PorterStemFilter(result);
		        result = new CapitalizationFilter(result);
		 
			} catch (IOException e) {
				e.printStackTrace();
			}
	        return new TokenStreamComponents(src, result);
	    }
	 
	 
	
	

}

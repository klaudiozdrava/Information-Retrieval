package Controller;


import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.util.List;


import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.search.similarities.TFIDFSimilarity;


/**
 * Servlet implementation class IRServlet
 */
public class IRServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private final String BM25 = "D:\\Lucene-Texts\\BM25";
	private final String Vectors = "D:\\Lucene-Texts\\Vectors";
	private final String dataDir = "D:\\Lucene-Texts\\InfoRet";
	
	private final TFIDFSimilarity TDIDF = new DefaultSimilarity ();
	private final BM25Similarity BM25sim = new BM25Similarity();
	
	boolean FirstTime=true;
	Searcher searcherBM25;
	Searcher searcherIDF;
	Searcher searcher;  
	
	private void Indexing(Similarity sim,String folder) {
		 try {
			    File[] listOfFiles = new File(folder).listFiles();
			    if (listOfFiles.length==0) {
			    	Index indexer= new Index(folder,sim);
					int numDocs=indexer.CreateIndex(dataDir);
					System.out.println(numDocs + " Have been indexed");
					indexer.close();
					
			    }
			} catch (IOException e) {
				e.printStackTrace();
		}
	}
	
	private void AdjustInformations() throws IOException {
		
		Indexing(BM25sim,BM25);
		Indexing(TDIDF,Vectors);
		searcherBM25= new Searcher(BM25,BM25sim);
		searcherIDF= new Searcher (Vectors,TDIDF);
		searcher = searcherIDF;
	}
	
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		String action = request.getServletPath();
		switch(action) {
			case "/Similar":
				String query=request.getQueryString();
				int index=query.indexOf( '=' );
				String id =query.substring(index+1, query.length());
				List<MyDocument> simDocs = searcher.FindSimilarDocs(Integer.parseInt(id));
				request.setAttribute("documents", simDocs);
				jakarta.servlet.RequestDispatcher rdi = request.getRequestDispatcher("Results.jsp");
		        rdi.forward(request, response);
				break;
			case "/BM25":
				searcher=searcherBM25;
				jakarta.servlet.RequestDispatcher r = request.getRequestDispatcher("Results.jsp");
		        r.forward(request, response);
				break;
			case "/Vectors":
				searcher=searcherIDF;
				jakarta.servlet.RequestDispatcher d = request.getRequestDispatcher("Results.jsp");
		        d.forward(request, response);
				break;
			default:
				if(FirstTime) {
					AdjustInformations();
					FirstTime=false;
				}
				String searchQuery = request.getParameter("SearchIR");
				try {
					List<MyDocument> mydocs =searcher.search(searchQuery);
					request.setAttribute("documents", mydocs);
					jakarta.servlet.RequestDispatcher rd = request.getRequestDispatcher("Results.jsp");
			        rd.forward(request, response);
			        
				} catch (IOException e) {
					e.printStackTrace();
				
				} catch (InvalidTokenOffsetsException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				break;
		}
		
	}

}

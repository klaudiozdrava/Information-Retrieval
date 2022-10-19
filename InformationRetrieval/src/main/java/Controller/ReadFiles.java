package Controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ReadFiles {
	
	public Map<String,String> Extract(File file) throws IOException {
		   try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String st;
			   boolean Title=false;
			   boolean Authors=false;
			   boolean Words=false;
			   
			   String TitleContent="";
			   String WordContent="";
			   String AuthorsContent="";	   
			   
			   Map<String,String> map = new HashMap <>();
			   while ((st = br.readLine()) != null) {
				   if (st.equals(".T")) {
					   Title=true;
				   }
				   else if(st.equals(".B")) {
					   Title=false;
				   }
				   else if(st.equals(".W")) {
					   Title=false;
					   Words=true;
				   }
				   else if(Title) {
					  TitleContent = TitleContent + st + " ";
				   }
				   else if(st.equals(".A")) {
					   Words=false;
					   Authors=true;
				   }
				   else if(Words) {
					   WordContent=WordContent + st + " ";
				   }
				   else if(st.equals(".N") || st.equals(".K")) {
					   break;
				   }
				   else if(Authors) {
					   AuthorsContent=AuthorsContent + st + " " ;
				   }
				   
				}
			   map.put("Title", TitleContent);
			   map.put("Words", WordContent);
			   map.put("Authors", AuthorsContent);
			   
			   return map;
		  }
	   }


}

package Controller;



public class MyDocument {
	
	private String title;
	private String content;
	private String authors;
	private int id;
	private String highlighted;
	private String docNumber;
	
	
	MyDocument(int Id,String title,String content,String authors,String high,String docN){
		this.title=title;
		this.content=content;
		this.authors=authors;
		this.id=Id;
		this.highlighted=high;
		this.docNumber=docN;
	}

	public String getDocNumber() {
		return docNumber;
	}

	public String getHighlighted() {
		return highlighted;
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	public String getAuthors() {
		return authors;
	}
	
	
	
}

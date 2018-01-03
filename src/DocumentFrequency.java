
public class DocumentFrequency {
	long docLength;
	long maxFrequency;
	String documentName;
	String documentTitle;
	
	public DocumentFrequency(long a, long b, String name, String title){
		this.docLength=a;
		this.maxFrequency=b;
		this.documentName=name;
		this.documentTitle=title;
	}
}

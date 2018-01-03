import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GoodreadsCrawlerITR2 {

	public static Document doc;
	public static int bookCounter=33542;
	
	public static void main(String[] args) throws Exception {

//		doc = Jsoup.connect("https://www.goodreads.com/book/show/100090.Lord_of_a_Visible_World").userAgent("Mozilla").timeout(0).get();
//		saveBook("lovecraftian", doc);
		BufferedReader br = null;

		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader("goodreads/FINALDATA.txt"));
			String bookURL="";
			String category="";
			
			bookURL = br.readLine();
			int counter=0;
			
			if(counter!=bookCounter)
			{
				while ((sCurrentLine = br.readLine()) != null) {
					if(sCurrentLine.contains("https://")) {
						//System.out.println("Book " + counter + " skipped");
						counter++;
						category="";
						bookURL=sCurrentLine;
						if(counter==bookCounter) {
							break;
						}
					}
				}
			}
			
			while ((sCurrentLine = br.readLine()) != null) {
				if(sCurrentLine.contains("https://")) {
					doc = Jsoup.connect(bookURL).userAgent("Mozilla").timeout(0).get();
					try {
						saveBook(category, doc);
						category="";
						System.out.println("Book " + counter + " saved");
						counter++;
					}
					catch(Exception e) {
						System.out.println("############# counter="+ counter+" BOOK NOT SAVED URL:"+ bookURL + " CATEGORY:"+category + " ####################");
					}
					bookURL=sCurrentLine;
				}
				else {
					category = category +sCurrentLine + ",";
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}
	
	
	public static void saveBook(String bookCategory, Document doc) throws Exception {
		
		PrintWriter writer;
		
		try {
			writer = new PrintWriter("goodreads/books/book"+bookCounter+".txt","UTF-8");
			bookCounter++;
			String bookURL = doc.baseUri();//	driver.getCurrentUrl();
			String bookTitle = doc.getElementById("bookTitle").text() ;//findElement(By.id("bookTitle")).getText();
			String bookAuthor = doc.getElementsByAttributeValue("class", "authorName").get(0).text();//driver.findElement(By.className("authorName")).getText();
			String bookRating = doc.getElementsByAttributeValue("class", "average").get(0).text();	//driver.findElement(By.className("average")).getText();
			Element descriptionElement;
			String bookDescription="";
			if(doc.getElementsByAttributeValue("id","description").size()!=0)	{
				descriptionElement = doc.getElementById("description");
				if(descriptionElement.getElementsContainingText("...more").size()!=0) {
					bookDescription = descriptionElement.getElementsByTag("span").get(1).text();
				}
				else {
					bookDescription = descriptionElement.getElementsByTag("span").get(0).text();
				}
			}
			Elements detailsElement = doc.getElementsByAttributeValue("class", "row");
			String bookDetails ="";
			if(detailsElement.size()>=2)
				bookDetails = detailsElement.get(0).text() +" "+ detailsElement.get(1).text();
			
			Elements moreDetailElements = doc.getElementById("bookDataBox").getElementsByAttributeValue("class", "clearFloats");
			
			String moreDetails = "";
			for(Element element : moreDetailElements) {
				 moreDetails += (element.text() + " ");
			}
			moreDetails = moreDetails.replaceFirst("...less", "");
			
			StringBuffer output = new StringBuffer();
			output.append(bookURL + "\n");
			output.append(bookTitle + "\n");
			output.append(bookCategory+"\n");
			output.append(bookAuthor+"\n");
			output.append(bookDescription+"\n");
			output.append(bookDetails+"\n");
			output.append(bookRating+"\n");
			output.append(moreDetails+"\n");
			writer.write(output.toString());
			writer.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

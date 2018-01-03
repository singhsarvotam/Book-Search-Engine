import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
public class newArticle {
	public static void writeToFile(String body){
		try{
			if(!body.trim().isEmpty()){

				String path = "goodreads/bookurls.txt";
				
				File file = new File(path);
				file.getParentFile().mkdirs();
				PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
				writer.println(body);
				
				writer.close();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void writeNewFile(String name,String body){
		try{
			if(!body.trim().isEmpty()){

				String path = "goodreads/books/"+name;
				
				File file = new File(path);
				file.getParentFile().mkdirs();
				PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
				writer.println(body);
				
				writer.close();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
//				System.out.println(test.toDate(2457004));

	}
}

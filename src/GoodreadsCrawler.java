import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class GoodreadsCrawler {

	public static WebDriver driver;
	public static int bookCounter=10019;
	

	
	public static void main(String[] args) {
		
		System.setProperty("webdriver.chrome.driver", "D:/Backup/Selenium/chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("https://www.goodreads.com/genres/list");//
		driver.findElement(By.partialLinkText("Sign In")).click();
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		driver.findElement(By.id("user_email")).sendKeys("khan.saquib.a@gmail.com");
		driver.findElement(By.id("user_password")).sendKeys("wolf@123");
		driver.findElement(By.name("next")).click();
//		driver.get("https://www.goodreads.com/shelf/show/classics");
//		saveBooksInCategory("classics");
		
		//Page 1
		int bookGenresCount= driver.findElements(By.className("shelfStat")).size();
//		for(int counter=0; counter < bookGenresCount; counter++) {
//			WebElement element = driver.findElements(By.className("shelfStat")).get(counter).findElement(By.tagName("a"));
//			String category = element.getText();
//			driver.get("https://www.goodreads.com/shelf/show/"+category);
//			saveBooksInCategory(category);
//			driver.get("https://www.goodreads.com/genres/list");
//			System.out.println("GENRE "+ counter +"-"+category+" completed" );
//		}
		
		System.out.println("Page 2");
		//Page 2
		driver.findElement(By.className("next_page")).click();
		bookGenresCount = driver.findElements(By.className("shelfStat")).size();
		for(int counter=25; counter < bookGenresCount; counter++) {
			WebElement element = driver.findElements(By.className("shelfStat")).get(counter).findElement(By.tagName("a"));
			String category = element.getText();
			driver.get("https://www.goodreads.com/shelf/show/"+category);
			saveBooksInCategory(category);
			driver.get("https://www.goodreads.com/genres/list?page=2");
			System.out.println("GENRE "+ counter +"-"+category+" completed" );
		}

		System.out.println("Page 3");
		//Page 3
		driver.findElement(By.className("next_page")).click();
		bookGenresCount = driver.findElements(By.className("shelfStat")).size();
		for(int counter=0; counter < bookGenresCount; counter++) {
			WebElement element = driver.findElements(By.className("shelfStat")).get(counter).findElement(By.tagName("a"));
			String category = element.getText();
			driver.get("https://www.goodreads.com/shelf/show/"+category);
			saveBooksInCategory(category);
			driver.get("https://www.goodreads.com/genres/list?page=3");
			System.out.println("GENRE "+ counter +"-"+category+" completed" );
		}

		//close the chrome window
		driver.close();
	}
	
	
	
	public static void saveBooksInCategory(String category) {
		int booksOnPage = 0;
		int counter=0;
		PrintWriter writer;
		
		try {
			writer = new PrintWriter(new FileOutputStream("goodreads/bookURLS.txt",true));
		
		while(true) {
			booksOnPage = driver.findElements(By.className("bookTitle")).size();
			System.out.println(driver.getCurrentUrl());
			for(int countBook=0; countBook<booksOnPage;countBook++) {
				counter++;
				if(counter<149)
					continue;
				WebElement element = driver.findElements(By.className("bookTitle")).get(countBook);
				writer.append(element.getAttribute("href") + "\t" + category + "\n");
			}
			
			if(counter>299) {
				writer.flush();
				writer.close();
				return;
			}
			if(driver.findElements(By.className("next_page")).size()!=0) {
				if(driver.findElement(By.className("next_page")).getAttribute("class").contains("next_page disabled")) {
					writer.flush();
					writer.close();
					return;
				}
				else
					driver.findElement(By.className("next_page")).click();
			}
			else {
				writer.flush();
				writer.close();
				return;
			}
		}
		
		}catch (Exception e) {
			System.out.println("Failed at category " + category);
			e.printStackTrace();
		}
		
		
	}

}

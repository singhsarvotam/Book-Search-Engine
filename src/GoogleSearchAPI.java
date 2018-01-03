import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GoogleSearchAPI {
	public static ArrayList<String> googleSearch(String search) {
		ArrayList<String> list = new ArrayList<String>();

		String google = "https://www.google.com/search?q=";
		String charset = "UTF-8";
		search = search.replace(" ", "+");
		Document doc;
		try {
			doc = Jsoup.connect(google + URLEncoder.encode(search, charset) + "&num=" + String.valueOf(20))
					.userAgent("Mozilla").timeout(0).get();
			Elements urls = doc.select("div.g > h3.r > a");
			for (Element link : urls) {
				String title = link.text();
				String url = link.attr("href");
				url = url.substring(7,url.indexOf('&'));
				String data = url + " " + title;
				list.add(data);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;

	}

	public static void main(String[] args) {

		System.out.println(googleSearch("harry potter").toString());
	}

}

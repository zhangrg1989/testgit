package music_spider;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Spider {
	public static void main(String[] args){
		String url = "http://music.163.com/discover/playlist";
        try {
			Document doc = Jsoup
			        .connect(url)
			        .header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36 Edge/16.16299")
			        .header("Referer", "http://music.163.com/")
			        .header("Host", "music.163.com")
			        .timeout(1000).get()
			        ;
//			System.out.println(doc);
//			Elements elements = doc.select("div.tags");
//	        Elements elements2 = elements.select("i");
			Elements elements = doc.select("div.u-page");
//	        Elements elements2 = elements.select("title");
//	        Elements elements3 = elements.select("a");
	        System.out.println(elements);
	        
//	        for (Element el:elements){
////	        	System.out.println(el.attr("a"));
//	        	System.out.println(el.attr("href"));
//	        	System.out.println(el.text());
//	        }
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
}

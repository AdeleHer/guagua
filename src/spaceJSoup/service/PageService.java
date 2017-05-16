package spaceJSoup.service;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PageService {
	private static final String categoryUrl="http://space.thu.edu.tw/";
	public ArrayList<String> getCategoryUrl(){
		ArrayList<String> urlList=new ArrayList();
		Document doc=null;
		try {
			doc=Jsoup.connect(categoryUrl).get();
			Elements category=doc.select(".dropdown-menu").get(1).select("li a");
			for(int i=0;i<category.size();i++){
				Element c=category.get(i);
				urlList.add(c.attr("href"));
			}
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return urlList;
	}
	public ArrayList<String> getFieldUrl(Document doc){
		ArrayList<String> urlList=new ArrayList();
		Elements e=doc.select("h2 a");
		for(int i=0;i<e.size();i++){
			Element ee=e.get(i);
			urlList.add(ee.attr("href"));
		}
		return urlList;
	}
}	

package spaceJSoup.service;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DocumentService {
	public Map<String,String> analyze(Document doc){
		Map<String,String> rMap=new HashMap();
		rMap.put("title", doc.select(".title-line div h1").get(0).text());
		//rMap.put("description", doc.select(".description p").get(0).text().substring(5));
		String description = doc.select(".description p").get(0).text().substring(5);
		String next =  doc.select(".description p").get(1).text();
		if(next.startsWith("借用規定")){
			rMap.put("rentRule", doc.select(".description p").get(1).text().substring(5));
		}else{
			description = doc.select(".description p").get(1).text();
			rMap.put("rentRule", doc.select(".description p").get(2).text());
		}
		rMap.put("description",description);
		Elements trs=doc.select("table tr");
		rMap.put("capacity", trs.get(0).child(1).text());
		rMap.put("hasAirCondition",trs.get(1).child(1).text());
		rMap.put("hasETable", trs.get(2).child(1).text());
		rMap.put("hasProjector",trs.get(3).child(1).text());
		rMap.put("movable", trs.get(4).child(1).text());
		rMap.put("hasStair", trs.get(5).child(1).text());
		
		return rMap;
		//String title = doc.select(".title-line div h1").get(0).text();
		//String description = doc.select(".description p").get(0).text();
		//String rentRule = doc.select(".description p").get(1).text();    //????????	
		//String capacity = trs.get(0).child(1).text();
		//String hasAirCondition = trs.get(1).child(1).text();
		//String hasETable = trs.get(2).child(1).text();
		//String hasProjector = trs.get(3).child(1).text();
		//String movable = trs.get(4).child(1).text();
		//String hasStair = trs.get(5).child(1).text();
		
		//System.out.println(capacity+","+hasAirCondition+","+hasETable+","+hasProjector+","+movable+","+hasStair);
	}
}

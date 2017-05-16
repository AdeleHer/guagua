package spaceJSoup.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class MainService {
	private static final String url="http://space.thu.edu.tw";
	private static final String jsonUrl="./spaceJson.json";
	public static void main(String args[]){
		Map <String,Map<String,Map>> result=new HashMap();
		Document doc=null;
		try {
			//doc=Jsoup.connect(url).get();
			DocumentService ds=new DocumentService();
			//Map<String,String> pageMap=ds.analyze(doc);
			PageService ps=new PageService();
			ArrayList<String> cList= ps.getCategoryUrl();
			for(int i=0;i<cList.size();i++){
				String tempUrl=url+cList.get(i);
				String category = cList.get(i).substring(cList.get(i).lastIndexOf("/")+1, cList.get(i).length());
				doc=Jsoup.connect(tempUrl).get();
				ArrayList tempList=ps.getFieldUrl(doc);
				Map<String,Map> roomMap=new HashMap();
				for(int j=0;j<tempList.size();j++){
					String roomUrl=url+tempList.get(j);
					doc=Jsoup.connect(roomUrl).get();
					Map<String,String> pageMap=ds.analyze(doc);
					roomMap.put(pageMap.get("title"), pageMap);
				}
				result.put(category, roomMap);
			}		
			JSONObject json= new JSONObject(result);
			FileWriter fw=new FileWriter(jsonUrl);
			BufferedWriter bw=new BufferedWriter (fw);
			bw.write(json.toString());
			bw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

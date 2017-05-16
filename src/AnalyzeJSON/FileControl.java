package AnalyzeJSON;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class FileControl {
	public String getJSONString(String url){
		String json="";
		try {
			FileReader fr = new FileReader(url);
			BufferedReader br = new BufferedReader(fr);
			String str;
			while ((str = br.readLine()) != null) {
				json+=str;
			}
			br.close();
			fr.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("找不到檔案："+url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			return json;
		}
	}
	public void storeMissData(Set course,Set teacher){
		storeFile("./TheCourseWithoutRoom.txt",course);
		storeFile("./TheTeacherWithoutAccount.txt",teacher);
	}
	public void storeFile(String url,Set list){
		File f = new File(url);
		if(!f.exists()){
			try {
				f.createNewFile();
			} catch (IOException e) {
				System.out.println("建立檔案錯誤!");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(url));
			Iterator it=list.iterator();
			while(it.hasNext()){
				bw.write(it.next().toString());
				bw.newLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("buffer close error");
				e.printStackTrace();
			}
		}
	}
	
}

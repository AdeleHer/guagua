package AnalyzeJSON;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;


public class MainClass {
	private static String teacherUrl="./teacher.json";
	private static String courseUrl="./course.json";
	public static void main(String args[]){
		FileControl fc=new FileControl();
		String teacher=fc.getJSONString(teacherUrl);
		String course=fc.getJSONString(courseUrl);
		JSONControl json=new JSONControl();
		json.JSONToArray(teacher,course);
	}
}

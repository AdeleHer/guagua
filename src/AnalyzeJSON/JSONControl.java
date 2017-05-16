package AnalyzeJSON;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONControl {
	public static Set NoAccountTeacher=new HashSet();
	public static Set NoRoomCourse=new HashSet();
	ArrayList a=new ArrayList();
	public ArrayList JSONToArray(String teacher,String course){
			JSONArray t;
			try {
				t = new JSONArray(teacher);
				AnalyzeTeacher(t);
				t = new JSONArray(course);
				AnalyzeCourse(t);		
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				System.out.println("JSON格式錯誤!");
				e.printStackTrace();
			}
		return a;
	}
	private void AnalyzeCourse(JSONArray t){
		DBControl dbc=new DBControl();
		for(int i=0;i<t.length();i++){
			JSONObject jsonobject;
			try {
				CourseBean coursebean=new CourseBean();
				jsonobject = t.getJSONObject(i);
				coursebean.setName(jsonobject.getString("name"));
				coursebean.setCode(jsonobject.getString("code"));			
				JSONArray teacher=jsonobject.getJSONArray("teachers");
				ArrayList teachers=new ArrayList();
				for(int j=0;j<teacher.length();j++){
					teachers.add(teacher.getString(j));
				}
				coursebean.setTeachers(teachers);
				ArrayList<ScheduleBean> sArray=new ArrayList();
				JSONArray schedules=jsonobject.getJSONArray("schedules");
				boolean isfindRoom=true;
				for(int j=0;j<schedules.length();j++){
					JSONArray detail=schedules.getJSONArray(j);
					if(detail.getString(2).equals("")||detail.getString(2).length()==0){
						isfindRoom=false;
					}else{
						ScheduleBean schedule=new ScheduleBean();
						schedule.setWeek(detail.getString(0));
						schedule.setLesson(detail.getString(1));
						schedule.setRoom(detail.getString(2));
						sArray.add(schedule);
					}
				}
				if(!isfindRoom){
					NoRoomCourse.add(t.getJSONObject(i));
				}
				coursebean.setSchedules(sArray);
				if(dbc.insertCourse(coursebean)){
				}else{
					
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		FileControl f=new FileControl();
		f.storeMissData(NoRoomCourse,NoAccountTeacher);
		try {
			dbc.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("資料庫關閉異常!");
			e.printStackTrace();
		}
		System.out.println("course 建立完畢!");
	}
	private void AnalyzeTeacher(JSONArray t){
		DBControl dbc=new DBControl();
		for(int i=0;i<t.length();i++){
			JSONObject jsonobject;
			try {
				jsonobject = t.getJSONObject(i);
				String account=jsonobject.getString("account");
				if(account.equals("")||account.length()==0){
				}else{
					TeacherBean bean=new TeacherBean(account,jsonobject.getString("group"),jsonobject.getString("name"));
					if(dbc.insertTeacher(bean)){
						
					}else{
						System.out.println("新增失敗!");
					}
				}
			}catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		try {
			dbc.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("資料庫關閉異常!");
			e.printStackTrace();
		}
		System.out.println("teacher 建立完畢!");
	}
}

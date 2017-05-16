package AnalyzeJSON;

import java.util.ArrayList;

public class CourseBean {
	private int sn;
	private ArrayList<ScheduleBean> schedules;
	private ArrayList teachers;
	private String code;
	private String name;
	public int getSn() {
		return sn;
	}
	public void setSn(int sn) {
		this.sn = sn;
	}
	public ArrayList<ScheduleBean> getSchedules() {
		return schedules;
	}
	public void setSchedules(ArrayList<ScheduleBean> schedules) {
		this.schedules = schedules;
	}
	public ArrayList getTeachers() {
		return teachers;
	}
	public void setTeachers(ArrayList teachers) {
		this.teachers = teachers;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}

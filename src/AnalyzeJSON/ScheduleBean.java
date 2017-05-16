package AnalyzeJSON;

public class ScheduleBean {
	private int week;
	private String lesson;
	public int getWeek() {
		return week;
	}
	public void setWeek(String week) {
		if(week.equals("一")){
			this.week=1;
		}
		if(week.equals("二")){
			this.week=2;
		}
		if(week.equals("三")){
			this.week=3;
		}
		if(week.equals("四")){
			this.week=4;
		}
		if(week.equals("五")){
			this.week=5;
		}
		if(week.equals("六")){
			this.week=6;
		}
		if(week.equals("日")){
			this.week=7;
		}
	}
	public String getLesson() {
		return lesson;
	}
	public void setLesson(String lesson) {
		this.lesson = lesson;
	}
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	private String room;
}

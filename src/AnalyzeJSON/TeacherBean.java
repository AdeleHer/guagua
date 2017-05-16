package AnalyzeJSON;

public class TeacherBean {
	private int sn;
	private String account;
	private String password="admin";
	private String group;
	private String dept;
	private String name;
	TeacherBean (String account,String group,String name){
		this.account = account;
		this.group = group;
		this.name = name;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSn() {
		return sn;
	}
	public void setSn(int sn) {
		this.sn = sn;
	}
}

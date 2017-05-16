package AnalyzeJSON;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DBControl {
	private static String sql_url = "jdbc:mysql://localhost:3306/guagua?uicode=true&characterEncoding=UTF-8";
	private static String admin = "root";
	private static String pdw = "abcd1234";
	private String sql;
	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	private int result = 0;
	private static JSONControl jsonc = new JSONControl();

	DBControl() {
		try {
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			conn = DriverManager.getConnection(sql_url, admin, pdw);
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("資料庫連接失敗!");
			e.printStackTrace();
		}
	}

	public int getTeacherSn(String account) {
		sql = "select sn from tb_user  where account='" + account + "'";
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			conn.commit();
			if (rs.next()) {
				return rs.getInt("sn");
			} else {
				return 0;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				System.out.println("rollback Error");
				e1.printStackTrace();
			}
			e.printStackTrace();
			return -1;
		} finally {
			try {
				conn.close();
				stmt.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("colse Error");
				e.printStackTrace();
			}
		}
	}

	public boolean insertTeacher(TeacherBean bean) {
		sql = "select sn from tb_user where account='" + bean.getAccount()
				+ "'";
		try {
			int userid = 0;
			int groupid = 0;
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				userid = rs.getInt("sn");
			} else {
				sql = "insert into tb_user(account,password)values('"
						+ bean.getAccount() + "','" + bean.getPassword() + "')";
				stmt = conn.createStatement();
				result = stmt.executeUpdate(sql);
				if (result > 0) {
					sql = "select sn from tb_user where account='"
							+ bean.getAccount() + "'";
					stmt = conn.createStatement();
					rs = stmt.executeQuery(sql);
					if (rs.next()) {
						userid = rs.getInt("sn");
					}
					sql = "select sn from tb_position where name = ?";
					PreparedStatement pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, bean.getGroup());
					rs = pstmt.executeQuery();
					if (rs.next()) {
						groupid = rs.getInt("sn");
					} else {
						sql = "insert into tb_position(name) values(?)";
						pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, bean.getGroup());
						result = pstmt.executeUpdate();
						sql = "select sn from tb_position where name = ?";
						pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, bean.getGroup());
						rs = pstmt.executeQuery();
						if (rs.next()) {
							groupid = rs.getInt("sn");
						}
					}
					sql = "insert into tb_user_profile(sn,name,position) values(?,?,?)";
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, userid);
					pstmt.setString(2, bean.getName());
					pstmt.setString(3, groupid + "");
					result = pstmt.executeUpdate();
					if (result > 0) {
						conn.commit();
						return true;
					}
				}
			}
			if (userid > 0) {
				sql = "select sn from tb_position where name = ?";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, bean.getGroup());
				rs = pstmt.executeQuery();
				if (rs.next()) {
					groupid = rs.getInt("sn");
				} else {
					sql = "insert into tb_position(name) values(?)";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, bean.getGroup());
					result = pstmt.executeUpdate();
					sql = "select sn from tb_position where name = ?";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, bean.getGroup());
					rs = pstmt.executeQuery();
					if (rs.next()) {
						groupid = rs.getInt("sn");
					}
				}
				sql = "select position from tb_user_profile where sn=" + userid;
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				String position = "";
				if (rs.next()) {
					position = rs.getString("position");
				}
				position += "," + groupid;
				sql = "update tb_user_profile set position='" + position
						+ "' where sn=" + userid;
				stmt = conn.createStatement();
				result = stmt.executeUpdate(sql);
				if (result > 0) {
					conn.commit();
					return true;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				System.out.println("rollback Error");
				e1.printStackTrace();
			}
			e.printStackTrace();
			return false;
		}/*
		 * finally { try { conn.close(); stmt.close(); rs.close(); } catch
		 * (SQLException e) { // TODO Auto-generated catch block
		 * System.out.println("colse Error"); e.printStackTrace(); } }
		 */
		return false;
	}

	public boolean insertCourse(CourseBean bean) {
		try {
			ArrayList teacherSn = new ArrayList();
			for (int i = 0; i < bean.getTeachers().size(); i++) {
				sql = "select sn from tb_user_profile where name=?";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, (String) bean.getTeachers().get(i));
				rs = pstmt.executeQuery();
				if (rs.next()) {
					int teacherid = rs.getInt("sn");
					teacherSn.add(teacherid);
				} else {
					jsonc.NoAccountTeacher.add(bean.getTeachers().get(i));
				}
			}
			sql = "insert into tb_course (courseid,name)values('"
					+ bean.getCode() + "','" + bean.getName() + "')";
			stmt = conn.createStatement();
			result = stmt.executeUpdate(sql);
			if (result > 0) {
				int courseid = 0;
				sql = "select sn from tb_course where courseid='"
						+ bean.getCode() + "'";
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				if (rs.next()) {
					courseid = rs.getInt("sn");
					for (int i = 0; i < teacherSn.size(); i++) {
						sql = "insert into rel_course_user (course_sn,user_sn) values ("
								+ courseid + "," + teacherSn.get(i) + ")";
						stmt = conn.createStatement();
						result = stmt.executeUpdate(sql);
					}
				}
				if (bean.getSchedules().size() <= 0
						|| bean.getSchedules().isEmpty()) {
					return false;
				}
				for (int i = 0; i < bean.getSchedules().size(); i++) {
					ScheduleBean sbean = bean.getSchedules().get(i);
					int roomid = 0;
					sql = "select sn from tb_room where name='"
							+ sbean.getRoom() + "'";
					stmt = conn.createStatement();
					rs = stmt.executeQuery(sql);
					if (!rs.next()) {
						sql = "insert into tb_room (name) values ('"
								+ sbean.getRoom() + "')";
						stmt = conn.createStatement();
						result = stmt.executeUpdate(sql);
						sql = "select sn from tb_room where name='"
								+ sbean.getRoom() + "'";
						stmt = conn.createStatement();
						rs = stmt.executeQuery(sql);
						if (rs.next()) {
							roomid = rs.getInt("sn");
						}
					} else {
						roomid = rs.getInt("sn");
					}
					sql = "insert into rel_course_room (course_sn,room_sn,days,section) values ("
							+ courseid
							+ ","
							+ roomid
							+ ","
							+ sbean.getWeek()
							+ "," + sbean.getLesson() + ")";
					stmt = conn.createStatement();
					result = stmt.executeUpdate(sql);
					if (result > 0) {
						conn.commit();
					}

				}
			} else {
				conn.rollback();
				System.out.println("insert tb_course error");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return true;
	}

	public void close() throws SQLException {
		conn.close();
		stmt.close();
		rs.close();
	}
}

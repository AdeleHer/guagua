package guagua;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ConnectSQL {
	private static String sql_url = "jdbc:mysql://localhost:3306/guagua?uicode=true&characterEncoding=UTF-8";
	private static String admin = "root";
	private static String pdw = "abcd1234";
	private String sql;
	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	private int result = 0;

	ConnectSQL() throws SQLException {
		DriverManager.registerDriver(new com.mysql.jdbc.Driver());
		conn = DriverManager.getConnection(sql_url, admin, pdw);
	}

	public void InsertBuilder(Map<String, String> building) throws SQLException {
		for (String jkeys : building.keySet()) {
			sql = "insert into tb_building(name,en)values('" + building.get(jkeys)
					+ "','" + jkeys + "')";
			stmt = conn.createStatement();
			result = stmt.executeUpdate(sql);
		}
	}

	public void InsertRoom(Map<String, Map<String,ArrayList>> room)
			throws SQLException {
		for (String build : room.keySet()) {
			sql = "select sn from tb_building  where en='" + build + "'";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			int id = 0;
			if (rs.next()) {
				id = rs.getInt("sn");
			}
			for(String floor:room.get(build).keySet()){
				sql="insert into tb_floor(building_sn,name)values("+id+",'"+floor+"')";
				stmt = conn.createStatement();
				result = stmt.executeUpdate(sql);
				sql="select sn from tb_floor where building_sn=? and name=?";
				PreparedStatement pstmt=conn.prepareStatement(sql);
				pstmt.setInt(1, id);
				pstmt.setString(2, floor);
				rs=pstmt.executeQuery();
				int floor_id=0;
				if(rs.next()){
					floor_id=rs.getInt("sn");
				}
				for(int i=0;i<room.get(build).get(floor).size();i++){
					sql="insert into tb_room(floor_sn,name)values("+floor_id+",'"+build+room.get(build).get(floor).get(i)+"')";
					stmt = conn.createStatement();
					result = stmt.executeUpdate(sql);
				}
			}
			/*String k = "z";
			int floor_id = -1;
			for (int i = 0; i < room.get(jkeys).size(); i++) {
				ArrayList t = room.get(jkeys).get(i);
				String temp=(String)t.get(0);
				if (!temp.equals(k)) {
					sql = "insert into building(building_id,name)values(" + id + ",'"
							+ t.get(0) + "')";
					stmt = conn.createStatement();
					result = stmt.executeUpdate(sql);
					sql = "select id from floor where building_id=? and name=?";
					PreparedStatement stmt = conn.prepareStatement(sql);
					stmt.setInt(1, id);
					stmt.setString(2, (String)t.get(0));
					rs = stmt.executeQuery();
					if (rs.next()) {
						floor_id = rs.getInt("id");
					}
					k = (String) t.get(0);
				}
				sql = "insert into room (building_id,name)values(" + floor_id + ",'"
						+ t.get(1) + "')";
				stmt = conn.createStatement();
				result = stmt.executeUpdate(sql);
			}*/
		}
		System.out.println("建立完畢!");
	}

	/*public void insertSQL(Map<String[], Map<String, ArrayList<String>>> dao)
			throws SQLException {
		int Building_id = 0, floor_id = 0;
		for (String[] i : dao.keySet()) {
			sql = "insert into building (name,en)values('" + i[0] + "','" + i[1]
					+ "')";
			stmt = conn.createStatement();
			result = stmt.executeUpdate(sql);
			sql = "select last_insert_ID();";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				Building_id = rs.getInt(1);
			}
			for (String fkey : dao.get(i).keySet()) {
				sql = "insert into floor (building_id,name)values (" + Building_id + ",'"
						+ fkey + "')";
				stmt = conn.createStatement();
				result = stmt.executeUpdate(sql);

				sql = "select last_insert_ID();";
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				if (rs.next()) {
					floor_id = rs.getInt(1);
				}

				ArrayList<String> room = dao.get(i).get(fkey);
				for (String roomkey : room) {
					sql = "insert into room (building_id,name)values(" + floor_id + ",'"
							+ roomkey + "')";
					stmt = conn.createStatement();
					result = stmt.executeUpdate(sql);
				}
			}
		}

	}*/

	public void close() throws SQLException {
		conn.close();
		stmt.close();
		rs.close();
	}
}

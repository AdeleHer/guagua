package guagua;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XLSXtoSQL_class {
	private static String xlsx_url = "D:/guagua/東海教室表_new.xlsx";
	private static Map<String, String> building = new HashMap();
	private static Map<String, Map<String, ArrayList>> room = new HashMap();

	public void readXLSX() throws IOException {
		File f = new File(xlsx_url);
		FileInputStream fis = new FileInputStream(f);
		XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
		XSSFSheet buildingSheet = myWorkBook.getSheetAt(0);
		XSSFSheet RoomSheet = myWorkBook.getSheetAt(1);
		Iterator<Row> rowIterator = buildingSheet.iterator();
		while (rowIterator.hasNext()) {
			Row row1 = rowIterator.next();
			if (row1.getRowNum() == 0)
				continue;
			building.put(row1.getCell(1).toString(), row1.getCell(0).toString());
		}
		rowIterator = RoomSheet.iterator();
		String k = "z";
		String last_row = "L";
		ArrayList t = new ArrayList();
		Map<String, ArrayList> t2 = new HashMap();
		while (rowIterator.hasNext()) {
			Row row2 = rowIterator.next();
			if (row2.getRowNum() == 0)
				continue;
			if (!row2.getCell(0).toString().equals(last_row)) {
				room.put(last_row, t2);
				last_row = row2.getCell(0).toString();
				t2 = new HashMap();
			}
			double rr = row2.getCell(1).getNumericCellValue();
			int ri = (int) rr;
			String rs = "" + ri;
			if (rs.length() < 2) {
				rs = "00" + rs;
			} else if (rs.length() < 3) {
				rs = "0" + rs;
			}
			String floor = rs.substring(0, 1);
			if (floor.equals("0"))
				floor = "b1";
			if (t2.containsKey(floor)) {
				t.add(rs);
				t2.put(floor, t);
			} else {
				t = new ArrayList();
				t.add(rs);
				t2.put(floor, t);
			}
		}
		room.put(last_row, t2);
		fis.close();
	}

	public static void main(String args[]) throws IOException, SQLException {
		XLSXtoSQL_class x = new XLSXtoSQL_class();
		x.readXLSX();
		ConnectSQL sql = new ConnectSQL(); 
		sql.InsertBuilder(building);
		sql.InsertRoom(room); 
		sql.close();
	}

}

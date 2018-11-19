package ices.sh.util.function;

import ices.sh.util.db.DB;

import java.sql.ResultSet;
import java.sql.SQLException;


public class KeyBuilder {

	public static String getKey(String prefix,int numberLength,String tableName,String columnName){
		String sql = "select "+columnName+" from "+tableName+" order by "+columnName;
		DB db = new DB();
		ResultSet rs = db.query(sql);
		int i = 0;
		try {
			while(rs.next()){
				String curNumber = rs.getString(columnName).substring(prefix.length(), prefix.length()+numberLength);
				if(Integer.parseInt(curNumber)==i){
					i++;
				}
				else{
					break;
				}
			}
			db.close();
			String retKeyNumber = String.valueOf(i);
			int zeroNumber = numberLength - retKeyNumber.length();
			for(int j=0;j<zeroNumber;j++){
				retKeyNumber = "0"+retKeyNumber;
			}
			return prefix+retKeyNumber;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			db.close();
			return null;
		}
	}
	
}
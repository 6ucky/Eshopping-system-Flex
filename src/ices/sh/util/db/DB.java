package ices.sh.util.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class DB {
	
	private String url="";
	private Connection conn;
	private Statement stmt = null;
	
	public DB(){
		try { 
			String classesPath  = this.getClass().getResource("/").getPath().toUpperCase();
			int endIndex = classesPath.indexOf("WEB-INF");
			String projectPath = classesPath.substring(1,endIndex).replaceAll("%20", " ");
			File f = new File(projectPath+"xml/DB.xml");
			SAXReader reader = new SAXReader(); 
			Document doc = reader.read(f); 
		
			Element root = doc.getRootElement();
			String className = root.elementText("className"); 
			
			url = root.elementText("url");
			String user = root.elementText("user");
			String password = root.elementText("password");
			
			
			Class.forName(className);
			conn = DriverManager.getConnection(url,user,password);
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public int insert(String sql) {
		try {
			System.out.println("执行数据库插入操作SQL:"+sql);
			return stmt.executeUpdate(sql);
		} catch (SQLException ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	public int delete(String sql) {
		int m = 0;
		try {
			System.out.println("执行数据库删除操作SQL:"+sql);
			m = stmt.executeUpdate(sql);
		} catch (SQLException ex) {
			ex.printStackTrace();

		}
		return m;
	}

	public ResultSet query(String sql) {
		try {
			System.out.println("执行数据库查询操作SQL:"+sql);
			ResultSet rs = stmt.executeQuery(sql);
			return rs;
		} catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public int update(String sql) {
		int m = 0;
		try {
			System.out.println("执行数据库更新操作SQL:"+sql);
			m = stmt.executeUpdate(sql);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return m;
	}
	public int create(String sql) {
		int m = 0;
		try {
			System.out.println("执行数据库创建操作SQL:"+sql);
			m = stmt.executeUpdate(sql);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return m;
	}
	public void close(){
		try {
			stmt.close();
			conn.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		
		
	}

}
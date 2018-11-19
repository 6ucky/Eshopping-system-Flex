import ices.sh.util.db.DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import flex.messaging.FlexContext;
import flex.messaging.io.amf.ASObject;

public class LoginObject {

	private DB db;

	public LoginObject() {
		db = new DB();
	}
	public List queryallbooklist() {
		String sql = "select * from davin.booklist order by id";
		List list = new ArrayList();
		ResultSet rs = db.query(sql);
		try {
			while (rs.next()) {
				ModelDetailBean modelDetailBean = new ModelDetailBean();
				modelDetailBean.setId(rs.getString("id"));
				modelDetailBean.setName(rs.getString("name"));
				modelDetailBean.setStatement(rs.getString("statement"));
				modelDetailBean.setSend(rs.getString("send"));
				list.add(modelDetailBean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			db.close();
			return list;
		}
		db.close();
		return list;
	}
	public String verifyLogin(String userName, String password) {
		String sql = "select * from davin.userinfo where username = '"
				+ userName + "' and password = '" + password + "'";
		ResultSet rs = db.query(sql);
		try {
			if (rs.next()) {
				HttpServletRequest request = FlexContext.getHttpRequest();
				HttpSession session = request.getSession();
				db.close();
				return "success";
			} else {
				db.close();
				return "wrongInput";
			}
		} catch (SQLException e) {
			db.close();
			e.printStackTrace();
			return "failed";
		}
	}
	public void insertLogin(Object obj) {
		ASObject asObject = (ASObject) obj;
		HashMap<String, Object> mp = (HashMap<String, Object>) asObject;
		String sql = "insert into davin.userinfo (username,password) values ";
		sql += "('" + (String) mp.get("username") + "',";
		sql += "'" + (String) mp.get("password") + "')";
		db.insert(sql);
		String sql2 = "CREATE TABLE davin.client" + (String) mp.get("username") + "(username character varying(10),namel character varying(20) primary key,charge numeric(12,2),time character varying(20),classify character varying(20),statement character varying(100))";
		db.create(sql2);
		db.close();
	}
	public List queryLogin() {
		String sql = "select * from davin.userinfo order by username";
		List list = new ArrayList();
		ResultSet rs = db.query(sql);
		try {
			while (rs.next()) {
				ModelDetailBean7 modelDetailBean7 = new ModelDetailBean7();
				modelDetailBean7.setUsername(rs.getString("username"));
				modelDetailBean7.setPassword(rs.getString("password"));
				list.add(modelDetailBean7);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			db.close();
			return list;
		}
		db.close();
		return list;
	}
	public void deleteLogin(Object[] deleteList) {
		String sql = "delete from davin.userinfo where username in (";
		for (Object obj : deleteList) {
			ASObject asObject = (ASObject) obj;
			HashMap<String, Object> mp = (HashMap<String, Object>) asObject;
			String s = (String) mp.get("username");
			sql += "'" + s + "',";
		}
		if (sql.endsWith(",")) {
			sql = sql.substring(0, sql.length() - 1) + ")";
			db.delete(sql);
		}
		db.close();
	}
	public void sendproduct(Object[] deleteList) {
		String sql = "update davin.booklist set read = 'false',send = 'ÒÑ·¢»õ' where id in (";
		for (Object obj : deleteList) {
			ASObject asObject = (ASObject) obj;
			HashMap<String, Object> mp = (HashMap<String, Object>) asObject;
			String s = (String) mp.get("id");
			sql += "'" + s + "',";
		}
		if (sql.endsWith(",")) {
			sql = sql.substring(0, sql.length() - 1) + ")";
			db.update(sql);
		}
		db.close();
	}
}
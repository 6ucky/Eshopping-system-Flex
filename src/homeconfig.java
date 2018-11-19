import ices.sh.mbs.bean.Location;
import ices.sh.util.db.DB;
import ices.sh.util.function.KeyBuilder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.Object;
import java.util.Date;
import java.text.DateFormat;

import flex.messaging.io.amf.ASObject;
public class homeconfig {
	private DB db;
	static String k;
	static String n;
	static double m;
	static String boughtnum123,name123;
	public homeconfig() {
		db = new DB();
	}
	public void insertbooklist()
	{
		Date now = new Date();
		DateFormat d1 = DateFormat.getDateInstance();
		DateFormat d2 = DateFormat.getDateTimeInstance();
			String sql1 = "delete from davin.client"+ k +" where namel in (";
			String sql2 = "insert into davin.booklist (ID,statement,read,name,send) values ";
			String sql3 = "update davin.product set boughtnum = boughtnum +'" + boughtnum123 + "',num = num - '" + boughtnum123 + "' where namel = '" + name123 + "'";
			db.update(sql3);
			System.out.println(sql1);
				sql1 += "'" + name123 + "')";
				sql2 += "('" + d2.format(now) + "',";
				sql2 += "'" + boughtnum123 + " 个 " + name123 + "',";
				sql2 += "'true',";
				sql2 += "'" + k + "',";
				sql2 += "'未发货')";
				db.insert(sql2);
				db.delete(sql1);
			db.close();
	}
	public List<String> boughtqualify(Object[] bought)
	{
		List list = new ArrayList();
		m=0;
		int z=0;
		for(Object obj : bought)
		{
			ASObject asObject = (ASObject) obj;
			HashMap<String, Object> mp = (HashMap<String, Object>) asObject;
			double i = Double.parseDouble((String)mp.get("boughtnum"));
			double j = Double.parseDouble((String)mp.get("charge"));
			m = m + i * j;
			name123=(String) mp.get("namel");
			boughtnum123=(String) mp.get("boughtnum");
			ModelDetailBean modelDetailBean = new ModelDetailBean();
			modelDetailBean.setNamel(name123);
			modelDetailBean.setBoughtnum(boughtnum123);
			list.add(modelDetailBean);
		}
		return list;
	}
	public String getsummoney()
	{
		return String.valueOf(m);
	}
	public String username(String namel)
	{
		if(namel.contains("0"))
		{
			return k;
		}
		else
		{
			k=namel;
			return "0";
		}
	}
	public void productname(Object[] List)
	{
		for (Object obj : List)
		{
		ASObject asObject = (ASObject) obj;
		HashMap<String, Object> mp = (HashMap<String, Object>) asObject;
		n = (String) mp.get("namel");
		}
	}
	public String productnameout()
	{
		return n;
	}
	public String queryproductinfo(String name)
	{
		String sql = "select statement from davin.product where namel ~* '"+ name +"'" ;
		ResultSet rs = db.query(sql);
		try {
			if (rs.next()) {
				name = rs.getString("statement");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			db.close();
		}
		return name;
	}
	@SuppressWarnings("unchecked")
	public void deleteuserproduct(Object[] deleteList)
	{
		String sql = "delete from davin.client"+ k +" where namel in (";
		for (Object obj : deleteList) {
			ASObject asObject = (ASObject) obj;
			HashMap<String, Object> mp = (HashMap<String, Object>) asObject;
			String s = (String) mp.get("namel");
			sql += "'" + s + "',";
		}
		if (sql.endsWith(",")) {
			sql = sql.substring(0, sql.length() - 1) + ")";
			db.delete(sql);
		}
		db.close();
	}
	public void insertuserproduct(Object[] obj1) {
		for (Object obj : obj1) 
		{
			ASObject asObject = (ASObject) obj;
			HashMap<String, Object> mp = (HashMap<String, Object>) asObject;
		String sql = "insert into davin.client"+ k +" (username,namel,charge,time,classify,statement) values ";
		sql += "('" + k + "',";
		sql += "'" + (String) mp.get("namel") + "',";
		sql += "'" + (String) mp.get("charge") + "',";
		sql += "'" + (String) mp.get("time") + "',";
		sql += "'" + (String) mp.get("classify") + "',";
		sql += "'" + (String) mp.get("statement") + "')";
		db.insert(sql);
		}
		db.close();
	}
	public void insertnewuserproduct(String name) {
		String sql1 = "select charge,time,classify,statement from davin.product where namel ~* '"+ name +"'";
		ResultSet rs = db.query(sql1);
		try
		{
			while (rs.next()) {
			String sql2 = "insert into davin.client"+ k +"(username,namel,charge,time,classify,statement) values";
			sql2 += "('" + k + "','"+ name +"',";
			sql2 += "'"+(rs.getString("charge"))+"',";
			sql2 += "'"+(rs.getString("time"))+"',";
			sql2 += "'"+(rs.getString("classify"))+"',";
			sql2 += "'"+(rs.getString("statement"))+"')";
			db.insert(sql2);
			}
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		db.close();
		}
	}
	public List queryuserproduct() {
		String sql = "select namel,charge,time,classify,statement from davin.client"+ k +" order by namel";
		List list = new ArrayList();
		ResultSet rs = db.query(sql);
		try {
			while (rs.next()) {
				ModelDetailBean modelDetailBean = new ModelDetailBean();
				modelDetailBean.setNamel(rs.getString("namel"));
				modelDetailBean.setCharge(rs.getString("charge"));
				modelDetailBean.setTime(rs.getString("time"));
				modelDetailBean.setClassify(rs.getString("classify"));
				modelDetailBean.setStatement(rs.getString("statement"));
				modelDetailBean.setBoughtnum("1");
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
	public List querybooklist() {
		String sql2 = "update davin.booklist set read = 'true' where name ~* '"+ k +"'";
		db.update(sql2);
		String sql = "select * from davin.booklist where name = '"+ k +"' order by id";
		List list = new ArrayList();
		ResultSet rs = db.query(sql);
		try {
			while (rs.next()) {
				ModelDetailBean modelDetailBean2 = new ModelDetailBean();
				modelDetailBean2.setId(rs.getString("id"));
				modelDetailBean2.setStatement(rs.getString("statement"));
				modelDetailBean2.setSend(rs.getString("send"));
				list.add(modelDetailBean2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			db.close();
			return list;
		}
		db.close();
		return list;
	}
	
	public List queryall() {
		String sql = "select * from davin.product where num != 0";
		List list = new ArrayList();
		ResultSet rs = db.query(sql);
		try {
			while (rs.next()) {
				ModelDetailBean modelDetailBean = new ModelDetailBean();
				modelDetailBean.setNamel(rs.getString("namel"));
				modelDetailBean.setCharge(rs.getString("charge"));
				modelDetailBean.setNum(rs.getString("num"));
				modelDetailBean.setTime(rs.getString("time"));
				modelDetailBean.setClassify(rs.getString("classify"));
				modelDetailBean.setStatement(rs.getString("statement"));
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
	public List querytravle() {
		String sql = "select * from davin.product where classify ~* '旅游度假'";
		List list = new ArrayList();
		ResultSet rs = db.query(sql);
		try {
			while (rs.next()) {
				ModelDetailBean modelDetailBean = new ModelDetailBean();
				modelDetailBean.setNamel(rs.getString("namel"));
				modelDetailBean.setCharge(rs.getString("charge"));
				modelDetailBean.setNum(rs.getString("num"));
				modelDetailBean.setTime(rs.getString("time"));
				modelDetailBean.setClassify(rs.getString("classify"));
				modelDetailBean.setStatement(rs.getString("statement"));
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
	public List querycomputer() {
		String sql = "select * from davin.product where classify ~* '电脑办公'";
		List list = new ArrayList();
		ResultSet rs = db.query(sql);
		try {
			while (rs.next()) {
				ModelDetailBean modelDetailBean = new ModelDetailBean();
				modelDetailBean.setNamel(rs.getString("namel"));
				modelDetailBean.setCharge(rs.getString("charge"));
				modelDetailBean.setNum(rs.getString("num"));
				modelDetailBean.setTime(rs.getString("time"));
				modelDetailBean.setClassify(rs.getString("classify"));
				modelDetailBean.setStatement(rs.getString("statement"));
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
	public List querycellphone() {
		String sql = "select * from davin.product where classify ~* '手机通讯'";
		List list = new ArrayList();
		ResultSet rs = db.query(sql);
		try {
			while (rs.next()) {
				ModelDetailBean modelDetailBean = new ModelDetailBean();
				modelDetailBean.setNamel(rs.getString("namel"));
				modelDetailBean.setCharge(rs.getString("charge"));
				modelDetailBean.setNum(rs.getString("num"));
				modelDetailBean.setTime(rs.getString("time"));
				modelDetailBean.setClassify(rs.getString("classify"));
				modelDetailBean.setStatement(rs.getString("statement"));
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
	public List querycar() {
		String sql = "select * from davin.product where classify ~* '汽车'";
		List list = new ArrayList();
		ResultSet rs = db.query(sql);
		try {
			while (rs.next()) {
				ModelDetailBean modelDetailBean = new ModelDetailBean();
				modelDetailBean.setNamel(rs.getString("namel"));
				modelDetailBean.setCharge(rs.getString("charge"));
				modelDetailBean.setNum(rs.getString("num"));
				modelDetailBean.setTime(rs.getString("time"));
				modelDetailBean.setClassify(rs.getString("classify"));
				modelDetailBean.setStatement(rs.getString("statement"));
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
	public List queryvideo() {
		String sql = "select * from davin.product where classify ~* '影像数码'";
		List list = new ArrayList();
		ResultSet rs = db.query(sql);
		try {
			while (rs.next()) {
				ModelDetailBean modelDetailBean = new ModelDetailBean();
				modelDetailBean.setNamel(rs.getString("namel"));
				modelDetailBean.setCharge(rs.getString("charge"));
				modelDetailBean.setNum(rs.getString("num"));
				modelDetailBean.setTime(rs.getString("time"));
				modelDetailBean.setClassify(rs.getString("classify"));
				modelDetailBean.setStatement(rs.getString("statement"));
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
	public List queryelectric() {
		String sql = "select * from davin.product where classify ~* '家用电器'";
		List list = new ArrayList();
		ResultSet rs = db.query(sql);
		try {
			while (rs.next()) {
				ModelDetailBean modelDetailBean = new ModelDetailBean();
				modelDetailBean.setNamel(rs.getString("namel"));
				modelDetailBean.setCharge(rs.getString("charge"));
				modelDetailBean.setNum(rs.getString("num"));
				modelDetailBean.setTime(rs.getString("time"));
				modelDetailBean.setClassify(rs.getString("classify"));
				modelDetailBean.setStatement(rs.getString("statement"));
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
	public List queryhomething() {
		String sql = "select * from davin.product where classify ~* '家居百货'";
		List list = new ArrayList();
		ResultSet rs = db.query(sql);
		try {
			while (rs.next()) {
				ModelDetailBean modelDetailBean = new ModelDetailBean();
				modelDetailBean.setNamel(rs.getString("namel"));
				modelDetailBean.setCharge(rs.getString("charge"));
				modelDetailBean.setNum(rs.getString("num"));
				modelDetailBean.setTime(rs.getString("time"));
				modelDetailBean.setClassify(rs.getString("classify"));
				modelDetailBean.setStatement(rs.getString("statement"));
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
	public List querybeauty() {
		String sql = "select * from davin.product where classify ~* '美容护理'";
		List list = new ArrayList();
		ResultSet rs = db.query(sql);
		try {
			while (rs.next()) {
				ModelDetailBean modelDetailBean = new ModelDetailBean();
				modelDetailBean.setNamel(rs.getString("namel"));
				modelDetailBean.setCharge(rs.getString("charge"));
				modelDetailBean.setNum(rs.getString("num"));
				modelDetailBean.setTime(rs.getString("time"));
				modelDetailBean.setClassify(rs.getString("classify"));
				modelDetailBean.setStatement(rs.getString("statement"));
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
	public List queryfashion() {
		String sql = "select * from davin.product where classify ~* '时尚配饰'";
		List list = new ArrayList();
		ResultSet rs = db.query(sql);
		try {
			while (rs.next()) {
				ModelDetailBean modelDetailBean = new ModelDetailBean();
				modelDetailBean.setNamel(rs.getString("namel"));
				modelDetailBean.setCharge(rs.getString("charge"));
				modelDetailBean.setNum(rs.getString("num"));
				modelDetailBean.setTime(rs.getString("time"));
				modelDetailBean.setClassify(rs.getString("classify"));
				modelDetailBean.setStatement(rs.getString("statement"));
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
	public List queryexpensive() {
		String sql = "select * from davin.product where classify ~* '奢侈品'";
		List list = new ArrayList();
		ResultSet rs = db.query(sql);
		try {
			while (rs.next()) {
				ModelDetailBean modelDetailBean = new ModelDetailBean();
				modelDetailBean.setNamel(rs.getString("namel"));
				modelDetailBean.setCharge(rs.getString("charge"));
				modelDetailBean.setNum(rs.getString("num"));
				modelDetailBean.setTime(rs.getString("time"));
				modelDetailBean.setClassify(rs.getString("classify"));
				modelDetailBean.setStatement(rs.getString("statement"));
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
	public List queryinsurance() {
		String sql = "select * from davin.product where classify ~* '保险'";
		List list = new ArrayList();
		ResultSet rs = db.query(sql);
		try {
			while (rs.next()) {
				ModelDetailBean modelDetailBean = new ModelDetailBean();
				modelDetailBean.setNamel(rs.getString("namel"));
				modelDetailBean.setCharge(rs.getString("charge"));
				modelDetailBean.setNum(rs.getString("num"));
				modelDetailBean.setTime(rs.getString("time"));
				modelDetailBean.setClassify(rs.getString("classify"));
				modelDetailBean.setStatement(rs.getString("statement"));
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
	public List queryvip() {
		String sql = "select * from davin.product where classify ~* 'VIP商品'";
		List list = new ArrayList();
		ResultSet rs = db.query(sql);
		try {
			while (rs.next()) {
				ModelDetailBean modelDetailBean = new ModelDetailBean();
				modelDetailBean.setNamel(rs.getString("namel"));
				modelDetailBean.setCharge(rs.getString("charge"));
				modelDetailBean.setNum(rs.getString("num"));
				modelDetailBean.setTime(rs.getString("time"));
				modelDetailBean.setClassify(rs.getString("classify"));
				modelDetailBean.setStatement(rs.getString("statement"));
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
	public void deleteModel(Object[] deleteList) {
		String sql = "delete from davin.homebookobject where namel in (";
		for (Object obj : deleteList) {
			ASObject asObject = (ASObject) obj;
			HashMap<String, Object> mp = (HashMap<String, Object>) asObject;
			String s = (String) mp.get("namel");
			sql += "'" + s + "',";
		}
		if (sql.endsWith(",")) {
			sql = sql.substring(0, sql.length() - 1) + ")";
			db.delete(sql);
		}
		db.close();
	}
	public void insertModel(Object obj) {
		ASObject asObject = (ASObject) obj;
		HashMap<String, Object> mp = (HashMap<String, Object>) asObject;
		String sql = "insert into davin.homebookobject (namel,person,company,companytime,gettime,classify,place) values ";
		sql += "('" + (String) mp.get("namel") + "',";
		sql += "'" + (String) mp.get("person") + "',";
		sql += "'" + (String) mp.get("company") + "',";
		sql += "'" + (String) mp.get("companytime") + "',";
		sql += "'" + (String) mp.get("gettime") + "',";
		sql += "'" + (String) mp.get("classify") + "',";
		sql += "'" + (String) mp.get("place") + "')";
		db.insert(sql);
		db.close();
	}
	public List querywatercharge() {
		String sql = "select * from davin.watercharge order by people";
		List list = new ArrayList();
		ResultSet rs = db.query(sql);
		try {
			while (rs.next()) {
				ModelDetailBean1 modelDetailBean1 = new ModelDetailBean1();
				modelDetailBean1.setPeople(rs.getString("people"));
				modelDetailBean1.setDate(rs.getString("date"));
				modelDetailBean1.setMoney(rs.getString("money"));
				modelDetailBean1.setClassify(rs.getString("classify"));
				list.add(modelDetailBean1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			db.close();
			return list;
		}
		db.close();
		return list;
	}
	public void insertwaterModel(Object obj) {
		ASObject asObject = (ASObject) obj;
		HashMap<String, Object> mp = (HashMap<String, Object>) asObject;
		String sql = "insert into davin.watercharge (people,date,classify,money) values ";
		sql += "('" + (String) mp.get("people") + "',";
		sql += "'" + (String) mp.get("date") + "',";
		sql += "'" + (String) mp.get("classify") + "',";
		sql += "'" + (String) mp.get("money") + "')";
		db.insert(sql);
		db.close();
	}
	public void deletewaterModel(Object[] deleteList) {
		String sql = "delete from davin.watercharge where people in (";
		for (Object obj : deleteList) {
			ASObject asObject = (ASObject) obj;
			HashMap<String, Object> mp = (HashMap<String, Object>) asObject;
			String s = (String) mp.get("people");
			sql += "'" + s + "',";
		}
		if (sql.endsWith(",")) {
			sql = sql.substring(0, sql.length() - 1) + ")";
			db.delete(sql);
		}
		db.close();
	}
	public void insertgasModel(Object obj) {
		ASObject asObject = (ASObject) obj;
		HashMap<String, Object> mp = (HashMap<String, Object>) asObject;
		String sql = "insert into davin.gascharge (people,date,classify,money) values ";
		sql += "('" + (String) mp.get("people") + "',";
		sql += "'" + (String) mp.get("date") + "',";
		sql += "'" + (String) mp.get("classify") + "',";
		sql += "'" + (String) mp.get("money") + "')";
		db.insert(sql);
		db.close();
	}
	public void deletegasModel(Object[] deleteList) {
		String sql = "delete from davin.gascharge where people in (";
		for (Object obj : deleteList) {
			ASObject asObject = (ASObject) obj;
			HashMap<String, Object> mp = (HashMap<String, Object>) asObject;
			String s = (String) mp.get("people");
			sql += "'" + s + "',";
		}
		if (sql.endsWith(",")) {
			sql = sql.substring(0, sql.length() - 1) + ")";
			db.delete(sql);
		}
		db.close();
	}
	public List queryeleccharge() {
		String sql = "select * from davin.eleccharge order by people";
		List list = new ArrayList();
		ResultSet rs = db.query(sql);
		try {
			while (rs.next()) {
				ModelDetailBean3 modelDetailBean3 = new ModelDetailBean3();
				modelDetailBean3.setPeople(rs.getString("people"));
				modelDetailBean3.setDate(rs.getString("date"));
				modelDetailBean3.setMoney(rs.getString("money"));
				modelDetailBean3.setClassify(rs.getString("classify"));
				list.add(modelDetailBean3);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			db.close();
			return list;
		}
		db.close();
		return list;
	}
	public void insertelecModel(Object obj) {
		ASObject asObject = (ASObject) obj;
		HashMap<String, Object> mp = (HashMap<String, Object>) asObject;
		String sql = "insert into davin.eleccharge (people,date,classify,money) values ";
		sql += "('" + (String) mp.get("people") + "',";
		sql += "'" + (String) mp.get("date") + "',";
		sql += "'" + (String) mp.get("classify") + "',";
		sql += "'" + (String) mp.get("money") + "')";
		db.insert(sql);
		db.close();
	}
	public void deleteelecModel(Object[] deleteList) {
		String sql = "delete from davin.eleccharge where people in (";
		for (Object obj : deleteList) {
			ASObject asObject = (ASObject) obj;
			HashMap<String, Object> mp = (HashMap<String, Object>) asObject;
			String s = (String) mp.get("people");
			sql += "'" + s + "',";
		}
		if (sql.endsWith(",")) {
			sql = sql.substring(0, sql.length() - 1) + ")";
			db.delete(sql);
		}
		db.close();
	}
	public List querytvcharge() {
		String sql = "select * from davin.tvcharge order by people";
		List list = new ArrayList();
		ResultSet rs = db.query(sql);
		try {
			while (rs.next()) {
				ModelDetailBean4 modelDetailBean4 = new ModelDetailBean4();
				modelDetailBean4.setPeople(rs.getString("people"));
				modelDetailBean4.setDate(rs.getString("date"));
				modelDetailBean4.setMoney(rs.getString("money"));
				modelDetailBean4.setClassify(rs.getString("classify"));
				list.add(modelDetailBean4);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			db.close();
			return list;
		}
		db.close();
		return list;
	}
	public void inserttvModel(Object obj) {
		ASObject asObject = (ASObject) obj;
		HashMap<String, Object> mp = (HashMap<String, Object>) asObject;
		String sql = "insert into davin.tvcharge (people,date,classify,money) values ";
		sql += "('" + (String) mp.get("people") + "',";
		sql += "'" + (String) mp.get("date") + "',";
		sql += "'" + (String) mp.get("classify") + "',";
		sql += "'" + (String) mp.get("money") + "')";
		db.insert(sql);
		db.close();
	}
	public void deletetvModel(Object[] deleteList) {
		String sql = "delete from davin.tvcharge where people in (";
		for (Object obj : deleteList) {
			ASObject asObject = (ASObject) obj;
			HashMap<String, Object> mp = (HashMap<String, Object>) asObject;
			String s = (String) mp.get("people");
			sql += "'" + s + "',";
		}
		if (sql.endsWith(",")) {
			sql = sql.substring(0, sql.length() - 1) + ")";
			db.delete(sql);
		}
		db.close();
	}
	public List queryphonecharge() {
		String sql = "select * from davin.phonecharge order by people";
		List list = new ArrayList();
		ResultSet rs = db.query(sql);
		try {
			while (rs.next()) {
				ModelDetailBean5 modelDetailBean5 = new ModelDetailBean5();
				modelDetailBean5.setPeople(rs.getString("people"));
				modelDetailBean5.setPhone(rs.getString("phone"));
				modelDetailBean5.setDate(rs.getString("date"));
				modelDetailBean5.setMoney(rs.getString("money"));
				modelDetailBean5.setClassify(rs.getString("classify"));
				list.add(modelDetailBean5);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			db.close();
			return list;
		}
		db.close();
		return list;
	}
	public void insertphoneModel(Object obj) {
		ASObject asObject = (ASObject) obj;
		HashMap<String, Object> mp = (HashMap<String, Object>) asObject;
		String sql = "insert into davin.phonecharge (people,phone,date,classify,money) values ";
		sql += "('" + (String) mp.get("people") + "',";
		sql += "'" + (String) mp.get("phone") + "',";
		sql += "'" + (String) mp.get("date") + "',";
		sql += "'" + (String) mp.get("classify") + "',";
		sql += "'" + (String) mp.get("money") + "')";
		db.insert(sql);
		db.close();
	}
	public void deletephoneModel(Object[] deleteList) {
		String sql = "delete from davin.phonecharge where people in (";
		for (Object obj : deleteList) {
			ASObject asObject = (ASObject) obj;
			HashMap<String, Object> mp = (HashMap<String, Object>) asObject;
			String s = (String) mp.get("people");
			sql += "'" + s + "',";
		}
		if (sql.endsWith(",")) {
			sql = sql.substring(0, sql.length() - 1) + ")";
			db.delete(sql);
		}
		db.close();
	}
	public List querynetcharge() {
		String sql = "select * from davin.netcharge order by people";
		List list = new ArrayList();
		ResultSet rs = db.query(sql);
		try {
			while (rs.next()) {
				ModelDetailBean6 modelDetailBean6 = new ModelDetailBean6();
				modelDetailBean6.setPeople(rs.getString("people"));
				modelDetailBean6.setDate(rs.getString("date"));
				modelDetailBean6.setMoney(rs.getString("money"));
				modelDetailBean6.setClassify(rs.getString("classify"));
				list.add(modelDetailBean6);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			db.close();
			return list;
		}
		db.close();
		return list;
	}
	public void insertnetModel(Object obj) {
		ASObject asObject = (ASObject) obj;
		HashMap<String, Object> mp = (HashMap<String, Object>) asObject;
		String sql = "insert into davin.netcharge (people,date,classify,money) values ";
		sql += "('" + (String) mp.get("people") + "',";
		sql += "'" + (String) mp.get("date") + "',";
		sql += "'" + (String) mp.get("classify") + "',";
		sql += "'" + (String) mp.get("money") + "')";
		db.insert(sql);
		db.close();
	}
	public void deletenetModel(Object[] deleteList) {
		String sql = "delete from davin.netcharge where people in (";
		for (Object obj : deleteList) {
			ASObject asObject = (ASObject) obj;
			HashMap<String, Object> mp = (HashMap<String, Object>) asObject;
			String s = (String) mp.get("people");
			sql += "'" + s + "',";
		}
		if (sql.endsWith(",")) {
			sql = sql.substring(0, sql.length() - 1) + ")";
			db.delete(sql);
		}
		db.close();
	}
	public List querymoneycharge() {
		String sql = "select * from davin.moneycharge order by date";
		List list = new ArrayList();
		ResultSet rs = db.query(sql);
		try {
			while (rs.next()) {
				ModelDetailBean8 modelDetailBean8 = new ModelDetailBean8();
				modelDetailBean8.setDate(rs.getString("date"));
				modelDetailBean8.setContent(rs.getString("content"));
				modelDetailBean8.setIncome(rs.getString("income"));
				modelDetailBean8.setOutcome(rs.getString("outcome"));
				list.add(modelDetailBean8);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			db.close();
			return list;
		}
		db.close();
		return list;
	}
	public void insertmoneycharge(Object obj) {
		ASObject asObject = (ASObject) obj;
		HashMap<String, Object> mp = (HashMap<String, Object>) asObject;
		String sql = "insert into davin.moneycharge (date,content,income,outcome) values ";
		sql += "('" + (String) mp.get("date") + "',";
		sql += "'" + (String) mp.get("content") + "',";
		sql += "'" + (String) mp.get("income") + "',";
		sql += "'" + (String) mp.get("outcome") + "')";
		db.insert(sql);
		db.close();
		/*String sql2 = "select money from davin.overmoney order by money";
		ResultSet rs = db.query(sql2);
		try {
			if (rs.next()) {
				z = rs.getInt("money");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			db.close();
		}
		db.close();
		x = (Integer)mp2.get("income");
		y = (Integer)mp2.get("outcome");
		z = z + x - y;
		String sql3 = "update davin.overmoney set ";
		sql3 += "money='" + z + "'";
		db.update(sql3);
		//x = ((Number) mp.get("income")) - ((Number) mp.get("outcome"));*/
	}
	
	public String queryovermoney() {
		String sql2 = "select money from davin.overmoney order by money";
		ResultSet rs = db.query(sql2);
		String z = null;
		try {
			if (rs.next()) {
				z = rs.getString("money");
				System.out.println(z);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			db.close();
			return z;
		}
		db.close();
		return z;
	}
	public void updateovermoney(Number money,Number rest) {
			String sql = "update davin.overmoney set ";
				sql += "money='" + money + "' where money='" + rest + "'";
				db.update(sql);
	}
	public List querystocks() {
		String sql = "select * from davin.stocksconfig order by namel";
		List list = new ArrayList();
		ResultSet rs = db.query(sql);
		try {
			while (rs.next()) {
				ModelDetailBean9 modelDetailBean9 = new ModelDetailBean9();
				modelDetailBean9.setNode(rs.getString("node"));
				modelDetailBean9.setNamel(rs.getString("namel"));
				modelDetailBean9.setPosition(rs.getString("position"));
				modelDetailBean9.setCostprice(rs.getString("costprice"));
				modelDetailBean9.setCost(rs.getString("cost"));
				modelDetailBean9.setMarket(rs.getString("market"));
				list.add(modelDetailBean9);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			db.close();
			return list;
		}
		db.close();
		return list;
	}
	public Integer testnews() {
		String sql = "select read from davin.booklist where name ~* '"+ k +"' order by id";
		Boolean temp;
		int label=0;
		ResultSet rs = db.query(sql);
		try {
			while (rs.next()) {
				temp=rs.getBoolean("read");
				if(temp==false)
				{
					label=1;
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			db.close();
			return label;
		}
		db.close();
		return label;
	}
	public void insertproduct(Object obj) {
		ASObject asObject = (ASObject) obj;
		HashMap<String, Object> mp = (HashMap<String, Object>) asObject;
		String sql = "insert into davin.product (namel,charge,num,time,classify,statement,sold) values ";
		sql += "('" + (String) mp.get("namel") + "',";
		sql += "'" + (String) mp.get("charge") + "',";
		sql += "'" + (String) mp.get("num") + "',";
		sql += "'" + (String) mp.get("time") + "',";
		sql += "'" + (String) mp.get("classify") + "',";
		sql += "'" + (String) mp.get("statement") + "',";
		sql += "'0')";
		db.insert(sql);
		db.close();
	}
	public void insertoldstocks(Number f,Number g,Number e,String temp) {
		String sql = "update davin.stocksconfig set ";
		sql += "position='" + f + "',costprice='" + g + "',cost='" + e + "' where namel='" + temp + "'";
		db.update(sql);
	}
	public void deletestocks(String temp) {
		String sql = "delete from davin.stocksconfig where namel in (";
		sql += "'" + temp + "')";
		db.delete(sql);
		db.close();
	}
	public void updateproductcharge(String name,String charge) {
		String sql = "update davin.product set ";
			sql += "charge='" + charge + "' where namel='" + name + "'";
			db.update(sql);
	}
	public void updateproductnum(String name,String num) {
		String sql = "update davin.product set ";
			sql += "num='" + num + "' where namel='" + name + "'";
			db.update(sql);
	}
	public List volumn() {
		String sql = "select namel from davin.product order by boughtnum desc";
		List list = new ArrayList();
		ResultSet rs = db.query(sql);
		try {
			while (rs.next()) {
						ModelDetailBean2 modelDetailBean = new ModelDetailBean2();
						modelDetailBean.setNamel(rs.getString("namel"));
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
}

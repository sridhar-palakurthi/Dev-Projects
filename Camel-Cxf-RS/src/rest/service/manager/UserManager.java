package rest.service.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rest.pojo.User;
import util.AppUtil;
import util.Database;

public class UserManager {

	private static UserManager _instance = null;

	public static UserManager getInstance() {
		if (_instance == null) {
			_instance = new UserManager();
		}
		return _instance;
	}

	public void creatUser(User userObj) throws Exception {
		String userInsQry = AppUtil.createInsertQuery(userColList(), "USER");
		Connection conn = null;
		PreparedStatement insertMetric = null;
		boolean isDuplicate;
		try {
			isDuplicate = isDuplicateUserId(userObj.getId());
			if (isDuplicate)
				throw new Exception("User exists with same ID,Pl choose any other");
			conn = Database.getConnection();
			conn.setAutoCommit(false);
			insertMetric = conn.prepareStatement(userInsQry);
			addUser(insertMetric, userObj);
			conn.commit();
		} catch (Exception ex) {
			if (conn != null)
				conn.rollback();
			throw ex;
		} finally {
			AppUtil.closePreparedStatement(insertMetric);
			AppUtil.closeConnection(conn);
		}
	}

	public void updateUser(User userObj) throws Exception {
		String userUpdQry = AppUtil.createUpdateQuery(userUpdateColList(), userWhereColList(), "USER");
		Connection conn = null;
		PreparedStatement userPstmt = null;
		try {
			conn = Database.getConnection();
			conn.setAutoCommit(false);
			userPstmt = conn.prepareStatement(userUpdQry);
			updateUsr(userPstmt, userObj);
			conn.commit();
		} catch (Exception ex) {
			if (conn != null)
				conn.rollback();
			if (ex.toString().contains("unique constraint"))
				throw new Exception("SSN is in use");
			else
				throw ex;
		} finally {
			AppUtil.closePreparedStatement(userPstmt);
			AppUtil.closeConnection(conn);
		}
	}

	private List<String> userUpdateColList() {
		// TODO Auto-generated method stub
		List<String> colList = new ArrayList<String>();
		colList.add("USER_NAME");
		colList.add("SSN");
		return colList;
	}

	private List<String> userWhereColList() {
		// TODO Auto-generated method stub
		List<String> colList = new ArrayList<String>();
		colList.add("USER_ID");
		return colList;
	}

	public void deleteUser(String userId) throws Exception {
		Connection conn = null;
		String updQryStr = "";
		updQryStr = "DELETE FROM  USER WHERE USER_ID ='"+userId+"'";
		try {
			conn = Database.getConnection();
			conn.setAutoCommit(false);
			AppUtil.executeUpdate(conn, updQryStr);
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			throw e;
		} finally {
			AppUtil.closeConnection(conn);
		}
	}

	public User findUser(String userId, String userName, String ssn) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Connection conn = null;
		ResultSet resultSet = null;
		User userObj = null;
		StringBuilder sql = new StringBuilder("SELECT USER_ID, USER_NAME, SSN FROM USER WHERE USER_ID =:userId");
		paramMap.put("userId", userId);
		try {
			conn = Database.getConnection();
			resultSet = AppUtil.executeQueryUsingNamedParameters(conn, sql.toString(), paramMap, 1);
			if (resultSet != null) {
				while (resultSet.next()) {
					userObj = new User();
					userObj.setId(resultSet.getString(1));
					userObj.setName(resultSet.getString(2));
					userObj.setSsn(resultSet.getString(3));
				}
			}
		} catch (SQLException ex) {
			throw ex;
		} finally {
			AppUtil.closeResultSet(resultSet);
			AppUtil.closeConnection(conn);
		}
		return userObj;
	}

	public List<User> listAllUsers() throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Connection conn = null;
		ResultSet resultSet = null;
		List<User> userList = new ArrayList<User>();
		StringBuilder sql = new StringBuilder("SELECT USER_ID, USER_NAME, SSN FROM USER");
		try {
			conn = Database.getConnection();
			resultSet = AppUtil.executeQueryUsingNamedParameters(conn, sql.toString(), paramMap, 1000);
			if (resultSet != null) {
				while (resultSet.next()) {
					User userObj = new User();
					userObj.setId(resultSet.getString(1));
					userObj.setName(resultSet.getString(2));
					userObj.setSsn(resultSet.getString(3));
					userList.add(userObj);
				}
			}
		} catch (SQLException ex) {
			throw ex;
		} finally {
			AppUtil.closeResultSet(resultSet);
			AppUtil.closeConnection(conn);
		}
		return userList;
	}

	public void addUser(PreparedStatement insertMetricPs, User userWrapper) throws SQLException {
		try {
			int paraColumnCount = 1;
			insertMetricPs.setObject(paraColumnCount++, userWrapper.getId());
			insertMetricPs.setObject(paraColumnCount++, userWrapper.getName());
			insertMetricPs.setObject(paraColumnCount++, userWrapper.getSsn());
			insertMetricPs.executeUpdate();
		} catch (SQLException e1) {
			throw e1;
		}
	}

	private void updateUsr(PreparedStatement userPstmt, User userObj) throws Exception {
		// TODO Auto-generated method stub
		try {
			int paraColumnCount = 1;
			userPstmt.setObject(paraColumnCount++, userObj.getName());
			userPstmt.setObject(paraColumnCount++, userObj.getSsn());
			userPstmt.setObject(paraColumnCount++, userObj.getId());
			userPstmt.executeUpdate();
		} catch (SQLException e1) {
			throw e1;
		}
	}

	private boolean isDuplicateUserId(String id) throws Exception {

		Map<String, Object> paramMap = new HashMap<String, Object>();
		Connection conn = null;
		ResultSet resultSet = null;
		boolean isDuplicateMetric = false;
		StringBuilder sql = new StringBuilder("SELECT 1 FROM USER WHERE USER_ID =:id ");
		paramMap.put("id", id);

		try {
			conn = Database.getConnection();
			resultSet = AppUtil.executeQueryUsingNamedParameters(conn, sql.toString(), paramMap, 1000);

			if (resultSet != null) {
				while (resultSet.next()) {
					isDuplicateMetric = true;
				}
			}
		} catch (SQLException ex) {
		} finally {
			AppUtil.closeResultSet(resultSet);
			AppUtil.closeConnection(conn);
		}
		return isDuplicateMetric;

	}

	public static List<String> userColList() {
		List<String> colList = new ArrayList<String>();
		colList.add("USER_ID");
		colList.add("USER_NAME");
		colList.add("SSN");
		return colList;
	}
}

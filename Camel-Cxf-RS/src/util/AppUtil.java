package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AppUtil {

	private static AppUtil _instance = null;

	public static AppUtil getInstance() {
		if (_instance == null) {
			_instance = new AppUtil();
		}
		return _instance;
	}

	public static String createInsertQuery(List<String> colNames, String tableName) {
		StringBuilder insertQuery = new StringBuilder("");

		StringBuilder columnNames = new StringBuilder("");
		StringBuilder questionMarks = new StringBuilder("");

		columnNames.append(" ( ");
		questionMarks.append(" ( ");
		int count = 0;
		for (String colName : colNames) {
			if (count != 0) {
				columnNames.append(",");
				questionMarks.append(",");
			}
			columnNames.append(colName);
			questionMarks.append("?");
			count++;

		}
		columnNames.append(" ) ");
		questionMarks.append(" ) ");

		insertQuery.append(" INSERT INTO ");
		insertQuery.append(tableName);
		insertQuery.append(columnNames);
		insertQuery.append(" VALUES ");
		insertQuery.append(questionMarks);

		return insertQuery.toString();
	}

	public static String createUpdateQuery(List<String> columnList, List<String> whereColumnList, String tableName) {
		String updateQry = null;
		StringBuilder builder = new StringBuilder();
		StringBuilder whereQry = new StringBuilder();
		int index = 0;

		for (String columnName : columnList) {

			if (index > 0) {
				builder.append(",");
			}

			builder.append(columnName + "=?");
			index++;
		}

		index = 0;
		for (String whereCol : whereColumnList) {
			if (index > 0) {
				whereQry.append(" AND ");
			}

			whereQry.append(whereCol + "=?");
			index++;
		}

		if (builder.length() > 0 && whereQry.length() > 0) {
			updateQry = "UPDATE " + tableName + " SET " + builder.toString() + " WHERE " + whereQry.toString();
		}

		return updateQry;
	}

	public static final ResultSet executeQueryUsingNamedParameters(Connection conn, String queryString, Map<String, Object> parameterMap, int batchSize) throws Exception {

		NamedParameterStatement stmt = null;
		try {

			List<String> removeListParams = new ArrayList<String>();
			Map<String, Object> newParameterMap = new HashMap<String, Object>();

			newParameterMap.putAll(parameterMap);

			// For List values we need to replace comma seperated values and
			// replace the query string.
			// Please note this will not work for Dates.
			for (Iterator itr = parameterMap.keySet().iterator(); itr.hasNext();) {
				String paramName = (String) itr.next();
				Object paramValue = parameterMap.get(paramName);
				if (paramValue instanceof List) {
					List listVals = (List) paramValue;
					StringBuffer sb = new StringBuffer();
					int idx = 0;
					for (Object obj : listVals) {
						idx++;
						String newParam = paramName + "__" + idx;
						sb.append(":" + newParam);
						sb.append(",");
						newParameterMap.put(newParam, obj);
					}
					queryString = replace(queryString, ":" + paramName, sb.substring(0, sb.length() - 1));

					removeListParams.add(paramName);
					// parameterMap.remove(paramName);
				}

			}

			for (String paramName : removeListParams) {
				newParameterMap.remove(paramName);
			}

			stmt = new NamedParameterStatement(conn, queryString);
			stmt.setFetchSize(batchSize);
			stmt.setFetchDirection(ResultSet.FETCH_FORWARD);

			for (Iterator itr = newParameterMap.keySet().iterator(); itr.hasNext();) {
				String paramName = (String) itr.next();
				Object paramValue = newParameterMap.get(paramName);
				stmt.setObject(paramName, paramValue);
			}
			return stmt.executeQuery();
		} catch (Exception e) {

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e1) {
					System.err.println("Error while closing the prepare statement");
					throw e1;
				}
			}

			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e3) {
					System.err.println(" Error while closing the connection ");
					throw e3;
				}
				conn = null;
			}
			throw new Exception(" Error while executing the query " + queryString + " , parameters are " + parameterMap + " " + e.getMessage(), e);
		}

	}

	public static int executeUpdate(Connection conn, String updstr) throws Exception {
		int retval = 0;
		try {
			Statement stmt = conn.createStatement();
			try {
				retval = stmt.executeUpdate(updstr);
			} catch (SQLException e) {
			} finally {
				try {
					stmt.close();
				} catch (SQLException e) {
				}
			}
		} catch (SQLException exc) {
			throw new Exception(exc);
		}
		return retval;
	}

	public static void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				//
			}
		}
	}

	public static void closePreparedStatement(PreparedStatement ps) {
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				//
			}
		}

	}

	public static void closeResultSet(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
			}
		}
	}

	/**
	 * Replaces all instances of oldString with newString in line.
	 * 
	 * @param line
	 *          the String to search to perform replacements on
	 * @param oldString
	 *          the String that should be replaced by newString
	 * @param newString
	 *          the String that will replace all instances of oldString
	 * 
	 * @return a String will all instances of oldString replaced by newString
	 */
	public static String replace(String line, String oldString, String newString) {
		String buf = "";
		int start = 0;
		int end = line.indexOf(oldString);
		int tokenSize = oldString.length();
		while (end != -1) {
			buf = buf + line.substring(start, end) + newString;
			start = end + tokenSize;
			end = line.indexOf(oldString, start);
		}
		buf = buf + line.substring(start);
		return buf;
	}

	/**
	 * Replaces all instances of oldString with newString in line. The count Integer is updated with number of
	 * replaces.
	 * 
	 * @param line
	 *          the String to search to perform replacements on
	 * @param oldString
	 *          the String that should be replaced by newString
	 * @param newString
	 *          the String that will replace all instances of oldString
	 * 
	 * @return a String will all instances of oldString replaced by newString
	 */
	public static final String replace(String line, String oldString, String newString, int[] count) {
		if (line == null) {
			return null;
		}
		int i = 0;
		if ((i = line.indexOf(oldString, i)) >= 0) {
			int counter = 0;
			counter++;
			char[] line2 = line.toCharArray();
			char[] newString2 = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j = i;
			while ((i = line.indexOf(oldString, i)) > 0) {
				counter++;
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
				j = i;
			}
			buf.append(line2, j, line2.length - j);
			count[0] = counter;
			return buf.toString();
		}
		return line;
	}
}

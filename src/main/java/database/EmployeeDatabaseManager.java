package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import server.Server;

public class EmployeeDatabaseManager implements DatabaseManager {
	private final EmployeeDatabaseManagerProxy employeeDatabaseManagerProxy;

	public EmployeeDatabaseManager() {
		employeeDatabaseManagerProxy = new EmployeeDatabaseManagerProxy();

	}

	class EmployeeDatabaseManagerProxy {

		boolean isEmployeeInSystemDatabase(String idNumber, String password) {
			String query = "SELECT COUNT(*) AS amount FROM CAR_STATION_EMPLOYEE WHERE ID_NUMBER = ? AND CAR_STATION_EMPLOYEE_PASSWORD =  ?";

			PreparedStatement myStatement;
			ResultSet queryResult = null;
			try {
				myStatement = Server.complexDatabaseManager.CONNECTION
						.prepareStatement(query);
				myStatement.setString(1, idNumber);
				myStatement.setString(2, password);

				queryResult = myStatement.executeQuery();

				while (queryResult.next()) {
					String resultString = queryResult.getString("amount");
					if (resultString.equals("1")) {
						return true;
					}
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}
		
		String[] fetchEmployee(String idNumber) {
			String query = "SELECT ID, CAR_STATION_EMPLOYEE_PASSWORD, TYPE, FIRST_NAME, LAST_NAME, PESEL, ID_NUMBER"
					+ " FROM CAR_STATION_EMPLOYEE WHERE ID_NUMBER = ?";

			PreparedStatement myStatement;
			ResultSet queryResult = null;
			try {
				myStatement = Server.complexDatabaseManager.CONNECTION
						.prepareStatement(query);
				myStatement.setString(1, idNumber);

				queryResult = myStatement.executeQuery();

				String id = null, employeePassword = null, firstName = null, lastName = null, type = null, pesel = null;
				while (queryResult.next()) {
					id = queryResult.getString("ID");
					employeePassword = queryResult
							.getString("CAR_STATION_EMPLOYEE_PASSWORD");
					type = queryResult
							.getString("TYPE");
					firstName = queryResult.getString("FIRST_NAME");
					lastName = queryResult.getString("LAST_NAME");
					pesel = queryResult.getString("PESEL");
				}
				return new String[] { id, employeePassword, type, firstName,
						lastName, pesel, idNumber};

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
	}

	public boolean isEmployeeInSystemDatabase(String idNumber, String password) {
		return employeeDatabaseManagerProxy.isEmployeeInSystemDatabase(
				idNumber, password);
	}
	
	public void addEmployeeOrder(String employeeID, String orderID) {
		String query = "SELECT add_employee_order(?,?);";
		
		PreparedStatement myStatement;
		ResultSet queryResult = null;
		try {
			myStatement = Server.complexDatabaseManager.CONNECTION.prepareStatement(query);
			myStatement.setInt(1, Integer.decode(employeeID));
			myStatement.setInt(2, Integer.decode(orderID));

			queryResult = myStatement.executeQuery();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public String[] fetchEmployee(String idNumber) {
		return employeeDatabaseManagerProxy.fetchEmployee(idNumber);
	}

}

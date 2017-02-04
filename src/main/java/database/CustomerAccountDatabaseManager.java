package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import server.Server;



public class CustomerAccountDatabaseManager implements DatabaseManager {

	private final CustomerAccountManagerProxy customerAccountManagerProxy;

	public CustomerAccountDatabaseManager() {
		customerAccountManagerProxy = new CustomerAccountManagerProxy();

	}

	class CustomerAccountManagerProxy {

		private boolean isCustomerInSystemDatabase(String idNumber,
				String password) {
			String query = "SELECT COUNT(*) AS amount FROM CUSTOMER WHERE ID_NUMBER = ? AND CUSTOMER_PASSWORD =  ?";

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

		private void changeAccountBalance(String customerID,
				String accountBalace) {
			String query = "SELECT update_customer_balance(?,?);";

			PreparedStatement myStatement;
			ResultSet queryResult = null;
			try {
				myStatement = Server.complexDatabaseManager.CONNECTION
						.prepareStatement(query);

				double doubleBalance = Double.parseDouble(accountBalace);

				myStatement.setInt(1, Integer.decode(customerID));
				myStatement.setInt(2, (int) doubleBalance);

				queryResult = myStatement.executeQuery();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private boolean isCustomerAlreadyRegistered(String idNumber) {
			String query = "SELECT COUNT(*) AS amount FROM CUSTOMER WHERE ID_NUMBER = ?";

			PreparedStatement myStatement;
			ResultSet queryResult = null;
			try {
				myStatement = Server.complexDatabaseManager.CONNECTION
						.prepareStatement(query);
				myStatement.setString(1, idNumber);

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

		String[] fetchCustomer(String idNumber) {
			String query = "SELECT ID, CUSTOMER_PASSWORD, FIRST_NAME, LAST_NAME, PESEL, ACCOUNT_BALLANCE"
					+ " FROM CUSTOMER WHERE ID_NUMBER = ?";

			PreparedStatement myStatement;
			ResultSet queryResult = null;
			try {
				myStatement = Server.complexDatabaseManager.CONNECTION
						.prepareStatement(query);
				myStatement.setString(1, idNumber);

				queryResult = myStatement.executeQuery();

				String id = null, customerPassword = null, firstName = null, lastName = null, pesel = null, accountBalance = null;
				while (queryResult.next()) {
					id = queryResult.getString("ID");
					customerPassword = queryResult
							.getString("CUSTOMER_PASSWORD");
					firstName = queryResult.getString("FIRST_NAME");
					lastName = queryResult.getString("LAST_NAME");
					pesel = queryResult.getString("PESEL");
					accountBalance = queryResult.getString("ACCOUNT_BALLANCE");
				}
				return new String[] { id, customerPassword, firstName,
						lastName, pesel, idNumber, accountBalance };

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		private boolean createCustomer(String idNumber, String pesel,
				String password) {
			String query = "SELECT create_customer(?,?,?);";

			PreparedStatement myStatement;
			ResultSet queryResult = null;
			try {
				myStatement = Server.complexDatabaseManager.CONNECTION
						.prepareStatement(query);
				myStatement.setString(1, idNumber);
				myStatement.setString(2, pesel);
				myStatement.setString(3, password);

				queryResult = myStatement.executeQuery();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}

		private void changeCustomerFirstName(String idNumber, String firstName) {
			String query = "SELECT change_customer_firstName(?, ?);";

			PreparedStatement myStatement;
			ResultSet queryResult = null;
			try {
				myStatement = Server.complexDatabaseManager.CONNECTION
						.prepareStatement(query);
				myStatement.setString(1, idNumber);
				myStatement.setString(2, firstName);

				queryResult = myStatement.executeQuery();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void changeCustomerLastName(String idNumber, String lastName) {
			String query = "SELECT change_customer_lastName(?, ?);";

			PreparedStatement myStatement;
			ResultSet queryResult = null;
			try {
				myStatement = Server.complexDatabaseManager.CONNECTION
						.prepareStatement(query);
				myStatement.setString(1, idNumber);
				myStatement.setString(2, lastName);

				queryResult = myStatement.executeQuery();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void changeCustomerPassword(String idNumber, String password) {
			String query = "SELECT change_customer_password(?, ?);";

			PreparedStatement myStatement;
			ResultSet queryResult = null;
			try {
				myStatement = Server.complexDatabaseManager.CONNECTION
						.prepareStatement(query);
				myStatement.setString(1, idNumber);
				myStatement.setString(2, password);

				queryResult = myStatement.executeQuery();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void updateCustomerAccountBalance(String customerID,
			String accountBalance) {
		customerAccountManagerProxy.changeAccountBalance(customerID,
				accountBalance);
	}

	public boolean isCustomerInSystemDatabase(String idNumber, String password) {
		return customerAccountManagerProxy.isCustomerInSystemDatabase(idNumber,
				password);
	}

	public void changeCustomerPassword(String idNumber, String password) {
		customerAccountManagerProxy.changeCustomerPassword(idNumber, password);
	}

	public void changeCustomerLastName(String idNumber, String lastName) {
		customerAccountManagerProxy.changeCustomerLastName(idNumber, lastName);
	}

	public void changeCustomerFirstName(String idNumber, String firstName) {
		customerAccountManagerProxy
				.changeCustomerFirstName(idNumber, firstName);
	}

	public String[] fetchCustomerFromDatabase(String idNumber) {
		return customerAccountManagerProxy.fetchCustomer(idNumber);
	}

	public boolean isCustomerAlreadyRegistered(String idNumber) {
		return customerAccountManagerProxy
				.isCustomerAlreadyRegistered(idNumber);
	}

	public boolean createCustomer(String idNumber, String pesel, String password) {
		return customerAccountManagerProxy.createCustomer(idNumber, pesel,
				password);
	}
}

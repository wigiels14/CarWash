package database;

import java.sql.Connection;
import java.sql.DriverManager;

public class ComplexDatabaseManager {
	private static ComplexDatabaseManager complexDatabaseManagerInstance;
	public CustomerAccountDatabaseManager customerAccountDatabaseManager;
	public VehicleDatabaseManager vehicleDatabaseManager;
	public EmployeeDatabaseManager employeeDatabaseManager;
	public ServiceDatabaseManager serviceDatabaseManager;
	public OrderDatabaseManager orderDatabaseManager;
	public DatabaseConnection databaseConnection;
	public final Connection CONNECTION;

	private class DatabaseConnection {
		private final String url = "jdbc:postgresql://localhost:5432/CarWash";
		private final String username = "postgres";
		private final String password = "admin";

		public Connection connectToDatabase() {
			Connection connection = null;
			try {
				Class.forName("org.postgresql.Driver");
				connection = DriverManager.getConnection(url, username,
						password);
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println(e.getClass().getName() + ": "
						+ e.getMessage());
				System.exit(0);
			}
			return connection;
		}
	}

	public static ComplexDatabaseManager getInstance() {
		if (complexDatabaseManagerInstance == null) {
			complexDatabaseManagerInstance = new ComplexDatabaseManager();
		}
		return complexDatabaseManagerInstance;
	}

	private ComplexDatabaseManager() {
		databaseConnection = new DatabaseConnection();
		CONNECTION = databaseConnection.connectToDatabase();
		customerAccountDatabaseManager = new CustomerAccountDatabaseManager();
		vehicleDatabaseManager = new VehicleDatabaseManager();
		employeeDatabaseManager = new EmployeeDatabaseManager();
		serviceDatabaseManager = new ServiceDatabaseManager();
		orderDatabaseManager = new OrderDatabaseManager();

	}

}

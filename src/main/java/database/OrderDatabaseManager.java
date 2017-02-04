package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import server.Server;

public class OrderDatabaseManager implements DatabaseManager {

	public void createOrder(String vehicleID) {
		String query = "SELECT create_order(?);";

		PreparedStatement myStatement;
		ResultSet queryResult = null;
		int result = Integer.decode(vehicleID);
		System.out.println(result);
		try {
			myStatement = Server.complexDatabaseManager.CONNECTION.prepareStatement(query);
			myStatement.setInt(1, result);

			queryResult = myStatement.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void changeOrderState(String orderID, String state) {
		String query = "SELECT change_order_status(?,?);";

		PreparedStatement myStatement;
		ResultSet queryResult = null;
		int result = Integer.decode(orderID);
		System.out.println(result);
		try {
			myStatement = Server.complexDatabaseManager.CONNECTION.prepareStatement(query);
			myStatement.setInt(1, result);
			myStatement.setString(2, state);

			queryResult = myStatement.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getOrderByVehicleID(String vehicleID) {
		String query = "SELECT ID FROM ORDERS WHERE VEHICLE_ID = ?";

		PreparedStatement myStatement;
		ResultSet queryResult = null;
		int result = Integer.decode(vehicleID);
		try {
			myStatement = Server.complexDatabaseManager.CONNECTION.prepareStatement(query);
			myStatement.setInt(1, result);

			queryResult = myStatement.executeQuery();

			String id = null;
			while (queryResult.next()) {
				id = queryResult.getString("ID");
			}
			return id;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public ArrayList<String[]> getOrdersByCustomerID(String customerID) {
		ArrayList<String[]> resArrayList = new ArrayList<String[]>();
		String query = "SELECT ID, VEHICLE_ID, STATE FROM ORDERS"
				+ " WHERE VEHICLE_ID IN (SELECT ID FROM VEHICLE WHERE CUSTOMER_ID = ?);";

		PreparedStatement myStatement;
		ResultSet queryResult = null;
		int result = Integer.decode(customerID);
		try {
			myStatement = Server.complexDatabaseManager.CONNECTION.prepareStatement(query);
			myStatement.setInt(1, result);

			queryResult = myStatement.executeQuery();

			String id = null, vehicleID = null, state = null;
			int iterator = 0;
			while (queryResult.next()) {
				id = queryResult.getString("ID");
				vehicleID = queryResult.getString("VEHICLE_ID");
				state = queryResult.getString("STATE");
				String[] resString = { id, vehicleID, state };
				resArrayList.add(resString);
			}
			return resArrayList;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	public String[] fetchOrdersByID(String orderID) {
		ArrayList<String[]> resArrayList = new ArrayList<String[]>();
		String query = "SELECT ID, VEHICLE_ID, STATE FROM ORDERS"
				+ " WHERE ID=?;";

		PreparedStatement myStatement;
		ResultSet queryResult = null;
		int result = Integer.decode(orderID);
		try {
			myStatement = Server.complexDatabaseManager.CONNECTION.prepareStatement(query);
			myStatement.setInt(1, result);

			queryResult = myStatement.executeQuery();

			String id = null, vehicleID = null, state = null;
			int iterator = 0;
			while (queryResult.next()) {
				id = queryResult.getString("ID");
				vehicleID = queryResult.getString("VEHICLE_ID");
				state = queryResult.getString("STATE");
				String[] resString = { id, vehicleID, state };
				return resString;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<String> fetchEmployeeOrder(String employeeID) {
		ArrayList<String> resArrayList = new ArrayList<String>();
		String query = "SELECT ORDERS_ID from ORDERS_CAR_STATION_EMPLOYEES WHERE CAR_STATION_EMPLOYEE_ID = ?";

		PreparedStatement myStatement;
		ResultSet queryResult = null;
		int result = Integer.decode(employeeID);
		try {
			myStatement = Server.complexDatabaseManager.CONNECTION.prepareStatement(query);
			myStatement.setInt(1, result);

			queryResult = myStatement.executeQuery();

			String id = null, vehicleID = null, state = null;
			int iterator = 0;
			while (queryResult.next()) {
				id = queryResult.getString("ORDERS_ID");
				resArrayList.add(id);
			}
			return resArrayList;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void createServiceOrders(String serviceID, String orderID) {
		String query = "SELECT create_service_orders(?,?)";

		PreparedStatement myStatement;
		ResultSet queryResult = null;
		try {
			myStatement = Server.complexDatabaseManager.CONNECTION.prepareStatement(query);
			myStatement.setInt(1, Integer.decode(serviceID));
			myStatement.setInt(2, Integer.decode(orderID));

			queryResult = myStatement.executeQuery();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<String[]> fetchServiceOrdersByCustomerID(String customerID) {
		ArrayList<String[]> resArrayList = new ArrayList<String[]>();
		String query = "SELECT SERVICE_ID, ORDER_ID FROM SERVICE_ORDERS " + "WHERE ORDER_ID IN(SELECT ID FROM ORDERS "
				+ " WHERE VEHICLE_ID IN (SELECT ID FROM VEHICLE WHERE CUSTOMER_ID = ?));";

		PreparedStatement myStatement;
		ResultSet queryResult = null;
		int result = Integer.decode(customerID);
		try {
			myStatement = Server.complexDatabaseManager.CONNECTION.prepareStatement(query);
			myStatement.setInt(1, result);

			queryResult = myStatement.executeQuery();

			String serviceID = null, orderID = null;
			int iterator = 0;
			while (queryResult.next()) {
				serviceID = queryResult.getString("SERVICE_ID");
				orderID = queryResult.getString("ORDER_ID");
				String[] resString = { serviceID, orderID };
				resArrayList.add(resString);
			}
			return resArrayList;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public ArrayList<String[]> fetchNotTakenOrders(String service) {
		ArrayList<String[]> resArrayList = new ArrayList<String[]>();
		String query = "SELECT ID, VEHICLE_ID, STATE FROM ORDERS" + " WHERE ID IN (SELECT ORDER_ID FROM SERVICE_ORDERS"
				+ " WHERE SERVICE_ID IN (SELECT ID FROM SERVICE WHERE service_type = ?)" + " EXCEPT"
				+ " SELECT ORDER_ID FROM SERVICE_ORDERS"
				+ " WHERE ORDER_ID IN (SELECT ORDERS_ID FROM ORDERS_CAR_STATION_EMPLOYEES));";

		PreparedStatement myStatement;
		ResultSet queryResult = null;
		try {
			myStatement = Server.complexDatabaseManager.CONNECTION.prepareStatement(query);
			myStatement.setString(1,service);

			queryResult = myStatement.executeQuery();

			String id = null, vehicleID = null, state = null;
			int iterator = 0;
			while (queryResult.next()) {
				id = queryResult.getString("ID");
				vehicleID = queryResult.getString("VEHICLE_ID");
				state = queryResult.getString("STATE");
				String[] resString = { id, vehicleID, state };
				resArrayList.add(resString);
			}
			return resArrayList;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}

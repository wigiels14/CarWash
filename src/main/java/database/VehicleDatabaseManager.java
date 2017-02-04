package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import server.Server;

public class VehicleDatabaseManager implements DatabaseManager {
	public ArrayList<String[]> fetchVehiclesByCustomerID(String customerID) {
		ArrayList<String[]> vehicles = new ArrayList<String[]>();
		String query = "SELECT ID, VIN, MARK, MODEL FROM VEHICLE WHERE CUSTOMER_ID = ?";

		PreparedStatement myStatement;
		ResultSet queryResult = null;
		try {
			myStatement = Server.complexDatabaseManager.CONNECTION
					.prepareStatement(query);
			myStatement.setInt(1, Integer.decode(customerID));

			queryResult = myStatement.executeQuery();

			String id = null, vin = null, mark = null, carModel = null;
			int iterator = 0;
			while (queryResult.next()) {
				id = queryResult.getString("ID");
				vin = queryResult.getString("VIN");
				mark = queryResult.getString("MARK");
				carModel = queryResult.getString("MODEL");
				String[] atributes = { id, vin, mark, carModel };
				vehicles.add(atributes);
			}
			return vehicles;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public boolean createVehicle(String vin, String mark, String model,
			String customerIDNumber) {
		String query = "SELECT create_vehicle(?,?,?,?);";

		PreparedStatement myStatement;
		ResultSet queryResult = null;
		try {
			myStatement = Server.complexDatabaseManager.CONNECTION
					.prepareStatement(query);
			myStatement.setString(1, vin);
			myStatement.setString(2, mark);
			myStatement.setString(3, model);
			myStatement.setInt(4, Integer.parseInt(customerIDNumber));

			queryResult = myStatement.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public String fetchVehicleIDByVIN(String vehicleVIN) {
		String query = "SELECT ID FROM VEHICLE WHERE VIN = ?";
		System.out.println("VINs: " + vehicleVIN);

		PreparedStatement myStatement;
		ResultSet queryResult = null;
		try {
			myStatement = Server.complexDatabaseManager.CONNECTION
					.prepareStatement(query);
			myStatement.setString(1, vehicleVIN);

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
	
	public String[] fetchVehicleByID(String vehicleID) {
		ArrayList<String[]> vehicles = new ArrayList<String[]>();
		String query = "SELECT ID, VIN, MARK, MODEL FROM VEHICLE WHERE ID = ?";

		PreparedStatement myStatement;
		ResultSet queryResult = null;
		try {
			myStatement = Server.complexDatabaseManager.CONNECTION
					.prepareStatement(query);
			myStatement.setInt(1, Integer.decode(vehicleID));

			queryResult = myStatement.executeQuery();

			String id = null, vin = null, mark = null, carModel = null;
			while (queryResult.next()) {
				id = queryResult.getString("ID");
				vin = queryResult.getString("VIN");
				mark = queryResult.getString("MARK");
				carModel = queryResult.getString("MODEL");
				return new String[] { id, vin, mark, carModel };
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}

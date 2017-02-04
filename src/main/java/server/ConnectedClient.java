package server;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.util.ArrayList;

import client.ClientQuery;

public class ConnectedClient extends Thread {
	private final Socket socket;
	private final ServerSocket serverSocket;
	private final Connection conn;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private DataOutputStream userNumberStream;

	public ConnectedClient(Connection conn, ServerSocket serverSocket, Socket socket) {
		this.conn = conn;
		this.serverSocket = serverSocket;
		this.socket = socket;
	}

	void initStreams() {
		OutputStream outPut;
		try {
			outPut = socket.getOutputStream();
			out = new ObjectOutputStream(outPut);

			BufferedInputStream inPut = new BufferedInputStream(socket.getInputStream());
			in = new ObjectInputStream(inPut);

			userNumberStream = new DataOutputStream(socket.getOutputStream());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		initStreams();

		while (socket.isConnected()) {
			Object clientQuery = convertClientQuery();
			if (clientQuery instanceof ClientQuery) {
				if (((ClientQuery) clientQuery).type.equals("isUserInSystemDatabase")) {
					proceedIsUserInSystemDatabase((ClientQuery) clientQuery);
				}
			}
			if (clientQuery instanceof ClientQuery) {
				if (((ClientQuery) clientQuery).type.equals("createCustomer")) {
					proceedCreateCustomer((ClientQuery) clientQuery);
				}
			}
			if (clientQuery instanceof ClientQuery) {
				if (((ClientQuery) clientQuery).type.equals("isCustomerAlreadyRegistered")) {
					proceedIsCustomerAlreadyRegistered((ClientQuery) clientQuery);
				}
			}
			if (clientQuery instanceof ClientQuery) {
				if (((ClientQuery) clientQuery).type.equals("fetchCustomer")) {
					fetchCustomer((ClientQuery) clientQuery);
				}
			}
			if (clientQuery instanceof ClientQuery) {
				if (((ClientQuery) clientQuery).type.equals("fetchEmployee")) {
					fetchEmployee((ClientQuery) clientQuery);
				}
			}
			if (((ClientQuery) clientQuery).type.equals("changeCustomerFirstName")) {
				proceedChangeCustomerFirstName((ClientQuery) clientQuery);
			}
			if (((ClientQuery) clientQuery).type.equals("changeCustomerLastName")) {
				proceedChangeCustomerLastName((ClientQuery) clientQuery);
			}
			if (((ClientQuery) clientQuery).type.equals("changeCustomerPassword")) {
				proceedChangeCustomerPassword((ClientQuery) clientQuery);
			}
			if (((ClientQuery) clientQuery).type.equals("addVehicle")) {
				proceedAddVehicle((ClientQuery) clientQuery);
			}
			if (((ClientQuery) clientQuery).type.equals("fetchAllServices")) {
				proceedFetchAllServices((ClientQuery) clientQuery);
			}
			if (((ClientQuery) clientQuery).type.equals("createOrder")) {
				proceedcreateOrder((ClientQuery) clientQuery);
			}
			if (((ClientQuery) clientQuery).type.equals("fetchOrderID")) {
				proceedfetchOrderID((ClientQuery) clientQuery);
			}
			if (((ClientQuery) clientQuery).type.equals("fetchVehicleID")) {
				proceedfetchVehicleID((ClientQuery) clientQuery);
			}
			if (((ClientQuery) clientQuery).type.equals("createServiceOrders")) {
				proceedCreateServiceOrders((ClientQuery) clientQuery);
			}
			if (((ClientQuery) clientQuery).type.equals("fetchOrdersByCustomerID")) {
				proceedFetchOrdersByCustomerID((ClientQuery) clientQuery);
			}
			if (((ClientQuery) clientQuery).type.equals("fetchServiceOrdersByCustomerID")) {
				proceedFetchServiceOrdersByCustomerID((ClientQuery) clientQuery);
			}
			if (((ClientQuery) clientQuery).type.equals("changeCustomerAccountBalance")) {
				proceedChangeAccountBalance((ClientQuery) clientQuery);
			}
			if (((ClientQuery) clientQuery).type.equals("fetchNotTakenOrders")) {
				proceedFetchNotTakenOrders((ClientQuery) clientQuery);
			}
			if (((ClientQuery) clientQuery).type.equals("addEmployeeOrder")) {
				proceedAddEmployeeOrder((ClientQuery) clientQuery);
			}
			if (((ClientQuery) clientQuery).type.equals("fetchEmployeeOrders")) {
				proceedFetchEmployeeOrders((ClientQuery) clientQuery);
			}
			if (((ClientQuery) clientQuery).type.equals("changeOrderStatus")) {
				proceedChangeOrderStatus((ClientQuery) clientQuery);
			}
			if (((ClientQuery) clientQuery).type.equals("sendExitApp")) {
				proceedSendExitApp();
			}
		}
	}
	
	private void proceedChangeOrderStatus(ClientQuery clientQuery) {
		String orderID = clientQuery.parameters[0];
		String status = clientQuery.parameters[1];
		
		Server.complexDatabaseManager.orderDatabaseManager.changeOrderState(orderID, status);
	}

	private void proceedFetchEmployeeOrders(ClientQuery clientQuery) {
		String employeeID = clientQuery.parameters[0];
		
		
		ArrayList<String[]> finalDatas = new ArrayList<String[]>();

		ArrayList<String> orderIDs = Server.complexDatabaseManager.orderDatabaseManager.fetchEmployeeOrder(employeeID);
		for (String orderID : orderIDs) {
			String[] ordersData = Server.complexDatabaseManager.orderDatabaseManager.fetchOrdersByID(orderID);
			String[] vehicle = Server.complexDatabaseManager.vehicleDatabaseManager.fetchVehicleByID(ordersData[1]);
			String serviceName[] = Server.complexDatabaseManager.serviceDatabaseManager
					.fetchServiceNameAndTypeByOrderID(orderID);
			
			String[] finalOrder = new String[10];
			finalOrder[0] = ordersData[0];
			finalOrder[1] = ordersData[1];
			finalOrder[2] = ordersData[2];
			finalOrder[3] = vehicle[0];
			finalOrder[4] = vehicle[1];
			finalOrder[5] = vehicle[2];
			finalOrder[6] = vehicle[3];
			finalOrder[7] = serviceName[0];
			finalOrder[8] = serviceName[1];
			
			finalDatas.add(finalOrder);
		}
		
		ArrayList<Object> objects = new ArrayList<Object>();
		objects.addAll(finalDatas);

		ClientQuery response = (new ClientQuery("fetchEmployeeOrders"));
		response.setAdditionalObjects(objects);
		sendResponce(response);
		
		
		
	}

	private void proceedAddEmployeeOrder(ClientQuery clientQuery) {
		Server.complexDatabaseManager.employeeDatabaseManager.addEmployeeOrder(clientQuery.parameters[0],
				clientQuery.parameters[1]);

		ClientQuery response = (new ClientQuery("addEmployeeOrder"));
		sendResponce(response);

	}

	private void proceedFetchNotTakenOrders(ClientQuery clientQuery) {
		String service = clientQuery.parameters[0];
		ArrayList<String[]> data = Server.complexDatabaseManager.orderDatabaseManager.fetchNotTakenOrders(service);

		ArrayList<String[]> finalDatas = new ArrayList<String[]>();

		for (String[] element : data) {
			String[] finalData = new String[10];
			finalData[0] = element[0];
			finalData[1] = element[1];
			finalData[2] = element[2];
			String[] vehicle = Server.complexDatabaseManager.vehicleDatabaseManager.fetchVehicleByID(element[1]);
			String serviceName[] = Server.complexDatabaseManager.serviceDatabaseManager
					.fetchServiceNameAndTypeByOrderID(element[0]);
			finalData[3] = vehicle[0];
			finalData[4] = vehicle[1];
			finalData[5] = vehicle[2];
			finalData[6] = vehicle[3];
			finalData[7] = serviceName[0];
			finalData[8] = serviceName[1];

			finalDatas.add(finalData);
		}

		ArrayList<Object> objects = new ArrayList<Object>();
		objects.addAll(finalDatas);

		ClientQuery response = (new ClientQuery("fetchNotTakenOrders"));
		response.setAdditionalObjects(objects);
		sendResponce(response);
	}

	private void proceedSendExitApp() {
		this.stop();
	}

	private void proceedChangeAccountBalance(ClientQuery clientQuery) {
		String customerID = clientQuery.parameters[0];
		String accountBalance = clientQuery.parameters[1];

		Server.complexDatabaseManager.customerAccountDatabaseManager.updateCustomerAccountBalance(customerID,
				accountBalance);
	}

	private void proceedFetchServiceOrdersByCustomerID(ClientQuery clientQuery) {
		String customerID = clientQuery.parameters[0];

		ArrayList<String[]> data = Server.complexDatabaseManager.orderDatabaseManager
				.fetchServiceOrdersByCustomerID(customerID);

		ArrayList<Object> objects = new ArrayList<Object>();
		objects.addAll(data);

		ClientQuery response = (new ClientQuery("fetchServiceOrdersByCustomerID"));
		response.setAdditionalObjects(objects);
		sendResponce(response);
	}

	private void proceedFetchOrdersByCustomerID(ClientQuery clientQuery) {
		String customerID = clientQuery.parameters[0];

		ArrayList<String[]> data = Server.complexDatabaseManager.orderDatabaseManager.getOrdersByCustomerID(customerID);

		ArrayList<Object> objects = new ArrayList<Object>();
		objects.addAll(data);

		ClientQuery response = (new ClientQuery("fetchOrdersByCustomerID"));
		response.setAdditionalObjects(objects);
		sendResponce(response);
	}

	private void proceedCreateServiceOrders(ClientQuery clientQuery) {
		String serviceID = clientQuery.parameters[0];
		String orderID = clientQuery.parameters[1];
		Server.complexDatabaseManager.orderDatabaseManager.createServiceOrders(serviceID, orderID);
	}

	private void proceedfetchVehicleID(ClientQuery clientQuery) {
		String vehicleVIN = clientQuery.parameters[1];
		String vehicleID = Server.complexDatabaseManager.vehicleDatabaseManager.fetchVehicleIDByVIN(vehicleVIN);

		ClientQuery response = (new ClientQuery("fetchVehicleID"));
		response.parameters[0] = vehicleID;
		response.parameters[1] = vehicleVIN;
		sendResponce(response);
	}

	private void proceedfetchOrderID(ClientQuery clientQuery) {
		String vehicleID = clientQuery.parameters[0];

		String orderID = Server.complexDatabaseManager.orderDatabaseManager.getOrderByVehicleID(vehicleID);

		ClientQuery response = (new ClientQuery("fetchOrderID"));
		response.parameters[0] = orderID;
		sendResponce(response);
	}

	private void proceedcreateOrder(ClientQuery clientQuery) {
		String vehicleID = clientQuery.parameters[0];

		Server.complexDatabaseManager.orderDatabaseManager.createOrder(vehicleID);
	}

	private void proceedFetchAllServices(ClientQuery clientQuery) {
		ClientQuery response = (new ClientQuery("fetchAllServices"));

		ArrayList<String[]> servicesStrings = Server.complexDatabaseManager.serviceDatabaseManager.fetchAllServices();
		ArrayList<Object> objects = new ArrayList<Object>();
		objects.addAll(servicesStrings);
		response.setAdditionalObjects(objects);
		sendResponce(response);
	}

	private void proceedAddVehicle(ClientQuery clientQuery) {
		String vin = clientQuery.parameters[0];
		String mark = clientQuery.parameters[1];
		String model = clientQuery.parameters[2];
		String customerIDNumber = clientQuery.parameters[3];

		Server.complexDatabaseManager.vehicleDatabaseManager.createVehicle(vin, mark, model, customerIDNumber);
	}

	private void proceedChangeCustomerPassword(ClientQuery clientQuery) {
		String idNumber = clientQuery.parameters[0];
		String password = clientQuery.parameters[1];

		Server.complexDatabaseManager.customerAccountDatabaseManager.changeCustomerPassword(idNumber, password);
	}

	private void proceedChangeCustomerLastName(ClientQuery clientQuery) {
		String idNumber = clientQuery.parameters[0];
		String lastName = clientQuery.parameters[1];

		Server.complexDatabaseManager.customerAccountDatabaseManager.changeCustomerLastName(idNumber, lastName);
	}

	private void proceedChangeCustomerFirstName(ClientQuery clientQuery) {
		String idNumber = clientQuery.parameters[0];
		String firstName = clientQuery.parameters[1];

		Server.complexDatabaseManager.customerAccountDatabaseManager.changeCustomerFirstName(idNumber, firstName);
	}

	private void proceedIsUserInSystemDatabase(ClientQuery clientQuery) {
		ClientQuery response = (new ClientQuery("isUserInSystemDatabase"));

		boolean isEmployeeInSystemDatabase = Server.complexDatabaseManager.employeeDatabaseManager
				.isEmployeeInSystemDatabase(clientQuery.parameters[0], clientQuery.parameters[1]);
		if (isEmployeeInSystemDatabase) {
			response.parameters[0] = clientQuery.parameters[0];
			response.isResponseTrue = true;
			response.parameters[9] = "employee";

			sendResponce(response);
			return;
		} else {
			boolean isCustomerInSystemDatabase = Server.complexDatabaseManager.customerAccountDatabaseManager
					.isCustomerInSystemDatabase(clientQuery.parameters[0], clientQuery.parameters[1]);
			response.parameters[0] = clientQuery.parameters[0];
			response.parameters[9] = "customer";
			response.isResponseTrue = isCustomerInSystemDatabase;

			sendResponce(response);
		}

	}

	private void proceedCreateCustomer(ClientQuery clientQuery) {
		ClientQuery response = (new ClientQuery("createCustomer"));
		String idNumber = clientQuery.parameters[0];
		String pesel = clientQuery.parameters[1];
		String password = clientQuery.parameters[2];
		boolean isResponseTrue = Server.complexDatabaseManager.customerAccountDatabaseManager.createCustomer(idNumber,
				pesel, password);

		response.isResponseTrue = isResponseTrue;

		sendResponce(response);
	}

	private void fetchCustomer(ClientQuery clientQuery) {
		ClientQuery response = (new ClientQuery("fetchCustomer"));
		String idNumber = clientQuery.parameters[0];
		String[] responseData = Server.complexDatabaseManager.customerAccountDatabaseManager
				.fetchCustomerFromDatabase(idNumber);

		response.parameters[0] = responseData[0];
		response.parameters[1] = responseData[1];
		response.parameters[2] = responseData[2];
		response.parameters[3] = responseData[3];
		response.parameters[4] = responseData[4];
		response.parameters[5] = responseData[5];
		response.parameters[6] = responseData[6];
		ArrayList<Object> objects = new ArrayList<Object>();
		objects.addAll(Server.complexDatabaseManager.vehicleDatabaseManager.fetchVehiclesByCustomerID(responseData[0]));
		response.setAdditionalObjects(objects);

		sendResponce(response);
	}

	private void fetchEmployee(ClientQuery clientQuery) {
		ClientQuery response = (new ClientQuery("fetchEmployee"));
		String idNumber = clientQuery.parameters[0];
		String[] responseData = Server.complexDatabaseManager.employeeDatabaseManager.fetchEmployee(idNumber);

		response.parameters[0] = responseData[0];
		response.parameters[1] = responseData[1];
		response.parameters[2] = responseData[2];
		response.parameters[3] = responseData[3];
		response.parameters[4] = responseData[4];
		response.parameters[5] = responseData[5];
		response.parameters[6] = responseData[6];
		sendResponce(response);
	}

	private void proceedIsCustomerAlreadyRegistered(ClientQuery clientQuery) {
		ClientQuery response = (new ClientQuery("isCustomerAlreadyRegistered"));
		String idNumber = clientQuery.parameters[0];
		boolean isResponseTrue = Server.complexDatabaseManager.customerAccountDatabaseManager
				.isCustomerAlreadyRegistered(idNumber);

		response.isResponseTrue = isResponseTrue;

		sendResponce(response);
	}

	private void sendResponce(ClientQuery response) {
		try {
			out.writeObject(response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Object convertClientQuery() {
		Object clientQuery = null;
		try {
			clientQuery = in.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return clientQuery;
	}
}

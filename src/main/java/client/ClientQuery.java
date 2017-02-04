package client;

import java.io.Serializable;
import java.util.ArrayList;

public class ClientQuery implements Serializable {
	private static final long serialVersionUID = 1L;
	public String type;
	public boolean isResponseTrue;
	public String parameters[] = new String[10];
	private final ArrayList<Object> additionalObjects = new ArrayList<Object>();

	public ClientQuery(String type) {
		for (int i = 0; i < 10; i++) {
			parameters[i] = new String();
		}
		this.type = type;
	}

	public void setAdditionalObjects(ArrayList<Object> objects) {
		additionalObjects.addAll(objects);
	}

	public ArrayList<Object> getAdditionalObjects() {
		return additionalObjects;
	}
}

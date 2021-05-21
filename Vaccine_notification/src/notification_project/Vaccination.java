package notification_project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Vaccination {
	public Vaccination() {
		// TODO Auto-generated constructor stub
	}
	public static void main(String[] args) {
		checkSlot();		
	}
	private static void checkSlot() {
		// TODO Auto-generated method stub
		int pin = 471001;
		String date = "20-05-2021";
		String type = "COVAXIN"; //(COVAXIN\COVISHIELD)
		HttpURLConnection connection;
		try {
			//https://cdn-api.co-vin.in/api/v2/appointment/sessions/findByPin?pincode=110001&date=31-03-2021&vaccine=COVAXIN
			//https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByPin?pincode=471001&date=20-05-2021
			URL url = new URL("https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByPin?"
					+"pincode="+pin	//+"110001"+
					+"&date="+date	//+"31-03-2021
					+"&vaccine="+type//+"COVISHIELD"
					);
			connection = (HttpURLConnection) url.openConnection();
			connection.connect();
			int code = connection.getResponseCode();
			InputStream is = connection.getInputStream();
			String response = connection.getResponseMessage();
			BufferedReader in = new BufferedReader(
					  new InputStreamReader(connection.getInputStream()));
			String inputLine;
			StringBuffer content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
			    content.append(inputLine);
			}
			in.close();
			response = content.toString();
			if(code != 200) {
				System.out.println(response);
			}else {
				
				Notification.sendNotification(response);
			}
				
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}	
}

package notification_project;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Online_code {

	private static final String USER_AGENT = "Mozilla/5.0";

	private static String GET_URL_COVAXIN = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByPin?";
	
	private static String GET_URL_COVISHIELD = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByPin?";
	
	private static Boolean END = false;

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		int pin = 471001;
		String date = "22-05-2021";
		String type = "COVAXIN"; //(COVAXIN\COVISHIELD)
		GET_URL_COVAXIN += "pincode="+pin	//+"110001"+
				+"&date="+date	//+"31-03-2021
				+"&vaccine="+type;//+"COVISHIELD"
				
		GET_URL_COVISHIELD += "pincode="+pin	//+"110001"+
				+"&date="+date	//+"31-03-2021
				+"&vaccine="+"COVISHIELD";//+"COVISHIELD"
				
		while(true) {			
			checkStatus();
			if(END)
				break;
			try {
				TimeUnit.SECONDS.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("GET DONE"); 
	}
	
	private static void checkStatus() {
		// TODO Auto-generated method stub

		Date date = new Date();
		System.out.println("Checking at :"+date.toString());
		String response1 = "";
		String response2 = "";
		try {
			response1 = sendGET(GET_URL_COVAXIN);
			response2 = sendGET(GET_URL_COVISHIELD);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(response.toString());
		formatResponse(response1,response2);
	}

	private static String sendGET(String URL) throws IOException {
		URL url = new URL(URL);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		//con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);
		int responseCode = con.getResponseCode();
		StringBuffer response = new StringBuffer();
		if (responseCode == HttpURLConnection.HTTP_OK) { 
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			con.disconnect();			
		} else {
			System.out.println("GET request not worked");
		}
		return response.toString();
	}

	private static void formatResponse(String response1,String response2) {
		// TODO Auto-generated method stub
		ObjectMapper mapper = new ObjectMapper();
	      try {
			Map res1 = mapper.readValue(response1, HashMap.class);
			Map res2 = mapper.readValue(response2, HashMap.class);
			
			ArrayList<Map> covidCenters = (ArrayList<Map>) res1.get("sessions");
			covidCenters.addAll(((ArrayList<Map>) res2.get("sessions")));
			for (Map center : covidCenters) {
				int age = (int) center.get("min_age_limit");
				if(age == 45) {
					int dose1 = (int) center.get("available_capacity_dose1");
					if(dose1>0) {
						System.out.println("available");
						END = true;
					}
				}
			}
			//System.out.println(emp.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	   
	}
}

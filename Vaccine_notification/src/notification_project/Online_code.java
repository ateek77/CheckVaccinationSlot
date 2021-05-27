package notification_project;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
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
	
	private static String  SESSION = "sessions";
	
	private static String NOTIFICATION_FILE_PATH = "C:\\Users\\mateek\\Desktop\\Slot_Notification.html";
	
	private static Boolean END = false;
	
	private static int AGE = 45;
	
	private static int PIN = 471001;
	
	private static String TODAY_DATE = "28-05-2021";
	
	private static String TYPE = "COVAXIN"; //COVISHIELD

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		//(COVAXIN\COVISHIELD)
		GET_URL_COVAXIN += "pincode="+PIN	//+"110001"+
				+"&date="+TODAY_DATE	//+"31-03-2021
				+"&vaccine="+TYPE;//+"COVISHIELD"
		System.out.println("start checking for vaccine at pin " + PIN + "| date " + TODAY_DATE);
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
		String response = "";
		try {
			response = sendGET(GET_URL_COVAXIN);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(response.toString());
		ArrayList<Map>  res = formatResponse(response);

		res = checkAvailability(res);
		if(res.size()>0)
			notifyUser(res);
		//System.out.println(emp.toString());
	}

	private static void notifyUser(ArrayList<Map> res) {
		// TODO Auto-generated method stub
		
		File htmlFile = new File(NOTIFICATION_FILE_PATH);
		
		try {
			Desktop.getDesktop().browse(htmlFile.toURI());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private static ArrayList<Map> checkAvailability(ArrayList<Map> res) {
		// TODO Auto-generated method stub
		ArrayList<Map> availableSlot = new ArrayList<Map>();
		for (Map center : res) {
			int age = (int) center.get("min_age_limit");
			if(age == AGE) {
				int dose1 = (int) center.get("available_capacity_dose1");
				if(dose1>0) {
					System.out.println("available for 45+ :" + center);
					availableSlot.add(center);
					//END = true;
				}
			}else if( age == 18) {
				int dose1 = (int) center.get("available_capacity_dose1");
				if(dose1>0) {
					System.out.println("available for 18+ :" +center);
					availableSlot.add(center);
				}
			}
		}
		return availableSlot;
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

	private static ArrayList<Map> formatResponse(String response) {
		// TODO Auto-generated method stub
		ObjectMapper mapper = new ObjectMapper();
	      try {
			Map res1 = mapper.readValue(response, HashMap.class);
			
			 return (ArrayList<Map>) res1.get(SESSION);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;	   
	}
}

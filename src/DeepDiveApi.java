import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * 
 */

/**
 * @author georgos7
 *
 */
public class DeepDiveApi {

	public HttpURLConnection PostHeader (URL u) {
		
				HttpURLConnection http =null;
				try {
					http = (HttpURLConnection)u.openConnection();
					http.setConnectTimeout(3000);
					http.setDoInput(true);
					http.setDoOutput(true);
					http.addRequestProperty("Content-Type", "application/json");
					http.setRequestMethod("POST");
					http.connect();
			
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return http;
	}
	
	public HttpURLConnection GetHeader (URL u) {
		
		HttpURLConnection http =null;
		try {
			http = (HttpURLConnection)u.openConnection();
			http.setDoInput(true);
			http.setDoOutput(false);
			http.addRequestProperty("Content-Type", "application/json");
			http.setRequestMethod("GET");
			http.connect();
	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return http;
}

public JSONObject  handleResponse(HttpURLConnection th) throws IOException {
		
		JSONObject  ob = null;
		
		if(th.getResponseCode()== 200) {
			
			
			BufferedReader in = null;
			try {
				in = new BufferedReader(new InputStreamReader(th.getInputStream()));

				String res = in.readLine();
				
				JSONParser parse = new JSONParser();
				
				ob = (JSONObject) parse.parse(res);
				
				in.close();
				
			} catch (IOException | ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return ob;
	}

	public JSONObject getUsersList(String str) {
		
		JSONObject clients = null;
		
		try {
			URL url = new URL("http://localhost:6005/DeepDiveget/userslist?username="+str);
			
			HttpURLConnection ht = this.GetHeader(url);
					
			clients = this.handleResponse(ht);
			
			
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clients;
		
	}
	public JSONObject onlineUsers(String st) {
		
JSONObject clients = null;
		
		try {
			URL url = new URL("http://localhost:6005/DeepDiveget/onlineusers?username="+st);
			
			HttpURLConnection ht = this.GetHeader(url);
					
			clients = this.handleResponse(ht);
			
			
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clients;
		
		
	}
	
	public JSONObject getFriendsList(String st){
		
		JSONObject clients = null;
		
		try {
			URL url = new URL("http://localhost:6005/DeepDiveget/friends?username="+st);
			
			HttpURLConnection ht = this.GetHeader(url);
					
			clients = this.handleResponse(ht);
			
			
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clients;
				
	}
	
public JSONObject getGroupRooms(String st){
		
		JSONObject clients = null;
		
		try {
			URL url = new URL("http://localhost:6005/DeepDiveget/grouprooms?username="+st);
			
			HttpURLConnection ht = this.GetHeader(url);
					
			clients = this.handleResponse(ht);
			
			
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clients;
				
	}

	public JSONObject sendReceivedMsg(String st){

		System.out.println("About to send message");

		JSONObject clients = null;

		try {
			URL url = new URL("http://localhost:3040/getReceivedMessage?message="+st);

			HttpURLConnection ht = this.GetHeader(url);

			clients = this.handleResponse(ht);
			System.out.println("Msg sets");


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clients;

	}
	

public JSONObject getPrivateGroupRooms(String st, String gt){
	
	JSONObject clients = null;
	
	try {
		URL url = new URL("http://localhost:6005/DeepDiveget/privategrouprooms?username="+st+"&groupname="+ gt);
		
		HttpURLConnection ht = this.GetHeader(url);
				
		clients = this.handleResponse(ht);
		
		
	
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return clients;
			
}
public JSONObject getGroupRoomMembers(String st, String gt){
	
	JSONObject clients = null;
	
	try {
		URL url = new URL("http://localhost:6005/DeepDiveget/groupmemberslist?username="+st+"&groupname="+ gt);
		
		HttpURLConnection ht = this.GetHeader(url);
				
		clients = this.handleResponse(ht);
		
		
	
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return clients;
			
}
	
}

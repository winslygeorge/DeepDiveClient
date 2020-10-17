import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Base64;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * 
 */

/**
 * @author georgos7
 *
 */
public class ConPrac {

	/**
	 * @param args
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 * @throws SignatureException 
	 * @throws s 
	 */
	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
	
		DeepDiveApi api = new DeepDiveApi();
		
		JSONArray clients = (JSONArray) api.getGroupRoomMembers("@_grace", "_caddox-fan-club").get("responseMessage");
		
		JSONObject ob = (JSONObject)clients.get(1);
		
		System.out.println(ob.get("username"));
	}

}

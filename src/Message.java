import java.io.Serializable;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.crypto.SecretKey;

/**
 * 
 */

/**
 * @author georgos7
 *
 */
public class Message implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8861133724187441662L;

	/**
	 *  -8861133724187441662L;
	 */

	protected String message;
	
	protected String from;
	
	protected String to;
	
	protected Date currenttime;
	
	protected Date time;
	
	protected String type;
	
	private String signature;
	
	private boolean isPrivate;
	
	protected boolean privateCode;
	protected Certificate certificate;

	private SecretKey sk;

	private String keyString;

	private String groupClient;
	
	public Message(String type, String msg) {
		
		this.type = type;
		this.message = msg;
	}
	
	public Message(String groupclient, String type,String fro, String to, String msg, boolean b) {
		
		this.from = fro;
		this.to = to;
		this.message = msg;
		this.currenttime = new Date();
		this.setPrivate(b);
		this.type = type;
		this.setGroupClient(groupclient);
	}
	
	

	public Message(String from, Certificate cert){
		
		this.from = from;
		
		this.certificate = cert;
		
		
	}
	public Message(String type2, String fro, String to2, String msg,  long l) {
		// TODO Auto-generated constructor stub
		
		this.from = fro;
		this.to = to2;
		this.message = msg;
		this.time = new Date(l);
		
		this.type = type2;
	}

	public Message(String string, X509Certificate accessCertification, SecretKey asymmetrickey) {
		// TODO Auto-generated constructor stub
		
		this.from = string;
		
		this.certificate = accessCertification;
		
		this.sk = asymmetrickey;
		
		
	}
	
	public Message(String userName, String from2, X509Certificate accessCertification) {
		// TODO Auto-generated constructor stub
		
		this.from = userName;
		this.to = from2;
		this.certificate = accessCertification;
	}

	public Message(String from2, String to2, boolean b) {
		// TODO Auto-generated constructor stub
		
		this.from = from2;
		this.to = to2;
		this.privateCode = b;
	}

	public Message(String type2, String from2, String to2,String sign, String ecns, boolean b, String st) {
		// TODO Auto-generated constructor stub
		
		this.type = type2;
		this.from = from2;
		this.to = to2;
		this.message = ecns;
		this.setPrivate(b);
		this.setSignature(sign);
		this.keyString = st;
	}

	
	public Message(String string,String userName, String from2, X509Certificate accessCertification) {
		// TODO Auto-generated constructor stub
		this.setGroupClient(string);
		this.from = userName;
		this.to = from2;
		this.certificate = accessCertification;
	}

	public SecretKey getAsymmetrickey() {
		return this.sk;
	}
	public String getFrom() {
		return this.from;
	}
	
	public String getTo() {
		return this.to;
	}
	
	public Date getTime() {
		return this.time;
	}
	
	public String getMessage() {
		return this.message;
	}
	public Certificate getCertificate() {
		
		return this.certificate;
	}
	
	public String getType() {
		return type;
	}
	
	public Date getCurrentTime() {
		return this.currenttime;
	}

	public boolean getisPrivate() {
		return isPrivate;
	}

	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}
	public boolean getPrivateCode() {
		return this.privateCode;
	}
	
	public String getKeyString() {
		return this.keyString;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getGroupClient() {
		return groupClient;
	}

	public void setGroupClient(String groupClient) {
		this.groupClient = groupClient;
	}
}


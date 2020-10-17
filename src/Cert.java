import java.io.Serializable;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

/**
 * 
 */

/**
 * @author georgos7
 *
 */
public class Cert implements Serializable {
	
	/**
	 * 
	 */
	

	private String str;
	
	private X509Certificate rct;
	private PublicKey key;
	
	private X509Certificate cert;
	
	
	public Cert(String st, PublicKey key) {
		
		setStr(st);
		setKey(key);
	}
	public Cert(String st, X509Certificate ct, X509Certificate rct) {
		
		setStr(st);
		setCert(ct);
		setRct(rct);
	}
	private static final long serialVersionUID = -6035178875132107970L;

	public String getStr() {
		return str;
	}
	public void setStr(String str) {
		this.str = str;
	}

	public PublicKey getKey() {
		return key;
	}

	public void setKey(PublicKey key) {
		this.key = key;
	}

	public X509Certificate getCert() {
		return cert;
	}

	public void setCert(X509Certificate cert) {
		this.cert = cert;
	}


	public X509Certificate getRct() {
		return rct;
	}
	
	public void setRct(X509Certificate rct) {
		this.rct = rct;
	}

}

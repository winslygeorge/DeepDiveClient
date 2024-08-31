import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;

/**
 * 
 */

/**
 * @author georgos7
 *
 */
public class MessageSecurity {
	
	private Cipher cipher;
	
	private SecretKey secretekey;
	
	protected String passwd;
	private SecretKey kss;
	
	private boolean isprivate = false;
	
	protected String u  = null;
	
	private String dekey = null; 
	
	protected String sender = null;
	
	public MessageSecurity(String user,String pass) {
		
		u = user;
		passwd = pass;
		
		System.out.println("message security initiated");
		
	}
	public MessageSecurity(String sendr,String user,String pass, String enkey) {
		
		u = user;
		passwd = pass;
		setDekey(enkey);
		this.sender = sendr;
	
	}
	
	public void setPublicMode() {
		
		
		try {
		
			//ProtectionParameter pparam = new  KeyStore.PasswordProtection("passwd".toCharArray());
			KeyStore keystore = KeyStore.getInstance("jks");
			
			keystore.load(new FileInputStream("confid/"+this.u+"/pirate.jks"), passwd.toCharArray());
		
		SecretKey  keysp =  new GetAccess(this.u, passwd).getAsymmetrickey();
		
		setSecretekey(keysp);
		
		setIsprivate(false);
		
		} catch (NoSuchAlgorithmException | KeyStoreException | CertificateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
	
	public String encryptData(String str) {
		
		String encodedString = "";
	
	
		if(isIsprivate() == false) {
			

			ByteBuffer clearr = ByteBuffer.allocate(str.getBytes(StandardCharsets.UTF_16).length);
			clearr.put(str.getBytes(StandardCharsets.UTF_16));
			
			
			ByteBuffer bt = this.encrypt(clearr, this.getSecretekey());
			
			
		 encodedString = Base64.getEncoder().encodeToString(bt.array());
			
			System.out.println(encodedString);
			
			return encodedString;
			
		}else if (isIsprivate() == true) {
			
			/*first encrypt data
			 * get the encrypted key
			 * lastly sign encrypted data
			 */
			System.out.println("private encryption initiated");
			
			kss = this.generateSecretKeySpec();
			System.out.println(kss);
			encodedString =  this.encryptMessage(str, kss);
		
		}else {
			
			System.out.println("mode not indicated");
			encodedString = null;
		}
		
		return encodedString;
	}
	
	public String decryptData(String enc, String msg) {
		
		String decodedString = "";	
		
		if(isIsprivate()== false) {
			
			
			System.out.println("decryption is on public mode...");

			byte[] fb = Base64.getDecoder().decode(enc);
			
			ByteBuffer g = ByteBuffer.allocate(fb.length);
			
			g.put(fb);
			
			ByteBuffer h =decrypt(g, this.getSecretekey());
			
			String s = new String(h.array(), StandardCharsets.UTF_16);
			
			return s;
			}else if(isIsprivate()) {
			
			System.out.println("decryption is on private mode");
			
			if(this.verifyMessage(enc, msg, this.sender)) {
				
				System.out.println("msg was verified");
				
				decodedString = this.decryptMessage(enc, this.decryptSymmetricKey(this.getDekey()));
				
				System.out.println("decryptedString : "+ decodedString);
				
			}else {
				
				System.out.println("The message is from unknown source ");
			}
			
		}else {
			
			System.out.println("decoder mode not indicated");
		}
		return decodedString;
	}

	public Cipher getCipher() {
		return cipher;
	}

	public void setCipher(Cipher cipher) {
		this.cipher = cipher;
	}

	public SecretKey getSecretekey() {
		return secretekey;
	}

	public void setSecretekey(SecretKey keysp) {
		this.secretekey = keysp;
	}
	
	public SecretKey getSks() {
		
		return this.kss;
	}

	public String signMessage(String msg) {
		
		byte [] signature = null;
		try {
			Signature sign = Signature.getInstance("SHA256WithRSA");
			GetAccess access = new GetAccess(this.u, passwd);
			
			
			PrivateKey pk = access.getAccessKey();
			sign.initSign(pk);
			
			sign.update(msg.getBytes());
			
			signature = sign.sign();
			
		
			
		} catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Base64.getEncoder().encodeToString(signature);
	}
	
	
	public boolean verifyMessage(String msg,String ms, String sendername) {
		System.out.println("verification iitialized");
		
		boolean verified = false;
		
		byte [] decodedMsg = Base64.getDecoder().decode(ms);
		
		byte[] dsmsg = msg.getBytes();
		
		Signature sign;
		try {
			
			sign = Signature.getInstance("SHA256WithRSA");
			
			String ur = ">"+sendername;
			System.out.println(ur);
			
			KeyStore k = KeyStore.getInstance("jks");
			
			k.load(new FileInputStream("confid/piratefriend.jks"), passwd.toCharArray());
			
		Certificate kp = 	k.getCertificate(ur);
		
			System.out.println(kp);
		
			sign.initVerify(kp);	
			
			sign.update(dsmsg);
			verified = sign.verify(decodedMsg);
			System.out.println(verified);
		} catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException | KeyStoreException | CertificateException | IOException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());

		}
	
	
		
		return verified;
	}
	
	public String encryptMessage(String str, SecretKey kss2) {
		
		
		ByteBuffer clearr = ByteBuffer.allocate(str.getBytes(StandardCharsets.UTF_16).length);
		clearr.put(str.getBytes(StandardCharsets.UTF_16));
		
		
		ByteBuffer bt = this.encrypt(clearr, kss2);
		
		
		String s = Base64.getEncoder().encodeToString(bt.array());
		
		System.out.println(s);
		return s;
	}
	public  String encryptSymmetricKey(String receivername, SecretKey spec) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException {
		System.out.println("initiating encryption of secret key" + spec);
		
		
		
		String encodedKey = "";
		String us = ">"+receivername;

			System.out.println(us);
		KeyStore k = KeyStore.getInstance("jks");
		
		k.load(new FileInputStream("confid/piratefriend.jks"), passwd.toCharArray());
		
		PublicKey pk = k.getCertificate(us).getPublicKey();
		
		System.out.println(pk + "....");
		Cipher cip = null;
		try {
			cip = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
			
			cip.init(Cipher.ENCRYPT_MODE, pk , new SecureRandom());
			
			cip.update(spec.getEncoded());
			
			
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());

		}
		try {
            assert cip != null;
            encodedKey =  Base64.getEncoder().encodeToString(cip.doFinal());
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());

		}
		
		return encodedKey;
	}
	
	public SecretKey decryptSymmetricKey(String string) {
		Cipher piper = null;
		
		SecretKey speckey = null;
		try {
			piper = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
			
			
			
			GetAccess access = new GetAccess(this.u, passwd);
			
			
			
			PrivateKey ky = access.getAccessKey();
			piper.init(Cipher.DECRYPT_MODE, ky, new SecureRandom());
			
			piper.update(Base64.getDecoder().decode(string));
			
			speckey = (SecretKey)new SecretKeySpec(piper.doFinal(), "AES");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		}
	
		
		
		return speckey;
	}
	
	public String decryptMessage(String msgg, SecretKey keyspec) {
		byte[] fb = Base64.getDecoder().decode(msgg);
		
		ByteBuffer g = ByteBuffer.allocate(fb.length);
		
		g.put(fb);
		
		ByteBuffer h =decrypt(g, keyspec);
		
		String s = new String(h.array(), StandardCharsets.UTF_16);
		
		return s;
	}
	public String getDekey() {
		return dekey;
	}
	public void setDekey(String dekey) {
		this.dekey = dekey;
	}
	public SecretKey generateSecretKeySpec() {
		
		System.out.println("key gen initiated");
		SecretKey keysecret = null;
		try {
			KeyGenerator keygen = KeyGenerator.getInstance("AES");
			keygen.init(256);
			
			 keysecret = keygen.generateKey();
			
			System.out.println("secrete key generated");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());

		}
System.out.println("key successful" + keysecret);
	return keysecret;	
	}
	public boolean isIsprivate() {
		return isprivate;
	}
	public void setIsprivate(boolean isprivate) {
		this.isprivate = isprivate;
	}
	
public  ByteBuffer encrypt(ByteBuffer clear, SecretKey key){
		ByteBuffer allbf= null;
		if(clear == null || !clear.hasRemaining()) {
			
			return clear;
		}
		try {
			Cipher cp = Cipher.getInstance("AES/ECB/PKCS5Padding");
			

			cp.init(Cipher.ENCRYPT_MODE, key, new SecureRandom());
			
			ByteBuffer encrypted = ByteBuffer.allocate(cp.getOutputSize(clear.remaining()));
			
			cp.doFinal(clear, encrypted);
			
			allbf = (ByteBuffer) encrypted.rewind();
		
			
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | ShortBufferException | IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());

		}
		
		
		return allbf;
	}

public ByteBuffer decrypt(ByteBuffer g2, SecretKey k) {
	
	
	ByteBuffer alldec = null;
	
	if(g2== null || !g2.hasRemaining()) {
		
		return g2;
	}
	
	try {
		
		
		Cipher cp = Cipher.getInstance("AES/ECB/PKCS5Padding");
		
		cp.init(Cipher.DECRYPT_MODE, k, new SecureRandom());
		
		ByteBuffer decrypted = ByteBuffer.allocate(cp.getOutputSize(g2.remaining()));
		
		cp.doFinal(g2, decrypted);
		
		 alldec= (ByteBuffer) g2.rewind();
	
		
	} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | ShortBufferException |
             IllegalBlockSizeException | BadPaddingException e) {
		// TODO Auto-generated catch block
		System.err.println(e.getMessage());

	}
    return alldec;
}
}
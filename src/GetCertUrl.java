import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * 
 */

/**
 * @author georgos7
 *
 */

public class GetCertUrl {

	/**
	 * @param args
	 */
	
	protected static File file = null;
	protected String userpass = null;
	protected X509Certificate cert = null;
	
	private static char [] passcode;
	
	protected PrivateKey key = null; 
	
public GetCertUrl (String hostname, int port, String user, String passwd) {
		// TODO Auto-generated method stub
		
	userpass = passwd;
		try {
			
			Socket client = new Socket(hostname, port);
			
			System.out.println("conneced to cert giver");
			ObjectOutputStream  out = new ObjectOutputStream(client.getOutputStream());
			
			ObjectInputStream  in = new ObjectInputStream(client.getInputStream());
			
			KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
			
			keygen.initialize(4096, new SecureRandom());
			
			KeyPair key = keygen.generateKeyPair();
			
			file = new File("confid");
			if(!file.exists()) {

				file.mkdir();
			
				System.out.println("directory created");
				
			}
			
			
				out.writeUTF("##reqcert>>");
				out.writeObject(new Cert(user, key.getPublic()));
				out.flush();
				System.out.println("cert to sign sent");
				Cert ct = (Cert)in.readObject();
		System.out.println("Access way provided");			
			System.out.println(store(passwd, key.getPrivate(), ct.getRct(), ct.getCert()));
			
			
			in.close();
			out.close();
			client.close();

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
public static boolean store(String userpasswd, PrivateKey key,X509Certificate certificate, X509Certificate certificate2) {
	
	boolean isconfigured = false;
	
	try {
		KeyStore keystore = KeyStore.getInstance("jks");
		
		keystore.load(null, userpasswd.toCharArray());
		
		X509Certificate[] chain = new  X509Certificate[2];
		chain[0] = certificate;
		chain[1] = certificate2;
		
		passcode = (userpasswd+"codd").toCharArray();
		keystore.setKeyEntry("userPrivateKey", key , passcode, chain );
		
		File file = new File("confid/pirate.jks");
		
		if(file.getFreeSpace()!= -1|| file.getFreeSpace()!=0) {
		
		keystore.store(new FileOutputStream(file) , userpasswd.toCharArray());
		
		}else {
			
			File fl = new File("confid/pirate1.jks");
			

			if(file.getFreeSpace()!= -1|| file.getFreeSpace()!=0) {
			
			keystore.store(new FileOutputStream(fl) , userpasswd.toCharArray());
			}else {
				
				System.err.println("you need upgrade for the system");
			}
		}
		
		if(keystore.size()!= 0||keystore.size()!=-1)
		{
		 isconfigured = true;
		}
		
		
		
	} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	return isconfigured;
	
	
}

public X509Certificate getAccessCertification(String args) {
	
	try {
		KeyStore keystore = KeyStore.getInstance("jks");
		
		keystore.load(new FileInputStream("confid/pirate.jks"), userpass.toCharArray());
		
	
		if(args.matches("root")) {
			
		cert = 	(X509Certificate) keystore.getCertificateChain("userPrivateKey")[0];
		
		}else if(args.trim().equals("client")) {
			
			cert = 	(X509Certificate) keystore.getCertificateChain("userPrivateKey")[1];
	}else {
			
			System.out.println("no such entry");
			
		}
			
	} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return cert;
	
}

protected PrivateKey getAccessKey() {
	
	try {
		KeyStore keystore = KeyStore.getInstance("jks");
		
		keystore.load(new FileInputStream("confid/pirate.jks"), userpass.toCharArray());
		
		key = (PrivateKey) keystore.getKey("userPrivateKey", this.getCode());
		
	} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException | UnrecoverableKeyException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
		
	return key;
	
	
}

private char[] getCode() {
	
	return passcode;
}
}

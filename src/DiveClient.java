import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import javax.crypto.SecretKey;

/**
 * 
 */

/**
 * @author georgos7
 *
 */
public class DiveClient{
	
	java.security.cert.X509Certificate cert;
	
	protected  String userName = null;
	

	private static Socket client;
	
	protected ObjectOutputStream out;
	protected String passwd = null;
	protected ObjectInputStream in;
	
	protected Queue<Message> msgq = new LinkedList<Message>();
	public DiveClient(String host, int p, String user, String pass ) {
		
		
		
		
		passwd = pass;
		userName = user;
		File file = new File("confid");
				
		if(!file.exists()) {
		
		GetCertUrl validate = new GetCertUrl(host, p, user, pass);
		
		System.out.println(validate.getAccessCertification("client").getSubjectDN()+" : certificate created");
			
		}
		
		
		GetAccess access = new GetAccess(pass);
		PublicKey pk = access.getAccessCertification("root").getPublicKey();
		try {
			
			access.getAccessCertification("client").verify(pk);
			
			
			setClient(new Socket("127.0.0.1", 5000));
			
			out = new ObjectOutputStream(client.getOutputStream());
			
			in = new ObjectInputStream(client.getInputStream());
			
			out.writeObject(new Message(user, access.getAccessCertification("client")));
			
			out.flush();

				
			
			Message ms = (Message)in.readObject();
			
			
			try{
				
				
				ms.getCertificate().verify(pk);
				
				
				
				if(this.storeImportant(pass, ms.getAsymmetrickey())== true){
					out.writeInt(1);
					out.flush();
					
					out.writeUTF(user);
					out.flush();
					System.out.println("client connected successfully to the server...");
					
					System.out.println(client.getInetAddress().getHostAddress()+":"+ client.getLocalPort()+":"+ user);
				
					
				}else {
					
					out.writeInt(0);
					out.flush();
					System.out.println("Client didn't get access code right..");
				}
				
		
				
			}catch(Exception e) {
				
				out.writeInt(0);
				out.flush();
				e.printStackTrace();
				
				in.close();
				out.close();
				client.close();
			}
		
			
			
		} catch (InvalidKeyException | CertificateException | NoSuchAlgorithmException | NoSuchProviderException
				| SignatureException | IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		

	}
	
	public void runClient() {
		
		try {
			
					while(true) {
						
					Message str = (Message)in.readObject();
					
					if(str == null)return;
					
					msgq.add(str);
					
					Message mx = null;
					
					if(msgq.size() > 0) {
						
						mx = msgq.remove();
					}
					
					if(mx == null) {
						
						continue;
					}else {
						
						if(!mx.getType().equalsIgnoreCase("token")) {
							
							out.writeObject(new Message("privatechat", "token", this.userName, mx.getFrom(), "delivered", false));
							out.flush();
							
						}
						
					}
					
					
					
					System.out.println(str.getCertificate()+" : "+str.getPrivateCode()+ " : "+str.getType());
					

					if( str.getPrivateCode() == true) {
						
						System.out.println("Private chat request was denied bi "+ str.getFrom());
					}else if(str.getCertificate()!=null) {
						
						
						File f = new File("confid/piratefriend.jks");
				
						
						if(f.createNewFile()) {
							
							System.out.println("file piratefriend List created");
							
							KeyStore k = KeyStore.getInstance("jks");
							
							
							k.load(null, passwd.toCharArray());
							
							k.store(new FileOutputStream(f), passwd.toCharArray());
							
							
							
						}else {
							
							System.out.println("piratefriend not created");
						}
						
						KeyStore skk = KeyStore.getInstance("jks");
						
						skk.load(new FileInputStream("confid/piratefriend.jks"), passwd.toCharArray());
						
						String m = ">"+str.getFrom();
						
						
												
						skk.load(new FileInputStream(f), passwd.toCharArray());
						
						System.out.print(skk.containsAlias(m));
						
						if(skk.containsAlias(m) != true) {
					
							
							

							if(this.storeImportant(str.getFrom(), passwd, str.getCertificate()) == true) {
								System.out.println(str.getFrom());
							
								System.out.println("Private chat Initiated.\n");
							}else {
								
								System.out.println("Error initiating private chat..");
							}
							
						}else {
							
							System.out.println("Private chat is on..");
						}
					
				}else if(str.getMessage().trim().equals(">>requestprivate##")) {
						
						System.out.println(str.getFrom()+" : "+ "is requesting a private chat. Enter y/n..?");
						
						Scanner scan = new Scanner(System.in);
						
						if(scan.nextLine().trim().equalsIgnoreCase("y")) {
						
							scan.close();
							out.writeObject(new Message(str.getTo(), str.getFrom(), new GetAccess(passwd).getAccessCertification("client") ));
							out.flush();
							
							System.out.println("...initializing...");
						}else {
							out.writeObject(new Message(str.getTo(), str.getFrom(), true));
							out.flush();
							System.out.println("private chat denied...");
						}
					
						
						
					}else {
						

						new MessageHandler(this.userName,this.passwd,str, out).HandleMessage();
						System.out.println(str.getMessage());
					}
					
						
						
						
					if(str.getFrom().isEmpty()) break;
					
				}
					
					
					in.close();
					out.close();
					client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (KeyStoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CertificateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}
	



	public Socket getClient() {
		return client;
	}


	public void setClient(Socket client) {
		DiveClient.client = client;
	}
	
	public Socket readData() {
				
			return client;
		}
	
	public boolean storeImportant(String friendName,String p, Certificate cert) {
		boolean is = false;
		
		try {
			KeyStore k = KeyStore.getInstance("jks");
			
			File f = new File("confid/piratefriend.jks");
			
			
			k.load(null, p.toCharArray());
			
			String ur = ">"+friendName;
			
			k.setCertificateEntry(ur, cert);
			
			k.store(new FileOutputStream(f), p.toCharArray());
			is = true;
			
			
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return is;
	}
	public boolean storeImportant(String p, SecretKey ky) {
		
		boolean is = false;
		
		try {
			KeyStore k = KeyStore.getInstance("jceks");
			
			k.load(null, p.toCharArray());
			
			KeyStore.ProtectionParameter pparam = new KeyStore.PasswordProtection(p.toCharArray());
			
			
			KeyStore.SecretKeyEntry sentry = new KeyStore.SecretKeyEntry(ky);
			k.setEntry("asymmetrickey", sentry, pparam);
			
			k.store(new FileOutputStream("confid/pirate.jceks"), p.toCharArray() );
			
			is = true;
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return is;
	}
	
public void Chat(Message message) {
	
	Chat chat = new Chat(this.userName, this.passwd, this.out, message, message.getisPrivate() );
	
	if(message.getisPrivate() == true) {
		
		try {
			File f = new File("confid/piratefriend.jks");
			
			
			if(f.createNewFile()) {
				
				System.out.println("file piratefriend List created");
				
				KeyStore k = KeyStore.getInstance("jks");
				
				
				k.load(null, passwd.toCharArray());
				
				k.store(new FileOutputStream(f), passwd.toCharArray());
				
				
				
			}else {
				
				System.out.println("piratefriend not created");
			}
			
			KeyStore skk = KeyStore.getInstance("jks");
			
			skk.load(new FileInputStream("confid/piratefriend.jks"), passwd.toCharArray());
			
			String m = ">"+message.getTo();
			
			
									
			skk.load(new FileInputStream(f), passwd.toCharArray());
			
			System.out.print(skk.containsAlias(m));
			
			if(skk.containsAlias(m)) {
				System.out.println("sending msg directly...\n");
				
				chat.chatSendMessage(message);
			}else {
				
				System.out.println("creating private env...\n");
				
				Thread ht = new Thread() {
					
					public void run() {
						
						
						chat.chatPrivateModeInit();
					}
				};
				
				
					Thread th = new Thread() {
					
					@Override 
					
					public void run() {
						
						try {
							Thread.sleep(300);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						chat.chatSendMessage(message);
					}
				};
				
				ht.start();
				
				ht.join();
				
		
			
				
				th.start();
				
				
			}
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	}else if(message.getisPrivate() == false) {
		
		chat.chatSendMessage(message);
		
		
	}else {
		
		System.err.println("chat mode not indicated");
	}
}

public boolean CreatGroup()  {
	
	boolean is = false;
	
	try {
		out.writeUTF("gay");
		out.flush();
		
		is = true;
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	return is;
}
}

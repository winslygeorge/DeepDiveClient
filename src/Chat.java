import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Date;

import javax.crypto.SecretKey;

public class Chat {
	
	protected ObjectOutputStream o;
	
	protected Message mess;
	
	private boolean isPrivate;
	
	protected String pass; 
	
	protected String user;
	
	public Chat (String us,String passwd, ObjectOutputStream out, Message message, boolean isprivatechat) {
		
		this.pass  = passwd;
		
		this.user = us;
		
		this.setPrivate(isprivatechat);
		
		this.o = out;
		this.mess = message;
	}
	
	

	public void setPrivate(boolean i) {
		
		this.isPrivate = i;
		
	}
	
	public boolean getIsPrivate() {
		
		return this.isPrivate;
	}
	
	public boolean chatSendMessage(Message message) {
		boolean is = false;
		try {

			if(message.getApplicationType().equalsIgnoreCase("app")){


				if(this.getIsPrivate()== false) {

					MessageSecurity security = new MessageSecurity(user, pass);

					security.setPublicMode();

					String smsg = security.encryptData(mess.getMessage());

					System.out.println(mess.getMessage());

					o.writeObject(new Message(message.getApplicationType(), message.getGroupClient(), message.getType(),message.getFrom(), message.getTo(),smsg, false));
					o.flush();

				}else if(this.getIsPrivate()== true) {

					System.out.println(message.getFrom()+" : "+ message.getTo()+ message.getMessage());

					MessageSecurity security = new MessageSecurity(user, pass);

					security.setIsprivate(true);

					String ecns = security.encryptData(message.getMessage());

					System.out.println("kk");

					String st = security.encryptSymmetricKey(message.getTo(), security.getSks());


					o.writeObject(new Message(message.getType(),message.getFrom(), message.getTo(), security.signMessage(ecns), ecns , true, st));
					o.flush();

					System.out.println(message.getTo()+" : " + new Date() + " : "+ message.getMessage());

				}else {

					System.out.println("chat mode not indicated");
				}

			}else{

				System.out.println("message on web mode : "+  message.getMessage());

				o.writeObject(message);
				o.flush();


			}
			

		
			
			is = true;
		} catch (IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		}
		return is;
		
	}
	
	public SecretKey getAsymKey(String p) {
		
		GetAccess access = new GetAccess(this.user, pass);

		
		SecretKey sk = access.getAsymmetrickey();
		
		return sk;
		
	}
	
	public void chatPrivateModeInit() {
		
		if(this.getIsPrivate()) {

		try {
			o.writeObject(new Message(mess.getApplicationType(),"privatechat", mess.getType(), mess.getFrom(), mess.getTo(), ">>requestprivate##", false));
			o.flush();
			o.writeObject(new Message(mess.getFrom(), mess.getTo(), new GetAccess(this.user, pass).getAccessCertification("client")));
			o.flush();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		}
		
	}}
}
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * 
 */

/**
 * @author georgos7
 *
 */
public class MessageHandler implements MsgInterface{
	
	private Message message;
	
	protected String u;
	protected String p;
	protected ObjectOutputStream out;

	public MessageHandler(String user, String passwd,Message str, ObjectOutputStream out) {
		
		this.p = passwd;
		this.u = user;
		this.out = out;
	
	this.setMessage(str);
	}

	public void setMessage( Message str) {
		this.message =  str;
	}

	@Override
	public void HandleMessage() {
		// TODO Auto-generated method stub
		
		
		
		Message msg = this.message;
		
		if(msg.getisPrivate()==false) {
			
			if(msg.getType().matches("token")) {
				
				
				System.out.println(msg.getMessage());
				
			}else {
				

				MessageSecurity security = new MessageSecurity(u, p);
				
				security.setPublicMode();
				
				String smsg = security.decryptData(msg.getMessage(), null);
						
				System.out.println(msg.getCurrentTime()+" : "+msg.getFrom()+" : "+ smsg);
				
			}
			
			
		}else if(msg.getisPrivate()== true) {
			
			MessageSecurity security = new MessageSecurity(msg.getFrom(),u, p, msg.getKeyString());
			
			security.setIsprivate(true);
			
			
			String st = security.decryptData(msg.getMessage(), msg.getSignature());
			
			System.out.println( msg.getFrom()+ " : "+ st);
			
		}else {
			
			System.out.println("chat mode was not indicated");
		}

		
	}
	
	public void  reply(String ms, Message pevMs) {
		
		Chat chat = new Chat(u, p,out,new Message("privatechat", "text", pevMs.to, pevMs.from, ms, false ), false);
	}
	

}

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
	protected String applicationType ;

	public MessageHandler(String applicationType,String user, String passwd,Message str, ObjectOutputStream out) {
		this.applicationType = applicationType;
		this.p = passwd;
		this.u = user;
		this.out = out;
	
	this.setMessage(str);
	}

	public void setMessage( Message str) {
		this.message =  str;
	}

	@Override
	public Message HandleMessage() {
		// TODO Auto-generated method stub
		
		
		
		Message msg = this.message;
		
		if(!msg.getisPrivate()) {
			
			if(msg.getType().matches("token")) {
				
				
				System.out.println(msg.getMessage());
				
			}else {
				

				if(this.applicationType.equalsIgnoreCase("app")){

					MessageSecurity security = new MessageSecurity(u, p);

					security.setPublicMode();

					String smsg = security.decryptData(msg.getMessage(), null);

					msg.setMessage(smsg);

					System.out.println(msg.getCurrentTime()+" : "+msg.getFrom()+" : "+ smsg);

				}else{

//					MessageSecurity security = new MessageSecurity(u, p);

//					security.setPublicMode();

//					String smsg = security.decryptData(msg.getMessage(), null);

//					msg.setMessage(smsg);

					System.out.println(msg.getCurrentTime()+" : "+msg.getFrom()+" : "+ msg.getMessage());
				}

				
			}
			
			
		}else if(msg.getisPrivate() && this.applicationType.equalsIgnoreCase("app")) {
			
			MessageSecurity security = new MessageSecurity(msg.getFrom(),u, p, msg.getKeyString());
			
			security.setIsprivate(true);
			
			
			String st = security.decryptData(msg.getMessage(), msg.getSignature());
			
			System.out.println( msg.getFrom()+ " : "+ st);
			
		}else {
			
			System.err.println("chat mode was not indicated");
		}

		return msg;
	}
	
	public void  reply(String ms, Message pevMs) {
		
		Chat chat = new Chat(u, p,out,new Message(this.applicationType,"privatechat", "text", pevMs.to, pevMs.from, ms, false ), false);
	}
	

}

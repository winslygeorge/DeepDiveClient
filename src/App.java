import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 
 */

/**
 * @author georgos7
 *
 */
public class App{

	/**
	 * @param args
	 * @throws  
	 * @throws
	 */
	
	static Scanner scan= null;
	protected static String user;
	static Scanner scanner = new Scanner(System.in);

	private Queue<Message> msgq = new LinkedList<>();
	private MessageHandler messageHandler;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println("Enter username : ");


String username = scanner.nextLine();

System.out.println("Enter Password : ");

String passwd = scanner.nextLine();
	
	DiveClient client = new DiveClient("app", "localhost", 3030,username, passwd );
	
	ExecutorService exe = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
	
	
	
	exe.submit(()->{
		
		client.runClient((message -> System.out.println(message.getMessage())));
		
	} );



exe.submit(()->{
		
	if(client.getClient().isConnected()) {
		while(true) {

			System.out.println("ApplicationType : ");

			String applicationType = scanner.nextLine();
			
			
			System.out.println("Type : ");
			
			String typ = scanner.nextLine();
			
			System.out.println("To : ");
			String friend =  scanner.nextLine();
			
			System.out.println("Msg : ");
			
			String msg = scanner.nextLine();
			
			client.Chat(new Message(applicationType,typ, "text", username, friend, msg,false));
		}
		

	}
});

	}



	
	

}
import java.net.InetAddress;


public class Client {

	public String name;
	public InetAddress address;
	public int port;
	public final int ID;
	public int attempt = 0;
	
	public Client(){
		this.ID = 0;
	}
	
	public Client (String name, InetAddress address, int port, final int ID){
		this.name = name;
		this.address = address;
		this.port = port;
		this.ID = ID;
	}
	
	public int getID(){
		return ID;
		
	}
}

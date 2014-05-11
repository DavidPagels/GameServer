import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;



public class GameServer implements Runnable{

	private List<Client> clients = new ArrayList<Client>();
	
	private DatagramSocket socket;
	private int port;
	private boolean running = false;
	private Thread run, manage, send, receive;
	private UniqueRandID ID;
	
	public GameServer (int port) throws SocketException {
		this.port = port;
		ID = new UniqueRandID();
		socket = new DatagramSocket(this.port);
		run = new Thread(this, "Server");
		run.start();
	} 

	public static void main(String [] args) throws SocketException{
		int port = 31336;
		if (args.length >= 1)
			port = Integer.parseInt(args[0]);
		System.out.println("Starting on Port: " + port);
		new GameServer(port);
	}

	public void run() {
		running = true;
		manageClients();
		receive();
	}
	
	private void manageClients(){
		manage = new Thread("Manage"){
			public void run() {
				while(running){
					
				}
			}
		};
		manage.start();
	}
	
	private String receive(){
		receive = new Thread("Receive"){
			public void run () {
				while(running) {
					byte [] data = new byte[1024];
					DatagramPacket packet = new DatagramPacket(data, data.length);
					try {
						socket.receive(packet);
					} catch (IOException e) {
						e.printStackTrace();
					}
					process(packet);
					
				}
			}
		};
		receive.start();
		return new String();
	}
	
	private void process(DatagramPacket packet){
		String packetString = new String(packet.getData());
		String[] type = packetString.split("/");
		
		if(type[0].equals("connect")){
			
			int thisId = ID.generateID();
			clients.add(new Client(type[1], packet.getAddress(), packet.getPort(), thisId));
			
			String responseText = "createSuccess/" + thisId + "/"; 
			send(responseText.getBytes(), packet.getAddress(), packet.getPort());
			
			String otherResponse = "addPlayer/"  + thisId + "/"; 
			sendToAll(otherResponse);
			
			System.out.println("Name: " + type[1] + ", ID: " + thisId);
			
		}
		
		else if (type[0].equals("move")){
			sendToAll(packetString);
		}
		else if (type[0].equals("message")){
			
			boolean found = false;
			Client c = new Client();
			int idToFind = Integer.parseInt(type[1]);
			int i = 0;
			while(!found && i < clients.size()){
				if(clients.get(i).ID == idToFind){
					c = clients.get(i);
					found = true;
				}
				i++;
			}
			packetString = type[0] + "/" + c.name + "/" + type[2] + "/";
			sendToAll(packetString);
		}
	}
	
	private void send(final byte [] data, final InetAddress address, final int port){
		send = new Thread("Send"){
			public void run () {
				DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
				try {
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		send.start();
	}
	
	private void sendToAll(String packet){
		for (Client c: clients){
			send(packet.getBytes(), c.address, c.port);
		}
	}
}


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Properties;
/**
 * This the is the Peer Node implementation class which acts as the main thread for the whole application
 * This defines and sets the first peer information join request and performs the proper handling of all the join operations.
 * This is the class which contact the Bootstrap node server for communication and setting up a CAN protocol.
 * @author Vinayak
 *
 */
public class PeerNodeImplementation implements PeerNodeSupport {
	
	private DatagramSocket datagramSocket = null;
	private DatagramPacket datagramPacket = null;
	private byte [] buffer_send;
	private InetAddress inetAddress;
	static Properties properties = null;
	private BufferedReader bufferedReader = null;
	private PeerInformation peerInformation = null;
	static LoggerDebug loggerDebug = null;
	private ReadWriteObject readWriteObject = null;
	Integer peerPort = null;
	String peerName = null;
	
	/**
	 * A peer node implementation parameterized constructor which performs the task of accepting the peer portnumber and peer name then assigns it to the peerinformatin which will act upon
	 * the port number and the name that has been passed to it.
	 * @param peerPort_In
	 * @param peerName_In
	 */
	public PeerNodeImplementation(InetAddress ipaddress, Integer peerPort_In, String peerName_In) {
		properties = Property.getProperty();
		buffer_send = new byte[Integer.parseInt(properties.getProperty("bufferSize"))];
		bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		loggerDebug = LoggerDebug.getInstance();
		readWriteObject = new ReadWriteObject();
		peerInformation = new PeerInformation();
		try {
			peerInformation.peerAddress = ipaddress;
			peerPort = peerPort_In;
			datagramSocket = new DatagramSocket(peerPort_In,peerInformation.peerAddress);
			peerName = peerName_In;
			peerInformation.peerPort = peerPort_In;
			peerInformation.peerName = peerName_In;
			System.out.println("Enter Boot Strap Ip address: ");
			peerInformation.bootStrapIPAddress = InetAddress.getByName(bufferedReader.readLine());
			peerInformation.bootStrapPortNumber = Integer.parseInt(properties.getProperty("socketPort"));
			// A random value or the co ordiantes has been assigned so that the splitting can be done properly.
			peerInformation.randomX= Double.valueOf(String.format("%.2f", Math.random()*10));
			peerInformation.randomY =Double.valueOf(String.format("%.2f", Math.random()*10));
            
		} catch (UnknownHostException e) {
			System.err.println("The unknow host exception has occurred in the Peer node implementation constructor.");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("The IOException has occurred in the Peer node implementation constructor.");
			System.exit(1);
			
		}
	}
	
	/**
	 * This is the first operation which is called whenever a join operation is being called up on the bootstrap server hence there will be no confusion
	 * about who will act as the active node.
	 *@param byte[] buffer_send_Operation 
	 */
	public void joinFirstOperation(byte[] buffer_send_Operation) {
		PacketInformation nodeDescriptionMessage = new PacketInformation();
		nodeDescriptionMessage.peerInformation_randomX = peerInformation.getRandomX();
		nodeDescriptionMessage.peerInformation_randomY = peerInformation.getRandomY();
		try {
		 	
   		  inetAddress = InetAddress.getByName(InetAddress.getLocalHost().getHostAddress());
		  nodeDescriptionMessage.messageEntry = properties.getProperty("joinOperation");
		  nodeDescriptionMessage.peerInformation_peerAddress = inetAddress;
		  nodeDescriptionMessage.peerInformation_peerName = peerName;
		  nodeDescriptionMessage.peerInformation_peerPort = peerPort;
		  
		  byte[] destination = readWriteObject.serialize(nodeDescriptionMessage);
		  DatagramPacket datagramPacket = new DatagramPacket (destination,
				  									destination.length,
                                                 peerInformation.bootStrapIPAddress,
                                                 peerInformation.bootStrapPortNumber);
		  
         datagramSocket.send(datagramPacket);
        } catch (SocketException e) {
        	System.err.println("The SocketException has occurred in the Peer node implementation joinFirstOperation function.");
			System.exit(1);
			
		} catch (UnknownHostException e) {
			System.err.println("The unknow host exception has occurred in the Peer node implementation joinFirstOperation function.");
			System.exit(1);
			
		} catch (IOException e) {
			System.err.println("The IOException has occurred in the Peer node implementation joinFirstOperation function.");
			System.exit(1);
			
		}
	}
	
	/**
	 * This is the main function which actiavtes the PeerNodeStarterThread class an then passes the datagram socket and the peer information.
	 */
	public void startPeerNodeImplementation() {
		try {
			
			PeerNodeStarterThread peerNodeStarterThread = new PeerNodeStarterThread(datagramSocket, peerInformation);
			  new Thread(peerNodeStarterThread).start();
				/**
				 * The switch case for different operation which has been attached to this file.
				 */
			  for(;;) {	  
				  	System.out.println("1.Join put string \"join\" ");
				    System.out.println("2.Insert File put string \"insert\" ");
					System.out.println("3.Search File put string \"search\" ");
					System.out.println("4.View Neighbours put string \"view\" ");
					System.out.println("5.View information of Peer by ip put string \"viewinfo\" ");
					System.out.println("6.Exit put string \"exit\" \n");
				  System.out.println("Enter Operation to Perform : ");
				  buffer_send = bufferedReader.readLine().getBytes();
				  switch(new String(buffer_send)) {
				  case "join":
					  joinFirstOperation(buffer_send);
				  break;
				  case "insert":
					  peerNodeStarterThread.insert();
				  break;
				  case "search":
					  peerNodeStarterThread.search();
				  break;
				  case "view":
					  //
					  break;
				  case "viewinfo":
					  peerNodeStarterThread.viewInformation();
					  break;
				  case "exit":
					  System.exit(1);
				  break;
				  default:
						System.out.println("Please enter correct option as defined in put string operation");
					}
				  }
		} catch (IOException e) {
			System.err.println("The IOException has occurred in the Peer node implementation startPeerNodeImplementation function.");
			System.exit(1);
			
		} 
	}

	

	public DatagramPacket getDatagramPacket() {
		return datagramPacket;
	}

	public void setDatagramPacket(DatagramPacket datagramPacket) {
		this.datagramPacket = datagramPacket;
	}
}


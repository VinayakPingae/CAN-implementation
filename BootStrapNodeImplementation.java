

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;
/**
 * This is the boot strap implementation class which performs the task of initiating the BootStrapNodeImplementation Node.
 * and on recieving the active node class request from the active node it will allow him to join the CAN node depending upon
 * the coordinates that he has provided and depending upon the position where the corodiantes lies.
 * @author Vinayak
 *
 */
public class BootStrapNodeImplementation implements BootStrapNodeSupport, Runnable{
	
	public static boolean debug = true;
	private byte[] buffer;
	private byte[] buffer_send;
	private DatagramPacket datagramPacket = null;
	private DatagramSocket datagramSocket = null;
	private InetAddress inetAddress = null;
	private List<PeerNode> tableEntries = null;
	private int countActiveNodes = 0;
	static Properties properties = null;
	private ReadWriteObject readwriteObject = null;
	private PacketInformation packetInformation = null;
	PeerNode peerNode = null;
	public static void debug( String message) {
		if(debug) {
			System.out.println(message);
		}
	}
	
	/**
	 * Initialize the read write object which will be used for the task of serializing and de serialzing the packet information	
	 * @param readwriteObject_In
	 */
	public BootStrapNodeImplementation(ReadWriteObject readwriteObject_In) {
		readwriteObject = readwriteObject_In;
		properties = Property.getProperty();
		buffer = new byte[Integer.parseInt(properties.getProperty("bufferSize"))];
		buffer_send = new byte[Integer.parseInt(properties.getProperty("bufferSize"))];
		datagramPacket = new DatagramPacket(buffer, buffer.length);
		packetInformation = new PacketInformation();
		countActiveNodes = 0;
		
	}

	@Override
	public void run() {
		try {
			inetAddress = InetAddress.getByName(InetAddress.getLocalHost().getHostAddress());
			System.out.println("Boot Strap IP Address : "+inetAddress);
			datagramSocket = new DatagramSocket(Integer.parseInt(properties.getProperty("socketPort")), inetAddress);
			tableEntries = new CopyOnWriteArrayList<PeerNode>();
			while(true) {
				
				datagramSocket.receive(datagramPacket);
				packetInformation = (PacketInformation) readwriteObject.deserialize(buffer);
				
				PacketInformation packetInformation_new = new PacketInformation();
				// Check whether the node count is not present then send the first node as all of the coordinate system 
				//belongs to you and then perform the required operation
				
				if(countActiveNodes == 0) {
					packetInformation_new.messageEntry = properties.getProperty("firstEntryResponse");
					packetInformation_new.responseEntry = properties.getProperty("joinOperation");		
				
				} else {
					// Second time we are not sending any message entry for the data gram packet
					packetInformation_new.messageEntry = properties.getProperty("contactNode");
					packetInformation_new.responseEntry = properties.getProperty("JoinRequestToActiveNode");
					PeerNode tempPeerNode = tableEntries.get(Property.randInt(0, tableEntries.size()-1));
					packetInformation_new.bootstrapServerName = tempPeerNode.getName();
					packetInformation_new.bootstrapServerIp = tempPeerNode.getIpAddress();
					packetInformation_new.bootstrapServerPort= tempPeerNode.getPort();
					
				}
				// Add the entries of the active node to the table list so that at a later point of time a randome entry will be picked up
				// from table list and then the action will be performed.
				if(packetInformation.getMessageEntry().equalsIgnoreCase(properties.getProperty("joinOperation"))) {
					peerNode = new PeerNode();
					peerNode.ipAddress = packetInformation.peerInformation_peerAddress;
					peerNode.Name = packetInformation.peerInformation_peerName;
					peerNode.port = packetInformation.peerInformation_peerPort;
					tableEntries.add(peerNode);
					countActiveNodes++;
				}
				// This is the way of serializing the packet information.
				buffer_send = readwriteObject.serialize(packetInformation_new);
				
				DatagramPacket datagramPacket_send = new DatagramPacket(buffer_send,buffer_send.length,datagramPacket.getAddress(),datagramPacket.getPort());
				datagramSocket.send(datagramPacket_send);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
		
	}

	
	
}

import java.net.InetAddress;
import java.net.UnknownHostException;



/**
 * This is the class which has been used for driving the peer node drive the inputs to the PeerDriver class is
 * portnumber and the portname which can provided in a format like this.
 * <portnumber> <peername>
 * @author Vinayak
 *
 */
public class PeerNodeDriver {
	
	public static void main(String[] args) {
		if(args.length!=3) {
			System.err.println("Please provide the arguments properly <Peer port number> <peer Name>");
			System.exit(1);
		}
		try {
			Integer peerPort = Integer.parseInt(args[0]);
			String peerName = args[1];
			InetAddress ip= InetAddress.getByName(args[2]);
			PeerNodeImplementation peerNodeImplementation = new PeerNodeImplementation(ip,peerPort, peerName);
			peerNodeImplementation.startPeerNodeImplementation();
		} catch(NumberFormatException | UnknownHostException numberFormatException) {
			System.err.println("Please provide the integer as the port number");
			System.exit(1);
		}
	}

}
 

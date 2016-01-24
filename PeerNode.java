
import java.io.Serializable;
import java.net.InetAddress;


/**
 * This class is a peer node class which has been used to store the name , IP address and the port after which they will be added to the 
 * Entries list so that the bootstrap server will have a list of nodes which are active in the network.
 * @author Vinayak Subhash Pingale
 * @since September 14th 2015
 * @version 1.0
 */

public class PeerNode implements Serializable {
	
	public String Name;
	public InetAddress ipAddress;
	public Integer port;
	public PeerInformation peerInformation;
	    
		public String getName() {
			return Name;
		}
		public void setName(String name) {
			Name = name;
		}
		public InetAddress getIpAddress() {
			return ipAddress;
		}
		public void setIpAddress(InetAddress ipAddress) {
			this.ipAddress = ipAddress;
		}
		public Integer getPort() {
			return port;
		}
		public void setPort(Integer port) {
			this.port = port;
		}
		@Override
		public String toString() {
			return "PeerNode [Name=" + Name + ", ipAddress=" + ipAddress
					+ ", port=" + port + "]";
		}
}


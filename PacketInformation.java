

import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * This is the class which plays a vital role in the project and it performs the task of being a packet of the datagram socket.
 * As being UDP the packets are transferred over the network and hence such kind of task is being performed by the PackteInformation class.
 * @author Vinayak
 *
 */
public class PacketInformation implements Serializable{
	
	/**
	 * 
	 */
	 static final long serialVersionUID = -5418477554624605274L;
	public String messageEntry = null;
	public  double peerInformation_x1  ;
	public double peerInformation_x2 ;
	public double peerInformation_y1 ;
	public double peerInformation_y2 ;
	public StringBuilder route_Builder = null;
	public String peerInformation_peerName = null;
		public InetAddress peerInformation_peerAddress = null;
	public Integer peerInformation_peerPort = null;
	public InetAddress peerInformation_bootStrapIPAddress = null;
	public Integer peerInformation_bootStrapPortNumber = null;
	public double peerInformation_randomX ;
	public double peerInformation_randomY ;
	public ArrayList<Neighbour> peerInformation_neighbours = null;
	public Hashtable<String, byte[]> files = null;
	public ArrayList<String> peerInformation_files = null;    
	public PeerNode peerNode = null; 
	public InetAddress Neighbour_ipAddress = null;
	public String Neighbour_name = null;
	public Integer Neighbour_port = null;
	public String responseEntry = null;
	public Neighbour neighbour = null;
	public PeerInformation peerInformation = null;
	public InetAddress bootstrapServerIp = null;
	public Integer bootstrapServerPort = null;
	public String bootstrapServerName = null;
	public double x_files ;
	public double y_files ;
	public String searchName;
	public PeerNode getPeerNode() {
		return peerNode;
	}
	
	public void setPeerNode(PeerNode peerNode) {
		this.peerNode = peerNode;
	}

    //public Neighbour neighbour = null;
    public PacketInformation() {
    	peerInformation_neighbours = new ArrayList<Neighbour>();
    	files = new Hashtable<String, byte[]>();
        peerInformation_files = new ArrayList<String>();   
        peerNode = new PeerNode();
        neighbour = new Neighbour();
        peerInformation = new PeerInformation();
        route_Builder = new StringBuilder();
    }
    
	public String getMessageEntry() {
		return messageEntry;
	}
	public void setMessageEntry(String messageEntry) {
		this.messageEntry = messageEntry;
	}
	public double getPeerInformation_x1() {
		return peerInformation_x1;
	}
	public void setPeerInformation_x1(double peerInformation_x1) {
		this.peerInformation_x1 = peerInformation_x1;
	}
	public double getPeerInformation_x2() {
		return peerInformation_x2;
	}
	public void setPeerInformation_x2(double peerInformation_x2) {
		this.peerInformation_x2 = peerInformation_x2;
	}
	public double getPeerInformation_y1() {
		return peerInformation_y1;
	}
	public void setPeerInformation_y1(double peerInformation_y1) {
		this.peerInformation_y1 = peerInformation_y1;
	}
	public double getPeerInformation_y2() {
		return peerInformation_y2;
	}
	public void setPeerInformation_y2(double peerInformation_y2) {
		this.peerInformation_y2 = peerInformation_y2;
	}
	public String getPeerInformation_peerName() {
		return peerInformation_peerName;
	}
	public void setPeerInformation_peerName(String peerInformation_peerName) {
		this.peerInformation_peerName = peerInformation_peerName;
	}
	public InetAddress getPeerInformation_peerAddress() {
		return peerInformation_peerAddress;
	}
	public void setPeerInformation_peerAddress(
			InetAddress peerInformation_peerAddress) {
		this.peerInformation_peerAddress = peerInformation_peerAddress;
	}
	public Integer getPeerInformation_peerPort() {
		return peerInformation_peerPort;
	}
	public void setPeerInformation_peerPort(Integer peerInformation_peerPort) {
		this.peerInformation_peerPort = peerInformation_peerPort;
	}
	public InetAddress getPeerInformation_bootStrapIPAddress() {
		return peerInformation_bootStrapIPAddress;
	}
	public void setPeerInformation_bootStrapIPAddress(
			InetAddress peerInformation_bootStrapIPAddress) {
		this.peerInformation_bootStrapIPAddress = peerInformation_bootStrapIPAddress;
	}
	public Integer getPeerInformation_bootStrapPortNumber() {
		return peerInformation_bootStrapPortNumber;
	}
	public void setPeerInformation_bootStrapPortNumber(
			Integer peerInformation_bootStrapPortNumber) {
		this.peerInformation_bootStrapPortNumber = peerInformation_bootStrapPortNumber;
	}
	public double getPeerInformation_randomX() {
		return peerInformation_randomX;
	}
	public void setPeerInformation_randomX(double peerInformation_randomX) {
		this.peerInformation_randomX = peerInformation_randomX;
	}
	public double getPeerInformation_randomY() {
		return peerInformation_randomY;
	}
	public void setPeerInformation_randomY(double peerInformation_randomY) {
		this.peerInformation_randomY = peerInformation_randomY;
	}
	public ArrayList<Neighbour> getPeerInformation_neighbours() {
		return peerInformation_neighbours;
	}
	public void setPeerInformation_neighbours(
			ArrayList<Neighbour> peerInformation_neighbours) {
		this.peerInformation_neighbours = peerInformation_neighbours;
	}
	public ArrayList<String> getPeerInformation_files() {
		return peerInformation_files;
	}
	public void setPeerInformation_files(ArrayList<String> peerInformation_files) {
		this.peerInformation_files = peerInformation_files;
	}

	public InetAddress getNeighbour_ipAddress() {
		return Neighbour_ipAddress;
	}
	public void setNeighbour_ipAddress(InetAddress neighbour_ipAddress) {
		Neighbour_ipAddress = neighbour_ipAddress;
	}
	public String getNeighbour_name() {
		return Neighbour_name;
	}
	public void setNeighbour_name(String neighbour_name) {
		Neighbour_name = neighbour_name;
	}
	public Integer getNeighbour_port() {
		return Neighbour_port;
	}
	public void setNeighbour_port(Integer neighbour_port) {
		Neighbour_port = neighbour_port;
	}

	public String getResponseEntry() {
		return responseEntry;
	}

	public void setResponseEntry(String responseEntry) {
		this.responseEntry = responseEntry;
	}

	public StringBuilder getRoute_Builder() {
		return route_Builder;
	}

	public void setRoute_Builder(StringBuilder route_Builder) {
		this.route_Builder = route_Builder;
	}

	public Hashtable<String, byte[]> getFiles() {
		return files;
	}

	public void setFiles(Hashtable<String, byte[]> files) {
		this.files = files;
	}

	public Neighbour getNeighbour() {
		return neighbour;
	}

	public void setNeighbour(Neighbour neighbour) {
		this.neighbour = neighbour;
	}

	public PeerInformation getPeerInformation() {
		return peerInformation;
	}

	public void setPeerInformation(PeerInformation peerInformation) {
		this.peerInformation = peerInformation;
	}

	public InetAddress getBootstrapServerIp() {
		return bootstrapServerIp;
	}

	public void setBootstrapServerIp(InetAddress bootstrapServerIp) {
		this.bootstrapServerIp = bootstrapServerIp;
	}

	public Integer getBootstrapServerPort() {
		return bootstrapServerPort;
	}

	public void setBootstrapServerPort(Integer bootstrapServerPort) {
		this.bootstrapServerPort = bootstrapServerPort;
	}

	public String getBootstrapServerName() {
		return bootstrapServerName;
	}

	public void setBootstrapServerName(String bootstrapServerName) {
		this.bootstrapServerName = bootstrapServerName;
	}

	public double getX_files() {
		return x_files;
	}

	public void setX_files(double x_files) {
		this.x_files = x_files;
	}

	public double getY_files() {
		return y_files;
	}

	public void setY_files(double y_files) {
		this.y_files = y_files;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "PacketInformation [messageEntry=" + messageEntry
				+ ", peerInformation_x1=" + peerInformation_x1
				+ ", peerInformation_x2=" + peerInformation_x2
				+ ", peerInformation_y1=" + peerInformation_y1
				+ ", peerInformation_y2=" + peerInformation_y2
				+ ", route_Builder=" + route_Builder
				+ ", peerInformation_peerName=" + peerInformation_peerName
				+ ", peerInformation_peerAddress="
				+ peerInformation_peerAddress + ", peerInformation_peerPort="
				+ peerInformation_peerPort
				+ ", peerInformation_bootStrapIPAddress="
				+ peerInformation_bootStrapIPAddress
				+ ", peerInformation_bootStrapPortNumber="
				+ peerInformation_bootStrapPortNumber
				+ ", peerInformation_randomX=" + peerInformation_randomX
				+ ", peerInformation_randomY=" + peerInformation_randomY
				+ ", peerInformation_neighbours=" + peerInformation_neighbours
				+ ", peerInformation_files=" + peerInformation_files
				+ ", peerNode=" + peerNode + ", Neighbour_ipAddress="
				+ Neighbour_ipAddress + ", Neighbour_name=" + Neighbour_name
				+ ", Neighbour_port=" + Neighbour_port + ", responseEntry="
				+ responseEntry + ", neighbour=" + neighbour
				+ ", peerInformation=" + peerInformation
				+ ", bootstrapServerIp=" + bootstrapServerIp
				+ ", bootstrapServerPort=" + bootstrapServerPort
				+ ", bootstrapServerName=" + bootstrapServerName + "]";
	}


	
}

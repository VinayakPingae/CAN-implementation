

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Hashtable;
import java.util.concurrent.CopyOnWriteArrayList;
/**
 * The Peer INformation class which serves the purpose of playing the main part of the coordinate system of CAN.
 * It activates the CAN system by modifying the x and y coordinates of the respective peers.
 * @author Vinayak
 *
 */
public class PeerInformation implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1898821071118095505L;
	public double x1;
	public double x2;
	public double y1;
	public double y2;
	public double midX;
	public double midY;
	public String peerName;
	public InetAddress peerAddress;
	public Integer peerPort;
	public InetAddress bootStrapIPAddress;
	public Integer bootStrapPortNumber;
	public double randomX;
	public double randomY;
	public CopyOnWriteArrayList<PeerInformation> neighbours;
	public Hashtable<String, byte[]> files_Insert = null;
	public String message_File = null;
	public String message_Name = null;

	public PeerInformation() {
		neighbours = new CopyOnWriteArrayList<PeerInformation>();
		files_Insert = new Hashtable<String, byte[]>();
	}

	public PeerInformation(String peerName, InetAddress ip, Integer port,
			double x1, double y1, double x2, double y2) {
		this.peerName = peerName;
		this.peerAddress = ip;
		this.peerPort = port;
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;

	}

	public double getX1() {
		return x1;
	}

	public void setX1(double x1) {
		this.x1 = x1;
	}

	public double getX2() {
		return x2;
	}

	public void setX2(double x2) {
		this.x2 = x2;
	}

	public double getY1() {
		return y1;
	}

	public void setY1(double y1) {
		this.y1 = y1;
	}

	public double getY2() {
		return y2;
	}

	public void setY2(double y2) {
		this.y2 = y2;
	}

	public String getPeerName() {
		return peerName;
	}

	public void setPeerName(String peerName) {
		this.peerName = peerName;
	}

	public InetAddress getBootStrapIPAddress() {
		return bootStrapIPAddress;
	}

	public CopyOnWriteArrayList<PeerInformation> getNeighbours() {
		return neighbours;
	}

	public void setNeighbours(CopyOnWriteArrayList<PeerInformation> neighbours) {
		this.neighbours = neighbours;
	}

	public Hashtable<String, byte[]> getFiles_Insert() {
		return files_Insert;
	}

	public void setFiles_Insert(Hashtable<String, byte[]> files_Insert) {
		this.files_Insert = files_Insert;
	}

	public String getMessage_File() {
		return message_File;
	}

	public void setMessage_File(String message_File) {
		this.message_File = message_File;
	}

	public String getMessage_Name() {
		return message_Name;
	}

	public void setMessage_Name(String message_Name) {
		this.message_Name = message_Name;
	}

	public void setBootStrapIPAddress(InetAddress bootStrapIPAddress) {
		this.bootStrapIPAddress = bootStrapIPAddress;
	}

	public Integer getBootStrapPortNumber() {
		return bootStrapPortNumber;
	}

	public void setBootStrapPortNumber(Integer bootStrapPortNumber) {
		this.bootStrapPortNumber = bootStrapPortNumber;
	}

	public InetAddress getPeerAddress() {
		return peerAddress;
	}

	public void setPeerAddress(InetAddress peerAddress) {
		this.peerAddress = peerAddress;
	}

	public Integer getPeerPort() {
		return peerPort;
	}

	public void setPeerPort(Integer peerPort) {
		this.peerPort = peerPort;
	}

	public double getRandomX() {
		return randomX;
	}

	public void setRandomX(double randomX) {
		this.randomX = randomX;
	}

	public double getRandomY() {
		return randomY;
	}

	public void setRandomY(double double1) {
		this.randomY = double1;
	}

	public double getMidX() {
		return midX;
	}

	public void setMidX(double midX) {
		this.midX = midX;
	}

	public double getMidY() {
		return midY;
	}

	public void setMidY(double midY) {
		this.midY = midY;
	}

	@Override
	public String toString() {
		return "PeerInformation [\"\n peerName=" + peerName + "\n peerAddress="
				+ peerAddress + "\n peerPort=" + peerPort + "]";
	}

}

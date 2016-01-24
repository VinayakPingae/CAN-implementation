

import java.io.Serializable;
import java.net.InetAddress;

public class Neighbour implements Serializable{
	public double X1;
	public double X2;
	public double Y1;
	public double Y2;
	public InetAddress ipAddress;
	public String name;
	public Integer port;
	public double getX1() {
		return X1;
	}
	public void setX1(double x1) {
		X1 = x1;
	}
	public double getX2() {
		return X2;
	}
	public void setX2(double x2) {
		X2 = x2;
	}
	public double getY1() {
		return Y1;
	}
	public void setY1(double y1) {
		Y1 = y1;
	}
	public double getY2() {
		return Y2;
	}
	public void setY2(double y2) {
		Y2 = y2;
	}
	public InetAddress getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(InetAddress ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
    
}

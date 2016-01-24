

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class PeerNodeStarterThread implements Runnable{
	private DatagramSocket datagramSocket = null;
	private PeerInformation peerInformation = null;
	private byte[] buffer_receive = null;
	private static Properties properties = null;
	private DatagramPacket datagramPacket_recieve = null;
	static LoggerDebug loggerDebug = null;
	private PacketInformation packetInformation = null;
	private PacketInformation packetInformation_receiveDetails = null;
	private ReadWriteObject readWriteObject = null;
	
	public PeerNodeStarterThread(DatagramSocket datagramSocket_In, PeerInformation peerInformation_In) {
		properties = Property.getProperty();
		this.datagramSocket = datagramSocket_In;
		this.peerInformation = peerInformation_In;
		loggerDebug = LoggerDebug.getInstance();
		this.packetInformation = new PacketInformation();
		this.packetInformation_receiveDetails = new PacketInformation(); 
		buffer_receive = new  byte [Integer.parseInt(properties.getProperty("bufferSize"))];
		readWriteObject = new ReadWriteObject();
		
		datagramPacket_recieve = new DatagramPacket (buffer_receive,buffer_receive.length);
	}
	public void join_A_Node() {
		try {
			datagramSocket.receive(datagramPacket_recieve);
			packetInformation_receiveDetails = (PacketInformation) readWriteObject.deserialize(buffer_receive);
			if(packetInformation_receiveDetails.messageEntry.contains("firstActiveNode")) {
				  this.peerInformation.setX1(0.0);
		      	  this.peerInformation.setY1(0.0);
		      	  this.peerInformation.setX2(10.0);
		      	  this.peerInformation.setY2(10.0);
		      	  
		    } else if(packetInformation_receiveDetails.messageEntry.contains(properties.getProperty("contactNode"))){
    
		    	packetInformation.messageEntry = properties.getProperty("JoinRequestToActiveNode");
		    	packetInformation.peerInformation_randomX = peerInformation.getRandomX();
		    	packetInformation.peerInformation_randomY = peerInformation.getRandomY();
		    	packetInformation.peerInformation = new PeerInformation(peerInformation.getPeerName(), 
														    			peerInformation.getPeerAddress(),
														    			peerInformation.getPeerPort(),
														    			peerInformation.getX1(),
														    			peerInformation.getY1(),
														    			peerInformation.getX2(),
														    			peerInformation.getY2());
		    	byte[] destination = readWriteObject.serialize(packetInformation);
        	    datagramSocket.send(new DatagramPacket(destination,
        		   									destination.length,
        		   									packetInformation_receiveDetails.bootstrapServerIp,
        		   									packetInformation_receiveDetails.bootstrapServerPort));
        	
        	
        } else if(packetInformation_receiveDetails.getMessageEntry().contains(properties.getProperty("JoinRequestToActiveNode"))){
        
        	packetInformation_receiveDetails.route_Builder.append(peerInformation.peerName);  	
        	routing_Logic(packetInformation_receiveDetails.peerInformation_randomX,packetInformation_receiveDetails.peerInformation_randomY, this.peerInformation,packetInformation_receiveDetails);
        	
       
        } else if(packetInformation_receiveDetails.getMessageEntry().contains(properties.getProperty("UpdateMeAsNeighbour"))){
        	
        	CopyOnWriteArrayList<PeerInformation> neighboursListFromAdd = this.peerInformation.neighbours;
        	this.peerInformation.neighbours = updateNieghbors(neighboursListFromAdd,packetInformation_receiveDetails);
        	
        	
        } else if(packetInformation_receiveDetails.getMessageEntry().contains(properties.getProperty("RemoveMeFromNeighbour"))){
        	CopyOnWriteArrayList<PeerInformation> neighboursListFromRemove = this.peerInformation.neighbours;
        	this.peerInformation.neighbours = updateNieghbors_Remove(neighboursListFromRemove,packetInformation_receiveDetails);
        	
        } else if(packetInformation_receiveDetails.getMessageEntry().contains("Success")){
        	
        	this.peerInformation.x1 = packetInformation_receiveDetails.peerInformation.getX1();
        	this.peerInformation.x2 = packetInformation_receiveDetails.peerInformation.getX2();
        	this.peerInformation.y1 = packetInformation_receiveDetails.peerInformation.getY1();
        	this.peerInformation.y2 = packetInformation_receiveDetails.peerInformation.getY2();
        	this.peerInformation.peerPort = packetInformation_receiveDetails.peerInformation.getPeerPort();
        	this.peerInformation.peerAddress = packetInformation_receiveDetails.peerInformation.getPeerAddress();
        	this.peerInformation.peerName = packetInformation_receiveDetails.peerInformation.getPeerName();	
        	
        	this.peerInformation.neighbours= packetInformation_receiveDetails.peerInformation.neighbours;
        	
        	
        	for(PeerInformation addlistToPeer: this.peerInformation.neighbours) {
				PacketInformation packetInformation_neighborUpdate = new PacketInformation();
				packetInformation_neighborUpdate.messageEntry ="UpdateMeAsNeighbour";
				packetInformation_neighborUpdate.peerInformation = this.peerInformation;
				byte[] destination = readWriteObject.serialize(packetInformation_neighborUpdate);
		    	datagramSocket.send(new DatagramPacket(destination,
	    		   									destination.length,
	    		   									addlistToPeer.peerAddress,
	    		   									addlistToPeer.peerPort
	    		   									));
			}
        	
			System.out.println("Success Message: Join has been Done Successfully" );
        
        } else if(packetInformation_receiveDetails.getMessageEntry().contains(properties.getProperty("Files_Added"))) {
        	// First take the active node form the peer
        	checkIfInMyZone(packetInformation_receiveDetails,datagramPacket_recieve.getAddress(),datagramPacket_recieve.getPort());
        	
        } else if(packetInformation_receiveDetails.getMessageEntry().contains(properties.getProperty("InMyZone"))) {
        	// If its in my zone
        	performFileTransfer(datagramPacket_recieve.getAddress(),datagramPacket_recieve.getPort());
        	
       } else if(packetInformation_receiveDetails.getMessageEntry().contains(properties.getProperty("FileTransferring"))) {
    	   copyContents(packetInformation_receiveDetails);
    	   
       } else if(packetInformation_receiveDetails.getMessageEntry().contains(properties.getProperty("SearchMe"))) {
    	   checkIfInMyZone_Search(packetInformation_receiveDetails,datagramPacket_recieve.getAddress(),datagramPacket_recieve.getPort());
       }else if(packetInformation_receiveDetails.getMessageEntry().contains(properties.getProperty("SuccessFound"))) {
    	   String token = packetInformation_receiveDetails.searchName;
    	   System.out.print("Found Via :");
    	   System.out.println("-------------->"+token);

       }
			
	} catch (IOException e) {
		e.printStackTrace();
	} catch (ClassNotFoundException e) {
		e.printStackTrace();
	}
	}
	
	private void copyContents(PacketInformation packetInformation_receiveDetails2) {
		this.peerInformation.files_Insert.put(packetInformation_receiveDetails2.peerInformation.message_Name, packetInformation_receiveDetails2.peerInformation.message_File.getBytes());
		 Set<String> keys = this.peerInformation.files_Insert.keySet();
	        for(String key: keys){
	        	System.out.println("key "+key+" value  "+new String(this.peerInformation.files_Insert.get(key)));
	        }
		
		
	}
	private void performFileTransfer(InetAddress peerAddress, Integer peerPort) {
		
		PeerInformation peer = new PeerInformation();
		 Set<String> keys = this.peerInformation.files_Insert.keySet();
	        for(String key: keys){
	        	peer.message_Name = key;
	        	peer.message_File = this.peerInformation.files_Insert.get(key).toString();
	        }
		
	    packetInformation_receiveDetails.peerInformation.message_Name = peer.message_Name;
	    packetInformation_receiveDetails.peerInformation.message_File = peer.message_File;
		packetInformation_receiveDetails.messageEntry="FileTransferring";
		
		byte[] destination = readWriteObject.serialize(packetInformation_receiveDetails);
			try {
				datagramSocket.send(new DatagramPacket(destination,
				   									destination.length,
				   									peerAddress,
				   									peerPort
				   									));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
	public void checkIfInMyZone(PacketInformation packetInformation_receiveDetails, InetAddress address, int port) {
		if(this.peerInformation.x1 < packetInformation_receiveDetails.x_files && this.peerInformation.x2 > packetInformation_receiveDetails.x_files  
				&& this.peerInformation.y1 < packetInformation_receiveDetails.y_files && this.peerInformation.y2 >  packetInformation_receiveDetails.y_files) {
			packetInformation_receiveDetails.messageEntry = "InMyZone";
			byte[] destination = readWriteObject.serialize(packetInformation_receiveDetails);
			
	    	try {
	    		datagramSocket.send(new DatagramPacket(destination,
				   									destination.length,
				   									address,
				   									port
				   									));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			PeerInformation peerInformation_Least = findLeastFromNegibourList(packetInformation_receiveDetails);
			re_route_Files(peerInformation_Least,packetInformation_receiveDetails);
		}
	}
	public void checkIfInMyZone_Search(PacketInformation packetInformation_receiveDetails, InetAddress address, int port) {
		System.out.println(packetInformation_receiveDetails.x_files+"   "+this.packetInformation_receiveDetails.y_files);
		if(this.peerInformation.x1 < packetInformation_receiveDetails.x_files && this.peerInformation.x2 > packetInformation_receiveDetails.x_files  
				&& this.peerInformation.y1 < packetInformation_receiveDetails.y_files && this.peerInformation.y2 >  packetInformation_receiveDetails.y_files) {
			packetInformation_receiveDetails.messageEntry = "SuccessFound";
			packetInformation_receiveDetails.searchName = this.peerInformation.peerName;
			byte[] destination = readWriteObject.serialize(packetInformation_receiveDetails);
			System.out.println(packetInformation_receiveDetails.searchName);
	    	try {
	    		datagramSocket.send(new DatagramPacket(destination,
				   									destination.length,
				   									address,
				   									port
				   									));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			PeerInformation peerInformation_Least = findLeastFromNegibourList(packetInformation_receiveDetails);
			re_route_Files(peerInformation_Least,packetInformation_receiveDetails);
		}
	}
	
	public CopyOnWriteArrayList<PeerInformation> updateNieghbors_Remove(CopyOnWriteArrayList<PeerInformation> neighboursListFromRemove,PacketInformation packetInformation_receiveDetails2) {
		//int indexToRemove = 0;
		
		CopyOnWriteArrayList<PeerInformation> peerINformationUpdated = new CopyOnWriteArrayList<PeerInformation>();
		for(PeerInformation neighborAdd : neighboursListFromRemove ) {
			if(neighborAdd.peerName.equals(packetInformation_receiveDetails2.peerInformation.peerName)) {
			} else {
				peerINformationUpdated.add(neighborAdd);
			}
    	}
		return peerINformationUpdated;
	}
	public CopyOnWriteArrayList<PeerInformation> updateNieghbors(CopyOnWriteArrayList<PeerInformation> neighboursListFromAdd, PacketInformation packetInformation_receiveDetails2) {
		//int indexToRemove = 0;
		CopyOnWriteArrayList<PeerInformation> peerINformationUpdated = new CopyOnWriteArrayList<PeerInformation>();
		for(PeerInformation neighborAdd : neighboursListFromAdd ) {
			if(neighborAdd.peerName.equals(packetInformation_receiveDetails2.peerInformation.peerName)) {
			} else {
				peerINformationUpdated.add(neighborAdd);
			}
    	}
		peerINformationUpdated.add(packetInformation_receiveDetails2.peerInformation);
		return peerINformationUpdated;
	}
	
	
	public PacketInformation setCoordinatesDownRectangle(PacketInformation packetInformation_receiveDetails2, double midY) {
		packetInformation_receiveDetails.peerInformation.x2 = this.peerInformation.x2;
		packetInformation_receiveDetails.peerInformation.x1 = this.peerInformation.x1;
		packetInformation_receiveDetails.peerInformation.y1 = this.peerInformation.y1;
		packetInformation_receiveDetails.peerInformation.y2 = midY;
		peerInformation.y1 = midY;
		return packetInformation_receiveDetails2;
	}
	
	public PacketInformation setCoordinatesUpRectangle(PacketInformation packetInformation_receiveDetails2,double midY) {
		packetInformation_receiveDetails.peerInformation.x2 = this.peerInformation.x2;
		packetInformation_receiveDetails.peerInformation.y2 = this.peerInformation.y2;	
		packetInformation_receiveDetails.peerInformation.x1 = this.peerInformation.x1;
		packetInformation_receiveDetails.peerInformation.y1 = midY;
		peerInformation.y1 = midY;
		return packetInformation_receiveDetails2;
	}
	
	public PacketInformation setCoordinatesLeftSquare(PacketInformation packetInformation_receiveDetails2,double midX) {
		packetInformation_receiveDetails.peerInformation.x1 = this.peerInformation.x1;
		packetInformation_receiveDetails.peerInformation.y1 = this.peerInformation.y1;
		packetInformation_receiveDetails.peerInformation.x2 = midX;
		packetInformation_receiveDetails.peerInformation.y2 = this.peerInformation.y2;
		peerInformation.x1 = midX;
		return packetInformation_receiveDetails2;
	}
	
	public PacketInformation setCoordinatesRightSquare(PacketInformation packetInformation_receiveDetails2,double midX) {
		packetInformation_receiveDetails.peerInformation.x1 = midX;
		packetInformation_receiveDetails.peerInformation.x2 = this.peerInformation.x2;
		packetInformation_receiveDetails.peerInformation.y1 = this.peerInformation.y1;
		packetInformation_receiveDetails.peerInformation.y2 = this.peerInformation.y2;
		peerInformation.x2 = midX;
		return packetInformation_receiveDetails2;
	}
	
	public void routing_Logic(double randomX_in, double randomY_in, PeerInformation peerInformation_In, PacketInformation packetInformation_receiveDetails2) throws IOException {
		
		// random x and random y lies in my zone  [x1> random x < x2 and y1>random y <y2]
		
		if(this.peerInformation.x1 < randomX_in && this.peerInformation.x2 > randomX_in  && this.peerInformation.y1 < randomY_in && this.peerInformation.y2 >  randomY_in) {
			
			double randX = peerInformation_In.getRandomX();
			double randY = peerInformation_In.getRandomY();
			double distanceX = peerInformation.x2-peerInformation.x1;
			double distanceY = peerInformation.y2-peerInformation.y1;
			double midX = medianTangent(peerInformation.x1,peerInformation.x2);
    		double midY = medianTangent(peerInformation.y1,peerInformation.y2);
    		
			if(distanceY > distanceX) {
        		// Rectangle
        		if(randY < midY) {
        			// Down 
        			//System.out.println("In my zone Rectangle Down");
        			setCoordinatesDownRectangle(packetInformation_receiveDetails2,midY);
        			
        		} else  if(randY > midY ) {
        			// Up
        			//System.out.println("In my zone Rectangle Up");
        			setCoordinatesUpRectangle(packetInformation_receiveDetails2,midY);
        				
        		}
        	}else if((this.peerInformation.y2 - this.peerInformation.y1) == (this.peerInformation.x2 - this.peerInformation.x1)) {
        		// Square.
        		//System.out.println("Inside square");
        		if(randX < midX ) {
        			// Left // point will be packet
        			//System.out.println("In my zone Square Left");
        			setCoordinatesLeftSquare(packetInformation_receiveDetails2,midX);
        		} else if(randX > midX ) {
        			// Right
        			//System.out.println("In my zone Square Right");
        			setCoordinatesRightSquare(packetInformation_receiveDetails2,midX);
        		}
        		
        	}
			
			
			CopyOnWriteArrayList<PeerInformation> myPeerList = new CopyOnWriteArrayList<PeerInformation>();
			CopyOnWriteArrayList<PeerInformation> myPacketPeerList = new CopyOnWriteArrayList<PeerInformation>();
			CopyOnWriteArrayList<PeerInformation> removeMeFromList = new CopyOnWriteArrayList<PeerInformation>();
			CopyOnWriteArrayList<PeerInformation> neighbours = new CopyOnWriteArrayList<PeerInformation>();
			neighbours = this.peerInformation.neighbours;
 			
			for(PeerInformation neighbour : neighbours) {
				if(decide_neighbors(neighbour, this.peerInformation) ) {
					// addded to my list
					myPeerList.add(neighbour);
					if(decide_neighbors_Packets(neighbour, this.packetInformation_receiveDetails)) {
							myPacketPeerList.add(neighbour); 
					}	
				} else {
					myPacketPeerList.add(neighbour);
					removeMeFromList.add(neighbour);
				}
			}
			


			myPacketPeerList.add(new PeerInformation(peerInformation.peerName,
					 peerInformation.peerAddress,
					 peerInformation.peerPort,
					 peerInformation.x1,
					 peerInformation.y1,
					 peerInformation.x2,
					 peerInformation.y2));
				// added packetlist to self
			myPeerList.add(packetInformation_receiveDetails.peerInformation);
			this.peerInformation.neighbours = myPeerList;
			packetInformation_receiveDetails.peerInformation.neighbours = myPacketPeerList;
			
			//** Printing of the peer list that has been added to the neighbour list
			
			// Peer Information add to the list
			PeerInformation peerinfo = new PeerInformation(this.peerInformation.peerName, 
														   this.peerInformation.peerAddress, 
														   this.peerInformation.peerPort, 
														   this.peerInformation.x1, 
														   this.peerInformation.y1, 
														   this.peerInformation.x2, 
														   this.peerInformation.y2);
			
			for(PeerInformation addlistToPeer: myPeerList) {
				PacketInformation packetInformation_neighborUpdate = new PacketInformation();
				packetInformation_neighborUpdate.messageEntry ="UpdateMeAsNeighbour";
				packetInformation_neighborUpdate.peerInformation = peerinfo;
				byte[] destination = readWriteObject.serialize(packetInformation_neighborUpdate);
		    	datagramSocket.send(new DatagramPacket(destination,
	    		   									destination.length,
	    		   									addlistToPeer.peerAddress,
	    		   									addlistToPeer.peerPort
	    		   									));
			}
			
			
			
			// Peer Info remove form the list
			PeerInformation peerinfo_remove = new PeerInformation(this.peerInformation.peerName, 
					   this.peerInformation.peerAddress, 
					   this.peerInformation.peerPort, 
					   this.peerInformation.x1, 
					   this.peerInformation.y1, 
					   this.peerInformation.x2, 
					   this.peerInformation.y2);

			for(PeerInformation removeFromPeer: removeMeFromList) {
				
				PacketInformation packetInformation_neighborUpdate = new PacketInformation();
				
				packetInformation_neighborUpdate.messageEntry ="RemoveMeFromNeighbour";
				packetInformation_neighborUpdate.peerInformation = peerinfo_remove;
				
				byte[] destination = readWriteObject.serialize(packetInformation_neighborUpdate);
		    	datagramSocket.send(new DatagramPacket(destination,
	    		   									destination.length,
	    		   									removeFromPeer.peerAddress,
	    		   									removeFromPeer.peerPort
	    		   									));
			}
			
			packetInformation_receiveDetails.messageEntry="Success";
			System.out.println("Success:"+packetInformation_receiveDetails.peerInformation.peerName);
			byte[] destination = readWriteObject.serialize(packetInformation_receiveDetails);
	    	datagramSocket.send(new DatagramPacket(destination,
    		   									destination.length,
    		   									packetInformation_receiveDetails.peerInformation.peerAddress,
    		   									packetInformation_receiveDetails.peerInformation.peerPort
    		   									));

		}
		else {
			packetInformation_receiveDetails2.route_Builder.append(peerInformation_In.peerName);
			PeerInformation peerInformation_Least = findLeastFromNegibourList(packetInformation_receiveDetails2);
			re_route(peerInformation_Least,packetInformation_receiveDetails2);
		}
	}
	
	private boolean decide_neighbors_Packets(PeerInformation neighbour , PacketInformation peerInformation) {
		double deciderX1 = peerInformation.peerInformation.getX1();
		double deciderX2 = peerInformation.peerInformation.getX2();
		double deciderY1 = peerInformation.peerInformation.getY1();
		double deciderY2 = peerInformation.peerInformation.getY2();
		if((deciderY2==neighbour.getY1()) &&
				(deciderX2 < neighbour.getX2()   && deciderX2 > neighbour.getX1() )  || (deciderX1 == neighbour.getX1() && deciderX2 > neighbour.getX2() ) || (deciderX1 == neighbour.getX2()   && deciderX1 < neighbour.getX1() ) ||	(deciderX1 > neighbour.getX1()  && deciderX1 < neighbour.getX2() ) ||
				(deciderX2 < neighbour.getX2()   && deciderX1 == neighbour.getX1() ) || (deciderX2 == neighbour.getX2()    && deciderX1 > neighbour.getX1()) || ( deciderX2 > neighbour.getX2()   && deciderX1 < neighbour.getX1()  ) || (deciderX1 == neighbour.getX1() && deciderX2 == neighbour.getX2()) ||
				(deciderX2 < neighbour.getX2()  && deciderX1 > neighbour.getX1()) )  {
					
				return true;
			
		}// Else if the current node lies above the neighbor node 
		else if ((deciderY1 == neighbour.getY2()) && (deciderX2 == neighbour.getX2() && deciderX1 == neighbour.getX1() ) || (deciderX1 < neighbour.getX1()  && deciderX1 == neighbour.getX2()) || (deciderX1 < neighbour.getX2()  && deciderX1 > neighbour.getX1() )  ||
				(deciderX2 < neighbour.getX2()  && deciderX2 > neighbour.getX1()  ) || (deciderX1 > neighbour.getX1()  && deciderX2 < neighbour.getX2() ) || (deciderX1 == neighbour.getX1() && deciderX2 < neighbour.getX2() ) || (deciderX2 > neighbour.getX2()  && deciderX1 == neighbour.getX1()  ) ||
				(deciderX1 < neighbour.getX1()  && deciderX2 > neighbour.getX2() ) ||	(deciderX2 == neighbour.getX2()  &&  deciderX1 > neighbour.getX1())) {
			
				return true;
			
		}// Else if the point lies in the left 
		else if ((deciderX2 == neighbour.getX1()) && (deciderY1 == neighbour.getY1() && deciderY2 == neighbour.getY2()) || (deciderY1 > neighbour.getY1()  && deciderY1 < neighbour.getY2() ) || (deciderY2 > neighbour.getY1()  && deciderY2 < neighbour.getY2() ) || (deciderY1 == neighbour.getY1() && deciderY2 > neighbour.getY2() ) ||
				(deciderY1 < neighbour.getY1()  && deciderY1 == neighbour.getY2()) || (deciderY1 < neighbour.getY1()  && deciderY2 > neighbour.getY2() ) || (deciderY1 > neighbour.getY1()  && deciderY2 < neighbour.getY2() ) || (deciderY1 == neighbour.y1 && deciderY2 < neighbour.y2 ) || deciderY1 > neighbour.y1  && deciderY2 == neighbour.y2) {
			
				return true;
			
		}// Else if the point lies in the Right
		else if ((deciderX1 == neighbour.getX2()) && (deciderY1 < neighbour.getY1()  && deciderY1 == neighbour.getY2()) || (deciderY1 > neighbour.getY1()  && deciderY1 < neighbour.getY2() ) ||(deciderY1 == neighbour.getY1() && deciderY2 == neighbour.getY2()) ||
				(deciderY1 == neighbour.y1 && deciderY2 > neighbour.y2 ) ||	(deciderY1 < neighbour.y1  && deciderY2 > neighbour.y2 ) ||	(deciderY1 > neighbour.y1  && deciderY2 < neighbour.y2 ) ||	(deciderY2 > neighbour.y1  && deciderY2 < neighbour.y2 ) ||
				(deciderY1 == neighbour.y1 && deciderY2 < neighbour.y2 ) ||	(deciderY1 > neighbour.y1  && deciderY2 == neighbour.y2))  {
			
				return true;
			
		}
		return false;
	}
	
	private boolean decide_neighbors(PeerInformation neighbour , PeerInformation peerInformation) {
		PeerInformation activePeer = this.peerInformation;
		double deciderX1 = activePeer.x1;
		double deciderX2 = activePeer.x2;
		double deciderY1 = activePeer.y1;
		double deciderY2 = activePeer.y2;
		
		if((deciderY2 == neighbour.y1 )&& (deciderX2 > neighbour.x1  && deciderX2 < neighbour.x2  )  || (deciderX1 == neighbour.x1 && deciderX2 > neighbour.x2  ) || (deciderX1 < neighbour.x1  && deciderX1 == neighbour.x2 ) || (deciderX1 > neighbour.x1  && deciderX1 < neighbour.x2  ) ||
				(deciderX1 == neighbour.x1 && deciderX2 < neighbour.x2  ) ||	(deciderX1 > neighbour.x1  && deciderX2 == neighbour.x2 )||	(deciderX1 < neighbour.x1  && deciderX2 > neighbour.x2  )||	(deciderX1 == neighbour.x1 && deciderX2 == neighbour.x2 )||	(deciderX1 > neighbour.x1  && deciderX2 < neighbour.x2) ) {
					
				return true;
			
		}// Else if the current node lies above the neighbor node 
		else if ((deciderY1 == neighbour.y2) && (deciderX1 == neighbour.x1 && deciderX2 == neighbour.x2) || (deciderX1 < neighbour.x1  && deciderX1 == neighbour.x2) || (deciderX1 > neighbour.x1  && deciderX1 < neighbour.x2 ) ||
				(deciderX2 > neighbour.x1  && deciderX2 < neighbour.x2 ) ||	(deciderX1 > neighbour.x1  && deciderX2 < neighbour.x2 ) ||	(deciderX1 == neighbour.x1 && deciderX2 < neighbour.x2 ) ||	(deciderX1 == neighbour.x1 && deciderX2 > neighbour.x2 ) ||	(deciderX1 < neighbour.x1  && deciderX2 > neighbour.x2 ) ||
				(deciderX1 > neighbour.x1  && deciderX2 == neighbour.x2)) {
			
				return true;
			
		}// Else if the point lies in the left 
		else if ((deciderX2 == neighbour.x1) &&	(deciderY1 == neighbour.y1 && deciderY2 == neighbour.y2 )||	(deciderY1 > neighbour.y1  && deciderY1 < neighbour.y2  )||	(deciderY2 > neighbour.y1  && deciderY2 < neighbour.y2  )||
				(deciderY1 == neighbour.y1 && deciderY2 > neighbour.y2  )||	(deciderY1 < neighbour.y1  && deciderY1 == neighbour.y2 )||	(deciderY1 < neighbour.y1  && deciderY2 > neighbour.y2  )||	(deciderY1 > neighbour.y1  && deciderY2 < neighbour.y2 ) ||
				(deciderY1 == neighbour.y1 && deciderY2 < neighbour.y2)  ||	(deciderY1 > neighbour.y1  && deciderY2 == neighbour.y2)) {
			
				return true;
			
		}// Else if the point lies in the Right
		else if ((deciderX1 == neighbour.x2) &&	(deciderY1 < neighbour.y1  && deciderY1 == neighbour.y2) ||	(deciderY1 > neighbour.y1  && deciderY1 < neighbour.y2 ) ||	(deciderY1 == neighbour.y1 && deciderY2 == neighbour.y2) ||
				(deciderY1 == neighbour.y1 && deciderY2 > neighbour.y2 ) ||	(deciderY1 < neighbour.y1  && deciderY2 > neighbour.y2 ) ||	(deciderY1 > neighbour.y1  && deciderY2 < neighbour.y2 ) ||
				(deciderY2 > neighbour.y1  && deciderY2 < neighbour.y2 ) ||	(deciderY1 == neighbour.y1 && deciderY2 < neighbour.y2 ) ||	(deciderY1 > neighbour.y1  && deciderY2 == neighbour.y2))  {
			
				return true;
			
		}
		return false;
	}
	
	
	public void re_route(PeerInformation peerInformation_Least, PacketInformation packetInformation_receiveDetails2) {
		byte[] destination = readWriteObject.serialize(packetInformation_receiveDetails2);
    	try {
			datagramSocket.send(new DatagramPacket(destination,
			   									destination.length,
			   									peerInformation_Least.peerAddress,
			   									peerInformation_Least.peerPort
			   									));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void re_route_Files(PeerInformation peerInformation_Least, PacketInformation packetInformation_receiveDetails2) {
		packetInformation_receiveDetails2.messageEntry="InMyZone";
		byte[] destination = readWriteObject.serialize(packetInformation_receiveDetails2);
    	try {
			datagramSocket.send(new DatagramPacket(destination,
			   									destination.length,
			   									peerInformation_Least.peerAddress,
			   									peerInformation_Least.peerPort
			   									));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public PeerInformation findLeastFromNegibourList(PacketInformation packetInformation_receiveDetails2) {
		
		double midpointX;
		double midpointY;
		double distance;
		
		Map<Double,PeerInformation> leastDistance = new HashMap<Double,PeerInformation>();
		for(PeerInformation neighbour : this.peerInformation.neighbours ) {
			midpointX = (neighbour.x1+neighbour.x2)/2;
			midpointY = (neighbour.y1+neighbour.y2)/2;
			distance = Math.sqrt(Math.pow(packetInformation_receiveDetails2.peerInformation_randomX - midpointX, 2)+ Math.pow(packetInformation_receiveDetails2.peerInformation_randomY -midpointY, 2));
			leastDistance.put(distance,neighbour);
		}
		Map<Double,PeerInformation> treeMap = new TreeMap<Double,PeerInformation>(leastDistance);
		PeerInformation list = null;
		for (Map.Entry<Double,PeerInformation> entry : treeMap.entrySet()) {
		    list = entry.getValue();
		    break;
		}
		return list;
	}
	
	
	@Override
	public void run() {
			for(;;) {
				join_A_Node();
			}
	}
	
	public double medianTangent(double first, double Second) {
		return ((first+Second)/2);
	}
	
	public double medianTangentOpposite(double first, double Second) {
		return ((first-Second)/2);
	}
	
	public void search() {
		String keyword;
		Scanner kbd = new Scanner(System.in);
		System.out.println("Please enter the FileName: ");
		keyword = kbd.nextLine().toLowerCase();
		int x = calculateHashX(keyword);
		int y = calculateHashY(keyword);
		packetInformation_receiveDetails.messageEntry = "SearchMe";
		packetInformation_receiveDetails.x_files = x;
		packetInformation_receiveDetails.y_files = y;
		byte[] destination = readWriteObject.serialize(packetInformation_receiveDetails);
			try {
				datagramSocket.send(new DatagramPacket(destination,
				   									destination.length,
				   									this.peerInformation.peerAddress,
				   									this.peerInformation.peerPort
				   									));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	
	public void insert() {
		String keyword;
		Scanner kbd = new Scanner(System.in);
		System.out.println("Please Insert the Keyword: ");
		keyword = kbd.nextLine().toLowerCase();
		int x = calculateHashX(keyword);
		int y = calculateHashY(keyword);
		fileRead(x,y,keyword);
	}

	public void fileRead(int x, int y, String keyword) {
		try {
			FileInputStream inputStream = new FileInputStream(keyword);
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			int nRead;
			byte[] data = new byte[65508];
			// Check for the content size of the file
			
			while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
			  buffer.write(data, 0, nRead);
			}
			packetInformation_receiveDetails.messageEntry = "Files_Added";
			packetInformation_receiveDetails.x_files = x;
			packetInformation_receiveDetails.y_files = y;
			this.peerInformation.files_Insert.put(keyword, buffer_receive);
			byte[] destination = readWriteObject.serialize(packetInformation_receiveDetails);
			datagramSocket.send(new DatagramPacket(destination,
    		   									destination.length,
    		   									this.peerInformation.peerAddress,
    		   									this.peerInformation.peerPort
    		   									));
			buffer.flush();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
	// This functions are required for the searching and inserting operations
	public static int calculateHashX(String keyword) {
		int sum = 0;

		if (keyword.length() == 1) {
			return 1;
		} else {
			for (int i = 0; i < keyword.length();) {
				sum = sum + keyword.charAt(i);
				i = i + 2;
			}
			return sum % 10;
		}

	}

	public static int calculateHashY(String keyword) {
		int sum = 0;
		if (keyword.length() == 1) {
			return 0;
		} else {
			for (int i = 1; i < keyword.toCharArray().length;) {
				sum = sum + keyword.charAt(i);
				i = i + 2;
			}
			return sum % 10;
		}
	}

	
	public void view() {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < this.peerInformation.neighbours.size(); i++) {
			stringBuilder.append(this.peerInformation.neighbours.get(i).peerAddress);
			stringBuilder.append(System.getProperty("line.separator"));
		}
		System.out.println(stringBuilder.toString());
	}

	
	public void viewInformation() {
		System.out.println("[x1 ="+this.peerInformation.x1+"]");
		System.out.println("[y1 ="+this.peerInformation.y1+"]");
		System.out.println("[x2 ="+this.peerInformation.x2+"]");
		System.out.println("[y2 ="+this.peerInformation.y2+"]");
		System.out.println("[peerPort ="+this.peerInformation.peerPort+"]");
		System.out.println("[peerAddress ="+this.peerInformation.peerAddress+"]");
		System.out.println("[peerName ="+this.peerInformation.peerName+"]");
		for(PeerInformation neighbour : this.peerInformation.neighbours) {
			System.out.println("-------Neigbour-----------");
			System.out.println("[neighbour.peerName ="+neighbour.peerName+"]");
			System.out.println("[neighbour.x1 ="+neighbour.x1+"]");
			System.out.println("[neighbour.y1 ="+neighbour.y1+"]");
			System.out.println("[neighbour.x2 ="+neighbour.x2+"]");
			System.out.println("[neighbour.y2 ="+neighbour.y2+"]");
		}
		
	}
	public DatagramSocket getDatagramSocket() {
		return datagramSocket;
	}
	public void setDatagramSocket(DatagramSocket datagramSocket) {
		this.datagramSocket = datagramSocket;
	}
	public PeerInformation getPeerInformation() {
		return peerInformation;
	}
	public void setPeerInformation(PeerInformation peerInformation) {
		this.peerInformation = peerInformation;
	}
	
}

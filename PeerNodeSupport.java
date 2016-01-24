

public interface PeerNodeSupport {
	/**
	 * The first function startPeerNodeImplementation which will be called and it will get the operation which the nodes want to perform with the Bootstrap node
	 * his function creates a reads the operation and then gives the call to  joinFirstOperation(String ) specified by the user. 
	 */
	
	public void startPeerNodeImplementation();
	/**
	 * This function accepts the parameter of byte type which is usually a join string and after that datagram socket is prepared and the datagram packet is also preprared
	 * and a string which is got from the server is then processed in this particular function
	 * @param buffer_send
	 */
	public void joinFirstOperation(byte[] buffer_send);
}

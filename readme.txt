Name - Vinayak Subhash Pingale
Bmail - vpingal1@binghamton.edu

LANGUAGE USED : JAVA

PERCENT COMPLETE:

I have an incomplete implementation of CAN with following operation


Design and Implementation details -

When a node joins, if its zone is a square, then split the zone vertically (this will give the node a rectangle zone). If the zone is a rectangle with height greater than width, split the zone horizontally (this will give the node
a square zone). When a node leaves, search its neighbors and merge its zone with a neighbor zone if that merge creates a rectangle or square zone. Otherwise, the neighbor with the smallest zone owns the zone of the departing node temporarily.

** I have used Datagram Socket for sending packets between port of bootstrap and between active nodes in the network.


JOIN Operation - 

In join operation, when the peer asks with the Peer Port and the Peer address for the conncetion 
Design of Join Operation works in this way : -

A bootstrap server node which activates the first datagram packet in the network and sends it back to the one who requested it first.

Whenever a new peer wants to join the network at that time the peer sends a datagram packet saying i need to join the network for the cause of creating a CAN network then the bootstrap tells the active node which sends the request saying you are the first node in the network who have contacted me for creating a CAN network, so the first task of this is to create a packet with the first active node message and then adding that peer information to the active node list. whenever the node is contacted then at that time boot strap server can provide any random active node to get contacted and then perform the splitting of the node.
After getting the active node as it is first in the network the node will set the cordiante as (0,0) and (10,10) so suppose A enters the system then A will be assigned the whole 10 coordinate system, which is a square by default.
When the second active node enters the system the splitting logic takes place in which it determines whether the zone is square or a rectangle depending on that logic of determining whether the random x and random y that has been passed in the datagram packet.
Suppose the random x and random y are not part of my zone then at that time re routing logic takes place in the system, the main task of re routing logic is to find the least distance node form the random x and random y and pass the request to that particular node and hence the same logic will repeat asking the nodes to determine whether they split the zone and after splitting zone turns out to be square or rectangle.

INSERT OPERATION -

Calculate the hash function for determining the coordinates from the file name that has been passed then use the same logic as we have used in join to insert the data in the file contents.
As i am transferring the data in datagram packets henceforth there is some limitation to the size of the file that can be transferred. 
This is working only for the two nodes and hence the nodes will determine that the data has been inserted in the files

SEARCH OPERATION -
Will give the node and the success message of finding at that particular node.

VIEW OPERATION -
viewinfo() is the function which show the information of the current peer.
No implementation of the view "name" has been done.


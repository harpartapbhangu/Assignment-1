import java.io.*;
import java.net.*;

public class IntermediateHost {
	DatagramSocket recieveSocket;
	DatagramSocket sendReceiveSocket;
	DatagramPacket sendPacket, receivePacket;
	
	public IntermediateHost() {
		try {
	         // Construct a datagram socket and bind it to any available 
	         // port on the local host machine. This socket will be used to
	         // send UDP Datagram packets.
			 
			sendReceiveSocket = new DatagramSocket();

	         // Construct a datagram socket and bind it to port 5000 
	         // on the local host machine. This socket will be used to
	         // receive UDP Datagram packets.
	         
			recieveSocket = new DatagramSocket(23);
	         // to test socket timeout (2 seconds)
	         //receiveSocket.setSoTimeout(2000);
	      } catch (SocketException se) {
	         se.printStackTrace();
	         System.exit(1);
	      } 
	   }
	
	 public static void main( String args[] )
	   {
		 IntermediateHost a = new IntermediateHost();
	      a.receiveAndSend();
	   }
	
	public void receiveAndSend()
	   {
	      // Construct a DatagramPacket for receiving packets up 
	      // to 100 bytes long (the length of the byte array).
	      byte data[] = new byte[100];
	      receivePacket = new DatagramPacket(data, data.length);
	      System.out.println("Intermediate Host: Waiting for Packet.\n");

	      // Block until a datagram packet is received from receiveSocket.
	      try {        
	         System.out.println("Waiting..."); // so we know we're waiting
	         recieveSocket.receive(receivePacket);
	      } catch (IOException e) {
	         System.out.print("IO Exception: likely:");
	         System.out.println("Receive Socket Timed Out.\n" + e);
	         e.printStackTrace();
	         System.exit(1);
	      }

	      // Process the received datagram.
	      System.out.println("Intermediate Host: Packet received:");
	      System.out.println("From Client: " + receivePacket.getAddress());
	      System.out.println("Client port: " + receivePacket.getPort());
	      int len = receivePacket.getLength();
	      System.out.println("Length: " + len);
	      System.out.print("Containing: " );

	      // Form a String from the byte array.
	      String received = new String(receivePacket.getData(),0,len);   
	      System.out.println(received + "\n");
	      
	      // Slow things down (wait 5 seconds)
	      try {
	          Thread.sleep(23);
	      } catch (InterruptedException e ) {
	          e.printStackTrace();
	          System.exit(1);
	      }
	 
	      // Create a new datagram packet containing the string received from the client.

	      // Construct a datagram packet that is to be sent to a specified port 
	      // on a specified host.
	      // The arguments are:
	      //  data - the packet data (a byte array). This is the packet data
	      //         that was received from the client.
	      //  receivePacket.getLength() - the length of the packet data.
	      //    Since we are echoing the received packet, this is the length 
	      //    of the received packet's data. 
	      //    This value is <= data.length (the length of the byte array).
	      //  receivePacket.getAddress() - the Internet address of the 
	      //     destination host. Since we want to send a packet back to the 
	      //     client, we extract the address of the machine where the
	      //     client is running from the datagram that was sent to us by 
	      //     the client.
	      //  receivePacket.getPort() - the destination port number on the 
	      //     destination host where the client is running. The client
	      //     sends and receives datagrams through the same socket/port,
	      //     so we extract the port that the client used to send us the
	      //     datagram, and use that as the destination port for the echoed
	      //     packet.

	      sendPacket = new DatagramPacket(receivePacket.getData(), receivePacket.getLength(),
	                               receivePacket.getAddress(), 69);

	      System.out.println( "Intermediate Host: Sending packet:");
	      System.out.println("To Server: " + sendPacket.getAddress());
	      System.out.println("Destination host port: " + sendPacket.getPort());
	      len = sendPacket.getLength();
	      System.out.println("Length: " + len);
	      System.out.print("Containing: ");
	      System.out.println(new String(sendPacket.getData(),0,len));
	      // or (as we should be sending back the same thing)
	      // System.out.println(received); 
	        
	      // Send the datagram packet to the client via the send socket. 
	      try {
	         sendReceiveSocket.send(sendPacket);
	      } catch (IOException e) {
	         e.printStackTrace();
	         System.exit(1);
	      }

	      System.out.println("Intermediate Host: packet sent");

	      // We're finished, so close the sockets.
		
	      sendReceiveSocket.close();
	      recieveSocket.close();
	   
	   }
	
}

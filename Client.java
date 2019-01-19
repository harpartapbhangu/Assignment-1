import java.io.*;
import java.net.*;

public class Client {
	   DatagramPacket sendPacket, recievePacket;
	   DatagramSocket sendReceiveSocket;
	   int dataLength = 0;
	   
	public Client() {
		 try {
	         // Construct a datagram socket and bind it to any available 
	         // port on the local host machine. This socket will be used to
			 
	         // send and receive UDP Datagram packets.
	         sendReceiveSocket = new DatagramSocket();	
	      } catch (SocketException se) {   // Can't create the socket.
	         se.printStackTrace();
	         System.exit(1);
	      }
	}

	public static void main(String[] args) {
		Client p = new Client();
		p.sendAndReceive();
	}

	public byte[] readRequest(String file,String mode) {
		byte[] b = new byte[100];
		b[0]= 0b00000000;
		b[1]= 0b00000001;
		byte[] c = file.getBytes();
		for(int i = 0;i<c.length;i++) {
			b[i+2] = c[i];
		}
		b[2+c.length] = 0b00000000;
		byte[] k = mode.getBytes();
		for(int i = 0;i<k.length;i++) {
			b[i+3+c.length] = k[i];
		}
		b[3+c.length+k.length] = 0b00000000;
		System.out.println("The data contains: " + file);
		dataLength=3+c.length+k.length+1;
		System.out.print("The data contains: ");
		for(int i = 0;i<dataLength-1;i++) {
			System.out.print(b[i]);
		}
		System.out.print(b[dataLength-1]+"\n");
		return b;
	}
	
	public byte[] writeRequest(String file,String mode) {
		byte[] a = new byte[100];
		a[0] = 0b00000000;
		a[1] = 0b00000010;
		byte[] c = file.getBytes();
		for(int i = 0;i<c.length;i++) {
			a[i+2] = c[i];
		}
		a[2+c.length] = 0b00000000;
		byte[] k = mode.getBytes();
		for(int i = 0;i<k.length;i++) {
			a[i+3+c.length] = k[i];
		}
		a[3+c.length+k.length] = 0b00000000;
		System.out.println("The data contains: " + file);
		dataLength=3+c.length+k.length+1;
		System.out.print("The data contains: ");
		for(int i = 0;i<dataLength-1;i++) {
			System.out.print(a[i]);
		}
		System.out.print(a[dataLength-1]+"\n");
		return a;
	}
	
	public void sendAndReceive(){
		 
		 byte[] msg = readRequest("test.txt","octet");
		 
		 try {
	         sendPacket = new DatagramPacket(msg, dataLength,
	                                         InetAddress.getLocalHost(),23);
	      } catch (UnknownHostException e) {
	         e.printStackTrace();
	         System.exit(1);
	      }
		 
		 try {
	         sendReceiveSocket.send(sendPacket);
	      } catch (IOException e) {
	         e.printStackTrace();
	         System.exit(1);
	      }
		 
		 byte data[] = new byte[100];
	      recievePacket = new DatagramPacket(data, data.length);

	      try {
	         // Block until a datagram is received via sendReceiveSocket.  
	         sendReceiveSocket.receive(recievePacket);
	      } catch(IOException e) {
	         e.printStackTrace();
	         System.exit(1);
	      }
	      
	      String p = new String(recievePacket.getData());
	      System.out.println(p);
	 }
	
}

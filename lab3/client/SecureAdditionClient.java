package client;
// A client-side class that uses a secure TCP/IP socket
// Authors: Marcus Broström and Linn Storesund

import java.io.*;
import java.net.*;
import java.security.KeyStore;
import java.util.Scanner;

import javax.net.ssl.*;

public class SecureAdditionClient {
	private InetAddress host;
	private int port;
	// This is not a reserved port number 
	static final int DEFAULT_PORT = 8189;
	static final String KEYSTORE = "client/LIUkeystore.ks";
	static final String TRUSTSTORE = "client/LIUtruststore.ks";
	static final String KEYSTOREPASS = "123456";
	static final String TRUSTSTOREPASS = "abcdef";
  
	
	// Constructor @param host Internet address of the host where the server is located
	// @param port Port number on the host where the server is listening
	public SecureAdditionClient( InetAddress host, int port ) {
		this.host = host;
		this.port = port;
	}
	
  // The method used to start a client object
	public void run() {
		try {
			KeyStore ks = KeyStore.getInstance( "JCEKS" );
			ks.load( new FileInputStream( KEYSTORE ), KEYSTOREPASS.toCharArray() );
			
			KeyStore ts = KeyStore.getInstance( "JCEKS" );
			ts.load( new FileInputStream( TRUSTSTORE ), TRUSTSTOREPASS.toCharArray() );
			
			KeyManagerFactory kmf = KeyManagerFactory.getInstance( "SunX509" );
			kmf.init( ks, KEYSTOREPASS.toCharArray() );
			
			TrustManagerFactory tmf = TrustManagerFactory.getInstance( "SunX509" );
			tmf.init( ts );
			
			SSLContext sslContext = SSLContext.getInstance( "TLS" );
			sslContext.init( kmf.getKeyManagers(), tmf.getTrustManagers(), null );
			SSLSocketFactory sslFact = sslContext.getSocketFactory();      	
			SSLSocket client =  (SSLSocket)sslFact.createSocket(host, port);
			client.setEnabledCipherSuites( client.getSupportedCipherSuites() );
			System.out.println("\n>>>> SSL/TLS handshake completed");

			BufferedReader socketIn;
			socketIn = new BufferedReader( new InputStreamReader( client.getInputStream() ) );
			PrintWriter socketOut = new PrintWriter( client.getOutputStream(), true );
		
		
			System.out.println("\n*****************************");
			System.out.println("***What do you want to do?***");
			System.out.println("0: upload a file to server");
			System.out.println("1: download a file from server");
			System.out.println("2: delete a file from server");
			System.out.println("*****************************");
			int option =0;
			option = Integer.parseInt((new BufferedReader(new InputStreamReader(System.in))).readLine().trim());

			if(option > 2 ){
				System.out.println("Please pick a number between 0 and 2");}
			

			switch(option){

				case 0:
					//uploadFile
					System.out.println("Which file do you want to upload?");
					BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
					String fileName = br.readLine();

					String data = "0";
					data = data + fileName + ".txt, ";
					
					try {
					//Read file
					File myFile = new File("client/"+fileName+".txt");
					Scanner myReader = new Scanner(myFile);
					while (myReader.hasNextLine()) {
					  data += myReader.nextLine();
					 // System.out.println(data);
					}
					myReader.close();
				  } catch (FileNotFoundException e) {
					System.out.println("An error occurred.");
					e.printStackTrace();
					break;
				  }

				  socketOut.println( data );
				  System.out.println( socketIn.readLine() );
			
				break;
					
				case 1: 
				  	// download file
					System.out.println("Which file do you want to download?");
					BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
					String fileNametoDownload = br1.readLine();
					String download = "1";
					download = download + fileNametoDownload + ".txt"; 

					socketOut.println( download );
				  	String writeToFile = socketIn.readLine();
				
					  if(!writeToFile.equals("Does not exist") ){
					try {
						File myObj = new File("client/" + fileNametoDownload + ".txt");
						if (myObj.createNewFile()) {
						  System.out.println(">>>> File downloaded: " + myObj.getName() + " from the server");
						  FileWriter myWriter = new FileWriter("client/" + fileNametoDownload+ ".txt");

						  myWriter.write(writeToFile);
      					  myWriter.close();

						}
						 else {
						  System.out.println("File already exists.");
						  break;
						}
					  } catch (IOException e) {
						System.out.println("An error occurred.");
						e.printStackTrace();
					  }
					}
					else 
					 System.out.println("File does not exist on server");
					break;

				case 2: 
				// delete file
				System.out.println("Which file do you want to delete?");
				BufferedReader ReadFileName = new BufferedReader(new InputStreamReader(System.in));
				String fileToDelete = ReadFileName.readLine();
				String delete = "2";
				delete = delete + fileToDelete + ".txt"; 
				socketOut.println( delete );
				String hello = socketIn.readLine();
				System.out.println(hello);

				break;

			}


		}
		
		catch( Exception x ) {
			System.out.println( x );
			x.printStackTrace();
		}

	}
	

	
	// The test method for the class @param args Optional port number and host name
	public static void main( String[] args ) {
		try {
			InetAddress host = InetAddress.getLocalHost();
			int port = DEFAULT_PORT;
			if ( args.length > 0 ) {
				port = Integer.parseInt( args[0] );
			}
			if ( args.length > 1 ) {
				host = InetAddress.getByName( args[1] );
			}
			SecureAdditionClient addClient = new SecureAdditionClient( host, port );
			addClient.run();
		}
		catch ( UnknownHostException uhx ) {
			System.out.println( uhx );
			uhx.printStackTrace();
		}
	}

	
}

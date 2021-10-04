package server;

// the secure server socket class
// Authors: Marcus Broström and Linn Storesund

import java.io.*;
import java.net.*;
import javax.net.ssl.*;
import java.security.*;
import java.util.Scanner;
import java.util.StringTokenizer;


public class SecureAdditionServer {
	private int port;
	// This is not a reserved port number
	static final int DEFAULT_PORT = 8189;
	static final String KEYSTORE = "client/LIUkeystore.ks";
	static final String TRUSTSTORE = "client/LIUtruststore.ks";
	static final String KEYSTOREPASS = "123456";
	static final String TRUSTSTOREPASS = "abcdef";
	
	/** Constructor
	 * @param port The port where the server
	 *    will listen for requests
	 */
	SecureAdditionServer( int port ) {
		this.port = port;
	}
	
	/** The method that does the work for the class */
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
			SSLServerSocketFactory sslServerFactory = sslContext.getServerSocketFactory();
			SSLServerSocket sss = (SSLServerSocket) sslServerFactory.createServerSocket( port );
			sss.setEnabledCipherSuites( sss.getSupportedCipherSuites() );
			
			System.out.println("\n>>>> SecureAdditionServer: active ");
			SSLSocket incoming = (SSLSocket)sss.accept();

      		BufferedReader in = new BufferedReader( new InputStreamReader( incoming.getInputStream() ) );
			PrintWriter out = new PrintWriter( incoming.getOutputStream(), true );			

			String wholeString = in.readLine();

			char optionChar = wholeString.charAt(0);
			int option = Character.getNumericValue(optionChar);
			
			switch(option){
				case 0:
					//upload
					String fileNameAndContent = wholeString.substring(1);
					String fileName = fileNameAndContent.substring(0,fileNameAndContent.indexOf(","));
					String content =  fileNameAndContent.split(",")[1].trim();

					out.println(">>>> The file " + fileName + " was successfully uploaded to server ");
					
					try {
						File myObj = new File("server/" + fileName);
						
						if (myObj.createNewFile()) {
						  System.out.println(">>> File created: " + myObj.getName());
						  FileWriter myWriter = new FileWriter("server/" + fileName);

						  myWriter.write(content);
      					  myWriter.close();

						} else {
						  System.out.println("File already exists.");
						}
					  } catch (IOException e) {
						System.out.println("An error occurred.");
						e.printStackTrace();
					  }
					  break;

				case 1:
					//download
					String fileNameDownload = wholeString.substring(1);
					String fileContent = "";

					try {
						//Read file
						File myFile = new File("server/" + fileNameDownload);

						if(myFile.exists()){
							Scanner myReader = new Scanner(myFile);
							while (myReader.hasNextLine()) {
							  fileContent += myReader.nextLine();
						
							}

							myReader.close();
							out.println( fileContent );


						}
						else{
							out.println( "Does not exist" );

							break;
							
						}

					
					  } catch (FileNotFoundException e) {
						System.out.println("An error occurred.");
						e.printStackTrace();
						break;

					  }

					break;
				case 2: 
					//delete
					String fileNameDelete = wholeString.substring(1);
					if(option == 2){
                        try {
                            //Read file
                            File deleteFile = new File("server/" + fileNameDelete);

                            //deletes the file if it's exist
                            if(deleteFile.delete()){

                                out.println(">>>> " + deleteFile + " was deleted successfully on the server!");

                            }
                            else{
                                out.println("File could not be deleted.");
                            }


                          } catch (Exception e) {
                            System.out.println("File does not exist");
                            e.printStackTrace();
                          }
						}
						else System.out.println("An asdsad occurred.");


			}

			incoming.close();
		}
		catch( Exception x ) {
			System.out.println( x );
			x.printStackTrace();
		}
	}
	
	
	/** The test method for the class
	 * @param args[0] Optional port number in place of
	 *        the default
	 */
	public static void main( String[] args ) {
		int port = DEFAULT_PORT;
		if (args.length > 0 ) {
			port = Integer.parseInt( args[0] );
		}
		SecureAdditionServer addServe = new SecureAdditionServer( port );
		addServe.run();
	}
}


package application;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
//code by Shahd mustafa
public class Server extends Application {
  // Text area for displaying contents
  private TextArea ta = new TextArea();
  
  // clients that can join
  private int CLIENT1 = 1;
  private int CLIENT2 = 2;

  @Override // Override the start method in the Application class
  public void start(Stage primaryStage) {
    // Create a scene and place it in the stage
    Scene scene = new Scene(new ScrollPane(ta), 450, 200);
    primaryStage.setTitle("MultiThreadServer"); // Set the stage title
    primaryStage.setScene(scene); // Place the scene in the stage
    primaryStage.show(); // Display the stage

    new Thread( () -> {
      try {
        // Create a server socket
        ServerSocket serverSocket = new ServerSocket(8080);
        Platform.runLater(() -> ta.appendText("MultiThreadServer started at " 
          + new Date() + '\n'));
        
    
        while (true) {
        	
        	
        	 Platform.runLater(() -> ta.appendText("waiting for client connection " 
        	          + new Date() + '\n'));
        	 
        	  //connecting the first client 
          // Listen for a new connection request
          Socket client1 = serverSocket.accept();
          
        //first client is in
          Platform.runLater( () -> { 
        	  ta.appendText(new Date() + "Client 1 joined session" + "\n");
          ta.appendText("Client 1's host name is "
                  + client1.getInetAddress().getHostName() + "\n");
          ta.appendText("Client 1's IP Address is "
                  + client1.getInetAddress().getHostAddress() + "\n");
        
          });
          //connecting the outputstram to the first client
          new DataOutputStream(client1.getOutputStream()).writeInt(CLIENT1);
          
          //connecting the second client 
          //creating socket
          Socket client2 = serverSocket.accept();
          
          Platform.runLater( () -> { 
        	  ta.appendText(new Date() + "Client 2 joined session" + "\n");
          ta.appendText("Client 2's host name is "
                  + client2.getInetAddress().getHostName() + "\n");
          ta.appendText("Client 2's IP Address is "
                  + client2.getInetAddress().getHostAddress() + "\n");
        
          });
          //connecting output stream for client 2
          
          new DataOutputStream(client1.getOutputStream()).writeInt(CLIENT2);
          
          Platform.runLater(()-> 
          ta.appendText(new Date() + "Starting chat session" + "\n"));
          
          
          //thread starts chat
          new Thread(new HandleAClient(client1, client2)).start();
         
        }
      }
      catch(IOException ex) {
        System.err.println(ex);
      }
    }).start();
  }
  
  // Define the thread class for handling new connection
  class HandleAClient implements Runnable {
    private Socket client1; // A connected socket
    private Socket client2;
    
    
   
    private DataInputStream inputFromClient1;
    private  DataOutputStream outputToClient1;
    private DataInputStream inputFromClient2;
    private  DataOutputStream outputToClient2;
    
    //constructor for the clients socket
    public HandleAClient(Socket client1, Socket client2) {
    	this.client1 = client1;
    	this.client2 = client2;
    }
    
	@Override
	public void run() {
	
		try {
			 inputFromClient1 = new DataInputStream(client1.getInputStream());
			 outputToClient1 = new DataOutputStream(client1.getOutputStream());
			inputFromClient2 = new DataInputStream(client2.getInputStream());
			 outputToClient2 = new DataOutputStream(client2.getOutputStream());
			 
			 String message = "";
			 
			 
			while(true) {
				
				//String message = "";
			 
				//cleint one send msg
				if(inputFromClient1.available()> 0 ) {
					 message = inputFromClient1.readUTF();
					outputToClient2.writeUTF("Friend: " + message);
					ta.appendText("Client 1: " + message + "\n");
					
				}
				if(inputFromClient2.available()> 0) {         //Client 2 sends msg
					message = inputFromClient2.readUTF();
					outputToClient1.writeUTF("Friend: " + message);
					ta.appendText("Client 2: " + message + "\n");
					
				} 
			 
				}
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}
    
   
    
  }

  
  public static void main(String[] args) {
    launch(args);
  }
}

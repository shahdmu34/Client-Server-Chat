package application;
import java.io.*;
import java.net.*;
import java.util.Scanner;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

//code by Shahd mustafa


public class Client extends Application {
  //input and output streams
  DataOutputStream toServer;
  DataInputStream fromServer;
 

  @Override // Override the start method in the Application class
  public void start(Stage primaryStage) {
    // Panel p to hold the label and text field
    BorderPane paneForTextField = new BorderPane();
    paneForTextField.setPadding(new Insets(5, 5, 5, 5)); 
    paneForTextField.setStyle("-fx-border-color: green");
    paneForTextField.setLeft(new Button("Send"));
    
    TextField tf = new TextField();
    tf.setAlignment(Pos.BOTTOM_RIGHT);
    paneForTextField.setCenter(tf);
    
    BorderPane mainPane = new BorderPane();
    // Text area to display contents
    TextArea ta = new TextArea();
    mainPane.setCenter(new ScrollPane(ta));
    mainPane.setTop(paneForTextField);
    
    // Create a scene and place it in the stage
    Scene scene = new Scene(mainPane, 450, 200);
    primaryStage.setTitle("Client"); // Set the stage title
    primaryStage.setScene(scene); // Place the scene in the stage
    primaryStage.show(); // Display the stage
    
   //intiallizing the function that will set up the connection
    connectServer(tf,ta);
	  
  }
  
  public void connectServer(TextField tf, TextArea ta) {
	  try {
			 
		  Socket socket = new Socket("localhost", 8080);     //creating socket
		  fromServer = new DataInputStream(socket.getInputStream());       
		  toServer = new DataOutputStream(socket.getOutputStream());
  
	 tf.setOnAction(e -> { 
		 
		 //will take in the text from the textfeild 
		  try {
			  String message = tf.getText().trim();	
			  toServer.writeUTF(message);     //send the message to the output
			  
			  toServer.flush();
			  ta.appendText("You: " + message + "\n");       //output msg to the client what they sent
				
			 
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	 
  });
		 
  }catch (Exception ex) {
	  ex.printStackTrace();
  }
	  //pulls in sent messages
	  new Thread(()-> {
				String messg;
				
				while(true) {
					
				try {
					if(fromServer.available() > 0) {   
					messg = fromServer.readUTF();        //reading in from the server
					ta.appendText( messg + "\n"); //outputs the friend messages
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				}
			
		}).start();
  
  }
 
  public static void main(String[] args) {
    launch(args);
  }
}
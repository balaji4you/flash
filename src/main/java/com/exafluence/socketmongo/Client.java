package com.exafluence.socketmongo;

import java.net.*;

import org.bson.Document;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;

import java.io.*; 
public class Client
{
    // initialize socket and input output streams
    private Socket socket            = null;
    private BufferedReader  input   = null;
    private BufferedWriter  out     = null;
private MongoCollection<Document> coll = null;
    // constructor to put ip address and port
    public Client(String address, int port)
    {
        // establish a connection
        try
        {
            socket = new Socket(address, port);
            System.out.println("Connected");
            // takes input from terminal
            input  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // sends output to the socket
            out    = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

       MongoClient mongo = new MongoClient("localhost" , 27017 );  
        MongoDatabase db = mongo.getDatabase("SCMLite");  
       coll = db.getCollection("Devices");
        }
        catch(UnknownHostException u)
        {
            System.out.println(u);
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
        System.out.println("Reading bytes now...");
        // string to read message from input
        String line = "";
        // keep reading until "Over" is input
        while (!line.equals("Over"))
        {
            System.out.println("Inside...");
            try
            {
                line = input.readLine();
               // Document doc = new Document("name", line);  
            //coll.insertOne(doc);
                // out.writeUTF(line);
                DBObject obj = (DBObject) JSON.parse(line);
                Document doc = new Document("name", obj); 
                coll.insertOne(doc);

              for (int i=0; i<line.length(); i++) 
                System.out.println(line);
            }
            catch(IOException i)
            {
                System.out.println(i);
            }
        }
        // close the connection
        try
        {
            input.close();
            out.close();
            socket.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }
    public static void main(String args[])
    {
        Client client = new Client("52.15.210.112", 12345);
    }
}
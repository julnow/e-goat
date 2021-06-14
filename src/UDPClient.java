import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;


import java.io.File;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.FileInputStream;

public class UDPClient {
	
	//stackoverflow function to check sha512
	private static String check(String path, MessageDigest md) throws IOException {
        try (DigestInputStream dis = new DigestInputStream(new FileInputStream(path), md)) {
            while (dis.read() != -1);
            md = dis.getMessageDigest();
        }

        StringBuilder result = new StringBuilder();
        for (byte b : md.digest()) {
            result.append(String.format("%02x", b));
        }
        System.out.println("Your sha512: ");
        System.out.println(result);
        return result.toString();

    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
 
    	ArrayList<Files> files = new ArrayList<Files>();
    	//files to be shared:
    	Scanner scan = new Scanner(System.in);
    	System.out.println("pls give path to shared folder:");
    	String path = scan.nextLine();
    	File directory = new File(path);
    	while(!directory.isDirectory())
    	{
    		System.out.println("error! directory doesn't exist, try again: \n");
    		path = scan.nextLine();
    		directory = new File(path);
    	}
    	//for each file in directory creating hash
    	for (File fileEntry : directory.listFiles()) {
            if (fileEntry.isFile()) {
            	MessageDigest md = MessageDigest.getInstance("SHA-512"); //creating md
                String sha = check(fileEntry.getPath(), md); //create sha for file
                Files file = new Files(sha, fileEntry.getPath()); //adding to list of files
                files.add(file);
            } else {
                System.out.println("error with " + fileEntry.getName());
            }
        }
    	
    	//creating connection with server - z http://www.if.pw.edu.pl/~lgraczyk/wiki/index.php/SK_Zadanie_4_Remote
    	 String message = "List of files";
         InetAddress serverAddress = InetAddress.getByName("localhost");
         System.out.println("connecting to: " + serverAddress);
         
         DatagramSocket socket = new DatagramSocket();
         byte[] stringContents = message.getBytes("utf8");

         DatagramPacket sentPacket = new DatagramPacket(stringContents, stringContents.length);
         sentPacket.setAddress(serverAddress);
         sentPacket.setPort(Config.PORT);
         socket.send(sentPacket);
         
         //creating string of files to be sent
         StringBuilder listOfFiles = new StringBuilder();
         for (Files file : files) {
             listOfFiles.append(file.getSha()).append("\t").append(file.getAddress()).append("\n");
         }
         //sending list to server
         stringContents = listOfFiles.toString().getBytes("utf8");
         sentPacket = new DatagramPacket(stringContents, stringContents.length);
         sentPacket.setAddress(serverAddress);
         sentPacket.setPort(Config.PORT);
         socket.send(sentPacket);
         System.out.println("list of files sent to server");
         
         //checking response from server
         DatagramPacket receivePacket = new DatagramPacket( new byte[Config.BUFFER_SIZE], Config.BUFFER_SIZE);
         socket.setSoTimeout(1010);
         try {
             socket.receive(receivePacket);
             System.out.println("Server received data");
         } catch (SocketTimeoutException ste) {
             System.out.println("!No response from the server");
         }
         
         //send sha512 of file to be read
        System.out.println("enter SHA512 of a file you wish to download: ");
        scan = new Scanner(System.in);
        String sha = scan.nextLine();
 	    stringContents = sha.getBytes("utf8");
 	    sentPacket = new DatagramPacket(stringContents, stringContents.length);
 	    sentPacket.setAddress(serverAddress);
 	    sentPacket.setPort(Config.PORT);
 	    socket.send(sentPacket);
 	        
 	    //Read response from the server (clients who have the file)
 	     receivePacket = new DatagramPacket( new byte[Config.BUFFER_SIZE], Config.BUFFER_SIZE);
 	     socket.setSoTimeout(1010);
 	     try {
 	    	 socket.receive(receivePacket);
 	         int length = receivePacket.getLength();
 	         message = new String(receivePacket.getData(), 0, length, StandardCharsets.UTF_8);
 	         if (message.equals("No such file!")) System.out.println(message);
 	         else System.out.println("following clients have the file: \n" + message);
 	        } catch (SocketTimeoutException ste) {
 	            System.out.println("No response from the server!");
 	        }
         
 	        
         //System.out.println(message + "Choose address and copy it");
         //String clientAdress = scan.nextLine();
     }
    	
    }
 

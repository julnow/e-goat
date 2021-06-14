import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class UDPServer {
	
	

    public static void main(String[] args) throws Exception{
    	
        //Otwarcie gniazda z okreslonym portem
        DatagramSocket datagramSocket = new DatagramSocket(Config.PORT);

        byte[] byteResponse;
        ArrayList<Files> files = new ArrayList<Files>();

        while (true){

            DatagramPacket receivedPacket
                    = new DatagramPacket( new byte[Config.BUFFER_SIZE], Config.BUFFER_SIZE);
            datagramSocket.receive(receivedPacket);
            int length = receivedPacket.getLength();
            String message =
                    new String(receivedPacket.getData(), 0, length, "utf8");

            // Port i host który wysłał nam zapytanie
            InetAddress address = receivedPacket.getAddress();
            int port = receivedPacket.getPort();
            
            if(message.equals("List of files")) {
            	DatagramPacket listPacket
                = new DatagramPacket( new byte[Config.BUFFER_SIZE], Config.BUFFER_SIZE);
            	datagramSocket.receive(listPacket);
                length = listPacket.getLength();
                message = new String(listPacket.getData(), 0, length, "utf8");
                String[] lines = message.split("\n");
                for (String line : lines) {
                	String[] splitLine = line.split("\t");
                	files.add(new Files(splitLine[0], splitLine[1]));
                	}
                byteResponse = "files added to the list".getBytes("utf8");
            }else {
            	ArrayList<String> clients = new ArrayList<String>();
            	for (Files file : files) {
            		if(message.equals(file.getSha())) {
            			clients.add(file.getAddress());
            		}
            	}
            	StringBuilder clientsList = new StringBuilder();
            	for (String addr : clients) {
            		clientsList.append(addr).append("\n");
            	}
            	if(clients.isEmpty()) {
            		byteResponse = "file unavailable".getBytes("utf8");
            	}else {
            		byteResponse = clientsList.toString().getBytes("utf8");
            	}
            }
                  

          	System.out.println(message);
            Thread.sleep(1000); 

            DatagramPacket response
                    = new DatagramPacket(
                        byteResponse, byteResponse.length, address, port);

            datagramSocket.send(response);
        }
    }
}
import java.net.InetAddress;

public class Files {
	
	String sha512;
	String address;
	InetAddress ip;
	int port;
	
	Files(String sha512, String address){
		this.sha512 = sha512;
		this.address = address;
	}
	
	Files(String sha512, String address, int port, InetAddress ip){
		this.sha512 = sha512;
		this.address = address;
		this.ip = ip;
		this.port = port;
				
	}
	
	String getAddress() {
		return address;
	}
	
	String getSha() {
		return sha512;
	}
	
	int getPort() {
		return port;
	}
	
	InetAddress getIp() {
		return ip;
	}
	
	
}

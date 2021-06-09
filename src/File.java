
import java.util.ArrayList;
import java.util.List;

public class File {
	
	String sha512;
	List<String> addresses = new ArrayList<String>();
	
	File(String sha512, String address){
		this.sha512 = sha512;
		addresses.add(address);
	}
	
	void addAddress(String address) {
		addresses.add(address);
	}
	
	String getSha() {
		return sha512;
	}
	
	List<String> getAddresses(){
		return addresses;
	}

}

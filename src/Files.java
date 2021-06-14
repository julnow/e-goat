public class Files {
	
	String sha512;
	String address;
	
	Files(String sha512, String address){
		this.sha512 = sha512;
		this.address = address;
	}
	
	String getAddress() {
		return address;
	}
	
	String getSha() {
		return sha512;
	}
}

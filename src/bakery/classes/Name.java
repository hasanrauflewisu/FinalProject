package bakery.classes;
/*A class intended to hold information of user who places order*/
public class Name {
	
	public String id;
	public String firstName;
	public String lastName;
	public String email;
	public String phoneNumber;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public Name(String id, String firstName, String lastName, String email, String phoneNumber) {
		this.id = id;
		this.firstName=firstName;
		this.lastName=lastName;
		this.email=email;
		this.phoneNumber = phoneNumber;
	}
	
	public void print() {
		System.out.println("User ID: "+getId());
		System.out.println("Name: "+getFirstName()+" "+getLastName());
		System.out.println("Email: "+getEmail());
		System.out.println("Phone number: "+getPhoneNumber()+"\n");
	}
	
}

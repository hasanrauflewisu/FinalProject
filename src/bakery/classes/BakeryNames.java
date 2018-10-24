package bakery.classes;
import java.util.*;
import java.util.concurrent.locks.*;
/*IGNORE THIS CLASS*/
public class BakeryNames {

	private volatile static BakeryNames instance = null;
	private static Lock lock = new ReentrantLock();
	
	public static HashMap<String, Name> Names = new HashMap<String,Name>();
	
	private BakeryNames() {		
	}
	
	public static BakeryNames getIstance() {
		if(instance == null) {
			lock.lock();
			if(instance == null) {
				System.out.println("BakeryNames not instantiated. Creating instance");
				instance = new BakeryNames();
			}
			lock.unlock();
		}else {
			System.out.println("BakeryNames instance found. Returning previous instance");			
		}
		return instance;
	}
	
	public Name FindByKey(String nameKey) {
		Name foundName = null;
		if(Names.containsKey(nameKey)) {
			foundName=Names.get(nameKey);
		}
		else{
			System.out.println("Key \""+nameKey+"\" not found");
		}
		return foundName;
	}
	
	public void PrintNameByKey(String nameKey) {
		Name toPrint = FindByKey(nameKey);
		if(toPrint==null) {
			System.out.println("No name with that key found.");
		}
		else {
			toPrint.print();
		}
	}
	
	public void PrintAllNames() {
		System.out.println("Printing out all names.\n");
		for(Map.Entry<String, Name> entry : Names.entrySet()) {
			String key = entry.getKey();
			Name name = entry.getValue();
			
			System.out.println("Name with key "+key+":");
			name.print();
		}
		System.out.println("End of names\n");
	}
	
	public boolean AddName(String nameKey, Name toAdd) {
		boolean isAdded = false;
		try {
			Names.put(nameKey, toAdd);
			isAdded = true;
		}catch(Exception e) {
			System.out.println(e);
		}
		return isAdded;
	}
	
	public boolean AddName(String nameKey, String firstName, String lastName, String email, String phoneNumber) {
		Name toAdd = new Name(nameKey, firstName, lastName, email, phoneNumber);
		return AddName(nameKey, toAdd);
	}
	
	public List<Name> Find(String firstName, String lastName, String email, String phoneNumber){
		List<Name> toReturn = new ArrayList<Name>();
		
		for(Map.Entry<String, Name> entry : Names.entrySet()) {
			
			Name nameToSort = entry.getValue();
			boolean keep = true;
			
			if(firstName != null && !firstName.isEmpty()) {
				if(!nameToSort.firstName.equalsIgnoreCase(firstName)) {
					keep = false;
			}}
			if(lastName != null && !lastName.isEmpty()) {
				if(!nameToSort.lastName.equalsIgnoreCase(lastName)) {
					keep = false;
			}}
			if(email != null && !email.isEmpty()) {
				if(!nameToSort.email.equalsIgnoreCase(email)) {
					keep = false;
			}}
			if(phoneNumber != null && !phoneNumber.isEmpty()) {
				if(!nameToSort.phoneNumber.equalsIgnoreCase(phoneNumber)) {
					keep = false;
			}}
			if(keep)
				toReturn.add(nameToSort);
		}
		
		return toReturn;
	}
}

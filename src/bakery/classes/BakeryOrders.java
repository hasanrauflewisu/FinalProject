package bakery.classes;
/*Singleton class that holds orders.*/
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.*;
/*IGNORE THIS CLASS*/
public class BakeryOrders {
	
	private volatile static BakeryOrders instance = null;
	private static Lock lock = new ReentrantLock();

	public static HashMap<String,Order> Orders = new HashMap<String,Order>();	
	
	private BakeryOrders() {		
	}
	
	public static BakeryOrders getInstance() {
		if (instance == null) {			
			lock.lock();
			if(instance == null) {
				System.out.println("BakeryOrders not instantiated. Creating instance");
				instance = new BakeryOrders();
			}
			lock.unlock();
		}
		else {
			System.out.println("BakeryOrders instance found. Returning previous instance.");
		}
		return instance;
	}
	
	public Order FindByKey(String orderKey) {
		Order foundOrder = null;
		if(Orders.containsKey(orderKey)) {
			System.out.println("Value with key \""+orderKey+"\" found.");
			foundOrder = Orders.get(orderKey);			
		}
		else {
			System.out.println("Key \""+orderKey+"\" not found.");
		}
		return foundOrder;
	}
	
	public void PrintOrderByKey(String orderKey) {
		Order toPrint = FindByKey(orderKey);
		if(toPrint == null) {
			System.out.println("No order with that key was found.");
		}
		else {
			toPrint.print();
		}
	}
	
	public void PrintAllOrders() {
		System.out.println("Printing out all orders.\n");
		for(Map.Entry<String, Order> entry : Orders.entrySet()) {
			String key = entry.getKey();
			Order order = entry.getValue();
			
			System.out.println("Order with key "+key+":");
			order.print();
		}
		System.out.println("End of orders\n");
	}
	
	public boolean AddOrder(String orderKey, Name user, LocalDate dateOrdered, Cake cakeOrdered, Cookie cookieOrdered, double cost, LocalDate dateCompleted, boolean processed) {
		Order toAdd = new Order(orderKey, user, dateOrdered, cakeOrdered, cookieOrdered, cost, dateCompleted,  processed);
		boolean isAdded = false;
		try {
			Orders.put(orderKey, toAdd);
			isAdded = true;
		}catch(Exception e) {
			System.out.println(e.toString());
		}
		return isAdded;	
	}
	
	public boolean RemoveOrder(String orderKey) {
		boolean isRemoved = false;
		try {
			Orders.remove(orderKey);
			isRemoved = true;
		}catch(Exception e) {
			System.out.println(e.toString());
		}
		return isRemoved;
	}
	
	public List<Order> Find(String firstName, String lastName, String email, String phoneNum, LocalDate orderDate, LocalDate completedDate) {
		List<Order> toReturn = new ArrayList<Order>();
		for(Map.Entry<String, Order> entry : Orders.entrySet()) {
			
			Order orderToSort = entry.getValue();
			Name userOfOrder = orderToSort.name;
			boolean keep = true;
			
			if(firstName != null && !firstName.isEmpty()) {
				if(!userOfOrder.firstName.equalsIgnoreCase(firstName)) {
					keep = false;
			}}
			if(lastName != null && !lastName.isEmpty()) {
				if(!userOfOrder.lastName.equalsIgnoreCase(lastName)) {
					keep = false;
			}}
			if(email != null && !email.isEmpty()) {
				if(!userOfOrder.email.equalsIgnoreCase(email)) {
					keep = false;
			}}
			if(phoneNum != null && !phoneNum.isEmpty()) {
				if(!userOfOrder.phoneNumber.equalsIgnoreCase(phoneNum)) {
					keep = false;
			}}
			if(orderDate != null) {
				if(!orderToSort.dateOrdered.equals(orderDate)) {
					keep = false;
			}}
			if(completedDate != null) {
				if(orderToSort.dateCompleted == null || (orderToSort.dateCompleted != null && !orderToSort.dateCompleted.equals(completedDate) )) {
					keep = false;
			}}			
			if(keep)
				toReturn.add(orderToSort);
		}
		return toReturn;
	}
	
}

package bakery.classes;

import java.io.File;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

//This class manages all orders sent to the bakery. It is a singleton class.
public class Bakery_BEnd_Orders {
	
	
	
	//create an object of SingleObject
	   private static Bakery_BEnd_Orders instance = new Bakery_BEnd_Orders();
	   private static HashMap<Integer, String> BakeryOrders =  new HashMap<Integer, String>();
	   private static int nextOrderNumber = 0;	   
	   private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd HH.mm.ss");
	   private static boolean initialized = false;
	   private static String url = "jdbc:sqlite:hassan_bakery.db";

	   //make the constructor private so that this class cannot be
	   //instantiated
	   private Bakery_BEnd_Orders(){}

	   //Get the only object available
	   public static Bakery_BEnd_Orders getInstance(){
		   if(!initialized) {
			   initializeDb();
			   populateOrders();
			   initialized = true;
		   }
	      return instance;
	   }

	   //Prints a message.
	   public void showMessage(){
	      System.out.println("Bakery BEEnd Main Class!");
	   }
	   
	   //Add an order to the class' hashmap
	   public boolean addOrders(Integer ordernumber, String orderString)
	   {		   
		   //Check if the given order number is good
		   if(!isValid(ordernumber)) {
			   System.out.println("Invalid order number");
			   return false;
		   }
		   //Check if the given order number is not in use.
		   if(BakeryOrders.containsKey(ordernumber)) {
			   System.out.println("Order number already in use.");
			   return false;
		   }
		   //Check if the given order string is good.
		   if(!checkOrder(orderString)) {
			   return false;
		   }
		   //Use string builder to add order time stamp and "not processed"
		   //label to end of string.
		   StringBuilder sb = new StringBuilder(orderString);
		   sb.append(":"+LocalDateTime.now().format(formatter));
		   sb.append(":not processed");
		   orderString = sb.toString();
		   //Insert order into hashmap.
		   try {
			   BakeryOrders.put(ordernumber, orderString);
			   insertDbOrder(ordernumber,orderString);
			   return true;
		   }
		   catch(Exception e) {
			   System.out.println(e.getMessage());
			   return false;
		   }
	   }
	   
	   //Remove an order from the hashmap
	   public boolean removeOrder(Integer ordernumber)
	   {
		   //Check if the given order number is good
		   if(!isValid(ordernumber)) {
			   System.out.println("Invalid order number");
			   return false;
		   }
		   //Check if order with specified ID exists.
		   if(!BakeryOrders.containsKey(ordernumber)) {
			   System.out.println("No order with id "+ordernumber+" exists.");
			   return false;
		   }
		   //Remove order from hashmap
		   try {
			   BakeryOrders.remove(ordernumber);
			   deleteDbOrder(ordernumber);
			   return true;
		   }catch(Exception e) {
			   System.out.println(e.getMessage());
			   return false;
		   }
	   }
	   
	   //Update an already existing order from the hashmap. The order string
	   //should be the same as if it were going through addOrders().
	   public boolean updateOrder(Integer ordernumber, String orderString)
	   {		   
		   //Check if the given order number is good
		   if(!isValid(ordernumber)) {
			   System.out.println("Invalid order number");
			   return false;
		   }
		   //Check if the given order number is in the hashmap
		   if(!BakeryOrders.containsKey(ordernumber)) {
			   System.out.println("Order not found");
			   return false;
		   }
		   //Check if the order string is good.
		   if(!checkOrder(orderString)) {
			   return false;
		   }
		   //Use string builder to add new timestamp and "not processed"
		   //label to end of order.
		   StringBuilder sb = new StringBuilder(orderString);
		   sb.append(":"+LocalDateTime.now().format(formatter));
		   sb.append(":not processed");		   
		   orderString = sb.toString();
		   //Update order string in hashmap
		   try {
			   BakeryOrders.put(ordernumber, orderString);
			   updateDbOrder(ordernumber,orderString);
			   return true;
		   }catch(Exception e) {
			   System.out.println(e.getMessage());
			   return false;
		   }
	   }
	   
	   //Labels an order as "processed" by adding a date/time stamp at the end to replace "not processed"
	   //in the order string.
	   public boolean processOrder(Integer ordernumber, LocalDateTime processDate) {
		   //Check if the given order number is good
		   if(!isValid(ordernumber)) {
			   System.out.println("Invalid order number");
			   return false;
		   }
		   //null check on process date.		   
		   if(processDate == null) {
			   System.out.println("Process date cannot be null");
			   return false;
		   }
		   //Check if order number is in hash map.
		   if(!BakeryOrders.containsKey(ordernumber)) {
			   System.out.println("Order not found");
			   return false;
		   }
		   //Insert processed time stamp into order string
		   String orderString = BakeryOrders.get(ordernumber);	//Get the order string
		   String[] orderSplit = orderString.split(":");		//Split it along :
		   orderSplit[11] = processDate.format(formatter);		//Change the 12th entry in array to processed timestamp
		   StringBuilder sb = new StringBuilder(orderSplit[0]);	//Use string builder to reconstruct order string
		   for(int index=1;index < orderSplit.length; index++) {//Use this loop to reconstruct order string
			   sb.append(":"+orderSplit[index]);
		   }
		   orderString = sb.toString();
		   //Add processed order string to hash map
		   try {
			   BakeryOrders.put(ordernumber, orderString);
			   updateDbOrder(ordernumber,orderString);
			   return true;
		   }catch(Exception e) {
			   System.out.println(e.getMessage());
			   return false;
		   }
	   }
	   
	   //Iterates through the hashmap and prints out information on all orders
	   public void displayOrders()
	   {		 
		   Set set = BakeryOrders.entrySet();
		  java.util.Iterator iterator =  set.iterator();
		  
		  System.out.println("Printing out all orders' info: ");
		   while(iterator.hasNext()) {
			   Map.Entry mentry = (Map.Entry)iterator.next();			   
			   printOrder((String) mentry.getValue());
		   }
		   
	   }
	 
	   //Prints out information on the specified order.
	   public void displayOrder( Integer ordernumber)
	   {
		   //Check if order number is in hash map and print it
		   if ( BakeryOrders.containsKey(ordernumber) ) {			   
			   String orderDetails =  BakeryOrders.get(ordernumber );
			   printOrder(orderDetails);			   
		   } else {
			   System.out.println("ordernumber " + ordernumber + "does not exists");
		   }
		   
	   }
	   
	   //Creates instances of cookie and cake classes as specified by the order and returns them
	   //in a container class.
	   public CakeCookieOrderHolder GetOrderItems(int orderID) {
		   if(!isValid(orderID)) {
			   System.out.println("Invalid id: "+orderID);
			   return null;
		   }
		   String orderDets = BakeryOrders.get(orderID);
		   String[] orderSplit = orderDets.split(":");
		   
		   Cookie cookie = makeCookie(Arrays.copyOfRange(orderSplit, 2, 6));
		   Cake cake = makeCake(Arrays.copyOfRange(orderSplit, 6, 10));
		   		   
		   return new CakeCookieOrderHolder(cake,cookie);
		   
	   }
	   
	   //Returns the next available order ID number to be used.
	   public int getNextOrderNumber() {
		   int toReturn = nextOrderNumber;
		   nextOrderNumber++;
		   return toReturn;
	   }
	   
	   //Returns a list of order IDs of orders that match the method parameters used, such as user ID, order date,
	   //and if processed or not. Each can be null to ignore that filter.
	   public ArrayList<Integer> findOrders(Integer userId, LocalDateTime orderDate, Boolean processed){
		   ArrayList<Integer> returnKeys = new ArrayList<Integer>();
		   for(Map.Entry<Integer, String> entry : BakeryOrders.entrySet()) {
			   boolean toAdd = true;
			   String orderString = entry.getValue();
			   String[] orderSplit = orderString.split(":");
			   //Filter by user id
			   if(userId != null && orderSplit[0].compareTo(Integer.toString(userId)) != 0) {
				   toAdd = false;
			   }
			   //Filter by order date
			   if(orderDate != null && !orderSplit[10].contains(orderDate.format(formatter))) {
				   toAdd = false;
			   }
			   //Filter by if processed or not
			   if(processed != null) {
				   if(processed.booleanValue() == true && orderSplit[10].contains("not processed")) {
					   toAdd = false;
				   }
				   if(processed.booleanValue() == false && !orderSplit[10].contains("not processed")) {
					   toAdd = false;
				   }
			   }
			   
			   if(toAdd) {
				   returnKeys.add(entry.getKey());
			   }
		   }
		   
		   return returnKeys;		   
	   }
	   
	   //Checks if the order string given to addOrder() or updateOrder() are good strings.
	   private boolean checkOrder(String orderString) {
		   		   
			String[] orderSplit = orderString.split(":");
			//Validate the order length 
			if(orderSplit.length != 10) {
				System.out.println("Invalid order string");
				return false;
			}
			//Check if user id in order string is good.
			int userID = Integer.parseInt(orderSplit[0]);
			if(!Bakery_BEnd_Users.hasId(userID)) {
				System.out.println("User id not found.");
				return false;
			}
			//check if order number in order string is valid.
			int orderID = Integer.parseInt(orderSplit[1]);
			if(!isValid(orderID)) {
				System.out.println("Invalid order number");
				return false;
			}
			//Check information on cookie specified in order. Start with quantity
			int cookieQuantity = Integer.parseInt(orderSplit[2]);
			if (cookieQuantity < 0) {
				System.out.println("Invalid cookie quantity");
				return false;
			}
			//Check if size specified is valid
			int cookieBoxType = Integer.parseInt(orderSplit[4]);
			if(cookieBoxType >= CookieSizeEnum.values().length || cookieBoxType < 0) {
				System.out.println("Invalid cookie box size");
				return false;
			}
			//Check if decoration specified is valid
			String cookieDecoration = orderSplit[5];
			if(!Arrays.asList(DecorationContainer.cookieDecos).contains(cookieDecoration.toLowerCase())) {
				System.out.println("Invalid cookie decoration");
				return false;
			}
			//Check information on cake specified in order. Start with quantity
			int cakeQuantity = Integer.parseInt(orderSplit[6]);
			if(cakeQuantity < 0) {
				System.out.println("Invalid cake quantity");
				return false;
			}
			//Check if size specified is valid
			int cakeSizeType = Integer.parseInt(orderSplit[8]);
			if(cakeSizeType >= CakeSizeEnum.values().length || cakeSizeType < 0) {
				System.out.println("Invalid cake size");
				return false;
			}
			//Check if decoration specified is valid
			String cakeDecoration = orderSplit[9];
			if(!Arrays.asList(DecorationContainer.cakeDecos).contains(cakeDecoration.toLowerCase())) {
				System.out.println("Invalid cake decoration");
				return false;
			}
			
			return true;
	   }

	   //Prints information of an order from its string.
	   private void printOrder(String orderString) {
		   String[] orderArray = orderString.split(":");
		   Cookie cookie = makeCookie(Arrays.copyOfRange(orderArray, 2, 6));
		   Cake cake = makeCake(Arrays.copyOfRange(orderArray, 6, 10));
		   
		   System.out.println("Displaying information for order: "+orderArray[1]);
		   System.out.println("This order was made "+orderArray[10]);
		   System.out.print("This order was made by:\t");
		   Bakery_BEnd_Users.displayUser(orderArray[0]);
		   System.out.println("This order contains :");
		   System.out.print("\t"); cookie.print();
		   System.out.print("\t"); cake.print();
		   if(orderArray[11].compareTo("not processed") == 0) {
			   System.out.println("This order has not been processed");			   
		   }else {
			   System.out.println("This order was processed on: "+ orderArray[11]);
		   }		   
	   }
	   
	   //Uses the ShapeFactory class to create a cookie object
	   private Cookie makeCookie(String[] cookieOrder) {
			ShapeFactory sf = new ShapeFactory();			
			Cookie toReturn = (Cookie) sf.getBake("cookie", cookieOrder[0], cookieOrder[2], "chocolate chip", cookieOrder[3], "");
			return toReturn;
	   }
	   
	   //Uses the ShapeFactory class to create a cake object.
	   private Cake makeCake(String[] cakeOrder) {
			ShapeFactory sf = new ShapeFactory();			
			Cake toReturn = (Cake) sf.getBake("cake", cakeOrder[0], cakeOrder[2], "vanilla", cakeOrder[3], "chocolate");
			return toReturn;
	   }
	   
	   //Checks if the given order id number is valid.
	   private boolean isValid(Integer idToCheck) {
		   return idToCheck != null && idToCheck < nextOrderNumber;
	   }
	   
	   //Checks for SQLite database file
	   private boolean checkDbExists() {
		   File dbFile = new File("hassan_bakery.db");
		   return dbFile.exists();
	   }
	   
	   private static Connection getConnection() {
		   String url = "jdbc:sqlite:hassan_bakery.db"; 
		   Connection conn = null;
		   try {
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		   return conn;
	   }
	   
	   //Creates SQLite database table
	   private static void initializeDb() {		   
		   String sql = "CREATE TABLE IF NOT EXISTS BakeryOrders (\n"
		   		+ " orderId integer PRIMARY KEY,\n"
		   		+ " orderString text NOT NULL\n"
		   		+ ");";
		   try(Connection conn = getConnection()){
			   Statement stmt = conn.createStatement();
			   stmt.execute(sql);
		   } catch (SQLException e) {
			   System.out.println(e.getMessage());
		   }
	   }
	   
	   private static void populateOrders() {
		   String sql = "SELECT orderId, orderString FROM BakeryOrders";
		   try(Connection conn = getConnection();
				   Statement stmt = conn.createStatement();
				   ResultSet rs = stmt.executeQuery(sql)){
			   while(rs.next()) {
				   BakeryOrders.put(rs.getInt("orderId"), rs.getString("orderString"));
				   nextOrderNumber = rs.getInt("orderID") + 1;					   
			   }			   
		   }catch (SQLException e){
			   System.out.println(e.getMessage());
		   }
	   }
	   
	   private void insertDbOrder(int id, String orderString) {
		   String sql = "INSERT INTO BakeryOrders(orderId,orderString) VALUES(?,?)";		   
		   try(Connection conn = getConnection();
				   PreparedStatement pstmt = conn.prepareStatement(sql)){
			   pstmt.setInt(1, id);
			   pstmt.setString(2, orderString);
			   pstmt.executeUpdate();
		   } catch (SQLException e) {
			   System.out.println(e.getMessage());
		   }
	   }
	   
	   private void updateDbOrder(int id, String orderString) {
		   String sql = "UPDATE BakeryOrders SET orderString = ? WHERE orderId = ?";
		   try(Connection conn = getConnection();
				   PreparedStatement pstmt = conn.prepareStatement(sql)){
			   pstmt.setString(1, orderString);
			   pstmt.setInt(2, id);			   
			   pstmt.executeUpdate();
		   } catch (SQLException e) {
			   System.out.println(e.getMessage());
		   }
	   }
	   
	   private void deleteDbOrder(int id) {
		   String sql = "DELETE FROM BakeryOrders WHERE orderId = ?";		   
		   try(Connection conn = getConnection();
				   PreparedStatement pstmt = conn.prepareStatement(sql);){
			   pstmt.setInt(1, id);
			   pstmt.executeUpdate();
		   } catch (SQLException e) {
			   System.out.println(e.getMessage());
		   }
	   }
	   
	   public void setUrl(String url) {
		Bakery_BEnd_Orders.url = url;
	   }
}

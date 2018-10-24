package bakery.classes;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Bakery_BEnd_Users {
	
	
	
	//create an object of SingleObject
	   private static Bakery_BEnd_Users instance = new Bakery_BEnd_Users();
	   private static HashMap<Integer, String> BakeryUsers =  new HashMap<Integer, String>();
	   private static int nextIdNumber = 0;
	   private static boolean initialized = false;
	   private static String url = "jdbc:sqlite:hassan_bakery.db";

	//make the constructor private so that this class cannot be
	   //instantiated
	   private Bakery_BEnd_Users(){}

	   //Get the only object available
	   public static Bakery_BEnd_Users getInstance(){
		   if(!initialized) {
			   initializeDb();
			   populateUsers();
			   initialized = true;
		   }
	      return instance;
	   }
	   
	   //Add a user to the hashmap. User string is in the format
	   //"first_name last_name:email"
	   public boolean addUser(Integer idNumber, String userString)
	   {		   
		   //Validate user id
		   if(!isValidId(idNumber)) {
			   System.out.println("Invalid ID number: "+idNumber);
			   return false;
		   }
		   //Check if id is in use
		   if(BakeryUsers.containsKey(idNumber)) {
			   System.out.println("ID number "+idNumber+" already in use.");			   
			   return false;
		   }
		   //Check if user already exists in hashmap
		   if(BakeryUsers.containsValue(userString)) {
			   System.out.println("Cannot add user with string representation:"+userString);
			   System.out.println("This user has already been registered");
			   return false;
		   }
		   //Add user to hashmap
		   try {
			   BakeryUsers.put(idNumber, userString);
			   insertDbUser(idNumber, userString);
			   return true;
		   }
		   catch(Exception e) {
			   System.out.println(e.getMessage());
			   return false;
		   }
	   }
	   
	   //Remove a user from the hashmap
	   public boolean removeUser(Integer idNumber)
	   {
		   //Validate user id
		   if(!isValidId(idNumber)) {
			   System.out.println("Invalid ID number: "+idNumber);
			   return false;
		   }
		   //See if user id is in hashmap or not
		   if(!BakeryUsers.containsKey(idNumber)) {
			   System.out.println("ID number "+idNumber+"not found.");			   
			   return false;
		   }
		   //Remove specified user
		   try {
			   BakeryUsers.remove(idNumber);
			   deleteDbUser(idNumber);
			   return true;
		   }catch(Exception e) {
			   System.out.println(e.getMessage());
			   return false;
		   }
	   }
	   
	   //Update an already existing user in the hashmap
	   public boolean updateUser(Integer idNumber, String userString)
	   {		   
		   //Validate id
		   if(!isValidId(idNumber)) {
			   System.out.println("Invalid ID number: "+idNumber);
			   return false;
		   }
		   //See if id is in use or not
		   if(!BakeryUsers.containsKey(idNumber)) {
			   System.out.println("ID number "+idNumber+" is not registered.");
			   return false;
		   }
		   //Check if user already exists in hashmap
		   if(BakeryUsers.containsValue(userString)) {
			   System.out.println("Cannot update user to string representation:"+userString);
			   System.out.println("This string is already in use");
			   return false;
		   }
		   //update user in database
		   try {
			   BakeryUsers.put(idNumber, userString);
			   updateDbUser(idNumber, userString);
			   return true;
		   }catch(Exception e) {
			   System.out.println(e.getMessage());
			   return false;
		   }
	   }
	   
	   //Iterates through the hashmap's entries and prints out each
	   //user's information
	   public void displayUsers() {
		   System.out.println("Printing all users.");
		   for(Map.Entry<Integer, String> entry : BakeryUsers.entrySet()) {
			   int userID=entry.getKey();
			   String userDetails = entry.getValue();
			   String[] userSplit = userDetails.split(":");
			   System.out.println("User ID: "+ userID);
			   System.out.println("\tName: "+ userSplit[0]);
			   System.out.println("\tEmail: "+ userSplit[1]);	
		   }
		   
	   }
	   
	   //Returns a list of user IDs filtered by the input parameters, 
	   //which are user's name and user's email. Each can be null
	   //to be ignored.
	   public ArrayList<Integer> findUser(String name, String email){
		   ArrayList<Integer> returnKeys = new ArrayList<Integer>();
		   for(Map.Entry<Integer, String> entry : BakeryUsers.entrySet()) {
			   boolean toAdd = true;
			   String userString = entry.getValue();
			   String[] userSplit = userString.split(":");
			   
			   if(name != null && name.toLowerCase().compareTo(userSplit[0].toLowerCase()) != 0) {
				   toAdd = false;
			   }
			   
			   if(email != null && email.toLowerCase().compareTo(userSplit[1].toLowerCase()) != 0) {
				   toAdd = false;
			   }
			   
			   if(toAdd) {
				   returnKeys.add(entry.getKey());
			   }			   			  
		   }
		   return returnKeys;
	   }
	   
	   //Prints out user information using the string representation
	   //of their numerical user ID.
	   //Simply calls Integer.parseInt() on the string ID and uses
	   //that result to call displayUser(int)
	   public static void displayUser(String userID) {
		   int userIDInt = Integer.parseInt(userID);
		   displayUser(userIDInt);
	   }
	 
	   //Prints out user information of the user specified by the
	   //integer user ID
	   public static void displayUser(Integer userID)
	   {
		   if ( BakeryUsers.containsKey(userID) ) {			   
			   String userDetails =  BakeryUsers.get(userID);
			   String[] userSplit = userDetails.split(":");
			   System.out.println("User ID: "+ userID);
			   System.out.println("\tName: "+ userSplit[0]);
			   System.out.println("\tEmail: "+ userSplit[1]);			   
		   } else {
			   System.out.println("user ID " + userID + "does not exists");
		   }
		   
	   }	  
	   
	   //A method to check if the hashmap has a specified user ID in it.
	   public static boolean hasId(Integer userID) {
		   return BakeryUsers.containsKey(userID);
	   }
	   
	   //A method to check if a given user ID number is valid.
	   public static boolean isValidId(Integer userID) {
		   return userID < nextIdNumber && 0 <= userID;
	   }
	   
	   //A method to return the next usable User ID number
	   public int getNextUserIdNumber() {
		   int toReturn = nextIdNumber;
		   nextIdNumber++;
		   return toReturn;
	   }	   
	   
	   //Checks for SQLite database file
	   private static boolean checkDbExists() {
		   File dbFile = new File("hassan_bakery.db");
		   return dbFile.exists();
	   }
	   
	   private static Connection getConnection() {		   
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
		   String sql = "CREATE TABLE IF NOT EXISTS BakeryUsers("
		   		+ " userId integer PRIMARY KEY,"
		   		+ " userString text NOT NULL);";
		   try(Connection conn = getConnection();
				   Statement stmt = conn.createStatement()){
			   stmt.execute(sql);
		   } catch (SQLException e) {
			   System.out.println(e.getMessage());
		   }
	   }
	   
	   private static void populateUsers() {
		   String sql = "SELECT userId, userString FROM BakeryUsers";
		   try(Connection conn = getConnection();
				   Statement stmt = conn.createStatement();
				   ResultSet rs = stmt.executeQuery(sql)){
			   while(rs.next()) {
				   BakeryUsers.put(rs.getInt("userId"), rs.getString("userString"));
				   nextIdNumber = rs.getInt("userId")+1;
			   }			   
		   }catch (SQLException e){
			   System.out.println(e.getMessage());
		   }
	   }
	   
	   private void insertDbUser(int id, String userString) {
		   String sql = "INSERT INTO BakeryUsers(userId,userString) VALUES(?,?)";
		   try(Connection conn = getConnection();
				   PreparedStatement pstmt = conn.prepareStatement(sql)){
			   pstmt.setInt(1, id);
			   pstmt.setString(2, userString);
			   pstmt.executeUpdate();
		   } catch (SQLException e) {
			   System.out.println(e.getMessage());
		   }
	   }
	   
	   private void updateDbUser(int id, String userString) {
		   String sql= "UPDATE BakeryUsers SET userString = ? WHERE userId = ?";
		   try(Connection conn = getConnection();
				   PreparedStatement pstmt = conn.prepareStatement(sql)){
			   pstmt.setString(1, userString);
			   pstmt.setInt(2, id);			   
			   pstmt.executeUpdate();
		   } catch (SQLException e) {
			   System.out.println(e.getMessage());
		   }
	   }
	   
	   private void deleteDbUser(int id) {
		   String sql = "DELETE FROM BakeryUsers WHERE userId = ?";
		   
		   try(Connection conn = getConnection();
				   PreparedStatement pstmt = conn.prepareStatement(sql);){
			   pstmt.setInt(1, id);
			   pstmt.executeUpdate();
		   } catch (SQLException e) {
			   System.out.println(e.getMessage());
		   }
	   }
	   
	   public void setUrl(String url) {
		Bakery_BEnd_Users.url = url;
	   }
}

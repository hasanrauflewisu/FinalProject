package bakery.classes;
import java.io.File;
import java.util.ArrayList;

///
//  The main Baker Program  illustrating the three design patterns
//  Decorator
//  Factory
//  Command
//  in the Command Pattern, all the above methods were used. 
/// State Pattern -  09/30/2018


public class Bakery_BEndMain {
	 public static void main(String[] args) 
	 {		   
		 System.out.println("\n Hassan Bakery  -  Java Main Program \n");
		 Bakery_BEnd_Users mBakery_BEnd_Users = Bakery_BEnd_Users.getInstance();
		 Bakery_BEnd_Orders mBakery_BEnd_Orders = Bakery_BEnd_Orders.getInstance();
		 
		 int userId1 = mBakery_BEnd_Users.getNextUserIdNumber();
		 int userId2 = mBakery_BEnd_Users.getNextUserIdNumber();
		 int userId3 = mBakery_BEnd_Users.getNextUserIdNumber();
		 
		 String userString1 = "John Doe "+userId1+":test@java.com";
		 String userString2 = "John Doe "+userId2+":test@java.com";
		 String userString3 = "John Doe "+userId3+":test@java.com";
		 
		 System.out.println("\n///////////////Adding users///////////////\n");
		 mBakery_BEnd_Users.addUser(userId1, userString1);
		 mBakery_BEnd_Users.addUser(userId2, userString2);
		 mBakery_BEnd_Users.addUser(userId3, userString3);
		 mBakery_BEnd_Users.displayUsers();
		 
		 System.out.println("\n///////////////Adding duplicate user ID///////////////\n");
		 mBakery_BEnd_Users.addUser(userId1, "John Doe alpha:test2@java.com");
		 
		 System.out.println("\n///////////////Adding duplicate user string///////////////\n");
		 int userIdDuplicate = mBakery_BEnd_Users.getNextUserIdNumber();
		 mBakery_BEnd_Users.addUser(userIdDuplicate, userString1);
		 
		 System.out.println("\n///////////////Adding invalid ID user///////////////\n");
		 mBakery_BEnd_Users.addUser(userId1 + 3, "John Doe 1:test@java.com");
		 		 		 
		 System.out.println("\n///////////////Updating user///////////////\n");
		 System.out.println("--Before--");
		 mBakery_BEnd_Users.displayUser(userId2);
		 String userString2Update = "Jon Doe "+userId2+":test2@java.com";
		 mBakery_BEnd_Users.updateUser(userId2, userString2Update);
		 System.out.println("--After--");
		 mBakery_BEnd_Users.displayUser(userId2);
		 
		 System.out.println("\n///////////////Removing user///////////////\n");
		 System.out.println("--Before--");
		 mBakery_BEnd_Users.displayUsers();
		 mBakery_BEnd_Users.removeUser(userId3);
		 System.out.println("--After--");
		 mBakery_BEnd_Users.displayUsers();
		 
		 System.out.println("\n///////////////Searching user///////////////\n");
		 String searchString =  "John Doe "+userId1;
		 ArrayList<Integer> searchedUsers = mBakery_BEnd_Users.findUser(searchString, null);
		 for(int key : searchedUsers) {
			 mBakery_BEnd_Users.displayUser(key);
		 }
		 
		 
		 int orderId1 = mBakery_BEnd_Orders.getNextOrderNumber();
		 int orderId2 = mBakery_BEnd_Orders.getNextOrderNumber();
		 int orderId3 = orderId1 + 3;
		 
		 System.out.println("\n///////////////Adding orders///////////////\n");
		 mBakery_BEnd_Orders.addOrders(orderId1, String.join(":", Integer.toString(userId1),Integer.toString(orderId1),"1","cookie","1","none","2","cake","0","xmas"));
		 mBakery_BEnd_Orders.addOrders(orderId2, String.join(":", Integer.toString(userId1),Integer.toString(orderId2),"0","cookie","1","none","2","cake","0","xmas"));
		 mBakery_BEnd_Orders.displayOrders();
		 
		 System.out.println("\n///////////////Adding invalid ID order///////////////\n");
		 mBakery_BEnd_Orders.addOrders(orderId3, String.join(":", Integer.toString(userId1),Integer.toString(orderId3),"1","cookie","1","none","2","cake","0","xmas"));		
		 
		 System.out.println("\n///////////////Updating order///////////////\n");
		 System.out.println("--Before--");
		 mBakery_BEnd_Orders.displayOrder(orderId2);
		 mBakery_BEnd_Orders.updateOrder(orderId2, String.join(":", Integer.toString(userId1),Integer.toString(orderId2),"2","cookie","1","none","2","cake","0","xmas"));
		 System.out.println("--After--");
		 mBakery_BEnd_Orders.displayOrder(orderId2);
		 
		 System.out.println("\n///////////////Removing order///////////////\n");
		 System.out.println("--Before--");
		 mBakery_BEnd_Orders.displayOrders();			 
		 mBakery_BEnd_Orders.removeOrder(orderId1);
		 System.out.println("--After--");
		 mBakery_BEnd_Orders.displayOrders();
		 
		 System.out.println("\n///////////////Getting order///////////////\n");
		 CakeCookieOrderHolder holder = mBakery_BEnd_Orders.GetOrderItems(orderId2);
		 if(holder != null) {
			 holder.cake.print();
			 holder.cookie.print();
		 } else {
			 System.out.println("Unable to get order items for order ID "+orderId2);
		 }
		 System.out.println("\n///////////////Searching order///////////////\n");
		 ArrayList<Integer> searchedOrders= mBakery_BEnd_Orders.findOrders(orderId1, null, null);
		 for(int key : searchedOrders) {
			 mBakery_BEnd_Orders.displayOrder(key);
		 }
		 
		 System.out.println("\n///////////////Processing order///////////////\n");
		 Bakery_Appliances appliance = new Bakery_Appliances();
		 SwitchOnOven switchOnOven = new SwitchOnOven(appliance);
		 ProcessOrders processOrders = new ProcessOrders(orderId2);
		 SwitchOffOven switchOffOven = new SwitchOffOven(appliance);
		 
		 BakingOvenRegulate regulator = new BakingOvenRegulate();		 
		 regulator.takeCommand(switchOnOven);
		 regulator.takeCommand(processOrders);
		 regulator.takeCommand(switchOffOven);
		 regulator.executeCommands();
		 
		 System.out.println("--Show unprocessed orders--");
		 ArrayList<Integer> unprocessedOrders= mBakery_BEnd_Orders.findOrders(null, null, false);
		 for(int key : unprocessedOrders) {
			 mBakery_BEnd_Orders.displayOrder(key);
		 }
		 System.out.println("--Show processed orders--");
		 ArrayList<Integer> processedOrders= mBakery_BEnd_Orders.findOrders(null, null, true);
		 for(int key : processedOrders) {
			 mBakery_BEnd_Orders.displayOrder(key);
		 }
	 }
	 
	 
	 
	 ///-------------------------------  Design Pattern Test Code --------------------------------//
	 public static void testJavaPattern() {
		 System.out.println("\n Assignment  One -  Decorator Pattern \n");
		 //Instantiate a new Cookie Object with no decoration
	      Bake cookie = new Cookie();
	      
	      Cake cake = new Cake();

	      // instantiate a new Cookie Object with Red Border Baked using bake Decorator
	      Bake cookiewreddeco = new RedBorderDecorator(new Cookie());

	      // instantiate a new Cake Object with Red Border Baked using bake Decorator
	      Bake cakewreddeco = new RedBorderDecorator(new Cake());
	      
	      
	      System.out.println("Bake Cookie in 350degrees with normal border");
	      cookie.bake_350degrees();

	      System.out.println("\nCookie is Baked 350 degrees with red border");
	      cookiewreddeco.bake_350degrees();

	      System.out.println("\nBake Cake in 350degrees with normal border");
	      cake.bake_350degrees();
	      
	      System.out.println("\nCake is baked 350 degrees with red border");
	      cakewreddeco.bake_350degrees();
	      
	      ///
	      //
	      //  Factory Pattern Illustration using Cake and Cookie Baking Example  Assignment 2
	      //
	      ///
	      System.out.println("\n\n\n Assignment  Two  - Factory Pattern \n\n");
	      ShapeFactory shapeFactory = new ShapeFactory();
	      //get an instantiated object of CookieShape Dough
	      Shape CookieShape  = shapeFactory.getShape("COOKIE");
	      //call bake method of CookieShape
	      CookieShape.bake();
	      
	      System.out.println("\n\n");
	      Shape CakeShape  = shapeFactory.getShape("CAKE");
	      //call bake method of CookieShape
	      CakeShape.bake();
	      
	      
	      ///
	      //
	      //  Command Pattern Illustration using Cake and Cookie Baking Example and regulating the GE Oven : Assignment 3
	      //
	      ///
	      
	      System.out.println("\n\n\n Assignment  Three  - Command Pattern \n\n");
	      
	      Bakery_Appliances bakery_Appliances =  new Bakery_Appliances();
	      SwitchOnOven switchOnOven = new SwitchOnOven(bakery_Appliances);
	      SwitchOffOven switchOffOven = new SwitchOffOven(bakery_Appliances);
	      
	      BakingOvenRegulate bakingOvenRegulate = new BakingOvenRegulate();
	      bakingOvenRegulate.takeCommand(switchOffOven);
	      bakingOvenRegulate.executeCommands();
	      
	      bakingOvenRegulate.takeCommand(switchOnOven);
	      bakingOvenRegulate.executeCommands();
	      
	      CookieShape.bake();
	      CakeShape.bake();
	      
	      System.out.println("\nBake Cake in 350degrees with normal border, Oven is On");
	      cake.bake_350degrees();
	      
	      System.out.println("\nCake is baked 350 degrees with red border, Oven is On");
	      cakewreddeco.bake_350degrees();
	      
	      bakingOvenRegulate.takeCommand(switchOffOven);
	      bakingOvenRegulate.executeCommands();
	      
	      
	      ///
	      // Iterator Pattern Illustrating the type of cookies and cakes  to be used in the project: Assignment 4
	      System.out.println("\n\n\n Assignment  Four  - Iterator Pattern \n\n");
	      System.out.println("\n Cake Names \n\n");
	      
	      CakeNameRepository cakeNameRepository = new CakeNameRepository();
	      CookieNameRepository cookieNameRepository = new CookieNameRepository();
	      
	      for(Iterator iter = cakeNameRepository.getIterator(); iter.hasNext();){
	          String name = (String)iter.next();
	          System.out.println("Cake Name : " + name);
	       }
	      
	      System.out.println("\n Cookie Names \n\n");
	      for(Iterator iter = cookieNameRepository.getIterator(); iter.hasNext();){
	          String name = (String)iter.next();
	          System.out.println("Cookie Name : " + name);
	      }
	      ///
	      //  State Pattern Illustrated by GE Oven State before it is being fired 
	      ///
	      System.out.println("\n\n\n Assignment  Five  - State Pattern \n\n");
	      GEOvenContext  gEOvenContext = new GEOvenContext();
	      
	      OvenStartState  ovenStartState = new OvenStartState();
	      ovenStartState.findGEOven_State(gEOvenContext);
	      
	      System.out.println(gEOvenContext.getGEOvenState().toString());
	      
	      System.out.println("\n\n");
	      OvenStopState  ovenStopState = new OvenStopState();
	      ovenStopState.findGEOven_State(gEOvenContext);
	      
	      System.out.println(gEOvenContext.getGEOvenState().toString());
	 }
	 
	 public static void testOrderString() {
		 BakeryOrders bakeOrders = BakeryOrders.getInstance();		 
		 
		 String testOrder = "Test Order:1:1:cookie:0:smilie:2:cake:1:birthday";
		 OrderStringReader.readOrderString(testOrder, bakeOrders);

		 bakeOrders.PrintAllOrders();

	   }
}

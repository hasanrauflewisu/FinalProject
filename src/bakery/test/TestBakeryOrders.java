package bakery.test;

import static org.junit.Assert.*;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import bakery.classes.Bakery_BEnd_Orders;
import bakery.classes.Bakery_BEnd_Users;

public class TestBakeryOrders {

	static Bakery_BEnd_Users userDb;
	static Bakery_BEnd_Orders toTest;	
	static int userId;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		File testDb = new File("test.db");
		if(testDb.exists()) {
			testDb.delete();
		}		
		
		initializeUsersDb();
		initializeOrdersDb();
		
		userDb = Bakery_BEnd_Users.getInstance();
		userDb.setUrl("jdbc:sqlite:test.db");
				
		toTest = Bakery_BEnd_Orders.getInstance();	
		toTest.setUrl("jdbc:sqlite:test.db");
		
		userId = userDb.getNextUserIdNumber();
		userDb.addUser(userId, "John Doe:JonDoe@JonDoe.JonDoe.jd");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		File testDb = new File("test.db");
		if(testDb.exists()) {
			testDb.delete();
		}
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAddOrders_validOrder() {
		int orderId=toTest.getNextOrderNumber();
		String orderString = String.join(":", Integer.toString(userId),Integer.toString(orderId),"1","cookie","1","none","2","cake","0","xmas");
		boolean added = toTest.addOrders(orderId, orderString);
		ResultSet rs = getOrdersFromDb();
		
		try {
			while(rs.next()){
				String fromDb = rs.getString("orderString");
				if(fromDb.equals(orderString)) {
					break;
				}
				if(rs.isAfterLast()) {
					fail("Entry was not properly inserted into user table.");
				}
			}
		}catch(SQLException e) {
			fail(e.getMessage());
		}
		assertTrue(added);		
	}
	
	@Test
	public void testAddOrders_invalidId() {
		assertFalse(toTest.addOrders(9999,String.join(":", Integer.toString(userId),Integer.toString(9999),"1","cookie","1","none","2","cake","0","xmas")));
	}

	@Test
	public void testAddOrders_duplicateIds() {
		int id = toTest.getNextOrderNumber();
		toTest.addOrders(id,String.join(":", Integer.toString(userId),Integer.toString(id),"1","cookie","1","none","2","cake","0","xmas"));
		assertFalse(toTest.addOrders(id,String.join(":", Integer.toString(userId),Integer.toString(id),"1","cookie","1","none","2","cake","0","xmas")));
	}
	
	@Test
	//Invalid user id to create bad order string
	public void testAddOrders_invalidOrderString() {
		int id=toTest.getNextOrderNumber();
		assertFalse(toTest.addOrders(id,String.join(":", Integer.toString(-1),Integer.toString(id),"1","cookie","1","none","2","cake","0","xmas")));
	}
	
	@Test
	public void testRemoveOrder_GoodDeletion() {
		int id = toTest.getNextOrderNumber();
		String orderString = String.join(":", Integer.toString(userId),Integer.toString(id),"1","cookie","1","none","2","cake","0","xmas");
		toTest.addOrders(id,orderString);
		
		boolean deleted = toTest.removeOrder(id);
		ResultSet rs = getOrdersFromDb();
		try {
			while(rs.next()) {
				String fromDb = rs.getString("orderString");
				if(fromDb.equals(orderString)) {
					fail("Entry not deleted from user table.");
				}
			}
		}catch(SQLException e) {
			fail(e.getMessage());
		}
		assertTrue(deleted);
	}
	
	@Test
	public void testRemoveOrder_InvalidId() {
		assertFalse(toTest.removeOrder(9999));
	}
	
	@Test
	public void TestRemoveOrder_OrderDNE() {
		int toDel = toTest.getNextOrderNumber();
		assertFalse(toTest.removeOrder(toDel));
	}

	@Test
	public void testUpdateOrder_goodUpdate() {
		int id=toTest.getNextOrderNumber();
		String orderString = String.join(":", Integer.toString(userId),Integer.toString(id),"1","cookie","1","none","2","cake","0","xmas");
		String updatedString = String.join(":", Integer.toString(userId),Integer.toString(id),"0","cookie","1","none","1","cake","0","xmas");
		toTest.addOrders(id, orderString);
		
		boolean updated = toTest.updateOrder(id, updatedString);
		ResultSet rs = getOrdersFromDb();
		try {
			while(rs.next()) {
				int fromDbInt = rs.getInt("orderId");
				String fromDbString = rs.getString("orderString");
				if(fromDbInt == id) {
					if(fromDbString.equals(updatedString)) {
						break;
					}else {
						fail("Entry was not updated in order table.");
					}
				}
				if(rs.isAfterLast()) {
					fail("Entry was not found in order table.");
				}
			}
		}catch(SQLException e) {
			fail(e.getMessage());
		}
		assertTrue(updated);
	}
	
	@Test
	public void testUpdateOrder_invalidId() {
		assertFalse(toTest.updateOrder(9999, "This shouldn't matter"));
	}
	
	@Test
	public void testUpdateOrder_OrderDNE() {
		int dummyId = toTest.getNextOrderNumber();
		assertFalse(toTest.updateOrder(dummyId, "This shouldn't matter"));
	}
	
	@Test
	public void testUpdateOrder_invalidOrder() {
		int id=toTest.getNextOrderNumber();
		String order = String.join(":", Integer.toString(-1),Integer.toString(id),"0","cookie","1","none","1","cake","0","xmas");
		assertFalse(toTest.updateOrder(id, order));
	}		

	@Test
	public void testProcessOrder_goodProcess() {
		int id=toTest.getNextOrderNumber();
		String orderString = String.join(":", Integer.toString(userId),Integer.toString(id),"1","cookie","1","none","2","cake","0","xmas");
		LocalDateTime ldt = LocalDateTime.now();
		toTest.addOrders(id, orderString);
		
		boolean processed = toTest.processOrder(id, ldt);
		ResultSet rs = getOrdersFromDb();
		try {
			while(rs.next()) {
				int fromDbInt = rs.getInt("orderId");
				String fromDbString = rs.getString("orderString");
				if(fromDbInt == id) {
					if(fromDbString.contains("not processed")) {
						fail("Entry was not marked as processed in order table.");
					} else {
						break;
					}
				}
				if(rs.isAfterLast()) {
					fail("Entry was not found in order table.");
				}
			}
		}catch(SQLException e) {
			fail(e.getMessage());
		}
		assertTrue(processed);
	}
	
	@Test
	public void testProcessOrder_invalidId() {
		assertFalse(toTest.processOrder(9999, LocalDateTime.now()));
	}
	
	@Test
	public void testProcessOrder_orderDNE() {
		int id = toTest.getNextOrderNumber();
		assertFalse(toTest.processOrder(id, LocalDateTime.now()));
	}
	
	@Test
	public void testProcessOrder_nullDate() {
		int id = toTest.getNextOrderNumber();
		assertFalse(toTest.processOrder(id, null));
	}

	@Test
	public void testGetOrderItems() {
		int id=toTest.getNextOrderNumber();
		String orderString = String.join(":", Integer.toString(userId),Integer.toString(id),"1","cookie","1","none","2","cake","0","xmas");		
		toTest.addOrders(id, orderString);		
		assertNotNull(toTest.GetOrderItems(id));
	}

	@Test
	public void testGetNextOrderNumber() {
		int id1 = toTest.getNextOrderNumber();
		int id2 = toTest.getNextOrderNumber();
		assertTrue(id1<id2 && id1>=0 && id2>0);
	}

	@Test
	public void testFindOrders() {
		int id=toTest.getNextOrderNumber();
		String orderString = String.join(":", Integer.toString(userId),Integer.toString(id),"1","cookie","1","none","2","cake","0","xmas");		
		toTest.addOrders(id, orderString);
		ArrayList<Integer> orderIds = toTest.findOrders(userId, null, null);
		boolean hasValues = orderIds.size() > 0;
		assertTrue(hasValues);
	}

	   private static Connection getConnection() {		   
		   Connection conn = null;
		   try {
			conn = DriverManager.getConnection("jdbc:sqlite:test.db");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		   return conn;
	   }
	   
	   private ResultSet getOrdersFromDb() {
		   String sql = "SELECT orderId, orderString FROM BakeryOrders";
		   try(Connection conn = getConnection();
				   Statement stmt = conn.createStatement();
				   ResultSet rs = stmt.executeQuery(sql)){
			   return rs;
		   }catch (SQLException e){
			   System.out.println(e.getMessage());
			   return null;
		   }
	   }	   
	   
	   private static void initializeUsersDb() {		   
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
	   
	   //Creates SQLite database table
	   private static void initializeOrdersDb() {		   
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
}

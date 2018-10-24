package bakery.test;

import static org.junit.Assert.*;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import bakery.classes.Bakery_BEnd_Users;

public class TestBakeryUsers {

	static Bakery_BEnd_Users toTest;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		File testDb = new File("test.db");
		if(testDb.exists()) {
			testDb.delete();
		}		
		initializeDb();
		toTest = Bakery_BEnd_Users.getInstance();			
		toTest.setUrl("jdbc:sqlite:test.db");
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
	public void testAddUser_validEntry() {
		String toInsert = "test test:test@test.test";
		int id = toTest.getNextUserIdNumber();
		boolean inserted = toTest.addUser(id, toInsert);
		ResultSet rs = getUsersFromDb();
		
		try {
			while(rs.next()){
				String fromDb = rs.getString("userString");
				if(fromDb.equals(toInsert)) {
					break;
				}
				if(rs.isAfterLast()) {
					fail("Entry was not properly inserted into user table.");
				}
			}
		}catch(SQLException e) {
			fail(e.getMessage());
			
		}
		assertTrue(inserted);
	}
	
	@Test
	public void testAddUser_invalidId() {
		int id=9999;
		String toInsert = "this:shouldn't@matter";
		assertFalse(toTest.addUser(id, toInsert));
	}
	
	@Test
	public void testAddUser_duplicateId() {
		int id=toTest.getNextUserIdNumber();
		String toInsert1 = "this:shouldn't@matter";
		String toInsert2 = "neither:should@this";
		
		toTest.addUser(id, toInsert1);
		assertFalse(toTest.addUser(id, toInsert2));
	}
	
	@Test
	public void testAddUser_duplicateUser() {
		int id1=toTest.getNextUserIdNumber();
		int id2=toTest.getNextUserIdNumber();
		String toInsert = "this:shouldnot@matter";		
		
		toTest.addUser(id1, toInsert);
		assertFalse(toTest.addUser(id2, toInsert));
	}		

	@Test
	public void testRemoveUser_GoodDeletion() {
		int id=toTest.getNextUserIdNumber();
		String toInsert = "delete:this@fool";
		toTest.addUser(id, toInsert);
		
		boolean deleted = toTest.removeUser(id);
		ResultSet rs = getUsersFromDb();		
		try {
			while(rs.next()){
				String fromDb = rs.getString("userString");
				if(fromDb.equals(toInsert)) {
					fail("Entry not deleted from user table.");
				}
			}
		}catch(SQLException e) {
			fail(e.getMessage());
			
		}
		assertTrue(deleted);
	}
	
	@Test
	public void testRemoveUser_invalidId() {
		assertFalse(toTest.removeUser(9999));
	}
	
	public void testRemoveUser_userDNE() {
		int toDel = toTest.getNextUserIdNumber();		
		assertFalse(toTest.removeUser(toDel));
	}

	@Test
	public void testUpdateUser_goodUpdate() {
		int id=toTest.getNextUserIdNumber();
		String toInsert = "update:this@fool";
		String updatedString = "updated:this@fool";
		toTest.addUser(id, toInsert);
		
		boolean updated = toTest.updateUser(id, updatedString);
		ResultSet rs = getUsersFromDb();		
		try {
			while(rs.next()){
				int fromDbInt = rs.getInt("userId");
				String fromDbString = rs.getString("userString");
				if(fromDbInt == id &&fromDbString.equals(updatedString)) {
					break;
				}
				if(rs.isAfterLast()) {
					fail("Entry was not properly updated in user table.");
				}
			}
		}catch(SQLException e) {
			fail(e.getMessage());
			
		}
		assertTrue(updated);
	}
	
	@Test
	public void testUpdateUser_invalidId() {
		assertFalse(toTest.updateUser(9999, "This:oughtNot@matter"));
	}
	
	@Test
	public void testUpdateUser_userDNE() {
		toTest.getNextUserIdNumber();
		int toUpdate = toTest.getNextUserIdNumber();
		assertFalse(toTest.updateUser(toUpdate, "ThisOne:shouldReally@notMatter"));
	}
	
	@Test
	public void testUpdateUser_conflictingUpdate() {
		int id1 = toTest.getNextUserIdNumber();
		int id2 = toTest.getNextUserIdNumber();
		
		String userString1 = "first:gotHere@first";
		String userString2 = "second:gotHere@second";
		
		toTest.addUser(id1, userString1);
		toTest.addUser(id2, userString2);
		
		assertFalse(toTest.updateUser(id2, userString1));
	}

	@Test
	public void testFindUser_findByName() {
		int id=toTest.getNextUserIdNumber();
		String toInsert = "hide"+id+":and@seek"+id;
		toTest.addUser(id, toInsert);
		ArrayList<Integer> results = toTest.findUser("hide"+id, null);
		boolean sizeBool = results.size() == 1;
		boolean valueBool = results.get(0) == id;
		
		assertTrue(sizeBool && valueBool);
	}
	
	@Test
	public void testFindUser_findByEmail() {
		int id=toTest.getNextUserIdNumber();
		String toInsert = "hide"+id+":and@seek"+id;
		toTest.addUser(id, toInsert);
		ArrayList<Integer> results = toTest.findUser(null, "and@seek"+id);
		boolean sizeBool = results.size() == 1;
		boolean valueBool = results.get(0) == id;
		
		assertTrue(sizeBool && valueBool);
	}

	@Test
	public void testFindUser_findByBoth() {
		int id=toTest.getNextUserIdNumber();
		String toInsert = "hide"+id+":and@seek"+id;
		toTest.addUser(id, toInsert);
		ArrayList<Integer> results = toTest.findUser("hide"+id, "and@seek"+id);
		boolean sizeBool = results.size() == 1;
		boolean valueBool = results.get(0) == id;
		
		assertTrue(sizeBool && valueBool);
	}
	
	@Test
	public void testIsValidId() {
		int validId = toTest.getNextUserIdNumber();
		int negativeId = -1;
		int overId = 9999;
		
		boolean shouldBeValid = Bakery_BEnd_Users.isValidId(validId);
		boolean negativeInvalid = Bakery_BEnd_Users.isValidId(negativeId);
		boolean overInvalid = Bakery_BEnd_Users.isValidId(overId);
		
		assertTrue((shouldBeValid) && (!negativeInvalid) && (!overInvalid));
	}

	@Test
	public void testGetNextOrderNumber() {
		int id1 = toTest.getNextUserIdNumber();
		int id2 = toTest.getNextUserIdNumber();
		
		assertTrue((id1 < id2) && Bakery_BEnd_Users.isValidId(id1) && Bakery_BEnd_Users.isValidId(id2));
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
   
   private ResultSet getUsersFromDb() {
	   String sql = "SELECT userId, userString FROM BakeryUsers";
	   try(Connection conn = getConnection();
			   Statement stmt = conn.createStatement();
			   ResultSet rs = stmt.executeQuery(sql)){
		   return rs;
	   }catch (SQLException e){
		   System.out.println(e.getMessage());
		   return null;
	   }
   }
   
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
}

package bakery.classes;
import java.time.LocalDate;
//A class intended to read and parse information from order strings.
public class OrderStringReader { 
	public static void readOrderString(String orderString, BakeryOrders orders) {
		
		CakeNameRepository cakenr = new CakeNameRepository();
		CookieNameRepository cookienr = new CookieNameRepository();
		
		String[] orderSplit = orderString.split(":");
		String name=orderSplit[0];
		String orderNum = orderSplit[1];
		int cookieQuantity = Integer.parseInt(orderSplit[2]);
		String cookie = orderSplit[3];
		int cookieBoxType = Integer.parseInt(orderSplit[4]);
		String cookieDecoration = orderSplit[5];
		int cakeQuantity = Integer.parseInt(orderSplit[6]);
		String cake=orderSplit[7];
		int cakeSizeType = Integer.parseInt(orderSplit[8]);
		String cakeDecoration = orderSplit[9];
		
		Name user = new Name("tempID","name","lastName","email","phone");					
		
		Cookie cookieOrder = (cookieQuantity != 0) ? cookienr.GenerateCookie(cookieQuantity, cookieBoxType, "chocolate chip", cookieDecoration) : null;
		Cake cakeOrder = (cakeQuantity != 0) ? cakenr.GenerateCake(cakeQuantity, cakeSizeType, "vanilla", "chocolate", cakeDecoration) : null;
		
		orders.AddOrder(orderNum, user, LocalDate.now(), cakeOrder, cookieOrder, 0.00, null, false);
	}
}

package bakery.classes;
import java.time.LocalDateTime;
//A class to command the oven to process a given order specified by the order ID.
public class ProcessOrders implements command_appliance {

	int orderToProcess;
	
	public ProcessOrders(int order) {
		orderToProcess = order;
	}
	//Gets instance of orders singleton and uses that to get cookie and cake instances,
	//creates processed timestamp and labels order as processed.
	@Override
	public void regulate_oven() {		
		Bakery_BEnd_Orders bakeryOrders = Bakery_BEnd_Orders.getInstance();				//Get orders singleton instance
		LocalDateTime dt = LocalDateTime.now();											//Create LocalDateTime for timestamp
		CakeCookieOrderHolder bakedGoods = bakeryOrders.GetOrderItems(orderToProcess);	//Get specified order's cake and cookie
		dt = bakedGoods.cookie.bake_350degrees(dt);										//"Bake" cookie and edit timestamp
		dt = bakedGoods.cake.bake_350degrees(dt);										//"Bake" cake and edit timestamp
		bakeryOrders.processOrder(orderToProcess, dt);									//Mark the order as processed with timestamp.
	}

}

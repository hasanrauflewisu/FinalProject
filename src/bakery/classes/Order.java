package bakery.classes;
import java.time.*;

/*A class intended to hold information about orders*/
public class Order {

	public String orderId;
	public LocalDate dateOrdered;
	public LocalDate dateCompleted;	
	public Cake cakeOrdered;
	public Cookie cookieOrdered;
	public Name name;
	public double cost;
	public boolean processed;
	
	public Order(String orderId, Name name, LocalDate dateOrdered, Cake cakeOrdered, Cookie cookieOrdered, double cost, LocalDate dateCompleted, boolean processed) {
		setOrderId(orderId);
		setName(name);
		setDateOrdered(dateOrdered);
		setCakeOrdered(cakeOrdered);
		setCookieOrdered(cookieOrdered);
		setDateCompleted(dateCompleted);
		setCost(cost);
		setProcessed(processed);
	}		
	
	public void print() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("This order was made by "+name.firstName +" "+name.lastName+".\n");
		sBuilder.append("This order was made on "+dateOrdered.toString()+".\n");
		if(this.dateCompleted == null) {
			sBuilder.append("This order has not been completed.\n");
		}else {
			sBuilder.append("This order was completed on "+dateCompleted.toString()+".\n");
		}
		if(this.cakeOrdered != null) {
			sBuilder.append("This order has "+cakeOrdered.toString()+"\n");
		}
		if(this.cookieOrdered != null) {
			sBuilder.append("This order has "+cookieOrdered.toString()+"\n");
		}
		if(this.cost > 0.0) {
			sBuilder.append("This order costs $"+cost+".\n");
		}
		System.out.println(sBuilder.toString());
	}
	
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public LocalDate getDateOrdered() {
		return dateOrdered;
	}

	public void setDateOrdered(LocalDate dateOrdered) {
		this.dateOrdered = dateOrdered;
	}

	public LocalDate getDateCompleted() {
		return dateCompleted;
	}

	public void setDateCompleted(LocalDate dateCompleted) {
		this.dateCompleted = dateCompleted;
	}

	public Cake getCakeOrdered() {
		return cakeOrdered;
	}

	public void setCakeOrdered(Cake cakeOrdered) {
		this.cakeOrdered = cakeOrdered;
	}

	public Cookie getCookieOrdered() {
		return cookieOrdered;
	}

	public void setCookieOrdered(Cookie cookieOrdered) {
		this.cookieOrdered = cookieOrdered;
	}

	public Name getName() {
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public boolean isProcessed() {
		return processed;
	}

	public void setProcessed(boolean processed) {
		this.processed = processed;
	}
}

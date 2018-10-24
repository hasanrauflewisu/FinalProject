package bakery.classes;
import java.time.LocalDateTime;

///
//   Cake  Base class inherites bake base class
///
//A cake class which can be ordered.
public class Cake implements Bake {
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public CakeSizeEnum getSize() {
		return size;
	}

	public void setSize(CakeSizeEnum size) {
		this.size = size;
	}

	public String getBorder() {
		return border;
	}

	public void setBorder(String border) {
		this.border = border;
	}

	public String getFilling() {
		return filling;
	}

	public void setFilling(String filling) {
		this.filling = filling;
	}
	
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int quantity;
	public String type;
	public CakeSizeEnum size;
	public String border;
	public String filling;
	
	public Cake() {
		this(1,0,"vanilla","flower","chocolate");
	}
	
	public Cake(int quantity, int size, String type, String border, String filling) {
		setQuantity(quantity);
		setSize(CakeSizeEnum.values()[size]);
		setType(type);
		setBorder(border);
		setFilling(filling);
	}
	
	//Edits a given LocalDateTime based on quantity of instance and returns it for use in processed timestamp.
	  @Override
	   public LocalDateTime bake_350degrees(LocalDateTime time) {
	      System.out.println("Baking "+quantity+" cake(s)");
	      for(int count = 1; count <= quantity; count++) {
	    	  time = time.plusMinutes(30);
	      }
	      return time;
	   }
	  
	  //Prints out class' toString();
	  public void print() {
		  System.out.println(toString());
	  }
	  
	  public String toString() {
		  if(quantity == 0) {
			  return ("No cakes");
		  }
		  return(quantity +" " + size.toString()+" "+ type +" cake with a "+ border +" border and "+ filling +" filling.");
	  }

	@Override
	public void bake_350degrees() {
		System.out.println("Dough Shape Type: Cake");
		
	}

}

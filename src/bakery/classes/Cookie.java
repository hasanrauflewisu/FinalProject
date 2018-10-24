package bakery.classes;
import java.time.LocalDateTime;

///
//   Cookie Class inherits from the Bake Base class
///
//A cookie class to be specified in an order.
public class Cookie implements Bake {
	
	public int getCount() {
		return quantity;
	}

	public void setCount(int count) {
		this.quantity = count;
	}

	public CookieSizeEnum getBoxSize() {
		return boxSize;
	}

	public void setBoxSize(CookieSizeEnum boxSize) {
		this.boxSize = boxSize;
	}

	public String getDecoration() {
		return decoration;
	}

	public void setDecoration(String decoration) {
		this.decoration = decoration;
	}

	public String getFlavor() {
		return flavor;
	}

	public void setFlavor(String flavor) {
		this.flavor = flavor;
	}

	public int quantity;
	public CookieSizeEnum boxSize;
	public String decoration;
	public String flavor;
	
	public Cookie() {
		this(1,0,"chocolate chip","smilie");
	}
	
	public Cookie(int count, int boxSize, String flavor, String decoration) {
		setCount(count);
		setBoxSize(CookieSizeEnum.values()[boxSize]);
		setFlavor(flavor);
		setDecoration(decoration);
	}

	//Edits a given LocalDateTime based on quantity of instance and returns it for use in processed timestamp.
   @Override
   public LocalDateTime bake_350degrees(LocalDateTime time) {
      System.out.println("Baking "+quantity+" batch(es) of cookies");
      for(int count = 1; count <= quantity; count++) {
    	  time = time.plusMinutes(20);
      }
      return time;
   }
   
	  //Prints out class' toString();
	  public void print() {
		  System.out.println(toString());
	  }
   
	  public String toString() {
		  if(quantity == 0) {
			  return ("No cookies");
		  }
		  return(quantity +" "+ boxSize.toString() +" of "+flavor+" cookies with "+ decoration +" decoration.");
	  }

	@Override
	public void bake_350degrees() {
		System.out.println("Dough Shape Type: Cookie");
		
	}
}

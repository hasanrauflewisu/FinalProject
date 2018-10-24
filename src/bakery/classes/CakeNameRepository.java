package bakery.classes;
import java.util.Arrays;
///
// Implementing CakeName Class which implements the Container interface,  the inner class NameIterator implements the Iterator interface
///
public class CakeNameRepository implements Container{
	public String names[] = {"vanilla", "chocolate"};
	public String fillings[] = {"chocolate", "vanilla"};
	public String decorations[] = {"flower", "birthday"};
	
	//Returns an iterator for this class.
	@Override
	public Iterator getIterator() {
		return new CakeNameIterator();
	}
	
	//Private iterator class.
	private class CakeNameIterator implements Iterator {
		int index;
		@Override
		public boolean hasNext() {
			if (index < names.length) {
				return true;
			}
			return false;
		}
		
		@Override
		public Object next() {
		   if (this.hasNext()) {
			   return names[index++];
		   }
		   return null;
		}		
	}
	
	//A method to create an instance of cake class. Performs checks on parameters before creating an
	//instance. If any of the parameters is not good, returns null.
	public Cake GenerateCake(int quantity, int size, String type, String filling, String decoration) {
		if(CakeSizeEnum.values().length <= size || size < 0) {
			System.out.println("Cakes not made in that size");
			return null;
		}
		if(!Arrays.asList(names).contains(type.toLowerCase())) {
			System.out.println("No "+type+" cakes found");
			return null;
		}
		if(!Arrays.asList(fillings).contains(filling.toLowerCase())) {
			System.out.println("No "+filling+" filling found");
			return null;
		}
		if(!Arrays.asList(decorations).contains(decoration)) {
			System.out.println("No "+decoration+" decoration found");
			return null;
		}
		ShapeFactory sf = new ShapeFactory();
		Cake toReturn = new Cake();
		return null;
	}
	
}

package bakery.classes;
import java.util.Arrays;

///
// Implementing CookieName Class which implements the Container interface,  the inner class NameIterator implements the Iterator interface
///

public class CookieNameRepository implements Container{
	public String colors[] = {"Brown", "Red"};
	public String decorations[] = {"flower", "birthday","smilie"};
	
	//Returns an iterator
	@Override
	public Iterator getIterator() {
		return new CookieNameIterator();
	}
	
	//A private iterator class.
	private class CookieNameIterator implements Iterator {
		int index;
		@Override
		public boolean hasNext() {
			if (index < colors.length) {
				return true;
			}
			return false;
		}
		
		@Override
		public Object next() {
		   if (this.hasNext()) {
			   return colors[index++];
		   }
		   return null;
		}
	}
	
	//A method to create an instance of cookie class. Performs checks on parameters before creating an
	//instance. If any of the parameters is not good, returns null.
	public Cookie GenerateCookie(int quantity, int boxSize, String flavor, String decoration) {
		if(CookieSizeEnum.values().length <= boxSize || boxSize < 0) {
			System.out.println("Box size not found. found found");
			return null;
		}
		if(Arrays.binarySearch(decorations, decoration.toLowerCase()) < 0) {
			System.out.println("No "+decoration+" decoration found");
			return null;
		}
		ShapeFactory sf = new ShapeFactory();
		Cookie toReturn = new Cookie();
		return toReturn;
	}
	
}

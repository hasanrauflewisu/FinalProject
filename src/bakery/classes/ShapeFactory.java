package bakery.classes;
///
//   ShapeFactory   shape factory base class , which instantiates Cookie or Cake class given the input
/// 
public class ShapeFactory {

	 //use getShape method to get object of type shape 
	   public Shape getShape(String shapeType){
	      if(shapeType == null){
	         return null;
	      }		
	      if(shapeType.equalsIgnoreCase("COOKIE")){
	         return new CookieShape();
	         
	      } else if(shapeType.equalsIgnoreCase("CAKE")){
	         return new CakeShape();
	         
	      } 
	      
	      return null;
	   }
	   
	   //Creates an instance of cake or cookie class, specified by the first parameter. Other parameters are given by an order string.
	   public Bake getBake(String bakeType, String bakeQuantity, String bakeSize, String flavor, String decoration, String filling) {
	      if(bakeType == null){
	         return null;
	      }
	      int bakeQuantityInt = Integer.parseInt(bakeQuantity);
	      int bakeSizeInt = Integer.parseInt(bakeSize);
	      if(bakeType.equalsIgnoreCase("COOKIE")){
	         return new Cookie(bakeQuantityInt, bakeSizeInt, flavor, decoration);
	         
	      } else if(bakeType.equalsIgnoreCase("CAKE")){
	         return new Cake(bakeQuantityInt, bakeSizeInt, flavor, decoration, filling);
	         
	      } 
	      
	      return null;
	   }

}

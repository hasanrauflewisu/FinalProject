package bakery.classes;
import java.time.LocalDateTime;

///
//  RedBorder Decorator Extends base BorderDecorator class
///
public class RedBorderDecorator extends BorderDecorator {

   public RedBorderDecorator(Bake mdecoratedBorder) {
      super(mdecoratedBorder);		
   }

   @Override
   public LocalDateTime bake_350degrees(LocalDateTime dateTime) {
	   decoratedBorder.bake_350degrees();	       
      setRedBorder(decoratedBorder);
      return LocalDateTime.now();
   }

   private void setRedBorder(Bake decoratedShape){
      System.out.println("Border Color: Red");
   }

@Override
	public void bake_350degrees() {
	   decoratedBorder.bake_350degrees();	       
       setRedBorder(decoratedBorder);	
	}
}
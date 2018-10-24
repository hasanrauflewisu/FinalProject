package bakery.classes;
import java.time.LocalDateTime;

///
//  The Decorator Pattern , which inherits the Bake Class into the Border Decoration for the Baking example
///
public abstract class BorderDecorator implements Bake {
   protected Bake decoratedBorder;

   public BorderDecorator(Bake mBakeBorder){
      this.decoratedBorder = mBakeBorder;
   }

   public void bake_350(){
	   decoratedBorder.bake_350degrees(LocalDateTime.now());
   }	
}
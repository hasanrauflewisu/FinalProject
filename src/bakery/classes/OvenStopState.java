package bakery.classes;
///
//  GE oven Stop State concrete class 
///
public class OvenStopState implements GEOven_State {

	public void findGEOven_State(GEOvenContext gEOvenContext) {
		
		System.out.println("The Oven is in Stop State");
		gEOvenContext.setGEOvenState(this);
	}
	
	public String toString() {
		return "GEOven is in Stop State";
	}
}

package bakery.classes;
///
//  GE oven Start State concrete class 
///
public class OvenStartState implements GEOven_State{
	
	public void findGEOven_State(GEOvenContext gEOvenContext) {
		
		System.out.println("The Oven is in Start State");
		gEOvenContext.setGEOvenState(this);
	}
	
	public String toString() {
		return "GEOven is in Start State";
	}

}

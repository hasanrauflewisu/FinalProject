package bakery.classes;
///
//  GE oven   State Context class 
///
public class GEOvenContext {
	private GEOven_State gEOven_State;
	
	public GEOvenContext() {
		gEOven_State = null;
	}
	
	public void setGEOvenState( GEOven_State gEOven_State) {
		this.gEOven_State = gEOven_State;
	}
	
	public GEOven_State getGEOvenState() {
		return (gEOven_State);
	}

}

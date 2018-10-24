package bakery.classes;
///
//  creating  concrete classes SwitchOffOven for the command interface for the pattern 
///
public class SwitchOffOven implements command_appliance {
	private Bakery_Appliances bake_appliances;
	
	public SwitchOffOven(Bakery_Appliances bake_appliances) {
		this.bake_appliances = bake_appliances;
	}
	
	@Override 
	public void regulate_oven() {
		bake_appliances.switchOff();
		
	}
}

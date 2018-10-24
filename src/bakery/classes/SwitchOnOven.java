package bakery.classes;
///
//  creating  concrete classes SwitchOnOven for the command interface for the pattern 
///
public class SwitchOnOven implements command_appliance {

	private Bakery_Appliances bake_appliances;
	
	public SwitchOnOven(Bakery_Appliances bake_appliances) {
		this.bake_appliances = bake_appliances;
	}
	
	@Override 
	public void regulate_oven() {
		bake_appliances.switchOn();
		
	}
}

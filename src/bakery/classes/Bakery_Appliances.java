package bakery.classes;

///
//  Create a request class  for the command pattern Used in Baking
///
public class Bakery_Appliances {
	private String name = "GEOven";
	private int    presettemperatureOn = 350;
	private int    presettemperatureOff = 0;
	
	public void switchOn() {
		System.out.println("Oven [ Name: " + name + ", Set Temperature: " + presettemperatureOn + "] Switched On");
	}
	
	public void switchOff() {
		System.out.println("Oven [ Name: " + name + ", Set Temperature: " + presettemperatureOff + "] Switched Off");
	}
}

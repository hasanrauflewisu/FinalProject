package bakery.classes;
///
//  creating  command invoke class to SwitchOn /  SwitchOff Oven for the command  pattern used in Baking Example
///

import java.util.ArrayList;
import java.util.List;

//A class to manage and execute commands given to the oven.
public class BakingOvenRegulate {
	
	private List<command_appliance>  commandOvenList = new ArrayList<command_appliance>(); 
	 
	public void takeCommand(command_appliance command){
		commandOvenList.add(command);		
	}
 
	public void executeCommands(){
		for (command_appliance command : commandOvenList) {
			command.regulate_oven();
		}
		commandOvenList.clear();
	}
}

package bakery.classes;
//An enum to specify the available sizes of cookies available
public enum CookieSizeEnum {
	boxOf8,
	boxOf16;
	
	@Override
	public String toString() {
		switch (this){
			case boxOf8: return "8-count box";
			case boxOf16: return "16-count box";
			default: throw new IllegalArgumentException();
		}
	}
}

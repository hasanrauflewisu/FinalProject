package bakery.classes;
//An enum class to specify the sizes of cakes available
public enum CakeSizeEnum {
	smallCake,
	largeCake;
	
	@Override
	public String toString() {
		switch(this) {
			case smallCake : return "small sized";
			case largeCake : return "large sized";
			default : throw new IllegalArgumentException();
		}
	}
}

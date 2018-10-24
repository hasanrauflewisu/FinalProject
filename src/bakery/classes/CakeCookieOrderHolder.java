package bakery.classes;
//A container class to hold cookie and cake instances
//to be given at the same time.
public class CakeCookieOrderHolder {
	public final Cake cake;
	public final Cookie cookie;
	public CakeCookieOrderHolder(Cake cake, Cookie cookie) {
		this.cake = cake;
		this.cookie = cookie;
	}

}

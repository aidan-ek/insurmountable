package insurmountable;

public class Time {
	private static long currentTime;
	
	public static void update() {
		currentTime = System.currentTimeMillis();
	}
	
	public static long getTime() {
		return currentTime;
	}
	
	public static long since(long input) {
		return currentTime - input;
	}
}

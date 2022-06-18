package insurmountable;
/**
 * [Time.java]
 * Class to manage time
 * @author Mohammad/Aiden
 * Date June 14, 2022
 */
public class Time {
	//Declare variables
	private static long currentTime;
	
	//Create time update
	public static void update() {
		//Set time to current time
		currentTime = System.currentTimeMillis();
	}
	
	//Create getter
	public static long getTime() {
		return currentTime;
	}
	
	//Send time passed
	public static long since(long input) {
		return currentTime - input;
	}
}

package wcg.client;

public class AuxMethods {
	public static int numberOfPlayers(String name) {
		return "WAR".equals(name) ? 2 : 4;
	}
}

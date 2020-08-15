package neueda.main;

import neueda.ui.Menu;

/**
 * @author Kevin Niland
 * @category Runner
 * @version 1.0
 *
 */
public class Runner {
	/**
	 * Entry point into application. Calls menu() from Menu.java
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		new Menu().menu();
	}
}

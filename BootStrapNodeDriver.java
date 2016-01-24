


/**
 * This is driver class for the BootStrap Driver and it is being used for creating the concrete object of BootStrapNodeImplementation class.
 * @author Vinayak Subhash Pingale
 * @since September 12th 2015
 * @version 1.0
 *
 */
public class BootStrapNodeDriver {
	public static void main(String[] args) {
		/**
		 * The main task of this properties object is to determine the special property which are defined in the configuration properties file so that the needed 
		 * properties can be called on demand from the particular file.
		 */
		ReadWriteObject readwriteObject = new ReadWriteObject();
		BootStrapNodeImplementation bootStrapImplementation = new BootStrapNodeImplementation(readwriteObject);
		new Thread(bootStrapImplementation).start();
	}
}

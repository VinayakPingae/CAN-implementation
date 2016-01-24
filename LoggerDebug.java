

/**
 * Description - Logger class has been defined for providing a logger level to this particular Bug movement application
 * It provides a method called as printToStdOut for printing the messages by defining the debug level 
 * and the message which has been provided as a parameter to it.
 * 
 * @Author Vinayak Subhash Pingale
 * @Version 1.0
 * @Since April 18th 2015
 * 
 */

public class LoggerDebug {
		
		private static int DEBUG_VALUE = 0;
		private volatile static LoggerDebug debugInstance = null;
		private LoggerDebug() {
		
		}
		/**
		 * Double check locking mechanism for checking the singleton pattern for logger instance 
		 * @return
		 */
		public static LoggerDebug getInstance() {
			if(debugInstance == null) {
				synchronized (LoggerDebug.class) {
					if(debugInstance == null) {
						debugInstance = new LoggerDebug();
					}
				}
			}
			return debugInstance;
		}
		
		/**
		 * printToStdout - This method has been used to print the debug levels for the Application
		 * @param debguglevel - For getting the debug level from the Command Line arguments
		 * @param message - to print the message that has been passed
		 * 
		 */
		public void printToStdout(int debugLevel, String message) {
			if(debugLevel == 1) {
				System.out.println(message);
			}
			else if(DEBUG_VALUE == debugLevel) {
				System.out.println(message);
			}  
		 }
		
		public static int getDEBUG_VALUE() {
			return DEBUG_VALUE;
		}
		public static void setDEBUG_VALUE(int debug_value) {
			DEBUG_VALUE = debug_value;
		}
		
}

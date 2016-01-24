

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

public class Property {
	private volatile static Property propertiesInstance = null;
	private static Properties properties = null;

	private Property() {
		
	}

	/**
	 * Double check locking mechanism for checking the singleton pattern for
	 * logger instance
	 * 
	 * @return
	 */
	public static Property getInstance() {
		if (propertiesInstance == null) {
			synchronized (Property.class) {
				if (propertiesInstance == null) {
					propertiesInstance = new Property();
				}
			}
		}
		return propertiesInstance;
	}
	
	public static int randInt(int min, int max) {

	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}

	
	public static Properties getProperty() {
		InputStream inputFileStream = null;
		properties= new Properties();
		try {
			inputFileStream = new FileInputStream("config.properties");
			
			properties.load(inputFileStream);
			//debug(""+properties.getProperty("name"));
		} catch (FileNotFoundException e) {
			System.err
					.println("Error ocurred while loading the properties file from the project");
			System.exit(1);
		} catch (IOException e) {
			System.err
					.println("I/O exception has ocurred while loading the properties file from the project");
			System.exit(1);
		} finally {
			if (inputFileStream != null) {
				try {
					inputFileStream.close();
				} catch (IOException e) {
					System.err
							.println("I/O exception has ocurred while closing the input handler for the properties file from the project");
					System.exit(1);
				}
			}
		}
		return properties;
	}

}

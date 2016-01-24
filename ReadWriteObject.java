

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
/**
 * This class performs the task of serializing and de serializing the packets which has been transferred via UDP. 
 * @author Vinayak
 *
 */
public class ReadWriteObject {
	/**
	 * Serialize object is the which converts the object into the byte array upon receiving
	 * @param obj
	 * @return
	 */
	public byte[] serialize(Object obj)  {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    ObjectOutputStream os;
		try {
			os = new ObjectOutputStream(out);
			os.writeObject(obj);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return out.toByteArray();
	}
	/**
	 * De serialize function accepts the byte buffer and returns the object type for the whole application
	 * @param data
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public Object deserialize(byte[] data) throws ClassNotFoundException, IOException {
	    ByteArrayInputStream in = new ByteArrayInputStream(data);
	    ObjectInputStream is = null;
		try {
			is = new ObjectInputStream(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    try {
			return is.readObject();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return is.readObject();
	}
}

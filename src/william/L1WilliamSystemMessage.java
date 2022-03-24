package william;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1WilliamSystemMessage {
    private static Log _log = LogFactory.getLog(L1WilliamSystemMessage.class);
    private int _id;
    private String _message;

    public L1WilliamSystemMessage(int id, String message) {
        this._id = id;
        this._message = message;
    }

    public int getId() {
        return this._id;
    }

    public String getMessage() {
        return this._message;
    }

    public static String ShowMessage(int id) {
        L1WilliamSystemMessage System_Message = SystemMessage.getInstance().getTemplate(id);
        if (System_Message == null) {
            return "";
        }
        return System_Message.getMessage();
    }
}

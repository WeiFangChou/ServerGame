package william;

import java.util.Calendar;

public class L1SystemMessage {
    private int _id;
    private String _message;
    private Calendar _resettime;

    public int get_id() {
        return this._id;
    }

    public void set_id(int _id2) {
        this._id = _id2;
    }

    public String get_message() {
        return this._message;
    }

    public void set_message(String _message2) {
        this._message = _message2;
    }

    public Calendar get_resettime() {
        return this._resettime;
    }

    public void set_resettime(Calendar _resettime2) {
        this._resettime = _resettime2;
    }
}

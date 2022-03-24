package william;

import com.lineage.DatabaseFactory;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Plug_Question {
    private static Plug_Question _instance;
    private static Logger _log = Logger.getLogger(Plug_Question.class.getName());
    private static final Log _logx = LogFactory.getLog(Plug_Question.class.getName());
    public static final HashMap<Integer, Plug_Question> _questionIdIndex = new HashMap<>();
    private String _answers;
    private int _id;
    private String _question;

    public static Plug_Question getInstance() {
        if (_instance == null) {
            _instance = new Plug_Question();
        }
        return _instance;
    }

    public static void reload() {
        _questionIdIndex.clear();
        _instance = null;
        getInstance();
    }

    private Plug_Question() {
        loadChackSerial();
    }

    private static void loadChackSerial() {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM 外掛提問");
            rs = pstm.executeQuery();
            fillChackSerial(rs);
        } catch (SQLException e) {
            _log.log(Level.SEVERE, "error while creating 外掛提問 table", (Throwable) e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    private static void fillChackSerial(ResultSet rs) throws SQLException {
        PerformanceTimer timer = new PerformanceTimer();
        int id = 0;
        while (rs.next()) {
            _questionIdIndex.put(Integer.valueOf(id), new Plug_Question(id, rs.getString("question"), rs.getString("answers")));
            id++;
        }
        _logx.info("載入AI外掛提問數據資料數量: " + _questionIdIndex.size() + "(" + timer.get() + "ms)");
    }

    public Plug_Question[] getQuestionList() {
        return (Plug_Question[]) _questionIdIndex.values().toArray(new Plug_Question[_questionIdIndex.size()]);
    }

    public Plug_Question getTemplate(int i) {
        return _questionIdIndex.get(Integer.valueOf(i));
    }

    public int getId() {
        return this._id;
    }

    public String getQuestion() {
        return this._question;
    }

    public String getAnswers() {
        return this._answers;
    }

    public Plug_Question(int id, String question, String answers) {
        this._id = id;
        this._question = question;
        this._answers = answers;
    }
}

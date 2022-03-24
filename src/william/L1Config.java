package william;

import com.lineage.server.model.skill.L1SkillId;
import java.util.Calendar;

public class L1Config {
    public static boolean _2226 = L1SystemMessageTable.get().getTemplate(2226).get_message().equals("true");
    public static int _2227 = Integer.valueOf(L1SystemMessageTable.get().getTemplate(2227).get_message()).intValue();
    public static boolean _4008 = L1SystemMessageTable.get().getTemplate(L1SkillId.I_LV30).get_message().equals("true");
    public static Calendar _4009 = L1SystemMessageTable.get().getTemplate(L1SkillId.ADLV80_1).get_resettime();
}

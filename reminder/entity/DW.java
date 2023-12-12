package reminder.entity;

/**
 * @author 86155
 **/
public class DW {
    String jiLu = "";
    int ciShu = 0;
    public DW (String time, int idx) {
        jiLu = time;
        ciShu = idx;
    }
    @Override
    public String toString() {
        return "今天第" + ciShu + "次喝水的时间为： " + jiLu;
    }
}

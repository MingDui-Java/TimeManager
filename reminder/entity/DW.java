package reminder.entity;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author 86155
 **/
public class DW implements Serializable {
    String jiLu = "";
    int ciShu = 0;
    LocalDate date = LocalDate.now();
    public DW (String time, int idx) {
        jiLu = time;
        ciShu = idx;
    }
    @Override
    public String toString() {
        return "今天第" + ciShu + "次喝水的时间为： " + jiLu;
    }
    public LocalDate getDate() {
        return date;
    }
}

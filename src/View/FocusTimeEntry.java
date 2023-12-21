package View;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 专注条目
 *
 * @author Keith
 * @version 1.0
 * @serial
 */
public class FocusTimeEntry implements Serializable {
    /**
     * 专注时长的标题、日期、时间
     */
    private String title;
    private LocalDate date;
    private Double minutes;

    /**
     * @param title   标题
     * @param date    日期
     * @param minutes     时间
     */
    public FocusTimeEntry(String title, LocalDate date, Double minutes) {
        this.title = title;
        this.date = date;
        this.minutes = minutes;
    }

    /**
     * @return 返回标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return 返回日期
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * @return 返回时间
     */
    public Double getMinutes() {
        return minutes;
    }

    /**
     * 若有相同标题的专注条目，则将时间合并
     * @param minutes  本次专注的时间
     */
    public void combineTime(double minutes) {
        this.minutes += minutes;
    }
}

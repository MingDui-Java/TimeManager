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
     * 专注时长的标题
     */
    private String title;
    /**
     * 专注时长的日期
     */
    private LocalDate date;
    /**
     * 专注时长的时间
     */
    private Double minutes;

    /**
     * 构造函数
     *
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
     * 返回标题
     *
     * @return 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 返回日期
     *
     * @return 日期
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * 得到时间
     *
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

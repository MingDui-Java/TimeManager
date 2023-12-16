package View;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author 15252
 */
public class FocusTimeEntry implements Serializable {

    private String title;
    private LocalDate date;
    private Double minutes;

    public FocusTimeEntry(String title, LocalDate date, Double minutes) {
        this.title = title;
        this.date = date;
        this.minutes = minutes;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getDate() {
        return date;
    }

    public Double getMinutes() {
        return minutes;
    }

    public void combineTime(double minutes) {
        this.minutes += minutes;
    }
}

package Model;

import Yang.GetTodoButtonPanel;
import Yang.ToDos;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author 15252
 */
public class TodoItem implements Serializable {

    private String title;
    //private String description;
    private LocalDate creationDay;
    private int remainingTime;
    private ToDos todo = null;

    public TodoItem(String title, int time) {
        this.title = title;
        //this.description = description;
        this.remainingTime = time * 60;
        creationDay = LocalDate.now();
    }
    public TodoItem(String title, int time, ToDos todo) {
        this.title = title;
        //this.description = description;
        this.remainingTime = time * 60;
        creationDay = LocalDate.now();
        this.todo = todo;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getCreationDay() {
        return creationDay;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    /*
    public String getDescription() {
        return description;
    }*/

    @Override
    public String toString() {
        return title + " - " + remainingTime / 60 + "minutes";
    }

    public ToDos getTodo() {
        return todo;
    }
}

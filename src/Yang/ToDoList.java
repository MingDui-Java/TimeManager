package Yang;
import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author 杨智方
 */
public class ToDoList implements Serializable {
    private String name;
    private JPanel bigListPanel;
    static ArrayList<ToDoList> toDoLists = new ArrayList<>();
    ArrayList<ToDos> toDos;
    public ToDoList(){
        bigListPanel = new JPanel();
        toDos = new ArrayList<>();
    }
    public void setName(String name) {
        this.name = name;
    }
    public JPanel getBigListPanel(){
        return bigListPanel;
    }
    public void setBigListPanel(JPanel panel){
        bigListPanel = panel;
    }
    public String getName() {
        return name;
    }
}

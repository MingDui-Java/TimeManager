package Yang;
import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author 杨智方
 */
public class ToDoList implements Serializable {
    private String name;
    private JPanel bigListPanel;
    private JPanel smallListPanel;
    private JPanel listPanel;
    public static ArrayList<ToDos> toDos;

    public ToDoList(){
        bigListPanel = new JPanel();
        smallListPanel = new JPanel();
        listPanel = new JPanel(new BorderLayout());
        toDos = new ArrayList<>();
    }
    public void setName(String name) {
        this.name = name;
    }
    public JPanel getBigListPanel(){
        return bigListPanel;
    }
    public JPanel getListPanel(){
        return listPanel;
    }
    public JPanel getSmallListPanel(){
        return smallListPanel;
    }

    public String getName() {
        return name;
    }

}

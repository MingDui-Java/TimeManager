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
    static ArrayList<ToDoList> toDoLists = new ArrayList<>();
    public static ArrayList<ToDos> toDos;
    private SizeHandler sizeHandler;
    public ToDoList(){
        bigListPanel = new JPanel();
        smallListPanel = new JPanel();
        listPanel = new JPanel(new BorderLayout());
        toDos = new ArrayList<>();
        sizeHandler = new SizeHandler(this);
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
    public SizeHandler getSizeHandler(){
        return sizeHandler;
    }
    public void setBigListPanel(JPanel panel){
        bigListPanel = panel;
    }
    public String getName() {
        return name;
    }

}

package Yang;
import javax.swing.*;
import java.io.Serializable;

/**
 * @author 杨智方
 */
public class ToDos implements Serializable {
    private boolean finished;
    private JPanel todoPanel;
    private JLabel label;
    private String name;
    public ToDos(String name,JPanel todoPanel, boolean finished,JLabel label){
        this.todoPanel = todoPanel;
        this.finished = finished;
        this.label = label;
        this.name = name;
    }
    public void setFinished(){
        this.finished = true;
        this.label.setText("<html><s>" + label.getText() + "</s></html>");
    }
    public String getName(){
        return name;
    }
    public boolean isFinished(){
        return finished;
    }

    public JPanel getPanel() {
        return this.todoPanel;
    }

    public void setPanel(JPanel todoPanel) {
        this.todoPanel = todoPanel;
    }
}

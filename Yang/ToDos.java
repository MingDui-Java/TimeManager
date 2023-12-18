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
        if(!this.finished) this.label.setText(label.getText() + "（已完成）");
        this.finished = true;
    }
    public String getName(){
        return name;
    }
    public boolean isFinished(){
        return finished;
    }
    public void setLabel(JLabel label){
        this.label = label;
    }
    public JLabel getLabel(){
        return label;
    }
    public JPanel getPanel() {
        return this.todoPanel;
    }

    public void setPanel(JPanel todoPanel) {
        this.todoPanel = todoPanel;
    }
}

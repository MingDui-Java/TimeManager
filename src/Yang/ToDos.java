package Yang;

import javax.swing.*;
import java.io.Serializable;

public class ToDos implements Serializable {
    private boolean finished;
    private JPanel todoPanel;
    public ToDos(JPanel todoPanel, boolean finished){
        this.todoPanel = todoPanel;
        this.finished = finished;
    }
    public void setFinished(){
        this.finished = finished;
    }

    public JPanel getPanel() {
        return this.todoPanel;
    }

    public void setPanel(JPanel todoPanel) {
        this.todoPanel = todoPanel;
    }
}

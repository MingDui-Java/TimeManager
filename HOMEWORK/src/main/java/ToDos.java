import javax.swing.*;

public class ToDos {
    private boolean finished;
    private JPanel todoPanel;
    public ToDos(JPanel todoPanel, boolean finished){
        this.todoPanel = todoPanel;
        this.finished = finished;
    }
    public void setFinished(boolean finished){
        this.finished = finished;
    }

    public JPanel getPanel() {
        return this.todoPanel;
    }

    public void setPanel(JPanel todoPanel) {
        this.todoPanel = todoPanel;
    }
}

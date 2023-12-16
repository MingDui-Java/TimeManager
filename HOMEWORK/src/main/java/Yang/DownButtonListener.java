package Yang;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.io.Serializable;

// 实现 ActionListener 接口的具名类
class DownButtonListener implements ActionListener, Serializable {
    private JPanel buttonPanel;
    private JButton closeButton;
    private JButton downButton;
    private JPanel bigListPanel;
    private JPanel listPanel;

    public DownButtonListener(JPanel buttonPanel, JButton closeButton, JButton downButton,
                               JPanel bigListPanel, JPanel listPanel) {
        this.buttonPanel = buttonPanel;
        this.closeButton = closeButton;
        this.downButton = downButton;
        this.bigListPanel = bigListPanel;
        this.listPanel = listPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        buttonPanel.remove(downButton);
        buttonPanel.add(closeButton);
        buttonPanel.revalidate();
        buttonPanel.repaint();
        bigListPanel.add(listPanel);
        bigListPanel.revalidate();
        bigListPanel.repaint();
    }
}

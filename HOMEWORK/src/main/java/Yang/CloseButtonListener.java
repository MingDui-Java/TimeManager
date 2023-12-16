package Yang;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.io.Serializable;

/**
 * @author 杨智方
 */
class CloseButtonListener implements ActionListener, Serializable {
    private JPanel buttonPanel;
    private JButton closeButton;
    private JButton downButton;
    private JPanel bigListPanel;
    private JPanel listPanel;

    public CloseButtonListener(JPanel buttonPanel, JButton closeButton, JButton downButton,
                               JPanel bigListPanel, JPanel listPanel) {
        this.buttonPanel = buttonPanel;
        this.closeButton = closeButton;
        this.downButton = downButton;
        this.bigListPanel = bigListPanel;
        this.listPanel = listPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        buttonPanel.remove(closeButton);
        buttonPanel.add(downButton);
        buttonPanel.revalidate();
        buttonPanel.repaint();
        bigListPanel.remove(listPanel);
        bigListPanel.revalidate();
        bigListPanel.repaint();
    }
}

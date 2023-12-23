/**
 * 日历模块
 */
package CalendarUnit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.io.Serializable;

/**
 * CloseButtonListener 类实现 ActionListener 接口，用于处理收起待办集中的待办的动作,当用户点击收起按钮时，该待办集所持有的所有待办都将不再被展示在日视图上。
 * @author 杨智方
 * @version 1.0
 */
class CloseButtonListener implements ActionListener, Serializable {
    /**
     * 按钮面板
     */
    private JPanel buttonPanel;
    /**
     * 收起按钮
     */
    private JButton closeButton;
    /**
     * 打开按钮
     */
    private JButton downButton;
    /**
     * 包含待办集和其持有的待办的面板
     */
    private JPanel bigListPanel;
    /**
     * 包含一个待办集中所有待办的面板
     */
    private JPanel listPanel;
    /**
     * 创建包含待办事项管理按钮的面板。
     *
     * @param listPanel 包含一个待办集中所有待办的面板
     * @param bigListPanel 包含待办集和其持有的待办的面板
     * @param buttonPanel 按钮面板
     * @param downButton 打开按钮
     * @param closeButton 收起按钮
     */
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

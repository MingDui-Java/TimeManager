//package Yang;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import javax.swing.JButton;
//import javax.swing.JPanel;
//import java.io.Serializable;
//
///**
// * @author 杨智方
// */
//// 实现 ActionListener 接口的具名类
//class OKButtonListener implements ActionListener, Serializable {
//    private JPanel buttonPanel;
//    private JButton closeButton;
//    private JButton downButton;
//    private JPanel bigListPanel;
//    private JPanel listPanel;
//
//    public OKButtonListener(JPanel buttonPanel, JButton closeButton, JButton downButton,
//                               JPanel bigListPanel, JPanel listPanel) {
//        this.buttonPanel = buttonPanel;
//        this.closeButton = closeButton;
//        this.downButton = downButton;
//        this.bigListPanel = bigListPanel;
//        this.listPanel = listPanel;
//    }
//
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        buttonPanel.remove(closeButton);
//        buttonPanel.add(downButton);
//        buttonPanel.revalidate();
//        buttonPanel.repaint();
//        bigListPanel.remove(listPanel);
//        bigListPanel.revalidate();
//        bigListPanel.repaint();
//    }
//}

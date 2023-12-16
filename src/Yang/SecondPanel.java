package Yang;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import java.util.*;


public class SecondPanel extends JPanel implements Serializable {

    private final JPanel ToDoListPanel; // 待办集
    private final JPanel hintPanel;
    private final ArrayList<JPanel> list;
    private final JScrollPane scrollPane;
    public ArrayList<JPanel> getList(){
        return list;
    }
    private CalendarPanel calendarPanelReference;
    public SecondPanel(String defaultString, int fromCalendar) {
        setLayout(new BorderLayout());
        hintPanel = new JPanel();
        list = new ArrayList<>();
        hintPanel.add(new JLabel(defaultString));
        hintPanel.setVisible(true);
        ToDoListPanel = new JPanel(); // 待办集
        ToDoListPanel.setLayout(new BoxLayout(ToDoListPanel, BoxLayout.Y_AXIS)); // 设置待办集为垂直布局
        JButton addButton = new JButton("+");
        JButton moreButton = new JButton("...");
        String DEFAULT_STRING = "请输入待办集名称";
        JTextField textField = GetTextField.getTextField(DEFAULT_STRING);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame popupFrame = new JFrame("添加待办集");
                JPanel popupPanel = new JPanel();
                JButton okButton = new JButton("√");
                okButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (textField.getText().isEmpty()) {
                            JOptionPane.showMessageDialog(popupFrame, "输入不能为空", "提示", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            popupFrame.dispose();
                            remove(hintPanel);
                            add(scrollPane, BorderLayout.CENTER); // 添加scroll到待办集中
                            scrollPane.setVisible(true);
                            revalidate();
                            repaint();
                            for(JPanel panel:list){// 判断是否存在相同名称待办
                                Component[] components = panel.getComponents();
                                for(Component component: components){
                                    if(component instanceof JLabel label){
                                        if(textField.getText().equals(label.getText())){
                                            JFrame tip = new JFrame("提示");
                                            JLabel tipLabel = new JLabel("已经添加过名称相同的待办集啦");
                                            tip.add(tipLabel);
                                            tip.setSize(300, 100);
                                            int x = getParent().getX() + (getParent().getWidth() - tip.getWidth()) / 2;
                                            int y = getParent().getY() + (getParent().getHeight() - tip.getHeight()) / 2;
                                            tip.setLocation(x, y);
                                            tip.setVisible(true);
                                            return ;
                                        }
                                    }
                                }
                            }
                            JPanel bigListPanel = new JPanel();
                            bigListPanel.setLayout(new BoxLayout(bigListPanel, BoxLayout.Y_AXIS));
                            JPanel smallListPanel = new JPanel();
                            smallListPanel.setLayout(new BoxLayout(smallListPanel,BoxLayout.Y_AXIS));
                            smallListPanel.setVisible(true);
                            JPanel listPanel = new JPanel(new BorderLayout());
                            listPanel.setMaximumSize(new Dimension(1800,59));
                            listPanel.setMaximumSize(new Dimension(1800,59));
                            JLabel userInputLabel = new JLabel(textField.getText());
                            userInputLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // 左对齐
                            listPanel.add(userInputLabel, BorderLayout.WEST); // 将名称添加到待办的左侧
                            JPanel buttonPanel = GetTodoButtonPanel.getTodoButtonPanel(smallListPanel,bigListPanel,ToDoListPanel);
                            listPanel.add(buttonPanel, BorderLayout.EAST); // 将按钮添加到待办的右侧
                            bigListPanel.add(listPanel);
                            bigListPanel.add(smallListPanel);
                            bigListPanel.setVisible(true);
                            list.add(listPanel);//待办集里加入待办
                            ToDoListPanel.add(bigListPanel);
                            ToDoListPanel.revalidate();
                            ToDoListPanel.repaint();
                            revalidate();
                            repaint();
                        }
                    }
                });
                popupPanel.setLayout(new FlowLayout());
                popupPanel.add(textField);
                popupPanel.add(okButton);
                popupFrame.add(popupPanel);
                popupFrame.setSize(300, 100);
                int x = getParent().getX() + (getParent().getWidth() - popupFrame.getWidth()) / 2;
                int y = getParent().getY() + (getParent().getHeight() - popupFrame.getHeight()) / 2;
                popupFrame.setLocation(x, y);
                popupFrame.setVisible(true);
                textField.dispatchEvent(new FocusEvent(textField,FocusEvent.FOCUS_GAINED,true));
                textField.requestFocusInWindow();
                okButton.dispatchEvent(new FocusEvent(okButton,FocusEvent.FOCUS_GAINED,true));
                okButton.requestFocusInWindow();
            }
        });
        moreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        if(fromCalendar == 1){
            JButton returnButton = new JButton("返回");
            returnButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Container container = getParent();
                    if(container instanceof JPanel){
                        CardLayout cardLayout = (CardLayout) container.getLayout();
                        cardLayout.show(container, "CalendarPanel");
                    }
                }
            });
            topPanel.add(returnButton,FlowLayout.LEFT);
        }
        topPanel.add(addButton);
        topPanel.add(moreButton);
        add(topPanel, BorderLayout.NORTH);
        scrollPane = new JScrollPane(ToDoListPanel); // 把待办集加到scroll中
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(hintPanel, BorderLayout.CENTER);
    }
    public JScrollPane getScrollPane(){
        return scrollPane;
    }
    public static void receiveTodoFromTips(String name, int year, int month, int day){

    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("待办集");
        SecondPanel panel = new SecondPanel("当前没有待办集，点击右上角 + 号创建一个吧",0);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(panel);
        frame.setSize(300, 400);
        frame.setVisible(true);
    }
}
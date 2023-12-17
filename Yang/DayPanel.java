package Yang;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import java.util.*;

/**
 * 这个类代表待办事项列表的面板。
 * 继承自 JPanel，并提供了添加待办事项集合的功能。
 * 通过按钮操作和用户输入，可以管理待办事项集合。
 *
 * @author 杨智方
 */
public class DayPanel extends JPanel implements Serializable {
    /**
     * 待办事项集合的面板。
     */
    protected Box ToDoListPanel;

    /**
     * 提示面板，用于显示默认文本或其他提示信息。
     */
    protected JPanel hintPanel;

    /**
     * 待办事项集合的列表。
     */
    protected ArrayList<ToDoList> list;

    /**
     * 可滚动的面板，用于包含待办事项集合。
     */
    protected JScrollPane scrollPane;

    /**
     * 文本字段，用于用户输入。
     */
    protected JTextField textField;
    private JFrame popupFrame;
    private boolean flag;

    /**
     * 获取待办事项集合的列表。
     *
     * @return 返回待办事项集合的列表
     */
    public ArrayList<ToDoList> getList() {
        return list;
    }

    public DayPanel() {
        setLayout(new BorderLayout());
        flag = false;
        list = new ArrayList<>();

        hintPanel = new JPanel();
        String defaultString = "您在当天还没有代办，点击右上角 + 号创建一个吧";
        hintPanel.add(new JLabel(defaultString));
        hintPanel.setVisible(true);

        ToDoListPanel = Box.createVerticalBox();
        JButton addButton = new JButton("+");
        String DEFAULT_STRING = "请输入待办集名称";
        textField = GetTextField.getTextField(DEFAULT_STRING);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!flag) {
                    flag = true;
                    popupFrame = new JFrame("添加待办集");
                    JPanel popupPanel = new JPanel();
                    JButton okButton = new JButton("√");
                    textField = GetTextField.getTextField(DEFAULT_STRING);
                    okButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (textField.getText().isEmpty()) {
                                JOptionPane.showMessageDialog(popupFrame, "输入不能为空", "提示", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                popupFrame.dispose();
                                createTodoList(textField.getText());
                            }
                        }
                    });
                    popupPanel.setLayout(new FlowLayout());
                    popupPanel.add(textField);
                    popupPanel.add(okButton);
                    popupFrame.add(popupPanel);
                    popupFrame.setSize(300, 100);
                    CalendarPanel calendarPanel = CalendarPanel.getInstance();
                    int x = calendarPanel.getX() + (calendarPanel.getWidth() - popupFrame.getWidth()) / 2;
                    int y = calendarPanel.getY() + (calendarPanel.getHeight() - popupFrame.getHeight()) / 2;
                    popupFrame.setLocation(x, y);
                    popupFrame.setVisible(true);
                    textField.dispatchEvent(new FocusEvent(textField, FocusEvent.FOCUS_GAINED, true));
                    textField.requestFocusInWindow();
                    okButton.dispatchEvent(new FocusEvent(okButton, FocusEvent.FOCUS_GAINED, true));
                    okButton.requestFocusInWindow();
                }
            }
        });
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton returnButton = new JButton("返回");
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CalendarPanel calendarPanel = CalendarPanel.getInstance();
                calendarPanel.showMonthPanel();
            }
        });
        topPanel.add(returnButton, FlowLayout.LEFT);
        topPanel.add(addButton);
        add(topPanel, BorderLayout.NORTH);
        scrollPane = new JScrollPane(ToDoListPanel); // 把待办集加到scroll中
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(hintPanel, BorderLayout.CENTER);
    }
    public void setScrollPane(JScrollPane scrollPane){
        this.scrollPane = scrollPane;
    }
    public void createTodoList(String name){
        remove(hintPanel);
        add(scrollPane, BorderLayout.CENTER); // 添加scroll到待办集中
        scrollPane.setVisible(true);
        revalidate();
        repaint();
        for(ToDoList toDoList1:list){// 判断是否存在相同名称待办集
            if(name.equals(toDoList1.getName())) {
                JFrame tip = new JFrame("提示");
                JLabel tipLabel = new JLabel("已经添加过名称相同的待办集啦");
                tip.add(tipLabel);
                tip.setSize(300, 100);
                CalendarPanel calendarPanel = CalendarPanel.getInstance();
                int x = calendarPanel.getX() + (calendarPanel.getWidth() - tip.getWidth()) / 2;
                int y = calendarPanel.getY() + (calendarPanel.getHeight() - tip.getHeight()) / 2;
                tip.setLocation(x, y);
                tip.setVisible(true);
                return;
            }
        }
        ToDoList toDoList1 = new ToDoList();

        JPanel bigListPanel = toDoList1.getBigListPanel();
        bigListPanel.setLayout(new BoxLayout(bigListPanel, BoxLayout.Y_AXIS));

        JPanel smallListPanel = toDoList1.getSmallListPanel();
        smallListPanel.setLayout(new BoxLayout(smallListPanel,BoxLayout.Y_AXIS));
        smallListPanel.setVisible(true);

        JPanel listPanel = toDoList1.getListPanel();
        listPanel.setMaximumSize(new Dimension(1700,40));//[1]

        toDoList1.setName(name);
        JLabel userInputLabel = new JLabel(name);
        userInputLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // 左对齐
        listPanel.add(userInputLabel, BorderLayout.WEST); // 将名称添加到待办的左侧
        JPanel buttonPanel = GetTodoButtonPanel.getTodoButtonPanel(smallListPanel,bigListPanel,ToDoListPanel,list);
        listPanel.add(buttonPanel, BorderLayout.EAST); // 将按钮添加到待办的右侧
        bigListPanel.add(listPanel);
        bigListPanel.add(smallListPanel);
        bigListPanel.setVisible(true);
        list.add(toDoList1);//待办集里加入待办
        ToDoListPanel.add(bigListPanel);
        ToDoListPanel.revalidate();
        ToDoListPanel.repaint();
        revalidate();
        repaint();
    }
}
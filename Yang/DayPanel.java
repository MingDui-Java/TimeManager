package Yang;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import java.util.*;

import static Yang.AddButtonListener.predefinedColors;

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
     * 获取待办事项集合的列表。
     *
     * @return 返回待办事项集合的列表
     */
    public ArrayList<ToDoList> getList() {
        return list;
    }

    public DayPanel() {
        setLayout(new BorderLayout());
        list = new ArrayList<>();

        hintPanel = new JPanel();
        String defaultString = "您在当天还没有代办，点击右上角 + 号创建一个吧";
        hintPanel.add(new JLabel(defaultString));
        hintPanel.setVisible(true);

        ToDoListPanel = Box.createVerticalBox();
        JButton addButton = new JButton("+");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newName = JOptionPane.showInputDialog("添加待办集");
                if (newName.isEmpty()) {
                    JOptionPane.showMessageDialog(new JFrame(), "输入不能为空", "提示", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    for (ToDoList toDoList1 : list) {// 判断是否存在相同名称待办集
                        if (newName.equals(toDoList1.getName())) {
                            JOptionPane.showMessageDialog(new JFrame(),"已经添加过名称相同的待办集啦","提示",JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                    }
                    createTodoList(newName);
                }
            }
        });
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton returnButton = new JButton("返回");
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remove(scrollPane);
                add(hintPanel);
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

        ToDoList toDoList1 = new ToDoList(name);

        JPanel bigListPanel = toDoList1.getBigListPanel();
        bigListPanel.setLayout(new BoxLayout(bigListPanel, BoxLayout.Y_AXIS));

        JPanel smallListPanel = toDoList1.getSmallListPanel();
        smallListPanel.setLayout(new BoxLayout(smallListPanel,BoxLayout.Y_AXIS));

        smallListPanel.setVisible(true);

        JPanel listPanel = toDoList1.getListPanel();
        listPanel.setMaximumSize(new Dimension(1700,40));//[1]

        JLabel userInputLabel = new JLabel(name);
        userInputLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // 左对齐
        listPanel.add(userInputLabel, BorderLayout.WEST); // 将名称添加到待办的左侧
        JPanel buttonPanel = GetTodoButtonPanel.getTodoButtonPanel(smallListPanel,bigListPanel,ToDoListPanel,list,toDoList1);
        listPanel.add(buttonPanel, BorderLayout.EAST); // 将按钮添加到待办的右侧
        bigListPanel.add(listPanel);
        bigListPanel.add(smallListPanel);
        bigListPanel.setVisible(true);
        list.add(toDoList1);//待办集里加入待办
        ToDoListPanel.add(bigListPanel);
        ToDoListPanel.revalidate();
        ToDoListPanel.repaint();

        if(scrollPane!=null) remove(scrollPane);
        JScrollPane scrollPane = new JScrollPane(ToDoListPanel); // 把待办集加到scroll中
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        setScrollPane(scrollPane);
        add(scrollPane, BorderLayout.CENTER); // 添加scroll到待办集中
        scrollPane.revalidate();
        scrollPane.repaint();
        scrollPane.setVisible(true);
        revalidate();
        repaint();

    }
    public void createTodo(ToDoList List){
        for(ToDoList toDoList:list){
            if(toDoList.getName().equals(List.getName())){
                ArrayList<ToDos> toDosArrayList = List.toDos;
                if(toDosArrayList==null) {
                    System.out.println("fuc");
                    return;
                }
                for(ToDos todo: toDosArrayList) {
                    JPanel TodoPanel = new JPanel(new BorderLayout());
                    Random random = new Random();
                    Color rColor = predefinedColors[random.nextInt(predefinedColors.length)];
                    TodoPanel.setBackground(rColor);
                    TodoPanel.setMaximumSize(new Dimension(1700, 40));

                    JLabel userInputLabel = new JLabel(todo.getName());
                    userInputLabel.setForeground(Color.white);
                    userInputLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // 左对齐
                    TodoPanel.add(userInputLabel, BorderLayout.WEST); // 将名称添加到待办的左侧
                    todo.setLabel(userInputLabel);
                    if(todo.isFinished()){
                        todo.getLabel().setText(todo.getLabel().getText() + "(已完成）");
                    }
                    JPanel buttonPanel2 = new JPanel(new GridLayout(1, 2));

                    toDoList.toDos.add(todo);//待办集里加入待办
                    JButton startButton = new JButton("开始");
                    StartButtonListener startButtonListener = new StartButtonListener(buttonPanel2, toDoList);
                    startButton.addActionListener(startButtonListener);

                    FinishButtonListener finishButtonListener = new FinishButtonListener(todo);
                    JButton finishButton = new JButton("完成");
                    finishButton.addActionListener(finishButtonListener);

                    buttonPanel2.add(startButton);
                    buttonPanel2.add(finishButton);
                    TodoPanel.add(buttonPanel2, BorderLayout.EAST); // 将按钮添加到待办的右侧
                    todo.setPanel(TodoPanel);
                    toDoList.getSmallListPanel().add(TodoPanel);
                    toDoList.getSmallListPanel().revalidate();
                    toDoList.getSmallListPanel().repaint();
                    toDoList.getBigListPanel().revalidate();
                    toDoList.getBigListPanel().repaint();
                    ToDoListPanel.revalidate();
                    ToDoListPanel.repaint();
            }
        }

        }
    }
}
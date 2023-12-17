package View;

import Model.TodoItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author 15252
 */
public class TaskPanel extends JPanel {

    private MainFrame mainframe;
    public static DefaultListModel<TodoItem> taskModel = new DefaultListModel<>();
    public JList<TodoItem> taskList = new JList<>(taskModel);
    private JButton addButton = new JButton("Add Task");
    private JButton deleteButton = new JButton("Delete Task");


    public TaskPanel(MainFrame mainFrame) {
        this.mainframe = mainFrame;

        loadTaskList();

        setLayout(new BorderLayout());
        add(new JScrollPane(taskList), BorderLayout.CENTER);

        // 单元格渲染器
        taskList.setCellRenderer(new TaskCellRenderer());

        // 添加按钮组件
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);

        // 添加面板
        add(buttonPanel, BorderLayout.SOUTH);

        // 添加任务监听器
        addButton.addActionListener(e -> {
            // 创建输入面板
            JPanel inputPanel = new JPanel();
            inputPanel.setLayout(new GridLayout(0, 2));

            // 面板上的文字
            inputPanel.add(new JLabel("Title:"));
            JTextField titleField = new JTextField(10);
            inputPanel.add(titleField);

            /*inputPanel.add(new JLabel("Description:"));
            JTextField descriptionField = new JTextField(10);
            inputPanel.add(descriptionField);*/

            inputPanel.add(new JLabel("Time:"));
            SpinnerModel timeModel = new SpinnerNumberModel(25, 0, 1440, 1);
            JSpinner timeSpinner = new JSpinner(timeModel);
            inputPanel.add(timeSpinner);

            // 显示对话框并获取用户输入
            int result = JOptionPane.showConfirmDialog(null, inputPanel, "Enter Task Details", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String title = titleField.getText();
                /*String description = descriptionField.getText();*/
                int time = (Integer) timeSpinner.getValue();

                // 检测是否存在同名任务
                boolean isDuplicate = false;
                for (int i = 0; i < taskModel.getSize(); i++) {
                    TodoItem item = taskModel.getElementAt(i);
                    if (item != null && item.getTitle().equals(title)) {
                        isDuplicate = true;
                        break;
                    }
                }

                if (!isDuplicate) {
                    // 创建新的TodoItem对象并设置属性
                    TodoItem newItem = new TodoItem(title, time);

                    // 将新创建的任务添加到模型中
                    taskModel.addElement(newItem);
                    saveTaskList();
                } else {
                    // 弹出提示框
                    JOptionPane.showMessageDialog(null, "不能新建同名任务", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 删除任务监听器
        deleteButton.addActionListener(e -> {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                TodoItem selectedTask = taskModel.getElementAt(selectedIndex);
                int confirmResult = JOptionPane.showConfirmDialog(TaskPanel.this, "Are you sure you want to delete this task?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (confirmResult == JOptionPane.YES_OPTION) {
                    taskModel.remove(selectedIndex);
                    saveTaskList();
                }
            }
        });

        // 开始任务监听器
        taskList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = taskList.locationToIndex(e.getPoint());
                    TodoItem selectedItem = taskModel.getElementAt(index);
                    if (selectedItem != null) {
                        showTimerPanel(selectedItem, index);
                    }
                }
            }
        });
    }

    // 显示计时器面板
    private void showTimerPanel(TodoItem selectedItem, int index) {
        mainframe.startTimer(selectedItem, index);
    }

    // 移除已完成事项
    public void removeCompletedTask(int index) {
        taskModel.remove(index);
    }

    // 保存序列化文件
    private void saveTaskList() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("tasks.ser"))) {
            oos.writeObject(new ArrayList<TodoItem>(Collections.list(taskModel.elements())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 加载序列化文件
    private void loadTaskList() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("tasks.ser"))) {
            List<TodoItem> list = (List<TodoItem>) ois.readObject();
            list.forEach(taskModel::addElement);
        } catch (FileNotFoundException e) {
            return;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public DefaultListModel<TodoItem> getTaskModel() {
        return taskModel;
    }
}
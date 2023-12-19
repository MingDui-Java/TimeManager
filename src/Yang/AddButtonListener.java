package Yang;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import Model.TodoItem;
import View.TaskPanel;
import timemanager.TimeManagerFrame;

/**
 * @author 杨智方
 */
// 实现 ActionListener 接口的具名类
class AddButtonListener implements ActionListener, Serializable {
	private JPanel buttonPanel;
	private JButton closeButton;
	private JButton downButton;
	private JPanel bigListPanel;
	private JPanel listPanel;
	private Box TodoListPanel;
	private ToDoList toDoList;

	public AddButtonListener(JPanel buttonPanel, JButton closeButton, JButton downButton, JPanel bigListPanel,
			JPanel listPanel, Box TodoListPanel, ToDoList toDoList) {
		this.buttonPanel = buttonPanel;
		this.closeButton = closeButton;
		this.downButton = downButton;
		this.bigListPanel = bigListPanel;
		this.listPanel = listPanel;
		this.TodoListPanel = TodoListPanel;
		this.toDoList = toDoList;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ArrayList<ToDos> todos = toDoList.toDos;
		String newName = JOptionPane.showInputDialog("添加待办");
		if (newName != null) {

			if (newName.isEmpty()) {
				JOptionPane.showMessageDialog(new JFrame(), "输入不能为空", "提示", JOptionPane.INFORMATION_MESSAGE);
			} else {
				if (todos != null)
					for (ToDos toDoItem : todos) {// 判断是否存在相同名称待办
						Component[] components = toDoItem.getPanel().getComponents();
						for (Component component : components) {
							if (component instanceof JLabel label) {
								if (newName.equals(label.getText())) {
									JOptionPane.showMessageDialog(new JFrame(), "已存在相同待办", "提示",
											JOptionPane.INFORMATION_MESSAGE);
									return;
								}
							}
						}
					}
				buttonPanel.remove(downButton);
				buttonPanel.add(closeButton);
				JPanel TodoPanel = new JPanel(new BorderLayout());
				JLabel userInputLabel = new JLabel(newName);
				ToDos toDoItem = new ToDos(newName, TodoPanel, false, userInputLabel);
				Random random = new Random();
				Color rColor = predefinedColors[random.nextInt(predefinedColors.length)];
				TodoPanel.setBackground(rColor);
				TodoPanel.setMaximumSize(new Dimension(1700, 40));
				userInputLabel.setForeground(Color.white);
				userInputLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // 左对齐
				TodoPanel.add(userInputLabel, BorderLayout.WEST); // 将名称添加到待办的左侧

				JPanel buttonPanel2 = new JPanel(new GridLayout(1, 2));

				JButton startButton = new JButton("开始");
				StartButtonListener startButtonListener = new StartButtonListener(buttonPanel2, toDoList);
				startButton.addActionListener(startButtonListener);

				FinishButtonListener finishButtonListener = new FinishButtonListener(toDoItem);
				JButton finishButton = new JButton("完成");
				finishButton.addActionListener(finishButtonListener);

				buttonPanel2.add(startButton);
				buttonPanel2.add(finishButton);
				TodoPanel.add(buttonPanel2, BorderLayout.EAST); // 将按钮添加到待办的右侧
				if (todos != null)
					todos.add(toDoItem);// 待办集里加入待办
				listPanel.add(TodoPanel);
				listPanel.revalidate();
				listPanel.repaint();
				bigListPanel.revalidate();
				bigListPanel.repaint();
				TodoListPanel.revalidate();
				TodoListPanel.repaint();
				CalendarPanel calendarPanel = CalendarPanel.getInstance();
				calendarPanel.saveTodoMap();
			}
		}
	}

	public static Color[] predefinedColors = { new Color(72, 61, 139), // 淡蓝色
			new Color(255, 192, 203), // 粉色
			new Color(173, 216, 230), // 浅蓝色
			new Color(78, 152, 217), new Color(64, 224, 208), new Color(128, 128, 128), new Color(119, 136, 153),
			new Color(47, 79, 79), };

}

class StartButtonListener implements ActionListener, Serializable {
	private JPanel buttonPanel;
	private ToDoList toDoList;

	public StartButtonListener(JPanel buttonPanel, ToDoList toDoList) {
		this.buttonPanel = buttonPanel;
		this.toDoList = toDoList;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// Keith
		// 创建输入面板
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new GridLayout(0, 2));
		ArrayList<ToDos> todos = toDoList.toDos;

		// 面板上的文字
		inputPanel.add(new JLabel("标题:"));
		JTextField titleField = new JTextField(10);
		JPanel p = (JPanel) buttonPanel.getParent();
		for (ToDos toDoItem : todos) {// 寻找对应的JPanel
			if (toDoItem.getPanel() == p) {
				titleField.setText(toDoItem.getName());
				break;
			}
		}

		inputPanel.add(titleField);

		inputPanel.add(new JLabel("时间:"));
		SpinnerModel timeModel = new SpinnerNumberModel(25, 0, 1440, 1);
		JSpinner timeSpinner = new JSpinner(timeModel);
		inputPanel.add(timeSpinner);

		// 显示对话框并获取用户输入
		int result = JOptionPane.showConfirmDialog(null, inputPanel, "事项信息", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			String title = titleField.getText();
			TodoItem newItem = null;
			int time = (Integer) timeSpinner.getValue();
			// 检测是否存在同名任务
			boolean isDuplicate = false;
			for (int i = 0; i < TaskPanel.taskModel.getSize(); i++) {
				TodoItem item = TaskPanel.taskModel.getElementAt(i);
				if (item != null && item.getTitle().equals(title)) {
					isDuplicate = true;
					break;
				}
			}

			if (!isDuplicate) {
				for (ToDos toDoItem : todos) {// 寻找对应的JPanel
					if (toDoItem.getPanel() == p) {
						newItem = new TodoItem(title, time, toDoItem);
						break;
					}
				}

				// 将新创建的任务添加到模型中
				TaskPanel.taskModel.addElement(newItem);
				saveTaskList();
				TimeManagerFrame.showTomato();
			} else {
				// 弹出提示框
				JOptionPane.showMessageDialog(null, "不能新建同名任务", "错误", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private static void saveTaskList() {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("./data/tasks.ser"))) {
			oos.writeObject(new ArrayList<TodoItem>(Collections.list(TaskPanel.taskModel.elements())));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class FinishButtonListener implements ActionListener, Serializable {
	private ToDos toDos;

	public FinishButtonListener(ToDos toDos) {
		this.toDos = toDos;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		toDos.setFinished();
	}
}

package View;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import Model.TodoItem;

/**
 * 事项面板
 *
 * @author Keith
 * @version 1.0
 */
public class TaskPanel extends JPanel {
	/**
	 * 持有总的TomatoPanel
	 */
	private TomatoPanel mainframe;
	/**
	 * 装载TodoItem专注事项的容器
	 */
	public static DefaultListModel<TodoItem> taskModel = new DefaultListModel<>();
	/**
	 * 负责渲染的JList
	 */
	public JList<TodoItem> taskList = new JList<>(taskModel) {
		@Override
		public int locationToIndex(Point location) {
			int index = super.locationToIndex(location);
			if (index != -1 && !getCellBounds(index, index).contains(location)) {
				return -1;
			} else {
				return index;
			}
		}
	};
	/**
	 * 添加事项按钮
	 */
	private JButton addButton = new JButton("添加事项");
	/**
	 * 删除事项按钮
	 */
	private JButton deleteButton = new JButton("删除事项");

	/**
	 * 构造函数
	 * @param tomatoPanel 大的Panel
	 */
	public TaskPanel(TomatoPanel tomatoPanel) {
		this.mainframe = tomatoPanel;

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
			inputPanel.add(new JLabel("标题:"));
			JTextField titleField = new JTextField(10);
			inputPanel.add(titleField);

			/*
			 * inputPanel.add(new JLabel("Description:")); JTextField descriptionField = new
			 * JTextField(10); inputPanel.add(descriptionField);
			 */

			inputPanel.add(new JLabel("时间:"));
			SpinnerModel timeModel = new SpinnerNumberModel(25, 1, 1440, 1);
			JSpinner timeSpinner = new JSpinner(timeModel);
			inputPanel.add(timeSpinner);

			// 显示对话框并获取用户输入
			int result = JOptionPane.showConfirmDialog(null, inputPanel, "事项信息", JOptionPane.OK_CANCEL_OPTION);
			if (result == JOptionPane.OK_OPTION) {
				String title = titleField.getText();
				/* String description = descriptionField.getText(); */
				if (title == null || title.trim().isEmpty()) {
					JOptionPane.showMessageDialog(null, "无法创建空事项", "错误", JOptionPane.ERROR_MESSAGE);
				} else {
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
			}
		});

		// 删除任务监听器
		deleteButton.addActionListener(e -> {
			int selectedIndex = taskList.getSelectedIndex();
			if (selectedIndex != -1) {
				TodoItem selectedTask = taskModel.getElementAt(selectedIndex);
				int confirmResult = JOptionPane.showConfirmDialog(TaskPanel.this, "确认删除该事项吗?", "删除",
						JOptionPane.YES_NO_OPTION);
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
					if (index != -1) {
						TodoItem selectedItem = taskModel.getElementAt(index);
						if (selectedItem != null) {
							showTimerPanel(selectedItem, index);
						}
					}
				}
			}
		});
	}

	/**
	 * 显示计时器面板
	 *
	 * @param selectedItem 被选中的事项
	 * @param index 事项对应的下标
	 */
	private void showTimerPanel(TodoItem selectedItem, int index) {
		mainframe.startTimer(selectedItem, index);
	}

	/**
	 * 移除已完成事项
	 *
	 * @param index 事项对应的下标
	 */
	public void removeCompletedTask(int index) {
		taskModel.remove(index);
	}

	/**
	 * 保存序列化文件
	 */
	public static void saveTaskList() {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Data/tasks.ser"))) {
			oos.writeObject(new ArrayList<TodoItem>(Collections.list(taskModel.elements())));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 加载序列化文件
	 */
	private void loadTaskList() {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Data/tasks.ser"))) {
			List<TodoItem> list = (List<TodoItem>) ois.readObject();
			list.forEach(taskModel::addElement);
		} catch (FileNotFoundException e) {
			return;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}

/**
 * 日历模块
 */

package CalendarUnit;

import static CalendarUnit.AddButtonListener.predefinedColors;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import timemanager.TimeManagerFrame;

/**
 * 这个类代表日视图的面板，继承自 JPanel，并提供了添加待办事项集合的功能。通过按钮操作和用户输入，可以管理待办事项集合。
 *
 * @author 杨智方
 * @version 1.0
 */
public class DayPanel extends JPanel implements Serializable {
	/**
	 * 用于垂直显示所有待办集
	 */
	protected Box ToDoListPanel;

	/**
	 * 提示面板，用于提示用户添加待办集。
	 */
	protected JPanel hintPanel;

	/**
	 * 存储待办集的数组。
	 */
	protected ArrayList<ToDoList> list;

	/**
	 * 用于加上滚动条
	 */
	protected JScrollPane scrollPane;

	/**
	 * 获取待办集的储存数组。
	 *
	 * @return 返回储存待办集的数组
	 */
	public ArrayList<ToDoList> getList() {
		return list;
	}

	/**
	 * DayPanel 类的构造方法，初始化日视图面板。
	 *
	 * <p>
	 * 构造方法设置了面板的布局，初始化了待办事项列表和提示面板，允许用户创建和管理待办事项集合。
	 * </p>
	 *
	 * @see CalendarUnit.CalendarPanel#showMonthPanel()
	 * @see CalendarUnit.CalendarPanel#saveTodoMap()
	 */
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
				if (newName != null) {
					if (newName.isEmpty()) {
						JOptionPane.showMessageDialog(new JPanel(), "输入不能为空", "提示", JOptionPane.INFORMATION_MESSAGE);
					} else if (newName.startsWith("提醒：")) {
						JOptionPane.showMessageDialog(null, "\"提醒：\"开头的待办集为待办事项导入，用户不可以用\"提醒：\"开头创建待办集", "错误",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						for (ToDoList toDoList1 : list) {// 判断是否存在相同名称待办集
							if (newName.equals(toDoList1.getName())) {
								JOptionPane.showMessageDialog(new JPanel(), "已经添加过名称相同的待办集啦", "提示",
										JOptionPane.INFORMATION_MESSAGE);
								return;
							}
						}
						createTodoList(newName);
						TimeManagerFrame.showTomato();
					}
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
				calendarPanel.saveTodoMap();
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

	/**
	 * 设置滚动栏
	 *
	 * @param scrollPane 滚动栏
	 */
	public void setScrollPane(JScrollPane scrollPane) {
		this.scrollPane = scrollPane;
	}

	/**
	 * 创建新的待办集，并将其添加到面板和数组中。
	 *
	 * @param name 待办集的名称
	 */
	public void createTodoList(String name) {
		remove(hintPanel);

		ToDoList toDoList1 = new ToDoList(name);

		JPanel bigListPanel = toDoList1.getBigListPanel();
		bigListPanel.setLayout(new BoxLayout(bigListPanel, BoxLayout.Y_AXIS));

		JPanel smallListPanel = toDoList1.getSmallListPanel();
		smallListPanel.setLayout(new BoxLayout(smallListPanel, BoxLayout.Y_AXIS));

		smallListPanel.setVisible(true);

		JPanel listPanel = toDoList1.getListPanel();
		listPanel.setMaximumSize(new Dimension(1700, 40));

		JLabel userInputLabel = new JLabel(name);
		userInputLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // 左对齐
		listPanel.add(userInputLabel, BorderLayout.WEST); // 将名称添加到待办的左侧
		JPanel buttonPanel = GetTodoButtonPanel.getTodoButtonPanel(smallListPanel, bigListPanel, ToDoListPanel, list,
				toDoList1);
		listPanel.add(buttonPanel, BorderLayout.EAST); // 将按钮添加到待办的右侧
		bigListPanel.add(listPanel);
		bigListPanel.add(smallListPanel);
		bigListPanel.setVisible(true);
		list.add(toDoList1);// 待办集里加入待办
		ToDoListPanel.add(bigListPanel);
		ToDoListPanel.revalidate();
		ToDoListPanel.repaint();

		if (scrollPane != null)
			remove(scrollPane);
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
		CalendarPanel calendarPanel = CalendarPanel.getInstance();
		calendarPanel.saveTodoMap();
	}

	/**
	 * 创建新的待办，并将其添加到面板和数组中。
	 *
	 * @param List 存储的待办集的集合
	 */
	public void createTodo(ToDoList List) {
		for (ToDoList toDoList : list) {
			if (toDoList.getName().equals(List.getName())) {
				ArrayList<ToDos> toDosArrayList = List.toDos;
				if (toDosArrayList == null) {
					return;
				}
				for (ToDos todo : toDosArrayList) {
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
					if (todo.isFinished()) {
						todo.getLabel().setText(todo.getLabel().getText() + "(已完成）");
					}
					JPanel buttonPanel2 = new JPanel(new GridLayout(1, 2));

					toDoList.toDos.add(todo);// 待办集里加入待办
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
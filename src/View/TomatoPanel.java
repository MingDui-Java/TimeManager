package View;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import Model.TodoItem;

/**
 * @author 15252
 */
public class TomatoPanel extends JPanel {

	private List<FocusTimeObserver> observers = new ArrayList<>();
	private CardLayout cardLayout = new CardLayout();
	private JPanel mainPanel = new JPanel(cardLayout); // 主面板
	private TaskPanel taskPanel = new TaskPanel(TomatoPanel.this); // 用于事项功能
	private StatisticsPanel statisticsPanel = new StatisticsPanel(); // 用于数据统计功能

	public TomatoPanel() {
		// 设置布局和添加子面板
		this.setLayout(new BorderLayout());
		mainPanel.add(taskPanel, "TaskPanel");
		mainPanel.add(statisticsPanel, "StatsPanel");

		// 注册观察者
		registerObserver(statisticsPanel);

		// 创建按钮并添加事件监听器
		JButton taskButton = new JButton("事项");
		taskButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(mainPanel, "TaskPanel");
			}
		});
		JButton statsButton = new JButton("数据统计");
		statsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(mainPanel, "StatsPanel");
				statisticsPanel.updateChart();
			}
		});

		// 添加按钮组件
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(taskButton);
		buttonPanel.add(statsButton);

		// 添加面板
		this.add(buttonPanel, BorderLayout.NORTH);
		this.add(mainPanel, BorderLayout.CENTER);
	}

	// 跳转计时器面板
	public void startTimer(TodoItem selectedItem, int index) {
		JPanel timerPanel = new TimerPanel(selectedItem, this, index);
		mainPanel.add(timerPanel, "TimerPanel");
		cardLayout.show(mainPanel, "TimerPanel");
	}

	// 跳转事项面板
	public void showTasks() {
		cardLayout.show(mainPanel, "TaskPanel");
	}

	// 注册观察者
	public void registerObserver(FocusTimeObserver observer) {
		observers.add(observer);
	}

	// 通知观察者
	public void notifyObservers(FocusTimeEntry focusTimeEntry) {
		for (FocusTimeObserver observer : observers) {
			observer.updateFocusTime(focusTimeEntry);
		}
	}

	// 移除已完成的任务
	public void removeCompletedTask(int index) {
		taskPanel.removeCompletedTask(index);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Tomato Timer");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(800, 600);
			frame.setLocationRelativeTo(null);
			frame.add(new TomatoPanel());
			frame.setVisible(true);
		});
	}
}
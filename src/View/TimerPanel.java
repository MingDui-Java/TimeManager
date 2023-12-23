package View;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

import Model.TodoItem;

/**
 * 计时器面板
 *
 * @author Keith
 * @version 1.0
 */
public class TimerPanel extends JPanel {
	/**
	 * 持有总的tomatoPanel
	 */
	private TomatoPanel tomatoPanel;
	/**
	 * 计时器对应的事项
	 */
	private TodoItem todoItem;
	/**
	 * 计时器面板的时间名
	 */
	private JLabel timeLabel;
	/**
	 * 计时器面板的任务名
	 */
	private JLabel taskNameLabel;
	/**
	 * 计时器
	 */
	private Timer timer;
	/**
	 * 开始按钮
	 */
	private JButton startButton;
	/**
	 * 控制按钮
	 */
	private JButton controlButton;
	/**
	 * 退出按钮
	 */
	private JButton backButton;
	/**
	 * 进度条
	 */
	private JProgressBar progressBar;
	/**
	 * 事项对应的下标
	 */
	private int index;
	/**
	 * 事项的原始时间
	 */
	private int originalTime;
	/**
	 * 事项的剩余时间
	 */
	private int remainingTime;

	/**
	 * 构造函数
	 *
	 * @param todoItem 事项
	 * @param tomatoPanel 总的TomatoPanel
	 * @param index 事项对应下标
	 */
	public TimerPanel(TodoItem todoItem, TomatoPanel tomatoPanel, int index) {
		this.tomatoPanel = tomatoPanel;
		this.todoItem = todoItem;
		this.originalTime = todoItem.getRemainingTime();
		this.index = index;
		remainingTime = todoItem.getRemainingTime();

		// 页面布局
		this.setLayout(new BorderLayout());

		setupTimerPanel();
	}

	/**
	 * 设置时间标签和计时器
	 */
	private void setupTimerPanel() {
		// 顶部面板
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

		JPanel taskNamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		taskNameLabel = new JLabel(todoItem.getTitle()); // 使用todoItem的标题
//		taskNameLabel.setFont(new Font("宋体", Font.BOLD, 16)); // 设置字体
		taskNamePanel.add(taskNameLabel);
		topPanel.add(taskNamePanel);

		/*
		 * JPanel taskDescriptionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		 * taskDescriptionLabel = new JLabel("<html>" + todoItem.getDescription() +
		 * "<html>"); // 使用todoItem的标题 taskDescriptionLabel.setFont(new Font("宋体",
		 * Font.BOLD, 12)); // 设置字体 taskDescriptionPanel.add(taskDescriptionLabel);
		 * topPanel.add(taskDescriptionPanel);
		 */

		this.add(topPanel, BorderLayout.NORTH); // 将详情面板添加到顶部

		// 中部面板
		JPanel middlePanel = new JPanel();
		middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
		middlePanel.add(Box.createVerticalGlue());

		timeLabel = new JLabel(formatTime(remainingTime));
		timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		middlePanel.add(timeLabel);

		progressBar = new JProgressBar(0, originalTime);
		progressBar.setValue(originalTime);
		progressBar.setStringPainted(true);
		progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
		progressBar.setMaximumSize(new Dimension(300, 20));
		middlePanel.add(progressBar);

		middlePanel.add(Box.createVerticalGlue());

		this.add(middlePanel, BorderLayout.CENTER);

		// 设置计时器
		timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				remainingTime--;
				todoItem.setRemainingTime(remainingTime);
				todoItem.setRemainingTime(remainingTime);
				timeLabel.setText(formatTime(remainingTime));
				progressBar.setValue(remainingTime); // 更新进度条

				if (remainingTime <= 0) {
					timer.stop();
					JOptionPane.showMessageDialog(new JPanel(), "时间到！", "番茄钟", JOptionPane.INFORMATION_MESSAGE);
					if (todoItem.getTodo() != null) {
						todoItem.getTodo().setFinished();
					}
					notifyFocusTime();
					tomatoPanel.removeCompletedTask(index);
					TaskPanel.saveTaskList();
					tomatoPanel.showTasks();
				}
			}
		});

		// 计时按钮
		startButton = new JButton("开始");
		startButton.addActionListener(e -> {
			timer.start();
			startButton.setVisible(false);
			controlButton.setVisible(true);
		});

		// 控制按钮
		controlButton = new JButton("停止");
		controlButton.setVisible(false);
		controlButton.addActionListener(e -> {
			if (timer.isRunning()) {
				timer.stop();
				controlButton.setText("继续");
			} else {
				timer.start();
				controlButton.setText("停止");
			}
		});

		// 退出按钮
		backButton = new JButton("返回");
		backButton.addActionListener(e -> {
			timer.stop();
			todoItem.setRemainingTime(remainingTime);
			notifyFocusTime();
			showTasks();
		});

		// 底部面板
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new FlowLayout());
		bottomPanel.add(startButton);
		bottomPanel.add(controlButton);
		bottomPanel.add(backButton);

		this.add(bottomPanel, BorderLayout.SOUTH);
	}

	/**
	 * 格式化时间
	 *
	 * @param totalSeconds 总时长
	 * @return 格式化后的时间
	 */
	private String formatTime(int totalSeconds) {
		int minutes = totalSeconds / 60;
		int seconds = totalSeconds % 60;
		return String.format("%02d:%02d", minutes, seconds);
	}

	/**
	 * 通知主面板切换事项面板
	 */
	private void showTasks() {
		tomatoPanel.showTasks();
	}

	/**
	 * 通知观察者
	 */
	private void notifyFocusTime() {
		Double focusedTime = (originalTime - remainingTime) * 1.0 / 60;
		tomatoPanel.notifyObservers(new FocusTimeEntry(todoItem.getTitle(), todoItem.getCreationDay(), focusedTime));
	}

	/**
	 * 结束计时器
	 */
	public void stopTimer() {
		timer.stop();
		todoItem.setRemainingTime(remainingTime);
		notifyFocusTime();
		showTasks();
	}
}

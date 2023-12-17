package View;

import Model.TodoItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author 15252
 */
public class TimerPanel extends JPanel {

    private TomatoPanel tomatoPanel;
    private TodoItem todoItem;
    private JLabel timeLabel;
    private Timer timer;
    private JButton startButton;
    private JButton controlButton;
    private JButton backButton;
    private JProgressBar progressBar;
    private JLabel taskNameLabel;
    private JLabel taskDescriptionLabel;
    private int index;
    private int originalTime;
    private int remainingTime;


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

    // 设置时间标签和计时器
    private void setupTimerPanel() {
        // 顶部面板
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        JPanel taskNamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        taskNameLabel = new JLabel(todoItem.getTitle()); // 使用todoItem的标题
        taskNameLabel.setFont(new Font("宋体", Font.BOLD, 16)); // 设置字体
        taskNamePanel.add(taskNameLabel);
        topPanel.add(taskNamePanel);

        /*
        JPanel taskDescriptionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        taskDescriptionLabel = new JLabel("<html>" + todoItem.getDescription() + "<html>"); // 使用todoItem的标题
        taskDescriptionLabel.setFont(new Font("宋体", Font.BOLD, 12)); // 设置字体
        taskDescriptionPanel.add(taskDescriptionLabel);
        topPanel.add(taskDescriptionPanel);*/

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
                timeLabel.setText(formatTime(remainingTime));
                progressBar.setValue(remainingTime); // 更新进度条

                if (remainingTime <= 0) {
                    timer.stop();
                    JOptionPane.showMessageDialog(TimerPanel.this, "Time's up!");
                    notifyFocusTime();
                    tomatoPanel.removeCompletedTask(index);
                    tomatoPanel.showTasks();
                }
            }
        });

        // 计时按钮
        startButton = new JButton("Start");
        startButton.addActionListener(e -> {
            timer.start();
            startButton.setVisible(false);
            controlButton.setVisible(true);
        });

        // 控制按钮
        controlButton = new JButton("Stop");
        controlButton.setVisible(false);
        controlButton.addActionListener(e -> {
            if (timer.isRunning()) {
                timer.stop();
                controlButton.setText("Continue");
            } else {
                timer.start();
                controlButton.setText("Stop");
            }
        });

        // 退出按钮
        backButton = new JButton("Back to Tasks");
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

    // 格式化时间
    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    // 通知主面板切换事项面板
    private void showTasks() {
        tomatoPanel.showTasks();
    }

    // 通知观察者
    private void notifyFocusTime() {
        Double focusedTime = (originalTime - remainingTime) * 1.0 / 60;
        tomatoPanel.notifyObservers(new FocusTimeEntry(todoItem.getTitle(), todoItem.getCreationDay(), focusedTime));
    }
}

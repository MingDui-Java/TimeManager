package View;

import Model.DailyFocusTimeChart;
import Model.TodoItem;
import Yang.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 15252
 */
public class MainFrame extends JFrame {

    private List<FocusTimeObserver> observers = new ArrayList<>();
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout); // 主面板
    private TaskPanel taskPanel = new TaskPanel(MainFrame.this); // 用于事项功能
    private StatisticsPanel statisticsPanel = new StatisticsPanel(); // 用于数据统计功能
    private CalendarPanel calendarPanel = new CalendarPanel();
    private JPanel container = new JPanel(new CardLayout());


    public MainFrame() {
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

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

        // 日视图
        JButton CalendarButton = new JButton("日视图");
        CalendarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "Container");
            }
        });

        // 创建并设置布局
        setLayout(new BorderLayout());

        // 添加按钮组件
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(taskButton);
        buttonPanel.add(statsButton);
        buttonPanel.add(CalendarButton);

        container.add(calendarPanel,"CalendarPanel");

        // 配置主面板
        mainPanel.add(taskPanel, "TaskPanel");
        mainPanel.add(statisticsPanel, "StatsPanel");
        mainPanel.add(container, "Container");

        // 添加面板
        add(buttonPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
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
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}

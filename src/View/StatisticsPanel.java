package View;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import Model.DailyFocusTimeChart;
import Model.FocusTimeChartFactory;
import Model.WeeklyFocusTimeChart;

/**
 * @author 15252
 */
public class StatisticsPanel extends JPanel implements FocusTimeObserver {

	public List<FocusTimeEntry> focusTimeEntries = new ArrayList<>();
	private ChartPanel chartPanel;
	private FocusTimeChartFactory chartFactory;

	public StatisticsPanel() {
		loadFocusTimeEntries();

		// 初始化数据统计面板
		setLayout(new BorderLayout());

		// 初始化工厂
		setChartFactory(new DailyFocusTimeChart());

		// 创建并添加图表面板
		JFreeChart chart = createChart();
		chartPanel = new ChartPanel(chart);

		// 创建日期范围选择组件
		JComboBox<String> dateRangeComboBox = createDateRangeComboBox();
		dateRangeComboBox.addActionListener(e -> {
			String selectedRange = (String) dateRangeComboBox.getSelectedItem();
			updateChart();
		});

		// 添加组件
		add(dateRangeComboBox, BorderLayout.NORTH);
		add(chartPanel, BorderLayout.CENTER);

		// 添加导出按钮
		JButton exportButton = new JButton("导出图表");
		exportButton.addActionListener(e -> saveChart());
		add(exportButton, BorderLayout.SOUTH);
	}

	// 创建日期范围选择下拉框
	private JComboBox<String> createDateRangeComboBox() {
		JComboBox<String> comboBox = new JComboBox<>();
		comboBox.addItem("今日");
		comboBox.addItem("本周");

		// 添加事件监听器
		comboBox.addActionListener(e -> {
			JComboBox<String> source = (JComboBox<String>) e.getSource();
			String selectedRange = (String) source.getSelectedItem();

			if ("今日".equals(selectedRange)) {
				setChartFactory(new DailyFocusTimeChart());
			} else if ("本周".equals(selectedRange)) {
				setChartFactory(new WeeklyFocusTimeChart());
			}
			updateChart();
		});

		return comboBox;
	}

	// 更新图表数据
	public void updateChart() {
		JFreeChart chart = createChart();
		chartPanel.setChart(chart);
	}

	// 更新数据
	public void updateFocusTime(FocusTimeEntry focusTimeEntry) {
		for (FocusTimeEntry entry : focusTimeEntries) {
			if (entry.getTitle().equals(focusTimeEntry.getTitle())
					&& entry.getDate().equals(focusTimeEntry.getDate())) {
				entry.combineTime(focusTimeEntry.getMinutes());
				return;
			}
		}
		focusTimeEntries.add(focusTimeEntry);
		saveFocusTimeEntries();
	}

	// 创建图表
	public JFreeChart createChart() {
		return chartFactory.createChart(focusTimeEntries);
	}

	// 设置对应的工厂
	public void setChartFactory(FocusTimeChartFactory chartFactory) {
		this.chartFactory = chartFactory;
	}

	// 弹出文件选择器并保存图表
	private void saveChart() {
		JFreeChart chart = chartPanel.getChart();
		if (chart != null) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("保存图表");
			fileChooser.setFileFilter(new FileNameExtensionFilter("PNG 图片", "png"));
			int userSelection = fileChooser.showSaveDialog(this);

			if (userSelection == JFileChooser.APPROVE_OPTION) {
				File fileToSave = fileChooser.getSelectedFile();
				String filePath = fileToSave.getAbsolutePath();
				if (!filePath.toLowerCase().endsWith(".png")) {
					fileToSave = new File(filePath + ".png");
				}
				try {
					ChartUtilities.saveChartAsPNG(fileToSave, chart, 800, 600);
					JOptionPane.showMessageDialog(this, "图表已保存: " + fileToSave.getAbsolutePath());
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(this, "保存图表失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
				}
			}
		} else {
			JOptionPane.showMessageDialog(this, "无可用图表导出", "错误", JOptionPane.ERROR_MESSAGE);
		}
	}

	// 保存序列化文件
	private void saveFocusTimeEntries() {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Data/focusTimeEntries.ser"))) {
			oos.writeObject(focusTimeEntries);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 加载序列化文件
	private void loadFocusTimeEntries() {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Data/focusTimeEntries.ser"))) {
			focusTimeEntries = (List<FocusTimeEntry>) ois.readObject();
		} catch (FileNotFoundException e) {
			return;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}

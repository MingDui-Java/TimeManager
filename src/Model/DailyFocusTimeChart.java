package Model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis3D;
import org.jfree.chart.axis.NumberAxis3D;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.data.category.DefaultCategoryDataset;

import View.FocusTimeEntry;

/**
 * @author 15252
 */
public class DailyFocusTimeChart implements FocusTimeChartFactory {

	// 柱状颜色
	private static final Map<String, Color> colorMap = new HashMap<>();
	private static final Random rand = new Random();

	@Override
	public JFreeChart createChart(List<FocusTimeEntry> focusTimeEntries) {
		// 创建数据集
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		LocalDate today = LocalDate.now();

		for (FocusTimeEntry entry : focusTimeEntries) {
			if (entry.getDate().isEqual(today)) {
				dataset.addValue(entry.getMinutes(), "Focus Time", entry.getTitle());
			}
		}

		// 创建图表
		JFreeChart chart = ChartFactory.createBarChart3D("专注时间", // 图表标题
				"事项", // X轴标签
				"时长（分钟）", // Y轴标签
				dataset, // 数据集
				PlotOrientation.VERTICAL, // 图表方向
				true, // 是否包含图例
				true, // 是否生成工具
				false // 是否生成URL链接
		);

		// 处理标题乱码
		chart.getTitle().setFont(new Font("宋体", Font.BOLD, 18));
		chart.getLegend().setItemFont(new Font("宋体", Font.BOLD, 15));

		// 获取图标区域对象
		CategoryPlot categoryPlot = (CategoryPlot) chart.getPlot();

		// 处理X Y轴乱码
		CategoryAxis3D categoryAxis3D = (CategoryAxis3D) categoryPlot.getDomainAxis();
		NumberAxis3D numberAxis3D = (NumberAxis3D) categoryPlot.getRangeAxis();
		categoryAxis3D.setTickLabelFont(new Font("宋体", Font.BOLD, 15));
		categoryAxis3D.setLabelFont(new Font("宋体", Font.BOLD, 15));
		numberAxis3D.setTickLabelFont(new Font("宋体", Font.BOLD, 15));
		numberAxis3D.setLabelFont(new Font("宋体", Font.BOLD, 15));

		numberAxis3D.setLowerBound(0.0);

		// 自动选择刻度
		numberAxis3D.setAutoTickUnitSelection(true);

		// 图形设置
		// 图形设置
		BarRenderer3D barRenderer3D = new BarRenderer3D() {
			public Paint getItemPaint(int row, int column) {
				String itemName = (String) dataset.getColumnKey(column);
				return getColorForItem(itemName);
			}
		};
		barRenderer3D.setMaximumBarWidth(0.07);

		// 在图形上显示数字
		barRenderer3D.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		barRenderer3D.setBaseItemLabelsVisible(true);
		barRenderer3D.setBaseItemLabelFont(new Font("宋体", Font.BOLD, 15));

		categoryPlot.setRenderer(barRenderer3D);

		return chart;
	}

	private Color getColorForItem(String itemName) {
		return colorMap.computeIfAbsent(itemName, k -> new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()));
	}
}

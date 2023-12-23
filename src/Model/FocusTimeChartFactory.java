package Model;

import View.FocusTimeEntry;
import org.jfree.chart.JFreeChart;

import java.util.List;

/**
 * 图表工厂接口
 *
 * @author Keith
 * @version 1.0
 */
public interface FocusTimeChartFactory {
    /**
     * 创建图表
     *
     * @param focusTimeEntries 专注时长条目容器
     * @return 返回要创建的图表
     */
    JFreeChart createChart(List<FocusTimeEntry> focusTimeEntries);
}

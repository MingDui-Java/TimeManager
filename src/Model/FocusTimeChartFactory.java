package Model;

import View.FocusTimeEntry;
import org.jfree.chart.JFreeChart;

import java.util.List;

/**
 * @author 15252
 */
public interface FocusTimeChartFactory {
    JFreeChart createChart(List<FocusTimeEntry> focusTimeEntries);
}

package CalendarUnit;
import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.Serializable;

/**
 * EachDay 类存储每一天的信息，将月视图上每一天的按钮和对应日视图信息连接起来。
 *
 * @author 杨智方
 * @version 1.0
 */
public class EachDay implements Serializable {
    /**
     * 月视图上的按钮
     */
    protected JButton dayButton;
    /**
     * 与该日期关联的日视图
     */
    protected DayPanel dayPanel;
    /**
     * 与该日期关联的待办集数量
     */
    protected int num;
    /**
     * 日期
     */
    protected int day;
    /**
     * EachDay 类的构造函数。
     *
     * @param dayButton 日期按钮
     * @param dayPanel 日视图
     * @param num 待办集数量
     * @param day 日期
     */
    public EachDay(JButton dayButton, DayPanel dayPanel, int num, int day){
        this.dayButton = dayButton;
        this.dayPanel = dayPanel;
        this.num = num;
        this.day = day;
        dayOnShow();
    }
    /**
     * 当日期按钮被显示时触发，更新按钮显示的日期和待办事项数量。
     */
    public void dayOnShow(){
        dayButton.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                dayButton.setText (String.format("<html>" + day + "<br><center><font size=\"-5\"><span style='position:relative;'><span style='position:absolute; top:0; left:-5px; font-size:5px;'>&#9679;</span>%d</font></center></span></html>", num));
            }
        });
    }
    /**
     * 设置日期按钮。
     *
     * @param button 日期按钮
     */
    public void setDayButton(JButton button){
        this.dayButton = button;
    }
    /**
     * 设置待办集数量。
     *
     * @param num 待办集数量
     */
    public void setNum(int num){
        this.num = num;
    }

}

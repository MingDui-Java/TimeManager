package Yang;
import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.Serializable;

/**
 * @author 杨智方
 */
public class EachDay extends JPanel implements Serializable {
    protected JButton dayButton;
    protected DayPanel dayPanel;
    protected int num;
    protected int day;
    public EachDay(JButton dayButton, DayPanel dayPanel, int num, int day){
        this.dayButton = dayButton;
        this.dayPanel = dayPanel;
        this.num = num;
        this.day = day;
        dayOnShow();
    }
    public void dayOnShow(){
        dayButton.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                dayButton.setText (String.format("<html>" + day + "<br><center><font size=\"-5\"><span style='position:relative;'><span style='position:absolute; top:0; left:-5px; font-size:5px;'>&#9679;</span>%d</font></center></span></html>", num));
            }
        });
    }
    public JButton getDayButton(){
        return dayButton;
    }
    public void setDayButton(JButton button){
        this.dayButton = button;
    }
    public void setNum(int num){
        this.num = num;
    }
    public DayPanel getSecondPanel(){
        return dayPanel;
    }
}

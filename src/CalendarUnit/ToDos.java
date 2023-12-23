/**
 * 日历模块
 */
package CalendarUnit;
import javax.swing.*;
import java.io.Serializable;

/**
 * 存储待办信息的类
 * @author 杨智方
 */
public class ToDos implements Serializable {
    /**
     * 待办事项的完成状态
     */
    private boolean finished;
    /**
     * 待办事项的面板
     */
    private JPanel todoPanel;
    /**
     * 待办事项显示的标签
     */
    private JLabel label;
    /**
     * 待办事项的名称
     */
    private String name;
    /**
     * ToDos 类的构造函数，用于初始化待办事项
     *
     * @param name 待办事项的名称
     * @param todoPanel 待办事项的面板
     * @param finished 待办事项的完成状态
     * @param label 待办事项显示的标签
     */
    public ToDos(String name,JPanel todoPanel, boolean finished,JLabel label){
        this.todoPanel = todoPanel;
        this.finished = finished;
        this.label = label;
        this.name = name;
    }
    /**
     * 设置待办事项为已完成状态，并更新标签显示
     */
    public void setFinished(){
        if(!this.finished) this.label.setText(label.getText() + "（已完成）");
        this.finished = true;
    }
    /**
     * 获取待办事项的名称
     *
     * @return 待办事项的名称
     */
    public String getName(){
        return name;
    }
    /**
     * 检查待办事项是否已完成
     *
     * @return true 如果待办事项已完成，否则为 false
     */
    public boolean isFinished(){
        return finished;
    }
    /**
     * 设置待办事项的标签
     *
     * @param label 待办事项显示的标签
     */
    public void setLabel(JLabel label){
        this.label = label;
    }
    /**
     * 获取待办事项的标签
     *
     * @return 待办事项显示的标签
     */
    public JLabel getLabel(){
        return label;
    }
    /**
     * 获取待办事项的面板
     *
     * @return 待办事项的面板
     */
    public JPanel getPanel() {
        return this.todoPanel;
    }
    /**
     * 设置待办事项的面板
     *
     * @param todoPanel 待办事项的面板
     */
    public void setPanel(JPanel todoPanel) {
        this.todoPanel = todoPanel;
    }
}

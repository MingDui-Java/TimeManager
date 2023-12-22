/**
 * 日历模块
 */
package CalendarUnit;
import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * 存储待办集信息的类
 * @author 杨智方
 */
public class ToDoList implements Serializable {
    /**
     * 待办集的名称
     */
    private String name;
    /**
     * 显示待办集和其所持有的待办的面板
     */
    private JPanel bigListPanel;
    /**
     * 显示待办集持有的待办的面板
     */
    private JPanel smallListPanel;
    /**
     * 显示待办集的面板
     */
    private JPanel listPanel;
    /**
     * 待办集所持有的待办的集合
     */
    public ArrayList<ToDos> toDos;
    /**
     * ToDoList类的构造函数，用于初始化待办事项列表
     *
     * @param name 待办事项列表名称
     */
    public ToDoList(String name){
        this.name = name;
        bigListPanel = new JPanel();
        smallListPanel = new JPanel();
        listPanel = new JPanel(new BorderLayout());
        toDos = new ArrayList<>();
    }
    /**
     * 获取待办集和其持有的待办的面板
     *
     * @return 待办集和其持有的待办的面板
     */
    public JPanel getBigListPanel(){
        return bigListPanel;
    }
    /**
     * 设置待办集面板
     *
     * @param panel 待办集面板
     */
    public void setListPanel(JPanel panel){
        listPanel = panel;
    }
    /**
     * 获取待办集面板
     *
     * @return 待办集面板
     */
    public JPanel getListPanel(){
        return listPanel;
    }
    /**
     * 获取待办集中显示所有待办的面板
     *
     * @return 待办集中显示所有待办的面板
     */
    public JPanel getSmallListPanel(){
        return smallListPanel;
    }
    /**
     * 获取待办事项列表名称
     *
     * @return 待办事项列表名称
     */
    public String getName() {
        return name;
    }

}

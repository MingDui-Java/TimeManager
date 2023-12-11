package View;

import Model.TodoItem;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 15252
 */
public class TaskCellRenderer extends DefaultListCellRenderer {

    private static final int BORDER_THICKNESS = 2;
    private static final Color BORDER_COLOR = Color.GRAY;
    private static final Color SHADOW_COLOR = new Color(0, 0, 0, 50);

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        // 检查值是否为TodoItem
        if (value instanceof TodoItem) {
            TodoItem task = (TodoItem) value;

            // 创建一个样式属性映射，用于设置字体和大小
            Map<TextAttribute, Object> fontAttributes = new HashMap<>();
            Font currentFont = getFont();

            // 设置字体样式（例如，"SansSerif"是字体名称，Font.PLAIN表示常规风格，14是字体大小）
            fontAttributes.put(TextAttribute.FAMILY, "SansSerif");
            fontAttributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD); // 设置为粗体
            fontAttributes.put(TextAttribute.SIZE, 14); // 设置字体大小

            // 创建新字体并应用样式
            Font newFont = currentFont.deriveFont(fontAttributes);
            setFont(newFont);

            // 格式化任务在列表中的显示方式
            String taskText = "<html><b>" + task.getTitle() + "</b><br>" +
                    "描述：" + task.getDescription() + "<br>" +
                    "时间：" + task.getRemainingTime() / 60 + " 分钟</html>";
            setText(taskText);

            // 设置单元格边框样式
            Border border = BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR, BORDER_THICKNESS),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
            );
            setBorder(border);
            setOpaque(true); // 设置单元格为不透明，以便背景颜色生效
            if (isSelected) {
                setBackground(list.getSelectionBackground()); // 选中时的背景色
                setForeground(list.getSelectionForeground()); // 选中时的前景色
            } else {
                setBackground(list.getBackground()); // 普通状态下的背景色
                setForeground(list.getForeground()); // 普通状态下的前景色
            }

            // 添加阴影效果
            if (!isSelected) {
                setBorder(BorderFactory.createCompoundBorder(
                        border,
                        BorderFactory.createMatteBorder(0, 0, BORDER_THICKNESS, 0, SHADOW_COLOR)
                ));
            }
        }

        return this;
    }
}

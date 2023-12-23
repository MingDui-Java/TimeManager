package reminder.tool;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

/**
 * 为标签添加鼠标悬停动效监听实现颜色变换
 * 
 * @author 86155
 **/
public class MouseInOut {
	/**
	 * 静态工具类不需要调用构造器
	 */
	public MouseInOut() {
		;
	}

	/**
	 * 为标签添加鼠标悬停动效监听实现颜色变换
	 * 
	 * @param jl 需要添加监听的标签
	 */
	public static void addColorChange(JLabel jl) {
		jl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				JLabel but = (JLabel) e.getSource(); // 鼠标指针指向标签
				but.setForeground(Color.WHITE);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				JLabel but = (JLabel) e.getSource(); // 鼠标指针离开标签
				but.setForeground(Color.BLACK);
			}
		});
	}
}

package notepad;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JLabel;

/**
 * 纯色填充的矩形标签
 *
 * @author Aintme
 * @version 1.0
 */
public class ColoredRectangleLabel extends JLabel {
	/**
	 * ColoredRectangleLabel类版本的标识符
	 */
	private static final long serialVersionUID = 5901245089028947749L;

	/**
	 * 纯色填充的矩形图标
	 *
	 * @author Aintme
	 * @version 1.0
	 */
	class ColoredRectangleIcon implements Icon {
		/**
		 * 填充的颜色
		 */
		private Color color;
		/**
		 * 宽度
		 */
		private int width;
		/**
		 * 高度
		 */
		private int height;

		/**
		 * 创建一个颜色为color，宽度为width，高度为height的纯色填充矩形图标
		 *
		 * @param color  填充的颜色
		 * @param width  矩形宽度
		 * @param height 矩形高度
		 */
		public ColoredRectangleIcon(Color color, int width, int height) {
			this.color = color;
			this.width = width;
			this.height = height;
		}

		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
			g.setColor(color);
			g.fillRect(x, y, width, height);
		}

		@Override
		public int getIconWidth() {
			return width;
		}

		@Override
		public int getIconHeight() {
			return height;
		}

		/**
		 * 设置图标颜色
		 * 
		 * @param color 要设置的颜色
		 */
		public void setColor(Color color) {
			this.color = color;
		}

		/**
		 * 获取图标颜色
		 * 
		 * @return 图表的颜色
		 */
		public Color getColor() {
			return color;
		}
	}

	/**
	 * 用于生成纯色矩形标签的图标
	 */
	private ColoredRectangleIcon coloredRectangleIcon;

	/**
	 * 创建一个颜色为color，宽度为width，高度为height的纯色填充矩形标签
	 * 
	 * @param color  填充的颜色
	 * @param width  矩形宽度
	 * @param height 矩形高度
	 */
	public ColoredRectangleLabel(Color color, int width, int height) {
		coloredRectangleIcon = new ColoredRectangleIcon(color, width, height);
		setIcon(coloredRectangleIcon);
	}

	/**
	 * 设置标签颜色
	 * 
	 * @param color 要设置的颜色
	 */
	public void setColor(Color color) {
		coloredRectangleIcon.setColor(color);
		revalidate();
		repaint();
	}

	/**
	 * 获取标签颜色
	 * 
	 * @return 标签的颜色
	 */
	public Color getColor() {
		return coloredRectangleIcon.getColor();
	}
}

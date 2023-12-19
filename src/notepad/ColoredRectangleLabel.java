package notepad;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JLabel;

public class ColoredRectangleLabel extends JLabel {

	class ColoredRectangleIcon implements Icon {
		private Color color;
		private int width;
		private int height;

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

		public void setColor(Color color) {
			this.color = color;
		}

		public Color getColor() {
			return color;
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 5901245089028947749L;
	private ColoredRectangleIcon coloredRectangleIcon;

	public ColoredRectangleLabel(Color color, int width, int height) {
		coloredRectangleIcon = new ColoredRectangleIcon(color, width, height);
		setIcon(coloredRectangleIcon);
	}

	public void setColor(Color color) {
		coloredRectangleIcon.setColor(color);
		revalidate();
		repaint();
	}

	public Color getColor() {
		return coloredRectangleIcon.getColor();
	}
}

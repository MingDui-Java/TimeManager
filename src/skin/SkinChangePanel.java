package skin;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * 采用单例模式的主题切换面板
 * 
 * @author Aintme
 * @version 1.0
 */
public class SkinChangePanel extends JPanel {

	/**
	 * SkinChangePanel类版本的标识符
	 */
	private static final long serialVersionUID = -1605009740166587569L;
	/**
	 * 主题切换面板单例
	 */
	static private SkinChangePanel instance;

	/**
	 * 创建一个主题切换面板实例
	 */
	private SkinChangePanel() {
		Box skinBox = Box.createVerticalBox();
		for (int i = 0; i < 5; i++) {
			skinBox.add(new SkinPanel(i));
		}
		JScrollPane scrollPane = new JScrollPane(skinBox);
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
		setVisible(true);
	}

	/**
	 * 获取唯一的主题切换面板实例
	 * 
	 * @return 唯一的主题切换面板实例
	 */
	static public SkinChangePanel getInstance() {
		if (instance == null) {
			instance = new SkinChangePanel();
		}
		return instance;
	}

	/**
	 * 显示主题更换提示信息弹窗
	 */
	public void showMessage() {
		JOptionPane.showMessageDialog(null, "新主题将在程序重启后生效", "主题", JOptionPane.INFORMATION_MESSAGE);
	}
}

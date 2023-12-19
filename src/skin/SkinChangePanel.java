package skin;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class SkinChangePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1605009740166587569L;
	static private SkinChangePanel instance;

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

	static public SkinChangePanel getInstance() {
		if (instance == null) {
			instance = new SkinChangePanel();
		}
		return instance;
	}

	public void showMessage() {
		JOptionPane.showMessageDialog(null, "新主题将在程序重启后生效", "主题", JOptionPane.INFORMATION_MESSAGE);
	}
}

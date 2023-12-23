package skin;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import timemanager.TimeManagerFrame;

/**
 * 主题预览面板
 * 
 * @author Aintme
 * @version 1.0
 */
public class SkinPanel extends JPanel {

	/**
	 * SkinPanel类版本的标识符
	 */
	private static final long serialVersionUID = -1575601851004457341L;

	/**
	 * 创建一个主题预览面板
	 */
	public SkinPanel(int indexOfSkin) {
		setLayout(new BorderLayout());
		JPanel imagePanel = new JPanel();
		imagePanel.setLayout(new GridLayout(2, 2));
		for (int i = 0; i < 4; i++) {
			JLabel label = new JLabel(
					new ImageIcon(TimeManagerFrame.class.getResource("/image/" + indexOfSkin + "_" + i + ".png")));
			imagePanel.add(label);
		}
		add(imagePanel, BorderLayout.CENTER);
		JButton changeButton = new JButton("<html><center>更<br>换<br>为<br>该<br>主<br>题</center></html>");
		changeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SkinSetting.changeSkin(indexOfSkin);
				SkinChangePanel.getInstance().showMessage();
			}
		});
		add(changeButton, BorderLayout.EAST);
	}
}

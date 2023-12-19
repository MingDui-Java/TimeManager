package notepad;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.LineBorder;

import reminder.page.Review;
import timemanager.TimeManagerFrame;

public class ContentPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3247059258782932988L;
//	private static final GridBagConstraints gbc = new GridBagConstraints();
//	static {
//		gbc.fill = GridBagConstraints.HORIZONTAL; // 水平拉伸
//		gbc.weightx = 1.0; // 使组件充满水平空间
//	}
	private FileInfo fileInfo;
	private JPopupMenu popupMenu;

	public ContentPanel(FileInfo fileInfo) {
		this.fileInfo = fileInfo;
		setLayout(new BorderLayout());
		popupMenu = new JPopupMenu();
		JMenuItem deleteItem = new JMenuItem("删除");
		deleteItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(null, "确定删除该笔记吗（不可恢复）?", "删除确认", JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					FileManager.getInstance().deleteFileInfo(fileInfo);
				}
			}
		});
		popupMenu.add(deleteItem);
		JMenuItem renameItem = new JMenuItem("重命名");
		renameItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String newName = JOptionPane.showInputDialog("输入新的笔记名称:");
				if (newName != null) {
					if (!newName.toLowerCase().endsWith(".tmnote")) {
						newName += ".tmnote";
					}
					if (!newName.equals(fileInfo.getName())) {
						if (fileInfo.rename(newName)) {
							FileManager.getInstance().updateContent();
						}
					}
				}
			}
		});
		popupMenu.add(renameItem);
		JMenuItem reminderItem = new JMenuItem("添加到提醒事项");
		reminderItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Review.getInstance().addFromNote(fileInfo);
				TimeManagerFrame.showReminder();
			}
		});
		popupMenu.add(reminderItem);
		JLabel nameLabel = new JLabel(fileInfo.getName());
		JLabel modifyLabel = new JLabel("  最近的一次修改：" + fileInfo.getDate());
		nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		modifyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(nameLabel, BorderLayout.WEST);
		panel.add(Box.createHorizontalStrut(30));
		panel.add(modifyLabel, BorderLayout.EAST);
		add(panel, BorderLayout.CENTER);
		add(Box.createHorizontalStrut(20), BorderLayout.WEST);
		add(Box.createHorizontalStrut(20), BorderLayout.EAST);
		setPreferredSize(new Dimension(getPreferredSize().width, 40));
		setMinimumSize(new Dimension(getPreferredSize().width, 40));
		setMaximumSize(new Dimension(getPreferredSize().width, 40));
		setBorder(new LineBorder(Color.BLACK, 1, true));
		setVisible(true);
	}

	public void openFile() {
		File file = fileInfo.getFile();
		if (file.exists()) {
			FileEditor.getInstance().openfile(fileInfo);
			NotepadPanel.getInstance().showEditor();
		} else {
			JOptionPane.showMessageDialog(null, "文件不存在，可能已经被删除或者移动", "错误", JOptionPane.INFORMATION_MESSAGE);
			FileManager.getInstance().deleteFileInfo(file);
		}
	}

	public void showPopup(JList<ContentPanel> contentList, MouseEvent e) {
		popupMenu.show(contentList, e.getX(), e.getY());
	}
}

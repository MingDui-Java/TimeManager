package notepad;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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

import reminder.page.Review;
import timemanager.TimeManagerFrame;

public class ContentPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3247059258782932988L;
	private static final GridBagConstraints gbc = new GridBagConstraints();
	static {
		gbc.fill = GridBagConstraints.HORIZONTAL; // 水平拉伸
		gbc.weightx = 1.0; // 使组件充满水平空间
	}
	private FileInfo fileInfo;
	private JPopupMenu popupMenu;

	public ContentPanel(FileInfo fileInfo) {
		this.fileInfo = fileInfo;
		setLayout(new GridBagLayout());
		popupMenu = new JPopupMenu();
		JMenuItem deleteItem = new JMenuItem("Delete");
		deleteItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(null, "Are you sure to delete the note?", "Confirm Delete",
						JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					FileManager.getInstance().deleteFileInfo(fileInfo);
				}
			}
		});
		popupMenu.add(deleteItem);
		JMenuItem renameItem = new JMenuItem("Rename");
		renameItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String newName = JOptionPane.showInputDialog("Enter new name:");
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
		JMenuItem reminderItem = new JMenuItem("Add To Reminder");
		reminderItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Review.getInstance().addFromNote(fileInfo);
				TimeManagerFrame.showReminder();
			}
		});
		popupMenu.add(reminderItem);
		JLabel nameLabel = new JLabel(fileInfo.getName());
		JLabel modifyLabel = new JLabel(" The latest modify: " + fileInfo.getDate());
		nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		modifyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(nameLabel, BorderLayout.WEST);
		panel.add(Box.createHorizontalStrut(30));
		panel.add(modifyLabel, BorderLayout.EAST);
		add(panel, gbc);
		setPreferredSize(new Dimension(getPreferredSize().width, 40));
		setMinimumSize(new Dimension(getPreferredSize().width, 40));
		setMaximumSize(new Dimension(getPreferredSize().width, 40));
		setVisible(true);
	}

	public void openFile() {
		File file = fileInfo.getFile();
		if (file.exists()) {
			FileEditor.getInstance().openfile(fileInfo);
			NotepadPanel.getInstance().showEditor();
		} else {
			JOptionPane.showMessageDialog(null, "Note does not exit, may be moved or deleted.", "Error",
					JOptionPane.INFORMATION_MESSAGE);
			FileManager.getInstance().deleteFileInfo(file);
		}
	}

	public void showPopup(JList<ContentPanel> contentList, MouseEvent e) {
		popupMenu.show(contentList, e.getX(), e.getY());
	}
}

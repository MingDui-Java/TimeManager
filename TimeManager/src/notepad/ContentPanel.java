package notepad;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

public class ContentPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3247059258782932988L;

	public ContentPanel(FileInfo fileInfo) {
		setLayout(new BorderLayout());
		JLabel label = new JLabel(fileInfo.getName() + " The latest modify: " + fileInfo.getDate());
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					FileEditor.getInstance().openfile(fileInfo);
					NotepadPanel.getInstance().showEditor();
				}
			}
		});
		add(label, BorderLayout.CENTER);
		JPopupMenu popupMenu = new JPopupMenu();
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
					if (!newName.toLowerCase().endsWith(".tmnode")) {
						newName += ".tmnode";
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
		JButton button = new JButton("...");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int x = 0;
				int y = button.getHeight();
				popupMenu.show(button, x, y);
			}
		});
		add(button, BorderLayout.EAST);
		setPreferredSize(new Dimension(getPreferredSize().width, 40));
		setVisible(true);
	}
}

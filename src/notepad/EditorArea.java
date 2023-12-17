package notepad;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.undo.UndoManager;

public class EditorArea extends JTextArea {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6354421493441903496L;
	private FileInfo fileInfo = null;
	private String contentSaved;
	private UndoManager undoManager = new UndoManager();
	private int index;
	private JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
	private JDialog findDialog = null;
	{
		setWrapStyleWord(true);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					EditorMenuBar.getInstance().showPopupMenu(e);
				}
			}
		});
		getDocument().addUndoableEditListener(undoManager);
	}

	// 打开已有的文件
	public EditorArea(FileInfo fileInfo) {
		this.fileInfo = fileInfo;
		setLineWrap(FormatSetting.getInstance().getWrapped());
		setBackground(FormatSetting.getInstance().getBackgroundColor());
		setForeground(FormatSetting.getInstance().getForegroundColor());
		setFont(new Font("Diolag", 0, FormatSetting.getInstance().getFontSize()));
		contentSaved = FileIOUtil.loadFromFile(fileInfo.getPath());
		setText(contentSaved);
		undoManager.discardAllEdits(); // 清空撤销栈(内设字符串不需要撤销)
		setVisible(true);
	}

	// 创建一个新的文件
	public EditorArea() {
		setLineWrap(FormatSetting.getInstance().getWrapped());
		setBackground(FormatSetting.getInstance().getBackgroundColor());
		setForeground(FormatSetting.getInstance().getForegroundColor());
		setFont(new Font("Diolag", 0, FormatSetting.getInstance().getFontSize()));
		contentSaved = ""; // 新建文件内容为空
		setLineWrap(true);
		setVisible(true);
	}

	// 返回是否成功保存
	public boolean saveFile() {
		if (fileInfo == null) { // 新建
			File file = FileIOUtil.choosePath();
			if (file != null) {
				if (file.exists()) {
					int result = JOptionPane.showConfirmDialog(null, "File already exists. Do you want to overwrite?",
							"Confirm Overwrite", JOptionPane.YES_NO_OPTION);
					if (result == JOptionPane.NO_OPTION) {
						return false;
					} else {
						fileInfo = FileManager.getInstance().findFileInfo(file);
					}
				} else {
					fileInfo = new FileInfo(file);
					FileManager.getInstance().addFileInfo(fileInfo);
					FileManager.getInstance().saveFileInfos();
				}
				contentSaved = getText(); // 保存更新内容
				FileIOUtil.saveToFile(getText(), fileInfo.getPath());
				return true;
			}
			return false;
		} else {
			fileInfo.update();
			FileManager.getInstance().saveFileInfos();
			contentSaved = getText(); // 保存更新内容
			FileIOUtil.saveToFile(getText(), fileInfo.getPath());
			return true;
		}
	}

	public boolean closeFile() {
		if (findDialog != null) {
			findDialog.setVisible(false);
		}
		if (!contentSaved.equals(getText())) {
			int result = JOptionPane.showConfirmDialog(null, "File not saved. Do you want to save?", "Confirm Save",
					JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.NO_OPTION) {
				return true;
			}
			return saveFile();
		}
		return true;
	}

	public void undo() {
		if (undoManager.canUndo()) {
			undoManager.undo();
		}
	}

	public void redo() {
		if (undoManager.canRedo()) {
			undoManager.redo();
		}
	}

	public void showFind() {
		index = 0;
		if (findDialog == null) {
			findDialog = new JDialog(parentFrame, "Find/Replace", false);
			findDialog.setLayout(new FlowLayout());
			findDialog.add(new JLabel("Find:"));
			JTextField findField = new JTextField(20);
			findField.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent e) {
					index = 0;
				}

				@Override
				public void insertUpdate(DocumentEvent e) {
					index = 0;
				}

				@Override
				public void changedUpdate(DocumentEvent e) {
					index = 0;
				}
			});
			findDialog.add(findField);
			findDialog.add(new JLabel("Replace:"));
			JTextField replaceField = new JTextField(20);
			findDialog.add(replaceField);
			JButton findButton = new JButton("FindNext");
			findButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					find(findField.getText(), true);
				}
			});
			findDialog.add(findButton);
			JButton replaceButton = new JButton("Replace");
			replaceButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					replace(findField.getText(), replaceField.getText());
				}
			});
			findDialog.add(replaceButton);
			JButton findAndReplaceButton = new JButton("FindAndReplace");
			findAndReplaceButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					find(findField.getText(), true);
					replace(findField.getText(), replaceField.getText());
				}
			});
			findDialog.add(findAndReplaceButton);
			JButton replaceAllButton = new JButton("ReplaceAll");
			replaceAllButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					index = 0;
					int count = 0; // 记录替换的次数
					while (find(findField.getText(), false)) {
						count++;
						replace(findField.getText(), replaceField.getText());
					}
					JOptionPane.showMessageDialog(null, String.format("%d places have been replaced", count),
							"ReplaceAll", JOptionPane.INFORMATION_MESSAGE);
				}
			});
			findDialog.add(replaceAllButton);
			findDialog.setSize(300, 165);
			findDialog.setResizable(false);
			findDialog.setLocationRelativeTo(parentFrame);
		}
		findDialog.setVisible(true);
	}

	// 返回是否找到了
	boolean find(String findText, boolean showNotFind) {
		requestFocus();
		String text = getText();
		index = text.indexOf(findText, index);
		if (index == -1) {
			if (showNotFind) {
				JOptionPane.showMessageDialog(null, "Text not found.", "Find", JOptionPane.INFORMATION_MESSAGE);
			}
			index = 0;
			return false;
		} else {
			setSelectionStart(index);
			setSelectionEnd(index + findText.length());
			index += findText.length();
			return true;
		}
	}

	void replace(String findText, String replaceText) {
		if (getSelectedText() != null && getSelectedText().equals(findText)) {
			replaceSelection(replaceText);
			index += replaceText.length() - findText.length(); // 修改index防止死循环
		}
	}
}

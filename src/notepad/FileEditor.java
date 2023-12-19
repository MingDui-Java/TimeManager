package notepad;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class FileEditor extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6982642957521472039L;

	private static FileEditor instance = null;
	private EditorArea editorArea = null;
	private JScrollPane scrollPane = null;
	private MouseWheelListener sysWheelListener = null;

	private FileEditor() {
		setLayout(new BorderLayout());
		add(EditorMenuBar.getInstance(), BorderLayout.NORTH);
		setVisible(true);
		scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		sysWheelListener = scrollPane.getMouseWheelListeners()[0];
		scrollPane.removeMouseWheelListener(sysWheelListener); // 移除系统默认滚轮事件防止缩放和滚动冲突
		scrollPane.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.isControlDown()) { // 通过调整字号实现缩放
					if (editorArea != null) {
						Font font = editorArea.getFont();
						if (e.getWheelRotation() < 0) { // 向前滚放大
							editorArea.setFont(new Font(font.getFamily(), font.getStyle(), font.getSize() + 1));
						} else {
							editorArea.setFont(new Font(font.getFamily(), font.getStyle(),
									font.getSize() - 1 >= 1 ? font.getSize() - 1 : 1));
						}
					}
				} else {
					scrollPane.addMouseWheelListener(sysWheelListener);
					sysWheelListener.mouseWheelMoved(e); // 触发系统滚动事件。
					scrollPane.removeMouseWheelListener(sysWheelListener);
				}
			}
		});
		add(scrollPane, BorderLayout.CENTER);
	}

	public static FileEditor getInstance() {
		if (instance == null) {
			instance = new FileEditor();
		}
		return instance;
	}

	public void newFile() {
		if (editorArea != null) { // 先remove防止新的实例被覆盖
			remove(editorArea);
		}
		editorArea = new EditorArea();
		scrollPane.setViewportView(editorArea);
		revalidate();
		repaint();
	}

	public boolean saveFile() {
		return editorArea.saveFile();
	}

	public void closeFile() {
		if (editorArea != null) {
			if (editorArea.closeFile()) {
				FormatSetting.getInstance().setFontSize(editorArea.getFont().getSize());
				EditorMenuBar.getInstance().close();
				FileManager.getInstance().updateContent();
				NotepadPanel.getInstance().showManager();
				editorArea = null;
			}
		}
	}

	public void openfile(FileInfo fileInfo) {
		editorArea = new EditorArea(fileInfo);
		scrollPane.setViewportView(editorArea);
		revalidate();
		repaint();
	}

	public void undo() {
		editorArea.undo();
	}

	public void redo() {
		editorArea.redo();
	}

	public void cut() {
		editorArea.cut();
	}

	public void copy() {
		editorArea.copy();
	}

	public void paste() {
		editorArea.paste();
	}

	public void delete() {
		editorArea.replaceSelection("");
	}

	public void selectAll() {
		editorArea.requestFocus();
		editorArea.selectAll();
	}

	public void showFind() {
		editorArea.showFind();
	}

	public void setWrapped(boolean wrapped) {
		FormatSetting.getInstance().setWrapped(wrapped);
		if (editorArea != null) {
			editorArea.setLineWrap(wrapped);
			editorArea.revalidate();
			editorArea.repaint();
		}
	}

	public void setBackgroundColor(Color backgroundColor) {
		FormatSetting.getInstance().setBackgroundColor(backgroundColor);
		if (editorArea != null) {
			editorArea.setBackground(backgroundColor);
			editorArea.revalidate();
			editorArea.repaint();
		}
	}

	public void setForegroundColor(Color foregroundColor) {
		FormatSetting.getInstance().setForegroundColor(foregroundColor);
		if (editorArea != null) {
			editorArea.setForeground(foregroundColor);
			editorArea.revalidate();
			editorArea.repaint();
		}
	}
}

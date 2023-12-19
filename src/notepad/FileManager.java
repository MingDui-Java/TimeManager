package notepad;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class FileManager extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6126239214991271975L;
	private static final String FILEINFOSPATH = "./data/fileinfos";

	private static FileManager instance = null;
	private List<FileInfo> fileInfos;
	private JList<ContentPanel> contentList;
	private DefaultListModel<ContentPanel> listModel;
	private Box defaultBox;
	private JScrollPane scrollPane;

	@SuppressWarnings("unchecked")
	private FileManager() {
		// 反序列化获取笔记列表
		File file = new File(FILEINFOSPATH);
		if (file.exists()) {
			try {
				FileInputStream fis = new FileInputStream(file);
				ObjectInputStream ois = new ObjectInputStream(fis);
				fileInfos = (List<FileInfo>) ois.readObject();
				ois.close();
				fis.close();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			fileInfos = new ArrayList<FileInfo>();
		}
		JLabel defaultLabel1 = new JLabel("当前没有笔记文件");
		defaultLabel1.setAlignmentX(Component.CENTER_ALIGNMENT);
		defaultLabel1.setForeground(Color.GRAY);
		defaultBox = Box.createVerticalBox();
		defaultBox.add(defaultLabel1, Box.CENTER_ALIGNMENT);
		JLabel defaultLabel2 = new JLabel("点击下方按钮新建或者导入吧");
		defaultLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);
		defaultLabel2.setForeground(Color.GRAY);
		defaultBox.add(defaultLabel2, Box.CENTER_ALIGNMENT);
		listModel = new DefaultListModel<>();
		contentList = new JList<>(listModel) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1409344193049405990L;

			@Override
			public int locationToIndex(Point location) {
				int index = super.locationToIndex(location);
				if (index != -1 && !getCellBounds(index, index).contains(location)) {
					return -1;
				} else {
					return index;
				}
			}
		};
		contentList.setCellRenderer(new DefaultListCellRenderer() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -6290993242483926649L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				// Use the JPanel for rendering
				return (Component) value;
			}
		});
		contentList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = contentList.locationToIndex(e.getPoint());
				if (index != -1) {
					ContentPanel selectedValue = listModel.getElementAt(index);
					if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
						// 双击左键的处理逻辑
						if (selectedValue != null) {
							selectedValue.openFile();
						}
					} else if (SwingUtilities.isRightMouseButton(e)) {
						// 右键单击的处理逻辑
						if (selectedValue != null) {
							selectedValue.showPopup(contentList, e);
						}
					}
				}
			}
		});
		scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//		scrollPane.setViewportView(contentList); // 将 contentBox 设置为视口的显示区域
		setLayout(new BorderLayout());
		updateContent();
		add(scrollPane, BorderLayout.CENTER);

//		Box buttonBox = Box.createHorizontalBox();
//		buttonBox.add(Box.createHorizontalGlue());
//		buttonPanel.setLayout(new BorderLayout());
		// 新增按钮
		JButton newButton = new JButton("新建笔记");
		newButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileEditor.getInstance().newFile();
				NotepadPanel.getInstance().showEditor();
			}
		});
//		buttonBox.add(newButton);
//		buttonBox.add(Box.createHorizontalGlue());
		// 导入按钮
		JButton importButton = new JButton("导入笔记");
		importButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileInfo fileInfo = NotepadPanel.getInstance().selectFile();
				if (fileInfo != null) {
					FileEditor.getInstance().openfile(fileInfo);
					NotepadPanel.getInstance().showEditor();
				}
			}
		});
//		buttonBox.add(importButton);
//		buttonBox.add(Box.createHorizontalGlue());
//		add(buttonBox, BorderLayout.SOUTH);
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(newButton);
		buttonPanel.add(importButton);
		add(buttonPanel, BorderLayout.SOUTH);
		setVisible(true);
	}

	public void updateContent() {
		if (fileInfos.isEmpty()) { // 如果不存在文件
			scrollPane.setViewportView(defaultBox);
		} else {
			listModel.clear();
			// 对fileInfos按照date排序
			Collections.sort(fileInfos, new Comparator<FileInfo>() {
				@Override
				public int compare(FileInfo o1, FileInfo o2) {
					return -o1.getDate().compareTo(o2.getDate());
				}
			});
			for (FileInfo fileInfo : fileInfos) {
				listModel.addElement(new ContentPanel(fileInfo));
			}
			scrollPane.setViewportView(contentList);
		}
		defaultBox.revalidate();
		defaultBox.repaint();
		saveFileInfos();
		revalidate();
		repaint();
	}

	public static FileManager getInstance() {
		if (instance == null) {
			instance = new FileManager();
		}
		return instance;
	}

	public void addFileInfo(FileInfo fileInfo) {
		fileInfos.add(fileInfo);
	}

	public void saveFileInfos() {
		File file = new File(FILEINFOSPATH);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(fileInfos);
			oos.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public FileInfo findFileInfo(File file) {
		for (FileInfo fileInfo : fileInfos) {
			if (fileInfo.infoOfFile(file)) {
				return fileInfo;
			}
		}
		return null;
	}

	public void deleteFileInfo(FileInfo fileinfo) {
		fileInfos.remove(fileinfo);
		fileinfo.delete();
		updateContent();
	}

	public void deleteFileInfo(File file) {
		deleteFileInfo(findFileInfo(file));
	}

	public void importFile(File file) {
		if (FileManager.getInstance().findFileInfo(file) == null) {
			addFileInfo(new FileInfo(file));
		}
		updateContent();
	}
}

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

/**
 * 采用单例模式的记事本文件管理面板
 * <p>
 * 显示文件管理中每个文件的信息
 * 
 * @author Aintme
 * @version 1.0
 * @see ContentPanel
 */
public class FileManager extends JPanel {

	/**
	 * FileManager 类版本的标识符
	 */
	private static final long serialVersionUID = -6126239214991271975L;
	/**
	 * 文件管理信息的序列化文件存储相对路径
	 */
	private static final String FILEINFOSPATH = "./data/fileinfos";
	/**
	 * 记事本文件管理面板单例
	 */
	private static FileManager instance = null;
	/**
	 * 文件管理中所有文件的信息
	 */
	private List<FileInfo> fileInfos;
	/**
	 * 显示所有文件信息的容器
	 */
	private JList<ContentPanel> contentList;
	/**
	 * contentList 的管理模型
	 */
	private DefaultListModel<ContentPanel> listModel;
	/**
	 * 文件管理中没有文件时显示的默认容器
	 */
	private Box defaultBox;
	/**
	 * 为文件信息添加的滚动条
	 */
	private JScrollPane scrollPane;

	/**
	 * 创建一个记事本文件管理面板实例
	 * <p>
	 * 创建一个记事本文件管理面板实例，同时会尝试从指定路径获取文件管理，如果获取失败会创建一个空白的文件管理
	 */
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
		// 文件管理中没有文件时的默认显示
		JLabel defaultLabel1 = new JLabel("当前没有笔记文件");
		defaultLabel1.setAlignmentX(Component.CENTER_ALIGNMENT);
		defaultLabel1.setForeground(Color.GRAY);
		defaultBox = Box.createVerticalBox();
		defaultBox.add(defaultLabel1, Box.CENTER_ALIGNMENT);
		JLabel defaultLabel2 = new JLabel("点击下方按钮新建或者导入吧");
		defaultLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);
		defaultLabel2.setForeground(Color.GRAY);
		defaultBox.add(defaultLabel2, Box.CENTER_ALIGNMENT);
		// 配置文件显示容器
		listModel = new DefaultListModel<>();
		contentList = new JList<>(listModel) {
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
		setLayout(new BorderLayout());
		updateContent();
		add(scrollPane, BorderLayout.CENTER);

		// 新增按钮
		JButton newButton = new JButton("新建笔记");
		newButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileEditor.getInstance().newFile();
				NotepadPanel.getInstance().showEditor();
			}
		});
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
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(newButton);
		buttonPanel.add(importButton);
		add(buttonPanel, BorderLayout.SOUTH);
		setVisible(true);
	}

	/**
	 * 更新显示文件信息的容器
	 * <p>
	 * 更新显示文件信息的容器，同时会序列化保存文件管理信息
	 */
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

	/**
	 * 获取唯一的记事本文件管理面板实例
	 * 
	 * @return 唯一的记事本文件管理面板实例
	 */
	public static FileManager getInstance() {
		if (instance == null) { // "懒汉"加载
			instance = new FileManager();
		}
		return instance;
	}

	/**
	 * 往文件管理中添加文件信息
	 * 
	 * @param fileInfo 需要添加的文件信息
	 */
	public void addFileInfo(FileInfo fileInfo) {
		fileInfos.add(fileInfo);
	}

	/**
	 * 序列化保存文件管理信息
	 */
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

	/**
	 * 在文件管理中查找指定文件的文件信息
	 * <p>
	 * 在文件管理中查找指定文件的文件信息，如果要查找的文件的文件信息不在文件管理中则返回NULL
	 * 
	 * @param file 需要查找的文件
	 * @return 查找的文件信息，如果要查找的文件的文件信息不在文件管理中则返回NULL
	 */
	public FileInfo findFileInfo(File file) {
		for (FileInfo fileInfo : fileInfos) {
			if (fileInfo.infoOfFile(file)) {
				return fileInfo;
			}
		}
		return null;
	}

	/**
	 * 从文件管理中删除指定的文件信息
	 * 
	 * @param fileInfo 需要删除的文件信息
	 */
	public void deleteFileInfo(FileInfo fileInfo) {
		if (fileInfo != null) {
			fileInfos.remove(fileInfo);
			fileInfo.delete();
			updateContent();
		}
	}

	/**
	 * 从文件管理中删除指定的文件的文件信息
	 * 
	 * @param file 需要删除文件信息的指定文件
	 */
	public void deleteFileInfo(File file) {
		deleteFileInfo(findFileInfo(file));
	}

	/**
	 * 导入现有文件的文件信息
	 * 
	 * @param file 需要导入文件信息的文件
	 */
	public void importFile(File file) {
		if (FileManager.getInstance().findFileInfo(file) == null) {
			addFileInfo(new FileInfo(file));
		}
		updateContent();
	}
}

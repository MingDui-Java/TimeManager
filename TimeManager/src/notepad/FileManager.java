package notepad;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class FileManager extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6126239214991271975L;
	private static final String FILEINFOSPATH = "./data/fileinfos";

	private static FileManager instance = null;
	private List<FileInfo> fileInfos;
	private Box contentBox;

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
		contentBox = Box.createVerticalBox();
		updateContent();
		JScrollPane scrollPane = new JScrollPane(contentBox);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setPreferredSize(new Dimension(getPreferredSize().width, 40)); // 设置 JScrollPane 的首选大小
		scrollPane.setViewportView(contentBox); // 将 contentBox 设置为视口的显示区域
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);

		// 新增按钮
		JButton newButton = new JButton("new a note");
		newButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileEditor.getInstance().newFile();
				NotepadPanel.getInstance().showEditor();
			}
		});
		add(newButton, BorderLayout.SOUTH);

		setVisible(true);
	}

	public void updateContent() {
		contentBox.removeAll();
		if (fileInfos.isEmpty()) { // 如果不存在文件
			JLabel label1 = new JLabel("no notes exits");
			label1.setAlignmentX(Component.CENTER_ALIGNMENT);
			label1.setForeground(Color.GRAY);
			contentBox.add(label1, Box.CENTER_ALIGNMENT);
			JLabel label2 = new JLabel("press \"new a note\"");
			label2.setAlignmentX(Component.CENTER_ALIGNMENT);
			label2.setForeground(Color.GRAY);
			contentBox.add(label2, Box.CENTER_ALIGNMENT);
		} else {
			// 对fileInfos按照date排序
			Collections.sort(fileInfos, new Comparator<FileInfo>() {
				@Override
				public int compare(FileInfo o1, FileInfo o2) {
					return -o1.getDate().compareTo(o2.getDate());
				}
			});
			for (FileInfo fileInfo : fileInfos) {
				contentBox.add(new ContentPanel(fileInfo), Box.CENTER_ALIGNMENT);
			}
		}
		contentBox.revalidate();
		contentBox.repaint();
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
}

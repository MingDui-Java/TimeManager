package notepad;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDate;

import javax.swing.JOptionPane;

/**
 * 文件信息，包含文件基本信息和修改日期
 * 
 * @author Aintme
 * @version 1.0
 * @serial 字段 file 和 date 会被保存
 */
public class FileInfo implements Serializable {
	/**
	 * FileInfo类版本的标识符
	 */
	private static final long serialVersionUID = -4407123003543449566L;
	/**
	 * 目标文件文件
	 */
	private File file;
	/**
	 * 文件的最新修改日期
	 */
	private LocalDate date;

	/**
	 * 创建一个文件信息
	 * 
	 * @param file 需要创建信息的文件
	 */
	public FileInfo(File file) {
		this.file = file;
		this.date = LocalDate.now();
	}

	/**
	 * 获取文件路径
	 * 
	 * @return 文件绝对路径
	 */
	public String getPath() {
		return file.getAbsolutePath();
	}

	/**
	 * 获取文件的修改日期
	 * 
	 * @return 文件的修改日期
	 */
	public LocalDate getDate() {
		return date;
	}

	/**
	 * 获取文件名称
	 * 
	 * @return 文件名称
	 */
	public String getName() {
		return file.getName();
	}

	/**
	 * 判断文件信息是否为该文件的信息
	 * 
	 * @param file 需要检测的文件
	 * @return 文件信息是否为该文件的信息
	 */
	public boolean infoOfFile(File file) {
		return file.equals(this.file);
	}

	/**
	 * 更新修改时间
	 */
	public void update() {
		date = LocalDate.now();
	}

	/**
	 * 尝试重命名文件
	 * <p>
	 * 尝试将原有的文件重命名，如果文件名存在会弹窗提醒用户是否覆盖
	 * 
	 * @param newName 新的文件名
	 * @return 文件重命名是否成功
	 */
	public boolean rename(String newName) {
		File newFile = new File(file.getParent() + "/" + newName);
		if (newFile.exists()) {
			int result = JOptionPane.showConfirmDialog(null, "文件已经存在，是否覆盖该文件？", "覆盖文件", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.NO_OPTION) {
				return false;
			} else {
				FileManager.getInstance().deleteFileInfo(newFile);
			}
		}
		file.renameTo(newFile);
		file = newFile;
		return true;
	}

	/**
	 * 删除文件
	 */
	public void delete() {
		file.delete();
	}

	/**
	 * 获取文件信息中的 file 字段
	 * 
	 * @return file 字段
	 */
	public File getFile() {
		return file;
	}
}

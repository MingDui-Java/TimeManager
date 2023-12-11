package notepad;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDate;

import javax.swing.JOptionPane;

public class FileInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4407123003543449566L;
	private File file;
	private LocalDate date;

	public FileInfo(File file) {
		this.file = file;
		this.date = LocalDate.now();
	}

	public String getPath() {
		return file.getAbsolutePath();
	}

	public LocalDate getDate() {
		return date;
	}

	public String getName() {
		return file.getName();
	}

	public boolean infoOfFile(File file) {
		return file.equals(this.file);
	}

	public void update() {
		date = LocalDate.now();
	}

	// 返回是否重命名成功
	public boolean rename(String newName) {
		File newFile = new File(file.getParent() + "/" + newName);
		if (newFile.exists()) {
			int result = JOptionPane.showConfirmDialog(null, "File already exists. Do you want to overwrite?",
					"Confirm Overwrite", JOptionPane.YES_NO_OPTION);
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

	public void delete() {
		file.delete();
	}
}

package notepad;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class FileIOUtil {

	public static void ckeckDir() {
		File file = new File("./Data");
		if (!(file.exists() && file.isDirectory())) {
			file.mkdir();
		}
		file = new File("./Note");
		if (!(file.exists() && file.isDirectory())) {
			file.mkdir();
		}
	}

	public static void saveToFile(String content, String filePath) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
			writer.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String loadFromFile(String filePath) {
		StringBuilder content = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				content.append(line).append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content.toString();
	}

	public static File choosePath() {
		JFrame frame = new JFrame("Path Chooser");
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("./Note"));
		int result = fileChooser.showOpenDialog(frame);
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			// 添加文件名后缀
			if (!file.getName().toLowerCase().endsWith(".tmnote")) {
				file = new File(file.getPath() + ".tmnote");
			}
			return file;
		} else {
			return null;
		}
	}
}

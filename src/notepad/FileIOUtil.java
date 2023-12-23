package notepad;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * 用于文件读写的工具类
 * 
 * @author Aintme
 * @version 1.0
 */
public class FileIOUtil {
	/**
	 * 这是一个静态工具类，不需要调用构造器
	 */
	private FileIOUtil() {
		;
	}

	/**
	 * 检查程序依赖目录
	 * <p>
	 * 检查程序依赖目录是否存在，如果不存在自动创建依赖目录
	 */
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

	/**
	 * 将字符串内容输入到指定路径的文件中
	 * 
	 * @param content  需要输入的字符串内容
	 * @param filePath 指定的文件路径
	 */
	public static void saveToFile(String content, String filePath) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
			writer.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取指定路径文件的内容
	 * 
	 * @param filePath 指定的文件路径
	 * @return 获取到的字符串内容
	 */
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

	/**
	 * 打开资源管理器选择文件路径
	 * <p>
	 * 打开资源管理器让用户选择文件路径，资源管理器中只会显示文件夹和笔记文件，该方法会屏蔽"提醒："开头的文件防止冲突，也会自动添加笔记文件的统一后缀名
	 * 
	 * @return 通过用户选择路径创建的文件
	 */
	public static File choosePath() {
		JFrame frame = new JFrame("文件选择");
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("./Note"));
		int result = fileChooser.showOpenDialog(frame);
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			// 添加文件名后缀
			if (file.getName().startsWith("提醒：")) {
				JOptionPane.showMessageDialog(null, "笔记不能以\"提醒：\"开头", "错误", JOptionPane.INFORMATION_MESSAGE);
				return null;
			} else if (!file.getName().toLowerCase().endsWith(".tmnote")) {
				file = new File(file.getPath() + ".tmnote");
			}
			return file;
		} else {
			return null;
		}
	}
}

package notepad;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 采用单例模式的文件编辑格式设置
 * 
 * 采用单例模式的文件编辑格式设置，用于 FileEditor 面板一些设置的保存
 * 
 * @author Aintme
 * @version 1.0
 * @see FileEditor
 * @serial 字段 wrapped backgroundColor foregroundColor fontSize 会被保存
 */
public class FormatSetting implements Serializable {
	/**
	 * FormatSetting类版本的标识符
	 */
	private static final long serialVersionUID = 963923461057038882L;
	/**
	 * 文件编辑格式设置的序列化文件存储相对路径
	 */
	private final static String FORMATSETTINGPATH = "./data/formatsetting";
	/**
	 * 文件编辑格式设置单例
	 */
	static private FormatSetting instance = null;
	/**
	 * 是否开启文本自动换行的标志位
	 */
	private boolean wrapped;
	/**
	 * 记事本编辑区域的背景颜色
	 */
	private Color backgroundColor;
	/**
	 * 记事本编辑区域的字体颜色
	 */
	private Color foregroundColor;
	/**
	 * 记事本编辑区域的字号大小
	 */
	private int fontSize;

	/**
	 * 创建一个文件编辑格式设置实例
	 * 
	 * @param wrapped         是否自动换行
	 * @param backgroundColor 背景颜色
	 * @param foregroundColor 字体颜色
	 * @param fontSize        字体大小
	 */
	private FormatSetting(boolean wrapped, Color backgroundColor, Color foregroundColor, int fontSize) {
		this.wrapped = wrapped;
		this.backgroundColor = backgroundColor;
		this.foregroundColor = foregroundColor;
		this.fontSize = fontSize;
	}

	/**
	 * 获取格式设置保存的文本自动换行的标志位
	 * 
	 * @return 文本自动换行的标志位
	 */
	public boolean getWrapped() {
		return wrapped;
	}

	/**
	 * 保存文本自动换行的标志位
	 * 
	 * @param wrapped 设置的自动换行标志位
	 */
	public void setWrapped(boolean wrapped) {
		this.wrapped = wrapped;
		saveFormatSetting();
	}

	/**
	 * 获取格式设置保存的记事本编辑区域的背景颜色
	 * 
	 * @return 记事本编辑区域的背景颜色
	 */
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * 保存记事本编辑区域的背景颜色
	 * 
	 * @param background 设置的背景颜色
	 */
	public void setBackgroundColor(Color background) {
		this.backgroundColor = background;
		saveFormatSetting();
	}

	/**
	 * 获取格式设置保存记事本编辑区域的字体颜色
	 * 
	 * @return 记事本编辑区域的字体颜色
	 */
	public Color getForegroundColor() {
		return foregroundColor;
	}

	/**
	 * 保存记事本编辑区域的背景颜色
	 * 
	 * @param foreground 设置的字体颜色
	 */
	public void setForegroundColor(Color foreground) {
		this.foregroundColor = foreground;
		saveFormatSetting();
	}

	/**
	 * 获取格式设置保存记事本编辑区域的字号大小
	 * 
	 * @return 记事本编辑区域的字号大小
	 */
	public int getFontSize() {
		return fontSize;
	}

	/**
	 * 保存记事本编辑区域的字号大小
	 * 
	 * @param fontSize 字号大小
	 */
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
		saveFormatSetting();
	}

	/**
	 * 获取唯一的文件编辑格式设置实例
	 * <p>
	 * 尝试从指定路径获取文件编辑格式设置，如果获取失败会创建一个默认的文件编辑格式设置
	 * 
	 * @return 唯一的文件编辑格式设置实例
	 */
	static public FormatSetting getInstance() {
		if (instance == null) {
			// 读取格式设置文件
			File file = new File(FORMATSETTINGPATH);
			if (!file.exists()) {
				instance = new FormatSetting(false, Color.white, Color.black, 12);
				instance.saveFormatSetting();
			} else {
				try {
					FileInputStream fis = new FileInputStream(file);
					ObjectInputStream ois = new ObjectInputStream(fis);
					instance = (FormatSetting) (ois.readObject());
					ois.close();
					fis.close();
				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		return instance;
	}

	/**
	 * 将文件编辑格式设置序列化并保存在指定路径
	 */
	private void saveFormatSetting() {
		File file = new File(FORMATSETTINGPATH);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(this);
			oos.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

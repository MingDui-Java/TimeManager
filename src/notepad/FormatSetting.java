package notepad;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class FormatSetting implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 963923461057038882L;
	private final static String FORMATSETTINGPATH = "./data/formatsetting";
	static private FormatSetting instance = null;
	private boolean wrapped;
	private Color backgroundColor;
	private Color foregroundColor;
	private int fontSize;

	private FormatSetting(boolean wrapped, Color backgroundColor, Color foregroundColor, int fontSize) {
		this.wrapped = wrapped;
		this.backgroundColor = backgroundColor;
		this.foregroundColor = foregroundColor;
		this.fontSize = fontSize;
	}

	public boolean getWrapped() {
		return wrapped;
	}

	public void setWrapped(boolean wrapped) {
		this.wrapped = wrapped;
		saveFormatSetting();
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color background) {
		this.backgroundColor = background;
		saveFormatSetting();
	}

	public Color getForegroundColor() {
		return foregroundColor;
	}

	public void setForegroundColor(Color foreground) {
		this.foregroundColor = foreground;
		saveFormatSetting();
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
		saveFormatSetting();
	}

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

package skin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.swing.UnsupportedLookAndFeelException;

public class SkinSetting implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2858687327795081262L;
	private static final String[] SKIN_STRINGS = { "com.jtattoo.plaf.hifi.HiFiLookAndFeel",
			"com.jtattoo.plaf.mint.MintLookAndFeel", "com.jtattoo.plaf.acryl.AcrylLookAndFeel",
			"com.jtattoo.plaf.bernstein.BernsteinLookAndFeel", "com.jtattoo.plaf.smart.SmartLookAndFeel" };
	private static final String SKINSETTINGPATH = "./data/skinsetting";
	private static SkinSetting instance;

	private int indexOfSkin;

	private SkinSetting() {
		indexOfSkin = 0;
	}

	public static void setSkin() {
		File file = new File(SKINSETTINGPATH);
		if (!file.exists()) {
			instance = new SkinSetting();
			instance.saveFormatSetting();
		} else {
			try {
				FileInputStream fis = new FileInputStream(file);
				ObjectInputStream ois = new ObjectInputStream(fis);
				instance = (SkinSetting) (ois.readObject());
				ois.close();
				fis.close();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		try {
			javax.swing.UIManager.setLookAndFeel(SKIN_STRINGS[instance.indexOfSkin]);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	public static void changeSkin(int indexOfSkin) {
		instance.indexOfSkin = indexOfSkin;
		instance.saveFormatSetting();
	}

	private void saveFormatSetting() {
		File file = new File(SKINSETTINGPATH);
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

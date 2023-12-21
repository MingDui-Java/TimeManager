package skin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.swing.UnsupportedLookAndFeelException;

/**
 * 采用单例模式的主题设置
 * 
 * @author Aintme
 * @version 1.0
 * @serial 字段 indexOfSkin 会被保存
 */
public class SkinSetting implements Serializable {

	/**
	 * SkinSetting 类版本的标识符
	 */
	private static final long serialVersionUID = 2858687327795081262L;
	/**
	 * 所有预设主题的类名
	 */
	private static final String[] SKIN_STRINGS = { "com.jtattoo.plaf.hifi.HiFiLookAndFeel",
			"com.jtattoo.plaf.mint.MintLookAndFeel", "com.jtattoo.plaf.acryl.AcrylLookAndFeel",
			"com.jtattoo.plaf.bernstein.BernsteinLookAndFeel", "com.jtattoo.plaf.smart.SmartLookAndFeel" };
	/**
	 * 主题设置的序列化文件存储相对路径
	 */
	private static final String SKINSETTINGPATH = "./data/skinsetting";
	/**
	 * 主题设置单例
	 */
	private static SkinSetting instance;
	/**
	 * 所设置主题的序号
	 * 
	 * 下标从0开始
	 */
	private int indexOfSkin;

	/**
	 * 创建一个主题设置实例，并且初始化主题序号为0
	 */
	private SkinSetting() {
		indexOfSkin = 0;
	}

	/**
	 * 设置主题
	 * 
	 * 尝试从指定路径后去主题设置单例，如果获取失败创建默认的主题设置单例，根据主题设置单例中的主题序号设置主题
	 */
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

	/**
	 * 更换主题
	 * <p>
	 * 更换主题并保存主题设置
	 * 
	 * @param indexOfSkin 更换成的主题序号
	 */
	public static void changeSkin(int indexOfSkin) {
		instance.indexOfSkin = indexOfSkin;
		instance.saveFormatSetting();
	}

	/**
	 * 序列化保存主题设置
	 */
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

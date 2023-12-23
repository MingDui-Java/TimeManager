package reminder.entity;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 喝水记录信息
 * 
 * @author 86155
 **/
public class DW implements Serializable {
	/**
	 * 喝水的记录
	 */
	String jiLu = "";
	/**
	 * 喝水的次数
	 */
	int ciShu = 0;
	/**
	 * 喝水的日期
	 */
	LocalDate date = LocalDate.now();

	/**
	 * 创建一个喝水记录实例
	 * 
	 * @param time 喝水的时间字符串
	 * @param idx  喝水的次数
	 */
	public DW(String time, int idx) {
		jiLu = time;
		ciShu = idx;
	}

	@Override
	public String toString() {
		return "今天第" + ciShu + "次喝水的时间为： " + jiLu;
	}

	/**
	 * 获取喝水记录的日期
	 * 
	 * @return 喝水记录的日期
	 */
	public LocalDate getDate() {
		return date;
	}
}

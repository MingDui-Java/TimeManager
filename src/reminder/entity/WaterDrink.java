/**
 * 提醒事项和喝水提醒的实体包
 *
 * @author DdddM
 * @version 1.0
 */
package reminder.entity;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 喝水记录
 *
 * @author DdddM
 * @version 1.0
 * @serial
 */
public class WaterDrink implements Serializable {
    /**
     * 喝水提醒构造函数
     */
    public WaterDrink() {

    }
    /**
     * 喝水记录的时间字符串
     */
    String record = "";
    /**
     * 本条喝水记录是当日的第几次
     */
    int ciShu = 0;
    /**
     * 本条喝水记录的日期
     */
    LocalDate date = LocalDate.now();
    /**
     * 喝水记录的构造函数
     *
     * @param time 时间字符串
     * @param idx 在当日的次数
     */
    public WaterDrink(String time, int idx) {
        record = time;
        ciShu = idx;
    }
    /**
     * 喝水记录字符串
     *
     * @return 格式为 今天第i次喝水的时间为： xx:xx:xx
     */
    @Override
    public String toString() {
        return "今天第" + ciShu + "次喝水的时间为： " + record;
    }
    /**
     * 获取喝水记录的日期
     *
     * @return 本条喝水记录的日期
     */
    public LocalDate getDate() {
        return date;
    }
}

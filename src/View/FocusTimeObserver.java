package View;

import Model.TodoItem;

/**
 * 观察者接口
 *
 * @author Keith
 * @version 1.0
 */
public interface FocusTimeObserver {
    /**
     * 观察者更新
     * @param focusTimeEntry 专注时间条目
     */
    void updateFocusTime(FocusTimeEntry focusTimeEntry);
}

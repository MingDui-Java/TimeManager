package Model;

import java.io.Serializable;
import java.time.LocalDate;

import Yang.ToDos;

/**
 * 待办事项
 *
 * @author Keith
 * @version 1.0
 * @serial
 */
public class TodoItem implements Serializable {

	/**
	 * 待办事项的标题
	 */
	private String title;
	// private String description;
	/**
	 * 待办事项的创建时间
	 */
	private LocalDate creationDay;
	/**
	 * 待办事项的剩余时间
	 */
	private int remainingTime;

	/**
	 * 与日历交互的实例
	 */
	private ToDos todo = null;

	/**
	 * 构造函数
	 *
	 * @param title 标题
	 * @param time 时间
	 */
	public TodoItem(String title, int time) {
		this.title = title;
		// this.description = description;
		this.remainingTime = time * 60;
		creationDay = LocalDate.now();
	}

	/**
	 * 构造函数
	 *
	 * @param title 标题
	 * @param time 时间
	 * @param todo 日历的todo
	 */
	public TodoItem(String title, int time, ToDos todo) {
		this.title = title;
		// this.description = description;
		this.remainingTime = time * 60;
		creationDay = LocalDate.now();
		this.todo = todo;
	}

	/**
	 * 返回标题
	 *
	 * @return 标题
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 返回创建时间
	 *
	 * @return 创建时间
	 */
	public LocalDate getCreationDay() {
		return creationDay;
	}

	/**
	 * 返回剩余专注时间
	 *
	 * @return 剩余专注时间
	 */
	public int getRemainingTime() {
		return remainingTime;
	}

	/**
	 * 设置剩余专注时间
	 *
	 * @param remainingTime 剩余专注时间
	 */
	public void setRemainingTime(int remainingTime) {
		this.remainingTime = remainingTime;
	}

	/*
	 * public String getDescription() { return description; }
	 */

	/**
	 * 在TaskPanel渲染的文字
	 *
	 * @return 在TaskPanel渲染的文字
	 */
	@Override
	public String toString() {
		return title + " - " + remainingTime / 60 + "minutes";
	}

	/**
	 * 返回ToDos
	 *
	 * @return 用于与日历交互
	 */
	public ToDos getTodo() {
		return todo;
	}
}

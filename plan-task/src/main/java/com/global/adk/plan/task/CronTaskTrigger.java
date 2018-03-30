/**
 * www.global.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */

package com.global.adk.plan.task;

import com.global.adk.common.exception.PlanTaskException;

import java.sql.Timestamp;
import java.util.*;

/**
 * 基于cron表达式的实现(非标准)不支持day－of－weak（周字段），L（当月最后一天），W（最接近指定值得工作日），＃（第几个周几）等。 例如：
 * <li>"0 0 *  * * *" = 每天、每小时0分0秒时执行。</li> <li>"* 10  * * * *" = 每隔10分钟执行一次。</li>
 * <li>"0 0 8-10  * * *" = 8，9，10点整执行</li> <li>"0 0/30 8-10  * *" = 每天8:00,8:30,
 * 9:00, 9:30 、10 执行.</li> <li>"0 0 0 25 12 ＊" = 每年12月25日 00:00:00执行</li> <li>
 * "*" 表示所有值；</li> <li>"?" 表示未说明的值，即不关心它为何值；</li> <li>"-" 表示一个指定的范围；</li> <li>
 * "," 表示附加一个可能值；</li> <li>"/" 符号前表示开始时间，符号后表示每次递增的值</li>
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0 2014-1-6
 * @version 1.0.0
 * @see
 */
public class CronTaskTrigger implements PlanTaskTrigger {
	
	private final BitSet seconds = new BitSet(60);
	
	private final BitSet minutes = new BitSet(60);
	
	private final BitSet hours = new BitSet(24);
	
	private final BitSet daysOfMonth = new BitSet(32);
	
	private final BitSet months = new BitSet(13);
	
	private final BitSet years = new BitSet(9999);
	
	private final String expression;
	
	private final String taskName;
	
	public CronTaskTrigger(String expression, String taskName) {
		
		this.expression = expression;
		
		this.taskName = taskName;
		
		String[] cronFields = expression.split(" ");
		
		if (cronFields.length != 6) {
			throw new PlanTaskException(String.format("计划任务%s非法的cron表达式:%s", this.taskName, this.expression));
		}
		
		initializer(seconds, cronFields[0], 0, 59, "seconds");
		initializer(minutes, cronFields[1], 0, 59, "minutes");
		initializer(hours, cronFields[2], 0, 23, "hours");
		initializer(daysOfMonth, cronFields[3], 1, 31, "daysOfMonth");
		daysOfMonth.set(0, false);
		initializer(months, cronFields[4], 1, 12, "months");
		initializer(years, cronFields[5], 1982, 9999, "years");
	}
	
	private void initializer(BitSet bitset, String value, int min, int max, String field) {
		
		if (value.equals("*") || value.equals("?")) {
			//-  *  ?
			for (int x = min, y = max + 1; x < y; x++) {
				bitset.set(caclulateValue(field, x), true);
			}
			return;
		}
		
		if (value.matches("[1-9]?[0-9]{1,3}")) {
			// number
			int number = Integer.parseInt(value);
			
			if (!(number >= min && number <= max)) {
				throw new PlanTaskException(String.format("计划任务%s非法的cron表达式:%s，表达式约束：%s,错误域：%s值：%s", this.taskName,
					this.expression, "(number >= min && number <= max)", field, value));
			}
			
			bitset.set(caclulateValue(field, number), true);
			
		} else if (value.matches("[1-9]?[0-9]{1,3}/[1-9]?[0-9]{1,3}")) {
			int subIndex = value.indexOf('/');
			int pre = Integer.parseInt(value.substring(0, subIndex));
			int suf = Integer.parseInt(value.substring(subIndex + 1));
			
			if (!(pre >= min && pre <= max) || !(suf >= min && suf <= max)) {
				throw new PlanTaskException(String.format("计划任务%s非法的cron表达式:%s，表达式约束：%s,错误域：%s值：%s", this.taskName,
					this.expression, "(pre >= min && pre <= max) || !(suf >= min && suf <= max)", field, value));
			}
			
			bitset.set(caclulateValue(field, pre), true);
			while ((pre += suf) < max) {
				bitset.set(caclulateValue(field, pre), true);
			}
		} else if (value.matches("[1-9]?[0-9]{1,3}-[1-9]?[0-9]{1,3}")) {
			int subIndex = value.indexOf('-');
			int pre = Integer.parseInt(value.substring(0, subIndex));
			int suf = Integer.parseInt(value.substring(subIndex + 1));
			
			if (!(pre >= min && pre <= max) || !(suf >= min && suf <= max)) {
				throw new PlanTaskException(String.format("计划任务%s非法的cron表达式:%s，表达式约束：%s,错误域：%s值：%s", this.taskName,
					this.expression, "(pre >= min && pre <= max) || !(suf >= min && suf <= max)", field, value));
			}
			
			do {
				bitset.set(caclulateValue(field, pre), true);
			} while (caclulateValue(field, ++pre) <= suf);
			
		} else if (value.matches("[1-9]?[0-9]{1,3}(,[1-9]?[0-9]{1,3})+")) {
			String[] elements = value.split(",");
			for (int i = 0, j = elements.length; i < j; i++) {
				int v = Integer.parseInt(elements[i]);
				if (!(v >= min && v <= max)) {
					throw new PlanTaskException(String.format("计划任务%s非法的cron表达式:%s，表达式约束：%s,错误域：%s值：%s", this.taskName,
						this.expression, "[0-9]{1,2}(,[0-9]{1,2})+", field, value));
				}
				bitset.set(caclulateValue(field, v), true);
			}
			
		} else {
			throw new PlanTaskException(String.format("计划任务%s非法的cron表达式:%s，错误域：%s值：%s", this.taskName, this.expression,
				field, value));
		}
		
	}
	
	private int caclulateValue(String field, int value) {
		
		switch (field) {
			case "months":
				return value - 1;
			default:
				return value;
		}
		//        return value;
	}
	
	//- spring CronSequenceGenerator
	@Override
	public Timestamp nextDate(Date now) {
		
		Calendar nowDate = Calendar.getInstance();
		nowDate.setTime(now);
		nowDate.set(Calendar.MILLISECOND, 0);
		Date nextDate = doNext(nowDate, nowDate.get(Calendar.YEAR)) ? nowDate.getTime() : null;
		return nextDate == null ? null : new Timestamp(nextDate.getTime());
		
	}
	
	private boolean doNext(Calendar calendar, int dot) {
		
		List<Integer> resets = new ArrayList<Integer>();
		
		int second = calendar.get(Calendar.SECOND);
		List<Integer> emptyList = Collections.emptyList();
		int updateSecond = findNext(this.seconds, second, calendar, Calendar.SECOND, Calendar.MINUTE, emptyList);
		if (second == updateSecond) {
			resets.add(Calendar.SECOND);
		}
		
		int minute = calendar.get(Calendar.MINUTE);
		int updateMinute = findNext(this.minutes, minute, calendar, Calendar.MINUTE, Calendar.HOUR_OF_DAY, resets);
		if (minute == updateMinute) {
			resets.add(Calendar.MINUTE);
		} else {
			doNext(calendar, dot);
		}
		
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int updateHour = findNext(this.hours, hour, calendar, Calendar.HOUR_OF_DAY, Calendar.DAY_OF_MONTH, resets);
		if (hour == updateHour) {
			resets.add(Calendar.HOUR_OF_DAY);
		} else {
			doNext(calendar, dot);
		}
		
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		int updateDayOfMonth = findNext(this.daysOfMonth, dayOfMonth, calendar, Calendar.DAY_OF_MONTH, Calendar.MONTH,
			resets);
		if (dayOfMonth == updateDayOfMonth) {
			resets.add(Calendar.DAY_OF_MONTH);
		} else {
			doNext(calendar, dot);
		}
		
		int month = calendar.get(Calendar.MONTH);
		int updateMonth = findNext(this.months, month, calendar, Calendar.MONTH, Calendar.YEAR, resets);
		if (month == updateMonth) {
			resets.add(Calendar.YEAR);
		} else {
			doNext(calendar, dot);
		}
		//循环匹配即可。
		int year = calendar.get(Calendar.YEAR);
		int nextYear = years.nextSetBit(year);
		if (nextYear == -1) {
			return Boolean.FALSE;
		}
		calendar.set(Calendar.YEAR, nextYear);
		return Boolean.TRUE;
	}
	
	private int findNext(BitSet bits, int value, Calendar calendar, int field, int nextField, List<Integer> lowerOrders) {
		
		int nextValue = bits.nextSetBit(value);
		// roll over if needed
		if (nextValue == -1) {
			calendar.add(nextField, 1);
			reset(calendar, Arrays.asList(field));
			nextValue = bits.nextSetBit(0);
		}
		if (nextValue != value) {
			calendar.set(field, nextValue);
			reset(calendar, lowerOrders);
		}
		return nextValue;
	}
	
	/**
	 * Reset the calendar setting all the fields provided to zero.
	 */
	private void reset(Calendar calendar, List<Integer> fields) {
		
		for (int field : fields) {
			calendar.set(field, field == Calendar.DAY_OF_MONTH ? 1 : 0);
		}
	}
	
	@Override
	public String getTaskName() {
		
		return taskName;
	}
}

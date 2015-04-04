package com.howbuy.lib.interfaces;

/**
 * @author rexy  840094530@qq.com 
 * @date 2014-2-28 下午1:37:39
 */
public interface ITimerListener {
	public static final int TIMER_START = 0;
	public static final int TIMER_SCHEDULE = 1;
	public static final int TIMER_END = 2;
	public void onTimerRun(int which,int timerState,boolean hasTask,int timesOrSize);
}

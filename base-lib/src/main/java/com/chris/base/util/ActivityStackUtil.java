package com.chris.base.util;

import android.app.ActivityManager;
import android.content.Context;

import java.util.Stack;

/**
 * 类描述 ：Activity栈管理
 * Author: Christain
 * Date  : 16/3/18
 */
public class ActivityStackUtil {

	private volatile Stack<ActivityInter> activities = new Stack<ActivityInter>();

//	//双重校验锁单例写法
//	private static volatile ActivityStackUtil instance;

	//静态内部类单例写法
	private static class ActivityStackUtilLoader {
		private static final ActivityStackUtil INSTANCE = new ActivityStackUtil();
	}

	private ActivityStackUtil() {

	}

//	/**
//	 * 双重校验锁单例写法
//	 * @return
//     */
//	public static ActivityStackUtil getInstance() {
//		if (instance == null) {
//			synchronized (ActivityStackUtil.class) {
//				if (instance == null) {
//					instance = new ActivityStackUtil();
//				}
//			}
//		}
//		return instance;
//	}

	/**
	 * 静态内部类单例写法
     */
	public static ActivityStackUtil getInstance() {
		return ActivityStackUtilLoader.INSTANCE;
	}

	public void addActivity(ActivityInter activity) {
		if (activity != null) {
			activities.add(activity);
		}
	}

	public void removeActivity(ActivityInter activity) {
		if (activity != null) {
			activities.remove(activity);
		}
	}

	public void finishOther() {
		ActivityInter activity = getCurrentActivity();
		while (!activities.isEmpty()){
			ActivityInter act = activities.pop();
			if (act != activity && act != null){
				act.finishActivity();
			}
		}
		addActivity(activity);
	}

	/**
	 * 先把activity从栈中移除，然后调用Activityr的finish方法
     */
	public void finishActivity(ActivityInter activity) {
		removeActivity(activity);
		activity.finishActivity();
	}

	/**
	 * 获取当前activity对象
     */
	public ActivityInter getCurrentActivity() {
		if (activities.size() > 0){
			return activities.lastElement();
		}
		else {
			return null;
		}
	}

	/**
	 * 清空activity栈
	 */
	public void clearStack(){
		while (!activities.isEmpty()){
			ActivityInter act = activities.pop();
			if (act != null){
				act.finishActivity();
			}
		}
	}

	/**
	 * 退出程序
	 * @param context
     */
	public void exit(Context context) {
		try {
			clearStack();
			ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			manager.killBackgroundProcesses(context.getPackageName());
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public interface ActivityInter {
		void finishActivity();
	}
}

package com.chris.base.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.chris.base.SuperApp;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * ===============================
 * 描    述：SharePrefrence工具类
 * 作    者：Christain
 * 创建日期：2018/7/19 下午2:21
 * ===============================
 */
public class AppDataPrefrenceUtil {

    public static final String FILE_NAME = "APP_DATA_BASE";

    private final SharedPreferences mSPrefs;

    private static AppDataPrefrenceUtil INSTANCE;

    private AppDataPrefrenceUtil() {
        mSPrefs = SuperApp.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    public static AppDataPrefrenceUtil getInstance() {
        if (INSTANCE == null) {
            synchronized (AppDataPrefrenceUtil.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AppDataPrefrenceUtil();
                }
            }
        }
        return INSTANCE;
    }

    public synchronized void put(String key, Object object) {
        SharedPreferences.Editor editor = mSPrefs.edit();
        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }
        SharedPreferencesCompat.apply(editor);
    }

    public String getString(String key) {
        return mSPrefs.getString(key, "");
    }

    public String getString(String key, String defaultObject) {
        return mSPrefs.getString(key, defaultObject);
    }

    public int getInt(String key) {
        return mSPrefs.getInt(key, 0);
    }

    public int getInt(String key, int defaultObject) {
        return mSPrefs.getInt(key, defaultObject);
    }

    public boolean getBoolean(String key) {
        return mSPrefs.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultObject) {
        return mSPrefs.getBoolean(key, defaultObject);
    }

    public float getFloat(String key) {
        return mSPrefs.getFloat(key, 0);
    }

    public float getFloat(String key, float defaultObject) {
        return mSPrefs.getFloat(key, defaultObject);
    }

    public long getLong(String key) {
        return mSPrefs.getLong(key, 0);
    }

    public long getLong(String key, long defaultObject) {
        return mSPrefs.getLong(key, defaultObject);
    }

    public Object get(String key, Object defaultObject) {
        if (defaultObject instanceof String) {
            return mSPrefs.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return mSPrefs.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return mSPrefs.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return mSPrefs.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return mSPrefs.getLong(key, (Long) defaultObject);
        }
        return null;
    }

    /**
     * 移除某个key值已经对应的值
     */
    public synchronized void remove(String key) {
        SharedPreferences.Editor editor = mSPrefs.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     */
    public synchronized void clear() {
        SharedPreferences.Editor editor = mSPrefs.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     */
    public boolean contains(String key) {
        return mSPrefs.contains(key);
    }

    /**
     * 返回所有的键值对
     */
    public Map<String, ?> getAll() {
        return mSPrefs.getAll();
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
            editor.commit();
        }
    }
}
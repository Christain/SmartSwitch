package com.chris.base;

import com.chris.base.util.PhoneUtil;

/**
 * ===============================
 * 描    述：常量
 * 作    者：Christain
 * 创建日期：2018/7/18 下午10:26
 * ===============================
 */
public class Constants {

    //图片缓存根目录
    public static final String PATH_ROOT_DIR = PhoneUtil.getSdCardRootPath() + "/SmartSwitch/";

    //图片缓存地址
    public static final String PATH_IMAGE_CACHE_DIR = PATH_ROOT_DIR + "cache/";


    //=====================SharePrefrence======================//

    public static final String SP_VERSION_CODE = "SP_VERSION_CODE";

}

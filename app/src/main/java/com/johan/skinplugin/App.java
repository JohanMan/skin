package com.johan.skinplugin;

import android.app.Application;

import com.johan.skin.library.SkinPlugin;

/**
 * @author : FengYiHuan
 * @Description : SkinPlugin
 * @Company : 深圳市爱聊科技有限公司
 * @vesion : v
 * @Create Date : 2022/8/2 09:21
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SkinPlugin.getInstance().init(this);
    }

}

package com.johan.skin.library;

import android.app.Activity;

/**
 * @author : FengYiHuan
 * @Description : SkinPlugin
 * @Company : Johan
 * @vesion : v
 * @Create Date : 2022/8/1 19:50
 */
public interface SkinObservable {

    void register(Activity activity, SkinObserver observer);

    void unregister(Activity activity);

}

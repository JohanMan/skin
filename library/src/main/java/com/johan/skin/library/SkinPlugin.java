package com.johan.skin.library;

import android.app.Activity;
import android.app.Application;
import android.view.LayoutInflater;

import java.lang.reflect.Field;

/**
 * @author : FengYiHuan
 * @Description : SkinPlugin
 * @Company : Johan
 * @vesion : v
 * @Create Date : 2022/8/1 09:04
 */
public class SkinPlugin {

    private static class SkinPluginHolder {
        private static final SkinPlugin INSTANCE = new SkinPlugin();
    }

    public static SkinPlugin getInstance() {
        return SkinPluginHolder.INSTANCE;
    }

    private boolean hasInit;
    private boolean enable = true;
    private SkinResource skinResource;

    public void init(Application application) {
        hasInit = true;
        skinResource = new SkinResource(application);
        application.registerActivityLifecycleCallbacks(new SkinLifecycleCallback());
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public void install(Activity activity) {
        if (!enable) {
            return;
        }
        if (!hasInit) {
            init(activity.getApplication());
        }
        hook(activity);
    }

    public void uninstall(Activity activity) {
        if (!enable) {
            return;
        }
        if (!hasInit) {
            init(activity.getApplication());
        }
        unhook(activity);
    }

    public void loadSkin(String skinPath) {
        if (!enable) {
            return;
        }
        if (!hasInit) {
            return;
        }
        skinResource.loadSkin(skinPath);
    }

    public void updateStatusBar(Activity activity) {
        if (!enable) {
            return;
        }
        if (!hasInit) {
            return;
        }
        SkinUtil.updateStatusBarColor(activity, skinResource);
    }

    private void hook(Activity activity) {
        if (activity == null) {
            return;
        }
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        SkinFactory newFactory = new SkinFactory(activity, skinResource);
        skinResource.register(activity, newFactory);
        if (!hookFactorySet(layoutInflater, newFactory)) {
            hookFactory(layoutInflater, newFactory);
        }
    }

    private boolean hookFactorySet(LayoutInflater layoutInflater, LayoutInflater.Factory2 newFactory) {
        try {
            Field mFactorySet = LayoutInflater.class.getDeclaredField("mFactorySet");
            mFactorySet.setAccessible(true);
            mFactorySet.set(layoutInflater, false);
            layoutInflater.setFactory2(newFactory);
            return true;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean hookFactory(LayoutInflater layoutInflater, LayoutInflater.Factory2 newFactory) {
        try {
            Field mFactory2 = LayoutInflater.class.getDeclaredField("mFactory2");
            mFactory2.setAccessible(true);
            mFactory2.set(layoutInflater, newFactory);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void unhook(Activity activity) {
        skinResource.unregister(activity);
    }

}

package com.johan.skin.library;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : FengYiHuan
 * @Description : SkinPlugin
 * @Company : Johan
 * @vesion : v
 * @Create Date : 2022/8/1 17:21
 */
public class SkinResource implements SkinObservable {

    private Map<Class, SkinObserver> observerMap = new HashMap<>();

    private Application application;

    private boolean useDefaultSkin;
    private String skinPackageName;

    private Resources appResource;
    private Resources skinResource;

    public SkinResource(Application application) {
        this.application = application;
        // 得到当前APP的Resources
        this.appResource = application.getResources();
        this.useDefaultSkin = true;
    }

    @Override
    public void register(Activity activity, SkinObserver observer) {
        if (observerMap.containsKey(activity.getClass())) {
            return;
        }
        observerMap.put(activity.getClass(), observer);
    }

    @Override
    public void unregister(Activity activity) {
        observerMap.remove(activity.getClass());
    }

    public void loadSkin(String skinPath) {
        useDefaultSkin = true;
        // 如果传递进来的皮肤文件路径是null，则表示使用默认的皮肤
        if (!TextUtils.isEmpty(skinPath)) {
            // 传递进来的有皮肤包的文件路径则加载
            try {
                // 皮肤包文件不存在
                if (!new File(skinPath).exists()) {
                    return;
                }
                // 反射创建AssetManager
                AssetManager assetManager = AssetManager.class.newInstance();
                // 通过反射得到方法：public int addAssetPath(String path)方法
                Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
                // 设置当前方法可以被访问
                addAssetPath.setAccessible(true);
                // 调用该方法，传入皮肤包文件路径
                addAssetPath.invoke(assetManager, skinPath);
                // 根据当前的显示与配置(横竖屏、语言等)创建皮肤包的Resources
                skinResource = new Resources(assetManager, appResource.getDisplayMetrics(), appResource.getConfiguration());
                // 获取外部皮肤包的包名，首先得到PackageManager对象
                PackageManager packageManager = application.getPackageManager();
                // 通过getPackageArchiveInfo得到外部皮肤包文件的包信息
                PackageInfo info = packageManager.getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES);
                if (info == null) {
                    return;
                }
                // 得到皮肤包包名
                skinPackageName = info.packageName;
                useDefaultSkin = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 被观察者通知所有观察者
        notifyObservers();
    }

    private void notifyObservers() {
        for (SkinObserver observer : observerMap.values()) {
            observer.notifyUpdate();
        }
    }

    public int getIdentifier(int resId) {
        if (useDefaultSkin) {
            return resId;
        }
        // 在皮肤包中不一定就是 当前程序的 id
        // 获取对应id 在当前的名称 colorPrimary
        String resName = appResource.getResourceEntryName(resId);
        String resType = appResource.getResourceTypeName(resId);
        int skinId = skinResource.getIdentifier(resName, resType, skinPackageName);
        return skinId;
    }

    public int getColor(int resId) {
        if (useDefaultSkin) {
            return appResource.getColor(resId);
        }
        int skinId = getIdentifier(resId);
        if (skinId == 0) {
            return appResource.getColor(resId);
        }
        return skinResource.getColor(skinId);
    }

    public ColorStateList getColorStateList(int resId) {
        if (useDefaultSkin) {
            return appResource.getColorStateList(resId);
        }
        int skinId = getIdentifier(resId);
        if (skinId == 0) {
            return appResource.getColorStateList(resId);
        }
        return skinResource.getColorStateList(skinId);
    }

    public Drawable getDrawable(int resId) {
        if (useDefaultSkin) {
            return appResource.getDrawable(resId);
        }
        int skinId = getIdentifier(resId);
        if (skinId == 0) {
            return appResource.getDrawable(resId);
        }
        return skinResource.getDrawable(skinId);
    }

    public Object getBackground(int resId) {
        String resourceTypeName = appResource.getResourceTypeName(resId);
        if (resourceTypeName.equals("color")) {
            return getColor(resId);
        } else {
            return getDrawable(resId);
        }
    }

}

package com.johan.skin.library;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.reflect.Constructor;
import java.util.HashMap;

/**
 * @author : FengYiHuan
 * @Description : SkinPlugin
 * @Company : Johan
 * @vesion : v
 * @Create Date : 2022/8/1 09:06
 */
public class SkinFactory implements LayoutInflater.Factory2, SkinObserver {

    private static final String[] mClassPrefixList = {
            "android.widget.",
            "android.webkit.",
            "android.app.",
            "android.view."
    };

    private static final Class<?>[] mConstructorSignature = new Class[]{Context.class, AttributeSet.class};
    private static final HashMap<String, Constructor<? extends View>> mConstructorMap = new HashMap<>();

    private Activity activity;
    private SkinResource skinResource;
    private SkinAttributeCollector attributeCollector;

    public SkinFactory(Activity activity, SkinResource skinResource) {
        this.activity = activity;
        this.skinResource = skinResource;
        this.attributeCollector = new SkinAttributeCollector();
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        // 先解析简称的View
        View view = createSimpleView(name, context, attrs);
        if (null == view) {
            // 再解析全称的View
            view = createView(name, context, attrs);
        }
        // 如果找到View 获取属性
        if (null != view) {
            SkinView skinView = attributeCollector.collectSkinView(view, attrs);
            if (skinView != null) {
                skinView.update(skinResource);
            }
        }
        return view;
    }

    private View createSimpleView(String name, Context context, AttributeSet attrs) {
        if (-1 != name.indexOf('.')) {
            return null;
        }
        for (int i = 0; i < mClassPrefixList.length; i++) {
            View view = createView(mClassPrefixList[i] + name, context, attrs);
            if (view != null) {
                return view;
            }
        }
        return null;
    }

    private View createView(String name, Context context, AttributeSet attrs) {
        Constructor<? extends View> constructor = findConstructor(context, name);
        try {
            return constructor.newInstance(context, attrs);
        } catch (Exception ignore) {
        }
        return null;
    }

    private Constructor<? extends View> findConstructor(Context context, String name) {
        Constructor<? extends View> constructor = mConstructorMap.get(name);
        if (constructor == null) {
            try {
                Class<? extends View> clazz = context.getClassLoader().loadClass
                        (name).asSubclass(View.class);
                constructor = clazz.getConstructor(mConstructorSignature);
                constructor.setAccessible(true);
                mConstructorMap.put(name, constructor);
            } catch (Exception ignore) {
            }
        }
        return constructor;
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return null;
    }

    @Override
    public void notifyUpdate() {
        SkinUtil.updateStatusBarColor(activity, skinResource);
        if (attributeCollector != null) {
            attributeCollector.notifyUpdate(skinResource);
        }
    }

}

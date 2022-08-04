package com.johan.skin.library;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;

import java.util.HashSet;
import java.util.Set;

/**
 * @author : FengYiHuan
 * @Description : SkinPlugin
 * @Company : Johan
 * @vesion : v
 * @Create Date : 2022/8/1 16:51
 */
public class SkinUtil {

    private static final Set<String> attributeSet = new HashSet<>();

    // 兼容，如果状态栏的色值没有拿到，则使用系统默认的
    private static int[] defaultColor = {R.attr.colorPrimaryDark};
    // 状态栏和navigationBar
    private static int[] barColor = {android.R.attr.statusBarColor, android.R.attr.navigationBarColor};

    static {
        attributeSet.add("textColor");
        attributeSet.add("background");
        attributeSet.add("src");
        attributeSet.add("drawableLeft");
        attributeSet.add("drawableRight");
        attributeSet.add("drawableTop");
        attributeSet.add("drawableBottom");
    }

    public static boolean canHandle(String attributeName) {
        return attributeSet.contains(attributeName);
    }

    public static void updateStatusBarColor(Activity activity, SkinResource resource) {
        // 5.0以上才能修改
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int[] resbarIds = getResId(activity, barColor);
            // 如果有该值，则可以替换状态栏颜色
            if (resbarIds[0] != 0) {
                activity.getWindow().setStatusBarColor(resource.getColor(resbarIds[0]));
            } else {
                // 没有值，则使用兼容色值colorPrimaryDark
                int resbarId = getResId(activity, defaultColor)[0];
                if (resbarId != 0) {
                    activity.getWindow().setStatusBarColor(resource.getColor(resbarId));
                }
            }
            // 底部NavigationBar如果存在则也要改变色值
            if (resbarIds[1] != 0) {
                activity.getWindow().setNavigationBarColor(resource.getColor(resbarIds[1]));
            }
        }
    }

    public static int[] getResId(Context context, int[] attrs) {
        int[] ints = new int[attrs.length];
        //获得样式属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        for (int i = 0; i < typedArray.length(); i++) {
            ints[i] = typedArray.getResourceId(i, 0);
        }
        typedArray.recycle();
        return ints;
    }

}

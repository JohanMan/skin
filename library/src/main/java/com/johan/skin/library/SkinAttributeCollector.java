package com.johan.skin.library;

import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : FengYiHuan
 * @Description : SkinPlugin
 * @Company : Johan
 * @vesion : v
 * @Create Date : 2022/8/1 13:42
 */
public class SkinAttributeCollector {

    private List<SkinView> skinViews = new ArrayList<>();

    /**
     * 收集可处理的View
     * @param view 目标View
     * @param attributeSet 属性参数
     */
    public SkinView collectSkinView(View view, AttributeSet attributeSet) {
        List<SkinAttribute> skinAttributes = new ArrayList<>();
        // 先筛选一遍，需要修改属性的才往下走
        for (int i = 0; i < attributeSet.getAttributeCount(); i++) {
            // 获取属性名字
            String attributeName = attributeSet.getAttributeName(i);
            // 如果当前属性名字是需要修改的属性则去处理
            if (SkinUtil.canHandle(attributeName)) {
                // 拿到属性值，@2130968664
                String attributeValue = attributeSet.getAttributeValue(i);
                // 写死的色值，暂时不修改
                if (attributeValue.startsWith("#")) {
                    continue;
                }
                int resId;
                // ?开头的是系统参数，如下修改
                if (attributeValue.startsWith("?")) {
                    // 拿到去掉?后的值。
                    // 强转成int，系统编译后的值为int型，即R文件中的id，例如：?123456
                    // 系统的资源id下只有一个标签，类似于resource标签下的style标签，但是style下只有一个item标签
                    // 所以只拿第一个attrid
                    int attrId = Integer.parseInt(attributeValue.substring(1));
                    // 获得资源id
                    resId = SkinUtil.getResId(view.getContext(), new int[]{attrId})[0];
                } else {
                    // 其他正常的标签则直接拿到@color/black中在R文件中的@123456
                    // 去掉@后的值则可以直接通过setColor(int resId);传入
                    resId = Integer.parseInt(attributeValue.substring(1));
                }
                if (resId != 0) {
                    // 保存属性名字和对应的id用于换肤使用
                    Log.e("Test", "Collect SkinAttribute ----------- " + attributeName + " - " + resId);
                    SkinAttribute skinPain = new SkinAttribute(attributeName, resId);
                    skinAttributes.add(skinPain);
                }
            }
        }
        // 如果当前view检查出来了需要替换的资源id，则保存起来
        // 上面的循环已经循环出了当前view中的所有需要换肤的标签和redId
        if (!skinAttributes.isEmpty()) {
            SkinView skinView = new SkinView(view, skinAttributes);
            skinViews.add(skinView);
            return skinView;
        }
        return null;
    }

    /**
     * 通知更新
     */
    public void notifyUpdate(SkinResource resource) {
        for (SkinView skinView : skinViews) {
            skinView.update(resource);
        }
    }

}

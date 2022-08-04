package com.johan.skin.library;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.view.ViewCompat;

import java.util.List;

/**
 * @author : FengYiHuan
 * @Description : SkinPlugin
 * @Company : Johan
 * @vesion : v
 * @Create Date : 2022/8/1 11:57
 */
public class SkinView {

    private View view;
    private List<SkinAttribute> skinAttributes;

    public SkinView(View view, List<SkinAttribute> skinAttributes) {
        this.view = view;
        this.skinAttributes = skinAttributes;
    }

    public void update(SkinResource resource) {
        for (SkinAttribute skinAttribute : skinAttributes) {
            Log.e("Test", "update skinAttributes ----------- " + view + " - " + skinAttribute.getName());
            Drawable left = null, right = null, top = null, bottom = null;
            switch (skinAttribute.getName()) {
                case "background":
                    Object background = resource.getBackground(skinAttribute.getResId());
                    if (background instanceof Integer) {
                        view.setBackgroundColor((int) background);
                    } else {
                        ViewCompat.setBackground(view, (Drawable) background);
                    }
                    break;
                case "src":
                    background = resource.getBackground(skinAttribute.getResId());
                    if (view instanceof ImageView) {
                        ImageView imageView = ((ImageView) view);
                        if (background instanceof Integer) {
                            imageView.setImageDrawable(new ColorDrawable((Integer) background));
                        } else if (background instanceof Drawable) {
                            imageView.setImageDrawable((Drawable) background);
                        }
                    }
                    break;
                case "textColor":
                    if (view instanceof TextView) {
                        ((TextView) view).setTextColor(resource.getColorStateList(skinAttribute.getResId()));
                    }
                    break;
                case "drawableLeft":
                    left = resource.getDrawable(skinAttribute.getResId());
                    break;
                case "drawableTop":
                    top = resource.getDrawable(skinAttribute.getResId());
                    break;
                case "drawableRight":
                    right = resource.getDrawable(skinAttribute.getResId());
                    break;
                case "drawableBottom":
                    bottom = resource.getDrawable(skinAttribute.getResId());
                    break;
                default:
                    break;
            }
            if (left != null || top != null || right != null || bottom != null) {
                if (view instanceof TextView) {
                    ((TextView) view).setCompoundDrawables(left, top, right, bottom);
                }
            }
        }
    }

}

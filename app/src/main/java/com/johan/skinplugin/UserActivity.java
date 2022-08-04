package com.johan.skinplugin;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.johan.skin.library.SkinPlugin;

/**
 * @author : FengYiHuan
 * @Description : SkinPlugin
 * @Company : Johan
 * @vesion : v
 * @Create Date : 2022/8/2 14:31
 */
public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        findViewById(R.id.load_skin_button).setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(UserActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(UserActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                permissionPass();
            }
        });
        findViewById(R.id.unload_skin_button).setOnClickListener(v -> {
            SkinPlugin.getInstance().loadSkin("");
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissions[0] == Manifest.permission.READ_EXTERNAL_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            permissionPass();
        }
    }

    private void permissionPass() {
        SkinPlugin.getInstance().loadSkin(Environment.getExternalStorageDirectory() + "/skin.apk");
    }

}

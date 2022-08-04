package com.johan.skinplugin;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.johan.skin.library.SkinPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : FengYiHuan
 * @Description : SkinPlugin
 * @Company : 深圳市爱聊科技有限公司
 * @vesion : v
 * @Create Date : 2022/8/1 09:39
 */
public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("Test", "onCreate ----------- ");
        setContentView(R.layout.activity_home);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        List<String> dataList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            dataList.add("Number is ----> " + (i + 1));
        }
        HomeAdapter homeAdapter = new HomeAdapter(this, dataList);
        recyclerView.setAdapter(homeAdapter);
        findViewById(R.id.load_skin_button).setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                permissionPass();
            }
        });
        findViewById(R.id.unload_skin_button).setOnClickListener(v -> {
            SkinPlugin.getInstance().loadSkin("");
        });
        findViewById(R.id.go_user_button).setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, UserActivity.class)));
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

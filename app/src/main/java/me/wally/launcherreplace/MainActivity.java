package me.wally.launcherreplace;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button changeLauncherBtn = findViewById(R.id.changeLauncherBtn);
        changeLauncherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLauncher("me.wally.launcherreplace.MainActivityAlias");
                Toast.makeText(MainActivity.this.getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        Button resetBtn = findViewById(R.id.resetBtn);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLauncher("me.wally.launcherreplace.MainActivity");
                Toast.makeText(MainActivity.this.getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void setLauncher(String newLauncher) {
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(getComponentName(),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        pm.setComponentEnabledSetting(new ComponentName(MainActivity.this, newLauncher), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        //重启Launcher
        rebootLauncher();
    }

    private void rebootLauncher() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        PackageManager pm = getPackageManager();
        List<ResolveInfo> resolves = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo res : resolves) {
            if (res.activityInfo != null) {
                ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                am.killBackgroundProcesses(res.activityInfo.packageName);
            }
        }
    }
}

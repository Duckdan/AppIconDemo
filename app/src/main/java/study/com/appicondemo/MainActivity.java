package study.com.appicondemo;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import me.leolin.shortcutbadger.ShortcutBadger;

public class MainActivity extends AppCompatActivity {
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = (ImageView) findViewById(R.id.iv);

        TextView tvFestival = (TextView) findViewById(R.id.tv_festival);
        tvFestival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchIcon(1);
                iv.setBackgroundResource(R.mipmap.icon_festival);
                int badgeCount = 1;
                //用于生成APP又上角的图标，但是在我手机（华为荣耀青春版，Android系统7.0）没有效果
                //不过在另一款华为手机（华为meta7 Android系统6.0）上亲测有效
                //此处使用的是第三插件ShortcutBadger,地址是https://github.com/leolin310148/ShortcutBadger
                boolean applyCount = ShortcutBadger.applyCount(MainActivity.this, badgeCount);//for 1.1.4+
                Toast.makeText(MainActivity.this, "App图标-节日版" + applyCount, Toast.LENGTH_SHORT).show();
            }
        });

        TextView tvNormal = (TextView) findViewById(R.id.tv_normal);
        tvNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchIcon(2);
                iv.setBackgroundResource(R.mipmap.icon_normal);
                Toast.makeText(MainActivity.this, "App图标-普通版", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * @param useCode 1、为活动图标 2 为用普通图标 3、不启用判断
     */
    private void switchIcon(int useCode) {

        try {
            //要跟manifest的activity-alias 的name保持一致
            String icon_tag = "study.com.appicondemo.icon_tag";
            String icon_tag_1212 = "study.com.appicondemo.icon_tag_1212";

            if (useCode != 3) {

                PackageManager pm = getPackageManager();

                ComponentName normalComponentName = new ComponentName(
                        getBaseContext(),
                        icon_tag);
                //正常图标新状态，此处使用用来修改清单文件中activity-alias下的android:enable的值
                int normalNewState = useCode == 2 ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                        : PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
                //新状态跟当前状态不一样才执行
                if (pm.getComponentEnabledSetting(normalComponentName) != normalNewState) {
                    //PackageManager.DONT_KILL_APP表示执行此方法时不杀死当前的APP进程
                    pm.setComponentEnabledSetting(
                            normalComponentName,
                            normalNewState,
                            PackageManager.DONT_KILL_APP);
                }

                ComponentName actComponentName = new ComponentName(
                        getBaseContext(),
                        icon_tag_1212);
                //活动图标新状态
                int actNewState = useCode == 1 ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                        : PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
                //新状态跟当前状态不一样才执行
                if (pm.getComponentEnabledSetting(actComponentName) != actNewState) {
                    pm.setComponentEnabledSetting(
                            actComponentName,
                            actNewState,
                            PackageManager.DONT_KILL_APP);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

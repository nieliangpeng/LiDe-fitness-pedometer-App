package cn.bluemobi.dylan.step.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.io.IOException;

import cn.bluemobi.dylan.step.R;
import cn.bluemobi.dylan.step.step.UpdateUiCallBack;
import cn.bluemobi.dylan.step.step.service.StepService;
import cn.bluemobi.dylan.step.step.utils.SharedPreferencesUtils;
import cn.bluemobi.dylan.step.view.StepArcView;
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
//    private TextView tv_data;
    private StepArcView cc;
//    private TextView tv_set;
    private TextView tv_isSupport;
    private ImageView anim;
    private SharedPreferencesUtils sp;
    private ImageView more;
    private ImageView music;
    private MediaPlayer mediaPlayer=null;
    private void assignViews() {
//        tv_data = (TextView) findViewById(R.id.tv_data);
        cc = (StepArcView) findViewById(R.id.cc);
//        tv_set = (TextView) findViewById(R.id.tv_set);
        tv_isSupport = (TextView) findViewById(R.id.tv_isSupport);
        anim= (ImageView) findViewById(R.id.img);
        more= (ImageView) findViewById(R.id.more);
        music= (ImageView) findViewById(R.id.music);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.d("aaaaaa","aaaaaaaaaaaaaaaaaaaaaaaa");
        setContentView(R.layout.activity_main);
//        Log.d("aaaaaa","aaaaaaaaaaaaaaaaaaaaaaaa");
        assignViews();
//        Log.d("aaaaaa","bbbbbbbbbbbbbbbbbbbbbbbbbbb");
        initData();
//        Log.d("aaaaaa","cccccccccccccccccccccccccccc");
        addListener();
        AnimationDrawable loadingDrawable =(AnimationDrawable) anim.getDrawable();
        loadingDrawable.start();
    }


    private void addListener() {
//        tv_set.setOnClickListener(this);
//        tv_data.setOnClickListener(this);
        more.setOnClickListener(this);
        music.setOnClickListener(this);
    }

    private void initData() {
        sp = new SharedPreferencesUtils(this);
        //获取用户设置的计划锻炼步数
        String planWalk_QTY = (String) sp.getParam("planWalk_QTY", "7000");
        //设置当前步数为0
        cc.setCurrentCount(Integer.parseInt(planWalk_QTY), 0);
        tv_isSupport.setText("启动中，力德健身计步器...");
        setupService();
    }


    private boolean isBind = false;

    //开启计步服务
    private void setupService() {
        Intent intent = new Intent(this, StepService.class);
        isBind = bindService(intent, conn, Context.BIND_AUTO_CREATE);
        startService(intent);
    }


    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            StepService stepService = ((StepService.StepBinder) service).getService();
            //设置初始化数据
            String planWalk_QTY = (String) sp.getParam("planWalk_QTY", "7000");
            cc.setCurrentCount(Integer.parseInt(planWalk_QTY), stepService.getStepCount());
            //设置步数监听回调，更新UI
            stepService.registerCallback(new UpdateUiCallBack() {
                @Override
                public void updateUi(int stepCount) {
                    String planWalk_QTY = (String) sp.getParam("planWalk_QTY", "7000");
                    cc.setCurrentCount(Integer.parseInt(planWalk_QTY), stepCount);
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more:
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(), more);
                popupMenu.getMenuInflater().inflate(R.menu.menu_option, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // TODO
                        switch (item.getItemId()){
                            case R.id.tv_set:
                                startActivity(new Intent(MainActivity.this, SetPlanActivity.class));
                                break;
                            case R.id.tv_data:
                                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
                                break;
                        }
                        return false;
                    }
                });
                // 显示弹出菜单
                popupMenu.show();
                break;
            case R.id.music:
                //启动音乐
                mediaPlayer=new MediaPlayer();
                Uri uri = Uri.parse("http://sc1.111ttt.cn/2016/1/11/11/204111410014.mp3");
                try {
                    mediaPlayer.setDataSource(MainActivity.this,uri);
                } catch (IOException e) {
                    Log.i("11",e.getMessage());
                }
                try {
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.start();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isBind) {
            this.unbindService(conn);
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            //按键keyCode是否是返回键，如果是的话
            if(mediaPlayer!=null){
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer=null;
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

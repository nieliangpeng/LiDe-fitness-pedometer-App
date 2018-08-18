package cn.bluemobi.dylan.step.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import cn.bluemobi.dylan.step.R;
import cn.bluemobi.dylan.step.adapter.VpAdapter;

public class Main2Activity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private ViewPager vPager;
    private VpAdapter vpAdapter;
    private static  int[] imgs = {R.mipmap.a,R.mipmap.b,R.mipmap.c};
    private ArrayList<ImageView> imageViews;
    private ImageView[] dotViews;//小圆点
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Log.i("11111","可以111");
        vPager= (ViewPager) findViewById(R.id.guide_ViewPager);
        initImages();
        initDots();
        vPager.setOnPageChangeListener(this);
    }
    private void initImages(){
        //设置每一张图片都填充窗口
        ViewPager.LayoutParams mParams = new ViewPager.LayoutParams();
        imageViews = new ArrayList<ImageView>();
        for(int i=0; i<imgs.length; i++)
        {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);//设置布局
            iv.setImageResource(imgs[i]);//为ImageView添加图片资源
            iv.setScaleType(ImageView.ScaleType.FIT_XY);//这里也是一个图片的适配
            imageViews.add(iv);
            if (i == imgs.length -1 ){
                //为最后一张图片添加点击事件
                iv.setOnTouchListener(new View.OnTouchListener(){
                    @Override
                    public boolean onTouch(View v, MotionEvent event){
                        Intent toMainActivity = new Intent(Main2Activity.this, MainActivity.class);//跳转到主界面
                        startActivity(toMainActivity);
                        return true;

                    }
                });
            }

        }
        vpAdapter=new VpAdapter(imageViews);
        vPager.setAdapter(vpAdapter);
    }
    //小圆点数量判断以及小圆点之间的间隔&小圆点默认启动显示的位置
    private void initDots(){
        LinearLayout layout = (LinearLayout)findViewById(R.id.dot_Layout);
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(20, 20);
        mParams.setMargins(10, 0, 10,0);//设置小圆点左右之间的间隔
        dotViews = new ImageView[imgs.length];
        //判断小圆点的数量，从0开始，0表示第一个
        for(int i = 0; i < imageViews.size(); i++)
        {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(mParams);
            imageView.setImageResource(R.drawable.cb_plan_selector1);
            if(i== 0)
            {
                imageView.setSelected(true);//默认启动时，选中第一个小圆点
            }
            else {
                imageView.setSelected(false);
            }
            dotViews[i] = imageView;//得到每个小圆点的引用，用于滑动页面时，（onPageSelected方法中）更改它们的状态。
            layout.addView(imageView);//添加到布局里面显示
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        for(int i = 0; i < dotViews.length; i++)
        {
            dotViews[i].setImageResource(R.drawable.cb_plan_selector1);
            if(position == i)
            {
                dotViews[i].setSelected(true);
            }
            else {
                dotViews[i].setSelected(false);
            }
        }
    }

    @Override
    public void onPageSelected(int position) {
        Log.i("gaibian","改变");
        for(int i = 0; i < dotViews.length; i++)
        {
            if(position == i)
            {
                dotViews[i].setSelected(true);
            }
            else {
                dotViews[i].setSelected(false);
            }
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {
//        for(int i = 0; i < dotViews.length; i++)
//        {
//            dotViews[i].setImageResource(R.drawable.cb_plan_selector1);
//            if(position == i)
//            {
//                dotViews[i].setSelected(true);
//            }
//            else {
//                dotViews[i].setSelected(false);
//            }
//        }
    }
}

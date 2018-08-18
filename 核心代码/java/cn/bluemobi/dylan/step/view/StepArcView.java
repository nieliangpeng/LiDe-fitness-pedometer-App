package cn.bluemobi.dylan.step.view;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import cn.bluemobi.dylan.step.R;
/**
 * Created by 夏天 on 2018/8/9.
 * 自定义显示步数进度的圆弧以及步数内容
 */

public class StepArcView extends View {
    //圆弧的宽度
    private float borderWidth = dipToPx(20);
    //画步数的数值的字体大小
    private float numberTextSize = 0;
    //步数
    private String stepNumber = "0";
    //开始绘制圆弧的角度
    private float startAngle = 135;
    //终点对应的角度和起始点对应的角度的夹角
    private float angleLength = 270;
    //所要绘制的当前步数的红色圆弧终点到起点的夹角
    private float currentAngleLength = 0;
    //动画时长
    private int animationLength = 3000;

    public StepArcView(Context context) {
        super(context);
    }

    public StepArcView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StepArcView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //中心点的x坐标
        float centerX = (getWidth()) / 2;
        //指定圆弧的外轮廓矩形区域
        RectF rectF = new RectF(0 + borderWidth, borderWidth, 2 * centerX - borderWidth, 2 * centerX - borderWidth);
        //1.绘制整体的黄色圆弧
        drawArcYellow(canvas, rectF);
        //2.绘制当前进度的红色圆弧
        drawArcRed(canvas, rectF);
        //3.绘制当前进度的红色数字
        drawTextNumber(canvas, centerX);
        //4.绘制"步数"的文字
        drawTextStepString(canvas, centerX);
    }

    //绘制总步数的黄色圆弧
    private void drawArcYellow(Canvas canvas, RectF rectF) {
        Paint paint = new Paint();
        //默认画笔颜色，黄色
        paint.setColor(getResources().getColor(R.color.white));
        //结合处为圆弧
        paint.setStrokeJoin(Paint.Join.ROUND);
        //设置画笔的样式 Paint.Cap.Round ,Cap.SQUARE等分别为圆形、方形
        paint.setStrokeCap(Paint.Cap.ROUND);
        //设置画笔的填充样式  Paint.Style.STROKE  ：仅描边
        paint.setStyle(Paint.Style.STROKE);
        //抗锯齿功能
        paint.setAntiAlias(true);
        //设置画笔宽度
        paint.setStrokeWidth(borderWidth);
        /*绘制圆弧的方法
         * drawArc(RectF oval, float startAngle, float sweepAngle, boolean useCenter, Paint paint)//画弧，
         参数一是RectF对象，一个矩形区域椭圆形的界限用于定义在形状、大小、电弧，
         参数二是起始角(度)在电弧的开始，圆弧起始角度，单位为度。
         参数三圆弧扫过的角度，顺时针方向，单位为度,从右中间开始为零度。
         参数四是如果这是true(真)的话,在绘制圆弧时将圆心包括在内，通常用来绘制扇形；如果它是false(假)这将是一个弧线,
         参数五是Paint对象；
         */
        canvas.drawArc(rectF, startAngle, angleLength, false, paint);
    }

    //绘制当前步数的红色圆弧
    private void drawArcRed(Canvas canvas, RectF rectF) {
        Paint paintCurrent = new Paint();
        paintCurrent.setStrokeJoin(Paint.Join.ROUND);
        paintCurrent.setStrokeCap(Paint.Cap.ROUND);
        paintCurrent.setStyle(Paint.Style.STROKE);
        paintCurrent.setAntiAlias(true);
        paintCurrent.setStrokeWidth(borderWidth);
        paintCurrent.setColor(getResources().getColor(R.color.green));
        canvas.drawArc(rectF, startAngle, currentAngleLength, false, paintCurrent);
    }

    //圆环中心的步数
    private void drawTextNumber(Canvas canvas, float centerX) {
        Paint vTextPaint = new Paint();
        vTextPaint.setTextAlign(Paint.Align.CENTER);
        vTextPaint.setAntiAlias(true);//抗锯齿功能
        vTextPaint.setTextSize(numberTextSize);
        Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);
        vTextPaint.setTypeface(font);//字体风格
        vTextPaint.setColor(getResources().getColor(R.color.green));
        Rect bounds_Number = new Rect();
        vTextPaint.getTextBounds(stepNumber, 0, stepNumber.length(), bounds_Number);
        canvas.drawText(stepNumber, centerX, getHeight() / 2 + bounds_Number.height() / 2, vTextPaint);
    }

    //圆环中心[步数]的文字
    private void drawTextStepString(Canvas canvas, float centerX) {
        Paint vTextPaint = new Paint();
        vTextPaint.setTextSize(dipToPx(16));
        vTextPaint.setTextAlign(Paint.Align.CENTER);
        vTextPaint.setAntiAlias(true);//抗锯齿功能
        vTextPaint.setColor(getResources().getColor(R.color.grey));
        String stepString = "步";
        Rect bounds = new Rect();
        vTextPaint.getTextBounds(stepString, 0, stepString.length(), bounds);
        canvas.drawText(stepString, centerX, getHeight() / 2 + bounds.height() + getFontHeight(numberTextSize), vTextPaint);
    }

    /**
     * 获取当前步数的数字的高度
     * @param fontSize 字体大小
     * @return 字体高度
     */
    public int getFontHeight(float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        Rect bounds_Number = new Rect();
        paint.getTextBounds(stepNumber, 0, stepNumber.length(), bounds_Number);
        return bounds_Number.height();
    }

    //dip 转换成px
    private int dipToPx(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }

    /**
     * 所走的步数进度
     * @param totalStepNum  设置的步数
     * @param currentCounts 所走步数
     */
    public void setCurrentCount(int totalStepNum, int currentCounts) {
        //如果当前走的步数超过总步数则圆弧还是270度，不能成为园
        if (currentCounts > totalStepNum) {
            currentCounts = totalStepNum;
        }

        //上次所走步数占用总共步数的百分比
        float scalePrevious = (float) Integer.valueOf(stepNumber) / totalStepNum;
        //换算成弧度最后要到达的角度的长度-->弧长
        float previousAngleLength = scalePrevious * angleLength;

        //所走步数占用总共步数的百分比
        float scale = (float) currentCounts / totalStepNum;
        //换算成弧度最后要到达的角度的长度-->弧长
        float currentAngleLength = scale * angleLength;
        //开始执行动画
        setAnimation(previousAngleLength, currentAngleLength, animationLength);
        stepNumber = String.valueOf(currentCounts);
        setTextSize(currentCounts);
    }
    private void setAnimation(float start, float current, int length) {
        ValueAnimator progressAnimator = ValueAnimator.ofFloat(start, current);
        progressAnimator.setDuration(length);
        progressAnimator.setTarget(currentAngleLength);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentAngleLength = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        progressAnimator.start();
    }
    //设置文本大小,防止步数特别大之后放不下，将字体大小动态设置
    public void setTextSize(int num) {
        String s = String.valueOf(num);
        int length = s.length();
        if (length <= 4) {
            numberTextSize = dipToPx(50);
        } else if (length > 4 && length <= 6) {
            numberTextSize = dipToPx(40);
        } else if (length > 6 && length <= 8) {
            numberTextSize = dipToPx(30);
        } else if (length > 8) {
            numberTextSize = dipToPx(25);
        }
    }
}

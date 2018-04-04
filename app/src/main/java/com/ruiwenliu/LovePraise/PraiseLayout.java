package com.ruiwenliu.LovePraise;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Random;

/**
 * Created by 10547 on 2018/4/4.
 */

public class PraiseLayout extends RelativeLayout {
    //随机数
    private Random mRandom;
    //图片资源
    //控件的宽高
    private int mWidth, mHeight;
    //获取图片的宽高
    private int mDrawableWidth, mDrawableHeight;
    //插值器数组
    private Interpolator[] mInterpolator;

    public PraiseLayout(Context context) {
        this(context, null);
    }

    public PraiseLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PraiseLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRandom = new Random();

        //获取图片的宽高
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.timg);
        mDrawableWidth = drawable.getIntrinsicWidth();
        mDrawableHeight = drawable.getIntrinsicHeight();

        mInterpolator = new Interpolator[]{new AccelerateDecelerateInterpolator(), new AccelerateInterpolator(),
                new DecelerateInterpolator(), new LinearInterpolator()};
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取控件的宽高
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
    }



    /**
     * 添加一个点赞的view
     * 一个类似抖音点赞的功能，菜鸟第一次写，有什么不到之处还望指出，我的Q1054750389
     */
    public void addLove() {
        final ImageView imgPraise = new ImageView(getContext());
        //设置图片资源(随机数)
        imgPraise.setImageResource(R.drawable.timg);
        LayoutParams params = new LayoutParams(
                400, 400);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins((int) mX,(int)mY,0,0);
        imgPraise.setLayoutParams(params);

        addView(imgPraise);
        //添加的效果是：有移动和透明的变化
        AnimatorSet animatorSet = getAimator(imgPraise);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //动画执行完毕后，将其移除
                removeView(imgPraise);
            }
        });
        animatorSet.start();
    }

    private float mX;
    private  float mY;

    public  void startPublish(float x,float y){
        mX=x;
        mY=y;
        addLove();

    }


    /**
     * 设置属性动画效果
     * @param loveIv
     * @return
     */
    public AnimatorSet getAimator(ImageView loveIv) {
        //添加的效果是：有放大和透明的变化
        AnimatorSet allAnimator = new AnimatorSet();
        AnimatorSet innerAnimator = new AnimatorSet();
        //添加两个属性动画
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(loveIv, "alpha", 1f, 0.3f);
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(loveIv, "scaleX", 0.8f, 1.0f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(loveIv, "scaleY", 0.8f, 1.0f);
        ObjectAnimator translateAnimator = ObjectAnimator.ofFloat(loveIv, "translationY", 0, 0, -200,0);
        //一起执行动画
        innerAnimator.playTogether(translateAnimator,scaleXAnimator,scaleYAnimator,alphaAnimator);
        innerAnimator.setDuration(500);
        //运行的路径动画  按顺序执行动画
        allAnimator.playSequentially(innerAnimator, getBezierAnimator(loveIv));
        return allAnimator;
    }
    private PointF mEndPoint=null;
    /**
     * 绘制图片的弹出路线
     * @param imgPraise
     * @return
     */
    private Animator getBezierAnimator(final ImageView imgPraise) {
        PointF point1 = new PointF(0,0);
        PointF point2 = new PointF(0,0);

        mEndPoint = new PointF(mX,mY);
        PraiseEvaluator typeEvaluator = new PraiseEvaluator(point1, point2);
        //第一个参数 typeEvaluator  第二个参数就是p0，第三个参数就是p3
        ValueAnimator bezererAnimator = ObjectAnimator.ofObject(typeEvaluator, new PointF(mX,mY), mEndPoint);
        //插值器
        bezererAnimator.setInterpolator(mInterpolator[mRandom.nextInt(mInterpolator.length - 1)]);
        bezererAnimator.setDuration(500);//5000
        bezererAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //设置图片的透明度
                float t = animation.getAnimatedFraction();
                imgPraise.setAlpha(1 - t + 0.2f);
            }
        });
        return bezererAnimator;
    }



}

package then.com.swip_card;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Build;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.facebook.rebound.BaseSpringSystem;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import then.com.swip_card.utils.AnimUtils;
import then.com.swip_card.utils.CardListener;
import then.com.swip_card.utils.CardUtils;


public class DragCard extends RelativeLayout {
    public static final int TOP_DRAG = 1;//上
    public static final int BOTTOM_DRAG = 2;//下
    public static final int LEFT_DRAG = 3;//左
    public static final int RIGHT_DRAG = 4;//右
    private ViewDragHelper mDragHelper;
    private int mVisibleNum = 5;//可见数
    private int mIndex = 0;//当前索引
    private GestureDetectorCompat gestureDetector;
    private ArrayList<View> viewCollection = new ArrayList<View>();
    private BaseGragAdapter baseGragAdapter;
    private HashMap<View, ViewPropertity> orign_propertitys = new HashMap<>();//view的原始属性集合
    private Stack<ViewPropertity> last_properties = new Stack<>();//飞过去之后的属性集合
    private boolean animt_finish = true;//动画是否结束
    private boolean release_flag = true;//是否滑动完毕
    private int card_margin = 20;//card之间的margin
    private CardListener cardListener;
    //创建facebook spring动画
    private final BaseSpringSystem mSpringSystem = SpringSystem.create();
    private final CustomSpringListener mSpringListener = new CustomSpringListener();
    private Spring mScaleSpring;

    private class CustomSpringListener extends SimpleSpringListener {
        @Override
        public void onSpringUpdate(Spring spring) {
            float value = (float) spring.getCurrentValue();
            float scale = 1f - (value * 0.5f);
            ViewCompat.setScaleX(DragCard.this, scale);
            ViewCompat.setScaleY(DragCard.this, scale);
        }
    }

    public DragCard(Context context) {
        super(context);
    }

    public DragCard(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public DragCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DragCard(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setGravity(Gravity.CENTER_HORIZONTAL);
        loadSpring();//加载spring动画
        init();
    }

    private void loadSpring() {
        mScaleSpring = mSpringSystem.createSpring();
        SpringConfig sc = SpringConfig.fromOrigamiTensionAndFriction(50, 4);
        mScaleSpring.setSpringConfig(sc);
        mScaleSpring.addListener(mSpringListener);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mScaleSpring.addListener(mSpringListener);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mScaleSpring != null)
            mScaleSpring.addListener(mSpringListener);

    }

    private DataSetObserver dataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            for (View view : viewCollection)
                view.setVisibility(VISIBLE);
            last_properties.clear();
            orign_propertitys.clear();
            loadData();
            mScaleSpring.setCurrentValue(0.9f);
            mScaleSpring.setEndValue(0.0f);
        }
    };

    public void setAdapter(BaseGragAdapter<?> mAdapter) {
        this.baseGragAdapter = mAdapter;
        this.baseGragAdapter.registerDataSetObserver(dataSetObserver);
        clearCache();
        loadData();
        mScaleSpring.setCurrentValue(0.9f);
        mScaleSpring.setEndValue(0.0f);
    }

    private void clearCache() {
        mIndex = 0;
        last_properties.clear();
        orign_propertitys.clear();
    }


    /**
     * 加载数据
     */
    private void loadData() {
        mIndex = 0;
        for (int i = mVisibleNum - 1; i >= 0; i--) {
            if (mVisibleNum > baseGragAdapter.getCount())
                break;
            int index = (mVisibleNum - 1) - i;
            ViewGroup parent = (ViewGroup) viewCollection.get(index);
            View child = baseGragAdapter.getView(i, null
                    , null);
            parent.addView(child);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) child.getLayoutParams();
            params.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            float s_x = 1.0f - i * 0.01f;
            float s_y = 1.0f - i * 0.01f;
            float alpha = 1.0f - i * 0.05f;
            float y = index * card_margin + params.topMargin;
            float x = params.leftMargin;
            ViewPropertity propertity = ViewPropertity.createProperties(x, y, s_x, s_y, alpha);
            CardUtils.setProperties(parent, propertity);
            orign_propertitys.put(parent, propertity);
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mDragHelper.cancel();
            return false;
        }
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            mDragHelper.processTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    //把滑动过去的历史滑动回来
    private void animBackward() {
        animt_finish = false;
        View back = viewCollection.remove(0);
        if (back.getVisibility()==GONE)
            back.setVisibility(VISIBLE);
        ViewGroup viewGroup = (ViewGroup) back.getParent();
        if (viewGroup != null) {
            viewGroup.removeView(back);
        }
        viewGroup.addView(back);
        ViewPropertity last = last_properties.pop();
        CardUtils.setProperties(back, last);
        viewCollection.add(back);
        ViewGroup group = (ViewGroup) back;
        group.removeAllViews();
        group.addView(baseGragAdapter.getView((mIndex - 1), null, null));
        ViewPropertity propertity = ViewPropertity.createProperties(0, (mVisibleNum - 1) * card_margin, 1.0f, 1.0f, 1.0f);
        Animator animator = AnimUtils.getValueAnimator(last, propertity, back);
        animator.setInterpolator(new DecelerateInterpolator());
        AnimatorSet as = new AnimatorSet();
        ArrayList<Animator> aCollection = new ArrayList<Animator>();
        for (int i = viewCollection.size() - 1; i >= 0; i--) {
            View view = viewCollection.get(i);
            if (back == view)
                continue;
            float s_x = ViewCompat.getScaleX(view);
            float s_y = ViewCompat.getScaleY(view);
            float alpha = ViewCompat.getAlpha(view);
            float y = ViewCompat.getTranslationY(view);
            ViewPropertity start = ViewPropertity.createProperties(0, y, s_x, s_y, alpha);
            s_x -= 0.01f;
            s_y -= 0.01f;
            alpha -= 0.05f;
            y -= card_margin;
            ViewPropertity end = ViewPropertity.createProperties(0, y, s_x, s_y, alpha);
            ValueAnimator valueAnimator = AnimUtils.getValueAnimator(start, end, view);
            aCollection.add(valueAnimator);
        }
        as.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                release_flag = true;
                animt_finish = true;
                mIndex--;
                cardListener.endSwip(mIndex);
            }
        });
        aCollection.add(animator);
        as.playTogether(aCollection);
        as.start();
    }

    //把所有组件向前移动
    private void animLast() {
        AnimatorSet as = new AnimatorSet();
        ArrayList<Animator> aCollection = new ArrayList<Animator>();
        for (int i = viewCollection.size() - 1; i >= 0; i--) {
            View view = viewCollection.get(i);
            if (view == getTopView()) {
                continue;
            }
            float s_x = ViewCompat.getScaleX(view);
            float s_y = ViewCompat.getScaleY(view);
            float alpha = ViewCompat.getAlpha(view);
            float y = ViewCompat.getTranslationY(view);
            ViewPropertity start = ViewPropertity.createProperties(0, y, s_x, s_y, alpha);
            s_x += 0.01f;
            s_y += 0.01f;
            alpha += 0.05f;
            y += card_margin;
            ViewPropertity end = ViewPropertity.createProperties(0, y, s_x, s_y, alpha);
            ValueAnimator valueAnimator = AnimUtils.getValueAnimator(start, end, view);
            aCollection.add(valueAnimator);
        }
        as.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                release_flag = true;
                animt_finish = true;
                mIndex++;
                moveToBack();
            }
        });
        as.playTogether(aCollection);
        as.start();

    }

    private void moveToBack() {
        View temp = getTopView();
        ViewPropertity viewPropertity = CardUtils.getAllProperties(temp);
        last_properties.push(viewPropertity);
        ViewGroup viewParent = (ViewGroup) temp.getParent();
        if (viewParent != null) {
            viewParent.removeView(temp);
            viewParent.addView(temp, 0);
        }
        for (int i = (viewCollection.size() - 1); i > 0; i--) {
            View current = viewCollection.get(i - 1);
            viewCollection.set(i, current);
        }
        viewCollection.set(0, temp);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) temp.getLayoutParams();
        float s = (1.0f - 0.01f * (mVisibleNum - 1));
        float y = params.topMargin;
        float x = params.leftMargin;
        float alpha = 1.0f;
        ViewPropertity propertity = ViewPropertity.createProperties(x, y, s, s, alpha, 0);
        CardUtils.setProperties(temp, propertity);
        //如果有数据就进行加载
        if ((mIndex + mVisibleNum - 1) < baseGragAdapter.getCount()) {
            ViewGroup cg = (ViewGroup) temp;
            cg.removeAllViews();
            cg.addView(baseGragAdapter.getView((mIndex + mVisibleNum - 1), null, null));
        } else {
            temp.setVisibility(GONE);
        }

        if (mIndex + 1 > baseGragAdapter.getCount()) {
            if (cardListener != null)
                cardListener.loadEnd(mIndex);
            return;
        }
        if (cardListener != null)
            cardListener.endSwip(mIndex);
    }

    private View getTopView() {
        return this.viewCollection.get(this.viewCollection.size() - 1);
    }

    private void init() {
        loadVisibileViews();
        mDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public int getViewHorizontalDragRange(View child) {
                return 1;
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return 1;
            }

            private int view_left = -1;

            @Override
            public boolean tryCaptureView(View view, int i) {
                //如果动画没有完成就不允许拖动了
                if (!animt_finish)
                    return false;
                //判断如果是DragContentView就进行拖动
                if (view_left == -1)
                    view_left = view.getLeft();
                return getTopView() == view && release_flag;

            }

            private int left = 0;
            private int top = 0;
            private int total_x = 0;

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
                float rotation_coefficient = 20f;
                total_x += dx;
                float mRotation = (total_x / rotation_coefficient);
                this.left = total_x;
                this.top = top;
                ViewCompat.setRotation(changedView, mRotation);
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                release_flag = false;
                int left_num = 0;
                int top_num = 0;

                boolean scroll = false;
                if (Math.abs(this.left) > 300) {
                    if (this.left > 0)
                        left_num = this.left + 1000;
                    else
                        left_num = this.left - 1000;
                    scroll = true;
                }
                if (Math.abs(this.top) > 300) {
                    if (this.top > 0)
                        top_num = this.top + 1000;
                    else
                        top_num = this.top - 1000;
                    scroll = true;
                }
                if (scroll) {
                    animt_finish = false;
                    float rotation_coefficient = 20f;
                    float startRotation = (this.left / rotation_coefficient);
                    float endRotation = (left_num / rotation_coefficient);
                    Animator animator = AnimUtils.getValueAnimator(ViewPropertity.createProperties(this.left, this.top, 1.0f, 1.0f, 1.0f, startRotation),
                            ViewPropertity.createProperties(left_num, top_num, 1.0f, 1.0f, 1.0f, endRotation), releasedChild);
                    if (cardListener != null)
                        cardListener.startSwip(mIndex);
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            animLast();
                            view_left = -1;
                            total_x = 0;
                        }
                    });
                    animator.setDuration(500);
                    animator.start();
                } else {
                    mDragHelper.smoothSlideViewTo(releasedChild, view_left, top_num + getPaddingTop());
                    ViewCompat.postInvalidateOnAnimation(DragCard.this);
                    release_flag = true;
                }
            }

            /**
             * 当拖拽到状态改变时回调
             * @params 新的状态
             */
            @Override
            public void onViewDragStateChanged(int state) {
                switch (state) {
                    case ViewDragHelper.STATE_DRAGGING:  // 正在被拖动
                        break;
                    case ViewDragHelper.STATE_IDLE:  // view没有被拖拽或者 正在进行fling/snap
                        break;
                    case ViewDragHelper.STATE_SETTLING: // fling完毕后被放置到一个位置
                        break;
                }
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
               /*
                return newLeft 可以不让拖出边界
                final int leftBound = getPaddingLeft();
                final int rightBound = getWidth() - mDragView.getWidth();
                final int newLeft = Math.min(Math.max(left, leftBound), rightBound);
*/
                return left;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
           /*
                           return newTop 可以不让拖出边界
           final int topBound = getPaddingTop();
                final int bottomBound = getHeight() - mDragView.getHeight();
                final int newTop = Math.min(Math.max(top, topBound), bottomBound);*/
                return top;
            }
        });
    }

    /**
     * 上一个
     *
     * @param direction
     */
    public void preCard(int direction) {
        //如果动画没完成就不执行
        if (!animt_finish)
            return;
        //如果栈里面没有了
        if (last_properties.size() < 1)
            return;

        animBackward();
    }

    /**
     * @param direction 方向
     */
    public void dragCard(int direction) {
        if (mIndex > baseGragAdapter.getCount()) {
            return;
        }
        if (!animt_finish)
            return;
        animt_finish = false;
        final View topView = getTopView();
        ObjectAnimator animator = ObjectAnimator.ofFloat(topView, "drag", ViewCompat.getTranslationX(topView), 30f, -1200f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                ViewCompat.setTranslationX(topView, value);
                float rotation_coefficient = 30f;
                float mRotation = (value / rotation_coefficient);
                if (value < 0)
                    ViewCompat.setRotation(topView, mRotation);
            }
        });
        animator.setDuration(700);
        animator.setInterpolator(new DecelerateInterpolator());
        AnimatorSet as = new AnimatorSet();
        ArrayList<Animator> aCollection = new ArrayList<Animator>();
        for (int i = viewCollection.size() - 1; i >= 0; i--) {
            View view = viewCollection.get(i);
            if (view == getTopView()) {
                continue;
            }
            ViewPropertity propertity = CardUtils.getAllProperties(view);
            float s_x = propertity.s_x;
            float s_y = propertity.s_y;
            float alpha = propertity.alpha;
            float y = propertity.y;
            ViewPropertity start = ViewPropertity.createProperties(0, y, s_x, s_y, alpha);
            s_x += 0.01f;
            s_y += 0.01f;
            alpha += 0.05f;
            y += card_margin;
            ViewPropertity end = ViewPropertity.createProperties(0, y, s_x, s_y, alpha);
            ValueAnimator valueAnimator = AnimUtils.getValueAnimator(start, end, view);
            aCollection.add(valueAnimator);
        }
        aCollection.add(animator);
        as.playTogether(aCollection);
        as.start();
        as.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animt_finish = true;
                mIndex++;
                moveToBack();

            }
        });
        if (cardListener != null)
            cardListener.startSwip(mIndex);
    }

    private void loadVisibileViews() {
        for (int i = 0; i < mVisibleNum; i++) {
            FrameLayout v = new FrameLayout(getContext());
            viewCollection.add(v);
            addView(v);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v.getLayoutParams();
            params.width = LayoutParams.WRAP_CONTENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            v.setLayoutParams(params);
        }
    }

    public int getmIndex() {
        return mIndex;
    }


    public void setCardListener(CardListener cardListener) {
        this.cardListener = cardListener;
    }

    public static class ViewPropertity {
        public float y;//偏移y
        public float x;//偏移x
        public float alpha;//透明度
        public float s_x;//放大x
        public float s_y;//放大y
        public float rotate;//旋转

        public ViewPropertity(float x, float y, float s_x, float s_y, float alpha) {
            this.x = x;
            this.y = y;
            this.alpha = alpha;
            this.s_x = s_x;
            this.s_y = s_y;
        }

        public ViewPropertity(float x, float y, float s_x, float s_y, float alpha, float rotate) {
            this.x = x;
            this.y = y;
            this.alpha = alpha;
            this.s_x = s_x;
            this.s_y = s_y;
            this.rotate = rotate;
        }

        public ViewPropertity(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public static ViewPropertity createProperties(float x, float y, float s_x, float s_y, float alpha) {
            return new ViewPropertity(x, y, s_x, s_y, alpha);
        }

        public static ViewPropertity createProperties(float x, float y, float s_x, float s_y, float alpha, float rotate) {
            return new ViewPropertity(x, y, s_x, s_y, alpha, rotate);
        }

        public static ViewPropertity createProperties(float top, float left) {
            return new ViewPropertity(left, top);
        }
    }

}

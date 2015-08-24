package then.com.library.swip_card.manager;

import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.ArrayList;

import then.com.library.swip_card.DragCard;
import then.com.library.swip_card.utils.AnimUtils;
import then.com.library.swip_card.utils.CardUtils;


public class TopToBottomLayoutManager extends BaseLayoutManager {
    @Override
    public DragCard.ViewPropertity layout(float scale, float card_margin, float alpha, View child, int position, int visibleNum) {
        float s_x = 1.0f - position * scale;
        float s_y = 1.0f - position * scale;
        float tmp_alpha = 1.0f - position * alpha;
        int index = (visibleNum - 1) - position;
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) child.getLayoutParams();
        float y = index * card_margin + params.topMargin;
        float x = params.leftMargin;
        DragCard.ViewPropertity propertity = DragCard.ViewPropertity.createProperties(x, y, s_x, s_y, tmp_alpha);
        return propertity;
    }

    @Override
    public void onViewPositionChanged(View view, float totalX, float totalY,float rotation_coefficient) {
        float mRotation = (totalX / rotation_coefficient);
        ViewCompat.setRotation(view, mRotation);
    }

    @Override
    public AnimatorSet animLast(float m_scale, float card_margin, float m_alpha, View nowView, ArrayList<View> viewCollection) {
        AnimatorSet as = new AnimatorSet();
        ArrayList<Animator> aCollection = new ArrayList<Animator>();
        for (int i = viewCollection.size() - 1; i >= 0; i--) {
            View view = viewCollection.get(i);
            if (view == nowView) {
                continue;
            }
            float s_x = ViewCompat.getScaleX(view);
            float s_y = ViewCompat.getScaleY(view);
            float alpha = ViewCompat.getAlpha(view);
            float y = ViewCompat.getTranslationY(view);
            DragCard.ViewPropertity start = DragCard.ViewPropertity.createProperties(0, y, s_x, s_y, alpha);
            s_x += m_scale;
            s_y += m_scale;
            alpha += m_alpha;
            y += card_margin;
            DragCard.ViewPropertity end = DragCard.ViewPropertity.createProperties(0, y, s_x, s_y, alpha);
            ValueAnimator valueAnimator = AnimUtils.getValueAnimator(start, end, view);
            aCollection.add(valueAnimator);
        }
        as.playTogether(aCollection);
        return as;
    }

    @Override
    public AnimatorSet animForward(int direction, float m_scale, float card_margin, float m_alpha, View nowView, ArrayList<View> viewCollection) {
        final View topView = nowView;
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
            if (view == topView) {
                continue;
            }
            DragCard.ViewPropertity propertity = CardUtils.getAllProperties(view);
            float s_x = propertity.s_x;
            float s_y = propertity.s_y;
            float alpha = propertity.alpha;
            float y = propertity.y;
            DragCard.ViewPropertity start = DragCard.ViewPropertity.createProperties(0, y, s_x, s_y, alpha);
            s_x += m_scale;
            s_y += m_scale;
            alpha += m_alpha;
            y += card_margin;
            DragCard.ViewPropertity end = DragCard.ViewPropertity.createProperties(0, y, s_x, s_y, alpha);
            ValueAnimator valueAnimator = AnimUtils.getValueAnimator(start, end, view);
            aCollection.add(valueAnimator);
        }
        aCollection.add(animator);
        as.playTogether(aCollection);
        return as;
    }


    @Override
    public AnimatorSet backToLocation(int mVisibleNum, float m_scale, float card_margin, float m_alpha, View nowView,DragCard.ViewPropertity orginPropertity ,ArrayList<View> viewCollection) {
        DragCard.ViewPropertity propertity = DragCard.ViewPropertity.createProperties(0, (mVisibleNum - 1) * card_margin, 1.0f, 1.0f, 1.0f);
        Animator animator = AnimUtils.getValueAnimator(orginPropertity, propertity, nowView);
        animator.setInterpolator(new DecelerateInterpolator());
        AnimatorSet as = new AnimatorSet();
        ArrayList<Animator> aCollection = new ArrayList<Animator>();
        for (int i = viewCollection.size() - 1; i >= 0; i--) {
            View view = viewCollection.get(i);
            if (nowView == view)
                continue;
            float s_x = ViewCompat.getScaleX(view);
            float s_y = ViewCompat.getScaleY(view);
            float alpha = ViewCompat.getAlpha(view);
            float y = ViewCompat.getTranslationY(view);
            DragCard.ViewPropertity start = DragCard.ViewPropertity.createProperties(0, y, s_x, s_y, alpha);
            s_x -= m_scale;
            s_y -= m_scale;
            alpha -= m_alpha;
            y -= card_margin;
            DragCard.ViewPropertity end = DragCard.ViewPropertity.createProperties(0, y, s_x, s_y, alpha);
            ValueAnimator valueAnimator = AnimUtils.getValueAnimator(start, end, view);
            aCollection.add(valueAnimator);
        }
        aCollection.add(animator);
        as.playTogether(aCollection);
        return as;
    }

    @Override
    public void moveToBack(View nowView, float mScale, float m_alpha, float card_margin, int mVisibleNum) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) nowView.getLayoutParams();
        float s = (1.0f - mScale * (mVisibleNum - 1));
        float y = params.topMargin;
        float x = params.leftMargin;
        float alpha = 1.0f;
        DragCard.ViewPropertity propertity = DragCard.ViewPropertity.createProperties(x, y, s, s, alpha, 0);
        CardUtils.setProperties(nowView, propertity);
    }
}

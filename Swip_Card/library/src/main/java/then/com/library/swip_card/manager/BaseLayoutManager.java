package then.com.library.swip_card.manager;

import android.view.View;

import com.nineoldandroids.animation.AnimatorSet;

import java.util.ArrayList;

import then.com.library.swip_card.DragCard;


public abstract class BaseLayoutManager {
    public abstract DragCard.ViewPropertity layout(float scale, float margin, float alpha, View view, int position, int visibleNum);

    //当view进行滑动的时候
    public abstract void onViewPositionChanged(View view,float totalX,float totalY,float rotation_coefficient);
    //滑动的时候，自动滑动到不可见
    public abstract AnimatorSet animLast(float m_scale, float card_margin, float m_alpha, View nowView, ArrayList<View> views);

    //点击前下一个滑动
    public abstract AnimatorSet animForward(int direction, float m_scale, float card_margin, float m_alpha, View nowView, ArrayList<View> views);

    //回到原来的位置
    public abstract AnimatorSet backToLocation(int mVisibleNum, float m_scale, float card_margin, float m_alpha, View nowView, DragCard.ViewPropertity orginPropertity, ArrayList<View> viewCollection);

    //把划走的位置进行重置
    public abstract void moveToBack(View nowView, float mScale, float m_alpha, float card_margin, int mVisibleNum);
}

package then.com.swip_card.manager;

import android.view.View;

import com.nineoldandroids.animation.AnimatorSet;

import java.util.ArrayList;

import then.com.swip_card.DragCard;

public abstract class BaseLayoutManager {
    public abstract DragCard.ViewPropertity layout(float scale, float margin, float alpha, View view, int position, int visibleNum);

    //点击前下一个滑动
    public abstract AnimatorSet animForward(int direction, float m_scale, float card_margin, float m_alpha, View nowView, ArrayList<View> views);

    //回到原来的位置
    public abstract void backToLocation(int direction, float m_scale, float card_margin, float m_alpha, View nowView, ArrayList<View> views);
    //把划走的位置进行重置
    public abstract void moveToBack(View nowView, float mScale, float m_alpha, float card_margin,int mVisibleNum);
}

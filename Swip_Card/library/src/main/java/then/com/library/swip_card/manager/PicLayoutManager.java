package then.com.library.swip_card.manager;

import android.view.View;

import com.nineoldandroids.animation.AnimatorSet;

import java.util.ArrayList;

import then.com.library.swip_card.DragCard;


//模仿相册的layout
public class PicLayoutManager extends BaseLayoutManager {
    @Override
    public DragCard.ViewPropertity layout(float scale, float margin, float alpha, View child, int position, int visibleNum) {

        DragCard.ViewPropertity propertity = DragCard.ViewPropertity.createProperties(0, 0, 1.0f, 1.0f, 1.0f,10);
        return null;
    }

    @Override
    public void onViewPositionChanged(View view, float totalX, float totalY, float rotation_coefficient) {

    }

    @Override
    public AnimatorSet animLast(float m_scale, float card_margin, float m_alpha, View nowView, ArrayList<View> views) {
        return null;
    }

    @Override
    public AnimatorSet animForward(int direction, float m_scale, float card_margin, float m_alpha, View nowView, ArrayList<View> views) {
        return null;
    }

    @Override
    public AnimatorSet backToLocation(int mVisibleNum, float m_scale, float card_margin, float m_alpha, View nowView, DragCard.ViewPropertity orginPropertity, ArrayList<View> viewCollection) {
        return null;
    }

    @Override
    public void moveToBack(View nowView, float mScale, float m_alpha, float card_margin, int mVisibleNum) {

    }
}

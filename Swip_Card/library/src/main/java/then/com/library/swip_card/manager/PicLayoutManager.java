package then.com.library.swip_card.manager;

import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.ArrayList;
import java.util.Random;

import then.com.library.swip_card.DragCard;
import then.com.library.swip_card.utils.AnimUtils;


//模仿相册的layout
public class PicLayoutManager extends BaseLayoutManager {
    @Override
    public DragCard.ViewPropertity layout(float scale, float margin, float alpha, View child, int position, int visibleNum) {
        DragCard.ViewPropertity propertity = null;
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) child.getLayoutParams();

        switch (position % 3) {
            //正常
            case 0:
                propertity = DragCard.ViewPropertity.createProperties(0, 0, 1.0f, 1.0f, 1.0f, 0);
                break;
            case 1:
                propertity = DragCard.ViewPropertity.createProperties(0, 0, 1.0f, 1.0f, 1.0f, 10);
                break;
            case 2:
                propertity = DragCard.ViewPropertity.createProperties(0, 0, 1.0f, 1.0f, 1.0f, 0);
                //正常
                break;
        }
        return propertity;
    }

    @Override
    public void onViewPositionChanged(View view, float totalX, float totalY, float rotation_coefficient) {

    }

    @Override
    public AnimatorSet animLast(float m_scale, float card_margin, float m_alpha, View nowView, ArrayList<View> views) {
        AnimatorSet as = new AnimatorSet();
        ArrayList<Animator> aCollection = new ArrayList<Animator>();
        for (int i = views.size() - 1; i >= 0; i--) {
            View view = views.get(i);
            if (view == nowView) {
                continue;
            }
            float rotation = ViewCompat.getRotation(view);
            float y = ViewCompat.getTranslationY(view);
            float x = ViewCompat.getTranslationX(view);
            DragCard.ViewPropertity start = DragCard.ViewPropertity.createProperties(x, y, 1.0f, 1.0f, 1.0f, rotation);
            final int r = (int) rotation;
            switch (r) {
                case 0:
                    if (new Random().nextInt(2) >= 1)
                        rotation = 10;
                    else
                        rotation = -10;
                    break;
                case -10:
                    rotation = 0;
                    break;
                case 10:
                    rotation = 0;
                    break;
            }
            DragCard.ViewPropertity end = DragCard.ViewPropertity.createProperties(x, y, 1.0f, 1.0f, 1.0f, rotation);
            ValueAnimator valueAnimator = AnimUtils.getValueAnimator(start, end, view);
            aCollection.add(valueAnimator);
        }
        as.setDuration(200);
        as.setInterpolator(new DecelerateInterpolator());
        as.playTogether(aCollection);
        return as;
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

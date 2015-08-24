package then.com.library.swip_card.utils;


import android.support.v4.view.ViewCompat;
import android.view.View;

import com.nineoldandroids.animation.ValueAnimator;

import then.com.library.swip_card.DragCard;


public class AnimUtils {
    public static ValueAnimator getValueAnimator(DragCard.ViewPropertity start, DragCard.ViewPropertity end, final View view) {
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new ViewPropertityEvaluator(), start, end);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                DragCard.ViewPropertity propertity = (DragCard.ViewPropertity) valueAnimator.getAnimatedValue();
                ViewCompat.setTranslationY(view, propertity.y);
                ViewCompat.setTranslationX(view, propertity.x);
                ViewCompat.setScaleY(view, propertity.s_y);
                ViewCompat.setScaleX(view, propertity.s_x);
                ViewCompat.setAlpha(view, propertity.alpha);
                ViewCompat.setRotation(view, propertity.rotate);
            }
        });
        return valueAnimator;
    }
}

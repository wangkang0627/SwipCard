package then.com.library.swip_card.utils;

import android.support.v4.view.ViewCompat;
import android.view.View;

import then.com.library.swip_card.DragCard;


public class CardUtils {
    //设置所有属性
    public static void setProperties(View view, DragCard.ViewPropertity propertity) {
        ViewCompat.setTranslationX(view, propertity.x);
        ViewCompat.setTranslationY(view, propertity.y);
        ViewCompat.setScaleX(view, propertity.s_x);
        ViewCompat.setScaleY(view, propertity.s_y);
        ViewCompat.setAlpha(view, propertity.alpha);
        ViewCompat.setRotation(view, propertity.rotate);
    }
    //获取所有属性
    public static DragCard.ViewPropertity getAllProperties(View view){
        return DragCard.ViewPropertity.createProperties(ViewCompat.getTranslationX(view), ViewCompat.getTranslationY(view),
                ViewCompat.getScaleX(view), ViewCompat.getScaleY(view), ViewCompat.getAlpha(view), ViewCompat.getRotation(view));
    }
}

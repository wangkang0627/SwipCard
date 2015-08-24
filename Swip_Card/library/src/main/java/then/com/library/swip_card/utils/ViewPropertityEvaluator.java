package then.com.library.swip_card.utils;

import com.nineoldandroids.animation.TypeEvaluator;

import then.com.library.swip_card.DragCard;


/**
 * Created by Administrator on 2015/8/16 0016.
 */
public class ViewPropertityEvaluator implements TypeEvaluator<DragCard.ViewPropertity> {
    @Override
    public DragCard.ViewPropertity evaluate(float fraction, DragCard.ViewPropertity start, DragCard.ViewPropertity end) {
        float x = start.x + fraction * (end.x - start.x);
        float y = start.y + fraction * (end.y - start.y);
        float alpha = start.alpha + fraction * (end.alpha - start.alpha);
        float s_x = start.s_x + fraction * (end.s_x - start.s_x);
        float s_y = start.s_y + fraction * (end.s_y - start.s_y);
        float rotate = start.rotate + fraction * (end.rotate - start.rotate);
        return DragCard.ViewPropertity.createProperties(x, y, s_x, s_y, alpha,rotate);
    }
}

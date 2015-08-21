package then.com.swip_card.manager;

import android.view.View;
import android.widget.FrameLayout;

import then.com.swip_card.DragCard;


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
}

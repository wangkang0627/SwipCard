package then.com.swip_card.manager;

import android.view.View;

import then.com.swip_card.DragCard;

public abstract class BaseLayoutManager {
    public abstract DragCard.ViewPropertity layout(float scale,float margin,float alpha,View view,int position,int visibleNum);
}

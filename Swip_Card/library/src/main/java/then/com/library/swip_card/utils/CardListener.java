package then.com.library.swip_card.utils;

public abstract class CardListener {
    //滑动到最后一个
    public void loadEnd(int position) {
    }

    //开始滑动
    public void startSwip(int position) {
    }

    //滑动结束
    public void endSwip(int position) {
    }
}

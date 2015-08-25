package then.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import then.com.library.swip_card.BaseGragAdapter;
import then.com.model.PicModel;
import then.com.swip_card.R;

/**
 * Created by Administrator on 2015/8/24 0024.
 */
public class PicAdapter extends BaseGragAdapter<PicModel> {
    private Context context;

    public PicAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder createHolder(int position) {
        return new PicViewHolder(LayoutInflater.from(context).inflate(R.layout.item_pic_layout, null));
    }

    @Override
    public void bindViews(ViewHolder viewHolder, int position) {
        PicViewHolder picViewHolder = (PicViewHolder) viewHolder;
        PicModel picModel = getItem(position);
        picViewHolder.iv_image.setImageResource(picModel.res_id);
    }

    private class PicViewHolder extends BaseGragAdapter.ViewHolder {
        public ImageView iv_image;
        public PicViewHolder(View rootView) {
            super(rootView);
            iv_image = (ImageView) rootView.findViewById(R.id.iv_image);
        }
    }
}

package then.com.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import then.com.library.swip_card.BaseGragAdapter;
import then.com.swip_card.R;

/**
 * Created by Administrator on 2015/8/16 0016.
 */
public class MyAdapter extends BaseGragAdapter<Integer> {
    private Context context;

    public MyAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder createHolder(int position) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.card_item, null));
    }

    @Override
    public void bindViews(ViewHolder viewHolder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
        myViewHolder.code.setText(position + "");
    }

    private class MyViewHolder extends BaseGragAdapter.ViewHolder {
        public TextView code;

        public MyViewHolder(View rootView) {
            super(rootView);
            code = (TextView) rootView.findViewById(R.id.text);
        }
    }
}

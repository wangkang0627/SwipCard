package then.com.library.swip_card;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/16 0016.
 */
public abstract class BaseGragAdapter<T> extends BaseAdapter {
    protected List<T> mDatas = new ArrayList<T>();

    public void refreshData(List<T> data) {
        if (data != null) {
            mDatas.clear();
            mDatas.addAll(data);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = createHolder(position);
            convertView = viewHolder.rootView;
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();
        bindViews(viewHolder, position);
        return convertView;
    }

    public abstract ViewHolder createHolder(int position);

    public abstract void bindViews(ViewHolder viewHolder, int position);

    public class ViewHolder {
        public View rootView;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
        }
    }
}

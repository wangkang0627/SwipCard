package then.com.swip_card;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.internal.widget.ViewUtils;
import android.support.v7.widget.AppCompatButton;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import then.com.library.swip_card.DragCard;
import then.com.library.swip_card.manager.PicLayoutManager;
import then.com.library.swip_card.utils.CardListener;
import then.com.test.MyAdapter;

public class MainActivity extends AppCompatActivity {
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_container);
        listView = (ListView) findViewById(R.id.contentlist);
        final ArrayList<ActivityList> data = new ArrayList<>();
        data.add(new ActivityList(SwipCardActivity.class, "滑动卡片"));
        data.add(new ActivityList(PicActivity.class, "滑动相册"));
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return data.size();
            }

            @Override
            public ActivityList getItem(int i) {
                return data.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                if (view == null)
                    view = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, null);
                TextView textView = (TextView) view;
                textView.setText(getItem(i).name);
                return view;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ActivityList activityList = data.get(i);
                Intent intent = new Intent(MainActivity.this, activityList.aClass);
                startActivity(intent);
            }
        });
    }

    public static class ActivityList {
        public Class aClass;
        public String name;

        public ActivityList(Class aClass, String name) {
            this.aClass = aClass;
            this.name = name;
        }
    }
}

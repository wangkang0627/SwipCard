package then.com.swip_card;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.internal.widget.ViewUtils;
import android.support.v7.widget.AppCompatButton;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import then.com.swip_card.utils.CardListener;
import then.com.test.MyAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final MyAdapter myAdapter = new MyAdapter(this);
        final List<Integer> data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            data.add(i);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                myAdapter.refreshData(data);
            }
        }, 1000);
        final DragCard dragCard = (DragCard) findViewById(R.id.parent);
        dragCard.setAdapter(myAdapter);
        findViewById(R.id.test_a).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dragCard.dragCard(1);
            }
        });
        findViewById(R.id.test_b).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dragCard.preCard(1);
            }
        });
        dragCard.setCardListener(new CardListener() {
            @Override
            public void loadEnd(int position) {
                super.loadEnd(position);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        myAdapter.refreshData(data);
                    }
                }, 1000);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

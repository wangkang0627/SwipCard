package then.com.swip_card;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import then.com.adapter.PicAdapter;
import then.com.library.swip_card.DragCard;
import then.com.library.swip_card.manager.PicLayoutManager;
import then.com.library.swip_card.utils.CardListener;
import then.com.model.PicModel;
import then.com.test.MyAdapter;


public class PicActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_layout);
        final PicAdapter myAdapter = new PicAdapter(this);
        final List<PicModel> data = new ArrayList<>();
        data.add(new PicModel(R.mipmap.img1));
        data.add(new PicModel(R.mipmap.img2));
        data.add(new PicModel(R.mipmap.img3));
        data.add(new PicModel(R.mipmap.img2));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                myAdapter.refreshData(data);
            }
        }, 1000);
        final DragCard dragCard = (DragCard) findViewById(R.id.parent);
        dragCard.setmDisappearDuration(700);
        dragCard.setLayoutManager(new PicLayoutManager());
        dragCard.setAdapter(myAdapter);
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

}

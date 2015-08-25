package then.com.model;

import android.support.annotation.DrawableRes;

import java.io.Serializable;


public class PicModel implements Serializable {
    public int res_id;

    public PicModel( int res_id) {
        this.res_id = res_id;
    }
}

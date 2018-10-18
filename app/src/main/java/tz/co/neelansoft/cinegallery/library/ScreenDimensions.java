package tz.co.neelansoft.cinegallery.library;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by landre on 11/10/2017.
 */
public class ScreenDimensions {
    private final DisplayMetrics dm;

    public ScreenDimensions(Context context){
        this.dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);

    }

    private float getDpWidth(){

        return dm.widthPixels/dm.density;
    }


    public boolean isMedium(){
        int MEDIUM = 600;
        return getDpWidth() >= MEDIUM;
    }
}

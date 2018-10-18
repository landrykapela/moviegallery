package tz.co.neelansoft.cinegallery.library;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by landre on 11/10/2017.
 */
public class ScreenDimensions {
    private DisplayMetrics dm;
    private Context context;
    private int SMALL = 480;
    private int MEDIUM = 600;
    private int LARGE = 800;
    public ScreenDimensions(Context context){
        this.dm = new DisplayMetrics();
        this.context = context;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);

    }

    public float getDpWidth(){

        float width = dm.widthPixels/dm.density;
        return width;
    }

    public float getDpHeight(){
        float height = dm.heightPixels/dm.density;
        return height;
    }


    public int getPixelWidth(){

        int width = dm.widthPixels;
        return width;
    }

    public int getPixelHeight(){
        int height = dm.heightPixels;
        return height;
    }
    public boolean isSmall(){
        return getDpWidth() <= SMALL;
    }

    public boolean isMedium(){
        return getDpWidth() >= MEDIUM;
    }
    public boolean isXLarge(){
        return getDpWidth() >= LARGE;
    }
}

package dh_kang.nozero.IntegratedClass;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by dh93 on 2017-09-03.
 */

public class CheckNetworkState extends Activity {
    public boolean returnNetworkState(Context context){
        ConnectivityManager connectManager = (ConnectivityManager) context.getSystemService (Context.CONNECTIVITY_SERVICE);
        return connectManager.getActiveNetworkInfo() != null;
    }
}

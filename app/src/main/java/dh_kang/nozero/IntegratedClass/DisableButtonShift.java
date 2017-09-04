package dh_kang.nozero.IntegratedClass;

import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import java.lang.reflect.Field;

/**
 * Created by dh93 on 2017-09-04.
 */
public class DisableButtonShift{
    public void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                item.setChecked(item.getItemData().isChecked());    // set once again checked value, so view will be updated
            }
        } catch (NoSuchFieldException e) {
            // Exception : NoSuchFieldException
        } catch (IllegalAccessException e) {
            // Exception : IllegalAccessException
        }
    }
}

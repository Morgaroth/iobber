package pl.edu.agh.iobber;

import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;

import java.util.logging.Logger;

/**
 * jakieś proste uniwersalne metodki robiące coś:P
 */
public class Helpers {
    private Logger logger = Logger.getLogger(Helpers.class.getSimpleName());

    public static void closeDrawerCorrectly(DrawerLayout mDrawerLayout) {
        if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            mDrawerLayout.closeDrawer(Gravity.RIGHT);
        } else {
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }
    }
}

package dh_kang.nozero.IntegratedClass;

import android.graphics.Paint;
import android.text.TextPaint;
import android.widget.TextView;

/**
 * Created by dh93 on 2016-12-02.
 */
public final class TextViewHelper {
    private static Paint mStaticMasurePaint;

    public static String shrinkWithWordUnit(TextView textView, String s, float maxWidth) {
        if (mStaticMasurePaint == null) mStaticMasurePaint = new TextPaint();

        mStaticMasurePaint.setTextSize(textView.getTextSize());
        mStaticMasurePaint.setTypeface(textView.getTypeface());
        String[] tokens = s.replace("\n", "").split(" ");
        String newStr = "";
        int currLineWidth = 0;

        for (String token : tokens) {
            if (currLineWidth + mStaticMasurePaint.measureText(" " + token) > maxWidth) {
                newStr += "\n" + token;
                currLineWidth = (int) mStaticMasurePaint.measureText(token);
            } else {
                if (newStr.equals("")) {
                    newStr = token;
                    currLineWidth = (int) mStaticMasurePaint.measureText(token);
                } else {
                    newStr += " " + token;
                    currLineWidth += mStaticMasurePaint.measureText(" " + token);
                }
            }
        }
        return newStr;
    }

    private TextViewHelper() {
    }
}
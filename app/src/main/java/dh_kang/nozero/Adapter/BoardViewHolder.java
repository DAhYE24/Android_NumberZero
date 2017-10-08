package dh_kang.nozero.Adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractSwipeableItemViewHolder;

import dh_kang.nozero.R;

/**
 * Created by dh93 on 2017-10-08.
 */

public class BoardViewHolder extends AbstractSwipeableItemViewHolder {
    private static final String TAG = "NOZERO_FINAL";      /* LOG TEST */
    private final float OPTIONS_AREA_PROPORTION = 0.5f;
    private final float REMOVE_ITEM_THRESHOLD = 0.6f;
    private View swipeableContainer, optionView1, optionView2, optionView3;
    public ImageView img_writerImg;
    public TextView txt_writerName, txt_writingTitle, txt_writingTime;
    private float lastSwipeAmount;

    public float getLastSwipeAmount() {
        return lastSwipeAmount;
    }

    /* Connect xml components with viewholder */
    public BoardViewHolder(View itemLayoutView) {
        super(itemLayoutView);
        swipeableContainer = itemLayoutView.findViewById(R.id.swipeable_container);
        optionView1 = itemLayoutView.findViewById(R.id.option_view_1);
        optionView2 = itemLayoutView.findViewById(R.id.option_view_2);
        optionView3 = itemLayoutView.findViewById(R.id.option_view_3);
        img_writerImg = (ImageView) itemLayoutView.findViewById(R.id.img_writerImg);
        txt_writerName = (TextView) itemLayoutView.findViewById(R.id.txt_writerName);
        txt_writingTitle = (TextView) itemLayoutView.findViewById(R.id.txt_writingTitle);
        txt_writingTime = (TextView) itemLayoutView.findViewById(R.id.txt_writingTime);
    }

    /* Container which has widgets */
    @Override
    public View getSwipeableContainerView() {
        Log.e(TAG, "getSwipeableContainerView 메서드 실행");
        return swipeableContainer;
    }

    /* Checking slide motion and act */
    @Override
    public void onSlideAmountUpdated(float horizontalAmount, float verticalAmount, boolean isSwiping) {
        Log.e(TAG, "onSlideAmountUpdated 메서드 실행");
        int itemWidth = itemView.getWidth();
        float optionItemWidth = itemWidth * OPTIONS_AREA_PROPORTION / 3;
        int offset = (int) (optionItemWidth + 0.5f);
        float p = Math.max(0, Math.min(OPTIONS_AREA_PROPORTION, -horizontalAmount)) / OPTIONS_AREA_PROPORTION;

        if (optionView1.getWidth() == 0) {
            setLayoutWidth(optionView1, (int) (optionItemWidth + 0.5f));
            setLayoutWidth(optionView2, (int) (optionItemWidth + 0.5f));
            setLayoutWidth(optionView3, (int) (optionItemWidth + 0.5f));
        }

        optionView1.setTranslationX(-(int) (p * optionItemWidth * 3 + 0.5f) + offset);
        optionView2.setTranslationX(-(int) (p * optionItemWidth * 2 + 0.5f) + offset);
        optionView3.setTranslationX(-(int) (p * optionItemWidth * 1 + 0.5f) + offset);

        if (horizontalAmount < (-REMOVE_ITEM_THRESHOLD)) {
            swipeableContainer.setVisibility(View.INVISIBLE);
            optionView1.setVisibility(View.INVISIBLE);
            optionView2.setVisibility(View.INVISIBLE);
            optionView3.setVisibility(View.INVISIBLE);
        } else {
            swipeableContainer.setVisibility(View.VISIBLE);
            optionView1.setVisibility(View.VISIBLE);
            optionView2.setVisibility(View.VISIBLE);
            optionView3.setVisibility(View.VISIBLE);
        }

        lastSwipeAmount = horizontalAmount;
    }

    /* Set list blank's width */
    private static void setLayoutWidth(View view, int width) {
        Log.e(TAG, "setLayoutWidth 메서드 실행");
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.width = width;
        view.setLayoutParams(lp);
    }
}

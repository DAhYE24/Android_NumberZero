package dh_kang.nozero.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultAction;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionDefault;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionMoveToSwipedDirection;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionRemoveItem;

import java.util.ArrayList;

import dh_kang.nozero.DataSet.BoardValues;
import dh_kang.nozero.R;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by dh93 on 2017-10-08.
 */

public class BoardAdapter extends RecyclerView.Adapter<BoardViewHolder> implements SwipeableItemAdapter<BoardViewHolder> {
    private static final String TAG = "NOZERO_FINAL";      /* LOG TEST */
    private final float OPTIONS_AREA_PROPORTION = 0.5f;  /* Declare java parameters */
    private final float REMOVE_ITEM_THRESHOLD = 0.6f;
    private Context context;
    private ArrayList<BoardValues> boardValues;

    interface Swipeable extends SwipeableItemConstants {
    }

    /* 어댑터에 데이터 입력 */
    public BoardAdapter(Context context) {
        setHasStableIds(true);  // 어댑터에 연결되는 각 아이템 항목에 대해 고유한 아이디를 부여하겠다는 메소드
        boardValues = new ArrayList<>();
        this.context = context;
        for (int i = 0; i < 10; i++)
            boardValues.add(new BoardValues(i, "테스트 제목 " + i, "테스트 유저 " + i, "http://cfile24.uf.tistory.com/image/257A5846595A20D0064977", "테스트 시간 " + i));
    }

    @Override
    public long getItemId(int position) {
        return boardValues.get(position).getBoardIdx();
    }

    @Override
    public BoardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_board, parent, false);
        return new BoardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BoardViewHolder holder, int position) {
        holder.txt_writerName.setText(boardValues.get(position).getUserNickname());
        holder.txt_writingTitle.setText(boardValues.get(position).getBoardTitle());
        holder.txt_writingTime.setText(boardValues.get(position).getUpdateAt());
        Glide.with(context).load(boardValues.get(position).getProfileThumbnailUrl())
                .bitmapTransform(new CropCircleTransformation(new CustomBitmapPool()))
                .override(150, 150).into(holder.img_writerImg);

        BoardValues boardValues = this.boardValues.get(position);

        // set swiping properties
        holder.setMaxLeftSwipeAmount(-OPTIONS_AREA_PROPORTION);
        holder.setMaxRightSwipeAmount(0);
        holder.setSwipeItemHorizontalSlideAmount(boardValues.isPinned() ? -OPTIONS_AREA_PROPORTION : 0);
    }

    @Override
    public int getItemCount() {
        return boardValues.size();
    }

    @Override
    public SwipeResultAction onSwipeItem(BoardViewHolder holder, int position, int result) {
        if (result == Swipeable.RESULT_SWIPED_LEFT) {
            if (holder.getLastSwipeAmount() < (-REMOVE_ITEM_THRESHOLD)) {
                return new SwipeLeftRemoveAction(this, position);
            } else {
                return new SwipeLeftPinningAction(this, position);
            }
        } else {
            return new SwipeCancelAction(this, position);
        }
    }

    @Override
    public int onGetSwipeReactionType(BoardViewHolder holder, int position, int x, int y) {
        return Swipeable.REACTION_CAN_SWIPE_LEFT;
    }

    @Override
    public void onSetSwipeBackground(BoardViewHolder holder, int position, int type) {
        if (type == Swipeable.DRAWABLE_SWIPE_LEFT_BACKGROUND) {
            holder.itemView.setBackgroundColor(0xffff6666);
        }
    }

    static class SwipeLeftRemoveAction extends SwipeResultActionRemoveItem {
        BoardAdapter adapter;
        int position;

        public SwipeLeftRemoveAction(BoardAdapter adapter, int position) {
            this.adapter = adapter;
            this.position = position;
        }

        @Override
        protected void onPerformAction() {
            adapter.boardValues.remove(position);
            adapter.notifyItemRemoved(position);
        }
    }

    static class SwipeLeftPinningAction extends SwipeResultActionMoveToSwipedDirection {
        BoardAdapter adapter;
        int position;

        public SwipeLeftPinningAction(BoardAdapter adapter, int position) {
            this.adapter = adapter;
            this.position = position;
        }

        @Override
        protected void onPerformAction() {
            adapter.boardValues.get(position).setPinned(true);
            adapter.notifyItemChanged(position);
        }
    }


    static class SwipeCancelAction extends SwipeResultActionDefault {
        BoardAdapter adapter;
        int position;

        public SwipeCancelAction(BoardAdapter adapter, int position) {
            this.adapter = adapter;
            this.position = position;
        }

        @Override
        protected void onPerformAction() {
            adapter.boardValues.get(position).setPinned(false);
            adapter.notifyItemChanged(position);
        }
    }
}

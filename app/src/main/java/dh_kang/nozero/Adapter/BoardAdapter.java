package dh_kang.nozero.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultAction;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionDefault;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionMoveToSwipedDirection;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionRemoveItem;

import java.util.ArrayList;

import dh_kang.nozero.DataSet.BoardValues;
import dh_kang.nozero.R;

/**
 * Created by dh93 on 2017-10-08.
 */

public class BoardAdapter extends RecyclerView.Adapter<BoardViewHolder> implements SwipeableItemAdapter<BoardViewHolder> {
    interface Swipeable extends SwipeableItemConstants {
    }

    ArrayList<BoardValues> boardValues;
    final float OPTIONS_AREA_PROPORTION = 0.5f;
    final float REMOVE_ITEM_THRESHOLD = 0.6f;

    public BoardAdapter() {
        setHasStableIds(true);
        boardValues = new ArrayList<>();
        for (int i = 0; i < 10; i++) boardValues.add(new BoardValues(i, "1", "2", "3", "4"));
    }

    @Override
    public long getItemId(int position) {
        return boardValues.get(position).id;
    }

    @Override
    public BoardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_board, parent, false);
        return new BoardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BoardViewHolder holder, int position) {
        holder.text1.setText("item " + position);

        BoardValues item = boardValues.get(position);

        // set swiping properties
        holder.setMaxLeftSwipeAmount(-OPTIONS_AREA_PROPORTION);
        holder.setMaxRightSwipeAmount(0);
        holder.setSwipeItemHorizontalSlideAmount(item.pinned ? -OPTIONS_AREA_PROPORTION : 0);
    }

    @Override
    public int getItemCount() {
        return boardValues.size();
    }

    @Override
    public SwipeResultAction onSwipeItem(BoardViewHolder holder, int position, int result) {
        if (result == Swipeable.RESULT_SWIPED_LEFT) {
            if (holder.lastSwipeAmount < (-REMOVE_ITEM_THRESHOLD)) {
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
            adapter.boardValues.get(position).pinned = true;
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
            adapter.boardValues.get(position).pinned = false;
            adapter.notifyItemChanged(position);
        }
    }
}

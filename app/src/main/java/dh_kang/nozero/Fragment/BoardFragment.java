package dh_kang.nozero.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.h6ah4i.android.widget.advrecyclerview.animator.SwipeDismissItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;

import dh_kang.nozero.Activity.acti_ActiBoard;
import dh_kang.nozero.Adapter.BoardAdapter;
import dh_kang.nozero.R;

/**
 * Created by dh93 on 2016-10-14.
 */
public class BoardFragment extends Fragment {
    private static final String TAG = "NOZERO_FINAL";      /* LOG TEST */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /* Declare xml components */
        View view = inflater.inflate(R.layout.frag_board, container, false);
        RecyclerView list_board;
        EditText edit_writingEngin;
        ImageButton btn_searchWriting, btn_write;

        /* Init widgets*/
        edit_writingEngin = (EditText) view.findViewById(R.id.edit_writingEngin);
        list_board = (RecyclerView) view.findViewById(R.id.list_board);
        btn_searchWriting = (ImageButton) view.findViewById(R.id.btn_searchWriting);
        btn_write = (ImageButton) view.findViewById(R.id.btn_write);


        /* RecyclerView(Custom Library) */
        RecyclerViewSwipeManager swipeManager = new RecyclerViewSwipeManager();
        list_board.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        list_board.setAdapter(swipeManager.createWrappedAdapter(new BoardAdapter(getContext())));
        list_board.setItemAnimator(new SwipeDismissItemAnimator());
        swipeManager.attachRecyclerView(list_board);
//        list_board.addItemDecoration(new SimpleListDividerDecorator(ContextCompat.getDrawable(this, R.drawable.list_divider), true));

        return view;
    }
}
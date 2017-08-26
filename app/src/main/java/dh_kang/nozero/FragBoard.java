package dh_kang.nozero;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by dh93 on 2016-10-14.
 */
public class FragBoard extends Fragment {
    /* 로그 테스트 */
    private static final String TAG = "NOZERO_FINAL";

    /* XML 선언 */
    ImageView imgQnA, imgTip;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View v = inflater.inflate( R.layout.frag_board, container, false );

        /* XML 초기화 */
        imgQnA = (ImageView)v.findViewById(R.id.fb_imgQnA);
        imgTip = (ImageView)v.findViewById(R.id.fb_imgTip);

        /* QnA 게시판 */
        imgQnA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ActiBoard.class);
                startActivity(i);
            }
        });

        /* TIP 게시판 */
        imgTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ActiBoard.class);
                startActivity(i);
            }
        });

        return v;
    }
}
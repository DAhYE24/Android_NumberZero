package dh_kang.nozero;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by dh93 on 2016-11-28.
 */
public class DialContact extends Dialog {
    /* 로그 테스트 */
    private static final String TAG = "NOZERO_FINAL";

    /* XML 선언 */
    Button dc_btnOk;

    /* 필수 선언 */
    public DialContact(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dial_contact);

        /* 초기화 */
        dc_btnOk = (Button)findViewById(R.id.dc_btnOk);

        /* 확인버튼 누르면 종료 */
        dc_btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}

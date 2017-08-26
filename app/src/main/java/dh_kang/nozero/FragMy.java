package dh_kang.nozero;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by dh93 on 2016-10-14.
 */
public class FragMy extends Fragment {
    /* 로그 테스트 */
    private static final String TAG = "NOZERO_FINAL";

    /* XML 선언 */
    TextView fm_txtId, fm_txtEmail; // 유저 아이디
    ImageButton fm_btnImg; // 사진 설정
    Button fm_btnWriting, fm_btnModify; // 내가 쓴 글, 정보 수정
    ImageView fm_imgUser; // 유저 이미지
    RelativeLayout fm_lyBox, fm_lyContact, fm_lyTutorial, fm_lyWithdraw; // 향수보관함, 콘텍트, 튜토리얼, 탈퇴

    /* JAVA 선언 */;
    SharedPreferences userInfo;
    String fm_picPath = null; // 사진경로(기본값)
    private static int RESULT_LOAD_IMAGE = 1; // 이미지 앨범에서 가져오기
    DialBox boxDialog;
    DialContact contactDialog;
    DialIcon iconDialog;
    DialJoin infoDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState ) {
        View v = inflater.inflate(R.layout.frag_my, container, false);

        /* XML 초기화 */
        fm_txtId = (TextView) v.findViewById(R.id.fm_txtId);
        fm_txtEmail = (TextView) v.findViewById(R.id.fm_txtEmail);
        fm_imgUser = (ImageView) v.findViewById(R.id.fm_imgUser);
        fm_btnImg = (ImageButton) v.findViewById(R.id.fm_btnImg);
        fm_btnWriting = (Button) v.findViewById(R.id.fm_btnWriting);
        fm_btnModify = (Button) v.findViewById(R.id.fm_btnModify);
        fm_lyBox = (RelativeLayout) v.findViewById(R.id.fm_lyBox);
        fm_lyContact = (RelativeLayout) v.findViewById(R.id.fm_lyContact);
        fm_lyTutorial = (RelativeLayout) v.findViewById(R.id.fm_lyTutorial);
        fm_lyWithdraw = (RelativeLayout) v.findViewById(R.id.fm_lyWithdraw);

        /* 텍스트 세팅(아이디, 이메일) */
        userInfo = getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        fm_txtId.setText(userInfo.getString("userId", ""));
        fm_txtEmail.setText(userInfo.getString("userEmail", ""));

        /* 유저 이미지 불러오기*/
        loadUserImage();

        /* 향수 보관함 */
        fm_lyBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boxDialog = new DialBox(getActivity());
                boxDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                boxDialog.show();
            }
        });

        /* 오류신고 및 문의사항 */
        fm_lyContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactDialog = new DialContact(getActivity());
                contactDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                contactDialog.show();
            }
        });

        /* 탈퇴 버튼 선택 */
        fm_lyWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = userWithdraw();
                dialog.show();
            }
        });

        /* 정보 수정 버튼 선택 */
        fm_btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInfo = getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = userInfo.edit();
                editor.putBoolean("joinCheck", false);// 수정하는 경우
                editor.commit();

                infoDialog = new DialJoin(getActivity());
                infoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                infoDialog.show();
            }
        });

        /* 아이콘 선택 */
        fm_btnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iconDialog = new DialIcon(getActivity());
                iconDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                iconDialog.show();

//                /* 앨범에서 이미지 가져오는 방식 */
//                Intent intent = new Intent(Intent.ACTION_PICK,
//                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                getActivity().startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });

        /* 서버 이미지 전송 테스트 */
        fm_lyTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* 앨범에서 이미지 가져오는 방식 */
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                getActivity().startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });

        return v;
    }

    public void loadUserImage() {
        userInfo = getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        fm_picPath = userInfo.getString("userPicPath", ""); // 저장된 이미지 값이 있으면 불러오기
        if(fm_picPath.equals("")) fm_picPath = "http://i.imgur.com/DtlZIUr.png"; // 저장된 거 없으면 기본 이미지 설정
        Glide.with(getContext()).load(fm_picPath).override(200, 200).into(fm_imgUser); // Glide로 이미지 경로 & url 연결
    }

    private AlertDialog userWithdraw() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("회원탈퇴");
        builder.setMessage("넘버제로를 탈퇴하시겠습니까?\n탈퇴시 모든 정보가 삭제됩니다");

        builder.setPositiveButton("예", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 탈퇴하도록, 모든 sharedPreference 삭제 + 어플 다시 실행
            }
        });

        builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        return dialog;
    }
}
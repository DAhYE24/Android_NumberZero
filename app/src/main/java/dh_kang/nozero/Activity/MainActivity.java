package dh_kang.nozero.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import dh_kang.nozero.Fragment.FragBoard;
import dh_kang.nozero.Fragment.FragInfo;
import dh_kang.nozero.Fragment.FragLogin;
import dh_kang.nozero.Fragment.FragMain;
import dh_kang.nozero.Fragment.FragMy;
import dh_kang.nozero.Fragment.FragSearch;
import dh_kang.nozero.IntegratedClass.DisableButtonShift;
import dh_kang.nozero.R;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "NOZERO_FINAL";   /* LOG TEST */
    private BottomNavigationView bottomNavigationView; /* Declare xml components */
    Fragment fragment = null;
    private FragmentManager fragmentManager;
//    FragmentTransaction transaction = null;


    /* JAVA 선언 */
    Boolean userCheck = false; // 유저 체크, false일 때는 로그인 화면
    SharedPreferences userInfo;
    public static Context mContext;

    /* 유저 사진 */
    private static int RESULT_LOAD_IMAGE = 1;
    String picturePath;

    /* 서버 이미지 테스트 */
    int serverResponseCode = 0;
    ProgressDialog dialog = null;
    String upLoadServerUri = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.page_main);

        /* Init BottomNavigationView */
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().findItem(R.id.btn_action_home).setChecked(true);
        bottomNavigationView.getMenu().findItem(R.id.btn_action_info).setCheckable(false); // TODO : 체크해보기 https://stackoverflow.com/questions/44498537/set-no-item-pre-selected-in-bottom-navigation-view
        DisableButtonShift disableButtonShift = new DisableButtonShift();
        disableButtonShift.disableShiftMode(bottomNavigationView);  // Fix bottom buttons

        /* Fragment Setting */
        fragmentManager = getSupportFragmentManager();
        fragment = new FragMain();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.am_lyFrag, fragment).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                bottomNavigationView.getMenu().findItem(R.id.btn_action_info).setCheckable(true);
                bottomNavigationView.getMenu().findItem(R.id.btn_action_home).setChecked(false);
                switch (item.getItemId()) {
                    case R.id.btn_action_info:
                        fragment = new FragInfo();
                        break;
                    case R.id.btn_action_search:
                        fragment = new FragSearch();
                        break;
                    case R.id.btn_action_home:
                        bottomNavigationView.getMenu().findItem(R.id.btn_action_home).setChecked(true);
                        fragment = new FragMain();
                        break;
                    case R.id.btn_action_talk:
                        fragment = new FragBoard();
                        break;
                    case R.id.btn_action_user:
                        fragment = new FragMy();
                        break;
                }
                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.am_lyFrag, fragment).commit();
                return true;
            }
        });

        /* Context 저장 */
        mContext = this;

        /* userInfo 불러오기 */
        userInfo = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        userCheck = userInfo.getBoolean("userCheck", false);

//        /* 프레그먼트 등록 */
//        firstPageFragment();
    }
//
//    private void firstPageFragment() {
//        Fragment fragment = new FragMain();
//
//        /* 첫 번째 프레그먼트 보여주기 */
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction(); //전환
//        transaction.add(R.id.am_lyFrag, fragment);
//        transaction.commit();
//    }


    public void ChangeFragment(View v) {
//        Fragment fg = null;
//        switch (v.getId()) { //버튼 별 프레그먼트 전환
//            case R.id.am_btnInfo: // 향수정보 버튼
//                /* 버튼 클릭 시 변화 */
//                am_btnInfo.setBackgroundResource(R.drawable.btn_clicked);
//                am_btnSearch.setBackgroundResource(R.drawable.btn_white_border);
//                am_btnMain.setBackgroundResource(R.drawable.btn_white_border);
//                am_btnBoard.setBackgroundResource(R.drawable.btn_white_border);
//                am_btnMy.setBackgroundResource(R.drawable.btn_white_border);
//
//                fg = new FragInfo();
//                break;
//            case R.id.am_btnSearch: // 향수검색 버튼
//                /* 버튼 클릭 시 변화 */
//                am_btnInfo.setBackgroundResource(R.drawable.btn_white_border);
//                am_btnSearch.setBackgroundResource(R.drawable.btn_clicked);
//                am_btnMain.setBackgroundResource(R.drawable.btn_white_border);
//                am_btnBoard.setBackgroundResource(R.drawable.btn_white_border);
//                am_btnMy.setBackgroundResource(R.drawable.btn_white_border);
//
//                fg = new FragSearch();
//                break;
//            case R.id.am_btnMain: // 메인화면 버튼
//                /* 버튼 클릭 시 변화 */
//                am_btnInfo.setBackgroundResource(R.drawable.btn_white_border);
//                am_btnSearch.setBackgroundResource(R.drawable.btn_white_border);
//                am_btnMain.setBackgroundResource(R.drawable.btn_clicked);
//                am_btnBoard.setBackgroundResource(R.drawable.btn_white_border);
//                am_btnMy.setBackgroundResource(R.drawable.btn_white_border);
//
//                if(userCheck == true) fg = new FragMain();
//                else fg = new FragLogin();
//                break;
//            case R.id.am_btnBoard: // 향수토크 버튼
//                /* 버튼 클릭 시 변화 */
//                am_btnInfo.setBackgroundResource(R.drawable.btn_white_border);
//                am_btnSearch.setBackgroundResource(R.drawable.btn_white_border);
//                am_btnMain.setBackgroundResource(R.drawable.btn_white_border);
//                am_btnBoard.setBackgroundResource(R.drawable.btn_clicked);
//                am_btnMy.setBackgroundResource(R.drawable.btn_white_border);
//
//                if(userCheck == true)   fg = new FragBoard();
//                else    fg = new FragLogin();
//                break;
//            case R.id.am_btnMy: // MY향수 버튼
//                /* 버튼 클릭 시 변화 */
//                am_btnInfo.setBackgroundResource(R.drawable.btn_white_border);
//                am_btnSearch.setBackgroundResource(R.drawable.btn_white_border);
//                am_btnMain.setBackgroundResource(R.drawable.btn_white_border);
//                am_btnBoard.setBackgroundResource(R.drawable.btn_white_border);
//                am_btnMy.setBackgroundResource(R.drawable.btn_clicked);
//
//                if(userCheck == true) fg = new FragMy();
//                else fg = new FragLogin();
//                break;
//        }
//
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction(); //전환
//        transaction.replace(R.id.am_lyFrag, fg);
//        transaction.commit();
    }

    /* FragMy에서 사지의 값 받아오는 부분 */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri imageUri = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(imageUri,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
//            SharedPreferences.Editor editor = userInfo.edit();
//            editor.putString("userPicPath", picturePath);
//            editor.commit();

            upLoadServerUri = "http://pridena1030.cafe24.com/NoZ_upload.php";

            dialog = ProgressDialog.show(MainActivity.this, "", "Uploading file...", true);
            new Thread(new Runnable() {
                public void run() {
                    uploadFile(picturePath);
                }
            }).start();
        }
    }

    private int uploadFile(String sourceFileUri) {
        String fileName = sourceFileUri; // 불러온 URi(uploadFilePath + "" + uploadFileName)

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {
            dialog.dismiss();
            Log.e("uploadFile", "Source File not exist :"
                    + picturePath);

            return 0;
        } else {
            try {
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL("file:" + upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\"; filename=" + fileName + "" + lineEnd);
                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if (serverResponseCode == 200) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(MainActivity.this, "File Upload Complete.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();
            } catch (MalformedURLException ex) {
                dialog.dismiss();
                ex.printStackTrace();
                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {
                dialog.dismiss();
                e.printStackTrace();
            }
            dialog.dismiss();
            return serverResponseCode;
        } // End else block
    }
}

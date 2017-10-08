package dh_kang.nozero.Activity;

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
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import dh_kang.nozero.Fragment.BoardFragment;
import dh_kang.nozero.Fragment.FragInfo;
import dh_kang.nozero.Fragment.FragMain;
import dh_kang.nozero.Fragment.FragMy;
import dh_kang.nozero.Fragment.SearchFragment;
import dh_kang.nozero.IntegratedClass.DisableButtonShift;
import dh_kang.nozero.R;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "NOZERO_FINAL";   /* LOG TEST */
    private BottomNavigationView bottomNavigationView; /* Declare xml components */
    private Fragment fragment = null;   /* Declare java components */
    private FragmentManager fragmentManager;

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
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
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
        final FragmentTransaction transaction = fragmentManager.beginTransaction(); // TODO : final 역할 재확인
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
                        fragment = new SearchFragment();
                        break;
                    case R.id.btn_action_home:
                        bottomNavigationView.getMenu().findItem(R.id.btn_action_home).setChecked(true);
                        fragment = new FragMain();
                        break;
                    case R.id.btn_action_talk:
                        fragment = new BoardFragment();
                        break;
                    case R.id.btn_action_user:
                        fragment = new FragMy();
                        break;
                }
                final FragmentTransaction transaction = fragmentManager.beginTransaction();
//                transaction.setCustomAnimations(R.anim.anim_slide_in, R.anim.anim_slide_out);   // Animation
                transaction.replace(R.id.am_lyFrag, fragment).commit();
                return true;
            }
        });

        /* Context 저장 */
        mContext = this;

        /* userInfo 불러오기 */
//        userInfo = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
//        userCheck = userInfo.getBoolean("userCheck", false);
    }

    /* FragMy에서 사진의 값 받아오는 부분 */
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

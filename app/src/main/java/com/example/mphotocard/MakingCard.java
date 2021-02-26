package com.example.mphotocard;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.network.storage.ImageUploadResponse;
import com.kakao.util.KakaoParameterException;
import com.kakao.util.helper.log.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MakingCard extends AppCompatActivity implements View.OnTouchListener {
//public class MakingCard extends AppCompatActivity  {


    InputMethodManager imm;  //키보드 내리기
    private int pos;

    private ImageView imageView3; //배경이미지
    private TextView txt_making; //내용
    private EditText edt_Title; //제목입력

    private Button btn_edt_txt; // 내용편집 버튼
    private Button btn_edt_img; //이미지 편집 버튼
    private Button btn_save; //저장 버튼

    private KakaoLink kakaoLink;
    private KakaoTalkLinkMessageBuilder mKakaoTalkLinkMessageBuilder;

    private Button btn_kakao; //카카오공유버튼


    private TextView textView1;

    final static int REQUEST_CODE_TEXT = 1;
    final static int REQUEST_CODE_IMG = 2;

    private String uri;
    private String content;
    int txtColor = Color.WHITE;
    int txtGravity = Gravity.CENTER_HORIZONTAL;
    float txtSize = 20f;

    String title;

    float txtShadow_rd = 0f;
    float txtShadow_x = 0f;
    float txtShadow_y = 0f;
    int txtShadow_color = 0;

    float txt_x = 0f;
    float txt_y = 0f;

    private float oldXvalue;
    private float oldYvalue;


    private ArrayList<MyCardItem> items = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.making_card);


        //권한 체크 해주는 팝업창
        TedPermission.with(MakingCard.this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage("카메라 권한이 필요합니다.")  //카메라 권한 팝업떳을때 나타나는 메세지
                .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();


        Bundle extras = getIntent().getExtras();

        imageView3 = findViewById(R.id.imageView3);
        txt_making = findViewById(R.id.txt_making);
        edt_Title = findViewById(R.id.edt_Title);

        pos = extras.getInt("pos");

        content = extras.getString("content", "");
        //Uri uri = Uri.parse(extras.getString("uri"));
        uri = extras.getString("resId", "");
        txtColor = extras.getInt("txtColor", Color.BLACK);
        txtGravity = extras.getInt("txtGravity", Gravity.CENTER);
        txtSize = extras.getFloat("txtSize", 20f);

        title = extras.getString("title", null);

        txtShadow_rd = extras.getFloat("txtShadow_rd", 0f);
        txtShadow_x = extras.getFloat("txtShadow_x", 0f);
        txtShadow_y = extras.getFloat("txtShadow_y", 0f);
        txtShadow_color = extras.getInt("txtShadow_color", 0);

        txt_x = extras.getFloat("txt_x", 279f);
        txt_y = extras.getFloat("txt_y", 599f);


        txt_making.setShadowLayer(txtShadow_rd, txtShadow_x, txtShadow_y, txtShadow_color);

        Log.i(this.getClass().getName(), "이것은" + uri + "샘플에서넘어오는이미지경로");


        imageView3.setImageURI(Uri.parse(uri));
        txt_making.setText(content);
        txt_making.setTextColor(txtColor);
        txt_making.setGravity(txtGravity);
        txt_making.setTextSize(TypedValue.COMPLEX_UNIT_PX, txtSize);
        txt_making.setX(txt_x - 279f);
        txt_making.setY(txt_y - 599f);
//        txt_making.setX(txt_x );
//        txt_making.setY(txt_y );


        edt_Title.setText(title);


        //int fontNum = getSharedPreferences("Font", MODE_PRIVATE).getInt("font", 0);
        SharedPreferences j_pref = getSharedPreferences("j_pref", MODE_PRIVATE);
        String json = j_pref.getString("list", "");
        try {
            JSONArray jsonArray = new JSONArray(json);
            JSONObject jsonObject = jsonArray.getJSONObject(pos);
            int txt_font = jsonObject.getInt("txt_font");
            switch (txt_font) {
                case 0:
                    txt_making.setTypeface(Typeface.DEFAULT);
                    break;

                case 1:
                    txt_making.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/batang.ttf"));

                    break;

                case 2:
                    txt_making.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/utoimage_son_guelssi.ttf"));

                    break;

                case 3:
                    txt_making.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/ss_flower_road.ttf"));

                    break;

                case 4:
                    txt_making.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/seoul_hangang_b.ttf"));

                    break;

                case 5:
                    txt_making.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/tlab_shin_young_bok.otf"));

                    break;

                case 6:
                    txt_making.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/bmyeonsung.ttf"));


                    break;

                case 7:
                    txt_making.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/jalnan.ttf"));

                    break;

                case 8:
                    txt_making.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/jeongum.ttf"));


                    break;

                case 9:
                    txt_making.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/jejuhallasan.ttf"));

                    break;

                case 10:
                    txt_making.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/tvn_bold.ttf"));


                    break;


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        try {
//            kakaoLink = KakaoLink.getKakaoLink(getApplicationContext());
//            mKakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
//        } catch (KakaoParameterException e) {
//            e.getMessage();
//        }


        //텍스트뷰 드래그드랍
        txt_making.setOnTouchListener(this);


        //텍스트 편집 버튼
        btn_edt_txt =

                findViewById(R.id.btn_edt_txt);
        btn_edt_txt.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Edit_Text.class);


                //텍스트뷰(txt_making)에 있는 글 담아서 edit_text 액티비티로 보내기
                intent.putExtra("key", txt_making.getText().toString());
                intent.putExtra("txtColor", txt_making.getCurrentTextColor());
                intent.putExtra("txtGravity", txt_making.getGravity());
                intent.putExtra("txtSize", txt_making.getTextSize());

//                intent.putExtra("X",txt_making.getX()+oldXvalue-(imageView3.getWidth()/2));
//                intent.putExtra("Y",txt_making.getY()+oldYvalue-(imageView3.getHeight()/2));
//                intent.putExtra("x", txt_making.getX());
//                intent.putExtra("y", txt_making.getY());
//                Log.d("좌표값보낸","x"+txt_making.getX()+"랄"+imageView3.getWidth()+"y"+txt_making.getY()+"랄"+imageView3.getHeight());
//                Log.d("뷰크기","x"+txt_making.getWidth()+"반"+(txt_making.getWidth()/2)+"랄"+imageView3.getWidth()+"y"+txt_making.getHeight()+"반"+(txt_making.getHeight()/2)+"랄"+imageView3.getHeight());


//                intent.putExtra("X",imageView3.getX()+(txt_making.getX())-(imageView3.getWidth()/2));
//                //v.setX(v.getX() + (event.getX()) - (v.getWidth() / 2));
//                intent.putExtra("Y",imageView3.getY()+(txt_making.getY())-(imageView3.getHeight()/2));


                //그림자 정보
                float txtShadow_rd = txt_making.getShadowRadius();
                float txtShadow_x = txt_making.getShadowDx();
                float txtShadow_y = txt_making.getShadowDy();
                int txtShadow_color = txt_making.getShadowColor();

                intent.putExtra("txtShadow_rd", txtShadow_rd);
                intent.putExtra("txtShadow_x", txtShadow_x);
                intent.putExtra("txtShadow_y", txtShadow_y);
                intent.putExtra("txtShadow_color", txtShadow_color);

                //int position = pos;
                intent.putExtra("pos", pos);


                //미리보기용 이미지도 보내기
                intent.putExtra("ex_img", uri);
//
//                SharedPreferences pref_font = getSharedPreferences("Font", MODE_PRIVATE);
//
//                SharedPreferences.Editor editor = pref_font.edit();
//                editor.putInt("font", 0);
//                editor.apply();


                startActivityForResult(intent, REQUEST_CODE_TEXT);
            }
        });

        //이미지 편집 버튼
        btn_edt_img =

                findViewById(R.id.btn_edt_img);
        btn_edt_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditImage.class);
                //Intent intent = new Intent(getApplicationContext(), EditImage_2.class);

                startActivityForResult(intent, REQUEST_CODE_IMG);
            }
        });

        //저장 버튼
        btn_save = findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {


                //=====뷰캡쳐 이미지로 저장
                //captureAndSave();


//                //============JSON테스트---대실패
//                sendArray();
//                Intent intent = new Intent(getApplicationContext(), MyCardList.class);
//
//                startActivity(intent);

                //SharedPreferences 로 저장
                //               saveDataForShared();

                jsonDataSetting();

                Intent intent = new Intent(getApplicationContext(), MyCardList.class);

                startActivity(intent);
                finish();

            }

        });

        btn_kakao = findViewById(R.id.btn_kaka);
        btn_kakao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //shareBitmap(screenShot(image),"myimage");

                shareKakao();

//
//                Intent sendIntent = new Intent(Intent.ACTION_SEND);
//                //sendIntent.setClassName("com.android.mms", "com.android.mms.ui.ComposeMessageActivity");
//                sendIntent.setClassName("com.example.mphotocard", "com.example.mphotocard.MakingCard");
//                sendIntent.putExtra("sms_body", "some text");
//                sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(uri));
//                sendIntent.setType("image/png");
//                startActivity(sendIntent);

//                // OS 콘텐츠 공유를 통한 Image를 전송할 때
//                //Intent intent = new Intent(Intent.ACTION_SEND);
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_SEND);
//                intent.setType("image/*");
//                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(uri));
//                intent.setPackage("com.kakao.talk");
//                startActivity(intent);

//                try {
//                    KakaoLink kakaoLink = KakaoLink.getKakaoLink(getApplicationContext());
//                    KakaoTalkLinkMessageBuilder messageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
//                    messageBuilder.addImage(RelativeLayout.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
//                } catch (KakaoParameterException e) {
//                    e.printStackTrace();
//                }
            }
        });


    } //End of onCreate


    private void shareKakao() {
        //final RelativeLayout capture = findViewById(R.id.ral);//캡쳐할영역
        //screenShot(capture);
        //shareBitmap(screenShot(capture), "myimage");


//        //카카오링크 이미지 업로드
//        File imageFile = new File("path/of/image/file");
//
//        KakaoLinkService.getInstance().uploadImage(this, false, imageFile, new ResponseCallback<ImageUploadResponse>() {
//            @Override
//            public void onFailure(ErrorResult errorResult) {
//                Logger.e(errorResult.toString());
//            }
//
//            @Override
//            public void onSuccess(ImageUploadResponse result) {
//                Logger.d(result.getImageUrl());
//            }
//        });
//
//        final RelativeLayout capture = findViewById(R.id.ral);//캡쳐할영역
//        capture.buildDrawingCache();
//        capture.setDrawingCacheEnabled(true);
//        Bitmap bitmap = capture.getDrawingCache();
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG,100, stream);
//        byte[] byteArray = stream.toByteArray();
//
//        final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra(Intent.EXTRA_STREAM, byteArray);
//        intent.setType("image/png");
//        intent.setPackage("com.kakao.talk");
//        startActivity(intent);


        //                //이미지파일을 비트맵변수(sendBitmap)에 담음
//                Bitmap sendBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.sample);
//                //비트맵 변수에 담은 이미지파일을 바이트어레이로 변경
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                sendBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                byte[] byteArray = stream.toByteArray();
//
//                //Intent intent = new Intent(Sample.this, MakingCard.class);
//                Intent intent = new Intent(getApplicationContext(), MakingCard.class);
//
//                intent.putExtra("key","오늘하루도 행복하세요.");
//
//                //바이트어레이 부가데이터로 저장
//                intent.putExtra("image",byteArray);
//
//                //액티비티 이동
//                startActivity(intent);


        final RelativeLayout capture = findViewById(R.id.ral);//캡쳐할영역
        capture.buildDrawingCache();
        capture.setDrawingCacheEnabled(true);
        Bitmap bitmap = capture.getDrawingCache();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "uri", null);
        Uri uri_1 = Uri.parse(path);

        MediaScannerConnection.scanFile(MakingCard.this, //앨범에 사진을 보여주기 위해 Scan을 합니다.
                new String[]{uri_1.getPath()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                    }
                });


        // OS 콘텐츠 공유를 통한 Image를 전송할 때
        //Intent intent = new Intent(Intent.ACTION_SEND);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(uri_1.toString()));
        intent.setPackage("com.kakao.talk");
        startActivity(intent);


    }


    private void captureAndSave() {


        //=====뷰캡쳐 이미지로 저장
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PhotoCard";

        final RelativeLayout capture = findViewById(R.id.ral);//캡쳐할영역

        File file = new File(path);

        if (!file.exists()) {
            file.mkdirs();
            //Toast.makeText(getApplicationContext(), "폴더가 생성되었습니다.", Toast.LENGTH_SHORT).show();
        }

        SimpleDateFormat day = new SimpleDateFormat("yyyyMMddHHmmss");

        Date date = new Date();

        capture.buildDrawingCache();
        Bitmap captureview = capture.getDrawingCache();
        FileOutputStream fos = null;

        try {

            fos = new FileOutputStream(path + "/Card" + day.format(date) + ".jpeg");

            captureview.compress(Bitmap.CompressFormat.JPEG, 100, fos);

            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path + "/Card" + day.format(date) + ".JPEG")));

            String mPath = "file://" + path + "/Card" + day.format(date) + ".JPEG";
            Log.e("캡쳐경로", "" + mPath);

            //Toast.makeText(getApplicationContext(), "저장완료", Toast.LENGTH_SHORT).show();

            fos.flush();

            fos.close();

            capture.destroyDrawingCache();

        } catch (FileNotFoundException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }


//============스크린샷 ==========================================
//    private void takeScreenshot() {
//        Date now = new Date();
//        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
//
//        try {
//            // image naming and path  to include sd card  appending name you choose for file
//            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";
//
//            // create bitmap screen capture
//            View v1 = getWindow().getDecorView().getRootView();
//            v1.setDrawingCacheEnabled(true);
//            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
//            v1.setDrawingCacheEnabled(false);
//
//            File imageFile = new File(mPath);
//
//            FileOutputStream outputStream = new FileOutputStream(imageFile);
//            int quality = 100;
//            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
//            outputStream.flush();
//            outputStream.close();
//
//            openScreenshot(imageFile);
//        } catch (Throwable e) {
//            // Several error may come out with file handling or DOM
//            e.printStackTrace();
//        }
//    }
//
//    //최근 생성 된 이미지를 여는 방법
//    private void openScreenshot(File imageFile) {
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_VIEW);
//        Uri uri = Uri.fromFile(imageFile);
//        intent.setDataAndType(uri, "image/*");
//        startActivity(intent);
//    }
//============스크린샷 ==========================================


//============공유 ==========================================

//    public Bitmap screenShot(View view) {
//        final RelativeLayout capture = findViewById(R.id.ral);//캡쳐할영역
//        capture.buildDrawingCache();
//        Bitmap captureview = capture.getDrawingCache();
//
//        return captureview;
//    }
//
//
//    //////// this method share your image
//    private void shareBitmap(Bitmap bitmap, String fileName) {
//        try {
//            File file = new File(getApplicationContext().getCacheDir(), fileName + ".png");
//            FileOutputStream fOut = new FileOutputStream(file);
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
//            fOut.flush();
//            fOut.close();
//            file.setReadable(true, false);
//
//            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
//            intent.setType("image/png");
//            intent.setPackage("com.kakao.talk");
//            startActivity(intent);
//
//
//            // OS 콘텐츠 공유를 통한 Image를 전송할 때
////                //Intent intent = new Intent(Intent.ACTION_SEND);
////                Intent intent = new Intent();
////                intent.setAction(Intent.ACTION_SEND);
////                intent.setType("image/*");
////                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(uri));
////                intent.setPackage("com.kakao.talk");
////                startActivity(intent);
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void saveDataForShared() {
        //SharedPreferences & Editor 객체 생성
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        imageView3 = findViewById(R.id.imageView3);
        txt_making = findViewById(R.id.txt_making);
        edt_Title = findViewById(R.id.edt_Title);

        //저장 할 값 입력
        editor.putString("profilePic", uri);
        editor.putString("content", txt_making.getText().toString());
        editor.putString("title", edt_Title.getText().toString());
        editor.putInt("txtColor", txt_making.getCurrentTextColor());
        editor.putInt("txtGravity", txt_making.getGravity());
        editor.putFloat("txtSize", txt_making.getTextSize());  //getTextSize() 리턴값은 float임.

        editor.putFloat("txtShadow_rd", txt_making.getShadowRadius());
        editor.putFloat("txtShadow_x", txt_making.getShadowDx());
        editor.putFloat("txtShadow_y", txt_making.getShadowDy());
        editor.putInt("txtShadow_color", txt_making.getShadowColor());


        //Log.d(this.getClass().getName(), "으아아아앙" + uri + "aaaaaaaaaaaaa");
        //Log.d("pref", "으아아아앙" + uri + "aaaaaaaaaaaaa");
        Log.d("pref", "content" + txt_making.getText().toString() +
                "title" + edt_Title.getText().toString() +
                "profilePic" + uri);

        editor.apply();

    }

//    private void setJason() {
//        SharedPreferences pref = getSharedPreferences("jpref", MODE_PRIVATE);
//        SharedPreferences.Editor editor = pref.edit();
//
//        JSONArray jsonArray = new JSONArray();
//
//        try{
//            for(int i = 0; i < jsonArray.length(); i++){
//                JSONObject jsonObject = new JSONObject(); //배열 내에 들어갈 json
//                jsonObject.put("profilePic",uri);
//                jsonObject.put("content",txt_making.getText().toString());
//                jsonObject.put("title",edt_Title.getText().toString());
//                jsonObject.put("txtColor",txt_making.getCurrentTextColor());
//                jsonObject.put("txtGravity",txt_making.getGravity());
//                jsonObject.put("txtSize",txt_making.getTextSize());
//
//                jsonArray.put(jsonObject);
//            }
//
//        }catch (JSONException e){
//            e.printStackTrace();
//        }
//
//
//        editor.apply();
//    }


//
//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//    private void jsonDataSetting() {
//
//        SharedPreferences pref = getSharedPreferences("j_pref", MODE_PRIVATE);
//        String data = pref.getString("list","");  //data->기존데이터
//        SharedPreferences.Editor editor = pref.edit();
//
//        if (data == null) {
//            try {
//                JSONArray jsonArray = new JSONArray();
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("profilePic", uri);
//                jsonObject.put("content", txt_making.getText().toString());
//                jsonObject.put("title", edt_Title.getText().toString());
//                jsonObject.put("txtColor", txt_making.getCurrentTextColor());
//                jsonObject.put("txtGravity", txt_making.getGravity());
//                jsonObject.put("txtSize", Float.toString(txt_making.getTextSize()));  //getTextSize() 리턴값은 float임.
//
//                jsonObject.put("txtShadow_rd", Float.toString(txt_making.getShadowRadius()));
//                jsonObject.put("txtShadow_x", Float.toString(txt_making.getShadowDx()));
//                jsonObject.put("txtShadow_y", Float.toString(txt_making.getShadowDy()));
//                jsonObject.put("txtShadow_color", txt_making.getShadowRadius());
//
//                jsonArray.put(jsonObject);
//                String json = jsonArray.toString();
//                Log.d("제이슨저장되냐고1 : ", json + "");
//
//                editor.putString("list",json);
//                editor.apply();
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        } else {
//
//            try {
//                JSONArray jsonArray = new JSONArray(data);
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("profilePic", uri);
//                jsonObject.put("content", txt_making.getText().toString());
//                jsonObject.put("title", edt_Title.getText().toString());
//                jsonObject.put("txtColor", txt_making.getCurrentTextColor());
//                jsonObject.put("txtGravity", txt_making.getGravity());
//                jsonObject.put("txtSize", Float.toString(txt_making.getTextSize()));  //getTextSize() 리턴값은 float임.
//
//                jsonObject.put("txtShadow_rd", Float.toString(txt_making.getShadowRadius()));
//                jsonObject.put("txtShadow_x", Float.toString(txt_making.getShadowDx()));
//                jsonObject.put("txtShadow_y", Float.toString(txt_making.getShadowDy()));
//                jsonObject.put("txtShadow_color", txt_making.getShadowRadius());
//
//                jsonArray.put(jsonObject);
//                String json = jsonArray.toString();
//
//                editor.putString("list",json);
//                editor.apply();
//
//                Log.d("제이슨저장되냐고2 : ", json + "");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//
//
//    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void jsonDataSetting() {
        //SharedPreferences pref_font = getSharedPreferences("Font", MODE_PRIVATE);

        SharedPreferences pref = getSharedPreferences("j_pref", MODE_PRIVATE);

        String data = pref.getString("list", "");  //data->기존데이터
        SharedPreferences.Editor editor = pref.edit();

        // if (data.equals("") && data == null)

        try {
            JSONArray jsonArray = new JSONArray(data);
            //
            //
            //JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("profilePic", uri);
            jsonObject.put("content", txt_making.getText().toString());
            jsonObject.put("title", edt_Title.getText().toString());
            jsonObject.put("txtColor", txt_making.getCurrentTextColor());
            jsonObject.put("txtGravity", txt_making.getGravity());
            jsonObject.put("txtSize", Float.toString(txt_making.getTextSize()));  //getTextSize() 리턴값은 float임.

            jsonObject.put("txtShadow_rd", Float.toString(txt_making.getShadowRadius()));
            jsonObject.put("txtShadow_x", Float.toString(txt_making.getShadowDx()));
            jsonObject.put("txtShadow_y", Float.toString(txt_making.getShadowDy()));
            jsonObject.put("txtShadow_color", txt_making.getShadowColor());

//            double xd = (double) txt_making.getX();
//            Log.d("뷰xd좌표값은", "바로바로" + xd + "~~~~");

            String c = Float.toString(txt_making.getX());
            Log.d("뷰x좌표값은", "바로바로" + c + "~~~~");
            String d = Float.toString(txt_making.getY());


            jsonObject.put("txt_x", c);
            jsonObject.put("txt_y", d);

//            jsonObject.put("txt_x", (double) txt_making.getX());
//            jsonObject.put("txt_y", (double) txt_making.getY());

            //jsonObject.put("txtFont", txt_making.getTypeface().toString());
            int fontNum = getSharedPreferences("Font", MODE_PRIVATE).getInt("font", 0);
            jsonObject.put("txt_font", fontNum);
            Log.e("폰트식별", "" + fontNum + "~!~!~!~!~!~!!~!~!");


            jsonArray.put(jsonObject);
            String json = jsonArray.toString();
            Log.d("제이슨저장되냐고1 : ", json + "");

            editor.putString("list", json);
            //editor.putString("list", json);
            editor.apply();

            Log.d("제이슨저장되냐고2 : ", json + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


//    //============JSON테스트---대실패
//    private void sendArray() {
//
//
////        imageView3 = findViewById(R.id.imageView3);
////        txt_making = findViewById(R.id.txt_making);
////        edt_Title = findViewById(R.id.edt_Title);
//
//        JSONObject wrapObject = new JSONObject();
//        JSONArray jsonArray = new JSONArray();
//        //Log.d("jsonArray : ", "" + jsonArray);
//
//
//        try{
//
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("profilePic",uri);
//                jsonObject.put("content",txt_making.getText().toString());
//                jsonObject.put("title",edt_Title.getText().toString());
//                jsonArray.put(jsonObject);
//                Log.d("jsonArray : ", "" + jsonArray);
//
//            wrapObject.put("list",jsonArray);
//
//            Log.d("jsonArray : ", "" + wrapObject);
//            //실제 데이터 전송 메소드
//            //receiveArray(wrapObject.toString());
//        }catch (JSONException e){
//            e.printStackTrace();
//        }
//
//    }
//
//    //============JSON테스트2 제이슨->쉐어드 ---
//
//    private static final String jList = "jList";
//
//    private void setJasonPref ( String key, ArrayList<MyCardItem> values) {
//        SharedPreferences pref = getSharedPreferences("jpref",MODE_PRIVATE);
//        SharedPreferences.Editor editor = pref.edit();
//
//
//        JSONObject wrapObject = new JSONObject();
//        JSONArray jsonArray = new JSONArray();
//        Log.d("jsonArray1 : ", "" + jsonArray);
//
//
//        try{
//
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("profilePic",uri);
//                jsonObject.put("content",txt_making.getText().toString());
//                jsonObject.put("title",edt_Title.getText().toString());
//                jsonArray.put(jsonObject);
//                Log.d("jsonArray2 : ", "" + jsonArray);
//
//            wrapObject.put("list",jsonArray);
//
//            Log.d("jsonArray_wrapObject : ", "" + wrapObject);
//            //실제 데이터 전송 메소드
//            //receiveArray(wrapObject.toString());
//        }catch (JSONException e){
//            e.printStackTrace();
//        }
//
//
//            if (!values.isEmpty()) {
//                editor.putString(key, jsonArray.toString());
//            } else {
//                editor.putString(key,null);
//            }
//        Log.d("jsonArray__prf", ""+pref);
//
//
//        editor.apply();
//    }
//
//
// ============JSON테스트2 제이슨->쉐어드 ---
//
//    private static final String jList = "jList";
//
//    private void setJasonPref ( String key, ArrayList<MyCardItem> values) {
//        SharedPreferences pref = getSharedPreferences("jpref",MODE_PRIVATE);
//        SharedPreferences.Editor editor = pref.edit();
//
//
//        JSONArray jsonArray = new JSONArray();
//
//        for (int i = 0; i< values.size(); i++){
//            jsonArray.put(values.get(i));
//
//        }
//            if (!values.isEmpty()) {
//                editor.putString(key, jsonArray.toString());
//            } else {
//                editor.putString(key,null);
//            }
//        editor.apply();
//    }


//    private void sendArray() {
//
//        //JSONObject wrapObject = new JSONObject();
//        JSONArray jsonArray = new JSONArray();
//        try{
//            for(int i = 0; i < items.size(); i++){
//                JSONObject jsonObject = new JSONObject(); //배열 내에 들어갈 json
//                jsonObject.put("profilePic",items.get(i).getCardImg());
//                jsonObject.put("content",items.get(i).getCardContent());
//                jsonObject.put("title",items.get(i).getCardTitle());
//                jsonObject.put("txtColor",items.get(i).getTxtColor());
//                jsonObject.put("txtGravity",items.get(i).getTxtGravity());
//                jsonObject.put("txtSize",items.get(i).getTxtSize());
//
//                jsonArray.put(jsonObject);
//            }
//            Log.d("JSON Test", jsonArray.toString());
//
//            //wrapObject.put("list",jsonArray);
//
//            //실제 데이터 전송 메소드
//            //receiveArray(wrapObject.toString());
//        }catch (JSONException e){
//            e.printStackTrace();
//        }
//
//    }


    //requestCode에 현재액티비에서 보낸 CODE가 담겨옴
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case 1:
                if (resultCode == RESULT_OK) { //에딧_텍스트에서 보내온 resultCode와비교
                    //에딧_텍스트액티비티에서 인텐트에 담아온 정보 꺼내기
                    String edtKey = data.getStringExtra("edtKey");
                    int txtColor = data.getIntExtra("txtColor", Color.WHITE);
                    int txtGravity = data.getIntExtra("txtGravity", Gravity.CENTER);
                    float txtSize = data.getFloatExtra("txtSize", (float) 20);
                    //글씨 그림자 값
                    float txtShadow_rd = data.getFloatExtra("txtShadow_rd", 0);
                    float txtShadow_x = data.getFloatExtra("txtShadow_x", 0);
                    float txtShadow_y = data.getFloatExtra("txtShadow_y", 0);
                    int txtShadow_color = data.getIntExtra("txtShadow_color", 0);

                    //int txtFont = data.getIntExtra("txtFont",Typeface.NORMAL);
                    int txtFont = data.getIntExtra("txt_font", 0);


                    SharedPreferences pref_font = getSharedPreferences("Font", MODE_PRIVATE);

                    SharedPreferences.Editor editor = pref_font.edit();

                    switch (txtFont) {
                        case 0:
                            txt_making.setTypeface(Typeface.DEFAULT);
                            editor.putInt("font", 0);
                            editor.apply();
                            break;

                        case 1:
                            txt_making.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/batang.ttf"));
                            editor.putInt("font", 1);
                            editor.apply();
                            break;

                        case 2:
                            txt_making.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/utoimage_son_guelssi.ttf"));
                            editor.putInt("font", 2);
                            editor.apply();
                            break;

                        case 3:
                            txt_making.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/ss_flower_road.ttf"));
                            editor.putInt("font", 3);
                            editor.apply();
                            break;

                        case 4:
                            txt_making.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/seoul_hangang_b.ttf"));
                            editor.putInt("font", 4);
                            editor.apply();
                            break;

                        case 5:
                            txt_making.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/tlab_shin_young_bok.otf"));
                            editor.putInt("font", 5);
                            editor.apply();
                            break;

                        case 6:
                            txt_making.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/bmyeonsung.ttf"));
                            editor.putInt("font", 6);
                            editor.apply();

                            break;

                        case 7:
                            txt_making.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/jalnan.ttf"));
                            editor.putInt("font", 7);
                            editor.apply();
                            break;

                        case 8:
                            txt_making.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/jeongum.ttf"));
                            editor.putInt("font", 8);
                            editor.apply();

                            break;

                        case 9:
                            txt_making.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/jejuhallasan.ttf"));
                            editor.putInt("font", 9);
                            editor.apply();
                            break;

                        case 10:
                            txt_making.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/tvn_bold.ttf"));

                            editor.putInt("font", 10);
                            editor.apply();
                            break;

                    }


                    //Typeface font = (Typeface)txtFont;
//                    Typeface font = Typeface.createFromAsset(getBaseContext().getAssets(),"font/"+txtFont+".ttf");
//                    txt_making.setTypeface(font);

//                    float X = data.getFloatExtra("X",0);
//                    float Y = data.getFloatExtra("Y",0);
//                    txt_making.setX(X);
//                    txt_making.setY(Y);


//                    color = getSharedPreferences("color",MODE_PRIVATE).getInt("color", Color.WHITE);
//                    txt_making.setTextColor(color);
                    //sdw_colorChanged(color);

                    //텍스트뷰에 수정된 문자열 넣기
                    txt_making.setText(edtKey);
                    txt_making.setTextColor(txtColor);
                    txt_making.setGravity(txtGravity);
                    //주의!! 사용자 입력값 또는 넘겨받은 값을 그대로 받으려면 픽셀 Unit을 설정해줘야함
                    txt_making.setTextSize(TypedValue.COMPLEX_UNIT_PX, txtSize);
                    //넘겨받은 그림자 값 설정
                    txt_making.setShadowLayer(txtShadow_rd, txtShadow_x, txtShadow_y, txtShadow_color);

                    //txt_making.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/batang.ttf"));


                } else {

                }

                break;

            case 2:
                if (resultCode == RESULT_OK) { //에딧_이미지에서 보내온 이미지 받기
                    uri = data.getStringExtra("uri");
                    //Uri photoUri = Uri.parse(getUri);
                    //이미지뷰에 수정된 이미지 넣기
                    //imageView3.setRotation(90.0f);
                    imageView3.setImageURI(Uri.parse(uri));

                    Log.i(this.getClass().getName(), "이것은 " + uri + "에디트에서넘어오는이미지경로");

                    //imageView.setImageURI();
                }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int parentWidth = ((ViewGroup) v.getParent()).getWidth();    // 부모 View 의 Width
        int parentHeight = ((ViewGroup) v.getParent()).getHeight();    // 부모 View 의 Height

//        ACTION_DOWN : 처음 눌렸을 때
//        ACTION_MOVE : 누르고 움직였을 때
//        ACTION_UP : 누른걸 땠을 때

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // 뷰 누름
            oldXvalue = event.getX() + oldXvalue;
            oldYvalue = event.getY() + oldYvalue;
            Log.d("viewTest", "oldXvalue : " + oldXvalue + " oldYvalue : " + oldYvalue);    // View 내부에서 터치한 지점의 상대 좌표값.
            Log.d("viewTest", "v.getX() : " + v.getX());    // View 의 좌측 상단이 되는 지점의 절대 좌표값.
            Log.d("viewTest", "RawX : " + event.getRawX() + " RawY : " + event.getRawY());    // View 를 터치한 지점의 절대 좌표값.
            Log.d("viewTest", "v.getHeight : " + v.getHeight() + " v.getWidth : " + v.getWidth());    // View 의 Width, Height

        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            // 뷰 이동 중
            v.setX(v.getX() + (event.getX()) - (v.getWidth() / 2));
            v.setY(v.getY() + (event.getY()) - (v.getHeight() / 2));

        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            // 뷰에서 손을 뗌

            if (v.getX() < 0) {
                v.setX(0);
            } else if ((v.getX() + v.getWidth()) > parentWidth) {
                v.setX(parentWidth - v.getWidth());
            }

            if (v.getY() < 0) {
                v.setY(0);
            } else if ((v.getY() + v.getHeight()) > parentHeight) {
                v.setY(parentHeight - v.getHeight());
            }

        }
        return true;


    }


    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            //권한 허용 했을때 나오는 토스트메세지

            Toast.makeText(getApplicationContext(), "권한이 허용됨", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            //권한 허용안했을때 나오는 토스트 메세지

            Toast.makeText(getApplicationContext(), "권한이 거부됨", Toast.LENGTH_SHORT).show();
        }
    };
}





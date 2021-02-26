package com.example.mphotocard;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

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

public class EditingCard extends AppCompatActivity implements View.OnTouchListener {
//public class MakingCard extends AppCompatActivity  {


    InputMethodManager imm;  //키보드 내리기
    private int pos;

    private MyCardListAdapter adapter;

    private ImageView imageView3; //배경이미지
    private TextView txt_making; //내용
    private EditText edt_Title; //제목입력

    private Button btn_edt_txt; // 내용편집 버튼
    private Button btn_edt_img; //이미지 편집 버튼
    private Button btn_save; //저장 버튼
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
        setContentView(R.layout.editing_card);


        //권한 체크 해주는 팝업창
        TedPermission.with(EditingCard.this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage("카메라 권한이 필요합니다.")  //카메라 권한 팝업떳을때 나타나는 메세지
                .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();


        Bundle extras = getIntent().getExtras();

        imageView3 = findViewById(R.id.imageView3_edt);
        txt_making = findViewById(R.id.txt_making_edt);
        edt_Title = findViewById(R.id.edt_Title_edt);

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

        edt_Title.setText(title);

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


        //텍스트뷰 드래그드랍
        txt_making.setOnTouchListener(this);


        //텍스트 편집 버튼
        btn_edt_txt = findViewById(R.id.btn_edt_txt_edt);
        btn_edt_txt.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Edit_Text_edt.class);


                //텍스트뷰(txt_making)에 있는 글 담아서 edit_text 액티비티로 보내기
                intent.putExtra("key", txt_making.getText().toString());
                intent.putExtra("txtColor", txt_making.getCurrentTextColor());
                intent.putExtra("txtGravity", txt_making.getGravity());
                intent.putExtra("txtSize", txt_making.getTextSize());


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


                startActivityForResult(intent, REQUEST_CODE_TEXT);
            }
        });

        //이미지 편집 버튼
        btn_edt_img = findViewById(R.id.btn_edt_img_edt);
        btn_edt_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditImage_edt.class);
                //Intent intent = new Intent(getApplicationContext(), EditImage_2.class);

                startActivityForResult(intent, REQUEST_CODE_IMG);
            }
        });

        //저장 버튼
        btn_save = findViewById(R.id.btn_save_edt);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {


                //adapter.items.get()


                jsonDataSetting();

                Intent intent = new Intent(getApplicationContext(), MyCardList.class);

                startActivity(intent);
                finish();

            }

        });

        btn_kakao = findViewById(R.id.btn_kaka_edt);
        btn_kakao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendKakao();


            }
        });


    } //End of onCreate

    private void sendKakao() {

        final RelativeLayout capture = findViewById(R.id.ral_edt);//캡쳐할영역
        capture.buildDrawingCache();
        capture.setDrawingCacheEnabled(true);
        Bitmap bitmap = capture.getDrawingCache();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "uri", null);
        Uri uri_1 = Uri.parse(path);

        MediaScannerConnection.scanFile(EditingCard.this, //앨범에 사진을 보여주기 위해 Scan을 합니다.
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

        final RelativeLayout capture = findViewById(R.id.ral_edt);//캡쳐할영역

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

            Toast.makeText(getApplicationContext(), "저장완료", Toast.LENGTH_SHORT).show();

            fos.flush();

            fos.close();

            capture.destroyDrawingCache();

        } catch (FileNotFoundException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void jsonDataSetting() {

        SharedPreferences j_pref = getSharedPreferences("j_pref", MODE_PRIVATE);
        String json = j_pref.getString("list", null);
        SharedPreferences.Editor editor = j_pref.edit();

        try {
            JSONArray jsonArray = new JSONArray(json);

            JSONObject jsonObject = jsonArray.getJSONObject(pos);
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

            jsonObject.put("txt_x", Float.toString(txt_making.getX()));
            jsonObject.put("txt_y", Float.toString(txt_making.getY()));

            int fontNum = getSharedPreferences("Font", MODE_PRIVATE).getInt("font", 0);
            jsonObject.put("txt_font", fontNum);


            //jsonArray.put(pos,jsonObject);


            String n_json = jsonArray.toString();

            editor.putString("list", n_json);
            editor.apply();


        } catch (JSONException e) {
            e.printStackTrace();
        }

        //adapter.notifyDataSetChanged();


    }


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
                    int txtFont = data.getIntExtra("txtFont", 0);
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





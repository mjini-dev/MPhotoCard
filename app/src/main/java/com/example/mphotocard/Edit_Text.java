package com.example.mphotocard;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class Edit_Text extends AppCompatActivity
        implements View.OnClickListener,
        ColorPickerDialog.OnColorChangedListener {

    InputMethodManager imm;  //키보드 내리기

    private int pos;

    private Button btn_input_txt;
    private Button btn_cancel1;
    private EditText txt_edit;
    private TextView txt_making;
    private ImageView ex_img;

    //폰트설정
    private Button btn_font;
    private Spinner txt_spinner;
    private String fontPath;

    //폰트정렬
    private Button btn_txt_left;
    private Button btn_txt_center;
    private Button btn_txt_right;

    //내용색상
    private Button btn_txt_color;
    int color;

    //드래그드랍
    private float oldXvalue;
    private float oldYvalue;

    //폰트 크기
    private SeekBar txt_size_seekBar;
    private TextView tv_percent;

    //글씨 테두리
    private Switch sw_outline;
    private Switch sw_shadow;
    private LinearLayout li_shdw;
    private Button btn_shd_color;
    int color_sdw;


    //private String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_text);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        txt_edit = findViewById(R.id.txt_edit);
        ex_img = findViewById(R.id.ex_img);
        txt_making = findViewById(R.id.txt_making);

        btn_input_txt = findViewById(R.id.btn_input_txt);
        btn_cancel1 = findViewById(R.id.btn_cancel1);

        //btn_font = findViewById(R.id.btn_font);
        txt_spinner = findViewById(R.id.txt_spinner);

        btn_txt_left = findViewById(R.id.btn_txt_left);
        btn_txt_center = findViewById(R.id.btn_txt_center);
        btn_txt_right = findViewById(R.id.btn_txt_right);

        btn_txt_color = findViewById(R.id.btn_txt_color);

        txt_size_seekBar = findViewById(R.id.txt_size_seekBar);
        tv_percent = findViewById(R.id.tv_percent);

        //sw_outline = findViewById(R.id.sw_outline);
        sw_shadow = findViewById(R.id.sw_shadow);
        li_shdw = findViewById(R.id.li_shdw);
        btn_shd_color = findViewById(R.id.btn_shd_color);


        ex_img.setOnClickListener(myClickListener);
        btn_input_txt.setOnClickListener(myClickListener);
        btn_cancel1.setOnClickListener(myClickListener);

        //메이킹액티비티에서 인텐트에 담아온 값 꺼내오기
        //나를 호출한 인텐트 얻어오기
        Intent intent = getIntent();

        pos = intent.getIntExtra("pos",0);

        //인텐트에 담겨있는 값 꺼내오기_메이킹에서넘어온값
        String key = intent.getStringExtra("key");
        int txtColor = intent.getIntExtra("txtColor", Color.BLACK);
        int txtGravity = intent.getIntExtra("txtGravity", Gravity.CENTER_HORIZONTAL);
        float txtSize = intent.getFloatExtra("txtSize", (float) 20);
        String ex_img_uri = intent.getStringExtra("ex_img");


        //글씨 그림자 값
        float txtShadow_rd = intent.getFloatExtra("txtShadow_rd", 0);
        float txtShadow_x = intent.getFloatExtra("txtShadow_x", 0);
        float txtShadow_y = intent.getFloatExtra("txtShadow_y", 0);
        int txtShadow_color = intent.getIntExtra("txtShadow_color", 0);


        //에디트텍스트뷰에 문자열,색상값 넣기
        txt_edit.setText(key);
        txt_edit.setTextColor(txtColor);
        btn_txt_color.setBackgroundColor(txtColor);
        txt_edit.setGravity(txtGravity);
        //주의!! 사용자 입력값 또는 넘겨받은 값을 그대로 받으려면 픽셀 Unit을 설정해줘야함
        txt_edit.setTextSize(TypedValue.COMPLEX_UNIT_SP, txtSize);

        //넘겨받은 그림자 값 설정
        txt_edit.setShadowLayer(txtShadow_rd, txtShadow_x, txtShadow_y, txtShadow_color);


        float X = intent.getFloatExtra("x", 0);
        float Y = intent.getFloatExtra("y", 0);
        //txt_edit.setX(ex_img.getWidth()-txt_edit.getWidth());
        Log.d("좌표값받", "x" + X + "y" + Y);

        int parentWidth = ((ViewGroup) ex_img.getParent()).getWidth();    // 부모 View 의 Width
        int parentHeight = ((ViewGroup) ex_img.getParent()).getHeight();    // 부모 View 의 Height

//        if(X < 0){
//            txt_edit.setX(0);
//        }else if((X + txt_edit.getWidth()) >parentWidth){
//            txt_edit.setX(parentWidth-X);
//        }
//
//        if(Y < 0){
//            txt_edit.setY(0);
//        }else if((Y + txt_edit.getHeight()) > parentHeight){
//            txt_edit.setY(parentHeight-Y);
//        }

//
//        txt_edit.setX(X-279);
//        txt_edit.setY(Y-599);

        ex_img.setImageURI(Uri.parse(ex_img_uri));


        SharedPreferences j_pref = getSharedPreferences("j_pref", MODE_PRIVATE);
        String json = j_pref.getString("list", "");
        try {
            JSONArray jsonArray = new JSONArray(json);
            JSONObject jsonObject = jsonArray.getJSONObject(pos);
            int txt_font = jsonObject.getInt("txt_font");
            switch (txt_font) {
                case 0:
                    txt_edit.setTypeface(Typeface.DEFAULT);
                    txt_spinner.setSelection(0);
                    break;

                case 1:
                    txt_edit.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/batang.ttf"));
                    txt_spinner.setSelection(1);
                    break;

                case 2:
                    txt_edit.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/utoimage_son_guelssi.ttf"));
                    txt_spinner.setSelection(2);
                    break;

                case 3:
                    txt_edit.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/ss_flower_road.ttf"));
                    txt_spinner.setSelection(3);
                    break;

                case 4:
                    txt_edit.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/seoul_hangang_b.ttf"));
                    txt_spinner.setSelection(4);
                    break;

                case 5:
                    txt_edit.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/tlab_shin_young_bok.otf"));
                    txt_spinner.setSelection(5);
                    break;

                case 6:
                    txt_edit.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/bmyeonsung.ttf"));
                    txt_spinner.setSelection(6);

                    break;

                case 7:
                    txt_edit.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/jalnan.ttf"));
                    txt_spinner.setSelection(7);
                    break;

                case 8:
                    txt_edit.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/jeongum.ttf"));
                    txt_spinner.setSelection(8);

                    break;

                case 9:
                    txt_edit.setTypeface(Typeface.createFromAsset(Edit_Text.this.getAssets(), "myfont/jejuhallasan.ttf"));
                    txt_spinner.setSelection(9);
                    break;

                case 10:
                    txt_edit.setTypeface(Typeface.createFromAsset(Edit_Text.this.getAssets(), "myfont/tvn_bold.ttf"));
                    txt_spinner.setSelection(10);

                    break;



            }
        } catch (JSONException e) {
            e.printStackTrace();
        }







        //커서 끝에 위치시키기
        txt_edit.setSelection(txt_edit.length());


        //그림자 스위치 설정 저장
        SharedPreferences shadowPref = getSharedPreferences("shadow", MODE_PRIVATE);
        SharedPreferences.Editor editor = shadowPref.edit();

        if (txtShadow_rd != 0 && txtShadow_x != 0 && txtShadow_y != 0 && txtColor != 0) {

            sw_shadow.setChecked(shadowPref.getBoolean("shadow_SW", false)); //false는 기본값

        } else {
            sw_shadow.setChecked(false);
        }


////=============텍스트뷰 드래그드랍==================
//
//        txt_edit.setOnTouchListener(this);


//=============폰트설정 다이얼로그==================

        txt_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

                //Toast.makeText(Edit_Text.this,"선택된 아이템 : "+txt_spinner.getItemAtPosition(position),Toast.LENGTH_SHORT).show();

                String str_font = txt_spinner.getSelectedItem().toString();

//               Typeface typeface1 = ResourcesCompat.getFont(getApplicationContext(),R.font.batang);
//               Typeface typeface2 = ResourcesCompat.getFont(getApplicationContext(),R.font.utoimage_son_guelssi);
//               Typeface typeface3 = ResourcesCompat.getFont(getApplicationContext(),R.font.ss_flower_road);
//               Typeface typeface4 = ResourcesCompat.getFont(getApplicationContext(),R.font.seoul_hangang_b);
//
//
//               //Typeface typeface = Typeface.createFromFile(fontPath);
//
//               String fontPath0 = getURLForResource(R.font.batang); //android.resource://com.example.mphotocard/2131296256
//               String fontPath1 = getURLForResource(R.font.utoimage_son_guelssi); //android.resource://com.example.mphotocard/2131296266
//               String fontPath2 = getURLForResource(R.font.ss_flower_road); //android.resource://com.example.mphotocard/2131296263
//               String fontPath3 = getURLForResource(R.font.seoul_hangang_b); //android.resource://com.example.mphotocard/2131296262
//               String fontPath4 = getURLForResource(R.font.tlab_shin_young_bok); //android.resource://com.example.mphotocard/2131296264
//               String fontPath5 = getURLForResource(R.font.bmyeonsung); //android.resource://com.example.mphotocard/2131296257
//               String fontPath6 = getURLForResource(R.font.jalnan); //android.resource://com.example.mphotocard/2131296258
//               String fontPath7 = getURLForResource(R.font.jeongum); //android.resource://com.example.mphotocard/2131296260
//               String fontPath8 = getURLForResource(R.font.jejuhallasan); //android.resource://com.example.mphotocard/2131296259
//               String fontPath9 = getURLForResource(R.font.tvn_bold); //android.resource://com.example.mphotocard/2131296265
//               Log.d("fontPath0","폰트경로는 바로~!~"+fontPath0);
//               Log.d("fontPath1","폰트경로는 바로~!~"+fontPath1);
//               Log.d("fontPath2","폰트경로는 바로~!~"+fontPath2);
//               Log.d("fontPath3","폰트경로는 바로~!~"+fontPath3);
//               Log.d("fontPath4","폰트경로는 바로~!~"+fontPath4);
//               Log.d("fontPath5","폰트경로는 바로~!~"+fontPath5);
//               Log.d("fontPath6","폰트경로는 바로~!~"+fontPath6);
//               Log.d("fontPath7","폰트경로는 바로~!~"+fontPath7);
//               Log.d("fontPath8","폰트경로는 바로~!~"+fontPath8);
//               Log.d("fontPath9","폰트경로는 바로~!~"+fontPath9);
//
//
//               fontPath = txt_edit.getFontFeatureSettings();
//
//               // Typeface typeface = Typeface.createFromFile(fontPath);
//
////               //시스템에 저장되어있는 폰트확인
////               String fontPath = "/system/fonts";
////               File fontFiles = new File(fontPath);
////               File fontFileArray[] = fontFiles.listFiles();
////               String fontPathString = "";
////               for(File index : fontFileArray)
////               {
////                   fontPathString += index.toString();
////                   fontPathString += "\n";
////               }
//               Log.d("font", fontPath+"으아아아아아아");
//
//               if (str_font.equals("기본")) {
//                   txt_edit.setTypeface(Typeface.DEFAULT);
//               } else if (str_font.equals("김포평화 바탕")) {
//                   txt_edit.setTypeface(typeface1);
//               } else if (str_font.equals("비비트리 손글씨")) {
//                   txt_edit.setTypeface(typeface2);
//               } else if (str_font.equals("상상토끼 꽃길")) {
//                   txt_edit.setTypeface(typeface3);
//               } else if (str_font.equals("서울한강")) {
//                   txt_edit.setTypeface(typeface4);
//               }

                if (str_font.equals("기본")) {
                    txt_edit.setTypeface(Typeface.DEFAULT);
                    Log.e("폰트0",""+Typeface.DEFAULT);
                } else if (str_font.equals("김포평화 바탕")) {
                    txt_edit.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/batang.ttf"));
                    Log.e("폰트1",""+Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/batang.ttf"));
                } else if (str_font.equals("비비트리 손글씨")) {
                    txt_edit.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/utoimage_son_guelssi.ttf"));
                    Log.e("폰트2",""+Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/utoimage_son_guelssi.ttf"));
                } else if (str_font.equals("상상토끼 꽃길")) {
                    txt_edit.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/ss_flower_road.ttf"));
                    Log.e("폰트3",""+Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/ss_flower_road.ttf"));
                } else if (str_font.equals("서울한강")) {
                    txt_edit.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/seoul_hangang_b.ttf"));
                    Log.e("폰트4",""+Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/seoul_hangang_b.ttf"));
                } else if (str_font.equals("신영복")) {
                    txt_edit.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/tlab_shin_young_bok.otf"));
                    Log.e("폰트5",""+Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/tlab_shin_young_bok.otf"));
                } else if (str_font.equals("연성")) {
                    txt_edit.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/bmyeonsung.ttf"));
                    Log.e("폰트6",""+Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/bmyeonsung.ttf"));
                } else if (str_font.equals("잘난")) {
                    txt_edit.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/jalnan.ttf"));
                    Log.e("폰트7",""+Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/jalnan.ttf"));
                } else if (str_font.equals("정음")) {
                    txt_edit.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/jeongum.ttf"));
                    Log.e("폰트8",""+Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/jeongum.ttf"));
                } else if (str_font.equals("제주한라산")) {
                    txt_edit.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/jejuhallasan.ttf"));
                    Log.e("폰트9",""+Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/jejuhallasan.ttf"));
                } else if (str_font.equals("티비엔")) {
                    txt_edit.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/tvn_bold.ttf"));
                    Log.e("폰트10",""+Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/tvn_bold.ttf"));
                }

//                폰트로그
//                2019-10-13 21:08:45.348 15730-15730/com.example.mphotocard E/폰트0: android.graphics.Typeface@8c16c915
//                2019-10-13 21:08:49.318 15730-15730/com.example.mphotocard E/폰트1: android.graphics.Typeface@95905a95
//                2019-10-13 21:08:52.351 15730-15730/com.example.mphotocard E/폰트2: android.graphics.Typeface@bb488ab5
//                2019-10-13 21:08:54.386 15730-15730/com.example.mphotocard E/폰트3: android.graphics.Typeface@bb495815
//                2019-10-13 21:08:59.695 15730-15730/com.example.mphotocard E/폰트4: android.graphics.Typeface@64980575
//                2019-10-13 21:09:02.007 15730-15730/com.example.mphotocard E/폰트5: android.graphics.Typeface@67503075
//                2019-10-13 21:09:04.526 15730-15730/com.example.mphotocard E/폰트6: android.graphics.Typeface@83be7c55
//                2019-10-13 21:09:09.720 15730-15730/com.example.mphotocard E/폰트7: android.graphics.Typeface@83bfdcf6
//                2019-10-13 21:09:12.451 15730-15730/com.example.mphotocard E/폰트8: android.graphics.Typeface@8490c7b5
//                2019-10-13 21:09:15.343 15730-15730/com.example.mphotocard E/폰트9: android.graphics.Typeface@dc9227b5
//                2019-10-13 21:09:17.774 15730-15730/com.example.mphotocard E/폰트10: android.graphics.Typeface@dc91da35


                //               switch (position) {
//                   case 0:
//                       txt_edit.setTypeface(getResources().getFont(R.font.ss_flower_road));
//                       break;
//               }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


//=============폰트정렬==================
        btn_txt_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_edit.setGravity(Gravity.LEFT);

            }
        });

        btn_txt_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_edit.setGravity(Gravity.CENTER_HORIZONTAL);
            }
        });

        btn_txt_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_edit.setGravity(Gravity.RIGHT);
            }
        });


//============폰트 색상===============
        btn_txt_color.setOnClickListener(this);

//==============폰트 테두리 스위치===============
        //sw_outline.setOnCheckedChangeListener(new outLineSwitchListener());

//==============그림자 스위치===============
        sw_shadow.setOnCheckedChangeListener(new shadowSwitchListener());

        //그림자 색상
        btn_shd_color.setOnClickListener(this);

//============폰트 크기===============
        float size = txtSize;  //초기 글자크기 txtSize 는 픽셀단위...? , getTextSize()는 크기를 px단위로 반환..

        float spSize = txtSize / getResources().getDisplayMetrics().scaledDensity; //픽셀을 sp로 변환

        String sizeS = Integer.toString((int) spSize); //sp값을 String으로 형변환

        final int minVal = 0;
        txt_size_seekBar.setMax(28);  //최대크기
        //txt_size_seekBar.setMin(10);
        txt_size_seekBar.setProgress((int) spSize);
        txt_edit.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        tv_percent.setText(sizeS);

        txt_size_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                if (progress >= (float) (minVal + 10)) {
                    seekBar.setProgress(progress);
                    txt_edit.setTextSize(progress);
                    tv_percent.setText(progress + "");
                } else {
                    seekBar.setProgress(minVal);
                    txt_edit.setTextSize((float) (minVal + 10));
                    tv_percent.setText((minVal + 10) + "");
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


//        //============폰트 크기 테스트===============
//        int size = 0;  //초기 글자크기
//        final int minVal = -10;
//        txt_size_seekBar.setMax(10);  //최대크기
//        //txt_size_seekBar.setMin(10);
//        txt_size_seekBar.setProgress(size);
//        txt_edit.setTextSize((float)0);
//
//        txt_size_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
//                if (progress >= (float)(minVal+10)) {
//                    seekBar.setProgress(progress);
//                    txt_edit.setTextSize(progress);
//                } else {
//                    seekBar.setProgress(minVal);
//                    txt_edit.setTextSize((float)(minVal+10));
//                }
//
//
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//
//            }
//        });
//        //============폰트 크기 테스트===============


//        //======================컬러피커 테스트->안됨
//        btn_txt_color.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                AlertDialog.Builder colorDialogBuilder = new AlertDialog.Builder(
//                        Edit_Text.this);
//                LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
//                View dialogview = inflater.inflate(R.layout.color, null);
//                picker = findViewById(R.id.picker);
//                svBar = findViewById(R.id.svbar);
//                opacityBar = findViewById(R.id.opacitybar);
//                picker.addSVBar(svBar);
//                picker.addOpacityBar(opacityBar);
//                picker.setOnColorChangedListener(new ColorPicker.OnSdwColorChangedListener()
//                {
//                    @Override
//                    public void onColorChanged(int color) {
//
//                    }
//                });
//                colorDialogBuilder.setTitle("Choose Text Color");
//                colorDialogBuilder.setView(dialogview);
//                colorDialogBuilder.setPositiveButton(R.string.ok,
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Log.d("으아아아", "Color :" + picker.getColor());
//                                btn_txt_color.setTextColor(picker.getColor());
//                                picker.setOldCenterColor(picker.getColor());
//                            }
//                        });
//                colorDialogBuilder.setNegativeButton("Cancel",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                            }
//                        });
//                AlertDialog colorPickerDialog = colorDialogBuilder.create();
//                colorPickerDialog.show();
//            }
//
//        });

//==============적용버튼!!!===============
        btn_input_txt.setOnClickListener(new View.OnClickListener() {
            //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                //수정입력한 내용 얻어오기
                String data = txt_edit.getText().toString();
                int txtColor = txt_edit.getCurrentTextColor();
                int txtGravity = txt_edit.getGravity();
                float txtSize = txt_edit.getTextSize();

                float txtShadow_rd = txt_edit.getShadowRadius();
                float txtShadow_x = txt_edit.getShadowDx();
                float txtShadow_y = txt_edit.getShadowDy();
                int txtShadow_color = txt_edit.getShadowColor();
                Log.d("그림자컬러", "c" + txtShadow_color);

                int txtFont = txt_edit.getTypeface().getStyle();
                //int txtFont = txt_edit.getTypeface().
                //Typeface txtFont = txt_edit.getTypeface();
                //String txtFont = txt_edit.getTypeface().toString();
                Log.d("폰트폰트", "오호호호" + txtFont);


                //수정된 내용을 담기위한 인텐트 생성
                Intent intent = new Intent();

                //수정된 정보 담기
                intent.putExtra("edtKey", data);
                intent.putExtra("txtColor", txtColor);
                intent.putExtra("txtGravity", txtGravity);
                intent.putExtra("txtSize", txtSize);

                intent.putExtra("txtShadow_rd", txtShadow_rd);
                intent.putExtra("txtShadow_x", txtShadow_x);
                intent.putExtra("txtShadow_y", txtShadow_y);
                intent.putExtra("txtShadow_color", txtShadow_color);

                String str_font = txt_spinner.getSelectedItem().toString();
                if (str_font.equals("기본")) {
                    intent.putExtra("txt_font", 0);

                } else if (str_font.equals("김포평화 바탕")) {
                    intent.putExtra("txt_font", 1);

                } else if (str_font.equals("비비트리 손글씨")) {
                    intent.putExtra("txt_font", 2);

                } else if (str_font.equals("상상토끼 꽃길")) {
                    intent.putExtra("txt_font", 3);

                } else if (str_font.equals("서울한강")) {
                    intent.putExtra("txt_font", 4);

                } else if (str_font.equals("신영복")) {
                    intent.putExtra("txt_font", 5);

                } else if (str_font.equals("연성")) {
                    intent.putExtra("txt_font", 6);

                } else if (str_font.equals("잘난")) {
                    intent.putExtra("txt_font", 7);

                } else if (str_font.equals("정음")) {
                    intent.putExtra("txt_font", 8);

                } else if (str_font.equals("제주한라산")) {
                    intent.putExtra("txt_font", 9);

                } else if (str_font.equals("티비엔")) {
                    intent.putExtra("txt_font", 10);
                }


//                intent.putExtra("X",txt_edit.getX()+oldXvalue-(ex_img.getWidth()/2));
//                intent.putExtra("Y",txt_edit.getY()+oldYvalue-(ex_img.getHeight()/2));

                //resultCode(성공의 의미)와 수정내용을 담은 인텐트 설정
                setResult(RESULT_OK, intent);

                //액티비티 종료(메이킹엑티비티로 이동)
                finish();

//                str = editText.getText().toString();
//                Intent intent = new Intent(Edit_Text.this , MakingCard.class);
//                intent.putExtra("str",str);
//
//                startActivity(intent);
//                finish();
            }
        });

        btn_cancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //resultCode담기
                setResult(RESULT_CANCELED);

                //액티비티 종료(메이킹엑티비티로 이동)
                finish();
            }
        });

    }

    private String getURLForResource(int fontId) {
        return Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + fontId).toString();
    }
//=================온크리에이트 종료


    View.OnClickListener myClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideKeyboard();
            switch (v.getId()) {
                case R.id.ex_img:
                    break;

                case R.id.btn_input_txt:
                    break;

                case R.id.btn_cancel1:
                    break;
            }
        }
    };

    private void hideKeyboard() {
        imm.hideSoftInputFromWindow(txt_edit.getWindowToken(), 0);

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_txt_color:
                //color = PreferenceManager.getDefaultSharedPreferences(this).getInt("color", Color.WHITE);
                color = getSharedPreferences("color", MODE_PRIVATE).getInt("color", Color.WHITE);

                new ColorPickerDialog(this, this, color).show();

                break;


//            case R.id.btn_shd_color :
//
//                color_sdw = getSharedPreferences("color", MODE_PRIVATE).getInt("color", Color.WHITE);
//
//                new ColorPickerDialog_SDW(this, (ColorPickerDialog_SDW.OnSdwColorChangedListener) this, color_sdw).show();
//
//                break;


//            case R.id.btn_font :
//
//                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(
//                        getApplicationContext());
//
//                alertBuilder.setTitle("글꼴설정");
//
//                // List Adapter 생성
//                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                        getApplicationContext(),
//                        android.R.layout.select_dialog_singlechoice);
//                adapter.add("사과");
//                adapter.add("딸기");
//                adapter.add("오렌지");
//                adapter.add("수박");
//                adapter.add("참외");
//
//                // 버튼 생성
//                alertBuilder.setNegativeButton("취소",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog,
//                                                int which) {
//                                dialog.dismiss();
//                            }
//                        });
//
//                // Adapter 셋팅
//                alertBuilder.setAdapter(adapter,
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog,
//                                                int id) {
////
////                                // AlertDialog 안에 있는 AlertDialog
////                                String strName = adapter.getItem(id);
////                                AlertDialog.Builder innBuilder = new AlertDialog.Builder(
////                                        getApplicationContext());
////                                innBuilder.setMessage(strName);
////                                innBuilder.setTitle("당신이 선택한 것은 ");
////                                innBuilder
////                                        .setPositiveButton(
////                                                "확인",
////                                                new DialogInterface.OnClickListener() {
////                                                    public void onClick(
////                                                            DialogInterface dialog,
////                                                            int which) {
////                                                        dialog.dismiss();
////                                                    }
////                                                });
////                                innBuilder.show();
//                            }
//                        });
//                alertBuilder.show();
//                break;

            default:
                break;
        }

    }

    @Override
    public void colorChanged(int color) {
        //PreferenceManager.getDefaultSharedPreferences(this).edit().putInt("color", color).commit();
        getSharedPreferences("color", MODE_PRIVATE).edit().putInt("color", color).apply();

        btn_txt_color.setBackgroundColor(color);
        txt_edit.setTextColor(color);
    }

//    public void sdw_colorChanged(int color_sdw) {
//
//        btn_shd_color.setBackgroundColor(color);
//
//    }


    class outLineSwitchListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            final float strokeWidth = 5.0f;
            Canvas canvas = new Canvas();
            Paint myPaint = txt_edit.getPaint(); //텍스트에딧에서 페인트를가져오면...?
            //Paint myPaint = new Paint();
            String txt = txt_edit.getText().toString();
            Rect rc = new Rect();

            if (isChecked) {

                //외곽선그리기
                myPaint.setStyle(Paint.Style.STROKE);
                myPaint.setStrokeWidth(strokeWidth);
                myPaint.setColor(Color.WHITE);
                //canvas.drawText(txt,(-rc.left)+ strokeWidth / 2.0f, (-rc.top)+strokeWidth/2.0f, myPaint);
                canvas.drawText(txt, (-rc.left) + strokeWidth, (-rc.top) + strokeWidth, myPaint);

//===================외곽선시도  실패======================================================
//                final float strokeWidth = 5.0f;
//                Canvas canvas = new Canvas();
//
//                Paint myPaint = txt_edit.getPaint();
//                //Paint myPaint = new Paint();
//                String txt = txt_edit.getText().toString();
//                Rect rc = new Rect();
//
//                myPaint.setTextSize(100.0f);
//                myPaint.getTextBounds(txt,0,txt.length(),rc);
//
//                //외곽선그리기
//                myPaint.setStyle(Paint.Style.STROKE);
//                myPaint.setStrokeWidth(strokeWidth);
//                myPaint.setColor(Color.WHITE);
//                //canvas.drawText(txt,(-rc.left)+ strokeWidth / 2.0f, (-rc.top)+strokeWidth/2.0f, myPaint);
//                canvas.drawText(txt,(-rc.left)+ strokeWidth, (-rc.top)+strokeWidth, myPaint);
//                // draw text
//                myPaint.setStyle(Paint.Style.FILL );
//                myPaint.setColor( color );
//                canvas.drawText( txt, ( -rc.left ) + strokeWidth / 2.0f, (-rc.top) + strokeWidth / 2.0f, myPaint );
//===================외곽선시도  실패======================================================

//===================외곽선시도  실패======================================================
//                txt_edit.getPaint().setColor(color); //페인트 색 지정
//
//                txt_edit.getPaint().setColor(Color.BLACK);
//                txt_edit.getPaint().setStyle(Paint.Style.STROKE); //STROKE 스타일로 그리기
//                txt_edit.getPaint().setStrokeWidth(4); //STROKE 두께 지정
//
//                txt_edit.getPaint().setStyle(Paint.Style.FILL);
//                txt_edit.getPaint().setColor(Color.WHITE);
//
//
//                Paint textPaint = new Paint();
//                textPaint.setAntiAlias(true);
//                textPaint.setColor(Color.WHITE);
//
//                Paint strokePaint = new Paint();
//                strokePaint.setAntiAlias(true);
//                strokePaint.setColor(Color.BLACK);
//                strokePaint.setStyle(Paint.Style.STROKE);
//                strokePaint.setStrokeWidth(4);
//                Canvas canvas = new Canvas();
//                canvas.drawText(txt_edit.getText().toString(), 100, 100, strokePaint);
//===================외곽선시도  실패======================================================

//====================어설프게 되긴하는거===============================================
//                txt_edit.getPaint().setAntiAlias(true);
//                //txt_edit.getPaint().setColor(color);
//
//                txt_edit.getPaint().setStyle(Paint.Style.FILL_AND_STROKE);
//                txt_edit.getPaint().setColor(Color.WHITE);
//                txt_edit.getPaint().setStrokeWidth(4);
//
//
////                txt_edit.getPaint().setColor(Color.BLACK);
////                txt_edit.getPaint().setStyle(Paint.Style.FILL);
//==================일단여기까지 한묶음.. 위두줄은 제외====================

            } else {

                //외곽선그리기
                myPaint.setStyle(Paint.Style.FILL);
                myPaint.setStrokeWidth(0.0f);
                myPaint.setColor(Color.TRANSPARENT);
                //canvas.drawText(txt,(-rc.left)+ strokeWidth / 2.0f, (-rc.top)+strokeWidth/2.0f, myPaint);
                canvas.drawText(txt, (-rc.left) + strokeWidth, (-rc.top) + strokeWidth, myPaint);

            }

        }

//        private void DrawText( String text, Canvas canvas ) {
//
//            final float strokeWidth = 5.0f;
//
//            Paint myPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//            String txt = txt_edit.getText().toString();
//            Rect rc = new Rect();
//
//            myPaint.setTextSize(txt_edit.getTextSize());
//            myPaint.getTextBounds(txt,0,txt.length(),rc);
//
//            //외곽선그리기
//            myPaint.setStyle(Paint.Style.FILL_AND_STROKE);
//            myPaint.setStrokeWidth(strokeWidth);
//            myPaint.setColor(Color.WHITE);
//            canvas.drawText(txt,(-rc.left)+ strokeWidth / 2.0f, (-rc.top)+strokeWidth/2.0f, myPaint);
//
//            // draw text
//            myPaint.setStyle(Paint.Style.FILL );
//            myPaint.setColor( Color.BLACK );
//            canvas.drawText( text, ( -rc.left ) + strokeWidth / 2.0f, (-rc.top) + strokeWidth / 2.0f, myPaint );
//
//        }

    }

    class shadowSwitchListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            SharedPreferences shadowPref = getSharedPreferences("shadow", MODE_PRIVATE);
            SharedPreferences.Editor editor = shadowPref.edit();

            if (isChecked) {
                txt_edit.setShadowLayer(5, 5, 5, Color.BLACK);
                //txt_making.setShadowLayer(5,5,5,Color.BLACK);

                //li_shdw.setVisibility(View.VISIBLE);

                editor.putBoolean("shadow_SW", isChecked);
                editor.apply();

            } else {
                txt_edit.setShadowLayer(0, 0, 0, Color.TRANSPARENT);

                li_shdw.setVisibility(View.GONE);

                editor.putBoolean("shadow_SW", false);
                editor.apply();


            }

        }
    }


//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//
//        int parentWidth = ((ViewGroup) v.getParent()).getWidth();    // 부모 View 의 Width
//        int parentHeight = ((ViewGroup) v.getParent()).getHeight();    // 부모 View 의 Height
//
////        ACTION_DOWN : 처음 눌렸을 때
////        ACTION_MOVE : 누르고 움직였을 때
////        ACTION_UP : 누른걸 땠을 때
//
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            // 뷰 누름
//            oldXvalue = event.getX();
//            oldYvalue = event.getY();
//            Log.d("viewTest", "oldXvalue : " + oldXvalue + " oldYvalue : " + oldYvalue);    // View 내부에서 터치한 지점의 상대 좌표값.
//            Log.d("viewTest", "v.getX() : " + v.getX());    // View 의 좌측 상단이 되는 지점의 절대 좌표값.
//            Log.d("viewTest", "RawX : " + event.getRawX() + " RawY : " + event.getRawY());    // View 를 터치한 지점의 절대 좌표값.
//            Log.d("viewTest", "v.getHeight : " + v.getHeight() + " v.getWidth : " + v.getWidth());    // View 의 Width, Height
//
//        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
//            // 뷰 이동 중
//            v.setX(v.getX() + (event.getX()) - (v.getWidth() / 2));
//            v.setY(v.getY() + (event.getY()) - (v.getHeight() / 2));
//
//        } else if (event.getAction() == MotionEvent.ACTION_UP) {
//            // 뷰에서 손을 뗌
//
//            if (v.getX() < 0) {
//                v.setX(0);
//            } else if ((v.getX() + v.getWidth()) > parentWidth) {
//                v.setX(parentWidth - v.getWidth());
//            }
//
//            if (v.getY() < 0) {
//                v.setY(0);
//            } else if ((v.getY() + v.getHeight()) > parentHeight) {
//                v.setY(parentHeight - v.getHeight());
//            }
//
//        }
//        return true;
//
//
//    }


}




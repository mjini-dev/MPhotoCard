package com.example.mphotocard;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditImage_edt extends AppCompatActivity {

    private static final String TAG = "mCard";
    private static final int REQUEST_IMAGE_CAPTURE = 672;
    private static final int PICK_FROM_ALBUM = 673;
    private static final int REQUEST_IMAGE_CROP = 674;
    private String imageFilePath;
    private Uri uri, photoURI, albumURI;
    private File tempFile;
    private Button btn_input_img;
    private Button btn_cancel2;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_image_edt);

        btn_input_img = findViewById(R.id.btn_input_img_edt);
        btn_cancel2 = findViewById(R.id.btn_cancel2_edt);
        imageView = findViewById(R.id.imageView3_edt);


        //권한 체크 해주는 팝업창
        TedPermission.with(getApplicationContext())
                .setPermissionListener(permissionListener)
                .setRationaleMessage("카메라 권한이 필요합니다.")  //카메라 권한 팝업떳을때 나타나는 메세지
                .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

        //촬영버튼 눌렀을때
        findViewById(R.id.btn_capture_edt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //카메라 인텐트 실행
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (intent.resolveActivity(getPackageManager()) != null) {
                    //임시파일생성
                    tempFile = null;
                    try {
                        tempFile = createImageFile(); //사진찍은후 저장 할 임시폴더 (아래에 정의함)
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //임시파일생성되면 실행
                    if (tempFile != null) {
                        uri = FileProvider.getUriForFile(getApplicationContext(),"com.example.mphotocard", tempFile);
                        //uri = FileProvider.getUriForFile(getApplicationContext(), getPackageName(), tempFile);
//                        Uri providerURI = FileProvider.getUriForFile(getApplicationContext(), getPackageName(), tempFile);
//                        uri = providerURI;
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);


                    }
                }
            }
        });

        //앨범에서 이미지 가져오기
        findViewById(R.id.btn_album_edt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_FROM_ALBUM);

            }
        });


        //저장버튼 눌렀을때
        btn_input_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //iv_result에 담긴 이미지를(이미지의 경로 or Uri) intent로 전달

                uri = FileProvider.getUriForFile(getApplicationContext(),"com.example.mphotocard", tempFile);
//                uri = Uri.fromFile(tempFile);
                Intent intent = new Intent();
                intent.putExtra("uri", uri.toString());
                setResult(RESULT_OK, intent);
                finish();




            }
        });

        btn_cancel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //resultCode담기
                setResult(RESULT_CANCELED);

                //액티비티 종료(메이킹엑티비티로 이동)
                finish();
            }
        });


    }

    // Android 6(마시멜로)에서는 Uri.fromFile 함수를 사용하였으나
    //  7.0부터는 이 함수를 사용할 시 FileUriExposedException이 발생하므로 아래와 같이 함수를 작성
    //이미지파일 이름을 년,월,일,시... -중복피하기위해 생성 해서 저장 임시폴더
    private File createImageFile() throws IOException {
        //이미지 파일 이름
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        //이미지가 저장될 폴더 이름

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        //빈 파일 생성
        File imageFile = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        imageFilePath = imageFile.getAbsolutePath();
        Log.d(this.getClass().getName(), imageFilePath + "");
        return imageFile;
    }


    // 카메라 전용 크롭
    private void cropCameraImage() {
        //====크롭수정 테스트...안되네...
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);
        grantUriPermission(list.get(0).activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        int size = list.size();
        if (size == 0) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            //Toast.makeText(this, "용량이 큰 사진의 경우 시간이 오래 걸릴 수 있습니다.", Toast.LENGTH_SHORT).show();
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);

            File croppedFileName = null;
            try {
                croppedFileName = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            File folder = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            //File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File tempFile = new File(folder.toString(), croppedFileName.getName());

            uri = FileProvider.getUriForFile(getApplicationContext(), getPackageName(), tempFile);

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString()); //Bitmap 형태로 받기 위해 해당 작업 진행

            Intent i = new Intent(intent);
            ResolveInfo res = list.get(0);
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            grantUriPermission(res.activityInfo.packageName, uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            startActivityForResult(i, REQUEST_IMAGE_CROP);

        }
        //=============크롭수정테스트


        }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            imageView = findViewById(R.id.imageView3_edt);
            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
            ExifInterface exif = null;

            try {
                exif = new ExifInterface(imageFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int exifOrientation;
            int exifDegree;

            if (exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegrees(exifOrientation);

            } else {
                exifDegree = 0;
            }


            //setImage();
            bitmap = rotate(bitmap, exifDegree);
//            imageView.setImageBitmap(bitmap);

            //=========================사진 크롭 후 바로 메이킹으로 넘기기  -> 사진회전 실패 -_-
            //회전값 반영한 비트맵이미지를 uri 로 바꿈
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "uri", null);
            uri = Uri.parse(path);

            cropCameraImage();

            MediaScannerConnection.scanFile(EditImage_edt.this, //앨범에 사진을 보여주기 위해 Scan을 합니다.
                    new String[]{uri.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });


//            uri = FileProvider.getUriForFile(getApplicationContext(),"com.example.mphotocard", tempFile);
//                uri = Uri.fromFile(tempFile);
            Intent intent = new Intent();
            intent.putExtra("uri", uri.toString());
            setResult(RESULT_OK, intent);
            finish();



            if (resultCode != RESULT_OK) {
                Toast.makeText(this, "취소되었습니다", Toast.LENGTH_SHORT).show();

                //촬영중 뒤로가기눌러 빈 tempFlie 생길경우 삭제
                if (tempFile != null) {
                    if (tempFile.exists()) {
                        if (tempFile.delete()) {
                            Log.e(TAG, tempFile.getAbsolutePath() + "삭제 성공");
                            tempFile = null;
                        }
                    }
                }

                return;
            }


        } else if (requestCode == PICK_FROM_ALBUM && resultCode == RESULT_OK) {

            uri = data.getData();


            Cursor cursor = null;
            try {
                //Uri스키마를 content:/// -> file:///로 변경
                String[] proj = {MediaStore.Images.Media.DATA};

                assert uri != null;
                cursor = getContentResolver().query(uri, proj, null, null, null);

                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                cursor.moveToFirst();

                tempFile = new File(cursor.getString(column_index));

            } finally {
                if (cursor != null) {
                    cursor.close();
                }


            }

            //cropImage(uri);
            cropCameraImage();

            MediaScannerConnection.scanFile(EditImage_edt.this, //앨범에 사진을 보여주기 위해 Scan을 합니다.
                    new String[]{uri.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });

            //=========================사진 크롭 후 바로 메이킹으로 넘기기

            Intent intent = new Intent();
            intent.putExtra("uri", uri.toString());
            setResult(RESULT_OK, intent);
            finish();

            //예외처리 (앨범에서 선택x->뒤로가기 OR 촬영후 저장ㅌ->뒤로가기)
            if (resultCode != RESULT_OK) {
                Toast.makeText(this, "취소되었습니다", Toast.LENGTH_SHORT).show();

                //촬영중 뒤로가기눌러 빈 tempFlie 생길경우 삭제
                if (tempFile != null) {
                    if (tempFile.exists()) {
                        if (tempFile.delete()) {
                            Log.e(TAG, tempFile.getAbsolutePath() + "삭제 성공");
                            tempFile = null;
                        }
                    }
                }

                return;
            }


        } else if (requestCode == REQUEST_IMAGE_CROP && resultCode == RESULT_OK) {
            // } else if (requestCode == REQUEST_IMAGE_CROP ) {

            //=============데스트
            try { //bitmap 형태의 이미지로 가져오기 위해 아래와 같이 작업하였으며 Thumbnail을 추출하였습니다.

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                Bitmap thumbImage = ThumbnailUtils.extractThumbnail(bitmap, 128, 128);
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                thumbImage.compress(Bitmap.CompressFormat.JPEG, 100, bs); //이미지가 클 경우 OutOfMemoryException 발생이 예상되어 압축


                //여기서는 ImageView_edt에 setImageBitmap을 활용하여 해당 이미지에 그림띄우기

                imageView.setImageBitmap(thumbImage);
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage().toString());
            }
            //================데스트

            //imageView.setImageURI(uri);

//            //==========여기서부터살려
//            imageView.setImageURI(null);
//            imageView.setImageURI(uri);
//            revokeUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//            //==========여기까지 살려


            //예외처리 (앨범에서 선택x->뒤로가기 OR 촬영후 저장ㅌ->뒤로가기)
            if (resultCode != RESULT_OK) {
                Toast.makeText(this, "취소되었습니다", Toast.LENGTH_SHORT).show();

                //촬영중 뒤로가기눌러 빈 tempFlie 생길경우 삭제
                if (tempFile != null) {
                    if (tempFile.exists()) {
                        if (tempFile.delete()) {
                            Log.e(TAG, tempFile.getAbsolutePath() + "삭제 성공");
                            tempFile = null;
                        }
                    }
                }

                return;
            }

        }

        //예외처리 (앨범에서 선택x->뒤로가기 OR 촬영후 저장ㅌ->뒤로가기)
        if (resultCode != RESULT_OK) {
            Toast.makeText(this, "취소되었습니다", Toast.LENGTH_SHORT).show();

            //촬영중 뒤로가기눌러 빈 tempFlie 생길경우 삭제
            if (tempFile != null) {
                if (tempFile.exists()) {
                    if (tempFile.delete()) {
                        Log.e(TAG, tempFile.getAbsolutePath() + "삭제 성공");
                        tempFile = null;
                    }
                }
            }

            return;
        }
    }



    //EXIF 정보를 회전각도로 변환하는 메소드, @param exifOrientation EXIF 회전각 / @return 실제 각도
    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        } else
            return 0;
    }

    //이미지 정방향 회전시킴 @param bitmap 비트맵 이미지,degrees 회전 각도 / @return 회전된 이미지
    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        //회전각도셋팅
        matrix.postRotate(degree);
        //이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);


    }



    private static int getExifOrientation(String src) throws IOException {
        int orientation = 1;
        try {
//            if your are targeting only api level >= 5 ExifInterface exif =
//            new ExifInterface(src); orientation =
//            exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
//

            if (Build.VERSION.SDK_INT >= 5) {
                Class<?> exifClass = Class
                        .forName("android.media.ExifInterface");
                Constructor<?> exifConstructor = exifClass
                        .getConstructor(new Class[]{String.class});
                Object exifInstance = exifConstructor
                        .newInstance(new Object[]{src});
                Method getAttributeInt = exifClass.getMethod("getAttributeInt",
                        new Class[]{String.class, int.class});
                Field tagOrientationField = exifClass
                        .getField("TAG_ORIENTATION");
                String tagOrientation = (String) tagOrientationField.get(null);
                orientation = (Integer) getAttributeInt.invoke(exifInstance,
                        new Object[]{tagOrientation, 1});

            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return orientation;
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

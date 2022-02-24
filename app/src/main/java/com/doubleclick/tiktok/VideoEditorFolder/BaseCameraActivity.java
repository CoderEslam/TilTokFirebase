package com.doubleclick.tiktok.VideoEditorFolder;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.opengl.GLException;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.daasuu.gpuv.camerarecorder.CameraRecordListener;
import com.daasuu.gpuv.camerarecorder.GPUCameraRecorder;
import com.daasuu.gpuv.camerarecorder.GPUCameraRecorderBuilder;
import com.daasuu.gpuv.camerarecorder.LensFacing;
import com.doubleclick.tiktok.FaceFilters.FaceFilterActivity;
import com.doubleclick.tiktok.HomeActivity;
import com.doubleclick.tiktok.R;
import com.doubleclick.tiktok.SoundActivity;
import com.doubleclick.tiktok.VideoEditorFolder.widget.SampleCameraGLView;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.opengles.GL10;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.IntBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BaseCameraActivity extends AppCompatActivity {

    private SampleCameraGLView sampleGLView;
    protected GPUCameraRecorder GPUCameraRecorder;
    private String filepath;
    private TextView recordBtn,puseBtn,AddSound,Close;
    protected LensFacing lensFacing = LensFacing.BACK;
    protected int cameraWidth = 1280;
    protected int cameraHeight = 720;
    protected int videoWidth = 720;
    protected int videoHeight = 720;
    private String soundUrl,soundTitle = null;
    private MediaPlayer mediaPlayer;
    private ImageView Filter;

    private boolean toggleClick = false;

    private ListView lv;

    protected void onCreateActivity() {
//        getSupportActionBar().hide();
        recordBtn = findViewById(R.id.btn_record);
        puseBtn = findViewById(R.id.puseBtn);
        AddSound= findViewById(R.id.AddSound);
        Close = findViewById(R.id.Close);
        Filter = findViewById(R.id.Filter);
        soundUrl = getIntent().getStringExtra("soundUrl");
        soundTitle = getIntent().getStringExtra("soundTitle");

        if(soundTitle != null){
            AddSound.setText(soundTitle);
        }

        Filter.setOnClickListener(view -> {
            Intent intent = new Intent(BaseCameraActivity.this, FaceFilterActivity.class);
            startActivity(intent);
            Animatoo.animateCard(BaseCameraActivity.this);
            finish();

        });



        Close.setOnClickListener(view -> {

            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            Animatoo.animateSlideDown(BaseCameraActivity.this);
            // from method  ->  releaseCamera()
            if (GPUCameraRecorder != null) {
                GPUCameraRecorder.stop();
                GPUCameraRecorder.release();
                GPUCameraRecorder = null;
            }

            finish();

        });
        AddSound.setOnClickListener(view -> {

            Intent intent = new Intent(getApplicationContext(), SoundActivity.class);
            startActivity(intent);
            Animatoo.animateCard(BaseCameraActivity.this);
            finish();
        });



        puseBtn.setOnClickListener(view -> {

            GPUCameraRecorder.stop();

            if (soundUrl != null){
                try {
                    mediaPlayer.stop();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }

            AddSound.setText("Add Sound");

        });

        recordBtn.setOnClickListener(v -> {


            if (recordBtn.getText().equals(getString(R.string.app_record))) {
                if (mediaPlayer!=null){
                    mediaPlayer.release();
                    mediaPlayer.stop();
                }

                filepath = getVideoFilePath();
                GPUCameraRecorder.start(filepath);
                recordBtn.setText("Stop");

                lv.setVisibility(View.GONE);
            } else {
                GPUCameraRecorder.stop();
                recordBtn.setText(getString(R.string.app_record));
                lv.setVisibility(View.VISIBLE);
                Toast.makeText(BaseCameraActivity.this,"you can only Recored for 30 seconds ",Toast.LENGTH_LONG).show();

                if (soundUrl != null){
                    Toast.makeText(getApplicationContext(),""+soundUrl,Toast.LENGTH_LONG).show();
                    try {
                        mediaPlayer = new MediaPlayer();
                        mediaPlayer.setDataSource(soundUrl);
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    }catch (IOException e){
                        Toast.makeText(getApplicationContext(),""+e.toString(),Toast.LENGTH_LONG).show();

                    }

                }

            }

            ///////////////////stop recorder in 30 seconds ///////////////////
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    GPUCameraRecorder.stop();

                    if (soundUrl != null){
                        try {
                            mediaPlayer.stop();
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        }
                    }

                    AddSound.setText("Add Sound");
                    recordBtn.setText(R.string.app_record);
                }
            },30000);
            ///////////////////stop recorder in 30 seconds ///////////////////



        });
        findViewById(R.id.btn_flash).setOnClickListener(v -> {
            if (GPUCameraRecorder != null && GPUCameraRecorder.isFlashSupport()) {
                GPUCameraRecorder.switchFlashMode();
                GPUCameraRecorder.changeAutoFocus();
            }
        });

        findViewById(R.id.btn_switch_camera).setOnClickListener(v -> {
            releaseCamera();
            if (lensFacing == LensFacing.BACK) {
                lensFacing = LensFacing.FRONT;
            } else {
                lensFacing = LensFacing.BACK;
            }
            toggleClick = true;
        });

        findViewById(R.id.btn_image_capture).setOnClickListener(v -> {
            captureBitmap(bitmap ->  {
                new Handler().post(() -> {
                    String imagePath = getImageFilePath();
                    Toast.makeText(getApplicationContext(),"At  = 95 " + imagePath,Toast.LENGTH_LONG).show();
                    saveAsPngImage(bitmap, imagePath);
                    Toast.makeText(getApplicationContext(),"At  = 97 " + imagePath,Toast.LENGTH_LONG).show();
                    exportPngToGallery(getApplicationContext(), imagePath);
                });
            });
        });

        lv = findViewById(R.id.filter_list);

        final List<FilterType> filterTypes = FilterType.createFilterList();
        FilterAdapter filterAdapter = new FilterAdapter(this, R.layout.row_white_text, filterTypes);
        lv.setAdapter(filterAdapter.whiteMode());
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (GPUCameraRecorder != null) {
                    GPUCameraRecorder.setFilter(FilterType.createGlFilter(filterTypes.get(position), getApplicationContext()));
                }
            }
        });

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseCamera();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mediaPlayer.stop();
    }

    private void releaseCamera() {
        if (sampleGLView != null) {
            sampleGLView.onPause();
        }

        if (GPUCameraRecorder != null) {
            GPUCameraRecorder.stop();
            GPUCameraRecorder.release();
            GPUCameraRecorder = null;
        }

        if (sampleGLView != null) {
            ((FrameLayout) findViewById(R.id.wrap_view)).removeView(sampleGLView);
            sampleGLView = null;
        }
    }


    private void setUpCameraView() {
        runOnUiThread(() -> {
            FrameLayout frameLayout = findViewById(R.id.wrap_view);
            frameLayout.removeAllViews();
            sampleGLView = null;
            sampleGLView = new SampleCameraGLView(getApplicationContext());
            sampleGLView.setTouchListener((event, width, height) -> {
                if (GPUCameraRecorder == null) return;
                GPUCameraRecorder.changeManualFocusPoint(event.getX(), event.getY(), width, height);
            });
            frameLayout.addView(sampleGLView);
        });
    }


    private void setUpCamera() {
        setUpCameraView();

        GPUCameraRecorder = new GPUCameraRecorderBuilder(this, sampleGLView)
                //.recordNoFilter(true)
                .cameraRecordListener(new CameraRecordListener() {
                    @Override
                    public void onGetFlashSupport(boolean flashSupport) {
                        runOnUiThread(() -> {
                            findViewById(R.id.btn_flash).setEnabled(flashSupport);
                        });
                    }

                    @Override
                    public void onRecordComplete() {
                        exportMp4ToGallery(getApplicationContext(), filepath);
                    }

                    @Override
                    public void onRecordStart() {
                        runOnUiThread(() -> {
                            lv.setVisibility(View.GONE);
                        });
                    }

                    @Override
                    public void onError(Exception exception) {
                        Log.e("GPUCameraRecorder", exception.toString());
                    }

                    @Override
                    public void onCameraThreadFinish() {
                        if (toggleClick) {
                            runOnUiThread(() -> {
                                setUpCamera();
                            });
                        }
                        toggleClick = false;
                    }

                    @Override
                    public void onVideoFileReady() {

                    }
                })
                .videoSize(videoWidth, videoHeight)
                .cameraSize(cameraWidth, cameraHeight)
                .lensFacing(lensFacing)
                .build();


    }

//    private void changeFilter(Filters filters) {
//        GPUCameraRecorder.setFilter(Filters.getFilterInstance(filters, getApplicationContext()));
//    }


    private interface BitmapReadyCallbacks {
        void onBitmapReady(Bitmap bitmap);
    }

    private void captureBitmap(final BitmapReadyCallbacks bitmapReadyCallbacks) {
        sampleGLView.queueEvent(() -> {
            EGL10 egl = (EGL10) EGLContext.getEGL();
            GL10 gl = (GL10) egl.eglGetCurrentContext().getGL();
            Bitmap snapshotBitmap = createBitmapFromGLSurface(sampleGLView.getMeasuredWidth(), sampleGLView.getMeasuredHeight(), gl);

            runOnUiThread(() -> {
                bitmapReadyCallbacks.onBitmapReady(snapshotBitmap);
            });
        });
    }

    private Bitmap createBitmapFromGLSurface(int Width, int Height, GL10 gl) {

        int bitmapBuffer[] = new int[Width * Height]; //for Pixels
        int bitmapSource[] = new int[Width * Height];
        IntBuffer intBuffer = IntBuffer.wrap(bitmapBuffer);
        intBuffer.position(0);

        try {
            gl.glReadPixels(0, 0, Width, Height, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, intBuffer);
            int offset1, offset2, texturePixel, blue, red, pixel;
            for (int i = 0; i < Height; i++) {
                offset1 = i * Width;
                offset2 = (Height - i - 1) * Width;
                for (int j = 0; j < Width; j++) {
                    texturePixel = bitmapBuffer[offset1 + j];
                    blue = (texturePixel >> 16) & 0xff;
                    red = (texturePixel << 16) & 0x00ff0000;
                    pixel = (texturePixel & 0xff00ff00) | red | blue;
                    bitmapSource[offset2 + j] = pixel;
                }
            }
        } catch (GLException e) {
            Log.e("CreateBitmap", "createBitmapFromGLSurface: " + e.getMessage(), e);
            Toast.makeText(getApplicationContext()," At 261  " + e.getMessage(),Toast.LENGTH_LONG).show();
            return null;
        }

        return Bitmap.createBitmap(bitmapSource, Width, Height, Bitmap.Config.ARGB_8888);
    }

    public void saveAsPngImage(Bitmap bitmap, String filePath) {
        try {
            File file = new File(filePath);
            Toast.makeText(getApplicationContext(),"File Created = " + file.createNewFile(),Toast.LENGTH_LONG).show();
            if (file.createNewFile()) {
                FileOutputStream outStream = new FileOutputStream(file);
                Toast.makeText(getApplicationContext(),"File Created = " + outStream.toString(),Toast.LENGTH_LONG).show();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                outStream.close();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext()," At 277 " + e.getMessage(),Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext()," At 280 " + e.getMessage(),Toast.LENGTH_LONG).show();

        }
    }


    public static void exportMp4ToGallery(Context context, String filePath) {
        final ContentValues values = new ContentValues(2);
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
        values.put(MediaStore.Video.Media.DATA, filePath);
        context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + filePath)));
    }

    //name of Video
    public static String getVideoFilePath() {
        return getAndroidMoviesFolder().getAbsolutePath() + "/" + new SimpleDateFormat("yyyyMM_dd-HHmmss").format(new Date()) + "GPUCameraRecorder.mp4";
    }

    public static File getAndroidMoviesFolder() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
    }

    //to get image form Gallery File
    private static void exportPngToGallery(Context context, String filePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        mediaScanIntent.getData();
        File file = new File(filePath);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);

        /////////////////////////////////////////////////////////
//        final ContentValues values = new ContentValues(2);
//        values.put(MediaStore.Images.Media.MIME_TYPE, "image/*");
//        values.put(MediaStore.Images.Media.DATA, filePath);
//        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + filePath)));
        /////////////////////////////////////////////////////////////////
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,filePath);
        values.put(MediaStore.Images.Media.DESCRIPTION,"FromCamera");
        Uri imgUri   = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        Toast.makeText(context,""+imgUri,Toast.LENGTH_LONG).show();
        ///////////////////////////////////////////////////////////////

    }

    //name of image
//    public static String getImageFilePath() {
//        return getAndroidImageFolder().getAbsolutePath()  + "GPUCameraRecorder.png";
//    }
     //get file path of image
    public static String getImageFilePath() {
        return getAndroidImageFolder().getAbsolutePath() + "/" + new SimpleDateFormat("yyyyMM_dd-HHmmss").format(new Date()) + "GPUCameraRecorder.png";
    }

    public static File getAndroidImageFolder() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    }


}

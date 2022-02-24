package com.doubleclick.tiktok;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.Manifest;
import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.doubleclick.tiktok.Adapters.DemoAdapter;
import com.doubleclick.tiktok.MainRecyclerView.VerticalSpacingItemDecorator;
import com.doubleclick.tiktok.MainRecyclerView.VideoPlayerRecyclerAdapter;
import com.doubleclick.tiktok.MainRecyclerView.VideoPlayerRecyclerView;
import com.doubleclick.tiktok.Model.MediaObject;
import com.doubleclick.tiktok.Model.Responses.ApiClient;
import com.doubleclick.tiktok.Model.Responses.ApiInterface;
import com.doubleclick.tiktok.Model.Responses.RetrivingData;
import com.doubleclick.tiktok.VideoEditorFolder.PortraitCameraActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {


    private DemoAdapter demoAdapter;

    private VideoPlayerRecyclerView recyclerView;
    ArrayList<MediaObject> mediaObjects = new ArrayList<>();
    //    private static ApiInterface apiInterface;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseStorage storage;
    private TextView discription;
    private static final int CAMERA_PREMAISSION_REQUEST_CODE = 88888;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        discription = findViewById(R.id.discription);


//        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();

        init();

    }

    private void init() {

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlags(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlags(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
///////////////////////recyclerView//////////////////////////////////
        recyclerView = findViewById(R.id.RecyclerViewSwiper);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(0);
        recyclerView.addItemDecoration(itemDecorator);
        recyclerView.setLayoutManager(linearLayoutManager);
        ///////////////////////////////////////////////////////////////
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        ////////////////////////////////////////////////////////////////////////
//        LoadAllPostes();


//        try {
//            makeHttpRequest(new URL("http://www.rojkharido.com/tiktok/posts.php"));

//        }
//        catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        reference.child("MediaObject").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    ArrayList<RetrivingData> mediaObjects = new ArrayList<>();
                    RetrivingData mediaObject;

//                    String data = snapshot.getValue().toString();
//                    Toast.makeText(HomeActivity.this,""+snapshot.getValue(),Toast.LENGTH_LONG).show();
//                    Log.e("Data ==  ","" + data);
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        mediaObject = dataSnapshot.getValue(RetrivingData.class);
/*
//
//
//                    try {
//                        mediaObjects   = getDataFromJSON(snapshot.getValue().toString());
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
*/
                        long s = snapshot.getChildrenCount();
                        mediaObjects.add(mediaObject);
//
                    }
                    recyclerView.setMediaObjects(mediaObjects);
                    VideoPlayerRecyclerAdapter adapter = new VideoPlayerRecyclerAdapter(mediaObjects, initGlide());
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    recyclerView.setKeepScreenOn(true);
                    recyclerView.smoothScrollToPosition(mediaObjects.size() + 1);

                } else {
                    Toast.makeText(HomeActivity.this, "Network Field.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private ArrayList<MediaObject> getDataFromJSON(String json) throws JSONException {
        ArrayList<MediaObject> mediaObjects = new ArrayList<>();

        JSONArray baseJSON = new JSONArray(json);
        JSONObject All_POSTS = baseJSON.getJSONObject(0);

//            if (All_POSTS.length() >  0) {
//                for (int x = 0; x < All_POSTS.length();x++) {
//                    JSONObject allData = All_POSTS.getJSONObject(0);
        String title = All_POSTS.getString("title");
        String date = All_POSTS.getString("date");
        String description = All_POSTS.getString("description");
        String media_url = All_POSTS.getString("media_url");
        String post_categories = All_POSTS.getString("post_categories");
        String post_id = All_POSTS.getString("post_id");
        String thumbnail = All_POSTS.getString("thumbnail");
        String user_id = All_POSTS.getString("user_id");

        Toast.makeText(HomeActivity.this, "" + media_url, Toast.LENGTH_LONG).show();

        mediaObjects.add(new MediaObject(title, description, date, user_id, post_categories, post_id, media_url, thumbnail));
//                }
//            }

        return mediaObjects;
    }

//    private void LoadAllPostes() {
//////        loader.setVisibility(View.VISIBLE);
////
////        Call<Users> call = apiInterface.performAllPostes();
////
////        call.enqueue(new Callback<Users>() {
////            @Override
////            public void onResponse(Call<Users> call, Response<Users> response) {
////
////                if (response.isSuccessful()) {
//////                    loader.setVisibility(View.GONE);
////                    ArrayList<MediaObject> mediaObjects =  response.body().getAll_Postes();
////
////                    recyclerView.setMediaObjects(mediaObjects);
////                    VideoPlayerRecyclerAdapter adapter = new VideoPlayerRecyclerAdapter(mediaObjects, initGlide());
////                    recyclerView.setAdapter(adapter);
////                    adapter.notifyDataSetChanged();
////                    recyclerView.setKeepScreenOn(true);
////                    recyclerView.smoothScrollToPosition(mediaObjects.size() + 1);
////                } else {
//////                    loader.setVisibility(View.GONE);
////                    Toast.makeText(HomeActivity.this, "Network Field.", Toast.LENGTH_SHORT).show();
////                }
////            }
////
////            @Override
////            public void onFailure(Call<Users> call, Throwable t) {
////                Toast.makeText(HomeActivity.this, "Network  Error Connect ."+t, Toast.LENGTH_SHORT).show();
//////                loader.setVisibility(View.GONE);
////            }
////        });
//
////        loader.setVisibility(View.VISIBLE);
//
////        Call<Users> call = apiInterface.performAllPostes();
////        call.enqueue(new Callback<Users>() {
////            @Override
////            public void onResponse(Call<Users> call, Response<Users> response) {
////
////                if(response.isSuccessful())
////                {
//////                    loader.setVisibility(View.GONE);
////                    mediaObjects =  response.body().getAll_Postes();
////
////                    recyclerView.setMediaObjects(mediaObjects);
////                    VideoPlayerRecyclerAdapter adapter = new VideoPlayerRecyclerAdapter(mediaObjects, initGlide());
////                    recyclerView.setAdapter(adapter);
////                    adapter.notifyDataSetChanged();
////                    recyclerView.setKeepScreenOn(true);
////                    recyclerView.smoothScrollToPosition(mediaObjects.size()+1);
////                }
////                else
////                {
//////                    loader.setVisibility(View.GONE);
////                    Toast.makeText(HomeActivity.this, "Network Error.", Toast.LENGTH_SHORT).show();
////                }
////            }
////
////            @Override
////            public void onFailure(Call<Users> call, Throwable t) {
////                Toast.makeText(HomeActivity.this, "Network Error. == "+ t, Toast.LENGTH_SHORT).show();
////                Log.e("Error = ",""+t);
//////                loader.setVisibility(View.GONE);
////            }
////        });
//    }

    public static void setWindowFlags(Activity activity, final int bits, boolean on) {

        Window window = activity.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        if (on) {
            layoutParams.flags |= bits;
        } else {
            layoutParams.flags &= bits;
        }
        window.setAttributes(layoutParams);


    }

    private RequestManager initGlide() {
        RequestOptions options = new RequestOptions()
                .placeholder(R.color.white)
                .error(R.color.red);

        return Glide.with(this)
                .setDefaultRequestOptions(options);
    }

    @Override
    protected void onDestroy() {
        if (recyclerView != null)
            recyclerView.releasePlayer();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (recyclerView != null)
            recyclerView.releasePlayer();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

/*    @Override
*    public void overridePendingTransition(int enterAnim, int exitAnim) {
*       super.overridePendingTransition(enterAnim, exitAnim);
   }*/

    public void FollowingBtn(View view) {
        Intent intent = new Intent(HomeActivity.this, FollowingActivity.class);
        startActivity(intent);
//        overridePendingTransition(R.anim.animate_fade_enter,R.anim.animate_fade_exit);
        Animatoo.animateDiagonal(HomeActivity.this);
        finish();

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermission();
    }

    public void AddBtn(View view) {
        checkPermission();
        GoToPortraitCameraActivity();
    }

    private void checkPermission() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return;
        }

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WAKE_LOCK},
                    CAMERA_PREMAISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case CAMERA_PREMAISSION_REQUEST_CODE:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(HomeActivity.this, "permission has been granded", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(HomeActivity.this, "permission has been Denied", Toast.LENGTH_SHORT).show();
                }
                break;

        }

    }

    private void GoToPortraitCameraActivity(){
        Intent intent = new Intent(HomeActivity.this, PortraitCameraActivity.class);
        startActivity(intent);
//        overridePendingTransition(R.anim.animate_fade_enter,R.anim.animate_fade_exit);
        Animatoo.animateSlideUp(HomeActivity.this);
        finish();
    }

    public void accountPage(View view) {
        Intent intent = new Intent(HomeActivity.this,AccountActivity.class);
        startActivity(intent);
        Animatoo.animateSplit(HomeActivity.this);
        finish();

    }

    public void discoverPage(View view) {

        Intent intent = new Intent(HomeActivity.this,DiscoverActivity.class);
        startActivity(intent);
        Animatoo.animateSplit(HomeActivity.this);
        finish();
    }

    public void MassegPage(View view) {

        Intent intent = new Intent(HomeActivity.this,MassegeActivity.class);
        startActivity(intent);
        Animatoo.animateSplit(HomeActivity.this);
        finish();
    }


    /*
    public static final String feachData(String s){
        URL url = createUrl(s);
        String jsoResponse = null;
        try {
            jsoResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e("HomeActivity  == ", ""+e);
        }
        return jsoResponse;
    }

    private static URL createUrl(String s) {
        URL url =null;
        try {
            url  = new URL(s);
        } catch (MalformedURLException e) {
            Log.e("HomeActivity  == ", ""+e);
        }
        return url;
    }


    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url==null){
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode()==200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else {
//                Toast.makeText(HomeActivity.this,"Error",Toast.LENGTH_LONG).show();
            }


        }catch(IOException e){
//            Toast.makeText(HomeActivity.this,"Error"+e,Toast.LENGTH_LONG).show();
            Log.e("HomeActivty ","problem to retriving data ");

        }finally {
            if (urlConnection!=null){
                urlConnection.disconnect();
           }
            if (inputStream!=null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream!=null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line!=null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
    */

}
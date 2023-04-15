package com.example.loginform;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class NextScreen extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private GestureDetectorCompat gestureDetectorCompat;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!isInternetAvailable()){
            Toast.makeText(getApplicationContext(), "opps!", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.activity_nextscreen_noiternet);
            init();
            gestureDetectorCompat = new GestureDetectorCompat(this, new gestureListener());

        }
        else {
            setContentView(R.layout.activity_next_screen);
            init();
            process();
        }
    }

    private  class gestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(!isInternetAvailable()){
//            progressDoalog.dismiss();
                //inflater
                Toast.makeText(getApplicationContext(), "opps!", Toast.LENGTH_SHORT).show();
                setContentView(R.layout.activity_nextscreen_noiternet);
                init();
            }else {
                setContentView(R.layout.activity_next_screen);
                init();
                process();
            }            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetectorCompat.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
    public void txtRetryClicked(View view){
        if(!isInternetAvailable()){
            Toast.makeText(getApplicationContext(), "opps!", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.activity_nextscreen_noiternet);
            init();
        }else {
            setContentView(R.layout.activity_next_screen);
            init();
            process();
        }
    }
    private boolean isInternetAvailable(){
        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
    private void init(){

    }
    private void process(){
        firebaseAuth = FirebaseAuth.getInstance();
        try {
            currentUserId = firebaseAuth.getCurrentUser().getUid();
        }catch (Exception e){
            Log.d("TAG", "MSG: "+e.toString());
        }
        if (currentUserId == null) {
            startActivity(new Intent(NextScreen.this, MainActivity.class));
            finish();
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menubar, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.sign_out:
                signOut();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void signOut(){
        firebaseAuth.signOut();
        startActivity(new Intent(NextScreen.this, MainActivity.class));
        finish();
    }
}

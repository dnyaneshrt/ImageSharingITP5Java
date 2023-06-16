package com.itp.imagesharingappjava;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Button btn_loadImage;
    FloatingActionButton floatingActionButton;
    ImageView imageView;
    ProgressBar progressBar;
    String imageurl=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       btn_loadImage= findViewById(R.id.btn_loadImage);
       floatingActionButton=findViewById(R.id.floatingActionButton);
       imageView=findViewById(R.id.imageView);
       progressBar=findViewById(R.id.progressBar);

       loadImages();
       btn_loadImage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               loadImages();
           }
       });

       floatingActionButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Snackbar.make(view,"I am floating action button",Snackbar.LENGTH_LONG).show();

               /*Create an ACTION_SEND Intent*/
               Intent intent = new Intent(android.content.Intent.ACTION_SEND);
               /*The type of the content is text, obviously.*/
               intent.setType("text/plain");
               /*Applying information Subject and Body.*/
               intent.putExtra(android.content.Intent.EXTRA_TEXT, "welcome to our Image sharing app:-----> "+imageurl);
               /*Fire!*/
               startActivity(Intent.createChooser(intent,"share this image using...."));
           }
       });
    }
//code to call an API using volley api librabry...
    private void loadImages() {
        progressBar.setVisibility(View.VISIBLE);

// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://meme-api.com/gimme";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                           imageurl =response.getString("url");
                            Glide.with(MainActivity.this).load(imageurl).addListener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                            Toast.makeText(MainActivity.this, "failed to load the image", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                            progressBar.setVisibility(View.GONE);
                                            return false;
                                        }
                                    }).into(imageView);

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "unable to get response from api: "+error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

       // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }
}
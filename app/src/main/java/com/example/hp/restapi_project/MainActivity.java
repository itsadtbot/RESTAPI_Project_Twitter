package com.example.hp.restapi_project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.SearchService;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity {

    private long lastId = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Twitter.initialize(this);
        setContentView(R.layout.activity_main);
        final TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        StatusesService statusesService = twitterApiClient.getStatusesService();

        /*Call<Tweet> call = statusesService.show(524971209851543553L, null, null, null);
        call.enqueue(new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                TextView TV = (TextView)findViewById(R.id.textView2);
                TV.setText(result.data.idStr);
            }

            public void failure(TwitterException exception) {
                TextView TV = (TextView)findViewById(R.id.textView2);
                TV.setText(R.string.failure);
            }
        });*/
        Button button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText editText= (EditText) findViewById(R.id.editText);
                String qstr;
                qstr=editText.getText().toString();
                SearchService searchService = twitterApiClient.getSearchService();
                Call<Search> call;
                if (lastId == 0) {
                    call = searchService.tweets(qstr, null, null, null, null, 15, null, null, null, null);
                } else {
                    call = searchService.tweets(qstr, null, null, null, null, 15, null, lastId, null, null);
                }
                    call.enqueue(new Callback<Search>() {
                    @Override
                    public void success(Result<Search> result) {
                        if(result.data.tweets.size()==0) {
                            TextView TV = (TextView) findViewById(R.id.textView2);
                            TV.setText("No tweets found");
                        }
                        else {
                            TextView TV = (TextView) findViewById(R.id.textView2);
                            TV.setText(result.data.tweets.get(0).text);
                            lastId = result.data.tweets.get(0).id;
                        }
                    }

                    public void failure(TwitterException exception) {
                        TextView TV = (TextView)findViewById(R.id.textView2);
                        TV.setText(R.string.failure);
                    }
                });
            }
        });


    }

}

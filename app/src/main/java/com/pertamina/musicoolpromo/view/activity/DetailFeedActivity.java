package com.pertamina.musicoolpromo.view.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pertamina.musicoolpromo.R;
import com.pertamina.musicoolpromo.model.Feed.NewsFeedResult;
import com.pertamina.musicoolpromo.model.promo.PromoResult;
import com.pertamina.musicoolpromo.view.base.BaseActivity;
import com.uncopt.android.widget.text.justify.JustifiedTextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import static com.pertamina.musicoolpromo.view.utilities.GlobalString.Preferences.INTENT_EXTRA_FEED;

public class DetailFeedActivity extends BaseActivity implements Html.ImageGetter {

    public static final String EXTRA_DATA_FEED = "extra_data_feed";
    public static final String EXTRA_DATA_PROMO = "extra_data_promo";
    private NewsFeedResult newsFeedResult;
    private PromoResult promoResult;

    private ImageView imageFeed;
    private TextView titleFeed;
    private JustifiedTextView resumeFeed;
    private TextView datefeed;
    private TextView timefeed;
    private Drawable empty;

    private String type;

    Toolbar toolbar;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_feed);

        setUpView();
        setupListener();
        generateView();

    }

    @Override
    public void setUpView() {

        type = getIntent().getStringExtra(INTENT_EXTRA_FEED);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(type);
        getSupportActionBar().setElevation(0);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);

        imageFeed = findViewById(R.id.image_detail_feed);
        titleFeed = findViewById(R.id.title_detail_feed);
        resumeFeed = findViewById(R.id.description_detail_feed);
        datefeed = findViewById(R.id.date_detail);
        timefeed = findViewById(R.id.time_detail);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void generateView() {

        if (type.equals("News")) {

            newsFeedResult = getIntent().getParcelableExtra(EXTRA_DATA_FEED);
            Glide.with(this)
                    .load(newsFeedResult.getImages())
                    .into(imageFeed);
            titleFeed.setText(newsFeedResult.getTitle());
            Spanned spanned = Html.fromHtml(newsFeedResult.getResume(), DetailFeedActivity.this, null);
            resumeFeed.setText(spanned);
            resumeFeed.setMovementMethod(LinkMovementMethod.getInstance());


            String string = newsFeedResult.getCreated_at();
            String[] parts = string.split(" ");
            String part1 = parts[0];
            String part2 = parts[1];
            datefeed.setText(part1);
            timefeed.setText(part2);
        }else {
            promoResult = getIntent().getParcelableExtra(EXTRA_DATA_PROMO);
            Glide.with(this)
                    .load(promoResult.getImages())
                    .into(imageFeed);
            titleFeed.setText(promoResult.getTitle());
            Spanned spanned = Html.fromHtml(promoResult.getDescription(), DetailFeedActivity.this, null);
            resumeFeed.setText(spanned);
            resumeFeed.setMovementMethod(LinkMovementMethod.getInstance());

            String string = promoResult.getCreated_at();
            String[] parts = string.split(" ");
            String part1 = parts[0];
            String part2 = parts[1];
            datefeed.setText(part1);
            timefeed.setText(part2);
        }
    }

    @Override
    public void setupListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public Drawable getDrawable(String s) {
        LevelListDrawable d = new LevelListDrawable();
        empty = getResources().getDrawable(R.drawable.background_feed);
        d.addLevel(0, 0, empty);
        d.setBounds(0, 0, 200, 200);
        new LoadImage().execute(s, d);
        return d;
    }

    class LoadImage extends AsyncTask<Object, Void, Bitmap> {
        private LevelListDrawable mDrawable;

        @Override
        protected Bitmap doInBackground(Object... params) {
            String source = (String) params[0];
            mDrawable = (LevelListDrawable) params[1];
            Log.d("lol", "doInBackground " + source);
            try {
                InputStream is = new URL(source).openStream();
                return BitmapFactory.decodeStream(is);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                BitmapDrawable d = new BitmapDrawable(bitmap);
                mDrawable.addLevel(1, 1, d);
                //mDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                mDrawable.setBounds(0, 0, empty.getMinimumWidth(), bitmap.getHeight());
                mDrawable.setLevel(1);
                CharSequence t = resumeFeed.getText();
                resumeFeed.setText(t);
            }
        }
    }
}

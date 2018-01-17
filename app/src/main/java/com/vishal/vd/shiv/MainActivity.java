package com.vishal.vd.shiv;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.imangazaliev.circlemenu.CircleMenu;
import com.imangazaliev.circlemenu.CircleMenuButton;
import com.vishal.vd.shiv.activity.MainImageActivity;
import com.vishal.vd.shiv.ytubevideos.ChannelActivity;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener {

    SliderLayout sliderLayout ;

    HashMap<String, String> HashMapForURL ;

    HashMap<String, Integer> HashMapForLocalRes ;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.ringtone);
                mp.start();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        final ViewGroup snackbarContainer = findViewById(R.id.snackbar_contaner);
        final CircleMenu circleMenu = findViewById(R.id.circle_menu);
        circleMenu.setOnItemClickListener(new CircleMenu.OnItemClickListener() {
            @Override
            public void onItemClick(CircleMenuButton menuButton) {
                Snackbar.make(snackbarContainer, menuButton.getHintText(), Snackbar.LENGTH_LONG).show();
            }
        });

        circleMenu.setOnItemClickListener(new CircleMenu.OnItemClickListener() {
            @Override
            public void onItemClick(CircleMenuButton menuButton) {
                switch (menuButton.getId()) {
                    case R.id.events:
                        intent = new Intent(getApplicationContext(),DisplayData.class);
                        startActivity(intent);
                        break;
                    case R.id.team:
                        //swapImages();
                        break;
                    case R.id.web:
                        intent = new Intent(getApplicationContext(),WebsearchActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.photos:
                        //shareImage();



                        break;
                    case R.id.video:
                        //shareImage();
                        intent = new Intent(getApplicationContext(),ChannelActivity.class);
                        startActivity(intent);

                        break;

                }
            }
        });
        circleMenu.setEventListener(new CircleMenu.EventListener() {
            @Override
            public void onMenuOpenAnimationStart() {
                Log.d("CircleMenuStatus", "onMenuOpenAnimationStart");
            }

            @Override
            public void onMenuOpenAnimationEnd() {
                Log.d("CircleMenuStatus", "onMenuOpenAnimationEnd");
            }

            @Override
            public void onMenuCloseAnimationStart() {
                Log.d("CircleMenuStatus", "onMenuCloseAnimationStart");
            }

            @Override
            public void onMenuCloseAnimationEnd() {
                Log.d("CircleMenuStatus", "onMenuCloseAnimationEnd");
            }

            @Override
            public void onButtonClickAnimationStart(@NonNull CircleMenuButton menuButton) {
                Log.d("CircleMenuStatus", "onButtonClickAnimationStart");
            }

            @Override
            public void onButtonClickAnimationEnd(@NonNull CircleMenuButton menuButton) {
                Log.d("CircleMenuStatus", "onButtonClickAnimationEnd");
            }

        });

        sliderLayout = (SliderLayout)findViewById(R.id.slider);

        //Call this method if you want to add images from URL .
        AddImagesUrlOnline();

        //Call this method to add images from local drawable folder .
        //AddImageUrlFormLocalRes();

        //Call this method to stop automatic sliding.
        //sliderLayout.stopAutoCycle();

        for(String name : HashMapForURL.keySet()){

            TextSliderView textSliderView = new TextSliderView(MainActivity.this);

            textSliderView
                    .description(name)
                    .image(HashMapForURL.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            textSliderView.bundle(new Bundle());

            textSliderView.getBundle()
                    .putString("extra",name);

            sliderLayout.addSlider(textSliderView);
        }
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.DepthPage);

        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);

        sliderLayout.setCustomAnimation(new DescriptionAnimation());

        sliderLayout.setDuration(3000);

        sliderLayout.addOnPageChangeListener(MainActivity.this);

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            SharedPreferences sp=getSharedPreferences("login",MODE_PRIVATE);
            SharedPreferences.Editor e=sp.edit();
            e.clear();
            e.commit();

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_events) {
            intent = new Intent(getApplicationContext(),DisplayData.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            intent = new Intent(getApplicationContext(),MainImageActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_videos) {
            intent = new Intent(getApplicationContext(),ChannelActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_team) {

        } else if (id == R.id.nav_web) {
            intent = new Intent(getApplicationContext(),WebsearchActivity.class);
            startActivity(intent);

        }else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_admin) {
            intent = new Intent(getApplicationContext(),AdminLoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    protected void onStop() {

        sliderLayout.stopAutoCycle();

        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

        Toast.makeText(this,slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {

        Log.d("Slider Demo", "Page Changed: " + position);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void AddImagesUrlOnline(){

        HashMapForURL = new HashMap<String, String>();

        HashMapForURL.put("1", "https://pbs.twimg.com/media/B5jUak0CUAA86Xn.jpg:large");
        HashMapForURL.put("2", "https://i.pinimg.com/originals/a4/d0/75/a4d075eaecfd16c801a93b198239a1a3.jpg");
        HashMapForURL.put("3", "https://i.ytimg.com/vi/Yu7IpufScfQ/maxresdefault.jpg");
        HashMapForURL.put("4", "https://i.pinimg.com/originals/58/e2/f6/58e2f6e2ebb018b213d79c09249e0edd.jpg");
        HashMapForURL.put("5", "http://4.bp.blogspot.com/-6E6jSvPys00/USIIhKk5fYI/AAAAAAAAIRY/tKSO5rfRvcQ/s1600/shivaji-maharaj-photo-i16.jpg");
    }

   /* public void AddImageUrlFormLocalRes(){

        HashMapForLocalRes = new HashMap<String, Integer>();

        HashMapForLocalRes.put("CupCake", R.drawable.cupcake);
        HashMapForLocalRes.put("Donut", R.drawable.donut);
        HashMapForLocalRes.put("Eclair", R.drawable.eclair);
        HashMapForLocalRes.put("Froyo", R.drawable.froyo);
        HashMapForLocalRes.put("GingerBread", R.drawable.gingerbread);

    }*/
}

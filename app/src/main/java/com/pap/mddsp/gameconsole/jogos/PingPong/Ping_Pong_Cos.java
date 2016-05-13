package com.pap.mddsp.gameconsole.jogos.PingPong;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import com.pap.mddsp.gameconsole.R;

public class Ping_Pong_Cos extends FragmentActivity implements
        ActionBar.TabListener {

    private ViewPager viewPager;
    public TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    // Tab titles
    private String[] tabs = {"Barra Player","Bola","Barra Computador"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ping__pong__cos);

        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this).setTag(tab_name));
            Log.d("Teste", tab_name);
        }


    hideSystemUI();


        /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                Log.d("Teste Tab:", "" + (position) + " length :" + (tabs.length - 1));
                if (position <= tabs.length - 1) {
                    actionBar.setSelectedNavigationItem(position);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                // remove the following flag for version < API 19
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }


    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        // on tab selected
        // show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }

}

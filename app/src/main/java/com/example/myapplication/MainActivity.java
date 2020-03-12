package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;




//import androidx.viewpager.widget.PagerAdapter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity implements Tab1.OnFragmentInteractionListener,Tab2.OnFragmentInteractionListener,Tab3.OnFragmentInteractionListener,Tab4.OnFragmentInteractionListener,Tab5.OnFragmentInteractionListener {

    String vulResult,detailsResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("Shared Preferences"));
        tabLayout.addTab(tabLayout.newTab().setText("Local Database Storage"));
        tabLayout.addTab(tabLayout.newTab().setText("Log Leak"));
        tabLayout.addTab(tabLayout.newTab().setText("Clipboard Data"));
        tabLayout.addTab(tabLayout.newTab().setText("Intent Spoofing"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager)findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        Intent intent = getIntent();
        vulResult= intent.getStringExtra("vulresult");
        detailsResult=intent.getStringExtra("detailsresult");
        final String packName = intent.getStringExtra("pack");

        Bundle bundle = new Bundle();
        bundle.putString("vulresult", vulResult);
        bundle.putString("detailsresult",detailsResult);

        Tab1 fragobj = new Tab1();
        fragobj.setArguments(bundle);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

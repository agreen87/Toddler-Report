package com.example.toddlerreport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import androidx.viewpager.widget.ViewPager;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    PassData passData = new PassData();

    String studentName;
    String phoneOne;
    String phoneTwo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main2);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        studentName = bundle.getString("studentName");
        phoneOne = bundle.getString("phoneOne");
        phoneTwo = bundle.getString("phoneTwo");

        System.out.println(studentName + " main2");

        passData.setName(studentName);
        passData.setPhoneOne(phoneOne);
        passData.setPhoneTwo(phoneTwo);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager viewPager = findViewById(R.id.viewPager);

        ArrayList<String> arrayList = new ArrayList<>();

        // Add title in array list
        arrayList.add("Toilet");
        arrayList.add("Food");
        arrayList.add("Sleep");


        // Setup tab layout
        tabLayout.setupWithViewPager(viewPager);

        // Prepare view pager
        prepareViewPager(viewPager, arrayList);
    }

    //  This causes the back button to work
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void prepareViewPager(ViewPager viewPager, ArrayList<String> arrayList) {

        // In order for your fragments to show you have to:

        // Initialize main adapter
        MainAdapter adapter = new MainAdapter(getSupportFragmentManager());

        // Initialize fragment
        Fragment fragment = new Fragment();

        // Ues for loop
        for (int i = 0; i < arrayList.size(); i++) {

            // Add the three fragments
            adapter.addFragment(fragment, arrayList.get(i));
            fragment = new Fragment();
        }
        viewPager.setAdapter(adapter);
    }

    private class MainAdapter extends FragmentPagerAdapter {

        // Initialize array list
        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        ArrayList<String> stringArrayList = new ArrayList<>();
        int[] imageList = {R.drawable.ic_toilet, R.drawable.ic_food, R.drawable.ic_sleep};

        // Create constructor
        public void addFragment(Fragment fragment, String s) {
            fragmentArrayList.add(fragment);

            // Add title
            stringArrayList.add(s);
        }

        public MainAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {

            Bundle bundle = new Bundle();
            bundle.putString("name", studentName);
            bundle.putString("phoneOne", phoneOne);
            bundle.putString("phoneTwo", phoneTwo);

            Fragment fragment = new Fragment();

            switch (position) {
                case 0:
                    fragment = new ToiletTab();
                    fragment.setArguments(bundle);
                    break;
                case 1:
                    fragment = new FoodTab();
                    fragment.setArguments(bundle);
                    break;
                case 2:
                    fragment = new SleepTab();
                    fragment.setArguments(bundle);
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Return fragment array list size
            return fragmentArrayList.size();
        }

        @NonNull
        @Override
        public CharSequence getPageTitle(int position) {
            // Initialize drawable
            Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), imageList[position]);

            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

            // Initialize spannable spring

            SpannableString spannableString = new SpannableString("  " +
                    stringArrayList.get(position));

            // Initialize image span
            ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);

            // Set span
            spannableString.setSpan(imageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            // Return spannable string
            return spannableString;
        }
    }
}



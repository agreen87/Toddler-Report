package com.example.toddlerreport;


import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdapter extends FragmentPagerAdapter {

    private PassData passData = new PassData();

    private int numberOfTabs;
    String name;

    public PageAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numberOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

      // String name =  passData.getName();

        // Main2Activity main2Activity = new Main2Activity();
       // name = main2Activity.getStudentName();

      //name = passData.getName();


     // System.out.println(name + " getName");
     // passData.showName();

        Fragment fragment;

       // name = passData.getName();

      //  System.out.println(name + " pageAdapter");

        Bundle bundle = new Bundle();
        bundle.putString("name", passData.getName());

        switch (position) {
            case 0:
                fragment = new ToiletTab();
                fragment.setArguments(bundle);
                break;
               // return new ToiletTab();
            case 1:
                fragment = new FoodTab();
                fragment.setArguments(bundle);
                break;
               // return new FoodTab();
            case 2:
                fragment = new SleepTab();
                fragment.setArguments(bundle);
                break;
                //return new SleepTab();
            default:
                fragment = null;
                break;
                    //return null;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}

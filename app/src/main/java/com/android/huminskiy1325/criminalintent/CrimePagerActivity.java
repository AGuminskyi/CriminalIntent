package com.android.huminskiy1325.criminalintent;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.UUID;

public class CrimePagerActivity extends FragmentActivity implements CrimeFragment.Callbacks{
    @Override
    public void OnCrimeUpdate(Crime crime) {

    }

    private ViewPager mViewPager;
    private ArrayList<Crime> mCrimes;
    private CrimeFragment crimeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_crime_pager);
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        mCrimes = CrimeLab.get(this).getCrimes();

        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
//                получает экземпляр Crime для заданной позиции в наборе данных, после чего
//                использует его идентификатор для создания и возвращения правильно настроен-
//                        ного экземпляра CrimeFragment.
                Crime crime = mCrimes.get(position);
                crimeFragment.newInstance(crime.getID());
                return CrimeFragment.newInstance(crime.getID());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

//        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
////                Crime crime = mCrimes.get(position);
////                if (crime.getTitle() != null) {
////                    setTitle(crime.getTitle());
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });

        UUID crimeId = (UUID)getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
        for (int i = 0; i < mCrimes.size(); i++){
            if(mCrimes.get(i).getID().equals(crimeId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
//        if(crimeFragment.backButtonWasPressed() == true)
            super.onBackPressed();

    }
}

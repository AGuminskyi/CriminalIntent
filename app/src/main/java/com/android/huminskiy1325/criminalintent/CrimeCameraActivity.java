package com.android.huminskiy1325.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by cubru on 07.02.2017.
 */

public class CrimeCameraActivity extends FragmentActivity {

   // ImageButton mPhotoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //hide window title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //hide panel state b and other level OC
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction()
                    .add(R.id.fragmentContainer, CrimeCameraFragment.newInstance())
                    .commit();
//                    .replace(R.id.fragmentContainer, CrimeCameraFragment.newInstance())
//                    .commit();
        }
    }
}

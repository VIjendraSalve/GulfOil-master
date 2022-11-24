package com.taraba.gulfoilapp;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by Mansi on 6/1/16.
 */
public class NotificationDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_details);

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setTitle("Grass Root");


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Bundle b = getIntent().getExtras();

        if (b == null) {
            Fragment notFrag1 = new NotificationDeatailFragment();
            Bundle bundle = new Bundle();
            bundle.putString("displayAction", "");
            notFrag1.setArguments(bundle);
            FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
            ft1.replace(R.id.container_body1, notFrag1);
            ft1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft1.addToBackStack(null);
            ft1.commit();
        } else {
            Fragment notFrag = new NotificationDeatailFragment();
            Bundle bundle = new Bundle();
            bundle.putString("displayAction", "");
            bundle.putSerializable("notifiy", b.getSerializable("notifiy"));
            bundle.putString("displayAction", "MechanicNotification");
            notFrag.setArguments(bundle);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container_body1, notFrag);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        getSupportFragmentManager().popBackStack();
        finish();
    }
}
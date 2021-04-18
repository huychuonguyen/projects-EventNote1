package com.example.mypersonalandroidproject;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.mypersonalandroidproject.fragment.AddEventFragment;
import com.example.mypersonalandroidproject.fragment.HomeFragment;

public class MainActivity extends AppCompatActivity {

    private ImageButton btnMenu;
    private TextView tvHome;
    private TextView tvAddEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    @Override
    public void onBackPressed() {
        Fragment addEventFragment = getSupportFragmentManager().findFragmentByTag(AddEventFragment.TAG);
        // kiểm tra nếu fragment đang show hiện tại là AddEventFragment
        // thì thoát app
        if(addEventFragment != null && addEventFragment.isVisible()) {
            this.finish();
        } else super.onBackPressed();
    }

    private void initView() {
        btnMenu = findViewById(R.id.btnMenu);
        tvHome = findViewById(R.id.tvHome);
        tvAddEvent = findViewById(R.id.tvAddEvent);

        btnMenu.setOnClickListener((view) -> {
            final LinearLayout lnMenu = findViewById(R.id.lnMenu);

            if(lnMenu.getVisibility() == View.VISIBLE)
                lnMenu.setVisibility(View.GONE);
            else   lnMenu.setVisibility(View.VISIBLE);
        });

        tvHome.setOnClickListener((view) -> {
            loadHomeFragment();
        });

        tvAddEvent.setOnClickListener((view) -> {
            loadAddEventFragment();
        });

        // mở app show AddEventFragment
       loadAddEventFragment();
    }

    private void loadHomeFragment() {
        Fragment homeFragment;
        // tìm fragment theo TAG
        // nếu tìm thấy thì pop ra và show lại
        // còn không thì tạo mới và add vào
        homeFragment = getSupportFragmentManager().findFragmentByTag(HomeFragment.TAG);
        if(homeFragment != null) {
            if(!homeFragment.isVisible())
                getSupportFragmentManager().popBackStack();
        } else {
            homeFragment = HomeFragment.newInstance("","");
            replaceFragment(homeFragment, HomeFragment.TAG);
        }
    }

    private void loadAddEventFragment() {
        Fragment addEventFragment;
        // tìm fragment theo TAG
        // nếu tìm thấy thì pop ra và show lại
        // còn không thì tạo mới và add vào
        addEventFragment = getSupportFragmentManager().findFragmentByTag(AddEventFragment.TAG);
        if(addEventFragment != null) {
            if(!addEventFragment.isVisible())
                getSupportFragmentManager().popBackStack();
        } else {
            addEventFragment = AddEventFragment.newInstance("","");
            replaceFragment(addEventFragment, AddEventFragment.TAG);
        }
    }

    private void replaceFragment(Fragment fragment, String tag) {
        // Create transaction
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack if needed
        transaction.replace(R.id.frameContainer, fragment, tag);
        transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    }
}
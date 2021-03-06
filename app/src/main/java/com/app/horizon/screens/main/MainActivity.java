package com.app.horizon.screens.main;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.widget.PopupMenu;

import com.app.horizon.HorizonMainApplication;
import com.app.horizon.R;
import com.app.horizon.core.base.BaseActivity;
import com.app.horizon.databinding.ActivityMainBinding;
import com.app.horizon.screens.authentication.LoginActivity;
import com.app.horizon.screens.main.home.category.CategoryFragment;
import com.app.horizon.screens.main.leaderboard.LeaderboardFragment;
import com.app.horizon.screens.main.profile.ProfileFragment;
import com.app.horizon.utils.BottomNavigationBehaviour;
import com.app.horizon.utils.ConnectivityReceiver;
import com.app.horizon.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;


public class MainActivity extends BaseActivity<MainActivityViewModel> implements
        PopupMenu.OnMenuItemClickListener {

    private ActivityMainBinding binding;
    @Inject
    MainActivityViewModel viewModel;

    public BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        Fragment fragment;
        switch (item.getItemId()) {
            case R.id.navigation_home:
                binding.title.setText(R.string.categories);
                fragment = new CategoryFragment();
                loadFragment(fragment);
                return true;
            case R.id.navigation_profile:
                binding.title.setText(R.string.profile);
                fragment = new ProfileFragment();
                loadFragment(fragment);
                return true;
            case R.id.navigation_leader:
                binding.title.setText(R.string.people);
                fragment = new LeaderboardFragment();
                loadFragment(fragment);
                return true;
        }
        return false;
    };


    @Override
    public MainActivityViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initBinding();

        //load the category fragment by default
        binding.title.setText(R.string.categories);
        loadFragment(new CategoryFragment());

        binding.menuBtn.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
            popupMenu.setOnMenuItemClickListener(MainActivity.this);
            popupMenu.inflate(R.menu.menu);
            popupMenu.show();
        });
    }

    private void initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Attaching bottom sheet behaviour - hide/show on scroll
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)
                binding.navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehaviour());
    }

    public void loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    //This won't show a blank page when android back press button is clicked from fragment
                    //.addToBackStack(null)
                    .replace(R.id.frame_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_logout:
                goLoginScreen();
                return true;
        }
        return false;
    }

    private void goLoginScreen() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}

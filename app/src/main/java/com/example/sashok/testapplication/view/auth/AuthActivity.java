package com.example.sashok.testapplication.view.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.sashok.testapplication.R;
import com.example.sashok.testapplication.network.model.auth.UserResponse;
import com.example.sashok.testapplication.utils.SessionManager;
import com.example.sashok.testapplication.view.main.MainActivity;

import java.util.ArrayList;
import java.util.List;


public class AuthActivity extends AppCompatActivity implements AuthListener {
    public static final String TAG = "TAG";
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_activity);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(LoginFragment.newInstance(), "Login");
        adapter.addFragment(RegistrationFragment.newInstance(), "Register");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onAuthSuccess(UserResponse user) {
        SessionManager.login(getApplicationContext(), user);
        startActivity(new Intent(this, MainActivity.class));

    }

    @Override
    public void onAuthError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}


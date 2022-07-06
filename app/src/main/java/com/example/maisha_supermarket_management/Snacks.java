package com.example.maisha_supermarket_management;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class Snacks extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    BottomNavigationView bottomNavigationView;

    Settings settingsFragment = new Settings();
    Favourites favouritesFragment = new Favourites();
    MyHome myHomeFragment = new MyHome();
    Chats chatsFragment = new Chats();
    OnCart onCartFragment = new OnCart();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_men);

        bottomNavigationView = findViewById(R.id.men_bottom_navigation_view);
        bottomNavigationView.setSelectedItemId(R.id.startHome);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        Settings settingsFragment = new Settings();
        Favourites favouritesFragment = new Favourites();
        MyHome myHomeFragment = new MyHome();
        Chats chatsFragment = new Chats();
        OnCart onCartFragment = new OnCart();

        viewPager = findViewById(R.id.MenViewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.MenTabs);
        tabLayout.setupWithViewPager(viewPager);
    }
    private void setupViewPager(ViewPager viewPager) {
        Snacks.ViewPagerAdapter adapter = new Snacks.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new SnackBites(), "Snacks and Bites");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.menContainer,settingsFragment).commit();
                return true;
            case R.id.favourites:
                getSupportFragmentManager().beginTransaction().replace(R.id.menContainer,favouritesFragment).commit();
                return true;
            case R.id.startHome:
                getSupportFragmentManager().beginTransaction().replace(R.id.menContainer,myHomeFragment).commit();
                return true;
//            case R.id.chats:
//                getSupportFragmentManager().beginTransaction().replace(R.id.menContainer,chatsFragment).commit();
//                return true;
            case R.id.cart:
                getSupportFragmentManager().beginTransaction().replace(R.id.menContainer,onCartFragment).commit();
                return true;
        }
        return false;
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

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//            super.destroyItem(container, position, object);
            ViewPager viewPager = (ViewPager) container;
//            View view = (View) object;
//            viewPager.removeView(view);
        }
    }
}
package com.example.darkflix;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.darkflix.ui.dashboard.DashboardFragment;
import com.example.darkflix.ui.home.HomeFragment;
import com.example.darkflix.ui.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Fragment> fragmentList = new ArrayList<>();
    BottomNavigationView bottomNavView;
    FrameLayout frameLayout;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavView = findViewById(R.id.nav_view);
        frameLayout = findViewById(R.id.nav_fragment);

        fragmentList.add(new HomeFragment());
        fragmentList.add(new DashboardFragment());
        fragmentList.add(new ProfileFragment());

        bottomNavView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setFragment(fragmentList.get(0));
                    return  true;
                case R.id.navigation_dashboard:
                    setFragment(fragmentList.get(1));
                    return  true;
                case R.id.navigation_profile:
                    setFragment(fragmentList.get(2));
                    return  true;
                default:
                    return false;
            }
        });

        bottomNavView.setSelectedItemId(R.id.navigation_home);
    }

    public void testAPIMethod() {
//        MoviesDBAPIInterface apiInterface = RetrofitClient.getRftInstance().create(MoviesDBAPIInterface.class);
//        apiInterface.getSearchList("tv", "on_the_air", AppConstants.MOVIESDB_APIKEY)
//                .toObservable().subscribeOn(Schedulers.io())
//                .subscribe(new Observer<ResponseBody>() {
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(@NonNull ResponseBody responseBody) {
//                        String str = null;
//                        try {
//                            str = responseBody.string();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        Log.i("MainActivity Res", str);
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//                        Log.i("MainActivity Error", String.valueOf(e));
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
    }

    public void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_fragment, fragment);
        fragmentTransaction.commit();
    }
}
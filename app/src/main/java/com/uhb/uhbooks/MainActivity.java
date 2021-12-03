package com.uhb.uhbooks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.uhb.uhbooks.Utils.SessionManager;
import com.uhb.uhbooks.Utils.Utils;
import com.uhb.uhbooks.api.ApiInterface;
import com.uhb.uhbooks.fragments.ItemsFragment;
import com.uhb.uhbooks.fragments.LoginFragment;
import com.uhb.uhbooks.interfaces.MainListener;
import com.uhb.uhbooks.models.Item;
import com.uhb.uhbooks.models.User;
import com.uhb.uhbooks.network.InternetConnectionListener;

public class MainActivity extends AppCompatActivity implements MainListener, InternetConnectionListener,
        NavigationView.OnNavigationItemSelectedListener,
        SessionManager.SessionListener {

    private static final String TAG = "MainActivity";
    private static final String FRAGMENT_ITEM = "item_fragment";
    private static final String FRAGMENT_LOGIN = "login_fragment";

    private SessionManager mSession;

    private Toolbar toolbar;
    private ProgressBar progressBar;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private ImageView ivUser;
    private TextView tvUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        navigationView = findViewById(R.id.nav_view);

        ((App) getApplication()).setInternetConnectionListener(this);
        ((App) getApplication()).setSessionListener(this);
        mSession = ((App) getApplication()).getSession();

        setToolBar();
        setDrawer();
        updateLoginState();

        itemFragment(Item.Level.STUDENT);
    }


    private void setToolBar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
    }

    private void setDrawer() {
        drawer = findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.action_student);

        ivUser = navigationView.getHeaderView(0).findViewById(R.id.ivUser);
        tvUsername = navigationView.getHeaderView(0).findViewById(R.id.tvTitle);

    }


    private void itemFragment(Item.Level level) {
        ItemsFragment fragment = ItemsFragment.getInstance(level);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_main_container, fragment, FRAGMENT_ITEM);
        transaction.commit();

        switch (level) {
            case STUDENT:
                navigationView.setCheckedItem(R.id.action_student);
                toolbar.setTitle(User.Level.STUDENT.name());
                break;
            case INSTRUCTOR:
                navigationView.setCheckedItem(R.id.action_instructor);
                toolbar.setTitle(User.Level.INSTRUCTOR.name());
                break;
//            case FAVORITE:
//                navigationView.setCheckedItem(R.id.action_favorite);
//                toolbar.setTitle(User.Level.FAVORITE.name());
//                break;
        }
    }

    private void loginFragment() {
        LoginFragment fragment = new LoginFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.add(R.id.fragment_main_container, fragment, FRAGMENT_LOGIN);
        transaction.addToBackStack(FRAGMENT_LOGIN);
        transaction.commit();
    }


    private void updateLoginState() {
        boolean isLoggedIn = mSession.isLoggedIn();
        navigationView.getMenu().findItem(R.id.action_instructor).setVisible(isLoggedIn && mSession.getLevel() == User.Level.INSTRUCTOR);
//        navigationView.getMenu().findItem(R.id.action_favorite).setVisible(isLoggedIn);
        navigationView.getMenu().findItem(R.id.action_login).setVisible(!isLoggedIn);
        navigationView.getMenu().findItem(R.id.action_logout).setVisible(isLoggedIn);

        Glide.with(this)
                .load(R.drawable.default_profile)
                .circleCrop()
                .into(ivUser);
        tvUsername.setText((isLoggedIn) ? mSession.getName() : getString(R.string.welcome));

    }

    private void logout() {
        mSession.clearUserSettings();
        updateLoginState();
        itemFragment(Item.Level.STUDENT);
    }


    @Override
    public void onInternetUnavailable() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Utils.showInternetUnAvailable(MainActivity.this);
            }
        });
    }

    @Override
    public void onLoading(boolean loading) {
        progressBar.setVisibility((loading) ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onLoginSuccess(User user, String token) {
        Fragment currentFragment =
                getSupportFragmentManager().findFragmentById(R.id.fragment_main_container);
        if (currentFragment instanceof LoginFragment)
            getSupportFragmentManager().popBackStack(FRAGMENT_LOGIN, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        mSession.createLoginSession(
                token,
                user.getId(),
                user.getUsername(),
                user.getLevel()
        );
        updateLoginState();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_student:
                itemFragment(Item.Level.STUDENT);
                break;

            case R.id.action_instructor:
                itemFragment(Item.Level.INSTRUCTOR);
                break;

//            case R.id.action_favorite:
//                itemFragment(Item.Level.FAVORITE);
//                break;

            case R.id.action_login:
                loginFragment();
                break;

            case R.id.action_logout:
                logout();
                break;

            default:
                drawer.closeDrawer(GravityCompat.START);
                return false;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onTokenInvalid() {
        logout();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
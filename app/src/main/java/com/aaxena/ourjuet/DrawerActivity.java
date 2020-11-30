package com.aaxena.ourjuet;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;

import com.google.android.material.navigation.NavigationView;

import dagger.android.AndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class DrawerActivity extends AppCompatActivity implements
        NavController.OnDestinationChangedListener
        , NavigationView.OnNavigationItemSelectedListener,
        HasSupportFragmentInjector {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return null;
    }

    @Override
    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
        
    }
}

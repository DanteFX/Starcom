package com.dantefx.starcom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.dantefx.starcom.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sh = getSharedPreferences("sleepyTaskSharedPreferences", MODE_PRIVATE);
        name = sh.getString("name", "");

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main2);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        if(name.equals("")){
            Intent intent = new Intent(MainActivity.this, UserInitView.class);
            startActivity(intent);
        }else {
            binding.toolbar.setTitle("Bienvenido "+name);
            binding.toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        }
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RelaxActivity.class);
                startActivity(intent);
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        binding.toolbar.setTitle("Bienvenido "+name);
        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.white));
    }

    @Override
    public void onRestart() {
        super.onRestart();
        binding.toolbar.setTitle("Bienvenido "+name);
        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.white));
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.toolbar.setTitle("Bienvenido "+name);
        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.white));
    }
    @Override
    public void onPause() {
        super.onPause();
        binding.toolbar.setTitle("Bienvenido "+name);
        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.white));
    }

    @Override
    public void onStop() {
        super.onStop();
        binding.toolbar.setTitle("Bienvenido "+name);
        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.white));
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.toolbar.setTitle("Bienvenido "+name);
        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.white));
    }




    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main2);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        binding.toolbar.setTitle("Bienvenido "+name);
        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.white));
    }
}

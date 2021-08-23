package com.example.task;

import static com.example.task.Session.SaveSharedPreference.getFirstName;
import static com.example.task.Session.SaveSharedPreference.getLastName;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.task.Dialog.CurrencySelection;
import com.example.task.Dialog.Rules;
import com.google.android.material.navigation.NavigationView;

public class Setting extends AppCompatActivity {
RelativeLayout container_2,currency_button,bank_button,campaign_button;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    // Make sure to be using androidx.appcompat.app.ActionBarDrawerToggle version.
    private ActionBarDrawerToggle drawerToggle;
    TextView name;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        context=this;
        /*ToolBar With NavBar*/
        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");
//        defaultScreen();



        // This will display an Up icon (<-), we will replace it with hamburger later
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_burger_icon);


        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();

        // Setup toggle to display hamburger icon with nice animation
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);

        // ...From section above...
        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);

        // Setup drawer view
        setupDrawerContent(nvDrawer);

        /*ToolBar With NavBar End*/
        nvDrawer.getMenu().getItem(7).setChecked(true);

        container_2 = findViewById(R.id.container_2);
        currency_button = findViewById(R.id.currency_button);
        bank_button = findViewById(R.id.bank_button);
        name = findViewById(R.id.name);

        name.setText(getFirstName(context)+" "+getLastName(context));
//        campaign_button = findViewById(R.id.campaign_button);
        container_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Setting.this,MyAccount.class));
            }
        });
        currency_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrencySelection exitDialog = new CurrencySelection(Setting.this);
                exitDialog.show();
                Window window = exitDialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });

        bank_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Setting.this,PaymentMethod.class));
            }
        });

//        campaign_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Setting.this,CampaignView.class));
//            }
//        });
    }

    public void vehicle_click(View view) {
        startActivity(new Intent(Setting.this,VehicleManagement.class));
    }

    public void document(View view) {
        startActivity(new Intent(Setting.this,DocumentManagement.class));
    }


    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.navigation_open,  R.string.navigation_close);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }



    public void selectDrawerItem(MenuItem menuItem) {

        Intent i;
        switch(menuItem.getItemId()) {
            case R.id.home:
                i = new Intent(Setting.this,HomeOffline.class);
                startActivity(i);
                break;
            case R.id.my_wallet:
                i = new Intent(Setting.this,Wallet.class);
                startActivity(i);
                break;
            case R.id.travel_request:
                i = new Intent(Setting.this,TravelRequest.class);
                startActivity(i);
                break;
//            case R.id.inter_city:
//                i = new Intent(Setting.this,InterCityRequests.class);
//                startActivity(i);
//                break;
            case R.id.history:
                i = new Intent(Setting.this,History.class);
                startActivity(i);
                break;
            case R.id.notification_toolbar:
                i = new Intent(Setting.this,Notifications.class);
                startActivity(i);
                break;
            case R.id.invite_friends:
                i = new Intent(Setting.this,InviteFriends.class);
                startActivity(i);
                break;
            case R.id.setting:
                i = new Intent(Setting.this,Setting.class);
                startActivity(i);
                break;
            case R.id.campaign_menu:
                i = new Intent(Setting.this,CampaignView.class);
                startActivity(i);
                break;

            default:
                Toast.makeText(this, "Coming Soon...", Toast.LENGTH_SHORT).show();
        }


        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
//         setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }
}
package com.android.babbler.Participant;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.babbler.DataClasses.User;
import com.android.babbler.R;
import com.android.babbler.User.ContactUsFragment;
import com.android.babbler.User.SettingsActivity;
import com.android.babbler.User.MessagesFragment;
import com.android.babbler.User.MyProfileFragment;

public class UserMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView=null;
    Toolbar toolbar=null;
    User member= User.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        //Main fragment shows the list of latest sessions
        android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        SessionListFragment sessionListFragment=new SessionListFragment();
        fragmentTransaction.replace(R.id.fragment_container,sessionListFragment);
        fragmentTransaction.commit();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_main, menu);
        TextView tv_name=findViewById(R.id.user_name);
        TextView tv_email=findViewById(R.id.tv_signup);
        tv_name.setText(member.getM_name());
        tv_email.setText(member.getM_email());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this,SettingsActivity.class));
           return true;
        }
        else if(id==R.id.logout){
            User.getInstance().setConnected(false);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        switch (item.getItemId()){
            case R.id.session_list:
                SessionListFragment sessionListFragment=new SessionListFragment();
                fragmentTransaction.replace(R.id.fragment_container,sessionListFragment);
                fragmentTransaction.addToBackStack("sessionList");
                fragmentTransaction.commit();
                break;
            case R.id.profile:
                MyProfileFragment myProfileFragment =new MyProfileFragment();
                fragmentTransaction.replace(R.id.fragment_container, myProfileFragment);
                fragmentTransaction.addToBackStack("profile");
                fragmentTransaction.commit();
                break;
            case R.id.search:
                SearchFragment searchFragment=new SearchFragment();
                fragmentTransaction.replace(R.id.fragment_container,searchFragment);
                fragmentTransaction.addToBackStack("searchFragment");
                fragmentTransaction.commit();
                break;
            case R.id.contactUs:
                ContactUsFragment contactUsFragment=new ContactUsFragment();
                fragmentTransaction.replace(R.id.fragment_container,contactUsFragment);
                fragmentTransaction.addToBackStack("contactUs");
                fragmentTransaction.commit();
                break;
            case R.id.messages:
                MessagesFragment messagesFragment =new MessagesFragment();
                fragmentTransaction.replace(R.id.fragment_container, messagesFragment);
                fragmentTransaction.addToBackStack("messages");
                fragmentTransaction.commit();
                break;
            case R.id.mySessions:
                ParticipantSessionsFragment participantSessionsFragment=new ParticipantSessionsFragment();
                fragmentTransaction.replace(R.id.fragment_container,participantSessionsFragment);
                fragmentTransaction.addToBackStack("participantSessions");
                fragmentTransaction.commit();
                break;
            case R.id.download_skype:
                Intent i = new Intent(Intent.ACTION_VIEW);
                Uri marketUri = Uri.parse("market://details?id=com.skype.raider");
                Intent myIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(myIntent);
                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}


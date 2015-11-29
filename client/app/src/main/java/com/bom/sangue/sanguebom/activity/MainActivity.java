package com.bom.sangue.sanguebom.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.bom.sangue.sanguebom.fragment.BloodCompatibility;
import com.bom.sangue.sanguebom.fragment.MyProfileFragment;
import com.bom.sangue.sanguebom.fragment.NavigationDrawerFragment;
import com.bom.sangue.sanguebom.R;
import com.bom.sangue.sanguebom.persistence.dao.UserDAO;

public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.[
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    // FIXME: 09/11/15 Destroy 'switch' resolve this with command design pattern.
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        Fragment instance = null;
        String tag = "";
        switch (position) {
            case 0:
                instance = new MyProfileFragment();
                tag = "MyProfileFragment";
                break;
            case 1:
                instance = new BloodCompatibility();
                tag = "BloodCompatibility";
                break;
            case 2:
                UserDAO userDAO = UserDAO.getInstance(getApplicationContext());
                userDAO.deleteMyUser();
                userDAO.closeConnection();
                System.exit(0);
                break;
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, instance, tag)
                .commit();

    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package call_recording.bkarogyam.com.management.Activity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;

import call_recording.bkarogyam.com.management.Config.DbHandler;
import call_recording.bkarogyam.com.management.Fragments.AssignCallFragment;
import call_recording.bkarogyam.com.management.Fragments.CallHistoryFragment;
import call_recording.bkarogyam.com.management.Fragments.ReportFragment;
import call_recording.bkarogyam.com.management.R;

public class ManagerActivity extends AppCompatActivity {

    private ActionBar toolbar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.assign_call:
                    fragment=new AssignCallFragment();
                    toolbar.setTitle("Assign Call");
                    loadFragment(fragment);
                    return true;
                case R.id.report:
                    fragment=new ReportFragment();
                    toolbar.setTitle("Report");
                    loadFragment(fragment);
                    return true;
                case R.id.call_history:
                    fragment=new CallHistoryFragment();
                    toolbar.setTitle("Call History");
                    loadFragment(fragment);
                    return true;
                case R.id.logout:
                    DbHandler.unsetSession(ManagerActivity.this,"logout");
                    return true;
                default:
                    fragment=new AssignCallFragment();
                    toolbar.setTitle("Assign Call");
                    loadFragment(fragment);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        toolbar = getSupportActionBar();
        toolbar.setTitle("Assign Call");
        loadFragment(new AssignCallFragment());
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}

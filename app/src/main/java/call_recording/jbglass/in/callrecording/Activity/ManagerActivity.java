package call_recording.jbglass.in.callrecording.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import call_recording.jbglass.in.callrecording.Fragments.AssignCallFragment;
import call_recording.jbglass.in.callrecording.Fragments.CallHistoryFragment;
import call_recording.jbglass.in.callrecording.Fragments.ProfileFragment;
import call_recording.jbglass.in.callrecording.Fragments.ReportFragment;
import call_recording.jbglass.in.callrecording.R;

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
                case R.id.profile:
                    fragment=new ProfileFragment();
                    toolbar.setTitle("Profile");
                    loadFragment(fragment);
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

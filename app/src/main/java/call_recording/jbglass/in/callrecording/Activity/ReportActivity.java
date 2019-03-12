package call_recording.jbglass.in.callrecording.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import call_recording.jbglass.in.callrecording.Adapters.ReportAdapter;
import call_recording.jbglass.in.callrecording.Models.ReportDataPOJO;
import call_recording.jbglass.in.callrecording.R;

public class ReportActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        getSupportActionBar().setTitle("Report");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        List<ReportDataPOJO> reportDataPOJOList= new Gson().fromJson(getIntent().getExtras().getString("report_data"),new TypeToken<List<ReportDataPOJO>>(){}.getType());
        Log.e("report_data",getIntent().getExtras().getString("report_data"));
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ReportAdapter reportAdapter=new ReportAdapter(this,getSupportFragmentManager(),reportDataPOJOList,getIntent().getExtras().getString("status"));
        recyclerView.setAdapter(reportAdapter);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

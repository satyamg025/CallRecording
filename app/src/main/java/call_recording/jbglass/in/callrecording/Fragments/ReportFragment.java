package call_recording.jbglass.in.callrecording.Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import call_recording.jbglass.in.callrecording.Activity.ReportActivity;
import call_recording.jbglass.in.callrecording.Config.DbHandler;
import call_recording.jbglass.in.callrecording.JSONBody.ReportBody;
import call_recording.jbglass.in.callrecording.Models.EmployeeDataPOJO;
import call_recording.jbglass.in.callrecording.Models.EmployeeResponsePOJO;
import call_recording.jbglass.in.callrecording.Models.ReportResponsePOJO;
import call_recording.jbglass.in.callrecording.Networking.ServiceGenerator;
import call_recording.jbglass.in.callrecording.R;
import call_recording.jbglass.in.callrecording.Requests.EmployeeRequest;
import call_recording.jbglass.in.callrecording.Requests.ReportRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportFragment extends Fragment {

    SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd");
    SimpleDateFormat formatter2 = new SimpleDateFormat("YYYY-MM-dd");
    Spinner employee,status;
    TextView from_date,to_date;
    List<String> employee_list = new ArrayList<String>();
    List<String> emp_id_list = new ArrayList<String>();
    List<String> status_li = new ArrayList<String>();
    int selected_item1=0,selected_item2=0;
    ProgressDialog progressDialog;
    Button submit;
    String date_from="",date_to="";
    Calendar myCalendar = Calendar.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_report, container, false);

        employee=(Spinner)view.findViewById(R.id.employee);
        status=(Spinner)view.findViewById(R.id.status);
        submit=(Button)view.findViewById(R.id.submit);
        from_date=(TextView)view.findViewById(R.id.from_date);
        to_date=(TextView)view.findViewById(R.id.to_date);

        Date today = new Date();
        date_from=formatter.format(today);
        from_date.setText(date_from);
        date_to=formatter.format(today);
        to_date.setText(date_to);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        progressDialog.show();

        EmployeeRequest employeeRequest= ServiceGenerator.createService(EmployeeRequest.class, DbHandler.getString(getContext(), "bearer", ""));
        Call<EmployeeResponsePOJO> employeeResponsePOJOCall=employeeRequest.call();
        employeeResponsePOJOCall.enqueue(new Callback<EmployeeResponsePOJO>() {
            @Override
            public void onResponse(Call<EmployeeResponsePOJO> call, Response<EmployeeResponsePOJO> response) {
                progressDialog.dismiss();
                Log.e("res_code",String.valueOf(response.code()));
                if (response.code() == 200) {
                    List<EmployeeDataPOJO> employeeDataPOJOS=response.body().getEmployees();
                    if(employeeDataPOJOS.size()==0){
                        new android.support.v7.app.AlertDialog.Builder(getContext()).setTitle("Error").setMessage("No employee assigned")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                }).create().show();
                    }
                    for(int i=0;i<employeeDataPOJOS.size();i++){
                        employee_list.add(employeeDataPOJOS.get(i).getName()+" ("+employeeDataPOJOS.get(i).getEmpId()+")");
                        emp_id_list.add(employeeDataPOJOS.get(i).getId());
                    }
                    selected_item1=0;
                    selected_item2=0;

                    status_li.add("Completed");
                    status_li.add("Incomplete");

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_spinner_item, employee_list);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    employee.setAdapter(dataAdapter);

                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_spinner_item, status_li);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    status.setAdapter(dataAdapter2);

                } else if (response.code() == 403) {
                    Toast.makeText(getContext(), "Not Authorized", Toast.LENGTH_LONG).show();
                    DbHandler.unsetSession(getContext(), "isforcedLoggedOut");
                } else {
                    new android.support.v7.app.AlertDialog.Builder(getContext()).setTitle("Error").setMessage("Unable to connect to server")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).create().show();
                }
            }

            @Override
            public void onFailure(Call<EmployeeResponsePOJO> call, Throwable throwable) {
                progressDialog.dismiss();
                Log.e("res_Error", throwable.getMessage());
                new android.support.v7.app.AlertDialog.Builder(getContext()).setTitle("Error").setMessage("Unable to connect to server")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
            }
        });

        final DatePickerDialog.OnDateSetListener dates = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                view.setMaxDate(new Date().getTime());
                from_date.setText(formatter.format(myCalendar.getTime()));
                date_from=formatter.format(myCalendar.getTime());
            }

        };

        final DatePickerDialog.OnDateSetListener dates2 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                view.setMaxDate(new Date().getTime());
                to_date.setText(formatter.format(myCalendar.getTime()));
                date_to=formatter.format(myCalendar.getTime());
            }

        };

        from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), dates, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), dates2, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        employee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_item1=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_item2=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog=new ProgressDialog(getContext());
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                String complete="Y";
                if(selected_item2==1){
                    complete="N";
                }
                Date dat = null;
                try {
                    dat = formatter.parse(date_from);
                } catch (ParseException e) {
                    Log.e("Error1",e.getMessage());
                    e.printStackTrace();
                }
                String formattedDate = formatter2.format(dat);

                Date dat2 = null;
                try {
                    dat2 = formatter.parse(date_to);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String formattedDate2 = formatter2.format(dat2);

                Log.e("Dates_Det",date_from+" "+date_to+" "+formattedDate+" "+formattedDate2);
                ReportBody reportBody=new ReportBody(complete,emp_id_list.get(selected_item1),date_from,date_to);
                Log.e("report_data",new Gson().toJson(reportBody));
                ReportRequest reportRequest=ServiceGenerator.createService(ReportRequest.class,DbHandler.getString(getContext(),"bearer",""));
                Call<ReportResponsePOJO> call=reportRequest.call(reportBody);
                final String finalComplete = complete;
                call.enqueue(new Callback<ReportResponsePOJO>() {
                    @Override
                    public void onResponse(Call<ReportResponsePOJO> call, Response<ReportResponsePOJO> response) {
                        progressDialog.dismiss();
                        if (response.code() == 200) {
                            Log.e("report_data2",new Gson().toJson(response.body().getData()));

                            Intent intent=new Intent(getContext(), ReportActivity.class);
                            intent.putExtra("report_data",new Gson().toJson(response.body().getData()));
                            intent.putExtra("status", finalComplete);
                            startActivity(intent);
                        } else if (response.code() == 403) {

                            Toast.makeText(getContext(), "Not Authorized", Toast.LENGTH_LONG).show();
                            DbHandler.unsetSession(getContext(), "isforcedLoggedOut");
                        } else {
                            new AlertDialog.Builder(getContext()).setTitle("Error").setMessage("Unable to connect to server")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    }).create().show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ReportResponsePOJO> call, Throwable throwable) {
                        progressDialog.dismiss();
                        new AlertDialog.Builder(getContext()).setTitle("Error").setMessage("Unable to connect to server")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                }).create().show();

                    }
                });
            }
        });
        return view;
    }
}

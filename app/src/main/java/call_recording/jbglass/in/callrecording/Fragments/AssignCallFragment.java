package call_recording.jbglass.in.callrecording.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

import call_recording.jbglass.in.callrecording.Activity.AssignCallActivity;
import call_recording.jbglass.in.callrecording.Activity.MainActivity;
import call_recording.jbglass.in.callrecording.Adapters.CallListAdapter;
import call_recording.jbglass.in.callrecording.Config.DbHandler;
import call_recording.jbglass.in.callrecording.JSONBody.AssignCallBody;
import call_recording.jbglass.in.callrecording.Models.AssignCallPOJO;
import call_recording.jbglass.in.callrecording.Models.DataPOJO;
import call_recording.jbglass.in.callrecording.Models.EmployeeDataPOJO;
import call_recording.jbglass.in.callrecording.Models.EmployeeResponsePOJO;
import call_recording.jbglass.in.callrecording.Networking.ServiceGenerator;
import call_recording.jbglass.in.callrecording.R;
import call_recording.jbglass.in.callrecording.Requests.AssignCallRequest;
import call_recording.jbglass.in.callrecording.Requests.EmployeeRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssignCallFragment extends Fragment {

    EditText name,title,details,mobile;
    TextView date;
    Button submit;
    String date_st="";
    Calendar myCalendar = Calendar.getInstance();
    SimpleDateFormat formatter = new SimpleDateFormat("dd MMM YYYY");
    SimpleDateFormat formatter2 = new SimpleDateFormat("YYYY-mm-dd");
    Spinner employee;
    List<String> employee_list = new ArrayList<String>();
    List<String> emp_id_list = new ArrayList<String>();
    int selected_item=0;
    ProgressDialog progressDialog;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_assign_call, container, false);

        name=(EditText)view.findViewById(R.id.name);
        date=(TextView)view.findViewById(R.id.call_date);
        title=(EditText)view.findViewById(R.id.title);
        details=(EditText)view.findViewById(R.id.details);
        mobile=(EditText)view.findViewById(R.id.mobile_no);
        employee=(Spinner)view.findViewById(R.id.employee);
        submit=(Button)view.findViewById(R.id.submit);

        Date today = new Date();
        date_st=formatter.format(today);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        progressDialog.show();

        EmployeeRequest employeeRequest=ServiceGenerator.createService(EmployeeRequest.class,DbHandler.getString(getContext(), "bearer", ""));
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
                    selected_item=0;
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_spinner_item, employee_list);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    employee.setAdapter(dataAdapter);

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
                date.setText(formatter.format(myCalendar.getTime()));
                date_st=formatter.format(myCalendar.getTime());
            }

        };

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), dates, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        employee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_item=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flag=0;

                if(name.getText().toString().equals("")) {
                    flag = 1;
                    name.setError("Please enter valid name");
                }

                if(title.getText().toString().equals("")) {
                    flag = 1;
                    title.setError("Please enter valid title");
                }

                if(details.getText().toString().equals("")) {
                    flag=1;

                    details.setError("Please enter valid details");
                }

                if(mobile.getText().toString().equals("")) {
                    flag=1;
                    mobile.setError("Please enter valid mobile");
                }

                if(flag==0){
                    name.setError(null);
                    title.setError(null);
                    details.setError(null);
                    mobile.setError(null);

                    Date dat = null;
                    try {
                        dat = formatter.parse(date_st);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String formattedDate = formatter2.format(dat)+"T18:30:00.000Z";

                    final ProgressDialog progressDialog=new ProgressDialog(getContext());
                    progressDialog.setMessage("Loading...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    AssignCallBody assignCallBody=new AssignCallBody(name.getText().toString(),formattedDate,emp_id_list.get(selected_item),title.getText().toString(),details.getText().toString(),mobile.getText().toString(),"Y");

                    AssignCallRequest assignCallRequests= ServiceGenerator.createService(AssignCallRequest.class, DbHandler.getString(getContext(), "bearer", ""));
                    Call<AssignCallPOJO> call=assignCallRequests.call(assignCallBody);
                    call.enqueue(new Callback<AssignCallPOJO>() {
                        @Override
                        public void onResponse(Call<AssignCallPOJO> call, Response<AssignCallPOJO> response) {
                            progressDialog.dismiss();
                            if (response.code() == 200) {
                                name.setText("");
                                title.setText("");
                                details.setText("");
                                mobile.setText("");
                                Toast.makeText(getContext(), "Call Successfully assigned", Toast.LENGTH_LONG).show();
                            } else if (response.code() == 403) {
                                progressDialog.dismiss();

                                Toast.makeText(getContext(), "Not Authorized", Toast.LENGTH_LONG).show();
                                DbHandler.unsetSession(getContext(), "isforcedLoggedOut");
                            } else {
                                progressDialog.dismiss();
                                new AlertDialog.Builder(getContext()).setTitle("Error").setMessage("Unable to connect to server")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        }).create().show();
                            }
                        }

                        @Override
                        public void onFailure(Call<AssignCallPOJO> call, Throwable throwable) {
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

            }
        });
        return view;
    }

}

package call_recording.bkarogyam.com.management.Adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import call_recording.bkarogyam.com.management.Config.DbHandler;
import call_recording.bkarogyam.com.management.JSONBody.GetFeedbackBody;
import call_recording.bkarogyam.com.management.Models.GetFeedbackPOJO;
import call_recording.bkarogyam.com.management.Models.ReportDataPOJO;
import call_recording.bkarogyam.com.management.Networking.ServiceGenerator;
import call_recording.bkarogyam.com.management.R;
import call_recording.bkarogyam.com.management.Requests.GetFeedbackRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.view_holder>{
    private Context context;
    FragmentManager fragmentManager;
    String status;

    List<ReportDataPOJO> listDatumPOJOS=new ArrayList<ReportDataPOJO>();
    public ReportAdapter(Context context, FragmentManager supportFragmentManager, List<ReportDataPOJO> data,String status) {
        this.context = context;
        this.fragmentManager=supportFragmentManager;
        this.listDatumPOJOS=data;
        this.status=status;
    }

    @NonNull
    @Override
    public ReportAdapter.view_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.report_layout,parent,false);
        return new ReportAdapter.view_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportAdapter.view_holder holder, int position) {
        holder.title.setText(listDatumPOJOS.get(position).getTitle()+" ("+listDatumPOJOS.get(position).getMobile()+")");
        holder.time.setText(listDatumPOJOS.get(position).getCallTime());
        if(status.equals("Y")){
            holder.complete_status.setText("Completed");
        }
        else{
            holder.complete_status.setText("Incomplete");
        }
    }

    @Override
    public int getItemCount() {
        return listDatumPOJOS.size();
    }

    public class view_holder extends RecyclerView.ViewHolder{

        final ImageView menu;
        TextView title,time,complete_status;

        public view_holder(View itemView) {
            super(itemView);

            title=(TextView)itemView.findViewById(R.id.title);
            time=(TextView)itemView.findViewById(R.id.time);
            complete_status=(TextView)itemView.findViewById(R.id.complete_status);

            menu = (ImageView) itemView.findViewById(R.id.menu);

            menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(status.equals("Y")) {
                        PopupMenu popup = new PopupMenu(context, menu);
                        popup.inflate(R.menu.report_complete_menu);
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.description:
                                        new AlertDialog.Builder(context).setTitle("Description").setMessage(listDatumPOJOS.get(getAdapterPosition()).getSubtitle())
                                                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.dismiss();
                                                    }
                                                }).create().show();
                                        return true;

                                    case R.id.remark:
                                        new AlertDialog.Builder(context).setTitle("Remark").setMessage(listDatumPOJOS.get(getAdapterPosition()).getRemarks())
                                                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.dismiss();
                                                    }
                                                }).create().show();
                                        return true;

                                    case R.id.recording:
                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://bkarogyam.com/records/"+listDatumPOJOS.get(getAdapterPosition()).getUpload()));
                                        context.startActivity(browserIntent);
                                        return true;

                                    case R.id.feedback:
                                        final ProgressDialog progressDialog;
                                        progressDialog = new ProgressDialog(context);
                                        progressDialog.setMessage("Loading...");
                                        progressDialog.setCancelable(false);
                                        progressDialog.show();

                                        GetFeedbackBody getFeedbackBody = new GetFeedbackBody(listDatumPOJOS.get(getAdapterPosition()).getDisposition());

                                        GetFeedbackRequest getFeedbackRequest = ServiceGenerator.createService(GetFeedbackRequest.class, DbHandler.getString(context, "bearer", ""));
                                        Call<GetFeedbackPOJO> call = getFeedbackRequest.call(getFeedbackBody);
                                        call.enqueue(new Callback<GetFeedbackPOJO>() {
                                            @Override
                                            public void onResponse(Call<GetFeedbackPOJO> call, Response<GetFeedbackPOJO> response) {

                                                if (response.code() == 200) {
                                                    progressDialog.dismiss();
                                                    StringBuilder msg = new StringBuilder("<ul style=\"list-style-type:disc\">");

                                                    if (response.body().getData().size() == 0) {
                                                        msg.append("<li>&nbsp ").append("No Feedback given").append("</li>");
                                                    }
                                                    for (int i = 0; i < response.body().getData().size(); i++) {
                                                        msg.append("<li>&nbsp ").append(response.body().getData().get(i).getName()).append("</li>");

                                                    }

                                                    msg.append("</ul>");

                                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                                    builder.setTitle("Feedback");
                                                    AlertDialog alertDialog = builder.create();

                                                    builder.setTitle(Html.fromHtml(msg.toString()));
                                                    alertDialog.getWindow().setLayout(600, 400);


                                                    new AlertDialog.Builder(context).setTitle("Feedback").setMessage(Html.fromHtml(msg.toString())).setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {

                                                        }
                                                    }).create().show();


                                                } else if (response.code() == 403) {
                                                    progressDialog.dismiss();

                                                    Toast.makeText(context, "Not Authorized", Toast.LENGTH_LONG).show();
                                                    DbHandler.unsetSession(context, "isforcedLoggedOut");
                                                } else {
                                                    progressDialog.dismiss();
                                                    new androidx.appcompat.app.AlertDialog.Builder(context).setTitle("Error").setMessage("Unable to connect to server")
                                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                                }
                                                            }).create().show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<GetFeedbackPOJO> call, Throwable t) {
                                                progressDialog.dismiss();
                                                new androidx.appcompat.app.AlertDialog.Builder(context).setTitle("Error").setMessage("Unable to connect to server")
                                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                            }
                                                        }).create().show();

                                            }
                                        });
                                        return true;

                                    default:
                                        return false;
                                }
                            }
                        });
                        popup.show();
                    }
                    else{
                        PopupMenu popup = new PopupMenu(context, menu);
                        popup.inflate(R.menu.report_incomplete_menu);
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.description:
                                        new AlertDialog.Builder(context).setTitle("Description").setMessage(listDatumPOJOS.get(getAdapterPosition()).getSubtitle())
                                                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.dismiss();
                                                    }
                                                }).create().show();
                                        return true;

                                    default:
                                        return false;
                                }
                            }
                        });
                        popup.show();
                    }

                }
            });

        }
    }
}

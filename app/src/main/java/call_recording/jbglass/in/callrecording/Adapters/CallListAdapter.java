package call_recording.jbglass.in.callrecording.Adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaRecorder;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import call_recording.jbglass.in.callrecording.Activity.FeedbackActivity;
import call_recording.jbglass.in.callrecording.Config.DbHandler;
import call_recording.jbglass.in.callrecording.Fragments.dialog_select_sim;
import call_recording.jbglass.in.callrecording.JSONBody.HistoryBody;
import call_recording.jbglass.in.callrecording.Models.HistoryDataPOJO;
import call_recording.jbglass.in.callrecording.Models.ListDatumPOJO;
import call_recording.jbglass.in.callrecording.Networking.ServiceGenerator;
import call_recording.jbglass.in.callrecording.R;
import call_recording.jbglass.in.callrecording.Requests.HistoryRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallListAdapter extends RecyclerView.Adapter<CallListAdapter.view_holder> {

    private Context context;
    FragmentManager fragmentManager;
    public static final String TAG = "MediaRecording";

    List<ListDatumPOJO> listDatumPOJOS=new ArrayList<ListDatumPOJO>();
    public CallListAdapter(Context context, FragmentManager supportFragmentManager, List<ListDatumPOJO> data) {
        this.context = context;
        this.fragmentManager=supportFragmentManager;
        this.listDatumPOJOS=data;
    }

    @NonNull
    @Override
    public view_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.users_list_header,parent,false);
        return new view_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull view_holder holder, int position) {
        holder.title.setText(listDatumPOJOS.get(position).getTitle());
        holder.name.setText(listDatumPOJOS.get(position).getName());
        holder.call_img.setText(String.valueOf(listDatumPOJOS.get(position).getName().toUpperCase().charAt(0)));
    }

    @Override
    public int getItemCount() {
        return listDatumPOJOS.size();
    }

    public class view_holder extends RecyclerView.ViewHolder{

        final ImageView menu;
        TextView title,name,call_img;
        LinearLayout ll;

        public view_holder(View itemView) {
            super(itemView);

            title=(TextView)itemView.findViewById(R.id.title);
            name=(TextView)itemView.findViewById(R.id.name);
            call_img=(TextView)itemView.findViewById(R.id.call_img);
            ll=(LinearLayout)itemView.findViewById(R.id.ll);

            menu = (ImageView) itemView.findViewById(R.id.menu);
            menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, FeedbackActivity.class));
                }
            });

            call_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog_select_sim dialog=dialog_select_sim.instance(listDatumPOJOS.get(getAdapterPosition()).getTitle(), listDatumPOJOS.get(getAdapterPosition()).getMobile(),listDatumPOJOS.get(getAdapterPosition()).getId());
                    dialog.show(fragmentManager,"");
                }
            });

            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog_select_sim dialog=dialog_select_sim.instance(listDatumPOJOS.get(getAdapterPosition()).getTitle(), listDatumPOJOS.get(getAdapterPosition()).getMobile(),listDatumPOJOS.get(getAdapterPosition()).getId());
                    dialog.show(fragmentManager,"");
                }
            });

            menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popup = new PopupMenu(context, menu);
                    popup.inflate(R.menu.recycler_menu);
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

                                case R.id.history:
                                    final ProgressDialog progressDialog;
                                    progressDialog=new ProgressDialog(context);
                                    progressDialog.setMessage("Loading...");
                                    progressDialog.setCancelable(false);
                                    progressDialog.show();

                                    HistoryBody historyBody=new HistoryBody(listDatumPOJOS.get(getAdapterPosition()).getMobile());

                                    HistoryRequest historyRequest = ServiceGenerator.createService(HistoryRequest.class, DbHandler.getString(context, "bearer", ""));
                                    Call<HistoryDataPOJO> historyDataPOJOCall = historyRequest.call(historyBody);
                                    historyDataPOJOCall.enqueue(new Callback<HistoryDataPOJO>() {
                                        @Override
                                        public void onResponse(Call<HistoryDataPOJO> call, Response<HistoryDataPOJO> response) {

                                            if (response.code() == 200) {
                                                progressDialog.dismiss();
                                                StringBuilder msg= new StringBuilder("<ul style=\"list-style-type:disc\">");

                                                    if(response.body().getData().size()==0){
                                                        msg.append("<li>&nbsp ").append("No History to show").append("</li>");
                                                    }
                                                    for (int i = 0; i < response.body().getData().size(); i++) {
                                                        if(response.body().getData().get(i).getEmpDetails().size()>0){
                                                            msg.append("<li>&nbsp ").append(response.body().getData().get(i).getEmpDetails().get(0).getName()).append(" (");
                                                        }
                                                        else{
                                                            msg.append("<li>&nbsp --- (");
                                                        }
                                                        msg.append(response.body().getData().get(i).getCallDate().split("T")[0]).append(")<p>").append(response.body().getData().get(i).getRemarks()).append("</p></li>");
                                                    }


                                                msg.append("</ul>");

                                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                                builder.setTitle("History");
                                                AlertDialog alertDialog=builder.create();

                                                builder.setTitle(Html.fromHtml(msg.toString()));
                                                alertDialog.getWindow().setLayout(600, 400);


                                                new AlertDialog.Builder(context).setTitle("History").setMessage(Html.fromHtml(msg.toString())).setPositiveButton("Close", new DialogInterface.OnClickListener() {
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
                                                new android.support.v7.app.AlertDialog.Builder(context).setTitle("Error").setMessage("Unable to connect to server")
                                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                            }
                                                        }).create().show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<HistoryDataPOJO> call, Throwable t) {
                                            progressDialog.dismiss();
                                            new android.support.v7.app.AlertDialog.Builder(context).setTitle("Error").setMessage("Unable to connect to server")
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
            });

        }
    }
}

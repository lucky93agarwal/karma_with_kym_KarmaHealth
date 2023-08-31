package com.devkraft.karmahealth.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.devkraft.karmahealth.Model.APIMessageResponse;
import com.devkraft.karmahealth.Model.MedicenListModel;
import com.devkraft.karmahealth.Model.RefreshTokenRequest;
import com.devkraft.karmahealth.Model.RefreshTokenResponse;
import com.devkraft.karmahealth.R;
import com.devkraft.karmahealth.Screen.HomeActivity;
import com.devkraft.karmahealth.Screen.InfoActivity;
import com.devkraft.karmahealth.Screen.LoginActivity;
import com.devkraft.karmahealth.Utils.AppUtils;
import com.devkraft.karmahealth.inter.OnItemClick;
import com.devkraft.karmahealth.retrofit.ServiceGeneratorTwo;
import com.devkraft.karmahealth.retrofit.UserService;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class EveningMedicenAdapter extends RecyclerView.Adapter<EveningMedicenAdapter.ViewHolder> {
    public MedicenListModel mModel;
    public static List<MedicenListModel> mProductList;
    public static Context mcontext;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor edit;
    public OnItemClick click;
    public EveningMedicenAdapter(List<MedicenListModel> productList, Context context) {
        super();
        mcontext = context;
        mProductList = productList;
        click = (OnItemClick) mcontext;
    }

    @NonNull
    @Override
    public EveningMedicenAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.recycler_home_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EveningMedicenAdapter.ViewHolder holder, int position) {

        mModel = mProductList.get(position);

        holder.tvNoofTablet.setText(mModel.noofpills);
        holder.tvmedicenNametv.setText(mModel.mName);
        holder.tvTime.setText(mModel.time);
        if(mModel.drugForm.toString().equalsIgnoreCase("Tablet")){
            holder.mediceniconiv.setBackgroundResource(R.drawable.ic_tablet_icon);
        }else  if(mModel.drugForm.equalsIgnoreCase("Liquid")){
            holder.mediceniconiv.setBackgroundResource(R.drawable.ic_liquid_icon);
        }else  if(mModel.drugForm.equalsIgnoreCase("Injection")){
            holder.mediceniconiv.setBackgroundResource(R.drawable.ic_injection_icon);
        }

        holder.tvButtonViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(mcontext, holder.tvButtonViewOption);
                //inflating menu from xml resource
                popup.inflate(R.menu.options_menu);
                Menu menu = popup.getMenu();
                if(mProductList.get(position).isDelete){

                    menu.findItem(R.id.menu2).setVisible(true);
                }else {

                    menu.findItem(R.id.menu2).setVisible(false);
                }
                if(mProductList.get(position).url.length() >0){

                    menu.findItem(R.id.menu1).setVisible(true);
                }else {

                    menu.findItem(R.id.menu1).setVisible(false);
                }
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu1:
                                //handle menu1 click
                                Intent intent = new Intent(mcontext, InfoActivity.class);
                                intent.putExtra("url",mProductList.get(position).url);
                                intent.putExtra("title",mProductList.get(position).mName);
                                mcontext.startActivity(intent);
                                return true;
                            case R.id.menu2:
                                //handle menu2 click
                                AlertDialog.Builder builder = new AlertDialog.Builder(mcontext, R.style.AlertDialogTheme);
                               // builder.setTitle("क्या इस दवा को हटाना चाहते है?");
                                builder.setMessage("क्या आप "+mProductList.get(position).mName+" को हटाना चाहते है?");
                                builder.setPositiveButton("हाँ", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        onDelete(mProductList.get(position).id);
                                    }
                                });
                                builder.setNegativeButton("नहीं", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                builder.show();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                //displaying the popup
                popup.show();
            }
        });


        if (mModel.whenToTake.toString().equalsIgnoreCase("HS")) {
            holder.tvWhen.setText("रात को सोने से पहले");

        } else if (mModel.whenToTake.toString().equalsIgnoreCase("AC")) {

            holder.tvWhen.setText("भोजन से पहले");

        } else  if (mModel.whenToTake.toString().equalsIgnoreCase("SOS")) {

            holder.tvWhen.setText("जरूरत पड़ने पर");
        } else if (mModel.whenToTake.toString().equalsIgnoreCase("PC")) {
            holder.tvWhen.setText("भोजन के बाद");
        }else {
            holder.tvWhen.setTextSize(TypedValue.COMPLEX_UNIT_SP,0);
            holder.tvWhen.setText("");
        }
        if(mModel.check == true){
            holder.clickBtnIcon.setImageResource(R.drawable.checkbtn);
        }else {
            holder.clickBtnIcon.setImageResource(R.drawable.nocheck);
        }
        holder.clickBtnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mModel.check == true){

                    holder.clickBtnIcon.setImageResource(R.drawable.nocheck);
                    mModel.check = false;
                }else {

                    holder.clickBtnIcon.setImageResource(R.drawable.checkbtn);
                    mModel.check = true;
                }
            }
        });
    }
    public void refreshToken(){
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken(sharedPreferences.getString("refreshToken",""));
        Log.i("refreshToken", "refreshToken api request 278 = " + new Gson().toJson(request));

        UserService service = ServiceGeneratorTwo.createService(UserService.class, null, null,false);
        service.refreshToken(request).enqueue(new Callback<RefreshTokenResponse>() {
            @Override
            public void onResponse(Call<RefreshTokenResponse> call, retrofit2.Response<RefreshTokenResponse> response) {
                Log.i("refreshToken", "prescription api response 0121 code = " + response.code());
                if (response.isSuccessful()) {
                    Log.i("refreshToken", "prescription api response = " + new Gson().toJson(response.body()));

                    edit.putString("Ptoken", response.body().accessToken);
                    edit.apply();
                    Toast.makeText(mcontext, "Try Again", Toast.LENGTH_SHORT).show();

                } else {
                    edit.clear();
                    edit.apply();
                    Intent intent = new Intent(mcontext, LoginActivity.class);
                    mcontext.startActivity(intent);
                    ((HomeActivity)mcontext).finish();
                }
            }

            @Override
            public void onFailure(Call<RefreshTokenResponse> call, Throwable t) {
                Log.i("checkmodeldata", "api error message response  = " + t.getMessage());
            }
        });

    }
    public void onDelete(String id){
      //   Toast.makeText(mcontext, "id = "+id+" user id = "+sharedPreferences.getString("kymPid", "134388"), Toast.LENGTH_SHORT).show();

        UserService service = ServiceGeneratorTwo.createService(UserService.class, null, null, false);
        String token = "Bearer " + sharedPreferences.getString("Ptoken", "134388");
        service.deleteDrugAPI(sharedPreferences.getString("kymPid", "134388"),String.valueOf(id),"ED",token).enqueue(new Callback<APIMessageResponse>() {
            @Override
            public void onResponse(Call<APIMessageResponse> call, retrofit2.Response<APIMessageResponse> response) {
                Log.i("checkmodrug", "api login response 0 code = " + response.code());
                Log.i("checkmodrug", "api login response  = " + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    // Log.i("checkmodrug", "api login LoginNewResponse response = " + response.body().message);
                    click.onClickAdapter("checkupAdd");
                //    Toast.makeText(mcontext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }else if(response.code() == 401){
                    refreshToken();
                } else {
                    Toast.makeText(mcontext, "Please try after some time.", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<APIMessageResponse> call, Throwable t) {
                Toast.makeText(mcontext, "Please try after some time.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mediceniconiv;
        public TextView tvmedicenNametv;
        public TextView tvNoofTablet,tvWhen;
        public TextView tvTime,tvButtonViewOption;
        public ImageView clickBtnIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            sharedPreferences = mcontext.getSharedPreferences("userData", MODE_PRIVATE);
            edit = sharedPreferences.edit();
            this.tvWhen = (TextView) itemView.findViewById(R.id.bottomttv);
            this.mediceniconiv = (ImageView) itemView.findViewById(R.id.medicenicon);
            this.tvButtonViewOption = (TextView) itemView.findViewById(R.id.textViewOptions);
            this.tvmedicenNametv = (TextView) itemView.findViewById(R.id.medicenNametv);
            this.tvNoofTablet = (TextView) itemView.findViewById(R.id.nooftablettv);
            this.tvTime = (TextView) itemView.findViewById(R.id.timetv);
            this.clickBtnIcon = (ImageView) itemView.findViewById(R.id.clickbtnicon);
        }
    }
}


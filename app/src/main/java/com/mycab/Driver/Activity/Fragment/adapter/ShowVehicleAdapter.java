package com.mycab.Driver.Activity.Fragment.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.mycab.Driver.Activity.Fragment.Activity.AddBankDetailActivity;
import com.mycab.Driver.Activity.Fragment.model.ShowVehicleModel;
import com.mycab.databinding.RowShowVehicleLayoutBinding;


import java.util.ArrayList;


public class ShowVehicleAdapter extends RecyclerView.Adapter<ShowVehicleAdapter.MyViewHolder> {


    private Context mContext;
    private ArrayList<ShowVehicleModel> vehicleList;

    public ShowVehicleAdapter(Context mContext, ArrayList<ShowVehicleModel> vehicleList) {
        this.mContext = mContext;
        this.vehicleList = vehicleList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(RowShowVehicleLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ShowVehicleModel modelObject = vehicleList.get(position);
        holder.rowShowVehicleLayoutBinding.txVehicleName.setText(modelObject.getVehicleName());


       /* notifyItemChanged(position);
        modelObject.setVehicleId("swift");
        notifyItemRemoved(position);*/
        holder.rowShowVehicleLayoutBinding.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    AddBankDetailActivity.dialogVehicle.dismiss();
                    if (mContext instanceof AddBankDetailActivity) {
                        ((AddBankDetailActivity)mContext).GetSingleVehicleId( modelObject.getVehicleId());
                    }

                    AddBankDetailActivity.binding.txVehicle.setText(modelObject.getVehicleName());


                }
            }
        });


       /* holder.rowShowVehicleLayoutBinding.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                       @Override
                                                       public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                                           if(isChecked){

                                                               AddBankDetailActivity.Arr_vehicleId.add(modelObject.getVehicleId());
                                                               AddBankDetailActivity.Arr_vehicleName.add(modelObject.getVehicleName());
                                                           }else {
                                                               try {
                                                                   AddBankDetailActivity.Arr_vehicleId.remove(modelObject.getVehicleId());
                                                                   AddBankDetailActivity.Arr_vehicleName.remove(modelObject.getVehicleName());
                                                               } catch (Exception e) {
                                                                   e.printStackTrace();
                                                               }
                                                           }
                                                       }
                                                   }
        );

        AddBankDetailActivity.btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddBankDetailActivity.dialogVehicle.dismiss();
                if (mContext instanceof AddBankDetailActivity) {
                    ((AddBankDetailActivity)mContext).GetShowVehicle();
                }
            }
        });*/



    }

    @Override
    public int getItemCount() {
        return vehicleList == null ? 0 : vehicleList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RowShowVehicleLayoutBinding rowShowVehicleLayoutBinding;
        public MyViewHolder(RowShowVehicleLayoutBinding rowShowVehicleLayoutBinding) {
            super(rowShowVehicleLayoutBinding.getRoot());
            this.rowShowVehicleLayoutBinding = rowShowVehicleLayoutBinding;
        }

    }
}

package com.mastercoding.explicitintentapp.Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mastercoding.explicitintentapp.Model.SugessstionModel;
import com.mastercoding.explicitintentapp.R;

import java.util.ArrayList;

public class SugesstionAdapter extends RecyclerView.Adapter<SugesstionAdapter.ViewHolder>{
    private Context context;
    private ArrayList<SugessstionModel> data;
    public SugesstionAdapter(Context context, ArrayList<SugessstionModel> data) {
        this.context = context;
        this.data = data;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sugesstion_content_layout
                ,parent,false);
        return new ViewHolder(view) {
        };
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SugessstionModel model = data.get(position);
        holder.SetDetails(model);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView foodname,foodcalorie;
        ViewHolder(View itemView){
            super(itemView);
            image=itemView.findViewById(R.id.image);
            foodname=itemView.findViewById(R.id.namefood);
            foodcalorie=itemView.findViewById(R.id.caloriefood);
        }
        void SetDetails(SugessstionModel model){
            Glide.with(image).load(model.getImage()).into(image);
            foodname.setText(model.getName());
            foodcalorie.setText(model.getCalorie()+" kcal");
        }
    }
}

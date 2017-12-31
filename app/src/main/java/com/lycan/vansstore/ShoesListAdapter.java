package com.lycan.vansstore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by LYCAN on 26-Dec-17.
 */

public class ShoesListAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Shoes> shoessList;

    public ShoesListAdapter(Context context, int layout, ArrayList<Shoes> shoessList) {
        this.context = context;
        this.layout = layout;
        this.shoessList = shoessList;
    }

    @Override
    public int getCount() {
        return shoessList.size();
    }

    @Override
    public Object getItem(int position) {
        return shoessList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        ImageView imageView;
        TextView txtName, txtPrice;
    }

    @Override
    public View getView(int position, View view, ViewGroup ViewGrup) {

        View row = view;
        ViewHolder holder=new ViewHolder();

        if(row == null){
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=inflater.inflate(layout, null);

            holder.txtName=(TextView)row.findViewById(R.id.txtName);
            holder.txtPrice=(TextView)row.findViewById(R.id.txtPrice);
            holder.imageView=(ImageView)row.findViewById(R.id.imgShoes);
            row.setTag(holder);
        }
        else{
            holder=(ViewHolder)row.getTag();
        }

        Shoes shoes=shoessList.get(position);

        holder.txtName.setText(shoes.getName());
        holder.txtPrice.setText(shoes.getPrice());

        //Firebase
        String urlImage = shoes.getImage();
        Glide.with(context).load(urlImage).into(holder.imageView);

        //SQLite
        /*
        byte[] shoesImage=shoes.getImage();
        Bitmap bitmap= BitmapFactory.decodeByteArray(shoesImage, 0, shoesImage.length);
        holder.imageView.setImageBitmap(bitmap);
        */

        return row;
    }
}

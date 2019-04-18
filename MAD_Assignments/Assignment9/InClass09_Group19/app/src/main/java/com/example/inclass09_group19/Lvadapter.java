package com.example.inclass09_group19;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Lvadapter extends ArrayAdapter<ContactDetails> {
    public Lvadapter(Context context, int resource, ArrayList<ContactDetails> objects, ThirdFragment cont) {
        super(context, resource, objects);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //Log.d("position : " , String.valueOf(position));

        ContactDetails contactDetails = getItem(position);

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contact_list, parent, false);
            viewHolder = new ViewHolder();
          viewHolder.txtFullName = (TextView) convertView.findViewById(R.id.txtFullName);
            viewHolder.txtEmailID = (TextView) convertView.findViewById(R.id.txtEmailID);
            viewHolder.txtContactNo = (TextView) convertView.findViewById(R.id.txtPhone);
            viewHolder.imgURL =(ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(viewHolder);
        }


        viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.txtFullName.setText(contactDetails.Name);
        viewHolder.txtEmailID.setText(contactDetails.EmailID);

        viewHolder.txtContactNo.setText(contactDetails.ContactNo);
        if(!contactDetails.imgURL.isEmpty())
        {
            byte[] decodedString = Base64.decode(contactDetails.imgURL, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            viewHolder.imgURL.setImageBitmap(decodedByte);
        }



        return convertView;
    }




    private static class ViewHolder {
        TextView txtFullName, txtEmailID, txtContactNo;
        ImageView imgURL;
    }


}


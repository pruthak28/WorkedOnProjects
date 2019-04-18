package com.example.group19_hw06;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyVw> {

    ArrayList<Message> arrcp = new ArrayList<>();
    ArrayList<String> keyList = new ArrayList<>();
    MessageOperations editExpense;
    String userName;
    public MessageAdapter(ArrayList<Message> arrcp, Context cont, ArrayList<String> keyList, String userName) {
        this.editExpense =  (MessageOperations) cont;
        this.arrcp = arrcp;
        this.keyList = keyList;
        this.userName = userName;
    }


    class MyVw extends RecyclerView.ViewHolder {
        TextView txtMsg, txtFirsName, txtDate;
        ImageView txtMsgImg, imgDelete;

        public MyVw(View itemView) {
            super(itemView);
            txtMsg = (TextView) itemView.findViewById(R.id.txtMsgValue);
            txtFirsName = (TextView) itemView.findViewById(R.id.txtMsgFirstName);
            txtDate = (TextView) itemView.findViewById(R.id.txtMsgTime);
            txtMsgImg = (ImageView) itemView.findViewById(R.id.ivMsgImg);
            imgDelete = (ImageView) itemView.findViewById(R.id.ivDelete);


        }
    }

    @Override
    public MyVw onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.msg, viewGroup, false);


        MyVw vh = new MyVw(view);


        return vh;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(MyVw holder, int i) {
        final Message message = arrcp.get(i);
        final int index = i;

        if(message.msgText.isEmpty())
        {
            holder.txtMsg.setVisibility(View.GONE);
        }
        else
        {
            holder.txtMsg.setVisibility(View.VISIBLE);
            holder.txtMsg.setText(message.msgText);
        }

        holder.txtFirsName.setText(String.valueOf(message.firstName));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy  hh:mm:ss aa");

        PrettyTime p = new PrettyTime();

        holder.txtDate.setText(p.format(message.dt));

        if(message.imgUrl.isEmpty())
        {
            holder.txtMsgImg.setVisibility(View.GONE);
        }
        else
        {
            holder.txtMsgImg.setVisibility(View.VISIBLE);
            byte[] decodedString = Base64.decode(message.imgUrl, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.txtMsgImg.setImageBitmap(decodedByte);
        }

        if(!(message.firstName + " " + message.lastName).equalsIgnoreCase(userName))
        {
            holder.imgDelete.setVisibility(View.GONE);
        }
        else
        {
            holder.imgDelete.setVisibility(View.VISIBLE);
        }


        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editExpense.deleteMessage(arrcp.get(index), keyList.get(index));
            }
        });

    }



    @Override
    public int getItemCount() {
        return arrcp.size();
    }




    public interface MessageOperations{
        void deleteMessage(Message expenseDetails, String key);
    }
}

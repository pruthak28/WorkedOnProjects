package com.example.group19_ic10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyVw> {

    ArrayList<Note> arrcp = new ArrayList<>();
    MessageOperations msgOperations;
    public NotesAdapter(ArrayList<Note> arrcp, Context cont) {
        this.arrcp = arrcp;
        this.msgOperations = (MessageOperations) cont;
    }


    class MyVw extends RecyclerView.ViewHolder {
        TextView txtNote;
        ImageView imgDelete;

        public MyVw(View itemView) {
            super(itemView);
            txtNote = (TextView) itemView.findViewById(R.id.txtNote);
            imgDelete = (ImageView) itemView.findViewById(R.id.ivDelete);
        }
    }

    @Override
    public MyVw onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.notes, viewGroup, false);


        MyVw vh = new MyVw(view);

        return vh;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(MyVw holder, int i) {
        final Note notes = arrcp.get(i);
        final int index = i;

        holder.txtNote.setText(notes.getText());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msgOperations.DisplayNote(notes);
            }
        });


        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread g=new GETRequest();
                MainActivity.httpUrl="http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/note/delete";
                MainActivity.request.put("x-access-token",MainActivity.response.get("token").toString());

                MainActivity.body.put("msgId",arrcp.get(index).getId());
                g.start();

                while(true) {
                    if (MainActivity.request.size() == 0)
                        break;
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                arrcp.remove(index);
                notifyDataSetChanged();
            }
        });

    }



    @Override
    public int getItemCount() {
        return arrcp.size();
    }




    public interface MessageOperations{
        void DisplayNote(Note notes);
    }
}

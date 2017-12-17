package com.example.abhinavchinta.gdg_app;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private List<ListItem> List;
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, IDno;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            //IDno = (TextView) view.findViewById(R.id.idno);
        }
    }


    public Adapter(List<ListItem> list){
        this.List = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ListItem listItem= List.get(position);
        holder.title.setText(listItem.getTitle());
        //holder.IDno.setText(listItem.getId());
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(view.getContext(),movie.getId()+"",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(view.getContext(), Content_Activity.class);
                intent.putExtra("id", listItem.getId());
                intent.putExtra("data",data);
                intent.putExtra("body",listItem.getBody());
                view.getContext().startActivity(intent);
            }
        });
        //holder.year.setText(movie.getYear());
    }

    @Override
    public int getItemCount() {
        return List.size();
    }
}

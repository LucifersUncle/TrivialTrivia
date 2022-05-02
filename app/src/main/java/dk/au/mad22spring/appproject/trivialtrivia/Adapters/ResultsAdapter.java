package dk.au.mad22spring.appproject.trivialtrivia.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dk.au.mad22spring.appproject.trivialtrivia.Models.UserModel;
import dk.au.mad22spring.appproject.trivialtrivia.R;
import dk.au.mad22spring.appproject.trivialtrivia.ViewModels.ResultsViewModel;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ResultsViewHolder>{

    public interface IResultsItemClicked{
    }

    private ArrayList<UserModel> userList;

    public ResultsAdapter(){
    }

    public void updateUserList(ArrayList<UserModel> lists){
        userList = lists;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ResultsAdapter.ResultsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_results, parent, false);
        ResultsViewHolder vh = new ResultsViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ResultsViewHolder holder, int position) {
        holder.txtName.setText(userList.get(position).userName);
        holder.txtPoint.setText(userList.get(position).score);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ResultsViewHolder extends RecyclerView.ViewHolder{

        TextView txtName, txtPoint;

        public ResultsViewHolder(@NonNull View itemView){
            super(itemView);

            txtName = itemView.findViewById(R.id.txt_listResults_playerName);
            txtPoint = itemView.findViewById(R.id.txt_listResult_score);
        }

    }
}

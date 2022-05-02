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

public class LobbyAdapter extends RecyclerView.Adapter<LobbyAdapter.LobbyViewHolder> {

    public interface ILobbyItemClicked{
    }

    //private ILobbyItemClickedListener listener;

    //Data in the adapter
    private ArrayList<UserModel> userList;

    public LobbyAdapter(){

    }

    public void updateUserList(ArrayList<UserModel> lists){
        userList = lists;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LobbyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_player, parent, false);
        LobbyViewHolder vh = new LobbyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull LobbyViewHolder holder, int position) {
        holder.txtName.setText(userList.get(position).userName);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    //Holds information about each item in the list
    public class LobbyViewHolder extends RecyclerView.ViewHolder{

        TextView txtName;

        //ILobbyItemClickedListener listener;

        public LobbyViewHolder(@NonNull View itemView){
            super(itemView);

            txtName = itemView.findViewById(R.id.txt_players_playerName);
            //listener = lobbyItemClickedListener;
        }

    }
}

package dk.au.mad22spring.appproject.trivialtrivia.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dk.au.mad22spring.appproject.trivialtrivia.Models.Game;
import dk.au.mad22spring.appproject.trivialtrivia.Models.User;
import dk.au.mad22spring.appproject.trivialtrivia.R;

public class JoinGameAdapter extends RecyclerView.Adapter<JoinGameAdapter.JoinGameViewHolder> {

    public interface IJoinGameItemClickedListener{
        void onJoinGameClicked(int index);
}
    private IJoinGameItemClickedListener listener;

    private ArrayList<Game> gameList;

    public JoinGameAdapter(IJoinGameItemClickedListener listener){
        this.listener=listener;
        this.gameList= new ArrayList<Game>();
    }

    public void updatePlayerList(ArrayList<Game> list){
        gameList = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public JoinGameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_games, parent, false);
        JoinGameViewHolder vh = new JoinGameViewHolder(v, listener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull JoinGameViewHolder holder, int position) {
        holder.textViewGameName.setText(gameList.get(position).getGameName());
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }

    public class JoinGameViewHolder extends RecyclerView.ViewHolder {

        TextView textViewGameName;

        IJoinGameItemClickedListener listener;

        public JoinGameViewHolder(@NonNull View itemView, IJoinGameItemClickedListener joinGameItemClickedListener) {
            super(itemView);

            textViewGameName = itemView.findViewById(R.id.textviewGameName);
            listener = joinGameItemClickedListener;
        }
    }
}

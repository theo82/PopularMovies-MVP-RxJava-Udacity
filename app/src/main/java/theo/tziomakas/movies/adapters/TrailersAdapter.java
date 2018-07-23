package theo.tziomakas.movies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import theo.tziomakas.movies.R;
import theo.tziomakas.movies.models.Trailers;
import theo.tziomakas.movies.ui.detail.DetailActivity;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersViewHolder> {
    private List<Trailers> trailersList;
    Context context;
    final private TrailersAdapterOnClickHandler mClickHandler;


    public interface TrailersAdapterOnClickHandler{
        void onClick(String trailerKey);
    }

    public TrailersAdapter(List<Trailers> trailersList, Context context, TrailersAdapterOnClickHandler mClickHandler){
        this.trailersList = trailersList;
        this.context = context;
        this.mClickHandler = mClickHandler;
    }

    @Override
    public TrailersAdapter.TrailersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.trailers_row;
        LayoutInflater inflater = LayoutInflater.from(context);

        boolean shouldAttactToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem,parent,shouldAttactToParentImmediately);

        TrailersViewHolder trailersViewHolder = new TrailersViewHolder(view);

        return trailersViewHolder;
    }

    @Override
    public void onBindViewHolder(TrailersAdapter.TrailersViewHolder holder, int position) {
        Trailers trailers = trailersList.get(position);
        holder.mTrailerTextView.setText(trailers.getName());

        Log.v("Trailers: ",trailers.getName());
    }

    @Override
    public int getItemCount() {
        return trailersList.size();
    }

    public void setTrailersData(List<Trailers> trailersList){
        this.trailersList = trailersList;
        notifyDataSetChanged();
    }


    public class TrailersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTrailerTextView;

        public TrailersViewHolder(View itemView) {
            super(itemView);

            mTrailerTextView = (TextView) itemView.findViewById(R.id.trailer_text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String trailerKey = trailersList.get(adapterPosition).getKey();
            mClickHandler.onClick(trailerKey);
        }
    }

}
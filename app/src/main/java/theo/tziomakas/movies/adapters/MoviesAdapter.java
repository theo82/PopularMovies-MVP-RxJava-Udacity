package theo.tziomakas.movies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

import theo.tziomakas.movies.models.Result;
import theo.tziomakas.movies.R;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesHolder> {

    public static final String POSTER_PATH = "http://image.tmdb.org/t/p/w185//";


    List<Result> resultList;
    Context context;

    final private MoviesAdapterOnClickHandler mClickHandler;

    public interface MoviesAdapterOnClickHandler{
        void onClick(String movieId);
    }

    public MoviesAdapter(List<Result> resultList, Context context, MoviesAdapterOnClickHandler mClickHandler ){
        this.resultList = resultList;
        this.context = context;
        this.mClickHandler = mClickHandler;
    }

    @Override
    public MoviesHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_movies,parent,false);
        MoviesHolder mh = new MoviesHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder( MoviesHolder holder, int position) {

        String posterPath = POSTER_PATH + resultList.get(position).getPosterPath();

        Picasso.with(context)
                .load(posterPath)
                .placeholder(R.drawable.ic_broken_image)
                .resize(506, 759)
                .centerCrop()
                .into(holder.mMovieImage);

    }

    @Override
    public int getItemCount() {

        return resultList.size();

    }
    public void setMoviesData(List<Result> resultList){
        this.resultList = resultList;
        notifyDataSetChanged();
    }

    public void clear() {
        int size = this.resultList.size();
        this.resultList.clear();
        notifyItemRangeRemoved(0, size);
    }

    public class MoviesHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView mMovieImage;

        public MoviesHolder(View itemView) {
            super(itemView);
            mMovieImage = itemView.findViewById(R.id.movieImage);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            String movieId = resultList.get(adapterPosition).getId();
            //Log.v("MovieAdapter",movieId);
            mClickHandler.onClick(String.valueOf(movieId));

        }
    }

}

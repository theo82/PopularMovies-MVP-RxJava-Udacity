package theo.tziomakas.movies.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import theo.tziomakas.movies.R;
import theo.tziomakas.movies.models.Reviews;
import theo.tziomakas.movies.models.ReviewsResponse;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private List<Reviews> reviewsList;

    public ReviewAdapter(List<Reviews> reviewsList) {
        this.reviewsList = reviewsList;
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView reviewAuthorTextView;
        private TextView reviewTextView;

        private ReviewAdapterViewHolder(View view) {
            super(view);
            reviewAuthorTextView = view.findViewById(R.id.movie_review_author);
            reviewTextView = view.findViewById(R.id.movie_review);
        }
    }

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);

        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder holder, int position) {

        Reviews review = reviewsList.get(position);
        holder.reviewAuthorTextView.setText(review.getAuthor());
        holder.reviewTextView.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
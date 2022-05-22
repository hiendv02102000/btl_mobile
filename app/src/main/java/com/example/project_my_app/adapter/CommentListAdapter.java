package com.example.project_my_app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.project_my_app.R;
import com.example.project_my_app.model.Comment;

import java.util.List;


public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> {

    private List<Comment> localDataSet ;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewName , textViewComment;

        public ViewHolder(View view) {
            super(view);
            textViewName = (TextView) view.findViewById(R.id.name);
            textViewComment = (TextView) view.findViewById(R.id.textComment);
        }

        public TextView getTextViewName() {
            return textViewName;
        }

        public TextView getTextViewComment() {
            return textViewComment;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public CommentListAdapter(List<Comment> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.comment_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        int i =position;

        viewHolder.getTextViewName().setText(localDataSet.get(i).getUser().getUsername()+":");
        viewHolder.getTextViewComment().setText(localDataSet.get(i).getContent());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(localDataSet==null) return 0;
        return localDataSet.size();
    }

    public void setLocalDataSet(List<Comment> localDataSet) {
        this.localDataSet = localDataSet;
        notifyDataSetChanged();
    }
    public void addData(Comment comment){
        this.localDataSet.add(0,comment);
        notifyDataSetChanged();
    }
}

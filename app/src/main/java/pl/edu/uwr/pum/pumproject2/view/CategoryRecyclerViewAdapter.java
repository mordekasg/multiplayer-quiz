package pl.edu.uwr.pum.pumproject2.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import pl.edu.uwr.pum.pumproject2.R;
import pl.edu.uwr.pum.pumproject2.model.CategoryEntity;

public class CategoryRecyclerViewAdapter extends ListAdapter <CategoryEntity, CategoryRecyclerViewAdapter.CategoryViewHolder> {
    protected CategoryRecyclerViewAdapter(@NonNull DiffUtil.ItemCallback<CategoryEntity> diffCallback) {
        super(diffCallback);
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvId;
        private final TextView tvName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_view,
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryEntity current = getItem(position);
        holder.tvId.setText(String.valueOf(current.getId()));
        holder.tvName.setText(current.getName());
    }

    static class CategoryDiff extends DiffUtil.ItemCallback<CategoryEntity>{
        @Override
        public boolean areItemsTheSame(@NonNull CategoryEntity oldItem, @NonNull CategoryEntity newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull CategoryEntity oldItem, @NonNull CategoryEntity newItem) {
            return oldItem.getId() == newItem.getId();
        }
    }
}

package com.sample.leapskills.adapters;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sample.leapskills.Entities.Category;
import com.sample.leapskills.Entities.Product;
import com.sample.leapskills.R;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {


    private Activity activity;
    private Category category;
    RecyclerView recyclerView;


    public ProductAdapter(Activity activity, Category category) {
        this.activity = activity;
        this.category = category;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_product, viewGroup, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        final Product product = category.getProducts().get(position);
        holder.textViewTitle.setText(product.getName());
        ProductDetailsAdapter adapter = new ProductDetailsAdapter(activity, product.getVariants());
        recyclerView.setAdapter(adapter);

    }


    @Override
    public int getItemCount() {
        return category.getProducts().size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_view_product);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        }
    }
}

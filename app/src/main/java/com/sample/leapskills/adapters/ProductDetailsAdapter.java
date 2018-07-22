package com.sample.leapskills.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sample.leapskills.Entities.Variant;
import com.sample.leapskills.R;

import java.util.List;

public class ProductDetailsAdapter extends RecyclerView.Adapter<ProductDetailsAdapter.ProductViewHolder> {
    private Activity activity;
    private List<Variant> variant;


    public ProductDetailsAdapter(Activity activity, List<Variant> variants) {
        this.activity = activity;
        this.variant = variants;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_product_details, viewGroup, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        if (variant.get(position).getSize() != null)
            holder.txtSize.setText("Size: " + variant.get(position).getSize());
        holder.txtPrice.setText("Price: " + variant.get(position).getPrice());
        holder.txtColor.setText("Color: " + variant.get(position).getColor());

    }


    @Override
    public int getItemCount() {
        return variant.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView txtColor, txtSize, txtPrice;

        public ProductViewHolder(View itemView) {
            super(itemView);

            txtColor = itemView.findViewById(R.id.txt_color);
            txtSize = itemView.findViewById(R.id.txt_size);
            txtPrice = itemView.findViewById(R.id.txt_price);
        }
    }
}

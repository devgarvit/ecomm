package com.sample.leapskills.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sample.leapskills.Entities.Category;
import com.sample.leapskills.R;

import java.util.List;

public class CategorySpinnerAdapter extends BaseAdapter {
    Context context;
    List<Category> categoryList;

    public CategorySpinnerAdapter(@NonNull Context context, @NonNull List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @Override
    public int getCount() {
        return categoryList.size()-1;
    }

    @Override
    public Object getItem(int position) {
        return categoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.adapter_spinner_category, null);

            holder = new ViewHolder();
            holder.txtCategory = (TextView) convertView.findViewById(R.id.txt_category_spinner_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtCategory.setText(categoryList.get(position).getName());
        return convertView;
    }

    static class ViewHolder {
        TextView txtCategory;
    }
}

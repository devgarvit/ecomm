package com.sample.leapskills.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import com.sample.leapskills.Entities.Category;
import com.sample.leapskills.Entities.Item;
import com.sample.leapskills.Persistent.TinyDB;
import com.sample.leapskills.R;
import com.sample.leapskills.Retrofit.RetrofitTask;
import com.sample.leapskills.Retrofit.RetrofitTaskListener;
import com.sample.leapskills.Utils.NetworkUtils;
import com.sample.leapskills.adapters.CategorySpinnerAdapter;
import com.sample.leapskills.adapters.ProductAdapter;
import com.sample.leapskills.common.CommonKeyUtility;
import com.sample.leapskills.common.CommonUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RetrofitTaskListener {
    ProgressDialog progressDialog;
    AppCompatSpinner spinnerCategory, spinnerSubCategory;
    RecyclerView recyclerView;
    TreeMap<Integer, Category> hashMap;
    List<Category> listCategory, listSubCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerCategory = (AppCompatSpinner) findViewById(R.id.spinner_category);
        spinnerSubCategory = (AppCompatSpinner) findViewById(R.id.spinner_sub_category);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressDialog = new ProgressDialog(this);

        retrofit(getApplicationContext());
    }


    private void retrofit(Context context) {
        if (CommonUtility.isNetworkAvailable(context)) {
            showProgressBar(false);
            String url = NetworkUtils.ITEM();
            RetrofitTask task = new RetrofitTask<>(this, CommonKeyUtility.HTTP_REQUEST_TYPE.GET, CommonKeyUtility.CallerFunction.GET_ITEMS, url, getApplicationContext());
            task.execute(false);
        } else {
            CommonUtility.alertDialog("No Internet Connection", this);
        }
    }

    private void showProgressBar(boolean isCancellable) {
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(isCancellable);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    private void dismissProgressBar() {
        if (null != progressDialog && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void onRetrofitTaskComplete(Response response, Context context, CommonKeyUtility.CallerFunction callerFunction) {
        dismissProgressBar();
        if (response != null) {
            Item item = (Item) response.body();
            if (item != null) {
                // Persist Object for further use
                new TinyDB(getApplicationContext()).putObject("item", item);
                sortSubCategory(item);
            }
        } else
            CommonUtility.alertDialog("We could not reach the server.Please try again", this);

    }

    private void sortSubCategory(Item item) {
        hashMap = new TreeMap<>();
        for (int i = 0; i < item.getCategories().size(); i++) {
            hashMap.put(item.getCategories().get(i).getId(), item.getCategories().get(i));
        }

        setCategoryAdapter(item.getCategories());
    }

    private void setCategoryAdapter(List<Category> list) {
        listCategory = list;
        Category category = new Category();
        category.setName("Select Category");
        list.add(category);

        CategorySpinnerAdapter adapter = new CategorySpinnerAdapter(getApplicationContext(), list);
        spinnerCategory.setAdapter(adapter);
        spinnerCategory.setSelection(adapter.getCount());
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i < listCategory.size() - 1) {
                    if (listCategory.get(i).getProducts().size() > 0) {
                        ProductAdapter adapter = new ProductAdapter(MainActivity.this, listCategory.get(i));
                        recyclerView.setAdapter(adapter);
                    } else if (listCategory.get(i).getChildCategories().size() > 0) {
                        ProductAdapter adapter = new ProductAdapter(MainActivity.this, hashMap.get(listCategory.get(i).getChildCategories().get(0)));
                        recyclerView.setAdapter(adapter);
                    }
                }
                setSubCategoryAdapter(listCategory.get(i).getChildCategories());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setSubCategoryAdapter(List<Integer> list) {
        List<Category> categoryList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                categoryList.add(hashMap.get(list.get(i)));
            }
            spinnerSubCategory.setVisibility(View.VISIBLE);
        } else {
            spinnerSubCategory.setVisibility(View.GONE);
        }
        setAdapter(categoryList);
    }

    private void setAdapter(List<Category> categoryList) {
        listSubCategory = categoryList;
        Category category = new Category();
        category.setName("Sub Category");
        categoryList.add(category);

        CategorySpinnerAdapter adapter = new CategorySpinnerAdapter(getApplicationContext(), categoryList);
        spinnerSubCategory.setAdapter(adapter);
        spinnerSubCategory.setSelection(0);
        spinnerSubCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (listSubCategory.get(i).getProducts().size() > 0) {
                    ProductAdapter adapter = new ProductAdapter(MainActivity.this, listSubCategory.get(i));
                    recyclerView.setAdapter(adapter);
                } else if (listSubCategory.get(i).getChildCategories().size() > 0) {
                    ProductAdapter adapter = new ProductAdapter(MainActivity.this, hashMap.get(listSubCategory.get(i).getChildCategories().get(0)));
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    @Override
    public void onRetrofitTaskFailure(Throwable t, CommonKeyUtility.CallerFunction callerFunction) {
        dismissProgressBar();
        CommonUtility.alertDialog("We could not reach the server.Please try again", this);

    }
}

package henrygarant.com.cryptomanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.cloudinary.android.MediaManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private AssetAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CoinMarketCapService mService;
    private Context mContext;
    public static boolean didInit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        if (!MainActivity.didInit) {
            MediaManager.init(mContext);
            MainActivity.didInit = true;
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mService = ApiUtils.getCoinMarketCapService();
        mRecyclerView = (RecyclerView) findViewById(R.id.assetList);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);

        mAdapter = new AssetAdapter(this, new ArrayList<Asset>(0), new AssetAdapter.PostItemListener() {

            @Override
            public void onPostClick(Asset clickedAsset) {
                //start new activity and pass asset
                Intent intent = new Intent(mContext, ClickedAssetActivity.class);
                intent.putExtra("clickedAsset", new Gson().toJson(clickedAsset));
                Log.d("Main Activity", "clicked " + clickedAsset.getName());
                mContext.startActivity(intent);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new AssetDividerItemDecoration(this, R.drawable.asset_divider));

        loadAnswers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);
        SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(mContext, R.array.sorts, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(mSpinnerAdapter);
        spinner.setOnItemSelectedListener((new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sort = parent.getItemAtPosition(position).toString();
                Log.d("Main Activity", "sort changed to " + sort);
                mAdapter.sortAnswers(id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.spinner) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadAnswers() {
        mService.getAssets().enqueue(new Callback<List<Asset>>() {

            @Override
            public void onResponse(Call<List<Asset>> call, Response<List<Asset>> response) {
                if(response.isSuccessful()) {
                    mAdapter.updateAnswers(response.body());
                    Log.d("MainActivity", "posts loaded from API");
                }else {
                    int statusCode  = response.code();
                    // handle request errors depending on status code
                    Log.d("MainActivity", "status code " + statusCode);
                }
            }

            @Override
            public void onFailure(Call<List<Asset>> call, Throwable t) {
                Log.d("MainActivity", "error loading from API");
                Log.d("MainActivity", t.getMessage());
            }
        });
    }

    void refreshItems() {
        // Load items
        // ...
        loadAnswers();
        // Load complete
        Toast.makeText(mContext, "Assets Loaded", Toast.LENGTH_SHORT);
        Log.d("Main Activity", "assets loaded");
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        // Stop refresh animation
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshItems();
    }

}

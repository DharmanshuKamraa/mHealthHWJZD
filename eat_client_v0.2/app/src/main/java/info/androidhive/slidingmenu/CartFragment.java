package info.androidhive.slidingmenu;

import android.app.ActionBar;
import android.app.ListFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import info.androidhive.slidingmenu.adapter.FoodItemListAdapter;
import info.androidhive.slidingmenu.api.FoodItemConnect;
import info.androidhive.slidingmenu.interfaces.FoodItemAsyncResponse;
import info.androidhive.slidingmenu.model.FoodItem;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.view.PieChartView;

public class CartFragment extends ListFragment implements FoodItemAsyncResponse {

    private PieChartView chart;
    private PieChartData data;
    private ActionBar mActionBar;

    public CartFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cart, container, false);

        mActionBar = getActivity().getActionBar();
        mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
//        mActionBar.hide();
//        checking change

//        ListView myListView = (ListView) rootView.findViewById(R.id.);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.suggest_menu, menu);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        return super.onOptionsItemSelected(item);
//        switch (item.getItemId()) {
//            case R.id.explore_fragment:
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    public void getFoodList() {
        FoodItemConnect f = new FoodItemConnect();
        f.delegate = this;
        f.activity = getActivity();
        f.execute("GET" , "items/" , "");
    }

    public void processItemListFetched(String s) {
        Log.i("List items", s);
        ArrayList<FoodItem> foodItems = new ArrayList<FoodItem>();
        try {
            JSONArray jsonListItems = new JSONArray(s);
            for (int i = 0; i < jsonListItems.length(); i++) {
                JSONObject jsonObject = jsonListItems.getJSONObject(i);

//                FoodItem f = new FoodItem(1 , "Eggs" , "2$" , "http://globe-views.com/dcim/dreams/eggs/eggs-01.jpg" , "Co Op Hanover");
                FoodItem f = new FoodItem(
                        jsonObject.getInt("id") ,
                        jsonObject.getString("name") ,
                        jsonObject.getString("price") ,
                        "http://globe-views.com/dcim/dreams/eggs/eggs-01.jpg" ,
                        jsonObject.getString("store")
                );
                foodItems.add(f);
            }
            FoodItemListAdapter mAdapter = new FoodItemListAdapter(getActivity() ,  foodItems);
            setListAdapter(mAdapter);

        } catch (Exception e) {
            Log.i("ISSUE" , e.toString());
        }

    }
    public void processFailed(String s) {

    }

}

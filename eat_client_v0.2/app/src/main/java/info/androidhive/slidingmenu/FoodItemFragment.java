package info.androidhive.slidingmenu;

import android.app.Activity;
import android.app.ListFragment;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.FormatFlagsConversionMismatchException;

import info.androidhive.slidingmenu.adapter.FoodItemListAdapter;
import info.androidhive.slidingmenu.api.FoodItemConnect;
import info.androidhive.slidingmenu.interfaces.FoodItemAsyncResponse;
import info.androidhive.slidingmenu.model.FoodItem;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {
 * @link FoodItemFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link
 * FoodItemFragment
 * #newInstance} factory method to
 * create an instance of this fragment.
 */

public class FoodItemFragment extends ListFragment implements FoodItemAsyncResponse {
    public FoodItemFragment(){}
    Spinner food_type_spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_food_item, container, false);
//        FoodItem f1 = new FoodItem(1 , "Eggs" , "2$" , "http://globe-views.com/dcim/dreams/eggs/eggs-01.jpg" , "Co Op Hanover");
//        FoodItem f2 = new FoodItem(2 , "Eggs" , "2$" , "http://globe-views.com/dcim/dreams/eggs/eggs-01.jpg" , "Co Op Hanover");
//        ArrayList<FoodItem> foodItems = new ArrayList<FoodItem>();
//        foodItems.add(f1);
//        foodItems.add(f2);
//        FoodItemListAdapter mAdapter = new FoodItemListAdapter(getActivity() ,  foodItems);
//        setListAdapter(mAdapter);

        String[] strings = {"Protein" , "Cheese"};
        food_type_spinner = (Spinner) rootView.findViewById(R.id.explore_filter_type);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity() ,R.array.food_type ,
                android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource();
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        food_type_spinner.setAdapter(adapter);
        setInitialAdapter();
        return rootView;
    }

    public void setInitialAdapter() {
        FoodItemConnect f = new FoodItemConnect();
        f.delegate = this;
        f.activity = getActivity();
        f.execute("GET" , "items/" , "");
    }

    public void processItemListFetched(String s) {
        Log.i("List items" , s);
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

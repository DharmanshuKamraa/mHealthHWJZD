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

import java.util.ArrayList;

import info.androidhive.slidingmenu.adapter.FoodItemListAdapter;
import info.androidhive.slidingmenu.model.FoodItem;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FoodItemFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FoodItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodItemFragment extends ListFragment {
    public FoodItemFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_food_item, container, false);
        FoodItem f1 = new FoodItem(1 , "Eggs" , "2$" , "http://globe-views.com/dcim/dreams/eggs/eggs-01.jpg" , "Co Op Hanover");
        FoodItem f2 = new FoodItem(2 , "Eggs" , "2$" , "http://globe-views.com/dcim/dreams/eggs/eggs-01.jpg" , "Co Op Hanover");
        ArrayList<FoodItem> foodItems = new ArrayList<FoodItem>();
        foodItems.add(f1);
        foodItems.add(f2);
        FoodItemListAdapter mAdapter = new FoodItemListAdapter(getActivity() ,  foodItems);
        setListAdapter(mAdapter);
        return rootView;
    }

    public void onFoodItemAddToCartClicked(View v){
        Log.i("TAG", "Add to cart clicked in activty");
    }
}

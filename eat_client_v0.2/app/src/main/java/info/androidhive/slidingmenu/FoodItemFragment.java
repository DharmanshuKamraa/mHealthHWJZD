package info.androidhive.slidingmenu;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import info.androidhive.slidingmenu.api.FoodItemConnect;
import info.androidhive.slidingmenu.interfaces.FoodItemAsyncResponse;
import info.androidhive.slidingmenu.model.FoodItemCard;
import info.androidhive.slidingmenu.utils.CustomCardExapander;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;


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

public class FoodItemFragment extends Fragment implements FoodItemAsyncResponse {
    public FoodItemFragment(){}
    Spinner food_type_spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_food_item, container, false);

//        String[] strings = {"Protein" , "Cheese"};
//        food_type_spinner = (Spinner) rootView.findViewById(R.id.explore_filter_type);

//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity() ,R.array.food_type ,
//                android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource();
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        food_type_spinner.setAdapter(adapter);
        setInitialAdapter();
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
        inflater.inflate(R.menu.sort_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
//        switch (item.getItemId()) {
//            case R.id.sort_it1:
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
    }

    public void setInitialAdapter() {
        FoodItemConnect f = new FoodItemConnect();
        f.delegate = this;
        f.activity = getActivity();
        f.execute("GET" , "items/" , "");
    }

    public void makeFilterCall(String q) {
        FoodItemConnect f = new FoodItemConnect();
        f.delegate = this;
        f.activity = getActivity();
        if (q.isEmpty()) {
            f.execute("GET" , "items/" , "");
        } else {
            f.execute("GET" , "items/" , "q="+q);
        }

    }

    public void processItemListFetched(String s) {
//        ArrayList<FoodItem> foodItems = new ArrayList<FoodItem>();
        ArrayList<Card> cards = new ArrayList<Card>();
        try {
            JSONArray jsonListItems = new JSONArray(s);
            for (int i = 0; i < jsonListItems.length(); i++) {
                JSONObject jsonObject = jsonListItems.getJSONObject(i);
                FoodItemCard card = new FoodItemCard(getActivity());
                card.setParams(
                            jsonObject.getLong("id") ,
                            jsonObject.getString("name") ,
                            "Co Op Hanover." ,
                            jsonObject.getString("price") ,
                            jsonObject.getInt("check_carted_by_user") ,
                            jsonObject.getString("image_url")
                );

                CustomCardExapander cardExpand = new CustomCardExapander(getActivity() ,
                                                                        jsonObject.getString("carbs"),
                                                                        jsonObject.getString("protein"),
                                                                        jsonObject.getString("fats"),
                                                                        jsonObject.getString("vitamins")
                );

                card.addCardExpand(cardExpand);

                cards.add(card);
                Log.i("check loop" , "here");
            }

            CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(),cards);

            CardListView listView = (CardListView) getActivity().findViewById(R.id.myCardList);
            listView.setAdapter(mCardArrayAdapter);

            EditText search_view = (EditText) getActivity().findViewById(R.id.search_filter);
            search_view.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    makeFilterCall(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        } catch (Exception e) {
            Log.i("ISSUE" , e.toString());
        }
    }

    public void processFailed(String s) {

    }
}

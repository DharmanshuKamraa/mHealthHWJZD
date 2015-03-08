package info.androidhive.slidingmenu;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import info.androidhive.slidingmenu.api.CartProgressConnect;
import info.androidhive.slidingmenu.api.FoodItemConnect;
import info.androidhive.slidingmenu.interfaces.CartProgressAsyncResponse;
import info.androidhive.slidingmenu.interfaces.FoodItemAsyncResponse;
import info.androidhive.slidingmenu.model.FoodItemCard;
import info.androidhive.slidingmenu.utils.CustomCardExapander;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardListView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyCartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyCartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class MyCartFragment extends Fragment implements FoodItemAsyncResponse , CartProgressAsyncResponse {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_my_cart, container, false);
        setInitialAdapter();
        return rootView;
    }

    public void setProgressButtonClick() {
        TextView progress_view = (TextView) getActivity().findViewById(R.id.txt_progress_view);
        progress_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity() , "Checking" , Toast.LENGTH_LONG);
                LinearLayout linearLayout = (LinearLayout) getActivity().findViewById(R.id.progress_view);
                linearLayout.animate().translationY(-1*linearLayout.getHeight()).alpha(0);
            }
        });
    }

    public void setInitialAdapter() {
        FoodItemConnect f = new FoodItemConnect();
        f.delegate = this;
        f.activity = getActivity();
        f.execute("GET" , "items/" , "carted=1");

        CartProgressConnect c = new CartProgressConnect();
        c.delegate = this;
        c.activity = getActivity();
        c.execute("GET" , "fetch_cart_progress" , "");
    }

    public void makeFilterCall(String q) {
        FoodItemConnect f = new FoodItemConnect();
        f.delegate = this;
        f.activity = getActivity();
        if (q.isEmpty()) {
            f.execute("GET" , "items/" , "carted=1");
        } else {
            f.execute("GET" , "items/" , "carted=1&q="+q);
        }
    }

    public void processItemListFetched(String s) {
//        ArrayList<FoodItem> foodItems = new ArrayList<FoodItem>();
        Log.i("checking" , "here is");

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

                CustomCardExapander cardExpand = new CustomCardExapander(getActivity());
                card.addCardExpand(cardExpand);
                cards.add(card);
            }

            final CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(),cards);

            CardListView listView = (CardListView) getActivity().findViewById(R.id.myCardList);
            listView.setAdapter(mCardArrayAdapter);
        } catch (Exception e) {
            Log.i("ISSUE" , e.toString());
        }
    }

    public void processCartProgress(String s) {
        Log.i("Results" , s);
        try {
            JSONObject progress = new JSONObject(s);

            RoundCornerProgressBar progressCarbs = (RoundCornerProgressBar) getActivity().findViewById(R.id.progress_carbs);
            progressCarbs.setProgress(progress.getLong("carbs"));

            RoundCornerProgressBar progressProtein = (RoundCornerProgressBar) getActivity().findViewById(R.id.progress_proteins);
            progressProtein.setProgress(progress.getLong("protein"));

            RoundCornerProgressBar progressFats = (RoundCornerProgressBar) getActivity().findViewById(R.id.progress_fats);
            progressFats.setProgress(progress.getLong("fats"));

            RoundCornerProgressBar progressVitamins = (RoundCornerProgressBar) getActivity().findViewById(R.id.progress_vitamins);
            progressVitamins .setProgress(progress.getLong("vitamins"));

        } catch (Exception e) {
            Log.i("INCORRECT_JSON" , e.toString());
        }

    }

    public void processFailed(String s) {

    }

}

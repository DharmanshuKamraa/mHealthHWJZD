package info.androidhive.slidingmenu.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import info.androidhive.slidingmenu.R;
import it.gmariotti.cardslib.library.internal.CardExpand;

/**
 * Created by Dharmanshu on 3/4/15.
 */
public class CustomCardExapander extends CardExpand {
    protected String mCarbs = "200g";
    protected String mProtein = "150g";
    protected String mFats = "140g";
    protected String mViatmins  = "120g";

    public CustomCardExapander (Context context) {
        super(context, R.layout.custom_inner_base_expand);
    }

    public CustomCardExapander(Context context , String carbs , String protein , String fats , String vitamins) {
        super(context, R.layout.custom_inner_base_expand);
        mCarbs = carbs + "g";
        mProtein = protein + "g";
        mFats = fats + "g";
        mViatmins = vitamins + "g";
    }

    public void setupInnerViewElements(ViewGroup parent, View view) {

        if (view == null) return;

        //Retrieve TextView elements
        TextView tx1 = (TextView) view.findViewById(R.id.card_expand_inner_carbs);
        tx1.setText("CARBOHYDRATES : " + mCarbs);

        TextView tx2 = (TextView) view.findViewById(R.id.card_expand_inner_proteins);
        tx2.setText("PROTEINS : " + mProtein);


        TextView tx3 = (TextView) view.findViewById(R.id.card_expand_inner_fats);
        tx3.setText("FATS : " + mFats);

        TextView tx4 = (TextView) view.findViewById(R.id.card_expand_inner_vitamins);
        tx4.setText("VITAMINS : " + mViatmins);
//
//        //Set value in text views
//        if (tx1 != null) {
//            tx1.setText(getContext().getString(R.string.demo_expand_customtitle1));
//        }
//
//        if (tx2 != null) {
//            tx2.setText(getContext().getString(R.string.demo_expand_customtitle2));
//        }
    }
}

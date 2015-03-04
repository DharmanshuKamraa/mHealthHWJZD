package info.androidhive.slidingmenu.model;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import info.androidhive.slidingmenu.R;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by Dharmanshu on 3/3/15.
 */
public class FoodItemCard extends Card {

    protected String mTitle;
    protected String mPrice;
    protected String mSeller;
    protected Long mId;

    /**
     * Constructor with a custom inner layout
     * @param context
     */
    public FoodItemCard(Context context) {
        this(context, R.layout.food_item_card);
    }

    public void setParams(Long id, String title , String seller , String price) {
        this.mId = id;
        this.mTitle = title;
        this.mSeller= seller;
        this.mPrice = price;
    }

    /**
     *
     * @param context
     * @param innerLayout
     */
    public FoodItemCard(Context context, int innerLayout) {
        super(context, innerLayout);
        init();
    }

    /**
     * Init
     */
    private void init(){

        //No Header

        //Set a OnClickListener listener
        setOnClickListener(new OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                Toast.makeText(getContext(), "Click Listener card=", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        setShadow(true);
        setCardElevation(2);
        //Retrieve elements
        TextView sellerView = (TextView) parent.findViewById(R.id.seller);
//        mSecondaryTitle = (TextView) parent.findViewById(R.id.carddemo_myapps_main_inner_secondaryTitle);
//        mRatingBar = (RatingBar) parent.findViewById(R.id.carddemo_myapps_main_inner_ratingBar);
//

        if (mTitle!=null)
            sellerView.setText("SELLER :- " + mSeller);

        TextView titleView = (TextView) parent.findViewById(R.id.title);
        titleView.setText(mTitle);

        Button buttonView = (Button) parent.findViewById(R.id.btn_add_to_cart);
        buttonView.setOnClickListener( new View.OnClickListener() {
            boolean added = false;
            @Override
            public void onClick(View v) {
                if (added == false) {
                    v.setBackgroundResource(R.drawable.ok);
                } else {
                    v.setBackgroundResource(R.drawable.plus);
                }
            }
        });

//
//        if (mSecondaryTitle!=null)
//            mSecondaryTitle.setText(R.string.demo_custom_card_googleinc);
//
//        if (mRatingBar!=null)
//            mRatingBar.setNumStars(5);
//        mRatingBar.setMax(5);
//        mRatingBar.setRating(4.7f);

    }
}

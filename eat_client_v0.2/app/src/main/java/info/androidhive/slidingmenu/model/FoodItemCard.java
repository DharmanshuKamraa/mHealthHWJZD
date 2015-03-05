package info.androidhive.slidingmenu.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;

import info.androidhive.slidingmenu.R;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardExpand;
import it.gmariotti.cardslib.library.internal.ViewToClickToExpand;


/**
 * Created by Dharmanshu on 3/3/15.
 */
public class FoodItemCard extends Card {

    protected String mTitle;
    protected String mPrice;
    protected String mSeller;
    protected Long mId;
    protected Boolean mIsCarted;
    protected Boolean mIsExpanded = false;
    protected String mImageUrl;
    protected Bitmap mDrawableImage;
    protected Boolean imageSet = false;

    /**
     * Constructor with a custom inner layout
     * @param context
     */
    public FoodItemCard(Context context) {
        this(context, R.layout.food_item_card);
    }

    public void setParams(Long id, String title , String seller , String price , int isCarted , String imageUrl) {
        this.mId = id;
        this.mTitle = title;
        this.mSeller= seller;
        this.mPrice = price;
        this.mImageUrl = imageUrl;
        Log.i("image_url" , mImageUrl);

        if (isCarted == 0) {
            this.mIsCarted = false;
        } else {
            this.mIsCarted = true;
        }
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
//        setOnClickListener(new OnCardClickListener() {
//            @Override
//            public void onClick(Card card, View view) {
//                Toast.makeText(getContext(), "Click Listener card=" + mTitle, Toast.LENGTH_LONG).show();
//            }
//        });
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        ViewToClickToExpand viewToClickToExpand = ViewToClickToExpand.builder().setupView(view);
        this.setViewToClickToExpand(viewToClickToExpand);

        setShadow(true);
        setCardElevation(2);
        TextView sellerView = (TextView) parent.findViewById(R.id.seller);

        if (mTitle!=null)
            sellerView.setText("SELLER : " + mSeller);

        TextView titleView = (TextView) parent.findViewById(R.id.title);
        titleView.setText(mTitle);

        TextView priceView = (TextView) parent.findViewById(R.id.price);
        priceView.setText("PRICE : " + mPrice + "$");

        Button buttonView = (Button) parent.findViewById(R.id.btn_add_to_cart);

        ImageView imageView = (ImageView) parent.findViewById(R.id.card_thumbnail_image);

        if (!imageSet) {
            DownloadImageTask downloadImageTask = new DownloadImageTask(imageView);
            downloadImageTask.execute(mImageUrl);
            imageView.setImageResource(R.drawable.cart_red);
        } else {
            imageView.setImageBitmap(mDrawableImage);
        }
//

        if (mIsCarted == true) {
            buttonView.setBackgroundResource(R.drawable.ok);
        } else {
            buttonView.setBackgroundResource(R.drawable.plus);
        }

        buttonView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsCarted == false) {
                    v.setBackgroundResource(R.drawable.ok);
                    mIsCarted = true;
                    Toast.makeText(getContext(), mTitle + "has been added to your cart.", Toast.LENGTH_SHORT).show();
                } else {
                    v.setBackgroundResource(R.drawable.plus);
                    mIsCarted = false;
                    Toast.makeText(getContext(), mTitle + "has been removed from your cart.", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        if (mSecondaryTitle!=null)
//            mSecondaryTitle.setText(R.string.demo_custom_card_googleinc);
//
//        if (mRatingBar!=null)
//            mRatingBar.setNumStars(5);
//        mRatingBar.setMax(5);
//        mRatingBar.setRating(4.7f);

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Log.i("downloading" , urls[0]);

            Bitmap mIcon11 = null;
            try {
                mIcon11 = BitmapFactory.decodeStream((InputStream) new URL(urls[0].replaceAll(" ", "%20")).getContent());
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            if (imageSet == false) {
                bmImage.setImageBitmap(result);
                imageSet = true;
                mDrawableImage = result;
            }
        }
    }

}

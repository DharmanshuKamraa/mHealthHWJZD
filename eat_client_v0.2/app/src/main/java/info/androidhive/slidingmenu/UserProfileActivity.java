package info.androidhive.slidingmenu;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class UserProfileActivity extends Activity {

    private Uri mImageCaptureUri;
    private ImageView mImageView;
    // temp buffer for storing the image
    private byte[] mProfilePictureArray;

    private static final String URI_INSTANCE_STATE_KEY = "saved_uri";
    private static final String TMP_PROFILE_IMG_KEY = "saved_uri";

    private static final String IMAGE_UNSPECIFIED = "image/*";
    public static final int REQUEST_CODE_TAKE_FROM_CAMERA = 0;
    public static final int REQUEST_CODE_CROP_PHOTO = 2;

    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    private boolean isTakenFromCamera;

    public void onSaveClicked(View v) {
        // Save profile
        saveProfile();
        // Making a "toast" informing the user the profile is saved.
        Toast.makeText(getApplicationContext(),
                getString(R.string.ui_profile_toast_save_text),
                Toast.LENGTH_SHORT).show();
        // Close the activity
        finish();
    }



    // generate a temp file uri for capturing profile photo
    private Uri getPhotoUri() {
        Uri photoUri = Uri.fromFile(new File(Environment
                .getExternalStorageDirectory(), JPEG_FILE_PREFIX
                + String.valueOf(System.currentTimeMillis()) + JPEG_FILE_SUFFIX));

        return photoUri;
    }

    public void onChangePhotoClicked(View v) {
        // Take photo from camera，
        // Construct an intent with action
        // MediaStore.ACTION_IMAGE_CAPTURE

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Construct temporary image path and name to save the taken
        // photo
        mImageCaptureUri = getPhotoUri();

        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                mImageCaptureUri);
        intent.putExtra("return-data", true);
        try {
            startActivityForResult(intent, REQUEST_CODE_TAKE_FROM_CAMERA);
        }catch (ActivityNotFoundException e){
            e.printStackTrace();
        }
        isTakenFromCamera = true;

    }

    public void onLogoutClicked(View v) {
        // Making a "toast" informing the user changes are canceled.
        Toast.makeText(getApplicationContext(),
                getString(R.string.ui_profile_toast_logout_text),
                Toast.LENGTH_SHORT).show();
        // Close the activity
        finish();
    }

    // Crop and resize the image for profile
    private void cropImage() {
        // Use existing crop activity.
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(mImageCaptureUri, IMAGE_UNSPECIFIED);

        // Specify image size
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);

        // Specify aspect ratio, 1:1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);

        // REQUEST_CODE_CROP_PHOTO is an integer tag you defined to
        // identify the activity in onActivityResult() when it returns
        startActivityForResult(intent, REQUEST_CODE_CROP_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_CODE_TAKE_FROM_CAMERA: {
                if (resultCode == RESULT_OK) {
                    cropImage();
                    break;
                }
                break;
            } // ACTION_TAKE_PHOTO_B
            case REQUEST_CODE_CROP_PHOTO: {
                Bundle extras = data.getExtras();
                if (extras!=null){
                    Bitmap photo = extras.getParcelable("data");
                    // load the byte array to the image view
                    dumpImage(photo);
                    loadImageToView();
                }
                if(isTakenFromCamera){
                    File f = new File(mImageCaptureUri.getPath());
                    if(f.exists())
                        f.delete();
                }
                break;
            }
        }
    }

    //load profile from SharedPreferences
    private void loadProfile() {

        String key, str_val;
        int int_val;

        // Load and update all profile views
        key = getString(R.string.preference_private);
        SharedPreferences prefs = getSharedPreferences(key, MODE_PRIVATE);

        // Load user name
        key = getString(R.string.preference_key_profile_name);
        str_val = prefs.getString(key, "");
        ((EditText) findViewById(R.id.editName)).setText(str_val);

        // Load user email
        key = getString(R.string.preference_key_profile_email);
        str_val = prefs.getString(key, "");
        ((EditText) findViewById(R.id.editEmail)).setText(str_val);

        // Load user phone number
        key = getString(R.string.preference_key_profile_psw);
        str_val = prefs.getString(key, "");
        ((EditText) findViewById(R.id.editPsw)).setText(str_val);

        // Load gender info and set radio box
        key = getString(R.string.preference_key_profile_motivate);
        int_val = prefs.getInt(key, -1);
        if (int_val >= 0) {
            RadioButton radioBtn = (RadioButton) ((RadioGroup) findViewById(R.id.radioMotivate))
                    .getChildAt(int_val);
            radioBtn.setChecked(true);
        }

        // Load student major info
        key = getString(R.string.preference_key_profile_zipcode);
        str_val = prefs.getString(key, "");
        ((EditText) findViewById(R.id.editZipcode)).setText(str_val);

        // Load profile photo from internal storage
        try {
            // open the file using a file input stream
            FileInputStream fis = openFileInput(getString(R.string.profile_photo_file_name));
            // the file's data will be read into a bytearray output stream
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            // inputstream -> buffer -> outputstream
            byte[] buffer = new byte[5 * 1024];
            int n;
            // read data in a while loop
            while ((n = fis.read(buffer)) > -1) {
                bos.write(buffer, 0, n); // Don't allow any extra bytes to creep
                // in, final write
            }
            fis.close();
            //get the byte array from the ByteArrayOutputStream
            mProfilePictureArray = bos.toByteArray();
            bos.close();
        } catch (IOException e) {
            mImageView.setImageResource(R.drawable.default_profile);
        }

        // load the byte array to the image view
        loadImageToView();
    }

    //save profile to SharedPreferences
    private void saveProfile() {

        String key, str_val;
        int int_val;

        key = getString(R.string.preference_private);
        SharedPreferences prefs = getSharedPreferences(key, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();

        // Write screen contents into corresponding editor fields.
        key = getString(R.string.preference_key_profile_name);
        str_val = ((EditText) findViewById(R.id.editName)).getText().toString();
        editor.putString(key, str_val);

        key = getString(R.string.preference_key_profile_email);
        str_val = ((EditText) findViewById(R.id.editEmail)).getText()
                .toString();
        editor.putString(key, str_val);

        key = getString(R.string.preference_key_profile_psw);
        str_val = ((EditText) findViewById(R.id.editPsw)).getText()
                .toString();
        editor.putString(key, str_val);

        key = getString(R.string.preference_key_profile_motivate);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioMotivate);
        int_val = radioGroup.indexOfChild(findViewById(radioGroup
                .getCheckedRadioButtonId()));
        editor.putInt(key, int_val);

        key = getString(R.string.preference_key_profile_zipcode);
        str_val = ((EditText) findViewById(R.id.editZipcode)).getText()
                .toString();
        editor.putString(key, str_val);

        editor.commit();
        // Save profile image into internal storage.
        try {
            // if the user did not change default profile
            // picture, mProfilePictureArray will be null

            FileOutputStream fos = openFileOutput(
                    getString(R.string.profile_photo_file_name), MODE_PRIVATE);
            fos.write(mProfilePictureArray);
            fos.flush();
            fos.close();

        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
    }
    // load image byte array to image view
    private void loadImageToView() {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(
                    mProfilePictureArray);
            Bitmap bmap = BitmapFactory.decodeStream(bis);
            mImageView.setImageBitmap(bmap);
            bis.close();
        } catch (Exception ex) {
        }
    }
    // convert bitmap to byte array
    private void dumpImage(Bitmap bmap) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            mProfilePictureArray = bos.toByteArray();
            bos.close();
        } catch (IOException ioe) {

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the image capture uri before the activity goes into background
        // Uri is a Parcelable object, so use putParcelable() to put it into the Bundle
        // see http://developer.android.com/reference/android/os/Bundle.html#putParcelable(java.lang.String, android.os.Parcelable)
        outState.putParcelable(URI_INSTANCE_STATE_KEY, mImageCaptureUri);

        // Save profile image into internal storage.
        if (mProfilePictureArray != null) {
            // use putByteArray() to put a byte array into a Bundle
            // see http://developer.android.com/reference/android/os/Bundle.html#putByteArray(java.lang.String, byte[])
            outState.putByteArray(TMP_PROFILE_IMG_KEY, mProfilePictureArray);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mImageView = (ImageView) findViewById(R.id.imageProfile);

        if(savedInstanceState != null)
            mImageCaptureUri = savedInstanceState.getParcelable(URI_INSTANCE_STATE_KEY);

        loadProfile();
    }
}

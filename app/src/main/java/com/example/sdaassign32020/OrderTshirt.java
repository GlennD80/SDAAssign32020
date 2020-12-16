package com.example.sdaassign32020;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaScannerConnection;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static android.app.Activity.RESULT_CANCELED;
import static android.os.Environment.DIRECTORY_PICTURES;
//import javax.print.attribute.standard.Media;


/*
 * A simple {@link Fragment} subclass.
 * @author Chris Coughlan 2019
 */
public class OrderTshirt extends Fragment {

    public OrderTshirt() {
        // Required empty public constructor
    }

    //class wide variables
    private Spinner mSpinner;
    private EditText mCustomerName;
    private EditText meditDelivery;
    private ImageView mCameraImage;
    private TextView meditCollection;
    private boolean setColMessage;
    private boolean setDelMessage;
    Uri imageUri;
    private File image = null;


    //static keys
    private static final int REQUEST_TAKE_PHOTO = 2;
    private static final String TAG = "OrderTshirt";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment get the root view.
        final View root = inflater.inflate(R.layout.fragment_order_tshirt, container, false);

        mCustomerName = root.findViewById(R.id.editCustomer);
        meditDelivery = root.findViewById(R.id.editDeliver);
        meditDelivery.setImeOptions(EditorInfo.IME_ACTION_DONE);
        meditDelivery.setRawInputType(InputType.TYPE_CLASS_TEXT);
        meditCollection = root.findViewById(R.id.editCollect);

        mCameraImage = root.findViewById(R.id.imageView);
        Button mSendButton = root.findViewById(R.id.sendButton);

        //set a listener on the the camera image
        mCameraImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dispatchTakePictureIntent(v);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        //set a listener to start the email intent.
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail(v);
            }
        });

        //set a focus for delivery field
        meditDelivery.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean hasfocus) {
                if (hasfocus) {
                    disableCollect();
                } else {

                }
            }
        });

        //mSpinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        //initialise spinner using the integer array
        mSpinner = root.findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(root.getContext(), R.array.ui_time_entries, R.layout.spinner_days);
        mSpinner.setAdapter(adapter);
        mSpinner.setEnabled(true);

        return root;
    }

    private void sendEmail(View v) {
        //check that Name is not empty, and ask do they want to continue
        String customerName = mCustomerName.getText().toString();
        String delAddress = meditDelivery.getText().toString();
        if (mCustomerName == null || customerName.equals(""))
        {
            Toast.makeText(getContext(), "Enter your name, Please", Toast.LENGTH_SHORT).show();

        } else if(setDelMessage == true && (delAddress.equals(null) || delAddress.equals(""))) {
            Toast.makeText(getContext(),"Enter your address, Please", Toast.LENGTH_SHORT).show();

            /* we can also use a dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Notification!").setMessage("Customer Name not set.").setPositiveButton("OK", null).show();*/

        } else {

            Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{"glenngarmets@me.ie"});
            email.putExtra(Intent.EXTRA_SUBJECT, "Order Request");
            email.putExtra(Intent.EXTRA_TEXT, createOrderSummary(v));
            email.putExtra(Intent.EXTRA_STREAM, imageUri);
            email.setType("image/jpeg");

            email.setType("message/rfc822");
            startActivity(Intent.createChooser(email, "Choose an Email client: "));
        }
    }

    private String createOrderSummary(View v) {
        {
            String orderMessage = "";
            String deliveryInstruction = meditDelivery.getText().toString();
            String customerName = getString(R.string.customer_name) + ": " + mCustomerName.getText().toString();

            orderMessage += customerName + "\n" + "\n" + getString(R.string.order_message_1) + "\n";

            //sets email message content based on the whether the user picks delivery or collection methos
            if(setDelMessage == true){
                orderMessage += "\n" + "Deliver order to this address";
                orderMessage += "\n" + deliveryInstruction + "\n";
            } else if (setDelMessage == false){
                orderMessage += "\n" + getString(R.string.order_message_collect) + mSpinner.getSelectedItem().toString() + " days." + "\n";
            }

            orderMessage += "\n" + getString(R.string.order_message_end) + "\n" + mCustomerName.getText().toString();

            return orderMessage;
        }
    }
    
    /**
     * disable collect spinner and text with onclick
     */
    //set a listener on radio buttons to execute code based on radio selection
    private void disableCollect() {
        String deliveryInstruction = meditDelivery.getText().toString();
        if(deliveryInstruction != null || !deliveryInstruction.equals(""))
        {
            meditCollection.setVisibility(View.INVISIBLE);
            mSpinner.setVisibility(View.INVISIBLE);
            setDelMessage = true;
        } else {
            Log.i(TAG, "onClick: Collection Disabled");
        }
    }

    /**
     * <p>This intent method starts our camera and allows for a picture to be taken and passed back<br>
     *     It also creates a places for the image file to be saved</p>
     *     <br>
     * <p>the following code was adapted from the following: <br>
     *
     */
    //Take a photo note the view is being passed so we can get context because it is a fragment.
    //update this to save the image so it can be sent via email
    private void dispatchTakePictureIntent(View v) throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(v.getContext().getPackageManager()) != null) {
           /* File photoFile = null;
            photoFile = createImageFile(REQUEST_TAKE_PHOTO);
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);*/
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    //}

    /**
     * <p>onActivityResult method returns the image taken by the camera. It also executes the galleryAddPic(),
     * setPic() method, and sets the image in the imageView replacing the default image </p>
     * <p>The code used in this onActivityResult method and was adapted from a few sources like stackoverflow <br>
     * </p>
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK && data !=null){
            Bundle bundle = data.getExtras();
            Bitmap finalPhoto = (Bitmap) bundle.get("data");
            mCameraImage.setImageBitmap(finalPhoto);

            BitmapDrawable drawable = (BitmapDrawable) mCameraImage.getDrawable();
            Bitmap bitmap = drawable.getBitmap();

            //imageUri = data.getData();
            //Uri imageUri = data.getData();
            //Bitmap bmap = mCameraImage.getDrawingCache();
            //mCameraImage = root.findViewById(R.id.imageView);
            //ImageView image = findViewById(R.id.imageView);
            //image.setImageBitmap(finalPhoto);
            //BitmapDrawable drawable = (BitmapDrawable) mCameraImage.getDrawable();
            //Bitmap bitmap = drawable.getBitmap();

        }
    }
}

    /**
    *  <p>method that creates a timestamp for picture file </p> <br>
    *  Adapted from https://developer.android.com/training/camera/photobasics
    */
    /*String currentPhotoPath;
    private File createImageFile(int requestTakePhoto) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  *//* prefix *//*
                ".jpg",         *//* suffix *//*
                storageDir      *//* directory *//*
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

     /**
     * <p>the galleryAddPic method adds photo to device gallery and makes it available to the user</p> <br>
     * following code was adapted from: https://developer.android.com/training/camera/photobasics
     *
     */
/*    public void galleryAddPic()
    {
        // Tell the media scanner about the new file so that it is
        // immediately available to the user.
        MediaScannerConnection.scanFile(getActivity(),
                new String[] { image.toString() }, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                        imageUri = uri;
                    }
                });
    }*/

    /**
     * <p>@param path is passed to this method with a string object of the absolute path of the image file created. <br>
     *      * The purpose of this method is to get the dimensions of the imageView and make sure the image taken will fit inside of it
     *      </p>
     * <p>this setPic method was adapted from the following:
     *    Android documentation: https://developer.android.com/training/camera/photobasics</span><br>
     * </p>
     */
    //formats image to fit in the imageview
/*    private void setPic(String currentPhotoPath) {
        // Get the dimensions of the View
        int targetW = mCameraImage.getWidth();
        int targetH = mCameraImage.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(this.currentPhotoPath, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.max(1, Math.min(photoW/targetW, photoH/targetH));

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(this.currentPhotoPath, bmOptions);
        mCameraImage.setImageBitmap(bitmap);
    }*/


    /**
     * <p>onActivityResult method returns the image taken by the camera. It also executes the galleryAddPic(),
     * setPic() method, and sets the image in the imageView replacing the default image </p>
     * <p>The code used in this onActivityResult method was adapted from a few sources including: <br>
     * Android Documentation: https://developer.android.com/training/camera/photobasics</span><br>
     * </p>
     */
/*    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_TAKE_PHOTO){
            if (resultCode == RESULT_OK) {
                //add image to gallery
                galleryAddPic();
                //fit the returned image into the imageview
                setPic(currentPhotoPath);
                //crates a bitmap from the path of the returned image
                Bitmap bp = BitmapFactory.decodeFile(currentPhotoPath);
                //sets the bitmap int the imageview
                mCameraImage.setImageBitmap(bp);
            }
        } else if(resultCode == RESULT_CANCELED){
            Toast.makeText(getActivity(), "picture capture fail", Toast.LENGTH_LONG).show();
        }*/

    //}
//}
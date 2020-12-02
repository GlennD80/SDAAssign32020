package com.example.sdaassign32020;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import java.io.Serializable;

import static android.app.Activity.RESULT_OK;


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


    //static keys
    private static final int REQUEST_TAKE_PHOTO = 2;
    private static final String TAG = "OrderTshirt";

    ImageView selectedImage;

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
                dispatchTakePictureIntent(v);
            }
        });

        //set a listener to start the email intent.
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail(v);
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


    //Take a photo note the view is being passed so we can get context because it is a fragment.
    //update this to save the image so it can be sent via email
    private void dispatchTakePictureIntent(View v)
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(v.getContext().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);




        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK && data !=null){
            Bundle bundle = data.getExtras();
            Bitmap finalPhoto = (Bitmap) bundle.get("data");
            mCameraImage.setImageBitmap(finalPhoto);

            BitmapDrawable drawable = (BitmapDrawable) mCameraImage.getDrawable();
            //imageUri = data.getData();
            Bitmap bitmap = drawable.getBitmap();
            //Uri imageUri = data.getData();

            //Bitmap bmap = mCameraImage.getDrawingCache();

            //mCameraImage = root.findViewById(R.id.imageView);

            //ImageView image = findViewById(R.id.imageView);
            //image.setImageBitmap(finalPhoto);

            //BitmapDrawable drawable = (BitmapDrawable) mCameraImage.getDrawable();
            //Bitmap bitmap = drawable.getBitmap();

        }
    }



    /*
     * Returns the Email Body Message, update this to handle either collection or delivery
     */
    private String createOrderSummary(View v)
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

    //Update to send an email
    private void sendEmail(View v)
    {
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
            //email.putExtra(Intent.EXTRA_STREAM, imageUri);
            email.setType("image/jpeg");

            email.setType("message/rfc822");
            startActivity(Intent.createChooser(email, "Choose an Email client: "));
        }
    }
}



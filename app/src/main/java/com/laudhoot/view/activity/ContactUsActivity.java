package com.laudhoot.view.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.laudhoot.Laudhoot;
import com.laudhoot.R;
import com.laudhoot.util.LocationStateManager;
import com.laudhoot.util.NetworkStateManager;
import com.laudhoot.util.Toaster;
import com.laudhoot.view.fragment.ShoutFeedFragment;
import com.laudhoot.web.WebConstants;
import com.laudhoot.web.util.AuthorizationUtil;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by root on 29/1/16.
 */
public class ContactUsActivity extends ActionBarActivity {

    public static final String LATITUDE = "lat";
    public static final String LONGITUDE = "lon";

    private static final int LOAD_SCREENSHOT_ONE = 551;
    private static final int LOAD_SCREENSHOT_TWO = 552;
    private static final int LOAD_SCREENSHOT_THREE = 553;
    private static final int SEND_EMAIL_TO_SUPPORT = 554;
    private static final int READ_FAQ = 555;

    private String clientId;
    private String geofenceCode;
    private Double latitude;
    private Double longitude;
    private boolean viewedFAQ;

    @Inject
    Toaster toaster;

    @Inject
    LocationStateManager locationStateManager;

    @Inject
    TelephonyManager telephonyManager;

    @Inject
    NetworkStateManager networkStateManager;

    @Bind(R.id.problem_description)
    EditText problemDescription;

    @Bind(R.id.problem_screenhot1)
    ImageButton screenshot1;

    @Bind(R.id.problem_screenhot2)
    ImageButton screenshot2;

    @Bind(R.id.problem_screenhot3)
    ImageButton screenshot3;

    ArrayList<String> files = new ArrayList<String>(3);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_us);
        ButterKnife.bind(this);
        ((Laudhoot) (getApplication())).inject(this);
        clientId = getIntent().getStringExtra(InitializationActivity.CLIENT_ID);
        geofenceCode = getIntent().getStringExtra(InitializationActivity.GEOFENCE_CODE);
        latitude = getIntent().getDoubleExtra(LATITUDE, 0);
        longitude = getIntent().getDoubleExtra(LONGITUDE, 0);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.laudhoot_theme_color)));
        actionBar.setIcon(R.drawable.ic_launcher);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact_us, menu);
        return true;
    }

    @OnClick(R.id.read_faq)
    public void readFAQ(Button readFAQ) {
        viewedFAQ = true;
        Intent intent = new Intent(this, FAQSActivity.class);
        intent.putExtra(InitializationActivity.CLIENT_ID, clientId);
        startActivityForResult(intent, READ_FAQ);
    }

    @OnClick(R.id.problem_screenhot1)
    public void attachScreenshotOne() {
        startActivityForResult(getLoadScreenshotIntent(), LOAD_SCREENSHOT_ONE);
    }

    @OnClick(R.id.problem_screenhot2)
    public void attachScreenshotTwo() {
        startActivityForResult(getLoadScreenshotIntent(), LOAD_SCREENSHOT_TWO);
    }

    @OnClick(R.id.problem_screenhot3)
    public void attachScreenshotThree() {
        startActivityForResult(getLoadScreenshotIntent(), LOAD_SCREENSHOT_THREE);
    }

    private Intent getLoadScreenshotIntent() {
        return new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && null != data) {
            String picturePath = getPicturePath(data);
            if (requestCode == LOAD_SCREENSHOT_ONE) {
                files.add(0, picturePath);
                screenshot1.setBackgroundTintList(null);
                screenshot1.setBackground(new BitmapDrawable(BitmapFactory.decodeFile(picturePath)));
            } else if (requestCode == LOAD_SCREENSHOT_TWO) {
                files.add(1, picturePath);
                screenshot2.setBackgroundTintList(null);
                screenshot2.setBackground(new BitmapDrawable(BitmapFactory.decodeFile(picturePath)));
            } else if (requestCode == LOAD_SCREENSHOT_THREE) {
                files.add(2, picturePath);
                screenshot3.setBackgroundTintList(null);
                screenshot3.setBackground(new BitmapDrawable(BitmapFactory.decodeFile(picturePath)));
            }
        }
    }

    private String getPicturePath(Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.send_email_to_us: {
                String problemDescriptionText = problemDescription.getText().toString();
                if (problemDescriptionText != null && problemDescriptionText.length() > 0) {
                    final Intent ei = new Intent(Intent.ACTION_SEND_MULTIPLE);
                    ei.setType("plain/text");
                    ei.putExtra(Intent.EXTRA_EMAIL, new String[]{WebConstants.SUPPORT_EMAIL});
                    ei.putExtra(Intent.EXTRA_SUBJECT, "Feedback/Query for laudhoot.");
                    ei.putExtra(Intent.EXTRA_TEXT, getSupportMailBody(problemDescriptionText));
                    ei.setType("message/rfc822");
                    if (files != null && files.size() > 0) {
                        ArrayList<Uri> uris = new ArrayList<Uri>();
                        for(String filePath : files) {
                            if(filePath != null) {
                                File file = new File(filePath);
                                uris.add(Uri.fromFile(file));
                            }
                        }
                        ei.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
                    }
                    startActivityForResult(Intent.createChooser(ei, "Choose an email app..."), SEND_EMAIL_TO_SUPPORT);
                } else {
                    toaster.makeToast(getString(R.string.describe_problem));
                }
                break;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private String getSupportMailBody(String problemDescriptionText) {
        StringBuilder bodyBuilder = new StringBuilder();
        String newLine = System.getProperty("line.separator");
        bodyBuilder.append(problemDescriptionText).append(newLine).append(newLine)
                .append("KINDLY DO NOT EDIT THE INFORMATION GATHERED FOR USE BY SUPPORT CENTER.").append(newLine)
                .append("Debug Key : " + clientId).append(newLine)
                .append("Network Available : " + networkStateManager.isConnected()).append(newLine)
                .append("Network Type: " + networkStateManager.getNetworkType()).append(newLine)
                .append("GPS Available : " + locationStateManager.hasGPS()).append(newLine)
                .append("GPS Enabled : " + locationStateManager.isGPSEnabled()).append(newLine)
                .append("GPS N/W Enabled : " + locationStateManager.isNetworkEnabled()).append(newLine)
                .append("Location Latitude : " + latitude).append(newLine)
                .append("Location Longitude : " + longitude).append(newLine)
                .append("Resolved Location : " + geofenceCode).append(newLine)
                .append("Carrier : " + telephonyManager.getSimOperatorName()).append(newLine)
                .append("Manufacturer : " + Build.MANUFACTURER).append(newLine)
                .append("Model : " + Build.MODEL).append(newLine)
                .append("Instrument Identity : " + telephonyManager.getDeviceId()).append(newLine)
                .append("OS VERSION : " + Build.VERSION.CODENAME).append(newLine)
                .append("Viewed FAQ : " + viewedFAQ);
        return bodyBuilder.toString();
    }
}
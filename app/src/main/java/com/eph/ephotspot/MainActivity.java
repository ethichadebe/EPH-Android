package com.eph.ephotspot;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.limurse.iap.DataWrappers;
import com.limurse.iap.IapConnector;
import com.limurse.iap.PurchaseServiceListener;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private CardView cvTap;

    private BottomSheetBehavior behavior;
    private View bottomSheet;
    String[] passwords = {
            "EPN739582893", "EPN272955839", "EPN212469615", "EPN699157113", "EPN845312492", "EPN270634772", "EPN843521644", "EPN842792838",
            "EPN819672195", "EPN455520822", "EPN597315918", "EPN198316355", "EPN337365479", "EPN464147566", "EPN477967961", "EPN613878129",
            "EPN723139744", "EPN344018891", "EPN200014240", "EPN479766973", "EPN881645746", "EPN454412098", "EPN536750292", "EPN585862119",
            "EPN258896921", "EPN840523730", "EPN280789646", "EPN530165851", "EPN376480318", "EPN501204366", "EPN704014156", "EPN255311284",
            "EPN272398822", "EPN492622514", "EPN619897598", "EPN679948666", "EPN387746860", "EPN625671502", "EPN646865113", "EPN200054015",
            "EPN676098874", "EPN272795616", "EPN319139410", "EPN309329896", "EPN465283711", "EPN254053891", "EPN744573854", "EPN405370673",
            "EPN508480826", "EPN127123267", "EPN886106139", "EPN662564931", "EPN878238455", "EPN338738097", "EPN203619381", "EPN365098229",
            "EPN484322384", "EPN221531429", "EPN315392287", "EPN691911597", "EPN271908649", "EPN181253894", "EPN106346465", "EPN497612517",
            "EPN872339194", "EPN432903371", "EPN178363002", "EPN419619324", "EPN472978149", "EPN583423696", "EPN774574167", "EPN612461548",
            "EPN305315449", "EPN579467006", "EPN671871205", "EPN892423803", "EPN710801992", "EPN601326364", "EPN176222205", "EPN477756110",
            "EPN651981089", "EPN764201128", "EPN593712976", "EPN232396802", "EPN717860265", "EPN353803871", "EPN120997581", "EPN119894200",
            "EPN320511567", "EPN842117419", "EPN341589105", "EPN312169088", "EPN151964251", "EPN252405253", "EPN162502357", "EPN115197953"
    };

    private RelativeLayout rlR5, rlR10, rlR15, rlR20;

    TextView tvPassword, tvCopy, tvWhatsapp;
    private MutableLiveData<Boolean> isBillingClientConnected;
    private IapConnector iapConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rlR5 = findViewById(R.id.rlR5);
        rlR10 = findViewById(R.id.rlR10);
        rlR15 = findViewById(R.id.rlR15);
        rlR20 = findViewById(R.id.rlR20);
        tvPassword = findViewById(R.id.tvPassword);
        tvCopy = findViewById(R.id.tvCopy);
        tvWhatsapp = findViewById(R.id.tvWhatsapp);
        cvTap = findViewById(R.id.cvTap);

        bottomSheet = findViewById(R.id.nsvGoogleBilling);
        behavior = BottomSheetBehavior.from(bottomSheet);


        tvPassword.setText(getSavedPassword());

        tvWhatsapp.setMovementMethod(LinkMovementMethod.getInstance());

        tvCopy.setOnClickListener(v -> {
            if (!tvPassword.getText().toString().isEmpty()) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", tvPassword.getText().toString());
                Toast.makeText(MainActivity.this, "Password copied", Toast.LENGTH_SHORT).show();
                clipboard.setPrimaryClip(clip);
            }

        });

        cvTap.setOnClickListener(v -> {
            String strPassword = passwords[randomToken(passwords.length - 1)];
            tvPassword.setText(strPassword);

            tvCopy.setVisibility(View.VISIBLE);
            // Storing data into SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("EPNaledi", MODE_PRIVATE);
            // Creating an Editor object to edit(write to the file)
            SharedPreferences.Editor myEdit = sharedPreferences.edit();

            // Storing the key and its value as the data fetched from edittext
            myEdit.putString("password", strPassword);

            // Once the changes have been made, we need to commit to apply those changes made,
            // otherwise, it will throw an error
            myEdit.commit();

            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        });

        tvPassword.setOnClickListener(v -> {
            if (!tvPassword.getText().toString().isEmpty()) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", tvPassword.getText().toString());
                Toast.makeText(MainActivity.this, "Password copied", Toast.LENGTH_SHORT).show();
                clipboard.setPrimaryClip(clip);
            }
        });

        behavior.setHideable(true);

        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        isBillingClientConnected = new MutableLiveData<>();
        isBillingClientConnected.setValue(false);
        List<String> nonConsumableList = Collections.singletonList("Lifetime");
        List<String> consumableList = Arrays.asList("r5", "r10", "r15", "r20");
        List<String> subsList = Collections.singletonList("subscription");

        iapConnector = new IapConnector(this, nonConsumableList, consumableList, subsList,
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgLAMA9UFcUfb083QzTMBtLuL9zwYZTypVNZ6kv6WNGJhs0qeIQYzlFw5Tgf62SyAgZNFhXnmiUHMTMQNMU2a7DROsWJJEqkehqbFxrmYp4x4t4/yEFMjT193YR4mdJ0ncEp2cg+2YknrWl9ZoHwv5zPfYaU95Bv5Vlgu/7cmzUOi+er9HzsccUyKi3PDHJRLp6pQAbskPmrYCR+xutbmrmECoc5/T1JOrCkbRMtgSAop5wXqdP8N6gjkaELcOOd5eN5S5IS7Wt6FE1xq3EGJu22Ibo3Iwk1fexl7Gj+MqwRoNkR+oG2q/r5vHYqcmvWubXxpWMEbtwiTfFV0PIAb9QIDAQAB",
                true);

        iapConnector.addBillingClientConnectionListener((status, billingResponseCode) -> {
            Log.d(TAG, "Status: " + status + "\nResponse code: " + billingResponseCode);
            isBillingClientConnected.setValue(status);
        });

        iapConnector.addPurchaseListener(new PurchaseServiceListener() {
            @Override
            public void onPricesUpdated(@NonNull Map<String, ? extends List<DataWrappers.ProductDetails>> map) {

            }

            @Override
            public void onProductPurchased(@NonNull DataWrappers.PurchaseInfo purchaseInfo) {
                Toast.makeText(MainActivity.this, purchaseInfo.getSku(), Toast.LENGTH_SHORT).show();

                switch (purchaseInfo.getSku()) {
                    case "r5":
                        Toast.makeText(MainActivity.this, "Thank you for buying the R5 package", Toast.LENGTH_SHORT).show();
                        break;
                    case "r10":
                        Toast.makeText(MainActivity.this, "Thank you for buying the R10 package", Toast.LENGTH_SHORT).show();
                        break;
                    case "r15":
                        Toast.makeText(MainActivity.this, "Thank you for buying the R15 package", Toast.LENGTH_SHORT).show();
                        break;
                    case "r20":
                        Toast.makeText(MainActivity.this, "Thank you for buying the R20 package", Toast.LENGTH_SHORT).show();
                        break;
                }
                tvPassword.setText(passwords[randomToken(passwords.length - 1)]);
            }

            @Override
            public void onProductRestored(@NonNull DataWrappers.PurchaseInfo purchaseInfo) {

            }

            @Override
            public void onPurchaseFailed(@Nullable DataWrappers.PurchaseInfo purchaseInfo, @Nullable Integer integer) {

            }
        });


        rlR5.setOnClickListener(v -> {
            iapConnector.purchase(this, "r5", "", "");
            //behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        });

        rlR10.setOnClickListener(v -> {
            iapConnector.purchase(this, "r10", "", "");
            //behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        });

        rlR15.setOnClickListener(v -> {
            iapConnector.purchase(this, "r15", "", "");
            //behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        });

        rlR20.setOnClickListener(v -> {
            iapConnector.purchase(this, "r20", "", "");
            //behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        });
    }

    String getMacAddr() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        String macAddress = wInfo.getMacAddress();
        return "MAC Address : " + macAddress;
    }

    int randomToken(int limit) {
        // create instance of Random class
        Random rand = new Random();

        // Generate random integers in range 0 to 999
        return rand.nextInt(limit);
    }

    private String getSavedPassword() {
        // Retrieving the value using its keys the file name must be same in both saving and retrieving the data
        SharedPreferences sh = getSharedPreferences("EPNaledi", Context.MODE_PRIVATE);

        String strPassword = sh.getString("password", "");

        // The value will be default as empty string because for the very
        // first time when the app is opened, there is nothing to show

        if (!strPassword.isEmpty()) {
            tvCopy.setVisibility(View.VISIBLE);
        }

        return strPassword;
    }
}
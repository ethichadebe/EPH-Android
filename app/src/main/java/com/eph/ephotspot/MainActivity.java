package com.eph.ephotspot;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
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
import androidx.lifecycle.MutableLiveData;

import com.limurse.iap.DataWrappers;
import com.limurse.iap.IapConnector;
import com.limurse.iap.PurchaseServiceListener;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    String[] passwordsR5 = {
            "EPN739582893", "EPN272955839", "EPN212469615", "EPN699157113", "EPN845312492", "EPN270634772", "EPN843521644",
            "EPN842792838", "EPN819672195", "EPN455520822", "EPN597315918", "EPN198316355", "EPN337365479", "EPN464147566",
            "EPN477967961", "EPN613878129", "EPN723139744", "EPN344018891", "EPN200014240", "EPN479766973", "EPN881645746",
            "EPN454412098", "EPN536750292", "EPN585862119", "EPN258896921", "EPN840523730", "EPN280789646", "EPN530165851",
            "EPN376480318", "EPN501204366", "EPN704014156", "EPN255311284", "EPN272398822", "EPN492622514", "EPN619897598",
            "EPN679948666", "EPN387746860", "EPN625671502", "EPN646865113", "EPN200054015", "EPN676098874", "EPN272795616",
            "EPN319139410", "EPN309329896", "EPN465283711", "EPN254053891", "EPN744573854", "EPN405370673", "EPN508480826",
            "EPN127123267", "EPN886106139", "EPN662564931", "EPN878238455", "EPN338738097", "EPN203619381", "EPN365098229",
            "EPN484322384", "EPN221531429", "EPN315392287", "EPN691911597", "EPN271908649", "EPN181253894", "EPN106346465",
            "EPN497612517", "EPN872339194", "EPN432903371", "EPN178363002", "EPN419619324", "EPN472978149", "EPN583423696",
            "EPN774574167", "EPN612461548", "EPN305315449", "EPN579467006", "EPN671871205", "EPN892423803", "EPN710801992",
            "EPN601326364", "EPN176222205", "EPN477756110", "EPN651981089", "EPN764201128", "EPN593712976", "EPN232396802",
            "EPN717860265", "EPN353803871", "EPN120997581", "EPN119894200", "EPN320511567", "EPN842117419", "EPN341589105",
            "EPN312169088", "EPN151964251", "EPN252405253", "EPN162502357", "EPN115197953"
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

        //tvPassword.setText(getSavedPassword());

        tvWhatsapp.setMovementMethod(LinkMovementMethod.getInstance());

        tvCopy.setOnClickListener(v -> {
            if (!tvPassword.getText().toString().isEmpty()) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", tvPassword.getText().toString());
                Toast.makeText(MainActivity.this, "Password copied", Toast.LENGTH_SHORT).show();
                clipboard.setPrimaryClip(clip);
            }

        });

        tvPassword.setOnClickListener(v -> {
            if (!tvPassword.getText().toString().isEmpty()) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", tvPassword.getText().toString());
                Toast.makeText(MainActivity.this, "Password copied", Toast.LENGTH_SHORT).show();
                clipboard.setPrimaryClip(clip);
            }
        });

        isBillingClientConnected = new MutableLiveData<>();
        isBillingClientConnected.setValue(false);
        List<String> nonConsumableList = Collections.singletonList("Lifetime");
        List<String> consumableList = Arrays.asList("r5", "r5_0", "r5_1", "r5_2", "r5_3", "r5_4", "r5_5", "r5_6", "r5_7", "r5_8", "r5_9",
                "r5_10", "r5_11", "r5_12", "r5_13", "r5_14", "r5_15", "r5_16", "r5_17", "r5_18", "r5_19",
                "r10", "r10_0", "r10_1", "r10_2", "r10_3", "r10_4", "r10_5", "r10_6", "r10_7", "r10_8", "r10_9",
                "r10_10", "r10_11", "r10_12", "r10_13", "r10_14", "r10_15", "r10_16", "r10_17", "r10_18", "r10_19",
                "r15", "r15_0", "r15_1", "r15_2", "r15_3", "r15_4", "r15_5", "r15_6", "r15_7", "r15_8", "r15_9",
                "r15_10", "r15_11", "r15_12", "r15_13", "r15_14", "r15_15", "r15_16", "r15_17", "r15_18", "r15_19",
                "r20", "r20_0", "r20_1", "r20_2", "r20_3", "r20_4", "r20_5", "r20_6", "r20_7", "r20_8", "r20_9",
                "r20_10", "r20_11", "r20_12", "r20_13", "r20_14", "r20_15", "r20_16", "r20_17", "r20_18", "r20_19");
        List<String> subsList = Collections.singletonList("");

        iapConnector = new IapConnector(this, nonConsumableList, consumableList, subsList,
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgLAMA9UFcUfb083QzTMBtLuL9zwYZTypVNZ" +
                        "6kv6WNGJhs0qeIQYzlFw5Tgf62SyAgZNFhXnmiUHMTMQNMU2a7DROsWJJEqkehqbFxrmYp4x4t4" +
                        "/yEFMjT193YR4mdJ0ncEp2cg+2YknrWl9ZoHwv5zPfYaU95Bv5Vlgu/7cmzUOi+er9HzsccUyKi3" +
                        "PDHJRLp6pQAbskPmrYCR+xutbmrmECoc5/T1JOrCkbRMtgSAop5wXqdP8N6gjkaELcOOd5eN5S5IS" +
                        "7Wt6FE1xq3EGJu22Ibo3Iwk1fexl7Gj+MqwRoNkR+oG2q/r5vHYqcmvWubXxpWMEbtwiTfFV0PIAb9QIDAQAB",
                true);

        iapConnector.addBillingClientConnectionListener((status, billingResponseCode) -> {
            Log.d(TAG, "Status: " + status + "\nResponse code: " + billingResponseCode);
            isBillingClientConnected.setValue(status);
        });


        iapConnector.addPurchaseListener(new PurchaseServiceListener() {
            public void onPricesUpdated(@NotNull Map iapKeyPrices) {

            }

            public void onProductPurchased(@NonNull DataWrappers.PurchaseInfo purchaseInfo) {
                if (purchaseInfo.getSku().contains("r5")) {
                    savePassword("r5", passwordsR5[randomToken(passwordsR5.length - 1)]);
                    tvPassword.setText(getSavedPassword("r5"));
                    Toast.makeText(MainActivity.this, "30 minutes token bought success", Toast.LENGTH_SHORT).show();
                } else if (purchaseInfo.getSku().contains("r10")) {
                    savePassword("r5", passwordsR5[randomToken(passwordsR5.length - 1)]);
                    tvPassword.setText(getSavedPassword("r5"));
                    Toast.makeText(MainActivity.this, "30 minutes token bought success", Toast.LENGTH_SHORT).show();
                } else if (purchaseInfo.getSku().contains("r15")) {
                    savePassword("r5", passwordsR5[randomToken(passwordsR5.length - 1)]);
                    tvPassword.setText(getSavedPassword("r5"));
                    Toast.makeText(MainActivity.this, "30 minutes token bought success", Toast.LENGTH_SHORT).show();
                } else if (purchaseInfo.getSku().contains("r20")) {
                    savePassword("r5", passwordsR5[randomToken(passwordsR5.length - 1)]);
                    tvPassword.setText(getSavedPassword("r5"));
                    Toast.makeText(MainActivity.this, "30 minutes token bought success", Toast.LENGTH_SHORT).show();
                }
            }

            public void onProductRestored(@NonNull DataWrappers.PurchaseInfo purchaseInfo) {

            }

            @Override
            public void onPurchaseFailed(@Nullable DataWrappers.PurchaseInfo purchaseInfo, @Nullable Integer billingResponseCode) {
                //Toast.makeText(getApplicationContext(), "Your purchase has been failed", Toast.LENGTH_SHORT).show();
            }
        });

        rlR5.setOnClickListener(v -> {
            String ID = "r5_" + getSavedProductID("r5");
            iapConnector.purchase(this, ID, null, null);
        });

        rlR10.setOnClickListener(v -> {
            String ID = "r10_" + getSavedProductID("r10");
            iapConnector.purchase(this, ID, null, null);
        });

        rlR15.setOnClickListener(v -> {
            String ID = "r15_" + getSavedProductID("r15");
            iapConnector.purchase(this, ID, null, null);
        });

        rlR20.setOnClickListener(v -> {
            iapConnector.purchase(this, "r20", null, null);
        });
    }

    int randomToken(int limit) {
        // create instance of Random class
        Random rand = new Random();

        // Generate random integers in range 0 to 999
        return rand.nextInt(limit);
    }

    String getSavedPassword(String passwordType) {
        // Retrieving the value using its keys the file name must be same in both saving and retrieving the data
        SharedPreferences sh = getSharedPreferences("EPNaledi", Context.MODE_PRIVATE);

        String store = sh.getString(passwordType, "");

        // The value will be default as empty string because for the very
        // first time when the app is opened, there is nothing to show

        if (!store.isEmpty()) {
            tvCopy.setVisibility(View.VISIBLE);
        }

        return store;
    }

    private int getSavedProductID(String idType) {
        // Retrieving the value using its keys the file name must be same in both saving and retrieving the data
        SharedPreferences sh = getSharedPreferences("EPNaledi", Context.MODE_PRIVATE);

        int productID = sh.getInt(idType, 0) + 1;

        Toast.makeText(this, "Product ID: " + productID, Toast.LENGTH_SHORT).show();
        saveProductID(idType, productID);
        // The value will be default as empty string because for the very
        // first time when the app is opened, there is nothing to show

        return productID + 1;
    }

    void savePassword(String passwordType, String password) {
        // Storing data into SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("EPNaledi", MODE_PRIVATE);

        // Creating an Editor object to edit(write to the file)
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        // Storing the key and its value as the data fetched from edittext
        myEdit.putString(passwordType, password);

        // Once the changes have been made, we need to commit to apply those changes made,
        // otherwise, it will throw an error
        myEdit.apply();
    }

    private void saveProductID(String idType, int productID) {
        // Storing data into SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("EPNaledi", MODE_PRIVATE);

        // Creating an Editor object to edit(write to the file)
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        // Storing the key and its value as the data fetched from edittext
        myEdit.putInt(idType, productID);

        // Once the changes have been made, we need to commit to apply those changes made,
        // otherwise, it will throw an error
        myEdit.apply();
    }
}
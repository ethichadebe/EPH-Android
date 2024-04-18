package com.eph.ep_hotspot;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.MutableLiveData;

import com.limurse.iap.DataWrappers;
import com.limurse.iap.IapConnector;
import com.limurse.iap.PurchaseServiceListener;

import java.net.NetworkInterface;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private RelativeLayout rlR5, rlR10, rlR15, rlR20;
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

        isBillingClientConnected = new MutableLiveData<>();
        isBillingClientConnected.setValue(false);
        List<String> nonConsumableList = Collections.singletonList("Lifetime");
        List<String> consumableList = Arrays.asList("r5", "r10", "r15", "r20");
        List<String> subsList = Collections.singletonList("subscription");

        Toast.makeText(MainActivity.this, getMacAddr(), Toast.LENGTH_SHORT).show();
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
                if (purchaseInfo.getSku().equals("r5")) {
                    Toast.makeText(MainActivity.this, "Thank you for buying the R5 package\n" + getMacAddr(), Toast.LENGTH_SHORT).show();
                } else if (purchaseInfo.getSku().equals("r10")) {
                    Toast.makeText(MainActivity.this, "Thank you for buying the R10 package", Toast.LENGTH_SHORT).show();
                } else if (purchaseInfo.getSku().equals("r15")) {
                    Toast.makeText(MainActivity.this, "Thank you for buying the R15 package", Toast.LENGTH_SHORT).show();
                } else if (purchaseInfo.getSku().equals("r20")) {
                    Toast.makeText(MainActivity.this, "Thank you for buying the R20 package", Toast.LENGTH_SHORT).show();
                }
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
        });

        rlR10.setOnClickListener(v -> {
            iapConnector.purchase(this, "r10", "", "");
        });

        rlR15.setOnClickListener(v -> {
            iapConnector.purchase(this, "r15", "", "");
        });

        rlR20.setOnClickListener(v -> {
            iapConnector.purchase(this, "r20", "", "");
        });
    }

    private String getMacAddr() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        String macAddress = wInfo.getMacAddress();
        return "MAC Address : " + macAddress;
    }

}
package com.eph.ephotspot;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.ProductDetailsResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.collect.ImmutableList;

import java.util.List;

public class MainActivity extends AppCompatActivity {
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

    BillingClient billingClient;
    TextView tvPassword, tvCopy, tvWhatsapp;
    private MutableLiveData<Boolean> isBillingClientConnected;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout rlR5 = findViewById(R.id.rlR5);
        RelativeLayout rlR10 = findViewById(R.id.rlR10);
        RelativeLayout rlR15 = findViewById(R.id.rlR15);
        RelativeLayout rlR20 = findViewById(R.id.rlR20);

        tvPassword = findViewById(R.id.tvPassword);
        tvCopy = findViewById(R.id.tvCopy);
        tvWhatsapp = findViewById(R.id.tvWhatsapp);

        PurchasesUpdatedListener purchasesUpdatedListener = (billingResult, purchases) -> {
            // To be implemented in a later section.
            if ((billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) && (purchases != null)) {
                for (Purchase purchase : purchases) {
                    handlePurchase(purchase);
                }
            } else if ((billingResult.getResponseCode() == BillingClient.BillingResponseCode.BILLING_UNAVAILABLE) && (purchases != null)) {
                Toast.makeText(MainActivity.this, "BILLING_UNAVAILABLE", Toast.LENGTH_SHORT).show();
            } else if ((billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_NOT_OWNED) && (purchases != null)) {
                Toast.makeText(MainActivity.this, "ITEM_NOT_OWNED", Toast.LENGTH_SHORT).show();
            } else if ((billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) && (purchases != null)) {
                Toast.makeText(MainActivity.this, "USER_CANCELED", Toast.LENGTH_SHORT).show();
            } else if ((billingResult.getResponseCode() == BillingClient.BillingResponseCode.NETWORK_ERROR) && (purchases != null)) {
                Toast.makeText(MainActivity.this, "NETWORK_ERROR", Toast.LENGTH_SHORT).show();
            } else if ((billingResult.getResponseCode() == BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED) && (purchases != null)) {
                Toast.makeText(MainActivity.this, "FEATURE_NOT_SUPPORTED", Toast.LENGTH_SHORT).show();
            } else if ((billingResult.getResponseCode() == BillingClient.BillingResponseCode.SERVICE_DISCONNECTED) && (purchases != null)) {
                Toast.makeText(MainActivity.this, "SERVICE_DISCONNECTED", Toast.LENGTH_SHORT).show();
            } else if ((billingResult.getResponseCode() == BillingClient.BillingResponseCode.DEVELOPER_ERROR) && (purchases != null)) {
                Toast.makeText(MainActivity.this, "DEVELOPER_ERROR", Toast.LENGTH_SHORT).show();
            } else if ((billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) && (purchases != null)) {
                Toast.makeText(MainActivity.this, "ITEM_ALREADY_OWNED", Toast.LENGTH_SHORT).show();
            } else if ((billingResult.getResponseCode() == BillingClient.BillingResponseCode.ERROR) && (purchases != null)) {
                Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            } else if ((billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_UNAVAILABLE) && (purchases != null)) {
                Toast.makeText(MainActivity.this, "ITEM_UNAVAILABLE", Toast.LENGTH_SHORT).show();
            } else if ((billingResult.getResponseCode() == BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE) && (purchases != null)) {
                Toast.makeText(MainActivity.this, "SERVICE_UNAVAILABLE", Toast.LENGTH_SHORT).show();
            }
        };

        billingClient = BillingClient.newBuilder(this)
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases()
                // Configure other settings.
                .build();

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
        /*List<String> nonConsumableList = Collections.singletonList("Lifetime");
        List<String> consumableList = Arrays.asList("r5", "r5_0", "r5_1", "r5_2", "r5_3", "r5_4", "r5_5", "r5_6", "r5_7", "r5_8", "r5_9",
                "r5_10", "r5_11", "r5_12", "r5_13", "r5_14", "r5_15", "r5_16", "r5_17", "r5_18", "r5_19",
                "r10", "r10_0", "r10_1", "r10_2", "r10_3", "r10_4", "r10_5", "r10_6", "r10_7", "r10_8", "r10_9",
                "r10_10", "r10_11", "r10_12", "r10_13", "r10_14", "r10_15", "r10_16", "r10_17", "r10_18", "r10_19",
                "r15", "r15_0", "r15_1", "r15_2", "r15_3", "r15_4", "r15_5", "r15_6", "r15_7", "r15_8", "r15_9",
                "r15_10", "r15_11", "r15_12", "r15_13", "r15_14", "r15_15", "r15_16", "r15_17", "r15_18", "r15_19",
                "r20", "r20_0", "r20_1", "r20_2", "r20_3", "r20_4", "r20_5", "r20_6", "r20_7", "r20_8", "r20_9",
                "r20_10", "r20_11", "r20_12", "r20_13", "r20_14", "r20_15", "r20_16", "r20_17", "r20_18", "r20_19");
        List<String> subsList = Collections.singletonList("");*/

        rlR5.setOnClickListener(v -> buyHotspot("r5"));

        rlR10.setOnClickListener(v -> buyHotspot("r10"));

        rlR15.setOnClickListener(v -> buyHotspot("r15"));

        rlR20.setOnClickListener(v -> buyHotspot("r20"));
    }

    /*int randomToken(int limit) {
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
    }*/

    void handlePurchase(Purchase purchase) {
        ConsumeParams consumeParams =
                ConsumeParams.newBuilder()
                        .setPurchaseToken(purchase.getPurchaseToken())
                        .build();

        ConsumeResponseListener listener = (billingResult, purchaseToken) -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                // Handle the success of the consume operation.
            }
        };

        billingClient.consumeAsync(consumeParams, listener);

        //Verify
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            //Verify the signature for the purchase
            if (!verifyValidSignature(purchase.getOriginalJson(), purchase.getSignature())) {
                Toast.makeText(this, "Error: Invalid purchase", Toast.LENGTH_SHORT).show();
            }

            //Acknowledge the purchase if not Acknowledged
            if (!purchase.isAcknowledged()) {
                AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.getPurchaseToken())
                        .build();
                billingClient.acknowledgePurchase(acknowledgePurchaseParams, acknowledgePurchaseResponseListener);

                tvPassword.setText(passwordsR5[2]);
                Toast.makeText(this, "Purchased", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Already Purchased", Toast.LENGTH_SHORT).show();
            }
        } else if (purchase.getPurchaseState() == Purchase.PurchaseState.UNSPECIFIED_STATE) {
            Toast.makeText(this, "UNSPECIFIED_STATE", Toast.LENGTH_SHORT).show();
        } else if (purchase.getPurchaseState() == Purchase.PurchaseState.PENDING) {
            Toast.makeText(this, "PENDING", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean verifyValidSignature(String originalJson, String signature) {
        String base64Key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgLAMA9UFcUfb083QzTMBtLuL9zwYZTypVNZ" +
                "6kv6WNGJhs0qeIQYzlFw5Tgf62SyAgZNFhXnmiUHMTMQNMU2a7DROsWJJEqkehqbFxrmYp4x4t4" +
                "/yEFMjT193YR4mdJ0ncEp2cg+2YknrWl9ZoHwv5zPfYaU95Bv5Vlgu/7cmzUOi+er9HzsccUyKi3" +
                "PDHJRLp6pQAbskPmrYCR+xutbmrmECoc5/T1JOrCkbRMtgSAop5wXqdP8N6gjkaELcOOd5eN5S5IS" +
                "7Wt6FE1xq3EGJu22Ibo3Iwk1fexl7Gj+MqwRoNkR+oG2q/r5vHYqcmvWubXxpWMEbtwiTfFV0PIAb9QIDAQAB";

        return Verify.verifyPurchase(base64Key, originalJson, signature);
    }

    AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener = new AcknowledgePurchaseResponseListener() {
        @Override
        public void onAcknowledgePurchaseResponse(@NonNull BillingResult billingResult) {
            Toast.makeText(MainActivity.this, "Acknowledged", Toast.LENGTH_SHORT).show();
        }
    };

    /*private void saveProductID(String idType, int productID) {
        // Storing data into SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("EPNaledi", MODE_PRIVATE);

        // Creating an Editor object to edit(write to the file)
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        // Storing the key and its value as the data fetched from edittext
        myEdit.putInt(idType, productID);

        // Once the changes have been made, we need to commit to apply those changes made,
        // otherwise, it will throw an error
        myEdit.apply();
    }*/

    private void buyHotspot(String productId) {
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {

            }

            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    QueryProductDetailsParams queryProductDetailsParams =
                            QueryProductDetailsParams.newBuilder()
                                    .setProductList(
                                            ImmutableList.of(
                                                    QueryProductDetailsParams.Product.newBuilder()
                                                            .setProductId(productId)
                                                            .setProductType(BillingClient.ProductType.INAPP)
                                                            .build()))
                                    .build();
                    billingClient.queryProductDetailsAsync(
                            queryProductDetailsParams,
                            new ProductDetailsResponseListener() {
                                public void onProductDetailsResponse(@NonNull BillingResult billingResult,
                                                                     @NonNull List<ProductDetails> productDetailsList) {
                                    // check billingResult
                                    // process returned productDetailsList

                                    for (ProductDetails productDetails : productDetailsList) {
                                        ImmutableList<BillingFlowParams.ProductDetailsParams> productDetailsParamsList =
                                                ImmutableList.of(
                                                        BillingFlowParams.ProductDetailsParams.newBuilder()
                                                                // retrieve a value for "productDetails" by calling queryProductDetailsAsync()
                                                                .setProductDetails(productDetails)
                                                                // For one-time products, "setOfferToken" method shouldn't be called.
                                                                // For subscriptions, to get an offer token, call
                                                                // ProductDetails.subscriptionOfferDetails() for a list of offers
                                                                // that are available to the user.
                                                                .build()
                                                );

                                        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                                                .setProductDetailsParamsList(productDetailsParamsList)
                                                .build();
                                        // Launch the billing flow
                                        billingClient.launchBillingFlow(MainActivity.this, billingFlowParams);

                                    }
                                }
                            }
                    );
                }

            }
        });
    }
}
package com.eph.ephotspot;

import android.text.TextUtils;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class Verify {
    private static final String TAG = "Verify";

    private static final String KEY_FACTORY_ALGORITHM = "RSA";

    private static final String SIGNATURE_ALGORITHM = "SHA1withRSA";

    public static boolean   verifyPurchase(String base64PublicKey, String signedData, String signature) {
        if (TextUtils.isEmpty(signedData) || TextUtils.isEmpty(base64PublicKey) || TextUtils.isEmpty(signature)) {
            return false;
        }

        PublicKey publicKey = generatePublicKey(base64PublicKey);
        return verifyKey(publicKey, signedData, signature);
    }

    private static boolean verifyKey(PublicKey publicKey, String signedData, String signature) {
        byte[] signatureByte;
        signatureByte = Base64.getDecoder().decode(signature);

        try {
            Signature signatureAlgorithm = Signature.getInstance(SIGNATURE_ALGORITHM);
            signatureAlgorithm.initVerify(publicKey);
            signatureAlgorithm.update(signedData.getBytes());

            if (!signatureAlgorithm.verify(signatureByte)){
                return false;
            }
            return true;
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            return false;
        }
    }

    private static PublicKey generatePublicKey(String encodePublicKey) {
        try {
            byte[] decodeKey = Base64.getDecoder().decode(encodePublicKey);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_FACTORY_ALGORITHM);
            return keyFactory.generatePublic(new X509EncodedKeySpec(decodeKey));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

}

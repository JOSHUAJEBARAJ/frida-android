/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.pm.PackageManager
 *  android.hardware.fingerprint.FingerprintManager
 *  android.hardware.fingerprint.FingerprintManager$AuthenticationCallback
 *  android.hardware.fingerprint.FingerprintManager$AuthenticationResult
 *  android.hardware.fingerprint.FingerprintManager$CryptoObject
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.CancellationSignal
 *  android.os.Handler
 */
package android.support.v4.hardware.fingerprint;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RequiresPermission;
import java.security.Signature;
import javax.crypto.Cipher;
import javax.crypto.Mac;

public final class FingerprintManagerCompat {
    private final Context mContext;

    private FingerprintManagerCompat(Context context) {
        this.mContext = context;
    }

    @NonNull
    public static FingerprintManagerCompat from(@NonNull Context context) {
        return new FingerprintManagerCompat(context);
    }

    @Nullable
    @RequiresApi(value=23)
    private static FingerprintManager getFingerprintManagerOrNull(@NonNull Context context) {
        if (context.getPackageManager().hasSystemFeature("android.hardware.fingerprint")) {
            return (FingerprintManager)context.getSystemService(FingerprintManager.class);
        }
        return null;
    }

    @RequiresApi(value=23)
    static CryptoObject unwrapCryptoObject(FingerprintManager.CryptoObject cryptoObject) {
        if (cryptoObject == null) {
            return null;
        }
        if (cryptoObject.getCipher() != null) {
            return new CryptoObject(cryptoObject.getCipher());
        }
        if (cryptoObject.getSignature() != null) {
            return new CryptoObject(cryptoObject.getSignature());
        }
        if (cryptoObject.getMac() != null) {
            return new CryptoObject(cryptoObject.getMac());
        }
        return null;
    }

    @RequiresApi(value=23)
    private static FingerprintManager.AuthenticationCallback wrapCallback(final AuthenticationCallback authenticationCallback) {
        return new FingerprintManager.AuthenticationCallback(){

            public void onAuthenticationError(int n, CharSequence charSequence) {
                authenticationCallback.onAuthenticationError(n, charSequence);
            }

            public void onAuthenticationFailed() {
                authenticationCallback.onAuthenticationFailed();
            }

            public void onAuthenticationHelp(int n, CharSequence charSequence) {
                authenticationCallback.onAuthenticationHelp(n, charSequence);
            }

            public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult authenticationResult) {
                authenticationCallback.onAuthenticationSucceeded(new AuthenticationResult(FingerprintManagerCompat.unwrapCryptoObject(authenticationResult.getCryptoObject())));
            }
        };
    }

    @RequiresApi(value=23)
    private static FingerprintManager.CryptoObject wrapCryptoObject(CryptoObject cryptoObject) {
        if (cryptoObject == null) {
            return null;
        }
        if (cryptoObject.getCipher() != null) {
            return new FingerprintManager.CryptoObject(cryptoObject.getCipher());
        }
        if (cryptoObject.getSignature() != null) {
            return new FingerprintManager.CryptoObject(cryptoObject.getSignature());
        }
        if (cryptoObject.getMac() != null) {
            return new FingerprintManager.CryptoObject(cryptoObject.getMac());
        }
        return null;
    }

    @RequiresPermission(value="android.permission.USE_FINGERPRINT")
    public void authenticate(@Nullable CryptoObject cryptoObject, int n, @Nullable android.support.v4.os.CancellationSignal cancellationSignal, @NonNull AuthenticationCallback authenticationCallback, @Nullable Handler handler) {
        FingerprintManager fingerprintManager;
        if (Build.VERSION.SDK_INT >= 23 && (fingerprintManager = FingerprintManagerCompat.getFingerprintManagerOrNull(this.mContext)) != null) {
            cancellationSignal = cancellationSignal != null ? (CancellationSignal)cancellationSignal.getCancellationSignalObject() : null;
            fingerprintManager.authenticate(FingerprintManagerCompat.wrapCryptoObject(cryptoObject), (CancellationSignal)cancellationSignal, n, FingerprintManagerCompat.wrapCallback(authenticationCallback), handler);
        }
    }

    @RequiresPermission(value="android.permission.USE_FINGERPRINT")
    public boolean hasEnrolledFingerprints() {
        int n = Build.VERSION.SDK_INT;
        boolean bl = false;
        if (n >= 23) {
            FingerprintManager fingerprintManager = FingerprintManagerCompat.getFingerprintManagerOrNull(this.mContext);
            boolean bl2 = bl;
            if (fingerprintManager != null) {
                bl2 = bl;
                if (fingerprintManager.hasEnrolledFingerprints()) {
                    bl2 = true;
                }
            }
            return bl2;
        }
        return false;
    }

    @RequiresPermission(value="android.permission.USE_FINGERPRINT")
    public boolean isHardwareDetected() {
        int n = Build.VERSION.SDK_INT;
        boolean bl = false;
        if (n >= 23) {
            FingerprintManager fingerprintManager = FingerprintManagerCompat.getFingerprintManagerOrNull(this.mContext);
            boolean bl2 = bl;
            if (fingerprintManager != null) {
                bl2 = bl;
                if (fingerprintManager.isHardwareDetected()) {
                    bl2 = true;
                }
            }
            return bl2;
        }
        return false;
    }

    public static abstract class AuthenticationCallback {
        public void onAuthenticationError(int n, CharSequence charSequence) {
        }

        public void onAuthenticationFailed() {
        }

        public void onAuthenticationHelp(int n, CharSequence charSequence) {
        }

        public void onAuthenticationSucceeded(AuthenticationResult authenticationResult) {
        }
    }

    public static final class AuthenticationResult {
        private final CryptoObject mCryptoObject;

        public AuthenticationResult(CryptoObject cryptoObject) {
            this.mCryptoObject = cryptoObject;
        }

        public CryptoObject getCryptoObject() {
            return this.mCryptoObject;
        }
    }

    public static class CryptoObject {
        private final Cipher mCipher;
        private final Mac mMac;
        private final Signature mSignature;

        public CryptoObject(@NonNull Signature signature) {
            this.mSignature = signature;
            this.mCipher = null;
            this.mMac = null;
        }

        public CryptoObject(@NonNull Cipher cipher) {
            this.mCipher = cipher;
            this.mSignature = null;
            this.mMac = null;
        }

        public CryptoObject(@NonNull Mac mac) {
            this.mMac = mac;
            this.mCipher = null;
            this.mSignature = null;
        }

        @Nullable
        public Cipher getCipher() {
            return this.mCipher;
        }

        @Nullable
        public Mac getMac() {
            return this.mMac;
        }

        @Nullable
        public Signature getSignature() {
            return this.mSignature;
        }
    }

}

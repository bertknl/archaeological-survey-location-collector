package edu.upenn.sas.archaeologyapp.models;

import android.content.SharedPreferences;
import android.content.Context;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class SecretData {

    static private SharedPreferences getSharedPreferences(Context context) throws GeneralSecurityException, IOException {
        String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

        return EncryptedSharedPreferences.create(
                "encrypted_preferences", // fileName
                masterKeyAlias, // masterKeyAlias
                context, // context
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, // prefKeyEncryptionScheme
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM // prefvalueEncryptionScheme
        );

    }

    static public String getToken(Context context) throws GeneralSecurityException, IOException {
        return SecretData.getSharedPreferences(context).getString("token", "");
    }
    static public String getToken(SharedPreferences sharedPreferences) {
        return sharedPreferences.getString("token", "");
    }

    static public void setToken(String token, Context context) throws GeneralSecurityException, IOException {
         SecretData.getSharedPreferences(context).edit().putString("token", token).apply();
    }
    static public void setToken(String token, SharedPreferences sharedPreferences)   {
         sharedPreferences.edit().putString("token", token).apply();
    }
}

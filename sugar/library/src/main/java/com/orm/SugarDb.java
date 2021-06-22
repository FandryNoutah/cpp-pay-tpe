package com.orm;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import android.util.Base64;
import android.util.Log;

import com.orm.helper.ManifestHelper;
import com.orm.util.SugarCursorFactory;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import okio.ByteString;

import static com.orm.util.ContextUtil.getContext;
import static com.orm.helper.ManifestHelper.getDatabaseVersion;
import static com.orm.helper.ManifestHelper.getDbName;
import static com.orm.SugarContext.getDbConfiguration;

public class SugarDb extends SQLiteOpenHelper {
    private static final String LOG_TAG = "Sugar";

    private final SchemaGenerator schemaGenerator;
    private SQLiteDatabase sqLiteDatabase;
    private int openedConnections = 0;
    private static String[] mSecrets;


    String keys = new String(Base64.decode("N2RiYWE3MmQ0NWE2MzE3MWIwODgyOGUzYjQxMWFmNWRlM2FiMzU4ZWRhMzM5NjA4ZjgzOWQ1NTU1YTQ0ZTNlNA==",Base64.DEFAULT));

    //Prevent instantiation
    private SugarDb(String[]secrets) {
        super(getContext(), getDbName(), null, getDatabaseVersion());
        schemaGenerator = SchemaGenerator.getInstance();
        this.mSecrets = secrets;

    }

    public static SugarDb getInstance(String[] secrets) {
        return new SugarDb(secrets);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        schemaGenerator.createDatabase(sqLiteDatabase);
        final SugarDbConfiguration configuration = getDbConfiguration();

        if (null != configuration) {
            sqLiteDatabase.setLocale(configuration.getDatabaseLocale());
            sqLiteDatabase.setMaximumSize(configuration.getMaxSize());
            sqLiteDatabase.setPageSize(configuration.getPageSize());
        }
    }

    /*@Override
    public void onConfigure(SQLiteDatabase db) {
        final SugarDbConfiguration configuration = getDbConfiguration();

        if (null != configuration) {
            db.setLocale(configuration.getDatabaseLocale());
            db.setMaximumSize(configuration.getMaxSize());
            db.setPageSize(configuration.getPageSize());
        }

        super.onConfigure(db);
    }*/

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        schemaGenerator.doUpgrade(sqLiteDatabase, oldVersion, newVersion);
    }

    public synchronized SQLiteDatabase getDB() {
        if (this.sqLiteDatabase == null) {
            try{
                String key = new CryptLib().decryptCipherText(keys,mSecrets[0],mSecrets[1]);
                this.sqLiteDatabase = getWritableDatabase(key);
            }catch (Exception exception){

            }

        }

        return this.sqLiteDatabase;
    }

    //@Override
    public synchronized SQLiteDatabase getReadableDatabase() {
        if(ManifestHelper.isDebugEnabled()) {
            Log.d(LOG_TAG, "getReadableDatabase");
        }
        openedConnections++;
        try{
            String key = new CryptLib().decryptCipherText(keys,mSecrets[0],mSecrets[1]);
            return super.getReadableDatabase(key);
        }catch (Exception exception){
                return null;
        }

    }

    @Override
    public synchronized void close() {
        if(ManifestHelper.isDebugEnabled()) {
            Log.d(LOG_TAG, "getReadableDatabase");
        }
        openedConnections--;
        if(openedConnections == 0) {
            if(ManifestHelper.isDebugEnabled()) {
                Log.d(LOG_TAG, "closing");
            }
            super.close();
        }
    }



    private static class CryptLib {

        /**
         * Encryption mode enumeration
         */
        private enum EncryptMode {
            ENCRYPT, DECRYPT
        }

        // cipher to be used for encryption and decryption
        private Cipher _cx;

        // encryption key and initialization vector
        private byte[] _key, _iv;

        public CryptLib() throws NoSuchAlgorithmException, NoSuchPaddingException {
            // initialize the cipher with transformation AES/CBC/PKCS5Padding
            _cx = Cipher.getInstance("AES/CBC/PKCS5Padding");
            _key = new byte[32]; //256 bit key space
            _iv = new byte[16]; //128 bit IV
        }


        /**
         *
         * @param inputText Text to be encrypted or decrypted
         * @param encryptionKey Encryption key to used for encryption / decryption
         * @param mode specify the mode encryption / decryption
         * @param initVector Initialization vector
         * @return encrypted or decrypted bytes based on the mode
         * @throws UnsupportedEncodingException
         * @throws InvalidKeyException
         * @throws InvalidAlgorithmParameterException
         * @throws IllegalBlockSizeException
         * @throws BadPaddingException
         */
        private byte[] encryptDecrypt(String inputText, String encryptionKey,
                                      EncryptMode mode, String initVector) throws UnsupportedEncodingException,
                InvalidKeyException, InvalidAlgorithmParameterException,
                IllegalBlockSizeException, BadPaddingException {

            SecretKeySpec keySpec = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES"); // Create a new SecretKeySpec for the specified key data and algorithm name.

            IvParameterSpec ivSpec = new IvParameterSpec(initVector.getBytes("UTF-8")); // Create a new IvParameterSpec instance with the bytes from the specified buffer iv used as initialization vector.

            // encryption
            if (mode.equals(EncryptMode.ENCRYPT)) {
                // Potentially insecure random numbers on Android 4.3 and older. Read for more info.
                // https://android-developers.blogspot.com/2013/08/some-securerandom-thoughts.html
                _cx.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);// Initialize this cipher instance
                return _cx.doFinal(inputText.getBytes("UTF-8")); // Finish multi-part transformation (encryption)
            } else {
                _cx.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);// Initialize this cipher instance
                byte[] decodedValue = ByteString.decodeHex(inputText).toByteArray();
                return _cx.doFinal(decodedValue); // Finish multi-part transformation (decryption)
            }
        }

        /***
         * This function computes the SHA256 hash of input string
         * @param text input text whose SHA256 hash has to be computed
         * @param length length of the text to be returned
         * @return returns SHA256 hash of input text
         * @throws NoSuchAlgorithmException
         * @throws UnsupportedEncodingException
         */
        private static String SHA256 (String text, int length) throws NoSuchAlgorithmException, UnsupportedEncodingException {

            String resultString;
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            md.update(text.getBytes("UTF-8"));
            byte[] digest = md.digest();

            StringBuilder result = new StringBuilder();
            for (byte b : digest) {
                result.append(String.format("%02x", b)); //convert to hex
            }

            if(length > result.toString().length()) {
                resultString = result.toString();
            } else {
                resultString = result.toString().substring(0, length);
            }

            return resultString;

        }

        /**
         * Author Andrinarivo Rakotozafinirina
         * need this function as parse don't support specials characters in query
         * we need to encrypt it as hexa string
         * @param bytes
         * @return
         */
        public static String byte2hex(byte[] bytes) {
            StringBuilder result = new StringBuilder();
            for (byte aByte : bytes) {
                result.append(String.format("%02x", aByte));
            }
            return result.toString();
        }


        public String encryptPlainText(String plainText, String key, String iv) throws Exception {
            byte[] bytes = encryptDecrypt(plainText, key, EncryptMode.ENCRYPT, iv);
            return byte2hex(bytes);
        }

        public String decryptCipherText(String cipherText, String key, String iv) throws Exception {
            byte[] bytes = encryptDecrypt(cipherText, key, EncryptMode.DECRYPT, iv);
            return new String(bytes);
        }





        /**
         * Generate IV with 16 bytes
         * @return
         */
        public String generateRandomIV16() {
            SecureRandom ranGen = new SecureRandom();
            byte[] aesKey = new byte[16];
            ranGen.nextBytes(aesKey);
            StringBuilder result = new StringBuilder();
            for (byte b : aesKey) {
                result.append(String.format("%02x", b)); //convert to hex
            }
            if (16 > result.toString().length()) {
                return result.toString();
            } else {
                return result.toString().substring(0, 16);
            }
        }

    }

}

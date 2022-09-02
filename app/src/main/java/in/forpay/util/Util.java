package in.forpay.util;

import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Collection;
import java.util.Map;

import javax.crypto.Cipher;

public class Util {



    public static String sslDecryption(String encKey) throws Exception{

        String privateKeyString="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";

        privateKeyString="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";
        byte[] privateBytes = Base64.decode(privateKeyString,Base64.DEFAULT);
        PKCS8EncodedKeySpec privatekeySpec = new PKCS8EncodedKeySpec(privateBytes);
        KeyFactory privatekeyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = privatekeyFactory.generatePrivate(privatekeySpec);

        byte[] cipherTextArray=Base64.decode(encKey, Base64.NO_WRAP);
        //Get Cipher Instance RSA With ECB Mode and OAEPWITHSHA-512ANDMGF1PADDING Padding
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-512ANDMGF1PADDING");

        //Initialize Cipher for DECRYPT_MODE
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        //Perform Decryption
        byte[] decryptedTextArray = cipher.doFinal(cipherTextArray);

        return new String(decryptedTextArray);
    }

    public static  String encryptLongData(String enc,String encKey)
    {
        String encoded = "";
        byte[] encrypted = null;
        try {
            byte[] publicBytes = Base64.decode(encKey, Base64.DEFAULT);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey pubKey = keyFactory.generatePublic(keySpec);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING"); //or try with "RSA"
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            encrypted = cipher.doFinal(enc.getBytes());
            encoded = Base64.encodeToString(encrypted, Base64.DEFAULT);
        }
        catch (Exception e) {
            Log.d("LoginActivity", "error "+e.toString());
            e.printStackTrace();
        }
        return encoded;
    }


    public static String sslEncryptMain(String enc,String encKey) {
        String encryptedSecretKey="";

        //Log.d("LoginActivity", "enc received "+enc.toString());


        try{
            //Log.d("LoginActivity", "step 1 ");

            // Get an instance of the RSA key generator


            // Generate the KeyPair
            //KeyPair keyPair = keyPairGenerator.generateKeyPair();

            // Get the public and private key
            //PublicKey publicKey = keyPair.getPublic();
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(4096);
            byte[] publicBytes = Base64.decode(encKey,Base64.DEFAULT);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            try {
                PublicKey publicKey = keyFactory.generatePublic(keySpec);


                String key = encodeKey(publicKey.getEncoded());

                //Log.d("LoginActivity", "public key is "+key);

                // Encryption
                byte[] cipherTextArray = encrypt(enc, publicKey);
                encryptedSecretKey = Base64.encodeToString(cipherTextArray, Base64.NO_WRAP);
            }
            catch (Exception e){
                //Log.d("LoginActivity","Error "+e.toString());
            }

            //PrivateKey privateKey = keyPair.getPrivate();

/*
            byte[] privateBytes = Base64.decode(privateKeyString,Base64.DEFAULT);
            PKCS8EncodedKeySpec privatekeySpec = new PKCS8EncodedKeySpec(privateBytes);
            KeyFactory privatekeyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = privatekeyFactory.generatePrivate(privatekeySpec);

            String privateKey1 = encodeKey(privateKey.getEncoded());
            //Log.d("private key is ",privateKey1);



 */



            //String decryptedText = decrypt(encryptedSecretKey, privateKey);

            //Log.d("Encryption",decryptedText+" secret key "+encryptedSecretKey);

            /*
            String ALGORITHM = "RSA";
            PublicKey key = KeyFactory.getInstance(ALGORITHM)
                    .generatePublic(new X509EncodedKeySpec(Base64.decode(publicKeyString, Base64.DEFAULT)));

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] encryptedBytes = cipher.doFinal(inputData);
            encryptedSecretKey = new String(encryptedBytes, StandardCharsets.UTF_8);
            return encryptedSecretKey;
            */
            /*
            // 1. generate secret key using AES
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");

            keyGenerator.init(128); // AES is currently available in three key sizes: 128, 192 and 256 bits.The design and strength of all key lengths of the AES algorithm are sufficient to protect classified information up to the SECRET level
            SecretKey secretKey = keyGenerator.generateKey();

            /*

            // 2. get string which needs to be encrypted
            String text = enc;

            // 3. encrypt string using secret key
            byte[] raw = secretKey.getEncoded();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(new byte[16]));
            String cipherTextString = Base64.encodeToString(cipher.doFinal(text.getBytes(Charset.forName("UTF-8"))), Base64.DEFAULT);


            // 4. get public key
            X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(Base64.decode(publicKeyString, Base64.DEFAULT));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(publicSpec);


            // 6. encrypt secret key using public key
            Cipher cipher2 = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
            cipher2.init(Cipher.ENCRYPT_MODE, publicKey);
            encryptedSecretKey = Base64.encodeToString(cipher2.doFinal(secretKey.getEncoded()), Base64.DEFAULT);

            // 7. pass cipherTextString (encypted sensitive data) and encryptedSecretKey to your server via your preferred way.
            // Tips:
            // You may use JSON to combine both the strings under 1 object.
            // You may use a volley call to send this data to your server.

            //NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException | InvalidAlgorithmParameterException
         */
        }

        catch (Exception e) {
            Log.d("LoginActivity", "error 2 "+e.toString());
            e.printStackTrace();

        }
        //encryptedSecretKey="63304b9904f2b078379b7dee044ab0dcb928e287f2e1c08c73b63a06f24ee600b63d13f85273b9f63e12392b55737de87090f7e247221059abbadc1e6d37adb15fdef8ecfa52ca5024f02b744cb7d64edc5c35ccb06d19e0e307d86308fb1f671ace43cb3a171573177747a77c435d64567d1d2473b0e7c44607c9b62eb2f9ec370b608b60ff2081aaee9a733d331e7007338ee052a565bfa882e3ab1d6d184c41b0269f9bd7d976bd3daa784ca2b754798495be72696355146719c60886d19de631abb319be2903c3d98a6b5ba6641bf3d5f512f50b44d23f9f4c7e214106927567b265a596131f45e97ae8ff60344348db7e9a7249baaa3700b0f8d9e1cb2fc2ea1e8dd96cf111ac58567f59c266cb01450b0d0025dd2f6ce26d28669218476c6647b198b716addb90305e4118450da2e913eba62d44a66ce4f1b3dac50ed0bae5f79a7e23c0d514e969d097ba24d8a02ca173f3667ef4e89fa946c51904ca1a3bb676e128874566b836ae45602d79109e1c0f8249ce2a1222f6b8fa34ab28e5670f840250ae682b1cbb662ede2d8e53c4e0299a4ac9939cf93fa70b8476156b45fc87f79e35d35a897c6a25b2e4b4bf3bc70b20274428d85c9fd521298a15190725bc6822c3f1fcf1afae11ca9b37e2eeeba3b823a80288e6988f603037daa2dbe123b51775c229aa129f70bec148fdaaaa5557d3c1124c434692f6b926aa";
        return encryptedSecretKey;
    }


    private static String encodeKey(byte[] keyBytes) {
        return new String(Base64.encode(keyBytes, Base64.DEFAULT));
    }
    private static byte[] encrypt (String plainText,PublicKey publicKey ) throws Exception
    {
        //Get Cipher Instance RSA With ECB Mode and OAEPWITHSHA-512ANDMGF1PADDING Padding
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-512ANDMGF1PADDING");

        //Initialize Cipher for ENCRYPT_MODE
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        //Perform Encryption
        byte[] cipherText = cipher.doFinal(plainText.getBytes()) ;

        return cipherText;
    }

    private static String decrypt (String encKey, PrivateKey privateKey) throws Exception
    {
        byte[] cipherTextArray=Base64.decode(encKey, Base64.NO_WRAP);
        //Get Cipher Instance RSA With ECB Mode and OAEPWITHSHA-512ANDMGF1PADDING Padding
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-512ANDMGF1PADDING");

        //Initialize Cipher for DECRYPT_MODE
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        //Perform Decryption
        byte[] decryptedTextArray = cipher.doFinal(cipherTextArray);

        return new String(decryptedTextArray);
    }
    public static String encryptRequest(String requestReceived){
        String response=null;

        byte[] data = new byte[0];
        try {
            data = requestReceived.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response = Base64.encodeToString(data, Base64.DEFAULT);

        return response;
    }
    public static String decryptResponse(String responseReceived){
        String response=null;

        byte[] data = Base64.decode(responseReceived, Base64.DEFAULT);
        try {
            response = new String(data, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public static JSONObject mapToJson(Map<?, ?> data)
    {
        JSONObject object = new JSONObject();

        for (Map.Entry<?, ?> entry : data.entrySet())
        {
            /*
             * Deviate from the original by checking that keys are non-null and
             * of the proper type. (We still defer validating the values).
             */
            String key = (String) entry.getKey();
            if (key == null)
            {
                throw new NullPointerException("key == null");
            }
            try
            {
                object.put(key, wrap(entry.getValue()));
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        return object;
    }

    public static JSONArray collectionToJson(Collection data)
    {
        JSONArray jsonArray = new JSONArray();
        if (data != null)
        {
            for (Object aData : data)
            {
                jsonArray.put(wrap(aData));
            }
        }
        return jsonArray;
    }

    public static JSONArray arrayToJson(Object data) throws JSONException
    {
        if (!data.getClass().isArray())
        {
            throw new JSONException("Not a primitive data: " + data.getClass());
        }
        final int length = Array.getLength(data);
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < length; ++i)
        {
            jsonArray.put(wrap(Array.get(data, i)));
        }

        return jsonArray;
    }

    private static Object wrap(Object o)
    {
        if (o == null)
        {
            return null;
        }
        if (o instanceof JSONArray || o instanceof JSONObject)
        {
            return o;
        }
        try
        {
            if (o instanceof Collection)
            {
                return collectionToJson((Collection) o);
            }
            else if (o.getClass().isArray())
            {
                return arrayToJson(o);
            }
            if (o instanceof Map)
            {
                return mapToJson((Map) o);
            }
            if (o instanceof Boolean ||
                    o instanceof Byte ||
                    o instanceof Character ||
                    o instanceof Double ||
                    o instanceof Float ||
                    o instanceof Integer ||
                    o instanceof Long ||
                    o instanceof Short ||
                    o instanceof String)
            {
                return o;
            }
            if (o.getClass().getPackage().getName().startsWith("java."))
            {
                return o.toString();
            }
        }
        catch (Exception ignored)
        {
        }
        return null;
    }
}

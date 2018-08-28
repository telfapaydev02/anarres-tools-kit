package com.anarres.toolskit.support;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.x509.RSAPublicKeyStructure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;

/**
 * Created by ath on 2016/7/2.
 */
public class HashKit {
    public static final Logger logger = LoggerFactory.getLogger(HashKit.class);

    public static final String MD5 = "MD5";

    public static final String SHA_1 = "SHA-1";

    public static final String SHA_256 = "SHA-256";

    private static final SecureRandom random = new SecureRandom();

    public static byte[] genSalt(int len) {
        byte[] salt = new byte[len];
        random.nextBytes(salt);
        return salt;
    }

    /**
     *
     * @param src
     * @param keyname 例如 RSA
     * @param signname 例如 SHA1withRSA
     * @param pubKey
     * @param signature
     * @param charset
     * @return
     * @throws InvalidKeySpecException
     * @throws SignatureException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     */
    public static boolean verify(String src, String keyname, String signname, String pubKey, String signature, String charset) throws InvalidKeySpecException, SignatureException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        try {
            ASN1InputStream in = new ASN1InputStream(HexKit.decodeHex(pubKey.toCharArray()));
            RSAPublicKeyStructure pStruct = RSAPublicKeyStructure.getInstance(in.readObject());
            RSAPublicKeySpec spec = new RSAPublicKeySpec(pStruct.getModulus(), pStruct.getPublicExponent());
            KeyFactory kf = KeyFactory.getInstance(keyname);
            if (in != null) {
                in.close();
            }
            PublicKey pk = kf.generatePublic(spec);
            Signature signetcheck = Signature.getInstance(signname);
            signetcheck.initVerify(pk);
            byte[] by = src.getBytes(charset);
            signetcheck.update(by);
            return signetcheck.verify(HexKit.decodeHex(signature.toCharArray()));
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     *
     * @param src 源字符传
     * @param keyname 例如： RSA
     * @param signname 例如 SHA1withRSA
     * @param privateKey
     * @param charset
     * @return
     * @throws SignatureException
     * @throws DecoderException
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws InvalidKeySpecException
     */
    public static String signature(String src, String keyname, String signname, String privateKey, String charset) throws SignatureException, DecoderException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Hex.decodeHex(privateKey));
            KeyFactory keyf = KeyFactory.getInstance(keyname);
            PrivateKey myprikey = keyf.generatePrivate(priPKCS8);
            Signature signet = Signature.getInstance(signname);
            signet.initSign(myprikey);
            byte[] infoByte = src.getBytes(charset);
            signet.update(infoByte);
            byte[] signed = signet.sign();
            String signature = Hex.encodeHexString(signed);
            return signature;
        } catch (Exception e) {
            throw e;
        }
    }

    public static String base64(String str) {
        return Base64.getEncoder().encodeToString(str.getBytes());
    }

    public static String base64Decode(String str) {
        return new String(Base64.getDecoder().decode(str));
    }

    public static byte[] DesEncode(byte[] str, byte[] pwd) {
        try{
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(pwd);
            //创建一个密匙工厂，然后用它把DESKeySpec转换成
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            //Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance("DES");
            //用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
            //现在，获取数据并加密
            //正式执行加密操作
            return cipher.doFinal(str);
        }catch(Throwable e){
            e.printStackTrace();
        }
        return null;

    }

    public static String DesEncodeHex(String str, String pwd, String charset) throws UnsupportedEncodingException {
        return Hex.encodeHexString(DesEncode(str.getBytes(charset), pwd.getBytes(charset)));
    }

    public static byte[] DesDecode(byte[] src, byte[] password) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom random = new SecureRandom();
        // 创建一个DESKeySpec对象
        DESKeySpec desKey = new DESKeySpec(password);
        // 创建一个密匙工厂
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        // 将DESKeySpec对象转换成SecretKey对象
        SecretKey securekey = keyFactory.generateSecret(desKey);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance("DES");
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, random);
        // 真正开始解密操作
        return cipher.doFinal(src);
    }

    public static String MD5Encode(String origin) {
       return encode(MD5, origin);
    }

    public static String MD5Encode(String origin, String charset) {
        return encode(MD5, origin, charset);
    }

    public static String SHA1Encode(String origin) {
        return encode(SHA_1, origin);
    }

    public static String encode(String algorithmName, String origin) {
        String resultString = null;
        try {
            MessageDigest md = MessageDigest.getInstance(algorithmName);
            resultString = Hex.encodeHexString(md.digest(origin
                    .getBytes()));
        } catch (Exception ex) {
            logger.error(algorithmName + "失败", ex);
        }
        return resultString;
    }

    public static String encode(String algorithmName, String origin, String charset) {
        String resultString = null;
        try {
            MessageDigest md = MessageDigest.getInstance(algorithmName);
            resultString = Hex.encodeHexString(md.digest(origin
                    .getBytes(charset)));
        } catch (Exception ex) {
            logger.error(algorithmName + "失败", ex);
        }
        return resultString;
    }

    public static String SHA1EncodeHex(byte[] source, byte[] salt, int hashIterations) {
        try {
            return Hex.encodeHexString(encode(SHA_1, source, salt, hashIterations));
        } catch (NoSuchAlgorithmException e) {
            logger.error("", e);
            return "";
        }
    }

    public static String encodeHex(String algorithmName, byte[] source, byte[] salt, int hashIterations) {
        try {
            return Hex.encodeHexString(encode(algorithmName, source, salt, hashIterations));
        } catch (NoSuchAlgorithmException e) {
            logger.error("", e);
            return "";
        }
    }

    public static byte[] encode(String algorithmName, byte[] source, byte[] salt, int hashIterations) throws NoSuchAlgorithmException {

        MessageDigest digest = MessageDigest.getInstance(algorithmName);

        if (salt != null) {
            digest.reset();
            digest.update(salt);
        }
        byte[] hashed = digest.digest(source);
        int iterations = hashIterations - 1; //already hashed once above
        //iterate remaining number:
        for (int i = 0; i < iterations; i++) {
            digest.reset();
            hashed = digest.digest(hashed);
        }
        return hashed;

    }

    /**
     *
     * @param map
     * @param separator  参数之间的分隔符，如 k1=v1&k2=v2, k1=v1|k2=v2 的separator 分别为 & 和 |
     * @param eq     key和value之间的分隔符，如key=value, key:value的eq分别为 = 和 :
     * @param order 负数为从小到大， 正数为从大到小。 0 表示不进行任何排序。
     * @return
     */
    public static String signSrc(Map<String, ? extends Object> map, String separator, String eq, boolean useEmpty, int order) {
        if(map == null || map.size() < 1) {
            return "";
        }
        String[] keys = new String[map.size()];
        keys = map.keySet().toArray(keys);
        if(order != 0) {
            Arrays.sort(keys, (o1, o2) -> {
                if(order < 0) {
                    return o1.compareToIgnoreCase(o2);
                } else {
                    return o2.compareToIgnoreCase(o1);
                }
            });
        }
        String src = "";
        for(String key: keys) {
            if(!useEmpty && (null == map.get(key) || "".equals(map.get(key).toString().trim()))) {
                continue;
            }
            Object value = map.get(key) == null ? "" : map.get(key);
            src += key + eq + value.toString() + separator;
        }
        return src.substring(0, src.length() - separator.length());
    }
}

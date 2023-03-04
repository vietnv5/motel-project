/*
 * Created on Apr 12, 2016
 *
 * Copyright (C) 2016 by Viettel Network Company. All rights reserved
 */
package com.slook.util;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;


/**
 * PasswordEncoder.java
 *
 * @author Nguyen Hai Ha (hanh45@viettel.com.vn)
 * @version 1.0.0
 * @since Apr 12, 2016
 */
public class PasswordEncoder
{
    private static final Logger logger = LoggerFactory.getLogger(PasswordEncoder.class);
    private static final char[] PASSWORD = "d4ebaae0-e44f-11e2-a28f-0800200c9a66".toCharArray();
    private static final byte[] SALT = {(byte) 0xba, (byte) 0x96, (byte) 0x57, (byte) 0x14, (byte) 0xef, (byte) 0xd4, (byte) 0x20, (byte) 0xf1};

    public static String encrypt(String property)
    {
        String encrypt = "";
        try
        {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD));
            Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
            pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
            encrypt = base64Encode(pbeCipher.doFinal(property.getBytes("UTF-8")));
        }
        catch (GeneralSecurityException | UnsupportedEncodingException e)
        {
            logger.error("{}", e);
        }
        catch (Exception e)
        {
            logger.error("{}", e);
        }

        return encrypt;
    }

    public static String encryptUrlParam(String property)
    {
        String encrypt = "";
        try
        {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD));
            Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
            pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
            encrypt = base64Encode(pbeCipher.doFinal(property.getBytes("UTF-8")));
            encrypt = encrypt.replace('+', '-').replace('/', '_').replace("%", "%25").replace("\n", "%0A");
        }
        catch (GeneralSecurityException | UnsupportedEncodingException e)
        {
            logger.error("{}", e);
        }
        catch (Exception e)
        {
            logger.error("{}", e);
        }

        return encrypt;
    }

    private static String base64Encode(byte[] bytes)
    {
        byte[] encodedBytes = Base64.encodeBase64(bytes);
        return new String(encodedBytes);
    }

    public static String decrypt(String property)
    {
        String decrypt = property;
        try
        {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD));
            Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
            pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
            decrypt = new String(pbeCipher.doFinal(base64Decode(property)), "UTF-8");
        }
        catch (GeneralSecurityException | UnsupportedEncodingException e)
        {
            logger.error("{}", e);
        }
        catch (Exception e)
        {
            logger.error("{}", e);
        }

        return decrypt;
    }

    public static String decryptUrlParam(String property)
    {
        String decrypt = "";
        try
        {
            property = property.replace("%0A", "\n").replace("%25", "%").replace('_', '/').replace('-', '+');
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD));
            Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
            pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
            decrypt = new String(pbeCipher.doFinal(base64Decode(property)), "UTF-8");
        }
        catch (GeneralSecurityException | UnsupportedEncodingException e)
        {
            logger.error("{}", e);
        }
        catch (Exception e)
        {
            logger.error("{}", e);
        }

        return decrypt;
    }

    private static byte[] base64Decode(String property) throws IOException
    {
        byte[] decodedBytes = Base64.decodeBase64(property);
        return decodedBytes;
    }

    public static void main(String[] args)
    {
        System.out.println(decrypt("tsU/ /Dp+lUCGBYvIradkAi<"));
        System.out.println(decrypt(" +g=="));
        System.out.println(decrypt("oNH+ =="));
    }
}

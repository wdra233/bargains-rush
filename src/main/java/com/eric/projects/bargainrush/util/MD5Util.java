package com.eric.projects.bargainrush.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {

    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    private static String FIXED_SALT = "1a2b3c4d";

    public static String inputPassToFormPass(String inputPass) {
        String src = ""+FIXED_SALT.charAt(0)+FIXED_SALT.charAt(2) + inputPass +FIXED_SALT.charAt(5) + FIXED_SALT.charAt(4);
        return md5(src);
    }

    public static String formPassToDbPass(String formPass, String salt) {
        String src = ""+salt.charAt(0)+salt.charAt(2) + formPass +salt.charAt(5) + salt.charAt(4);
        return md5(src);
    }

    public static String inputPassToDbPass(String inputPass, String randSalt) {
        return formPassToDbPass(inputPassToFormPass(inputPass), randSalt);
    }
}

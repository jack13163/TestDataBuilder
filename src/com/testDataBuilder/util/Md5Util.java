package com.testDataBuilder.util;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.io.FileUtils;

public class Md5Util {

	public static byte[] md5(byte[] input){
		MessageDigest alg = null;
        try {
            alg = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {            
            e.printStackTrace();
        } // or "SHA-1"
		alg.update(input);
		byte[] digest = alg.digest();
		return digest;
	}

	public static String byte2hex(byte[] b) {
		StringBuffer buffer = new StringBuffer();
		String stmp;
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				buffer.append("0");
			}
			buffer.append(stmp);
		}
		return buffer.toString().toUpperCase();
	}


	public static String md5(String input) throws Exception {
		return byte2hex(md5(input.getBytes()));
	}
    
    public static void main(String[] args) throws IOException {
        byte[] bytes = FileUtils.readFileToByteArray(new File("D:\\TDBKeySource.license"));
        byte[] key = Md5Util.md5(bytes);
        String s = Md5Util.byte2hex(key);
        System.out.println(s);
    }
}

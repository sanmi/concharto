package com.tech4d.tsm.lab;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.junit.Test;

import com.tech4d.tsm.util.PasswordUtil;

public class Temp {

	@Test public void test() {
		for (int i=0; i<1000; i++) {
			String key = PasswordUtil.encrypt("sanmi" + Long.toString(System.currentTimeMillis() + i));
			String key_orig = key;
			try {
				key = URLEncoder.encode(key, "US-ASCII");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (key_orig.indexOf("+") != -1) {
				System.out.println(key);
			}
		}
	}
	
	@Test public void testURLEncoding() throws UnsupportedEncodingException {
		String tmp = URLEncoder.encode("Gaspar de Portolà", "UTF-8");
		System.out.println( tmp );
		System.out.println( URLDecoder.decode(tmp, "UTF-8" ));
		System.out.println( URLDecoder.decode("Gaspar+de+Portol%c3%a0", "UTF-8" ));
		
	}
}

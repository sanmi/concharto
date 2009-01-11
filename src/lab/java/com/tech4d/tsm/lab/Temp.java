/*******************************************************************************
 * ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 * 
 * The contents of this file are subject to the Mozilla Public License Version 
 * 1.1 (the "License"); you may not use this file except in compliance with 
 * the License. You may obtain a copy of the License at 
 * http://www.mozilla.org/MPL/
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 * 
 * The Original Code is Concharto.
 * 
 * The Initial Developer of the Original Code is
 * Time Space Map, LLC
 * Portions created by the Initial Developer are Copyright (C) 2007 - 2009
 * the Initial Developer. All Rights Reserved.
 * 
 * Contributor(s):
 * Time Space Map, LLC
 * 
 * ***** END LICENSE BLOCK *****
 ******************************************************************************/
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

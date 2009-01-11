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
package com.tech4d.tsm.lab.diff;

import java.util.List;

import org.junit.Test;

public class TestDiff2 {
	
	@Test 
	public void newDiff() {
		Object[] a = new Object[] {
		        "asdfsdf sdf sdf asdf asdf asdf asdfasd"
		    };

		Object[] b = new Object[] {
		        "asdfsdf sdf sdf asdf as asdf asdf---asd wer"
		    };

		    Diff diff = new Diff(a, b);
		    List<?> diffOut = diff.diff();
		    System.out.println(diff.diffs);
		    
		    StringDiff fileDiff = new StringDiff(
		    		"dfasfdasdfasdfasdf\nasdfasdfasdfasdf\nsdfgsdfg\n23434", 
		    		"dfas--sfdasdfasdfasdf\nasdfasdfasdfasdf\nsdfgsdfg23333\nsdfgsdfg23333\n23434" 
		    );
	}
}

/*******************************************************************************
 * Copyright 2009 Time Space Map, LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.tsm.concharto.lab.diff;

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

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

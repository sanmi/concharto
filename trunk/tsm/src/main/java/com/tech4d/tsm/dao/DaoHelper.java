package com.tech4d.tsm.dao;

import java.util.List;

public class DaoHelper {
	
	public static Object getOnlyFirst(List<?> obj) {
		if (obj.size() == 1) {
            return obj.get(0);
        } else {
            return null;
        }
	}
    

}

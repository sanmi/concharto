package com.tech4d.tsm.web.util;


public class PaginatingFormHelper {
    public static final String FORM_NEXT = "next";
    public static final String FORM_PREVIOUS = "previous";

    /**
     * If the user clicked next, previous or a page number, they want us to page
     * through the search results.  Otherwise we return 0.
     * @param paginatingForm search form
     * @param pageSize number of records in a page
     * @return firstRecord to start the search results
     */
    public static int calculateFirstRecord(PaginatingForm paginatingForm, int pageSize) {
        String pageCommand = paginatingForm.getPageCommand();
        Integer currRecord = paginatingForm.getCurrentRecord();
        //if this value is null, we assume it is uninitialized and should be zero because
        //the form object was just created
        if (currRecord == null) {
            currRecord = 0;
        }
        if (FORM_NEXT.equals(pageCommand)) {
            currRecord +=  pageSize;
        }
        if (FORM_PREVIOUS.equals(pageCommand)) {
            currRecord -=  pageSize;
            //Just in case someone is messing with us
            if (currRecord < 0) {
                currRecord = 0;
            }
        }
        return currRecord;
    }


}

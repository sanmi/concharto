package com.tech4d.tsm.web;

public class ChangeHistoryForm implements PaginatingForm {
    private String pageCommand;
    private Integer currentRecord;
    
    public Integer getCurrentRecord() {
        return currentRecord;
    }
    public void setCurrentRecord(Integer currentRecord) {
        this.currentRecord = currentRecord;
    }
    public String getPageCommand() {
        return pageCommand;
    }
    public void setPageCommand(String pageCommand) {
        this.pageCommand = pageCommand;
    }
     

}

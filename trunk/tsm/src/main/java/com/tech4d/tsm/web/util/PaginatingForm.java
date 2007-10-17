package com.tech4d.tsm.web.util;

public interface PaginatingForm {

    public Integer getCurrentRecord();

    public void setCurrentRecord(Integer currentRecord);

    public String getPageCommand();

    public void setPageCommand(String pageCommand);

}
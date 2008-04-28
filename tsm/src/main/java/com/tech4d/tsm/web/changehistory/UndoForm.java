package com.tech4d.tsm.web.changehistory;

public class UndoForm {
	private Long id;
	private Integer toRev;
	private String page;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getToRev() {
		return toRev;
	}
	public void setToRev(Integer toRev) {
		this.toRev = toRev;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
}

<%@ page language="java" contentType="text/plain" %>
<%@ page import="com.tech4d.tsm.util.JSONFormat, com.tech4d.tsm.model.Event, com.tech4d.tsm.web.eventsearch.EventSearchForm, java.util.*" %>
<%=  ((EventSearchForm)(request.getSession().getAttribute("eventSearchForm"))).getSearchResults() %>

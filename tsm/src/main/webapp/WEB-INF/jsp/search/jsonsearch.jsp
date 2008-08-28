<%@ page language="java" contentType="text/plain" %>
<%@ page import="com.tech4d.tsm.util.JSONFormat, com.tech4d.tsm.model.Event, java.util.*" %>
<%=  JSONFormat.toJSON((Collection<Event>)request.getAttribute("events")) %>

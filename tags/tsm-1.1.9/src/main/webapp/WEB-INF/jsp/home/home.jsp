<%--
Copyright 2009 Time Space Map, LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
--%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page import="java.util.Random" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>

<%
  //for defeating browser caches of iframes
  Random rand = new Random();
  request.setAttribute("rand", rand.nextInt());
 %>
<tsm:page title="Home">

  <jsp:attribute name="stylesheet">header.css,main.css</jsp:attribute>
  <jsp:attribute name="bodyattr">onload="init()" id="home"</jsp:attribute>
  <jsp:attribute name="nohead">true</jsp:attribute>
  <jsp:attribute name="nohomemenu">true</jsp:attribute>
  <jsp:attribute name="script">prototype.js,effects.js,dragdrop.js,resizable.js,livepipe.js,window.js,help.js</jsp:attribute>
  <jsp:attribute name="head">
    <script type="text/javascript">
  //<![CDATA[
  var COOKIE_SELECTED_TAB = 'selectedTab';
  var TAB_INDEX = 'index';
  var TAB_INFO = 'info';
  var CLASS_TAB = 'mainTabSelected';    
  function init() {
    setupHelpPanels();
  }
  
  function search() {
      document.getElementById("eventSearchForm").limitWithinMapBounds.value = 'false';
      document.event.submit();
  }
  
  function editEvent() {
    document.location='${basePath}edit/event.htm?add';
  }
   
  function selectTab(tab) {
    var tabName = 'tab'+tab;
    $$('.infopane').each(function(item) {
        $(item).style.display = 'none';
    });
    $$('.mainTab').each(function(item) {
    	Element.removeClassName(item, CLASS_TAB);
    });
    $(tab).style.display = 'inline';
    Element.toggleClassName(tabName, CLASS_TAB);
    setCookie(COOKIE_SELECTED_TAB, tab, 20);
  }

  function goToIndex() {
    document.location = 'index.htm';
  }
    
  //]]>
  </script>
  </jsp:attribute>  
  
  <jsp:body>
    <div id="heading">
      <img class="centerdiv" alt="title-home" src="images/${hostprefix}title-home.png" /> 
    </div>
    <form:form name="event" id="eventSearchForm" action="search/eventsearch.htm" commandName="eventSearch" onsubmit="search(); return false">
      <form:hidden path="boundingBoxSW" htmlEscape="true"/>
      <form:hidden path="boundingBoxNE" htmlEscape="true"/>
      <form:hidden path="mapCenter" htmlEscape="true"/>
      <form:hidden path="mapCenterOverride"/>
      <form:hidden path="searchResults" htmlEscape="true"/>
      <form:hidden path="mapZoom"/>
      <form:hidden path="zoomOverride"/>
      <form:hidden path="mapType"/>
      <form:hidden path="isGeocodeSuccess"/>
      <form:hidden path="isAddEvent"/>
      <form:hidden path="editEventId"/>
      <form:hidden path="displayEventId"/>
      <form:hidden path="linkHereEventId"/>
      <form:hidden path="embed"/>
      <c:if test="${isFirstView != null}">
        <input type="hidden" id="isFirstView" value="true"/>          
      </c:if>
      <div id="searchboxcontent" class="centerdiv">               
        <div id="tagline">
          An encyclopedic atlas of history and happenings that anyone can edit.<%-- TODO finish this <a href="#">anyone can edit.</a>--%></div>
        <div id="totals"> <a href='${basePath}search/eventsearch.htm?_what='><b>${totalEvents}</b> events and counting...</a> </div>
        <jsp:include page="../search/include/searchbar.jsp">
          <jsp:param name="showSearchOptions" value="false"/>
          <jsp:param name="showAdminBar" value="false"/>
        </jsp:include>
      </div>
    </form:form>
    <div id="main-pad"></div>
    <div id="main">
      <div id="left">          
        <div class="mainTabBar">
          <!--  special processing for the index page -->
          <c:choose>
            <c:when test="${empty isIndex}">
              <c:set var="selectedTab" value="${cookie.selectedTab.value}"/>
              <c:if test="${empty selectedTab}">
                <c:set var="selectedTab" value="info"/>
              </c:if>
            </c:when>
            <c:otherwise>
              <c:set var="selectedTab" value="index"/>
            </c:otherwise>
          </c:choose>
          <%-- Note: don't put a line break in the following lines - they need to be one line for proper html rendering --%>
          <span id="tabinfo" class="mainTab ${selectedTab == 'info' ? 'mainTabSelected' : ''}"><a href="#" onClick="selectTab('info'); return false;">Info</a></span>
          <span id="tablatest" class="mainTab ${selectedTab == 'latest' ? 'mainTabSelected' : ''}"><a href="#" onClick="selectTab('latest'); return false;">Latest</a></span>
          <span id="tabtags" class="mainTab ${selectedTab == 'tags' ? 'mainTabSelected' : ''}"><a href="#" onClick="selectTab('tags'); return false;">Tags</a></span>
          <%-- note: the extra &nbsp; is for safari display.  If you take it away, the background color on the tab top and bottom margins disappear --%>
          <span id="tabindex" class="mainTab ${selectedTab == 'index' ? 'mainTabSelected' : ''}"><a href="index.htm" onClick="goToIndex(); return false;">Index</a>&nbsp;</span>
        </div>
        <div id="mainpane">
          <jsp:include page="include/spotlight.jsp"/>
          <div class="recent" >
            <div class="infopane" id="info" style="display: ${selectedTab == 'info' ? 'inline' : 'none'}">
              <jsp:include page="include/info.jsp"/>
            </div>
            <div class="infopane" id="latest" style="display: ${selectedTab == 'latest' ? 'inline' : 'none'}">
              <jsp:include page="include/latest.jsp"/>
            </div>
            <div class="infopane" id="tags" style="display: ${selectedTab == 'tags' ? 'inline' : 'none'}">
              <jsp:include page="include/tags.jsp"/>
            </div>
            <div class="infopane" id="index" style="display: ${selectedTab == 'index' ? 'inline' : 'none'}">
              <jsp:include page="include/tagindex.jsp"/>
            </div>
          </div>
          <div class="clearfloat"></div>
        </div>      
      </div>
    </div>

    
  </jsp:body>
</tsm:page>


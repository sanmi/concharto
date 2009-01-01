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

  function init() {

	  //display the last tab that the user selected
    var selected = getCookie('selectedTab');
    if (selected != '') {
        selectTab(selected);
    } else {
        selectTab('info');
    }
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
    	Element.removeClassName(item, 'mainTabSelected');
    });
    $(tab).style.display = 'inline';
    Element.toggleClassName(tabName, 'mainTabSelected');
    setCookie('selectedTab', tab, 20);
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
        <jsp:include page="search/include/searchbar.jsp">
          <jsp:param name="showSearchOptions" value="false"/>
          <jsp:param name="showAdminBar" value="false"/>
        </jsp:include>
      </div>
    </form:form>
    <div id="main-pad"></div>
    <div id="main">
      <div id="left">          
        <div class="mainTabBar">
          <span id="tabinfo" class="mainTab ${cookie.selectedTab.value == 'info' ? 'mainTabSelected' : ''}"><a href="#" onClick="selectTab('info'); return false;">Info</a></span>
          <span id="tablatest" class="mainTab ${cookie.selectedTab.value == 'latest' ? 'mainTabSelected' : ''}"><a href="#" onClick="selectTab('latest'); return false;">Latest</a></span>
          <span id="tabtags" class="mainTab ${cookie.selectedTab.value == 'tags' ? 'mainTabSelected' : ''}"><a href="#" onClick="selectTab('tags'); return false;">Tags</a></span>
          <span id="tabindex" class="mainTab ${cookie.selectedTab.value == 'index' ? 'mainTabSelected' : ''}"><a href="#" onClick="selectTab('index'); return false;">Index</a></span>
        </div>
        <div id="mainpane">
        <jsp:include page="include/mainspotlight.jsp"/>
          <div class="recent" >
            <div class="infopane" id="info" style="display: ${cookie.selectedTab.value == 'info' ? 'inline' : 'none'}">
              <jsp:include page="include/maininfo.jsp"/>
            </div>
            <div class="infopane" id="latest" style="display: ${cookie.selectedTab.value == 'latest' ? 'inline' : 'none'}">
              <jsp:include page="include/mainlatest.jsp"/>
            </div>
            <div class="infopane" id="tags" style="display: ${cookie.selectedTab.value == 'tags' ? 'inline' : 'none'}">
              <jsp:include page="include/maintags.jsp"/>
            </div>
            <div class="infopane" id="index" style="display: ${cookie.selectedTab.value == 'index' ? 'inline' : 'none'}">
              <jsp:include page="include/mainindex.jsp"/>
            </div>
          </div>
          <div class="clearfloat"/>
        </div>      
      </div>
    </div>

    
  </jsp:body>
</tsm:page>


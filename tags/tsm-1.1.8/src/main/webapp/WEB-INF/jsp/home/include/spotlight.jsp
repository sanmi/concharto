<%-- 
 ***** BEGIN LICENSE BLOCK *****
 Version: MPL 1.1
 
 The contents of this file are subject to the Mozilla Public License Version 
 1.1 (the "License"); you may not use this file except in compliance with 
 the License. You may obtain a copy of the License at 
 http://www.mozilla.org/MPL/
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the
 License.
 
 The Original Code is Concharto.
 
 The Initial Developer of the Original Code is
 Time Space Map, LLC
 Portions created by the Initial Developer are Copyright (C) 2007 - 2009
 the Initial Developer. All Rights Reserved.
 
 Contributor(s):
 Time Space Map, LLC
 
 ***** END LICENSE BLOCK *****
--%>
<div id="right">
  <div style="background-color: white; padding: 0 5px 0 5px;">
    <!-- 
    this form is a hack to defeat the browser cache on home.htm.  We need the cache for 
    properly handling back buttons to the home page (otherwise the spotlight iframe won't match the page
    content), but we want to be able to click next and see a new spotlight.  This problem
    is most obvious on MacOS Safari, and intermittently so on IE 6, Windows
    --> 
    <form name="nextForm" id="nextForm" method="post" action="${basePath}home.htm">
      <input type="hidden" name="tagindex" value="0"/>
    </form>
  
    <div id="spotlightbox">
      <div class="next"><a href="#" onclick="document.nextForm.submit(); return false;">next</a></div>
      <p>${spotlightLabel}</p>
      <div class="clearfloat"></div>
      <small class="spotlighthelp">Click on red icons, lines or areas</small>
      <small>View a <a href='<c:out value="${spotlightLink}"/>'>larger map</a></small>
      <div class="clearfloat"></div>
      <div id="borderbox">
        <iframe id="embeddedmap" 
           &nc means don't count this as a page hit in google analytics 
               &r is to defeat certain types of browser iframe page caching 
               &h is to indicate to the embedded page that this is from the home page 
          src='${spotlightEmbedLink}&amp;r=${rand}&amp;h=1&amp;nc'
          height="330" width="450" frameborder="0" scrolling="no">
          This browser doesn't support embedding a map.
        </iframe>
       </div>
    </div>
    <div style="height: 15px;"></div>
  </div>
</div>

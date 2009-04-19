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
<div id="help_where">
	<div class="hd">
		<jsp:include page="helpclose.jsp"/>
		Where
	</div>
	<div class="bd">
		Enter a country, city, state, address or latitude/longitude coordinates.  
		Be specific - when there are multiple matches (e.g. Lancaster) we simply show the first one. 
		Some examples are:
		<ul>
      <li>Paris, France</li>
      <li>1060 West Addison, Chicago, IL 60613</li>
      <li>-10.893611, -77.520278</li>
      <li>42? 56? 19? N, 78? 52? 25? W</li>
    </ul>
  </div>
</div>

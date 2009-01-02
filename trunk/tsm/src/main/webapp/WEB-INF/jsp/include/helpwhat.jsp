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
<div id="help_what">
	<div class="hd">
		<jsp:include page="helpclose.jsp"/>
		Search Terms
	</div>
	<div class="bd">
    Some examples of valid search terms:
    <ul>
      <li>Exact match: <em><b>"</b>Battle of Gettysburg<b>"</b></em></li>
      <li>Must have all words: <em>+Battle +Gettysburg</em></li>
      <li>Must have 'Battle' but not 'Gettysburg':<em> +Battle -Gettysburg</em></li>
      <li>Any word beginning with 'Battle':<em> Battle*</em></li>
    </ul>
  </div>
</div>

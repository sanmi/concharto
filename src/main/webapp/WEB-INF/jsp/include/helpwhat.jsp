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

<%-- help text --%>
<jsp:include page="helpwhen.jsp"/>

<div id="help_what" class="helpbox">
	<div class="hd">
		<jsp:include page="helpclose.jsp"/>
		Search Terms
	</div>
	<div class="bd">
    Some examples of valid search terms:
    <ul>
      <li>Exact match: <em><b>"</b>Battle of Gettysburg<b>"</b></em></li>
      <li>Must have all words: <em>+Battle +Gettysburg</em></li>
      <li>Exclude 'Gettysburg':<em> +Battle -Gettysburg</em></li>
    </ul>
  </div>
</div>
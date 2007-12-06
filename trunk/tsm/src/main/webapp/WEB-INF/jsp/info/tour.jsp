<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>


<tsm:page title="Tour">
	<jsp:attribute name="stylesheet">textcontent.css,header.css</jsp:attribute>

	<jsp:body>
  	<div id="tourpage" class="textcontent" >
  		<br/>
			<p>				
				Time Space Map
				is an online encyclopedic atlas of history and happenings that anyone can edit.  It is a 
				<em>geographic wiki</em> that gives you simple and elegant answers to hard questions like 
				"What interesting things happened near where I am driving? What happened near our hotel 
				in Paris? What is happening near my town and surrounding areas? What was New York City 
				like from 1858 to 1869?"
			</p>
			<ul>
			  <li><a class="links" href="#search">Search</a></li>
			  <li><a class="links" href="#add">Add or Edit</a></li>
			  <li><a class="links" href="#changes">View or Undo Changes</a></li>
			  <li><a class="links" href="#discuss">Discuss</a></li>
			  <li><a class="links" href="#flag">Flag for removal</a></li>
			</ul>
			
			<a name="search"></a>
			<h2>Search</h2>
			<p>
				Enter a place, a time and a text filter and click "search" or 
				if you want to find out what happened on the map you are viewing, just hit search without 
				filling any thing in.  All fields are optional.
			</p>
			<img src="${basePath}images/info/search.png">
			
			<a name="add"></a>
			<h2>Add or Edit</h2>			
			<p>
				It's all about <b>events</b>!  Anyone can add an event and anyone can edit an event. 
			</p>
			<img src="${basePath}images/info/add.png">

			<a name="changes"></a>
			<h2>View or Undo Changes</h2>
			<p>
				It isn't a wiki if you can't view or undo changes.  You can see both text and 
				geographical changes and easily revert back to an older version.  All changes are tracked! 				
			</p>
			<img src="${basePath}images/info/changes.png">
			
			<a name="discuss"></a>
			<h2>Discuss</h2>
			<p>
			Discussion is the lifeblood of a good wiki.  Did Napoleon really win the battle of Borodino 1812?
			</p>
			<p>
			<em>This feature is coming soon!</em>			  
			</p>			

			<a name="flag"></a>
			<h2>Flag for removal</h2>
			<p>
				Some things just don't belong on the Atlas.  Anyone can flag an event for removal and it
				will be reviewed and removed if necessary.  Of course it is always better to correct
				the event if possible, but there are good reasons for removal.  For example:
			</p>
			<ul>
				<li>It is a duplicate of something someone else entered</li>
				<li>It is not encyclopedic (see our <a href="#">policies</a>)</li>
				<li>It is totally fake</li>
			</ul>
			<img src="${basePath}images/info/flag.png">
			
		</div>	  
	</jsp:body>
</tsm:page>


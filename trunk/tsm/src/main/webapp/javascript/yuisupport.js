  function setHelpPanel(panelContent, linkContext, width /* optional */) {
		var panel;
		if (width==null || width=='null' || width=='') { width = 250};
		
		panel = new YAHOO.widget.Panel(panelContent, { 
			width: width + "px", 
			visible:false, 
			context:[linkContext,"tl","br"],
			effect:{effect:YAHOO.widget.ContainerEffect.FADE,duration:0.25},
			constraintoviewport:true 
		});
		panel.render(document.body);
		YAHOO.util.Event.addListener(linkContext, "click", panel.show, panel, true);
		return panel;
	}

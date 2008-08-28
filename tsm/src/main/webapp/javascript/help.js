  function setupHelpPanels() {
    $$('.link_help').each(function(link){
        var modal = new Control.Modal(link, {
				className: 'helpbox',
        overlayOpacity: 0.7,
        position: 'relative', 
        offsetLeft: 20, 
        offsetTop: 20
        });
    });
	}

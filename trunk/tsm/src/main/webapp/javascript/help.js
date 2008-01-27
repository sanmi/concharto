  function setupHelpPanels() {
    document.getElementsByClassName('link_help').each(function(link){
        var modal = new Control.Modal(link, {
				containerClassName: 'helpbox',
        opacity: 0.2,
        position: 'relative', 
        offsetLeft: 20, 
        offsetTop: 20
        });
    });
	}

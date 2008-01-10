  function setupHelpPanels() {
    document.getElementsByClassName('link_help').each(function(link){
        new Control.Modal(link, {
				containerClassName: 'helpbox',
        opacity: 0.2,
        position: 'relative', 
        offsetLeft: 20, 
        offsetTop: 20
        });
    });
	}

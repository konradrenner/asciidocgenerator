"use strict";

var PDF = {
	addLinkToPdfIcon : function (){
		var pdfIcon = document.getElementById('pdf');
		var infoIcon = document.getElementById('detailsIcon');
		//TODO navigationkey aus seite nehmen und falls vorhanden pdf generierung anstossen, falls nicht vorhanden, einen alert ausgeben
		var navigationKey = document.getElementById('articleNavigationKey');
		if(navigationKey != null) {
			pdfIcon.setAttribute('href', pdfIcon.getAttribute('href') + navigationKey.value);
			pdfIcon.parentNode.classList.remove('iconhidden');
			infoIcon.parentNode.classList.remove('iconhidden')
		}
		
	}
}



PDF.addLinkToPdfIcon();
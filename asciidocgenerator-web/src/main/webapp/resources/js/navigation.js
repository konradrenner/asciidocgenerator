"use strict";

var SideNavigation =
  {
    toggleMenu : function(toggler) {
	    var descriptor = toggler.parentNode;
	    var menuItem = descriptor.parentNode;
	    menuItem.classList.add('activeMenuElement');
	    var toggled = toggler.classList.contains('toggled');
	    var list = document.querySelector("li.activeMenuElement > ul");
	    if (toggled) {
		    list.classList.add('hiddenElement');
		    toggler.classList.remove('toggled');
		    toggler.classList.add('nottoggled');
	    } else {
		    list.classList.remove('hiddenElement');
		    toggler.classList.remove('nottoggled');
		    toggler.classList.add('toggled');
	    }
	    menuItem.classList.remove('activeMenuElement');
	    return toggled;
    },
    /*
    changeIcon : function(toggler, iconA, iconB, toggled) {
	    toggler.firstChild.setAttribute('src', (toggled) ? iconA : iconB);
    },*/
    handleClick : function(toggler, iconA, iconB) {
	    this.toggleMenu(toggler);
	    //var toggled = toggler.classList.contains('toggled');
	    //this.changeIcon(toggler, iconA, iconB, toggled);
    },
            expandSideNavigation: function () {
                var fullPath = window.location.pathname;
                var contextRoot = BaseContext.get();
                var slices = 4;
                if (contextRoot.length === 0) {
                    slices--;
                }
                var path = fullPath.split("/").slice(slices).join("/");
                var toggler = document.querySelectorAll(" .nottoggled");
                var lastElement = toggler[0];
                if (path == '' || toggler.length == 0) {
                    return;
                }
                for (var i = 0; i < toggler.length; i++) {
                    var menuItemOfToggler = toggler[i].parentNode.parentNode;
                    var id = menuItemOfToggler.id.split("/").slice(1).join("/").toLowerCase();
                    path = path.toLowerCase();
                    path = decodeURI(path);
                    if (path.indexOf(id) !== -1) {
                        toggler[i].click();
                        lastElement = toggler[i];
                    }
                }
                this.selectLinkByHref(lastElement);
    },
    selectLinkByHref : function(lastElement) {
	    var links = lastElement.parentNode.parentNode.parentNode.parentNode.getElementsByTagName('A');
	    var linkWithoutSuffix = Functions.removeFileSuffixFromPath(window.location.pathname);
	    linkWithoutSuffix = decodeURI(linkWithoutSuffix);
	    for (var i = 0; i < links.length; i++) {
		    if (decodeURI(links[i].getAttribute('href')) == linkWithoutSuffix) {
			    links[i].setAttribute('style', 'background-color: #222');
			    return;
		    }
	    }
    },

    unfoldAll : function(toggler) {
	    toggler.setAttribute('onClick', 'SideNavigation.foldAll(this)')
	    toggler.classList.remove('allSelectorNottoggled');
	    toggler.classList.add('allSelectorToggled');
	    var notToggledElements = document.querySelectorAll('.hoverable.nottoggled');
	    for (var i = 0; i < notToggledElements.length; i++) {
		    SideNavigation.handleClick(
		      notToggledElements[i]);
	    }
    },

    foldAll : function(toggler) {
	    toggler.setAttribute('onClick', 'SideNavigation.unfoldAll(this)')
	    toggler.classList.remove('allSelectorToggled');
	    toggler.classList.add('allSelectorNottoggled');
	    var notToggledElements = document.querySelectorAll('.hoverable.toggled');
	    for (var i = 0; i < notToggledElements.length; i++) {
		    SideNavigation.handleClick(
		      notToggledElements[i]);
	    }
    }
  };

var Functions = {
	removeFileSuffixFromPath : function(path) {
		var pathFragments = path.split("/");
		var len = pathFragments.length;
		var lastPathFragment = pathFragments.slice(len - 1).join();
		var indexOfSuffixStart = lastPathFragment.indexOf(".");
		if (indexOfSuffixStart == -1) {
			return path;
		}
		return pathFragments.slice(0, len - 1).join("/");
	}
}

var MainNavigation = {
  markSelectedMainNavigation : function() {
	  var path = window.location.pathname.split("/").slice(3, 4).join();
	  var mainMenuItem = document.getElementById(path);
	  if (mainMenuItem !== null)
		  mainMenuItem.classList.add('selectedMenuItem');
  },

  decodeGroupName : function(toggler, content) {
	  toggler.innerHTML = decodeURI(content);
  }
}

var NavigationGroup = {
  open : function() {
	  document.getElementById("groupDropdown").classList.toggle("show");
  },

  filterFunction : function() {
	  var input, filter, ul, li, a, i;
	  input = document.getElementById("groupSearch");
	  filter = input.value.toUpperCase();
	  div = document.getElementById("groupDropdown");
	  a = div.getElementsByTagName("a");
	  for (i = 0; i < a.length; i++) {
		  if (a[i].innerHTML.toUpperCase().indexOf(filter) > -1) {
			  a[i].style.display = "";
		  } else {
			  a[i].style.display = "none";
		  }
	  }
  }
}

var Category = {
  select : function(toggler, categoryName) {
	  var categoriesList = toggler.parentNode.parentNode.children;
	  for (var i = 0; i < categoriesList.length; i++) {
		  var child = categoriesList[i].firstElementChild;
		  if (child.classList.contains('categorieToggled')) {
			  child.classList.remove('categorieToggled');
		  }
	  }

	  toggler.classList.add('categorieToggled');
	  $.get(BaseContext.get()+"/category?categoriename=" + categoryName, {}, function(result) {
		  $("#categoriesContent").html(result);
		  document.getElementById('document').id = '';
		  document.getElementById('articleList').id = '';
		  document.getElementById('articleListHeader').id = '';

	  });
  },

  selectCategorie : function() {
	  var path = window.location.pathname;
	  if ((path.indexOf("/categories/") == -1) || (path.indexOf("/list/") == -1)) {
		  return;
	  }
	  var categorie = path.substring(path.lastIndexOf("/") + 1);
	  var categories = document.getElementsByTagName("a");
	  for (var i = 0; i < categories.length; i++) {
		  if (categories[i].textContent == categorie) {
			  categories[i].click();
			  return;
		  }
	  }

  }
}

var Info = {
  showModal : function() {
	  var modal = document.getElementById('infoModal');
	  modal.style.display = "block";
  },

  hideModal : function() {
	  var modal = document.getElementById('infoModal');
	  modal.style.display = "none";
  },

  clickInModal : function(event) {
	  var modal = document.getElementById('infoModal');
	  if (event.target == modal) {
		  modal.style.display = "none";
	  }
  }
};

window.onload = function() {
	SideNavigation.expandSideNavigation();
	MainNavigation.markSelectedMainNavigation();
	Category.selectCategorie();
}

var DropDown = {
	get:function(){
		return document.getElementById('dropdown');
	},
	toggle: function(event) {
		var dropdown = DropDown.get();		
		var disp = 'inline-block';
		if (dropdown.style.display === disp) {
			disp = 'none';
		}
		dropdown.style.display = disp;
		event.stopPropagation();
	},
	close: function(){
		var dropdown = DropDown.get();	
		var disp = 'inline-block';
		if(dropdown.style.display === disp){
			dropdown.style.display = 'none';
		}
	}
};

var BaseContext = {
	get:function(){
		return $('#baseContext').attr('href');
	}
};

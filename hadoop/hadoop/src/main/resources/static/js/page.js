$(document).ready(function(){


	screenHeight = $(window).outerHeight(true) - $('.navbar-fixed').outerHeight(true)- $('.news-container').outerHeight(true);

	$('.main_slide').height(screenHeight);
	$(window).resize(function(){
		screenHeight = $(window).outerHeight(true) - $('.navbar-fixed').outerHeight(true)- $('.news-container').outerHeight(true);
		$('.main_slide').height(screenHeight);
	});




if($('.splash-screen-slider').length > 0) {
		ul = $('.splash-screen-slider ul');
		ul.children().each(function(i,li){
			ul.prepend(li);
		});
		setTimeout(function() { scroller(); }, 5000);
	}

sl2 = $(window).outerHeight(true);

$('.sld').height(sl2);



	 $(window).bind('scroll', function() {
       var movableHeight = ($(window).height())
       if ($(window).scrollTop() > movableHeight) {

       			$('#desktop-nav-div').addClass('fixed');

       }
       else {

       			$('#desktop-nav-div').removeClass('fixed');

       }



   });




});


function scroller() {
	$('.splash-screen-slider li:last').fadeOut(3000, function() {
		$('.splash-screen-slider ul').prepend($('.splash-screen-slider li:last'));
		$('.splash-screen-slider li:first').show();
		setTimeout(function() { scroller(); }, 5000);
	});
}

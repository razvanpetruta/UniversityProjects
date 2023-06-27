$(document).ready(function () {
    let slideNum = $('.page').length,
        wrapperWidth = 100 * slideNum,
        slideWidth = 100 / slideNum;

    $('.wrapper').width(wrapperWidth + '%');
    $('.page').width(slideWidth + '%');

    $('a.scrollitem').click(function (e) {
        $('a.scrollitem').removeClass('selected');
        $(this).addClass('selected');

        let slideNumber = $($(this).attr('href')).index('.page'),
            margin = slideNumber * -100 + '%';

        $('.wrapper').animate({marginLeft: margin},1000);
        e.preventDefault();
    });
});
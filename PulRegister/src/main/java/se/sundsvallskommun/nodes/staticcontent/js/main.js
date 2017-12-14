
function is_running_msie() {

    var ua = window.navigator.userAgent;
    var msie = ua.indexOf("MSIE ");

    if (msie > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./))  // If Internet Explorer, return version number
    {
        return true;
    }
    else  // If another browser, return 0
    {
        return false;
    }
}
$(document).ready(function () {

    $("#displayServletByPathResponse").click(function() {
        $.ajax({
            type: "GET",
            url: '/bin/my-project/custom/servlet-path',
            /*data : {
                pass your request parameter here, currently we are not passing any data
            },*/
            success: function (data, textStatus, jqXHR) {
                alert(JSON.stringify(data));
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alert('error!!');
            }
        });
    });
});
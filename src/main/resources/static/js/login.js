$(function($) {

    $("#login").click(function (){

        var loginDTO = {
            "username": $("#username").val(),
            "password": $("#password").val(),
        }

        $.ajax({
            method: "POST",
            dataType: "json",
            data: "json",
            contentType: "application/json;charset=UTF-8",
            data: JSON.stringify(loginDTO),
            url: "http://localhost:8989/login",
            success: function (data){
                alert(data.message)
                var a = data.obj.tokenhead;
                // alert(a)
                var b = data.obj.token;
                // alert(b)
                var c = ' '
                alert(a + c + b)
                if(data.code == 200){

                    localStorage.setItem("token", a + c + b)

                    location.href="http://localhost:8989/getPerson"
                }
            }
        })
    })


});
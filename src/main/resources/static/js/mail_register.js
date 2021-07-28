$(function($) {

    $("#register").click(function (){

        var registerDTO = {
            "mail": $("#username").val(),
            "password": $("#password").val(),
            "rePwd": $("#resetpw").val(),
        }

        $.ajax({
            method: "POST",
            dataType: "json",
            data: "json",
            contentType: "application/json;charset=UTF-8",
            data: JSON.stringify(registerDTO),
            url: "http://localhost:8989/registerByMail",
            success: function (data){
                alert(data.message)
            }
        })
    })


});

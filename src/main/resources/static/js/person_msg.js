$(function ($) {

    $("#headerImg").click(function () {
        $("#head-img").click();
    })


    $("#head-img").change(function (event) {
        //根据这个 <input> 获取文件的 HTML5 js 对象
        var files = event.target.files, file;
        if (files && files.length > 0) {
            // 获取目前上传的文件
            file = files[0];
            // 来在控制台看看到底这个对象是什么
            console.log(file);
            // 那么我们可以做一下诸如文件大小校验的动作
            if (file.size > 1024 * 1024 * 5) {
                alert('图片大小不能超过 5MB!');
                return false;
            }
            // !!!!!!
            // 下面是关键的关键，通过这个 file 对象生成一个可用的图像 URL
            // 获取 window 的 URL 工具
            var URL = window.URL || window.webkitURL;
            // 通过 file 生成目标 url
            var imgURL = URL.createObjectURL(file);
            $("#headerImg").attr("src", imgURL);
        }
    })

    alert(localStorage.getItem("token"))

    $.ajax({
        type: "POST",
        dataType: "json",
        data: "json",
        beforeSend: function (request) {
            request.setRequestHeader("Authorization", localStorage.getItem("token"));
        },
        contentType: "application/json;charset=UTF-8",
        url: "http://localhost:8989/findAdmin",
        success: function (data){
            if(data.code == 200){

                $("#headerImg").attr("src", data.obj.imgPath);
                $("#realName").attr("value", data.obj.realName);
                $("#phone").attr("value", data.obj.phone);
                $("#age").attr("value", data.obj.age);
                $("#mail").attr("value", data.obj.mail);

            }
        }
    })


    $("#save").click(function (){

        var formData = new FormData($("#updateUserForm")[0]);

        $.ajax({
            method: "POST",
            dataType: "json",
            data: formData,
            cache: false,
            async: false,
            processData : false,  //必须false才会避开jQuery对 formdata 的默认处理
            contentType : false,
            beforeSend: function (request) {
                request.setRequestHeader("Authorization", localStorage.getItem("token"));
            },
            url: "http://localhost:8989/updateAdmin",
            success: function (data){
                alert(data.msg)
            }
        })

    })



})



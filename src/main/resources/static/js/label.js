var lab=new Array();
var str="";
var flag=0;
$(function ($) {
    var selected=$(".span_3 .s_label");
    if(selected.length!=0)  flag=1;
    for(var i=0;i<selected.length;i++){
        lab.push(selected[i].innerHTML);
    }
    //弹出
    $("#label").hover(function () {
        $(this).stop().animate({
            opacity: '1'
        }, 600);
    }, function () {
        $(this).stop().animate({
            opacity: '0.6'
        }, 1000);
    }).on('click',function (e) {
        stopFunc(e);
        $("#labelBox").css({display: "block"});
    });
    //关闭
    $(".close_btn").hover(function () {
        $(this).css({color: 'black'})
    }, function () {
        $(this).css({color: '#999'})
    }).on('click', function (e) {
        stopFunc(e);
        $("#labelBox").css({display: 'none'});
    });
    //阻止冒泡
    $("#labelBox").on('click', function (e) {
        stopFunc(e);
    });
    //点击其他位置隐藏窗口
    $("body").on('click', function () {
        $("#labelBox").css({display: 'none'});
    });
    //显示子标签
    $(".span_1").on('click', function () {
        $(".spans").css({display: 'none'});
        this.getElementsByClassName("spans")[0].style.display="block";
    });
    //将选择标签加入已选标签
    $(".span_2").on('click', function () {
        var text=$(this).text();
        if(lab[0]==null){
            lab.push(text);
            let label= "<span><span class='s_label'>"+text+"</span><span class='deleted'>✖</span></span>";
            $(".span_3").append(label);
            //删除标签
            $(".deleted").on('click',function (){
                var dtext=this.parentNode.getElementsByClassName("s_label")[0].innerHTML;
                for(var j=0;j<lab.length;j++){
                    if(lab[j]==dtext){
                        lab.splice(j,1);
                        this.parentNode.remove();
                    }
                }
            });
        }else { //标签不重复
            let i = 0;
            for(i;i<lab.length;i++){
                if(lab[i]==text){
                    break;
                }
            }
            if(i==lab.length){
                lab.push(text);
                let label= "<span><span class='s_label'>"+text+"</span><span class='deleted'>✖</span></span>";
                $(".span_3").append(label);
                $(".deleted").on('click',function (){
                    var dtext=this.parentNode.getElementsByClassName("s_label")[0].innerHTML;
                    for(var j=0;j<lab.length;j++){
                        if(lab[j]==dtext){
                            lab.splice(j,1);
                            this.parentNode.remove();
                        }
                    }
                });
            }
        }
    });
    //删除标签
    $(".deleted").on('click',function (){
        var dtext=this.parentNode.getElementsByClassName("s_label")[0].innerHTML;
        for(var j=0;j<lab.length;j++){
            if(lab[j]==dtext){
                lab.splice(j,1);
                this.parentNode.remove();
            }
        }
    });
    //提交修改
    $(".insert_in").on('click', function () {
        console.log(lab)
        $("#labelBox").css({display: 'none'});
        for(var i=0;i<lab.length;i++){
            if(i==0){
                str+=lab[i];
            }else {
                str=str+"@@"+lab[i];
            }
        }
        var d={"article":"5","date":str};
        if(flag==0){
            $.ajax({
                type: "post",
                dataType: 'json',
                url: "/html/insertLabel",
                contentType: 'application/json',
                data:JSON.stringify(d)
            });
        }else {
            $.ajax({
                type: "post",
                dataType: 'json',
                url: "/html/updateLabel",
                contentType: 'application/json',
                data:JSON.stringify(d)
            });
        }
    });
});
//阻止冒泡
function stopFunc(e) {
    e.stopPropagation ? e.stopPropagation() : e.cancelBubble = true;
};
//删除标签


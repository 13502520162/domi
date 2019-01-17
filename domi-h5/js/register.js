let globalHttp = 'http://';
let globalAjaxUrl = globalHttp + "yanis.vicp.io:11790";
var options = {
    useEasing: true,
    useGrouping: true,
    separator: ',',
    decimal: '.',
};

setTimeout(function () {
    var demo = new CountUp('may-borrow-money', 0, 200000, 0, 4, options);
    if (!demo.error) {
        demo.start();
    } else {
        console.error(demo.error);
    }
}, 500);

window.onresize = function () {
    if (document.activeElement.tagName == "INPUT" || document.activeElement.tagName == "TEXTAREA") {
        setTimeout(function () {
            var top = document.activeElement.getBoundingClientRect().top;
            window.scrollTo(0, top);
        }, 0);
    }
};

$('.registration-agreement a').click(function () {
    window.location.href = 'user-agreement.html'
});


// 发送验证码
$('.verification-code').click(function () {
    let cellphone = $('.cellphone').val();
    if (cellphone=='') {
        prompt4m('请输入手机号码','middle',2000);
        return
    }
    if(!(/^1[34578]\d{9}$/.test(cellphone))){
        prompt4m('手机号码有误，请重填','middle',2000);
        return false;
    }
    var p = new Promise(function (resolve, reject) {
        $.ajax({
                type: "GET",
                url: globalAjaxUrl + '/app/getRandomNum?cellphone='+cellphone+'&type=0',
                success: function (res) {
                    resolve(res);
                }
            }
        );
    });
    p.then(function (res) {
        let randomNum = res.data.randomNum;
        let signKey = hex_md5('daidai'+cellphone+randomNum);
        $.ajax({
                type: "GET",
                url: globalAjaxUrl + '/app/getCellPhoneCode?cellphone='+cellphone+'&type=0&signKey='+signKey,
                success: function (res) {
                    if (res.errcode===0){
                        prompt4m(res.info,'middle',2000);
                    } else {
                        prompt4m(res.info,'middle',2000);
                    }
                }
            }
        );
    });

});


// 注册
$('.register-now-btn').click(function () {
    let cellphone = $('.cellphone').val();
    let checkCode = $('.checkCode').val();
    let password = $('.password').val();
    if (cellphone=='') {
        prompt4m('手机号码不能为空','middle',2000);
        return false
    }
    if(!(/^1[34578]\d{9}$/.test(cellphone))){
        alert("手机号码有误，请重填");
        prompt4m('手机号码有误，请重填','middle',2000);
        return false;
    }
    if (checkCode=='') {
        prompt4m('验证码不能为空','middle',2000);
        return false
    }
    if (password=='') {
        prompt4m('密码不能为空','middle',2000);
        return false
    }


    let password1 = hex_md5(password);
    $.ajax({
            type: "GET",
            url: globalAjaxUrl + '/web/register.do?cellphone='+cellphone+'&password='+password1+'&inviteCode='+2+'&code='+checkCode,
            success: function (res) {
                if (res.errcode===0){
                    prompt4m(res.info,'middle',2000);
                    console.log()
                } else {
                    prompt4m(res.info,'middle',2000);
                }
            }
        }
    );
});



/** mobile web toast提示框
 * 使用方式：
 *      prompt4m("屌丝逆袭了","bottom",1000);
 *      prompt4m("出任SEO","bottom");
 *      prompt4m("赢取白富美",1000);
 *      prompt4m("走向人生巅峰");
 */
function prompt4m() {
    //默认设置
    var dcfg={
        msg:"提示信息",
        postion:"top",//展示位置，可能值：bottom,top,middle,默认top：是因为在mobile web环境下，输入法在底部会遮挡toast提示框
        time:3000,//展示时间
    };
    //*********************以下为参数处理******************
    var len = arguments.length;
    if(arguments.length>0){
        dcfg.msg =arguments[0];

        var arg1 =arguments[1];
        var regx = /(bottom|top|middle)/i;
        var numRegx = /[1-9]\d*/;
        if(regx.test(arg1)){
            dcfg.postion=arg1;
        }else if(numRegx.test(arg1)){
            dcfg.time=arg1;
        }

        var arg2 =arguments[2];
        var numRegx = /[1-9]\d*/;
        if(numRegx.test(arg2)){
            dcfg.time=arg2;
        }
    }
//*********************以上为参数处理******************
    if ($(".prompt4m").length <= 0) {
        $("body").append("<div class='prompt4m'>" + dcfg.msg + "</div>");
    } else {
        $(".prompt4m").html(dcfg.msg);
    }
    var w = $(".prompt4m").width(),ww = $(window).width();
    $(".prompt4m").fadeIn(300);
    $(".prompt4m").css("left",(ww-w)/2-10);
    if("bottom"==dcfg.postion){
        $(".prompt4m").css("bottom",50);
        $(".prompt4m").css("top","");//这里为什么要置空，自己琢磨，我就不告诉
    }else if("top"==dcfg.postion){
        $(".prompt4m").css("bottom","");
        $(".prompt4m").css("top",50);
    }else if("middle"==dcfg.postion){
        var h = $(".prompt4m").height(),hh = $(window).height();
        $(".prompt4m").css("bottom",(hh-h)/2-10);
    }
    setTimeout(function() {
        $(".prompt4m").fadeOut(300);
    }, dcfg.time);
}


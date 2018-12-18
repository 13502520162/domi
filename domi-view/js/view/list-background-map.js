// 上传图片
function upFileBackground(a) {
    var $this = $(a);
    var fr = new FileReader(); // 这个FileReader应该对应于每一个读取的文件都需要重新new一个
    var files = $this[0].files[0]; // files可以获取当前文件输入框中选择的所有文件，返回列表
    fr.readAsDataURL(files); // 读取的内容是加密以后的本地文件路径
    fr.onload = function (e) { // 数据读取完成时触发onload对应的响应函数
        let src =   pageCommon.getTokenUrl(e.target.result);
        $this.parent().hide();
        $this.parents('.list-background-map-upload').find('.img-src').show();
        $this.parents('.list-background-map-upload').find('img').attr('src', src);
        $this.parents('.list-background-map-upload').attr('data-src', src);
    };
}

$('.re-upload').click(function () {
    $(this).parents('.list-background-map-upload').find('input').val('');
    $(this).prev().attr('src', '');
    $(this).parents('.list-background-map-upload').find('.fileImg').show();
    $(this).parent().hide();
});

//  icon  的Id
let arrId = localStorage.getItem('arrId');
if (arrId) {
    arrId = arrId.split(',');
    for (let i = 0; i < arrId.length; i++) {
        $('.list-background-map-upload').eq(i).attr('data-icon-id', arrId[i]);
    }
}


// 确定
$('.list-background-map-confirm').click(function () {
    if (!arrId){
        pageCommon.layerMsg('请先上传分类模块的图片',2);
        return false;
    }
    let iconLen = $('.list-background-map-upload-icon').length;
    let photoLen = $('.list-background-map-upload-photo').length;
    let iconArr = [];
    for (let i = 0; i < iconLen; i++) {
        let iconObj = {
            id: $('.list-background-map-upload-icon').eq(i).attr('data-icon-id') || '',
            background: $('.list-background-map-upload-icon').eq(i).attr('data-src') || ''
        };
        iconArr.push(iconObj);
    }

    if (iconArr[0].background == '' || iconArr[1].background == ''|| iconArr[2].background == ''){
        pageCommon.layerMsg('请选择图片',2);
        return false;
    }

    let photoArr = [];
    for (let i = 0; i < photoLen; i++) {
        let photoObj = {
            id: $('.list-background-map-upload-photo').eq(i).attr('data-icon-id') || '',
            background: $('.list-background-map-upload-photo').eq(i).attr('data-src') || ''
        };
        photoArr.push(photoObj);
    }

    if (photoArr[0].background == '' || photoArr[1].background == ''|| photoArr[2].background == ''){
        pageCommon.layerMsg('请选择图片',2);
        return false;
    }

    let obj = {
        icon: iconArr,
        photo: photoArr
    };

    let post = {newData: JSON.stringify(obj)};
    let url = globalAjaxUrl + '/admin/icon/addBackground';
    pageCommon.postAjax(url, post, function (res) {
        pageCommon.layerMsg(res.msg);
        photoInit();
    });
});


// 取消
$('.list-background-map-cancel').click(function () {
    $('.img-src').hide();
    $('.fileImg').show();
    photoInit();
});

photoInit();
function photoInit(){
    // 列表背景初始化
    let url = globalAjaxUrl + '/admin/icon/getBackground';
    pageCommon.getAjax(url, {}, function (res) {
        if (res.msg == '获取成功'){
            console.log(res);
            for (let i = 0; i < res.icon.length; i++) {
                $('.list-background-map-upload-icon').eq(i).attr('data-icon-id',res.icon[i].id);
                $('.list-background-map-upload-icon').eq(i).attr('data-src',res.icon[i].background);
                $('.list-background-map-upload-icon').eq(i).find('img').attr('src', res.icon[i].background);
            }

            for (let i = 0; i < res.photo.length; i++) {
                $('.list-background-map-upload-photo').eq(i).attr('data-icon-id',res.photo[i].id);
                $('.list-background-map-upload-photo').eq(i).attr('data-src',res.photo[i].background);
                $('.list-background-map-upload-photo').eq(i).find('img').attr('src', res.photo[i].background);
            }


            $('.img-src').show();
            $('.fileImg').hide();
        }
    });
}

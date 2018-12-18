// 上传icon
function upFile(a) {
    let $this = $(a);
    let fr = new FileReader(); // 这个FileReader应该对应于每一个读取的文件都需要重新new一个
    let files = $this[0].files[0]; // files可以获取当前文件输入框中选择的所有文件，返回列表
    fr.readAsDataURL(files); // 读取的内容是加密以后的本地文件路径
    fr.onload = function (e) { // 数据读取完成时触发onload对应的响应函数
        let src =   pageCommon.getTokenUrl(e.target.result);
        $this.parent().hide();
        $this.parents('.img-file').find('.img-src').show();
        $this.parents('.img-file').find('img').attr('src', src);
        $this.parents('li').attr('data-src', src);
    };
}


// 上传图片
function upFilePhoto(a) {
    let $this = $(a);
    let fr = new FileReader(); // 这个FileReader应该对应于每一个读取的文件都需要重新new一个
    let files = $this[0].files[0]; // files可以获取当前文件输入框中选择的所有文件，返回列表
    fr.readAsDataURL(files); // 读取的内容是加密以后的本地文件路径
    fr.onload = function (e) { // 数据读取完成时触发onload对应的响应函数
        let src =   pageCommon.getTokenUrl(e.target.result);
        $this.parent().hide();
        $this.parents('.img-file').find('.img-src').show();
        $this.parents('.img-file').find('img').attr('src', src);
        $this.parents('.img-file').attr('data-src', src);
    };
}


$('.re-upload').click(function () {
    $(this).parents('.img-file').find('input').val('');
    $(this).prev().attr('src', '');
    $(this).parents('.img-file').find('.fileImg').show();
    $(this).parent().hide();
});


let iconOne = $('.icon1');
let iconTwo = $('.icon2');
let iconThree = $('.icon3');
// 确定
$('.rotation-chart-confirm').click(function () {

    //icon图片和内容
    let oneSrc = iconOne.attr('data-src') || '';
    let twoSrc = iconTwo.attr('data-src') || '';
    let threeSrc = iconThree.attr('data-src') || '';
    let oneVal = iconOne.find('.icon-ipt').val() || '';
    let twoVal = iconTwo.find('.icon-ipt').val() || '';
    let threeVal = iconThree.find('.icon-ipt').val() || '';
    let oneId = iconOne.attr('data-id') || '';
    let twoId = iconTwo.attr('data-id') || '';
    let threeId = iconThree.attr('data-id') || '';

    // 图片
    let photoOne = $('.photo-content-left .img-file').attr('data-src') || '';
    let photoTwo = $('.photo-content-right-top .img-file').attr('data-src') || '';
    let photoThree = $('.photo-content-right-bottom .img-file').attr('data-src') || '';
    let photoOneId = $('.photo-content-left').attr('data-id') || '';
    let photoTwoId = $('.photo-content-right-top').attr('data-id') || '';
    let photoThreeId = $('.photo-content-right-bottom').attr('data-id') || '';

    if (oneSrc == ''||twoSrc == ''||threeSrc == ''||photoOne == ''||photoTwo == ''||photoThree == ''){
        pageCommon.layerMsg('请选择图片',2);
        return false;
    }

    if (oneVal == ''||twoVal == ''||threeVal == ''){
        pageCommon.layerMsg('请输入内容',2);
        return false;
    }
    let obj = {
        icon: [
            {
                id: oneId,
                imgUrl: oneSrc,
                name: oneVal,
            },
            {
                id: twoId,
                imgUrl: twoSrc,
                name: twoVal
            },
            {
                id: threeId,
                imgUrl: threeSrc,
                name: threeVal,
            }
        ],
        photo: [
            {
                id: photoOneId,
                imgUrl: photoOne,
            },
            {
                id: photoTwoId,
                imgUrl: photoTwo,
            },
            {
                id: photoThreeId,
                imgUrl: photoThree
            }
        ]
    };

    let post = {newData: JSON.stringify(obj)};
    let url = globalAjaxUrl + '/admin/icon/addLoanPlatform';
    pageCommon.postAjax(url, post, function (res) {
        pageCommon.layerMsg(res.msg);
        iconInit();
    });
});


// 取消
$('.rotation-chart-cancel').click(function () {
    $('.img-src').hide();
    $('.fileImg').show();
    $('.icon-ipt').val('');
    iconInit();
});


iconInit();
function iconInit(){
    // 分类初始化
    let url = globalAjaxUrl + '/admin/icon/getIcon';
    pageCommon.getAjax(url, {}, function (res) {
        let arrId = [];
        if (res.icon.length && res.photo.length) {
            let iconArr = ['icon1', 'icon2', 'icon3'];
            for (let i = 0; i < res.icon.length; i++) {
                res.icon[i].field = iconArr[i];
                arrId.push( res.icon[i].id);
            }
            for (let j = 0; j < res.icon.length; j++) {
                $('.' + res.icon[j].field).attr('data-id', res.icon[j].id);
                $('.' + res.icon[j].field).attr('data-src', res.icon[j].imgUrl);
                $('.' + res.icon[j].field).find('.img-src img').attr('src', res.icon[j].imgUrl);
                $('.' + res.icon[j].field).find('.icon-ipt').val(res.icon[j].name);
            }

            let photoArr = ['photo-content-left', 'photo-content-right-top', 'photo-content-right-bottom'];
            for (let y = 0; y < res.photo.length; y++) {
                res.photo[y].field = photoArr[y];
                arrId.push( res.photo[y].id);
            }

            for (let k = 0; k < res.photo.length; k++) {
                $('.' + res.photo[k].field).attr('data-id', res.photo[k].id);
                $('.' + res.photo[k].field).find('.img-file').attr('data-src', res.photo[k].imgUrl);
                $('.' + res.photo[k].field).find('.img-src img').attr('src', res.photo[k].imgUrl);
            }

            $('.img-src').show();
            $('.fileImg').hide();
            localStorage.setItem('arrId',arrId);
        }
    });
}


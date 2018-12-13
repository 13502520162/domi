$(function () {
    $('.add-platform').perfectScrollbar();
    $('.platform-synopsis').tagsInput({
        defaultText: '添加标签',
        maxChars: 5,
        onChange: function () {
            var len = $('.tagsinput>span').length;
            if (len >= 3) {
                $('.tagsinput').find('input').attr('disabled', 'disabled');
            } else {
                $('.tagsinput').find('input').removeAttr('disabled');
            }
        },
    });

    // 分类和标签初始化
    let url = globalAjaxUrl + '/admin/loanPlatform/getLabelAndType';
    pageCommon.getAjax(url, {}, function (res) {
        for (let i = 0; i < res.typeList.length; i++) {
            $('.platform-classification').append('<option value="' + res.typeList[i].id + '">' + res.typeList[i].name + '</option>');
        }
        for (let j = 0; j < res.labelList.length; j++) {
            $('.platform-label').append('<option value="' + res.labelList[j].id + '">' + res.labelList[j].name + '</option>');
        }

        // 数据填充
        let query = pageCommon.getUrlParams();
        let data = $(".platform-content", parent.document).contents().find('.content-edit').parents('li').attr('data-data');
        if (data) {
            if (query.field == 'edit') {
                data = JSON.parse(data);
                $('.add-platform').attr('data-id', data.id);
                $('.platform-title').val(data.name);
                $('.maximum-borrowable').val(data.maxMoney);
                $('.daily-interest-rate').val(data.dayRatio);
                $('.platform-synopsis').importTags(data.platformDesc);
                $('.platform-url').val(data.url);
                $('.platform-classification').val(data.typeId);
                $('.platform-label').val(data.labelId);
                $('.cooperation-mode').val(data.model);
                $('.platform-price').val(data.money);
                $('.platform-pick-up-people').val(data.dockPeople);
                $('.contact-information').val(data.phone);
                $('.fileImg').hide();
                $('.platform-img').show();
                $('.platform-img img').attr('src', data.logo);
                $('.platform-photo').attr('data-src', data.logo);
            }
        }
    });

    // 上传图片的事件
    $('#fileImg').change(function () {
        var $this = $(this);
        var fr = new FileReader(); // 这个FileReader应该对应于每一个读取的文件都需要重新new一个
        var files = $this[0].files[0]; // files可以获取当前文件输入框中选择的所有文件，返回列表
        fr.readAsDataURL(files); // 读取的内容是加密以后的本地文件路径
        fr.onload = function (e) { // 数据读取完成时触发onload对应的响应函数
            var src = pageCommon.getTokenUrl(e.target.result);
            $('.platform-img img').attr('src', src);
            $('.platform-photo').attr('data-src', src);
            $('.fileImg').hide();
            $('.platform-img').show();
        };
    });

    // 清除上传的logo
    $('.re-upload').click(function () {
        $('#fileImg').val('');
        $('.platform-img img').attr('src', '');
        $('.fileImg').show();
        $('.platform-img').hide();
    });
});
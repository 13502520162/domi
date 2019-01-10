$(function () {

    let query = pageCommon.getUrlParams();
    let data = $('.rotation-chart-iframe', parent.document).contents().find('.content-data').text();
    if (data) {
        if (query.field == 'edit' || query.field == 'view') {
            data = JSON.parse(data);
            $('.banner .article-photo').attr('data-src', data.imgUrl);
            $('.banner .article-img img').attr('src', data.imgUrl);
            $('.bannerBg .article-photo').attr('data-src', data.background);
            $('.bannerBg .article-img img').attr('src', data.background);
            $('.banner-url').val(data.url);
            $('.fileImg').hide();
            $('.article-img').show();

            if (query.field == 'view') {
                $('input,select,textarea').attr('disabled', 'disabled');
                $('.re-upload').remove();
                $('.tagsinput').find('a').remove();
            }
        }
    }




    $('#fileImg').change(function () {
        var $this = $(this);
        var fr = new FileReader(); // 这个FileReader应该对应于每一个读取的文件都需要重新new一个
        var files = $this[0].files[0]; // files可以获取当前文件输入框中选择的所有文件，返回列表
        fr.readAsDataURL(files); // 读取的内容是加密以后的本地文件路径
        fr.onload = function (e) { // 数据读取完成时触发onload对应的响应函数
            var src = pageCommon.getTokenUrl(e.target.result);
            $this.parents('.add-article-filed-photo').find('.article-img img').attr('src', src);
            $this.parents('.add-article-filed-photo').find('.article-photo').attr('data-src', src);
            $this.parents('.add-article-filed-photo').find('.fileImg').hide();
            $this.parents('.add-article-filed-photo').find('.article-img').show();
        };
    });


    $('#fileBgImg').change(function () {
        var $this = $(this);
        var fr = new FileReader(); // 这个FileReader应该对应于每一个读取的文件都需要重新new一个
        var files = $this[0].files[0]; // files可以获取当前文件输入框中选择的所有文件，返回列表
        fr.readAsDataURL(files); // 读取的内容是加密以后的本地文件路径
        fr.onload = function (e) { // 数据读取完成时触发onload对应的响应函数
            var src = pageCommon.getTokenUrl(e.target.result);
            $this.parents('.add-article-filed-photo').find('.article-img img').attr('src', src);
            $this.parents('.add-article-filed-photo').find('.article-photo').attr('data-src', src);
            $this.parents('.add-article-filed-photo').find('.fileImg').hide();
            $this.parents('.add-article-filed-photo').find('.article-img').show();
        };
    });


    $('.re-upload').click(function () {
        $(this).parents('.add-article-filed-photo').find('#fileImg').val('');
        $(this).parents('.add-article-filed-photo').find('.article-img img').attr('src', '');
        $(this).parents('.add-article-filed-photo').find('.fileImg').show();
        $(this).parents('.add-article-filed-photo').find('.article-img').hide();
    });
});
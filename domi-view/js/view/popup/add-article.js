var E = window.wangEditor;
var editor = new E('#editor');
$('.add-article').perfectScrollbar();


let url = globalAjaxUrl + '/admin/articleType/getAllArticleType';
pageCommon.getAjax(url, {}, function (res) {
    res = res.data
    for (let i = 0; i < res.articleTypes.length; i++) {
        $('.article-classification').append('<option value="' + res.articleTypes[i].id + '">' + res.articleTypes[i].name + '</option>');
    }
    // 数据填充
    let query = pageCommon.getUrlParams();
    let data = $('.article-management-iframe', parent.document).contents().find('.content-data').text();
    if (data) {
        if (query.field == 'edit' || query.field == 'view') {
            data = JSON.parse(data);
            console.log(data);
            $('.add-article').attr('data-id', data.id);
            $('.article-title').val(data.title);
            $('.article-author').val(data.author);
            $('.article-classification').val(data.typeId);
            editor.txt.html(data.content);
            $('.content-editor').text(data.content);
            $('.platform-url').val(data.url);
            $('.article-img img').attr('src', data.imgUrl);
            $('.article-photo').attr('data-src', data.imgUrl);
            $('.fileImg').hide();
            $('.article-img').show();
            if (query.field == 'view') {
                $('input,select,textarea').attr('disabled', 'disabled');
                $('.re-upload').remove();
                editor.$textElem.attr('contenteditable', false)
            }
        }
    }
});


// 图片上传
$('#fileImg').change(function () {
    var $this = $(this);
    var fr = new FileReader(); // 这个FileReader应该对应于每一个读取的文件都需要重新new一个
    var files = $this[0].files[0]; // files可以获取当前文件输入框中选择的所有文件，返回列表
    fr.readAsDataURL(files); // 读取的内容是加密以后的本地文件路径
    fr.onload = function (e) { // 数据读取完成时触发onload对应的响应函数
        var src = pageCommon.getTokenUrl(e.target.result);
        $('.article-img img').attr('src', src);
        $('.article-photo').attr('data-src', src);
        $('.fileImg').hide();
        $('.article-img').show();
    };
});


// 重新上传
$('.re-upload').click(function () {
    $('#fileImg').val('');
    $('.article-img img').attr('src', '');
    $('.fileImg').show();
    $('.article-img').hide();
});


// 自定义菜单配置
editor.customConfig.menus = [
    'head',  // 标题
    'bold',  // 粗体
    'fontSize',  // 字号
    'fontName',  // 字体
    'italic',  // 斜体
    'underline',  // 下划线
    'strikeThrough',  // 删除线
    'foreColor',  // 文字颜色
    'backColor',  // 背景颜色
    'link',  // 插入链接
    'list',  // 列表
    'justify',  // 对齐方式
    'quote',  // 引用
    /*        'emoticon',  // 表情*/
    'image',  // 插入图片
    'table',  // 表格
    'video',  // 插入视频
    'code',  // 插入代码
    'undo',  // 撤销
    'redo'  // 重复
];
editor.customConfig.uploadImgShowBase64 = true;  // 使用 base64 保存图片
editor.customConfig.customUploadImg = function (files, insert) {
    let fr = new FileReader(); // 这个FileReader应该对应于每一个读取的文件都需要重新new一个
    let file = files[0]; // files可以获取当前文件输入框中选择的所有文件，返回列表
    fr.readAsDataURL(file); // 读取的内容是加密以后的本地文件路径
    fr.onload = function (e) { // 数据读取完成时触发onload对应的响应函数
        let src = pageCommon.getTokenUrl(e.target.result);
        insert(src) // 插入到 编辑器中  insert 是编辑器对象
    };
};
editor.customConfig.onchange = function (html) {
    // html 即变化之后的内容
    $('.content-editor').text();
    $('.content-editor').text(html)
};
editor.create()





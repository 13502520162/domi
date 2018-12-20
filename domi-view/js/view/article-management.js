
let table = layui.table;
let permissionValue = pageCommon.getPermissionValue();

table.render({
    elem: '#article-management-content-table'
    , even: true //开启隔行背景
    , method: 'GET'
    , limits: [10, 20, 30, 50, 100, 200]
    , limit: 10 //注意：请务必确保 limit 参数（默认：10）是与你服务端限定的数据条数一致 //支持所有基础参数
    , url: globalAjaxUrl + '/admin/article/getArticle'
    , cols: [[
        {field: 'title', width: '20%', title: '标题', align: 'center'}
        , {field: 'id', title: 'ID', align: 'center', hide: true}
        , {field: 'author', width: '10%', title: '作者', align: 'center'}
        , {field: 'imgUrl', width: '10%', title: '图片', templet: '#imgUrl', align: 'center'}
        , {field: 'content', title: '内容', align: 'center'}
        , {field: 'typeName', width: '10%',title: '推荐分类', align: 'center'}
        , {field: 'update', title: '更新时间', align: 'center'}
        , {fixed: 'right', width: '15%', title: '操作', toolbar: '#barDemo', align: 'center'}
    ]]
    , page: true
    , done: function (res, curr, count) {
        getArticleManagementSuccess(res);
    }
});



function getArticleManagementSuccess(res) {
    let liLen = $('.article-management-label>ul').find('.add-li').length;
    if (res.msg) {
        if (liLen != res.type.length) {
            for (let j = 0; j < res.type.length; j++) {
                let html = `<li class="add-li" data-id="${res.type[j].id}">
                            <div class="article-management-label-tit">
                                <span class="article-management-label-tit-span">${res.type[j].name}</span>
                                <input type="text" class="article-management-label-tit-ipt">
                            </div>
                            <span class="article-management-label-down"><i class="fa fa-chevron-down"></i></span>
                            <div class="article-management-label-option">
                                <p class="article-title-edit">编辑标题</p>
                                <p class="article-title-remove">删除标题</p>
                            </div>
                        </li>`;
                $('.add-article-li').before(html);
            }
        }
    }
}



table.on('tool(article-management-content-table)', function (obj) {
    let data = obj.data;
    if (obj.event === 'del') {
        if (!permissionValue.remove){
            pageCommon.layerMsg('你没有权限删除',2);
            return false;
        }
        layer.confirm('确定删除嘛？', function (index) {
            let url = globalAjaxUrl + '/admin/article/deleteArticle?articleId='+ data.id;
            pageCommon.getAjax(url, {}, function (res) {
                pageCommon.layerMsg(res.msg, 1);
                obj.del();
                layer.close(index);
            });
        });
    }else if (obj.event == 'edit'){
        if (!permissionValue.edit){
            pageCommon.layerMsg('你没有权限编辑',2);
            return false;
        }
        $('.content-data').text(JSON.stringify(data));
        let index = pageCommon.layerParentOpenIframe({
            url: globalUrl + '/view/popup/add-article.html?field=edit',
            title: '编辑文章',
            confirm: function () {
                var body = parent.layer.getChildFrame('body', index);
                var tit = body.find('.article-title').val();
                var id = body.find('.add-article').attr('data-id');
                var author = body.find('.article-author').val();
                var classification = body.find('.article-classification').val();
                var photo = body.find('.article-photo').attr('data-src');
                var content = body.find('.add-article-content').val();
                if (tit == '') {
                    pageCommon.layerMsg('标题不能为空', 2);
                    return false;
                }
                if (author == '') {
                    pageCommon.layerMsg('作者不能为空', 2);
                    return false;
                }
                if (photo == undefined) {
                    pageCommon.layerMsg('图片不能为空', 2);
                    return false;
                }
                if (content == '') {
                    pageCommon.layerMsg('内容不能为空', 2);
                    return false;
                }
                let arr = [];
                let obj = {
                    id:id,
                    title: tit,
                    author: author,
                    typeId: classification,
                    updateTime:"",
                    content: content,
                    imgUrl: photo
                };
                arr.push(obj);
                let url = globalAjaxUrl + '/admin/article/updateArticle';
                let data = {newData: JSON.stringify(arr)};
                pageCommon.postAjax(url, data, function (res) {
                    pageCommon.layerMsg(res.msg, 1);
                    parent.layer.close(index);
                    labelSelection('.article-active');
                });
            },
            cancel: function (index, layero) {
                parent.layer.close(index);
            }
        });
    }else if (obj.event == 'view'){
        $('.content-data').text(JSON.stringify(data));
        let index = pageCommon.layerParentOpenIframe({
            url: globalUrl + '/view/popup/add-article.html?field=view',
            title: '预览文章',
            btn:['关闭'],
            confirm: function (index, layero) {
                parent.layer.close(index);
            }
        });
    }
});


function labelSelection(e){
    let len = $(e).parent().find('li').length;
    let index = $(e).index();
    let id = $(e).attr('data-id');
    if (len - index != 1) {
        $(e).addClass('article-active').siblings().removeClass('article-active');
        table.reload('article-management-content-table', {
            url: globalAjaxUrl + '/admin/article/getArticleByArticleType'
            , where: {
                articleTypeId: id
            }
        });
    }

    if (index == 0){
        table.render({
            elem: '#article-management-content-table'
            , even: true //开启隔行背景
            , method: 'GET'
            , limits: [10, 20, 30, 50, 100, 200]
            , limit: 10 //注意：请务必确保 limit 参数（默认：10）是与你服务端限定的数据条数一致 //支持所有基础参数
            , url: globalAjaxUrl + '/admin/article/getArticle'
            , cols: [[
                {field: 'title', width: '20%', title: '标题', align: 'center'}
                , {field: 'id', title: 'ID', align: 'center', hide: true}
                , {field: 'author', width: '10%', title: '作者', align: 'center'}
                , {field: 'imgUrl', width: '10%', title: '图片', templet: '#imgUrl', align: 'center'}
                , {field: 'content', title: '内容', align: 'center'}
                , {field: 'typeName', width: '10%',title: '推荐分类', align: 'center'}
                , {field: 'update',width: '10%', title: '更新时间', align: 'center'}
                , {fixed: 'right', width: '15%', title: '操作', toolbar: '#barDemo', align: 'center'}
            ]]
            , page: true
            , done: function (res, curr, count) {
                getArticleManagementSuccess(res);
            }
        });
    }
}
//标题栏的切换
$('.article-management-label>ul').on('click', 'li', function () {
    labelSelection(this);

});



// 编辑标题 ok
$('.article-management-label').on('click', '.article-title-edit', function (e) {
    if (!permissionValue.edit){
        pageCommon.layerMsg('你没有权限编辑',2);
        return false;
    }
    var span = $(this).parents('li').find('.article-management-label-tit-span');
    var input = $(this).parents('li').find('.article-management-label-tit-ipt');
    var id = $(this).parents('li').attr('data-id');
    $(this).parents('.article-management-label-option').hide();
    var text = span.text();
    span.hide();
    input.show();
    input.val(text).focus();
    document.onkeydown = function (e) {
        var ev = document.all ? window.event : e;
        if (ev.keyCode == 13) {
            span.text(input.val());
            span.show();
            input.hide();

            let arr = [];
            let obj = {
                name: input.val(),
                id:id
            };
            arr.push(obj);
            let url = globalAjaxUrl + '/admin/articleType/updateArticleType';
            let data = {newData: JSON.stringify(arr)};
            pageCommon.postAjax(url, data, function (res) {
                pageCommon.layerMsg(res.msg, 1);
            });
        }
    }
});

// 删除标题 ok
$('.article-management-label').on('click', '.article-title-remove', function (e) {
    if (!permissionValue.remove){
        pageCommon.layerMsg('你没有权限删除',2);
        return false;
    }
    let $this = $(this);
    let id = $(this).parents('li').attr('data-id');
    let url = globalAjaxUrl + '/admin/articleType/deleteArticleTypeCount?articleTypeId='+id;
    pageCommon.layerConfirm(function () {
        $this.parents('li').remove();
        pageCommon.getAjax(url, {}, function (res) {
            pageCommon.layerMsg(res.msg, 1);
            getArticleManagement();
        })
    });
});

// 添加标题  ok
$('.add-article-title').click(function () {
    if (!permissionValue.add){
        pageCommon.layerMsg('你没有权限添加',2);
        return false;
    }
    $('.article-title-ipt').show();
    $('.article-title-ipt').focus();
    document.onkeydown = function (e) {
        var ev = document.all ? window.event : e;
        if (ev.keyCode == 13) {
            var val = $.trim($('.article-title-ipt').val());
            if (val) {
                var html = ` <li>
                            <div class="article-management-label-tit">
                                <span class="article-management-label-tit-span">${val}</span>
                                <input type="text" class="article-management-label-tit-ipt">
                            </div>
                            <span class="article-management-label-down"><i class="fa fa-chevron-down"></i></span>
                            <div class="article-management-label-option">
                                <p class="article-title-edit">编辑标题</p>
                                <p class="article-title-remove">删除标题</p>
                            </div>
                        </li>`;
                $('.add-article-li').before(html);
                let url = globalAjaxUrl + '/admin/articleType/addArticleType';
                let arr = [];
                let obj = {
                    name: val
                };
                arr.push(obj);
                let data = {newData: JSON.stringify(arr)};
                pageCommon.postAjax(url, data, function (res) {
                    pageCommon.layerMsg(res.msg, 1);
                    $('.article-title-ipt').val('').hide();
                    window.location.reload();
                });

            } else {
                layer.msg('请输入标题名称')
            }
        }
    }
});


// 创建文章
$('.article-management-top').on('click', '.article-management', function () {
    if (!permissionValue.add){
        pageCommon.layerMsg('你没有权限创建',2);
        return false;
    }
    var index = pageCommon.layerParentOpenIframe({
        url: globalUrl + '/view/popup/add-article.html',
        title: '创建文章',
        confirm: function () {
            var body = parent.layer.getChildFrame('body', index);
            var tit = body.find('.article-title').val();
            var author = body.find('.article-author').val();
            var classification = body.find('.article-classification').val();
            var photo = body.find('.article-photo').attr('data-src');
            var content = body.find('.add-article-content').val();
            if ($.trim(tit) == '') {
                pageCommon.layerMsg('标题不能为空', 2);
                return false;
            }
            if ($.trim(author) == '') {
                pageCommon.layerMsg('作者不能为空', 2);
                return false;
            }
            if ($.trim(photo) == undefined) {
                pageCommon.layerMsg('图片不能为空', 2);
                return false;
            }
            if ($.trim(content) == '') {
                pageCommon.layerMsg('内容不能为空', 2);
                return false;
            }
            let arr = [];
            let obj = {
                title: tit,
                author: author,
                typeId: classification,
                updateTime:"",
                content: content,
                imgUrl: photo
            };
            arr.push(obj);
            let url = globalAjaxUrl + '/admin/article/addArticle';
            let data = {newData: JSON.stringify(arr)};
            pageCommon.postAjax(url, data, function (res) {
                pageCommon.layerMsg(res.msg, 1);
                layer.close(index);
                getArticleManagement();
            });
        },
        cancel: function (index, layero) {
            layer.close(index);
        }
    });
});













$('.article-management-label>ul').on('mouseenter', 'li', function () {
    $(this).find('.fa-chevron-down').show();
});

$('.article-management-label>ul').on('mouseleave', 'li', function () {
    $(this).find('.article-management-label-option,.fa-chevron-down').hide();
});

$('.article-management-label').on('mouseenter', '.article-management-label-down', function () {
    $(this).next().show();
});

$('.article-management-content').perfectScrollbar();
function articleResize() {
    var modelH = $('.article-management-content>ul').outerWidth(true); //获取宽度
    $('.article-management-content>ul>li').css('width', (modelH / 3 - 20));  // 因为 li 是要 3个为一排加上外边框
    $('.article-management-content').perfectScrollbar('update');

}

setTimeout(() => {
    articleResize();
}, 10);


// 页面初始化
function getArticleManagement() {
    let url = globalAjaxUrl + '/admin/article/getArticle';
    pageCommon.getAjax(url, {}, getArticleManagementSuccess);
}

function getArticleManagementSuccess(res) {
    getArticleManagementList(res);
    $('.article-management-label-all-num').text(res.data.length);
    $('.article-management-label').find('.add-li').remove();
    for (let j = 0; j < res.type.length; j++) {
      let html =`<li class="add-li" data-id="${res.type[j].id}">
                            <div class="article-management-label-tit">
                                <span class="article-management-label-tit-span">${res.type[j].name}</span>
                                <input type="text" class="article-management-label-tit-ipt">
                                <span class="article-management-label-num">${res.type[j].count}</span>
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

function getArticleManagementList(res) {
    $('.article-management-content>ul').html('');
    for (let i = 0; i < res.data.length; i++) {
        let html = `<li  data-data='${JSON.stringify(res.data[i])}'  data-id="${res.data[i].id}" data-typeId="${res.data[i].typeId}" >
                            <p class="article-management-content-tit">${res.data[i].title}</p>
                            <img src="${res.data[i].imgUrl}" alt="" class="article-management-content-img">
                            <p class="article-management-content-con">${res.data[i].content}</p>
                            <div class="article-management-content-option">
                                <span class="article-management-content-time">更新于${res.data[i].update}</span>
                                <ul class="article-management-content-ul">
                                    <li class="article-management-content-edit" onclick="articleManagementContentEdit(this)"><i class="fa fa-edit"></i></li>
                                    <li class="article-management-content-remove" onclick="articleManagementContentRemove(this)"><i class="fa fa-trash-o"></i></li>
                                </ul>
                            </div>
                        </li>`;
        $('.article-management-content>ul').append(html);
    }
    pageCommon.noRelevantData('.article-management-content>ul li','.article-management-content>ul');
    articleResize();
}


//标题栏的切换
$('.article-management-label>ul').on('click', 'li', function () {
    var len = $(this).parent().find('li').length;
    var index = $(this).index();
    let id = $(this).attr('data-id');
    if (len - index != 1) {
        $(this).addClass('article-active').siblings().removeClass('article-active');
        let url = globalAjaxUrl + '/admin/article/getArticleByArticleType?articleTypeId='+id;
        pageCommon.getAjax(url, {}, function (res) {
            getArticleManagementList(res);
        })
    }

    if (index == 0){
        getArticleManagement();
    }

});



// 编辑标题 ok
$('.article-management-label').on('click', '.article-title-edit', function (e) {
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


// 添加文章
$('.article-management-top').on('click', '.article-management', function () {
    var index = pageCommon.layerParentOpenIframe({
        url: globalUrl + '/view/popup/add-article.html',
        title: '添加文章',
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

//  编辑文章
function articleManagementContentEdit(a){
    $(a).addClass('content-edit');
    var index = pageCommon.layerParentOpenIframe({
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
                layer.close(index);
                $(a).removeClass('content-edit');
                getArticleManagement();
            });
        },
        cancel: function (index, layero) {
            layer.close(index);
            $(a).removeClass('content-edit');
        }
    });
}


// 删除文章
function articleManagementContentRemove(a) {
    pageCommon.layerConfirm(function () {
        let id = $(a).parents('li').attr('data-id');
        let url = globalAjaxUrl + '/admin/article/deleteArticle?articleId='+ id;
        pageCommon.getAjax(url, {},function (res) {
            pageCommon.layerMsg(res.msg, 1);
            getArticleManagement();
        });
    });

}



$('.article-management-content-ul').on('click', '.article-management-content-remove', function () {
    alert('123')
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

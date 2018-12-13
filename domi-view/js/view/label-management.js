$('.label-management-content').perfectScrollbar();

function labelManagementResize() {
    var modelH = $('.label-management-content>ul').outerWidth(true); //获取宽度
    $('.label-management-content>ul>li').css('width', (modelH / 3 - 20));  // 因为 li 是要 3个为一排加上外边框
    $('.label-management-content').perfectScrollbar('update');
}

setTimeout(() => {
    labelManagementResize();
}, 10);


// 页面初始化
function getLabelManagement() {
    let url = globalAjaxUrl + '/admin/loanPlatform/getLoanPlatformLabel';
    pageCommon.getAjax(url, {}, getLabelManagementSuccess);
}

function getLabelManagementSuccess(res) {
    $('.label-management-label').find('.add-li').remove();
    getLabelManagementList(res);
    $('.label-management-label-all-num').text(res.data.length);
    for (let j = 0; j < res.label.length; j++) {
        let label = `<li data-id="${res.label[j].id}" class="add-li">
                            <div class="platform-label-tit">
                                <span class="label-management-label-tit-span">${res.label[j].name}</span>
                                <input type="text" class="label-management-label-tit-ipt">
                                <span class="platform-label-num">${res.label[j].count}</span>
                            </div>
                            <span class="platform-label-down"><i class="fa fa-chevron-down"></i></span>
                            <div class="platform-label-option">
                                <p class="label-management-title-edit">编辑标签</p>
                                <p class="label-management-title-remove">删除标签</p>
                            </div>
                        </li>`;
        $('.add-label-management-li').before(label);
    }
}

// 根据标签获取文章
function getLabelManagementList(res) {
    $('.label-management-content>ul').html('');
    for (let i = 0; i < res.data.length; i++) {
        var html = `    <li data-id="${res.data[i].id}" data-labelId="${res.data[i].labelId}" >
                            <div class="label-management-content-all">
                                <div class="label-management-content-all-top">
                                    <img src="${res.data[i].logo}" class="label-management-logo" alt="">
                                    <div class="label-management-synopsis">
                                     <p class="fw">${res.data[i].name}</p>
                                        <p class="fw">最高可借 ￥${res.data[i].maxMoney}</p>
                                    </div>
                                </div>
                                <div class="label-management-content-all-bottom">
                                    <div class="label-management-cooperation-mode">
                                             <span>合作模式 : </span>
                                        <span>${res.data[i].model}</span>
                                    </div>
                                    <div class="label-management-price">
                                       <span>价格 : </span>
                                        <span>￥${res.data[i].money}</span>
                                    </div>
                                </div>
                            </div>
                            <div class="label-management-content-option">
                                <span class="label-management-content-time">更新于${res.data[i].update}</span>
                                <ul class="label-management-content-ul">
                                    <li class="label-management-content-remove"><i class="fa fa-trash-o"></i></li>
                                </ul>
                            </div>
                        </li>`;
        $('.label-management-content>ul').append(html);
    }
    pageCommon.noRelevantData('.label-management-content>ul li','.label-management-content>ul');
    labelManagementResize();
}


//标题栏的切换
$('.label-management-label>ul').on('click', 'li', function () {
    let len = $(this).parent().find('li').length;
    let index = $(this).index();
    let id = $(this).attr('data-id');
    if (len - index != 1) {
        $(this).addClass('label-management-active').siblings().removeClass('label-management-active');
        let url = globalAjaxUrl + '/admin/loanPlatform/getLoanPlatformByLabelId?labelId=' + id;
        pageCommon.getAjax(url, {}, function (res) {
            getLabelManagementList(res);
        })
    }
    if (index == 0) {
        getLabelManagement();
    }

});


// 标题编辑 ok
$('.label-management-label').on('click', '.label-management-title-edit', function (e) {
    var span = $(this).parents('li').find('.label-management-label-tit-span');
    var input = $(this).parents('li').find('.label-management-label-tit-ipt');
    $(this).parents('.label-management-label-option').hide();
    var id = $(this).parents('li').attr('data-id');
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
                id: id
            };
            arr.push(obj);
            let url = globalAjaxUrl + '/admin/loanPlatform/updateLoanPlatformLabel';
            let data = {newData: JSON.stringify(arr)};
            pageCommon.postAjax(url, data, function (res) {
                pageCommon.layerMsg(res.msg, 1);
            });
        }
    }
});

// 标题删除 ok
$('.label-management-label').on('click', '.label-management-title-remove', function (e) {
    let $this = $(this);
    let id = $(this).parents('li').attr('data-id');
    let url = globalAjaxUrl + '/admin/loanPlatform/deleteLoanPlatformLabel?typeId=' + id;
    pageCommon.layerConfirm(function () {
        $this.parents('li').remove();
        pageCommon.getAjax(url, {}, function (res) {
            pageCommon.layerMsg(res.msg, 1);
        })
    });
});


// 添加标题
$('.add-label-management-title').click(function () {
    $('.label-management-title-ipt').show();
    $('.label-management-title-ipt').focus();
    document.onkeydown = function (e) {
        var ev = document.all ? window.event : e;
        if (ev.keyCode == 13) {
            var val = $.trim($('.label-management-title-ipt').val());
            if (val) {
                var html = ` <li>
                            <div class="label-management-label-tit">
                                <span class="label-management-label-tit-span">${val}</span>
                                <input type="text" class="label-management-label-tit-ipt">
                            </div>
                            <span class="label-management-label-down"><i class="fa fa-chevron-down"></i></span>
                            <div class="label-management-label-option">
                                <p class="label-management-title-edit">编辑标题</p>
                                <p class="label-management-title-remove">删除标题</p>
                            </div>
                        </li>`;


                let url = globalAjaxUrl + '/admin/loanPlatform/addLoanPlatformLabel';
                let arr = [];
                let obj = {
                    name: val
                };
                arr.push(obj);
                let data = {newData: JSON.stringify(arr)};
                pageCommon.postAjax(url, data, function (res) {
                    pageCommon.layerMsg(res.msg, 1);
                    $('.add-label-management-li').before(html);
                    $('.label-management-title-ipt').val('').hide();
                });
            } else {
                layer.msg('请输入标题名称')
            }
        }
    }
});


//  删除平台
$('.label-management-content').on('click', '.label-management-content-remove', function () {
    let id = $(this).parents('li').attr('data-id');
    let labelId = $(this).parents('li').attr('data-labelid');
    let url = globalAjaxUrl + '/admin/loanPlatform/deleteLoanPlatform?loanPlatformId=' + id + '&labelId=' + labelId + '&typeId=' + '';
    pageCommon.layerConfirm(function () {
        pageCommon.getAjax(url, {}, function (res) {
            pageCommon.layerMsg(res.msg, 1);
            getLabelManagement();
        });
    });
});


$('.label-management-label>ul').on('mouseenter', 'li', function () {
    $(this).find('.fa-chevron-down').show();
});

$('.label-management-label>ul').on('mouseleave', 'li', function () {
    $(this).find('.platform-label-option,.fa-chevron-down').hide();
});

$('.label-management-label').on('mouseenter', '.platform-label-down', function () {
    $(this).next().show();
});


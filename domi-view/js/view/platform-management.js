$('.platform-content').perfectScrollbar();


//  平台布局元素计算
function platformResize() {
    var modelH = $('.platform-content>ul').outerWidth(true); //获取宽度
    $('.platform-content>ul>li').css('width', (modelH / 3 - 20));  // 因为 li 是要 3个为一排加上外边框
    $('.platform-content').perfectScrollbar('update');
}

setTimeout(() => {
    platformResize();
}, 10);


// 页面初始化
function getPlatformManagement() {
    let url = globalAjaxUrl + '/admin/loanPlatform/getLoanPlatform';
    pageCommon.getAjax(url, {}, getPlatformManagementSuccess);
}
function getPlatformManagementSuccess(res) {
    if (res.msg) {
        $('.platform-label').find('.add-li').remove();
        $('.platform-label-all-num').text(res.data.length);
        getPlatformList(res);
        for (let j = 0; j < res.type.length; j++) {
            let label = `<li data-id="${res.type[j].id}" class="add-li">
                            <div class="platform-label-tit">
                                <span class="platform-label-tit-span">${res.type[j].name}</span>
                                <input type="text" class="platform-label-tit-ipt">
                                <span class="platform-label-num">${res.type[j].count}</span>
                            </div>
                            <span class="platform-label-down"><i class="fa fa-chevron-down"></i></span>
                            <div class="platform-label-option">
                                <p class="platform-title-edit">编辑分类</p>
                                <p class="platform-title-remove">删除分类</p>
                            </div>
                        </li>`;
            $('.add-platform-li').before(label);
        }

    }
}

// 根据分类获取平台
function getPlatformList(res) {
    $('.platform-content>ul').html('');
    for (let i = 0; i < res.data.length; i++) {
        var html = ` <li data-data='${JSON.stringify(res.data[i])}' data-id="${res.data[i].id}" data-labelId="${res.data[i].labelId}" data-typeId="${res.data[i].typeId}">
                            <div class="platform-content-all">
                                <div class="platform-content-all-top">
                                    <img src="${res.data[i].logo}"  class="platform-logo" alt="">
                                    <div class="platform-synopsis">
                                        <p class="fw">${res.data[i].name}</p>
                                        <p class="fw">最高可借 ￥${res.data[i].maxMoney}</p>
                                    </div>
                                </div>
                                <div class="platform-content-all-bottom">
                                    <div class="platform-cooperation-mode">
                                        <span>合作模式 : </span>
                                        <span>${res.data[i].model}</span>
                                    </div>
                                    <div class="platform-price">
                                        <span>价格 : </span>
                                        <span>￥${res.data[i].money}</span>
                                    </div>
                                </div>
                            </div>
                            <div class="platform-content-option">
                                <span class="platform-content-time">更新于${res.data[i].update}</span>
                                <ul class="platform-content-ul">
                                    <li class="platform-content-edit" onclick="platformContentEdit(this)" data-id="${res.data[i].id}"><i class="fa fa-edit"></i></li>
                                    <li class="platform-content-remove" onclick="platformContentRemove(this)"  data-id="${res.data[i].id}"><i class="fa fa-trash-o"></i></li>
                                </ul>
                            </div>
                        </li>`;
        $('.platform-content>ul').append(html);
    }
    pageCommon.noRelevantData('.platform-content>ul li','.platform-content>ul');
    platformResize();
}

//分类的切换
$('.platform-label>ul').on('click', 'li', function () {
    let len = $(this).parent().find('li').length;
    let index = $(this).index();
    let id = $(this).attr('data-id');
    if (len - index != 1) {
        $(this).addClass('platform-active').siblings().removeClass('platform-active');
        let url = globalAjaxUrl + '/admin/loanPlatform/getLoanPlatformByType?typeId='+id;
        pageCommon.getAjax(url, {}, function (res) {
            getPlatformList(res);
        })
    }
    if (index == 0){
        getPlatformManagement();
    }
});





// 分类添加 ok
$('.add-platform-title').click(function () {
    $('.platform-title-ipt').show();
    $('.platform-title-ipt').focus();
    document.onkeydown = function (e) {
        var ev = document.all ? window.event : e;
        if (ev.keyCode == 13) {
            var val = $.trim($('.platform-title-ipt').val());
            if (val) {
                var html = ` <li>
                            <div class="platform-label-tit">
                                <span class="platform-label-tit-span">${val}</span>
                                <input type="text" class="platform-label-tit-ipt">
                                 <span class="label-management-label-num">0</span>
                            </div>
                            <span class="platform-label-down"><i class="fa fa-chevron-down"></i></span>
                            <div class="platform-label-option">
                                <p class="platform-title-edit">编辑分类</p>
                                <p class="platform-title-remove">删除分类</p>
                            </div>
                        </li>`;

                let url = globalAjaxUrl + '/admin/loanPlatform/addLoanPlatformType';
                let arr = [];
                let obj = {
                    name: val
                };
                arr.push(obj);
                let data = {newData: JSON.stringify(arr)};
                pageCommon.postAjax(url, data, function (res) {
                    pageCommon.layerMsg(res.msg, 1);
                    $('.add-platform-li').before(html);
                    $('.platform-title-ipt').val('').hide();
                });
            } else {
                pageCommon.layerMsg('请输入标题名称')
            }
        }
    }
});


// 分类编辑 ok
$('.platform-label').on('click', '.platform-title-edit', function (e) {
    var span = $(this).parents('li').find('.platform-label-tit-span');
    var input = $(this).parents('li').find('.platform-label-tit-ipt');
    var id = $(this).parents('li').attr('data-id');
    $(this).parents('.platform-label-option').hide();
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
            let url = globalAjaxUrl + '/admin/loanPlatform/updateLoanPlatformType';
            let data = {newData: JSON.stringify(arr)};
            pageCommon.postAjax(url, data, function (res) {
                pageCommon.layerMsg(res.msg, 1);
            });
        }
    }
});

// 分类删除  ok
$('.platform-label').on('click', '.platform-title-remove', function (e) {
    let $this = $(this);
    let id = $(this).parents('li').attr('data-id');
    let url = globalAjaxUrl + '/admin/loanPlatform/deleteLoanPlatformType?typeId='+id;
    pageCommon.layerConfirm(function () {
        $this.parents('li').remove();
        pageCommon.getAjax(url, {}, function (res) {
            pageCommon.layerMsg(res.msg, 1);
            getPlatformManagement();
        })
    });

});




// 添加平台
$('.platform-top').on('click', '.platform', function () {
    var index = pageCommon.layerParentOpenIframe({
        url: globalUrl + '/view/popup/add-platform.html',
        title: '添加平台',
        confirm: function () {
            var body = parent.layer.getChildFrame('body', index),
                tit = body.find('.platform-title').val(),  // 平台名称
                maximumBorrowable = body.find('.maximum-borrowable').val(),  // 最高可借
                dailyInterestRate = body.find('.daily-interest-rate').val(),  // 日利率
                photo = body.find('.platform-photo').attr('data-src'),   // 平台logo
                synopsis = body.find('.platform-synopsis').val(), // 平台简介
                classification = body.find('.platform-classification').val(),  // 平台分类
                label = body.find('.platform-label').val(),  // 平台标签
                platformUrl = body.find('.platform-url').val(), // 平台链接
                cooperationMode = body.find('.cooperation-mode').val(), // 合作模式
                platformPrice = body.find('.platform-price').val(), // 价格
                pickUpPeople = body.find('.platform-pick-up-people').val(), // 对接人
                contactInformation = body.find('.contact-information').val(); // 联系方式

            var elArr = ['platform-title','maximum-borrowable','daily-interest-rate', 'platform-synopsis', 'platform-classification', 'platform-label', 'cooperation-mode', 'platform-price', 'platform-pick-up-people', 'contact-information'];
            var alertArr = ['平台名称','最高可借', '日利率','平台简介', '平台分类', '平台标签', '合作模式', '价格', '对接人', '联系方式'];

            for (var i in elArr) {
                if ($.trim(body.find('.' + elArr[i]).val()) == '') {
                    pageCommon.layerMsg(alertArr[i] + '不能为空', 2);
                    return false;
                }
            }
            if (photo == undefined) {
                pageCommon.layerMsg('logo不能为空', 2);
                return false;
            }
            if (isURL(platformUrl) == false) {
                pageCommon.layerMsg('网址不正确', 2);
                return false;
            }

            let arr = [];
            let obj = {
                name: tit,
                maxMoney: maximumBorrowable,
                dayRatio: dailyInterestRate,
                logo: photo,
                platformDesc: synopsis,
                typeId: classification,
                labelId: label,
                imgUrl: platformUrl,
                model: cooperationMode,
                money: platformPrice,
                dockPeople: pickUpPeople,
                phone: contactInformation
            };
            arr.push(obj);
            let url = globalAjaxUrl + '/admin/loanPlatform/addLoanPlatform';
            let data = {newData: JSON.stringify(arr)};
            pageCommon.postAjax(url, data, function (res) {
                pageCommon.layerMsg(res.msg, 1);
                layer.close(index);
                getPlatformManagement();
            });
        },
        cancel: function (index, layero) {
            layer.close(index);
        }
    });
});

// 编辑平台
function platformContentEdit(a){
    $(a).addClass('content-edit');
    let index = pageCommon.layerParentOpenIframe({
        url: globalUrl + '/view/popup/add-platform.html?field=edit',
        title: '编辑平台',
        confirm: function () {
            let body = parent.layer.getChildFrame('body', index),
                id = body.find('.add-platform').attr('data-id'),  // id
                tit = body.find('.platform-title').val(),  // 平台名称
                maximumBorrowable = body.find('.maximum-borrowable').val(),  // 最高可借
                dailyInterestRate = body.find('.daily-interest-rate').val(),  // 日利率
                photo = body.find('.platform-photo').attr('data-src'),   // 平台logo
                synopsis = body.find('.platform-synopsis').val(), // 平台简介
                classification = body.find('.platform-classification').val(),  // 平台分类
                label = body.find('.platform-label').val(),  // 平台标签
                platformUrl = body.find('.platform-url').val(), // 平台链接
                cooperationMode = body.find('.cooperation-mode').val(), // 合作模式
                platformPrice = body.find('.platform-price').val(), // 价格
                pickUpPeople = body.find('.platform-pick-up-people').val(), // 对接人
                contactInformation = body.find('.contact-information').val(); // 联系方式

            let elArr = ['platform-title','maximum-borrowable','daily-interest-rate', 'platform-synopsis', 'platform-classification', 'platform-label', 'cooperation-mode', 'platform-price', 'platform-pick-up-people', 'contact-information'];
            let alertArr = ['平台名称','最高可借', '日利率','平台简介', '平台分类', '平台标签', '合作模式', '价格', '对接人', '联系方式'];

            for (var i in elArr) {
                if ($.trim(body.find('.' + elArr[i]).val()) == '') {
                    pageCommon.layerMsg(alertArr[i] + '不能为空', 2);
                    return false;
                }
            }
            if (photo == undefined) {
                pageCommon.layerMsg('logo不能为空', 2);
                return false;
            }
            if (isURL(platformUrl) == false) {
                pageCommon.layerMsg('网址不正确', 2);
                return false;
            }

            let arr = [];
            let obj = {
                id:id,
                name: tit,
                maxMoney: maximumBorrowable,
                dayRatio: dailyInterestRate,
                logo: photo,
                platformDesc: synopsis,
                typeId: classification,
                labelId: label,
                imgUrl: platformUrl,
                model: cooperationMode,
                money: platformPrice,
                dockPeople: pickUpPeople,
                phone: contactInformation
            };
            arr.push(obj);
            let url = globalAjaxUrl + '/admin/loanPlatform/updateLoanPlatform';
            let data = {newData: JSON.stringify(arr)};
            pageCommon.postAjax(url, data, function (res) {
                pageCommon.layerMsg(res.msg, 1);
                layer.close(index);
                $(a).removeClass('content-edit');
                getPlatformManagement();
            });
        },
        cancel: function (index, layero) {
            layer.close(index);
            $(a).removeClass('content-edit');
        }
    });
}


// 删除平台
function platformContentRemove(a) {
    pageCommon.layerConfirm(function () {
        let id = $(a).attr('data-id');
        let labelid = $(a).parents('li').attr('data-labelid');
        let typeid = $(a).parents('li').attr('data-typeid');
        let url = globalAjaxUrl + '/admin/loanPlatform/deleteLoanPlatform?loanPlatformId='+ id+'&typeId='+ typeid+'&labelId='+ labelid;
        pageCommon.getAjax(url, {},function (res) {
            pageCommon.layerMsg(res.msg, 1);
            getPlatformManagement();
        });
    });

}


// 建议的正则
function isURL(str) {
    return !!str.match(/(((^https?:(?:\/\/)?)(?:[-;:&=\+\$,\w]+@)?[A-Za-z0-9.-]+|(?:www.|[-;:&=\+\$,\w]+@)[A-Za-z0-9.-]+)((?:\/[\+~%\/.\w-_]*)?\??(?:[-\+=&;%@.\w_]*)#?(?:[\w]*))?)$/g);
}





$('.platform-label>ul').on('mouseenter', 'li', function () {
    $(this).find('.fa-chevron-down').show();
});

$('.platform-label>ul').on('mouseleave', 'li', function () {
    $(this).find('.platform-label-option,.fa-chevron-down').hide();
});

$('.platform-label').on('mouseenter', '.platform-label-down', function () {
    $(this).next().show();
});


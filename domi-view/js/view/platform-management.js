let table = layui.table;
//  平台布局元素计算
function platformResize() {
    var modelH = $('.platform-content>ul').outerWidth(true); //获取宽度
    $('.platform-content>ul>li').css('width', (modelH / 3 - 20));  // 因为 li 是要 3个为一排加上外边框
    $('.platform-content').perfectScrollbar('update');
}

setTimeout(() => {
    platformResize();
}, 10);

getPlatformManagement();
// 页面初始化
function getPlatformManagement() {
    let url = globalAjaxUrl + '/admin/loanPlatform/getLoanPlatform';
    pageCommon.getAjax(url, {}, getPlatformManagementSuccess);
}

function getPlatformManagementSuccess(res) {
    $('.platform-label>ul').find('.add-li').remove();
    if (res.msg) {
        for (let j = 0; j < res.type.length; j++) {
            let label = `<li data-id="${res.type[j].id}" class="add-li">
                            <div class="platform-label-tit">
                                <span class="platform-label-tit-span">${res.type[j].name}</span>
                                <input type="text" class="platform-label-tit-ipt">
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



table.render({
    elem: '#channel-data-content-table'
    , even: true //开启隔行背景
    , method: 'GET'
    , limits: [10, 20, 30, 50, 100, 200]
    , limit: 10 //注意：请务必确保 limit 参数（默认：10）是与你服务端限定的数据条数一致 //支持所有基础参数
    , url: globalAjaxUrl + '/admin/channel/getChannelData'
    , where: {
        beginDate: '',
        endDate: '',
        name: ''
    }
    , cols: [[
        {field: 'channelName', width: '20%', title: '产品名称', align: 'center'}
        , {field: 'id', title: 'ID', align: 'center', hide: true}
        , {field: 'register', title: 'icon', align: 'center'}
        , {field: 'activation', title: '导流单价', align: 'center'}
        , {field: 'cooperationMode', title: '推荐分类', align: 'center'}
        , {field: 'price', title: '更新时间', align: 'center'}
        ,{fixed: 'right',width: '15%', title:'操作', toolbar: '#barDemo', align: 'center'}
    ]]
    , page: true
    , done: function (res, curr, count) {
        $('.layui-table-main').perfectScrollbar(); //数据渲染完成后的回调
    }
});


//监听行工具事件
table.on('tool(channel-management-table)', function(obj){
    var data = obj.data;
    if(obj.event === 'del'){
        layer.confirm('确定删除嘛？', function(index){
            let url = globalAjaxUrl + '/admin/loanPlatform/deleteLoanPlatform?loanPlatformId='+ id+'&typeId='+ typeid+'&labelId='+ labelid;
            pageCommon.getAjax(url, {},function (res) {
                pageCommon.layerMsg(res.msg, 1);
                getPlatformManagement();
            });
        });
    } else if(obj.event === 'edit'){
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
});



//分类的切换
$('.platform-label>ul').on('click', 'li', function () {
    let len = $(this).parent().find('li').length;
    let index = $(this).index();
    let id = $(this).attr('data-id');
    if (len - index != 1 && index !=0) {
        $(this).addClass('platform-active').siblings().removeClass('platform-active');
        table.reload('channel-data-content-table', {
            url: globalAjaxUrl + '/admin/channel/getChannelData'
            , cols: [[
                {field: 'channelName', width: '20%', title: '产品名称', align: 'center'}
                , {field: 'id', title: 'ID', align: 'center', hide: true}
                , {field: 'register', title: 'icon', align: 'center'}
                , {field: 'activation', title: '导流单价', align: 'center'}
                , {field: 'cooperationMode', title: '排序',edit: 'text', align: 'center'}
                , {field: 'price', title: '更新时间', align: 'center'}
                ,{fixed: 'right',width: '15%', title:'操作', toolbar: '#barDemo', align: 'center'}
            ]]
        });
    }else {
        $(this).addClass('platform-active').siblings().removeClass('platform-active');
        table.reload('channel-data-content-table',{
            url: globalAjaxUrl + '/admin/channel/getChannelData'
            , cols: [[
            {field: 'channelName', width: '20%', title: '产品名称', align: 'center'}
            , {field: 'id', title: 'ID', align: 'center', hide: true}
            , {field: 'register', title: 'icon', align: 'center'}
            , {field: 'activation', title: '导流单价', align: 'center'}
            , {field: 'cooperationMode', title: '推荐分类', align: 'center'}
            , {field: 'price', title: '更新时间', align: 'center'}
            ,{fixed: 'right',width: '15%', title:'操作', toolbar: '#barDemo', align: 'center'}
        ]]
        });
    }
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
            if (platformUrl == '') {
                pageCommon.layerMsg('网址不能为空', 2);
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
        })
    });

});



$('.platform-label>ul').on('mouseenter', 'li', function () {
    $(this).find('.fa-chevron-down').show();
});

$('.platform-label>ul').on('mouseleave', 'li', function () {
    $(this).find('.platform-label-option,.fa-chevron-down').hide();
});

$('.platform-label').on('mouseenter', '.platform-label-down', function () {
    $(this).next().show();
});
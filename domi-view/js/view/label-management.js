
// 页面初始化
getLabelManagement();
function getLabelManagement() {
    let url = globalAjaxUrl + '/admin/loanPlatform/getLoanPlatformLabel';
    pageCommon.getAjax(url, {}, getLabelManagementSuccess);
}

function getLabelManagementSuccess(res) {
    $('.label-management-label>ul').find('.add-li').remove();
    for (let j = 0; j < res.label.length; j++) {
        let label = `<li data-id="${res.label[j].id}" class="add-li">
                            <div class="platform-label-tit">
                                <span class="label-management-label-tit-span">${res.label[j].name}</span>
                                <input type="text" class="label-management-label-tit-ipt">
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


let table1 = layui.table;


table1.render({
    elem: '#label-management-content-table'
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
        {type: 'checkbox'}
        , {field: 'channelName', width: '20%', title: '渠道名', align: 'center'}
        , {field: 'id', title: 'ID', align: 'center', hide: true}
        , {field: 'register', title: '注册', align: 'center'}
        , {field: 'activation', title: '激活', align: 'center'}
        , {field: 'cooperationMode', title: '合作模式', align: 'center'}
        , {field: 'price', title: '价格', align: 'center'}
        , {field: 'type', title: '类型', align: 'center'}
    ]]
    , page: true
    , done: function (res, curr, count) {
        $('.layui-table-main').perfectScrollbar(); //数据渲染完成后的回调
        $('.total-registration>span').text(res.countJson.registerCount); // 注册总数
        $('.total-activation>span').text(res.countJson.activationCount); // 激活总数
    }
});

//标题栏的切换
$('.label-management-label>ul').on('click', 'li', function () {
    let len = $(this).parent().find('li').length;
    let index = $(this).index();
    let id = $(this).attr('data-id');
    if (len - index != 1 && index !=0) {
        $(this).addClass('label-management-active').siblings().removeClass('label-management-active');
        table1.reload('label-management-content-table', {
            url: globalAjaxUrl + '/admin/channel/getChannelData'
            , cols: [[
                {field: 'channelName', width: '20%', title: '产品名称', align: 'center'}
                , {field: 'id', title: 'ID', align: 'center', hide: true}
                , {field: 'register', title: 'icon', align: 'center'}
                , {field: 'activation', title: '导流价格', align: 'center'}
                , {field: 'cooperationMode', title: '排序', align: 'center'}
                , {field: 'price', title: '更新时间', align: 'center'}
                , {field: 'type', title: '操作', align: 'center'}
            ]]
        });
    }else {
        table1.reload('label-management-content-table');
        $(this).addClass('label-management-active').siblings().removeClass('label-management-active');
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



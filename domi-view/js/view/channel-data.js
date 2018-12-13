let start = '', end = '', laydate = layui.laydate, table = layui.table;
//日期范围
laydate.render({
    elem: '.channel-data-start',
    range: true,
    max: 0,
    done: function (value, date, endDate) {
        if (value) {
            let start = date.year + '-' + date.month + '-' + date.date,
                end = endDate.year + '-' + endDate.month + '-' + endDate.date,
                val = $('.data-management-name').val();
            table.reload('channel-data-content-table', {
                url: globalAjaxUrl + '/admin/channel/getChannelData'
                , where: {
                    beginDate: start,
                    endDate: end,
                    name: val
                }
            });
        } else {
            table.reload('channel-data-content-table', {
                url: globalAjaxUrl + '/admin/channel/getChannelData'
                , where: {
                    beginDate: '',
                    endDate: '',
                    name: ''
                }
            });
        }

    }
});


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


// 回车键搜索
document.onkeydown = function (e) {
    if (e.key == 'Enter') {
        let val = $('.data-management-name').val();
        table.reload('channel-data-content-table', {
            url: globalAjaxUrl + '/admin/channel/getChannelData'
            , where: {
                beginDate: '',
                endDate: '',
                name: val
            }
        });
    }
};


// 重置
$('.data-management-empty').click(function () {
    $('.channel-data-start').val('');
    $('.data-management-name').val('');
    table.reload('channel-data-content-table', {
        url: globalAjaxUrl + '/admin/channel/getChannelData'
        , where: {
            beginDate: '',
            endDate: '',
            name: ''
        }
    });
});


// 全部导出
$('.channel-data-export-all').click(function () {
    let url = window.location.href = globalAjaxUrl + '/admin/channel/exportXls?beginDate=' + start + '&endDate=' + end;
    pageCommon.getAjax(url, {}, function (res) {
        console.log(res);
    });
});

// 批量导出
$('.channel-data-batch-export').click(function () {
    let check = table.checkStatus('channel-data-content-table'); //idTest 即为基础参数 id 对应的值

    let len = check.data.length;

    if (!len) {
        pageCommon.layerMsg('请选择要导出的内容', 2);
        return false;
    }
    let idArr = [];

    for (let i = 0; i < len; i++) {
        idArr.push(check.data[i].id);
    }

    let obj = {
        beginDate: start,
        endDate: end,
        arr: idArr
    };
    let url = globalAjaxUrl + '/admin/channel/batchChannelExportXls';
    pageCommon.postExcelFile(obj, url)

});


$('.management-option-date>p').click(function () {
    $(this).addClass('active').siblings().removeClass('active');
    let time = $(this).attr('data-time');
    if (time != 'all') {
        table.reload('channel-data-content-table', {
            url: globalAjaxUrl + '/admin/channel/getTimeData'
            , where: {
                beginDate: '',
                endDate: '',
                name: '',
                time:time
            }
        });
    } else {
        table.reload('channel-data-content-table', {
            url: globalAjaxUrl + '/admin/channel/getChannelData'
            , where: {
                beginDate: '',
                endDate: '',
                name: ''
            }
        });
    }

});

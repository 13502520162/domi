let start = '', end = '', laydate = layui.laydate, table = layui.table;
laydate.render({
    elem: '.data-management-start',
    range: true,
    max: 0,
    done: function (value, date, endDate) {
        if (value) {
            let arr = value.split(' - ');
            let start = arr[0],
                end =  arr[1];
            let val = $('.data-management-name').val();
            table.reload('data-management-content-table', {
                url: globalAjaxUrl + '/admin/loanPlatform/getLoanPlatformData'
                , where: {
                    beginDate: start,
                    endDate: end,
                    name: val
                }
            });
        } else {
            table.reload('data-management-content-table', {
                url: globalAjaxUrl + '/admin/loanPlatform/getLoanPlatformData'
                , where: {
                    beginDate: '',
                    endDate: '',
                    name: ''
                }
            });
        }

    }
});



document.onkeydown = function (e) {
    if (e.key == 'Enter') {
        let val = $('.data-management-name').val();
        table.reload('data-management-content-table', {
            url: globalAjaxUrl + '/admin/loanPlatform/getLoanPlatformData'
            , where: {
                beginDate: '',
                endDate: '',
                name: val
            }
        });
    }
};

;

table.render({
    elem: '#data-management-content-table'
    , even: true //开启隔行背景
    , method: 'GET'
    , limits: [10, 20, 30, 50, 100, 200]
    , limit: 10 //注意：请务必确保 limit 参数（默认：10）是与你服务端限定的数据条数一致 //支持所有基础参数
    , url: globalAjaxUrl + '/admin/loanPlatform/getLoanPlatformData'
    , where: {
        beginDate: '',
        endDate: '',
        name: ''
    }
    , cols: [[
        {type: 'checkbox'}
        , {field: 'productName', width: '20%', title: '产品名称', align: 'center'}
        , {field: 'id', title: 'ID', align: 'center', hide: true}
        , {field: 'creationTime', width: '20%', title: '创建时间', align: 'center'}
        , {field: 'displayData', title: '展示数据', align: 'center'}
        , {field: 'clickData', title: '点击数据', align: 'center'}
        , {field: 'conversionRate', width: '20%', title: '转化率', align: 'center'}
    ]]
    , page: true
    , done: function (res, curr, count) {
        $('.layui-table-main').perfectScrollbar(); //数据渲染完成后的回调
    }
});

// 全部导出
$('.data-management-export-all').click(function () {
    let value = $('.data-management-start').val();
    let arr = value.split(' - ');
    start = arr[0];
    end = arr[1];
    let url = window.location.href = globalAjaxUrl + '/admin/loanPlatform/exportXls?beginDate=' + start + '&endDate=' + end;
    pageCommon.getAjax(url, {}, function (res) {
        console.log(res);
    });
});

// 批量导出
$('.data-management-batch-export').click(function () {
    let check = table.checkStatus('data-management-content-table'); // 即为基础参数 id 对应的值
    let len = check.data.length;
    if (!len) {
        pageCommon.layerMsg('请选择要导出的内容', 2);
        return false;
    }
    let idArr = [];
    for (let i = 0; i < len; i++) {
        console.log(check.data[i].id);
        idArr.push(check.data[i].id);
    }
    let obj = {
        beginDate: start,
        endDate: end,
        arr: idArr
    };
    let url = globalAjaxUrl + '/admin/loanPlatform/batchExportXls';
    pageCommon.postExcelFile(obj, url)
});


$('.management-option-date>p').click(function () {
    $(this).addClass('active').siblings().removeClass('active');
    let time = $(this).attr('data-time');
    let dateVal;
    if (time != 'all') {
        if (time == '1') {
            dateVal = pageCommon.getTimeForMat();
            $('.data-management-start').val(dateVal.start + ' - ' + dateVal.end);
        } else if (time == '-1') {
            dateVal = pageCommon.getTimeForMat(1);
            $('.data-management-start').val(dateVal.start + ' - ' + dateVal.start);
        } else {
            dateVal = pageCommon.getTimeForMat(time);
            $('.data-management-start').val(dateVal.start + ' - ' + dateVal.end);
        }
        table.reload('data-management-content-table', {
            url: globalAjaxUrl + '/admin/loanPlatform/getTimeCount'
            , where: {
                beginDate: '',
                endDate: '',
                name: '',
                time: time
            }
        });
    } else {
        table.reload('data-management-content-table', {
            url: globalAjaxUrl + '/admin/loanPlatform/getLoanPlatformData'
            , where: {
                beginDate: '',
                endDate: '',
                name: ''
            }

        });
        $('.data-management-start').val('');
    }
});
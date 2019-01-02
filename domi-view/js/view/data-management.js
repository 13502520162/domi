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
                    name: ''
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
                page:1,
                limit:100,
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
        , {field: 'platformDataName',  title: '产品名称', align: 'center'}
        , {field: 'id', title: 'ID', align: 'center', hide: true}
        , {field: 'date', title: '创建时间', align: 'center'}
       /* , {field: 'displayData', title: '展示数据', align: 'center'}*/
        , {field: 'clickPeople',width: '20%',  title: '点击数据', align: 'center'}
        , { title: '点击分布',width:'15%', templet:'#clickDistribution', align: 'center'}
/*        , {field: 'conversionRate', width: '20%', title: '转化率', align: 'center'}*/
    ]]
    , page: true
    ,parseData: function(res){ //将原始数据解析成 table 组件所规定的数据
        return {
            "code": res.data.code, //解析接口状态
            "msg": res.info, //解析提示文本
            "count": res.data.count, //解析数据长度
            "data": res.data.data //解析数据列表
        };
    }
    , done: function (res, curr, count) {
        $('.layui-table-main').perfectScrollbar(); //数据渲染完成后的回调
    }
});

table.on('tool(data-management-content-table)',function (obj) {
    let data = obj.data;
    if (obj.event == 'view'){
        let index = pageCommon.layerParentOpenIframe({
            url: globalUrl + '/view/popup/click-distribution.html?id='+data.id+'&name='+ data.productName,
            title: '查看点击分布',
            btn:['关闭'],
            shadeClose:true,
            area:['800px','520px'],
            confirm: function (index, layero) {
                parent.layer.close(index);
            },
            success:function (layero, index) {
                $(layero[0]).find('.layui-layer-btn').remove();
            }
        });
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


// 天数筛选
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
            },parseData: function(res){ //将原始数据解析成 table 组件所规定的数据
                return {
                    "code": res.data.code, //解析接口状态
                    "msg": res.info, //解析提示文本
                    "count": res.data.count, //解析数据长度
                    "data": res.data.data //解析数据列表
                };
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
            ,parseData: function(res){ //将原始数据解析成 table 组件所规定的数据
                return {
                    "code": res.data.code, //解析接口状态
                    "msg": res.info, //解析提示文本
                    "count": res.data.count, //解析数据长度
                    "data": res.data.data //解析数据列表
                };
            }

        });
        $('.data-management-start').val('');
    }
});

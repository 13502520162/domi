let start = '', end = '', laydate = layui.laydate, table = layui.table;


//日期范围
laydate.render({
    elem: '.date-start',
    range: true,
    max: 0,
    done: function (value, date, endDate) {
        if (value) {
            let arr = value.split(' - ');
            let start = arr[0],
                end =  arr[1];
            table.reload('user-list', {
                url: globalAjaxUrl + '/admin/userData/getUserList'
                , where: {
                    beginDate: start,
                    endDate: end
                }
            });
        } else {
            table.reload('user-list', {
                url: globalAjaxUrl + '/admin/userData/getUserList'
                , where: {
                    beginDate: '',
                    endDate: '',
                    page:1
                }
            });
        }

    }
});

table.render({
    elem: '#user-list'
    , method: 'GET'
    , limits: [10, 20, 30, 50, 100, 200]
    , limit: 10 //注意：请务必确保 limit 参数（默认：10）是与你服务端限定的数据条数一致 //支持所有基础参数
    , url: globalAjaxUrl + '/admin/userData/getUserList'
    ,height: 'full-130'
    , where: {
        beginDate: '',
        endDate: ''
    }
    , cols: [[
        {field: 'phone',  title: '用户号码', align: 'center'}
        , {field: 'id', title: 'ID', align: 'center',hide:true}
        , {field: 'registrationTime',title: '注册时间', align: 'center'}
        , {field: 'activationTime', title: '激活时间', align: 'center'}
        , {field: 'loginTime', title: '最近登录时间', align: 'center'}
    ]]
    , page: true
    , done: function (res, curr, count) {
        $('.layui-table-main').perfectScrollbar(); //数据渲染完成后的回调
    }
    ,parseData: function(res){ //将原始数据解析成 table 组件所规定的数据
        return {
            "code": res.data.code, //解析接口状态
            "msg": res.info, //解析提示文本
            "count": res.data.count, //解析数据长度
            "data": res.data.userDatas //解析数据列表
        };
    }
});

$('.management-option-date>p').click(function () {
    $(this).addClass('active').siblings().removeClass('active');
    let time = $(this).attr('data-time');
    let dateVal;
    let beginDate,endDate;
    if (time != 'all') {
        if (time == '1') {
            dateVal = pageCommon.getTimeForMat();
            $('.date-start').val(dateVal.start + ' - ' + dateVal.end);
            beginDate = dateVal.start;
            endDate = dateVal.end;
        } else if (time == '-1') {
            dateVal = pageCommon.getTimeForMat(1);
            $('.date-start').val(dateVal.start + ' - ' + dateVal.start);
            beginDate = dateVal.start;
            endDate = dateVal.end;
        } else {
            dateVal = pageCommon.getTimeForMat(time);
            $('.date-start').val(dateVal.start + ' - ' + dateVal.end);
            beginDate = dateVal.start;
            endDate = dateVal.end;
        }
        table.reload('user-list', {
            url:  globalAjaxUrl + '/admin/userData/getUserList'
            , where: {
                beginDate: beginDate,
                endDate:endDate
            }
        });
    } else {
        table.reload('user-list', {
            url: globalAjaxUrl + '/admin/userData/getUserList'
            , where: {
                beginDate: '',
                endDate: '',
                page:1
            }
        });
    }

});


document.onkeydown = function (e) {
    if (e.key == 'Enter') {
        let val = $('.photo-number').val();
        table.reload('user-list', {
            url: globalAjaxUrl + '/admin/userData/getUserList'
            , where: {
                beginDate: '',
                endDate: '',
                page:1,
                limit:100,
                phone: val
            }
        });
    }
};


// 导出用户
$('.channel-data-export-all').click(function () {
    let url = window.location.href = globalAjaxUrl + '/admin/userData/exportXls?beginDate=' + start + '&endDate=' + end;
    pageCommon.getAjax(url, {}, function (res) {
        console.log(res);
    });
});

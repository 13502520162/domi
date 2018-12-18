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
                    endDate: ''
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
});

$('.management-option-date>p').click(function () {
    $(this).addClass('active').siblings().removeClass('active');
    let time = $(this).attr('data-time');
    if (time != 'all') {
        table.reload('user-list', {
            url: globalAjaxUrl + '/admin/userData/getTimeCount'
            , where: {
                beginDate: '',
                endDate: '',
                time: time
            }
        });
    } else {
        table.reload('user-list', {
            url: globalAjaxUrl + '/admin/userData/getTimeCount'
            , where: {
                beginDate: '',
                endDate: ''
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
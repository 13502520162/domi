let start = '', end = '', table = layui.table;

table.render({
    elem: '#user-list'
    , method: 'GET'
    , limits: [10, 20, 30, 50, 100, 200]
    , limit: 10 //注意：请务必确保 limit 参数（默认：10）是与你服务端限定的数据条数一致 //支持所有基础参数
    , url: globalAjaxUrl + '/admin/employee/getUserFeedBack'
    ,height: 'full-130'
    , cols: [[
        {field: 'phone',  title: '用户号码', align: 'center'}
        , {field: 'id', title: 'ID', align: 'center',hide:true}
        , {field: 'dateTime',title: '反馈时间', align: 'center'}
        , {field: 'content', title: '反馈内容', align: 'center'}
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
            "data": res.data.feedbacks //解析数据列表
        };
    }
});

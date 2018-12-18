let table = layui.table
    ,form = layui.form;

table.render({
    elem: '#popular-management-table'
    , even: true //开启隔行背景
    , method: 'GET'
    , limits: [10, 20, 30, 50, 100, 200]
    , limit: 10 //注意：请务必确保 limit 参数（默认：10）是与你服务端限定的数据条数一致 //支持所有基础参数
    , url: '/domi/domi-view/js/view/JsonTest/popular-management.json'
    , height: 'full-25'
    , cols: [[
        {field: 'popularNames', title: '名称', align: 'center'}
        , {field: 'id', title: 'ID', align: 'center', hide: true}
        , {field: 'operationTime', title: '操作时间', align: 'center'}
        , {field: 'state', width: '20%', title: '状态',templet: '#state', align: 'center'}
        , {field: 'roofPlacement', width: '20%', title: '置顶',templet: '#roofPlacement',  align: 'center'}
    ]]
    , page: true
    , done: function (res, curr, count) {
        $('.layui-table-main').perfectScrollbar(); //数据渲染完成后的回调
        $('tbody').find('[data-field="roofPlacement"]').find('input').removeAttr('disabled');
        form.render();
    }
});

//监听状态的操作
form.on('switch(state)', function(obj){
    layer.tips(this.name + '：'+ obj.elem.checked, obj.othis);
    if (obj.elem.checked){
        $(obj.elem).parents('tr').find('[data-field="roofPlacement"]').find('input').removeAttr('disabled');
    } else {
        $(obj.elem).parents('tr').find('[data-field="roofPlacement"]').find('input').attr('disabled','disabled');

        $(obj.elem).parents('tr').find('[data-field="roofPlacement"]').find('input').prop('checked',false)
    }
    form.render();
});

//监听置顶的操作
form.on('switch(roofPlacement)', function(obj){
    layer.tips(this.name + '：'+ obj.elem.checked, obj.othis);
});
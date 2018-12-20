let table = layui.table
    , form = layui.form;
table.render({
    elem: '#popular-management-table'
    , even: true //开启隔行背景
    , method: 'GET'
    , limits: [10, 20, 30, 50, 100, 200]
    , limit: 10 //注意：请务必确保 limit 参数（默认：10）是与你服务端限定的数据条数一致 //支持所有基础参数
    , url: globalAjaxUrl + '/admin/loanPlatform/getRoof'
    , height: 'full-25'
    , cols: [[
        {field: 'name', title: '名称', align: 'center'}
        , {field: 'id', title: 'ID', align: 'center', hide: true}
        , {field: 'hotTime', title: '操作时间', align: 'center'}
        , {field: 'hotState', width: '20%', title: '状态', templet: '#state', align: 'center'}
        , {field: 'oneState', width: '20%', title: '置顶', templet: '#roofPlacement', align: 'center'}
    ]]
    , page: true
    , done: function (res, curr, count) {
        $('.layui-table-main').perfectScrollbar(); //数据渲染完成后的回调
        for (let i = 0; i < res.data.length; i++) {
            if (res.data[i].hotState) {
                $('tbody').find('[data-field="oneState"]').eq(i).find('input').removeAttr('disabled');
                form.render();
            }
        }
    }
});

//监听状态的操作
form.on('switch(hotState)', function (obj) {
   /* layer.tips(this.name + '：' + obj.elem.checked, obj.othis);*/
    let arr = [];
    let id = obj.elem.attributes[3].value;
    if (obj.elem.checked) {
        $(obj.elem).parents('tr').find('[data-field="roofPlacement"]').find('input').removeAttr('disabled');
        let obj1 = {
            id: parseInt(id),
            hotState: true,
            oneState: false
        };
        arr.push(obj1);

    } else {
        $(obj.elem).parents('tr').find('[data-field="roofPlacement"]').find('input').attr('disabled', 'disabled');
        $(obj.elem).parents('tr').find('[data-field="roofPlacement"]').find('input').prop('checked', false);
        let obj1 = {
            id: parseInt(id),
            hotState: false,
            oneState: false
        };
        arr.push(obj1);
    }

    let url = globalAjaxUrl + '/admin/loanPlatform/Roof';
    let data = {newData: JSON.stringify(arr)};
    pageCommon.postAjax(url, data, function (res) {
        pageCommon.layerMsg(res.msg, 1);
        table.reload('popular-management-table');
        form.render();
    });
});

//监听置顶的操作
form.on('switch(oneState)', function (obj) {
    let arr = [];
    let id = obj.elem.attributes[3].value;
    if (obj.elem.checked) {
        let obj1 = {
            id: parseInt(id),
            hotState: true,
            oneState: true
        };
        arr.push(obj1);

    } else {
        let obj1 = {
            id: parseInt(id),
            hotState: true,
            oneState: false
        };
        arr.push(obj1);
    }

    let url = globalAjaxUrl + '/admin/loanPlatform/Roof';
    let data = {newData: JSON.stringify(arr)};
    pageCommon.postAjax(url, data, function (res) {
        pageCommon.layerMsg(res.msg, 1);
        table.reload('popular-management-table');
        form.render();
    });

  /*  layer.tips(this.name + '：' + obj.elem.checked, obj.othis);*/
});
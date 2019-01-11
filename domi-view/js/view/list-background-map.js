let table = layui.table;

table.render({
    elem: '#list-background-map-table'
    , method: 'GET'
    , limits: [10, 20, 30, 50, 100, 200]
    , limit: 10 //注意：请务必确保 limit 参数（默认：10）是与你服务端限定的数据条数一致 //支持所有基础参数
    , url: globalAjaxUrl + '/admin/icon/getIcon'
    , cols: [[
        {field: 'id', title: 'ID', align: 'center'}
        , {field: 'name', title: '名称', align: 'center'}
        , {field: 'background', title: '背景图', templet: '#background', align: 'center'}
        , {field: 'dateTime', title: '操作时间', align: 'center'}
        , {fixed: 'right', title: '操作', toolbar: '#barDemo', align: 'center'}
    ]]
    , done: function (res, curr, count) {
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

//监听行工具事件
table.on('tool(list-background-map-table)', function (obj) {
    var data = obj.data;
    if (obj.event === 'edit') {
        console.log(data);
        let index = pageCommon.layerParentOpenIframe({
            url: globalUrl + '/view/popup/editorial-category-background.html?background='+data.background+'&name='+ data.name,
            title: '编辑图片',
            area: ['800px', '330px'],
            confirm: function () {
                let body = parent.layer.getChildFrame('body', index);
                let photo = body.find('.article-photo').attr('data-src'); // banner
                if ($.trim(photo) == '') {
                    pageCommon.layerMsg('图片不能为空', 2);
                    return false;
                }

                let obj = {
                    id: data.id,
                    background: photo
                };

                let url = globalAjaxUrl + '/admin/icon/addBackground';
                pageCommon.postAjax(url, JSON.stringify(obj), function (res) {
                    if (res.errcode===0) {
                        parent.layer.close(index);
                        pageCommon.layerMsg(res.info, 1);
                        table.reload('list-background-map-table');
                    } else {
                        pageCommon.layerMsg(res.info, 2);
                        return false;
                    }

                });
            },
            cancel: function (index, layero) {
                parent.layer.close(index);
            },
            success:function (layero, index) {
            }
        });
    }
});

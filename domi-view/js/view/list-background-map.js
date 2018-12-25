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
        , {field: 'updateTime', title: '操作时间', align: 'center'}
        , {fixed: 'right', title: '操作', toolbar: '#barDemo', align: 'center'}
    ]]
    , done: function (res, curr, count) {
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
                let arr = [];
                let obj = {
                    id: data.id,
                    background: photo
                };
                arr.push(obj);
                let post = {newData: JSON.stringify(arr)};
                let url = globalAjaxUrl + '/admin/icon/addBackground';
                pageCommon.postAjax(url, post, function (res) {
                    if (!res.state) {
                        pageCommon.layerMsg('编辑失败', 2);
                        return false;
                    } else {
                        parent.layer.close(index);
                        pageCommon.layerMsg('编辑成功', 1);
                        table.reload('list-background-map-table');
                    }

                });
            },
            cancel: function (index, layero) {
                parent.layer.close(index);
            }
        });
    }
});

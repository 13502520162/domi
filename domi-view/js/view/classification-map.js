let table = layui.table;
let permissionValue = pageCommon.getPermissionValue();
table.render({
    elem: '#classification-map-table'
    , method: 'GET'
    , limits: [10, 20, 30, 50, 100, 200]
    , limit: 10 //注意：请务必确保 limit 参数（默认：10）是与你服务端限定的数据条数一致 //支持所有基础参数
    , url: globalAjaxUrl + '/admin/icon/getIcon'
    , cols: [[
        {field: 'id', title: 'ID', align: 'center'}
        , {field: 'name', title: '名称', align: 'center'}
        , {field: 'imgUrl', title: '图片', templet: '#imgUrl', align: 'center'}
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
table.on('tool(classification-map-table)', function (obj) {
    var data = obj.data;
    if (obj.event === 'edit') {
        if (!permissionValue.edit){
            pageCommon.layerMsg('你没有权限编辑',2);
            return false;
        }
        let index = pageCommon.layerParentOpenIframe({
            url: globalUrl + '/view/popup/editorial-category-background.html?photoUrl='+data.imgUrl+'&name='+ data.name,
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
                    imgUrl: photo
                };
                let url = globalAjaxUrl + '/admin/icon/addLoanPlatform';
                pageCommon.postAjax(url, JSON.stringify(obj), function (res) {
                    if (!res.state) {
                        pageCommon.layerMsg('编辑失败', 2);
                        return false;
                    } else {
                        parent.layer.close(index);
                        pageCommon.layerMsg('编辑成功', 1);
                        table.reload('classification-map-table');
                    }

                });
            },
            cancel: function (index, layero) {
                parent.layer.close(index);
            }
        });
    }
});

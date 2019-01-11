let table = layui.table;
let permissionValue = pageCommon.getPermissionValue();
table.render({
    elem: '#rotation-chart-table'
    , method: 'GET'
    , limits: [10, 20, 30, 50, 100, 200]
    , limit: 10 //注意：请务必确保 limit 参数（默认：10）是与你服务端限定的数据条数一致 //支持所有基础参数
    , url: globalAjaxUrl + '/admin/banner/getBanners'
    , cols: [[
        {field: 'imgUrl', title: 'banner图', width: '15%', templet: '#imgUrl', align: 'center'}
        , {field: 'id', title: 'ID', align: 'center', hide: true}
        , {field: 'background', title: 'banner背景图', width: '15%', templet: '#background', align: 'center'}
        , {field: 'url', title: 'banner链接', align: 'center'}
        , {field: 'sort', title: '排序', edit: 'text', width: '10%', align: 'center'}
        , {field: 'dateTime', width: '13%', title: '操作时间', align: 'center'}
        , {fixed: 'right', title: '操作', toolbar: '#barDemo', width: '15%', align: 'center'}
    ]]
    , page: true
    ,loading:true
    , done: function (res, curr, count) {
        $('.layui-table-main').perfectScrollbar(); //数据渲染完成后的回调
    },parseData: function(res){ //将原始数据解析成 table 组件所规定的数据
        return {
            "code": res.errcode, //解析接口状态
            "msg": res.message, //解析提示文本
            "count": res.data.count, //解析数据长度
            "data": res.data.list //解析数据列表
        };
    }
});

//监听行工具事件
table.on('tool(rotation-chart-table)', function (obj) {
    var data = obj.data;
    if (obj.event === 'del') {
        if (!permissionValue.remove){
            pageCommon.layerMsg('你没有权限删除',2);
            return false;
        }
        layer.confirm('确定删除嘛？', function (index) {
            let url = globalAjaxUrl + '/admin/banner/delBanner?bannerId=' + data.id;
            pageCommon.getAjax(url, {}, function (res) {
                pageCommon.layerMsg(res.info, 1);
                obj.del();
                layer.close(index);
            });
        });
    } else if (obj.event === 'edit') {
        if (!permissionValue.edit){
            pageCommon.layerMsg('你没有权限编辑',2);
            return false;
        }
        $('.content-data').text(JSON.stringify(data));
        let index = pageCommon.layerParentOpenIframe({
            url: globalUrl + '/view/popup/add-photos.html?field=edit',
            title: '编辑图片',
            area: ['800px', '600px'],
            confirm: function () {
                let body = parent.layer.getChildFrame('body', index);
                let banner = body.find('.banner .article-photo').attr('data-src'); // banner
                let bannerBg = body.find('.bannerBg .article-photo').attr('data-src'); //  banner背景
                let bannerUrl = body.find('.banner-url').val();  // banner链接
                if ($.trim(banner) == '') {
                    pageCommon.layerMsg('banner图不能为空', 2);
                    return false;
                }
                if ($.trim(bannerBg) == '') {
                    pageCommon.layerMsg('banner背景不能为空', 2);
                    return false;
                }

                if ($.trim(bannerUrl) == '') {
                    pageCommon.layerMsg('banner链接不能为空', 2);
                    return false;
                }


                let obj = {
                    id: data.id,
                    imgUrl: banner,
                    background: bannerBg,
                    url: bannerUrl
                };
                let url = globalAjaxUrl + '/admin/banner/addBanner';
                pageCommon.postAjax(url, JSON.stringify(obj), function (res) {
                    if (res.errcode===0) {
                        parent.layer.close(index);
                        pageCommon.layerMsg(res.info, 1);
                        table.reload('rotation-chart-table');
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
    } else if (obj.event === 'view') {
        $('.content-data').text(JSON.stringify(data));
        let index = pageCommon.layerParentOpenIframe({
            url: globalUrl + '/view/popup/add-photos.html?field=view',
            title: '预览图片',
            area: ['800px', '600px'],
            btn:['关闭'],
            confirm: function (index, layero) {
                parent.layer.close(index);
            }
        });
    }
});


table.on('edit(rotation-chart-table)', function (obj) {
    let value = obj.value //得到修改后的值
        , data = obj.data //得到所在行所有键值
        , id = data.id;

    let url = globalAjaxUrl + '/admin/banner/bannerSort?id=' + id + '&sort=' + parseInt(value);
    pageCommon.getAjax(url, {}, function (res) {
        if (res.state) {
            pageCommon.layerMsg(res.info, 1);
            table.reload('rotation-chart-table');
        } else {
            pageCommon.layerMsg(res.info, 2);
            table.reload('rotation-chart-table');
        }
    });
});

//  新增图片
$('.new-photos').click(function () {
    $('#privilege-management-data').text('');
    let index = pageCommon.layerParentOpenIframe({
        url: globalUrl + '/view/popup/add-photos.html',
        title: '新增图片',
        area: ['800px', '600px'],
        confirm: function () {
            let body = parent.layer.getChildFrame('body', index);
            let banner = body.find('.banner .article-photo').attr('data-src'); // banner
            let bannerBg = body.find('.bannerBg .article-photo').attr('data-src'); //  banner背景
            let bannerUrl = body.find('.banner-url').val();  // banner链接
            if ($.trim(banner) == '') {
                pageCommon.layerMsg('banner图不能为空', 2);
                return false;
            }
            if ($.trim(bannerBg) == '') {
                pageCommon.layerMsg('banner背景不能为空', 2);
                return false;
            }

            if ($.trim(bannerUrl) == '') {
                pageCommon.layerMsg('banner链接不能为空', 2);
                return false;
            }


            let obj = {
                imgUrl: banner,
                background: bannerBg,
                url: bannerUrl
            };

            let url = globalAjaxUrl + '/admin/banner/addBanner';
            pageCommon.postAjax(url, JSON.stringify(obj), function (res) {
                if (res.errcode===0) {
                    parent.layer.close(index);
                    pageCommon.layerMsg(res.info, 1);
                    table.reload('rotation-chart-table');
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
});

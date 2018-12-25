let table = layui.table;

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
        , {field: 'updateTime', width: '13%', title: '操作时间', align: 'center'}
        , {fixed: 'right', title: '操作', toolbar: '#barDemo', width: '15%', align: 'center'}
    ]]
    , page: true
    , done: function (res, curr, count) {
        $('.layui-table-main').perfectScrollbar(); //数据渲染完成后的回调
    }
});

//监听行工具事件
table.on('tool(rotation-chart-table)', function (obj) {
    var data = obj.data;
    if (obj.event === 'del') {
        layer.confirm('确定删除嘛？', function (index) {
            let url = globalAjaxUrl + '/admin/banner/delBanner?bannerId=' + data.id;
            pageCommon.getAjax(url, {}, function (res) {
                pageCommon.layerMsg(res.info, 1);
                obj.del();
                layer.close(index);
            });
        });
    } else if (obj.event === 'edit') {
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

                let arr = [];
                let obj = {
                    id: data.id,
                    imgUrl: banner,
                    background: bannerBg,
                    url: bannerUrl
                };
                arr.push(obj);
                let post = {newData: JSON.stringify(arr)};
                let url = globalAjaxUrl + '/admin/banner/addBanner';
                pageCommon.postAjax(url, post, function (res) {
                    if (!res.state) {
                        pageCommon.layerMsg('编辑失败', 2);
                        return false;
                    } else {
                        parent.layer.close(index);
                        pageCommon.layerMsg('编辑成功', 1);
                        table.reload('rotation-chart-table');
                    }

                });
            },
            cancel: function (index, layero) {
                parent.layer.close(index);
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
            pageCommon.layerMsg(res.msg, 1);
            table.reload('rotation-chart-table');
        } else {
            pageCommon.layerMsg(res.msg, 2);
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

            let arr = [];
            let obj = {
                imgUrl: banner,
                background: bannerBg,
                url: bannerUrl
            };
            arr.push(obj);
            let data = {newData: JSON.stringify(arr)};
            let url = globalAjaxUrl + '/admin/banner/addBanner';
            pageCommon.postAjax(url, data, function (res) {
                console.log(res);
                if (!res.state) {
                    pageCommon.layerMsg('添加失败', 2);
                    return false;
                } else {
                    parent.layer.close(index);
                    pageCommon.layerMsg('添加成功', 1);
                    table.reload('rotation-chart-table');
                }

            });

        },
        cancel: function (index, layero) {
            parent.layer.close(index);
        }
    });
});
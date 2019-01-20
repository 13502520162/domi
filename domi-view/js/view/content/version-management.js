let table = layui.table;


table.render({
    elem: '#version-management'
    , method: 'GET'
    , limits: [10, 20, 30, 50, 100, 200]
    , limit: 10 //注意：请务必确保 limit 参数（默认：10）是与你服务端限定的数据条数一致 //支持所有基础参数
    , url: globalAjaxUrl + '/admin/viewVersion'
    , cols: [[
        {field: 'currentVersion', width: '10%', title: '版本号', align: 'center'}
        , {field: 'id', title: 'ID', align: 'center', hide: true}
        , {field: 'downLoadUrl', width: '40%', title: '下载链接', align: 'center'}
        , {field: 'description', width: '30%', title: '更新描述', align: 'center'}
        , {field: 'isForceUpdate', title: '强制更新', align: 'center', 'templet': '#isForceUpdate'}
        , {fixed: 'right', title: '操作', toolbar: '#barDemo', align: 'center'}
    ]]
    , done: function (res, curr, count) {
        $('.layui-table-main').perfectScrollbar(); //数据渲染完成后的回调
    },
    where: {
        type: 2
    },
    parseData: function (res) { //将原始数据解析成 table 组件所规定的数据
        return {
            "code": res.errcode, //解析接口状态
            "msg": res.info, //解析提示文本
            "count": res.data.count, //解析数据长度
            "data": res.data.list //解析数据列表
        };
    }
});


//监听行工具事件
table.on('tool(version-management-table)', function (obj) {
    var data = obj.data;
    console.log(obj);
    if (obj.event === 'edit') {
        $('#version-management-data').text(JSON.stringify(data));
        let index = pageCommon.layerParentOpenIframe({
            url: globalUrl + '/view/popup/add-version-management.html?filed=edit',
            title: '编辑渠道',
            area: ['800px', '480px'],
            confirm: function (index, layero) {
                let body = parent.layer.getChildFrame('body', index);
                let appName = body.find('#app-name').val(); // app名字
                let versionNumber = body.find('.version-number').val(); // 版本号
                let downloadLinks = body.find('.download-links').val(); // 下载链接
                let updateDescription = body.find('.update-description').val();  // 更新描述
                let isForceUpdate = body.find('input[name="isForceUpdate"]:checked').val();  // 强制更新
                if ($.trim(versionNumber) == '') {
                    pageCommon.layerMsg('版本号不能为空', 2);
                    return false;
                }
                if ($.trim(downloadLinks) == '') {
                    pageCommon.layerMsg('下载链接不能为空', 2);
                    return false;
                }
                if ($.trim(updateDescription) == '') {
                    pageCommon.layerMsg('更新描述不能为空', 2);
                    return false;
                }

                let index1 = pageCommon.layerLoad(true);
                let url = globalAjaxUrl + '/admin/updateVersion?id='+obj.data.id+'&type=' + parseInt(appName) + '&currentVersion=' + parseInt(versionNumber) + '&downLoadUrl=' + downloadLinks + '&description=' + updateDescription + '&isForceUpdate=' + parseInt(isForceUpdate);
                pageCommon.getAjax(url, {}, function (res) {
                    if (res.errcode === 0) {
                        parent.layer.close(index);
                        pageCommon.layerMsg(res.info, 1);
                        table.reload('version-management');
                    } else {
                        parent.layer.close(index1);
                        pageCommon.layerMsg(res.info, 2);
                        return false;
                    }
                })

       /*         let obj = {
                    id: obj.id,
                    type: parseInt(appName),
                    currentVersion: parseInt(versionNumber),
                    downLoadUrl: downloadLinks,
                    description: updateDescription,
                    isForceUpdate: parseInt(isForceUpdate),
                };
                let url = globalAjaxUrl + '/admin/updateVersion';

                pageCommon.postAjax(url, JSON.stringify(obj), function (res) {
                    if (res.errcode === 0) {
                        table.reload('channel-management-table', {
                            url: globalAjaxUrl + 'version-management'
                        });
                        parent.layer.close(index);
                        pageCommon.layerMsg(res.info, 1);
                    } else {
                        pageCommon.layerMsg(res.info, 2);
                        return false;
                    }
                });*/
            },
            cancel: function (index, layero) {
                layer.close(index);
            },
            success: function () {

            }
        });
    }
});

//  创建渠道
$('.add-channel-management').click(function () {
    $('#channel-management-data').text('');
    let index = pageCommon.layerParentOpenIframe({
        url: globalUrl + '/view/popup/add-version-management.html',
        area: ['800px', '480px'],
        title: '创建渠道',
        confirm: function () {
            let body = parent.layer.getChildFrame('body', index);
            let appName = body.find('#app-name').val(); // app名字
            let versionNumber = body.find('.version-number').val(); // 版本号
            let downloadLinks = body.find('.download-links').val(); // 下载链接
            let updateDescription = body.find('.update-description').val();  // 更新描述
            let isForceUpdate = body.find('input[name="isForceUpdate"]:checked').val();  // 强制更新
            if ($.trim(versionNumber) == '') {
                pageCommon.layerMsg('版本号不能为空', 2);
                return false;
            }
            if ($.trim(downloadLinks) == '') {
                pageCommon.layerMsg('下载链接不能为空', 2);
                return false;
            }
            if ($.trim(updateDescription) == '') {
                pageCommon.layerMsg('更新描述不能为空', 2);
                return false;
            }
            let index1 = pageCommon.layerLoad(true);
            let url = globalAjaxUrl + '/admin/addVersion?type=' + parseInt(appName) + '&currentVersion=' + parseInt(versionNumber) + '&downLoadUrl=' + downloadLinks + '&description=' + updateDescription + '&isForceUpdate=' + parseInt(isForceUpdate);
            pageCommon.getAjax(url, {}, function (res) {
                if (res.errcode === 0) {
                    parent.layer.close(index);
                    pageCommon.layerMsg(res.info, 1);
                    table.reload('version-management');
                } else {
                    parent.layer.close(index1);
                    pageCommon.layerMsg(res.info, 2);
                    return false;
                }
            })

            /*    let obj = {
             type: parseInt(appName),
             currentVersion: parseInt(versionNumber),
             downLoadUrl: downloadLinks,
             description: updateDescription,
             isForceUpdate: parseInt(isForceUpdate),
         };*/
            /*  pageCommon.postAjax(url, JSON.stringify(obj), function (res) {
                  if (res.errcode===0){
                      table.reload('channel-management-table', {
                          url: globalAjaxUrl + 'version-management'
                      });
                      parent.layer.close(index);
                      pageCommon.layerMsg(res.info, 1);
                  } else {
                      pageCommon.layerMsg(res.info, 2);
                      return false;
                  }
              });*/
        },
        cancel: function (index, layero) {
            parent.layer.close(index);
        },
        success: function (layero, index) {
        }
    });
});

// 版本切换
$('.app-toggle ul li').click(function () {
    let type = $(this).attr('data-type');
    $(this).addClass('active').siblings().removeClass('active');

    table.reload('version-management', {
        url: globalAjaxUrl + '/admin/viewVersion'
        , where: {
            type
        }
    });
});


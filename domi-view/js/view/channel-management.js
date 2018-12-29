let table = layui.table;
let permissionValue = pageCommon.getPermissionValue();
table.render({
    elem: '#channel-management-table'
    , method: 'GET'
    , limits: [10, 20, 30, 50, 100, 200]
    , limit: 10 //注意：请务必确保 limit 参数（默认：10）是与你服务端限定的数据条数一致 //支持所有基础参数
    , url: globalAjaxUrl + '/admin/channel/getChannel'
    , cols: [[
        {field: 'channelName',  title: '渠道名', align: 'center'}
        , {field: 'id', title: 'ID', align: 'center',hide:true}
        , {field: 'accountName', title: '渠道账号', align: 'center'}
        , {field: 'password', title: '渠道密码', align: 'center'}
        , {field: 'url',width: '25%', title: '链接', align: 'center'}
        , {field: 'cooperationMode', title: '合作模式', align: 'center'}
        , {field: 'price', title: '价格', align: 'center'}
        , {field: 'type', title: '类型', align: 'center'}
        , {field: 'collectionAccount', title: '收款账号', align: 'center',hide:true}
        , {field: 'payee', title: '收款人', align: 'center',hide:true}
        ,{fixed: 'right',width: '15%', title:'操作', toolbar: '#barDemo', align: 'center'}
    ]]
    , page: true
    , done: function (res, curr, count) {
        $('.layui-table-main').perfectScrollbar(); //数据渲染完成后的回调
    }
});


//监听行工具事件
table.on('tool(channel-management-table)', function(obj){
    var data = obj.data;
    if(obj.event === 'del'){
        if (!permissionValue.remove){
            pageCommon.layerMsg('你没有权限删除',2);
            return false;
        }
        layer.confirm('确定删除嘛？', function(index){

            let url = globalAjaxUrl + '/admin/channel/deleteChannel?channelId='+data.id;
            pageCommon.getAjax(url, {}, function (res) {
                pageCommon.layerMsg(res.msg, 1);
                obj.del();
                layer.close(index);
            });
        });
    } else if(obj.event === 'edit'){
        if (!permissionValue.edit){
            pageCommon.layerMsg('你没有权限编辑',2);
            return false;
        }
        $('#channel-management-data').text(JSON.stringify(data));
        let index = pageCommon.layerParentOpenIframe({
            url: globalUrl + '/view/popup/add-channel-management.html?filed=edit',
            title: '编辑渠道',
            confirm: function (index, layero) {
                let body = parent.layer.getChildFrame('body', index);
                let name = body.find('.channel-name').val(); // 渠道名
                let account = body.find('.channel-account').val(); // 渠道账号
                let password = body.find('.channel-password').val(); // 渠道密码
                let id = body.find('.add-article').attr('data-id'); // id
                let channelUrl = body.find('.channel-url').val(); // 链接
                let cooperationMode = body.find('.cooperation-mode').val();  // 合作模式
                let channelPrice = body.find('.channel-price').val();  // 价格
                let type = body.find('.channel-type').val(); // 类型
                let collectionAccount = body.find('.collection-account').val(); // 收款账户
                let payee = body.find('.payee').val(); // 收款人
                if ($.trim(name) == '') {
                    pageCommon.layerMsg('渠道名不能为空', 2);
                    return false;
                }
                if ($.trim(account) == '') {
                    pageCommon.layerMsg('渠道账号不能为空', 2);
                    return false;
                }
                if ($.trim(password) == '') {
                    pageCommon.layerMsg('渠道密码不能为空', 2);
                    return false;
                }
                if ($.trim(cooperationMode) == '') {
                    pageCommon.layerMsg('合作模式不能为空', 2);
                    return false;
                }
                if ($.trim(channelPrice) == '') {
                    pageCommon.layerMsg('价格不能为空', 2);
                    return false;
                }
                if ($.trim(type) == '') {
                    pageCommon.layerMsg('类型不能为空', 2);
                    return false;
                }
                if ($.trim(collectionAccount) == '') {
                    pageCommon.layerMsg('收款账户不能为空', 2);
                    return false;
                }
                if ($.trim(payee) == '') {
                    pageCommon.layerMsg('收款人不能为空', 2);
                    return false;
                }

                let obj = {
                    id:id,
                    name: name,
                    accountName:account,
                    password:password,
                    model: cooperationMode,
                    money: channelPrice,
                    type:type,
                    receivables:collectionAccount,
                    receivablesPeople:payee
                };
                let url = globalAjaxUrl + '/admin/channel/updateChannel';

       /*         pageCommon.postAjax(url, data, function (res) {
                    pageCommon.layerMsg(res.msg, 1);
                    layer.close(index);
                    table.reload('channel-management-table', {
                        url: globalAjaxUrl + '/admin/channel/getChannel'
                    });
                },function () {

                },'','application/x-www-form-urlencoded; charset=UTF-8');*/

                pageCommon.postAjax(url, JSON.stringify(obj), function (res) {
                    pageCommon.layerMsg(res.msg, 1);
                    layer.close(index);
                    table.reload('channel-management-table', {
                        url: globalAjaxUrl + '/admin/channel/getChannel'
                    });
                    parent.layer.close(index);
                });
            },
            cancel: function (index, layero) {
                console.log(index);
                layer.close(index);
            }
        });
    } else if(obj.event === 'view'){
        $('#channel-management-data').text(JSON.stringify(data));
        let index = pageCommon.layerParentOpenIframe({
            url: globalUrl + '/view/popup/add-channel-management.html?filed=view',
            title: '预览渠道',
            btn:['关闭'],
            confirm: function (index, layero) {
                parent.layer.close(index);
            }
        });
    }
});

//  创建渠道
$('.add-channel-management').click(function () {
    if (!permissionValue.add){
        pageCommon.layerMsg('你没有权限创建',2);
        return false;
    }
    $('#channel-management-data').text('');
    let index = pageCommon.layerParentOpenIframe({
        url: globalUrl + '/view/popup/add-channel-management.html',
        title: '创建渠道',
        confirm: function () {
            let body = parent.layer.getChildFrame('body', index);
            let name = body.find('.channel-name').val(); // 渠道名
            let account = body.find('.channel-account').val(); // 渠道账号
            let password = body.find('.channel-password').val(); // 渠道密码
            let cooperationMode = body.find('.cooperation-mode').val();  // 合作模式
            let channelPrice = body.find('.channel-price').val();  // 价格
            let type = body.find('.channel-type').val(); // 类型
            let collectionAccount = body.find('.collection-account').val(); //收款账户
            let payee = body.find('.payee').val(); //收款人
            if ($.trim(name) == '') {
                pageCommon.layerMsg('渠道名不能为空', 2);
                return false;
            }
            if ($.trim(account) == '') {
                pageCommon.layerMsg('渠道账号不能为空', 2);
                return false;
            }
            if ($.trim(password) == '') {
                pageCommon.layerMsg('渠道密码不能为空', 2);
                return false;
            }
            if ($.trim(cooperationMode) == '') {
                pageCommon.layerMsg('合作模式不能为空', 2);
                return false;
            }
            if ($.trim(channelPrice) == '') {
                pageCommon.layerMsg('价格不能为空', 2);
                return false;
            }
            if ($.trim(type) == '') {
                pageCommon.layerMsg('类型不能为空', 2);
                return false;
            }
            if ($.trim(collectionAccount) == '') {
                pageCommon.layerMsg('收款账户不能为空', 2);
                return false;
            }
            if ($.trim(payee) == '') {
                pageCommon.layerMsg('收款人不能为空', 2);
                return false;
            }

            let obj = {
                name: name,
                accountName:account,
                password:password,
                model: cooperationMode,
                money: channelPrice,
                type:type,
                receivables:collectionAccount,
                receivablesPeople:payee
            };
            let url = globalAjaxUrl + '/admin/channel/addChannel';

            pageCommon.postAjax(url, JSON.stringify(obj), function (res) {

               if (!res.state){
                   pageCommon.layerMsg(res.msg, 2);
                   return false;
               } else {
                   table.reload('channel-management-table', {
                       url: globalAjaxUrl + '/admin/channel/getChannel'
                   });
                   parent.layer.close(index);
                   pageCommon.layerMsg(res.msg, 1);
               }
            });
        },
        cancel: function (index, layero) {
            parent.layer.close(index);
        }
    });
});


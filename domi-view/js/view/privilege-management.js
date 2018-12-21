let table = layui.table;

table.render({
    elem: '#privilege-management-table'
    , method: 'GET'
    , limits: [10, 20, 30, 50, 100, 200]
    , limit: 10 //注意：请务必确保 limit 参数（默认：10）是与你服务端限定的数据条数一致 //支持所有基础参数
    , url: globalAjaxUrl + '/admin/employee/getEmployee'
    , cols: [[
        {field: 'name', width: '15%', title: '名称', align: 'center'}
        , {field: 'id', title: 'ID', align: 'center', hide: true}
        , {field: 'accountNumber', width: '10%', title: '账号', align: 'center'}
        , {field: 'password', title: '密码', width: '10%', align: 'center'}
        , {field: 'permission', title: '基本权限', align: 'center'}
        , {field: 'updateTime', width: '13%', title: '操作时间', align: 'center'}
        , {fixed: 'right', title: '操作', toolbar: '#barDemo', width: '15%', align: 'center'}
    ]]
    , page: true
    , done: function (res, curr, count) {
        $('.layui-table-main').perfectScrollbar(); //数据渲染完成后的回调
    }
});

//监听行工具事件
table.on('tool(privilege-management-table)', function (obj) {
    var data = obj.data;
    //console.log(obj)
    if (obj.event === 'del') {
        layer.confirm('确定删除嘛？', function (index) {
            let url = globalAjaxUrl + '/admin/employee/deleteEmployee?empId=' + data.id;
            pageCommon.getAjax(url, {}, function (res) {
                pageCommon.layerMsg(res.msg, 1);
                obj.del();
                layer.close(index);
            });
        });
    } else if (obj.event === 'edit') {
        $('#privilege-management-data').text(JSON.stringify(data));
        let index = pageCommon.layerParentOpenIframe({
            url: globalUrl + '/view/popup/add-privilege-management.html?empId=' + data.id,
            title: '编辑权限',
            area: ['800px', '520px'],
            confirm: function () {
                let body = parent.layer.getChildFrame('body', index);
                let id = body.find('.add-article').attr('data-id'); // id
                let name = body.find('.name').val(); // 名称
                let accountNumber = body.find('.account-number').val(); // 账号
                let password = body.find('.password').val();  // 密码
                if ($.trim(name) == '') {
                    pageCommon.layerMsg('名称不能为空', 2);
                    return false;
                }
                if ($.trim(accountNumber) == '') {
                    pageCommon.layerMsg('账号不能为空', 2);
                    return false;
                }
                if ($.trim(password) == '') {
                    pageCommon.layerMsg('密码不能为空', 2);
                    return false;
                }
                let arr = ['pictureManagement', 'loanPlatform', 'newsAndInformation', 'channelPromotion', 'platformManagement'];
                let newArr = [];
                for (let j = 0; j < arr.length; j++) {
                    let objFor = {
                        id: body.find('.' + arr[j]).attr('data-id'),
                        use: body.find('.' + arr[j] + ' .checkedAll').prop('checked'),
                        edit: body.find('.' + arr[j] + ' .edit').prop('checked'),
                        add: body.find('.' + arr[j] + ' .add').prop('checked'),
                        remove: body.find('.' + arr[j] + ' .remove').prop('checked')
                    };
                    newArr.push(objFor);
                }
                let obj = {
                    empId: id,
                    name: name,
                    accountNumber: accountNumber,
                    password: password,
                    arr: newArr
                };
                let data = {newData: JSON.stringify(obj)};
                let url = globalAjaxUrl + '/admin/employee/updateEmployee';
                pageCommon.postAjax(url, data, function (res) {
                    pageCommon.layerMsg(res.msg, 1);
                    parent.layer.close(index);
                    table.reload('privilege-management-table');
                });

            },
            cancel: function (index, layero) {
                parent.layer.close(index);
            }
        });
    }
});

//  新增权限
$('.add-privilege-management').click(function () {
    $('#privilege-management-data').text('');
    let index = pageCommon.layerParentOpenIframe({
        url: globalUrl + '/view/popup/add-privilege-management.html',
        title: '新增权限',
        area: ['800px', '520px'],
        confirm: function () {
            let body = parent.layer.getChildFrame('body', index);
            let name = body.find('.name').val(); // 名称
            let accountNumber = body.find('.account-number').val(); // 账号
            let password = body.find('.password').val();  // 密码
            if ($.trim(name) == '') {
                pageCommon.layerMsg('名称不能为空', 2);
                return false;
            }
            if ($.trim(accountNumber) == '') {
                pageCommon.layerMsg('账号不能为空', 2);
                return false;
            }
            console.log($.trim(password).length);
            if ($.trim(password) == '' || $.trim(password).length < 6) {
                pageCommon.layerMsg('密码不能为空并且长度应小于6位', 2);
                return false;
            }
            let arr = ['pictureManagement', 'loanPlatform', 'newsAndInformation', 'channelPromotion', 'platformManagement'];
            let newArr = [];
            for (let j = 0; j < arr.length; j++) {
                let objFor = {
                    id: body.find('.' + arr[j]).attr('data-id'),
                    use: body.find('.' + arr[j] + ' .checkedAll').prop('checked'),
                    edit: body.find('.' + arr[j] + ' .edit').prop('checked'),
                    add: body.find('.' + arr[j] + ' .add').prop('checked'),
                    remove: body.find('.' + arr[j] + ' .remove').prop('checked')
                };
                newArr.push(objFor);
            }
            let obj = {
                name: name,
                accountNumber: accountNumber,
                password: password,
                arr: newArr
            };
            let data = {newData: JSON.stringify(obj)};
            let url = globalAjaxUrl + '/admin/employee/addEmployee';
            pageCommon.postAjax(url, data, function (res) {
                console.log(res);
                if (!res.state) {
                    pageCommon.layerMsg(res.msg, 2);
                    return false;
                } else {
                    parent.layer.close(index);
                    pageCommon.layerMsg(res.msg, 1);
                    table.reload('privilege-management-table');
                }

            });

        },
        cancel: function (index, layero) {
            parent.layer.close(index);
        }
    });
});


// 搜索
$('.search').click(function () {
    search();
});

function search() {
    let val = $('.search-privilege-management').val();
    table.reload('privilege-management-table', {
        url: globalAjaxUrl + '/admin/employee/getEmployee'
        , where: {
            name: val
        }
    });
}


document.onkeydown = function (e) {
    if (e.key == 'Enter') {
        search();
    }
};
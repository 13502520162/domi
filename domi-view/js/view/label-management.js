
let table = layui.table;
let permissionValue = pageCommon.getPermissionValue();

function LabelManagement() {

}
LabelManagement.prototype = {
    constructor:LabelManagement,
    init:function () {
        this._table();
        this._toolEvent();
    },
    /**
     * 表格初始化
     * @private
     */
    _table:function(){
        let _this = this;
        table.render({
            elem: '#label-management-content-table'
            , even: true //开启隔行背景
            , method: 'GET'
            , limits: [10, 20, 30, 50, 100, 200]
            , limit: 10 //注意：请务必确保 limit 参数（默认：10）是与你服务端限定的数据条数一致 //支持所有基础参数
            , url: globalAjaxUrl + '/admin/loanPlatform/getLoanPlatformLabel'
            , cols: [[
                {field: 'name', width: '20%', title: '产品名称', align: 'center'}
                , {field: 'id', title: 'ID', align: 'center', hide: true}
                , {field: 'logo', title: 'logo', templet: '#logo', align: 'center'}
                , {field: 'money', title: '导流单价', align: 'center'}
                , {field: 'labelName', title: '推荐标签', align: 'center'}
                , {field: 'update', title: '更新时间', align: 'center'}
                , {fixed: 'right', width: '15%', title: '操作', toolbar: '#barDemo', align: 'center'}
            ]]
            , page: true
            , done: function (res, curr, count) {
                _this._getLabelManagementSuccess(res);
            }
        });
    },
    _getLabelManagementSuccess:function (res) {
        let liLen = $('.label-management-label>ul').find('.add-li').length;
        if (res.msg) {
            if (liLen != res.label.length) {
                for (let j = 0; j < res.label.length; j++) {
                    let label = `<li data-id="${res.label[j].id}" class="add-li">
                            <div class="platform-label-tit">
                                <span class="label-management-label-tit-span">${res.label[j].name}</span>
                                <input type="text" class="label-management-label-tit-ipt">
                            </div>
                            <span class="platform-label-down"><i class="fa fa-chevron-down"></i></span>
                            <div class="platform-label-option">
                                <p class="label-management-title-edit">编辑标签</p>
                                <p class="label-management-title-remove">删除标签</p>
                            </div>
                        </li>`;
                    $('.add-label-management-li').before(label);
                }
            }
        }
    },
    /**
     * 标签的选中
     * @param e
     * @private
     */
    _labelSelection:function (e) {
        let _this = this;
        let len = $(e).parent().find('li').length;
        let index = $(e).index();
        let id = $(e).attr('data-id');
        if (len - index != 1 && index != 0) {
            $(e).addClass('label-management-active').siblings().removeClass('label-management-active');
            table.reload('label-management-content-table', {
                url: globalAjaxUrl + '/admin/loanPlatform/getLoanPlatformByLabelId'
                , where: {
                    labelId: id
                }
                , cols: [[
                    {field: 'name', width: '20%', title: '产品名称', align: 'center'}
                    , {field: 'id', title: 'ID', align: 'center', hide: true}
                    , {field: 'logo', title: 'logo', templet: '#logo', align: 'center'}
                    , {field: 'money', title: '导流单价', align: 'center'}
                    , {field: 'labelName', title: '推荐标签', align: 'center'}
                    , {field: 'update', title: '更新时间', align: 'center'}
                    , {fixed: 'right', width: '15%', title: '操作', toolbar: '#barDemo', align: 'center'}
                ]]
            });
        } else {
            $(e).addClass('label-management-active').siblings().removeClass('label-management-active');
            _this._table();
        }
    },
    _toolEvent:function () {
        table.on('tool(label-management-content-table)', function (obj) {
            let data = obj.data;
            if (obj.event === 'del') {
                if (!permissionValue.remove){
                    pageCommon.layerMsg('你没有权限删除',2);
                    return false;
                }
                layer.confirm('确定删除嘛？', function (index) {
                    let url = globalAjaxUrl + '/admin/loanPlatform/deleteByLabel?loanPlatformId=' + data.id + '&labelId=' + data.labelId;
                    pageCommon.getAjax(url, {}, function (res) {
                        pageCommon.layerMsg(res.msg, 1);
                        obj.del();
                        layer.close(index);
                    });
                });
            }
        });
    }
};

let labelManagement = new LabelManagement();
labelManagement.init();





//标题栏的切换
$('.label-management-label>ul').on('click', 'li', function () {
    labelManagement._labelSelection(this);
});


// 标题编辑 ok
$('.label-management-label').on('click', '.label-management-title-edit', function (e) {

    if (!permissionValue.edit){
        pageCommon.layerMsg('你没有权限编辑',2);
        return false;
    }
    let span = $(this).parents('li').find('.label-management-label-tit-span');
    let input = $(this).parents('li').find('.label-management-label-tit-ipt');
    $(this).parents('.label-management-label-option').hide();
    let id = $(this).parents('li').attr('data-id');
    let text = span.text();
    span.hide();
    input.show();
    input.val(text).focus();
    document.onkeydown = function (e) {
        let ev = document.all ? window.event : e;
        if (ev.keyCode == 13) {
            span.text(input.val());
            span.show();
            input.hide();
            let arr = [];
            let obj = {
                name: input.val(),
                id: id
            };
            arr.push(obj);
            let url = globalAjaxUrl + '/admin/loanPlatform/updateLoanPlatformLabel';
            let data = {newData: JSON.stringify(arr)};
            pageCommon.postAjax(url, data, function (res) {
                pageCommon.layerMsg(res.msg, 1);
            });
        }
    }
});

// 标题删除 ok
$('.label-management-label').on('click', '.label-management-title-remove', function (e) {
    if (!permissionValue.remove){
        pageCommon.layerMsg('你没有权限删除',2);
        return false;
    }
    let $this = $(this);
    let id = $(this).parents('li').attr('data-id');
    let url = globalAjaxUrl + '/admin/loanPlatform/deleteLoanPlatformLabel?typeId=' + id;
    pageCommon.layerConfirm(function () {
        $this.parents('li').remove();
        pageCommon.getAjax(url, {}, function (res) {
            pageCommon.layerMsg(res.msg, 1);
            window.location.reload();
        })
    });
});


// 添加标题
$('.add-label-management-title').click(function () {
    if (!permissionValue.add){
        pageCommon.layerMsg('你没有权限添加',2);
        return false;
    }
    $('.label-management-title-ipt').show();
    $('.label-management-title-ipt').focus();
    document.onkeydown = function (e) {
        let ev = document.all ? window.event : e;
        if (ev.keyCode == 13) {
            let val = $.trim($('.label-management-title-ipt').val());
            if (val) {
                let html = ` <li>
                            <div class="label-management-label-tit">
                                <span class="label-management-label-tit-span">${val}</span>
                                <input type="text" class="label-management-label-tit-ipt">
                            </div>
                            <span class="label-management-label-down"><i class="fa fa-chevron-down"></i></span>
                            <div class="label-management-label-option">
                                <p class="label-management-title-edit">编辑标题</p>
                                <p class="label-management-title-remove">删除标题</p>
                            </div>
                        </li>`;


                let url = globalAjaxUrl + '/admin/loanPlatform/addLoanPlatformLabel';
                let arr = [];
                let obj = {
                    name: val
                };
                arr.push(obj);
                let data = {newData: JSON.stringify(arr)};
                pageCommon.postAjax(url, data, function (res) {
                    pageCommon.layerMsg(res.msg, 1);
                    $('.add-label-management-li').before(html);
                    $('.label-management-title-ipt').val('').hide();
                    window.location.reload();
                });
            } else {
                layer.msg('请输入标题名称')
            }
        }
    }
});



$('.label-management-label>ul').on('mouseenter', 'li', function () {
    $(this).find('.fa-chevron-down').show();
});

$('.label-management-label>ul').on('mouseleave', 'li', function () {
    $(this).find('.platform-label-option,.fa-chevron-down').hide();
});

$('.label-management-label').on('mouseenter', '.platform-label-down', function () {
    $(this).next().show();
});



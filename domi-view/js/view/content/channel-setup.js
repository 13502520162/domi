let laydate = layui.laydate,
    table = layui.table;
let permissionValue = pageCommon.getPermissionValue();
laydate.render({
    elem: '.date-start',
    range: true,
    max: 0,
    done: function (value, date, endDate) {
        if (value) {
            let start = date.year + '-' + date.month + '-' + date.date,
                end = endDate.year + '-' + endDate.month + '-' + endDate.date;
            table.reload('channel-setup-table', {
                url: globalAjaxUrl + '/admin/channel/getData'
                , where: {
                    beginDate: start,
                    endDate: end
                }
            });
        } else {
            table.reload('channel-setup-table');
        }

    }
});

//转换静态表格
table.init('channel-setup-table', {
    limit: 10
    , method: 'GET'
    , limits: [10, 20, 30, 50, 100, 200]
    , url: globalAjaxUrl + '/admin/channel/getData'
    , page: true
    , where: {
        beginDate: '',
        endDate: '',
        name:''
    }
    , done: function (res, curr, count) {
        $('.layui-table-main').perfectScrollbar(); //数据渲染完成后的回调
    },
    parseData: function(res){ //将原始数据解析成 table 组件所规定的数据
        $('.content-text-statistics-bottom').eq(0).find('.real-data .num').text(res.data.activationAndRegister.allRegisterCount);
        $('.content-text-statistics-bottom').eq(0).find('.display-data .num').text(res.data.activationAndRegister.allRegisterOut);
        $('.content-text-statistics-bottom').eq(1).find('.real-data .num').text(res.data.activationAndRegister.allActivationCount);
        $('.content-text-statistics-bottom').eq(1).find('.display-data .num').text(res.data.activationAndRegister.allActivationOut);
        $('.content-text-statistics-bottom').eq(2).find('.real-data .num').text(res.data.newAndOldUser.newUser);
        $('.content-text-statistics-bottom').eq(2).find('.display-data .num').text(res.data.newAndOldUser.oldUser);
        $('.content-text-statistics-top').eq(2).find('.num').text(res.data.newAndOldUser.newUser+res.data.newAndOldUser.oldUser);
        $('.content-text-statistics-bottom').eq(3).find('.real-data .num').text(res.data.clickPeople.newClick);
        $('.content-text-statistics-bottom').eq(3).find('.display-data .num').text(res.data.clickPeople.oldClick);
        $('.content-text-statistics-top').eq(3).find('.num').text(res.data.clickPeople.newClick+res.data.clickPeople.oldClick);
        return {
            "code": res.data.code, //解析接口状态
            "msg": res.info, //解析提示文本
            "count": res.data.count, //解析数据长度
            "data": res.data.channels //解析数据列表
        };
    }
});


// 回车键搜索
document.onkeydown = function (e) {
    if (e.key == 'Enter') {
        let val = $('.data-management-name').val();
        table.reload('channel-setup-table', {
            url: globalAjaxUrl + '/admin/channel/getData'
            , where: {
                beginDate: '',
                endDate: '',
                page:1,
                limit:10,
                name: val
            }
        });
    }
};


//监听工具条
table.on('tool(channel-setup-table)', function (obj) {
    let data = obj.data;
    if (obj.event === 'setUp') {  // 设置
        if (!permissionValue.add){
            pageCommon.layerMsg('你没有权限操作',2);
            return false;
        }
        let index = layer.open({
            type: 1,
            title:'设置',
            area: ['300px', '200px'], //宽高
            content: `
                <div class="is-statistics">
                    <div class="set-up">
                        <div class="set-up-left">
                            <p>进</p>
                            <input type="number" class="enter">
                         </div>
                           <div class="set-up-right" >
                            <p>扣</p>
                            <input type="number" class="buckle">
                        </div>
                    </div>
                    <div class="data-option">
                        <span class="open setup">设置扣量</span>
                        <span class="cancel">取消扣量</span>
                    </div>
                </div>
            `,
            success: function (layero, index) {
            }
        });


        // 设置扣量
        $(".setup").click(function () {
            let enter =  parseInt($.trim($('.enter').val()));
            let buckle = parseInt($.trim($('.buckle').val()));
            if (!enter || !buckle) {
                pageCommon.layerMsg('进或扣 值都不能为空',2);
                return false;
            }
            let url = globalAjaxUrl + '/admin/channel/setChannelDataRule';
            let objS = {
                id: obj.data.id,
                come: enter,
                abandon: buckle
            };
            pageCommon.postAjax(url, JSON.stringify(objS), function (res) {
                if (res.errcode===0){
                    pageCommon.layerMsg(res.info, 1);
                    layer.close(index);
                    table.reload('channel-setup-table');
                } else {
                    pageCommon.layerMsg(res.info, 2);
                    return false;
                }

            });
        });

        // 取消扣量
        $(".cancel").click(function () {
            let url = globalAjaxUrl + '/admin/channel/setChannelDataRule';
            let objS = {
                id: obj.data.id,
                come: 1,
                abandon: 0
            };
            pageCommon.postAjax(url, JSON.stringify(objS), function (res) {
                if (res.errcode===0){
                    pageCommon.layerMsg(res.info, 1);
                    layer.close(index);
                    table.reload('channel-setup-table');
                } else {
                    pageCommon.layerMsg(res.info, 2);
                    return false;
                }
            });
        });

    } else if (obj.event === 'del') { // 删除
        if (!permissionValue.remove){
            pageCommon.layerMsg('你没有权限删除',2);
            return false;
        }
        layer.confirm('你确定删除该渠道嘛？', function (index) {
            let url = globalAjaxUrl + '/admin/channel/delete?channelId=' + data.id;
            pageCommon.getAjax(url, {}, function (res) {
                pageCommon.layerMsg(res.msg, 1);
                obj.del();
                layer.close(index);
            });
        });
    }
    else if (obj.event === 'view'){
        let index = pageCommon.layerParentOpenIframe({
            url: globalUrl + '/view/popup/save-view.html?filed=view&id='+data.id,
            title: '留存查看',
            btn:['关闭'],
            shadeClose: true,
            area:['1250px', '700px'],
            confirm: function (index, layero) {
                layer.close(index);
            },
            success:function (layero, index) {
                $(layero[0]).find('.layui-layer-btn').remove();
            }
        });
    }
});


$('.management-option-date>p').click(function () {
    $(this).addClass('active').siblings().removeClass('active');
    $('.liActive').text($(this).text() +'用户统计');
    let time = $(this).attr('data-time');
    let dateVal;
    let beginDate,endDate;
    if (time != 'all') {
        if (time == '1') {
            dateVal = pageCommon.getTimeForMat();
            $('.date-start').val(dateVal.start + ' - ' + dateVal.end);
            beginDate = dateVal.start;
            endDate = dateVal.end;
        } else if (time == '-1') {
            dateVal = pageCommon.getTimeForMat(1);
            $('.date-start').val(dateVal.start + ' - ' + dateVal.start);
            beginDate = dateVal.start;
            endDate = dateVal.start;
        } else {
            dateVal = pageCommon.getTimeForMat(time);
            $('.date-start').val(dateVal.start + ' - ' + dateVal.end);
            beginDate = dateVal.start;
            endDate = dateVal.end;
        }

        table.reload('channel-setup-table', {
            url: globalAjaxUrl + '/admin/channel/getData'
            , where: {
                beginDate: beginDate,
                endDate:endDate,
                name: ''
            },
            parseData: function(res){ //将原始数据解析成 table 组件所规定的数据
                $('.content-text-statistics-bottom').eq(0).find('.real-data .num').text(res.data.activationAndRegister.allRegisterCount);
                $('.content-text-statistics-bottom').eq(0).find('.display-data .num').text(res.data.activationAndRegister.allRegisterOut);
                $('.content-text-statistics-bottom').eq(1).find('.real-data .num').text(res.data.activationAndRegister.allActivationCount);
                $('.content-text-statistics-bottom').eq(1).find('.display-data .num').text(res.data.activationAndRegister.allActivationOut);
                $('.content-text-statistics-bottom').eq(2).find('.real-data .num').text(res.data.newAndOldUser.newUser);
                $('.content-text-statistics-bottom').eq(2).find('.display-data .num').text(res.data.newAndOldUser.oldUser);
                $('.content-text-statistics-bottom').eq(3).find('.real-data .num').text(res.data.clickPeople.newClick);
                $('.content-text-statistics-bottom').eq(3).find('.display-data .num').text(res.data.clickPeople.oldClick);
                return {
                    "code": res.data.code, //解析接口状态
                    "msg": res.info, //解析提示文本
                    "count": res.data.count, //解析数据长度
                    "data": res.data.channels //解析数据列表
                };
            }
        });
    } else {
        table.reload('channel-setup-table', {
            url: globalAjaxUrl + '/admin/channel/getData'
            , where: {
                beginDate: '',
                endDate:'',
                name: ''
            }
        });
        $('.date-start').val('');
    }

});

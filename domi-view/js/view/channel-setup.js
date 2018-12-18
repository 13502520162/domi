let laydate = layui.laydate,
    table = layui.table;

let start = laydate.render({
    elem: '.date-start',
    range:true
});



//转换静态表格
table.init('channel-setup-table', {
    limit: 10
    , method: 'GET'
    , limits: [10, 20, 30, 50, 100, 200]
    , url: globalAjaxUrl + '/admin/channel/channelSetUp'
    , page: true
    , where: {
        time: 7
    }
    , done: function (res, curr, count) {
        $('.content-text-statistics-bottom').eq(0).find('.real-data .num').text(res.dataArray[0].registrationCount);
        $('.content-text-statistics-bottom').eq(0).find('.display-data .num').text(res.dataArray[0].registerOut);
        $('.content-text-statistics-bottom').eq(1).find('.real-data .num').text(res.dataArray[0].activationCount);
        $('.content-text-statistics-bottom').eq(1).find('.display-data .num').text(res.dataArray[0].activationOut);
        $('.content-text-statistics-bottom').eq(2).find('.real-data .num').text(res.dataArray[0].loginCount);
        $('.content-text-statistics-bottom').eq(2).find('.display-data .num').text(res.dataArray[0].loginOut);
        $('.layui-table-main').perfectScrollbar(); //数据渲染完成后的回调
    }
});


// 回车键搜索
document.onkeydown = function (e) {
    if (e.key == 'Enter') {
        let val = $('.data-management-name').val();
        table.reload('channel-setup-table', {
            url: globalAjaxUrl + '/admin/channel/channelSetUp'
            , where: {
                beginDate: '',
                endDate: '',
                name: val
            }
        });
    }
};


//监听工具条
table.on('tool(channel-setup-table)', function (obj) {
    let data = obj.data;
    if (obj.event === 'setUp') {  // 设置
        let index =  layer.open({
            type: 1,
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
                        <span class="close" style="display: none">关闭</span>
                        <span class="open" style="display: none">开启</span>
                        <span class="cancel">取消</span>
                    </div>
                </div>
            `,
            success:function (layero, index) {
                if (data.statistics == '无'){
                    $('#layui-layer'+ index).find('.data-option').prepend('<span class="open" style="">开启</span>')
                } else {
                    $('#layui-layer'+ index).find('.data-option').prepend('<span class="close" style="">关闭</span>')
                }
            }
        });


        // 关闭
        $(".close").click(function () {
            let url = globalAjaxUrl + '/admin/channel/SetUp';
            let objS = {
                id:obj.data.id,
                state:'0',
                in: parseInt($('.enter').val()) || 0,
                out: parseInt($('.buckle').val()) || 0
            };
            let data = {newData: JSON.stringify(objS)};
            pageCommon.postAjax(url, data, function (res) {
                pageCommon.layerMsg(res.msg, 1);
                layer.close(index);

                table.reload('channel-setup-table');
            });
        });

        // 开启
        $(".open").click(function () {
            let url = globalAjaxUrl + '/admin/channel/SetUp';
            let objS = {
                id:obj.data.id,
                state:'1',
                in: parseInt($('.enter').val()) || '',
                out: parseInt($('.buckle').val()) || ''
            };
            let data = {newData: JSON.stringify(objS)};
            pageCommon.postAjax(url, data, function (res) {
                pageCommon.layerMsg(res.msg, 1);
                layer.close(index);
                table.reload('channel-setup-table');
            });
        });


        // 取消
        $(".cancel").click(function () {
            layer.close(index);
        });

    } else if (obj.event === 'del') { // 删除
        layer.confirm('你确定删除该渠道嘛？', function (index) {
            obj.del();
            layer.close(index);
        });
    } else if (obj.event === 'dataStatistics') {  // 统计数据
      let index =  layer.open({
            type: 1,
            area: ['420px', '240px'], //宽高
            content: `
                <div class="is-statistics">
                    <div class="data-main">
                        <span data-type="1" class="active">统计数据</span>
                        <span data-type="2">不统计数据</span>
                    </div>
                    <div class="data-option">
                        <span class="confirm">确定</span>
                        <span class="cancel">取消</span>
                    </div>
                </div>
            `
        });

        $('.data-main span').click(function () {
            $(this).addClass('active').siblings().removeClass('active');
        });

        $(".confirm").click(function () {
            let val = $('.data-main').find('.active').attr('data-type');
            alert(val);
          layer.close(index);
        });

        $(".cancel").click(function () {
          layer.close(index);
        });
    } else if (obj.event === 'notDataStatistics') {  // 未统计数据
        let index =  layer.open({
            type: 1,
            area: ['420px', '240px'], //宽高
            content: `
                <div class="is-statistics">
                    <div class="data-main">
                        <span data-type="1">统计数据</span>
                        <span data-type="2" class="active">不统计数据</span>
                    </div>
                    <div class="data-option">
                        <span class="confirm">确定</span>
                        <span class="cancel">取消</span>
                    </div>
                </div>
            `
        });

        $('.data-main span').click(function () {
           $(this).addClass('active').siblings().removeClass('active');
        });

        $(".confirm").click(function () {
            let val = $('.data-main').find('.active').attr('data-type');
            alert(val);
            layer.close(index);
        });

        $(".cancel").click(function () {
            layer.close(index);
        });
    }
});


$('.management-option-date>p').click(function () {
    $(this).addClass('active').siblings().removeClass('active');
    let time = $(this).attr('data-time');
    let dateVal;
    if (time != 'all') {
        if (time == '1') {
            dateVal = pageCommon.getTimeForMat();
            $('.date-start').val(dateVal.start + ' - ' + dateVal.end);
        } else if (time == '-1') {
            dateVal = pageCommon.getTimeForMat(1);
            $('.date-start').val(dateVal.start + ' - ' + dateVal.start);
        } else {
            dateVal = pageCommon.getTimeForMat(time);
            $('.date-start').val(dateVal.start + ' - ' + dateVal.end);
        }

        table.reload('channel-setup-table', {
            url: globalAjaxUrl + '/admin/channel/channelSetUp'
            , where: {
                beginDate: '',
                endDate: '',
                name: '',
                time:time
            }
        });
    } else {
        table.reload('channel-setup-table', {
            url: globalAjaxUrl + '/admin/channel/channelSetUp'
            , where: {
                beginDate: '',
                endDate: '',
                name: ''
            }
        });
    }

});
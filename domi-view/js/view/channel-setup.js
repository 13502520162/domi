let laydate = layui.laydate,
    table = layui.table;

let start = laydate.render({
    elem: '.date-start',
    range:true
});



//转换静态表格
table.init('channel-setup-table', {
    limit: 10
    , method: 'POST'
    , limits: [10, 20, 30, 50, 100, 200]
    , url: '/domi/domi-view/js/view/JsonTest/channel-setup.json'
    , page: true
    , done: function (res, curr, count) {
        $('.layui-table-main').perfectScrollbar(); //数据渲染完成后的回调
    }
});


//监听工具条
table.on('tool(channel-setup-table)', function (obj) {
    var data = obj.data;
    if (obj.event === 'setUp') {  // 设置
        let index =  layer.open({
            type: 1,
            area: ['300px', '200px'], //宽高
            content: `
                <div class="is-statistics">
                    <div class="set-up">
                        <div class="set-up-left">
                            <p>进</p>
                            <input type="text" class="enter">
                         </div>
                           <div class="set-up-right" >
                            <p>扣</p>
                            <input type="text" class="buckle">
                        </div>
                    </div>
                    <div class="data-option">
                        <span class="close">关闭</span>
                 <!--       <span class="open fn-hide">开启</span>-->
                        <span class="cancel">取消</span>
                    </div>
                </div>
            `
        });


        // 关闭
        $(".close").click(function () {
            layer.close(index);
        });

        // 开启
        $(".open").click(function () {
            layer.close(index);
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
        /* data.state = '未扣量';
         table.reload('channel-setup-table');*/
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


// 查询
$('.query').click(function () {
    let start = $('.date-start').val(),
        end = $('.date-end').val();
    let obj = {
        start: start,
        end: end
    };
    alert(JSON.stringify(obj))
});

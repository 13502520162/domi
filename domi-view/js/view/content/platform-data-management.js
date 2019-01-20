let start = '', end = '', laydate = layui.laydate, table = layui.table, userArray = [], channelArray = [],
    dataArray = [];

// 日期选择
laydate.render({
    elem: '.date-start',
    range: true,
    max: 0,
    done: function (value, date, endDate) {
        if (value) {
            let arr = value.split(' - ');
            start = arr[0];
            end = arr[1];
            table.reload('content-table-statistics', {
                url: globalAjaxUrl + '/admin/channel/getBusinessData'
                , where: {
                    beginDate: start,
                    endDate: end
                }
            });
        } else {
            table.reload('content-table-statistics');
        }
    }
});

//  图表初始化
function eChartsInit(data) {
    let myChart = echarts.init(document.getElementById('main'));
    let option = {
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross',
                crossStyle: {
                    color: '#999'
                }
            }
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        toolbox: {
            feature: {
                dataView: {show: true, readOnly: false},
                magicType: {show: true, type: ['line', 'bar']},
                restore: {show: true},
                saveAsImage: {show: true}
            }
        },
        legend: {
            data: ['注册人数', '新增/激活人数', '活跃人数', '点击人数']
        },
        xAxis: [
            {
                type: 'category',
                data: data.xData,
                axisPointer: {
                    type: 'shadow'
                }
            }
        ],
        yAxis: [
            {
                type: 'value'
            }
        ],
        series: [
            {
                name: '注册人数',
                type: 'line',
                data: data.seriesData.register
            },
            {
                name: '新增/激活人数',
                type: 'line',
                data: data.seriesData.newActivation
            },
            {
                name: '活跃人数',
                type: 'line',
                data: data.seriesData.active
            },
            {
                name: '点击人数',
                type: 'line',
                data: data.seriesData.click
            }
        ]
    };
    myChart.setOption(option);
}

// 表格初始化
table.render({
    elem: '#content-table-statistics'
    , even: true //开启隔行背景
    , method: 'GET'
    , limits: [10, 20, 30, 50, 100, 200]
    , limit: 10 //注意：请务必确保 limit 参数（默认：10）是与你服务端限定的数据条数一致 //支持所有基础参数
    , url: globalAjaxUrl + '/admin/channel/getBusinessData'
    , where: {
        beginDate: '',
        endDate: ''
    }
    , cols: [[
        {type: 'checkbox'}
        , {field: 'date', title: '统计时间', align: 'center'}
        , {field: 'id', title: 'ID', align: 'center', hide: true}
        , {field: 'registrationCount', title: '注册人数', align: 'center'}
        , {field: 'activationCount', title: '新增/激活人数', align: 'center'}
        , {field: 'onlineCount', title: '活跃人数', align: 'center'}
        , {field: 'clickCount', title: '点击次数', align: 'center'}
    ]]
    , page: true
    , done: function (res, curr, count) {
        $('.layui-table-main').perfectScrollbar(); //数据渲染完成后的回调
    }
    , parseData: function (res) { //将原始数据解析成 table 组件所规定的数据
        dataInit(res.data.allData, res.data.allSum);
        return {
            "code": res.errcode, //解析接口状态
            "msg": res.info, //解析提示文本
            "count": res.data.count, //解析数据长度
            "data": res.data.allData //解析数据列表
        };
    }
});


function dataInit(data, sum) {

    let register = [];
    let newActivation = [];
    let active = [];
    let xData = [];
    let click = [];
    for (let i = 0; i < data.length; i++) {
        register.push(data[i].registrationCount);  // 注册
        newActivation.push(data[i].activationCount); // 新增/激活人数
        active.push(data[i].onlineCount); // 活跃人数
        click.push(data[i].clickCount); // 点击人数

        if (data[i].date) {

            xData.push(data[i].date); // 操作时间
        }else {
            xData.push(data[i].hour); // 操作时间
        }
    }
    $('.current-date').attr('data-start',data[0].date);
    $('.current-date').attr('data-end',data[data.length-1].date);

    //顶部数据
    $('.content-text-statistics-bottom .num').eq(0).text(sum.registrationCount);
    $('.content-text-statistics-bottom .num').eq(1).text(sum.activationCount);
    $('.content-text-statistics-bottom .num').eq(2).text(sum.onlineCount);
    $('.content-text-statistics-bottom .num').eq(3).text(sum.clickCount);


    // 图表数据
    let ChartData = {
        xData: xData,
        seriesData: {
            register: register,
            newActivation: newActivation,
            active: active,
            click: click,
        }
    };
    eChartsInit(ChartData); //  初始化图表
}


// 下拉数据改变
$('.content-main-sel').change(function () {
    let val = $(this).val();
    switch (val) {
        case '0':
            table.reload('content-table-statistics', {
                url: globalAjaxUrl + '/admin/channel/getBusinessData'
                , parseData: function (res) { //将原始数据解析成 table 组件所规定的数据
                    dataInit(res.data.natureData, res.data.natureSum);
                    return {
                        "code": res.errcode, //解析接口状态
                        "msg": res.info, //解析提示文本
                        "count": res.data.count, //解析数据长度
                        "data": res.data.natureData //解析数据列表
                    };
                }
            });
            $('.content-text-statistics-top .fr').text('自然量');
            break;
        case  '1':
            table.reload('content-table-statistics', {
                url: globalAjaxUrl + '/admin/channel/getBusinessData'
                , parseData: function (res) { //将原始数据解析成 table 组件所规定的数据
                    dataInit(res.data.publishData, res.data.publishSum);
                    return {
                        "code": res.errcode, //解析接口状态
                        "msg": res.info, //解析提示文本
                        "count": res.data.count, //解析数据长度
                        "data": res.data.publishData //解析数据列表
                    };
                }
            });
            $('.content-text-statistics-top .fr').text('推广量');
            break;
        default:
            table.reload('content-table-statistics', {
                url: globalAjaxUrl + '/admin/channel/getBusinessData'
                , parseData: function (res) { //将原始数据解析成 table 组件所规定的数据
                    dataInit(res.data.allData, res.data.allSum);
                    return {
                        "code": res.errcode, //解析接口状态
                        "msg": res.info, //解析提示文本
                        "count": res.data.count, //解析数据长度
                        "data": res.data.allData //解析数据列表
                    };
                }
            });
            $('.content-text-statistics-top .fr').text('全部来源');
    }
});


// 全部导出
$('.content-table-statistics-all').click(function () {
    let selVal = $('.content-main-sel').val();
    let value = $('.date-start').val();
    if (value) {
        let arr = value.split(' - ');
        start = arr[0];
        end = arr[1];
    } else {
        start = '';
        end = '';
    }
    let url = window.location.href = globalAjaxUrl + '/admin/channel/excelBusinessData?beginDate=' + start + '&endDate=' + end + '&sourceType=' + selVal;
    pageCommon.getAjax(url, {}, function (res) {
        console.log(res);
    });
});

// 导出当前页
$('.content-table-statistics-export').click(function () {
    let selVal = $('.content-main-sel').val();
    start = $('.current-date').attr('data-start');
    end = $('.current-date').attr('data-end');
    console.log(start);
    console.log(end);
    let url = window.location.href = globalAjaxUrl + '/admin/channel/excelBusinessData?beginDate=' + start + '&endDate=' + end + '&sourceType=' + selVal;
    pageCommon.getAjax(url, {}, function (res) {
        console.log(res);
    });
});


//  天数筛选
$('.management-option-date>p').click(function () {
    $(this).addClass('active').siblings().removeClass('active');
    $('.liActive').text($(this).text() + '用户统计');
    let time = $(this).attr('data-time');
    let dateVal;
    let beginDate, endDate;
    $('.content-main-sel').val('-1');
    currTime();
    if (time != 'all') {
        if (time == '1') {
            $('.content-table-statistics-export').hide();
            dateVal = pageCommon.getTimeForMat();
            $('.date-start').val(dateVal.start + ' - ' + dateVal.end);
            $('.time').parent().html('<p>截止今日<span  class="time"></span></p>');
            beginDate = dateVal.start;
            endDate = dateVal.end;
            currTime();
            table.reload('content-table-statistics', {
                url: globalAjaxUrl + '/admin/channel/getBusinessData'
                , where: {
                    beginDate:beginDate,
                    endDate: endDate
                }
                , cols: [[
                    {type: 'checkbox'}
                    , {field: 'hour', title: '统计时间', align: 'center'}
                    , {field: 'id', title: 'ID', align: 'center', hide: true}
                    , {field: 'registrationCount', title: '注册人数', align: 'center'}
                    , {field: 'activationCount', title: '新增/激活人数', align: 'center'}
                    , {field: 'onlineCount', title: '活跃人数', align: 'center'}
                    , {field: 'clickCount', title: '点击次数', align: 'center'}
                ]],
                page:false
            });
        } else if (time == '-1') {
            $('.content-table-statistics-export').hide();
            dateVal = pageCommon.getTimeForMat(1);
            $('.date-start').val(dateVal.start + ' - ' + dateVal.start);
            $('.time').parent().html('<p>截止昨日<span  class="time">24:00</span></p>')
            beginDate = dateVal.start;
            endDate = dateVal.start;
            table.reload('content-table-statistics', {
                url: globalAjaxUrl + '/admin/channel/getBusinessData'
                , where: {
                    beginDate:beginDate,
                    endDate: endDate
                }
                , cols: [[
                    {type: 'checkbox'}
                    , {field: 'hour', title: '统计时间', align: 'center'}
                    , {field: 'id', title: 'ID', align: 'center', hide: true}
                    , {field: 'registrationCount', title: '注册人数', align: 'center'}
                    , {field: 'activationCount', title: '新增/激活人数', align: 'center'}
                    , {field: 'onlineCount', title: '活跃人数', align: 'center'}
                    , {field: 'clickCount', title: '点击次数', align: 'center'}
                ]],
                page:false
            });
        } else {
            $('.content-table-statistics-export').show();
            dateVal = pageCommon.getTimeForMat(time);
            $('.date-start').val(dateVal.start + ' - ' + dateVal.end);
            $('.time').parent().html('<p>截止今日<span  class="time"></span></p>');
            beginDate = dateVal.start;
            endDate = dateVal.end;
            currTime();
            table.reload('content-table-statistics', {
                url: globalAjaxUrl + '/admin/channel/getBusinessData'
                , where: {
                    beginDate:beginDate,
                    endDate: endDate
                }
                , cols: [[
                    {type: 'checkbox'}
                    , {field: 'date', title: '统计时间', align: 'center'}
                    , {field: 'id', title: 'ID', align: 'center', hide: true}
                    , {field: 'registrationCount', title: '注册人数', align: 'center'}
                    , {field: 'activationCount', title: '新增/激活人数', align: 'center'}
                    , {field: 'onlineCount', title: '活跃人数', align: 'center'}
                    , {field: 'clickCount', title: '点击次数', align: 'center'}
                ]],
                page:true
            });
        }
    } else {
        $('.content-table-statistics-export').show();
        table.reload('content-table-statistics', {
            url: globalAjaxUrl + '/admin/channel/getBusinessData'
            , where: {
                beginDate: '',
                endDate: ''
            }
        });
        $('.date-start').val('');
    }
});


function currTime() {
    let date = new Date();
    let HH = date.getHours();
    let MM = date.getMinutes();
    $('.time').text(HH + ':' + MM);
}


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
                url: globalAjaxUrl + '/admin/channel/getDateData'
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
            data: ['注册人数', '新增/激活人数', '活跃人数']
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
    , url: globalAjaxUrl + '/admin/channel/getDateData'
    , where: {
        beginDate: '',
        endDate: ''
    }
    , cols: [[
        {type: 'checkbox'}
        , {field: 'dateTime', width: '20%', title: '统计时间', align: 'center'}
        , {field: 'id', title: 'ID', align: 'center', hide: true}
        , {field: 'registrationCount', width: '20%', title: '注册人数', align: 'center'}
        , {field: 'activationCount', title: '新增/激活人数', align: 'center'}
        , {field: 'loginCount', title: '活跃人数', align: 'center'}
    ]]
    , page: true
    , done: function (res, curr, count) {
        $('.layui-table-main').perfectScrollbar(); //数据渲染完成后的回调
    }
    ,parseData: function(res){ //将原始数据解析成 table 组件所规定的数据
        dataInit(res);
        return {
            "code": res.data.code, //解析接口状态
            "msg": res.info, //解析提示文本
            "count": res.data.count, //解析数据长度
            "data": res.data.data //解析数据列表
        };
    }
});


function dataInit(res) {
    res = res.data;
    let register = [];
    let newActivation = [];
    let active = [];
    let xData = [];
    userArray = [];
    channelArray = [];
    dataArray = [];
    for (let i = 0; i < res.data.length; i++) {
        register.push(res.data[i].registrationCount);  // 注册
        newActivation.push(res.data[i].activationCount); // 新增/激活人数
        active.push(res.data[i].loginCount); // 活跃人数
        xData.push(res.data[i].dateTime); // 操作时间
    }

    let data = {
        xData: xData,
        seriesData: {
            register: register,
            newActivation: newActivation,
            active: active
        }
    };
    eChartsInit(data); //  初始化图表

    $('.content-text-statistics-bottom .num').eq(0).text(res.HistoryData.registrationCount);
    $('.content-text-statistics-bottom .num').eq(1).text(res.HistoryData.activationCount);
    $('.content-text-statistics-bottom .num').eq(2).text(res.HistoryData.loginCount);
    console.log(res);
    userArray.push(res.userData);
    channelArray.push(res.channel);
    dataArray.push(res.data);
}


// 下拉数据改变
$('.content-main-sel').change(function () {
    let val = $(this).val();
    switch (val) {
        case '自然量':
            $('.content-text-statistics-bottom .num').eq(0).text(userArray[0].registrationCount);
            $('.content-text-statistics-bottom .num').eq(1).text(userArray[0].activationCount);
            $('.content-text-statistics-bottom .num').eq(2).text(userArray[0].loginCount);
            $('.content-text-statistics-top .fr').text('自然量');
            break;
        case  '推广量':
            $('.content-text-statistics-bottom .num').eq(0).text(channelArray[0].registrationCount);
            $('.content-text-statistics-bottom .num').eq(1).text(channelArray[0].activationCount);
            $('.content-text-statistics-bottom .num').eq(2).text(channelArray[0].loginCount);
            $('.content-text-statistics-top .fr').text('推广量');
            break;
        default:
            $('.content-text-statistics-bottom .num').eq(0).text(dataArray[0].registrationCount);
            $('.content-text-statistics-bottom .num').eq(1).text(dataArray[0].activationCount);
            $('.content-text-statistics-bottom .num').eq(2).text(dataArray[0].loginCount);
            $('.content-text-statistics-top .fr').text('全部来源');
    }
});


// 全部导出
$('.content-table-statistics-all').click(function () {
    let value = $('.date-start').val();
    let arr = value.split(' - ');
    start = arr[0];
    end = arr[1];
    let url = window.location.href = globalAjaxUrl + '/admin/channel/getExpt?beginDate=' + start + '&endDate=' + end;
    pageCommon.getAjax(url, {}, function (res) {
        console.log(res);
    });
});

// 批量导出
$('.content-table-statistics-export').click(function () {
    let check = table.checkStatus('content-table-statistics'); //idTest 即为基础参数 id 对应的值
    let len = check.data.length;

    if (!len) {
        pageCommon.layerMsg('请选择要导出的内容', 2);
        return false;
    }
    let Arr = [];

    for (let i = 0; i < len; i++) {
        Arr.push(check.data[i].dateTime);
    }

    let obj = {
        arr: Arr
    };

    let url = globalAjaxUrl + '/admin/channel/getBatchExpt';
    pageCommon.postExcelFile(obj, url)
});


//  天数筛选
let curDare = pageCommon.getTimeForMat();
$('.date-start').val(curDare.start + ' - ' + curDare.end);
$('.management-option-date>p').click(function () {
    $(this).addClass('active').siblings().removeClass('active');
    $('.liActive').text($(this).text() + '用户统计');
    let time = $(this).attr('data-time');
    let dateVal;
    let beginDate,endDate;
    $('.content-main-sel').val('全部来源');
    currTime();
    if (time != 'all') {
        if (time == '1') {
            dateVal = pageCommon.getTimeForMat();
            $('.date-start').val(dateVal.start + ' - ' + dateVal.end);
            $('.time').parent().html('<p>截止今日<span  class="time"></span></p>');
            beginDate = dateVal.start;
            endDate = dateVal.end;
            currTime();
        } else if (time == '-1') {
            dateVal = pageCommon.getTimeForMat(1);
            $('.date-start').val(dateVal.start + ' - ' + dateVal.start);
            $('.time').parent().html('<p>截止昨日<span  class="time">24:00</span></p>')
            beginDate = dateVal.start;
            endDate = dateVal.start;
        } else {
            dateVal = pageCommon.getTimeForMat(time);
            $('.date-start').val(dateVal.start + ' - ' + dateVal.end);
            $('.time').parent().html('<p>截止今日<span  class="time"></span></p>');
            beginDate = dateVal.start;
            endDate = dateVal.end;
            currTime();
        }

        table.reload('content-table-statistics', {
            url: globalAjaxUrl + '/admin/channel/getDateData'
            , where: {
                beginDate:beginDate,
                endDate: endDate
            }
        });
    } else {
        table.reload('content-table-statistics', {
            url: globalAjaxUrl + '/admin/channel/getDateData'
            , where: {
                beginDate:'',
                endDate: ''
            }
        });
        $('.date-start').val('');
    }
});

currTime();
function currTime(){
    let date = new Date();
    let HH = date.getHours();
    let MM = date.getMinutes();
    $('.time').text(HH+':'+MM);
}


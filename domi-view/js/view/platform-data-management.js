let start = '', end = '', laydate = layui.laydate, table = layui.table, userArray = [], channelArray = [],
    dataArray = [];

laydate.render({
    elem: '.date-start',
    range: true,
    max: 0,
    done: function (value, date, endDate) {
        if (value) {
            let arr = value.split(' - ');
            let start = arr[0],
                end = arr[1];
            table.reload('user-list', {
                url: globalAjaxUrl + '/admin/userData/getUserList'
                , where: {
                    beginDate: start,
                    endDate: end
                }
            });
        } else {
            table.reload('user-list', {
                url: globalAjaxUrl + '/admin/userData/getUserList'
                , where: {
                    beginDate: '',
                    endDate: ''
                }
            });
        }

    }
});


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
        /*   toolbox: {
               feature: {
                   dataView: {show: true, readOnly: false},
                   magicType: {show: true, type: ['line', 'bar']},
                   restore: {show: true},
                   saveAsImage: {show: true}
               }
           },*/
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
                type: 'value',
                name: '',
                min: 'dataMin',
                interval: 50,
                axisLabel: {
                    formatter: '{value}'
                }
            }
        ],
        series: [
            {
                name: '注册人数',
                type: 'bar',
                data: data.seriesData.register
            },
            {
                name: '新增/激活人数',
                type: 'bar',
                data: data.seriesData.newActivation
            },
            {
                name: '活跃人数',
                type: 'bar',
                data: data.seriesData.active
            }
        ]
    };
    myChart.setOption(option);
}


table.render({
    elem: '#content-table-statistics'
    , even: true //开启隔行背景
    , method: 'GET'
    , limits: [10, 20, 30, 50, 100, 200]
    , limit: 10 //注意：请务必确保 limit 参数（默认：10）是与你服务端限定的数据条数一致 //支持所有基础参数
    , url: globalAjaxUrl + '/admin/channel/getMyData'
    , where: {
        time: 7
    }
    , cols: [[
        {type: 'checkbox'}
        , {field: 'nowDate', width: '20%', title: '统计时间', align: 'center'}
        , {field: 'id', title: 'ID', align: 'center', hide: true}
        , {field: 'registerCount', width: '20%', title: '注册人数', align: 'center'}
        , {field: 'activationCount', title: '新增/激活人数', align: 'center'}
        , {field: 'loginCount', title: '活跃人数', align: 'center'}
    ]]
    , page: true
    , done: function (res, curr, count) {
        $('.layui-table-main').perfectScrollbar(); //数据渲染完成后的回调
        dataInit(res);
    }
});


function dataInit(res) {
    let register = [];
    let newActivation = [];
    let active = [];
    let xData = [];
    for (let i = 0; i < res.data.length; i++) {
        register.push(res.data[i].registerCount);  // 注册
        newActivation.push(res.data[i].activationCount); // 新增/激活人数
        active.push(res.data[i].loginCount); // 活跃人数
        xData.push(res.data[i].nowDate); // 操作时间
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


    $('.content-text-statistics-bottom .num').eq(0).text(res.dataArray[0].registerCount);
    $('.content-text-statistics-bottom .num').eq(1).text(res.dataArray[0].activationCount);
    $('.content-text-statistics-bottom .num').eq(2).text(res.dataArray[0].loginCount);
    userArray.push(res.userArray);
    channelArray.push(res.channelArray);
    dataArray.push(res.dataArray);

}


// 下拉数据改变
$('.content-main-sel').change(function () {
    let val = $(this).val();
    switch (val) {
        case '自然量':
            $('.content-text-statistics-bottom .num').eq(0).text(userArray[0][0].registerCount);
            $('.content-text-statistics-bottom .num').eq(1).text(userArray[0][0].activationCount);
            $('.content-text-statistics-bottom .num').eq(2).text(userArray[0][0].loginCount);
            break;
        case  '推广量':
            $('.content-text-statistics-bottom .num').eq(0).text(channelArray[0][0].registerCount);
            $('.content-text-statistics-bottom .num').eq(1).text(channelArray[0][0].activationCount);
            $('.content-text-statistics-bottom .num').eq(2).text(channelArray[0][0].loginCount);
            break;
        default:
            $('.content-text-statistics-bottom .num').eq(0).text(dataArray[0][0].registerCount);
            $('.content-text-statistics-bottom .num').eq(1).text(dataArray[0][0].activationCount);
            $('.content-text-statistics-bottom .num').eq(2).text(dataArray[0][0].loginCount);
    }
});


// 全部导出
$('.content-table-statistics-all').click(function () {
    alert('全部导出')
});

// 批量导出
$('.content-table-statistics-export').click(function () {
    let check = table.checkStatus('content-table-statistics'); //idTest 即为基础参数 id 对应的值
    let len = check.data.length;
    let idArr = [];
    for (let i = 0; i < len; i++) {
        console.log(check.data[i].id);
        idArr.push(check.data[i].id);
    }
    alert('选中的Id有：' + idArr);
});

$('.management-option-date>p').click(function () {
    $(this).addClass('active').siblings().removeClass('active');
});
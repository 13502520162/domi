let laydate = layui.laydate;

let start = laydate.render({
    elem: '.date-start'
    ,range: true
    ,max:0
});




function eChartsInit(xData,seriesData){
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
            data:['注册人数','新增/激活人数','活跃人数']
        },
        xAxis: [
            {
                type: 'category',
                data: xData,
                axisPointer: {
                    type: 'shadow'
                }
            }
        ],
        yAxis: [
            {
                type: 'value',
                name: '',
                min: 0,
                max: 250,
                interval: 50,
                axisLabel: {
                    formatter: '{value}'
                }
            }
        ],
        series: [
            {
                name:'注册人数',
                type:'bar',
                data:[2, 4, 7, 23, 25, 76, 135]
            },
            {
                name:'新增/激活人数',
                type:'bar',
                data:[6, 5, 9, 26, 28, 70, 175]
            },
            {
                name:'活跃人数',
                type:'bar',
                data:[2, 2, 3, 4, 6, 10, 20]
            }
        ]
    };
    myChart.setOption(option);
}

let xData = ['00:00', '04:00', '08:00', '12:00', '16:00', '20:00', '24:00'];
let seriesData = [820, 932, 901, 934, 1290, 1330, 1320];
eChartsInit(xData,seriesData);





// 查询
$('.data-management-query').click(function () {
    let val = $('.data-management-name').val(),
        start = $('.data-management-start').val(),
        end = $('.data-management-end').val();
    let obj = {
        val: val,
        start: start,
        end: end
    };
    alert(JSON.stringify(obj))
});


let table = layui.table;

table.render({
    elem: '#content-table-statistics'
    , even: true //开启隔行背景
    , method: 'POST'
    , limits: [10, 20, 30, 50, 100, 200]
    , limit: 10 //注意：请务必确保 limit 参数（默认：10）是与你服务端限定的数据条数一致 //支持所有基础参数
    , url: '/domi/domi-view/js/view/JsonTest/platform-data-management.json'
    , cols: [[
        {type: 'checkbox'}
        , {field: 'statisticalTime', width:'20%',title: '统计时间', align: 'center'}
        , {field: 'id', title: 'ID', align: 'center',hide:true}
        , {field: 'enrolment', width:'20%',  title: '注册人数', align: 'center'}
        , {field: 'newActivationNumber', title: '新增/激活人数', align: 'center'}
        , {field: 'activeNumber',   title: '活跃人数', align: 'center'}
    ]]
    , page: true
    ,done:function (res, curr, count) {
        $('.layui-table-main').perfectScrollbar(); //数据渲染完成后的回调
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

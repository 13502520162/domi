
//   菜单下拉
$('.personal').click(function () {
    $('.personal-settings').toggle();
});

// 修改密码
$('.change-password').click(function () {
    let content = `
            <div class="change-password">
                <div class="password">
                    <span class="fw dsl">原密码:</span>
                    <input type="text" placeholder="请输入原密码" class="password-ipt original-password">
                </div>
                <div class="password">
                    <span class="fw dsl">新密码:</span>
                    <input type="text" placeholder="请输入新密码"  class="password-ipt new-password">
                </div>
                <div class="password">
                    <span class="fw dsl">确认密码:</span>
                    <input type="text" placeholder="确认输入新密码" class="password-ipt confirm-new-password">
                </div>
                <div class="password-opi">
                    <span class="confirm dsl">确认</span>
                    <span class="cancel dsl">取消</span>
                </div>
            </div>`;
    let index = layer.open({
        type: 1,
        area: ['300px', '230px'], //宽高
        content: content
    });

    // 确认
    $('.confirm').click(function () {
        let originalPassword = $(".original-password").val();
        let newPassword = $(".new-password").val();
        let confirmNewPassword = $(".confirm-new-password").val();
        if ($.trim(originalPassword)== '') {
            pageCommon.layerMsg('原密码不能为空', 2);
            return false;
        }
        if ($.trim(newPassword) == '') {
            pageCommon.layerMsg('请输入新密码', 2);
            return false;
        }
        if ($.trim(confirmNewPassword) =='') {
            pageCommon.layerMsg('确认输入新密码', 2);
            return false;
        }
        let obj = {
            originalPassword:originalPassword,
            newPassword:newPassword,
            confirmNewPassword:confirmNewPassword
        };
        alert(JSON.stringify(obj));
    });

    // 取消
    $('.cancel').click(function () {
        layer.close(index)
    });
});

// 退出
$('.sign-out').click(function () {
    setTimeout(() => {
        window.location.href = globalUrl + '/index.html';
    }, 100);
});


let table = layui.table, laydate = layui.laydate;


//日期范围
laydate.render({
    elem: '.channel-data-start',
    range: true,
    max: 0,
    done: function (value, date, endDate) {
        if (value) {
            let arr = value.split(' - ');
            let start = arr[0],
                end =  arr[1];
            table.reload('channel-user', {
                url: globalAjaxUrl + '/admin/userData/getUserList'
                , where: {
                    beginDate: start,
                    endDate: end
                }
            });
        } else {
            table.reload('channel-user', {
                url: globalAjaxUrl + '/admin/userData/getUserList'
                , where: {
                    beginDate: '',
                    endDate: ''
                }
            });
        }

    }
});

table.render({
    elem: '#channel-user'
    , method: 'GET'
    , limits: [10, 20, 30, 50, 100, 200]
    , limit: 10 //注意：请务必确保 limit 参数（默认：10）是与你服务端限定的数据条数一致 //支持所有基础参数
    , url: globalAjaxUrl + '/admin/userData/getUserList'
    ,height: 'full-160'
    , where: {
        beginDate: '',
        endDate: ''
    }
    , cols: [[
        {field: 'phone',  title: '用户号码', align: 'center'}
        , {field: 'id', title: 'ID', align: 'center',hide:true}
        , {field: 'registrationTime',title: '注册时间', align: 'center'}
        , {field: 'activationTime', title: '激活时间', align: 'center'}
        , {field: 'loginTime', title: '最近登录时间', align: 'center'}
    ]]
    , page: true
    , done: function (res, curr, count) {
        $('.layui-table-main').perfectScrollbar(); //数据渲染完成后的回调
    }
});

$('.management-option-date>p').click(function () {
    $(this).addClass('active').siblings().removeClass('active');
});

let form = layui.form;
let query = pageCommon.getUrlParams();
let empId = query.empId;


let url = globalAjaxUrl + '/admin/employee/getTree';
pageCommon.getAjax(url, {}, function (res) {
    res = res.data;
    let arr = ['pictureManagement', 'loanPlatform', 'newsAndInformation', 'channelPromotion', 'platformManagement'];
    for (let i = 0; i < res.data.length; i++) {
        res.data[i].field = arr[i];
    }
    for (let j = 0; j < res.data.length; j++) {
        let html = `
                      <div class="add-article-filed" style="margin-bottom: 30px;">
                    <span class="add-article-tit" style="position: absolute;">${res.data[j].name}:</span>
                    <div class="permission-selection">
                        <div class="permission-selection-l ${res.data[j].field}" data-id="${res.data[j].id}">
                            <p><input type="checkbox" title="使用" class="checkedAll" lay-filter="${res.data[j].field}"
                                      lay-skin="primary"></p>
                            <p class="option-p"><input type="checkbox" lay-filter="checkbox" class="edit" title="编辑" disabled
                                                       lay-skin="primary"></p>
                            <p class="option-p"><input type="checkbox" lay-filter="checkbox" class="add" title="添加" disabled
                                                       lay-skin="primary"></p>
                            <p class="option-p"><input type="checkbox" lay-filter="checkbox" class="remove" title="删除" disabled
                                                       lay-skin="primary"></p>
                        </div>
                    </div>
                </div>`;
        $('.operationTime').before(html);
        form.render();
    }
});

if (query.empId) {

    let getParams = globalAjaxUrl + '/admin/employee/getEmpPermission?empId=' + empId;
    pageCommon.getAjax(getParams, {}, function (res) {

        res = res.data;
        $('.name').val(res.Employee.name);
        $('.account-number').val(res.Employee.accountNumber);
        $('.password').val(res.Employee.password);
        $('.add-article').attr('data-id',res.Employee.empId);
        $('.channel-type').val(res.Employee.updateTime);
        let arr = ['pictureManagement', 'loanPlatform', 'newsAndInformation', 'channelPromotion', 'platformManagement'];

        for (let i = 0; i < res.array.length; i++) {
            res.array[i].field = arr[i];
        }
        for (let j = 0; j < res.array.length; j++) {
            $('.'+res.array[j].field).find('.checkedAll').prop('checked',res.array[j].useData);
            $('.'+res.array[j].field).find('.edit').prop('checked',res.array[j].editData);
            $('.'+res.array[j].field).find('.add').prop('checked',res.array[j].addData);
            $('.'+res.array[j].field).find('.remove').prop('checked',res.array[j].deleteData);
            let checked =  $('.'+res.array[j].field).find('.checkedAll').prop('checked');
            if (checked){
                $('.'+res.array[j].field).find('.edit,.add,.remove').removeAttr('disabled');
            }
            form.render();
        }
    });
}

$('.add-article').perfectScrollbar();

//  图片管理
form.on('checkbox(pictureManagement)', function (data) {
    if (data.elem.checked) {
        $('.pictureManagement').find('.edit,.add,.remove').removeAttr('disabled');
        form.render();
    } else {
        $('.pictureManagement').find('.edit,.add,.remove').attr('disabled', 'disabled');
        $('.pictureManagement').find('.edit,.add,.remove').prop("checked", false);
        form.render();
    }
});


// 贷款平台
form.on('checkbox(loanPlatform)', function (data) {
    if (data.elem.checked) {
        $('.loanPlatform').find('.edit,.add,.remove').removeAttr('disabled');
        form.render();
    } else {
        $('.loanPlatform').find('.edit,.add,.remove').attr('disabled', 'disabled');
        $('.loanPlatform').find('.edit,.add,.remove').prop("checked", false);
        form.render();
    }
});


// 新闻资讯
form.on('checkbox(newsAndInformation)', function (data) {
    if (data.elem.checked) {
        $('.newsAndInformation').find('.edit,.add,.remove').removeAttr('disabled');
        form.render();
    } else {
        $('.newsAndInformation').find('.edit,.add,.remove').attr('disabled', 'disabled');
        $('.newsAndInformation').find('.edit,.add,.remove').prop("checked", false);
        form.render();
    }
});

// 渠道推广
form.on('checkbox(channelPromotion)', function (data) {
    if (data.elem.checked) {
        $('.channelPromotion').find('.edit,.add,.remove').removeAttr('disabled');
        form.render();
    } else {
        $('.channelPromotion').find('.edit,.add,.remove').attr('disabled', 'disabled');
        $('.channelPromotion').find('.edit,.add,.remove').prop("checked", false);
        form.render();
    }
});

// 平台管理
form.on('checkbox(platformManagement)', function (data) {
    if (data.elem.checked) {
        $('.platformManagement').find('.edit,.add,.remove').removeAttr('disabled');
        form.render();
    } else {
        $('.platformManagement').find('.edit,.add,.remove').attr('disabled', 'disabled');
        $('.platformManagement').find('.edit,.add,.remove').prop("checked", false);
        form.render();
    }
});

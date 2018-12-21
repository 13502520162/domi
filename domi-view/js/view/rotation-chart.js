let permissionValue = pageCommon.getPermissionValue();
// 计算元素宽高
function rotationResize() {
    let modelH = $('.rotation-chart-bottom>ul').outerWidth(true); //获取宽度
    $('.rotation-chart-bottom>ul>li').css('width', modelH / 2 - 20);  // 因为 li 是要 2个为一排加上外边框
}

// getRotationChart页面请求
function getRotationChart() {
    let url = globalAjaxUrl + '/admin/banner/getBanners';
    pageCommon.getAjax(url, {}, getRotationChartSuccess);
}

// 页面请求成功回调
function getRotationChartSuccess(data) {
    if (data.mag = 'ok') {
        $('.rotation-chart-bottom-ul').html('');
        for (let i = 0; i < data.data.length; i++) {
            let html = `<li flag="true" data-id="${data.data[i].id}">
                        <div class="photo-left" data-base="${data.data[i].imgUrl}">
                          <div class="option">
                                <p class="option-p"><i class="fa fa-ellipsis-h"></i></p>
                                <ul class="option-ul">
                                    <li class="edit">编辑</li>
                                </ul>
                            </div>
                            <a href="javascript:;" class="file" style="display: none;">
                                <p><i class="fa fa-plus"></i></p>
                                <p>banner</p>
                                <input type="file" onchange="upFile(this)" accept="image/jpg,image/jpeg,image/png" name="">
                            </a>
                            <img src="${data.data[i].imgUrl}" alt="" class="file-img">
                        </div>
                        <div class="photo-right" data-base="${data.data[i].background}">
                            <div class="option">
                                <p class="option-p"><i class="fa fa-ellipsis-h"></i></p>
                                <ul class="option-ul">
                                    <li class="edit">编辑</li>
                                </ul>
                            </div>
                            <a href="javascript:;" class="file" style="display: none;">
                                <p><i class="fa fa-plus"></i></p>
                                <p>banner背景</p>
                                <input type="file" onchange="upFile(this)" accept="image/jpg,image/jpeg,image/png" name="">
                            </a>
                            <img src="${data.data[i].background}" alt="" class="file-img">
                            <p class="sort"><span>${i + 1}</span><span>/</span><span>${data.data.length}</span></p>
                            <span class="remove"><i class="fa fa-remove"></i></span>
                        </div>
                    </li>`;

            let len = $('.rotation-chart-bottom-ul>li').length;
            if (len < 6) {
                $('.rotation-chart-bottom-ul').append(html);
            } else {
                pageCommon.layerMsg('添加不能超过6组',2)
            }
            rotationResize();
            hoverEle();
        }
        pageCommon.noRelevantData('.rotation-chart-bottom-ul>li','.rotation-chart-bottom-ul')

    }
}

function hoverEle() {
    $('.option-p').hover(function () {
        $(this).next().show();
    });

    $('.rotation-chart-bottom-ul>li').mouseleave(function () {
        $(this).find('.option-ul').hide();
    });
}


// 上传图片
function upFile(a) {
    let $this = $(a);
    let fr = new FileReader(); // 这个FileReader应该对应于每一个读取的文件都需要重新new一个
    let files = $this[0].files[0]; // files可以获取当前文件输入框中选择的所有文件，返回列表
    fr.readAsDataURL(files); // 读取的内容是加密以后的本地文件路径
    fr.onload = function (e) { // 数据读取完成时触发onload对应的响应函数
        let src =   pageCommon.getTokenUrl(e.target.result);
        $this.parent().parent().attr('data-base', src);
        $this.parent().hide();
        $this.parent().parent().find('.file-img').removeClass('fn-hide').show();
        $this.parent().parent().find('.file-img').attr('src',src);
        $this.parents('li').attr('flag', true);
        $this.val('');
    };
}


// 添加按钮
$('.add-rotation-chart').click(function () {
    $('.rotation-chart-bottom-ul>div').remove();
    if (!permissionValue.add){
        pageCommon.layerMsg('你没有权限添加',2);
        return false;
    }
    let html = `<li flag="true" class="new-li">
                        <div class="photo-left"}">
                          <div class="option">
                                <p class="option-p"><i class="fa fa-ellipsis-h"></i></p>
                                <ul class="option-ul">
                                    <li class="edit">编辑</li>
                                </ul>
                            </div>
                            <a href="javascript:;" class="file">
                                <p><i class="fa fa-plus"></i></p>
                                <p>banner</p>
                                <input type="file" onchange="upFile(this)" accept="image/jpg,image/jpeg,image/png" name="">
                            </a>
                            <img src="" alt="" class="file-img fn-hide">
                        </div>
                        <div class="photo-right">
                            <div class="option">
                                <p class="option-p"><i class="fa fa-ellipsis-h"></i></p>
                                <ul class="option-ul">
                                    <li class="edit">编辑</li>
                                </ul>
                            </div>
                            <a href="javascript:;" class="file">
                                <p><i class="fa fa-plus"></i></p>
                                <p>banner背景</p>
                                <input type="file" onchange="upFile(this)" accept="image/jpg,image/jpeg,image/png" name="">
                            </a>
                            <img src="" alt="" class="file-img fn-hide">
                            <span class="remove"><i class="fa fa-remove"></i></span>
                        </div>
                    </li>`;
    let len = $('.rotation-chart-bottom-ul>li').length;
    if (len < 6) {
        $('.rotation-chart-bottom-ul').append(html);
    } else {
        pageCommon.layerMsg('添加不能超过6组',2)
    }

    let modelH = $('.rotation-chart-bottom>ul').outerWidth(true); //获取宽度
    $('.rotation-chart-bottom>ul>li').css('width', modelH / 2 - 20);  // 因为 li 是要 2个为一排加上外边框
    hoverEle();
});

// 编辑
$('.rotation-chart-bottom-ul').on('click', '.edit', function () {
    if (!permissionValue.edit){
        pageCommon.layerMsg('你没有权限编辑',2);
        return false;
    }
    let flag = $(this).parents('li').attr('flag');
    if (flag) {
        $(this).parents('.option').next().show();
        $(this).parents('.option').next().next().hide();
        $(this).parents('li').attr('flag', false);
        $(this).parents('li').addClass('new-li');
    }
});


// 删除
$('.rotation-chart-bottom-ul').on('click', '.remove', function () {
    if (!permissionValue.remove){
        pageCommon.layerMsg('你没有权限删除',2);
        return false;
    }
    let $this = $(this);
    let index = layer.confirm('确定删除该分组嘛？', {
        btn: ['确认', '取消'] //按钮
    }, function () {
        layer.close(index);
        let id = $this.parents('li').attr('data-id');
        if (id) {
            $this.parents('li').remove();
            let url = globalAjaxUrl + '/admin/banner/delBanner?bannerId=' + id;
            pageCommon.getAjax(url, {}, function (res) {
                pageCommon.layerMsg(res.msg)
            });
            setTimeout(() => {
                getRotationChart();
            }, 1000);
        } else {
            $this.parents('li').remove();
        }
        pageCommon.noRelevantData('.rotation-chart-bottom-ul>li','.rotation-chart-bottom-ul','暂无数据,请添加图片')

    }, function () {
        layer.close(index);
    });
});

// 确定按钮
$('.rotation-chart-confirm').click(function () {
    if (!permissionValue.add){
        pageCommon.layerMsg('你没有权限保存',2);
        return false;
    }
    let data = [];
    let len = $('.rotation-chart-bottom-ul>.new-li').length;
    if (len) {
        for (let i = 0; i < len; i++) {
            let left = $('.rotation-chart-bottom-ul>.new-li').eq(i).find(' .photo-left').attr('data-base');
            let right = $('.rotation-chart-bottom-ul>.new-li').eq(i).find('.photo-right').attr('data-base');
            let id = $('.rotation-chart-bottom-ul>.new-li').eq(i).attr('data-id');
            id = id || 'null';
            if (left != undefined || right != undefined) {
                let obj = {
                    'imgUrl': left,
                    'background': right,
                    'id': id
                };
                data.push(obj)
            }
        }
        let post = {newData: JSON.stringify(data)};
        let url = globalAjaxUrl + '/admin/banner/addBanner';
        pageCommon.postAjax(url, post, function (data) {
            getRotationChart();
            pageCommon.layerMsg(data.msg)
        });
        /* if (!data){

         }else {
             layer.msg('请添加图片');
         }*/
    } else {
        pageCommon.layerMsg('请添加或修改图片',2);
    }
});

// 取消
$('.rotation-chart-cancel').click(function () {
    getRotationChart();
});



let globalHttp = 'http://';
/*let globalAjaxUrl = globalHttp + "yanis.vicp.io:11790";*/
let globalAjaxUrl = globalHttp + "app.duomimarkt.com";
/*let globalAjaxUrl = globalHttp + "192.168.0.105";*/
let globalUrl = globalHttp + window.location.host + '/domi/domi-view';
let loginToken = sessionStorage.getItem('loginToken');
let pageCommon = {
    /**
     * get ajax 请求
     * @param url
     * @param data
     * @param succ_fn
     * @param err_fn
     * @param datatype
     */
    getAjax: function (url, data, succ_fn, err_fn, datatype) {
        let uri = $.trim(url);	      //去除空格
        datatype = datatype || 'json';
        data = data || {};
        $.ajax({
                type: "GET",
                url: uri,
                async: true,
                data: data,
                dataType: datatype,
                beforeSend(request) {
                    request.setRequestHeader('Authorization', loginToken);
                },
                success: function (response) {
                    succ_fn(response);
                },
                error: err_fn
            }
        );
    },
    /**
     * post ajax 请求
     * @param url
     * @param data
     * @param succ_fn
     * @param err_fn
     * @param datatype
     */
    postAjax: function (url, data, succ_fn, err_fn, datatype, contentType) {
        let uri = $.trim(url);	      //去除空格
        datatype = datatype || 'json';
        contentType = contentType || 'application/json;charset=UTF-8';
        $.ajax({
                type: "POST",
                url: uri,
                async: true,
                data: data,
                dataType: datatype,
                contentType: contentType,
                beforeSend(request) {
                    request.setRequestHeader('Authorization', loginToken);
                },
                success: function (response) {
                    succ_fn(response);
                },
                error: err_fn
            }
        );
    },
    /**
     * 获取 参数值
     * @returns {{"": string}}
     */
    getUrlParams: function () {
        if (window.location.search == '') {
            return {
                "": '无URL参数'
            }
        }
        let q = window.location.search.substr(1).split('&');
        let returnValue = {};
        for (let i = 0; i < q.length; i++) {
            let temp = q[i].split("=");
            returnValue[temp[0]] = decodeURI(temp[1])

        }
        return returnValue;
    },
    layerParentOpenIframe: function (obj) {
        // 检测请求方式
        let url = obj.url || '没有传数据';
        let title = obj.title || '信息';
        let area = obj.area || ['800px', '850px'];
        let btn = obj.btn || ['保存', '取消'];
        let confirm = obj.confirm;
        let cancel = obj.cancel;
        let success = obj.success;
        let shadeClose = obj.shadeClose || false;
        let layIndex = parent.layer.open({
            type: 2,
            anim: false,
            shadeClose: shadeClose,
            move: false,
            btn: btn,
            loading: false, //是否显示加载条
            title: title,
            closeBtn: 0,
            isOutAnim: false,
            resize: false,
            area: area,
            content: url,
            scrollbar: false,
            yes: function (index, layero) {
                /*            pageCommon.layerLoad(true);*/
                confirm(index, layero);
            },
            btn2: function (index, layero) {
                cancel(index, layero)
            },
            success: function (index, layero) {
                success(index, layero)
            },
            end: function () {
                parent.layer.close(layIndex + 1); //再执行关闭
                parent.layer.closeAll(); //再执行关闭
            }
        });
        return layIndex;
    },
    layerMsg: function (msg, icon, time, success) {
        msg = msg || "内容";
        icon = icon || 6;
        time = time || 2000;
        let indexLayer;
        indexLayer = parent.layer.msg(msg, {
                icon: icon,
                time: time,
                offset: 'auto',
                isOutAnim: false
                , success: function (index, layero) {

                }
            },
            function () { //关闭后回调
                if (success) {
                    success();

                }

            });

        return indexLayer;
    },
    /*
   * 加载层
   * */
    layerLoad: function (isParent) {
        let indexLayer;
        isParent = isParent || false;
        if (isParent) {
            indexLayer = top.layer.load(1, {
                shade: [0.1, '#000'],
                scrollbar: false,
                isOutAnim: false
                , fixed: false
                , success: function (index, layero) {
                }
            });
        } else {
            indexLayer = layer.load(1, {
                shade: [0.1, '#000'],
                scrollbar: false,
                isOutAnim: false
                , fixed: false
                , success: function (index, layero) {
                }
            });
        }

        return indexLayer; //作为关闭的索引抛出
    },
    /**
     *
     * @param {	Function} cancelCallback 取消的回调
     * @param {	Function} confirmCallback 确认的回调
     * @param {String} title  内容
     * @param {String} content  提示的内容
     * @param {String} btn   按钮
     * @returns {*}
     */
    layerConfirm: function (cancelCallback, confirmCallback, title, content, btn) {
        let indexLayer;
        title = title || '取消确认';
        content = content || '确定删除?';
        btn = btn || ['确定', '取消'];
        indexLayer = layer.confirm(content, {
            title: title,
            resize: false,
            btn: btn //按钮
            , isOutAnim: false
            , closeBtn: 0
            , move: false
            , success: function (layero, index) {
            }
        }, function () {
            if (cancelCallback) {
                cancelCallback();
            }
        }, function () {
            if (confirmCallback) {
                confirmCallback();
            }
        });

        return indexLayer;
    },
    /**
     *  根据传入base64  返回 七牛的 url图片链接
     * @param base
     * @returns {*}
     */
    getTokenUrl: function (base) {
        let self = this;
        let src;
        let token = localStorage.getItem('token');
        let pic = base.split("base64,")[1];
        let url = 'http://upload-z2.qiniup.com/putb64/-1';
        $.ajax({
            url: url,
            type: 'POST',
            async: false,
            beforeSend(request) {
                request.setRequestHeader('Content-Type', 'application/octet-stream');
                request.setRequestHeader('Authorization', 'UpToken ' + token) // token服务端请求
            },
            data: pic,
            success: function (data) {
                let domain = localStorage.getItem('domain');
                src = ' http://' + domain + '/' + data.key;
            },
            error:function (data) {
                if (data.status!=200){
                    self.returnLogin();
                }
            }
        });
        return src;
    },
    noRelevantData: function (Length, el, tip) {
        let liLenGht = $(Length).length;
        tip = tip || '暂无数据';
        if (liLenGht <= 0) {
            let html = '<div style="text-align: center;margin-top: 50px;">' +
                '<i class="fa fa-exclamation-circle" style="font-size: 120px;color: #e6e6e6"></i>' +
                '<p style="margin-top:12px;color:#ccc;">' + tip + '</p>' +
                '</div>';
            $(el).append(html);
        }
    },
    /**
     *  仿form表单形式导出excel
     * @param params  参数
     * @param url  后台接口
     */
    postExcelFile: function (params, url) {
        let form = document.createElement("form");
        form.style.display = 'none';
        form.action = url;
        form.method = "post";
        document.body.appendChild(form);

        for (let key in params) {
            let input = document.createElement("input");
            input.type = "hidden";
            input.name = key;
            input.value = params[key];
            form.appendChild(input);
        }
        form.submit();
        form.remove();
    },
    /**
     *  传入数字返回现在到之前的日期
     * @param count
     * @returns {string}
     */
    getTimeForMat(count) {
        // 拼接时间
        count = parseInt(count) || 0;
        let time1 = new Date();
        let Y1 = time1.getFullYear();
        let M1 = ((time1.getMonth() + 1) > 10 ? (time1.getMonth() + 1) : '0' + (time1.getMonth() + 1));
        let D1 = (time1.getDate() > 10 ? time1.getDate() : '0' + time1.getDate());
        let timer1 = Y1 + '-' + M1 + '-' + D1;// 当前时间
        let time2 = new Date();
        time2.setTime(time2.getTime() - (24 * 60 * 60 * 1000 * count));
        let Y2 = time2.getFullYear();
        let M2 = ((time2.getMonth() + 1) > 10 ? (time2.getMonth() + 1) : '0' + (time2.getMonth() + 1));
        let D2 = (time2.getDate() >= 10 ? time2.getDate() : '0' + time2.getDate());
        let timer2 = Y2 + '-' + M2 + '-' + D2;// 之前的7天或者30天

        return {
            start: timer2,
            end: timer1
        }
    },
    /**
     *  获取模块操作权限
     * @returns {*}
     */
    getPermissionValue: function () {
        let _this = this;
        let data;
        // 获取员工权限  员工ID 和 模块 ID
        let loginInfo = localStorage.getItem('loginInfo');
        loginInfo = JSON.parse(loginInfo);
        let modelId = sessionStorage.getItem('modelId');
        if (loginInfo == null) {
            _this.returnLogin();
            return false;
        }
        let getEmpPermissionUrl = globalAjaxUrl + '/admin/employee/getPermission?modelId=' + modelId + '&employeeId=' + loginInfo.employeeId;
        $.ajax({
                type: "GET",
                url: getEmpPermissionUrl,
                async: false,
                beforeSend(request) {
                    request.setRequestHeader('Authorization', loginToken);
                },
                success: function (res) {
                    if (res.errcode === 3) {
                        _this.returnLogin();
                        return false
                    }
                    if (res.data.permission != null) {
                        let obj = {
                            add: res.data.permission.addData,
                            remove: res.data.permission.deleteData,
                            edit: res.data.permission.editData
                        };
                        data = obj;
                    }
                }
            }
        );
        return data;
    },
    returnLogin: function () {
        this.layerMsg('请先登录账号', 2);
        setTimeout(() => {
            top.window.location.href = globalUrl + '/admin/index.html';
        }, 1500);
    }
};


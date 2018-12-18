let globalHttp = 'http://';
let globalAjaxUrl = globalHttp + "192.168.0.104";
let globalUrl = globalHttp + window.location.host;

let pageCommon = {
    /**
     * get ajax ����
     * @param url
     * @param data
     * @param succ_fn
     * @param err_fn
     * @param datatype
     */
    getAjax: function (url, data, succ_fn, err_fn, datatype) {
        let uri = $.trim(url);	      //ȥ���ո�
        datatype = datatype || 'json';
        data = data || {};
        $.ajax({
                type: "GET",
                url: uri,
                async: true,
                data: data,
                dataType: datatype,
                success: function (response) {
                    succ_fn(response);
                },
                error: err_fn
            }
        );
    },
    /**
     * post ajax ����
     * @param url
     * @param data
     * @param succ_fn
     * @param err_fn
     * @param datatype
     */
    postAjax: function (url, data, succ_fn, err_fn, datatype) {
        let uri = $.trim(url);	      //ȥ���ո�
        datatype = datatype || 'json';
        $.ajax({
                type: "POST",
                url: uri,
                async: true,
                data: data,
                dataType: datatype,
                success: function (response) {
                    succ_fn(response);
                },
                error: err_fn
            }
        );
    },
    /**
     * ��ȡ ����ֵ
     * @returns {{"": string}}
     */
    getUrlParams:function(){
        if (window.location.search==''){
            return{
                "":'��URL����'
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
        // �������ʽ
        let url = obj.url || 'û�д�����';
        let title = obj.title || '��Ϣ';
        let area = obj.area || ['800px', '480px'];
        let btn = obj.btn || ['����', 'ȡ��'];
        let confirm = obj.confirm;
        let cancel = obj.cancel;
        let layIndex = parent.layer.open({
            type: 2,
            anim: false,
            shadeClose: false,
            move: false,
            btn: btn,
            loading: false, //�Ƿ���ʾ������
            title: title,
            closeBtn: 0,
            isOutAnim: false,
            resize: false,
            area: area,
            content: url,
            scrollbar: false,
            yes:function (index, layero) {
                confirm(index,layero );
            },
            btn2:function (index, layero) {
                cancel(index, layero)
            }
        });
        return layIndex;
    },
    layerMsg: function (msg, icon, isParent, time, success) {
        msg = msg || "����";
        icon = icon || 6;
        time = time || 2000;
        isParent = isParent || true;
        let indexLayer;

        if (isParent == true) {
            indexLayer = parent.layer.msg(msg, {
                    icon: icon,
                    time: time,
                    offset: 'auto',
                    isOutAnim: false
                    , success: function (index, layero) {

                    }
                },
                function () { //�رպ�ص�
                    if (success) {
                        success();

                    }

                });
        } else {
            indexLayer = layer.msg(msg, {
                    icon: icon,
                    time: time,
                    offset: 'auto',
                    isOutAnim: false
                    , success: function (index, layero) {
                    }
                },
                function () { //�رպ�ص�
                    if (success) {
                        success();

                    }

                });
        }

        return indexLayer;
    },
    /*
   * ���ز�
   * */
    layerLoad: function (isParent) {
        let indexLayer;
        isParent = isParent || false;
        if(isParent){
            indexLayer = top.layer.load(1, {
                shade:[0.1, '#000'],
                scrollbar: false,
                isOutAnim : false
                ,fixed: false
                , success: function (index, layero) {
                }
            });
        }else {
            indexLayer = layer.load(1, {
                shade:[0.1, '#000'],
                scrollbar: false,
                isOutAnim : false
                ,fixed: false
                , success: function (index, layero) {
                }
            });
        }

        return indexLayer; //��Ϊ�رյ������׳�
    },
    /**
     *
     * @param {	Function} cancelCallback ȡ���Ļص�
     * @param {	Function} confirmCallback ȷ�ϵĻص�
     * @param {String} title  ����
     * @param {String} content  ��ʾ������
     * @param {String} btn   ��ť
     * @returns {*}
     */
    layerConfirm: function (cancelCallback, confirmCallback,title,content, btn) {
        let indexLayer;
        title = title || 'ȡ��ȷ��';
        content = content || 'ȷ��ɾ��?';
        btn = btn ||['ȷ��', 'ȡ��'];
        indexLayer = layer.confirm(content, {
            title: title,
            resize: false,
            btn: btn //��ť
            ,isOutAnim: false
            ,closeBtn:0
            ,move: false
            ,success:function (layero, index) {
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
     *  ���ݴ���base64  ���� ��ţ�� urlͼƬ����
     * @param base
     * @returns {*}
     */
    getTokenUrl:function (base) {
        let src;
        let token = localStorage.getItem('token');
        let pic = base.split("base64,")[1];
        let url = 'http://upload-z2.qiniup.com/putb64/-1';
        $.ajax({
            url: url,
            type: 'POST',
            async:false,
            beforeSend (request) {
                request.setRequestHeader('Content-Type', 'application/octet-stream');
                request.setRequestHeader('Authorization', 'UpToken ' + token) // token���������
            },
            data: pic,
            success: function (data) {
                let domain =  localStorage.getItem('domain');
                src = ' http://' + domain +'/'+ data.key;
            }
        });
        return src;
    },
    noRelevantData:function (Length, el, tip) {
        let liLenGht = $(Length).length;
        tip = tip || '��������';
        if (liLenGht <= 0) {
            let html = '<div style="text-align: center;margin-top: 50px;">' +
                '<i class="fa fa-exclamation-circle" style="font-size: 120px;color: #e6e6e6"></i>' +
                '<p style="margin-top:12px;color:#ccc;">' + tip + '</p>' +
                '</div>';
            $(el).append(html);
        }
    },
    /**
     *  ��form����ʽ����excel
     * @param params  ����
     * @param url  ��̨�ӿ�
     */
    postExcelFile:function (params, url) {
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
     *  �������ַ������ڵ�֮ǰ������
     * @param count
     * @returns {string}
     */
    getTimeForMat(count){
        // ƴ��ʱ��
        count = parseInt(count)  || 0;
        let time1 = new Date();
        let Y1 = time1.getFullYear();
        let M1 = ((time1.getMonth() + 1) > 10 ? (time1.getMonth() + 1) : '0' + (time1.getMonth() + 1));
        let D1 = (time1.getDate() > 10 ? time1.getDate() : '0' + time1.getDate());
        let timer1 = Y1 + '-' + M1 + '-' + D1;// ��ǰʱ��
        let time2 = new Date();
        time2.setTime(time2.getTime() - (24 * 60 * 60 * 1000 * count));
        let Y2 = time2.getFullYear();
        let M2 = ((time2.getMonth() + 1) > 10 ? (time2.getMonth() + 1) : '0' + (time2.getMonth() + 1));
        let D2 = (time2.getDate() >= 10 ? time2.getDate() : '0' + time2.getDate());
        let timer2 = Y2 + '-' + M2 + '-' + D2;// ֮ǰ��7�����30��

        return {
            start: timer2,
            end: timer1
        }
    }
};


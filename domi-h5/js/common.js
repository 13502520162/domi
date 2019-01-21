let globalHttp = 'http://';
/*let globalAjaxUrl = globalHttp + "yanis.vicp.io:11790";*/
let globalAjaxUrl = globalHttp + "app.duomimarkt.com";
/*let globalAjaxUrl = globalHttp + "192.168.0.105";*/
let globalUrl = globalHttp + window.location.host + '/domi/domi-view';
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
    getTokenUrl: function (base) {
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
            }
        });
        return src;
    }
};


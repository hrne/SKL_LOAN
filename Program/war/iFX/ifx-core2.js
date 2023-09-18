/*
 **   ___ _______  __
 **  |_ _|  ___\ \/ /
 **   | || |_   \  /   SystemOne 2014
 **   | ||  _|  /  \
 **  |___|_|   /_/\_\
 **
 */
/*備註:
1.FKEY 統一調整成 int
*/
var Ifx = (function ($) {
    var NEWLINE = '\r\n';
    var DEFULT_SIZE = '830:1170'; // 列印畫面大小預設值A4
    var _self = null;
    var ocjson = {};
    var octext = "";
    var Class = function () {
        var klass = function () {
            this.init.apply(this, arguments);
        };
        klass.prototype.init = function () {
        };
        return klass;
    };
    var Ifx = new Class;
    // 柯:已這種寫法所以不能 再次送出 只能重新交易
    var result_output_data = null; // total swift result
    Ifx.fn = Ifx.prototype;
    // Module begin
    Ifx.fn.init = function (txcd, auth, tranDef, pfns, sysvar, panel) {
        _self = this;
        this.RIMTESTNO = false;
        this.RIMTESTNOSTR = "";
        this.txcd = txcd;
        this.fkey = 0;
        this.def = tranDef;
        this.xxrjnlid = ""; // LCR01回調 更改 RESV 之 TXNO 欄位 (for修改後放行resv的bug)
        // this.def.display[0].def.list[0][0] = "***" +
        // this.def.display[0].def.list[0][0]
        // + "***";
        this.auth = auth;
        this.escRim = "";
        this.escSync = false;
        // this.printServiceUrl = tranEnv.printServiceUrl;
        this.sysvar = sysvar;
        this.panel = panel;
        // this.util = new IfxUtil(this);
        this.fieldMap = {}; // all fields map
        this.sysFields = []; // sys fields list
        this.preDC_Fields = []; // pre data collect field list
        this.dcFields = []; // data collect field list
        this.xmtedFields = []; // xmted fields list
        this.triggerFieldMap = {}; // callable field list
        this.step = "sys"; // sys, dc, xmted
        this.currentIndex = -1;
        this.firstIndex = -1;
        this.keys = new IfxKeys(this);
        this.keys.bind();
        this.rtn = new IfxRtn(this);
        this.rtn.callHandler = new IfxCall(this);
        this.rtn.callHandler.calendar();
        this.help = new IfxHelp(this, this.panel.help, this.panel.tooltip);
        this.ifxHost = new IfxHost(this);
        this.jnlId = -1;
        this.hostOvrMode = false; // #HSUPCD
        this.tranEnd = false;
        this.saved = false;
        this.batchMode = false;
        this.mouseFirst = false;
        this.firstInputIndex = 0;
        this.isScAll = false;
        this.countDownTran = false;
        this.istimeout = 0; // 柯 測試 0 沒事 其他則超時
        this.eachSend = false; // 柯 新增給BATCH時的特殊需求 (整批列印XT90B & 單獨列印單據XS964)
        this.onlyprint = ""; // for XR929 除了該設定,其他不列印
        this.showErrorText = {}; // 中文欄位檢查並限制tita上送中心
        this.initFields();
        this.pageScrollHeight = 0;
        this.tradeReason = "";
        this.superVisorR = "";
        this.ntxbufT = {};
        var self = this;
        this.def.docs = refactorPfns();

        function refactorPfns() {
            var result = {},
                k, v, ss, prompt;
            if ($.isEmptyObject(pfns)) return result;
            // for ( var k in pfns) {
            for (var i = 0; i < self.def.select.length; i++) { // for every
                // output select
                if (self.def.select[i].device == "QUERY.GRID") continue;
                // 預設可重印，但如果是FALSE就是FALSE
                // Eric提:只有查詢類才看此設定，更新類都是要重印
                self.def.select[i].dupable = _self.isUpdatedTran() ? true : self.def.select[i].dupable;
                k = self.def.select[i].form;
                if ($.type(k) === 'string') {
                    if (k.toUpperCase().indexOf('.PART') != -1) {
                        self.def.select[i].isPart = true;
                        continue;
                    }
                    if (k.indexOf('.PFNX') != -1) {
                        self.def.select[i].isPFNX = true;
                        v = pfns[k];
                    } else {
                        v = combineTemplates(k);
                    }
                } else {
                    v = combineTemplates(k);
                }
                console.log(k + ":");
                console.log(v);
                // 柯 R6上無法讀取\r 故更為 \n來判斷,不清楚是否後續會有問題???
                // ss = v.split(/\r\n/g);
                v = v.replace(/\r/g, "");
                ss = v.split(/\n/g);
                prompt = (ss.shift()).replace(/;/g, "");
                result[k] = {
                    "form": k,
                    "prompt": prompt,
                    "content": ss
                };
            }
            console.dir(result);
            return result;
        }

        function removeQuotationMark(name) {
            var v = pfns[name];
            if (v === undefined) {
                alert("Developer's bug, undefined pfn name:" + name);
                throw "fatal error:undefined pfn name:" + name;
            }
            var pos1 = v.indexOf('"');
            var pos2 = v.lastIndexOf('"');
            if (pos1 == 0 && pos2 > pos1) v = v.slice(pos1 + 1, pos2);
            return v;
        }

        function combineTemplates(formId) {
            if (IfxUtl.is_array(formId)) {
                var t = "";
                $.each(formId, function (i, x) {
                    if (isNaN(x)) {
                        t += removeQuotationMark(x);
                    } else {
                        var z = '';
                        while (x-- > 0) {
                            z += NEWLINE;
                        }
                        t += z;
                    }
                });
                return t;
            } else {
                return removeQuotationMark(formId);
            }
        }

        $(window).scroll(() => {
            if ($(document).scrollTop() > 0) _self.pageScrollHeight = $(document).scrollTop();
        });

        //重新整理將原本ntxbuf塞回
        $(window).bind('beforeunload', function () {
            if (_self.getValue('CHAIN$') != "0")
                dup.dup(_self.ntxbufT, _self.txcd);
        });
    };

    function id2name(id) {
        if (id && id.match(/fld_/)) {
            id = id.replace(/fld_/, "#");
            console.log(id);
            return id;
        }
        return null;
    };
    Ifx.fn.initFields = function () {
        console.log("init fields 2");
        var self = this,
            fldIndex = 0,
            refCount = 0,
            o, it;
        $.each(this.def.rtns, function (i, rtn) {
            if (rtn.render == 'swift') {
                _self.swiftTran = true;
                _self.swiftDcName = rtn.name;
                console.log('a swift tran, dc:' + self.swiftDcName);
            }
            $.each(rtn.fields, function (i, x) {
                o = new _FieldRunTime(x);
                if (o.type == '@') {
                    console.log("append trigger field:%s", o.name);
                    self.triggerFieldMap[o.name] = o;
                    self.fieldMap[o.name] = o;
                } else {
                    if (o.name === '#_SWIFTFORM_') {
                        _self.swiftTran = true;
                    }
                    o.idx = fldIndex++;
                    o.init(rtn);
                    appendField(rtn, o);
                }
            });
        });
        console.log("init fields done:" + fldIndex + ", ref count:" + refCount);
        console.log("sys fields:" + self.sysFields.length);
        console.log("preDC fields:" + self.preDC_Fields.length);
        console.log("dc fields:" + self.dcFields.length);
        console.log("xmted fields:" + self.xmtedFields.length);
        // console.log(this.fieldMap);
        // find swift form index
        if (self.swiftTran) {
            self.initSwiftForm();
        }
        // append 9 #it fields
        for (var i = 1; i <= 9; i++) { // copy from generated JS
            it = {
                'name': "#it" + i,
                'type': 'X',
                'len': 120,
                'dlen': 0,
                'attr': 'S',
                '_auto_': true,
            };
            var rtn = {
                type: 'tmp',
                name: '*it'
            };
            // o = $.extend(it, _fieldRunTime);
            o = new _FieldRunTime(it);
            o.idx = fldIndex++;
            o.init(rtn);
            o.enabled(false);
            self.fieldMap[o.name] = o;
        }
        // append 9 #x fields
        for (var i = 1; i <= 9; i++) { // copy from generated JS
            it = {
                'name': "#x" + i,
                'type': 'X',
                'len': 120,
                'dlen': 0,
                'attr': 'S',
                '_auto_': true,
            };
            var rtn = {
                type: 'tmp',
                name: '*x'
            };
            // o = $.extend(it, _fieldRunTime);
            o = new _FieldRunTime(it);
            o.idx = fldIndex++;
            o.init(rtn);
            o.enabled(false);
            self.fieldMap[o.name] = o;
        }
        // append 9 #i fields
        for (var i = 1; i <= 9; i++) { // copy from generated JS
            it = {
                'name': "#i" + i,
                'type': 'n',
                'len': 6,
                'dlen': 0,
                'attr': 'S',
                '_auto_': true,
            };
            var rtn = {
                type: 'tmp',
                name: '*int'
            };
            // o = $.extend(it, _fieldRunTime);
            o = new _FieldRunTime(it);
            o.idx = fldIndex++;
            o.init(rtn);
            o.enabled(false);
            self.fieldMap[o.name] = o;
        }

        function appendField(rtn, fld) {
            var list = self.getFieldsByRtnType(rtn.type);
            fld.index = list.length;
            list.push(fld);
            if (fld.ref) {
                refCount++;
                fld.copyRef(self.fieldMap[fld.name]);
            } else {
                self.fieldMap[fld.name] = fld;
            }
        }
    };
    Ifx.fn.getFieldsByRtnType = function (rtnType) {
        switch (rtnType.toLowerCase()) {
            case "sys":
                return this.sysFields;
                break;
            case "predc":
                return this.preDC_Fields;
                break;
            case "dc":
                return this.dcFields;
                break;
            case "rtn":
            case "form":
                return this.xmtedFields;
                break;
            default:
                throw "programmer's bug!! unknown rtn type:" + rtnType;
        }
    };
    var _layout = 1;
    var _closetabText = "";
    var _canClose = true;
    var _nextNoclose = false;
    Ifx.fn.setup = function () {
        IfxUtl.ifx = this;
        this.parseAuth();
        this.initGlobal();
        _layout = this.getCurrentLayout();
    };
    Ifx.fn.diyblock = function (message, tempshowtext) {
        // _self.diyunblock();
        // unBlock();
        this.keys.blocking(true);
        console.log(".......diyblock......." + message);
        var $fakeblock;
        var printname = "存摺印表機"; // 預設
        if (tempshowtext) {
            printname = tempshowtext;
            $fakeblock = $("<div class='fakeblock cnfrm-block'>" + "自動列印:<br/>請插入<span style='color:red;'>" + message + "</span>到" + printname + "</div>");
        } else {
            $fakeblock = $("<div class='fakeblock cnfrm-block'>" + message + "</div>");
        }
        $fakeblock.css({
            position: 'fixed',
            top: 3,
            left: 3,
            "background-color": "yellow",
            "font-size": "200%"
        });
        $('.fakeblock').remove();
        $("body").css({
            "opacity": "0.5"
        }).append($fakeblock);
    };
    Ifx.fn.diyunblock = function () {
        console.log(".......diyunblock.......");
        this.keys.blocking(false);
        $('.fakeblock').remove();
        $("body").css({
            "opacity": "1"
        })
    };
    Ifx.fn.block = function (message) {
        console.log(".......block......." + message);
        this.keys.blocking(true);
        blockIt('請稍候', message);
    };
    Ifx.fn.unblock = function () {
        console.log(".......unblock.......");
        this.keys.blocking(false);
        // this.keys.blocking(false);
        try {
            unBlock();
        } catch (e) {
            // console.log("交易前rim回調錯誤");
        }
    };
    Ifx.fn.NaviKeyEnabled = function (enabled) {
        this.keys.enableNaviKeys(enabled);
    };
    Ifx.fn.fyi = function (msg, timeout, nDelay, css) {
        if (nDelay) {
            setTimeout(function () {
                blockAWhile(msg, timeout, css);
            }, nDelay);
        } else {
            blockAWhile(msg, timeout, css);
        }
    };
    Ifx.fn.parseAuth = function () {
        this.canCorrect = false;
        this.canApprove = false;
        if (getSysvar('hodecd$') != '0') {
            this.canCorrect = true;
        }
        if (getSysvar('passcd$') != '0') {
            this.canApprove = true;
        }
    };
    Ifx.fn.parseAuthOld = function () {
        this.canCorrect = false;
        this.canApprove = false;
        if (this.auth.auth.toUpperCase() === 'B') {
            this.canCorrect = this.canApprove = true;
        }
        if (this.auth.pass && this.auth.pass.toUpperCase() === 'NO') {
            this.canApprove = false;
        }
        if (this.auth.cor && this.auth.cor.toUpperCas() === 'NO') {
            this.canCorrect = false;
        }
    };

    function resizeSelf() {
        // $(window).trigger("scroll"); //$(this).scroll(); 換頁籤時需觸發 scroll
        // 指令，重新定義 div按鈕位置
        // 指令，重新定義 div按鈕位置
        console.log("doc height1:" + $(document.body).height());
        var theFrame = $('#' + _iframeId, parent.document.body);
        var theCenter = $('#center', parent.document.body);
        console.log("doc height2:" + $(document.body).height());
        // theFrame.height($(document.body).height());
        theFrame.height(theCenter.height() - 30);
        // if($(document.body).height() < theCenter.height() ) {
        // $(document.body).height(theCenter.height());
        // }
        return;
        console.log("doc height1:" + $(document.body).height());
        var theFrame = $('#' + _iframeId, parent.document.body);
        console.log("theFrame height:" + theFrame.height());
        if ($(document.body).height() > theFrame.height()) {
            console.log("changing theFrame height...");
            var h = Math.max($(document.body).height(), screen.height - 30);
            // var h = $(document.body).height();
            theFrame.height(h);
            // theFrame.height($(document.body).height() + 30);
            console.log("theFrame height change to:" + theFrame.height());
        } else {
            console.log("theFrame height NO CHANGE:" + theFrame.height());
        }
        // if($(document.body).width() > theFrame.width() ) {
        // console.log("changing theFrame height...");
        // var h = $(document.body).width();// + 50, screen.height - 50);
        // theFrame.width(h);
        // //theFrame.height($(document.body).height() + 30);
        // console.log("theFrame height change to:"+theFrame.height());
        // }
    };

    function queueTask(context, fn, nMili) {
        setTimeout(function () {
            fn();
        }, nMili == undefined ? 0 : nMili);
    }

    Ifx.fn.addWidget = function () {
        $.each(this.dcFields, function (i, x) {
            if (x.type == 'F') {
                x.ui().datebox({
                    required: false
                });
            }
        });
    };
    Ifx.fn.postDisplay = function () {
        var self = this;
        console.log("postDisplay!!!");
        // $(this.panel.entry)[0].scrollIntoView();
        // $('.pageTable').equals('width');
        this.addFieldButtonHandler();
        this.addFieldButtonChangeHandler();
        this.registerTab();
        // check device agent
        deviceInit();
        registerCheckbox();
        this.refreshStatus();
        queueTask(null, resizeSelf);
        // 鍾 先關掉自動暫存功能
        // var saveInterval = 60 * 1000;
        var saveInterval = 60 * 1000;

        // end
        function saveIt() {
            if (!self.isSaved()) {
                //				self.save();
            }
            if (!self.tranEnd && self.isSavable()) {
                setTimeout(function () {
                    saveIt();
                }, saveInterval);
            }
        }

        if (saveInterval > 0) {
            setTimeout(function () {
                saveIt();
            }, saveInterval);
        }
        $(window).resize(function () {
            resizeSelf();
        });
    };
    Ifx.fn.addFieldButtonChangeHandler = function () {
        // var xlf = document.getElementsByClassName("field_file_button");
        // for (var i = 0; i < xlf.length; i++) {
        // xlf[i].addEventListener('change', handleFile, false);
        // }
        var abortFile, fildName;
        $(".field_file_button").on("click", cleanFile);

        function cleanFile(e) {
            // 合庫版本 ie10無用!
            e.target.value = null;
        };
        $(".field_file_button").on("change", handleFile);
        $('#boxclose').click(function () {
            if (abortFile && abortFile != null) abortFile.abort();
        });
        var X = XLS;

        // xxxxx
        function handleFile(e) {
            var $btn = $(this);
            if ($btn.attr("type") != 'file') {
                return;
            }
            var fencode;
            if ($btn.attr("encode")) {
                fencode = $btn.attr("encode");
            }
            console.log("get encode:" + fencode);
            if (e.target.value.length == 0) {
                return;
            }
            var files = e.target.files;
            var filepath = e.target.value;
            var f = files[0];
            var data = new FormData();
            for (var x = 0; x < files.length; x++) data.append("file", files[x]);
            var uploadUrl = location.protocol + "//" + location.host + "/iFX/mvc/file/Upload";
            abortFile = $.ajax({
                type: "POST",
                url: uploadUrl,
                data: data,
                cache: false,
                processData: false,
                contentType: false,
                dataType: 'json', // 回傳的資料格式
                success: function (data) {
                    fildName = $("#fldName" + $btn[0].tabIndex).text();
                    $("#status" + $btn[0].tabIndex).html(data.content);
                    $("#status" + $btn[0].tabIndex).show();
                    // $(".field_file_button").attr("disabled", true);
                    abortFile = null;
                    var fileName = "";
                    for (var x = 0; x < data.fileNames.length; x++) fileName += data.fileNames[x] + ";";
                    _self.setValue(fildName, fileName.substring(0, fileName.length - 1));
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    alert("上傳失敗 Status : " + XMLHttpRequest.status + "readyStatus : " + XMLHttpRequest.readyState + "textstatus : " + textStatus);
                },
                xhr: function () {
                    var xhr = new window.XMLHttpRequest(); // 建立xhr(XMLHttpRequest)物件
                    xhr.upload.addEventListener("progress", function (progressEvent) { // 監聽ProgressEvent
                        if (progressEvent.lengthComputable) {
                            var percentComplete = progressEvent.loaded / progressEvent.total;
                            var percentVal = Math.round(percentComplete * 100) + "%";
                            $("#percent" + $btn[0].tabIndex).text(percentVal); // 進度條百分比文字
                            $("#bar" + $btn[0].tabIndex).width(percentVal); // 進度條顏色
                        }
                    }, false);
                    return xhr; // 注意必須將xhr(XMLHttpRequest)物件回傳
                }
            }).fail(function () {
                $("#percent" + $btn[0].tabIndex).text("0%"); // 錯誤發生進度歸0%
                $("#bar" + $btn[0].tabIndex).width("0%");
            });
            abortFile;
            /*
			 * }; // 不是txt才做 if (f.type.indexOf("text") != -1) {
			 * reader.readAsText(f); } else if (f.type.indexOf("excel") != -1) {
			 * reader.readAsArrayBuffer(f); } else { alert("格式不為[xls]、[txt]。"); } } }
			 * function fixdata(data) { var o = "", l = 0, w = 10240; for (; l <
			 * data.byteLength / w; ++l) o += String.fromCharCode.apply(null,
			 * new Uint8Array(data.slice(l * w, l w + w))); o +=
			 * String.fromCharCode.apply(null, new Uint8Array(data.slice(l *
			 * w))); return o; }
			 */
            {
                var reader = new FileReader();
                var name = f.name;
                if ($btn.attr("filename")) {
                    var filename = name.split(".");
                    if (filename.length > 1) {
                        filename.pop(); // 柯移除附檔名
                    }
                    var filename2 = filename.join("."); // 重新組合
                    _self.setValue($btn.attr("filename"), filename2);
                }
                /*
				 * reader.onload = function(e) { if (f.type.indexOf("text") !=
				 * -1) { octext = reader.result; console.log("octext:" +
				 * octext); } else if (f.type.indexOf("excel") != -1) { //
				 * 改成device做 start // "list" 改成 null 自動抓取第一個頁籤 var temp =
				 * deviceX.excelReadtoJosn(filepath); // 大小寫沒差 ocjson = {
				 * "list": JSON.parse(temp) }; console.dir(ocjson); // end }
				 * else { // 預設其他都為 TXT octext = reader.result;
				 * console.log("octext:" + octext); } }; // 不是txt才做 if
				 * (f.type.indexOf("text") != -1) { reader.readAsText(f,
				 * fencode); } else if (f.type.indexOf("excel") != -1) {
				 * reader.readAsArrayBuffer(f); // 改成device做，因ie10版本無法用
				 * FileReader() ?? } else { // 預設其他都為 TXT reader.readAsText(f,
				 * fencode); }
				 */
            }
        }

        function fixdata(data) {
            var o = "",
                l = 0,
                w = 10240;
            for (; l < data.byteLength / w; ++l) o += String.fromCharCode.apply(null, new Uint8Array(data.slice(l * w, l * w + w)));
            o += String.fromCharCode.apply(null, new Uint8Array(data.slice(l * w)));
            return o;
        }

        function process_wb(wb) {
            var output = "";
            // ocjson= JSON.stringify(to_json(wb), 2, 2);
            ocjson = to_json(wb);
            // output = to_csv(wb);
            console.log("output:" + output);
            console.log("ocjson:" + ocjson);
            console.log("ocjson to json:" + JSON.stringify(ocjson));
            if (typeof console !== 'undefined') console.log("output", new Date());
        }

        /*
		 * function to_csv(workbook) { var result = [];
		 * workbook.SheetNames.forEach(function(sheetName) { var csv =
		 * X.utils.sheet_to_csv(workbook.Sheets[sheetName]); if(csv.length > 0){
		 * result.push("SHEET: " + sheetName); result.push("");
		 * result.push(csv); } }); return result.join("\n"); }
		 */
        function to_json(workbook) {
            var result = {};
            workbook.SheetNames.forEach(function (sheetName) {
                var roa = X.utils.sheet_to_row_object_array(workbook.Sheets[sheetName]);
                if (roa.length > 0) {
                    result[sheetName] = roa;
                }
            });
            return result;
        }
    };
    Ifx.fn.addFieldButtonHandler = function () {
        var self = this,
            once, fromKeyboard = false;
        // add field button handler
        // $('.field_button').on('keydown', function(e) {
        // alert('kbd');
        // fromKeyboard = true;
        // });
        $('.field_button').on('click', function (e) {
            var $btn = $(this),
                name = IfxUtl.id2name(this.id),
                fld = self.getField(name);
            _self.help.hide();
            if ($btn.attr("type") == 'file') {
                return;
            }
            // 潘 按鈕控制
            // $btn.css({
            // 'background-color': '#E3E4FA',
            // 'color': '#737CA1'
            // });
            once = self.handleFldEvent(e.type, $btn, fld); // 柯 增加此行 待測試 (改位置)
            // 增加此行
            // 待測試
            // (改位置)
            if (!once) { // 柯 增加此行 待測試 (改位置)
                var oldText = $btn.attr('oldText');
                if (!oldText) {
                    $btn.attr('oldText', $btn.val());
                    oldText = $btn.attr('oldText');
                }
                var times = $btn.attr('clickTimes') || 1;
                times = parseInt(times, 10);
                // var t = oldText + " (" + times + ")";
                // $btn.val(t); //柯: 合庫提出 移除按鈕的+1次數
                $btn.attr('clickTimes', times + 1);
                // $btn.attr('disabled', true); //柯
                // once = self.handleFldEvent(e.type, $btn, fld); //柯
                // if (!once) { //柯
                // if (!once) { //柯
                $btn.attr('disabled', false);
                // } //柯
                // 鍾 該欄位如果不是可輸入,不需keyforward
                if ($btn._tabbable) {
                    setTimeout(function () {
                        self.KeyForward('field button click');
                    }, 1);
                }
                ;
                // end
                e.preventDefault();
            }
        });
    };
    Ifx.fn.display = function (firstTime) {
        var self = this;
        this.fldCounter = 0;
        var tabindex = 1;
        var result = [];
        var dcFieldMap = {};
        $.each(this.dcFields, function (i, x) {
            dcFieldMap[x.name] = x;
        });
        var layout = getDefaultLayout();
        try {
            var screenContent = ifxUI.renderScreen(this.def, function (tabIndex, x) {
                try {
                    var temp = dcFieldMap[x].display(tabIndex);
                    // 潘 小日曆
                    if (dcFieldMap[x].attr == "I" && dcFieldMap[x].type == "D" && dcFieldMap[x].len >= 7) setTimeout(function () {
                        $('#fld_' + dcFieldMap[x].name.substring(1)).datepickerTW({
                            showOn: "button",
                            beforeShow: function (input, inst) {
                                var offset = $(input).offset();
                                var height = $(input).height();
                                window.setTimeout(function () {
                                    var ttop = offset.top + height + 4;
                                    if ($(document).height() - ttop < 300) ttop = ttop - 100;
                                    inst.dpDiv.css({
                                        top: ttop + 'px',
                                        left: offset.left + 'px'
                                    })
                                }, 1);
                                /*
								inst.dpDiv.css({
									marginTop: -input.offsetHeight-50 + 'px',
									marginLeft: input.offsetWidth + 'px'
								});
								*/
                                _self.jumpOrBack($("#" + this.id).attr("dc-index"));
                            },
                            buttonImage: "images/icons8-32.png",
                            buttonImageOnly: true,
                            showButtonText: 'Choose a date',
                            onClose: function () {
                                _self.KeyForward('datepicker');
                            },
                            changeYear: true,
                            changeMonth: true,
                            yearRange: '38:2100',
                            //dateFormat: 'yyymmdd',
                            defaultDate: '+1w'
                        });
                    }, 500);
                    return temp;
                } catch (ee) {
                    alert('programmer\'s bug!!\n\nerror raised by renderScreen():' + ee + '\n\ncheck display field:' + x);
                    throw ee;
                }
            });
            result.push(screenContent);
        } catch (ex) {
            alert("failed to render screen\n" + ex);
        }
        // result.push("<br/>");
        // console.log(result.join(""));
        $(this.panel.entry).append(result.join(""));

        // 最後一個tabIndex 潘
        var lastTabIndex = 0;
        /*
		var rex = /tabindex='\d+'/g;
		var tabIndexArray = result.join("").match(rex);
		if (tabIndexArray)
			lastTabIndex = tabIndexArray[tabIndexArray.length - 1].replace(/tabindex=|'/g, "");
		*/
        $(this.panel.buttons).append(createButtons(lastTabIndex)); // 修改為 .buttons
        // 小柯改 start 功能為滾動畫面超過entryArea高度時 更改css entryArea
        // var $b = $('#entryArea');
        // $(window).scroll((function(){
        // //用closure的概念，避免額外的timer、mainFunction變數汙染變數空間
        // var timer;
        // var mainFunction = function(e){
        // if ($(this).scrollTop() > $b.height() +36) { //
        // $( "#buttonsArea2" ).show("fast");
        // } else {
        // $( "#buttonsArea2" ).hide("fast");
        // }
        // };
        // return function(e){
        // if(timer) clearTimeout(timer);
        // timer = setTimeout(function(){
        // mainFunction(e);
        // }, 100);
        // };
        // })());
        // 小柯改 end
        // 小柯改 end
        var buddy = ifxUI.getBuddy();
        _.each(buddy, function (x) {
            $('#' + IfxUtl.name2id(x)).addClass('buddy');
            console.log($('#' + IfxUtl.name2id(x)).hasClass('buddy'));
        });
        // 鍾 表格每格最後欄位magin-right 7px;
        var rside = ifxUI.getRside();
        _.each(rside, function (x) {
            $('#' + IfxUtl.name2id(x)).addClass('rside');
        });
        // end
        injectHandlers();

        function injectHandlers() {
            // 柯: YN click 改 mousedown for ie10 BUG?
            $('#btn_yn').on('mousedown keypress', function (e) {
                console.log("mousedown yn");
                // 移除click後,補回客戶熟悉的空白
                if (e.type == "keypress") {
                    // if (e.keyCode != $.ui.keyCode.SPACE)
                    // 	return;
                    return;
                }
                if (self.tranEnd) {
                    console.log("tran is end, no submit");
                    if (Object.keys(self.showErrorText).length > 0) {
                        alert(self.showErrorText[Object.keys(self.showErrorText)[0]]);
                    }
                    return;
                }
                var $thisBtn = $(this);
                if ($thisBtn.attr('disabled') == true) {
                    console.log("submit is disabled");
                    return;
                }
                try {
                    canSubmit();
                } catch (ee) {
                    printException(ee);
                }
            });

            function easyValue(x) {
                return x.charAt(0) == "#" ? _self.getValue(x) : x;
            }

            // 潘 NAV 取消
            document.onclick = function () {
                const menuNodeList = top.frames[1].document.querySelectorAll('.dropdown-menu.show');
                Array.from(menuNodeList).forEach((element) => {
                    element.classList.remove('show');
                });
            }
            $('#btn_new1').on('click', function () {
                var t = _self.getValue('BTN_NEW1$').trim();
                var bindMap = IfxUtl.str2map(t);
                IfxUtl.collectValue(bindMap, easyValue);
                _self.bindHandler(bindMap);
                if ($(this).val().trim() == "提交") $(this).hide();
            });
            $('#btn_new2').on('click', function () {
                var bindMap = IfxUtl.str2map(_self.getValue('BTN_NEW2$').trim());
                IfxUtl.collectValue(bindMap, easyValue);
                _self.bindHandler(bindMap);
                if ($(this).val().trim() == "提交") $(this).hide();
            });
            $('#btn_new3').on('click', function () {
                var bindMap = IfxUtl.str2map(_self.getValue('BTN_NEW3$').trim());
                IfxUtl.collectValue(bindMap, easyValue);
                _self.bindHandler(bindMap);
                if ($(this).val().trim() == "提交") $(this).hide();
            });
            $('#btn_new4').on('click', function () {
                var bindMap = IfxUtl.str2map(_self.getValue('BTN_NEW4$').trim());
                IfxUtl.collectValue(bindMap, easyValue);
                _self.bindHandler(bindMap);
                if ($(this).val().trim() == "提交") $(this).hide();
            });
            $('#btn_new5').on('click', function () {
                var bindMap = IfxUtl.str2map(_self.getValue('BTN_NEW5$').trim());
                IfxUtl.collectValue(bindMap, easyValue);
                _self.bindHandler(bindMap);
                if ($(this).val().trim() == "提交") $(this).hide();
            });
            $('#xbtn_cancel').on('click', function () {
                _self.KeysEscapeTran();
            });
            $('#xbtn_save').on('click', function () {
                _self.trySave(true);
            });
            // 柯 新增給國外部測試 隱藏上方畫面
            $('#xbtn_entryhide').on('click', function () {
                _self.tryentryHide();
            });
            $('#xbtn_screen').on('click', function () {
                _self.printScreenForEric();
            });
            // 柯:新增此段給特殊交易使用
            // 只有顯示grid2的時候
            $('#xbtn_spesend').on('click', function () {
                // _self.block();
                var titahead = _self.getValue("#OEACHHEAD"); // 上送頭
                var getdataA = "#OEACHTITAA"; // 表格內容欄位
                var getdataB = "#OEACHTITAB"; // 表格內容欄位
                var gridsmsgid = _self.getValue("#GRIDSMSGID"); // 傳送交易代號
                var daiamtAll = _self.getValue("#DAIAMT"); // 總TOTAL 貸
                var jieamtAll = _self.getValue("#JIEAMT"); // 總TOTAL 借
                var txcdxa98z = _self.getValue("#TXCD"); // 潘 20180108
                // TODO #DAIAMT #JIEAMT 相等才能送出?
                var gridData1 = grid.getCheckedRow(_self.panel.gridControls);
                var gridData2 = grid2.getCheckedRow(_self.panel.gridControls_2);
                var gridData1text = "",
                    gridData2text = "";
                if (gridData1.length == 0 || gridData2.length == 0) {
                    alert("請勾選表格資料!");
                    return;
                }
                if (daiamtAll != jieamtAll && txcdxa98z != "XA98Z") { // 潘
                    // 20180108
                    alert("借貸金額不平!");
                    return;
                }
                $.each(gridData1, function (i, x) {
                    console.dir(x);
                    gridData1text += "A" + x[getdataA];
                });
                $.each(gridData2, function (i, x) {
                    console.dir(x);
                    gridData2text += "B" + x[getdataB];
                });
                var titatext = titahead + gridData1text + gridData2text;
                _self.ifxHost.initSend();
                _ajaxSender = _self.ifxHost.send(gridsmsgid, true, titatext, function (totaList) {
                    console.log("batchMode done");

                    function buildReport() {
                        var DETAIL_VAR = "#batch-detail";
                        var BATCH_SELECT = "#batch-select";
                        var batchdetail = "";
                        var oTota = totaList[0];
                        batchdetail = oTota.getDumpPrint();
                        _self.setValue(DETAIL_VAR, batchdetail);
                        // build pfnx
                        var oSelect = null;
                        $.each(_self.def["select"], function (i, x) {
                            if (x.form == _self.getValue(BATCH_SELECT).trim()) {
                                oSelect = x;
                                return false;
                            }
                        });
                        if (oSelect != null) {
                            // make a dummy tota for printPFNX
                            var oTota = {
                                getTxForm: function () {
                                    return "BATCH";
                                }
                            };
                            return _self.printPFNX(oSelect, oTota);
                        }
                    }

                    var rpt = buildReport();
                    console.log(rpt.content);
                    _self.displayOutput(_self.panel.output, [rpt]);
                    $('#xbtn_spesend').off("click").hide();
                    _self.unblock();
                    _self.postTran();
                }, _self, null, null, false);
            });
            $('#xbtn_again').on('click', function () {
                // 鍾 按重新交易,交易畫面全部清空
                // self.undoDisableCollect();
                $('#main-container').effect('fade', 123, function () {
                    window.location.href = window.location.href; // 柯: 原
                    // location.reload();
                    // 更改
                });
                // end
            });
            // textarea maxlength
            $('textarea[maxlength]').keyup(function () {
                var max = parseInt($(this).attr('maxlength'));
                console.log("maxlength:" + max);
                if ($(this).val().length > max) {
                    $(this).val($(this).val().substr(0, $(this).attr('maxlength')));
                }
            });
            $('body').on("dblclick", function (evt) {
                // 柯 移除for jqgrid亂跳 先確認是在哪個欄位
                // var currentField = self.getCurrentField();
                // if(currentField.name =="btn_yn"){
                // console.log("body but Is in btn_yn,return!")
                // return;
                // }
                // self.goCurrentField(); //直接移除功能
                console.log("in body!");
                evt.preventDefault();
            });
            /*
			 * slower $(".field_input").on( "click paste ", function(evt) {
			 * console.log("EVENT " + evt.type.toUpperCase() + " on " +
			 * $(this).attr('name'));
			 *
			 * var b = jumpOrBack($(this).attr('dc-index'), evt);
			 *
			 * if (evt.type == "click") evt.preventDefault(); });
			 */
            $(self.panel.entry).delegate(".field_input", "mousedown paste ", // 柯 修改
                // click->註冊
                // mousedown
                // 才不會發生假值問題
                function (evt) {
                    console.log("EVENT " + evt.type.toUpperCase() + " on " + $(this).attr('name'));
                    var b = _self.jumpOrBack($(this).attr('dc-index'), evt);
                    // if (evt.type == "click"){ //應該沒用,且改mousedown會錯
                    // evt.preventDefault();
                    // }
                });
            // 柯 加入此段測試 var 最後欄位保留select的問題
            $(self.panel.entry).delegate(".field_input", "blur", function (evt) {
                $(this).removeClass('myFocusHint');
            });
            /*
			 * slower $(".field_input").on("focus", function(evt) { var r =
			 * $(this); r.addClass('myFocusHint'); });
			 */
            $(self.panel.entry).delegate(".field_input", "focus", function (evt) {
                try {
                    $('.myFocusHint').removeClass('myFocusHint');
                    $('.myFocusRowHint').removeClass('myFocusRowHint');
                    var $r = $(this);
                    $r.addClass('myFocusHint');
                    $currTd = $r.parent();
                    $tr = $currTd.parent();
                    var highLights = [];
                    highLights.push($currTd);
                    var colIndex = $tr.children().index($currTd);
                    var cells = $.map($tr.children(), function (x) {
                        return $(x);
                    });
                    if (colIndex == 0) {
                    } else {
                        if (cells[colIndex - 1].hasClass('input-label')) highLights.push(cells[colIndex - 1]);
                    }
                } catch (ee) {
                    console.log(ee);
                }
                addRowHint();

                function addRowHint() {
                    $.each(highLights, function (i, x) {
                        x.addClass('myFocusRowHint');
                    });
                }
            });
            /*
			 * $(self.panel.entry).delegate("td","click", function(evt) { var t =
			 * $(this); alert(t.text() + "\n\n" + t.html()); return false; });
			 */
        }

        Ifx.fn.jumpOrBack = function (dcIndex, evt) {
            console.log("======== jumpOrBack?");
            if (self.currentIndex < 0) return;
            if (!dcIndex) { // not my field
                console.log("not my field");
                self.goCurrentField();
                return true;
            }
            dcIndex = parseInt(dcIndex, 10);
            // var thatField = self.getField(thatName);
            // if(!thatField.isTabStop()) return;
            if (dcIndex != self.currentIndex) {
                console.log("clicking field Index:" + dcIndex);
                console.log("currentIndex:" + self.currentIndex);
                if (dcIndex < self.currentIndex) { // backward
                    console.log('back to index:' + dcIndex);
                    if (self.isSwiftMode()) {
                        self.leaveSwiftForm('up');
                    }
                    self.nextField(dcIndex, true);
                } else {
                    console.log('try going to index:' + dcIndex);
                    if (self.isSwiftMode()) {
                        goSwiftForm('forward', 'jump', continueNextField);

                        function continueNextField() {
                            self.nextField(self.swiftIndex + 1, false, true, dcIndex); // continue
                            // after
                            // swift
                            // form
                            // field
                        }
                    } else {
                        if (_self.mouseFirst) {
                            self.nextField(dcIndex, true);
                        } else {
                            self.nextField(self.currentIndex + 1, false, true, dcIndex);
                        }
                    }
                }
            } else {
                console.log("click on same field");
            }
        }

        function createButtons(lastTabIndex) {
            // 鍾 <確定>鍵改<確定傳送> <ESC離開>鍵改<離開/ESC><重新查詢>鍵改<重新交易>
            // var s1 = createButton("btn_yn", "確定傳送");
            var s1 = createButton("btn_yn", "確定", false, lastTabIndex++);
            // var s2 = createButton("xbtn_cancel", "按ESC離開");
            var noCloseFg = false;
            if (getTabFn("getTabMap")()[getTabFn("getTabTitle")()].noclose == "true" || getTabFn("getTabMap")()[getTabFn("getTabTitle")()].noclose == true)
                noCloseFg = true;
            var s2 = noCloseFg ? createButton("xbtn_cancel", "離開", true, lastTabIndex++) : createButton("xbtn_cancel", "離開", false, lastTabIndex++);
            var s3 = createButton("xbtn_again", "重新交易", true, lastTabIndex++);
            var s4 = "";
            if (_chain != 9 && _self.fkey == 0 && _self.txcd.substring(2, 3) != "0" && _self.txcd.substring(2, 3) != "9" && _self.txcd.substring(2, 3) != "R")
                s4 = createButton("xbtn_save", "暫  存", true, lastTabIndex++);
            var s5 = createButton("xbtn_spesend", "上送中心", true, lastTabIndex++);
            var s6 = createButton("xbtn_entryhide", "隱藏/顯示", true, lastTabIndex++); // 初始隱藏
            var s7 = createButton("xbtn_print", "重印單據", true, lastTabIndex++); // 初始隱藏
            var s9 = "";
            var s10 = createButton("btn_new1", "", true, lastTabIndex++);
            var s11 = createButton("btn_new2", "", true, lastTabIndex++);
            var s12 = createButton("btn_new3", "", true, lastTabIndex++);
            var s13 = createButton("btn_new4", "", true, lastTabIndex++);
            var s14 = createButton("btn_new5", "", true, lastTabIndex++);
            /*
			 * 潘 暫時拿掉 if (_server_os != "1") { s9 = createButton("xbtn_screen",
			 * "規格書"); }
			 */
            if (firstTime) {
                self.getFieldsByRtnType("dc").push({
                    name: "btn_yn"
                });
            }
            // return "<div style='margin-left:80px'>" + s1 + "&nbsp;" + s3 + "&nbsp;" + s2
            // + "&nbsp;" + s4 +
            // "&nbsp;" + s5 + "&nbsp;" + s6 + "&nbsp;" + s7 + "&nbsp;" + s9 + "</div>";
            return "<div style='margin-left:80px'>" + s1 + "&nbsp;" + s3 + "&nbsp;" + s4 + "&nbsp;" + s2 + "&nbsp;" + s5 + "&nbsp;" + s6 + "&nbsp;" + s7 + "&nbsp;" + s9 + "&nbsp;" + s10 + "&nbsp;" + s11 +
                "&nbsp;" + s12 + "&nbsp;" + s13 + "&nbsp;" + s14 + "</div>";
        }

        function createButton(id, value, hide, lastTabIndex) {
            var s = '<input type="button" class="xbtn" {hide} name="{id}" id="{id}" value="{value}" tabindex="{tabindex}"/>';
            if (hide)
                s = s.replace(/{hide}/, 'style="display:none"');
            else
                s = s.replace(/{hide}/, '');

            if (lastTabIndex >= 12)
                s = s.replace(/{tabindex}/g, lastTabIndex);
            else
                s = s.replace(/tabindex="{tabindex}"/g, "");
            return s.replace(/{id}/g, id).replace(/{value}/, value);
        }

        function displayPage(self, n, p) {
            var list = p.def.list;
            var l = 0;
            layout = overrideLayout(layout, p.name, p.def);
            console.log("layout:");
            console.dir(layout);
            var columns = parseInt(layout.cols, 10);
            var r = [];
            var pId = "p_" + p.name;
            r.push("<table id='{id}' class='pageTable'  style='table-layout:auto;margin-top:0px;margin-left:{xx}px'>".replace(/{id}/, pId).replace(/{xx}/, layout.screen.left));
            var caption = list[l++][0];
            console.log("caption:" + caption);
            if (caption) {
                r.push("<tr><td  class='caption ' colspan='" + (columns) * 3 + "'  align='left'>&nbsp;" + caption + "</td></tr>");
            }

            // every line
            function getLabelWidth(i) {
                var offset = 0;
                if (columns > 1 && i % 2 != 0) {
                    offset = 2;
                }
                return {
                    label: layout.screen.width[offset],
                    field: layout.screen.width[offset + 1]
                };
            }

            var cells = 0;
            var trClosed = true;
            var k;
            var h;
            var line;
            var i = 0;
            // $.each(list, function(i,line){
            for (; l < list.length; l++) {
                line = list[l];
                i++; // cells count
                k = 0;
                var label, cmd = line[k++];
                var colspan = false;
                var gridRow = false;
                if (cmd == "#<->#" || cmd == "#grid#") {
                    colspan = true;
                    gridRow = (cmd == "#grid#") ? true : false;
                    if (cells % columns != 0) cells++;
                    label = line[k++];
                } else {
                    label = cmd;
                }
                if ((cells) % columns == 0) {
                    if (!trClosed) {
                        if (columns > 1) r.push("<td/><td/><td/>");
                        r.push("</tr>");
                    }
                    r.push("<tr valign='top'>");
                    trClosed = false;
                } else {
                    r.push("<td width='2'/>"); // add seperator for cell1 and
                    // cell2
                }
                var widthDef = getLabelWidth(i);
                console.dir(widthDef);
                // first element is Label
                if (label && label.length > 0 && label != "\f") {
                    label = replaceCounter(self, label, "&nbsp;");
                    h = "<td class='label' width='{label_width}' valign='top' align='left'>" + label + "</td><td class='field' valigh='top' {colspan} width='{field_width}'>";
                    if (!colspan) {
                        h = h.replace(/{label_width}/, widthDef.label).replace(/{field_width}/, widthDef.field);
                    } else {
                        h = h.replace(/{label_width}/, widthDef.label).replace(/{field_width}/, widthDef.label + widthDef.field * 3);
                    }
                    h = h.replace(/{colspan}/, colspan ? "colspan='4" : "");
                    console.log(h);
                    r.push(h);
                } else { // non label(empty "")
                    r.push("<td colspan='2' class='field'>");
                    console.log("<td colspan='2'>");
                }
                // then process the others
                var x;
                for (; k < line.length; k++) {
                    x = line[k];
                    if (x == "<br/>") {
                        r.push(x);
                    } else {
                        if ($.isArray(x)) {
                            if (gridRow) beginGrid();
                            drawGridCells(x);
                        } else {
                            drawElement(x);
                        }
                    }
                    // });
                }
                if (gridRow) endGrid();
                r.push("</td>");
                if (colspan) cells++;
                if ((cells + 1) % columns == 0) {
                    r.push("</tr>");
                    trClosed = true;
                }
                cells++;
                // });
            }
            if (!trClosed) r.push("</tr>");
            r.push("</table>");
            // console.log(r.join(""));
            return r.join("");

            function drawElement(x) {
                if (x.match(/#/)) {
                    var fld = dcFieldMap[x];
                    if (!fld) throw "couldn't find field:" + x;
                    r.push(fld.display(tabindex++));
                } else {
                    r.push("<span class='plaintext'>" + x + "</span>");
                }
                r.push("&nbsp;");
            }

            function drawGridCells(arr) {
                if (!gridFirstRowAdded) {
                    r.push("<tr class='gray'>");
                    gridFirstRowAdded = true;
                } else {
                    r.push("<tr>");
                }
                for (var i = 0; i < arr.length; i++) {
                    r.push("<td>");
                    drawElement(arr[i]);
                    r.push("</td>");
                }
                r.push("</tr>");
            }

            var gridStarted = false;
            var gridFirstRowAdded = false;

            function beginGrid() {
                if (!gridStarted) {
                    r.push("<div>");
                    r.push("<table class='gridRow'>");
                    gridStarted = true;
                }
            }

            function endGrid() {
                r.push("</table></div>");
                gridStarted = false;
                gridFirstRowAdded = false;
            }
        }
    }; // end of Ifx.display
    // pre submit
    function canSubmit() {
        console.log("canSubmit?");
        _self.timeoutt(0);
        console.log("this.istimeout:" + this.istimeout);
        if (_self.currentIndex == _self.ynIndex) {
            if (_self.getValue("#HCODE") == 1)
                _self.setValue("#TRMTYP", "00");

            // 顯示提示警告文字，含確定取消功能
            if (checkWarnbefore()) {
                return;
            }
            // 檢測主管授權等需求
            if (checkSupervisorOvr()) {
                return;
            }

            console.log("Maybe first block!!!!");
            _self.block("Submit ...."); // 柯 經測試可能不出現
            _self.diyblock("Submit ...."); // 補這行
            var fakebatch = _self.getValue('FAKEBATCH$').toLowerCase();
            if (fakebatch == "true") {
                specialSubmit();
            } else if (fakebatch == "runs" && _self.def.tim.runrim) { // 特殊eric交易先送最多兩組rim
                runfirstSubmit(_self.def.tim.runrim);
            } else {
                var sendBefore = _self.ifxHost.getsendBefore();
                if (!sendBefore) { // 除了中心回覆回的其餘都關閉
                    if (_self.mouseFirst) {
                        if (_self.isScAll) {
                            _self.isScAll = false;
                            _self.transmit();
                        } else {
                            _self.isScAll = true;
                            _self.diyunblock();
                            _self.unblock();
                            _self.currentIndex = _self.firstInputIndex;
                            _self.nextField(_self.firstInputIndex, false, true, _self.ynIndex);
                        }
                    } else _self.transmit();
                } else {
                    console.log("already send!!");
                }
            }
            return;
        } else {
            // 柯 新增for swift交易可以直接送出的bug
            if (_self.isSwiftMode()) {
                console.log("SWIFT交易未完全,無法送出");
                return;
            }
            /*
			 * console.log("no, you can't submit now"); self.goCurrentField();
			 */
            _self.nextField(_self.currentIndex, false, true, _self.ynIndex);
        }
        console.log("sorry");
    } // end canSubmit
    // ERCI 特殊交易要先送幾個TITA上去時使用
    function runfirstSubmit(runrimdata) {
        var runrimarray = runrimdata.split(",");
        var rimdata = [];
        var tempdata = "";
        for (var i = 0; i < runrimarray.length; i++) {
            tempdata = runrimarray[i];
            if (tempdata.charAt(0) == "#") {
                tempdata = _self.getValue(tempdata);
            }
            rimdata.push(tempdata);
        }
        _self.sendRim(rimdata[0], rimdata[1], function (totaList) {
            console.log("Maybe first block in transmit!!!!");
            _self.block("<h2>Submit ....</h2>"); // 柯 新增這個看看是否可以比較ok
            var oTota = totaList[0];
            console.log("oTota:" + oTota.text);
            if (rimdata.length <= 2) {
                gotransmit();
                return;
            }
            _self.sendRim(rimdata[2], rimdata[3], function (totaList2) {
                console.log("Maybe second block in transmit!!!!");
                _self.block("<h2>Submit ....</h2>"); // 柯 新增這個看看是否可以比較ok
                var oTota2 = totaList2[0];
                console.log("oTota:" + oTota2.text);
                gotransmit();
                return;
            }, function (oTota2) {
                return;
            });
        }, function (oTota) {
            return;
        });

        function gotransmit() {
            var sendBefore = _self.ifxHost.getsendBefore();
            if (!sendBefore) { // 除了中心回覆回的其餘都關閉
                _self.transmit();
            } else {
                console.log("already send!!");
            }
        }
    }

    // 供趙使用VAR模擬 BATCH JQGRID
    function specialSubmit() {
        // TODO 有需要加入 _self.batchMode = true; ?
        _self.batchMode = true;
        var batchmax = parseInt(_self.getValue("#batchmax"), 10); // 總grid數
        var batchmsgid = _self.getValue("#batchmsgid"); // msg id
        var j = 0; // 總需上傳比數
        var rows = []; // 資料
        // 是否要每筆印
        var beachsend = (_self.getValue("#beachsend") && _self.getValue("#beachsend").toLowerCase() == "true") ? true : false;
        for (var i = 1; i <= batchmax; i++) {
            var num = i.toString(); // 格式化
            var eachok = _self.getValue("#BTH_CHK_" + num); // 每行是否勾選
            if (IfxUtl.trim(eachok).length > 0) {
                rows.push(_self.getValue("#BTH_SND_" + num)); // 每行資料
                j++;
            }
        }
        var origTxcd = _self.getValue("#TXCD");
        // 整批當中每一筆回傳的tota list
        var batchtotalist = [];
        async.mapSeries(rows, fakebatchsend, function (err, results) {
            _self.setValue("#TXCD", origTxcd);
            console.log("batchMode done");

            function buildReport() {
                var DETAIL_VAR = "#batch-detail";
                var BATCH_SELECT = "#batch-select";
                _self.setValue(DETAIL_VAR, results.join("\n"));
                // build pfnx
                var oSelect = null;
                $.each(_self.def["select"], function (i, x) {
                    if (x.form == _self.getValue(BATCH_SELECT).trim()) {
                        oSelect = x;
                        return false;
                    }
                });
                if (oSelect != null) {
                    // make a dummy tota for printPFNX
                    var oTota = {
                        getTxForm: function () {
                            return "BATCH";
                        }
                    };
                    return _self.printPFNX(oSelect, oTota);
                }
            }

            var rpt = buildReport();
            console.log(rpt.content);
            _self.displayOutput(_self.panel.output, [rpt]);
            if (batchtotalist.length > 0 && beachsend) {
                console.log("整批架構之各筆列印....");
                setTimeout(function () {
                    _self.eachSend = true;
                    _self.processOutput_second(batchtotalist);
                    $('#btn_yn').off("click mousedown keypress").hide(); // for整批列印後因已經
                    // processTom
                    // 故會導致列印錯誤
                }, 10);
            }
            _self.batchMode = false;
            _self.unblock();
            _self.postTran();
        });

        function fakebatchsend(oRow, callback) {
            var TranText = oRow + "$";
            console.log("send id:" + batchmsgid + "\nrim text:" + TranText);
            sendTim(TranText);

            function sendTim(TranText) {
                _self.setValue("#TXCD", batchmsgid);
                var fakerim = true;
                console.log("batchCfg tran text:" + TranText);
                var text = TranText;
                _self.ifxHost.initSend();
                // 因為在整批時 會導致 ifx-ims-host.js中 data['updTx'] = "1"; 無法更改
                _ajaxSender = _self.ifxHost.send(batchmsgid, fakerim, text, function (totaList) {
                    // alert("success");
                    var oBatchBag = {
                        "hi": "1"
                    };
                    _self.rcvErrorHandler(totaList, oBatchBag);
                    // 柯 新增for XX010回傳先塞值問題
                    if (oBatchBag.success) {
                        if (!totaList[0].isError()) {
                            if (beachsend) {
                                // batchtotalist.push(totaList[0]);
                                for (var i = 0; i < totaList.length; i++) {
                                    if (!totaList[i].isWarnning()) { // 踢掉warning!
                                        // 只保留正常欄位
                                        batchtotalist.push(totaList[i]);
                                    }
                                }
                            }
                            callback(null, "OK   -" + totaList[0].getHostSeq() + "-" + totaList[0].getLastText());
                        } else {
                            callback(null, "ERROR-" + totaList[0].getHostSeq() + "-" + totaList[0].getErrmsg());
                        }
                    } else {
                        callback(null, "ERRTOM-" + oBatchBag.errmsg);
                    }
                }, function (oTota) {
                    callback(null, "ERRTOM-" + oTota.text);
                });
            }
        }
    }

    // 小柯 測試 winodws.confirm -> jquery confirm START
    (function ($) {
        var h = [];
        h.push("<div class='cnfrm-block'>");
        h.push("<div class='cnfrm-msg'></div>");
        h.push("<div>　</div>");
        // h.push('<input id="reson" type="text" maxlength="50" style="width:
        // 400px;"/>');
        h.push('<textarea name="textarea" id="reson" style="width:400px; height:50px; maxlength="100""></textarea>');
        h.push("<div>　</div>");
        h.push("<div>　</div>");
        h.push("<input type='button' class='cnfrm-yes ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only' />");
        h.push("<input type='button' class='cnfrm-no ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only' />");
        h.push("</div>");
        var html = h.join("");
        $.dialog2 = function (title, msg, yesText, noText, reson) {
            if (reson == undefined) html = html.replace('<textarea name="textarea" id="reson" style="width:400px; height:50px; maxlength="100""></textarea>', "");
            var $div = $(html);
            $div.find(".cnfrm-msg").html(msg); // 柯 .text 改 .html
            $div.find(".cnfrm-yes").val(yesText || "Yes");
            $div.find(".cnfrm-no").val(noText || "No");
            var win = $div.dialog({
                title: title || "Confirmation",
                resizable: false,
                width: 550,
                maxWidth: 550,
                modal: true,
                deactivate: function () {
                    this.destroy(); // remove itself after close
                },
                draggable: false,
                autoOpen: false,
                position: "center",
                dialogClass: 'fixed-dialog',
                show: {
                    effect: "blind",
                    duration: 200
                }
            });
            win.dialog("open");
            var dfd = $.Deferred();
            $div.find(":button").click(function () {
                if (this.className.indexOf("cnfrm-yes") != -1) {
                    if (html.indexOf('id="reson"') != -1 && $("#reson").val().trim() == "") {
                        alert("退回理由不可空白");
                        return;
                    }
                    dfd.resolve();
                } else {
                    dfd.reject();
                }
                win.dialog("destroy");
            });
            return dfd.promise();
        };
    })(jQuery);
    // 小柯 測試 winodws.confirm -> jquery confirm END
    //交易理由 潘
    (function ($) {
        var h = [];
        h.push("<div class='reason-block'>");
        h.push("<div class='reason-msg'></div>");
        h.push("<div>　</div>");
        h.push('<textarea name="textarea" id="reason" style="width:400px; height:50px; maxlength="100""></textarea>');
        h.push("<div>　</div>");
        h.push("<div>　</div>");
        h.push("<input type='button' class='reason-yes ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only' />");
        h.push("<input type='button' class='reason-no ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only' />");
        h.push("</div>");
        var html = h.join("");
        $.dialogReason = function (reson) {
            var $div = $(html);
            $div.find(".reason-msg").html(reson); // 柯 .text 改 .html
            $div.find(".reason-yes").val("確定");
            $div.find(".reason-no").val("取消");
            var win = $div.dialog({
                title: "請輸入交易原因",
                resizable: false,
                width: 550,
                maxWidth: 550,
                modal: true,
                deactivate: function () {
                    this.destroy(); // remove itself after close
                },
                draggable: false,
                autoOpen: false,
                position: "center",
                dialogClass: 'fixed-dialog',
                show: {
                    effect: "blind",
                    duration: 200
                }
            }).css("font-style", "Microsoft JhengHei");
            win.dialog("open");
            var dfd = $.Deferred();
            $div.find(":button").click(function () {
                if (this.className.indexOf("reason-yes") != -1) {
                    dfd.resolve();
                } else {
                    dfd.reject();
                }
                win.dialog("destroy");
            });
            return dfd.promise();
        };
    })(jQuery);
    //選擇印表機 潘
    (function ($) {
        var h = [];
        h.push("<div class='printer-block'>");
        h.push("<div class='printer-msg'></div>");
        h.push("<div>　</div>");
        h.push("<div>　</div>");
        h.push("<select id='printerList' size='1'> </select>")
        h.push("<div>　</div>");
        h.push("<input type='button' class='printer-yes ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only' />");
        h.push("<input type='button' class='printer-no ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only' />");
        h.push("</div>");
        var html = h.join("");
        $.dialogPrinter = function (val) {
            var $div = $(html);
            var printerList;
            $.ajax({
                type: 'post',
                dataType: 'json',
                url: "http://localhost:8090/St1Printer/",
                cache: false,
                data: "[{\"Action\": 1}]",
                success: function (data) {
                    if (data.Result == "S") {
                        $div.find(".printer-msg").html("請選擇印表機");
                        $div.find(".printer-yes").val("確定");
                        $div.find(".printer-no").val("取消");
                        var win = $div.dialog({
                            title: "選擇印表機",
                            resizable: false,
                            width: 550,
                            maxWidth: 550,
                            modal: true,
                            deactivate: function () {
                                this.destroy(); // remove itself after close
                            },
                            draggable: false,
                            autoOpen: false,
                            position: "center",
                            dialogClass: 'fixed-dialog',
                            show: {
                                effect: "blind",
                                duration: 200
                            }
                        });
                        data.PrinterList.forEach((x) => {
                            $("#printerList").append(new Option(x.PrinterName, x.PrinterName));
                        });
                        win.dialog("open");
                        $div.find(":button").click(function () {
                            if (this.className.indexOf("printer-yes") != -1) {
                                _self.printReport(val, $("#printerList").val());
                            }
                            win.dialog("destroy");
                        });
                    } else
                        alert("印表錯誤 : " + data.Result + " " + data.resultDesc);
                },
                complete: function () {
                    console.log("complete done !");
                },
                'error': function (e) {
                    alert("印表機連線錯誤!請檢查周邊程式或印表機");
                    console.error(e);
                }
            });
        };
    })(jQuery);
    // 暫存檔名
    (function ($) {
        var h = [];
        h.push("<div class='scrTempName-block'>");
        h.push("<div class='scrTempName-msg'></div>");
        h.push("<div>　</div>");
        h.push('<input type="text" id="scrTempName" style="width:400px; maxlength="100"/>');
        h.push("<div>　</div>");
        h.push("<div style='float: left'> 交易畫面按F7可查詢暫存資料  </div>");
        h.push("<div>　</div>");
        h.push("<div>　</div>");
        h.push("<input type='button' class='scrTempName-yes ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only' />");
        h.push("<input type='button' class='scrTempName-no ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only' />");
        h.push("</div>");
        var html = h.join("");
        $.dialogscrTempName = function (bPrompt) {
            var $div = $(html);
            $div.find(".scrTempName-yes").val("確定");
            $div.find(".scrTempName-no").val("取消");
            var win = $div.dialog({
                title: "暫存檔案名稱(如檔名重複將直接覆蓋)",
                resizable: false,
                width: 550,
                maxWidth: 550,
                modal: true,
                deactivate: function () {
                    this.destroy(); // remove itself after close
                },
                draggable: false,
                autoOpen: false,
                position: "center",
                dialogClass: 'fixed-dialog',
                show: {
                    effect: "blind",
                    duration: 200
                }
            }).css("font-style", "Microsoft JhengHei");
            win.dialog("open");
            $div.find(":button").click(function () {
                if (this.className.indexOf("scrTempName-yes") != -1)
                    _self.saveTran($.trim($("#scrTempName").val()));

                win.dialog("destroy");
            });
        };
    })(jQuery);
    // 選擇暫存的交易
    (function ($) {
        var h = [];
        h.push("<div class='tempScr-block'>");
        h.push("<div class='tempScr-msg'></div>");
        h.push("<div>　</div>");
        h.push("<div>　</div>");
        h.push("<select id='tempScrList' size='1'> </select>")
        h.push("<div>　</div>");
        h.push("<input type='button' class='tempScr-yes ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only' />");
        h.push("<input type='button' class='tempScr-no ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only' />");
        // h.push("<input type='button' class='tempScr-close ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only' style='float: right' />");
        h.push("</div>");
        var html = h.join("");
        var winOld;
        $.dialogTempScr = function (val) {
            if (winOld != undefined && winOld.dialog('isOpen') === true)
                return;

            if (val.brTlrNo == "")
                val.brTlrNo = "scr/" + _self.getValue("#KINBR") + _self.getValue("#TLRNO");
            $.ajax({
                type: 'post',
                dataType: 'json',
                url: _contextPath + '/mvc/hnd/tempScrList',
                cache: false,
                data: {_d: JSON.stringify(val)},
                success: function (data) {
                    if (data.status == "S") {
                        if (data.tempScrList.length <= 0) {
                            alert("無暫存資料");
                            return;
                        }
                        var $div = $(html);
                        $div.find(".tempScr-msg").html("請選擇暫存資料");
                        $div.find(".tempScr-yes").val("讀取");
                        $div.find(".tempScr-no").val("刪除");
                        // $div.find(".tempScr-close").val("取消");
                        var win = $div.dialog({
                            title: "請選擇暫存資料",
                            resizable: false,
                            width: 550,
                            maxWidth: 550,
                            modal: true,
                            deactivate: function () {
                                this.destroy(); // remove itself after close
                            },
                            draggable: false,
                            autoOpen: false,
                            position: "center",
                            dialogClass: 'fixed-dialog',
                            show: {
                                effect: "blind",
                                duration: 200
                            },
                            closeOnEscape: true,
                            close: function (event, ui) {
                                winOld = undefined;
                                $(this).dialog("destroy");
                            },
                            open: function (event, ui) {
                                // $(this).parent().find('.ui-dialog-titlebar-close').hide();
                            }
                        });
                        $("#tempScrList").empty();
                        data.tempScrList.forEach((x) => {
                            $("#tempScrList").append(new Option(x.split("/")[x.split("/").length - 1].replace(/\.txt/g, ""), x));
                        });
                        win.dialog("open");
                        $div.find(":button").click(function () {
                            if (this.className.indexOf("tempScr-yes") != -1) {
                                ifxFile.get($("#tempScrList").val(), _self.makeBlockers(), function (data) {
                                    if (data.status) {
                                        _self.fyi('交易恢復...', 1000);
                                        _self.deserialize(data.msg);
                                        _self.testsaveDup(data.msg);
                                        console.log("暫存交易系統變數設定..")
                                        _self.setValue("ISRESUME$", 1);
                                        _self.fyi('交易恢復完成...', 700, 100);
                                        _self.collect(true);
                                    } else {
                                        alert('錯誤:' + data.msg);
                                    }
                                });
                            }
                            if (this.className.indexOf("tempScr-no") != -1)
                                ifxFile.remove($("#tempScrList").val());

                            win.dialog("destroy");
                            winOld = undefined;
                        });
                        winOld = win;
                    }
                },
                complete: function () {
                    console.log("complete done !");
                },
                'error': function (e) {
                    console.error(e);
                }
            });
        };
    })(jQuery);


    function Captcha() {
        this.focus();
        return window.showModalDialog('user/info_html/Captcha/Captcha.html', '',
            'dialogWidth=430px;dialogHeight=140px, toolbar=no, location=no, directories=no, status=yes, menubar=no, scrollbars=no, resizable=no');
    }

    function inputReason(arr) {
        var dfd = $.dialogReason(setHostOvrReasons(arr).join("<br/>"));
        dfd.done(function () {
            _self.tradeReason = $("#reason").val();
            _self.block("Submit ....");
            _self.diyblock("Submit ....");
            _self.ifxHost.setsendBefore(true);
            _self.transmit();
        }).fail(function () {
            $('#btn_yn').attr('disabled', false);
            _self.goCurrentField();
            setTimeout(function () {
                _self.keys.bind(true);
            }, 10);
        });
    }

    function checkSupervisorOvr(hostOvrRqsp) {
        if (_self.getValue("#HCODE") == "1") {
            _self.setValue("RQSP$", "0005");
        } // 更正交易添加原因 暫測 小潘
        if (_self.getValue("#HCODE") == "1" && _self.getValue('LEVEL$') < 3) { // _sysvar['LEVEL']
            // 改
            return false;
        }
        // 更正不需要主管授權 (系統變數)
        // #DEFAULT22=X,2,S T(3,NO)ASGN(CHECKSUP$)
        if (_self.getValue("#HCODE") == "1" && _self.getValue('CHECKSUP$') == "NO") {
            return false;
        }
        if (_self.ifxHost.getsendBefore()) {
            console.log("already send!!");
            return true;
        }
        console.log("hcode:" + _self.getValue("#HCODE"));
        var m = (hostOvrRqsp == undefined) ? _self.getOvrReasons() : setHostOvrReasons(hostOvrRqsp);
        if (_self.getValue("#HCODE") == "1" || m.length > 0) {
            // reset some var
            _self.setValue("SUPNO$", ""); // reset supno$
            _self.setValue("#HSUPCD", 0); // reset #HSUPCD
            console.log("supervisor override");
            var dfd = // 小柯 增 測試 視窗改
                $.dialog2("主管授權", '需主管授權 :<br/>' + m.join("<br/>"), // 柯
                    // \n改<br/>
                    "遠端授權", "本機授權");
            dfd.done(function () { // 按下Yes時
                getRemoteOvr(m);
            }).fail(function () { // 按下No時
                getLocalSupervisor(m);
            });
            // var remote = window.confirm('需主管授權 :\n' + m.join("\n")
            // + ' \n\n[確定]遠端授權, [取消]本機授權');
            // //remote = false;
            // if (remote) {
            // getRemoteOvr(m);
            // } else {
            // getLocalSupervisor(m);
            // }
            return true;
        }
        return false;
    }

    // 顯示提示訊息並且可選擇 確定送出和取消送出
    // 涵式V(Q) 應該也可以做到類似此功能
    function checkWarnbefore() {
        var warnbeforesend = _self.getValue("WARNBFSEND$");
        warnbeforesend = IfxUtl.trim(warnbeforesend);
        if (warnbeforesend != "" && !window.confirm(warnbeforesend)) {
            console.log("cancel submit!! return.");
            return true;
        }
        return false;
    }

    function ovrTransmit(supId, rqspReasons) {
        _rqspReasons = rqspReasons.join(";");
        _self.superVisorR = _rqspReasons;
        _self.setValue("RQSP$", _rqspReasons);
        _self.setValue("SUPNO$", supId);
        // LAI提新增 KINBR進去給原先前面四位空白
        var kinbrstr = _self.getValue("#KINBR");
        _self.setValue("#EMPNOS", supId);
        if (_self.hostOvrMode)
            _self.setValue("#HSUPCD", 1);

        // 此段給有可能 主管放行後才需要特殊功能 START
        _self.block("Submit ...."); // 柯 經測試可能不出現
        _self.diyblock("Submit ...."); // 補這行
        var fakebatch = _self.getValue('FAKEBATCH$').toLowerCase();
        if (fakebatch == "true") {
            specialSubmit();
        } else if (fakebatch == "runs" && _self.def.tim.runrim) { // 特殊eric交易先送最多兩組rim
            runfirstSubmit(_self.def.tim.runrim);
        } else {
            _self.transmit();
        }
        // END
        _self.hostOvrMode = false;
        _rqspReasons = "";
    }

    var _rqspReasons = "";
    Ifx.fn.getRqspReasons = function () {
        return _rqspReasons;
    }

    function getRemoteOvr(reasons) {
        var fnSaveError, fnSaveDone, bean, data;
        fnSaveError = function (data) {
            alert("無法進行遠端授權(error saving screen)\n" + data.errmsg);
            _self.goCurrentField();
            _self.ifxHost.setsendBefore(false);
            return;
        };
        fnSaveDone = function (data) {
            var fn = getTabFn('startOvr'),
                oData = {
                    userId: _self.getValue('BRN$') + _self.getValue('TLRNO$'),
                    txcd: _self.txcd,
                    screenFile: data.id,
                    reasons: reasons,
                    obufgch: _self.getValue('OBUFG$') ? _self.getValue('OBUFG$') : 0
                },
                promise = fn(oData);
            promise.done(function (bOK, supId, supnm) {
                if (!bOK) { // cancelled by teller
                    _self.goCurrentField();
                    return;
                }
                // _self.setValue('MCRR$', supId);
                // 遠端授權是6位 取後兩碼
                // if (supId.length > 2) {
                // supId = supId.slice(4, 6);
                // }
                _self.setValue("SUPNO$", supId);
                _self.setValue("SUPNM$", supnm);
                _self.ovrid = data.id;
                // 合庫提:成功不需要跳提示
                setTimeout(function () {
                    console.log("主管已授權, by:" + supId);
                    // alert("主管已授權, by:" + supId);
                    ovrTransmit(supId, reasons);
                    _self.ifxHost.setsendBefore(true);
                }, 10);
            });
            promise.fail(function (x) {
                alert('主管拒絕授權:' + x);
                _self.goCurrentField();
                _self.ifxHost.setsendBefore(false);
            });
        };
        bean = _contextPath + "/mvc/hnd/screen/save";
        data = {
            brn: _self.getValue('BRN$'),
            tlrno: _self.getValue('TLRNO$'),
            txcd: _self.txcd,
            rqsp: reasons[0] ? reasons[0] : '0001',
            content: JSON.stringify(_self.serialize())
        };
        _self.sendBean(bean, data, "上傳畫面中....", fnSaveDone, fnSaveError);
    } // end getRemoteOvr
    function getLocalSupervisor(reasons) {
        var objParam = {};
        objParam.id = "";
        objParam.pasw = "";
        objParam.teller = _self.getValue("TLRNO$");
        objParam.errmsg = "";
        var superId = $("#_superId"),
            superPassword = $("#_superPassword");
        superId.val('');
        superPassword.val('');
        // 柯 [確認送出]按鈕功能移除
        $('#btn_yn').attr('disabled', true);
        $("#dialog-locovr").dialog({
            autoOpen: true,
            draggable: false,
            resizable: false,
            open: function () { // 新增開啟後bind ENTER按鈕
                $("#dialog-locovr").off("keypress").on('keypress', function (e) {
                    if (e.keyCode == $.ui.keyCode.ENTER) {
                        $(this).parent().find("button:eq(1)").trigger("click");
                    }
                });
            },
            height: 299,
            width: 270,
            modal: true,
            buttons: {
                "OK": function () {
                    if (superId.val() == _self.getValue("#TLRNO")) {
                        alert("不得輸入相同員編");
                        return;
                    }
                    if (superId.val().length > 0 && superPassword.val().length > 0) {
                        _self.ifxHost.setsendBefore(true);
                        verifySupervisor(superId.val(), superPassword.val(), {});
                        $(this).dialog("close");
                    }
                },
                Cancel: function () {
                    $(this).dialog("close");
                }
            },
            close: function () {
                $('#btn_yn').attr('disabled', false); // 柯 [確認送出]恢復按鈕功能
                _self.goCurrentField();
                setTimeout(function () {
                    _self.keys.bind(true);
                }, 10);
            },
            show: {
                effect: "blind",
                duration: 200
            }
        });
        _self.keys.unbindAll();

        // $("#dialog-locovr").dialog("open");
        function verifySupervisor(supid, password, objParam) {
            var supIndex = ["0007", "0017", "0027"];
            var text = {
                SUPID: supid + "",
                PW: password + "",
                ALLOWFG: supIndex.indexOf(reasons[0].substring(0, 4)) + ""
            }
            text = JSON.stringify(text).substring(1);
            //var text = "\"SUPID\":\"" + supid + "\", \"PW\":\"" + password + "\"}";
            _self.sendRim("LCR07", text, function (totaList) {
                console.log("Maybe first block in Super!!!!");
                _self.block("<h2>Submit ....</h2>"); // 柯 新增這個看看是否可以比較ok
                var oTota = totaList[0];
                var name = IfxUtl.substr_big5(oTota.obj.Name, 0, 12);
                // _self.setValue('MCRR$', supid);
                // _self.setValue("SUPNO$", supid);
                _self.setValue("SUPNM$", name);
                //_self.runSysRtn(); // 2014/04/03 應該不需要執行
                ovrTransmit(supid, reasons);
            }, function () {
                _self.goCurrentField();
                _self.ifxHost.setsendBefore(false);
                setTimeout(function () {
                    _self.keys.bind(true);
                }, 10);
            });
        }
    } // getLocalSupervisor
    // swift > 1K
    // 趙: map 不用幫忙擺，var會自己做。
    var _boxDef = {
        wholeData: '#swiftWholeData',
        data: '#swiftData',
        total: '#swiftTOTBOX',
        current: '#swiftCURBOX',
        map: {
            "entseq": "#ENTSEQ<-#TOAENTSEQ",
            "copyno": "#COPYNO<-#TOACCPYNO"
        },
        boxWanted: false,
        boxSize: 1000,
        next: null,
        hasNext: function () {
            return false;
        }
    };

    function initBoxes() {
        _boxDef.boxWanted = false;
        var wholeData = _self.getValue(_boxDef.wholeData);
        if (wholeData.length <= _boxDef.boxSize) return null;
        _boxDef.boxWanted = true;
        var chunks = IfxUtl.chunkString(wholeData, _boxDef.boxSize);
        console.log("swift data length:" + wholeData.length);
        console.log("chunks length:" + chunks.length);
        _self.setValue(_boxDef.total, chunks.length);
        _self.setValue(_boxDef.current, 0);
        _boxDef.next = function () {
            var curr = parseInt(_self.getValue(_boxDef.current), 10);
            _self.setValue(_boxDef.data, chunks[curr]);
            _self.setValue(_boxDef.current, ++curr);
        };
        _boxDef.hasNext = function () {
            return parseInt(_self.getValue(_boxDef.current), 10) < chunks.length;
        };
    }

    // Transmit : host comm
    var _ajaxSender = null;
    Ifx.fn.transmit = function (bFirstTime) {
        var target = this.def.tim.target,
            text, titasix, titaJson, bean, putMap, url, hostType = false; // 柯 新增
        // mq
        bFirstTime = (bFirstTime === undefined) ? true : bFirstTime;
        target = target || 'host';
        resetDocList();
        this.ifxHost.setsendBefore(true);
        if (target == 'host') {
            if (bFirstTime && this.isSwiftTran() && this.def.tim.box == "swift") {
                initBoxes();
            }
            // 柯 新增 mq
            // XW510 XW520?
            if (this.def.tim.way == "mq") {
                hostType = true;
            }
            this.ifxHost.initSend();
            console.log("@@@@transmit initSend");
            if (_boxDef.boxWanted) {
                _boxDef.next();
                this.ifxHost.setNextBox(bFirstTime, this.jnlId);
            }
            // host request supervisor override
            if (_self.hostOvrMode) {
                _self.ifxHost.setHostOvrMode(_self.jnlId);
            }
            var tempbuild = this.buildText(this.def.tim.list || this.def.tim);
            text = tempbuild[0];
            titasix = tempbuild[1];
            titaJson = tempbuild[2];
            console.log("text len   :" + text.length);
            console.log("titasix len:" + titasix.length);
            console.log("titaJson len:" + titaJson.length);
            console.log("this.RIMTESTNO" + this.RIMTESTNO);
            if (!this.RIMTESTNO) {
                if (this.def.tim.handler) {
                    _ajaxSender = this.ifxHost.send(this.txcd, false, text, makeRcvHandler(this.def.tim.handler), null, null, null, hostType, titaJson); // 柯 新增
                } else {
                    _ajaxSender = this.ifxHost.send(this.txcd, false, text, "receiveMain", this, null, null, hostType, titaJson); // 柯 新增
                }
            } else {
                console.log("transmit...");
                alert("上送中心X型態不能包含中文:" + this.RIMTESTNOSTR);
                // 柯 增加三行
                _self.diyunblock();
                _self.unblock();
                _self.ifxHost.setsendBefore(false);
            }
        } else if (target == 'file-grid') {
            console.log("ocjson:" + ocjson);
            _self.receiveBean(ocjson);
        } else if (target == 'file-txt-grid') {
            // 跳過欄位,並塞值 start
            var skipline, slipnum = 0,
                slip2fld = "",
                skipdata = "";
            if (this.def.tim.skipline) {
                skipline = this.def.tim.skipline.split(":");
                slipnum = parseInt(skipline[0], 10);
                slip2fld = (skipline.length > 1) ? skipline[1] : "";
            }
            // END
            // 柯: text組合成 json
            console.log("octext:" + octext);
            var arr = octext.split(/\r\n|\r|\n/);
            // 刪除空值陣列
            for (i = arr.length - 1; i >= 0; i--) {
                if (arr[i].length == 0) {
                    arr.splice(i, 1);
                } else {
                    // 是否有需求真的要一行一行檢查?
                    // break;
                }
            }
            // 轉json格式
            var data = $.map(arr, function (x, i) {
                if (slipnum > i) {
                    skipdata += x;
                    _self.setValue(slip2fld, skipdata);
                } else {
                    return {
                        '字串': x
                    };
                }
            });
            _self.receiveBean({
                list: data
            }); // list固定
        } else if (target == 'host-file') {
            bean = this.def.tim.bean;
            putMap = this.def.tim.put;
            recvHandler = IfxUtl.bind(this, 'displayToScreen');
            // this.sendBean(bean, putMap, null, recvHandler);
            this.sendBean(bean, putMap, null, recvHandler, false, true); // 最後一個參數
            // 特殊用法
        } else {
            bean = this.def.tim.bean;
            putMap = this.def.tim.put;
            recvHandler = IfxUtl.bind(this, 'receiveBean');
            // this.sendBean(bean, putMap, null, recvHandler);
            this.sendBean(bean, putMap, null, recvHandler);
        }
    };

    function makeRcvHandler(hndName) {
        return function (totaList) {
            _self.rcvForwarder(totaList, hndName);
        };
    }

    Ifx.fn.setJnlId = function (jId) {
        this.jnlId = jId;
        console.log("ifx jnl id:" + this.jnlId);
    };
    Ifx.fn.jnlHeader = function () {
        var self = this,
            keys = ['BRN$', 'ADD$', 'TLRNO$', 'DATE$', 'SUPNO$'],
            o = {};
        _.each(keys, function (x) {
            o[x] = self.getValue(x);
        });
        return o;
    };
    Ifx.fn.serialize = function () {
        var kv = {},
            f, inVisiblePages = [];
        rowStatus = [];
        for (var k in this.fieldMap) {
            // console.log('getting ' + k);
            try {
                f = this.getField(k);
                kv[k] = f.serialize();
            } catch (ee) {
                console.log('failed to seraialize ' + k);
            }
        }
        _.each(this.def.display, function (x) {
            if (!x.visible) {
                inVisiblePages.push(x.name);
            } else {
                if (x['hideRows'] != null) {
                    var t = {};
                    t[x.name] = x['hideRows'];
                    rowStatus.push(t);
                }
            }
        });
        return {
            'sysvar': _sysvar,
            'fields': kv,
            'rimcache': JSON.stringify(this.rtn.GETRIMCACHED()), // 柯:新增儲存rim因SETRIMCACHED做parse故先stringify
            'etc': {
                hide: inVisiblePages,
                currentIndex: this.currentIndex,
                rowStatus: rowStatus
            }
        };
    };
    Ifx.fn.deserialize = function (obj, bViewOnly, bOvrData) {
        var self = this,
            o = eval('(' + obj + ')');
        var rimcache = o.rimcache;
        flds = o.fields;
        if (!bViewOnly) {
            for (var k in o.sysvar) {
                self.setValue(k + '$', o.sysvar[k]);
            }
        }
        if (rimcache) {
            self.rtn.SETRIMCACHED(rimcache);
        }
        for (var k in flds) {
            self.getField(k).deserialize(flds[k]);
        }
        _.each(o.etc.hide, function (p) {
            $("#p_" + p).hide();
        });
        if (o.etc.rowStatus != null) {
            var allHideRows = [];
            _.each(o.etc.rowStatus, function (page) {
                for (var pname in page) {
                    _.each(page[pname], function (r) {
                        console.log('#p_' + pname + '_' + r);
                        allHideRows.push('#p_' + pname + '_' + r);
                    });
                }
            });
            if (allHideRows.length > 0) {
                console.log("hide:" + allHideRows.join(','));
                $(allHideRows.join(',')).hide('fast');
            }
        }
        if (!bViewOnly) {
            this.lastFocus = this.currentIndex = o.etc.currentIndex;
            this.goCurrentField();
        }
        // TODO: 主管授權下載畫面的時候,欄位表格等有些沒有隱藏到,故重新處理
        // 待測試
        if (bOvrData) {
            setFkeyandCollect();
        }
    };
    // old sendBean via beanProxy.jsp
    // 柯 新增此段為了 重新載入後 把暫存之資料放入F6功能中
    Ifx.fn.testsaveDup = function (obj, bViewOnly) {
        var self = this,
            o = eval('(' + obj + ')');
        flds = o.fields;
        var m = {};
        for (var k in flds) {
            if (flds[k].a == "hE") { // 經測試應該是 hE
                m[k] = flds[k].v;
            }
        }
        dup.dup(m);
    };
    // old sendBean via beanProxy.jsp
    // Ifx.fn.sendBean = function(bean, putMap, prompt, fnReceive) {
    // var self = this, params;
    // if (_.isString(putMap)) {
    // params = putMap;
    // } else {
    // params = buildInput(this, putMap);
    // params = JSON.stringify(params);
    // }
    // if (prompt == null) {
    // self.block("資料傳送中(" + bean + ")");
    // }
    // $.ajax({
    // type : 'post',
    // dataType : 'json',
    // url : 'beanProxy.jsp',
    // data : {
    // bean : bean,
    // reqJson : params
    // },
    // success : function(data) {
    // if (data.success) {
    // fnReceive(data);
    // } else {
    // alert('Error:\n' + data.errmsg);
    // }
    // },
    // complete : function() {
    // self.unblock();
    // },
    // 'error' : function() {
    // alert("send bean error");
    // }
    // });
    //
    // function buildInput(self, map) {
    // var r = {}, k = null, v;
    //
    // for (k in map) {
    // v = map[k];
    // if (v == null)
    // v = '#' + k;
    // if (isVar(v))
    // v = self.getValue(v);
    // r[k] = v;
    // }
    // return r;
    //
    // }
    // function isVar(n) {
    // return _(n).startsWith('#') || _(n).endsWith('$');
    // }
    // };
    //
    //
    Ifx.fn.sendBean = function (bean, putMap, prompt, fnReceive, fnError, filehost) { // 最後一個參數
        // 特殊用法
        var url, reqData;
        if (/^url:/.test(bean)) {
            url = _contextPath + bean.split(":")[1];
        } else {
            url = bean;
        }
        if (_.isString(putMap)) {
            reqData = putMap;
        } else {
            reqData = buildRequest(putMap);
            reqData = JSON.stringify(reqData);
        }
        _self.block(prompt || "資料傳送中....");
        console.log("url:" + url);
        console.log("prompt:" + prompt);
        $.ajax({
            type: 'post',
            dataType: 'json',
            url: url,
            cache: false,
            data: {
                _d: reqData,
            },
            success: function (data) {
                if (data.success) {
                    if (filehost) { // 最後一個參數 特殊用法
                        var newdata = {
                            "0": data
                        }; // 要0，不然displayscreen 解析會錯
                        fnReceive(_self.panel.output, newdata, false);
                    } else {
                        fnReceive(data);
                    }
                } else {
                    if (fnError) {
                        fnError(data);
                        return;
                    }
                    alert('錯誤:\n' + data.errmsg);
                }
            },
            complete: function () {
                console.log("complete done !");
                _self.unblock();
                _self.diyunblock();
            },
            'error': function () {
                alert("資料錯誤或查無資料:" + url);
                _self.ifxHost.setsendBefore(false);
            }
        });

        function buildRequest(map) {
            var r = {},
                k = null,
                v;
            for (k in map) {
                v = map[k];
                if (v == null) v = '#' + k;
                if (isVar(v)) v = _self.getValue(v);
                r[k] = v;
            }
            return r;
        }

        function isVar(n) {
            return _(n).startsWith('#') || _(n).endsWith('$');
        }
    };
    Ifx.fn.receiveBean = function (data) {
        console.log("receiveBean");
        this.setValue('TXFORM$', data.form);
        this.runAfterYN();
        var printWhat = this.printWhat(); // 柯 新增 FOR getQueryForm
        // 傳printWhat進去
        // 傳printWhat進去
        var gridDef = this.getQueryForm(printWhat);
        if (gridDef != null) {
            var gridData = this.prepareGridData(gridDef, data.list, 'repeat2var', false);
            this.displayGrid(gridDef, gridData, null, data.list2);
        }
        this.postTran();
    };
    Ifx.fn.repeat2var = function (repeat) {
        var getMap = this.def.tim.get,
            k = null,
            v;
        for (k in getMap) {
            v = getMap[k];
            // 新增 excel 動態換字
            if (v && v.toString().slice(0, 1) == "#") {
                v = this.getValue(v);
            }
            if (!v) v = k.slice(1);
            if (!repeat["序號"] && this.getValue("SPEEXCEL$").toUpperCase() == "Y") {
                var fld = this.getField(k);
                var temp = fld.getValue();
                if (repeat[v]) {
                    if (temp.length < 35) {
                        temp = IfxUtl.stringFormatterBig5(temp, 35);
                    }
                    if (temp.length > 35 && temp.length < 70) {
                        temp = IfxUtl.stringFormatterBig5(temp, 70);
                    }
                    if (temp.length > 70 && temp.length < 105) {
                        temp = IfxUtl.stringFormatterBig5(temp, 105);
                    }
                    if (temp.length > 105 && temp.length < 140) {
                        temp = IfxUtl.stringFormatterBig5(temp, 140);
                    }
                    console.log("temp:" + temp + (repeat[v] || ''));
                    this.setValue(k, temp + (repeat[v] || ''));
                }
            } else {
                if (repeat[v] && repeat[v].toString().indexOf("\n") != -1) {
                    var ss = repeat[v].split("\n");
                    for (var i = 0; i < ss.length; i++) {
                        ss[i] = IfxUtl.stringFormatterBig5(ss[i], 35);
                    }
                    repeat[v] = ss.join("");
                    console.log("after 換行 in excel:" + repeat[v]);
                }
                this.setValue(k, repeat[v] || '');
            }
        }
    };
    Ifx.fn.makeBlockers = function () {
        return {
            'block': IfxUtl.bind(this, 'block'),
            'unblock': IfxUtl.bind(this, 'unblock')
        };
    };
    Ifx.fn.openInfohtml = function (filename) {
        function openInfoWindow(filename) {
            var url = "user/info_html/" + filename; // 說明文件的路徑
            window.showModalDialog(url, window, 'resizable=yes;help=no;center=yes;status=no;scroll=yes;edge=sunken');
        }

        openInfoWindow(filename);
    };
    Ifx.fn.openRrportjsp = function (day, filebrno, filetlrno, filename, pagenum, where) {
        function openDupWindow(day, filebrno, filetlrno, filename, pagenum, where) {
            var width = 1100;
            var height = 718;
            var url = "batch_report.jsp?";
            if (pagenum) {
                url += "pagenum=" + pagenum + "&";
            }
            if (day) {
                url += "dt=" + day + "&";
            }
            if (filebrno) {
                url += "filebrno=" + filebrno + "&";
            }
            if (filetlrno) {
                url += "filetlrno=" + filetlrno + "&";
            }
            if (filename) {
                url += "filename=" + filename + "&";
            }
            if (where) {
                url += "gowhere=" + where + "&";
            }
            if (!window.showModalDialog) {
                window.showModalDialog = function (url, name, option) {
                    if (window.hasOpenWindow) {
                        window.newWindow.focus();
                    }
                    var re = new RegExp(";", "g");
                    var option = option.replace(re, '","'); // 把option转为json字符串
                    var re2 = new RegExp(":", "g");
                    option = '{"' + option.replace(re2, '":"') + '"}';
                    option = JSON.parse(option);
                    var openOption = 'width=' + parseInt(option.dialogWidth) + ',height=' + parseInt(option.dialogHeight) + ',left=' + (window.screen.width - parseInt(option.dialogWidth)) / 2 + ',top=' + (
                        window.screen.height - 30 - parseInt(option.dialogHeight)) / 2;
                    window.hasOpenWindow = true;
                    window.newWindow = window.open(url, name, openOption);
                }
            }
            window.showModalDialog(url, window, 'dialogWidth:' + width + 'px;dialogHeight:' + height + 'px;resizable:yes;help:1;maximize:1;center:yes;status:no;scroll:yes;edge:sunken');
        }

        openDupWindow(day, filebrno, filetlrno, filename, pagenum, where);
    };
    // 下載&檢視中鋼xml檔案
    Ifx.fn.downloadCsteelxml = function (filename, getfileext) {
        var url = _contextPath + "/mvc/hnd";
        url += "/web/csteeldownload/" + filename + "/" + getfileext;
        iframeAppend(url);
    };
    // 下載央媒檔案
    Ifx.fn.downloadYangmei = function (dt, filename, getfileext) {
        var url = _contextPath + "/mvc/hnd";
        if (!filename && !getfileext) {
            url += "/yangmei/downloadlist/" + dt;
        } else {
            // 因為單筆的是空的副檔名，故需要提供後給端末尋找下載
            url += "/yangmei/download/" + dt + "/" + filename + "/" + getfileext;
        }
        iframeAppend(url);
    };
    // 下載任何檔案
    Ifx.fn.downloadFilebypath = function (filepath) {
        var url = _contextPath + "/mvc/hnd";
        filepath = filepath.replace(/\\/g, "&");
        filepath = filepath.replace(/\//g, "&");
        filepath = filepath.replace('.', '$');
        url += "/filedownload/path/" + filepath;
        iframeAppend(url);
    };
    // 直接檢視PDF
    Ifx.fn.viewPdf = function (sno, fileType, itemN) {
        var ip = location.host.split(":");
        var port = ip.length > 1 ? ip[1].substring(0, 3) + "5" : "7005";
        var url;
        if (fileType != "")
            url = location.protocol + "//" + ip[0] + ":" + port + "/iTX/mvc/hnd/download/file/" + _self.getValue('TLRNO$') + "/" + sno + "/" + fileType + "/" + encodeURI(itemN);
        else
            url = location.protocol + "//" + ip[0] + ":" + port + "/iTX/mvc/hnd/download/file/" + sno;

        if (fileType != "")
            $.ajax({
                type: 'get',
                dataType: 'text',
                url: location.protocol + "//" + ip[0] + ":" + port + "/iTX/mvc/hnd/download/file/" + _self.getValue('TLRNO$') + "/" + sno + "/" + fileType + "/" + "000_111",
                cache: false,
                success: function (data) {
                    if (data == "1") {
                        alert("此報表未設定CdReport");
                        // var win = window.open(url);
                        // window.focus();
                    } else {
                        var win = window.open(url);
                        window.focus();
                    }
                },
                complete: function () {
                    console.log("complete done !");
                },
                'error': function (e) {
                    console.error(e);
                }
            });
        else {
            var win = window.open(url);
            window.focus();
        }
    }

    Ifx.fn.printReport = function (reportNo, printer, serverIp, callBack, callError) {
        var ip = location.host.split(":");
        var port = ip.length > 1 ? ip[1].substring(0, 3) + "5" : "7005";
        var url = location.protocol + "//" + ip[0] + ":" + port + "/iTX/mvc/hnd/printReport";
        //var url = "http://" + "192.168.10.100" + ":" + "8080" + "/iTX/mvc/hnd/printReport";
        var url2 = "http://" + serverIp + ":8090/St1Printer/";
        var param = {};
        param["reportNo"] = reportNo;
        param["printer"] = printer == undefined ? "DefaultPrinter" : printer;
        param["localIp"] = _self.getValue("IP$");
        param["Log"] = "1";
        _self.block("列印資料傳送中....");
        _self.diyblock("列印資料傳送中....");
        $.ajax({
            type: 'post',
            dataType: 'json',
            url: url,
            cache: false,
            data: {
                _d: JSON.stringify(param)
            },
            success: function (data) {
                if (data.success) {
                    url2 = "http://" + data.ServerIp + ":8090/St1Printer/";
                    synctakoPrintRelease();
                    async function synctakoPrintRelease() {
                        for (var i = 0; i < data.printList.length(); i++) {
                            await new Promise((resolve, reject) => {
                                takoPrintRelease(data.printList[i], resolve, reject);
                            });
                        }
                    }
                    /*
                    data.printList.forEach((x) => {
                        takoPrintRelease(x);
                    });
                    */
                } else {
                    alert(data.msg);
                }
                _self.unblock();
                _self.diyunblock();
            },
            complete: function () {
                console.log("complete done !");
            },
            'error': function (e) {
                console.error(e);
                _self.unblock();
                _self.diyunblock();
            }
        });

        function takoPrintRelease(param, resolve, reject) {
            $.ajax({
                type: 'post',
                dataType: 'json',
                url: url2,
                cache: false,
                data: JSON.stringify(param),
                success: function (data) {
                    if (data.Result != "S") {
                        alert("印表錯誤 : " + data.Result + " " + data.resultDesc);
                        if (reject)
                            reject();
                    } else if (resolve)
                        resolve();

                    console.log(data);
                },
                complete: function () {
                    console.log("complete done !");
                },
                'error': function (e) {
                    alert("印表機連線錯誤!請檢查周邊程式或印表機");
                    console.error(e);
                }
            });
        }
    }

    function iframeAppend(url) {
        var iframe = document.createElement("iframe");
        iframe.setAttribute("src", url);
        iframe.setAttribute("style", "display: none");
        document.body.appendChild(iframe);
    };
    Ifx.fn.changSysVar = function (name, value, prompt) {
        var bean = 'url:/mvc/hnd/dosys/sysvar';
        var data = {
            name: name,
            value: value
        };
        _self.sendBean(bean, data, prompt, function (data) {
            // on success
            console.log(data.msg);
        }, function (data) {
            // on error
            console.log(data.msg);
        });
    };
    Ifx.fn.dosysAction = function (actionType, prompt, shellname, shellpara) {
        var bean = 'url:/mvc/hnd/dosys/action';
        var data = {
            what: actionType,
            shellname: shellname,
            para: shellpara
        };
        _self.sendBean(bean, data, prompt, function (data) {
            // on success
            console.log(data.msg);
        }, function (data) {
            // on error
            console.log(data.msg);
        });
    };
    Ifx.fn.trySave = function (bPrompt) {
        if (!_self.isSavable()) {
            if (bPrompt === true) {
                alert('交易已完成或交易不可暫存。');
            }
            return;
        }
        if (_self.isSwiftTran()) { // swift不知可否暫存,目前測試先在len=0中擋住#_SWIFTFORM_欄位..
            console.log("swift相關交易不可暫存。");
            // return; 暫時移除 不清楚可否儲存
        }
        $.dialogscrTempName(bPrompt);
        // _self.saveTran(bPrompt);
    };
    // 關閉上方輸入畫面
    Ifx.fn.tryentryHide = function (dohide) {
        // 查詢類呼叫隱藏上方 self.panel.entry 的功能
        $('#xbtn_entryhide').show(); // 開啟按鈕
        if (dohide || $(_self.panel.entry).css("display") != "none") {
            console.log("...隱藏輸入畫面...");
            $(_self.panel.entry).hide();
            $(_self.panel.help).hide();
            _self.keys.enableNaviKeys(false); // for help往上按時出現
            $('#xbtn_save').hide(); // 隱藏時儲存會導致欄位錯誤,故隱藏
        } else {
            console.log("...顯示輸入畫面...");
            $(_self.panel.entry).show();
            _self.goCurrentField();
            _self.keys.enableNaviKeys(true);
            if (_self.getValue("#FUNCIND") != "5") $('#xbtn_save').show();
        }
    };
    Ifx.fn.isSaved = function () {
        return !this.isSavable() || this.saved;
    };
    Ifx.fn.isSavable = function () {
        return (this.tranEnd != true && this.txcd != 'XX004');
    };
    Ifx.fn.saveTran = function (bPrompt) {
        // keys=['BRN$', 'ADD$','TLRNO$','DATE$','SUPNO$'],
        //_self.block("儲存中請稍後..."); // 柯 經測試可能不出現
        _self.diyblock("儲存中請稍後..."); // 補這行
        // var fn = getTabFn('getTabTitle');
        // var num = fn().match(/\(\d+\)/g);
        // num = num == null ? null : num[0].replace(/\(|\)/g, "");
        var screenData = JSON.stringify(_self.serialize()),
            filename = _self.getTmpFilename(bPrompt);
        setTimeout((x) => {
            ifxFile.put(filename, screenData, _self.makeBlockers(), function (data) {
                if (data.status) {
                    _self.saved = true;
                    _self.unblock();
                    _self.diyunblock();
                    //_self.fyi('交易已儲存', 1500, 100);
                    // alert('交易儲存成功...');
                    // end
                } else {
                    alert('錯誤:' + data.msg);
                }
            });
        }, 500);

    };
    Ifx.fn.screenCopy = function (ovrId) {
        $(".field_input").attr("disabled", "true");
        $("[id^='btn_']").hide(); // submit
        $("[id^='xbtn_']").hide(); // cancel, else
        var fnCopyError, fnCopyDone, bean = _contextPath + "/mvc/hnd/screen/get/" + ovrId;
        fnCopyError = function (data) {
            alert("畫面複製錯誤:" + data.errmsg);
        };
        fnCopyDone = function (data) {
            _self.deserialize(data.buffer, true, true);
        };
        _self.sendBean(bean, {}, "下載交易畫面中....", fnCopyDone, fnCopyError);
    };
    Ifx.fn.resumeTran = function (filename) {
        var scrObj = {};
        scrObj["brTlrNo"] = filename;
        scrObj["txCode"] = _self.txcd;
        $.dialogTempScr(scrObj);
    };
    // begin ovr
    var ovrTags = ["RQSP$", "RQSP1$", "RQSP2$", "RQSP3$", "RQSP4$", "RQSP5$", "RQSP6$", "RQSP7$", "RQSP8$", "RQSP9$"];

    function setHostOvrReasons(arrHostRqsp) {
        var arr = [];
        $.each(arrHostRqsp, function (i, x) {
            // if (x[0] != "0000") {
            // x[1] = _self.getOvrReasonFromHelp(x[0]);
            // }
            x[1] = IfxUtl.rtrim(x[1]); // 柯 增加去空白
            arr.push(x[0] + ' ' + x[1].replace(/\s/g, ""));
        });
        return arr;
    }

    Ifx.fn.getOvrReasons = function () {
        var rr = {},
            value;
        _.each(ovrTags, function (n) {
            value = _self.getValue(n);
            var valueID = value.substring(0, 4); // 柯 主管授權代碼
            console.log("getOvrReasons:" + valueID + ":" + value);
            if (valueID != "0000" && IfxUtl.trim(valueID) != "") {
                rr[value] = _self.getOvrReasonFromHelp(value);
            } else if (valueID == "0000" && value.length > 4) { // 柯 主管授權自行設定值
                // T(3,0000測試主管授權)
                rr[0000] += value;
            }
        });
        var arr = [];
        for (var k in rr) {
            if (rr[0000] && rr[0000].split("0000").length > 1) {
                for (var i = 1; i < rr[0000].split("0000").length; i++) {
                    var tempal = rr[0000].split("0000");
                    tempal[i] = IfxUtl.rtrim(tempal[i]); // 柯 增加去空白
                    arr.push('0000 ' + tempal[i]);
                }
            } else {
                rr[k] = IfxUtl.rtrim(rr[k]); // 柯 增加去空白
                arr.push(k + ' ' + rr[k]);
            }
        }
        return arr;
    };
    var ovrReasonMap = null;
    Ifx.fn.getOvrReasonFromHelp = function (code) {
        if (ovrReasonMap == null) {
            ovrReasonMap = this.help.getOvrReasonMap();
        }
        var m = ovrReasonMap[code];
        m = m || '';
        return m;
    };
    // end ovr
    Ifx.fn.dialog = function (title, content, btns, opts) {
        _self.keys.unbindAll();
        myDialog(title, content, btns, function () {
            _self.keys.bind(true);
        }, opts);
    };
    Ifx.fn.getTmpFilename = function (num) {
        var filename = "scr/" + this.getValue('BRN$') + this.getValue('TLRNO$');
        filename += '/' + this.txcd + (num == null || num == undefined ? "_0" : "_" + num) + '.txt';
        return filename;
    };
    // 給做規格書使用
    Ifx.fn.getTmpFilenamefake = function () {
        var filename = "datasheet/" + this.getValue('BRN$') + this.getValue('TLRNO$');
        filename += '/' + this.txcd + '.txt';
        return filename;
    };
    // 給 開起說明網頁使用
    Ifx.fn.getExplainFilename = function () {
        var exname = "mini/explain/" + this.txcd;
        return exname;
    };
    // 柯 增第5個參數 傳入time out 時間
    Ifx.fn.sendRim = function (rimName, text, fnSuccess, fnError, etc, bMq) {
        var self = this;
        this.rimSent = true; // 小柯 補增這行
        // for psbk
        var timeouttime = 0;
        var psbkReturn = false;
        var hostType = false;
        if (typeof etc === "number") {
            timeouttime = etc;
        } else if (typeof etc === "object") {
            if (etc['timeouttime'] != undefined) {
                timeouttime = etc['timeouttime'];
            }
        }
        // 移除 this.def.tim.way == "mq" 的判斷
        if (bMq == true) {
            hostType = true;
        }
        this.ifxHost.initSend(typeof etc === "object" ? etc : undefined, true); // rim
        console.log("this.RIMTESTNO-sendRim-須注意rim x型態不能上中文");
        _ajaxSender = this.ifxHost.send(rimName, true, text, function (totaList) {
            // self.rimSent = false; //小柯 刪除這行
            // unBlock();
            var list = [];
            var i = 0;
            var totaObj = totaList[i];
            if (totaList.length > 1)
                totaObj = _self.rcvErrorHandler(totaList)[0];
            if (totaObj.isError()) {
                if (!_self.batchMode) {
                    //					alert("錯誤訊息:" + totaObj.getErrmsg());
                    alert(totaObj.getErrmsg());
                }
                if (fnError) {
                    fnError(totaObj, 'E');
                }
                var currentField = self.getCurrentField();
                if (currentField.name == "btn_yn") {
                    console.log("Is in btn_yn,return.")
                    return;
                }
                if (currentField.id() == null) {
                    if (self.lastFocus == null) {
                        setTimeout(function () {
                            closeMe(_layout, self.txcd);
                        }, 1000);
                    }
                }
                return;
            } else if (totaObj.isWarnning()) {
                var w = [];
                $.each(totaList, function (i, x) {
                    console.log("totaList.isWarnning?:" + x.isWarnning()); // 修改整段isWarnning中邏輯
                    // for調rim時
                    // warnning
                    if (x.isWarnning()) {
                        console.log("Warnning Errmsg:" + x.getWarnMsg());
                        w.push(w.length + 1 + "." + x.getWarnMsg());
                    } else {
                        console.log("good from Warnning");
                        console.log("##### rom:" + x.text);
                        if (fnSuccess) {
                            // fnSuccess(totaObj);
                            fnSuccess(totaList); // 柯, x 改成totaList
                        }
                    }
                });
                if (!_self.batchMode) {
                    alert("警告訊息:\n" + w.join("\n"));
                    _self.rtn.setIntervalChkOff();
                    setTimeout(function () {
                        _self.KeyForward('S() Warnning');
                    }, 0);
                }
            } else {
                if (rimName == "LCR01" || rimName == "LCR04")
                    _self.xxrjnlid = totaObj.getJnlId();
                else if (rimName == "LCR02" || rimName == "LCR04") {
                    // 依照TOTA回傳KEY重新給值
                    _self.setValue("ORELNO$", totaObj.getJnlId());

                    /* 潘 修改審核時抓到最新一筆紀錄 */
                    if (rimName == "LCR04")
                        _self.xxrjnlid = totaObj.getJnlId();
                }
                console.log("good");
                console.log("##### rom:" + totaObj.text);
                if (fnSuccess) {
                    // fnSuccess(totaObj);
                    fnSuccess(totaList);
                }
            }
        }, null, function (ajaxError) {
            if (fnError) fnError(ajaxError, 'E');
        }, timeouttime, hostType); // 柯 新增 mq
    };

    function myAlert(t) {
        $('#_errtext').text(t);
        $("#dialog-message").dialog({
            modal: false,
            buttons: {
                Ok: function () {
                    $(this).dialog("close");
                }
            }
        });
    }

    var tempIndex = 0;
    Ifx.fn.createTempFld = function (name, len, type) {
        len = (len === undefined) ? 1 : len;
        type = (type === undefined) ? "X" : type;
        var tmp = {
            'name': "#_tmp_" + name + "_" + (tempIndex++),
            'type': type,
            'len': len,
            'dlen': 0,
            'attr': 'S'
        };
        var o = new _FieldRunTime(tmp);
        o.init("tmp");
        return o;
    };

    function checkX(name) {
        var reX = /X\((\d+)\)/;
        return name.match(reX);
    }

    function checkN(name) {
        var reX = /N\((\d+)\)/;
        return name.match(reX);
    }

    Ifx.fn.getFieldForTom = function (name) {
        var z = checkX(name);
        if (z) {
            var len = parseInt(z[1], 10);
            return this.createTempFld("tom_x", len);
        } else {
            return this.getField(name);
        }
    };
    // 柯 build RESV 需要第二項參數為真 因需取消R6上對C欄位的加減(長度)
    Ifx.fn.timFieldToHost = function (name, resv) {
        console.log("##timFieldToHost-resv=" + resv);
        var z = checkX(name);
        var n = "";
        var fhost;
        if (z) {
            n = parseInt(z[1], 10);
            return (new Array(n + 1)).join(" ");
        }
        z = checkN(name);
        if (z) {
            n = parseInt(z[1], 10);
            return (new Array(n + 1)).join("0");
        }
        if (!resv) {
            if (this.getField(name).toHost(false, true) == "X-type-No-Chinese2") { // 寫法看有無要更改
                console.log("##timFieldToHost,X,Chinese2" + this.getField(name).name);
                this.RIMTESTNOSTR = this.RIMTESTNOSTR + this.getField(name).name + ":" + this.getField(name).value() + ".\n";
                // 上中心檢查中文 潘
                this.RIMTESTNO = true;
                this.RIMTESTNO = false;
            }
        }
        fhost = this.getField(name).toHost(resv);
        return fhost;
    };
    // 柯 build RESV 需要第二項參數為真 因需取消R6上對C欄位的加減(長度)
    Ifx.fn.buildText = function (array) {
        console.log("##buildText");
        var result = [],
            name;
        var resultJson = "{";
        this.RIMTESTNO = false;
        var resultrsix = [];
        this.RIMTESTNOSTR = "";
        for (var i = 0; i < array.length; i++) {
            name = array[i];
            console.log("name" + name + "array.length" + array.length + " name.charAt(0)" + name.charAt(0));
            if (name.charAt(0) == "#") {
                if (name.slice(0, 3) === '#__') {
                    continue;
                } else {
                    result.push(this.timFieldToHost(name));
                    resultrsix.push(this.timFieldToHost(name, true));
                    resultJson += '"' + name.substring(1) + '"' + ":" + '"' + this.timFieldToHost(name, true).replace(/\r\n|\n/g, "$n") + '",'
                }
            } else {
                result.push(name);
                resultrsix.push(name);
            }
        }
        // NBSDY && NNBSDY
        //resultJson += '"' + "NBSDY" + '"' + ":" + '"' + _self.getValue("NBSDY$") + '",';
        //resultJson += '"' + "NNBSDY" + '"' + ":" + '"' + _self.getValue("NNBSDY$") + '",';

        // 潘 退回理由
        if (_self.getValue("#Reject").trim() != "")
            resultJson += '"' + "Reject" + '"' + ":" + '"' + _self.getValue("#Reject").trim().replace(/\r\n|\n/g, "$n") + '",';
        // 潘 交易理由
        if (_self.tradeReason.trim() != "")
            resultJson += '"' + "TxReason" + '"' + ":" + '"' + _self.tradeReason.trim().replace(/\r\n|\n/g, "$n") + '",';

        if (_self.getValue("SPANDY$").trim() != "")
            resultJson += '"' + "SPANDY" + '"' + ":" + '"' + _self.getValue("SPANDY$").trim() + '",';

        if (resultJson != "{") resultJson = resultJson.substr(0, resultJson.length - 1) + "}";
        else resultJson = "";
        return [result.join(""), resultrsix.join(""), resultJson];
    };

    function getResvFields() {
        var array = [];
        $.each(_self.dcFields, function (i, x) {
            if (x.attr === "O" || x.attr === "L") array.push(x.name);
        });
        console.log("resv:" + array);
        // TODO filter resv-off fields
        if (_self.def["resv_off"]) {
            var excludeFields = _self.def["resv_off"].list;
            array = $.grep(array, function (x) {
                return $.inArray(x, excludeFields) == -1; // 柯 新增 == -1
                // 才會"每一個"真的off!!
                // 才會"每一個"真的off!!
            });
            console.log("resv_off:" + array);
        }
        return array;
    }

    Ifx.fn.buildResv = function () {
        // var array = this.def.resv ? this.def.resv.list : [];
        var resvWanted = _self.getValue('RESV$');
        var rimCached = _self.rtn.GETRIMCACHED(); // 柯新增
        var maybedest = JSON.stringify(rimCached);
        maybedest = "[[[" + maybedest + "]]]";
        if (!resvWanted == "1") return "";
        console.log("buildResv-getResvFields");
        var array = getResvFields();
        // 柯 build RESV 需要第二項參數為真 因需取消R6上對C欄位的加減(長度)
        var resv = this.buildText(array)[2];
        console.log("buildResv-resv:" + resv);
        var a = document.querySelectorAll('tr');
        var b = document.querySelectorAll('td');
        var c = document.querySelectorAll('input');
        var disnone = "!!!";
        a.forEach(function (x) {
            if (x.id) {
                var dis = $("#" + x.id).css("display").trim();
                if (dis == "none") disnone += ("#" + x.id + ",");
            }
        });
        b.forEach(function (x) {
            if (x.id) {
                var dis = $("#" + x.id).css("display").trim();
                if (dis == "none") disnone += ("#" + x.id + ",");
            }
        });
        c.forEach(function (x) {
            if (x.id) {
                var dis = $("#" + x.id).css("display").trim();
                if (dis == "none") disnone += ("#" + x.id + ",");
            }
        });
        return maybedest + resv + disnone;
    };
    Ifx.fn.rcvErrorHandler = function (totaList, oBatchBag) {
        oBatchBag = oBatchBag || {};
        oBatchBag.success = true;

        function showError(s) {
            if (_self.batchMode) {
                oBatchBag.success = false;
                oBatchBag.errmsg = s;
            } else {
                //				alert("錯誤訊息:" + s);
                alert(s);
            }
            // 柯 新增 這段 start 因再次按enter 會有問題
            if ($("#btn_yn").length > 0) {
                _self.ifxHost.setsendBefore(false);
                console.log("RE Focus to #btn_yn 送出");
                $("#btn_yn").focus();
            }
        }

        function showWarnning(totaListw) {
            console.log("check totaListw isWarnning? Bind!");
            $.each(totaListw, function (i, x) {
                console.log("totaList.isWarnning?:" + x.isWarnning()); // 修改整段isWarnning中邏輯
                // for調rim時
                // warnning
                if (x.isWarnning()) {
                    console.log("Warnning Errmsg:" + x.getWarnMsg());
                    w.push(w.length + 1 + "." + x.getWarnMsg());
                }
            });
            if (!_self.batchMode) {
                alert("警告訊息:\n" + w.join("\n"));
            }
        }

        var list = [];
        var w = [];
        for (var i = 0; i < totaList.length; i++) {
            var totaObj = totaList[i];
            if (totaObj.isError()) {
                if (totaObj.isCE999()) {
                    //					alert("錯誤訊息:" + totaObj.getErrmsg() + "\n按 [確定傳送] 再次送出");
                    alert(totaObj.getErrmsg() + "\n按 [確定傳送] 再次送出");
                    _self.ifxHost.setsendBefore(false); // 取消sendBefore
                } else {
                    if (totaObj.getTxForm() == "EC998") {
                        setTimeout(function () {
                            _self.ifxHost.setsendBefore(false); // 取消sendBefore
                            processHostOvr(totaObj, true);
                        }, 1);
                        return false;
                    }
                    showError(totaObj.getErrmsg());
                }
                return null;
            }
            if (totaObj.isWarnning()) {
                // 因showWarning()會集合整個警告訊息,故跳出
                if (w.length > 0) {
                    continue;
                }
                showWarnning(totaList);
                continue;
            }
            // 柯 EC999 = 主管授權
            if (totaObj.getTxForm() == "EC999") {
                setTimeout(function () {
                    _self.ifxHost.setsendBefore(false); // 取消sendBefore
                    processHostOvr(totaObj);
                }, 1);
                return false;
            }
            if (this.def.tom[totaObj.getTxForm()] || totaObj.getTxForm() == "GM001") {
                list.push(totaObj);
            } else {
                console.warn("unknown tota form:" + totaObj.getTxForm());
                if (this.unknownFormAction != '0') alert('Warnning:收到未定義之tota form:[' + totaObj.getTxForm() + ']');
            }
        }
        return list;
    };

    function processHostOvr(oTota, isReason) {
        var arr = [];
        if (oTota.isError() && oTota.getTxForm() == "EC998") {
            var t = {};
            t["NO"] = "";
            t["MSG"] = oTota.getErrmsg(isReason);
            arr.push(t);
        } else
            arr = oTota.obj.occursList;
        arr = _self.ifxHost.parseHostOvrRequest(arr, isReason);
        _self.hostOvrMode = true;
        if (isReason)
            inputReason(arr);
        else
            checkSupervisorOvr(arr);
    }

    Ifx.fn.getTotaMode = function (formName) {
        console.log("getTotaMode for form:" + formName);
        var totaMode = 0;
        $.each(this.def.rtns, function (i, x) {
            // o = new _FieldRunTime(x);
            if (x.type == "form" && x.name == formName) {
                totaMode = x['tota-mode'] || '0'; // 柯: 幾乎都是0
                return false; // break $.each
            }
        });
        console.log(formName + " tota-Mode:" + totaMode);
        return totaMode;
    };
    // tim handler
    Ifx.fn.rcvForwarder = function (totaList, hndName) {
        var self = this,
            list = [];
        list = this.rcvErrorHandler(totaList);
        if (list == null) {
            self.unblock(); // 柯 新增這行 因為有些會卡住?
            return;
        }
        var actor = parseHanlderName(hndName);
        setTimeout(function () {
            actor.fn(actor.cmd, list);
        }, 0);

        function parseHanlderName(name) {
            var ss, rtnQName, fnRtn = null,
                errmsg = null;
            ss = name.split(',');
            rtnQName = ss[0];
            if (rtnQName.indexOf('@') == -1) {
                fnRtn = self[rtnQName];
                if (fnRtn === undefined) {
                    errmsg = "programmer bug!!\n no such rtnQName:" + rtnQName;
                }
            } else {
                errmsg = "programmer bug!!\ndon't know how to process:" + rtnQName;
            }
            if (errmsg) {
                alert(errmsg);
                throw errmsg;
            }
            return {
                cmd: ss[1],
                fn: fnRtn
            };
        }
    };
    Ifx.fn.receiveMain = function (totaList, fnCallback) {
        // unBlock();
        console.log("receiveMain done");
        _self.unblock();
        _self.diyunblock();
        var list = [];
        list = this.rcvErrorHandler(totaList);
        if (!list) {
            // 交易送出後傳失敗訊息回來畫面無法編輯
            // this.postTran();
            return;
        }
        // 潘 印鑑掛失在submit之後 只有在登入時需呼叫
        if ((_self.txcd == "XS150" || _self.txcd == "XS050") && _self.fkey == "0") {
            var accountNumber = _self.getValue('#ACTNO');
            var userID = _self.getValue('#CIFKEY');
            var userName = "1234";
            if (_self.txcd == "XS150" && _self.getValue("#TRANS") == "1" && (_self.getValue("#STPCD") == "00" || _self.getValue("#STPCD") == "02")) deviceX.seal.reportLoss(accountNumber, userID, userName);
            if (_self.txcd == "XS150" && _self.getValue("#TRANS") == "2" && (_self.getValue("#STPCD") == "00" || _self.getValue("#STPCD") == "02")) deviceX.seal.revokeLoss(accountNumber, userID, userName);
            if (_self.txcd == "XS050" && _self.getValue("#XSCNT") == "1") deviceX.seal.closeAccount(accountNumber, userID, userName);
        }
        if (_self.fkey == "0" && _self.isUpdatedTran(_self.getValue("#TXCD")) && _self.getValue("CHAIN$") == "1" && _self.getValue("CAUTO$") == 1) {
            var fn = getTabFn('getTabTitle');
            var title = fn();
            fn = getTabFn("setAutoSunmitFg");
            fn(title);
        }
        if (_self.fkey == "2" && $("#btn_new1").val() == "退回") $("#btn_new1").hide();
        if (_boxDef.boxWanted && _boxDef.hasNext()) {
            console.log("box mode, try sending next chunk");
            var oTota = totaList[totaList.length - 1]; // if totalist have
            // warning ...
            console.log("oTota:" + oTota.getLastText());
            console.log("oTota:" + oTota.getTxForm());
            oTota.toSysVar(); // set sysvar from tota
            // if(oTota.text){
            _self.processTom(oTota);
            // }
            // 趙: 不用幫忙擺，var會自己做。
            // for ( var k in _boxDef.map) {
            // var pp = _boxDef.map[k].split("<-");
            // _self.copyTo(pp[1], pp[0]);
            // }
            // _self.setValue("#ENTSEQ", _self.getValue("#TOAENTSEQ"));
            // _self.setValue("#COPYNO", _self.getValue("#TOACCPYNO"));
            setTimeout(function () {
                _self.transmit(false);
            }, 0);
            return;
        }
        _self.block("<h2>output processing ....</h2>");
        setTimeout(function () {
            _self.processOutput(list, fnCallback);
        }, 0);
        // _self.escRim = {}; // 潘 清除離開按鈕rim
        _self.setEscRim("");
        return;
        // this.postTran();
    };
    /*
	 * 先掠過此段 等針的有印表機之後再處理 Ifx.fn.prepareOutput = function(outputList) { if
	 * (this.prt) { // 真的有印表機了 var mandatoryList = [], optionList = [];
	 * $.each(outputList, function(i, x) { if (x.mandatory)
	 * mandatoryList.push(x); else optionList.push(x); });
	 *
	 * this.printDocs(mandatoryList); if (optionList.length > 0) {
	 * this.displayOutput(this.panel.output, optionList); } } else {
	 * this.displayOutput(this.panel.output, outputList); } };
	 */
    var lastDocList = [];

    function resetDocList() {
        lastDocList = [];
        if (grid != null) {
            grid.unload();
            grid = null;
        }
        $(_self.panel.output).slideUp().html('');
        $(_self.panel.query).slideUp();
        $(_self.panel.query2).slideUp();
        // $(_self.panel.queryHeader).slideUp();
        $(_self.panel.queryHeader).hide().html('');
    }

    Ifx.fn.displayOutput = function (p, outputList, bDup, trandocScan) {
        // 是否使用假的存摺$功能
        // 柯 新增給BATCH時的特殊需求 (整批列印XT90B & 單獨列印單據XS964)
        if (_self.eachSend) trandocScan = true;
        // 柯新增 解析
        if (bDup) {
            var tempdublsit = [];
            $.each(outputList, function (i, x) {
                if (x === undefined)
                    return;
                var object3 = extend({}, x, JSON.parse(x.parameter));
                console.dir(object3);
                tempdublsit.push(object3);

            });

            function extend(target) {
                var sources = [].slice.call(arguments, 1);
                sources.forEach(function (source) {
                    for (var prop in source) {
                        if (source[prop] == "true") {
                            target[prop] = true;
                        } else if (source[prop] == "false") {
                            target[prop] = false;
                        } else {
                            target[prop] = source[prop];
                        }
                    }
                });
                return target;
            }

            if (tempdublsit) {
                outputList = tempdublsit;
                console.dir(outputList);
            }
        }
        // end
        var useFakePB = false;
        if (!useFakePB) {
            var mandatoryList = [],
                optionList = [];
            $.each(outputList, function (i, x) {
                // 整批在列印時 不知為何有多一個undefined
                if (!x) {
                    console.log("no this outputList!");
                    return;
                }
                if (_self.onlyprint && x.realform != _self.onlyprint) {
                    console.log("only print " + _self.onlyprint + "! ..return");
                    return;
                }
                console.log("displayOutput fkey:" + _self.fkey);
                // trandocScan 整批重印單據時一律印
                // 進入交易 XX909時重印單據 全部都option
                if ((x.mandatory || trandocScan) && _self.fkey != 8 && _self.fkey != 9) { // 交易明細and
                    // Journal
                    mandatoryList.push(x);
                    // 依據登入交易所回傳的 DISFORM 欄位 來判斷是否要同時顯示在畫面上
                    if (_self.getValue("DISFORM$") == "M") {
                        optionList.push(x);
                    }
                } else { // 是否要移除 else
                    optionList.push(x);
                }
            });
            if (mandatoryList) {
                this.printDocs(null, mandatoryList, bDup); // 印到印表機
            } else { // 沒有任何要印的就來個這個吧
                _self.postTran();
            }
            if (optionList) {
                if (_self.fkey == 8) // 潘 20180129
                    this.displayToScreen(p, optionList, true, trandocScan);
                else this.displayToScreen(p, optionList, bDup, trandocScan);
            }
        } else { // 沒印表機
            this.displayToScreen(p, outputList, bDup, trandocScan);
        }
    };
    Ifx.fn.displayToScreen = function (p, docList, bDup, trandocScan) {
        console.log("displayToScreen:" + docList.length);
        if (!docList || docList.length == 0 || trandocScan) // trandocScan
            // 整批重印單據不跑 印畫面!
            return;
        var self = this;
        var namePrefix = "_doc_";
        var btnPrefix = "btnPrint_";
        var t = "";
        var div = "";
        var h;
        var result = [];
        var bb = [],
            b;
        var tempDocList1 = []; // 暫存單據1
        var tempDocList2 = []; // 暫存單據2
        var firstDoc = ["TXRP9", "MT001", "MT002"]; // 要擺最前面的單據
        var lastDoc = ["FM01", "FM001", "FMV01", "FMR01", "FMH01", "FMHR01", "UPD01", "UPDH01", "UPD02", "UPDH02"]; // 要擺最後面的單據
        // merge docList with lastDocList
        $.each(docList, function (i, x) {
            var lastForm = findByForm(x);
            var bCheckbine = _self.getTotaMode(x.form) == 0 ? true : false; // 柯:如果不是0
            // 則分開
            console.log("bCheckbine:" + bCheckbine);
            if (lastForm != null && bCheckbine) {
                console.log("lastForm::" + lastForm);
                lastForm.content += "\n{{formfeed}}\n" + x.content;
            } else {
                if ($.inArray(x.realform, firstDoc) != -1) {
                    tempDocList1.push(x);
                } else if ($.inArray(x.realform, lastDoc) != -1) {
                    tempDocList2.push(x);
                } else {
                    lastDocList.push(x);
                }
            }
        });
        // 水單,電文放最前面
        for (var j = 0; j < tempDocList1.length; j++) {
            lastDocList.unshift(tempDocList1[j]);
        }
        // 登錄單的部分擺到最後面
        for (var j = 0; j < tempDocList2.length; j++) {
            lastDocList.push(tempDocList2[j]);
        }

        function findByForm(doc) {
            for (var i = 0; i < lastDocList.length; i++) {
                if (doc.form == lastDocList[i].form) {
                    if (doc.merge === false) return null;
                    else return lastDocList[i];
                }
            }
            return null;
        }

        // var currDocIndex = $('.myDummyDoc', $(p)).length;
        // rebuild all doc
        var currDocIndex = 0;
        var btnTexttmp = '存摺印表機';
        var btnText = "";
        $.each(lastDocList, function (i, x) {
            // 柯 新增此行bb歸0 因會重複印多次
            bb = []
            btnText = btnTexttmp;
            // if (bDup && x.dups) {
            // //暫時先移除顯示次數
            // btnText = btnTexttmp; //"(已重印" + x.dups + "次)"
            // }else{
            // btnText = btnTexttmp;
            // }
            div = '<div class="myDummyDoc" id="' + (namePrefix + i + currDocIndex) + '">';
            h = '<h3><a href="#">' + (x.titleName ? x.titleName : x.prompt) + (bDup ? "(重印單據)" : "") + '</a></h3>'; // 新增註記
            // 柯 隱藏按鈕行區塊 START
            b = '<div id="' + (namePrefix + btnPrefix + i + currDocIndex) + '">';
            bb.push(b);
            // print to windows printer
            b = "<button id='" + btnPrefix + "-winprn-" + i + currDocIndex + "'>" + (x.prompt.trim() == "PDF" || x.prompt.trim() == "FILE" ? "下載" : "列印") + "</button>";
            bb.push(b);
            // save to disk
            b = "<button id='" + btnPrefix + "-disk-" + i + currDocIndex + "'>" + "存檔" + "</button>";
            // bb.push(b);
            // save to pdf
            b = "<button id='" + btnPrefix + "-pdf-" + i + currDocIndex + "'>" + "PDF" + "</button>";
            // bb.push(b);
            // print to etabs 存摺印表機
            b = "<button id='" + btnPrefix + i + currDocIndex + "'>" + btnText + "</button>";
            // bb.push(b);
            // 柯 隱藏按鈕行區塊 END
            b = "</div>";
            bb.push(b);
            t = x.content;
            t = t.replace(/[ ]/g, "&nbsp;");
            // t = t.replace(/0/g,'Ø'); //數字零取代..醜!Ơ
            t = t.replace(/{{formfeed}}/g, "---換頁---"); // 只有screen和txt有置換
            var ss = t.split("\n");
            var edittexttemp = "";
            t = "";
            $.each(ss, function (i, l) {
                if (!l.replace(/&nbsp;/g, "").trim().length == 0) {
                    t += "<tr><td>&nbsp;&nbsp;</td><td nowrap>";
                    // 柯:增加編輯FROM時檢查該行0長度則可編輯
                    // 重印單據也可以改??
                    if (x.editAll) {
                        if (x.edit) {
                            t += "<div contenteditable class='outputeditable'>" + l + "</div>";
                        } else {
                            t += "<div>" + l + "</div>";
                        }
                        t += "</td><td>&nbsp;&nbsp;	</td></tr>";
                    } else {
                        if (x.edit && l.replace(/&nbsp;/g, "").trim().length == 0) {
                            t += "<div contenteditable class='outputeditable'>";
                        } else {
                            t += "<div>" + l;
                        }
                        t += "</div></td><td>&nbsp;&nbsp;	</td></tr>";
                    }
                }
            });
            if (x.edit) {
                for (var add = 0; add < 8; add++) { // 注意需再修改輸出之依據前面多加入的空行 來移除.
                    t += "<tr><td>&nbsp;&nbsp;</td><td nowrap><div contenteditable class='outputeditable'></div></td><td>&nbsp;&nbsp;	</td></tr>";
                }
            }
            t = "<table id='table-display-" + i + "' class='doctable'>" + t + "</table>";
            div = div + h + '<div>' + bb.join("&nbsp;") + t + '</div></div>';
            result.push(div);
        });
        // if(bAppend===true){
        // rebuild all doc
        $(p).html("&nbsp;&nbsp;列印資料<br/><br/>" + result.join(''));
        // if ($(p).html()) {
        // $(p).append("<br/>" + result.join(''));
        // } else {
        // $(p).html("&nbsp;&nbsp;列印資料<br/><br/>" + result.join(''));
        // }
        $(p + " table tr:even").addClass("evencolor");
        $(p + " table tr:odd").addClass("oddcolor");
        setEditRule(); // 不能貼上和空行
        function setEditRule() {
            $(".outputeditable").on("keydown", function (e) {
                var temp = e.char;
                if (temp == "\n") {
                    e.preventDefault(); // 停止這次
                    return;
                }
            }).on("paste", function (e) {
                e.preventDefault();
                return;
            });
            // if( window.clipboardData ){ //ie11 ie10??
            // content = window.clipboardData.getData('Text');
            // content = content.replace(/\n|\r\n/g, "");
            // if (window.getSelection)
            // window.getSelection().getRangeAt(0).insertNode(
            // document.createTextNode(content) );
            // }
        }

        function selecttoFolder() {
            try {
                var Message = "請選擇存檔路徑"; // 選擇提示訊息
                var Shell = new ActiveXObject("Shell.Application");
                var Folder = Shell.BrowseForFolder(0, Message, 0x0040, 17); // 起始目錄：,
                // 17我的電腦
                // var Folder = Shell.BrowseForFolder(0,Message,0); //起始目錄：桌面
                if (Folder != null) {
                    // Folder = Folder.Items().Item().Path;
                    // Folder = Folder.items(); // 返回 FolderItems 對象
                    // Folder = Folder.item(); // 返回 Folderitem 對象
                    Folder = Folder.Self.Path; // 返迴路徑
                    // if(Folder.charAt(Folder.length-1) != "\\"){
                    // Folder = Folder + "\\";
                    // }
                }
            } catch (e) {
                alert(e.message);
            }
            return Folder;
        }

        $.each(lastDocList, function (i, x) {
            // 依照單據是否可編輯,進行取代的動作
            function checkEditval(noreplace) {
                if (!x.edit) {
                    x.content = x.content.replace(/\n+$/, "");
                    return;
                }
                var inputValues = [];
                // TODO 使用此方式會可以用F12強制修改內容,是否有需要更改?(用合併的方式)
                $("#table-display-" + i + ' div').each(function () {
                    inputValues.push($(this).text().replace(/\xa0/g, '\x20')); // 只有字串
                    /*
					 * 潘改 text()所帶出&nbsp轉成\x3F,故replace to space Try checking
					 * for '\xa0' (which is the character created by &nbsp;):
					 * (item == '\xa0');true,why \xa0???
					 */
                });
                x.content = inputValues.join("\n").replace(/\n+$/, ""); // 依據前面多加入的空行
                // 來移除.
                // 取代回來給列印使用,txt存檔不用
                if (!noreplace) {
                    x.content = x.content.replace(/---換頁---/g, "{{formfeed}}");
                }
            }

            /*-------存摺印表機列印按鈕-------*/
            /*--因呼叫後沒印表機會當機，故如果是XS,XL,XF則可以按，其餘再次確認--全部confirm*/
            // TODO: refactor to array for buttons
            $('#' + btnPrefix + i + currDocIndex).button({
                icons: {
                    primary: "ui-icon-print"
                }
            }).on('click', function () {
                // var txcdCheck = _self.txcd.slice(1,2); // 20180115
                // if("SLF".indexOf(txcdCheck) == -1){ // 20180115
                if (!window.confirm('確認列印至"存摺印表機"?')) {
                    return;
                }
                // } // 20180115
                checkEditval();
                // _self.printDocs($(this), [ x ], bDup);
                _self.printSingleDoc($(this), x, bDup);
                if (bDup && x.docId) {
                    hiddendoc_btn(namePrefix, btnPrefix, i, currDocIndex);
                }
            });
            /*-------印表機列印按鈕-------*/
            $('#' + btnPrefix + "-winprn-" + i + currDocIndex).button({
                icons: {
                    primary: "ui-icon-print"
                }
            }).on('click', function () {
                if (x.realform.trim() == "PDF" || x.realform.trim() == "FILE") {
                    var t = x.printNo.split(";");
                    if (x.execProg.trim().length > 1) {
                        var val = _self.getValue("#" + x.execProg.trim()).trim();
                        if (val.length > 1) {
                            val = val.split('+');
                            _self.escSync = true;
                            var fn = getTabFn('setIfxSelf');
                            fn(_self);
                            _self.rtn.S(val);
                        }
                    }
                    if (x.realform.trim() == "FILE")
                        _self.viewPdf(_self.getValue("#" + t[0]), "7", x.titleName);
                    else
                        _self.viewPdf(_self.getValue("#" + t[0]), t.length > 1 ? t[1] : "1", x.titleName);
                    // self.viewPdf(_self.getValue("#" + x.printNo), "1");
                } else if (x.realform.trim() == "REPORT") {
                    var val = _self.getValue("#" + x.printNo);
                    _self.printReport(val);
                    //$.dialogPrinter(val);
                } else {
                    if (x.printNo && x.printNo.trim().length > 1) {
                        var val = _self.getValue("#" + x.printNo.trim()).trim();
                        if (val != "") {
                            val = val.split('+');
                            _self.escSync = true;
                            var fn = getTabFn('setIfxSelf');
                            fn(_self);
                            _self.rtn.S(val);
                        }
                    }
                    var value = document.getElementById(this.parentNode.parentNode.id).innerHTML;
                    var mywindow = window.open('', 'Ticket info', 'height=1000,width=800');
                    mywindow.document.write('<html><head>');
                    mywindow.document.write('<meta charset="UTF-8">');
                    mywindow.document.write('<meta http-equiv="X-UA-Compatible" content="ie=edge">');
                    mywindow.document.write('<title>' + x.form + ' ' + x.prompt + '</title>');
                    mywindow.document.write(
                        '<style type="text/css" media="screen">\r\n		body {\r\n			font-family: MingLiU; \r\n background-color: #eee;\r\n		}\r\n\r\n		#two {\r\n	font-family: MingLiU;\r\n		margin: 0 auto;\r\n			width: 640px;\r\n			padding: 10px 15px;\r\n			background-color: #fff;\r\n			margin-top: 10px;\r\n			position: relative;\r\n			z-index: 1;\r\n		}\r\n	</style>\r\n	<style media="print">\r\n		input {\r\n		font-family: MingLiU;\r\n	display: none;\r\n		}\r\n		#two {\r\n		font-family: MingLiU;\r\n	padding: 10px 15px;\r\n			background-color: #fff;\r\n			margin-top: 10px;\r\n			position: relative;\r\n		}\r\n	</style>'
                    );
                    mywindow.document.write('</head>');
                    mywindow.document.write('<body>');
                    mywindow.document.write('<div id="two">');
                    mywindow.document.write(value);
                    mywindow.document.write('</div>');
                    mywindow.document.write('<script src="script/jquery.js"></script>');
                    mywindow.document.write('<script type="text/javascript">');
                    var a = '#' + btnPrefix + "-winprn-" + i + currDocIndex;
                    mywindow.document.write('function testprint(){watermark({watermark_txt:"' + IfxUtl.dateFormatter(_self.getValue("CDATE$").substring(1)) + " " + new Date().getHours() + ":" + new Date().getMinutes() + ":" + new Date().getSeconds() +
                        " " + _self.getValue("#TLRNO") + '"}), $("' + a +
                        '").hide()}function watermark(a){var r={watermark_txt:"text",watermark_x:50,watermark_y:20,watermark_rows:0,watermark_cols:0,watermark_x_space:20,watermark_y_space:50,watermark_color:"3.1h",watermark_alpha:.3,watermark_fontsize:"16px",watermark_font:"Consolas",watermark_width:270,watermark_height:80,watermark_angle:15};if(1===arguments.length&&"object"==typeof arguments[0]){var e=arguments[0]||{};for(key in e)e[key]&&r[key]&&e[key]===r[key]||e[key]&&(r[key]=e[key])}var t,m,w=document.createDocumentFragment(),k=Math.max(document.body.scrollWidth,document.body.clientWidth),_=Math.max(document.body.scrollHeight,document.body.clientHeight);(0==r.watermark_cols||parseInt(r.watermark_x+r.watermark_width*r.watermark_cols+r.watermark_x_space*(r.watermark_cols-1))>k)&&(r.watermark_cols=parseInt((k-r.watermark_x+r.watermark_x_space)/(r.watermark_width+r.watermark_x_space)),r.watermark_x_space=parseInt((k-r.watermark_x-r.watermark_width*r.watermark_cols)/(r.watermark_cols-1))),(0==r.watermark_rows||parseInt(r.watermark_y+r.watermark_height*r.watermark_rows+r.watermark_y_space*(r.watermark_rows-1))>_)&&(r.watermark_rows=parseInt((r.watermark_y_space+_-r.watermark_y)/(r.watermark_height+r.watermark_y_space)),r.watermark_y_space=parseInt((_-r.watermark_y-r.watermark_height*r.watermark_rows)/(r.watermark_rows-1)));for(var o=0;o<r.watermark_rows;o++){m=r.watermark_y+(r.watermark_y_space+r.watermark_height)*o;for(var s=0;s<r.watermark_cols;s++){t=r.watermark_x+(r.watermark_width+r.watermark_x_space)*s;var l=document.createElement("div");l.id="mask_div"+o+s,l.appendChild(document.createTextNode(r.watermark_txt)),l.style.webkitTransform="rotate(-"+r.watermark_angle+"deg)",l.style.MozTransform="rotate(-"+r.watermark_angle+"deg)",l.style.msTransform="rotate(-"+r.watermark_angle+"deg)",l.style.OTransform="rotate(-"+r.watermark_angle+"deg)",l.style.transform="rotate(-"+r.watermark_angle+"deg)",l.style.visibility="",l.style.position="absolute",l.style.left=t+"px",l.style.top=m+"px",l.style.overflow="hidden",l.style.zIndex="9999",l.style.opacity=r.watermark_alpha,l.style.fontSize=r.watermark_fontsize,l.style.fontFamily=r.watermark_font,l.style.color=r.watermark_color,l.style.textAlign="center",l.style.width=r.watermark_width+"px",l.style.height=r.watermark_height+"px",l.style.display="block",w.appendChild(l)}}document.body.appendChild(w)}window.onafterprint=function(){location.reload()};'
                    );
                    mywindow.document.write('</script>');
                    // mywindow.document.write('<style type="text/css"> *{margin: 0; padding: 0;}
                    // body{font:14px MingLiU;}</style>');
                    mywindow.document.write('</body>');
                    mywindow.document.write('</html>');
                    setTimeout(function () {
                        mywindow.testprint();
                        mywindow.print();
                    }, 1000);
                    // mywindow.print();
                    // mywindow.close();
                }
            });
            /*-------存檔TXT按鈕-------*/
            $('#' + btnPrefix + "-disk-" + i + currDocIndex).button({
                icons: {
                    primary: "ui-icon-disk"
                }
            }).on('click', function () {
                checkEditval(true);
                var folderUtil = deviceX.folder;
                var fileUtil = deviceX.file;
                // 柯 新增存檔路徑
                var selectFolder = selecttoFolder();
                if (!selectFolder) return;
                var p = folderUtil.combine(selectFolder, "docs");
                folderUtil.create(p);
                var exportname = "#exportname";
                var fld = _self.getField(exportname, true);
                var name = "";
                if (fld) {
                    name = _self.getValue(exportname).trim() + "";
                } else {
                    name = _self.txcd + "_" + x.form + "_" + IfxUtl.getToday() + "_" + IfxUtl.getTimeString() + ".txt";
                }
                p = folderUtil.combine(p, name);
                // TODO: 檢查是否正確..
                var ss = replaceOutputdata(x.content);
                ss = ss.replace(/{{formfeed}}/g, "---換頁---");
                fileUtil.write(p, ss);
                console.log('saved to ' + p);
                alert('檔案已儲存在路徑: ' + p);
                // 是否自動開啟?
                // setTimeout(function(){
                // var Shell = new ActiveXObject("WScript.Shell");
                // Shell.Run("notepad " + p, 1); //自動開啟
                // Shell=null;
                // },2000);
            });
            /*-------存檔PDF按鈕-------*/
            $('#' + btnPrefix + "-pdf-" + i + currDocIndex).button({
                icons: {
                    primary: "ui-icon-document"
                }
            }).on('click', function () {
                checkEditval();
                deviceX.pdf.init(); // 類似 印表機的 begin初始化
                var name = _self.txcd + "_" + x.form + +"_" + IfxUtl.getToday() + "_" + IfxUtl.getTimeString() + ".pdf";
                var url = _contextPath + "/mvc/hnd/web/form/pdf/" + x.pdfform;
                var papersize = [0, 0];
                var papermargin = [0, 0, 0, 0];
                // 也有要取代換行??
                var ss = replaceOutputdata(x.content);
                if (x.printlpi) {
                    console.log("pdf printlpi:" + x.printlpi);
                    deviceX.pdf.printlpi(x.printlpi);
                }
                if (x.printcpi) {
                    console.log("pdf printcpi:" + x.printcpi);
                    deviceX.pdf.printsize(x.printcpi);
                }
                // 參數值：以 1/100 英吋為單位 寬高
                // 要設定就要全設
                if (x.papersize && x.papersize != "") {
                    papersize = x.papersize.split(":");
                    console.log("pdf套表紙張調整:" + papersize[0] + "," + papersize[1]);
                    // pdf會自動橫印,只要給原本顛倒的size即可
                    if (x.landscape) {
                        deviceX.pdf.setPaperSize(parseInt(papersize[1], 10), parseInt(papersize[0], 10));
                    } else {
                        deviceX.pdf.setPaperSize(parseInt(papersize[0], 10), parseInt(papersize[1], 10));
                    }
                }
                // 參數值：以 1/100 英吋為單位 左 上 右 下 預設15.25.15.25
                // 要設定就要全設
                if (x.papermargin && x.papermargin != "") {
                    papermargin = x.papermargin.split(":");
                    console.log("列印pdf套表紙張的頁邊空白寬度:" + papermargin[0] + "," + papermargin[1] + "," + papermargin[2] + "," + papermargin[3]);
                    deviceX.pdf.setPaperMargins(parseInt(papermargin[0], 10), parseInt(papermargin[1], 10), parseInt(papermargin[2], 10), parseInt(papermargin[3], 10));
                } else {
                    deviceX.pdf.setPaperMargins(25, 25, 25, 25); // 經測試後好像是這樣比較準
                }
                if (x.pdfform) {
                    var getpdfform = $.post(url);
                    getpdfform.done(function (data) {
                        if (data.success) {
                            console.log("taoform:" + x.pdfform);
                            console.log("formid:" + data.formid);
                            // console.log(data.formbyte2);
                            // var bufView = new Uint8Array(data.formbyte);
                            // //有參考價值
                            // deviceX.pdf.write(p, x.form, x.content ,
                            // btoa(String.fromCharCode.apply(null, bufView)));
                            // //傳入form 有參考價值
                            // var ss = x.content.split('\n');
                            // x.content = ss.slice(1).join('\n');
                            deviceX.pdf.write(name, x.form, ss, data.formbyte); // 傳入經java轉檔的string
                            // alert('saved to User tmp path' + name);
                        } else {
                            alert("讀取pdf form失敗");
                        }
                    });
                    getpdfform.fail(function (data) {
                        alert('傳送失敗:' + data + '\n請稍後再試!');
                    });
                } else {
                    deviceX.pdf.write(name, x.form, ss, null);
                    // alert('saved to User tmp path' + name);
                }
            });
        });
        $(p).fadeIn('fast', function () {
            // 0->false:hide
            $.each(lastDocList, function (i, x) {
                var jq = '#' + (namePrefix + i + currDocIndex);
                $(jq).accordion({
                    collapsible: true,
                    active: (i == 0) ? 0 : 0, // second
                    // 0:show,
                    // second
                    // 0->false:hide
                    heightStyle: "fill",
                    animate: 50
                }).click(function () {
                    queueTask(null, resizeSelf, 300);
                });
            });
            // 重算大小
            queueTask(null, resizeSelf);
            // 柯 如果有值 不要隱藏
            var seeallform = _self.getValue('SEEALLFORM$') ? _self.getValue('SEEALLFORM$') : "";
            if (seeallform == "") {
                queueTask(null, showOne); // 關閉隱藏
            }
        });

        function showOne() {
            $.each(lastDocList, function (i, x) {
                var jq = '#' + (namePrefix + i + currDocIndex);
                if (_self.isUpdatedTran()) {
                    // 更新全部隱藏
                    $(jq).accordion({
                        active: false,
                    });
                } else {
                    // 查詢再隱藏第一個之外的
                    $(jq).accordion({
                        active: (i == 0) ? 0 : false,
                    });
                }
            });
        }

        // $(p).draggable();
    };
    Ifx.fn.displayOutputPlain = function (p, outputList) {
        var self = this;
        console.log(outputList.length);
        var t = "";
        $.each(outputList, function (i, x) {
            console.dir(x);
            console.log(x.content);
            t += x.prompt + "\n\n";
            t += x.content;
            t += "\n\n\n";
        });
        // t = "<pre>" + t + "</pre>";
        t = t.replace(/[ ]/g, "&nbsp;");
        // t = t.replace(/\n/g,"<br/>");
        var ss = t.split("\n");
        console.log(ss.length);
        t = "";
        $.each(ss, function (i, x) {
            t += "<tr><td>&nbsp;&nbsp;</td><td>" + x + "</td><td>&nbsp;&nbsp;	</td></tr>";
        });
        t = "<table class='doctable'>" + t + "</table>";
        console.log(t);
        console.log(p);
        $(p).html(t);
        $(p + " table tr:even").addClass("evencolor");
        $(p + " table tr:odd").addClass("oddcolor");
        $(p).fadeIn();
    };
    // 列印一張傳票
    Ifx.fn.printSingleDoc = function ($bntPrint, doc, bDup) {
        // 重印單據
        if (bDup && doc.docId) {
            console.log("printSingleDoc:" + doc.docId);
            incrementPrintTimes(doc);
        }
        var waittime;
        var cpi = doc.printcpi ? doc.printcpi : _self.getValue("DEFULTCPI$"); // 10;12;13.3;15
        var lpi = doc.printlpi ? doc.printlpi : _self.getValue("DEFULTLPI$"); // 3;4;5;6;8
        console.dir(doc);
        if (!deviceX.init()) {
            waittime = 7;
        } else {
            waittime = 2;
        }
        deviceX.obtainSession();
        showPrompt(doc.prompt, waittime); // 好像只有本機存摺印表機可以用?
        // deviceX.setPromptMessage(doc.prompt); //要改用這個?
        setTimeout(function () {
            if (cpi != "") { // printcpi
                deviceX.setCpi(parseFloat(cpi)); // 小數13.3
            }
            if (lpi != "") { // printlpi
                deviceX.setLpi(parseInt(lpi, 10));
            }
            // deviceX.setDoubleWidth();
            var ss = doc.content.split('\n');
            if (ss[ss.length - 1] == "") {
                ss.pop(); // 刪除最後一個數組 (因小張單子長度不夠會錯誤)
            }
            deviceX.printDoc(ss, doc.prompt); // 待更改，應該都不用要eject
            console.log("printSingleDoc done");
            // deviceX.eject();
            deviceX.returnSession();
            _self.unblock();
            if (bDup && doc.docId) {
                dupdoc_addOnelog(doc);
            }
        }, 300); // TODO: 故意慢一點, 真的使用存摺印表機時 應該設短一些
    };

    // "XS"交易與"XF"交易 需主管授權
    function ifSupperNeed() {
        if (_self.txcd.slice(0, 2) == "XS" || _self.txcd.slice(0, 2) == "XF") {
            return true;
        }
        return false;
    };

    // 隱藏該doc的按鈕欄位
    function hiddendoc_btn(namePrefix, btnPrefix, doci, currDocIndex) {
        // 只有存款需要隱藏
        if (ifSupperNeed()) {
            console.log("存款交易,限列印1次..");
            var jq = '#' + (namePrefix + btnPrefix + doci + currDocIndex);
            $(jq).hide().html('');
        }
    }

    // 更新重印單據的次數
    function dupdoc_addOnelog(doc) {
        var bean = 'url:/mvc/hnd/doc/addlog/' + doc.docId;
        _self.sendBean(bean, {}, null, function (data) {
            console.log(data);
            doc.dups = doc.dups + 1; // 直接先加一次
        }, function (errData) {
            console.log(errData.errmsg);
        });
    }

    function incrementPrintTimes(doc) {
        var ss = doc.content.split("\n"),
            doclogcount = parseInt(doc.dups, 10) + 1,
            dupLabel = '［補印］'; // '[補印' + doclogcount +']'
        if (doclogcount <= 1) {
            return;
        }
        var len = dupLabel.length * 2;
        dupLabel = "{{D2-S}}" + dupLabel + "{{D2-E}}";
        if (doc.line0 == undefined) doc.line0 = ss[0]; // save original line[0]
        ss[0] = dupLabel + doc.line0.slice(len); // 先不管超長或位置改變
        doc.content = ss.join('\n');
    }

    function showPrompt(prompt, nSeconds, delay) {
        var css = {
            top: '250px',
            left: '250px',
            right: '',
            width: '600px',
            'font-size': '24px'
        };
        var tm = nSeconds * 1000;
        if (!delay) {
            delay = 0;
        }
        _self.fyi('請插入' + prompt, tm, delay, css);
    }

    // 列印所有傳票
    Ifx.fn.printDocs = function ($bntPrint, docs, bDup) {
        // alert('print ' + prompt);
        function fnPrint(docs) {
            var doc = docs.shift();
            if (doc != null) {
                var needtalksta = true;
                if (_server_os == "1") {
                    needtalksta = false;
                    if (_self.getValue("DISFORM$") == "M") {
                        needtalksta = true;
                    }
                }
                if (doc.prtwhat == "A") {
                    // 存摺印表機 OR win印表機
                    var whatprint = // 小柯 增 測試 視窗改
                        $.dialog2("選擇列印", '請插入 :' + doc.prompt + " 到", // 柯
                            // \n改<br/>
                            "印表機", "存摺印表機");
                    whatprint.done(function () { // 按下Yes時
                        prninterwin(doc, false, null);
                    }).fail(function () { // 按下No時
                        prninterp(doc, false);
                    });
                    // 不要自動列印
                } else if (doc.prtwhat == "E") { // 存摺印表機
                    prninterp(doc, needtalksta); // false 直接印
                } else if (doc.prtwhat == "Z") { // 印表機
                    prninterwin(doc, needtalksta, null); // false 直接印
                } else if (doc.prtwhat == "B") { // 空白 = TAP系列前插式印表機或媒體
                    var setprname = "Microsoft XPS Document Writer"; // TODO:
                    // 待確定印表機名稱
                    // TAP
                    prninterwin(doc, needtalksta, setprname);
                }
                // }, 8000); // TODO: 故意慢一點, 真的使用存摺印表機時 應該設短一些
                // var promise = _self.prt.printDoc(ss);
                // promise.done(fnPrint);
                // promise.fail(function (x) {
                // alert('print error:' + x);
                // });
            } else {
                // 只有印表機
                _self.diyunblock();
                _self.postTran(); // 改成這邊才做事情
            }
            return; // 柯 觀察程式碼應該要加入此段
        }

        function prninterwin(doc, needtalk, setprname) {
            // TODO: 檢查是否正確..
            var ss = replaceOutputdata(doc.content);
            // 柯:印表機只看FORMFFED故需要先加工
            ss = ss.replace(/{{formfeed}}\r\n/g, "{{formfeed}}");
            if (!deviceX) {
                console.log("win周邊設備驅動程式有誤 無法繼續運作。");
                return;
            }
            deviceX.init();
            var tempshowtext = "印表機";
            if (setprname) {
                tempshowtext = setprname;
            }
            if (!needtalk) {
                _self.diyblock(doc.prompt, tempshowtext);
            }
            if (!needtalk || window.confirm("請插入" + doc.prompt + " 到" + tempshowtext)) {
                // 柯 增加 字體大小
                deviceX.prn.beginDocument("IFX 文件");
                // deviceX.setPromptMessage(doc.prompt);
                var wprintsize = _self.getValue('WINPRNTSIZE$').split(":");
                // //deviceX.prn.setPrinterName("Microsoft XPS Document
                // Writer");
                if (wprintsize && wprintsize.length == 2) {
                    deviceX.prn.printsize(wprintsize[0], wprintsize[1]);
                } else {
                    if (doc.printcpi) {
                        deviceX.prn.setCpi(doc.printcpi);
                    }
                    if (doc.printlpi) {
                        deviceX.prn.setLpi(doc.printlpi);
                    }
                }
                // 橫印直印
                if (doc.landscape) {
                    deviceX.prn.setPaperOrientation(true);
                } else {
                    deviceX.prn.setPaperOrientation(false);
                }
                // 參數值：paperWidth - 列印紙張的寬度整數值，以 1/100 英吋為單位。
                // paperHeight - 列印紙張的高 (長) 度整數值，以 1/100 英吋為單位。
                if (doc.papersize && doc.papersize != "") {
                    var papersize = doc.papersize.split(":");
                    deviceX.prn.setPaperSize(parseInt(papersize[0], 10), parseInt(papersize[1], 10));
                    console.log("列印紙張調整:" + papersize[0] + "," + papersize[1]);
                }
                // 參數值：以 1/100 英吋為單位 左 上 右 下 預設15.25.15.25
                // 要設定就要全設
                if (doc.papermargin && doc.papermargin != "") {
                    console.log("x.papermargin:" + doc.papermargin);
                    var papermargin = doc.papermargin.split(":");
                    console.log("列印紙張的頁邊空白寬度:" + papermargin[0] + "," + papermargin[1] + "," + papermargin[2] + "," + papermargin[3]);
                    deviceX.prn.setPaperMargins(parseInt(papermargin[0], 10), parseInt(papermargin[1], 10), parseInt(papermargin[2], 10), parseInt(papermargin[3], 10));
                } else {
                    deviceX.prn.setPaperMargins(25, 25, 25, 25);
                }
                // 初步哪一台 (應該就沒用了...)
                if (setprname) { // TAP系列前插式印表機或媒體
                    deviceX.prn.setPrinterName(setprname);
                }
                // form對應哪一台
                // 使用 realform來對照
                var formprinter = null;
                // 經測試好像只有doc.form???
                var setform = (doc.sformname ? doc.sformname : (doc.realform ? doc.realform : doc.form));
                if (setform) { // TAP系列前插式印表機或媒體
                    formprinter = ifxFile.getformP(deviceX, setform, _self.txcd);
                    if (formprinter) {
                        console.log("成功指定Form to Printer:" + setform + ":" + formprinter);
                        deviceX.prn.setPrinterName(formprinter);
                    }
                }
                deviceX.prn.printText(ss);
                // 是否要顯示?
                var checkend;
                if (formprinter) { // TAP系列前插式印表機或媒體 setprname
                    checkend = deviceX.prn.endDocument(false, false); // Show
                    // PrintPreviewDialog
                    // ,Show
                    // PrintDialog
                } else {
                    checkend = deviceX.prn.endDocument(true, true); // Show
                    // PrintPreviewDialog
                    // ,Show
                    // PrintDialog
                }
                if (checkend) {
                    fnPrint(docs);
                }
            } else {
                fnPrint(docs);
            }
        }

        function prninterp(doc, needtalk) {
            var cpi = doc.printcpi ? doc.printcpi : _self.getValue("DEFULTCPI$"); // 10;12;13.3;15
            var lpi = doc.printlpi ? doc.printlpi : _self.getValue("DEFULTLPI$"); // 3;4;5;6;8
            var waittime;
            if (!deviceX) {
                console.log("p周邊設備驅動程式有誤 無法繼續運作。");
                return;
            }
            if (!deviceX.init()) {
                waittime = 10;
            } else {
                waittime = 5;
            }
            // _self.unblock();
            // _self.block("<h2>output processing ....</h2>");
            // showPrompt(doc.prompt,waittime); //,2000
            var ss = doc.content.split('\n');
            if (ss[ss.length - 1] == "") {
                ss.pop(); // 刪除最後一個數組 (因小張單子長度不夠會錯誤)
            }
            // _self.fyi('交易1123...', 5000);
            // _self.diyblock(doc.prompt,"存摺印表機");
            // }
            // _self.fyi('交易1123...', 5000);
            setTimeout(function () {
                // alert("請插入"+doc.prompt+ " 到存摺印表機");
                if (!needtalk || window.confirm("請插入" + doc.prompt + " 到存摺印表機")) {
                    // setTimeout(function() {
                    deviceX.obtainSession();
                    if (cpi != "") { // printcpi
                        deviceX.setCpi(parseInt(cpi, 10));
                    }
                    if (lpi != "") { // printlpi
                        deviceX.setLpi(parseInt(lpi, 10));
                    }
                    // 寫入磁條WRTMSR$,磁條內容MSRBUF$
                    var wrtmsr = _self.getValue("WRTMSR$");
                    var msrbuf = _self.getValue("MSRBUF$");
                    console.log("doc.kind:" + doc.kind + ",WRTMSR$:" + wrtmsr + "。");
                    if (doc.kind == "M" && wrtmsr == "Y") {
                        deviceX.setPromptMessage(doc.prompt);
                        deviceX.printLines(ss); // noeject 所以這個
                        deviceX.flushPrintData();
                        console.log("磁條內容 MSRBUF$:" + msrbuf + "。");
                        deviceX.writeMSDataprint(msrbuf, true, ss, cpi, lpi); // 須符合磁條規定
                    } else {
                        deviceX.printDoc(ss, doc.prompt); // printDoc 內都已經有
                        // EJECT
                    }
                    console.log("printDocs one done");
                    // deviceX.eject(); //可能可以不用 潘銷
                    var checkend = deviceX.returnSession();
                    if (checkend) {
                        fnPrint(docs);
                    }
                } else {
                    fnPrint(docs);
                }
            }, 300);
            // fnPrint(docs);
        }

        if (!bDup) {
            // 潘 周邊
            // if (!deviceX) {
            // alert("周邊設備驅動程式有誤。");
            // console.log("周邊設備驅動程式有誤 無法繼續運作。");
            // _self.postTran(); // TODO 20151020 ADD
            // return;
            // }
            fnPrint(docs);
            // return;
        } else { // 柯 重印單據
            fnPrint(docs);
            // return;
        }
        // 柯 移除此大段
        // var oDoc = docs[0];
        // var bean = 'url:/mvc/hnd/doc/get/' + oDoc.docId + "?_addLog=1";
        // _self.sendBean(bean, {}, null, function(data) {
        // console.log(data);
        // //if(data.doc.parameter){
        // // //測試一下
        // // console.log(data.doc.parameter);
        // // }
        // var content = data.doc.content, ss = content.split('\n'), p = "[重印"
        // + data.doc.dups + ']', pLen = IfxUtl.strlen(p);
        // ss[0] = p + ss[0].slice(pLen);
        // data.doc.content = ss.join("\n");
        // fnPrint([ data.doc ]);
        // // $bntPrint.text("列印(已重印" + data.doc.dups + "次)").button("refresh");
        // // "次)").button("refresh");
        // //var btnLabel = "列印(已重印" + data.doc.dups + "次)";
        // //$bntPrint.button('option', 'label', btnLabel);
        // }, function(errData) {
        // alert("重印錯誤:" + errData.errmsg);
        // });
    };
    // end host comm
    // ---------------------------------------
    // ------------------------------------
    // process output
    Ifx.fn.resetDupDoc = function () {
        this.dup = {};
        this.dup.docs = {};
        this.dup.count = 0;
    };
    Ifx.fn.isUpdatedTran = function (txcdtmp) {
        var chktxcd = this.txcd;
        if (txcdtmp) chktxcd = txcdtmp;

        if (chktxcd.trim() == "L8501")
            return true;

        if ((chktxcd[2] != "9" && chktxcd[2] != "R" && chktxcd[2] != "0")) {
            if (_self.getValue("#FUNCIND") != "5") return true;
            else return false;
        } else return false;
    };
    Ifx.fn.isNotxxTran = function () {
        if (this.txcd.slice(0, 2) == "XX") {
            return false;
        } else {
            return true;
        }
    };

    function saveDoc(list) {
        // console.log("no save dup doc");
        // return;
        console.log("saveDoc list");
        var dupList = $.map(list, function (x) {
            // 整批在列印時 不知為何有多一個undefined
            if (x) {
                console.log("x.dupable:" + x.dupable + "," + "x.saved:" + x.saved);
            }
            return (x && x.dupable && !x.saved) ? x : null;
        });
        if (dupList.length == 0) return;
        var bean = 'url:/mvc/hnd/doc/save';

        function _save(aDoc, callback) {
            // 柯 略過content
            var docParam = {};
            for (var k in aDoc) {
                if (k != 'content' && k != 'form' && k != 'prompt' && k != 'totamrkey' && k != 'totacifkey') docParam[k] = aDoc[k];
            }
            // var srhRbrno = _self.getValue("#RBRNO");
            // var srhFbrno = _self.getValue("#FBRNO");
            // var srhAcbrno = _self.getValue("#ACBRNO"); //暫時還沒
            var jsondoc = JSON.stringify(docParam);
            console.dir(docParam);
            console.log(jsondoc);
            console.log("aDoc.realform:" + aDoc.realform);
            // 依照記號等於1時,使用XX9TL的部分 不讀取該單據
            // 更正完剛好無法重印(被GROUP蓋掉)
            var stractfg = _self.getValue("#ACTFG").toString();
            var tempdata = (stractfg == '4' || stractfg == '6') ? "1" : "";
            // CURCD有些看似沒有存?不然應該是使用CURCD是最準確的
            var tempcurrency;
            if (_self.getValue("#CURNM").trim().length != 0) {
                tempcurrency = _self.getValue("#CURNM");
            } else if (_self.hasField("#CURSNM") == true && _self.getValue("#CURSNM").trim().length != 0) {
                tempcurrency = _self.getValue("#CURSNM");
            } else {
                tempcurrency = _self.getValue("#CURCD");
            }
            var data = { // TODO 新增儲存MRKEY
                jnlId: _self.jnlId,
                docName: aDoc.sformname ? aDoc.sformname : aDoc.realform, // 柯
                // 看起來應該改成realform
                // 而不是.form
                docPrompt: aDoc.prompt,
                // 柯 補存列印參數等等...供重印單據時的 printDoc 列印
                docParameter: jsondoc,
                content: aDoc.content,
                srhCifkey: aDoc.totacifkey.length > 0 ? aDoc.totacifkey : _self.getValue("#CIFKEY"), // 取得該筆tota
                // CIFKEY
                srhMrkey: aDoc.totamrkey.length > 0 ? aDoc.totamrkey : _self.getValue("#MRKEY"),
                srhTemp: tempdata,
                srhCurrency: tempcurrency,
                srhTxamt: _self.getValue("#TXAMT")
            };
            _self.sendBean(bean, data, "上傳列印資料" + aDoc.form, function (data) {
                // on success
                aDoc.saved = true;
                callback(null, null);
            }, function (data) {
                // on error
                callback(data.errmsg);
            });
        }

        async.mapSeries(dupList, _save, function (err, results) {
            if (err) {
                // retry?
                var bRetry = window.confirm("儲存列印資料失敗:\n" + err + "\n是否重試?") || !window.confirm("確定放棄儲存?");
                if (bRetry) {
                    setTimeout(function () {
                        saveDoc(list);
                    }, 0);
                }
            }
        });
    }

    // passbook var
    var psbk = null;
    var psbkFORM_ID = "FS001";
    var titaHeader = ""; // 在transmit()設定
    Ifx.fn.sendPsbkReturn = function (returnText, fn, prntype) {
        var text = titaHeader + returnText;
        var etc = {
            'psbkReturn': true
        };
        var fnSuccess = function (totaList) {
            if (totaList[0].getTxForm() === psbkFORM_ID) {
                fn(true, convertFromFS001(totaList[0]));
            } else {
                fn(false, "not psbk form id, but:" + totaList[0].getTxForm());
            }
        };
        var fnError = function (oTota) {
            fn(false, {
                errmsg: oTota.getErrmsg()
            });
        }
        var psckrim = (prntype == 1) ? "XS280" : "XS181";
        _self.sendRim(psckrim, text, fnSuccess, fnError, etc); // 柯 補摺:
        // _self.txcd
        // _self.txcd
    };
    Ifx.fn.processOutput = function (totaList, objMoreData) {
        console.log("processOutput");
        _self.processOutput_second(totaList, objMoreData);
    };

    function convertFromFS001(oTota) {
        var settings = null,
            tomDef = _self.def.tom[oTota.getTxForm()],
            tomFields, occurs;
        oTota.toSysVar(); // set sysvar from tota
        _self.processTom(oTota);
        settings = _self.getLoopSettings();
        tomFields = $.map(tomDef, function (x) {
            return _self.getFieldForTom(x);
        });
        var obj = {
            lines: [], // 該行內容
            linedate: [], // 該行日期
            linepbbal: [], // 該行餘額 (黃金)
            hasMore: function () {
                return this.more == 1;
            },
            getReturnText: function () {
                return this.returnText;
            }
        };
        obj.startLine = parseInt(_self.getValue('#FS001_PBCOL'), 10);
        obj.totalLines = parseInt(_self.getValue('#FS001_DATACNT'), 10);
        obj.more = _self.getValue('#FS001_RETURN');
        // 0 一般,1 後面,2 黃金
        obj.prntype = parseInt(_self.getValue('#FS001_TYPE'), 10);
        obj.date = (obj.prntype == 0) ? _self.getValue('#FS001_PTXDAY').trim() : ""; // 上次補摺日
        // obj.date = _self.getValue('#FS001_PTXDAYX');
        obj.acnumber = oTota.getMrkey();
        obj.returnText = _self.getValue('#FS001_RTNTEXT');
        occurs = oTota.parsedOccurs;
        // if(occurs == null)
        // occurs = oTota.getOccurs(settings, tomFields, true); // reset
        // occurs array
        for (var i = 0; i < occurs.length; i++) {
            _self.occursToVar(occurs[i]);
            _self.runAfterYN();
            obj.lines.push(formatPsbkLine());
            obj.linedate.push(formatPsbkLinedata());
            if (obj.prntype == 2) {
                obj.linepbbal.push(formatPsbkLinepbbal());
            }
        }
        return obj;

        function formatPsbkLine() {
            var names;
            switch (obj.prntype) {
                case 0:
                    names = ["   ", "#FS001_DATE", " ", "#FS001_BRNO", " ", "#FS001_DSCPTX_R", " ", "#FS001_AMTX_DB", "#FS001_AMTX_CR", "#FS001_PBBALX", "#FS001_ECURCD"];
                    break;
                case 1:
                    names = ["#FS002_EDAYFG", " ", "#FS002_CTSEQ", " ", "#FS002_SDAY", " ", "#FS002_EDAY", " ", "#FS002_PRDCDR", " ", "#FS002_OPINTRT", " ", "#FS002_ECURCD", "#FS002_ACTNO", " ", "#FS002_PRIBAL",
                        " ", "#FS002_CLSDY", "      ", "#FS002_PAYCD", " ", "#FS002_ACXCD"
                    ];
                    break;
                case 2:
                    names = ["  ", "#FS011_DATE", " ", "#FS011_BRNO", "#FS011_DSCPTX_R", "#FS011_PRICEX", "   ", "#FS011_AMTX_DB", "   ", "#FS011_AMTX_CR", "#FS011_PBBALX", "#FS011_ECURCD"];
                    break;
                default:
                    console.log("obj.prntype error? " + obj.prntype);
                    break;
            }
            return returnNames(names);
        }

        // 存摺該行日期
        function formatPsbkLinedata() {
            var names;
            switch (obj.prntype) {
                case 0:
                    names = ["#FS001_DATE"];
                    break;
                case 2:
                    names = ["#FS011_DATE"];
                    break;
                default:
                    return "";
                    break;
            }
            return returnNames(names);
        }

        // 存摺該行餘額
        function formatPsbkLinepbbal() {
            var names;
            switch (obj.prntype) {
                case 2:
                    names = ["#FS011_PBBALX"];
                    break;
                default:
                    console.log("obj.prntype error?");
                    return "";
                    break;
            }
            return returnNames(names);
        }

        function returnNames(names) {
            var arr = [];
            for (var i = 0; i < names.length; i++) {
                var n = names[i];
                var v = n;
                if (_self.hasField(n)) {
                    v = _self.getField(n).getPrintValue(true);
                }
                arr.push(v);
            }
            return arr.join("");
        }
    }

    // / end of passbook
    // 多筆 ismulti 不要顯示alert 直接出去結果 並save.. 潘
    Ifx.fn.processOutput_second = function (totaList, objMoreData, ismulti) {
        console.log("processOutput_second");
        _self.block("<h2>output processing ....</h2>");
        this.resetDupDoc();

        function processItem(oTota, callback) {
            _self.processOneTota(oTota, objMoreData, callback);
        }

        async.mapSeries(totaList, processItem, function (err, results) {
            if (err && ismulti != true) {
                alert(err);
            } else {
                console.log("results:\n" + JSON.stringify(results, null, 2));
                results = _.flatten(results);
                console.log("flattened results:\n" + JSON.stringify(results, null, 2));
                console.log("results.length:" + results.length);
                //if (results.length > 0) { // 潘 單據全存
                console.log(results);
                // 20180102 modify
                saveDoc(results);
                _self.displayOutput(_self.panel.output, results);
                // setTimeout(function() {
                // saveDoc(results);
                // }, 0);
                //}
            }
            console.log("processOutput done");
            _self.unblock();
            if (objMoreData && !objMoreData.buttonized) {
                objMoreData.dialog(function () {
                    _self.postTran();
                });
                return;
            }
            // 柯:其他更改成displayOutput 來做判斷
            if (results.length <= 0 || err) {
                _self.postTran();
            }
        });
        console.log("async eachSeries done");
    };
    Ifx.fn.processOneTota = function (oTota, fnMoreData, callback) {
        var self = this,
            docForms, result = [],
            tmpR, o;
        oTota.toSysVar(); // set sysvar from tota
        this.processTom(oTota);
        console.log("curr form:" + oTota.getTxForm());
        // if(oTota.getTxForm() == "I4101"){
        // oTota.setTxForm("MT001");
        // console.log("curr form change to:" + oTota.getTxForm());
        // this.processTom(oTota);
        // console.log("now curr form change to:" + oTota.getTxForm());
        // }
        console.log(this.def.rtns);
        if (oTota.getTxForm() === "GM001") {
            var r = this.ifxHost.generateFreeFormat(oTota.text);
            r["form"] = oTota.getTxForm();
            r["mandatory"] = true;
            result.push(r);
            callback(null, result);
            // return result;
        }
        // 柯:特殊不處理MSGID
        var tota_txform = oTota.getTxForm();
        if (tota_txform == "ZZZZZ" || tota_txform == "XX004") {
            callback(null, []);
            return;
        }
        var oFormHandler = this.getFormHandler(tota_txform);
        if (oFormHandler != null) {
            docForms = this.printWhat();
            if (docForms) {
                setTimeout(function () {
                    oFormHandler.fn(oFormHandler.cmd, oTota, docForms[0], callback);
                }, 0);
            } else {
                callback(null, []);
            }
            return;
        }
        // this.runSysRtn();
        // if (this.getValue("TSKID$").charAt(1) != "U") {
        // // if (!this.isUpdatedTran()) {
        console.log("query type, calling printWithTemplate");
        docForms = this.printWhat();
        for (var k = 0; k < docForms.length; k++) {
            if (docForms[k] != null) {
                if (docForms[k].device == 'QUERY.GRID') {
                    console.log("docForms" + docForms);
                    var gridDef = this.getQueryForm(docForms, docForms[k].config.place); // 柯:需傳入grid的位置
                    // 傳printWhat進去
                    if (gridDef != null) {
                        this.displayGrid(gridDef, this.tota2GridData(oTota, gridDef), fnMoreData);
                    }
                } else {
                    if (docForms[k].isPFNX) {
                        tmpR = self.printPFNX(docForms[k], oTota);
                        result = result.concat(tmpR);
                    } else {
                        if (docForms[k].isPart) {
                            tmpR = self.printPartAsDoc(docForms[k]);
                            result = result.concat(tmpR);
                        } else {
                            if (isMultiRecords(docForms[k])) {
                                tmpR = this.printWithTemplate(docForms[k], oTota);
                                if (tmpR) {
                                    result = result.concat(tmpR);
                                }
                            } else {
                                simpleForm(oTota, docForms[k]);
                            }
                        }
                    }
                }
            }
        }
        // // } else {
        // // console.log("calling processTom");
        // // // result = result.concat(this.processTom(oTota)); original
        // // // 2013/04/29
        // // simpleForm(oTota);
        // // }
        // return result;
        callback(null, result);

        function simpleForm(oTota, docFormToPrint) {
            // self.processTom(oTota);
            var docForms;
            if (docFormToPrint != undefined) docForms = [docFormToPrint]; // 傳入列印單劇
            else docForms = self.printWhat(); // 判斷要列印那幾張
            for (var k = 0; k < docForms.length; k++) {
                if (docForms[k].isPFNX) {
                    tmpR = self.printPFNX(docForms[k], oTota);
                } else {
                    tmpR = self.mergeTomWithForm(oTota, docForms[k]);
                }
                if (tmpR) {
                    result = result.concat(tmpR);
                }
            }
        }

        function isMultiRecords(oSelect) {
            if (oSelect.isPFNX) return false;
            var docTemplate = splitTemplate(self.def.docs[oSelect.form].content);
            return docTemplate.detail != null;
        }
    };
    Ifx.fn.tota2GridData = function (oTota, gridDef) {
        var self = this,
            tomDef = this.def.tom[oTota.getTxForm()],
            tomFields, settings = this.getLoopSettings();
        tomFields = $.map(tomDef, function (x) {
            return self.getFieldForTom(x);
        });
        // var occurs = oTota.getOccurs(settings, tomFields);
        // var data = this.prepareGridData(gridDef, oTota.getOccurs(settings,
        // tomFields), 'occursToVar', true);
        var data = this.prepareGridData(gridDef, oTota.parsedOccurs, 'occursToVar', true);
        return data;
    };
    // / PART process
    Ifx.fn.printPart = function (partName) {
        var self = this;
        var part = this.getPartByName(partName.split('.')[0]);
        var prompt = null;
        try {
            prompt = part.display[0].def.prompt;
        } catch (ee) {
            console.error(ee);
        }
        if (!prompt) prompt = 'A4';
        prompt = parseBrace(prompt);
        var result = printScreen(part);
        return {
            'prompt': prompt,
            'result': result
        };
    };
    Ifx.fn.getPartByName = function (partName) {
        var arr = $.grep(this.def.parts, function (x, i) {
            return (x.name == partName);
        });
        arr = $.map(arr, function (x) {
            x.visible = true;
            return x;
        });
        return {
            display: arr
        };
    };
    Ifx.fn.displayGridHeader = function (gridDef, data2) {
        console.log('IN Ifx.fn.displayGridHeader');
        var self = this;
        var s;
        var partName = gridDef.config.header.split('.')[0];
        var part = this.getPartByName(partName);
        var tempfiled = "";
        try {
            var screenContent = ifxUI.renderScreen(part, function (tabIndex, x) {
                try {
                    console.log('Ifx.fn.displayGridHeader');
                    if (data2) {
                        _.each(data2, function (rec) {
                            tempfiled = x.substring(1);
                            if (rec[tempfiled]) self.setValue(x, rec[tempfiled]);
                        });
                    }
                    s = self.getField(x).simpleDisplay();
                    return s;
                } catch (ee) {
                    alert('programmer\'s bug!!\n\nerror raised by renderScreen():' + ee + '\n\ncheck display field:' + x);
                    throw ee;
                }
            });
            // $(this.panel.queryHeader).html(screenContent).show().draggable();
            console.log("screenContent : " + screenContent);
            $(this.panel.queryHeader).html(screenContent);
        } catch (ex) {
            alert("failed to render screen\n" + ex);
        }
    };
    // end of part
    // var hasMoreDataBefore = false;
    Ifx.fn.displayGrid = function (gridDef, data, objMoreData, data2) {
        if (gridDef.config.place == "grid2") {
            $('#queryArea').css({
                width: 'auto',
                float: 'left',
                overflow: 'hidden' // 增加此段for拉霸問題
            });
            $('#queryArea2').css({
                width: '45%',
                float: 'left'
            });
            $('#outputArea').css({
                'clear': 'both'
            });
            this.displayGrid2(gridDef, data, objMoreData, data2);
            $('#xbtn_spesend').show();
            $('#btn_yn').off("click mousedown keypress").hide();
            // 增加 帶回原交易之按鈕
            // 程式運作正常後 再將grid與grid2改成左右排列
        } else {
            this.displayGrid1(gridDef, data, objMoreData, data2);
        }
    }
    var grid = null;
    Ifx.fn.displayGrid1 = function (gridDef, data, objMoreData, data2) {
        console.log('Ifx.fn.displayGrid1');
        $(_self.panel.query).show();
        $(_self.panel.queryHeader).show();
        if (gridDef.config.header != null) {
            console.log('gridDef.config.header != null');
            this.displayGridHeader(gridDef, data2);
        }
        if (gridDef.config.detail === false) {
            console.log('gridDef.config.detail');
            return;
        }
        if (gridDef.config.exportExcel) {
            if (this.getValue('GRIDEXPORT$') == '0') {
                gridDef.config.exportExcel = false;
            } else {
                gridDef.config.exportExcelOption = this.getValue('GRIDEXPORT$');
            }
        }
        var self = this,
            gridFields = {};
        _.each(gridDef.fields, function (x) {
            gridFields[x.name] = self.getField(x.name);
        });
        if (grid != null) {
            var $btn = $('#btnMoreData');
            if (!objMoreData && this.displayGrid1.hasMoreDataBefore) {
                setTimeout(function () {
                    // $btn.val('全部資料已下載');
                    $btn.effect('pulsate', {}, 300, function () {
                        $btn.effect('explode', 400, function () {
                            $btn.hide();
                        });
                    });
                }, 0);
            } else {
                if (objMoreData) {
                    objMoreData.buttonized = true;
                    $btn.effect('pulsate', {}, 1000);
                }
            }
            var goCurrentPage = this.displayGrid1.hasMoreDataBefore;
            grid.refreshGrid(this.panel.query, data, true, true); // do go
            // lastpage!
            return;
        }
        grid = new IfxGrid(gridDef, gridFields, data);
        var handlers = {
            'bind': IfxUtl.bind(this, 'bindHandler'),
            'mapBack': mapBack,
            'batchProcessor': gridBatchProcessor,
            'deviceX': deviceX,
            'getValue': function (x) {
                return self.getValue(x);
            },
            'setValue': function (x, y) {
                return self.setValue(x, y);
            },
            'getField': function (x, y) {
                return self.getField(x, y);
            },
            'rtnCall': this.rtn.CALL
        };
        if (objMoreData && objMoreData.fnMore) {
            objMoreData.buttonized = true;
            this.displayGrid1.hasMoreDataBefore = true;
            handlers['moreData'] = objMoreData.fnMore;
        }
        grid.registerHandler(handlers);
        grid.render(this.panel.query, this.panel.gridControls, resizeSelf);
        // add grid map back NTXBUF5$ = 回傳值
        var nt5 = self.getValue('NTXBUF5$');
        var callerTab = null;
        if (nt5) {
            grid.setMapper(nt5);
        }

        function mapBack(mapper, data) {
            if (_layout != "1") {
                console.log('not support map Back for 2 window');
                return;
            }
            var fnCallerTab = getTabFn('getCallerTab');
            callerTab = fnCallerTab(_tabKey);
            var o = m2o(mapper);
            var fn = getTabFn('redirectCall');
            fn(callerTab, 'xaction', o, data);
            closeMe(_layout, true);
        }

        function m2o(m) {
            var o = {},
                ss;
            _.each(m.split(':'), function (x) {
                ss = x.split('<-');
                if (ss[0] != 'type') o['#' + ss[0]] = '#' + ss[1];
            });
            return o;
        }
    };
    var grid2 = null;
    Ifx.fn.displayGrid2 = function (gridDef, data, objMoreData, data2) {
        console.log('Ifx.fn.displayGrid2');
        $(_self.panel.query2).show();
        // $(_self.panel.queryHeader).show();
        // if (gridDef.config.header != null) {
        // console.log('gridDef.config.header != null');
        // this.displayGridHeader(gridDef, data2);
        // }
        if (gridDef.config.detail === false) {
            console.log('gridDef.config.detail');
            return;
        }
        if (gridDef.config.exportExcel) {
            if (this.getValue('GRIDEXPORT$') == '0') {
                gridDef.config.exportExcel = false;
            } else {
                gridDef.config.exportExcelOption = this.getValue('GRIDEXPORT$');
            }
        }
        var self = this,
            gridFields = {};
        _.each(gridDef.fields, function (x) {
            gridFields[x.name] = self.getField(x.name);
        });
        if (grid2 != null) {
            var $btn = $('#btnMoreData');
            if (!objMoreData && this.displayGrid2.hasMoreDataBefore) {
                setTimeout(function () {
                    // $btn.val('全部資料已下載');
                    $btn.effect('pulsate', {}, 300, function () {
                        $btn.effect('explode', 400, function () {
                            $btn.hide();
                        });
                    });
                }, 0);
            } else {
                if (objMoreData) {
                    objMoreData.buttonized = true;
                    $btn.effect('pulsate', {}, 1000);
                }
            }
            var goCurrentPage = this.displayGrid2.hasMoreDataBefore;
            grid2.refreshGrid(this.panel.query2, data, true, true);
            return;
        }
        grid2 = new IfxGrid(gridDef, gridFields, data);
        var handlers = {
            'bind': IfxUtl.bind(this, 'bindHandler'),
            'mapBack': mapBack,
            'batchProcessor': gridBatchProcessor,
            'deviceX': deviceX,
            'getValue': function (x) {
                return self.getValue(x);
            },
            'setValue': function (x, y) {
                return self.setValue(x, y);
            },
            'rtnCall': this.rtn.CALL
        };
        if (objMoreData && objMoreData.fnMore) {
            objMoreData.buttonized = true;
            this.displayGrid2.hasMoreDataBefore = true;
            handlers['moreData'] = objMoreData.fnMore;
        }
        grid2.registerHandler(handlers);
        grid2.render(this.panel.query2, this.panel.gridControls_2, resizeSelf);
        // add grid map back NTXBUF5$ = 回傳值
        var nt5 = self.getValue('NTXBUF5$');
        var callerTab = null;
        if (nt5) {
            grid.setMapper(nt5);
        }

        function mapBack(mapper, data) {
            if (_layout != "1") {
                console.log('not support map Back for 2 window');
                return;
            }
            var fnCallerTab = getTabFn('getCallerTab');
            callerTab = fnCallerTab(_tabKey);
            var o = m2o(mapper);
            var fn = getTabFn('redirectCall');
            fn(callerTab, 'xaction', o, data);
            closeMe(_layout, true);
        }

        function m2o(m) {
            var o = {},
                ss;
            _.each(m.split(':'), function (x) {
                ss = x.split('<-');
                if (ss[0] != 'type') o['#' + ss[0]] = '#' + ss[1];
            });
            return o;
        }
    };

    // TODO 整段邏輯關於type的部分可以重寫,需求前後調整與時間差導致程式碼非常亂
    function gridBatchProcessor(cfg, rows, totalRowsCount, ss) {
        _self.batchMode = true;
        var batchCfg = cfg.ifxBatch;
        var rimCode = batchCfg.rim;
        var secretName = batchCfg.txtno;
        var txtData = batchCfg.txtdata;
        var fakerim; // 新增 fakerim for 5系列時假tita label
        var btype = batchCfg.type;
        var start = batchCfg.start; // 起始位置
        var rcvcnt = batchCfg.rcvcnt; // 已傳筆數
        var lastDatasid = batchCfg.lastdatasid; // 最後重算的msgid
        var lastDatastita = batchCfg.lastdatastita; // 最後傳送的 titaHeader
        var beachsend = (batchCfg.eachsend == 'true') ? true : false;
        var onlyprint = batchCfg.onlyprint;

        if (onlyprint) {
            _self.onlyprint = onlyprint;
        }
        var numberid, numberset, numberlen; // 序號加入位置、長度
        var totallen; // 總數加入長度
        if (batchCfg.fakerim == "false") {
            fakerim = false;
        } else {
            fakerim = true;
        }
        if (batchCfg.numberid) {
            numberid = batchCfg.numberid.split(':'); // 位置:長度
            if (numberid.length != 1) {
                numberset = parseInt(numberid[0], 10);
                numberlen = parseInt(numberid[1], 10);
            } else {
                numberset = parseInt(numberid, 10);
                numberlen = 4;
            }
        }
        if (batchCfg.totallen) {
            totallen = parseInt(batchCfg.totallen, 10); // 總數的定義長度
        }
        var oldTxcd = "";
        var t = 0;
        if (btype == "1") {
            oldTxcd = _self.getValue(batchCfg.orig_txcode);
        }
        var pfnxName = batchCfg.pfnx;
        var colCaptions = batchCfg.col_captions;
        var colNames = batchCfg.col_names;
        _self.setValue("#batch-rows-selected", rows.length);
        _self.setValue("#batch-rows", totalRowsCount);
        console.log(batchCfg.name + "\n" + rows.length);
        var text = "",
            temptext = "",
            dataloop = 0,
            oldreadycont = 0,
            checkcont = 0,
            nosendcount = 0;
        var sCount = 0; //已送成功筆數

        function processOneRow(oRow, callback) {
            _self.diyblock(oRow.id); // 增加上傳數量的顯示
            if (btype == "6") {
                var bean = 'url:/mvc/hnd/doc/get/' + oRow[secretName] + "?_addLog=1";
                _self.sendBean(bean, {}, null, function (data) {
                    console.log(data);
                    // callback(null, data.doc);
                    var listdata = {
                        "0": data.doc
                    }; // 要0，不然displayscreen 解析會錯
                    _self.displayOutput(_self.panel.output, listdata, true, true); // trandocScan
                    // 整批重印單據不跑印畫面，需輸出印表機
                    callback(null, "OK|||已送至印表機");
                    return;
                }, function (errData) {
                    console.dir(errData);
                    callback(null, "ERRRIM|||" + errData.errmsg);
                    return;
                });
            }
            if (btype == "7") {
                /* 潘 開機報表 列印Start */
                var bean = 'url:/mvc/hnd/web/file';
                // 固定 分行 日期 型態 檔案
                var sendBeandata = {
                    filename: "repos//report//" + oRow['#filebrn'] + "//" + oRow['#fileday'] + "//" + oRow['#filetype'] + "//" + oRow[secretName] + ".rpt",
                    root: "ifw"
                };
                _self.sendBean(bean, sendBeandata, null, function (data) {
                    console.log(data);
                    /* 潘新增報表整批列印處理 換頁 & 去除控制記號 */
                    var b = true;
                    var content = "";
                    var contentTemp = data.content.split("\n");
                    for (var i = 0; i < contentTemp.length; i++) {
                        if (contentTemp[i].substring(0, 1) == "1") {
                            if (b) {
                                b = false;
                            } else {
                                content += "{{formfeed}}";
                            }
                            content += contentTemp[i].substring(1) + "\n";
                        } else {
                            content += contentTemp[i].substring(1) + "\n";
                        }
                    }
                    // callback(null, data.doc);
                    var resultdata = {};
                    // resultdata.content = data.content; // 潘
                    resultdata.content = content;
                    resultdata.prtwhat = "Z";
                    resultdata.form = "";
                    resultdata.prompt = "開機報表";
                    resultdata.parameter = '{"landscape":"true","printcpi":"15","printlpi":"8","papersize":"830:1170","sformname":"RPTFORM"}';
                    resultdata.dups = "";
                    var listdata = {
                        "0": resultdata
                    }; // 要0，不然displayscreen 解析會錯
                    _self.displayOutput(_self.panel.output, listdata, true, true); // trandocScan
                    // 整批重印單據不跑印畫面，需輸出印表機
                    callback(null, "OK|||已送至印表機");
                    return;
                }, function (errData) {
                    console.dir(errData);
                    callback(null, "ERRRIM|||" + errData.errmsg);
                    return;
                });
            }
            checkcont++;
            if (start && _self.getValue(start) > 0) {
                if (checkcont <= _self.getValue(start)) {
                    nosendcount++;
                    callback(null, "OK|||前次已上傳");
                    return;
                }
            }
            // send rim
            if (batchCfg.maxloop) {
                if (dataloop <= batchCfg.maxloop) {
                    temptext += oRow[txtData];
                    dataloop++;
                    oldreadycont++;
                }
                if (dataloop >= batchCfg.maxloop || oldreadycont == rows.length) {
                    // 1位控制+6總長+6本筆+~
                    if (rcvcnt) {
                        var chhhhh = IfxUtl.numericFormatter(oRow[secretName].slice(7, 13), 6);
                        var rcvcntdata = _self.getValue(rcvcnt);
                        text = oRow[secretName].slice(0, 1) + IfxUtl.numericFormatter(rows.length - nosendcount, 6) + IfxUtl.numericFormatter(rcvcntdata, 6) + IfxUtl.numericFormatter(dataloop, 6) + IfxUtl.numericFormatter(
                            oRow[secretName].slice(19), oRow[secretName].slice(19).length) + temptext + "$";
                    } else {
                        text = temptext;
                    }
                    dataloop = 0;
                    temptext = "";
                } else {
                    callback(null, "OK|||組合");
                    return;
                }
            } else {
                text = '"btnIndex":"' + oRow["btnFild"] + '", ' + oRow[secretName];
                text = '"selectReturnOK":"' + sCount + '", "selectTotal":"' + rows.length + '", ' + '"selectIndex":"' + checkcont + '", ' + text;
                // text = oRow[secretName];
                // text = oRow[secretName] + "$";
            }
            console.log("rim code:" + rimCode + "\nrim text:" + text);
            if (btype == "5") {
                var tmptext = "";
                oldTxcd = rimCode;
                // 序號
                if (numberid) {
                    console.log("numberid[0]:" + numberset);
                    console.log("numberid[1]:" + numberlen);
                    tmptext = text.slice(0, numberset) + IfxUtl.numericFormatter(oRow.id + 1, numberlen) + text.slice(numberset);
                } else {
                    console.log("no numberid");
                    tmptext = text;
                }
                // 總長
                if (totallen) {
                    tmptext = IfxUtl.numericFormatter(rows.length, totallen) + tmptext;
                    console.log("add total len:" + tmptext);
                }
                sendTim(tmptext);
            } else if (btype == "1") {
                _self.sendRim(rimCode, text, function (totaList) {
                    var oTota = totaList[0];
                    // 柯:整批放行時調rim取全部(XI110過長)
                    oTota.obj.EC.KINBR = _self.getValue("#KINBR");
                    oTota.obj.EC.TLRNO = _self.getValue("#TLRNO");
                    if (rimCode == "LCR03") {
                        _self.setValue("#EMPNOS", _self.getValue("#TLRNO"));
                        oTota.obj.EC.rim = "0";
                        oTota.obj.EC.AUTHNO = _self.getValue("#AUTHNO");
                        oTota.obj.EC.EMPNOS = _self.getValue("#TLRNO");
                        oldTxcd = oTota.obj.EC.TXCD;
                        oTota.obj.EC.HCODE = "0";
                        oTota.obj.EC["selectTotal"] = "" + rows.length;
                        oTota.obj.EC["selectIndex"] = "" + checkcont;
                        //oTota.obj.EC.EMPNOS = _self.getValue("#TLRNO");
                        //oTota.obj.EC.SUPNO = _self.getValue("#TLRNO");
                    }
                    var ototatemp = JSON.stringify(oTota.obj.EC);
                    console.log("good ototatemp:" + ototatemp);
                    oRow.tim = ototatemp;
                    // callback(null, oTota.text);
                    sendTim(oRow.tim);
                }, function (oTota) {
                    console.log("error:" + oTota.text);
                    // alert("rim error:"+ oTota.getErrmsg())
                    callback(null, "ERRRIM|||" + oTota.getErrmsg());
                });
            }

            function sendTim(oldTranText) {
                // 如欄位表格中有該變數，則從表格中取出該值替換
                if (oRow[batchCfg.rim]) {
                    oldTxcd = oRow[batchCfg.rim];
                    console.log("oldTxcd change to oRow[" + oldTxcd + "].");
                }
                // var oldTxcd = oRow['#OTXCD'];
                _self.setValue("#TXCD", oldTxcd);
                var text = "";
                // var text = _self.buildText(_self.def.tim.list ||
                // _self.def.tim);
                // _self.def.tim);
                console.log("batchCfg tran text:" + oldTranText);
                if (btype == "5") {
                    if (fakerim == false) {
                        console.log("type5但不要加入前置180.");
                    } else {
                        text = _self.ifxHost.buildTitaLabelForRim(oldTxcd);
                    }
                    text += oldTranText;
                }
                if (btype == "1") { // 少TITA LABEL 需補足
                    text = oldTranText;
                    console.log("new batchCfg tran text:" + text);
                }
                console.log("_server_os ,1 : R6, 0: Windows::?" + _server_os);
                // 回傳TOTA直接變TITA 在R6會有0E 0F問題
                // 無法檢查型態c 故直接取代..
                if (_server_os == "1") {
                    console.log("text->before 47 len:" + text.length);
                    console.log("text->before 47:" + text);
                    // 與原本tita上送邏輯不同(是看欄位型態..),測試看是否ok
                    /*
					 * var temptext = ""; var delspace = 0;
					 * //與原本tita上送邏輯不同(是看欄位型態..),測試看是否ok for(var j=0; j <
					 * text.length;j++){ if(text[j] == '\4'){ delspace++; }else
					 * if(text[j] == '\7'){ delspace++; }else{ if(delspace != 0 &&
					 * text[j] == " "){ delspace--; }else{ temptext += text[j]; } } }
					 * text = temptext;
					 */
                    // text = text.replace(/\7/g, '');
                    // console.log("text->after 47:"+text);
                    text = text.replace(/\4/g, '');
                    text = text.replace(/\7/g, '');
                    console.log("text->after 47 len:" + length);
                    console.log("text->after 47:" + text);
                }
                console.log("text->len:" + text.length);
                console.log("this.RIMTESTNO-sendTim:" + this.RIMTESTNO);
                if (!this.RIMTESTNO) {
                    _self.ifxHost.initSend();
                    // 因為在整批時 會導致 ifx-ims-host.js中 data['updTx'] = "1"; 無法更改
                    // type 5 固定自己組前180
                    _ajaxSender = _self.ifxHost.send(oldTxcd, (btype == "5" || btype == "1") ? false : fakerim, text, function (totaList) {
                        // alert("success");
                        var oBatchBag = {
                            "hi": "1"
                        };
                        _self.rcvErrorHandler(totaList, oBatchBag);
                        // 柯 新增for XX010回傳先塞值問題
                        if (txtData) {
                            _self.processTom(totaList[0]);
                        }
                        if (oBatchBag.success) {
                            // 不刪除了 筆數太多根本刪不到
                            // if(!batchCfg.maxloop){
                            // grid.deleterowGrid(ss[t]); // 柯 新增
                            // 成功時且不是loop才刪除表格
                            // t++;
                            // }
                            // 成功時才刪除表格
                            if (beachsend) {
                                // batchtotalist.push(totaList[0]);
                                for (var i = 0; i < totaList.length; i++) {
                                    if (!totaList[i].isWarnning()) { // 踢掉warning!
                                        // 只保留正常欄位
                                        batchtotalist.push(totaList[i]);
                                    }
                                }
                            }
                            sCount++;
                            callback(null, "OK|||" + totaList[0].getHostSeq() + "|||" + totaList[0].getMrkey() + "|||" + totaList[0].getLastText());
                        } else {
                            // t++; // 柯 新增 成功時才刪除表格
                            callback(null, "ERRTOM|||" + oBatchBag.errmsg);
                        }
                    }, function (oTota) {
                        // alert("error");
                        callback(null, "ERRTOM|||" + oTota.text);
                    });
                } else {
                    console.log("batch sendTim...");
                    alert("上送中心X型態不能包含中文:" + this.RIMTESTNOSTR);
                    // 柯 增加三行
                    _self.diyunblock();
                    _self.unblock();
                    _self.ifxHost.setsendBefore(false);
                }
            }
        }

        function buildReport() {
            var DETAIL_VAR = "#batch-detail";
            // calculate col name width
            var colCaptionWidth = $.map(colCaptions, function (x) {
                return IfxUtl.strlen(x);
            });

            // calculate col value length
            function replicateC(n, ch) {
                return Array(n + 1).join(ch || ' ');
            }

            var paddingMap = {};
            var colValuesWidth = $.map(colNames, function (x, i) {
                var xTrimmed = IfxUtl.trim(x);
                colNames[i] = xTrimmed;
                var leftSpacesLen = IfxUtl.rtrim(x).length - xTrimmed.length; // 左邊空白長度
                var rightSpacesLen = IfxUtl.ltrim(x).length - xTrimmed.length; // 左邊空白長度
                paddingMap[xTrimmed] = [replicateC(leftSpacesLen),
                    replicateC(rightSpacesLen)
                ];
                var fld = _self.getField(xTrimmed);
                var len = IfxUtl.strlen(fld.getPrintValue(true));
                len = leftSpacesLen + len + rightSpacesLen;
                return len;
            });
            // get every col width ==> max (colname, colvalue)
            var colWidth = $.map(colCaptionWidth, function (x, i) {
                return Math.max(x, colValuesWidth[i]);
            });
            // build detail
            var lines = [];
            var sep = " ";
            // col caption
            var cols = $.map(colCaptions, function (x, i) {
                return IfxUtl.stringFormatterBig5(x, colWidth[i]);
            });
            lines.push(cols.join(sep));
            // dash line
            cols = $.map(colWidth, function (x, i) {
                return replicateC(x, "=");
            });
            lines.push(cols.join(sep));
            // detail lines
            $.each(rows, function (j, r) {
                cols = $.map(colNames, function (x, i) {
                    var value = r[x] || '';
                    value = paddingMap[x][0] + value + paddingMap[x][1];
                    return IfxUtl.stringFormatterBig5(value, colWidth[i]);
                });
                lines.push(cols.join(sep));
            });
            _self.setValue(DETAIL_VAR, lines.join("\n"));
            // build pfnx
            var oSelect = null;
            $.each(_self.def["select"], function (i, x) {
                if (x.form == pfnxName) {
                    oSelect = x;
                    return false;
                }
            });
            if (oSelect != null) {
                // make a dummy tota for printPFNX
                var oTota = {
                    getTxForm: function () {
                        return "BATCH";
                    }
                };
                return _self.printPFNX(oSelect, oTota);
            } else {
                alert("沒有相符的單據, 無法列印單據");
                retuls = "error printing";
            }
        }

        var origTxcd = _self.getValue("#TXCD");
        // 0716加入提醒
        var batchtotalist = [];
        // 特殊功能將勾選值送入下一隻交易
        if (btype == "9") {
            var ntxbuf = {};
            ntxbuf["NTXBUF$"] = (rows.length + "").padStart(3, "0");
            rows.forEach((x) => {
                ntxbuf["NTXBUF$"] += x[secretName];
            });
            _self.chainByCall(rimCode, "1", ntxbuf, "");
            return;
        }
        async.mapSeries(rows, processOneRow, function (err, results) {
            _self.setValue("#TXCD", origTxcd);
            _self.diyunblock(); // 清除上傳數量顯示
            if (err) {
                alert(err);
            } else {
                var okCounts = 0;
                var errorCounts = 0;
                $.each(results, function (i, x) {
                    var oRow = rows[i];
                    console.log("row" + i);
                    console.log("mapSeries;" + i + "=>" + x);
                    var resultarray = x.split("|||"); // 使用陣列分開
                    var totaObj;
                    try {
                        totaObj = JSON.parse(resultarray[3]);
                    } catch (e) {
                        console.error(e);
                    }
                    if (resultarray[0] == "OK") {
                        // alert(i + " is error:"+ x);
                        okCounts++;
                        oRow['#batch-row-txtno'] = resultarray.length > 1 ? resultarray[1] : '';
                        oRow['#batch-row-status'] = "成功";
                        oRow['#batch-row-tota'] = resultarray.length > 3 ? resultarray[3] : ''; // 成功的電文
                        oRow['#batch-row-mrkey'] = resultarray.length > 2 ? resultarray[2] : '';
                        oRow['#batch-row-status-show'] = ''; // 錯誤記號
                        oRow['#batch-row-id'] = oRow.id + 1;
                        oRow['.tran-success'] = true; // 不知是啥
                        if (totaObj)
                            if (txtData)
                                oRow[txtData] = totaObj[txtData.substring(1)];
                    } else {
                        errorCounts++;
                        oRow['#batch-row-txtno'] = '';
                        oRow['#batch-row-status'] = "失敗 " + resultarray[1]; // tota在裡面
                        oRow['#batch-row-tota'] = resultarray[1]; // 失敗的電文
                        oRow['#batch-row-mrkey'] = ''; // 電文的mrkey
                        oRow['#batch-row-status-show'] = "失敗";
                        oRow['#batch-row-id'] = oRow.id + 1;
                        oRow['.tran-success'] = false;
                        if (totaObj)
                            if (txtData)
                                oRow[txtData] = totaObj[txtData.substring(1)];
                        // alert(i + " is " + x);
                    }
                });
                _self.setValue("#batch-rows-ok", okCounts);
                _self.setValue("#batch-rows-error", errorCounts);
                var rpt = buildReport(okCounts, errorCounts);
                console.log(rpt.content);
                _self.displayOutput(_self.panel.output, [rpt]);
                if (lastDatasid && lastDatastita) {
                    console.log("顯示列印單據btn...");
                    lastDatasid = _self.getValue(lastDatasid);
                    lastDatastita = _self.getValue(lastDatastita);
                    _self.lastDataSend(lastDatasid, "列印單據", lastDatastita, true);
                }
                // XT90B使用
                if (batchtotalist.length > 0 && beachsend) {
                    console.log("整批架構之各筆列印....");
                    setTimeout(function () {
                        _self.eachSend = true;
                        _self.processOutput_second(batchtotalist, null, true); // 整批就不會在show
                        // alert了
                        $('#btn_yn').off("click mousedown keypress").hide(); // for整批列印後因已經
                        // processTom
                        // 故會導致列印錯誤
                    }, 10);
                }
            }
            _self.batchMode = false;
            console.log("batchMode done");

            for (var i = 0; i < batchCfg.name.split(";").length; i++) {
                $("#btnBatch_" + i).attr('disabled', true);
                $("#btnBatch_" + i).attr("style", "color:gray");
            }

            _self.unblock();
            _self.postTran();
        });
    }

    Ifx.fn.scrollRollBack = function () {
        window.document.body.scrollTop = _self.pageScrollHeight;
        window.document.documentElement.scrollTop = _self.pageScrollHeight;
    }
    Ifx.fn.chainAutoSubmit = function (m, data) {
        if (!_self.isUpdatedTran(_self.getValue("#TXCD")) && _self.getValue("CAUTO$") != "2") canSubmit();
    }
    Ifx.fn.xaction = function (m, data) {
        this.clearLastField();
        var firstk = ""; // 柯 for跳回第一欄 欄位名稱儲存
        for (var k in m) {
            // self.setValue(k, x);
            console.log("xaction:" + k);
            if (firstk == "") {
                firstk = k;
            }
            console.log("xaction:" + data[m[k]]);
            // console.log(IfxUtl.name2id(k));
            // var oo = $('#' + IfxUtl.name2id(k));
            // console.log(oo.val());
            // oo.val(data[m[k]]);
            // console.log(oo.val());
            var fld = this.getField(k);
            if ("DF".indexOf(fld.type) != -1 && data[m[k]].indexOf("/") != -1) { // 柯:新增
                // FD時
                // 刪除斜線
                data[m[k]] = data[m[k]].replace(/\//g, "");
            }
            _self.setValue(k, m[k] == "#" ? "" : data[m[k]]);
        }
        // 柯 增加 查詢btn回來跳回第一欄 start
        if (firstk != "") {
            var targerid = $("#fld_" + firstk.slice(1)).attr('dc-index');

            function foo() {
                if (targerid > 0) {
                    console.log("xaction->targerid:" + targerid + " but maybe not work?");
                    var fld = _self.getField(_self.currentIndex);
                    if (fld.extra) {
                        if (fld.extra.backNext) _self.jumptoBack2(_self.currentIndex + 1, true, true);
                        else _self.jumptoBack2(targerid, true);
                    } else _self.jumptoBack2(targerid, true);
                }
            }

            // 柯 待測試 -> 用秒數判定有可能不會成功
            console.log("xaction go to " + firstk + ":" + targerid + ",0.8s");
            setTimeout(foo, 800);
        } else {
            this.goCurrentField();
        }
    };

    function resetBuckets() {
        // return; //哪時候加入的 return??
        var buckets = ["TOTAMT$", "TOTAMT1$", "TOTAMT2$", "TOTAMT3$", "TOTAMT4$", "TOTAMT5$", "TOTAMT6$", "TOTAMT7$", "TOTAMT8$", "TOTAMT9$"];
        _.each(buckets, function (x) {
            _self.setValue(x, "0");
        });
    }

    Ifx.fn.prepareGridData = function (gridDef, occurs, fnName, bSkipFirst) {
        var result = [],
            self = this,
            r, colNames = _.pluck(gridDef.fields, 'name'),
            i = 0,
            s = 0;
        // resetBuckets();
        _.each(occurs, function (rec) {
            r = {};
            var rectmp;
            // 柯:有長度的話，則是新的device excel轉json
            if (rec.length) {
                _.each(rec, function (rec2) {
                    rectmp = $.extend(rectmp, rec2);
                });
                // 取代原先rec
                rec = rectmp;
            }
            if (!rec["序號"] && self.getValue("SPEEXCEL$").toUpperCase() == "Y") {
                if (s != i) {
                    s = i;
                    i = i - 1;
                } else {
                    i = s;
                }
            }
            r['id'] = i;
            if (!bSkipFirst || i > 0) { // 若資料來自主機電文, 第一筆已經在processTom
                // self.occursToVar(rec);
                self[fnName](rec);
                self.runAfterYN();
            }
            _.each(colNames, function (x) {
                r[x] = preProcessField(x);
            });
            if (!rec["序號"] && self.getValue("SPEEXCEL$").toUpperCase() == "Y") {
                // result[i].concat(r);
                // console.dir("-------------");
                // console.dir(result[i]);
                // console.dir(r);
                /*
				 * 使用 result.pop(); 搭配前面 repeat2var 去合併質 var newObj1 =
				 * Object.nextend(result[i], r); console.dir(newObj1); result[i] =
				 * newObj1;
				 */
            }
            if (!rec["序號"] && self.getValue("SPEEXCEL$").toUpperCase() == "Y") {
                result.pop(); // 先刪掉之前舊的
            }
            result.push(r);
            i++;
        });
        // this.savedGridData = result;
        // return this.savedGridData;
        return result;

        function preProcessField(name) {
            var fld = self.getField(name),
                value = fld.getValue();
            var reNewLine = /\\[n|N]/g;
            // grid's button field, get field's bag('grid') value
            if (fld.bag('grid') != null) {
                return fld.bag('grid');
            }
            if (fld.type == 'F' || fld.type == 'D') {
                value = IfxUtl.dateFormatter(value);
            } else if (fld.type == 'x' || fld.type == 'X') {
                value = value.replace(reNewLine, '\n');
            }
            return value;
        }
    };
    // Ifx.fn.getSavedGridData=function() {
    // return this.savedGridData;
    // };
    Ifx.fn.printPartAsDoc = function (oSelect) {
        console.log("print Part for formId:" + oSelect.form);
        var r = this.printPart(oSelect.form);
        return {
            "form": oSelect.form,
            "realform": IfxUtl.is_array(oSelect.form) ? oSelect.form[2] : oSelect.form, // for
            // 列印和儲存
            // (重複但沒關係，需統一)
            "prompt": r.prompt,
            "content": r.result,
            "kind": oSelect.device.slice(0, 1),
            "prtwhat": oSelect.device.slice(1, 2) ? oSelect.device.slice(1, 2) : "",
            "mandatory": oSelect.mandatory,
            "dupable": oSelect.dupable,
            "printcpi": oSelect.printcpi,
            "printlpi": oSelect.printlpi,
            "papersize": oSelect.papersize ? oSelect.papersize : DEFULT_SIZE,
            "papermargin": oSelect.papermargin,
            "pdfform": oSelect.pdfform,
            "landscape": oSelect.landscape,
            "edit": oSelect.edit
        };
    };
    Ifx.fn.printPFNX = function (oSelect, tota) {
        var self = this,
            formid = tota.getTxForm();
        var mrkeypfnx = "";
        try {
            mrkeypfnx = tota.getMrkey();
        } catch (e) {
            // 沒有mrkey
        }
        console.log("print PFNX for formId:" + oSelect.form);
        var defaultCfg = {
            "tab": ["^", 40, 30]
        };
        var cfg = $.extend({}, defaultCfg);
        var result = $.map(this.def.docs[oSelect.form].content, function (x) {
            var pos = x.indexOf('!!');
            if (pos > 100) {
                var cmd = $.trim(x.slice(pos + 2));
                if (cmd) {
                    cfg = $.extend(cfg, JSON.parse(cmd));
                } else {
                    cfg = $.extend({}, defaultCfg); // restore default
                }
                x = x.slice(0, pos);
            }
            var xx = x.split(cfg.tab[0]);
            if (xx.length == 1) // no tab
                return parseBrace(x);
            var parts = [];
            $.each(xx, function (i, k) {
                var t = parseBrace(k);
                if (cfg.tab[i + 1] != undefined) {
                    t = pad(t, cfg.tab[i + 1]); // format to tab width
                }
                parts.push(t);
            });
            return parts.join("");
        });
        result = result.join('\n');
        return {
            "form": formid,
            "realform": IfxUtl.is_array(oSelect.form) ? oSelect.form[2] : oSelect.form,
            // sformname
            "prompt": this.replaceOutputVar(this.def.docs[oSelect.form].prompt),
            "content": result, // 移除 replaceOutputVar 可能會比較快
            "kind": oSelect.device.slice(0, 1),
            "prtwhat": oSelect.device.slice(1, 2) ? oSelect.device.slice(1, 2) : "",
            "mandatory": oSelect.mandatory,
            "dupable": oSelect.dupable,
            "printcpi": oSelect.printcpi,
            "printlpi": oSelect.printlpi,
            "papersize": oSelect.papersize ? oSelect.papersize : DEFULT_SIZE,
            "papermargin": oSelect.papermargin,
            "pdfform": this.replaceOutputVar(oSelect.pdfform, 2),
            "landscape": oSelect.landscape,
            "edit": this.replaceOutputVar(oSelect.edit, 2).toLowerCase() == "edit" ? true : false,
            "editAll": this.replaceOutputVar(oSelect.editAll, 2).toLowerCase() == "editall" ? true : false,
            "totamrkey": mrkeypfnx, // 整批的方式沒有 //XR929有問題,改改看
            "totacifkey": formid == "TXRP9" ? tota.text.slice(0, 10) : "" // 柯:新增
            // TXRP9
            // 前10碼存CIFKEY
        };
    };

    function pad(v, len) {
        return IfxUtl.stringFormatterBig5(v, Math.abs(len), len > 0);
    }

    function parseBrace(t) {
        var re = /\{\{(#.+?)}{2}/g;
        var ss;
        return t.replace(re, function (wholeMatch, name) {
            if (name[name.length - 1] == '!') {
                name = name.slice(0, name.length - 1);
                if (name == '#now') return IfxUtl.getNowString();
            } else if (name.indexOf(':') != -1) {
                ss = name.split(':');
                if (isNaN(ss[1])) {
                    return replaceIt(_self.getField(ss[0]).getPrintValue(false), ss[1]);
                } else {
                    return pad(_self.getValue(ss[0]), parseInt(ss[1]));
                }
            } else {
                // 長度暫定大於200就是特殊的需求,如{{#SWIFT_TEXT_PRT}} 等..故去空白列印
                var fieldname = _self.getField(name);
                if (fieldname.getDisplayLength() > 200) {
                    return IfxUtl.rtrim(fieldname.getPrintValue(true));
                }
                return fieldname.getPrintValue(true);
            }
        });

        function replaceIt(text, template) {
            var eatOne = poundSignEater(text),
                replaceCh, ch, result = '';
            for (var i = 0; i < template.length; i++) {
                var ch = template.charAt(i);
                if (ch != "#") {
                    result += ch;
                } else {
                    replaceCh = eatOne();
                    result += replaceCh;
                    if (IfxUtl.strlen("" + replaceCh) > 1) i++;
                }
            }
            return result;
        }
    }

    Ifx.fn.printWithTemplate = function (oSelect, tota) {
        var formid = tota.getTxForm();
        console.log("print formId:" + oSelect.form);
        var docTemplate = splitTemplate(this.def.docs[oSelect.form].content);
        console.dir(docTemplate);
        var result = docTemplate.detail != null ? this.generateMultiplePageDoc(oSelect, docTemplate, tota) : this.generateSinglePageDoc(oSelect, docTemplate, tota);
        if (result != null) {
            return {
                "form": formid,
                "realform": IfxUtl.is_array(oSelect.form) ? oSelect.form[2] : oSelect.form,
                "sformname": this.replaceOutputVar("SFORMNAME$", 2), // oSelect.sformname
                "prompt": this.replaceOutputVar(this.def.docs[oSelect.form].prompt),
                "content": this.replaceOutputVar(result), // 移除
                // replaceOutputVar
                // 可能會比較快
                "kind": oSelect.device.slice(0, 1),
                "prtwhat": oSelect.device.slice(1, 2) ? oSelect.device.slice(1, 2) : "",
                "mandatory": oSelect.mandatory,
                "dupable": oSelect.dupable,
                "printcpi": oSelect.printcpi,
                "printlpi": oSelect.printlpi,
                "papersize": oSelect.papersize ? oSelect.papersize : DEFULT_SIZE,
                "papermargin": oSelect.papermargin,
                "pdfform": this.replaceOutputVar(oSelect.pdfform, 2),
                "landscape": oSelect.landscape,
                "edit": this.replaceOutputVar(oSelect.edit, 2).toLowerCase() == "edit" ? true : false,
                "editAll": this.replaceOutputVar(oSelect.editAll, 2).toLowerCase() == "editall" ? true : false,
                "printNo": oSelect.printNo,
                "titleName": oSelect.titleName,
                "execProg": oSelect.execProg,
                "totamrkey": tota.getMrkey(),
                "totacifkey": formid == "TXRP9" ? tota.text.slice(0, 10) : "" // 柯:新增
                // TXRP9
                // 前10碼存CIFKEY
            };
        } else {
            return false;
        }
    };
    Ifx.fn.replaceOutputVar = function (buf, gotype) {
        // 柯:PFN傳入變數可供列印 額外增加10組
        if (typeof buf === 'undefined') {
            console.log("replaceOutputVar buf error!");
            return "";
        }
        // 柯:PFN傳入變數可供列印 額外增加10組
        var self = this,
            names;
        var reName, rePage = /&PAGE/igm,
            page = 0;
        gotype = gotype ? gotype : 0;
        switch (gotype) {
            case 1:
                names = ['PAGENUMBER$', 'TITLE$'];
                break;
            case 2:
                names = ['SFORMNAME$', 'SPDFFORM$', 'SEDITFORM$', 'TITLE1$']; // TODO
                // 等VAR
                // 全部換版後,才能移除TITLE1
                break;
            default:
                names = ['TITLE$', 'TITLE1$', 'TITLE2$', 'TITLE3$', 'TITLE4$', 'TITLE5$', 'TITLE6$', 'TITLE7$', 'TITLE8$', 'TITLE9$', 'TITLE10$'];
        }
        $.each(names, function (i, x) {
            x = x.replace(/\$/, '\\$');
            reName = new RegExp('(' + x + ')', 'mgi');
            buf = buf.replace(reName, function (m, k) {
                return IfxUtl.trim(self.getValue(k)); // 柯:這個部分 清除左右空白
            });
        });
        buf = buf.replace(rePage, function () {
            return ++page;
        });
        return buf;
    };
    // 增加grid的位置判斷
    Ifx.fn.getQueryForm = function (printWhat, gridplace) {
        // this.def['select'] --> printWhat
        // 原本為從頭找到尾，更改為有要印(printWhat)的當中找到一筆表格
        var x;
        console.log("printWhat " + printWhat); //
        console.log("this.def['select']  " + this.def['select']); // 柯 增
        for (var i = 0; i < printWhat.length; i++) {
            x = printWhat[i];
            if (isQueryForm(x)) return x;
        }
        return null;

        function isQueryForm(x) {
            return x.device.match(/QUERY/) && x.config.place == gridplace; // 新增判斷
        }
    };
    Ifx.fn.printWhat = function (oSelect) {
        var self = this,
            result = [];
        $.each(this.def["select"], function (i, x) {
            // if(isPrintForm(x)) {
            if (x.value === "9") result.push(x);
            else {
                console.log("field " + x.field + " value:" + self.getValue(x.field) + "==? select value:" + x.value);
                if (self.getValue(x.field) == x.value || parseInt(self.getValue("FKEY$"), 10) == 9) { // viewjournal時
                    // 9
                    // 都印...不清楚要印啥
                    console.log("output select ok, print this form: " + x.form);
                    result.push(x);
                } else {
                    console.log("output select match none, don't print " + x.form);
                }
            }
            // }
        });
        console.log("printWhat-result " + result);
        return result;

        function isPrintForm(x) {
            return !x.device.match(/QUERY/);
        }
    };

    function checkkTomBuddy(formName) {
        var b = _self.def.tom[formName + "/buddy"];
        if (!b) return;
        b = b.split(";");
        console.log(formName + "/buddy:" + b);
        $.each(b, function (i, x) {
            console.log("==>" + x);
            var ss = x.split("=");
            console.log(ss[0] + "=" + ss[1]);
            _self.setValue($.trim(ss[0]), $.trim(ss[1]));
        });
    }

    Ifx.fn.processTom = function (oTota) {
        if (oTota.getTxForm() == "GM001") return;
        var self = this,
            tomFields = $.map(this.def.tom[oTota.getTxForm()], function (x) {
                return self.getFieldForTom(x);
            });
        checkkTomBuddy(oTota.getTxForm());
        var settings = this.getLoopSettings();
        var occursMode = oTota.obj.occursList.length > 0;
        if (occursMode) {
            oTota.parsedOccurs = null;
            oTota.parsedOccurs = oTota.getOccurs(settings, tomFields, true);
            this.occursToVar(oTota.parsedOccurs[0]);
        } else {
            oTota.parseTotaForm(tomFields);
        }
        this.runAfterYN();
    };
    Ifx.fn.mergeTomWithForm = function (oTota, oSelect) {
        var result = [],
            self = this;
        var o = self.printWithTemplate(oSelect, oTota);
        if (o != false) result.push(o);
        return result;
    };

    function printScreen() {
        var bEditPattern = true;
        var buf = ifxUI.printScreen(_self.def, // 增加底線
            function (nouse, x, bBuddy) {
                var fld = _self.getField(x); // 增加底線
                if (!fld.isVisible()) return ""; // 新增
                return fld.getPrintValue(bEditPattern) + (bBuddy === true ? "" : " ");
            });
        console.log(buf);
        return buf;
    };
    Ifx.fn.generateSinglePageDoc = function (oSelect, docTemplate, tota) {
        var self = this;
        result = doFormat(this, oSelect.head, docTemplate.head);
        result = addPrintScreen2(result); // if screen copy, replace <<screen
        return result;

        function addPrintScreen2(ressult) {
            var re = /<<<print screen>>>/m;
            result = result.replace(re, function () {
                return printScreen();
            });
            return result;
        }

        function addPrintScreen(result) {
            if (!result) return result;
            var found = false,
                index = -1;
            var rr = result.split("\n");
            for (var i = 0; i < rr.length; i++) {
                console.log($.trim(rr[i]).toLowerCase());
                if ($.trim(rr[i]).toLowerCase() == "<<<print screen>>>") {
                    found = true;
                    index = i;
                    break;
                }
            }
            if (!found) return result; // non print screen
            rr.splice(index, 1); // �踵�<<screen copy>>
            rr.splice(index, 0, self.printScreen()); // �printScreen()蝯�
            rr = flatten(rr); // 頨箏像
            return rr.join("\n");
        }
    };
    Ifx.fn.generateMultiplePageDoc = function (oSelect, docTemplate, tota) {
        var self = this,
            settings = null,
            tomDef = this.def.tom[tota.getTxForm()],
            tomFields, lastMsg = tota.getMsgEnd() == "1",
            lines = 0, // 柯 移除 oSelect.lines ||
            // oSelect.lines
            // ||
            pages = oSelect.pages || 1;
        console.log("total lines:" + lines);
        console.log(" oSelect.detail:" + oSelect.detail);
        console.log(" docTemplate.detail:" + docTemplate.detail);
        settings = this.getLoopSettings();
        tomFields = $.map(tomDef, function (x) {
            return self.getFieldForTom(x);
        });
        var occurs = tota.parsedOccurs;
        if (occurs == null) occurs = tota.getOccurs(settings, tomFields, true); // reset
        // occurs
        // array
        // array
        // 柯 新增 "印表排序" start
        if (oSelect.sort) {
            var oSelectsort = oSelect.sort.replace(/\@/g, '#').replace(/\&/g, ',');
            console.log("occurs 執行排序:" + oSelectsort);
            IfxUtl.sortObjects(occurs, oSelectsort);
        }
        // end
        console.log(JSON.stringify(occurs, null, 4));
        console.log("total " + occurs.length + " occurs ");
        var pageOccurs = settings.inqprt;
        if (isNaN(pageOccurs)) {
            throw "Programmer Bug:INQPRT$內容錯誤!";
        }
        console.log("page occurs:" + pageOccurs);
        // resetBuckets();
        var result = "";
        var pageResult = "";
        var pageArray = [];
        var subTotalPrinted = false;
        var pagenumber = 1;
        console.log("total occurs:" + occurs.length);
        for (var i = 0; i < occurs.length; i++, lines++) {
            subTotalPrinted = false;
            // 柯 如果排序則第一筆會被換掉故新增
            if (i > 0 || oSelect.sort) { // first record was run at
                // processTom() already
                this.occursToVar(occurs[i]);
                this.runAfterYN();
            }
            // if (lines % pageOccurs == 0) { // process header
            // result += doFormat(self, oSelect.head, docTemplate.head);
            // }
            // 目前無此需求 doFormatsetlines (指定列印位置)
            pageResult += doFormat(self, oSelect.detail, docTemplate.detail) + '\n'; // 最後要加?
            if (((lines + 1) % pageOccurs == 0) && ((lines + 1) != occurs.length)) { // ke
                // 新增
                // &&後段
                console.log("reach page occurs:" + pageOccurs + ", i:" + i);
                pageResult += printSubtotal();
                pageResult += "{{formfeed}}" + '\n'; // 柯 補換頁記號
                console.log("formfeed");
                pageArray.push(pageResult);
                pageResult = "";
                pages++;
            }
        }
        oSelect.lines = lines; // store printed lines
        oSelect.pages = pages; // store printed pages
        if (!subTotalPrinted) {
            pageResult += printSubtotal();
            pageArray.push(pageResult);
            pageResult = "";
            pages++;
        }
        var headerPart = doFormat(self, oSelect.head, docTemplate.head);
        var docArray = [];
        var pagenumber = 1; // 增加頁次給變數在 PFN內替換
        _.each(pageArray, function (x) {
            self.setValue("PAGENUMBER$", pagenumber.toString());
            pagenumber++;
            docArray.push(self.replaceOutputVar(headerPart, 1));
            docArray.push(x);
        });
        result = docArray.join("");
        if (lastMsg) {
            if (docTemplate.total) {
                result += doFormat(self, oSelect.total, docTemplate.total);
            }
            // this.appendDupDoc(oSelect.form, result);
        }
        console.log(result);
        tota.resetOccurs();
        return result;

        function printSubtotal() {
            if (docTemplate.subtotal) {
                return doFormat(self, oSelect.subtotal, docTemplate.subtotal);
            }
            return "";
        }
    };

    function doFormat(self, fields, template) {
        console.log("doFormat combine :" + fields);
        console.log(typeof self);
        var text = "",
            v;
        $.each(fields, function (i, x) {
            v = self.getField(x).getPrintValue(true);
            console.log(x + " value:[" + self.getValue(x) + "] ==>[" + v + "]" + " len:" + IfxUtl.strlen(v));
            text += v;
        });
        console.log("text:[" + text + "]");
        console.log("text len:" + IfxUtl.strlen(text));
        console.log("with template:" + template);
        var j = 0;
        for (var k = 0; k < template.length; k++)
            if (template.charAt(k) == "#") j++;
        console.log("number of pound:" + j);
        var result = "";
        var eatOne = poundSignEater(text);
        var replaceCh;
        for (var i = 0; i < template.length; i++) {
            var ch = template.charAt(i);
            if (ch != "#") {
                result += ch;
            } else {
                replaceCh = eatOne();
                result += replaceCh;
                if (IfxUtl.strlen("" + replaceCh) > 1) i++;
            }
        }
        console.log(result);
        return result;
    }

    // 功能:指定位置列印
    // 欄位:行,列,內容
    function doFormatsetlines(self, fields) {
        console.log("doFormat set lines :" + fields);
        console.log(typeof self);
        var text = "";
        var linenum = 0,
            startnum = 0;
        $.each(fields, function (i, x) {
            if (i == 0) {
                linenum = parseInt(self.getValue(x), 10); // 預空行數
            } else if (i == 1) {
                startnum = parseInt(self.getValue(x), 10); // 預空列數
            } else if (i == 2) {
                for (var k = 0; k < linenum; k++) {
                    text += '\n';
                }
                for (var j = 0; j < startnum; j++) {
                    text += " ";
                }
                text += self.getValue(x); // 補齊後放入內容
            }
        });
        result = text;
        console.log(result);
        return result;
    }

    function poundSignEater(s) {
        var i = 0;
        return function () {
            return s.charAt(i++);
        };
    }

    function splitTemplate(lines) {
        var s = lines.join("\n");
        var head, detail, subtotal, total;
        var arr = find(["*"], s);
        head = arr[0];
        if (arr[1]) {
            arr = find(["|", "~"], arr[1]);
            detail = arr[0];
            if (arr[1]) {
                if (arr[2] == "|") {
                    arr = find(["~"], arr[1]);
                    subtotal = arr[0];
                    total = arr[1];
                } else {
                    total = arr[1];
                }
            }
        }
        // 柯: 拿掉第一位的換行，因為這樣會多一行
        if (total && total.indexOf("\n") == 0) {
            total = total.slice(1);
        }
        return {
            "head": head,
            "detail": detail,
            "subtotal": subtotal,
            "total": total
        };

        function find(tokens, s) {
            var arr = [];
            for (var k = 0; k < tokens.length; k++) {
                token = tokens[k];
                var i = s.indexOf(token);
                if (i != -1) {
                    arr.push(s.slice(0, i));
                    arr.push(s.slice(i + 1));
                    arr.push(token);
                    return arr;
                }
            }
            arr.push(s);
            arr.push(null);
            return arr;
        }
    };
    Ifx.fn.occursToVar = function (rec) {
        for (n in rec.hdrFields) {
            this.getField(n).setValueFromTom(rec.hdrFields[n]);
        }
        for (n in rec.occursFields) {
            this.getField(n).setValueFromTom(rec.occursFields[n]);
        }
    };
    Ifx.fn.getLoopSettings = function () {
        var obj = {};
        obj.inqhd = parseInt(this.getValue("INQHD$", 10));
        obj.inqlen = parseInt(this.getValue("INQLEN$", 10));
        obj.inqprt = parseInt(this.getValue("INQPRT$"), 10);
        obj.inqrec = parseInt(this.getValue("INQREC$"), 10);
        obj.loopheight = parseInt(this.getValue("LOOPHEIGHT$"), 10);
        // obj.loopIndex = this.getField("#LOOP").index;
        return obj;
    };

    function getDefaultLayout() {
        var _layout = {
            cols: 2,
            screen: {
                width: [160, 400, 160, 400],
                left: 0
            },
            printer: {
                width: [20, 20, 20, 20],
                top: 2,
                left: 3,
                gap: 1,
                colon: true,
                linesPerPage: 66
            }
        };
        return _layout;
    }

    /*
	 * $.fn.highlightOnce.defaults = { color : '#fff47f', duration : 'fast' };
	 * options = $.extend({}, $.fn.highlightOnce.defaults, options);
	 * TODO:�寧jquery extend靘�隞υverrideLayout
	 *
	 */
    function overrideLayout(layout, name, d) {
        if (d.layout) {
            var cc = d.layout.split(/\s*;\s*/);
            $.each(cc, function (i, x) {
                if (x) {
                    var o = layout;
                    var ss = x.split(/\s*=\s*/);
                    if (ss[0] == "cols" && ss[1] == "1") {
                        layout.screen.left = 70;
                    }
                    var aa = ss[0].split(".");
                    $.each(aa, function (i, y) {
                        if (!(y in o)) {
                            console.log("find no attrib " + y + ", probably invalid layout override,  please check " + x + " of part " + name);
                        } else {
                            if (i < aa.length - 1) {
                                o = o[y];
                            } else {
                                o[y] = eval('(' + ss[1] + ')');
                            }
                        }
                    });
                }
            });
        }
        console.dir(layout);
        return layout;
    }

    function replaceCounter(self, s, filer) {
        if (s.match(/\s\^/)) {
            self.fldCounter = self.fldCounter + 1;
            var t = self.fldCounter.toString();
            if (t.length < 2) t = filer + t;
            return s.replace(/\s\^/, t);
        } else {
            return s;
        }
    }

    function pageing(r, layout) {
        var top = layout.printer.top,
            left = layout.printer.left,
            linesPerPage = layout.printer.linesPerPage,
            counter = 0,
            lines = [];
        var topLines = top > 0 ? (new Array(top + 1)).join("\n") : "";
        var leftSpaces = format("", left);
        $.each(r, function (i, x) {
            if (counter == 0) {
                lines = lines.concat(topLines);
            }
            if (x == '\f') {
                counter = 0;
            } else {
                lines.push(leftSpaces + x);
                counter++;
                if (i == linesPerPage) {
                    lines.push('\f');
                    counter = 0;
                }
            }
        });
        return lines;
    }

    function flatten(array) {
        var flat = [];
        for (var i = 0, l = array.length; i < l; i++) {
            var type = Object.prototype.toString.call(array[i]).split(' ').pop().split(']').shift().toLowerCase();
            if (type) {
                flat = flat.concat(/^(array|collection|arguments|object)$/.test(type) ? flatten(array[i]) : array[i]);
            }
        }
        return flat;
    }

    function format(v, len) {
        return IfxUtl.stringFormatterBig5(v, len);
    }

    function replaceOutputdata(xcontent) {
        var ss = xcontent.replace(/\r/g, '').replace(/\n/g, '\r\n');
        ss = ss.replace(/&gt;/g, ">").replace(/&lt;/g, "<");
        var contentlsit = ss.split("\r\n");
        $.each(contentlsit, function (i, x) {
            contentlsit[i] = IfxUtl.rtrim(x);
        });
        ss = contentlsit.join("\r\n");
        return ss;
    }

    // 給做規格書使用
    Ifx.fn.printScreenForEric = function () {
        var bEditPattern = true;
        var withvisible = true; // 不論是否隱藏該<sub> DC,一併顯示
        var buf = ifxUI.printScreen(_self.def, // 增加底線
            function (nouse, x, bBuddy) {
                var fld = _self.getField(x); // 增加底線
                return fld.getPrintFake(bEditPattern) + (bBuddy === true ? "" : " ");
            }, withvisible);
        var screenData = buf,
            filename = _self.getTmpFilenamefake();
        ifxFile.put(filename, screenData, _self.makeBlockers(), function (data) {
            if (data.status) {
                alert('儲存輸出[規格書]成功...\n路徑:X:/ifxwriter/temp/datasheet/');
            } else {
                alert('錯誤:' + data.msg);
            }
        });
    };
    Ifx.fn.printScreen = function () {
        var self = this;
        this.fldCounter = 0;
        var dcFieldMap = {};
        $.each(this.dcFields, function (i, x) {
            dcFieldMap[x.name] = x;
        });
        var layout = getDefaultLayout();
        var result = [];
        var columns = parseInt(layout.cols, 10);
        $.each(this.def.display, function (i, p) {
            layout = overrideLayout(layout, p.name, p.def);
            result.push(columns == 1 ? printOneColumn(p) : printTwoColumn(p));
        });
        // result = pageing(flatten(result), layout);
        $.each(result, function (i, x) {
            console.log(x);
        });
        return result;

        function getWidth(i) {
            var offset = 0;
            if (columns > 1 && i % 2 != 0) {
                offset = 2;
            }
            return {
                labelWidth: parseInt(layout.printer.width[offset], 10),
                fieldWidth: parseInt(layout.printer.width[offset + 1], 10)
            };
        }

        function printOneColumn(p) {
            var result = [],
                col, w = getWidth(0),
                list = p.def.list,
                i = 0,
                caption = list[i++][0];
            if (caption) result.push(caption);
            for (; i < list.length;) {
                col = printColumn(list[i], w.labelWidth, w.fieldWidth);
                result.concat(col);
            }
            return result;
        }

        function printTwoColumn(p) {
            var result = [],
                leftCol, rightCol, w, list = p.def.list,
                i = 0,
                caption = list[i++][0];
            if (caption) result.push(caption);
            for (; i < list.length;) {
                console.log(p.name + ", #" + i + ", " + list[i][0]);
                if (list[i][0] == "\f") {
                    result.push("\f");
                    i++;
                    continue;
                }
                leftCol = null, rightCol = null;
                w = getWidth(i);
                if (list[i][0] == "#<->#") {
                    list[i].shift(); // shift out "#<->#"
                    leftCol = printColumn(list[i], w.labelWidth, w.fieldWidth * 2);
                } else {
                    leftCol = printColumn(list[i], w.labelWidth, w.fieldWidth);
                    if (list[i + 1] && list[i + 1][0] != "#<->#") {
                        w = getWidth(i + 1);
                        rightCol = printColumn(list[i + 1], w.labelWidth, w.fieldWidth);
                    }
                }
                if (rightCol != null) {
                    result = result.concat(combine(leftCol, rightCol));
                    i += 2;
                } else {
                    result = result.concat(leftCol);
                    i += 1;
                }
            }
            return result;
        }

        function combine(colLeft, colRight, leftDef, rightDef) {
            var result = [],
                max = Math.max(colLeft.length, colRight.length),
                gap = format("", layout.printer.gap),
                left, right;
            var leftWidth = getWidth(0).labelWidth + getWidth(0).fieldWidth;
            var rightWidth = getWidth(1).labelWidth + getWidth(1).fieldWidth;
            for (var i = 0; i < max; i++) {
                left = colLeft[i] ? colLeft[i] : "";
                right = colRight[i] ? colRight[i] : "";
                result.push(format(left, leftWidth) + gap + format(right, rightWidth));
            }
            return result;
        }

        function printColumn(line, labelWidth, fieldWidth) {
            var result = [],
                label = line.shift(),
                t, u, buffer = "";
            if (label.length > 0) {
                label = replaceCounter(self, label, " ");
                if (layout.printer.colon) {
                    u = format(label, labelWidth - 2) + ": ";
                } else {
                    u = format(label, labelWidth);
                }
                buffer += u;
            } else labelWidth = 0;
            $.each(line, function (i, x) {
                t = "";
                if (x == "<br/>") {
                    buffer += "\n";
                    if (labelWidth > 0) buffer += format("", labelWidth);
                } else {
                    if (x.match(/#/)) {
                        buffer += self.getField(x).getPrintFormatted();
                    } else {
                        buffer += x;
                    }
                }
            });
            console.log(buffer);
            return buffer.split("\n");
        }
    };
    //
    // ifx key handler and main screen layout helper function
    //
    Ifx.fn.getCurrentLayout = function () {
        if (top.window.location.toString().indexOf('easy') == -1) {
            return "2"; // 2 win
        } else {
            return "1"; // tab
        }
    };
    Ifx.fn.clearScreen = function (fn) {
        var self = this;
        setTimeout(function () {
            $(self.panel.entry).hide('fast', function () {
                $(self.panel.help).hide();
                $(self.panel.entry).html('');
                $(self.panel.entry).show('fast', function () {
                    if (fn) fn();
                });
            });
        }, 1);
    };
    Ifx.fn.KeysCopyTita = function () {
        var tita = window.prompt("TITA:", "");
        runWithOldTita(tita);
    };

    function runWithOldTita(tita) {
        if (!tita) return;
        console.log("tim.list:" + _self.def.tim.list);
        parseTim(_self.def.tim.list, tita);
        if (window.confirm("直接送出?")) {
            setTimeout(function () {
                _self.transmit();
            }, 10);
        } else {
            _self.step = "dc";
            _self.currentIndex = -1;
            _self.goField(0);
        }
    }

    Ifx.fn.KeysEC = function () {
        if (this.tranEnd || this.canCorrect === false) {
            alert('此交易不得更正(或交易已完成)');
            return;
        }
        window.location = encodeURI(window.location.toString() + "&m=ec");
    };
    Ifx.fn.KeysApproval = function () {
        if (this.tranEnd || this.canApprove === false) {
            alert('此交易不需放行(或交易已完成)');
            return;
        }
        window.location = encodeURI(window.location.toString() + "&m=approval");
    };

    function goOrigUrl() {
        var txcode = getQueryVariable('txcode');
        alert(getPathFromUrl() + "?txcode=" + txcode);
        window.location = encodeURI(getPathFromUrl() + "?txcode=" + txcode);
    }

    function getPathFromUrl() {
        var url = encodeURI(window.location.toString());
        return url.split("?")[0];
    }

    function getQueryVariable(variable) {
        var query = window.location.search.substring(1);
        var vars = query.split('&');
        for (var i = 0; i < vars.length; i++) {
            var pair = vars[i].split('=');
            if (decodeURIComponent(pair[0]) == variable) {
                return decodeURIComponent(pair[1]);
            }
        }
        console.log('Query variable %s not found', variable);
        return null;
    }

    Ifx.fn.KeysECPostProcess = function (mode, txno, rimName, fkey) {
        var self = this,
            prompt, rimCode;
        _self.fkey = fkey;
        console.log("KeysECPostProcess mode:" + mode + ",txno:" + txno + ",rim:" + rimName + ", fkey:" + fkey);
        mode = mode.toUpperCase();
        if (mode == 'EC') {
            prompt = '更正序號';
            rimCode = rimName || '000010';
        } else if (mode == 'APPROVAL') {
            prompt = '放行序號';
            rimCode = rimName || '000020';
        } else {
            prompt = '審核序號';
            rimCode = rimName || '000020';
        }
        // evt.preventDefault();
        // set I fields to disabled
        var all = $('.field_attr_I');
        if (mode == 'EC' || mode == 'APPROVAL') { // 提出只有更正與放行才要灰色
            all.addClass('field_I_disabled');
        }
        $.each(all, function (i, x) {
            _self.getField(IfxUtl.id2name(x.name)).enabled(false);
            if (_self.getField(IfxUtl.id2name(x.name)).type == "D" && _self.getField(IfxUtl.id2name(x.name)).len >= 7)
                if ($('#' + x.name).datepicker) setTimeout(function () {
                    $('#' + x.name).datepicker('disable');
                }, 500)
        });
        var r = txno;
        if (!r) {
            while (true) {
                r = window.prompt('請輸入' + prompt + '：', '0000000000000000');
                if (!r) {
                    // goOrigUrl();
                    closeMe(_layout, true);
                    return;
                }
                if (r == '000000000000' || r.length != 16) {
                    alert(prompt + '輸入錯誤');
                } else {
                    break;
                }
            }
        }
        // _self.setValue("FKEY$", fkey);
        // if (mode == 'EC') {
        // self.setValue("RENO$", "1");
        // // self.setValue("FKEY$", "1");
        // self.setValue("OBRN$", r.substr(0, 4));
        // self.setValue("OTLRNO$", r.substr(4, 2));
        // self.setValue("ONSEQ$", r.substr(6, 8));
        // } else {
        // self.setValue('RELFG$', '1');
        // //self.setValue("FKEY$", "2");
        // self.setValue('OBRN$', r.substr(0, 4));
        // self.setValue('OTLRNO$', r.substr(4, 2));
        // self.setValue('ONSEQ$', r.substr(6, 8));
        // self.setValue('RELNO$', r.substr(0, 14));
        // }
        // var entday = r.slice(0, 8); // 先取8位日期
        // var brn = r.substr(8, 4);
        // var tlrno = r.substr(12, 2);
        // var txno = r.substr(14, 8);
        var t = JSON.parse("{" + r);
        var entday = parseInt(t.Entdy, 10) + 19110000;
        var brn = t.TxNo.substr(0, 4);
        var tlrno = t.TxNo.substr(4, 6);
        var txno = t.TxNo.substr(10, 8);
        _self.setValue("OBRN$", brn);
        _self.setValue("OTLRNO$", tlrno);
        _self.setValue("ONSEQ$", txno);
        switch (fkey) {
            case 1:
                _self.setValue("#HCODE", "1");
                break;
            case 2:
                _self.setValue("#ACTFG", t.Actfg.slice(-1)); // last one is
                // ACTFG
                break;
        }
        var fnProtectMode = null; // default : execute every fields
        // 更正 放行 延遲重登 personal查詢(交易明細)
        if (fkey == 1 || fkey == 2 || fkey == 6 || fkey == 8 || fkey == 9) {
            fnProtectMode = function () { // protect every fields
                setAllRun(false);
            };
            // FOR 交易明細，為了要可以FOCUS在此按鈕上，故假裝成離開按鈕，並隱藏無用的[儲存]與[離開]按鈕
            if (fkey == 8) {
                $('#btn_yn').attr("value", "離開");
                $('#xbtn_cancel').off("click").hide();
                $('#xbtn_save').off("click").hide();
                $('#btn_yn').off("click mousedown keypress").on('mousedown', function () {
                    _self.KeysEscapeTran();
                });
                // 交易明細關閉重印單據(已在XX9JL有此功能)
                // _self.viewDupdoc(entday+brn+tlrno+txno); //DDCHECK 交易序號
                // 柯: 加入重印功能比對HostTran.java中 因timeout回傳之 系統變數ERTXTNO$
                var ertxtno = _self.getValue("ERTXTNO$");
                var txnotmp = txno;
                var ConfirmChk = false;
                console.log("ERTXTNO$:" + ertxtno);
                console.log("txnotmp:" + txnotmp);
                console.log("tlrno:" + _self.getValue('TLRNO$'));
                console.log("brno :" + _self.getValue('BRN$'));
                /*
				 * 潘 暫時註解 if (tlrno == _self.getValue('TLRNO$') && brn ==
				 * _self.getValue('BRN$')) ConfirmChk =
				 * window.confirm('是否需逾時重印?'); // if((txnotmp == ertxtno &&
				 * tlrno == _self.getValue('TLRNO$') ) || //
				 * window.confirm('是否重送單據?')){ // if( (txnotmp == ertxtno ||
				 * window.confirm('是否需逾時重印?') ) && tlrno == //
				 * _self.getValue('TLRNO$') ){ if (txnotmp == ertxtno ||
				 * ConfirmChk) { alert("提示:請點選逾時重印按鈕即可解除控管"); var sendtitaxx006 =
				 * brn + tlrno + (ertxtno != "" ? ertxtno : txnotmp); var
				 * txtnoxx006 = (ertxtno != "" ? ertxtno : txnotmp); // 20180123
				 * 潘 _self.lastDataSend("XX006", "逾時重印", sendtitaxx006, false,
				 * txtnoxx006); // 不自動印 //
				 * _self.lastDataSend("XX006","逾時重印",sendtitaxx006,false,ertxtno);
				 * //不自動印 }
				 */
            }
            // end
        }
        _self.runSysRtn();
        something = saveSomething();
        // RIM: 交易序號 KINBR + TLRNO + TXTNO
        var text = r + "$";
        console.log("rim text:" + text);
        _self.sendRim(rimCode, text, function (totaList) {
            var oTota = totaList[0];
            // 回傳此調rim之折返的tota並合併
            var alltotacombind = oTota.obj.EC;
            console.log("good:" + alltotacombind);
            // _self.setValue("#ACTFG",oTota.getACTFG());
            console.dir(self.def.tim);
            restoreSomething(something);
            var fnParseTita = function () {
                //更正取代ENTDY為當下會計日
                alltotacombind["ENTDY"] = _self.getValue("ENTDY$");
                parseTim(_self.def.tim.list, alltotacombind);
            }
            setTimeout(function () {
                _self.getResv(entday, brn, tlrno, txno, fnParseTita, runFKEYMode);
            }, 1);
        }, function (oTota) {
            console.log("error:" + oTota.text);
            // goOrigUrl();
            setTimeout(function () {
                closeMe(_layout, true);
            }, 1000);
            return;
        });

        function runFKEYMode() {
            _self.refreshStatus();
            _self.runSysRtn();
            if (fnProtectMode != null) fnProtectMode();
            runPreDC();
            this.step = "dc";
            // 鍾 更正及連動時確定從第一個欄位執行
            _self.currentIndex = -1;
            // end
            _self.goField(0);
            $('html, body').animate({
                scrollTop: 0
            }, 10);
            if (_self.fkey == 8) {
                $("#btn_yn").attr("value", "離開");
                for (var i = 1; i <= 5; i++)
                    $("#btn_new" + i).hide();
            }
        }

        function initHMode() {
            for (var k in self.fieldMap) {
                self.fieldMap[k].setHMode();
            }
        }

        function saveSomething() {
            // var _saveVar = [ "#KINBR", "#TRMSEQ", "#TXTNO", "#TLRNO",
            // "#SUPNO",
            // "#EMPNOT", "#EMPNOS" ];
            var _saveVar = ["#KINBR", "#TXTNO", "#TLRNO", "#EMPNOT", "#EMPNOS"];
            var obj = {};
            var currvalue = "";
            for (var i = 0; i < _saveVar.length; i++) {
                var varname = _saveVar[i];
                currvalue = "";
                try {
                    currvalue = self.getValue(varname);
                } catch (ee) {
                    currvalue = "";
                }
                obj[varname] = currvalue;
            }
            return obj;
        }

        function restoreSomething(obj) {
            for (var k in obj) {
                self.setValue(k, obj[k]);
            }
        }
    };

    function parseTim(list, text) {
        var fields = $.map(list, function (x) {
            if (x.charAt(0) === '#') return _self.getField(x);
            else return x;
        });
        _self.ifxHost.parseTim(fields, text);
    }

    var _tabKey = null;
    var _iframeId = null;
    var $tabWindow = null;
    var fnScrollTabWin = null;
    var fnGetScrollTop = null;
    Ifx.fn.getScrollTop = function () {
        return fnGetScrollTop();
    };
    // 柯 新增 start
    Ifx.fn.gettabs = function () {
        var fn = getTabFn('getTabslogoff');
        return fn();
    };
    Ifx.fn.closetab = function () {
        var fn = getTabFn('closeTab');
        fn(_tabKey, true);
    };
    // end
    Ifx.fn.registerTab = function () {
        console.log("registerTab!!!");
        var self = this;
        if (_layout == "1") {
            var fn = getTabFn('registerTran');
            var tid = this.getValue('__tid$');
            _tabKey = getTabFn('getTitleById')(tid);
            // _tabKey = this.getValue('__title$');
            _iframeId = fn(_tabKey, 'refresh', IfxUtl.bind(this, "refreshStatus"));
            fn(_tabKey, 'resize', resizeSelf); // call by eztab's windows
            // resize handler
            fn(_tabKey, 'scrollRollBack', IfxUtl.bind(this, "scrollRollBack"));
            fn(_tabKey, 'chainAutoSubmit', IfxUtl.bind(this, "chainAutoSubmit"));
            fn(_tabKey, 'xaction', IfxUtl.bind(this, "xaction"));
            fn(_tabKey, 'issaved', IfxUtl.bind(this, "isSaved"));
            fn(_tabKey, 'saveTran', IfxUtl.bind(this, "saveTran"));
            fn(_tabKey, 'clr', function () {
                window.location.href = window.location.href; // 柯: 原
                // location.reload();
                // 更改
            });
            fn(_tabKey, 'goCurrent', IfxUtl.bind(this, "goCurrentField"));
            // fn(_tabKey,'hasChain', IfxUtl.bind(this, "hasChain"));
            fn(_tabKey, 'waterMark', IfxUtl.bind(this, "waterMark"));
        } else {
            // two win, register onclick
            $('body').on("click", function (evt) {
                self.refreshStatus();
            });
        }
        fnGetScrollTop = getTabFn('getScrollTop');
        fnScrollTabWin = getTabFn('scrollTabWin');
        $tabWindow = getTabFn('getTabWindow$')();
        // fnScrollTabWin(0,'fast');
        // $(window).scrollTo({top:0}, 'fast');
    };
    var otherStatusField = {};
    Ifx.fn.setStatusField = function (key, value) {
        otherStatusField[key] = value;
    };
    Ifx.fn.refreshStatus = function () {
        // getTopFn('resetTranStatus')();
        if (this.getValue("__hot$") == "1") {
            getTopFn('resetTranStatus')();
            return;
        }
        var o = {
                'normal': '',
                'step': '',
                'ec': '',
                // 鍾 交易狀態增加一般時 normal [登錄]
                'chain': '',
                'other': otherStatusField
                // end
            },
            fn = getTopFn('setStatus');
        // o['txcode'] = "交易名稱－" + this.txcd + (this.auth.txnm ? "－" +
        // this.auth.txnm : "");
        o['txcode'] = this.txcd + (this.auth.txnm ? this.auth.txnm : "");
        var fkey = parseInt(this.getValue("FKEY$"), 10);
        var tt = getPromptByFkey(fkey);
        if (tt == null) tt = "登錄";
        if (fkey == 1) {
            o['ec'] = "[訂正]";
        } else {
            o['normal'] = "[" + tt + "]";
        }
        /*
		 * switch (fkey) { case "1": o['ec'] = "[更正]"; break; case "2":
		 * o['normal'] = "[放行]"; break; case "3": o['normal'] = "[審核/在途登錄]";
		 * break; case "5": o['normal'] = "[更正重登]"; break; case "6": o['normal'] =
		 * "[在途設定]"; break; case "7": o['normal'] = "[修改]"; break;
		 *
		 * default: o['normal'] = "[登錄]"; }
		 */
        // var chain = this.getValue('CHAIN$');
        // if(chain!=0) {
        // if(chain=='9')
        // o['chain'] = "[檢視]";
        // else
        // o['chain'] = "[連動]";
        // }
        fn(o);
        // if(this.lastFocus!=undefined) {
        // this.goCurrentField();
        // }
    };
    Ifx.fn.closeTabText = function (textdata, canclose) {
        _closetabText = textdata;
        _canClose = canclose;
    };
    Ifx.fn.nextNoClose = function (nextnoclose) {
        _nextNoclose = nextnoclose;
    };
    // 按TAB旁一樣都關閉,這個只給下方BUTTON使用
    Ifx.fn.KeysEscapeTran = function (bForce) {
        // TODO: if tran end, let it go without confirm
        try {
            if (_ajaxSender != null) {
                _ajaxSender.abort();
            }
        } catch (ee) {
        }
        if (_closetabText.length > 0) { // 提示
            alert(_closetabText.replace(/\\n/g, '\n')); // 端末會加入換行
        }
        if (_canClose) { // 可否close?
            // 查詢類可以直接離開
            if (_self.isUpdatedTran()) {
                bForce = false;
            } else {
                bForce = true;
            }
            closeMe(_layout, this.tranEnd || bForce);
        }
    };

    function closeMe(layout, tranEnd) {
        if (layout == "2") {
            if (window.confirm('確定要離開交易?')) {
                window.location = "menu.jsp";
            }
        } else {
            var fn = getTabFn('closeTab');
            fn(_tabKey, tranEnd, _self);
        }
    }

    function get2WinFn(name) {
        return parent.frames[0][name];
    }

    function getTabFn(name) {
        // return top.frames['easytab'][name];
        return window.parent[name];
    }

    function getTopFn(name) {
        // return top.frames['head'][name];
        return top.frames[0][name];
    }

    // end of ifx key handler
    // -------------------------------------------------------------
    // field manipulation
    Ifx.fn.dumpVar = function (i) {
        if (i == 1) {
            console.log('sysvar dump!!');
            for (k in this.sysvar) console.log(k + ":[" + this.sysvar[k] + "]");
        } else if (i == 3) {
            console.log('getCurrentField dump!!');
            if (!this.isSwiftMode()) {
                var fld = this.getCurrentField();
                fld.dump();
            }
        } else {
            console.log('sys fields:');
            $.each(this.sysFields, function (i, x) {
                if (x.getValue) console.log(x.name + ":[" + x.getValue() + "]");
            });
            console.log('end of sys fields:');
            console.log('dc fields');
            $.each(this.dcFields, function (i, x) {
                if (x.getValue) console.log(x.name + ":[" + x.getValue() + "]");
            });
            console.log('end of dc fields');
        }
    };
    Ifx.fn.getSysvar = function (k) {
        // hasOwnProperty(k), k in sysvar
        if (k in this.sysvar) {
            return this.sysvar[k];
        }
        throw "getSysvar() find no sysvar:" + k;
    };
    Ifx.fn.setSysvar = function (k, v) {
        if (k in this.sysvar) {
            console.log("set sysvar " + k + " to " + v);
            return this.sysvar[k] = v;
        }
        throw "setSysvar() find no sysvar:" + k;
    };
    Ifx.fn.getSysvarMap = function () {
        return this.sysvar;
    };
    Ifx.fn.isSysvar = function (n) {
        return (typeof (n) === "string" && n.charAt(n.length - 1) === "$");
    };
    Ifx.fn.copyTo = function (sourceFld, targetFld) {
        this.setValue(targetFld, this.getValue(sourceFld));
        console.log("copyTo() from:" + sourceFld + " value:" + this.getValue(sourceFld) + " to target:" + targetFld + ", value:" + this.getValue(targetFld));
    };
    // 柯 測試 設定超時 start
    Ifx.fn.timeoutt = function (isout, gotarget) {
        if (isout != null) {
            this.istimeout = isout;
            if (gotarget) {
                var bb = _self.jumptoBack2(isout);
            }
        } else {
            return this.istimeout;
        }
    };
    Ifx.fn.jumptoBack2 = function (dcIndex, always, backNext) { // always for xaction
        console.log("======== jumptoBack2?");
        if (_self.currentIndex < 0) return;
        if (!dcIndex) { // not my field
            console.log("not my field");
            _self.goCurrentField();
            return true;
        }
        dcIndex = parseInt(dcIndex, 10);
        if (dcIndex != _self.currentIndex || always) {
            console.log("clicking field Index:" + dcIndex);
            console.log("currentIndex:" + _self.currentIndex);
            if (dcIndex <= _self.currentIndex || backNext) { // 改<= for xaction 同欄位回傳時
                console.log('back to index:' + dcIndex);
                _self.nextField(dcIndex, true);
            } else {
                if (always) {
                    console.log('xaction back to currentIndex:' + _self.currentIndex);
                    _self.nextField(_self.currentIndex, true);
                }
            }
        } else {
            console.log("same field");
        }
    };
    // 柯 測試 設定超時 end
    Ifx.fn.getValue = function (n) {
        var v;
        if (this.isSysvar(n)) {
            if (n == "TIME$") {
                v = IfxUtl.getTimeString().slice(0, 6);
                return v;
            } else if (n == "MQSERVERPORT$") {
                v = _mqserverport;
                return v;
            }
            var v = this.getSysvar(n.slice(0, -1));
            return v;
        }
        if (n.indexOf('.') == -1) {
            var f = this.getField(n);
            return f.getValue();
        } else {
            var nn = n.split('.');
            v = this.getField(nn[0]).getValue();
            v = String.prototype[nn[1]].call(v);
            return v;
        }
    };
    // 誰會呼叫ifx.toHost()??
    // 鍾 上傳中心資料要有補空白或0
    Ifx.fn.toHost = function (n) {
        var v;
        if (this.isSysvar(n)) {
            if (n == "TIME$") {
                v = IfxUtl.getTimeString().slice(0, 6);
                return v;
            }
            var v = this.getSysvar(n.slice(0, -1));
            return v;
        }
        if (n.indexOf('.') == -1) {
            var f = this.getField(n);
            return f.toHost();
        } else {
            var nn = n.split('.');
            v = this.getField(nn[0]).toHost();
            v = String.prototype[nn[1]].call(v);
            return v;
        }
    };
    // end
    Ifx.fn.setValue = function (n, v) {
        if (this.isSysvar(n)) {
            this.setSysvar(n.slice(0, -1), v);
            return;
        }
        var f = this.getField(n);
        f.setValue(v);
    };
    Ifx.fn.setEscRim = function (v) {
        _self.escRim = v;
        var fn = getTabFn('setIfxSelf');
        fn(_self);
    };
    Ifx.fn.getField = function (n, noAlert) {
        var o = null;
        // get by index
        if (typeof (n) === "number") {
            return this.getFieldsByRtnType(this.step)[n];
            // o= this.def.fields[n];
        } else {
            if (n.indexOf('.') != -1) n = n.split('.')[0];
            o = this.fieldMap[n];
        }
        if (o == null) {
            o = this.fieldMap["#" + n.slice(1)]; // 嚙磐嚙褒進嚙踝蕭@XXX,嚙篁嚙踝為#nnn
            if (o == null) {
                o = getRefField(n);
                if (o == null) {
                    var s = 'Error!! no such field:' + n;
                    if (!(noAlert === true)) {
                        alert(s);
                        throw s; // 改位置
                    }
                }
            }
        }
        return o;

        function getRefField(x) {
            var r = /_@@_reffield_@@_/;
            x = x.replace(r, '');
            return _self.fieldMap[x];
        }
    };
    Ifx.fn.hasField = function (n) {
        try {
            var f = this.getField(n, true); // true--> no alert()
            return f != null;
        } catch (ee) {
        }
        return false;
    };
    Ifx.fn.getCurrentField = function () {
        console.log("getCurrentField1.");
        if (this.currentIndex < 0 || this.currentIndex >= this.ynIndex) {
            throw "not in dc area";
        }
        return this.dcFields[this.currentIndex];
    };
    Ifx.fn.getnotinField = function () {
        if (this.currentIndex < 0 || this.currentIndex >= this.ynIndex) {
            return true;
        }
        return false;
    };
    Ifx.fn.getTrigger = function (n) {
        return this.triggerFieldMap[n];
    };
    // end of field manipulation
    // ----------------------------------------------------------------
    // field navigation
    // ----------------------------------------------------------
    Ifx.fn.addKeyFilter = function () {
        var self = this;
        // TODO: refactor-->use jquery delegate()
        $.each(this.dcFields, function (i, x) {
            if (x.addKeyFilter && x.isEntryField()) {
                x.addKeyFilter(autoSkip);
                x.addInputFormat();
            }
        });
        textAreaControl();

        function autoSkip(targetId) { // 小柯 增加傳入參數 內容邏輯增加判斷
            if (isSameTarget()) {
                console.log('calling autoSkip');
                self.KeyForward('autoSkip');
            }

            function isSameTarget() {
                var fld = self.getCurrentField();
                if (fld == null) return false;
                return (fld.id() == targetId);
            }
        }

        // For IE Windows it seems to work to call
        // document.execCommand('OverWrite', false, true)
        // to enable overwrite mode for text controls on the page and
        // document.execCommand('OverWrite', false, false)
        // to switch it off.
    };

    function textAreaControl() {
        $("textarea[maxlength]").keypress(function (event) {
            var key = event.which;
            // all keys including return.
            if (key >= 33 || key == 13) {
                var maxLength = $(this).attr("maxlength");
                var length = this.value.length;
                if (length >= maxLength) {
                    event.preventDefault();
                }
            }
        });
    }

    Ifx.fn.collect = function (bGoFirstField, journal) { // 柯 測試 journal FOR
        // journal
        // journal
        bGoFirstField = (bGoFirstField === undefined) ? true : bGoFirstField;
        this.clearLastField();
        if (!journal) this.runSysRtn(true);
        // TODO execute preDC
        runPreDC();
        this.step = "dc";
        this.currentIndex = -1;
        this.ynIndex = this.dcFields.length - 1; // point to last field;
        if (bGoFirstField) // 柯 測試 journal FOR journal
            this.goField(0);
        this.saved = true;

        function addECButtons() {
            // remove function body 2013/11/08
            // useless function
        }
    };
    var $nonDisabledFlds = null;
    Ifx.fn.disableCollect = function () {
        console.log("disable every fields");
        // $("[id^='fld_']").attr("disabled", "true");
        $nonDisabledFlds = $('input:not(:disabled)');
        $(".field_input").attr("disabled", "true");
        // 柯 加入此段試試看...
        $(_self.panel.entry).undelegate(".field_input", "mousedown paste ");
        var btn = $("[id^='btn_new']");
        var ishiddden = [];
        for (var i = 0; i < btn.length; i++) ishiddden.push($('#' + btn[i].id).is(':hidden'));
        $("[id^='btn_']").hide(); // submit
        for (var i = 0; i < btn.length; i++)
            if (!ishiddden[i]) $('#' + btn[i].id).show();
        // 柯 新增更新類送出後無法儲存 所以乾脆關閉
        if (this.isUpdatedTran()) {
            $('#xbtn_save').hide(); // save hide
        }
        // if(_self.fkey == 0)
        if (!this.countDownTran) { // 倒數計時交易 不能"再次執行"
            // 柯 新增fkey其餘的皆不需要顯示 [重新交易]
            if (this.getValue("FKEY$") && parseInt(this.getValue("FKEY$"), 10) != 0) {
                $('#xbtn_again').hide(); // again
            } else {
                // 潘 更新類統一取消重新交易
                if (this.getValue("AGAIN$") != "1") $('#xbtn_again').show(); // again
            }
        }
        // $(this.panel.entry).off("focus click");
    };
    Ifx.fn.undoDisableCollect = function () {
        console.log("play again");
        this.tranEnd = false;
        // $("[id^='fld_']").attr("disabled", "true");
        $nonDisabledFlds.removeAttr("disabled");
        $("[id^='btn_']").show(); // submit
        $('#xbtn_canel').show(); // cancel
        $('#xbtn_again').hide(); // again
        $(this.panel.query).slideUp();
        $(this.panel.query2).slideUp();
        $(this.panel.output).slideUp().html('');
        this.keys.bind(true);
        this.collect();
    };
    Ifx.fn.runSysRtn = function (bRunAll) {
        console.log("= = = = begin run sys, all sys fields ? " + bRunAll);
        var self = this;
        $.each(this.sysFields, function (i, x) {
            try {
                if (bRunAll === true) {
                    x.pref(self.rtn);
                } else {
                    if (x.rtnName === '_sys') {
                        x.pref(self.rtn);
                    } else {
                        console.log('* * skip this field:' + x.name);
                    }
                }
            } catch (ee) {
                if (ee instanceof IfxError) {
                    if (ee.isFavlError()) {
                        alert(ee.message);
                    }
                } else {
                    alert(ee);
                    throw ee;
                }
            }
            console.log(x.name + "=" + x.getValue());
        });
        console.log("= = = =  end run sys");
    };
    Ifx.fn.runAfterYN = function () {
        this.step = "rtn";
        var formName = this.getValue("TXFORM$");
        console.log("\n\n----- runAfterYN for FORM:" + formName + "  ------\n\n");
        var fld;
        for (var i = 0; i < this.xmtedFields.length; i++) {
            fld = this.xmtedFields[i];
            console.log("\n\n\nprocess " + i + ", name:" + fld.name + ", rtn type:" + fld.rtnType + ", rtnName:" + fld.rtnName);
            if (fld.rtnType.toUpperCase() == "FORM") {
                if (fld.rtnName != formName) {
                    console.log("skip " + i + ", name:" + fld.name + ", rtn type:" + fld.rtnType + ", form:" + fld.rtnName);
                    continue;
                } else {
                    console.log("yes process " + formName + "'s field:" + fld.name);
                }
            }
            this.executePrefRtn(fld);
            // this.getField(i).pref(this.rtn);
        }
        console.log("\n\n\n------end of run after YN-------------------\n\n");
    };

    function runPreDC() {
        console.log("= = = =  begin of runPreDC");
        $.each(_self.preDC_Fields, function (i, x) {
            try {
                x.pref(_self.rtn);
            } catch (e) {
                if (e instanceof IfxError && e.isSkip()) {
                    console.log("skip " + x.name + ", go next field");
                } else {
                    alert(e);
                    throw e;
                }
            }
        });
        console.log("= = = =  end of runPreDC");
        // if(_self.txcd=="G2100")
        // _self.getResv("20131029","5050","20","00001693", function() {
        // alert("done");});
        // alert("done");});
    };

    function setAllRun(bRun) {
        bRun = (bRun === undefined) ? true : bRun;
        var fld;
        var actfg = _self.getValue("#ACTFG"); //n鍾
        console.log("set all Run? " + bRun);
        $.each(_self.dcFields, function (i, x) {
            if (x.name[0] == "#") { // filter btn_yn
                //fld = _self.getField(x.name);
                //fld.setRunnable(bRun);
                fld = _self.getField(i);

                if (fld) {
                    if ((actfg == "0" || actfg == "2" || actfg == "4" || actfg == "6") && fld.isH())
                        fld.setRunnable(true);
                    else
                        fld.setRunnable(bRun);
                } else
                    console.log("fld undefind!");

            }
        });
    }

    // http://localhost:8080/ifxweb2/mvc/hnd/jnl/resv/20131029/5050/20/00001693
    Ifx.fn.getResv = function (busdate, brn, tlrno, txno, fnCallbackTita, fnCallbackResvDone) {
        console.log("getResv-getResvFields");
        _self.block('<h2>讀取RESV...<h2>');
        var resvNames = getResvFields();
        // if(!_self.def.resv){
        if (!resvNames) {
            _self.unblock();
            setTimeout(function () {
                fnCallbackResvDone(); //
            }, 0);
            return;
        }
        // 柯 經過 LCR01調回時給值
        if (_self.xxrjnlid != "") {
            console.log("new journal txno:" + _self.xxrjnlid);
            brn = _self.xxrjnlid.substr(0, 4); // 取 BRNO 4位
            tlrno = _self.xxrjnlid.substr(4, 6); // 取 TLRNO 6位
            txno = _self.xxrjnlid.substr(10, 8); // 取 TXNO 8位
        }
        // TODO change to call sendBean
        var url = _contextPath + '/mvc/hnd/jnl/resv/' + busdate + "/" + brn + "/" + tlrno + "/" + txno;
        console.log("getResv url:" + url);
        var poster = $.post(url);
        poster.done(function (data) {
            if (data.retval) {
                var disnone = data.text.split("!!!");
                data.text = disnone[0];
                disnone.shift();
                disnone = disnone.join("").split(",");
                var cacheindex = "]]]";
                var datatext = data.text.split(cacheindex);
                var trueresv;
                if (data.text.slice(0, cacheindex.length) == "[[[") {
                    if (datatext.length > 1) {
                        var cache = datatext.shift().slice(cacheindex.length);
                        console.log("cache:" + cache);
                        if (cache.length > 0 && (_self.fkey == 3 || _self.fkey == 7)) { // 修改
                            // 審核
                            _self.rtn.SETRIMCACHED(cache);
                        }
                    }
                    trueresv = datatext.join("");
                } else {
                    trueresv = data.text;
                }
                console.log("trueresv:" + trueresv);
                parseTim(resvNames, trueresv);
                fnCallbackTita();
                //var a =
                $(function () {
                    disnone.forEach(function (x) {
                        if (x.trim().length > 0)
                            if ($(x).val() == "退回") ;
                            else $(x).hide();
                    });
                })
                //setTimeout(a, 200);
                if (fnCallbackResvDone) {
                    setTimeout(fnCallbackResvDone, 0);
                }
            } else {
                console.log(data.text);
                // alert(data.text); // show errmsg
                // 柯 如果錯誤還是繼續做 START
                fnCallbackTita();
                if (fnCallbackResvDone) {
                    setTimeout(fnCallbackResvDone, 0);
                }
                // END
            }
        });
        poster.fail(function (err) {
            // console.log(err.responseText || "getResv error");
            alert(err.responseText || "getResv error");
        });
        poster.always(function () {
            console.log("getResv always done");
            _self.unblock();
        });
    };
    Ifx.fn.viewDupdoc = function (fakejnlId) {
        $("#xbtn_print").off('click').show();
        // TODO 存款需主管授權
        if (ifSupperNeed()) {
            $("#xbtn_print").click(function () {
                var m = ["0000 存款交易重印,主管應注意存取條/存單重印內部控制."];
                console.log("supervisor override");
                var dfd = // 小柯 增 測試 視窗改
                    $.dialog2("主管授權", '需主管授權 :<br/>' + m.join("<br/>"), // 柯
                        // \n改<br/>
                        "遠端授權", "本機授權");
                dfd.done(function () { // 按下Yes時
                    getRemoteOvr(m);
                }).fail(function () { // 按下No時
                    getLocalSupervisor(m);
                });
                // 柯:重要~複寫 transmit!
                _self.transmit = function () {
                    dupdoc_init(fakejnlId, true, true); // 存款只能執行一次
                }
            });
        } else {
            $("#xbtn_print").click(function () {
                dupdoc_init(fakejnlId, true);
            }); // DDCHECK
        }
    };
    // 交易明細&整批列印之列印單據使用
    // 參數:RIM_NAME,顯示名稱,TITA_TEXT
    Ifx.fn.lastDataSend = function (datamsgid, btnshowname, dataTita, trigger, settxtno) {
        // dataTita 還沒確定是要甚麼tita & 交易?
        console.log("go last Data Send!");
        if ($("#xbtn_timeoutp").length > 0) {
            return;
        }
        $('#btn_yn').off("click mousedown keypress").hide();
        $("#xbtn_cancel").clone(true).attr("id", "xbtn_timeoutp").attr("name", "xbtn_timeoutp").attr("value", btnshowname).off('click').click(function () {
            // _self.eachSend = true; //直接印
            // if(_self.ifxHost.getsendBefore()){
            // console.log("already send!!");
            // return;
            // }
            // alert(btnshowname+",Msgid:"+datamsgid+",Tita:"+dataTita);
            /* 潘 新增單據重送XS XF需刷主管卡 並添加fkey==8只針對單據重送 */
            if (ifSupperNeed() && _self.fkey == 8) {
                var m = ["0000 存款交易重印,主管應注意存取條/存單重印內部控制."];
                console.log("supervisor override");
                var dfd = // 小柯 增 測試 視窗改
                    $.dialog2("主管授權", '需主管授權 :<br/>' + m.join("<br/>"), // 柯
                        // \n改<br/>
                        "遠端授權", "本機授權");
                dfd.done(function () { // 按下Yes時
                    getRemoteOvr(m);
                }).fail(function () { // 按下No時
                    getLocalSupervisor(m);
                });
                _self.transmit = function () {
                    resetDocList();
                    _self.ifxHost.setsendBefore(true);
                    _self.ifxHost.initSend();
                    // _self.setValue("#HCODE", "0");//for 有些因為這個欄位不會重印的問題
                    // 有要加這個?
                    // dataTita = "14000010201505120000800$"; //TODO 只是暫時測試
                    _ajaxSender = _self.ifxHost.send(datamsgid, true, dataTita, "receiveMain", _self, null, null, false, null, settxtno); // 柯 新增
                    // mq
                }
            } else {
                resetDocList();
                _self.ifxHost.setsendBefore(true);
                _self.ifxHost.initSend();
                // _self.setValue("#HCODE", "0");//for 有些因為這個欄位不會重印的問題 有要加這個?
                // dataTita = "14000010201505120000800$"; //TODO 只是暫時測試
                _ajaxSender = _self.ifxHost.send(datamsgid, true, dataTita, "receiveMain", _self, null, null, false, null, settxtno); // 柯 新增
                // mq
            }
            ;
        }).appendTo($("#xbtn_cancel").parent()).hide();
        // 自動執行
        if (trigger) {
            setTimeout(function () {
                $('#xbtn_timeoutp').trigger('click');
            }, 100);
        } else {
            $('#xbtn_timeoutp').show();
        }
    };
    // 沒有要提供了
    Ifx.fn.viewJournal = function (jnlId, resend) {
        var bean = 'url:/mvc/hnd/jnl/tita/' + jnlId;
        $(".field_input").attr("disabled", "true");
        $("[id^='btn_']").hide(); // submit
        // reset ntxcd
        _self.setValue('NTXCD$', '0');
        setAllRun(false);
        if (jnlId) { // 柯: 新增此判斷 可能可以在普通交易的BTN進入 有可能為空
            _self.sendBean(bean, {}, null, function (data) {
                if (data.jnlId)
                    jnlId = data.jnlId;
                parseTim(_self.def.tim.list, data.tita);
                console.log("sendBean-getResvFields");
                console.log("viewJournal-jnlId" + jnlId);
                var resvNames = getResvFields();
                var disnone = data.resv.split("!!!");
                data.resv = disnone[0];
                disnone.shift();
                disnone = disnone.join("").split(",");
                var cacheindex = "]]]";
                var datatext = data.resv.split(cacheindex);
                var trueresv;
                if (data.resv.slice(0, cacheindex.length) == "[[[") {
                    if (datatext.length > 1) datatext.shift();
                    trueresv = datatext.join("");
                } else {
                    trueresv = data.resv;
                }
                parseTim(resvNames, trueresv);
                $("#xbtn_print").off('click').show();
                if (ifSupperNeed()) {
                    $("#xbtn_print").click(function () {
                        var m = ["0000 存款交易重印,主管應注意存取條/存單重印內部控制."];
                        console.log("supervisor override");
                        var dfd = // 小柯 增 測試 視窗改
                            $.dialog2("主管授權", '需主管授權 :<br/>' + m.join("<br/>"), // 柯
                                // \n改<br/>
                                "遠端授權", "本機授權");
                        dfd.done(function () { // 按下Yes時
                            getRemoteOvr(m);
                        }).fail(function () { // 按下No時
                            getLocalSupervisor(m);
                        });
                        // 柯:重要~複寫 transmit!
                        _self.transmit = function () {
                            dupdoc_init(jnlId, false, true); // 存款只能執行一次
                        }
                    });
                } else {
                    $("#xbtn_print").click(function () {
                        dupdoc_init(jnlId);
                    }); // DDCHECK
                }
                // 柯 for journal fkey 檢視 9 predc start
                setFkeyandCollect();
                // end
                disnone.forEach(function (x) {
                    if (x.trim().length > 0)
                        if (_self.fkey == 9 && x == "#xbtn_print") ;
                        else $(x).hide();
                });
                dupdoc_init(jnlId);
            });
        } else {
            console.log("viewJournal-jnlId = null");
        }
        // TODO 有timeout功能後,此功能是否還有需要?
        if (resend) {
            // 不能查詢類
            $('#btn_yn').show(); // submit
            // $('#btn_yn').attr('disabled', false).attr("value", "還原送出");
            $('#btn_yn').attr("value", "還原送出");
            // 柯: YN click 改 mousedown for ie10 BUG 之 統一
            $('#btn_yn').off('click mousedown keypress').on('mousedown', function () {
                console.log("click yn on FKEY =9 檢視");
                // var atext = "10580500000000110000 0000000000 XS930 86995619
                // 0000000000000000000105805
                // 000000000000000000000110581058105810 0000 11 0";
                var titatext = "11    0";
                // 如何知道當下是要印還是不要印? 在printwhat中 判斷 fkey 9? 不管他 全印
                _self.setValue("#KINBR", _self.getValue("BRN$"));
                _self.setValue("#TLRNO", _self.getValue("TLRNO$"));
                // _self.setValue("#RPTFG", "1");
                // _self.setValue("INQHD$", 72);
                // _self.setValue("INQLEN$", 51);
                // _self.setValue("LOOPHEIGHT$", 2);
                // _self.setValue("INQPRT$", 40);
                _self.setValue("#ENTDY", _self.getValue("ENTDY$"));
                titatext = _self.ifxHost.buildTitaLabelForRim("XS930") + titatext; // build
                // 頭 +
                // 欄位
                _self.ifxHost.initSend();
                // 傳入交易代號?? 序號??
                _ajaxSender = _self.ifxHost.send("XS930", false, titatext, "receiveMain", _self, null, null, false);
                $('#btn_yn').hide();
            });
        }
    };

    // id/不同功能/執行一次
    function dupdoc_init(jnlId, fake, once) {
        var bean;
        if (fake) {
            // String busdate, String brn, String tlrno, String txno
            bean = 'url:/mvc/hnd/doc/listfake/' + jnlId; // findByF4
        } else {
            bean = 'url:/mvc/hnd/doc/list/' + jnlId;
        }
        resetDocList();
        _self.sendBean(bean, {}, null, function (data) {
            dupdoc_getDocs(jnlId, data);
        });
        if (once) {
            console.log("隱藏 重印單據按鈕.");
            $("#xbtn_print").off('click').hide();
        }
    }

    function dupdoc_getDocs(jnlId, data) {
        console.log(data);
        if (!data.list || data.list.length == 0) {
            alert("沒有單據,JNLID:" + jnlId);
            return;
        }
        async.mapSeries(data.list, dupdoc_getOne, function (err, results) {
            if (err) {
                alert(err);
            } else {
                console.log(results);
                // append results to outputarea
                _self.displayOutput(_self.panel.output, results, true);
            }
        });
    }

    function dupdoc_getOne(oDocHeader, callback) {
        var bean = 'url:/mvc/hnd/doc/get/' + oDocHeader.docId; // 重印單據不主動印，故移除重印單據時
        // 後面加的 +
        // "?_addLog=1"
        _self.sendBean(bean, {}, null, function (data) {
            console.log(data);
            callback(null, data.doc);
        }, function (errData) {
            callback(errData.errmsg);
        });
    }

    Ifx.fn.goField = function (i) {
        if (i < this.currentIndex) { // navigation up
            this.previousField(i);
        } else if (i > this.currentIndex) {
            this.nextField(i);
        }
    };
    Ifx.fn.getCurrentField = function () {
        console.log("getCurrentField2.");
        return this.getField(this.currentIndex);
    };
    Ifx.fn.goCurrentField = function (mode) {
        // this.ui().off('keyup keydown keypress');
        console.log("step:" + this.step);
        console.log("current:" + this.currentIndex);
        console.log("last focus:" + this.lastFocus);
        if ($('#xbtn_print').css("display") != "none") {
            console.log("in 重印單據 return");
            return;
        }
        if (this.step != "dc") return;
        if (this.isSwiftMode()) {
            goSwiftForm('current', mode);
            return;
        }
        if (this.lastFocus == undefined) {
            alert("程式有誤 無法繼續(no focus)");
            closeMe(_layout, this.txcd);
            return;
        }
        this.currentIndex = this.lastFocus;
        if (this.currentIndex == this.ynIndex) {
            setTimeout(function () {
                _self.scrollFieldInToView($("#btn_yn").css("display") == "none" ? $("#xbtn_cancel") : $("#btn_yn"), function () {
                    console.log("I am focus btn_yn");
                    _self.clearLastField();
                    if ($("#btn_yn").css("display") == "none") $("#xbtn_cancel").focus();
                    else $("#btn_yn").focus();
                    // 潘 fky9 自動送出
                    if (_self.fkey == 9 && _chain == 3) $('#btn_yn').trigger('mousedown');
                });
            }, 0);
            return;
        }
        var f = this.getField(this.currentIndex);
        // var $f =$('#' + IfxUtl.name2id(f.name));
        var $f = f.ui();
        if ($f.length == 0) {
            if (f.name != "#_SWIFTFORM_") { // swift不知可否暫存,目前測試先擋住此欄位..
                alert("FATAL, programmer's bug\n" + f.name + " is not an UI field, please check field type");
            }
            return;
        }
        // this.scrollFieldInToView($f, setFocus);
        // 小柯 暫時修改成這樣
        var currIndex = this.currentIndex;
        // setTimeout(function() {
        _self.scrollFieldInToView($f, setFocus2);

        function setFocus2() { // 柯 拿掉裡面timeout
            console.log('currIndex:' + currIndex);
            console.log('self.currentIndex:' + _self.currentIndex);
            if (currIndex != _self.currentIndex) return; // 已經tab至其他欄位
            if ($f.datepicker) $f.datepicker('enable');
            $f.select();
            $f.focus();
            //_self.help.repositionHelp(); // 避免一進入有help時，畫面異常
            console.log('core focus');
            console.log('core select');
        }

        // }, 0); //原先100改20後 從SIWFT 快速按到VAR 比較不會有暫存的SELECT問題
        // function setFocus() {
        // setTimeout(function() {
        //
        // f.focus();
        // console.log('core focus');
        // // since the focus event is generally too early in WebKit:
        // window.setTimeout(function() {
        // try {
        // $f.select();
        // // this.ui().s('keyup keydown keypress');
        // console.log('core select');
        // } catch (ee) {
        // }
        // }, 0);
        //
        // }, 0);
        // }
    };
    var $scrollTargetPane = $('html, body');
    var isLayout = false;
    var myLayout = null;
    Ifx.fn.setScrollTarget = function ($jqObj, aLayout) {
        $scrollTargetPane = $jqObj;
        isLayout = true;
        myLayout = aLayout;
    };
    Ifx.fn.getScrollTop = function () {
        return $scrollTargetPane.scrollTop();
    };

    function resizeLayout() {
        if (myLayout) {
            // myLayout.resetOverflow("center");
        }
    }

    Ifx.fn.scrollFieldInToView = function ($fld, callback) {
        console.log('in the scrollFieldInToView');
        var padding, tmpspace, lbound, ubound, scrollTop, swiftTop, fldHeight, tabHeight, fldTop; // Desired
        // page
        // "padding"
        f1(callscroll);

        function f1(callscroll) {
            setTimeout(function () {
                // return;
                // return;
                fldTop = parseInt($fld.offset().top, 10);
                // var fld = IfxUtl.name2id(name);
                swiftTop = $('#swiftPanel').is(':visible') ? parseInt($('#swiftPanel').offset().top, 10) - 200 : 0;
                console.log('swiftTop:' + swiftTop);
                fldHeight = $fld.height();
                scrollTop = _self.getScrollTop(); // parseInt(fnGetScrollTop(),10);
                tabHeight = parseInt($tabWindow.height(), 10);
                console.log('scrollFieldInToView, fld top:' + fldTop + ", scrollTop:" + scrollTop);
                console.log('fld height:' + fldHeight);
                console.log('tab height:' + tabHeight);
                // var theFrame = $('#' + _iframeId, parent.document.body);
                // var frameHeight = theFrame.height();
                // console.log('iframe height:' + frameHeight);
                // var bodyHeight = $scrollTargetPane.height();
                // console.log('body height:' + bodyHeight);
                // var scrollHeight = $scrollTargetPane.prop('scrollHeight');
                // console.log('scrollHeight:' + scrollHeight);
                //
                // var ONELINE = bodyHeight / 4;
                // var center = (2 * bodyHeight / 3);
                // var newScrollTop = scrollTop;
                padding = 200; // Desired page "padding"
                tmpspace = 100;
                lbound = fldTop - tabHeight + padding;
                ubound = fldTop - padding + tmpspace;
                console.log('lbound:' + lbound);
                console.log('ubound:' + ubound);
                callscroll();
            }, 10);
        }

        function callscroll() {
            if (ubound < 0) {
                console.log('ubound 小於 0 不調整位置');
            } else if (scrollTop < lbound) {
                $scrollTargetPane.scrollTop(lbound + tmpspace);
            } else if (scrollTop > ubound - swiftTop) {
                $scrollTargetPane.scrollTop(ubound - tmpspace - swiftTop);
            }
            if (callback) callback();
        }

        // if (fldTop > bodyHeight) { // 超出畫面可視範圍
        // newScrollTop += (fldTop - ONELINE);
        //
        // console.log('==>scrolling down #1 to ' + newScrollTop);
        //
        // } else if (fldTop > center) { // 在畫面下半
        // newScrollTop += (fldTop - ONELINE);
        //
        // console.log('==>scrolling down #2 to ' + newScrollTop);
        //
        // } else if (fldTop < 50) {
        // newScrollTop = scrollTop + fldTop - (ONELINE);
        // console.log('==>scrolling up #1 to ' + newScrollTop);
        // }
        // if (newScrollTop == scrollTop) {
        // console.log("no scroll");
        // if (callback)
        // callback();
        // } else {
        // newScrollTop = Math.max(0, newScrollTop);
        // newScrollTop = Math.min(scrollHeight, newScrollTop);
        // console.log("=====>min max scrollTop" + newScrollTop);
        //
        // // $scrollTargetPane.scrollTop(newScrollTop);
        // $scrollTargetPane.animate({
        // scrollTop : newScrollTop
        // }, 'fast', function() {
        // if (callback)
        // callback();
        // fldTop = parseInt($fld.offset().top, 10);
        // console.log("now fld top:" + fldTop);
        // console.log("now scrollTop:" + $scrollTargetPane.scrollTop());
        // });
        //
        // }
        //
        return;
        //
        // // $scrollTargetPane.animate({
        // // scrollTop : newScrollTop
        // // }, 10,function(){
        // // fldTop = parseInt($fld.offset().top, 10);
        // // console.log("now fld top:"+ fldTop);
        // // console.log("now scrollTop:"+ $scrollTargetPane.scrollTop());
        // // });
        //
        // if (fldTop - scrollTop < 30) {
        // // window.scrollBy(0,-40);
        // console.log("==^^^^^^^scroll up");
        // $scrollTargetPane.animate({
        // scrollTop : fldTop - 30
        // }, 100, function() {
        // fldTop = parseInt($fld.offset().top, 10);
        // console.log("now fld top:" + fldTop);
        // });
        //
        // // fnScrollTabWin(fldTop - 30,'fast');
        //
        // }
        //
        // if (fldTop - scrollTop > height * 0.7) {
        //
        // // var s2 = fldTop - (height * 0.2);
        // s2 = fldTop - 100;
        // console.log("===VVVV scroll down:" + s2);
        // $scrollTargetPane.animate({
        // scrollTop : s2
        // }, 100);
        //
        // // fnScrollTabWin( s2,'fast');
        // }
        // console.log('leave scrollFieldToView');
    };
    Ifx.fn.previousField = function (i) {
        if (i >= 0 && i < this.currentIndex) {
            this.clearLastField();
            this.currentIndex = i;
            this.goCurrentField();
        }
    };
    Ifx.fn.clearLastField = function () {
        console.log('clear last field');
        this.help.hide();
        this.errmsg();
        try { // 因為 btn_yn 沒有isUI
            // if (this.getCurrentField() != null &&
            // this.getCurrentField().isUI()){
            // $('.myFocusHint').removeClass('myFocusHint');
            // $('.myFocusRowHint').removeClass('myFocusRowHint');
            // }
            $('.myFocusHint').removeClass('myFocusHint');
            $('.myFocusRowHint').removeClass('myFocusRowHint');
            console.log('remove focus hint');
        } catch (ee) {
        }
    };
    Ifx.fn.nextField = function (i, ignoreCurrent, bJump, jumpToIndex) {
        try {
            this.__nextField(i, ignoreCurrent, bJump, jumpToIndex);
        } catch (ex) {
            var t = printStackTrace({
                e: ex
            });
            console.error(t);
            alert("FATAL(programmer's bug:nextField())!!\n\n" + t);
        }
    };
    Ifx.fn.__nextField = function (i, ignoreCurrent, bJump, jumpToIndex) {
        var self = this,
            transmitOnce = false;
        this.saved = false;
        console.log("__nextField:" + i + "," + ignoreCurrent + "," + bJump + "," + jumpToIndex);
        var currentField = null;
        // ignoreCurrent:true - no validating current, false - validate current
        // field
        if (this.currentIndex >= 0 && i > this.currentIndex) {
            currentField = this.getCurrentField();
            if (!currentField) return;
            console.log("validate field:" + currentField.name);
            if (!this.executePostRtn(currentField) && !ignoreCurrent) {
                console.log("#1 field validate fail");
                currentField.dump();
                _self.isScAll = false;
                return;
            }
            // currentField.setDisplayValue();
        }
        if (currentField) currentField.dump();
        // no more hMode and fkey
        /*
		 * var hMode = false; // always false now var fkey =
		 * this.getValue("FKEY$"); if (fkey > 0) hMode = true;
		 */
        for (; i < this.ynIndex; i++) {
            console.log("i:" + i + ", yn:" + this.ynIndex);
            // 鍾 欄位隱藏,不須輸入,可是還是要執行程式,不能跳過
            if (!this.dcFields[i].isTabbable()) {
                // end
                console.log(this.dcFields[i].name + " is not tabbable, skip it");
                continue;
            }
            this.clearLastField();
            this.currentIndex = i;
            if (!this.dcFields[i].isRunnable()) {
                console.log("** NOT RUNNABLE ** " + this.dcFields[i].name + " is not runnable, skip it");
                continue;
            }
            // currentField = this.getField(i);
            currentField = this.dcFields[i];
            currentField.skipped = false;
            /*
			 * if (hMode) { if (!currentField.isH()) { console.log("HMODE do't
			 * enter this field:" + currentField.name + ", currentIndex:" +
			 * this.currentIndex); continue; } else { console.log("HMODE enter
			 * this field:" + currentField.name + ", currentIndex:" +
			 * this.currentIndex); } }
			 */
            console.log("enter this field:" + currentField.name + ", field index:" + currentField.index + ", currentIndex:" + this.currentIndex);
            // enter swift form
            if (this.isSwiftMode()) {
                console.log("\in swift form\n");
                if (bJump) {
                    goSwiftForm('forward', 'jump', jumpToIndex > self.swiftIndex ? continueNextField : undefined);

                    function continueNextField() {
                        self.nextField(self.swiftIndex + 1, false, true, jumpToIndex); // continue
                        // after
                        // swift
                        // form
                        // field
                    }
                } else {
                    goSwiftForm('forward');
                }
                return;
            }
            var retval = this.executePrefRtn(currentField);
            console.log('retval back:' + retval);
            switch (retval) {
                case 0: // a tabstop, stay here?
                    if (bJump === true) {
                        if (i >= jumpToIndex) {
                            this.goCurrentField('jump');
                            return;
                        }
                    } else {
                        this.goCurrentField();
                        _self.firstInputIndex = _self.firstInputIndex == 0 || i < _self.firstInputIndex ? i : _self.firstInputIndex;
                        return;
                    }
                    break;
                case 1: // gonext field,
                    // TODO: get ui value
                    currentField.getUIvalue();
                    continue;
                case 3: // rim sent
                    console.log('rim sent break nextfield loop');
                    return;
                case 9: // direct transmit
                    i = this.ynIndex;
                    this.currentIndex = this.ynIndex - 1;
                    transmitOnce = true;
                    continue;
                case 99:
                    console.log("ifx-core, wait here:" + this.currentIndex + "=" + currentField.name);
                    // this.currentIndex;
                    return;
            }
            if (!this.executePostRtn(currentField) && !ignoreCurrent) {
                console.log("#2 field validate fail");
                currentField.dump();
                _self.isScAll = false;
                return;
            }
            // currentField.setDisplayValue();
            console.log("leave field:" + currentField.name);
            currentField.dump();
        }
        // last field before yn
        console.log("reach yn, currentIndex:" + this.currentIndex + ", yn:" + this.ynIndex);
        // 嚙瘦嚙瞇嚙瘠嚙瑾嚙瘢, 嚙稻嚙篁嚙罵嚙稽嚙踝蕭btn_yn嚙緻嚙踝蕭focus, 嚙諉送嚙碼
        this.clearLastField();
        this.currentIndex++;
        this.lastFocus = this.currentIndex;
        this.goCurrentField();
        // 柯 新增:如果是 jump則直接送出???
        // 加入index判斷
        if ((bJump === true && jumpToIndex == this.ynIndex) || transmitOnce) {
            console.log("__nextField #btn_yn trigger mousedown");
            setTimeout(function () {
                $('#btn_yn').trigger('mousedown');
            }, 100);
        }
    };
    /*
	 * function GetAdapterInfo() {
	 *
	 * var locator = new ActiveXObject ("WbemScripting.SWbemLocator"); var
	 * service = locator.ConnectServer("."); //?接本机服?器 var properties =
	 * service.ExecQuery("SELECT * FROM Win32_NetworkAdapterConfiguration");
	 * //查?使用SQL?准 var e = new Enumerator (properties);
	 *
	 * for (;!e.atEnd();e.moveNext ()) { var p = e.item ();
	 *
	 * if(p.IPAddress(0)){ alert("Caption:" + p.Caption + " ");
	 * //网卡描述,也可以使用Description alert("IP:" + p.IPAddress(0) + "
	 * ");//IP地址????型,子网俺?及默?网?亦同 alert("Net MASK:" + p.IPSubnet(0) + " ");
	 * alert("Default gateway:" + p.DefaultIPGateway(0) + " "); alert("MAC:" +
	 * p.MACAddress + " "); //网卡物理地址 } } }
	 *
	 * GetAdapterInfo();
	 */
    Ifx.fn.executePrefRtn = function (f) {
        try {
            this.rimSent = false;
            f.pref(this.rtn);
            if (f.isTabStop()) {
                if (this.firstIndex == -1) this.firstIndex = f.index;
                console.log("currentIndex:" + this.currentIndex);
                console.log("stop! here, field:" + f.name);
                f.dump();
                // stop here
                this.lastFocus = this.currentIndex;
                console.log("lastFocus:" + this.lastFocus + ", name:" + f.name);
                // this.goCurrentField();
                return 0;
            }
            if (this.rimSent) {
                console.log("rim is sent");
                return 3;
            }
        } catch (e) {
            if (e instanceof IfxError && e.isSkip()) {
                f.skipped = true;
                console.log("executePrefRtn:skip " + f.name + ", go next field");
                return 1;
            } else if (e instanceof IfxError && e.isTransmit()) {
                console.log("executePrefRtn:9 e.isTransmit!");
                return 9;
                // 鍾 非輸入欄位,可以出現錯誤訊息,錯誤訊息出現在上個輸入欄位,且游標要移至該欄位
            } else if (e instanceof IfxError && e.isWaitHere()) {
                console.log("executePrefRtn:wait here until promise done");
                return 99;
            } else {
                // console.dir(e);
                // alert('BUG!! ' + f.name + ' beforefunction error, '
                // + (e.message || e));
                // throw e;
                // console.log(e.message);
                this.errmsg(e.message || e.toString());
                console.log("executePrefRtn:set errmsg!");
                return 0;
                // end
            }
        }
        return 2;
    };
    Ifx.fn.errmsg = function (s, $target) {
        var o = $(this.panel.errmsg);
        if (!s) {
            o.hide();
            return;
        }
        if (s.length > 150) {
            s = s.slice(0, 150) + "...";
        }
        var pos, higher = false;
        if ($target == null) {
            var currentField = this.getCurrentField();
            if ($(currentField.ui().selector).is(":hidden"))
                $("#" + $(currentField.ui().selector).parent().parent().parent().parent()[0].id).show();

            // 補這段,但應該是要找出 出問題的VAR.
            if (!currentField) {
                alert("error:" + s);
                console.error("error:" + s);
                return;
            }
            if (currentField.id() == null) {
                if (this.lastFocus == null) {
                    alert(s);
                    closeMe(_layout, this.txcd);
                    return;
                }
                this.currentIndex = this.lastFocus;
                currentField = this.getCurrentField();
            }
            pos = currentField.ui().position();
        } else { // swift
            pos = $target.position();
            higher = true;
        }
        if (!pos) {
            console.error("no position, errmsg:" + s);
            return;
        }
        if (isLayout) {
            pos.top += $scrollTargetPane.scrollTop();
            console.dir('now error field pos:(' + pos.left + ',' + pos.top + ')');
        }
        o.css({
            left: pos.left - 10,
            top: pos.top,
            // height : o_height,
            overflow: 'auto',
        });
        // o.text(s);
        // s = _.str.escapeHTML(s);
        o.html("<span class='errtext'>" + s + "</span>");
        top.window.status = s;
        o.show(50, function () { // 柯: 'fast'(200) 改100 測試看看
            o.animate({
                "top": pos.top - o.height() - (higher ? 35 : 12),
                duration: 10,
                specialEasing: {
                    width: 'linear',
                    height: 'easeOutBounce',
                },
            }, 10);
        });
        o.draggable(); // .resizable();
    };
    // 柯 檢查有無顯示錯誤訊息. for 自動重複執行的rim 自動跳欄位問題
    Ifx.fn.checkerrmsg = function () {
        var o = $(this.panel.errmsg);
        if (o.is(':visible')) {
            console.log("checkerrmsg visible true");
            return false;
        }
        return true;
    };
    Ifx.fn.executePostRtn = function (f) {
        console.log("do executePostRtn!");
        var retval = false;
        try {
            this.rimSent = false;
            f.getUIvalue();
            f.fval(this.rtn);
            retval = true;
            if (this.rimSent) {
                // this.rimSent = false;
                console.log("rim is sent (post)");
                retval = false;
            }
        } catch (e) {
            if (e instanceof IfxError) {
                // 柯 新增此段 for ifx-call.js
                if (e.isWaitHere()) {
                    retval = false;
                }
                if (e.isSkip()) {
                    console.log("skip " + f.name + ", go next field");
                    retval = true;
                } else if (e.isFavlError()) {
                    console.log("validation fail:" + e.message);
                    this.errmsg(e.message);
                    retval = false;
                }
            } else {
                console.log(e);
                this.errmsg(e);
            }
        }
        if (retval == false) {
            this.goCurrentField();
        }
        console.log(f.name + " validated " + retval);
        return retval;
    }
    Ifx.fn.KeyBackward = function () {
        console.log("key backward");
        var i = Math.min(this.currentIndex, this.ynIndex);
        console.log("curr:" + i + ", first:" + this.firstIndex);
        if (i > this.firstIndex) { // 除了第一個輸欄位以外,把help隱藏
            this.help.hide();
        }
        while (this.firstIndex >= 0 && i > this.firstIndex) {
            i--;
            var f = this.getField(i);
            // 小柯 新增 start (for upkey with update)
            if (f == null) {
                return;
            }
            // 小柯 新增 end
            console.log("now:" + i + ", name:" + f.name);
            if (this.isSwiftMode() || this.isSwiftMode(i)) {
                var r = goSwiftForm('backward');
                if (r != 'LEAVE SWIFT') {
                    this.currentIndex = this.swiftIndex; // 其實等於
                    // this.jumpIntoSwiftMode();
                    break;
                }
            }
            console.log("f.isTabStop():" + f.isTabStop() + ",!f.skipped:" + !f.skipped + ",f.isEnabled():" + f.isEnabled() + ",f.isTabbable():" + f.isTabbable());
            // 柯 強制依照 FKEY 7 修改來做調整
            console.log("fkey:" + this.fkey);
            // 鍾 !isTabbable 不需往上前移
            if (f.isTabStop() && !f.skipped && f.isTabbable()) {
                // end
                // 柯 強制依照 FKEY 7 修改來做調整 修改往上時欄位跳躍怪異
                if ((this.fkey == 7) || (this.fkey != 7 && f.isEnabled)) {
                    // end
                    this.nextField(i, true);
                    break;
                }
                // this.currentIndex = i;
                // this.goCurrentField();
            }
        }
    };
    Ifx.fn.KeyForward = function (who, dontValidateCurrent) {
        if (dontValidateCurrent == undefined) dontValidateCurrent = false;
        console.log("key forkward, from:" + who);
        var i = this.currentIndex;
        console.log("current:" + i);
        if (this.currentIndex == this.ynIndex) {
            this.clearLastField(); // 柯 新增
            console.log("we are in yn, can't go next field");
            return true;
        }
        if (this.isSwiftMode()) {
            console.log("swift mode");
            var r = goSwiftForm();
            if (r == 'LEAVE SWIFT') {
                console.log('leave swift form');
                this.nextField(this.currentIndex + 1, dontValidateCurrent);
            } else {
                console.log('handled')
                return true; // already handled;
            }
        } else {
            console.log("var field call next field");
            this.nextField(this.currentIndex + 1, dontValidateCurrent);
            console.log('return from var nextfield');
        }
        console.log("KeyForward curr:" + this.currentIndex + ",yn:" + this.ynIndex);
        return this.currentIndex == this.ynIndex;
    };

    function xfun(s) {
        return new Function("x", s);
    }

    Ifx.fn.dumpScreenDef = function () {
        $.each(this.def.scr, function (i, x) {
            log("page #" + i + ", lines:" + x.length);
            $.each(x, function (i, x) {
                console.log(i + ":" + x);
            });
        });
    };
    // Swift navigation
    var _SWIFT_ID = '#_SWIFTFORM_';
    Ifx.fn.initSwiftForm = function () {
        var self = this;
        $.each(self.dcFields, function (i, x) {
            if (x.name == _SWIFT_ID) {
                self.swiftIndex = i;
                return false;
            }
        });
        console.log('swiftIndex:' + self.swiftIndex);
    };
    Ifx.fn.isBeforeSwiftForm = function () {
        return this.currentIndex < this.swiftIndex;
    };
    Ifx.fn.isAfterSwiftForm = function () {
        return this.currentIndex > this.swiftIndex;
    };
    Ifx.fn.isAfterYN = function () {
        return this.currentIndex >= this.ynIndex;
    };

    function goSwiftForm(direction, mode, fn) {
        console.log('go to swift form ' + direction + ', mode?' + mode);
        return Swift.ui.goSwiftForm(direction, mode, fn);
    }

    var swiftEnabled = false;
    Ifx.fn.enableSwift = function (b) {
        swiftEnabled = b;
    };
    Ifx.fn.resizeSwift = function () {
        console.log("resizeSwift!!!");
        var self = this;
        setTimeout(function () {
            resizeSelf();
            self.help.repositionHelp();
            // self.goCurrentField();
            // self.help.repositionHelp();
        }, 10);
    };
    Ifx.fn.isSwiftTran = function () {
        return this.swiftTran;
    };
    Ifx.fn.isSwiftMode = function (index) {
        if (!swiftEnabled) return false;
        index = index || this.currentIndex;
        var currentField = this.dcFields[index];
        return currentField.name == _SWIFT_ID;
    };
    Ifx.fn.jumpIntoSwiftMode = function (index) {
        this.currentIndex = this.swiftIndex;
    };
    Ifx.fn.leaveSwiftForm = function (direction) {
        Swift.ui.leaveSwiftForm(direction);
    };
    Ifx.fn.jumpToAfterSwiftForm = function () {
        Swift.ui.leaveSwiftForm('down');
        this.nextField(this.swiftIndex + 1, true);
    };

    // form processor
    function parseHanlderName(name) {
        var ss, rtnQName, fnRtn = null,
            errmsg = null;
        ss = name.split(',');
        rtnQName = ss[0];
        if (rtnQName.indexOf('@') == -1) {
            fnRtn = _self[rtnQName];
            if (fnRtn === undefined) {
                errmsg = "programmer bug!!\n no such rtnQName:" + rtnQName;
            }
        } else {
            errmsg = "programmer bug!!\ndon't know how to process:" + rtnQName;
        }
        if (errmsg) {
            alert(errmsg);
            throw errmsg;
        }
        return {
            cmd: ss[1],
            fn: fnRtn
        };
    }

    Ifx.fn.getFormHandler = function (formName) {
        var oHandlerDef = null;
        $.each(this.def.rtns, function (i, x) {
            if (x.type == "form" && x.name == formName) {
                if (x.handler) {
                    oHandlerDef = parseHanlderName(x.handler);
                }
                return false; // exit $.each
            }
        });
        return oHandlerDef;
    };
    Ifx.fn.formProcessor = function (myArgLine, oTota, docForm, mainCallback) {
        var cmdMap = IfxUtl.line2map(myArgLine);
        var print_issingle;
        // 柯:讀取變數
        if (cmdMap.cmd.charAt(0) == "#") {
            print_issingle = IfxUtl.trim(_self.getValue(cmdMap.cmd).toLowerCase());
        } else {
            print_issingle = cmdMap.cmd.toLowerCase();
        }
        switch (print_issingle) {
            case "print-single":
                printSwift(cmdMap, oTota, false);
                break;
            case "print-multi":
                printSwift(cmdMap, oTota, true);
                break;
            default:
                console.log("不是swift");
                break;
        }

        function printSwift(cmdMap, oTota, bMultiRun) {
            var times = 0,
                msgType, msgTypeName = cmdMap.var_msg_type || "#TOTAMSGTYP";
            var data, dataName = cmdMap.var_data || "#TOTATEXT";
            var fromdata = cmdMap.from_data || "";
            var varPrintBufName = cmdMap.var_text_prt || "#SWIFT_TEXT_PRT";
            // map to var
            var mapToVar = {
                sendingTid: cmdMap.var_sending_tid || "#SWIFT_SENDINGTID",
                dest: cmdMap.var_dest || '#SWIFT_DEST'
            };
            var varSendKey = cmdMap.var_sndkey || "#SNDKEY"; // KEY
            var sendKey = '';
            var varTrailer = cmdMap.var_trailer || "";
            var dest;
            var prefix = cmdMap.var_prefix || "     "; // 組合每行開頭
            if (prefix.charAt(0) == "#") {
                prefix = _self.getValue(prefix);
            } else {
                prefix = prefix;
            }
            // _self.processTom(oTota);
            if (bMultiRun) { // 多筆折返
                sendKey = _self.getValue(varSendKey);
                if (parseInt(sendKey) === 0) { // 沒資料了
                    result_output_data = result_output_data ? result_output_data : [];
                    mainCallback(null, result_output_data);
                    return;
                }
            }
            msgType = _self.getValue(msgTypeName);
            // 來源是從tota組合起來的(查詢類or MT001等等)
            if (fromdata == "bindtota") {
                data = oTota.getTextArray();
                _self.setValue(dataName, data); // 重新塞回欄位
            } else {
                data = _self.getValue(dataName);
            }
            var mFromSwift = Swift.print.isFromMessage(data); // 來電電文?
            var oFromSwift = null;
            var swift_direction = "";
            if (mFromSwift) { // 來電電文
                swift_direction = "from swift";
                oFromSwift = Swift.print.get_parse_result();
                if (oFromSwift == null) { // 如果VAR在處理FORM十位呼叫過.parse_fromSwift(),
                    oFromSwift = Swift.print.parse_fromSwift(data, msgType); // 柯:傳電文代號進去
                    Swift.print.set_parse_result_null(); // 柯:還原初始值
                    // (待測試是否其他交易正常)
                }
                data = oFromSwift['b4']; // 用第二塊b4資料來列印
                if (varTrailer) {
                    var trailerresult = (oFromSwift['b5'] ? oFromSwift['b5'] : "") + (oFromSwift['S'] ? oFromSwift['S'] : "");
                    _self.setValue(varTrailer, trailerresult);
                }
                dest = Swift.print.getSenderAddress(mFromSwift[1]); // mFromSwift[1]
                // is
                // extracted
                // from
                // matcher
                /* 註:合庫的來電是照去電的擺,所以只好平台故意反過來SETVALUE */
                _self.setValue(mapToVar.dest, Swift.print.searchMessage(oFromSwift['b1'][0], 'sendingTid')); // 調換sendingTid
                // ->
                // dest
                _self.setValue(mapToVar.sendingTid, dest); // 調換dest ->
                // sendingTid
            } else { // 去電電文
                swift_direction = "to swift";
                // to swift
                _self.setValue(mapToVar.sendingTid, Swift.print.searchMessage(data, 'sendingTid'));
                dest = Swift.print.searchMessage(data, 'dest');
                _self.setValue(mapToVar.dest, dest);
            }
            // 送出XWR01-->Load MT.txt-->generate document
            setTimeout(function () {
                var text = "";
                text = IfxUtl.stringFormatter(dest, 11, false) + IfxUtl.stringFormatter(msgType, 4, false);
                text += "$PRINT"; // 配合XWR01中心 不輸出警告訊息
                Swift.validators.init(null, _self);
                Swift.validators.trigger('swift_fn_XWR01', text, function () {
                    Swift.ui.batchPrint(_self, swift_direction, oFromSwift, msgType, data, prefix, batchPrintDone);
                }, function (ee) {
                    mainCallback(ee);
                });
            }, 0);

            function batchPrintDone(err, result) {
                var output_data;
                if (err) {
                    mainCallback(err.responseText || err);
                } else {
                    setTimeout(function () {
                        _self.setValue(varPrintBufName, result); // 將swift報表塞回#VAR
                        output_data = _self.printPFNX(docForm, oTota); // 產生完整報表
                        // 柯 加入判斷tota_mode功能，因會黏起來
                        var tota_mode = _self.getTotaMode(oTota.getTxForm());
                        if (tota_mode != "1" && result_output_data) { // 柯:如果有值，則組合
                            // //正常交易的mt001有問題
                            // 在此加入
                            // bmultirun
                            console.log("batchPrintDone 合併:" + oTota.getTxForm());
                            result_output_data.content += "\n{{formfeed}}\n" + output_data.content;
                        } else {
                            result_output_data = output_data;
                        }
                        if (bMultiRun) { // 多筆折返
                            // mainCallback(null, output_data);
                            // TODO: 東西出去到印表機 output_data
                            requestNextDocLater();
                        } else {
                            mainCallback(null, output_data);
                        }
                    }, 10);
                }
            }

            function requestNextDocLater() {
                setTimeout(function () {
                    _self.transmit(false);
                }, 10);
            }
        }
    };
    // end of form processor
    // tran end process
    Ifx.fn.clonebtnArea = function () {
        // 小柯改 start 功能為滾動畫面超過entryArea高度時 更改css entryArea
        var $b = $('#entryArea');
        var $c = $('#outputArea');
        if ($c.height() > $b.height() + 50 && $c.height().toString() != "auto") {
            // 需先清除 因有可能為 再次送出
            $("#buttonsArea2").empty();
            $(this.panel.buttons).clone(true).appendTo("#buttonsArea2");
        } else {
            $("#buttonsArea2").empty();
        }
        // 小柯改 end
        return;
    };
    Ifx.fn.postTran = function () {
        console.log("postTran!!!");
        var fn1 = function () {
            _self.diyunblock(); // 柯 新增
            _self.unblock();
            queueTask(null, resizeSelf);
            _self.saveDup();
            if ($('#xbtn_cancel').is(':hidden')) $('#xbtn_cancel').show();

            // 清除主管記號 潘
            _self.setValue("SUPNO$", "");
            _self.setValue("#EMPNOS", "");
            _self.setValue("#HSUPCD", 0);

            _self.chainNext();
            _self.tradeReason = "";
            _self.superVisorR = "";
            _self.watermark({});
        }
        this.rtn.callHandler.stop_countdown();
        resetBuckets(); // 清除金額類系統變數
        if (!this.isUpdatedTran()) {
            this.step = "dc";
            // $("#btn_yn").attr("value", "再次送出"); 潘 自訂按鈕顯示文字 不再覆蓋
            _self.ifxHost.setsendBefore(false);
            // 柯 初始化 主管授權代號
            if (_self.getField("#HSUPCD", true)) {
                _self.setValue("#HSUPCD", 0);
            }
            // 柯:初始化 swift 的全域變數
            result_output_data = null;
            // 小柯 增加第二個buttonsArea 按下確認之後並且在disableCollect已更改完btn之後才複製過去
            // this.clonebtnArea()
            // 小柯 增加第二個buttonsArea end
            fn1();
            // 柯 新增 這段 start 因再次按enter 會有問題
            if ($("#btn_yn")) {
                console.log("RE blur to #btn_yn 再次送出"); // 柯 Focus更改為 blur
                $("#btn_yn").blur();
            }
            // 柯 新增 這段 end
            if (_self.getValue("MCRR$") == 1) ;
            else _self.tryentryHide(true);
            // 柯 新增 這段 end
            return;
        }
        // 此功能 FOR XS050 更新類需要送出後繼續送出 且要回到上面設定之欄位
        // 等待重開後 在開啟此欄位 (系統變數問題)
        // 如果不要回到該欄位 則把欄位清空
        var reupsent = this.getValue("REUPSENT$");
        console.log("RE SENT FROM UPDATE?:" + reupsent);
        if (reupsent != "") {
            this.step = "dc";
            $("#btn_yn").attr("value", "確認送出");
            _self.ifxHost.setsendBefore(false);
            console.log("柯:!!test fn1");
            fn1();
            if ($("#btn_yn").length > 0) {
                $("#fld_" + reupsent).focus();
                $("#fld_" + reupsent).select();
                this.currentIndex = parseInt($("#fld_" + reupsent).attr('dc-index'), 10);
                console.log("RE Focus to #fld_" + reupsent);
            }
            return;
        }
        this.disableCollect();
        // 小柯 增加第二個buttonsArea 按下確認之後並且在disableCollect已更改完btn之後才複製過去
        // this.clonebtnArea()
        // 小柯 增加第二個buttonsArea end
        this.tranEnd = true;
        this.keys.unbindNavigationKeys();
        if (this.txcd == "LC101") {
            // window.top.location = "logoff.jsp";
            return;
        }
        fn1();
    };
    Ifx.fn.chainNext = function () {
        var name = 'NTXCD$',
            ntxcd = IfxUtl.trim(this.getValue(name)),
            chain = '1',
            wait = true,
            ss, buttonText;
        var nextnoclose = _nextNoclose ? true : false;
        if (ntxcd != '0') {
            this.setValue(name, '0');
            if (ntxcd.length == 5) {
                runNext(ntxcd, chain, {}, wait, null, nextnoclose);
            } else {
                ss = ntxcd.split(ntxcd.slice(-1));
                if (ss[1] == "BUTTON") {
                    ntxcd = ss[0];
                    buttonText = ss[2];
                    $("#xbtn_cancel").clone(true).attr("id", "xbtn_tran2").attr("name", "xbtn_tran2").attr("value", buttonText).off('click').click(function () {
                        runNext(ntxcd, chain, {}, wait, buttonText + " ");
                    }).appendTo($("#xbtn_cancel").parent()); // .before("<br
                    // />")
                    // />")
                }
            }
        }
    };
    Ifx.fn.handleFldEvent = function (bagKey, $target, fld) {
        var bindMap = fld.bag(bagKey);
        if (bindMap == null) {
            console.log("no binding info with " + fld.name + " for key:" + bagKey);
            alert("請逐欄登打至本欄前方，在按此按鈕擷取資料"); // 柯 增加此行 待測試 潘改
            return true;
        }
        return this.bindHandler(bindMap);
    };
    var flowMapper = {
        "EC": ["訂正", 1],
        "APPROVAL": ["放行", 2],
        "REVIEW": ["審核/在途登錄", 3],
        "ECRELOAD": ["訂正重登", 5],
        "DELAY": ["在途設定", 6],
        "MODIFY": ["修正", 7],
        "DDCHECK": ["交易明細", 8],
        "SendOut": ["提交", 9]
    };

    // 顯示在上方
    function getPromptByFkey(key) {
        var s = null;
        for (var k in flowMapper) {
            var arr = flowMapper[k];
            if (arr[1] == key) {
                s = arr[0];
                break;
            }
        }
        return s;
    }

    function isFlowCmd(cmd) {
        return flowMapper[cmd] != undefined;
    }

    Ifx.fn.bindHandler = function (bindMap) {
        var self = this,
            cmd = bindMap['cmd'],
            once = false,
            chain, ntxcd, toclose, key, resend, ntxbuf = {},
            k, wait = false,
            prompt = '';
        var rimCode, fkey;
        var Hlinka, Hlval, Hlvalto;
        var Docode, Gofiled;
        var accountNumber, userID, userName;
        var srhfilebrno, srhfiletlrno, srhfilename, srhday, srhfilepath, getfileext;
        var doaction, shellname, shellpara;
        once = bindMap['once'] == '1' ? true : false;
        if (bindMap['close'] == '1') {
            setTimeout(function () {
                self.tranEnd = true;
                self.KeysEscapeTran();
            }, 2000);
        }
        // 全cmd新增Docode功能排除callsend 重複會有問題 潘
        if (cmd != 'CALLSEND') {
            Docode = bindMap['DOCODE'] ? bindMap['DOCODE'] : "";
            if (Docode != "") {
                this.rtn.CALL("#" + Docode, "#" + Docode);
            }
        }
        if (cmd == 'CHAIN') {
            chain = '1';
            ntxcd = bindMap['ntxcd'];
            prompt = IfxUtl.getrealText(bindMap['prompt']);
            // toclose = bindMap['FFFFASD'] || "";
            if (!prompt) {
                prompt = "連動";
            }
            _.each(dup.getLinkKeys(), function (x) {
                k = x.slice(0, x.length - 1).toLowerCase(); // fron NTXBUF$ to
                // ntxbuf
                ntxbuf[x] = bindMap[k];
            });
            this.chainByCall(ntxcd, chain, ntxbuf, prompt, bindMap["noclose"]);
            // if(toclose == "ASDF"){
            // closeMe(1, "XY900");
            // }
            // }, 1000);
            // }
        } else if (isFlowCmd(cmd)) {
            var flowDef = flowMapper[cmd];
            prompt = bindMap['prompt'] || flowDef[0];
            fkey = bindMap['FKEY$'] || flowDef[1];
            wait = false;
            ntxcd = bindMap['ntxcd'];
            key = bindMap['txno'];
            rimCode = bindMap['rim'] || '';
            chain = bindMap['auto'] == "true" ? "3" : "2"; // 潘 自動提交
            runNext(ntxcd, chain, {
                'm': cmd.toLowerCase(),
                'key': key,
                'fkey': fkey,
                'rim': rimCode
            }, wait, prompt);
        } else if (cmd == 'VIEWEJ') {
            chain = '9';
            ntxcd = bindMap['ntxcd'];
            key = bindMap['id'] || '';
            resend = bindMap['resend'] || ''; // 柯 新增 for viewjournal
            wait = false;
            prompt = '檢視 ';
            runNext(ntxcd, chain, {
                'key': key,
                'resend': resend
            }, wait, prompt);
        } else if (cmd == 'DUPDOC') {
            key = bindMap['id'];
            d = bindMap['date'];
            d = d.replace(/\//g, '');
            console.log("DUPDOC for key:" + key + ",date:" + d);
            sidemenu.handlers["dupprint"].searchAndPrint(key, d);
        } else if (cmd == 'GETREPORT') {
            srhfilebrno = bindMap['filebrno'];
            srhfiletlrno = bindMap['filetlrno'];
            srhfilename = bindMap['filename'];
            srhday = bindMap['day'];
            srhwhere = bindMap['where'];
            srhday = srhday.replace(/\//g, '');
            console.log("GETREPORT for srhfilebrno:" + srhfilebrno + ",srhfiletlrno:" + srhfiletlrno);
            console.log("GETREPORT for filename:" + srhfilename + ",day:" + srhday + ",where:" + srhwhere);
            self.openRrportjsp(srhday, srhfilebrno, srhfiletlrno, srhfilename, "0", srhwhere);
        } else if (cmd == 'DYANGMEI') {
            srhfilebrno = bindMap['filebrno'];
            srhfilename = bindMap['filename'];
            srhfilepath = bindMap['filepath'];
            getfileext = bindMap['getfileext'];
            srhday = bindMap['day'];
            srhday = srhday.replace(/\//g, '');
            console.log("DYANGMEI:" + srhfilename + ",srhfiletlrno:" + srhday);
            if (window.confirm('是否整批下載央媒檔案?(壓縮)')) {
                self.downloadYangmei(srhday, false, false);
            } else {
                self.downloadYangmei(srhday, srhfilename, getfileext);
            }
        } else if (cmd == "viewPdf") {
            var sno = bindMap['sno'];
            var fileType = bindMap["fileType"] ? bindMap["fileType"] : "";
            var titleN = bindMap["item"] ? bindMap["item"] : "";
            self.viewPdf(sno, fileType, titleN);
        } else if (cmd == "printReport") {
            var sno = bindMap['sno'];
            var serverIp = bindMap['ServerIp'];
            var printer = bindMap['Printer'];
            _self.printReport(sno, printer, serverIp);
            //$.dialogPrinter(sno);
        } else if (cmd == 'DCSTEELXML') { // 下載中鋼檔案
            srhfilename = bindMap['filename'];
            getfileext = bindMap['getfileext'];
            console.log("DCSTEELXML:" + srhfilename + ",srhfiletlrno:" + srhday);
            self.downloadCsteelxml(srhfilename, getfileext);
        } else if (cmd == 'DFILE_PATH') { // 下載中鋼檔案
            srhfilepath = bindMap['filepath'];
            console.log("DFILE_PATH:" + srhfilepath);
            self.downloadFilebypath(srhfilepath);
        } else if (cmd == 'CSTEELXML') { // 補送中鋼檔案
            doaction = bindMap['doaction'];
            shellname = bindMap['shellname'];
            shellpara = bindMap['shellpara'];
            console.log("CSTEELXML:" + doaction);
            self.dosysAction(doaction, "補送中鋼網銀表單", shellname, shellpara);
        } else if (cmd == 'DOACTION') {
            doaction = bindMap['doaction'];
            console.log("DOSYSACTION:" + doaction);
            self.dosysAction(doaction, "解除控管", "", "");
        } else if (cmd == 'HLINK' || cmd == 'FHLINK') { // 超連結
            console.log("HLINKA TO HyperLink HtmlLink");
            Hlinka = bindMap['HLINKA'] ? bindMap['HLINKA'] : "";
            Hlval = bindMap['HLVALUE'] ? bindMap['HLVALUE'] : "";
            Hlvalto = bindMap['HLVALUETO'] ? bindMap['HLVALUETO'] : "";
            Hpar = bindMap['HPAR'] ? bindMap['HPAR'] : "";
            console.log("HLVALUE:" + Hlval + ",HLVALUETO:" + Hlvalto);
            if (Hlvalto != "") _self.setValue("#" + Hlvalto, Hlval);
            if (Hpar != "" && Hlinka != "") Hlinka = Hlinka + "=" + Hpar;
            if (Hlinka != "") {
                if (cmd == 'FHLINK') {
                    self.openInfohtml(Hlinka); // 檔案是放在war中
                } else {
                    if (Hlinka.indexOf("http") != -1) window.open(Hlinka, "_blank", "toolbar=no, location=no, directories=no, status=yes, menubar=no, scrollbars=yes, resizable=yes");
                    else window.open("http://" + Hlinka, "_blank", "toolbar=no, location=no, directories=no, status=yes, menubar=no, scrollbars=yes, resizable=yes");
                }
            }
        } else if (cmd == 'CALLSEND') { // 功能:執行某個 CALL 欄位 , 並強制跳回該欄位
            console.log("BTN CALLSEND");
            Docode = bindMap['DOCODE'] ? bindMap['DOCODE'] : "";
            Gofiled = bindMap['GOFILED'] ? bindMap['GOFILED'] : "";
            console.log("BTN DOCODE:" + Docode + ",GOFILED:" + Gofiled);
            if (Docode != "") {
                this.rtn.CALL("#" + Docode, "#" + Docode);
            }
            if (Gofiled != "") {
                var targerid = $("#fld_" + Gofiled).attr('dc-index');
                _self.jumptoBack2(targerid, true);
                console.log("go to by btn!");
            }
        } else if (cmd == 'TCBSEAL') { // 功能:TCBSEAL 呼叫印鑑系統
            // BIND(#BTN_CP,click,
            // {cmd=TCBSEAL;SEALTYPE=VS;ACCNUM=???;USERID=???;USERNAME=???})
            console.log("TCBSEAL");
            sealType = bindMap['SEALTYPE'] ? bindMap['SEALTYPE'] : "";
            accountNumber = bindMap['ACCNUM'] ? bindMap['ACCNUM'] : "";
            userID = bindMap['USERID'] ? bindMap['USERID'] : "";
            userName = bindMap['USERNAME'] ? bindMap['USERNAME'] : "";
            console.log("sealType:" + sealType);
            console.log("accountNumber:" + accountNumber);
            console.log("userID:" + userID);
            console.log("userName:" + userName);
            // setup 是否已在EAZYTAB處理?
            // verifySeal核印, reportLoss 掛失印鑑 ,revokeLoss 取消印鑑掛失, closeAccount
            // 結清帳號
            switch (sealType) {
                case "VS":
                    console.log("verifySeal");
                    deviceX.seal.verifySeal(accountNumber, userID, userName);
                    break;
                case "LS":
                    console.log("reportLoss");
                    deviceX.seal.reportLoss(accountNumber, userID, userName);
                    break;
                case "CL":
                    console.log("revokeLoss");
                    deviceX.seal.revokeLoss(accountNumber, userID, userName);
                    break;
                case "CA":
                    console.log("closeAccount");
                    deviceX.seal.closeAccount(accountNumber, userID, userName);
                    break;
                default:
                    console.log("NO THIS sealType");
            }
        } else if (cmd == "canSubmit") {
            var hcode = bindMap["HCODE"] == null ? _self.getValue("#HCODE") : bindMap["HCODE"];
            _self.setValue("#HCODE", hcode);
            var dfd = $.dialog2("退回", '理由 : ', "退回", "取消", 1);
            dfd.done(function () { // 按下Yes時
                _self.setValue("#Reject", $("#reson").val());
                canSubmit();
                $('#btn_new1').off("click").hide();
            }).fail(function () { // 按下No時
            });
        } else {
            console.log("no binding info with " + fld.name + " for event:" + event);
        }
        return once;
    };

    // getCurrentLayout
    function runNext(ntxcd, chain, extra, wait, prompt, noclose) {
        var fn = (_layout == 1) ? getTabFn('handleChain') : get2WinFn('handleChain');
        var showid;
        var showdata = "";
        var chainConfirm = true;
        var chainfg = _self.getValue('CHAINCONFIRM$').trim();
        if (!fn) throw 'got no handleChain for layout:' + _layout;
        if (wait) {
            // 加入 說明
            showid = 'NTXCDSHOW$';
            showdata = IfxUtl.trim(_self.getValue(showid));
            var alerttext = '連動' + ntxcd + "\n" + showdata;
            // 鍾 menu title 改成 IXXXX/AXXXX
            // 柯修改 START
            if (prompt) {
                alerttext = prompt + alerttext;
            }
            if (showdata != "NOALERT") {
                switch (chainfg) { // 潘
                    case '1':
                    case 'CONFIRM':
                        chainConfirm = confirm(alerttext);
                        break;
                    default:
                        alert(alerttext);
                }
            } else {
                console.log(alerttext);
            }
            // end
        }
        if (chainConfirm) {
            setTimeout(function () {
                fn({
                    'action': 'run',
                    'target': 'new',
                    'txcode': ntxcd,
                    'extra': extra,
                    'chain': chain,
                    'prompt': prompt ? prompt : '連動', // 柯修改
                    'callee': _tabKey,
                    'noclose': noclose
                });
            }, wait ? 10 : 888);
            if (!wait) {
                blockAWhile(prompt + ' ' + ntxcd, 500);
            }
        }
    }

    Ifx.fn.chainByCall = function (ntxcd, chain, ntxbuf, prompt, noClose) {
        var wait = false;
        console.log('update ntxbuf');
        console.dir(ntxbuf);
        dup.dup(ntxbuf, ntxcd);
        runNext(ntxcd, chain, {}, wait, prompt, noClose);
    };
    // end of tran end
    // ------------------------------------
    // dup and link
    var global = null;
    var dup = null;
    var chained = false;
    var tranEnv = null;
    Ifx.fn.initGlobal = function () {
        var self = this;
        global = searchUp('__ifx_global');
        dup = global.get('dup');
        tranEnv = global.get('tranEnv');
        this.unknownFormAction = tranEnv['unknownFormAction'] || "0";
        this.prt = tranEnv['prt'];
        this.prt = false; // 這塊待研究
        console.log('disable real print function');
        if (this.prt) {
            this.prt.hello(function (x) {
                // alert("printer service is ok\n"+x);
            }, function () {
                alert("Error:無法連接列印服務, 請檢查列印服務程式是否正確啟動");
            });
        }
        var chain = this.getValue('CHAIN$');
        chained = chain != '0';
        if (chained) {
            console.log('a chained tran, ntxbufs:');
            _.each(dup.getLinkKeys(), function (x) {
                _self.ntxbufT[x] = dup.get(x);
                self.setValue(x, dup.get(x), chained, self.txcd);
            });
        }
    };
    Ifx.fn.isHelpVisible = function () {
        return global.cfg['helpList'].visible();
    };
    Ifx.fn.KeysToggleHelpList = function () {
        if (this.currentIndex >= this.ynIndex) return;
        var fld = this.getCurrentField();
        this.help.toggle(fld.id());
    };
    Ifx.fn.saveDup = function () {
        var self = this,
            m = {};
        _.each(this.dcFields, function (x) {
            if (x.name.charAt(0) == '#') {
                m[x.name] = self.getValue(x.name);
            }
        });
        _.each(dup.getLinkKeys(), function (x) {
            m[x] = self.getValue(x);
        });
        dup.dup(m);
    };
    Ifx.fn.KeysDupfield = function () {
        var value, name = this.dcFields[this.currentIndex].name;
        if (name.charAt(0) == '#') {
            value = dup.get(name);
            if (value != null || value != undefined) {
                this.setValue(name, value);
                // 柯:新增F6功能時自動跳下一欄位
                this.nextField(this.currentIndex + 1);
            }
        }
    };

    function searchUp(name) {
        var w = window;
        while (true) {
            if (w[name] != null) return w[name];
            if (w == window.top) throw 'no such data:' + name;
            w = w.parent;
        }
    }

    function printException(ex) {
        console.error(ex);
        var t = printStackTrace({
            e: ex
        });
        console.error(t);
        alert("caught:" + t);
        throw ex;
    }

    // end of dup and link
    // ----------------------------
    Ifx.fn.setMouseFirst = function (bEnabled) {
        this.mouseFirst = bEnabled;
    }
    // --- begin device control
    var deviceX;

    function getDevice() {
        var fn = getTabFn('getDevice');
        deviceX = fn();
    }

    // 柯 新增給 rtn 使用
    Ifx.fn.getifxDevice = function () {
        // 20171225 add
        if (deviceX) {
            return deviceX;
        }
        var fn = getTabFn('getDevice');
        return fn();
    }

    // called by postDisplay
    function deviceInit() {
        console.log(("no test device now"));
        // return;
        // 潘檢核deviceX
        // getDevice();
        // testDevice();
        // add others check here
    }

    function testDevice() {
        try {
            var v = deviceX.version();
            console.log("device activex version:" + v);
        } catch (ee) {
            console.error(ee);
            alert("周邊設備驅動程式有誤 無法繼續運作\n" + ee);
            deviceX = false;
        }
    }

    // end of device control
    // called by postDisplay
    function registerCheckbox() {
        $(".ifx-checkbox").on("click", function (e) {
            var $id = $(this);
            var value = $id.val();
            if (value == "Y") {
                $id.val(""); // 合庫提出不要'N'
            } else {
                $id.val("Y");
            }
            // 若要自動跳到下一欄位 再呼叫nextfield
        });
    }

    Ifx.fn.setCountDownTran = function () {
        this.countDownTran = true;
    }
    Ifx.fn.countDownToZero = function () {
        $('#btn_yn').attr('disabled', true).attr("value", "已逾時");
    }
    // 柯 新增 for ifx-call.js RE countdown
    Ifx.fn.countDownToDefult = function () {
        if ($('#btn_yn').attr("value") == "已逾時") $('#btn_yn').attr('disabled', false).attr("value", "確定傳送");
    }
    // 柯:給IfxUtl.checkCharset 使用
    // 可能需要更好的方式來解決中文型態c轉碼有誤時,不要送出的功能...
    Ifx.fn.setTranEnd = function (tranend, fldname, errortext) {
        console.log("tranend:" + tranend);
        console.log("errortext:" + errortext);
        if (errortext || _self.showErrorText[fldname] != null) {
            _self.tranEnd = tranend;
            if (!tranend) {
                delete _self.showErrorText[fldname];
                if (Object.keys(_self.showErrorText).length != 0) {
                    _self.tranEnd = true;
                }
            } else {
                _self.showErrorText[fldname] = errortext;
            }
        }
    }
    Ifx.fn.watermark = function (arg) {
        // 獲取頁面寬度
        var maxWidth = Math.max(document.body.scrollWidth, window.screen.width) - 20;
        // 獲取頁面高度
        var maxHeight = Math.max(document.body.scrollHeight, document.body.clientHeight) + 100;
        if (maxHeight === 0) {
            console.info("該頁面無敏感內容~");
            return;
        }
        const setting = {
            text: _self.getValue("TLRNO$") + " " + _self.getValue("EMPNM$") + " " + new Date().getHours() + ":" + new Date().getMinutes() + ":" + new Date().getSeconds(),    // 預設水印內容
            beginX: 50,              // 預設起始x座標
            endX: 0,                 // 預設結束x座標
            beginY: 50,              // 預設起始y座標
            endY: 0,                 // 預設結束y座標
            intervalX: 150,          // 預設橫向間隔寬度
            intervalY: 100,          // 預設縱向間隔高度
            opacity: 0.5,            // 字型透明度
            angle: 20,               // 字型傾斜度
            fontsize: '16px',        // 字型大小
            fontFamily: 'Microsoft JhengHei',   // 字型
            width: 270,              // 水印（每個）寬度
            height: 80,              // 水印（每個）高度
            innerDate: true,        // 是否附帶日期
        };
        // 預設變數與自定義變數結合
        if (arguments.length === 1 && typeof arguments[0] === "object") {
            var src = arguments[0] || {};
            for (var key in src) {
                if (!src.hasOwnProperty(key) || !src[key]) {
                    continue;
                }
                for (var def in setting) {
                    if (key === def) {
                        setting[def] = src[key];
                    }
                }
            }
        }
        // 計算列個數
        var cols = parseInt((setting.intervalX + maxWidth - setting.beginX - setting.endX) / (setting.width + setting.intervalX) + "");
        // 計算行個數
        var rows = parseInt((setting.intervalY + maxHeight - setting.beginY - setting.endY) / (setting.height + setting.intervalY) + "");
        // 水印內容附加日期
        if (setting.innerDate) {
            var date = new Date();
            setting.text = [setting.text, " ", date.getFullYear(), "/", date.getMonth() + 1, "/", date.getDate()].join("");
        }
        var fragment = document.createDocumentFragment();
        var x, y;
        for (var i = 0; i < rows; i++) {
            y = setting.beginY + (setting.intervalY + setting.height) * i;
            for (var j = 0; j < cols; j++) {
                x = setting.beginX + (setting.width + setting.intervalX) * j;
                var element = document.createElement('div');
                var oldElement = document.getElementById('watermark' + i + j);
                if (oldElement)
                    oldElement.remove();
                element.id = 'watermark' + i + j;
                // 設定傾斜角
                element.style.MozTransform = "rotate(-" + setting.angle + "deg)";
                element.style.msTransform = "rotate(-" + setting.angle + "deg)";
                element.style.OTransform = "rotate(-" + setting.angle + "deg)";
                element.style.transform = "rotate(-" + setting.angle + "deg)";
                element.style.position = "absolute";
                element.style.left = x + 'px';
                element.style.top = y + 'px';
                element.style.overflow = "hidden";
                element.style.zIndex = "9999";
                element.style.pointerEvents = 'none';
                element.style.opacity = setting.opacity;
                element.style.fontSize = setting.fontsize;
                element.style.fontFamily = setting.fontFamily;
                element.style.color = '#aaa';
                element.style.textAlign = "center";
                element.style.width = setting.width + 'px';
                element.style.height = setting.height + 'px';
                element.style.display = "block";
                element.innerHTML = setting.text;
                fragment.appendChild(element);
            }
        }
        document.body.appendChild(fragment);
    };

    // 柯 重複使用,故獨立.(主管授權之交易畫面,viewJournal)
    function setFkeyandCollect() {
        _self.setValue('FKEY$', 9);
        _self.setValue('#FKEY', 9);
        _self.fkey = 9;
        _self.collect(false, true);
    }

    /*
	 * @param {Object} target 目標對象。 @param {Object} source 源對象。 @param {boolean}
	 * deep 是否複製(繼承)對像中的對象。 @returns {Object} 返回繼承了source對象屬性的新對象。先保留
	 */
    Object.nextend = function (target, /* optional */ source, /* optional */ deep) {
        target = target || {};
        var sType = typeof source,
            i = 1,
            options;
        if (sType === 'undefined' || sType === 'boolean') {
            deep = sType === 'boolean' ? source : false;
            source = target;
            target = this;
        }
        if (typeof source !== 'object' && Object.prototype.toString.call(source) !== '[object Function]') source = {};
        while (i <= 2) {
            options = i === 1 ? target : source;
            if (options != null) {
                for (var name in options) {
                    var src = target[name],
                        copy = options[name];
                    if (target === copy) continue;
                    if (deep && copy && typeof copy === 'object' && !copy.nodeType) target[name] = this.extend(src || (copy.length != null ? [] : {}), copy, deep);
                    else if (copy !== undefined)
                        if (i == 2) {
                            if (copy != "" && copy != ".00") {
                                if (target[name].length < 35) {
                                    target[name] = IfxUtl.substr_big5(target[name], 0, 35, true);
                                }
                                target[name] += copy;
                            }
                        } else {
                            target[name] = copy;
                        }
                }
            }
            i++;
        }
        return target;
    };
    // end of Module Ifx
    return Ifx;
}(jQuery));
var ifxUI = (function($) {


    var CMD_EXPAND = "#<->#",
        CMD_GRID = "#grid#",
        cmds = [CMD_EXPAND, CMD_GRID],
        TABLE_CLASS = "pageTable",
        HIDDEN_ROW = "///__HIDDEN__///",
        GRID_CLASS = "gridRow",
        CAPTION_CLASS = "caption ",
        LABEL_CLASS = "input-label",
        tabIndex = 0,
        labelIndex = 0,
        layout,
        MODE_LANDSCAPE = 'L',
        MODE_PORTRAIT = 'P',
        orientation = MODE_PORTRAIT,
        RE_LABEL_INDEX = /\s\^/,
        workArea = "#tmp",
        bScreenMode = true,
        buddy = [],
        rside = [];
    //鍾 rside for grid整行欄位隱藏時,不會出現空白,可以整行完全隱藏
    //   需將td之padding全清0,及欄位margin-right改為margin-left,
    //   所以比照buddy的作法在表格每格最後一個欄位之margin-right加7px


    function renderScreen(def, fn) {
        var blocks = [];

        buddy = [];
        //鍾 rside for grid整行欄位隱藏時,不會出現空白,可以整行完全隱藏
        rside = [];
        //end
        tabIndex = 0;
        labelIndex = 0;
        layout = getDefaultLayout();

        $.each(def.display, function(i, x) {
            x.visible = true;
            blocks.push(preparePage(x, fn));
        });

        var buf = [],
            r = [],
            caption;
        var windowWidth = $(window).width() - 150;
        $.each(blocks, function(i, block) {

            layout = overrideLayout(layout, block.name, block.def);
            buf.push("<table id='p_" + block.name + "' class='" + TABLE_CLASS  + "'>");
            buf.push(makeCaption(block, layout.cols * 2));

            if (layout.cols == 1) {
                r = renderOneColTable(block, layout);
                //鍾 目前預設是由上而下f再由左至右,
                //   可設定layout.order->1,改為由左至右,再由上而下 ps:合庫是習慣由左至右
            } else if (layout.order == 0) {
                r = renderTwoColsTable_P(block, layout);
            } else {
                r = renderTwoColsTable_L(block, layout);
            }
            //end
            buf = buf.concat(r);

            buf.push("</table>");
        });
        return buf.join("");
    }

    function renderTwoColsTable_P(block) {
        var buf = [],
            widthDef,
            colspan,
            left = [],
            right = [],
            expandCells = [],
            splitSize,
            max,
            b,
            //鍾 為讓INVOKEJS可以讓某個dc裡的某一列隱藏,
            //   所push tr時要再入id == dcname + "_" + r(該列之列數由1開始)
            r = 0,
            // 增加新的排版順序
            //   1             7
            //   2             8
            //   3 expand
            //   4             9
            //   5            10
            //   6            11
            //  12 expand
            //  之後照原排排版順序
            // Tsplit 第一個分割的cells = 6 , 最後一個Lexpand = 12
            Tsplit = 0,
            Lexpand = 0;
        //end

        $.each(block.boxes, function(i, box) {
            if (isExpanded(box)) {
                expandCells.push(i);
            }

            //if(box.label && box.label.match(RE_LABEL_INDEX)) {
            //	box.labelIndex = ++labelIndex;
            //}

        });

        // Tsplit 計算第一個分割的cells數 , 最後一個Lexpand
        if (expandCells.length > 0) {
            Tsplit = block.boxes.length;
            Lexpand = block.boxes.length;
            for (i = expandCells.length - 1; i >= 0; i--) {
                Tsplit = Math.ceil((Tsplit + i + 1) / 2);
                if (expandCells[i] > Tsplit) {
                    if (i > 0)
                        Tsplit = expandCells[i];
                    else {
                        Tsplit = 0;
                        Lexpand = 0;
                    }
                } else {
                    if (i < expandCells.length - 1) Lexpand = expandCells[i + 1];
                    break;
                }
            }
        }
        console.log(" Tsplit " + Tsplit + " Lexpand " + Lexpand);
        //end

        var k = 0;

        $.each(expandCells, function(i, n) {
            // 若在新排版下
            if (n <= Lexpand) {
                splitSize = Tsplit - i;
                if (n < Tsplit) max = n;
                else max = Tsplit;
                // 若在原排版下
            } else {
                splitSize = Math.ceil((n - k) / 2);
                max = k + splitSize;
            }
            //end
            for (; k < max; k++) {
                left.push(block.boxes[k]);
                // 增加新的排版順序
                if (k + splitSize < n || Tsplit > n)
                    //end
                    right.push(block.boxes[k + splitSize]);
                else {
                    left[left.length - 1].cmd = CMD_EXPAND;
                    right.push(null);
                }
            }
            left.push(block.boxes[n]);
            right.push(CMD_EXPAND); // expanded
            k = n + 1;
        });

        // 增加新的排版順序
        if (k < Lexpand) {
            splitSize = Tsplit - expandCells.length;
            max = Tsplit;
            // 若在原排版下
        } else {
            splitSize = Math.ceil((block.boxes.length - k) / 2);
            max = k + splitSize;
        }
        //end

        for (; k < max; k++) {
            left.push(block.boxes[k]);
            right.push(block.boxes[k + splitSize]);

        }



        for (var i = 0; i < left.length; i++) {
            //鍾 為讓INVOKEJS可以讓某個dc裡的某一列隱藏,
            //   所push tr時要再入id == dcname + "_" + r(該列之列數由1開始)
            //			buf.push("<tr>");
            r++;
            buf.push("<tr id='p_" + block.name + "_" + r + "' >");
            //end
            b = left[i];
            widthDef = getLabelWidth(2, 0); // 0 --> left side
            renderOneSide(b, widthDef);

            b = right[i];
            widthDef = getLabelWidth(2, 1); // 1 --> right side
            renderOneSide(b, widthDef);

            buf.push("</tr>");
        }

        return buf;


        function renderOneSide(box, widthDef) {
            if (!box) {
                buf.push("<td style='border-left-width:0px;border-right-width:0px'></td><td style='border-left-width:0px'></td>");
                return;
            }
            if (box == CMD_EXPAND) return;

            colspan = 1;
            if (isExpanded(box)) {
                colspan += 2;
            }
            if (box.label) {
                buf.push(makeLabel(box, widthDef.label));
            } else {
                colspan++;
            }

            buf.push("<td valign='top'  colspan='" + colspan + "' width='" + widthDef.field + "'>");
            if (box.cmd.action == CMD_GRID) {
                buf.push(renderGrid(box.cols, box.cmd.opt));
            } else {
                $.each(box.cols, function(i, c) {
                    buf.push(c);
                });
            }
            buf.push("</td>");
        }
    }

    function renderTwoColsTable_L(block) {

        var buf = [],
            widthDef,
            cells = 0,
            expand = false,
            colspan,
            r = 0;
        //鍾 為讓INVOKEJS可以讓某個dc裡的某一列隱藏,
        //   所push tr時要再入id == dcname + "_" + r(該列之列數由1開始)


        $.each(block.boxes, function(i, box) {
            widthDef = getLabelWidth(2, cells);

            //鍾 為讓INVOKEJS可以讓某個dc裡的某一列隱藏,
            //   所push tr時要再入id == dcname + "_" + r(該列之列數由1開始)
            //			    buf.push("<tr>");
            if (cells % 2 == 0) {
                r++;
                buf.push("<tr id='p_" + block.name + "_" + r + "' >");
            }
            //end

            colspan = 1;
            if (isExpanded(box)) {
                expand = true;
                colspan += 2;
                if (cells % 2 != 0) {
                    // close last tr
                    buf.push("<td colspan='2'></td></tr>");
                    //小柯 更改為此邏輯
                    r++;
                    buf.push("<tr id='p_" + block.name + "_" + r + "' >");
                    //原邏輯
                    //buf.push("<tr>");
                    cells++;
                }
                cells++; // new TR, +1, �]���j��̫�|�A�[1

            }

            if (box.label) {
                //if(box.label.match(RE_LABEL_INDEX))
                //	box.labelIndex = ++labelIndex;

                buf.push(makeLabel(box, widthDef.label));
            } else {
                colspan++;
            }

            buf.push("<td valign='top'  colspan='" + colspan + "' width='" + widthDef.field + "'>");


            if (box.cmd.action == CMD_GRID) {
                buf.push(renderGrid(box.cols, box.cmd.opt));
            } else {
                $.each(box.cols, function(i, c) {
                    buf.push(c);
                });
            }
            buf.push("</td>");


            if (cells % 2 != 0)
                buf.push("</tr>");
            cells++;
        });




        return buf;
    }

    function renderOneColTable(block) {

        var buf = [],
            r = 0;
        //鍾 為讓INVOKEJS可以讓某個dc裡的某一列隱藏,
        //   所push tr時要再入id == dcname + "_" + r(該列之列數由1開始)

        var widthDef = getLabelWidth(1, 0);



        $.each(block.boxes, function(i, box) {
            //鍾 為讓INVOKEJS可以讓某個dc裡的某一列隱藏,
            //   所push tr時要再入id == dcname + "_" + r(該列之列數由1開始)
            //			buf.push("<tr>");
            r++;
            buf.push("<tr id='p_" + block.name + "_" + r + "' >");
            //end

            if (box.label) {
                //if(box.label.match(RE_LABEL_INDEX))
                //	box.labelIndex = ++labelIndex;

                buf.push(makeLabel(box, widthDef.label));
            }
            buf.push("<td valign='top'  colspan='" + (box.label ? 1 : 2) + "' width='" + widthDef.field + "'>");

            if (box.cmd.action == CMD_GRID) {
                buf.push("<div class='tdSc'>")
                buf.push(renderGrid(box.cols, box.cmd.opt));
                buf.push("</div>")
            } else {
                $.each(box.cols, function(i, c) {
                    buf.push(c);
                });
            }
            buf.push("</td>");

            buf.push("</tr>");
        });




        return buf;
    }

    function renderGrid(cols, opt) {
        var buf = [],
            tblStarted = false,
            w = 0;
        // 鍾 為讓表格式一列太長時可訂成2列,  ps:如果在td加</br>表示訂成2列,因為有</br>則無法將該表列隱藏
        //    第二列要讀取欄位getColWidth時需再歸0

        function getColWidth(colIndex) {
            var t;
            if (opt && opt["s_cols"]) {
                t = opt["s_cols"][colIndex];
            }
            return parseInt(t, 10);
        }

        // 鍾 為讓INVOKEJS可以讓某個grid 或是 grid裡的某一列隱藏
        //    需在grid tabel,及 grid tr 加入id
        var grdid, trnm, trnms, r;
        if (opt["id"])
            grdid = " id='p_grd" + opt["id"] + "' ";
        else
            grdid = "";
        //end

        $.each(cols, function(i, x) {

            // 鍾 為讓INVOKEJS可以讓某個grid 或是 grid裡的某一列隱藏
            //    需在grid tabel,及 grid tr 加入id
            if (opt["id"] && i > 1) {
                r = i - 1;
                trnm = " id='p_grd" + opt["id"] + "_" + r + "' ";
                trnms = " id='p_grd" + opt["id"] + "_s" + r + "' ";
            } else {
                trnm = "";
                trnms = "";
            }
            //end

            if ($.isArray(x)) {
                if (!tblStarted) {
                    tblStarted = true;
                    // 鍾 為讓INVOKEJS可以讓某個grid 或是 grid裡的某一列隱藏
                    //    需在grid tabel,及 grid tr 加入id
                    buf.push("<table class='" + GRID_CLASS + "'" + grdid + ">");
                    //柯 新增 左右對齊 置中
                    if (opt["self_align"]) {
                        if (opt["self_align"] == "left") {
                            buf.push("<tr class='gray gray-left'>");
                        } else if (opt["self_align"] == "right") {
                            buf.push("<tr class='gray gray-right'>");
                        }
                    } else {
                        buf.push("<tr class='gray gray-center'>");
                    }
                } else {
                    buf.push("<tr " + trnm + " >");

                }
                //end
                w = 0;
                $.each(x, function(j, y) {
                    //鍾 表格第二列push tr 及 grid tr 加入id,w歸0,重新getColWidth
                    if (y == "<br/>" && i > 1) {
                        buf.push("</tr>");
                        buf.push("<tr " + trnms + " >");
                        w = 0;
                    } else {
                        var width = getColWidth(w++ % (x.length));
                        //end
                        if (isNaN(width)) {
                            buf.push("<td>");
                        } else {
                            buf.push("<td width='" + width + "'>");
                        }
                        if ($.isArray(y)) {
                            $.each(y, function(k, z) {
                                //鍾 表格title可以用變數,可變動
                                if (i <= 1) z = z.replace("field_label", "grid_label");
                                //end
                                buf.push(z);
                            });
                        } else {
                            //鍾 表格title可以用變數,可變動
                            if (i <= 1) y = y.replace("field_label", "grid_label");
                            //end
                            buf.push(y);
                        }
                        buf.push("</td>");
                        //鍾 表格第二列push tr 及 grid tr 加入id,w歸0,重新getColWidth
                    }
                    //end
                });

                buf.push("</tr>");

            } else {
                if (tblStarted) {
                    buf.push("</table>");
                    tblStarted = false;
                }
                buf.push(x);
            }
        });
        if (tblStarted) {
            buf.push("</table>");
            tblStarted = false;
        }

        return buf.join("");
    }

    function makeLabel(box, width) {
        var s = box.label;
        //		if(s.match(RE_LABEL_INDEX)) {
        //			var t=box.labelIndex.toString();
        //			if(t.length==1) t = "&nbsp;" + t;
        //			s=s.replace(RE_LABEL_INDEX, t);
        //		}
        return "<td class='" + LABEL_CLASS + "' width='" + width + "' valign='top' align='left'>" + s + "</td>";


    }

    function makeCaption(block, colspan) {

        if (block.caption) {
            return "<tr class='" + CAPTION_CLASS + "'><td colspan='" + colspan + "'><pre>" + block.caption + "</pre><hr class='style-two' /></td></tr>";
        } else {
            return "";
        }
    }

    function replaceLabelIndex(s) {
        if (s.match(RE_LABEL_INDEX)) {
            labelIndex++;
            var t = labelIndex.toString();
            if (t.length == 1) t = (bScreenMode ? '&nbsp;' : ' ') + t;
            s = s.replace(RE_LABEL_INDEX, t);
        }
        return s;
    }

    //鍾 &nbsp; 在screen print 時取代為space
    function replaceSpace(s) {
        if (s.match("&nbsp") && !bScreenMode) {
            s = s.replace(/&nbsp/g, " "); //柯 null->space
        }
        return s;
    }
    //end

    function preparePage(p, fn) {
        var boxes = [],
            //lines = p.def.list.slice(0),
            lines = $.extend(true, [], p.def.list),
            caption = lines.shift(),
            cmd, label,
            currLine = 0;

        $.each(lines, function(r, line) {
            cmd = "";
            label = line.shift();

            var gridLoopTimes = 0;
            if (isCmd(label)) {
                cmd = parseCmd(label);
                label = line.shift();
                if (cmd.action == CMD_GRID) {
                    gridLoopTimes = cmd.opt.loop;
                }
            }
            while (label.match(RE_LABEL_INDEX)) {
                label = replaceLabelIndex(label);
            }

            // 鍾 label 可以用變數代入,而非固定值
            var i = label.indexOf("#");
            if (i > -1)
                label = label.substr(0, i) + renderElement(fn, label.substr(i), 3);
            else
                label = replaceSpace(label);
            //end

            var cols = [],
                bCaptionRendered = false,
                origElement;

            $.each(line, function(c, element) {
                if ($.isArray(element)) {
                    if (gridLoopTimes > 0 && bCaptionRendered) { // field row with loop
                        origElement = $.extend(true, [], element);
                        console.log("loop:" + gridLoopTimes);
                        console.log(origElement);

                        for (var z = 0; z < gridLoopTimes; z++) {

                            element = copyFieldRow(origElement, z == 0 ? 0 : 1); // first row add 0, else add 1
                            console.log("%s:%s", (z + 1), element);
                            // 鍾 表格才需push rside[]
                            element = renderElement(fn, element, 1);
                            //end
                            cols.push(element); // caption row, or no loop
                        }
                    } else {
                        cols.push(renderElement(fn, element)); // caption row, or no loop
                        bCaptionRendered = true;
                    }


                } else {
                    cols.push(renderElement(fn, element));
                }

            });

            boxes.push({
                cmd: cmd,
                label: label,
                cols: cols
            });
        });


        return {
            caption: caption,
            boxes: boxes,
            name: p.name,
            def: p.def,
            hideRows: p.hideRows || [],
            hideGirdRows: []
        };
    }

    function copyFieldRow(array, n) {
        //var re=/(#\w+?)(\d{1,})/; //#TB_15, or #TC17
        var re = /(#\w+?)(\d+$)/g; //#TB_15, or #TC17

        function advanceRow(c) {
            c = c.replace(re, function(i, j, k) {
                // if match then i=#TB_15, j=#TB_, k=15
                return j + (parseInt(k, 10) + n); // #TB_ +  (15 + loopIndex).toString();
            });
            return c;
        }

        return (function iterate(arr) {
            $.each(arr, function(i, x) {
                if ($.isArray(x)) {
                    arr[i] = iterate(x);
                } else {
                    arr[i] = advanceRow(x);
                }
            });
            return arr;
        })(array);
    }

    // 鍾 push rside[]
    // adjfg==1 grid 1 row
    // adjfg==2 grid 2 row
    // adjfg==3 label
    function renderElement(fn, x, adjfg) {
        var buf = [],
            ss, adjfg2;
        //end
        if ($.isArray(x)) { // multiple item in one grid cell
            var colsInCell = [];
            $.each(x, function(i, z) {
                // 鍾 push rside[]
                adjfg2 = adjfg;
                if (adjfg == 1) {
                    if ($.isArray(z)) adjfg2 = 2;
                    else adjfg2 = 1;
                } else {
                    if (i == x.length - 1 || $.isArray(z))
                        adjfg2 = (adjfg == 2) ? 1 : 0;
                }
                colsInCell.push(renderElement(fn, z, adjfg2));
                //end
            });
            return colsInCell;
        } else {
            // 鍾 label 可代入變數名稱
            if (x.substr(0, 4) == " ^ #") {
                x = replaceLabelIndex(x);
                var i = x.indexOf("#");
                return x.substr(0, i) + fn(tabIndex++, x.substr(i), false);
            } else if (x.match(/#/)) {
                //end
            	if (x.match(/\+/g)) {
                    ss = x.split('+');
                    _.each(ss, function(y) {
                        if (y.indexOf("#") == -1)
                            buf.push(y);
                        else
                            buf.push(fn(tabIndex++, y, true));
                    });
                    // 鍾 push rside ,label push buddy,欄位右邊補空改左邊,
                    //	ss.pop();
                    if (adjfg == 3)
                      if(ss[0].substring(0,1) == "#")
                      	buddy.push(ss[0]);
                    ss.shift();
                    if(ss[0].substring(0,1) == "#")
                    	buddy.push(ss);
                    if (adjfg == 1) rside.push(ss[ss.length - 1]);
                    //end
                    return buf.join("");
                } else {
                    // 鍾 push rside ,label push buddy
                    if (adjfg == 3)
                    	if(x.substring(0,1) == "#")
                    	  buddy.push(x);
                    if (adjfg == 1)
                    	if(x.substring(0,1) == "#")
                    		rside.push(x);
                    //end
                    return fn(tabIndex++, x, false);
                }

            } else {
                // 鍾 screen print &nbsp;轉成空白
                x = replaceSpace(x);
                //end
                return replaceLabelIndex(x);
            }
        }
    }

    function isCmd(x) {
        return startsWithdArray(cmds, x) != -1;
    }

    function isGird(box) {
        return box.cmd.action == CMD_GRID;
    }

    function isExpanded(box) {
        return (box.cmd.action == CMD_EXPAND || (box.cmd.action == CMD_GRID && box.cmd.opt.expand));
    }

    function startsWithdArray(arr, k) {
        for (var i = 0; i < arr.length; i++) {
            if (k.indexOf(arr[i]) != -1) return i;
        }
        return -1;
    }

    function parseCmd(x) {
        var action = "",
            opt = {};

        if (x.indexOf(CMD_EXPAND) == 0) {
            action = CMD_EXPAND;
            opt = null;
        } else if (x.indexOf(CMD_GRID) == 0) {
            action = CMD_GRID;
            var ss = x.split(",");
            if (ss.length > 1) {
                ss.shift();
                var t = ss.join(",");
                opt = eval('(' + t + ')');
            }
        }
        return {
            action: action,
            opt: opt
        };
    }


    // --------------------------------------------------
    // Print
    // ----------------------------------------------------
    //是否要規定br寫法?
    var NEWLINE = "\n",
        FORMFEED = "\f",
        RE_BR = /<br\/>/g;

    function resetWorkArea() {
        $(workArea).empty();
    }

    function createPre(id) {
        $(workArea).append("<pre></pre>").id(id);
    }
    //第三個參數for產生規格書使用
    function printScreen(def, fn, withvisible) {
    	  console.log("printscreen fn=" + fn);
        var blocks = [];
        tabIndex = 0;
        labelIndex = 0;
        layout = getDefaultLayout();

        bScreenMode = false;
        $.each(def.display, function(i, x) {
            if (x.visible || withvisible) {
                blocks.push(preparePage(x, fn));
            }
        });

        var buf = [],
            r = [],
            caption;
        var fnIsHide;
        $.each(blocks, function(i, block) {
            if (i != 0)
                buf.push("");

            layout = overrideLayout(layout, block.name, block.def);

            // put caption
            //		buf.push(NEWLINE);
            if (block.caption) buf.push(block.caption + NEWLINE);


            fnIsHide = isHideRow(block.hideRows);

            if (layout.cols == 1) {
                r = printOneColTable(block, fnIsHide);
                // 鍾 設定layout.order->1,改為由左至右
            } else if (layout.order == 0) {
                r = printTwoColsTable_P(block, fnIsHide);
            } else {
                r = printTwoColsTable_H(block, fnIsHide);
            }
            //end
            buf = buf.concat(r);

        });

        //小柯 查找 HIDDEN 符號並RETURN掉，傳調整後的BUF2
        var buf2 = []
        $.each(buf, function(i, x) {
            if (x == HIDDEN_ROW) {
                return; //此列被隱藏  不列印
            }
            buf2.push(x);
        });
        return buf2.join(NEWLINE); //最後一行
    }

    function sliceString(s, width) {
        if (IfxUtl.strlen(s) < width) return s;

        var r = [];
        var t;
        var len = IfxUtl.strlen(s);
        for (var i = 0; i < len; i += width) {
            t = IfxUtl.substr_big5(s, i, width);
            r.push(t);
        }
        return r.join(NEWLINE);
    }

    function printLabel(box, width) {
        var s = box.label,
            colon = "";

        if (layout.printer.colon) {
            colon = ":";
        }

        //	if(s.match(RE_LABEL_INDEX)) {
        //		var t=box.labelIndex.toString();
        //		if(t.length<2) t = " " + t;
        //		s=s.replace(RE_LABEL_INDEX, t);
        //	}
        s = s.replace(RE_BR, NEWLINE);

        if ($.trim(s).length == 0) colon = ' '; // empty label, empty colon added by kntsai 2013/08/01,

        var ss = s.split(NEWLINE);
        var t = [];


        $.each(ss, function(i, x) {
            if (IfxUtl.strlen(x) < width) { // last line of label
                x = IfxUtl.stringFormatterBig5(x, width - colon.length);
                t.push(x + colon);
            } else {
                t.push(x + colon);
            }

        });

        return t;
    }


    function printTwoColsTable_P(block, fnIsHide) {
        var expandCells = [],
            k, i,
            leftSide = [], // left side boxes
            rightSide = []; // right side boxes

        // find expanded and advance labelIndex
        $.each(block.boxes, function(i, box) {
            if (isExpanded(box)) {
                expandCells.push(i);
            }
        });


        k = 0; // first box

        var center;
        $.each(expandCells, function(ignore1, x) {
            // 鍾 左右排版中間遇到展開一列時,應該先扣除已排之欄位後,再除2,不然會出BUG!! leftSide'length != rightSide's length
            center = Math.ceil((x - k) / 2);
            //end
            for (i = 0; k < x; k++, i++) {
                if (i < center) {
                    leftSide.push(block.boxes[k]);
                } else {
                    rightSide.push(block.boxes[k]);
                }
            }
            if (leftSide.length === rightSide.length + 1) {
                rightSide.push(null);
            }
            console.log("1 leftSide:" + leftSide.length + ", rightSide:" + rightSide.length);

            leftSide.push(block.boxes[k++]); // put expanded box to left side;
            rightSide.push(null); // put an empty box to right for balance
        });

        center = Math.ceil((block.boxes.length - k) / 2);
        for (i = 0; k < block.boxes.length; k++, i++) {
            if (i < center) {
                leftSide.push(block.boxes[k]);
            } else {
                rightSide.push(block.boxes[k]);
            }
        }
        if (leftSide.length === rightSide.length + 1) {
            rightSide.push(null);
        }

        console.log("2leftSide:" + leftSide.length + ", rightSide:" + rightSide.length);

        //	if(leftSide.length!=rightSide.length) {
        //		alert("BUG!! leftSide'length != rightSide's length");
        //		return;
        //	}

        var widthDefLeft = getPrintWidth(2, 0); // get left side layout
        var widthDefRight = getPrintWidth(2, 1); // get right side layout
        var leftSideWidth = widthDefLeft.labelWidth + widthDefLeft.fieldWidth;
        var expandedFieldWidth = widthDefLeft.fieldWidth + widthDefRight.labelWidth + widthDefRight.fieldWidth;
        var leftBuf, rightBuf, buf = [];

        var combinedBox = $.map(leftSide, function(leftBox, i) {

            if (fnIsHide(i)) return HIDDEN_ROW; // the row is hidden, continue next row


            buf = []; //reset buf
            // left box
            console.log("mapping " + i + " times");
            leftBuf = printOneBox(leftBox, widthDefLeft.labelWidth, isExpanded(leftBox) ? expandedFieldWidth : widthDefLeft.fieldWidth);
            rightBuf = rightSide[i] == null ? [] : printOneBox(rightSide[i], widthDefRight.labelWidth, widthDefRight.fieldWidth);

            if (!isExpanded(leftBox)) { // not expand box, padding text to width of box(label+field)
                leftBuf = $.map(leftBuf, function(x) {
                    return IfxUtl.stringFormatterBig5(x, leftSideWidth);
                });
                $.each(leftBuf, function(i, tt) {
                    console.log(IfxUtl.strlen(tt) + ":[" + tt + "]")
                });
            }
            console.log("leftBuf len:" + leftBuf.length + ", rightBuf len:" + rightBuf.length);
            while (leftBuf.length < rightBuf.length) leftBuf.push(IfxUtl.stringFormatterBig5("", leftSideWidth));
            while (rightBuf.length < leftBuf.length) rightBuf.push("");
            console.log("==>leftBuf len:" + leftBuf.length + ", rightBuf len:" + rightBuf.length);
            // combine left and right
            $.each(leftBuf, function(i, x) {
                buf.push(x + " " + rightBuf[i]);
            });
            return buf;
        });


        // flattern combinedBox
        return $.map(combinedBox, function(x) {
            return x;
        });


    }

    function printTwoColsTable_H(block, fnIsHide) {
        var expandCells = [],
            k, i,
            leftSide = [], // left side boxes
            rightSide = []; // right side boxes

        // find expanded and advance labelIndex
        $.each(block.boxes, function(i, box) {
            if (isExpanded(box)) {
                expandCells.push(i);
            }
        });


        k = 0; // first box
        var putLeft = true; // start from left side

        // helper method for putting box
        function easyPut(box) {
            if (putLeft) {
                leftSide.push(box);
            } else {
                rightSide.push(box);
            }
            putLeft = !putLeft;
        }

        $.each(expandCells, function(ignore1, x) {
            for (; k < x; k++) {
                easyPut(block.boxes[k]);
            }
            if (!putLeft) easyPut(null); // put an empty box to rightSide for balance
            console.log("1 leftSide:" + leftSide.length + ", rightSide:" + rightSide.length);

            leftSide.push(block.boxes[k++]); // put expanded box to left side;
            rightSide.push(null); // put an empty box to right for balance
            putLeft = true; // reset to left side
        });

        for (; k < block.boxes.length; k++) {
            easyPut(block.boxes[k]);
        }
        if (!putLeft) easyPut(null); // put an empty box to rightSide for balance

        console.log("2leftSide:" + leftSide.length + ", rightSide:" + rightSide.length);

        if (leftSide.length != rightSide.length) {
            alert("BUG!! leftSide'length != rightSide's length");
            return;
        }

        var widthDefLeft = getPrintWidth(2, 0); // get left side layout
        var widthDefRight = getPrintWidth(2, 1); // get right side layout
        var leftSideWidth = widthDefLeft.labelWidth + widthDefLeft.fieldWidth;
        var expandedFieldWidth = widthDefLeft.fieldWidth + widthDefRight.labelWidth + widthDefRight.fieldWidth;
        var leftBuf, rightBuf, buf = [];

        var combinedBox = $.map(leftSide, function(leftBox, i) {

            //小柯  列印顯示true 改成字串 並在 printScreen 移除
            if (fnIsHide(i)) return HIDDEN_ROW; // the row is hidden, continue next row

            buf = []; //reset buf
            // left box
            console.log("mapping " + i + " times");
            leftBuf = printOneBox(leftBox, widthDefLeft.labelWidth, isExpanded(leftBox) ? expandedFieldWidth : widthDefLeft.fieldWidth);
            rightBuf = rightSide[i] == null ? [] :
                printOneBox(rightSide[i], widthDefRight.labelWidth, widthDefRight.fieldWidth);

            if (!isExpanded(leftBox)) { // not expand box, padding text to width of box(label+field)
                leftBuf = $.map(leftBuf, function(x) {
                    return IfxUtl.stringFormatterBig5(x, leftSideWidth);
                });
                $.each(leftBuf, function(i, tt) {
                    console.log(IfxUtl.strlen(tt) + ":[" + tt + "]")
                });
            }
            console.log("leftBuf len:" + leftBuf.length + ", rightBuf len:" + rightBuf.length);
            while (leftBuf.length < rightBuf.length) leftBuf.push(IfxUtl.stringFormatterBig5("", leftSideWidth));
            while (rightBuf.length < leftBuf.length) rightBuf.push("");
            console.log("==>leftBuf len:" + leftBuf.length + ", rightBuf len:" + rightBuf.length);
            // combine left and right
            $.each(leftBuf, function(i, x) {
                buf.push(x + " " + rightBuf[i]);
            });
            return buf;
        });


        // flattern combinedBox
        return $.map(combinedBox, function(x) {
            return x;
        });


    }

    function isHideRow(hideRows) {
        return function(r) {
            return (hideRows.indexOf(r + 1) != -1);
        };
    }

    function printOneColTable(block, fnIsHide) {
        var buf = [];
        var fieldBuf = [];
        var labelBuf;
        var widthDef = getPrintWidth(1, 0); // one column, first column
        var fieldWidth;


        $.each(block.boxes, function(i, box) {

            if (fnIsHide(i)) return HIDDEN_ROW; // the row is hidden, continue next row

            buf = buf.concat(printOneBox(box, widthDef.labelWidth, widthDef.fieldWidth));
        });

        return buf;
    }

    function printOneBox(box, labelWidth, fieldWidth) {
        var buf = [], // for whole box
            labelBuf = [], // for label part
            fieldBuf = []; // for field part

        // 鍾 表格列印當資料超過長度時,加印至第二列,不是刪除
        var filedTWidth = 0;
        //end

        // 鍾 screen print label
        //	if (box.label.length == 1) return ""; // 先變註解 20130801 kntsai
        //end

        if (box.label) {
            //		if(box.label.match(RE_LABEL_INDEX)) {
            //			box.labelIndex = ++labelIndex;
            //		}
            labelBuf = printLabel(box, labelWidth);
        } else {
            // no lable, expand field width
            fieldWidth += labelWidth;
        }

        if (box.cmd.action == CMD_GRID) {
            fieldBuf.push(printGrid(box.cols, box.cmd.opt));
        } else {
            $.each(box.cols, function(i, c) {

                // 鍾 表格列印當資料超過長度時,加印至第二列,不是刪除
                if (c.indexOf("<br/>") != -1) {
                    c = c.replace(RE_BR, NEWLINE);
                    filedTWidth = 0;
                } else {
                    filedTWidth += IfxUtl.strlen(c);
                    if (filedTWidth > fieldWidth && i != 0) {
                        fieldBuf.push(NEWLINE);
                        filedTWidth = IfxUtl.strlen(c);
                    }
                }
                //fieldBuf.push(c + " ");
                if (c != "" && c != " ")
                    //end
                    fieldBuf.push(c); // gap had been placed at renderElement(callback to ifx-core2.js)
            });
        }

        fieldBuf = fieldBuf.join("").split(NEWLINE);

        // combine label with field
        var t;
        if (box.label) {
            var max = Math.max(labelBuf.length, fieldBuf.length); //�ݽ֤���
            for (var i = 0; i < max; i++) {
                // �p�Glabel�L�u(�Φ�Ƥ���), �ɺ����
                t = labelBuf[i] ? labelBuf[i] : IfxUtl.stringFormatterBig5("", labelWidth);
                if (fieldBuf[i]) {
                    t += " " + (fieldBuf[i]);
                }
                buf.push(t);
            }
        } else {
            for (var i = 0; i < fieldBuf.length; i++) {
                buf.push(fieldBuf[i] + " ");
            }
        }

        return buf;

    }

    function printGrid(cols, opt) {
        var buf = [],
            colsWidth = [],
            gridStarted = false,
            delimeterAdded = false,
            rowHeight = opt["row_height"] || 1;

        // filter hidden grid row
        if (opt.id) {
            var gPrefix = "grd" + opt.id + "_";
            var colsNew = [];
            var kk = 0;
            $.each(cols, function(i, x) {
                if ($.isArray(x)) {
                    if (!isHiddenGridRow(gPrefix + (kk))) {
                        colsNew.push(x);
                    }
                    kk++;
                }
            });
            cols = colsNew;
        }

        function getUserDefineColWidth(colIndex) {
            if (opt && opt["p_cols"]) {
                var t = opt["p_cols"][colIndex];
                return parseInt(t, 10);
            }
            return null;
        }

        $.each(cols, function(i, x) {
            if ($.isArray(x)) {
                $.each(x, function(j, y) {
                    if (!colsWidth[j]) colsWidth[j] = 0;
                    if ($.isArray(y)) {
                        $.each(y, function(k, z) {
                            colsWidth[j] = Math.max(colsWidth[j], IfxUtl.strlen(z));
                        });
                    } else {
                        colsWidth[j] = Math.max(colsWidth[j], IfxUtl.strlen(y));
                    }
                });
            };
        });

        var widthBefoerMe = [0],
            widthNow = 0;
        $.each(colsWidth, function(i, w) {
            widthNow += w;
            if (i > 0) {
                widthBefoerMe.push(widthNow);
            }
        });


        var deli = " ";
        $.each(colsWidth, function(i, x) {
            var w = getUserDefineColWidth(i);
            if (!isNaN(w)) {
                colsWidth[i] = Math.max(w, colsWidth[i]);
                // 鍾 表格線改用 =
                deli += new Array(colsWidth[i]).join("=") + " ";
                //end
            };
        });

        var gridLine = [];
        var max = 0;
        var cellContent = "";
        // 鍾 表格列印超過長度,改印至第二行,cellmax為一列實際的格數
        // ex : 二列四格的表格,  cellmax = 8, colsWidth.length = 4
        var cellmax = 0;
        //end
        //buf.push(NEWLINE);
        $.each(cols, function(i, x) {
            if ($.isArray(x)) { // array item means start of grid

                gridStarted = true;
                gridLine = [];
                cellmax = 0;
                buf.push(NEWLINE);
                $.each(x, function(j, y) {
                    if ($.isArray(y)) {
                        cellContent = "";
                        $.each(y, function(k, z) {
                            // 鍾 表格列欄位隱藏時不要列印
                            z = z.trim();
                            if (z.length > 0) {
                                cellContent += (z);
                                if (z != "<br/>") cellContent += " ";
                            }
                            // end
                        });

                    } else {
                        // 鍾 表格二行式時,換行記號不是真的換行
                        if (y == "<br/>") y = "<br//>";
                        // end
                        cellContent = y;

                    }

                    // 鍾 計算每一列的實際格子數
                    cellmax++;
                    //end
                    gridLine.push(cellContent.replace(RE_BR, NEWLINE).split(NEWLINE));
                });

                var max = _.reduce(gridLine, function(m, x) {
                    return Math.max(m, x.length);
                }, 0);
                var ele;

                for (var y = 0; y < max; y++) {
                    cellContent = "";
                    // 鍾 處理二行式表格,將第一行資料存至 tele
                    var tele = "";
                    var telefg = 0;
                    var wi = 0;
                    for (var x = 0; x < cellmax; x++, wi++) {
                        if (typeof(gridLine[x][y]) == "undefined") {
                            cellContent += IfxUtl.stringFormatterBig5("", colsWidth[x]);
                        } else {
                            ele = gridLine[x][y];
                            if (ele == "<br//>") {
                                buf.push(" " + cellContent + " ");
                                if (telefg == 1) {
                                    buf.push(NEWLINE);
                                    buf.push(" " + tele + " ");
                                }
                                buf.push(NEWLINE);
                                cellContent = "";
                                tele = "";
                                telefg = 0;
                                wi = -1;
                            } else {
                                var s = IfxUtl.stringFormatterBig5(
                                    ele ? ele : "", colsWidth[wi]);
                                cellContent += s;
                                if (IfxUtl.strlen(ele) > colsWidth[wi]) {
                                    var l = ele.substr(s.length);
                                    tele += IfxUtl.stringFormatterBig5(ele.substr(s.length), colsWidth[wi]);
                                    telefg = 1;
                                } else {
                                    tele += IfxUtl.stringFormatterBig5("", colsWidth[wi]);
                                }
                            }
                        }
                    }
                    //end
                    buf.push(" " + cellContent + " "); //fd
                    // 鍾 如該表格有印第二列時,先push tele
                    if (telefg == 1) {
                        buf.push(NEWLINE);
                        buf.push(" " + tele + " ");
                    }
                    //end
                    if (y + 1 < max) buf.push(NEWLINE);
                }
                //cellContent=IfxUtl.stringFormatterBig5(cellContent, colsWidth[j]);
                //buf.push(" "+cellContent + " ");//fd



                // underline for grid column title
                if (!delimeterAdded) {
                    buf.push(NEWLINE);
                    buf.push(deli)
                    delimeterAdded = true;
                } else {
                    while (max++ < rowHeight)
                        buf.push(NEWLINE);

                }


            } else {
                // text description outside grid
                if (gridStarted) {
                    buf.push(NEWLINE);
                    gridStarted = false;
                }
                buf.push(x.replace(RE_BR, NEWLINE) + " ");
            }

        });
        buf.push(NEWLINE);
        return buf.join("");
    }
    // ------------------------------------------
    // layout
    // -------------------------------------------
    function getDefaultLayout() {

        var _layout = {
            cols: 1,
            screen: {
                width: [160, 400, 160, 400],
                left: 0
            },
            screen_2: {
                width: [160, 400, 160, 400],
                left: 0
            },
            printer: { // one column
                width: [20, 40],
                top: 2,
                left: 5,
                gap: 1,
                colon: true,
                linesPerPage: 66

            },
            printer_2: { // two columns
                width: [20, 20, 20, 20],
                top: 2,
                left: 3,
                gap: 1,
                colon: true,
                linesPerPage: 66

            },
            // 鍾 可設定layout.order->1,改為由左至右
            order: 0
            //end
        };
        return _layout;
    }

    function overrideLayout(layout, name, d) {
        try {

            if (d.layout) {
                // two column must be override
                var cols = 1;
                var r = d.layout.match(/cols\s*=\s*(\d)/);
                if (r != null) {
                    cols = r[1];
                }
                if (cols != 1) { // change  layout to printer_2
                    layout.screen = layout.screen_2;
                    layout.printer = layout.printer_2;
                }

                var cc = d.layout.split(/\s*;\s*/);
                $.each(cc, function(i, x) {
                    if (x) {
                        var o = layout;
                        var ss = x.split(/\s*=\s*/);

                        if (ss[0] == "cols" && ss[1] == "1") {
                            layout.screen.left = 70;
                        }
                        var aa = ss[0].split(".");

                        $.each(aa, function(i, y) {

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
        } catch (ex) {
            alert("layout override error, please check dc name:[" + name + "]\n" + ex);
            return layout;
        }
    }

    function getLabelWidth(cols, i) {
        var offset = 0;
        if (cols > 1 && i % 2 != 0) {
            offset = 2;
        }
        return {
            label: layout.screen.width[offset],
            field: layout.screen.width[offset + 1]
        };
    }

    function getPrintWidth(cols, i) {
        var offset = 0;
        if (cols > 1 && i % 2 != 0) {
            offset = 2;
        }
        return {
            labelWidth: parseInt(layout.printer.width[offset], 10),
            fieldWidth: parseInt(layout.printer.width[offset + 1], 10)
        };
    }

    function getBuddy() {
        return _.flatten(buddy);
    }
    // 鍾 push rside
    function getRside() {
        return _.flatten(rside);
    }
    //end

    var hideGridRows = [];

    function isHiddenGridRow(rowId) {
        return hideGridRows.indexOf(rowId) != -1;
    }

    function appendHideGridRow(name, show) {
        //
        var i = hideGridRows.indexOf(name);
        if (i != -1) {
            hideGridRows.splice(i, 1); // remove this one
        }

        if (show == "0") {
            // append to array
            hideGridRows.push(name);
        }
    }
    return {
        renderScreen: renderScreen,
        printScreen: printScreen,
        // 鍾 push rside
        getBuddy: getBuddy,
        getRside: getRside,
        // end
        hideGridRow: appendHideGridRow
    };
})(jQuery);
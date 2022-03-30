var XLS = {};
(function make_xls(cT) {
    cT.version = "0.7.2";
    var e1 = 1252,
        ih;
    if (typeof module !== "undefined" && typeof require !== "undefined") {
        if (typeof cptable === "undefined") {
            cptable = require("./dist/cpexcel")
        }
        ih = cptable[e1]
    }

    function fC() {
        ev(1252)
    }

    function ev(fs) {
        e1 = fs;
        if (typeof cptable !== "undefined") {
            ih = cptable[fs]
        }
    }
    var fF = function hx(fs) {
        return String.fromCharCode(fs)
    };
    if (typeof cptable !== "undefined") {
        fF = function hw(fs) {
            if (e1 === 1200) {
                return String.fromCharCode(fs)
            }
            return cptable.utils.decode(e1, [fs & 255, fs >> 8])[0]
        }
    }
    var I = (typeof Buffer !== "undefined");

    function dr(fs) {
        return new(I ? Buffer : Array)(fs)
    }
    var bm = (function fz() {
        var kQ = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
        return {
            decode: function fs(k1, k0) {
                var kR = "";
                var kV, kT, kS;
                var kZ, kY, kX, kW;
                k1 = k1.replace(/[^A-Za-z0-9\+\/\=]/g, "");
                for (var kU = 0; kU < k1.length;) {
                    kZ = kQ.indexOf(k1.charAt(kU++));
                    kY = kQ.indexOf(k1.charAt(kU++));
                    kX = kQ.indexOf(k1.charAt(kU++));
                    kW = kQ.indexOf(k1.charAt(kU++));
                    kV = kZ << 2 | kY >> 4;
                    kT = (kY & 15) << 4 | kX >> 2;
                    kS = (kX & 3) << 6 | kW;
                    kR += String.fromCharCode(kV);
                    if (kX != 64) {
                        kR += String.fromCharCode(kT)
                    }
                    if (kW != 64) {
                        kR += String.fromCharCode(kS)
                    }
                }
                return kR
            }
        }
    })();

    function jX(kQ) {
        if (I) {
            return new Buffer(kQ, "binary")
        }
        var fs = kQ.split("").map(function (kR) {
            return kR.charCodeAt(0) & 255
        });
        return fs
    }

    function hn(kS, k0, kT, fs, kV) {
        if (kT === undefined) {
            kT = true
        }
        if (!fs) {
            fs = 8
        }
        if (!kV && fs === 8) {
            kV = 52
        }
        var kY, kU, kR = fs * 8 - kV - 1,
            kX = (1 << kR) - 1,
            kQ = kX >> 1;
        var k1 = -7,
            kZ = kT ? -1 : 1,
            kW = kT ? (fs - 1) : 0,
            k2 = kS[k0 + kW];
        kW += kZ;
        kY = k2 & ((1 << (-k1)) - 1);
        k2 >>>= (-k1);
        k1 += kR;
        for (; k1 > 0; kY = kY * 256 + kS[k0 + kW], kW += kZ, k1 -= 8) {}
        kU = kY & ((1 << (-k1)) - 1);
        kY >>>= (-k1);
        k1 += kV;
        for (; k1 > 0; kU = kU * 256 + kS[k0 + kW], kW += kZ, k1 -= 8) {}
        if (kY === kX) {
            return kU ? NaN : ((k2 ? -1 : 1) * Infinity)
        } else {
            if (kY === 0) {
                kY = 1 - kQ
            } else {
                kU = kU + Math.pow(2, kV);
                kY = kY - kQ
            }
        }
        return (k2 ? -1 : 1) * kU * Math.pow(2, kY - kV)
    }
    var a8 = /\u0000/g,
        a7 = /[\u0001-\u0006]/;
    var fJ, w;
    fJ = w = function hc(kR) {
        var fs = [];
        for (var kQ = 0; kQ < kR[0].length; ++kQ) {
            fs.push.apply(fs, kR[0][kQ])
        }
        return fs
    };
    var iX, ac;
    iX = ac = function jA(fs, kS, kT) {
        var kR = [];
        for (var kQ = kS; kQ < kT; kQ += 2) {
            kR.push(String.fromCharCode(eS(fs, kQ)))
        }
        return kR.join("")
    };
    var a, co;
    a = co = function bM(fs, kR, kQ) {
        return fs.slice(kR, (kR + kQ)).map(function (kS) {
            return (kS < 16 ? "0" : "") + kS.toString(16)
        }).join("")
    };
    var kC, jr;
    kC = jr = function (fs, kS, kT) {
        var kR = [];
        for (var kQ = kS; kQ < kT; kQ++) {
            kR.push(String.fromCharCode(hk(fs, kQ)))
        }
        return kR.join("")
    };
    var jp, dj;
    jp = dj = function hC(kQ, kR) {
        var fs = a5(kQ, kR);
        return fs > 0 ? kC(kQ, kR + 4, kR + 4 + fs - 1) : ""
    };
    var O, h2;
    O = h2 = function fG(kQ, kR) {
        var fs = 2 * a5(kQ, kR);
        return fs > 0 ? kC(kQ, kR + 4, kR + 4 + fs - 1) : ""
    };
    var gu, dg;
    gu = dg = function (kQ, fs) {
        return hn(kQ, fs)
    };
    var hP = function (fs) {
        return [].concat.apply([], fs)
    };
    if (typeof Buffer !== "undefined") {
        iX = function gq(fs, kQ, kR) {
            if (!Buffer.isBuffer(fs)) {
                return ac(fs, kQ, kR)
            }
            return fs.toString("utf16le", kQ, kR)
        };
        a = function (fs, kR, kQ) {
            return Buffer.isBuffer(fs) ? fs.toString("hex", kR, kR + kQ) : co(fs, kR, kQ)
        };
        jp = function de(kQ, kR) {
            if (!Buffer.isBuffer(kQ)) {
                return dj(kQ, kR)
            }
            var fs = kQ.readUInt32LE(kR);
            return fs > 0 ? kQ.toString("utf8", kR + 4, kR + 4 + fs - 1) : ""
        };
        O = function iS(kQ, kR) {
            if (!Buffer.isBuffer(kQ)) {
                return h2(kQ, kR)
            }
            var fs = 2 * kQ.readUInt32LE(kR);
            return kQ.toString("utf16le", kR + 4, kR + 4 + fs - 1)
        };
        kC = function bs(fs, kQ) {
            return this.toString("utf8", fs, kQ)
        };
        fJ = function (fs) {
            return (fs[0].length > 0 && Buffer.isBuffer(fs[0][0])) ? Buffer.concat(fs[0]) : w(fs)
        };
        hP = function (fs) {
            return Buffer.isBuffer(fs[0]) ? Buffer.concat(fs) : [].concat.apply([], fs)
        };
        gu = function g9(fs, kQ) {
            if (Buffer.isBuffer(fs)) {
                return fs.readDoubleLE(kQ)
            }
            return dg(fs, kQ)
        }
    }
    var hk = function (kQ, fs) {
        return kQ[fs]
    };
    var eS = function (kQ, fs) {
        return kQ[fs + 1] * (1 << 8) + kQ[fs]
    };
    var jN = function (kQ, fs) {
        var kR = kQ[fs + 1] * (1 << 8) + kQ[fs];
        return (kR < 32768) ? kR : (65535 - kR + 1) * -1
    };
    var a5 = function (kQ, fs) {
        return kQ[fs + 3] * (1 << 24) + (kQ[fs + 2] << 16) + (kQ[fs + 1] << 8) + kQ[fs]
    };
    var f0 = function (kQ, fs) {
        return (kQ[fs + 3] << 24) | (kQ[fs + 2] << 16) | (kQ[fs + 1] << 8) | kQ[fs]
    };
    var jF = function (fs) {
        return fs.match(/../g).map(function (kQ) {
            return parseInt(kQ, 16)
        })
    };
    var hZ = typeof Buffer !== "undefined" ? function (fs) {
            return Buffer.isBuffer(fs) ? new Buffer(fs, "hex") : jF(fs)
        } : jF;
    if (typeof cptable !== "undefined") {
        iX = function (fs, kQ, kR) {
            return cptable.utils.decode(1200, fs.slice(kQ, kR))
        };
        kC = function (fs, kQ, kR) {
            return cptable.utils.decode(65001, fs.slice(kQ, kR))
        };
        jp = function (kQ, kR) {
            var fs = a5(kQ, kR);
            return fs > 0 ? cptable.utils.decode(e1, kQ.slice(kR + 4, kR + 4 + fs - 1)) : ""
        };
        O = function (kQ, kR) {
            var fs = 2 * a5(kQ, kR);
            return fs > 0 ? cptable.utils.decode(1200, kQ.slice(kR + 4, kR + 4 + fs - 1)) : ""
        }
    }

    function d0(kY, kX) {
        var fs, kQ, kU, kT = [],
            kW, kV, kR, kS;
        switch (kX) {
        case "lpstr":
            fs = jp(this, this.l);
            kY = 5 + fs.length;
            break;
        case "lpwstr":
            fs = O(this, this.l);
            kY = 5 + fs.length;
            if (fs[fs.length - 1] == "\u0000") {
                kY += 2
            }
            break;
        case "cstr":
            kY = 0;
            fs = "";
            while ((kW = hk(this, this.l + kY++)) !== 0) {
                kT.push(fF(kW))
            }
            fs = kT.join("");
            break;
        case "wstr":
            kY = 0;
            fs = "";
            while ((kW = eS(this, this.l + kY)) !== 0) {
                kT.push(fF(kW));
                kY += 2
            }
            kY += 2;
            fs = kT.join("");
            break;
        case "dbcs":
            fs = "";
            kS = this.l;
            for (kR = 0; kR != kY; ++kR) {
                if (this.lens && this.lens.indexOf(kS) !== -1) {
                    kW = hk(this, kS);
                    this.l = kS + 1;
                    kV = d0.call(this, kY - kR, kW ? "dbcs" : "sbcs");
                    return kT.join("") + kV
                }
                kT.push(fF(eS(this, kS)));
                kS += 2
            }
            fs = kT.join("");
            kY *= 2;
            break;
        case "sbcs":
            fs = "";
            kS = this.l;
            for (kR = 0; kR != kY; ++kR) {
                if (this.lens && this.lens.indexOf(kS) !== -1) {
                    kW = hk(this, kS);
                    this.l = kS + 1;
                    kV = d0.call(this, kY - kR, kW ? "dbcs" : "sbcs");
                    return kT.join("") + kV
                }
                kT.push(fF(hk(this, kS)));
                kS += 1
            }
            fs = kT.join("");
            break;
        case "utf8":
            fs = kC(this, this.l, this.l + kY);
            break;
        case "utf16le":
            kY *= 2;
            fs = iX(this, this.l, this.l + kY);
            break;
        default:
            switch (kY) {
            case 1:
                kQ = hk(this, this.l);
                this.l++;
                return kQ;
            case 2:
                kQ = kX !== "i" ? eS(this, this.l) : jN(this, this.l);
                this.l += 2;
                return kQ;
            case 4:
                if (kX === "i" || (this[this.l + 3] & 128) === 0) {
                    kQ = f0(this, this.l);
                    this.l += 4;
                    return kQ
                } else {
                    kU = a5(this, this.l);
                    this.l += 4;
                    return kU
                }
                break;
            case 8:
                if (kX === "f") {
                    kU = gu(this, this.l);
                    this.l += 8;
                    return kU
                }
            case 16:
                fs = a(this, this.l, kY);
                break
            }
        }
        this.l += kY;
        return fs
    }

    function eu(kR, kQ) {
        var fs = a(this, this.l, kR.length >> 1);
        if (fs !== kR) {
            throw kQ + "Expected " + kR + " saw " + fs
        }
        this.l += kR.length >> 1
    }

    function hs(fs, kQ) {
        fs.l = kQ;
        fs.read_shift = d0;
        fs.chk = eu
    }
    var cN = {};
    var gw = function gw(ld) {
        ld.version = "0.8.1";

        function k4(lr) {
            var lt = "",
                ls = lr.length - 1;
            while (ls >= 0) {
                lt += lr.charAt(ls--)
            }
            return lt
        }

        function k6(lt, lr) {
            var ls = "";
            while (ls.length < lr) {
                ls += lt
            }
            return ls
        }

        function le(lr, lt) {
            var ls = "" + lr;
            return ls.length >= lt ? ls : k6("0", lt - ls.length) + ls
        }

        function kY(lr, lt) {
            var ls = "" + lr;
            return ls.length >= lt ? ls : k6(" ", lt - ls.length) + ls
        }

        function ln(lr, lt) {
            var ls = "" + lr;
            return ls.length >= lt ? ls : ls + k6(" ", lt - ls.length)
        }

        function kT(lr, lt) {
            var ls = "" + Math.round(lr);
            return ls.length >= lt ? ls : k6("0", lt - ls.length) + ls
        }

        function kR(lr, lt) {
            var ls = "" + lr;
            return ls.length >= lt ? ls : k6("0", lt - ls.length) + ls
        }
        var lg = Math.pow(2, 32);

        function lh(lr, lt) {
            if (lr > lg || lr < -lg) {
                return kT(lr, lt)
            }
            var ls = Math.round(lr);
            return kR(ls, lt)
        }

        function k8(ls, lr) {
            return ls.length >= 7 + lr && (ls.charCodeAt(lr) | 32) === 103 && (ls.charCodeAt(lr + 1) | 32) === 101 && (ls.charCodeAt(lr + 2) | 32) === 110 && (ls.charCodeAt(lr + 3) | 32) === 101 && (ls.charCodeAt(lr + 4) | 32) === 114 && (ls.charCodeAt(lr + 5) | 32) === 97 && (ls.charCodeAt(lr + 6) | 32) === 108
        }
        var li = [
            ["date1904", 0],
            ["output", ""],
            ["WTF", false]
        ];

        function k1(lr) {
            for (var ls = 0; ls != li.length; ++ls) {
                if (lr[li[ls][0]] === undefined) {
                    lr[li[ls][0]] = li[ls][1]
                }
            }
        }
        ld.opts = li;
        var lj = {
            0: "General",
            1: "0",
            2: "0.00",
            3: "#,##0",
            4: "#,##0.00",
            9: "0%",
            10: "0.00%",
            11: "0.00E+00",
            12: "# ?/?",
            13: "# ??/??",
            14: "m/d/yy",
            15: "d-mmm-yy",
            16: "d-mmm",
            17: "mmm-yy",
            18: "h:mm AM/PM",
            19: "h:mm:ss AM/PM",
            20: "h:mm",
            21: "h:mm:ss",
            22: "m/d/yy h:mm",
            37: "#,##0 ;(#,##0)",
            38: "#,##0 ;[Red](#,##0)",
            39: "#,##0.00;(#,##0.00)",
            40: "#,##0.00;[Red](#,##0.00)",
            45: "mm:ss",
            46: "[h]:mm:ss",
            47: "mmss.0",
            48: "##0.0E+0",
            49: "@",
            56: '"上午/下午 "hh"時"mm"分"ss"秒 "',
            65535: "General"
        };
        var k0 = [
            ["Sun", "Sunday"],
            ["Mon", "Monday"],
            ["Tue", "Tuesday"],
            ["Wed", "Wednesday"],
            ["Thu", "Thursday"],
            ["Fri", "Friday"],
            ["Sat", "Saturday"]
        ];
        var k7 = [
            ["J", "Jan", "January"],
            ["F", "Feb", "February"],
            ["M", "Mar", "March"],
            ["A", "Apr", "April"],
            ["M", "May", "May"],
            ["J", "Jun", "June"],
            ["J", "Jul", "July"],
            ["A", "Aug", "August"],
            ["S", "Sep", "September"],
            ["O", "Oct", "October"],
            ["N", "Nov", "November"],
            ["D", "Dec", "December"]
        ];

        function lq(lD, lr, lC) {
            var ly = lD < 0 ? -1 : 1;
            var lt = lD * ly;
            var lv = 0,
                lw = 1,
                lB = 0;
            var lx = 1,
                lz = 0,
                lA = 0;
            var lu = Math.floor(lt);
            while (lz < lr) {
                lu = Math.floor(lt);
                lB = lu * lw + lv;
                lA = lu * lz + lx;
                if ((lt - lu) < 5e-10) {
                    break
                }
                lt = 1 / (lt - lu);
                lv = lw;
                lw = lB;
                lx = lz;
                lz = lA
            }
            if (lA > lr) {
                lA = lz;
                lB = lw
            }
            if (lA > lr) {
                lA = lx;
                lB = lv
            }
            if (!lC) {
                return [0, ly * lB, lA]
            }
            if (lA === 0) {
                throw "Unexpected state: " + lB + " " + lw + " " + lv + " " + lA + " " + lz + " " + lx
            }
            var ls = Math.floor(ly * lB / lA);
            return [ls, ly * lB - ls * lA, lA]
        }

        function kV(lr, ls) {
            return "" + lr
        }
        ld._general_int = kV;
        var k5 = (function ll() {
            var lv = /\.(\d*[1-9])0+$/,
                lu = /\.0*$/,
                lt = /\.(\d*[1-9])0+/,
                ls = /\.0*[Ee]/,
                lr = /(E[+-])(\d)$/;

            function lA(lC) {
                var lB = (lC < 0 ? 12 : 11);
                var lD = lw(lC.toFixed(12));
                if (lD.length <= lB) {
                    return lD
                }
                lD = lC.toPrecision(10);
                if (lD.length <= lB) {
                    return lD
                }
                return lC.toExponential(5)
            }

            function ly(lB) {
                var lC = lB.toFixed(11).replace(lv, ".$1");
                if (lC.length > (lB < 0 ? 12 : 11)) {
                    lC = lB.toPrecision(6)
                }
                return lC
            }

            function lx(lC) {
                for (var lB = 0; lB != lC.length; ++lB) {
                    if ((lC.charCodeAt(lB) | 32) === 101) {
                        return lC.replace(lt, ".$1").replace(ls, "E").replace("e", "E").replace(lr, "$10$2")
                    }
                }
                return lC
            }

            function lw(lB) {
                return lB.indexOf(".") > -1 ? lB.replace(lu, "").replace(lv, ".$1") : lB
            }
            return function lz(lC, lD) {
                var lB = Math.floor(Math.log(Math.abs(lC)) * Math.LOG10E),
                    lE;
                if (lB >= -4 && lB <= -1) {
                    lE = lC.toPrecision(10 + lB)
                } else {
                    if (Math.abs(lB) <= 9) {
                        lE = lA(lC)
                    } else {
                        if (lB === 10) {
                            lE = lC.toFixed(10).substr(0, 12)
                        } else {
                            lE = ly(lC)
                        }
                    }
                }
                return lw(lx(lE))
            }
        })();
        ld._general_num = k5;

        function lo(lr, ls) {
            switch (typeof lr) {
            case "string":
                return lr;
            case "boolean":
                return lr ? "TRUE" : "FALSE";
            case "number":
                return (lr | 0) === lr ? kV(lr, ls) : k5(lr, ls)
            }
            throw new Error("unsupported value in General format: " + lr)
        }
        ld._general = lo;

        function kW(lr, ls) {
            return 0
        }

        function lf(ly, lr, lx) {
            if (ly > 2958465 || ly < 0) {
                return null
            }
            var lt = (ly | 0),
                ls = Math.floor(86400 * (ly - lt)),
                lz = 0;
            var lu = [];
            var lv = {
                D: lt,
                T: ls,
                u: 86400 * (ly - lt) - ls,
                y: 0,
                m: 0,
                d: 0,
                H: 0,
                M: 0,
                S: 0,
                q: 0
            };
            if (Math.abs(lv.u) < 0.000001) {
                lv.u = 0
            }
            k1(lr != null ? lr : (lr = []));
            if (lr.date1904) {
                lt += 1462
            }
            if (lv.u > 0.999) {
                lv.u = 0;
                if (++ls == 86400) {
                    ls = 0;
                    ++lt
                }
            }
            if (lt === 60) {
                lu = lx ? [1317, 10, 29] : [1900, 2, 29];
                lz = 3
            } else {
                if (lt === 0) {
                    lu = lx ? [1317, 8, 29] : [1900, 1, 0];
                    lz = 6
                } else {
                    if (lt > 60) {
                        --lt
                    }
                    var lw = new Date(1900, 0, 1);
                    lw.setDate(lw.getDate() + lt - 1);
                    lu = [lw.getFullYear(), lw.getMonth() + 1, lw.getDate()];
                    lz = lw.getDay();
                    if (lt < 60) {
                        lz = (lz + 6) % 7
                    }
                    if (lx) {
                        lz = kW(lw, lu)
                    }
                }
            }
            lv.y = lu[0];
            lv.m = lu[1];
            lv.d = lu[2];
            lv.S = ls % 60;
            ls = Math.floor(ls / 60);
            lv.M = ls % 60;
            ls = Math.floor(ls / 60);
            lv.H = ls;
            lv.q = lz;
            return lv
        }
        ld.parse_date_code = lf;

        function lk(lx, lt, ls, lz) {
            var lr = "",
                lA = 0,
                lw = 0,
                ly = ls.y,
                lu, lv = 0;
            switch (lx) {
            case 98:
                ly = ls.y + 543;
            case 121:
                switch (lt.length) {
                case 1:
                case 2:
                    lu = ly % 100;
                    lv = 2;
                    break;
                default:
                    lu = ly % 10000;
                    lv = 4;
                    break
                }
                break;
            case 109:
                switch (lt.length) {
                case 1:
                case 2:
                    lu = ls.m;
                    lv = lt.length;
                    break;
                case 3:
                    return k7[ls.m - 1][1];
                case 5:
                    return k7[ls.m - 1][0];
                default:
                    return k7[ls.m - 1][2]
                }
                break;
            case 100:
                switch (lt.length) {
                case 1:
                case 2:
                    lu = ls.d;
                    lv = lt.length;
                    break;
                case 3:
                    return k0[ls.q][0];
                default:
                    return k0[ls.q][1]
                }
                break;
            case 104:
                switch (lt.length) {
                case 1:
                case 2:
                    lu = 1 + (ls.H + 11) % 12;
                    lv = lt.length;
                    break;
                default:
                    throw "bad hour format: " + lt
                }
                break;
            case 72:
                switch (lt.length) {
                case 1:
                case 2:
                    lu = ls.H;
                    lv = lt.length;
                    break;
                default:
                    throw "bad hour format: " + lt
                }
                break;
            case 77:
                switch (lt.length) {
                case 1:
                case 2:
                    lu = ls.M;
                    lv = lt.length;
                    break;
                default:
                    throw "bad minute format: " + lt
                }
                break;
            case 115:
                if (ls.u === 0) {
                    switch (lt) {
                    case "s":
                    case "ss":
                        return le(ls.S, lt.length);
                    case ".0":
                    case ".00":
                    case ".000":
                    }
                }
                switch (lt) {
                case "s":
                case "ss":
                case ".0":
                case ".00":
                case ".000":
                    if (lz >= 2) {
                        lw = lz === 3 ? 1000 : 100
                    } else {
                        lw = lz === 1 ? 10 : 1
                    }
                    lA = Math.round((lw) * (ls.S + ls.u));
                    if (lA >= 60 * lw) {
                        lA = 0
                    }
                    if (lt === "s") {
                        return lA === 0 ? "0" : "" + lA / lw
                    }
                    lr = le(lA, 2 + lz);
                    if (lt === "ss") {
                        return lr.substr(0, 2)
                    }
                    return "." + lr.substr(2, lt.length - 1);
                default:
                    throw "bad second format: " + lt
                }
            case 90:
                switch (lt) {
                case "[h]":
                case "[hh]":
                    lu = ls.D * 24 + ls.H;
                    break;
                case "[m]":
                case "[mm]":
                    lu = (ls.D * 24 + ls.H) * 60 + ls.M;
                    break;
                case "[s]":
                case "[ss]":
                    lu = ((ls.D * 24 + ls.H) * 60 + ls.M) * 60 + Math.round(ls.S + ls.u);
                    break;
                default:
                    throw "bad abstime format: " + lt
                }
                lv = lt.length === 3 ? 1 : 2;
                break;
            case 101:
                lu = ly;
                lv = 1
            }
            if (lv > 0) {
                return le(lu, lv)
            } else {
                return ""
            }
        }

        function lb(ls) {
            if (ls.length <= 3) {
                return ls
            }
            var lr = (ls.length % 3),
                lt = ls.substr(0, lr);
            for (; lr != ls.length; lr += 3) {
                lt += (lt.length > 0 ? "," : "") + ls.substr(lr, 3)
            }
            return lt
        }
        var kU = (function kS() {
            var lI = /%/g;

            function lu(lM, lL, lP) {
                var lO = lL.replace(lI, ""),
                    lN = lL.length - lO.length;
                return lK(lM, lO, lP * Math.pow(10, 2 * lN)) + k6("%", lN)
            }

            function lt(lN, lM, lO) {
                var lL = lM.length - 1;
                while (lM.charCodeAt(lL - 1) === 44) {
                    --lL
                }
                return lK(lN, lM.substr(0, lL), lO / Math.pow(10, 3 * (lM.length - lL)))
            }

            function ly(lN, lR) {
                var lQ;
                var lL = lN.indexOf("E") - lN.indexOf(".") - 1;
                if (lN.match(/^#+0.0E\+0$/)) {
                    var lP = lN.indexOf(".");
                    if (lP === -1) {
                        lP = lN.indexOf("E")
                    }
                    var lM = Math.floor(Math.log(Math.abs(lR)) * Math.LOG10E) % lP;
                    if (lM < 0) {
                        lM += lP
                    }
                    lQ = (lR / Math.pow(10, lM)).toPrecision(lL + 1 + (lP + lM) % lP);
                    if (lQ.indexOf("e") === -1) {
                        var lO = Math.floor(Math.log(Math.abs(lR)) * Math.LOG10E);
                        if (lQ.indexOf(".") === -1) {
                            lQ = lQ[0] + "." + lQ.substr(1) + "E+" + (lO - lQ.length + lM)
                        } else {
                            lQ += "E+" + (lO - lM)
                        }
                        while (lQ.substr(0, 2) === "0.") {
                            lQ = lQ[0] + lQ.substr(2, lP) + "." + lQ.substr(2 + lP);
                            lQ = lQ.replace(/^0+([1-9])/, "$1").replace(/^0+\./, "0.")
                        }
                        lQ = lQ.replace(/\+-/, "-")
                    }
                    lQ = lQ.replace(/^([+-]?)(\d*)\.(\d*)[Ee]/, function (lT, lS, lV, lU) {
                        return lS + lV + lU.substr(0, (lP + lM) % lP) + "." + lU.substr(lM) + "E"
                    })
                } else {
                    lQ = lR.toExponential(lL)
                } if (lN.match(/E\+00$/) && lQ.match(/e[+-]\d$/)) {
                    lQ = lQ.substr(0, lQ.length - 1) + "0" + lQ[lQ.length - 1]
                }
                if (lN.match(/E\-/) && lQ.match(/e\+/)) {
                    lQ = lQ.replace(/e\+/, "e")
                }
                return lQ.replace("e", "E")
            }
            var lv = /# (\?+)( ?)\/( ?)(\d+)/;

            function lB(lQ, lP, lL) {
                var lS = parseInt(lQ[4]),
                    lO = Math.round(lP * lS),
                    lR = Math.floor(lO / lS);
                var lN = (lO - lR * lS),
                    lM = lS;
                return lL + (lR === 0 ? "" : "" + lR) + " " + (lN === 0 ? k6(" ", lQ[1].length + 1 + lQ[4].length) : kY(lN, lQ[1].length) + lQ[2] + "/" + lQ[3] + le(lM, lQ[4].length))
            }

            function lz(lN, lM, lL) {
                return lL + (lM === 0 ? "" : "" + lM) + k6(" ", lN[1].length + 2 + lN[4].length)
            }
            var lC = /^#*0*\.(0+)/;
            var lJ = /\).*[0#]/;
            var lx = /\(###\) ###\\?-####/;

            function lw(lN) {
                var lM = "",
                    lO;
                for (var lL = 0; lL != lN.length; ++lL) {
                    switch ((lO = lN.charCodeAt(lL))) {
                    case 35:
                        break;
                    case 63:
                        lM += " ";
                        break;
                    case 48:
                        lM += "0";
                        break;
                    default:
                        lM += String.fromCharCode(lO)
                    }
                }
                return lM
            }

            function lA(lN, lM) {
                var lL = Math.pow(10, lM);
                return "" + (Math.round(lN * lL) / lL)
            }

            function lE(lM, lL) {
                return Math.round((lM - Math.floor(lM)) * Math.pow(10, lL))
            }

            function lG(lL) {
                if (lL < 2147483647 && lL > -2147483648) {
                    return "" + (lL >= 0 ? (lL | 0) : (lL - 1 | 0))
                }
                return "" + Math.floor(lL)
            }

            function lF(lV, lR, lQ) {
                if (lV.charCodeAt(0) === 40 && !lR.match(lJ)) {
                    var lU = lR.replace(/\( */, "").replace(/ \)/, "").replace(/\)/, "");
                    if (lQ >= 0) {
                        return lF("n", lU, lQ)
                    }
                    return "(" + lF("n", lU, -lQ) + ")"
                }
                if (lR.charCodeAt(lR.length - 1) === 44) {
                    return lt(lV, lR, lQ)
                }
                if (lR.indexOf("%") !== -1) {
                    return lu(lV, lR, lQ)
                }
                if (lR.indexOf("E") !== -1) {
                    return ly(lR, lQ)
                }
                if (lR.charCodeAt(0) === 36) {
                    return "$" + lF(lV, lR.substr(lR[1] == " " ? 2 : 1), lQ)
                }
                var lO, lT;
                var lL, lX, lM, lZ = Math.abs(lQ),
                    lP = lQ < 0 ? "-" : "";
                if (lR.match(/^00+$/)) {
                    return lP + lh(lZ, lR.length)
                }
                if (lR.match(/^[#?]+$/)) {
                    lO = lh(lQ, 0);
                    if (lO === "0") {
                        lO = ""
                    }
                    return lO.length > lR.length ? lO : lw(lR.substr(0, lR.length - lO.length)) + lO
                }
                if ((lL = lR.match(lv)) !== null) {
                    return lB(lL, lZ, lP)
                }
                if (lR.match(/^#+0+$/) !== null) {
                    return lP + lh(lZ, lR.length - lR.indexOf("0"))
                }
                if ((lL = lR.match(lC)) !== null) {
                    lO = lA(lQ, lL[1].length).replace(/^([^\.]+)$/, "$1." + lL[1]).replace(/\.$/, "." + lL[1]).replace(/\.(\d*)$/, function (l1, l0) {
                        return "." + l0 + k6("0", lL[1].length - l0.length)
                    });
                    return lR.indexOf("0.") !== -1 ? lO : lO.replace(/^0\./, ".")
                }
                lR = lR.replace(/^#+([0.])/, "$1");
                if ((lL = lR.match(/^(0*)\.(#*)$/)) !== null) {
                    return lP + lA(lZ, lL[2].length).replace(/\.(\d*[1-9])0*$/, ".$1").replace(/^(-?\d*)$/, "$1.").replace(/^0\./, lL[1].length ? "0." : ".")
                }
                if ((lL = lR.match(/^#,##0(\.?)$/)) !== null) {
                    return lP + lb(lh(lZ, 0))
                }
                if ((lL = lR.match(/^#,##0\.([#0]*0)$/)) !== null) {
                    return lQ < 0 ? "-" + lF(lV, lR, -lQ) : lb("" + (Math.floor(lQ))) + "." + le(lE(lQ, lL[1].length), lL[1].length)
                }
                if ((lL = lR.match(/^#,#*,#0/)) !== null) {
                    return lF(lV, lR.replace(/^#,#*,/, ""), lQ)
                }
                if ((lL = lR.match(/^([0#]+)(\\?-([0#]+))+$/)) !== null) {
                    lO = k4(lF(lV, lR.replace(/[\\-]/g, ""), lQ));
                    lX = 0;
                    return k4(k4(lR.replace(/\\/g, "")).replace(/[0#]/g, function (l0) {
                        return lX < lO.length ? lO[lX++] : l0 === "0" ? "0" : ""
                    }))
                }
                if (lR.match(lx) !== null) {
                    lO = lF(lV, "##########", lQ);
                    return "(" + lO.substr(0, 3) + ") " + lO.substr(3, 3) + "-" + lO.substr(6)
                }
                var lN = "";
                if ((lL = lR.match(/^([#0?]+)( ?)\/( ?)([#0?]+)/)) !== null) {
                    lX = Math.min(lL[4].length, 7);
                    lM = lq(lZ, Math.pow(10, lX) - 1, false);
                    lO = "" + lP;
                    lN = lK("n", lL[1], lM[1]);
                    if (lN[lN.length - 1] == " ") {
                        lN = lN.substr(0, lN.length - 1) + "0"
                    }
                    lO += lN + lL[2] + "/" + lL[3];
                    lN = ln(lM[2], lX);
                    if (lN.length < lL[4].length) {
                        lN = lw(lL[4].substr(lL[4].length - lN.length)) + lN
                    }
                    lO += lN;
                    return lO
                }
                if ((lL = lR.match(/^# ([#0?]+)( ?)\/( ?)([#0?]+)/)) !== null) {
                    lX = Math.min(Math.max(lL[1].length, lL[4].length), 7);
                    lM = lq(lZ, Math.pow(10, lX) - 1, true);
                    return lP + (lM[0] || (lM[1] ? "" : "0")) + " " + (lM[1] ? kY(lM[1], lX) + lL[2] + "/" + lL[3] + ln(lM[2], lX) : k6(" ", 2 * lX + 1 + lL[2].length + lL[3].length))
                }
                if ((lL = lR.match(/^[#0?]+$/)) !== null) {
                    lO = lh(lQ, 0);
                    if (lR.length <= lO.length) {
                        return lO
                    }
                    return lw(lR.substr(0, lR.length - lO.length)) + lO
                }
                if ((lL = lR.match(/^([#0?]+)\.([#0]+)$/)) !== null) {
                    lO = "" + lQ.toFixed(Math.min(lL[2].length, 10)).replace(/([^0])0+$/, "$1");
                    lX = lO.indexOf(".");
                    var lS = lR.indexOf(".") - lX,
                        lY = lR.length - lO.length - lS;
                    return lw(lR.substr(0, lS) + lO + lR.substr(lR.length - lY))
                }
                if ((lL = lR.match(/^00,000\.([#0]*0)$/)) !== null) {
                    lX = lE(lQ, lL[1].length);
                    return lQ < 0 ? "-" + lF(lV, lR, -lQ) : lb(lG(lQ)).replace(/^\d,\d{3}$/, "0$&").replace(/^\d*$/, function (l0) {
                        return "00," + (l0.length < 3 ? le(0, 3 - l0.length) : "") + l0
                    }) + "." + le(lX, lL[1].length)
                }
                switch (lR) {
                case "#,###":
                    var lW = lb(lh(lZ, 0));
                    return lW !== "0" ? lP + lW : "";
                default:
                }
                throw new Error("unsupported format |" + lR + "|")
            }

            function lH(lN, lM, lO) {
                var lL = lM.length - 1;
                while (lM.charCodeAt(lL - 1) === 44) {
                    --lL
                }
                return lK(lN, lM.substr(0, lL), lO / Math.pow(10, 3 * (lM.length - lL)))
            }

            function lD(lM, lL, lP) {
                var lO = lL.replace(lI, ""),
                    lN = lL.length - lO.length;
                return lK(lM, lO, lP * Math.pow(10, 2 * lN)) + k6("%", lN)
            }

            function ls(lN, lR) {
                var lQ;
                var lL = lN.indexOf("E") - lN.indexOf(".") - 1;
                if (lN.match(/^#+0.0E\+0$/)) {
                    var lP = lN.indexOf(".");
                    if (lP === -1) {
                        lP = lN.indexOf("E")
                    }
                    var lM = Math.floor(Math.log(Math.abs(lR)) * Math.LOG10E) % lP;
                    if (lM < 0) {
                        lM += lP
                    }
                    lQ = (lR / Math.pow(10, lM)).toPrecision(lL + 1 + (lP + lM) % lP);
                    if (!lQ.match(/[Ee]/)) {
                        var lO = Math.floor(Math.log(Math.abs(lR)) * Math.LOG10E);
                        if (lQ.indexOf(".") === -1) {
                            lQ = lQ[0] + "." + lQ.substr(1) + "E+" + (lO - lQ.length + lM)
                        } else {
                            lQ += "E+" + (lO - lM)
                        }
                        lQ = lQ.replace(/\+-/, "-")
                    }
                    lQ = lQ.replace(/^([+-]?)(\d*)\.(\d*)[Ee]/, function (lT, lS, lV, lU) {
                        return lS + lV + lU.substr(0, (lP + lM) % lP) + "." + lU.substr(lM) + "E"
                    })
                } else {
                    lQ = lR.toExponential(lL)
                } if (lN.match(/E\+00$/) && lQ.match(/e[+-]\d$/)) {
                    lQ = lQ.substr(0, lQ.length - 1) + "0" + lQ[lQ.length - 1]
                }
                if (lN.match(/E\-/) && lQ.match(/e\+/)) {
                    lQ = lQ.replace(/e\+/, "e")
                }
                return lQ.replace("e", "E")
            }

            function lr(lU, lR, lQ) {
                if (lU.charCodeAt(0) === 40 && !lR.match(lJ)) {
                    var lT = lR.replace(/\( */, "").replace(/ \)/, "").replace(/\)/, "");
                    if (lQ >= 0) {
                        return lr("n", lT, lQ)
                    }
                    return "(" + lr("n", lT, -lQ) + ")"
                }
                if (lR.charCodeAt(lR.length - 1) === 44) {
                    return lH(lU, lR, lQ)
                }
                if (lR.indexOf("%") !== -1) {
                    return lD(lU, lR, lQ)
                }
                if (lR.indexOf("E") !== -1) {
                    return ls(lR, lQ)
                }
                if (lR.charCodeAt(0) === 36) {
                    return "$" + lr(lU, lR.substr(lR[1] == " " ? 2 : 1), lQ)
                }
                var lO;
                var lL, lW, lM, lY = Math.abs(lQ),
                    lP = lQ < 0 ? "-" : "";
                if (lR.match(/^00+$/)) {
                    return lP + le(lY, lR.length)
                }
                if (lR.match(/^[#?]+$/)) {
                    lO = ("" + lQ);
                    if (lQ === 0) {
                        lO = ""
                    }
                    return lO.length > lR.length ? lO : lw(lR.substr(0, lR.length - lO.length)) + lO
                }
                if ((lL = lR.match(lv)) !== null) {
                    return lz(lL, lY, lP)
                }
                if (lR.match(/^#+0+$/) !== null) {
                    return lP + le(lY, lR.length - lR.indexOf("0"))
                }
                if ((lL = lR.match(lC)) !== null) {
                    lO = ("" + lQ).replace(/^([^\.]+)$/, "$1." + lL[1]).replace(/\.$/, "." + lL[1]).replace(/\.(\d*)$/, function (l0, lZ) {
                        return "." + lZ + k6("0", lL[1].length - lZ.length)
                    });
                    return lR.indexOf("0.") !== -1 ? lO : lO.replace(/^0\./, ".")
                }
                lR = lR.replace(/^#+([0.])/, "$1");
                if ((lL = lR.match(/^(0*)\.(#*)$/)) !== null) {
                    return lP + ("" + lY).replace(/\.(\d*[1-9])0*$/, ".$1").replace(/^(-?\d*)$/, "$1.").replace(/^0\./, lL[1].length ? "0." : ".")
                }
                if ((lL = lR.match(/^#,##0(\.?)$/)) !== null) {
                    return lP + lb(("" + lY))
                }
                if ((lL = lR.match(/^#,##0\.([#0]*0)$/)) !== null) {
                    return lQ < 0 ? "-" + lr(lU, lR, -lQ) : lb(("" + lQ)) + "." + k6("0", lL[1].length)
                }
                if ((lL = lR.match(/^#,#*,#0/)) !== null) {
                    return lr(lU, lR.replace(/^#,#*,/, ""), lQ)
                }
                if ((lL = lR.match(/^([0#]+)(\\?-([0#]+))+$/)) !== null) {
                    lO = k4(lr(lU, lR.replace(/[\\-]/g, ""), lQ));
                    lW = 0;
                    return k4(k4(lR.replace(/\\/g, "")).replace(/[0#]/g, function (lZ) {
                        return lW < lO.length ? lO[lW++] : lZ === "0" ? "0" : ""
                    }))
                }
                if (lR.match(lx) !== null) {
                    lO = lr(lU, "##########", lQ);
                    return "(" + lO.substr(0, 3) + ") " + lO.substr(3, 3) + "-" + lO.substr(6)
                }
                var lN = "";
                if ((lL = lR.match(/^([#0?]+)( ?)\/( ?)([#0?]+)/)) !== null) {
                    lW = Math.min(lL[4].length, 7);
                    lM = lq(lY, Math.pow(10, lW) - 1, false);
                    lO = "" + lP;
                    lN = lK("n", lL[1], lM[1]);
                    if (lN[lN.length - 1] == " ") {
                        lN = lN.substr(0, lN.length - 1) + "0"
                    }
                    lO += lN + lL[2] + "/" + lL[3];
                    lN = ln(lM[2], lW);
                    if (lN.length < lL[4].length) {
                        lN = lw(lL[4].substr(lL[4].length - lN.length)) + lN
                    }
                    lO += lN;
                    return lO
                }
                if ((lL = lR.match(/^# ([#0?]+)( ?)\/( ?)([#0?]+)/)) !== null) {
                    lW = Math.min(Math.max(lL[1].length, lL[4].length), 7);
                    lM = lq(lY, Math.pow(10, lW) - 1, true);
                    return lP + (lM[0] || (lM[1] ? "" : "0")) + " " + (lM[1] ? kY(lM[1], lW) + lL[2] + "/" + lL[3] + ln(lM[2], lW) : k6(" ", 2 * lW + 1 + lL[2].length + lL[3].length))
                }
                if ((lL = lR.match(/^[#0?]+$/)) !== null) {
                    lO = "" + lQ;
                    if (lR.length <= lO.length) {
                        return lO
                    }
                    return lw(lR.substr(0, lR.length - lO.length)) + lO
                }
                if ((lL = lR.match(/^([#0]+)\.([#0]+)$/)) !== null) {
                    lO = "" + lQ.toFixed(Math.min(lL[2].length, 10)).replace(/([^0])0+$/, "$1");
                    lW = lO.indexOf(".");
                    var lS = lR.indexOf(".") - lW,
                        lX = lR.length - lO.length - lS;
                    return lw(lR.substr(0, lS) + lO + lR.substr(lR.length - lX))
                }
                if ((lL = lR.match(/^00,000\.([#0]*0)$/)) !== null) {
                    return lQ < 0 ? "-" + lr(lU, lR, -lQ) : lb("" + lQ).replace(/^\d,\d{3}$/, "0$&").replace(/^\d*$/, function (lZ) {
                        return "00," + (lZ.length < 3 ? le(0, 3 - lZ.length) : "") + lZ
                    }) + "." + le(0, lL[1].length)
                }
                switch (lR) {
                case "#,###":
                    var lV = lb("" + lY);
                    return lV !== "0" ? lP + lV : "";
                default:
                }
                throw new Error("unsupported format |" + lR + "|")
            }
            return function lK(lM, lL, lN) {
                return (lN | 0) === lN ? lr(lM, lL, lN) : lF(lM, lL, lN)
            }
        })();

        function lm(lr) {
            var lt = [];
            var lv = false,
                lw;
            for (var lu = 0, ls = 0; lu < lr.length; ++lu) {
                switch ((lw = lr.charCodeAt(lu))) {
                case 34:
                    lv = !lv;
                    break;
                case 95:
                case 42:
                case 92:
                    ++lu;
                    break;
                case 59:
                    lt[lt.length] = lr.substr(ls, lu - ls);
                    ls = lu + 1
                }
            }
            lt[lt.length] = lr.substr(ls);
            if (lv === true) {
                throw new Error("Format |" + lr + "| unterminated string ")
            }
            return lt
        }
        ld._split = lm;
        var k2 = /\[[HhMmSs]*\]/;

        function kX(ls, ly, lA, lQ) {
            var lK = [],
                lF = "",
                lJ = 0,
                lN = "",
                lv = "t",
                lE, lB, lI, lD;
            var lR = "H";
            while (lJ < ls.length) {
                switch ((lN = ls[lJ])) {
                case "G":
                    if (!k8(ls, lJ)) {
                        throw new Error("unrecognized character " + lN + " in " + ls)
                    }
                    lK[lK.length] = {
                        t: "G",
                        v: "General"
                    };
                    lJ += 7;
                    break;
                case '"':
                    for (lF = "";
                        (lD = ls.charCodeAt(++lJ)) !== 34 && lJ < ls.length;) {
                        lF += String.fromCharCode(lD)
                    }
                    lK[lK.length] = {
                        t: "t",
                        v: lF
                    };
                    ++lJ;
                    break;
                case "\\":
                    var lx = ls[++lJ],
                        lz = (lx === "(" || lx === ")") ? lx : "t";
                    lK[lK.length] = {
                        t: lz,
                        v: lx
                    };
                    ++lJ;
                    break;
                case "_":
                    lK[lK.length] = {
                        t: "t",
                        v: " "
                    };
                    lJ += 2;
                    break;
                case "@":
                    lK[lK.length] = {
                        t: "T",
                        v: ly
                    };
                    ++lJ;
                    break;
                case "B":
                case "b":
                    if (ls[lJ + 1] === "1" || ls[lJ + 1] === "2") {
                        if (lB == null) {
                            lB = lf(ly, lA, ls[lJ + 1] === "2");
                            if (lB == null) {
                                return ""
                            }
                        }
                        lK[lK.length] = {
                            t: "X",
                            v: ls.substr(lJ, 2)
                        };
                        lv = lN;
                        lJ += 2;
                        break
                    }
                case "M":
                case "D":
                case "Y":
                case "H":
                case "S":
                case "E":
                    lN = lN.toLowerCase();
                case "m":
                case "d":
                case "y":
                case "h":
                case "s":
                case "e":
                case "g":
                    if (ly < 0) {
                        return ""
                    }
                    if (lB == null) {
                        lB = lf(ly, lA);
                        if (lB == null) {
                            return ""
                        }
                    }
                    lF = lN;
                    while (++lJ < ls.length && ls[lJ].toLowerCase() === lN) {
                        lF += lN
                    }
                    if (lN === "m" && lv.toLowerCase() === "h") {
                        lN = "M"
                    }
                    if (lN === "h") {
                        lN = lR
                    }
                    lK[lK.length] = {
                        t: lN,
                        v: lF
                    };
                    lv = lN;
                    break;
                case "A":
                    lE = {
                        t: lN,
                        v: "A"
                    };
                    if (lB == null) {
                        lB = lf(ly, lA)
                    }
                    if (ls.substr(lJ, 3) === "A/P") {
                        if (lB != null) {
                            lE.v = lB.H >= 12 ? "P" : "A"
                        }
                        lE.t = "T";
                        lR = "h";
                        lJ += 3
                    } else {
                        if (ls.substr(lJ, 5) === "AM/PM") {
                            if (lB != null) {
                                lE.v = lB.H >= 12 ? "PM" : "AM"
                            }
                            lE.t = "T";
                            lJ += 5;
                            lR = "h"
                        } else {
                            lE.t = "t";
                            ++lJ
                        }
                    } if (lB == null && lE.t === "T") {
                        return ""
                    }
                    lK[lK.length] = lE;
                    lv = lN;
                    break;
                case "[":
                    lF = lN;
                    while (ls[lJ++] !== "]" && lJ < ls.length) {
                        lF += ls[lJ]
                    }
                    if (lF.substr(-1) !== "]") {
                        throw 'unterminated "[" block: |' + lF + "|"
                    }
                    if (lF.match(k2)) {
                        if (lB == null) {
                            lB = lf(ly, lA);
                            if (lB == null) {
                                return ""
                            }
                        }
                        lK[lK.length] = {
                            t: "Z",
                            v: lF.toLowerCase()
                        }
                    } else {
                        lF = ""
                    }
                    break;
                case ".":
                    if (lB != null) {
                        lF = lN;
                        while ((lN = ls[++lJ]) === "0") {
                            lF += lN
                        }
                        lK[lK.length] = {
                            t: "s",
                            v: lF
                        };
                        break
                    }
                case "0":
                case "#":
                    lF = lN;
                    while ("0#?.,E+-%".indexOf(lN = ls[++lJ]) > -1 || lN == "\\" && ls[lJ + 1] == "-" && "0#".indexOf(ls[lJ + 2]) > -1) {
                        lF += lN
                    }
                    lK[lK.length] = {
                        t: "n",
                        v: lF
                    };
                    break;
                case "?":
                    lF = lN;
                    while (ls[++lJ] === lN) {
                        lF += lN
                    }
                    lE = {
                        t: lN,
                        v: lF
                    };
                    lK[lK.length] = lE;
                    lv = lN;
                    break;
                case "*":
                    ++lJ;
                    if (ls[lJ] == " " || ls[lJ] == "*") {
                        ++lJ
                    }
                    break;
                case "(":
                case ")":
                    lK[lK.length] = {
                        t: (lQ === 1 ? "t" : lN),
                        v: lN
                    };
                    ++lJ;
                    break;
                case "1":
                case "2":
                case "3":
                case "4":
                case "5":
                case "6":
                case "7":
                case "8":
                case "9":
                    lF = lN;
                    while ("0123456789".indexOf(ls[++lJ]) > -1) {
                        lF += ls[lJ]
                    }
                    lK[lK.length] = {
                        t: "D",
                        v: lF
                    };
                    break;
                case " ":
                    lK[lK.length] = {
                        t: lN,
                        v: lN
                    };
                    ++lJ;
                    break;
                default:
                    if (",$-+/():!^&'~{}<>=€acfijklopqrtuvwxz".indexOf(lN) === -1) {
                        throw new Error("unrecognized character " + lN + " in " + ls)
                    }
                    lK[lK.length] = {
                        t: "t",
                        v: lN
                    };
                    ++lJ;
                    break
                }
            }
            var lM = 0,
                lH = 0,
                lw;
            for (lJ = lK.length - 1, lv = "t"; lJ >= 0; --lJ) {
                switch (lK[lJ].t) {
                case "h":
                case "H":
                    lK[lJ].t = lR;
                    lv = "h";
                    if (lM < 1) {
                        lM = 1
                    }
                    break;
                case "s":
                    if ((lw = lK[lJ].v.match(/\.0+$/))) {
                        lH = Math.max(lH, lw[0].length - 1)
                    }
                    if (lM < 3) {
                        lM = 3
                    }
                case "d":
                case "y":
                case "M":
                case "e":
                    lv = lK[lJ].t;
                    break;
                case "m":
                    if (lv === "s") {
                        lK[lJ].t = "M";
                        if (lM < 2) {
                            lM = 2
                        }
                    }
                    break;
                case "X":
                    if (lK[lJ].v === "B2") {}
                    break;
                case "Z":
                    if (lM < 1 && lK[lJ].v.match(/[Hh]/)) {
                        lM = 1
                    }
                    if (lM < 2 && lK[lJ].v.match(/[Mm]/)) {
                        lM = 2
                    }
                    if (lM < 3 && lK[lJ].v.match(/[Ss]/)) {
                        lM = 3
                    }
                }
            }
            switch (lM) {
            case 0:
                break;
            case 1:
                if (lB.u >= 0.5) {
                    lB.u = 0;
                    ++lB.S
                }
                if (lB.S >= 60) {
                    lB.S = 0;
                    ++lB.M
                }
                if (lB.M >= 60) {
                    lB.M = 0;
                    ++lB.H
                }
                break;
            case 2:
                if (lB.u >= 0.5) {
                    lB.u = 0;
                    ++lB.S
                }
                if (lB.S >= 60) {
                    lB.S = 0;
                    ++lB.M
                }
                break
            }
            var lr = "",
                lL;
            for (lJ = 0; lJ < lK.length; ++lJ) {
                switch (lK[lJ].t) {
                case "t":
                case "T":
                case " ":
                case "D":
                    break;
                case "X":
                    lK[lJ] = undefined;
                    break;
                case "d":
                case "m":
                case "y":
                case "h":
                case "H":
                case "M":
                case "s":
                case "e":
                case "b":
                case "Z":
                    lK[lJ].v = lk(lK[lJ].t.charCodeAt(0), lK[lJ].v, lB, lH);
                    lK[lJ].t = "t";
                    break;
                case "n":
                case "(":
                case "?":
                    lL = lJ + 1;
                    while (lK[lL] != null && ((lN = lK[lL].t) === "?" || lN === "D" || (lN === " " || lN === "t") && lK[lL + 1] != null && (lK[lL + 1].t === "?" || lK[lL + 1].t === "t" && lK[lL + 1].v === "/") || lK[lJ].t === "(" && (lN === " " || lN === "n" || lN === ")") || lN === "t" && (lK[lL].v === "/" || "$€".indexOf(lK[lL].v) > -1 || lK[lL].v === " " && lK[lL + 1] != null && lK[lL + 1].t == "?"))) {
                        lK[lJ].v += lK[lL].v;
                        lK[lL] = undefined;
                        ++lL
                    }
                    lr += lK[lJ].v;
                    lJ = lL - 1;
                    break;
                case "G":
                    lK[lJ].t = "t";
                    lK[lJ].v = lo(ly, lA);
                    break
                }
            }
            var lG = "",
                lP, lu;
            if (lr.length > 0) {
                lP = (ly < 0 && lr.charCodeAt(0) === 45 ? -ly : ly);
                lu = kU(lr.charCodeAt(0) === 40 ? "(" : "n", lr, lP);
                lL = lu.length - 1;
                var lt = lK.length;
                for (lJ = 0; lJ < lK.length; ++lJ) {
                    if (lK[lJ] != null && lK[lJ].v.indexOf(".") > -1) {
                        lt = lJ;
                        break
                    }
                }
                var lO = lK.length;
                if (lt === lK.length && lu.indexOf("E") === -1) {
                    for (lJ = lK.length - 1; lJ >= 0; --lJ) {
                        if (lK[lJ] == null || "n?(".indexOf(lK[lJ].t) === -1) {
                            continue
                        }
                        if (lL >= lK[lJ].v.length - 1) {
                            lL -= lK[lJ].v.length;
                            lK[lJ].v = lu.substr(lL + 1, lK[lJ].v.length)
                        } else {
                            if (lL < 0) {
                                lK[lJ].v = ""
                            } else {
                                lK[lJ].v = lu.substr(0, lL + 1);
                                lL = -1
                            }
                        }
                        lK[lJ].t = "t";
                        lO = lJ
                    }
                    if (lL >= 0 && lO < lK.length) {
                        lK[lO].v = lu.substr(0, lL + 1) + lK[lO].v
                    }
                } else {
                    if (lt !== lK.length && lu.indexOf("E") === -1) {
                        lL = lu.indexOf(".") - 1;
                        for (lJ = lt; lJ >= 0; --lJ) {
                            if (lK[lJ] == null || "n?(".indexOf(lK[lJ].t) === -1) {
                                continue
                            }
                            lI = lK[lJ].v.indexOf(".") > -1 && lJ === lt ? lK[lJ].v.indexOf(".") - 1 : lK[lJ].v.length - 1;
                            lG = lK[lJ].v.substr(lI + 1);
                            for (; lI >= 0; --lI) {
                                if (lL >= 0 && (lK[lJ].v[lI] === "0" || lK[lJ].v[lI] === "#")) {
                                    lG = lu[lL--] + lG
                                }
                            }
                            lK[lJ].v = lG;
                            lK[lJ].t = "t";
                            lO = lJ
                        }
                        if (lL >= 0 && lO < lK.length) {
                            lK[lO].v = lu.substr(0, lL + 1) + lK[lO].v
                        }
                        lL = lu.indexOf(".") + 1;
                        for (lJ = lt; lJ < lK.length; ++lJ) {
                            if (lK[lJ] == null || "n?(".indexOf(lK[lJ].t) === -1 && lJ !== lt) {
                                continue
                            }
                            lI = lK[lJ].v.indexOf(".") > -1 && lJ === lt ? lK[lJ].v.indexOf(".") + 1 : 0;
                            lG = lK[lJ].v.substr(0, lI);
                            for (; lI < lK[lJ].v.length; ++lI) {
                                if (lL < lu.length) {
                                    lG += lu[lL++]
                                }
                            }
                            lK[lJ].v = lG;
                            lK[lJ].t = "t";
                            lO = lJ
                        }
                    }
                }
            }
            for (lJ = 0; lJ < lK.length; ++lJ) {
                if (lK[lJ] != null && "n(?".indexOf(lK[lJ].t) > -1) {
                    lP = (lQ > 1 && ly < 0 && lJ > 0 && lK[lJ - 1].v === "-" ? -ly : ly);
                    lK[lJ].v = kU(lK[lJ].t, lK[lJ].v, lP);
                    lK[lJ].t = "t"
                }
            }
            var lC = "";
            for (lJ = 0; lJ !== lK.length; ++lJ) {
                if (lK[lJ] != null) {
                    lC += lK[lJ].v
                }
            }
            return lC
        }
        ld._eval = kX;
        var fs = /\[[=<>]/;
        var kZ = /\[([=<>]*)(-?\d+\.?\d*)\]/;

        function k9(lr, ls) {
            if (ls == null) {
                return false
            }
            var lt = parseFloat(ls[2]);
            switch (ls[1]) {
            case "=":
                if (lr == lt) {
                    return true
                }
                break;
            case ">":
                if (lr > lt) {
                    return true
                }
                break;
            case "<":
                if (lr < lt) {
                    return true
                }
                break;
            case "<>":
                if (lr != lt) {
                    return true
                }
                break;
            case ">=":
                if (lr >= lt) {
                    return true
                }
                break;
            case "<=":
                if (lr <= lt) {
                    return true
                }
                break
            }
            return false
        }

        function lp(lx, lv) {
            var ls = lm(lx);
            var lr = ls.length,
                ly = ls[lr - 1].indexOf("@");
            if (lr < 4 && ly > -1) {
                --lr
            }
            if (ls.length > 4) {
                throw "cannot find right format for |" + ls + "|"
            }
            if (typeof lv !== "number") {
                return [4, ls.length === 4 || ly > -1 ? ls[ls.length - 1] : "@"]
            }
            switch (ls.length) {
            case 1:
                ls = ly > -1 ? ["General", "General", "General", ls[0]] : [ls[0], ls[0], ls[0], "@"];
                break;
            case 2:
                ls = ly > -1 ? [ls[0], ls[0], ls[0], ls[1]] : [ls[0], ls[1], ls[0], "@"];
                break;
            case 3:
                ls = ly > -1 ? [ls[0], ls[1], ls[0], ls[2]] : [ls[0], ls[1], ls[2], "@"];
                break;
            case 4:
                break
            }
            var lu = lv > 0 ? ls[0] : lv < 0 ? ls[1] : ls[2];
            if (ls[0].indexOf("[") === -1 && ls[1].indexOf("[") === -1) {
                return [lr, lu]
            }
            if (ls[0].match(fs) != null || ls[1].match(fs) != null) {
                var lw = ls[0].match(kZ);
                var lt = ls[1].match(kZ);
                return k9(lv, lw) ? [lr, ls[0]] : k9(lv, lt) ? [lr, ls[1]] : [lr, ls[lw != null && lt != null ? 2 : 1]]
            }
            return [lr, lu]
        }

        function lc(lr, ls, lv) {
            k1(lv != null ? lv : (lv = []));
            var lu = "";
            switch (typeof lr) {
            case "string":
                lu = lr;
                break;
            case "number":
                lu = (lv.table != null ? lv.table : lj)[lr];
                break
            }
            if (k8(lu, 0)) {
                return lo(ls, lv)
            }
            var lt = lp(lu, ls);
            if (k8(lt[1])) {
                return lo(ls, lv)
            }
            if (ls === true) {
                ls = "TRUE"
            } else {
                if (ls === false) {
                    ls = "FALSE"
                } else {
                    if (ls === "" || ls == null) {
                        return ""
                    }
                }
            }
            return kX(lt[1], ls, lv, lt[0])
        }
        ld._table = lj;
        ld.load = function k3(ls, lr) {
            lj[lr] = ls
        };
        ld.format = lc;
        ld.get_table = function la() {
            return lj
        };
        ld.load_table = function kQ(ls) {
            for (var lr = 0; lr != 392; ++lr) {
                if (ls[lr] !== undefined) {
                    ld.load(ls[lr], lr)
                }
            }
        }
    };
    gw(cN);
    var gs = 0;
    var h6 = 1;
    var fe = 2;
    var fb = 3;
    var c6 = 4;
    var c2 = 5;
    var ge = 6;
    var bD = 7;
    var hO = 8;
    var eq = 10;
    var du = 11;
    var ab = 12;
    var U = 14;
    var ff = 16;
    var bT = 17;
    var bR = 18;
    var bN = 19;
    var e9 = 20;
    var bL = 21;
    var gk = 22;
    var dM = 23;
    var B = 30;
    var jb = 31;
    var aj = 64;
    var cg = 65;
    var V = 66;
    var jC = 67;
    var cs = 68;
    var hy = 69;
    var h8 = 70;
    var gm = 71;
    var gR = 72;
    var K = 73;
    var d3 = 4096;
    var cD = 8192;
    var cO = 80;
    var j = 81;
    var iF = [cO, j];
    var gd = {
        1: {
            n: "CodePage",
            t: fe
        },
        2: {
            n: "Category",
            t: cO
        },
        3: {
            n: "PresentationFormat",
            t: cO
        },
        4: {
            n: "ByteCount",
            t: fb
        },
        5: {
            n: "LineCount",
            t: fb
        },
        6: {
            n: "ParagraphCount",
            t: fb
        },
        7: {
            n: "SlideCount",
            t: fb
        },
        8: {
            n: "NoteCount",
            t: fb
        },
        9: {
            n: "HiddenCount",
            t: fb
        },
        10: {
            n: "MultimediaClipCount",
            t: fb
        },
        11: {
            n: "Scale",
            t: du
        },
        12: {
            n: "HeadingPair",
            t: d3 | ab
        },
        13: {
            n: "DocParts",
            t: d3 | B
        },
        14: {
            n: "Manager",
            t: cO
        },
        15: {
            n: "Company",
            t: cO
        },
        16: {
            n: "LinksDirty",
            t: du
        },
        17: {
            n: "CharacterCount",
            t: fb
        },
        19: {
            n: "SharedDoc",
            t: du
        },
        22: {
            n: "HLinksChanged",
            t: du
        },
        23: {
            n: "AppVersion",
            t: fb,
            p: "version"
        },
        26: {
            n: "ContentType",
            t: cO
        },
        27: {
            n: "ContentStatus",
            t: cO
        },
        28: {
            n: "Language",
            t: cO
        },
        29: {
            n: "Version",
            t: cO
        },
        255: {}
    };
    var ed = {
        1: {
            n: "CodePage",
            t: fe
        },
        2: {
            n: "Title",
            t: cO
        },
        3: {
            n: "Subject",
            t: cO
        },
        4: {
            n: "Author",
            t: cO
        },
        5: {
            n: "Keywords",
            t: cO
        },
        6: {
            n: "Comments",
            t: cO
        },
        7: {
            n: "Template",
            t: cO
        },
        8: {
            n: "LastAuthor",
            t: cO
        },
        9: {
            n: "RevNumber",
            t: cO
        },
        10: {
            n: "EditTime",
            t: aj
        },
        11: {
            n: "LastPrinted",
            t: aj
        },
        12: {
            n: "CreatedDate",
            t: aj
        },
        13: {
            n: "ModifiedDate",
            t: aj
        },
        14: {
            n: "PageCount",
            t: fb
        },
        15: {
            n: "WordCount",
            t: fb
        },
        16: {
            n: "CharCount",
            t: fb
        },
        17: {
            n: "Thumbnail",
            t: gm
        },
        18: {
            n: "ApplicationName",
            t: B
        },
        19: {
            n: "DocumentSecurity",
            t: fb
        },
        255: {}
    };
    var hF = {
        2147483648: {
            n: "Locale",
            t: bN
        },
        2147483651: {
            n: "Behavior",
            t: bN
        },
        1768515945: {}
    };
    (function () {
        for (var fs in hF) {
            if (hF.hasOwnProperty(fs)) {
                gd[fs] = ed[fs] = hF[fs]
            }
        }
    })();

    function hz(kQ) {
        var kR = kQ.read_shift(4),
            fs = kQ.read_shift(4);
        return new Date(((fs / 10000000 * Math.pow(2, 32) + kR / 10000000) - 11644473600) * 1000).toISOString().replace(/\.000/, "")
    }

    function y(fs, kQ, kR) {
        var kS = fs.read_shift(0, "lpstr");
        if (kR) {
            fs.l += (4 - ((kS.length + 1) & 3)) & 3
        }
        return kS
    }

    function ji(fs, kQ, kR) {
        var kS = fs.read_shift(0, "lpwstr");
        if (kR) {
            fs.l += (4 - ((kS.length + 1) & 3)) & 3
        }
        return kS
    }

    function d2(kQ, fs, kR) {
        if (fs === 31) {
            return ji(kQ)
        }
        return y(kQ, fs, kR)
    }

    function ks(fs, kQ, kR) {
        return d2(fs, kQ, kR === false ? 0 : 4)
    }

    function ga(fs, kQ) {
        if (!kQ) {
            throw new Error("dafuq?")
        }
        return d2(fs, kQ, 0)
    }

    function at(fs) {
        var kS = fs.read_shift(4);
        var kQ = [];
        for (var kR = 0; kR != kS; ++kR) {
            kQ[kR] = fs.read_shift(0, "lpstr")
        }
        return kQ
    }

    function e7(fs) {
        return at(fs)
    }

    function ie(fs) {
        var kR = b4(fs, j);
        var kQ = b4(fs, fb);
        return [kR, kQ]
    }

    function jO(fs) {
        var kS = fs.read_shift(4);
        var kQ = [];
        for (var kR = 0; kR != kS / 2; ++kR) {
            kQ.push(ie(fs))
        }
        return kQ
    }

    function fc(fs) {
        return jO(fs)
    }

    function dp(kR, kU) {
        var kT = kR.read_shift(4);
        var kV = {};
        for (var kS = 0; kS != kT; ++kS) {
            var kQ = kR.read_shift(4);
            var fs = kR.read_shift(4);
            kV[kQ] = kR.read_shift(fs, (kU === 1200 ? "utf16le" : "utf8")).replace(a8, "").replace(a7, "!")
        }
        if (kR.l & 3) {
            kR.l = (kR.l >> 2 + 1) << 2
        }
        return kV
    }

    function ix(kQ) {
        var kR = kQ.read_shift(4);
        var fs = kQ.slice(kQ.l, kQ.l + kR);
        if (kR & 3 > 0) {
            kQ.l += (4 - (kR & 3)) & 3
        }
        return fs
    }

    function M(fs) {
        var kQ = {};
        kQ.Size = fs.read_shift(4);
        fs.l += kQ.Size;
        return kQ
    }

    function R(kQ, fs) {}

    function b4(fs, kT, kS) {
        var kR = fs.read_shift(2),
            kQ, kU = kS || {};
        fs.l += 2;
        if (kT !== ab) {
            if (kR !== kT && iF.indexOf(kT) === -1) {
                throw new Error("Expected type " + kT + " saw " + kR)
            }
        }
        switch (kT === ab ? kR : kT) {
        case 2:
            kQ = fs.read_shift(2, "i");
            if (!kU.raw) {
                fs.l += 2
            }
            return kQ;
        case 3:
            kQ = fs.read_shift(4, "i");
            return kQ;
        case 11:
            return fs.read_shift(4) !== 0;
        case 19:
            kQ = fs.read_shift(4);
            return kQ;
        case 30:
            return y(fs, kR, 4).replace(a8, "");
        case 31:
            return ji(fs);
        case 64:
            return hz(fs);
        case 65:
            return ix(fs);
        case 71:
            return M(fs);
        case 80:
            return ks(fs, kR, !kU.raw && 4).replace(a8, "");
        case 81:
            return ga(fs, kR, 4).replace(a8, "");
        case 4108:
            return fc(fs);
        case 4126:
            return e7(fs);
        default:
            throw new Error("TypedPropertyValue unrecognized type " + kT + " " + kR)
        }
    }

    function kD(fs, kW) {
        var kZ = fs.l;
        var kX = fs.read_shift(4);
        var kQ = fs.read_shift(4);
        var kU = [],
            k1 = 0;
        var k2 = 0;
        var k3 = -1,
            kY;
        for (k1 = 0; k1 != kQ; ++k1) {
            var k4 = fs.read_shift(4);
            var kR = fs.read_shift(4);
            kU[k1] = [k4, kR + kZ]
        }
        var kT = {};
        for (k1 = 0; k1 != kQ; ++k1) {
            if (fs.l !== kU[k1][1]) {
                var kS = true;
                if (k1 > 0 && kW) {
                    switch (kW[kU[k1 - 1][0]].t) {
                    case 2:
                        if (fs.l + 2 === kU[k1][1]) {
                            fs.l += 2;
                            kS = false
                        }
                        break;
                    case 80:
                        if (fs.l <= kU[k1][1]) {
                            fs.l = kU[k1][1];
                            kS = false
                        }
                        break;
                    case 4108:
                        if (fs.l <= kU[k1][1]) {
                            fs.l = kU[k1][1];
                            kS = false
                        }
                        break
                    }
                }
                if (!kW && fs.l <= kU[k1][1]) {
                    kS = false;
                    fs.l = kU[k1][1]
                }
                if (kS) {
                    throw new Error("Read Error: Expected address " + kU[k1][1] + " at " + fs.l + " :" + k1)
                }
            }
            if (kW) {
                var k0 = kW[kU[k1][0]];
                kT[k0.n] = b4(fs, k0.t, {
                    raw: true
                });
                if (k0.p === "version") {
                    kT[k0.n] = String(kT[k0.n] >> 16) + "." + String(kT[k0.n] & 65535)
                }
                if (k0.n == "CodePage") {
                    switch (kT[k0.n]) {
                    case 0:
                        kT[k0.n] = 1252;
                    case 10000:
                    case 1252:
                    case 874:
                    case 1250:
                    case 1251:
                    case 1253:
                    case 1254:
                    case 1255:
                    case 1256:
                    case 1257:
                    case 1258:
                    case 932:
                    case 936:
                    case 949:
                    case 950:
                    case 1200:
                    case 1201:
                    case 65000:
                    case -536:
                    case 65001:
                    case -535:
                        ev(k2 = kT[k0.n]);
                        break;
                    default:
                        throw new Error("Unsupported CodePage: " + kT[k0.n])
                    }
                }
            } else {
                if (kU[k1][0] === 1) {
                    k2 = kT.CodePage = b4(fs, fe);
                    ev(k2);
                    if (k3 !== -1) {
                        var kV = fs.l;
                        fs.l = kU[k3][1];
                        kY = dp(fs, k2);
                        fs.l = kV
                    }
                } else {
                    if (kU[k1][0] === 0) {
                        if (k2 === 0) {
                            k3 = k1;
                            fs.l = kU[k1 + 1][1];
                            continue
                        }
                        kY = dp(fs, k2)
                    } else {
                        var k6 = kY[kU[k1][0]];
                        var k5;
                        switch (fs[fs.l]) {
                        case 65:
                            fs.l += 4;
                            k5 = ix(fs);
                            break;
                        case 30:
                            fs.l += 4;
                            k5 = ks(fs, fs[fs.l - 4]);
                            break;
                        case 31:
                            fs.l += 4;
                            k5 = ks(fs, fs[fs.l - 4]);
                            break;
                        case 3:
                            fs.l += 4;
                            k5 = fs.read_shift(4, "i");
                            break;
                        case 19:
                            fs.l += 4;
                            k5 = fs.read_shift(4);
                            break;
                        case 5:
                            fs.l += 4;
                            k5 = fs.read_shift(8, "f");
                            break;
                        case 11:
                            fs.l += 4;
                            k5 = ig(fs, 4);
                            break;
                        case 64:
                            fs.l += 4;
                            k5 = new Date(hz(fs));
                            break;
                        default:
                            throw new Error("unparsed value: " + fs[fs.l])
                        }
                        kT[k6] = k5
                    }
                }
            }
        }
        fs.l = kZ + kX;
        return kT
    }

    function fa(kQ, kX) {
        var fs = kQ.content;
        hs(fs, 0);
        var kV, kZ, kY, k3, k1;
        fs.chk("feff", "Byte Order: ");
        var kT = fs.read_shift(2);
        var kS = fs.read_shift(4);
        fs.chk(f3.utils.consts.HEADER_CLSID, "CLSID: ");
        kV = fs.read_shift(4);
        if (kV !== 1 && kV !== 2) {
            throw "Unrecognized #Sets: " + kV
        }
        kZ = fs.read_shift(16);
        k3 = fs.read_shift(4);
        if (kV === 1 && k3 !== fs.l) {
            throw "Length mismatch"
        } else {
            if (kV === 2) {
                kY = fs.read_shift(16);
                k1 = fs.read_shift(4)
            }
        }
        var k2 = kD(fs, kX);
        var kR = {
            SystemIdentifier: kS
        };
        for (var kW in k2) {
            kR[kW] = k2[kW]
        }
        kR.FMTID = kZ;
        if (kV === 1) {
            return kR
        }
        if (fs.l !== k1) {
            throw "Length mismatch 2: " + fs.l + " !== " + k1
        }
        var k0;
        try {
            k0 = kD(fs, null)
        } catch (kU) {}
        for (kW in k0) {
            kR[kW] = k0[kW]
        }
        kR.FMTID = [kZ, kY];
        return kR
    }
    var an = true;
    var f3 = (function jj() {
        var k8 = {};
        k8.version = "0.10.2";

        function kZ(ls) {
            var lj = 3;
            var lc = 512;
            var lu = 0;
            var lv = 0;
            var lk = 0;
            var lg = 0;
            var lb = 0;
            var lw = [];
            var la = ls.slice(0, 512);
            hs(la, 0);
            var lm = k6(la);
            lj = lm[0];
            switch (lj) {
            case 3:
                lc = 512;
                break;
            case 4:
                lc = 4096;
                break;
            default:
                throw "Major Version: Expected 3 or 4 saw " + lj
            }
            if (lc !== 512) {
                la = ls.slice(0, lc);
                hs(la, 28)
            }
            var lq = ls.slice(0, lc);
            k0(la, lj);
            var lh = la.read_shift(4, "i");
            if (lj === 3 && lh !== 0) {
                throw "# Directory Sectors: Expected 0 saw " + lh
            }
            la.l += 4;
            lk = la.read_shift(4, "i");
            la.l += 4;
            la.chk("00100000", "Mini Stream Cutoff Size: ");
            lg = la.read_shift(4, "i");
            lu = la.read_shift(4, "i");
            lb = la.read_shift(4, "i");
            lv = la.read_shift(4, "i");
            for (var ln, lp = 0; lp < 109; ++lp) {
                ln = la.read_shift(4, "i");
                if (ln < 0) {
                    break
                }
                lw[lp] = ln
            }
            var ll = kU(ls, lc);
            kV(lb, lv, ll, lc, lw);
            var lo = k2(ll, lk, lw, lc);
            lo[lk].name = "!Directory";
            if (lu > 0 && lg !== k4) {
                lo[lg].name = "!MiniFAT"
            }
            lo[lw[0]].name = "!FAT";
            lo.fat_addrs = lw;
            lo.ssz = lc;
            var le = {}, lt = [],
                fs = [],
                lf = [],
                ld = {};
            k9(lk, lo, ll, lt, lu, le, fs);
            k7(fs, ld, lf, lt);
            var li = lt.shift();
            lt.root = li;
            var lr = kW(lf, lt, fs, le, li);
            return {
                raw: {
                    header: lq,
                    sectors: ll
                },
                FileIndex: fs,
                FullPaths: lf,
                FullPathDir: ld,
                find: lr
            }
        }

        function k6(fs) {
            fs.chk(kS, "Header Signature: ");
            fs.chk(kQ, "CLSID: ");
            var la = fs.read_shift(2, "u");
            return [fs.read_shift(2, "u"), la]
        }

        function k0(la, lb) {
            var fs = 9;
            la.chk("feff", "Byte Order: ");
            switch ((fs = la.read_shift(2))) {
            case 9:
                if (lb !== 3) {
                    throw "MajorVersion/SectorShift Mismatch"
                }
                break;
            case 12:
                if (lb !== 4) {
                    throw "MajorVersion/SectorShift Mismatch"
                }
                break;
            default:
                throw "Sector Shift: Expected 9 or 12 saw " + fs
            }
            la.chk("0600", "Mini Sector Shift: ");
            la.chk("000000000000", "Reserved: ")
        }

        function kU(lb, ld) {
            var fs = Math.ceil(lb.length / ld) - 1;
            var lc = new Array(fs);
            for (var la = 1; la < fs; ++la) {
                lc[la - 1] = lb.slice(la * ld, (la + 1) * ld)
            }
            lc[fs - 1] = lb.slice(fs * ld);
            return lc
        }

        function k7(lh, le, lb, lk) {
            var lg = 0,
                lj = 0,
                lf = 0,
                la = 0,
                ld = 0,
                lc = lk.length;
            var li = new Array(lc),
                fs = new Array(lc);
            for (; lg < lc; ++lg) {
                li[lg] = fs[lg] = lg;
                lb[lg] = lk[lg]
            }
            for (; ld < fs.length; ++ld) {
                lg = fs[ld];
                lj = lh[lg].L;
                lf = lh[lg].R;
                la = lh[lg].C;
                if (li[lg] === lg) {
                    if (lj !== -1 && li[lj] !== lj) {
                        li[lg] = li[lj]
                    }
                    if (lf !== -1 && li[lf] !== lf) {
                        li[lg] = li[lf]
                    }
                }
                if (la !== -1) {
                    li[la] = lg
                }
                if (lj !== -1) {
                    li[lj] = li[lg];
                    fs.push(lj)
                }
                if (lf !== -1) {
                    li[lf] = li[lg];
                    fs.push(lf)
                }
            }
            for (lg = 1; lg !== lc; ++lg) {
                if (li[lg] === lg) {
                    if (lf !== -1 && li[lf] !== lf) {
                        li[lg] = li[lf]
                    } else {
                        if (lj !== -1 && li[lj] !== lj) {
                            li[lg] = li[lj]
                        }
                    }
                }
            }
            for (lg = 1; lg < lc; ++lg) {
                if (lh[lg].type === 0) {
                    continue
                }
                ld = li[lg];
                if (ld === 0) {
                    lb[lg] = lb[0] + "/" + lb[lg]
                } else {
                    while (ld !== 0) {
                        lb[lg] = lb[ld] + "/" + lb[lg];
                        ld = li[ld]
                    }
                }
                li[lg] = 0
            }
            lb[0] += "/";
            for (lg = 1; lg < lc; ++lg) {
                if (lh[lg].type !== 2) {
                    lb[lg] += "/"
                }
                le[lb[lg]] = lh[lg]
            }
        }

        function kW(lf, le, la, fs, lh) {
            var lg = new Array(lf.length);
            var ld = new Array(le.length),
                lc;
            for (lc = 0; lc < lf.length; ++lc) {
                lg[lc] = lf[lc].toUpperCase().replace(a8, "").replace(a7, "!")
            }
            for (lc = 0; lc < le.length; ++lc) {
                ld[lc] = le[lc].toUpperCase().replace(a8, "").replace(a7, "!")
            }
            return function lb(ll) {
                var lj;
                if (ll.charCodeAt(0) === 47) {
                    lj = true;
                    ll = lh + ll
                } else {
                    lj = ll.indexOf("/") !== -1
                }
                var lk = ll.toUpperCase().replace(a8, "").replace(a7, "!");
                var li = lj === true ? lg.indexOf(lk) : ld.indexOf(lk);
                if (li === -1) {
                    return null
                }
                return lj === true ? la[li] : fs[le[li]]
            }
        }

        function kV(lg, la, lh, lf, le) {
            var fs;
            if (lg === k4) {
                if (la !== 0) {
                    throw "DIFAT chain shorter than expected"
                }
            } else {
                if (lg !== -1) {
                    var lc = lh[lg],
                        lb = (lf >>> 2) - 1;
                    for (var ld = 0; ld < lb; ++ld) {
                        if ((fs = f0(lc, ld * 4)) === k4) {
                            break
                        }
                        le.push(fs)
                    }
                    kV(f0(lc, lf - 4), la - 1, lh, lf, le)
                }
            }
        }

        function k5(lk, fs, le, lh, lb) {
            var lc = lk.length;
            var la, lj;
            if (!lb) {
                lb = new Array(lc)
            }
            var li = lh - 1,
                ld, lf;
            la = [];
            lj = [];
            for (ld = fs; ld >= 0;) {
                lb[ld] = true;
                la[la.length] = ld;
                lj.push(lk[ld]);
                var lg = le[Math.floor(ld * 4 / lh)];
                lf = ((ld * 4) & li);
                if (lh < 4 + lf) {
                    throw "FAT boundary crossed: " + ld + " 4 " + lh
                }
                ld = f0(lk[lg], lf)
            }
            return {
                nodes: la,
                data: fJ([lj])
            }
        }

        function k2(ln, lh, lf, lj) {
            var lb = ln.length,
                lm = new Array(lb);
            var la = new Array(lb),
                fs, ll;
            var lk = lj - 1,
                le, ld, lc, lg;
            for (le = 0; le < lb; ++le) {
                fs = [];
                lc = (le + lh);
                if (lc >= lb) {
                    lc -= lb
                }
                if (la[lc] === true) {
                    continue
                }
                ll = [];
                for (ld = lc; ld >= 0;) {
                    la[ld] = true;
                    fs[fs.length] = ld;
                    ll.push(ln[ld]);
                    var li = lf[Math.floor(ld * 4 / lj)];
                    lg = ((ld * 4) & lk);
                    if (lj < 4 + lg) {
                        throw "FAT boundary crossed: " + ld + " 4 " + lj
                    }
                    ld = f0(ln[li], lg)
                }
                lm[lc] = {
                    nodes: fs,
                    data: fJ([ll])
                }
            }
            return lm
        }

        function k9(lj, lo, lp, ln, lk, ld, lh) {
            var la;
            var fs = 0,
                lg = (ln.length ? 2 : 0);
            var lf = lo[lj].data;
            var li = 0,
                lc = 0,
                lb, le, ll, lm;
            for (; li < lf.length; li += 128) {
                la = lf.slice(li, li + 128);
                hs(la, 64);
                lc = la.read_shift(2);
                if (lc === 0) {
                    continue
                }
                lb = iX(la, 0, lc - lg);
                ln.push(lb);
                le = {
                    name: lb,
                    type: la.read_shift(1),
                    color: la.read_shift(1),
                    L: la.read_shift(4, "i"),
                    R: la.read_shift(4, "i"),
                    C: la.read_shift(4, "i"),
                    clsid: la.read_shift(16),
                    state: la.read_shift(4, "i")
                };
                ll = la.read_shift(2) + la.read_shift(2) + la.read_shift(2) + la.read_shift(2);
                if (ll !== 0) {
                    le.ctime = ll;
                    le.ct = kR(la, la.l - 8)
                }
                lm = la.read_shift(2) + la.read_shift(2) + la.read_shift(2) + la.read_shift(2);
                if (lm !== 0) {
                    le.mtime = lm;
                    le.mt = kR(la, la.l - 8)
                }
                le.start = la.read_shift(4, "i");
                le.size = la.read_shift(4, "i");
                if (le.type === 5) {
                    fs = le.start;
                    if (lk > 0 && fs !== k4) {
                        lo[fs].name = "!StreamData"
                    }
                } else {
                    if (le.size >= 4096) {
                        le.storage = "fat";
                        if (lo[le.start] === undefined) {
                            lo[le.start] = k5(lp, le.start, lo.fat_addrs, lo.ssz)
                        }
                        lo[le.start].name = le.name;
                        le.content = lo[le.start].data.slice(0, le.size);
                        hs(le.content, 0)
                    } else {
                        le.storage = "minifat";
                        if (fs !== k4 && le.start !== k4) {
                            le.content = lo[fs].data.slice(le.start * k3, le.start * k3 + le.size);
                            hs(le.content, 0)
                        }
                    }
                }
                ld[lb] = le;
                lh.push(le)
            }
        }

        function kR(fs, la) {
            return new Date((((a5(fs, la + 4) / 10000000) * Math.pow(2, 32) + a5(fs, la) / 10000000) - 11644473600) * 1000)
        }
        var kT;

        function k1(fs, la) {
            if (kT === undefined) {
                kT = require("fs")
            }
            return kZ(kT.readFileSync(fs), la)
        }

        function kX(fs, la) {
            switch (la !== undefined && la.type !== undefined ? la.type : "base64") {
            case "file":
                return k1(fs, la);
            case "base64":
                return kZ(jX(bm.decode(fs)), la);
            case "binary":
                return kZ(jX(fs), la)
            }
            return kZ(fs)
        }
        var k3 = 64;
        var k4 = -2;
        var kS = "d0cf11e0a1b11ae1";
        var kQ = "00000000000000000000000000000000";
        var kY = {
            MAXREGSECT: -6,
            DIFSECT: -4,
            FATSECT: -3,
            ENDOFCHAIN: k4,
            FREESECT: -1,
            HEADER_SIGNATURE: kS,
            HEADER_MINOR_VERSION: "3e00",
            MAXREGSID: -6,
            NOSTREAM: -1,
            HEADER_CLSID: kQ,
            EntryTypes: ["unknown", "storage", "stream", "lockbytes", "property", "root"]
        };
        k8.read = kX;
        k8.parse = kZ;
        k8.utils = {
            ReadShift: d0,
            CheckField: eu,
            prep_blob: hs,
            bconcat: hP,
            consts: kY
        };
        return k8
    })();
    if (typeof require !== "undefined" && typeof module !== "undefined" && typeof an === "undefined") {
        module.exports = f3
    }

    function jG(fs, kQ) {
        fs.read_shift(kQ);
        return
    }

    function cb(fs, kQ) {
        fs.read_shift(kQ);
        return null
    }

    function z(kR, kS, kQ) {
        var fs = [],
            kT = kR.l + kS;
        while (kR.l < kT) {
            fs.push(kQ(kR, kT - kR.l))
        }
        if (kT !== kR.l) {
            throw new Error("Slurp error")
        }
        return fs
    }

    function gS(kS, kT, kR) {
        var kQ = [],
            kU = kS.l + kT,
            fs = kS.read_shift(2);
        while (fs-- !== 0) {
            kQ.push(kR(kS, kU - kS.l))
        }
        if (kU !== kS.l) {
            throw new Error("Slurp error")
        }
        return kQ
    }

    function ig(fs, kQ) {
        return fs.read_shift(kQ) === 1
    }

    function gc(fs) {
        return fs.read_shift(2, "u")
    }

    function fH(fs, kQ) {
        return z(fs, kQ, gc)
    }
    var kt = ig;

    function dE(kQ) {
        var fs = kQ.read_shift(1),
            kR = kQ.read_shift(1);
        return kR === 1 ? fs : fs === 1
    }

    function W(fs, kV, kU) {
        var kR = fs.read_shift(1);
        var kQ = 1,
            kT = "sbcs";
        if (kU === undefined || kU.biff !== 5) {
            var kS = fs.read_shift(1);
            if (kS) {
                kQ = 2;
                kT = "dbcs"
            }
        }
        return kR ? fs.read_shift(kR, kT) : ""
    }

    function id(fs) {
        var kZ = fs.read_shift(2),
            kU = fs.read_shift(1);
        var kQ = kU & 1,
            kW = kU & 4,
            kV = kU & 8;
        var kR = 1 + (kU & 1);
        var kY, kX;
        var k0 = {};
        if (kV) {
            kY = fs.read_shift(2)
        }
        if (kW) {
            kX = fs.read_shift(4)
        }
        var kS = (kU & 1) ? "dbcs" : "sbcs";
        var kT = kZ === 0 ? "" : fs.read_shift(kZ, kS);
        if (kV) {
            fs.l += 4 * kY
        }
        if (kW) {
            fs.l += kX
        }
        k0.t = kT;
        if (!kV) {
            k0.raw = "<t>" + k0.t + "</t>";
            k0.r = k0.t
        }
        return k0
    }

    function cw(kQ, kR, kT) {
        var fs;
        var kS = kQ.read_shift(1);
        if (kS === 0) {
            fs = kQ.read_shift(kR, "sbcs")
        } else {
            fs = kQ.read_shift(kR, "dbcs")
        }
        return fs
    }

    function bF(fs, kS, kR) {
        var kQ = fs.read_shift(kR !== undefined && kR.biff > 0 && kR.biff < 8 ? 1 : 2);
        if (kQ === 0) {
            fs.l++;
            return ""
        }
        return cw(fs, kQ, kR)
    }

    function dY(fs, kS, kR) {
        if (kR.biff !== 5 && kR.biff !== 2) {
            return bF(fs, kS, kR)
        }
        var kQ = fs.read_shift(1);
        if (kQ === 0) {
            fs.l++;
            return ""
        }
        return fs.read_shift(kQ, "sbcs")
    }

    function hJ(fs) {
        return fs.read_shift(8, "f")
    }
    var g1 = jG;
    var ez = function (kR, kT) {
        var kQ = kR.read_shift(4),
            kU = kR.l;
        var fs = false;
        if (kQ > 24) {
            kR.l += kQ - 24;
            if (kR.read_shift(16) === "795881f43b1d7f48af2c825dc4852763") {
                fs = true
            }
            kR.l = kU
        }
        var kS = kR.read_shift((fs ? kQ - 24 : kQ) >> 1, "utf16le").replace(a8, "");
        if (fs) {
            kR.l += 24
        }
        return kS
    };
    var ec = function (fs, kS) {
        var kT = fs.read_shift(2);
        var kR = fs.read_shift(4);
        var kW = fs.read_shift(kR, "cstr");
        var kV = fs.read_shift(2);
        var kY = fs.read_shift(2);
        var kU = fs.read_shift(4);
        if (kU === 0) {
            return kW.replace(/\\/g, "/")
        }
        var kQ = fs.read_shift(4);
        var kZ = fs.read_shift(2);
        var kX = fs.read_shift(kQ >> 1, "utf16le").replace(a8, "");
        return kX
    };
    var X = function (fs, kQ) {
        var kR = fs.read_shift(16);
        kQ -= 16;
        switch (kR) {
        case "e0c9ea79f9bace118c8200aa004ba90b":
            return ez(fs, kQ);
        case "0303000000000000c000000000000046":
            return ec(fs, kQ);
        default:
            throw "unsupported moniker " + kR
        }
    };
    var kl = function (kQ, kR) {
        var fs = kQ.read_shift(4);
        var kS = kQ.read_shift(fs, "utf16le").replace(a8, "");
        return kS
    };
    var iY = function (fs, kQ) {
        var kT = fs.l + kQ;
        var kX = fs.read_shift(4);
        if (kX !== 2) {
            throw new Error("Unrecognized streamVersion: " + kX)
        }
        var kR = fs.read_shift(2);
        fs.l += 2;
        var kZ, k1, kU, kS, k0, kY, kV;
        if (kR & 16) {
            kZ = kl(fs, kT - fs.l)
        }
        if (kR & 128) {
            k1 = kl(fs, kT - fs.l)
        }
        if ((kR & 257) === 257) {
            kU = kl(fs, kT - fs.l)
        }
        if ((kR & 257) === 1) {
            kS = X(fs, kT - fs.l)
        }
        if (kR & 8) {
            k0 = kl(fs, kT - fs.l)
        }
        if (kR & 32) {
            kY = fs.read_shift(16)
        }
        if (kR & 64) {
            kV = hz(fs, 8)
        }
        fs.l = kT;
        var kW = (k1 || kU || kS);
        if (k0) {
            kW += "#" + k0
        }
        return {
            Target: kW
        }
    };

    function a2(kR, kU) {
        var kT = kR.read_shift(1),
            kS = kR.read_shift(1),
            fs = kR.read_shift(1),
            kQ = kR.read_shift(1);
        return [kT, kS, fs, kQ]
    }

    function hL(kQ, kR) {
        var fs = a2(kQ, kR);
        fs[3] = 0;
        return fs
    }
    var gY = [null, "solid", "mediumGray", "darkGray", "lightGray", "darkHorizontal", "darkVertical", "darkDown", "darkUp", "darkGrid", "darkTrellis", "lightHorizontal", "lightVertical", "lightDown", "lightUp", "lightGrid", "lightTrellis", "gray125", "gray0625"];

    function ai(fs) {
        return fs.map(function (kQ) {
            return [(kQ >> 16) & 255, (kQ >> 8) & 255, kQ & 255]
        })
    }
    var bz = ai([0, 16777215, 16711680, 65280, 255, 16776960, 16711935, 65535, 0, 16777215, 16711680, 65280, 255, 16776960, 16711935, 65535, 8388608, 32768, 128, 8421376, 8388736, 32896, 12632256, 8421504, 10066431, 10040166, 16777164, 13434879, 6684774, 16744576, 26316, 13421823, 128, 16711935, 16776960, 65535, 8388736, 8388608, 32896, 255, 52479, 13434879, 13434828, 16777113, 10079487, 16751052, 13408767, 16764057, 3368703, 3394764, 10079232, 16763904, 16750848, 16737792, 6710937, 9868950, 13158, 3381606, 13056, 3355392, 10040064, 10040166, 3355545, 3355443, 16777215, 0]);

    function hH(fs) {
        return fs !== undefined && fs !== null
    }

    function gb(fs) {
        return Object.keys(fs)
    }

    function dK(kT, fs) {
        var kU = {};
        var kQ = gb(kT);
        for (var kS = 0; kS < kQ.length; ++kS) {
            var kR = kQ[kS];
            if (!fs) {
                kU[kT[kR]] = kR
            } else {
                (kU[kT[kR]] = kU[kT[kR]] || []).push(kR)
            }
        }
        return kU
    }

    function ae(fs) {
        for (var kQ = 0, kR = 1; kQ != 3; ++kQ) {
            kR = kR * 256 + (fs[kQ] > 255 ? 255 : fs[kQ] < 0 ? 0 : fs[kQ])
        }
        return kR.toString(16).toUpperCase().substr(1)
    }

    function aL(kQ, kS) {
        var kT = kQ.read_shift(2);
        var kR = kQ.read_shift(2);
        var fs = kQ.read_shift(2);
        return {
            r: kT,
            c: kR,
            ixfe: fs
        }
    }

    function l(kR) {
        var kQ = kR.read_shift(2);
        var fs = kR.read_shift(2);
        kR.l += 8;
        return {
            type: kQ,
            flags: fs
        }
    }

    function gG(fs, kR, kQ) {
        return kR === 0 ? "" : dY(fs, kR, kQ)
    }
    var b5 = ["SHOWALL", "SHOWPLACEHOLDER", "HIDEALL"];
    var bO = gc;

    function ky(kQ, kT) {
        var kS = kQ.read_shift(2),
            fs = kQ.read_shift(2, "i"),
            kR = kQ.read_shift(2, "i");
        return [kS, fs, kR]
    }

    function dL(kT) {
        var kR = kT.slice(kT.l, kT.l + 4);
        var kS = kR[0] & 1,
            kQ = kR[0] & 2;
        kT.l += 4;
        kR[0] &= ~3;
        var fs = kQ === 0 ? gu([0, 0, 0, 0, kR[0], kR[1], kR[2], kR[3]], 0) : f0(kR, 0) >> 2;
        return kS ? fs / 100 : fs
    }

    function hE(kR, kS) {
        var kQ = kR.read_shift(2);
        var fs = dL(kR);
        return [kQ, fs]
    }

    function kN(kR, kS) {
        kR.l += 4;
        kS -= 4;
        var kQ = kR.l + kS;
        var kT = W(kR, kS);
        var fs = kR.read_shift(2);
        kQ -= kR.l;
        if (fs !== kQ) {
            throw "Malformed AddinUdf: padding = " + kQ + " != " + fs
        }
        kR.l += fs;
        return kT
    }

    function fZ(kR, kS) {
        var kQ = kR.read_shift(2);
        var fs = kR.read_shift(2);
        var kU = kR.read_shift(2);
        var kT = kR.read_shift(2);
        return {
            s: {
                c: kU,
                r: kQ
            },
            e: {
                c: kT,
                r: fs
            }
        }
    }

    function aU(kR, kS) {
        var kQ = kR.read_shift(2);
        var fs = kR.read_shift(2);
        var kU = kR.read_shift(1);
        var kT = kR.read_shift(1);
        return {
            s: {
                c: kU,
                r: kQ
            },
            e: {
                c: kT,
                r: fs
            }
        }
    }
    var dx = aU;

    function g(kQ, kS) {
        kQ.l += 4;
        var kR = kQ.read_shift(2);
        var kT = kQ.read_shift(2);
        var fs = kQ.read_shift(2);
        kQ.l += 12;
        return [kT, kR, fs]
    }

    function kv(fs, kR) {
        var kQ = {};
        fs.l += 4;
        fs.l += 16;
        kQ.fSharedNote = fs.read_shift(2);
        fs.l += 4;
        return kQ
    }

    function dh(fs, kR) {
        var kQ = {};
        fs.l += 4;
        fs.cf = fs.read_shift(2);
        return kQ
    }
    var az = {
        21: g,
        19: jG,
        18: function (fs, kQ) {
            fs.l += 12
        },
        17: function (fs, kQ) {
            fs.l += 8
        },
        16: jG,
        15: jG,
        13: kv,
        12: function (fs, kQ) {
            fs.l += 24
        },
        11: function (fs, kQ) {
            fs.l += 10
        },
        10: function (fs, kQ) {
            fs.l += 16
        },
        9: jG,
        8: function (fs, kQ) {
            fs.l += 6
        },
        7: dh,
        6: function (fs, kQ) {
            fs.l += 6
        },
        4: jG,
        0: function (fs, kQ) {
            fs.l += 4
        }
    };

    function eR(fs, kT, kS) {
        var kR = fs.l;
        var kQ = [];
        while (fs.l < kR + kT) {
            var kV = fs.read_shift(2);
            fs.l -= 2;
            try {
                kQ.push(az[kV](fs, kR + kT - fs.l))
            } catch (kU) {
                fs.l = kR + kT;
                return kQ
            }
        }
        if (fs.l != kR + kT) {
            fs.l = kR + kT
        }
        return kQ
    }
    var b0 = gc;

    function jl(fs, kQ) {
        var kR = {};
        kR.BIFFVer = fs.read_shift(2);
        kQ -= 2;
        switch (kR.BIFFVer) {
        case 1536:
        case 1280:
        case 2:
        case 7:
            break;
        default:
            throw "Unexpected BIFF Ver " + kR.BIFFVer
        }
        fs.read_shift(kQ);
        return kR
    }

    function gf(fs, kQ) {
        if (kQ === 0) {
            return 1200
        }
        var kR;
        if ((kR = fs.read_shift(2)) !== 1200) {
            throw "InterfaceHdr codePage " + kR
        }
        return 1200
    }

    function jS(kQ, kS, kR) {
        if (kR.enc) {
            kQ.l += kS;
            return ""
        }
        var fs = kQ.l;
        var kT = bF(kQ, 0, kR);
        kQ.read_shift(kS + fs - kQ.l);
        return kT
    }

    function eT(fs, kT, kS) {
        var kV = fs.read_shift(4);
        var kU = fs.read_shift(1) >> 6;
        var kR = fs.read_shift(1);
        switch (kR) {
        case 0:
            kR = "Worksheet";
            break;
        case 1:
            kR = "Macrosheet";
            break;
        case 2:
            kR = "Chartsheet";
            break;
        case 6:
            kR = "VBAModule";
            break
        }
        var kQ = W(fs, 0, kS);
        if (kQ.length === 0) {
            kQ = "Sheet1"
        }
        return {
            pos: kV,
            hs: kU,
            dt: kR,
            name: kQ
        }
    }

    function aD(fs, kT) {
        var kR = fs.read_shift(4);
        var kS = fs.read_shift(4);
        var kU = [];
        for (var kQ = 0; kQ != kS; ++kQ) {
            kU.push(id(fs))
        }
        kU.Count = kR;
        kU.Unique = kS;
        return kU
    }

    function am(fs, kR) {
        var kQ = {};
        kQ.dsst = fs.read_shift(2);
        fs.l += kR - 2;
        return kQ
    }

    function a0(kQ, kS) {
        var kU = kQ.read_shift(2),
            kR = kQ.read_shift(2),
            kV = kQ.read_shift(2),
            kT = kQ.read_shift(2);
        kQ.read_shift(4);
        var fs = kQ.read_shift(1);
        kQ.read_shift(1);
        kQ.read_shift(2);
        return {
            r: kU,
            c: kR,
            cnt: kV - kR
        }
    }

    function ft(kQ, kR) {
        var kS = l(kQ);
        if (kS.type != 2211) {
            throw "Invalid Future Record " + kS.type
        }
        var fs = kQ.read_shift(4);
        return fs !== 0
    }
    var a9 = cb;

    function jI(fs, kQ) {
        fs.read_shift(2);
        return fs.read_shift(4)
    }

    function j0(fs, kQ) {
        var kS = fs.read_shift(2),
            kT;
        kT = fs.read_shift(2);
        var kR = {
            Unsynced: kS & 1,
            DyZero: (kS & 2) >> 1,
            ExAsc: (kS & 4) >> 2,
            ExDsc: (kS & 8) >> 3
        };
        return [kR, kT]
    }

    function jg(fs, kR) {
        var kQ = fs.read_shift(2),
            kU = fs.read_shift(2),
            kZ = fs.read_shift(2),
            kS = fs.read_shift(2);
        var kT = fs.read_shift(2),
            kY = fs.read_shift(2),
            kV = fs.read_shift(2);
        var kX = fs.read_shift(2),
            kW = fs.read_shift(2);
        return {
            Pos: [kQ, kU],
            Dim: [kZ, kS],
            Flags: kT,
            CurTab: kY,
            FirstTab: kV,
            Selected: kX,
            TabRatio: kW
        }
    }

    function f8(fs, kS, kR) {
        fs.l += 14;
        var kQ = W(fs, 0, kR);
        return kQ
    }

    function eU(kQ, kR) {
        var fs = aL(kQ);
        fs.isst = kQ.read_shift(4);
        return fs
    }

    function gF(kQ, kS, kR) {
        var fs = aL(kQ, 6);
        var kT = bF(kQ, kS - 6, kR);
        fs.val = kT;
        return fs
    }

    function bq(fs, kT, kS) {
        var kQ = fs.read_shift(2);
        var kR = dY(fs, 0, kS);
        return [kQ, kR]
    }

    function f(kQ, kT) {
        var fs = kT === 10 ? 2 : 4;
        var kS = kQ.read_shift(fs),
            kR = kQ.read_shift(fs),
            kV = kQ.read_shift(2),
            kU = kQ.read_shift(2);
        kQ.l += 2;
        return {
            s: {
                r: kS,
                c: kV
            },
            e: {
                r: kR,
                c: kU
            }
        }
    }

    function c7(fs, kR) {
        var kT = fs.read_shift(2),
            kQ = fs.read_shift(2);
        var kS = hE(fs);
        return {
            r: kT,
            c: kQ,
            ixfe: kS[0],
            rknum: kS[1]
        }
    }

    function kB(fs, kS) {
        var kT = fs.l + kS - 2;
        var kV = fs.read_shift(2),
            kQ = fs.read_shift(2);
        var kU = [];
        while (fs.l < kT) {
            kU.push(hE(fs))
        }
        if (fs.l !== kT) {
            throw "MulRK read error"
        }
        var kR = fs.read_shift(2);
        if (kU.length != kR - kQ + 1) {
            throw "MulRK length mismatch"
        }
        return {
            r: kV,
            c: kQ,
            C: kR,
            rkrec: kU
        }
    }

    function i9(kR, kT, kS) {
        var kV = {};
        var kQ = kR.read_shift(4),
            fs = kR.read_shift(4);
        var kW = kR.read_shift(4),
            kU = kR.read_shift(2);
        kV.patternType = gY[kW >> 26];
        kV.icvFore = kU & 127;
        kV.icvBack = (kU >> 7) & 127;
        return kV
    }

    function gB(fs, kQ) {
        return i9(fs, kQ, 0)
    }

    function bP(fs, kQ) {
        return i9(fs, kQ, 1)
    }

    function bw(fs, kQ) {
        var kR = {};
        kR.ifnt = fs.read_shift(2);
        kR.ifmt = fs.read_shift(2);
        kR.flags = fs.read_shift(2);
        kR.fStyle = (kR.flags >> 2) & 1;
        kQ -= 6;
        kR.data = i9(fs, kQ, kR.fStyle);
        return kR
    }

    function cS(fs, kR) {
        fs.l += 4;
        var kQ = [fs.read_shift(2), fs.read_shift(2)];
        if (kQ[0] !== 0) {
            kQ[0]--
        }
        if (kQ[1] !== 0) {
            kQ[1]--
        }
        if (kQ[0] > 7 || kQ[1] > 7) {
            throw "Bad Gutters: " + kQ
        }
        return kQ
    }

    function gD(kQ, kR) {
        var fs = aL(kQ, 6);
        var kS = dE(kQ, 2);
        fs.val = kS;
        fs.t = (kS === true || kS === false) ? "b" : "e";
        return fs
    }

    function jK(kQ, kS) {
        var fs = aL(kQ, 6);
        var kR = hJ(kQ, 8);
        fs.val = kR;
        return fs
    }
    var gg = gG;

    function eQ(kQ, kV, kU) {
        var fs = kQ.l + kV;
        var kT = kQ.read_shift(2);
        var kS = kQ.read_shift(2);
        var kR;
        if (kS >= 1 && kS <= 255) {
            kR = cw(kQ, kS)
        }
        var kW = kQ.read_shift(fs - kQ.l);
        kU.sbcch = kS;
        return [kS, kT, kR, kW]
    }

    function da(kR, kT, kS) {
        var kQ = kR.read_shift(2);
        var fs;
        var kU = {
            fBuiltIn: kQ & 1,
            fWantAdvise: (kQ >>> 1) & 1,
            fWantPict: (kQ >>> 2) & 1,
            fOle: (kQ >>> 3) & 1,
            fOleLink: (kQ >>> 4) & 1,
            cf: (kQ >>> 5) & 1023,
            fIcon: kQ >>> 15 & 1
        };
        if (kS.sbcch === 14849) {
            fs = kN(kR, kT - 2)
        }
        kU.body = fs || kR.read_shift(kT - 2);
        return kU
    }

    function bd(kQ, kT, fs) {
        if (fs.biff < 8) {
            return gF(kQ, kT, fs)
        }
        var kW = kQ.l + kT;
        var kU = kQ.read_shift(2);
        var kS = kQ.read_shift(1);
        var kV = kQ.read_shift(1);
        var kX = kQ.read_shift(2);
        kQ.l += 2;
        var kZ = kQ.read_shift(2);
        kQ.l += 4;
        var kR = cw(kQ, kV, fs);
        var kY = io(kQ, kW - kQ.l, fs, kX);
        return {
            chKey: kS,
            Name: kR,
            rgce: kY
        }
    }

    function c0(kQ, kT, kS) {
        if (kS.biff < 8) {
            return W(kQ, kT, kS)
        }
        var kU = gS(kQ, kT, ky);
        var fs = [];
        if (kS.sbcch === 1025) {
            for (var kR = 0; kR != kU.length; ++kR) {
                fs.push(kS.snames[kU[kR][1]])
            }
            return fs
        } else {
            return kU
        }
    }

    function hG(fs, kS, kR) {
        var kQ = aU(fs, 6);
        fs.l++;
        var kT = fs.read_shift(1);
        kS -= 8;
        return [gU(fs, kS, kR), kT]
    }

    function ja(fs, kS, kR) {
        var kQ = dx(fs, 6);
        fs.l += 6;
        kS -= 12;
        return [kQ, dI(fs, kS, kR, kQ)]
    }

    function E(kQ, kS) {
        var kR = kQ.read_shift(4) !== 0;
        var kT = kQ.read_shift(4) !== 0;
        var fs = kQ.read_shift(4);
        return [kR, kT, fs]
    }

    function dH(kR, kV, kU) {
        if (kU.biff < 8) {
            return
        }
        var kW = kR.read_shift(2),
            kS = kR.read_shift(2);
        var fs = kR.read_shift(2),
            kT = kR.read_shift(2);
        var kQ = dY(kR, 0, kU);
        if (kU.biff < 8) {
            kR.read_shift(1)
        }
        return [{
            r: kW,
            c: kS
        }, kQ, kT, fs]
    }

    function bJ(fs, kR, kQ) {
        return dH(fs, kR, kQ)
    }

    function iN(fs, kQ) {
        var kR = [];
        var kS = fs.read_shift(2);
        while (kS--) {
            kR.push(fZ(fs, kQ))
        }
        return kR
    }

    function cM(fs, kR) {
        var kS = g(fs, 22);
        var kQ = eR(fs, kR - 22, kS[1]);
        return {
            cmo: kS,
            ft: kQ
        }
    }

    function hK(kQ, kR, fs) {
        var k3 = kQ.l;
        try {
            kQ.l += 4;
            var kU = (fs.lastobj || {
                cmo: [0, 0]
            }).cmo[1];
            var kY;
            if ([0, 5, 7, 11, 12, 14].indexOf(kU) == -1) {
                kQ.l += 6
            } else {
                kY = g1(kQ, 6, fs)
            }
            var kT = kQ.read_shift(2);
            var kZ = kQ.read_shift(2);
            var k0 = b0(kQ, 2);
            var kW = kQ.read_shift(2);
            kQ.l += kW;
            var kS = "";
            for (var kV = 1; kV < kQ.lens.length - 1; ++kV) {
                if (kQ.l - k3 != kQ.lens[kV]) {
                    throw "TxO: bad continue record"
                }
                var k1 = kQ[kQ.l];
                var k2 = cw(kQ, kQ.lens[kV + 1] - kQ.lens[kV] - 1);
                kS += k2;
                if (kS.length >= (k1 ? kT : 2 * kT)) {
                    break
                }
            }
            if (kS.length !== kT && kS.length !== kT * 2) {
                throw "cchText: " + kT + " != " + kS.length
            }
            kQ.l = k3 + kR;
            return {
                t: kS
            }
        } catch (kX) {
            kQ.l = k3 + kR;
            return {
                t: kS || ""
            }
        }
    }
    var bI = function (fs, kS) {
        var kR = fZ(fs, 8);
        fs.l += 16;
        var kQ = iY(fs, kS - 24);
        return [kR, kQ]
    };
    var df = function (kQ, kS) {
        var fs = kQ.l + kS;
        kQ.read_shift(2);
        var kR = fZ(kQ, 8);
        var kT = kQ.read_shift((kS - 10) / 2, "dbcs");
        kT = kT.replace(a8, "");
        return [kR, kT]
    };

    function cn(fs, kQ) {
        var kS = [],
            kR;
        kR = fs.read_shift(2);
        kS[0] = h9[kR] || kR;
        kR = fs.read_shift(2);
        kS[1] = h9[kR] || kR;
        return kS
    }

    function eP(kQ, kR) {
        var fs = kQ.read_shift(2);
        var kS = [];
        while (fs-- > 0) {
            kS.push(hL(kQ, 8))
        }
        return kS
    }

    function fo(kQ, kR) {
        var fs = kQ.read_shift(2);
        var kS = [];
        while (fs-- > 0) {
            kS.push(hL(kQ, 8))
        }
        return kS
    }

    function h5(fs, kQ) {
        fs.l += 2;
        var kR = {
            cxfs: 0,
            crc: 0
        };
        kR.cxfs = fs.read_shift(2);
        kR.crc = fs.read_shift(4);
        return kR
    }
    var ju = jG;
    var ho = jG;
    var aN = jG;
    var jd = jG;
    var iO = ig;
    var D = aL;
    var kE = hJ;
    var aA = gc;
    var hD = gc;
    var g8 = hJ;
    var ek = ig;
    var ko = gc;
    var aq = ig;
    var hq = cb;
    var ci = ig;
    var bX = gc;
    var cd = ig;
    var e4 = ig;
    var gi = gc;
    var fi = cb;
    var bH = cb;
    var kw = cb;
    var f2 = cb;
    var C = cb;
    var kP = gc;
    var ku = gg;
    var dm = gc;
    var ik = ig;
    var cB = gg;
    var gC = bO;
    var cI = cb;
    var i = hJ;
    var c5 = cb;
    var jW = ig;
    var cv = gc;
    var av = ig;
    var x = ig;
    var e5 = gc;
    var Q = ig;
    var eX = gc;
    var g0 = ig;
    var hN = ig;
    var cH = hJ;
    var iU = fH;
    var hS = ig;
    var ib = fH;
    var dB = bF;
    var f9 = ig;
    var aZ = hJ;
    var km = ig;
    var fO = ig;
    var aS = ig;
    var u = jG;
    var j7 = jG;
    var kx = jG;
    var cm = jG;
    var jm = jG;
    var cF = jG;
    var eM = jG;
    var ke = jG;
    var fr = jG;
    var bc = jG;
    var cQ = jG;
    var a1 = jG;
    var cc = jG;
    var f6 = jG;
    var aY = jG;
    var jD = jG;
    var a4 = jG;
    var bx = jG;
    var J = jG;
    var fx = jG;
    var iz = jG;
    var fX = jG;
    var iG = jG;
    var ap = jG;
    var bW = jG;
    var j3 = jG;
    var f5 = jG;
    var ch = jG;
    var dQ = jG;
    var e8 = jG;
    var bv = jG;
    var ag = jG;
    var d1 = jG;
    var c4 = jG;
    var hI = jG;
    var b8 = jG;
    var eF = jG;
    var fQ = jG;
    var fT = jG;
    var N = jG;
    var dd = jG;
    var v = jG;
    var gL = jG;
    var cE = jG;
    var af = jG;
    var cK = jG;
    var bB = jG;
    var gE = jG;
    var il = jG;
    var dG = jG;
    var iv = jG;
    var iW = jG;
    var jJ = jG;
    var gK = jG;
    var ah = jG;
    var cG = jG;
    var hg = jG;
    var fB = jG;
    var bp = jG;
    var bn = jG;
    var eV = jG;
    var kF = jG;
    var dq = jG;
    var jZ = jG;
    var dJ = jG;
    var fk = jG;
    var ic = jG;
    var jf = jG;
    var aJ = jG;
    var fh = jG;
    var d9 = jG;
    var c3 = jG;
    var cC = jG;
    var es = jG;
    var hR = jG;
    var cJ = jG;
    var dl = jG;
    var kj = jG;
    var j2 = jG;
    var A = jG;
    var fL = jG;
    var jV = jG;
    var bt = jG;
    var fK = jG;
    var jx = jG;
    var bY = jG;
    var aK = jG;
    var iw = jG;
    var iq = jG;
    var dT = jG;
    var t = jG;
    var aH = jG;
    var gQ = jG;
    var cV = jG;
    var b = jG;
    var jw = jG;
    var fn = jG;
    var kq = jG;
    var gI = jG;
    var ia = jG;
    var jc = jG;
    var ao = jG;
    var j9 = jG;
    var f7 = jG;
    var bl = jG;
    var iu = jG;
    var hd = jG;
    var hv = jG;
    var gx = jG;
    var c8 = jG;
    var eL = jG;
    var aF = jG;
    var jY = bF;
    var kI = jG;
    var hW = jG;
    var fY = jG;
    var r = jG;
    var dC = jG;
    var Z = jG;
    var fI = jG;
    var dS = jG;
    var eg = jG;
    var fP = jG;
    var i0 = jG;
    var m = jG;
    var hb = jG;
    var hf = jG;
    var kz = jG;
    var hp = jG;
    var gH = jG;
    var hY = jG;
    var ce = jG;
    var Y = jG;
    var fS = jG;
    var bC = jG;
    var cl = jG;
    var gj = jG;
    var au = jG;
    var ip = jG;
    var gV = jG;
    var dU = jG;
    var jk = jG;
    var hM = jG;
    var ii = jG;
    var ea = jG;
    var dD = jG;
    var i5 = jG;
    var aC = jG;
    var aT = jG;
    var j8 = jG;
    var gO = jG;
    var dO = jG;
    var iy = jG;
    var aE = jG;
    var iM = jG;
    var eh = jG;
    var dN = jG;
    var cy = jG;
    var iJ = jG;
    var j5 = jG;
    var dW = jG;
    var g7 = jG;
    var iD = jG;
    var iI = jG;
    var ck = jG;
    var hT = jG;
    var cU = jG;
    var fd = jG;
    var dV = jG;
    var ew = jG;
    var j4 = jG;
    var d7 = jG;
    var b1 = jG;
    var db = jG;
    var gZ = jG;
    var bi = jG;
    var aO = jG;
    var eb = jG;
    var bG = jG;
    var fU = jG;
    var fm = jG;
    var ht = jG;
    var aP = jG;
    var a6 = jG;
    var hU = jG;
    var dv = jG;
    var bo = jG;
    var fp = jG;
    var eD = jG;
    var fM = jG;
    var h7 = jG;
    var kG = jG;
    var ep = jG;
    var d8 = jG;
    var e0 = jG;
    var jz = jG;
    var ka = jG;
    var be = jG;
    var bj = jG;
    var by = jG;
    var ey = jG;
    var hl = jG;
    var bk = jG;
    var hV = jG;
    var fV = jG;
    var q = jG;
    var dP = jG;
    var el = jG;
    var eC = jG;
    var jT = jG;
    var fw = jG;
    var kk = jG;
    var bA = jG;
    var k = jG;
    var ar = jG;
    var jv = jG;
    var eZ = jG;
    var aa = jG;
    var eE = jG;
    var h = jG;
    var a3 = jG;
    var ki = jG;
    var n = jG;
    var F = jG;
    var bZ = jG;
    var c1 = jG;
    var P = jG;
    var aW = jG;
    var fl = jG;
    var S = jG;
    var gn = jG;
    var eK = jG;
    var gA = jG;
    var bE = jG;
    var jL = jG;
    var gr = jG;
    var di = jG;
    var ds = jG;
    var kr = jG;
    var fW = jG;
    var eO = jG;
    var ax = jG;
    var d4 = jG;
    var eN = jG;
    var cX = jG;
    var iT = jG;
    var j6 = jG;
    var g2 = jG;
    var kc = jG;
    var fA = jG;
    var gT = jG;
    var bu = jG;
    var ej = jG;
    var iZ = jG;
    var dc = jG;
    var e2 = jG;
    var jy = jG;

    function br(kQ) {
        var fs = kQ.read_shift(1);
        return kQ.read_shift(fs, "sbcs")
    }

    function jE(kQ, kS, kR) {
        var fs = aL(kQ, 6);
        ++kQ.l;
        var kT = dY(kQ, kS - 7, kR);
        fs.val = kT;
        return fs
    }

    function d(kQ, kT, kS) {
        var fs = aL(kQ, 6);
        ++kQ.l;
        var kR = hJ(kQ, 8);
        fs.val = kR;
        return fs
    }
    var ak = function (fs) {
        return String.fromCharCode(fs)
    };
    var iL = /([\w:]+)=((?:")([^"]*)(?:")|(?:')([^']*)(?:'))/g;
    var em = /([\w:]+)=((?:")(?:[^"]*)(?:")|(?:')(?:[^']*)(?:'))/;

    function aB(kX, fs) {
        var kT = kX.split(/\s+/);
        var kU = [];
        if (!fs) {
            kU[0] = kT[0]
        }
        if (kT.length === 1) {
            return kU
        }
        var kQ = kX.match(iL),
            kV, kR, kW, kS;
        if (kQ) {
            for (kS = 0; kS != kQ.length; ++kS) {
                kV = kQ[kS].match(em);
                if ((kR = kV[1].indexOf(":")) === -1) {
                    kU[kV[1]] = kV[2].substr(1, kV[2].length - 2)
                } else {
                    if (kV[1].substr(0, 6) === "xmlns:") {
                        kW = "xmlns" + kV[1].substr(6)
                    } else {
                        kW = kV[1].substr(kR + 1)
                    }
                    kU[kW] = kV[2].substr(1, kV[2].length - 2)
                }
            }
        }
        return kU
    }

    function b3(kQ) {
        var kV = kQ.split(/\s+/);
        var kU = {};
        if (kV.length === 1) {
            return kU
        }
        var fs = kQ.match(iL),
            kW, kS, kR, kT;
        if (fs) {
            for (kT = 0; kT != fs.length; ++kT) {
                kW = fs[kT].match(em);
                if ((kS = kW[1].indexOf(":")) === -1) {
                    kU[kW[1]] = kW[2].substr(1, kW[2].length - 2)
                } else {
                    if (kW[1].substr(0, 6) === "xmlns:") {
                        kR = "xmlns" + kW[1].substr(6)
                    } else {
                        kR = kW[1].substr(kS + 1)
                    }
                    kU[kR] = kW[2].substr(1, kW[2].length - 2)
                }
            }
        }
        return kU
    }
    var g5 = {
        "&quot;": '"',
        "&apos;": "'",
        "&gt;": ">",
        "&lt;": "<",
        "&amp;": "&"
    };
    var ba = dK(g5);
    var e = "&<>'\"".split("");
    var hh = '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>\r\n';
    var p = {};
    var kh = function (kR, kS) {
        var fs;
        if (typeof kS !== "undefined") {
            fs = kS
        } else {
            if (typeof require !== "undefined") {
                try {
                    fs = require("crypto")
                } catch (kQ) {
                    fs = null
                }
            }
        }
        kR.rc4 = function (kX, kY) {
            var kW = new Array(256);
            var kZ = 0,
                kV = 0,
                kT = 0,
                kU = 0;
            for (kV = 0; kV != 256; ++kV) {
                kW[kV] = kV
            }
            for (kV = 0; kV != 256; ++kV) {
                kT = (kT + kW[kV] + (kX[kV % kX.length]).charCodeAt(0)) & 255;
                kU = kW[kV];
                kW[kV] = kW[kT];
                kW[kT] = kU
            }
            kV = kT = 0;
            out = Buffer(kY.length);
            for (kZ = 0; kZ != kY.length; ++kZ) {
                kV = (kV + 1) & 255;
                kT = (kT + kW[kV]) % 256;
                kU = kW[kV];
                kW[kV] = kW[kT];
                kW[kT] = kU;
                out[kZ] = (kY[kZ] ^ kW[(kW[kV] + kW[kT]) & 255])
            }
            return out
        };
        if (fs) {
            kR.md5 = function (kT) {
                return fs.createHash("md5").update(kT).digest("hex")
            }
        } else {
            kR.md5 = function (kT) {
                throw "unimplemented"
            }
        }
    };
    kh(p, typeof crypto !== "undefined" ? crypto : undefined);

    function d6(fs) {
        if (typeof cptable !== "undefined") {
            return cptable.utils.encode(1252, fs)
        }
        return fs.split("").map(function (kQ) {
            return kQ.charCodeAt(0)
        })
    }

    function i3(fs, kQ) {
        var kR = {};
        kR.Major = fs.read_shift(2);
        kR.Minor = fs.read_shift(2);
        return kR
    }

    function aw(fs, kR) {
        var kS = {};
        kS.Flags = fs.read_shift(4);
        var kQ = fs.read_shift(4);
        if (kQ !== 0) {
            throw "Unrecognized SizeExtra: " + kQ
        }
        kS.AlgID = fs.read_shift(4);
        switch (kS.AlgID) {
        case 0:
        case 26625:
        case 26126:
        case 26127:
        case 26128:
            break;
        default:
            throw "Unrecognized encryption algorithm: " + kS.AlgID
        }
        jG(fs, kR - 12);
        return kS
    }

    function fN(fs, kQ) {
        return jG(fs, kQ)
    }

    function gl(fs, kR) {
        var kT = {};
        var kQ = kT.EncryptionVersionInfo = i3(fs, 4);
        kR -= 4;
        if (kQ.Minor != 2) {
            throw "unrecognized minor version code: " + kQ.Minor
        }
        if (kQ.Major > 4 || kQ.Major < 2) {
            throw "unrecognized major version code: " + kQ.Major
        }
        kT.Flags = fs.read_shift(4);
        kR -= 4;
        var kS = fs.read_shift(4);
        kR -= 4;
        kT.EncryptionHeader = aw(fs, kS);
        kR -= kS;
        kT.EncryptionVerifier = fN(fs, kR);
        return kT
    }

    function ha(fs, kR) {
        var kS = {};
        var kQ = kS.EncryptionVersionInfo = i3(fs, 4);
        kR -= 4;
        if (kQ.Major != 1 || kQ.Minor != 1) {
            throw "unrecognized version code " + kQ.Major + " : " + kQ.Minor
        }
        kS.Salt = fs.read_shift(16);
        kS.EncryptedVerifier = fs.read_shift(16);
        kS.EncryptedVerifierHash = fs.read_shift(16);
        return kS
    }

    function cY(kW) {
        var kS = 0,
            kX;
        var kV = d6(kW);
        var kU = kV.length + 1,
            kR, kY;
        var kT, kQ, fs;
        kX = dr(kU);
        kX[0] = kV.length;
        for (kR = 1; kR != kU; ++kR) {
            kX[kR] = kV[kR - 1]
        }
        for (kR = kU - 1; kR >= 0; --kR) {
            kY = kX[kR];
            kT = ((kS & 16384) === 0) ? 0 : 1;
            kQ = (kS << 1) & 32767;
            fs = kT | kQ;
            kS = fs ^ kY
        }
        return kS ^ 52811
    }
    var cP = (function () {
        var kT = [187, 255, 255, 186, 255, 255, 185, 128, 0, 190, 15, 0, 191, 15, 0];
        var kQ = [57840, 7439, 52380, 33984, 4364, 3600, 61902, 12606, 6258, 57657, 54287, 34041, 10252, 43370, 20163];
        var fs = [44796, 19929, 39858, 10053, 20106, 40212, 10761, 31585, 63170, 64933, 60267, 50935, 40399, 11199, 17763, 35526, 1453, 2906, 5812, 11624, 23248, 885, 1770, 3540, 7080, 14160, 28320, 56640, 55369, 41139, 20807, 41614, 21821, 43642, 17621, 28485, 56970, 44341, 19019, 38038, 14605, 29210, 60195, 50791, 40175, 10751, 21502, 43004, 24537, 18387, 36774, 3949, 7898, 15796, 31592, 63184, 47201, 24803, 49606, 37805, 14203, 28406, 56812, 17824, 35648, 1697, 3394, 6788, 13576, 27152, 43601, 17539, 35078, 557, 1114, 2228, 4456, 30388, 60776, 51953, 34243, 7079, 14158, 28316, 14128, 28256, 56512, 43425, 17251, 34502, 7597, 13105, 26210, 52420, 35241, 883, 1766, 3532, 4129, 8258, 16516, 33032, 4657, 9314, 18628];
        var kU = function (kV) {
            return ((kV / 2) | (kV * 128)) & 255
        };
        var kR = function (kW, kV) {
            return kU(kW ^ kV)
        };
        var kS = function (kY) {
            var kZ = kQ[kY.length - 1];
            var k0 = 104;
            for (var kX = kY.length - 1; kX >= 0; --kX) {
                var kV = kY[kX];
                for (var kW = 0; kW != 7; ++kW) {
                    if (kV & 64) {
                        kZ ^= fs[k0]
                    }
                    kV *= 2;
                    --k0
                }
            }
            return kZ
        };
        return function (k2) {
            var k1 = d6(k2);
            var k0 = kS(k1);
            var kY = k1.length;
            var kW = dr(16);
            for (var kZ = 0; kZ != 16; ++kZ) {
                kW[kZ] = 0
            }
            var kV, k3, kX;
            if ((kY & 1) === 1) {
                kV = k0 >> 8;
                kW[kY] = kR(kT[0], kV);
                --kY;
                kV = k0 & 255;
                k3 = k1[k1.length - 1];
                kW[kY] = kR(k3, kV)
            }
            while (kY > 0) {
                --kY;
                kV = k0 >> 8;
                kW[kY] = kR(k1[kY], kV);
                --kY;
                kV = k0 & 255;
                kW[kY] = kR(k1[kY], kV)
            }
            kY = 15;
            kX = 15 - k1.length;
            while (kX > 0) {
                kV = k0 >> 8;
                kW[kY] = kR(kT[kX], kV);
                --kY;
                --kX;
                kV = k0 & 255;
                kW[kY] = kR(k1[kY], kV);
                --kY;
                --kX
            }
            return kW
        }
    })();
    var gX = function (kQ, kR, kS, kU, kT) {
        if (!kT) {
            kT = kR
        }
        if (!kU) {
            kU = cP(kQ)
        }
        var kV, fs;
        for (kV = 0; kV != kR.length; ++kV) {
            fs = kR[kV];
            fs ^= kU[kS];
            fs = ((fs >> 5) | (fs << 3)) & 255;
            kT[kV] = fs;
            ++kS
        }
        return [kT, kS, kU]
    };
    var dA = function (fs) {
        var kQ = 0,
            kR = cP(fs);
        return function (kS) {
            var kT = gX(null, kS, kQ, kR);
            kQ = kT[1];
            return kT[0]
        }
    };

    function iV(fs, kS, kR, kQ) {
        var kT = {
            key: gc(fs),
            verificationBytes: gc(fs)
        };
        if (kR.password) {
            kT.verifier = cY(kR.password)
        }
        kQ.valid = kT.verificationBytes === kT.verifier;
        if (kQ.valid) {
            kQ.insitu_decrypt = dA(kR.password)
        }
        return kT
    }

    function bf(kQ, kR, fs) {
        var kS = fs || {};
        kS.Info = kQ.read_shift(2);
        kQ.l -= 2;
        if (kS.Info === 1) {
            kS.Data = ha(kQ, kR)
        } else {
            kS.Data = gl(kQ, kR)
        }
        return kS
    }

    function eI(fs, kR, kQ) {
        var kS = {
            Type: fs.read_shift(2)
        };
        if (kS.Type) {
            bf(fs, kR - 2, kS)
        } else {
            iV(fs, kR - 2, kQ, kS)
        }
        return kS
    }

    function iK(fs) {
        return function (kQ, kR) {
            kQ.l += fs;
            return
        }
    }

    function hQ(fs, kQ) {
        fs.l += 1;
        return
    }

    function kO(fs, kQ) {
        var kR = fs.read_shift(2);
        return [kR & 16383, (kR >> 14) & 1, (kR >> 15) & 1]
    }

    function eH(fs, kS) {
        var kR = fs.read_shift(2),
            kQ = fs.read_shift(2);
        var kU = kO(fs, 2);
        var kT = kO(fs, 2);
        return {
            s: {
                r: kR,
                c: kU[0],
                cRel: kU[1],
                rRel: kU[2]
            },
            e: {
                r: kQ,
                c: kT[0],
                cRel: kT[1],
                rRel: kT[2]
            }
        }
    }

    function fD(fs, kS) {
        var kR = fs.read_shift(2),
            kQ = fs.read_shift(2);
        var kU = kO(fs, 2);
        var kT = kO(fs, 2);
        return {
            s: {
                r: kR,
                c: kU[0],
                cRel: kU[1],
                rRel: kU[2]
            },
            e: {
                r: kQ,
                c: kT[0],
                cRel: kT[1],
                rRel: kT[2]
            }
        }
    }

    function fy(fs, kR) {
        var kQ = fs.read_shift(2);
        var kS = kO(fs, 2);
        return {
            r: kQ,
            c: kS[0],
            cRel: kS[1],
            rRel: kS[2]
        }
    }

    function fg(kQ, kT) {
        var kS = kQ.read_shift(2);
        var fs = kQ.read_shift(2);
        var kU = (fs & 32768) >> 15,
            kR = (fs & 16384) >> 14;
        fs &= 16383;
        if (kU !== 0) {
            while (fs >= 256) {
                fs -= 256
            }
        }
        return {
            r: kS,
            c: fs,
            cRel: kU,
            rRel: kR
        }
    }

    function gy(fs, kS) {
        var kQ = (fs[fs.l++] & 96) >> 5;
        var kR = eH(fs, 8);
        return [kQ, kR]
    }

    function jM(kQ, kT) {
        var kR = (kQ[kQ.l++] & 96) >> 5;
        var fs = kQ.read_shift(2);
        var kS = eH(kQ, 8);
        return [kR, fs, kS]
    }

    function bU(fs, kR) {
        var kQ = (fs[fs.l++] & 96) >> 5;
        fs.l += 8;
        return [kQ]
    }

    function f4(kQ, kS) {
        var kR = (kQ[kQ.l++] & 96) >> 5;
        var fs = kQ.read_shift(2);
        kQ.l += 8;
        return [kR, fs]
    }

    function gh(fs, kS) {
        var kQ = (fs[fs.l++] & 96) >> 5;
        var kR = fD(fs, 8);
        return [kQ, kR]
    }

    function et(fs, kR) {
        var kQ = (fs[fs.l++] & 96) >> 5;
        fs.l += 7;
        return [kQ]
    }

    function gM(fs, kQ) {
        var kS = fs[fs.l + 1] & 1;
        var kR = 1;
        fs.l += 4;
        return [kS, kR]
    }

    function aR(fs, kR) {
        fs.l += 2;
        var kT = fs.read_shift(2);
        var kS = [];
        for (var kQ = 0; kQ <= kT; ++kQ) {
            kS.push(fs.read_shift(2))
        }
        return kS
    }

    function hr(kQ, kR) {
        var fs = (kQ[kQ.l + 1] & 255) ? 1 : 0;
        kQ.l += 2;
        return [fs, kQ.read_shift(2)]
    }

    function dZ(fs, kR) {
        var kQ = (fs[fs.l + 1] & 255) ? 1 : 0;
        fs.l += 2;
        return [kQ, fs.read_shift(2)]
    }

    function b9(fs, kQ) {
        var kR = (fs[fs.l + 1] & 255) ? 1 : 0;
        fs.l += 4;
        return [kR]
    }

    function ct(fs, kS) {
        var kR = fs.read_shift(1),
            kQ = fs.read_shift(1);
        return [kR, kQ]
    }

    function g3(fs, kQ) {
        fs.read_shift(2);
        return ct(fs, 2)
    }

    function cq(fs, kQ) {
        fs.read_shift(2);
        return ct(fs, 2)
    }

    function bb(fs, kR) {
        var kT = fs[fs.l] & 31;
        var kQ = (fs[fs.l] & 96) >> 5;
        fs.l += 1;
        var kS = fy(fs, 4);
        return [kQ, kS]
    }

    function i4(fs, kR) {
        var kT = fs[fs.l] & 31;
        var kQ = (fs[fs.l] & 96) >> 5;
        fs.l += 1;
        var kS = fg(fs, 4);
        return [kQ, kS]
    }

    function cz(kQ, kS) {
        var kU = kQ[kQ.l] & 31;
        var kR = (kQ[kQ.l] & 96) >> 5;
        kQ.l += 1;
        var fs = kQ.read_shift(2);
        var kT = fy(kQ, 4);
        return [kR, fs, kT]
    }

    function fu(fs, kR) {
        var kT = fs[fs.l] & 31;
        var kQ = (fs[fs.l] & 96) >> 5;
        fs.l += 1;
        var kS = fs.read_shift(2);
        return [dk[kS], iC[kS]]
    }

    function ei(fs, kS) {
        fs.l++;
        var kQ = fs.read_shift(1),
            kR = jt(fs);
        return [kQ, (kR[0] === 0 ? iC : aG)[kR[1]]]
    }

    function jt(fs, kQ) {
        return [fs[fs.l + 1] >> 7, fs.read_shift(2) & 32767]
    }
    var o = iK(4);
    var aQ = hQ;

    function j1(fs, kR) {
        fs.l++;
        var kS = fs.read_shift(2);
        var kQ = fs.read_shift(2);
        return [kS, kQ]
    }

    function L(fs, kQ) {
        fs.l++;
        return e6[fs.read_shift(1)]
    }

    function gp(fs, kQ) {
        fs.l++;
        return fs.read_shift(2)
    }

    function jP(fs, kQ) {
        fs.l++;
        return fs.read_shift(1) !== 0
    }

    function dn(fs, kQ) {
        fs.l++;
        return hJ(fs, 8)
    }

    function ca(fs, kQ) {
        fs.l++;
        return W(fs)
    }

    function bV(fs) {
        var kQ = [];
        switch ((kQ[0] = fs.read_shift(1))) {
        case 4:
            kQ[1] = ig(fs, 1) ? "TRUE" : "FALSE";
            fs.l += 7;
            break;
        case 16:
            kQ[1] = e6[fs[fs.l]];
            fs.l += 8;
            break;
        case 0:
            fs.l += 8;
            break;
        case 1:
            kQ[1] = hJ(fs, 8);
            break;
        case 2:
            kQ[1] = bF(fs);
            break
        }
        return kQ
    }

    function kH(fs, kT) {
        var kS = fs.read_shift(2);
        var kQ = [];
        for (var kR = 0; kR != kS; ++kR) {
            kQ.push(fZ(fs, 8))
        }
        return kQ
    }

    function kA(fs) {
        var kT = 1 + fs.read_shift(1);
        var kS = 1 + fs.read_shift(2);
        for (var kR = 0, kU = []; kR != kS && (kU[kR] = []); ++kR) {
            for (var kQ = 0; kQ != kT; ++kQ) {
                kU[kR][kQ] = bV(fs)
            }
        }
        return kU
    }

    function iR(kQ, kS) {
        var kR = (kQ.read_shift(1) >>> 5) & 3;
        var fs = kQ.read_shift(4);
        return [kR, 0, fs]
    }

    function al(kR, kT) {
        var kS = (kR.read_shift(1) >>> 5) & 3;
        var kQ = kR.read_shift(2);
        var fs = kR.read_shift(4);
        return [kS, kQ, fs]
    }

    function cZ(fs, kR) {
        var kQ = (fs.read_shift(1) >>> 5) & 3;
        fs.l += 4;
        var kS = fs.read_shift(2);
        return [kQ, kS]
    }

    function bK(fs, kR) {
        var kQ = (fs.read_shift(1) >>> 5) & 3;
        var kS = fs.read_shift(2);
        return [kQ, kS]
    }

    function fR(fs, kR) {
        var kQ = (fs.read_shift(1) >>> 5) & 3;
        fs.l += 4;
        return [kQ]
    }
    var je = hQ;
    var jo = hQ;
    var jh = hQ;
    var iP = hQ;
    var iH = hQ;
    var c = hQ;
    var he = hQ;
    var g6 = hQ;
    var i8 = hQ;
    var kJ = hQ;
    var gP = hQ;
    var gN = hQ;
    var eB = hQ;
    var i1 = hQ;
    var jn = hQ;
    var b2 = hQ;
    var hB = hQ;
    var f1 = hQ;
    var h0 = hQ;
    var cx = jG;
    var eJ = jG;
    var bQ = jG;
    var kL = jG;
    var b6 = {
        1: {
            n: "PtgExp",
            f: j1
        },
        2: {
            n: "PtgTbl",
            f: kL
        },
        3: {
            n: "PtgAdd",
            f: je
        },
        4: {
            n: "PtgSub",
            f: b2
        },
        5: {
            n: "PtgMul",
            f: kJ
        },
        6: {
            n: "PtgDiv",
            f: jo
        },
        7: {
            n: "PtgPower",
            f: i1
        },
        8: {
            n: "PtgConcat",
            f: aQ
        },
        9: {
            n: "PtgLt",
            f: g6
        },
        10: {
            n: "PtgLe",
            f: he
        },
        11: {
            n: "PtgEq",
            f: jh
        },
        12: {
            n: "PtgGe",
            f: iP
        },
        13: {
            n: "PtgGt",
            f: iH
        },
        14: {
            n: "PtgNe",
            f: gP
        },
        15: {
            n: "PtgIsect",
            f: c
        },
        16: {
            n: "PtgUnion",
            f: f1
        },
        17: {
            n: "PtgRange",
            f: jn
        },
        18: {
            n: "PtgUplus",
            f: h0
        },
        19: {
            n: "PtgUminus",
            f: hB
        },
        20: {
            n: "PtgPercent",
            f: eB
        },
        21: {
            n: "PtgParen",
            f: gN
        },
        22: {
            n: "PtgMissArg",
            f: i8
        },
        23: {
            n: "PtgStr",
            f: ca
        },
        28: {
            n: "PtgErr",
            f: L
        },
        29: {
            n: "PtgBool",
            f: jP
        },
        30: {
            n: "PtgInt",
            f: gp
        },
        31: {
            n: "PtgNum",
            f: dn
        },
        32: {
            n: "PtgArray",
            f: et
        },
        33: {
            n: "PtgFunc",
            f: fu
        },
        34: {
            n: "PtgFuncVar",
            f: ei
        },
        35: {
            n: "PtgName",
            f: iR
        },
        36: {
            n: "PtgRef",
            f: bb
        },
        37: {
            n: "PtgArea",
            f: gy
        },
        38: {
            n: "PtgMemArea",
            f: cZ
        },
        39: {
            n: "PtgMemErr",
            f: cx
        },
        40: {
            n: "PtgMemNoMem",
            f: eJ
        },
        41: {
            n: "PtgMemFunc",
            f: bK
        },
        42: {
            n: "PtgRefErr",
            f: fR
        },
        43: {
            n: "PtgAreaErr",
            f: bU
        },
        44: {
            n: "PtgRefN",
            f: i4
        },
        45: {
            n: "PtgAreaN",
            f: gh
        },
        57: {
            n: "PtgNameX",
            f: al
        },
        58: {
            n: "PtgRef3d",
            f: cz
        },
        59: {
            n: "PtgArea3d",
            f: jM
        },
        60: {
            n: "PtgRefErr3d",
            f: bQ
        },
        61: {
            n: "PtgAreaErr3d",
            f: f4
        },
        255: {}
    };
    var aX = {
        64: 32,
        96: 32,
        65: 33,
        97: 33,
        66: 34,
        98: 34,
        67: 35,
        99: 35,
        68: 36,
        100: 36,
        69: 37,
        101: 37,
        70: 38,
        102: 38,
        71: 39,
        103: 39,
        72: 40,
        104: 40,
        73: 41,
        105: 41,
        74: 42,
        106: 42,
        75: 43,
        107: 43,
        76: 44,
        108: 44,
        77: 45,
        109: 45,
        89: 57,
        121: 57,
        90: 58,
        122: 58,
        91: 59,
        123: 59,
        92: 60,
        124: 60,
        93: 61,
        125: 61
    };
    (function () {
        for (var fs in aX) {
            b6[fs] = b6[aX[fs]]
        }
    })();
    var im = {};
    var ij = {
        1: {
            n: "PtgAttrSemi",
            f: b9
        },
        2: {
            n: "PtgAttrIf",
            f: dZ
        },
        4: {
            n: "PtgAttrChoose",
            f: aR
        },
        8: {
            n: "PtgAttrGoto",
            f: hr
        },
        16: {
            n: "PtgAttrSum",
            f: o
        },
        32: {
            n: "PtgAttrBaxcel",
            f: gM
        },
        64: {
            n: "PtgAttrSpace",
            f: g3
        },
        65: {
            n: "PtgAttrSpaceSemi",
            f: cq
        },
        255: {}
    };
    var cj = /(^|[^A-Za-z])R(\[?)(-?\d+|)\]?C(\[?)(-?\d+|)\]?/g;
    var ee;

    function dy(kQ, fs, kW, kV, kT, kS) {
        var kR = kV.length > 0 ? parseInt(kV, 10) | 0 : 0,
            kU = kS.length > 0 ? parseInt(kS, 10) | 0 : 0;
        if (kU < 0 && kT.length === 0) {
            kU = 0
        }
        if (kT.length > 0) {
            kU += ee.c
        }
        if (kW.length > 0) {
            kR += ee.r
        }
        return fs + cA(kU) + jH(kR)
    }

    function hm(fs, kQ) {
        ee = kQ;
        return fs.replace(cj, dy)
    }

    function fj(kR, kU, kT) {
        var fs = aL(kR, 6);
        var kW = ef(kR, 8);
        var kQ = kR.read_shift(1);
        kR.read_shift(1);
        var kV = kR.read_shift(4);
        var kS = "";
        if (kT.biff === 5) {
            kR.l += kU - 20
        } else {
            kS = i7(kR, kU - 20, kT)
        }
        return {
            cell: fs,
            val: kW[0],
            formula: kS,
            shared: (kQ >> 3) & 1,
            tt: kW[1]
        }
    }

    function ef(kQ) {
        var fs;
        if (eS(kQ, kQ.l + 6) !== 65535) {
            return [hJ(kQ), "n"]
        }
        switch (kQ[kQ.l]) {
        case 0:
            kQ.l += 8;
            return ["String", "s"];
        case 1:
            fs = kQ[kQ.l + 2] === 1;
            kQ.l += 8;
            return [fs, "b"];
        case 2:
            fs = kQ[kQ.l + 2];
            kQ.l += 8;
            return [fs, "e"];
        case 3:
            kQ.l += 8;
            return ["", "s"]
        }
    }

    function kM(kQ, kT, fs, kS) {
        if (kS.biff < 8) {
            return jG(kQ, kT)
        }
        var kU = kQ.l + kT;
        var kV = [];
        for (var kR = 0; kR !== fs.length; ++kR) {
            switch (fs[kR][0]) {
            case "PtgArray":
                fs[kR][1] = kA(kQ);
                kV.push(fs[kR][1]);
                break;
            case "PtgMemArea":
                fs[kR][2] = kH(kQ, fs[kR][1]);
                kV.push(fs[kR][2]);
                break;
            default:
                break
            }
        }
        kT = kU - kQ.l;
        if (kT !== 0) {
            kV.push(jG(kQ, kT))
        }
        return kV
    }

    function io(kQ, kT, kS, kV) {
        var kU = kQ.l + kT;
        var fs = kf(kQ, kV);
        var kR;
        if (kU !== kQ.l) {
            kR = kM(kQ, kU - kQ.l, fs, kS)
        }
        return [fs, kR]
    }

    function i7(kQ, kT, kS) {
        var kV = kQ.l + kT;
        var kR, kU = kQ.read_shift(2);
        if (kU == 65535) {
            return [[], jG(kQ, kT - 2)]
        }
        var fs = kf(kQ, kU);
        if (kT !== kU + 2) {
            kR = kM(kQ, kT - kU - 2, fs, kS)
        }
        return [fs, kR]
    }

    function gU(kQ, kT, kS) {
        var kV = kQ.l + kT;
        var kR, kU = kQ.read_shift(2);
        var fs = kf(kQ, kU);
        if (kU == 65535) {
            return [[], jG(kQ, kT - 2)]
        }
        if (kT !== kU + 2) {
            kR = kM(kQ, kV - kU - 2, fs, kS)
        }
        return [fs, kR]
    }

    function dI(kQ, kU, kT, kS) {
        var kW = kQ.l + kU;
        var kR, kV = kQ.read_shift(2);
        if (kV == 65535) {
            return [[], jG(kQ, kU - 2)]
        }
        var fs = kf(kQ, kV);
        if (kU !== kV + 2) {
            kR = kM(kQ, kW - kV - 2, fs, kT)
        }
        return [fs, kR]
    }

    function kf(fs, kS) {
        var kT = fs.l + kS;
        var kR, kU, kQ = [];
        while (kT != fs.l) {
            kS = kT - fs.l;
            kU = fs[fs.l];
            kR = b6[kU];
            if (kU === 24 || kU === 25) {
                kU = fs[fs.l + 1];
                kR = (kU === 24 ? im : ij)[kU]
            }
            if (!kR || !kR.f) {
                kQ.push(jG(fs, kS))
            } else {
                kQ.push([kR.n, kR.f(fs, kS)])
            }
        }
        return kQ
    }

    function er(fs) {
        return fs.map(function kQ(kR) {
            return kR[1]
        }).join(",")
    }

    function ex(fs, k3, kQ, k1, k2) {
        if (k2 !== undefined && k2.biff === 5) {
            return "BIFF5??"
        }
        var kS = k3 !== undefined ? k3 : {
            s: {
                c: 0,
                r: 0
            }
        };
        var kX = [],
            kZ, kY, kU, ld, kW, kT, k5;
        if (!fs[0] || !fs[0][0]) {
            return ""
        }
        for (var k4 = 0, le = fs[0].length; k4 < le; ++k4) {
            var lc = fs[0][k4];
            switch (lc[0]) {
            case "PtgUminus":
                kX.push("-" + kX.pop());
                break;
            case "PtgUplus":
                kX.push("+" + kX.pop());
                break;
            case "PtgPercent":
                kX.push(kX.pop() + "%");
                break;
            case "PtgAdd":
                kZ = kX.pop();
                kY = kX.pop();
                kX.push(kY + "+" + kZ);
                break;
            case "PtgSub":
                kZ = kX.pop();
                kY = kX.pop();
                kX.push(kY + "-" + kZ);
                break;
            case "PtgMul":
                kZ = kX.pop();
                kY = kX.pop();
                kX.push(kY + "*" + kZ);
                break;
            case "PtgDiv":
                kZ = kX.pop();
                kY = kX.pop();
                kX.push(kY + "/" + kZ);
                break;
            case "PtgPower":
                kZ = kX.pop();
                kY = kX.pop();
                kX.push(kY + "^" + kZ);
                break;
            case "PtgConcat":
                kZ = kX.pop();
                kY = kX.pop();
                kX.push(kY + "&" + kZ);
                break;
            case "PtgLt":
                kZ = kX.pop();
                kY = kX.pop();
                kX.push(kY + "<" + kZ);
                break;
            case "PtgLe":
                kZ = kX.pop();
                kY = kX.pop();
                kX.push(kY + "<=" + kZ);
                break;
            case "PtgEq":
                kZ = kX.pop();
                kY = kX.pop();
                kX.push(kY + "=" + kZ);
                break;
            case "PtgGe":
                kZ = kX.pop();
                kY = kX.pop();
                kX.push(kY + ">=" + kZ);
                break;
            case "PtgGt":
                kZ = kX.pop();
                kY = kX.pop();
                kX.push(kY + ">" + kZ);
                break;
            case "PtgNe":
                kZ = kX.pop();
                kY = kX.pop();
                kX.push(kY + "<>" + kZ);
                break;
            case "PtgIsect":
                kZ = kX.pop();
                kY = kX.pop();
                kX.push(kY + " " + kZ);
                break;
            case "PtgUnion":
                kZ = kX.pop();
                kY = kX.pop();
                kX.push(kY + "," + kZ);
                break;
            case "PtgRange":
                break;
            case "PtgAttrChoose":
                break;
            case "PtgAttrGoto":
                break;
            case "PtgAttrIf":
                break;
            case "PtgRef":
                kU = lc[1][0];
                ld = aV(bh(gW(lc[1][1])), kS);
                kX.push(gW(ld));
                break;
            case "PtgRefN":
                kU = lc[1][0];
                ld = aV(bh(gW(lc[1][1])), kQ);
                kX.push(gW(ld));
                break;
            case "PtgRef3d":
                kU = lc[1][0];
                kW = lc[1][1];
                ld = aV(lc[1][2], kS);
                kX.push(k1[1][kW + 1] + "!" + gW(ld));
                break;
            case "PtgFunc":
            case "PtgFuncVar":
                var k0 = lc[1][0],
                    kV = lc[1][1];
                if (!k0) {
                    k0 = 0
                }
                var kR = kX.slice(-k0);
                kX.length -= k0;
                if (kV === "User") {
                    kV = kR.shift()
                }
                kX.push(kV + "(" + kR.join(",") + ")");
                break;
            case "PtgBool":
                kX.push(lc[1] ? "TRUE" : "FALSE");
                break;
            case "PtgInt":
                kX.push(lc[1]);
                break;
            case "PtgNum":
                kX.push(String(lc[1]));
                break;
            case "PtgStr":
                kX.push('"' + lc[1] + '"');
                break;
            case "PtgErr":
                kX.push(lc[1]);
                break;
            case "PtgArea":
                kU = lc[1][0];
                k5 = eY(lc[1][1], kS);
                kX.push(cR(k5));
                break;
            case "PtgArea3d":
                kU = lc[1][0];
                kW = lc[1][1];
                k5 = lc[1][2];
                kX.push(k1[1][kW + 1] + "!" + cR(k5));
                break;
            case "PtgAttrSum":
                kX.push("SUM(" + kX.pop() + ")");
                break;
            case "PtgAttrSemi":
                break;
            case "PtgName":
                kT = lc[1][2];
                var la = k1[0][kT];
                var lf = la.Name;
                if (lf in cu) {
                    lf = cu[lf]
                }
                kX.push(lf);
                break;
            case "PtgNameX":
                var k8 = lc[1][1];
                kT = lc[1][2];
                var k9;
                if (k1[k8 + 1]) {
                    k9 = k1[k8 + 1][kT]
                } else {
                    if (k1[k8 - 1]) {
                        k9 = k1[k8 - 1][kT]
                    }
                } if (!k9) {
                    k9 = {
                        body: "??NAMEX??"
                    }
                }
                kX.push(k9.body);
                break;
            case "PtgParen":
                kX.push("(" + kX.pop() + ")");
                break;
            case "PtgRefErr":
                kX.push("#REF!");
                break;
            case "PtgExp":
                ld = {
                    c: lc[1][1],
                    r: lc[1][0]
                };
                var k6 = {
                    c: kQ.c,
                    r: kQ.r
                };
                if (k1.sharedf[gW(ld)]) {
                    var lb = (k1.sharedf[gW(ld)]);
                    kX.push(ex(lb, kS, k6, k1, k2))
                } else {
                    var k7 = false;
                    for (kZ = 0; kZ != k1.arrayf.length; ++kZ) {
                        kY = k1.arrayf[kZ];
                        if (ld.c < kY[0].s.c || ld.c > kY[0].e.c) {
                            continue
                        }
                        if (ld.r < kY[0].s.r || ld.r > kY[0].e.r) {
                            continue
                        }
                        kX.push(ex(kY[1], kS, k6, k1, k2))
                    }
                    if (!k7) {
                        kX.push(lc[1])
                    }
                }
                break;
            case "PtgArray":
                kX.push("{" + lc[1].map(er).join(";") + "}");
                break;
            case "PtgMemArea":
                break;
            case "PtgAttrSpace":
                break;
            case "PtgTbl":
                break;
            case "PtgMemErr":
                break;
            case "PtgMissArg":
                kX.push("");
                break;
            case "PtgAreaErr":
                break;
            case "PtgAreaN":
                kX.push("");
                break;
            case "PtgRefErr3d":
                break;
            case "PtgMemFunc":
                break;
            default:
                throw "Unrecognized Formula Token: " + lc
            }
        }
        return kX[0]
    }
    var e3 = {
        1: "REFERENCE",
        2: "VALUE",
        3: "ARRAY"
    };
    var e6 = {
        0: "#NULL!",
        7: "#DIV/0!",
        15: "#VALUE!",
        23: "#REF!",
        29: "#NAME?",
        36: "#NUM!",
        42: "#N/A",
        43: "#GETTING_DATA",
        255: "#WTF?"
    };
    var i2 = dK(e6);
    var aG = {
        0: "BEEP",
        1: "OPEN",
        2: "OPEN.LINKS",
        3: "CLOSE.ALL",
        4: "SAVE",
        5: "SAVE.AS",
        6: "FILE.DELETE",
        7: "PAGE.SETUP",
        8: "PRINT",
        9: "PRINTER.SETUP",
        10: "QUIT",
        11: "NEW.WINDOW",
        12: "ARRANGE.ALL",
        13: "WINDOW.SIZE",
        14: "WINDOW.MOVE",
        15: "FULL",
        16: "CLOSE",
        17: "RUN",
        22: "SET.PRINT.AREA",
        23: "SET.PRINT.TITLES",
        24: "SET.PAGE.BREAK",
        25: "REMOVE.PAGE.BREAK",
        26: "FONT",
        27: "DISPLAY",
        28: "PROTECT.DOCUMENT",
        29: "PRECISION",
        30: "A1.R1C1",
        31: "CALCULATE.NOW",
        32: "CALCULATION",
        34: "DATA.FIND",
        35: "EXTRACT",
        36: "DATA.DELETE",
        37: "SET.DATABASE",
        38: "SET.CRITERIA",
        39: "SORT",
        40: "DATA.SERIES",
        41: "TABLE",
        42: "FORMAT.NUMBER",
        43: "ALIGNMENT",
        44: "STYLE",
        45: "BORDER",
        46: "CELL.PROTECTION",
        47: "COLUMN.WIDTH",
        48: "UNDO",
        49: "CUT",
        50: "COPY",
        51: "PASTE",
        52: "CLEAR",
        53: "PASTE.SPECIAL",
        54: "EDIT.DELETE",
        55: "INSERT",
        56: "FILL.RIGHT",
        57: "FILL.DOWN",
        61: "DEFINE.NAME",
        62: "CREATE.NAMES",
        63: "FORMULA.GOTO",
        64: "FORMULA.FIND",
        65: "SELECT.LAST.CELL",
        66: "SHOW.ACTIVE.CELL",
        67: "GALLERY.AREA",
        68: "GALLERY.BAR",
        69: "GALLERY.COLUMN",
        70: "GALLERY.LINE",
        71: "GALLERY.PIE",
        72: "GALLERY.SCATTER",
        73: "COMBINATION",
        74: "PREFERRED",
        75: "ADD.OVERLAY",
        76: "GRIDLINES",
        77: "SET.PREFERRED",
        78: "AXES",
        79: "LEGEND",
        80: "ATTACH.TEXT",
        81: "ADD.ARROW",
        82: "SELECT.CHART",
        83: "SELECT.PLOT.AREA",
        84: "PATTERNS",
        85: "MAIN.CHART",
        86: "OVERLAY",
        87: "SCALE",
        88: "FORMAT.LEGEND",
        89: "FORMAT.TEXT",
        90: "EDIT.REPEAT",
        91: "PARSE",
        92: "JUSTIFY",
        93: "HIDE",
        94: "UNHIDE",
        95: "WORKSPACE",
        96: "FORMULA",
        97: "FORMULA.FILL",
        98: "FORMULA.ARRAY",
        99: "DATA.FIND.NEXT",
        100: "DATA.FIND.PREV",
        101: "FORMULA.FIND.NEXT",
        102: "FORMULA.FIND.PREV",
        103: "ACTIVATE",
        104: "ACTIVATE.NEXT",
        105: "ACTIVATE.PREV",
        106: "UNLOCKED.NEXT",
        107: "UNLOCKED.PREV",
        108: "COPY.PICTURE",
        109: "SELECT",
        110: "DELETE.NAME",
        111: "DELETE.FORMAT",
        112: "VLINE",
        113: "HLINE",
        114: "VPAGE",
        115: "HPAGE",
        116: "VSCROLL",
        117: "HSCROLL",
        118: "ALERT",
        119: "NEW",
        120: "CANCEL.COPY",
        121: "SHOW.CLIPBOARD",
        122: "MESSAGE",
        124: "PASTE.LINK",
        125: "APP.ACTIVATE",
        126: "DELETE.ARROW",
        127: "ROW.HEIGHT",
        128: "FORMAT.MOVE",
        129: "FORMAT.SIZE",
        130: "FORMULA.REPLACE",
        131: "SEND.KEYS",
        132: "SELECT.SPECIAL",
        133: "APPLY.NAMES",
        134: "REPLACE.FONT",
        135: "FREEZE.PANES",
        136: "SHOW.INFO",
        137: "SPLIT",
        138: "ON.WINDOW",
        139: "ON.DATA",
        140: "DISABLE.INPUT",
        142: "OUTLINE",
        143: "LIST.NAMES",
        144: "FILE.CLOSE",
        145: "SAVE.WORKBOOK",
        146: "DATA.FORM",
        147: "COPY.CHART",
        148: "ON.TIME",
        149: "WAIT",
        150: "FORMAT.FONT",
        151: "FILL.UP",
        152: "FILL.LEFT",
        153: "DELETE.OVERLAY",
        155: "SHORT.MENUS",
        159: "SET.UPDATE.STATUS",
        161: "COLOR.PALETTE",
        162: "DELETE.STYLE",
        163: "WINDOW.RESTORE",
        164: "WINDOW.MAXIMIZE",
        166: "CHANGE.LINK",
        167: "CALCULATE.DOCUMENT",
        168: "ON.KEY",
        169: "APP.RESTORE",
        170: "APP.MOVE",
        171: "APP.SIZE",
        172: "APP.MINIMIZE",
        173: "APP.MAXIMIZE",
        174: "BRING.TO.FRONT",
        175: "SEND.TO.BACK",
        185: "MAIN.CHART.TYPE",
        186: "OVERLAY.CHART.TYPE",
        187: "SELECT.END",
        188: "OPEN.MAIL",
        189: "SEND.MAIL",
        190: "STANDARD.FONT",
        191: "CONSOLIDATE",
        192: "SORT.SPECIAL",
        193: "GALLERY.3D.AREA",
        194: "GALLERY.3D.COLUMN",
        195: "GALLERY.3D.LINE",
        196: "GALLERY.3D.PIE",
        197: "VIEW.3D",
        198: "GOAL.SEEK",
        199: "WORKGROUP",
        200: "FILL.GROUP",
        201: "UPDATE.LINK",
        202: "PROMOTE",
        203: "DEMOTE",
        204: "SHOW.DETAIL",
        206: "UNGROUP",
        207: "OBJECT.PROPERTIES",
        208: "SAVE.NEW.OBJECT",
        209: "SHARE",
        210: "SHARE.NAME",
        211: "DUPLICATE",
        212: "APPLY.STYLE",
        213: "ASSIGN.TO.OBJECT",
        214: "OBJECT.PROTECTION",
        215: "HIDE.OBJECT",
        216: "SET.EXTRACT",
        217: "CREATE.PUBLISHER",
        218: "SUBSCRIBE.TO",
        219: "ATTRIBUTES",
        220: "SHOW.TOOLBAR",
        222: "PRINT.PREVIEW",
        223: "EDIT.COLOR",
        224: "SHOW.LEVELS",
        225: "FORMAT.MAIN",
        226: "FORMAT.OVERLAY",
        227: "ON.RECALC",
        228: "EDIT.SERIES",
        229: "DEFINE.STYLE",
        240: "LINE.PRINT",
        243: "ENTER.DATA",
        249: "GALLERY.RADAR",
        250: "MERGE.STYLES",
        251: "EDITION.OPTIONS",
        252: "PASTE.PICTURE",
        253: "PASTE.PICTURE.LINK",
        254: "SPELLING",
        256: "ZOOM",
        259: "INSERT.OBJECT",
        260: "WINDOW.MINIMIZE",
        265: "SOUND.NOTE",
        266: "SOUND.PLAY",
        267: "FORMAT.SHAPE",
        268: "EXTEND.POLYGON",
        269: "FORMAT.AUTO",
        272: "GALLERY.3D.BAR",
        273: "GALLERY.3D.SURFACE",
        274: "FILL.AUTO",
        276: "CUSTOMIZE.TOOLBAR",
        277: "ADD.TOOL",
        278: "EDIT.OBJECT",
        279: "ON.DOUBLECLICK",
        280: "ON.ENTRY",
        281: "WORKBOOK.ADD",
        282: "WORKBOOK.MOVE",
        283: "WORKBOOK.COPY",
        284: "WORKBOOK.OPTIONS",
        285: "SAVE.WORKSPACE",
        288: "CHART.WIZARD",
        289: "DELETE.TOOL",
        290: "MOVE.TOOL",
        291: "WORKBOOK.SELECT",
        292: "WORKBOOK.ACTIVATE",
        293: "ASSIGN.TO.TOOL",
        295: "COPY.TOOL",
        296: "RESET.TOOL",
        297: "CONSTRAIN.NUMERIC",
        298: "PASTE.TOOL",
        302: "WORKBOOK.NEW",
        305: "SCENARIO.CELLS",
        306: "SCENARIO.DELETE",
        307: "SCENARIO.ADD",
        308: "SCENARIO.EDIT",
        309: "SCENARIO.SHOW",
        310: "SCENARIO.SHOW.NEXT",
        311: "SCENARIO.SUMMARY",
        312: "PIVOT.TABLE.WIZARD",
        313: "PIVOT.FIELD.PROPERTIES",
        314: "PIVOT.FIELD",
        315: "PIVOT.ITEM",
        316: "PIVOT.ADD.FIELDS",
        318: "OPTIONS.CALCULATION",
        319: "OPTIONS.EDIT",
        320: "OPTIONS.VIEW",
        321: "ADDIN.MANAGER",
        322: "MENU.EDITOR",
        323: "ATTACH.TOOLBARS",
        324: "VBAActivate",
        325: "OPTIONS.CHART",
        328: "VBA.INSERT.FILE",
        330: "VBA.PROCEDURE.DEFINITION",
        336: "ROUTING.SLIP",
        338: "ROUTE.DOCUMENT",
        339: "MAIL.LOGON",
        342: "INSERT.PICTURE",
        343: "EDIT.TOOL",
        344: "GALLERY.DOUGHNUT",
        350: "CHART.TREND",
        352: "PIVOT.ITEM.PROPERTIES",
        354: "WORKBOOK.INSERT",
        355: "OPTIONS.TRANSITION",
        356: "OPTIONS.GENERAL",
        370: "FILTER.ADVANCED",
        373: "MAIL.ADD.MAILER",
        374: "MAIL.DELETE.MAILER",
        375: "MAIL.REPLY",
        376: "MAIL.REPLY.ALL",
        377: "MAIL.FORWARD",
        378: "MAIL.NEXT.LETTER",
        379: "DATA.LABEL",
        380: "INSERT.TITLE",
        381: "FONT.PROPERTIES",
        382: "MACRO.OPTIONS",
        383: "WORKBOOK.HIDE",
        384: "WORKBOOK.UNHIDE",
        385: "WORKBOOK.DELETE",
        386: "WORKBOOK.NAME",
        388: "GALLERY.CUSTOM",
        390: "ADD.CHART.AUTOFORMAT",
        391: "DELETE.CHART.AUTOFORMAT",
        392: "CHART.ADD.DATA",
        393: "AUTO.OUTLINE",
        394: "TAB.ORDER",
        395: "SHOW.DIALOG",
        396: "SELECT.ALL",
        397: "UNGROUP.SHEETS",
        398: "SUBTOTAL.CREATE",
        399: "SUBTOTAL.REMOVE",
        400: "RENAME.OBJECT",
        412: "WORKBOOK.SCROLL",
        413: "WORKBOOK.NEXT",
        414: "WORKBOOK.PREV",
        415: "WORKBOOK.TAB.SPLIT",
        416: "FULL.SCREEN",
        417: "WORKBOOK.PROTECT",
        420: "SCROLLBAR.PROPERTIES",
        421: "PIVOT.SHOW.PAGES",
        422: "TEXT.TO.COLUMNS",
        423: "FORMAT.CHARTTYPE",
        424: "LINK.FORMAT",
        425: "TRACER.DISPLAY",
        430: "TRACER.NAVIGATE",
        431: "TRACER.CLEAR",
        432: "TRACER.ERROR",
        433: "PIVOT.FIELD.GROUP",
        434: "PIVOT.FIELD.UNGROUP",
        435: "CHECKBOX.PROPERTIES",
        436: "LABEL.PROPERTIES",
        437: "LISTBOX.PROPERTIES",
        438: "EDITBOX.PROPERTIES",
        439: "PIVOT.REFRESH",
        440: "LINK.COMBO",
        441: "OPEN.TEXT",
        442: "HIDE.DIALOG",
        443: "SET.DIALOG.FOCUS",
        444: "ENABLE.OBJECT",
        445: "PUSHBUTTON.PROPERTIES",
        446: "SET.DIALOG.DEFAULT",
        447: "FILTER",
        448: "FILTER.SHOW.ALL",
        449: "CLEAR.OUTLINE",
        450: "FUNCTION.WIZARD",
        451: "ADD.LIST.ITEM",
        452: "SET.LIST.ITEM",
        453: "REMOVE.LIST.ITEM",
        454: "SELECT.LIST.ITEM",
        455: "SET.CONTROL.VALUE",
        456: "SAVE.COPY.AS",
        458: "OPTIONS.LISTS.ADD",
        459: "OPTIONS.LISTS.DELETE",
        460: "SERIES.AXES",
        461: "SERIES.X",
        462: "SERIES.Y",
        463: "ERRORBAR.X",
        464: "ERRORBAR.Y",
        465: "FORMAT.CHART",
        466: "SERIES.ORDER",
        467: "MAIL.LOGOFF",
        468: "CLEAR.ROUTING.SLIP",
        469: "APP.ACTIVATE.MICROSOFT",
        470: "MAIL.EDIT.MAILER",
        471: "ON.SHEET",
        472: "STANDARD.WIDTH",
        473: "SCENARIO.MERGE",
        474: "SUMMARY.INFO",
        475: "FIND.FILE",
        476: "ACTIVE.CELL.FONT",
        477: "ENABLE.TIPWIZARD",
        478: "VBA.MAKE.ADDIN",
        480: "INSERTDATATABLE",
        481: "WORKGROUP.OPTIONS",
        482: "MAIL.SEND.MAILER",
        485: "AUTOCORRECT",
        489: "POST.DOCUMENT",
        491: "PICKLIST",
        493: "VIEW.SHOW",
        494: "VIEW.DEFINE",
        495: "VIEW.DELETE",
        509: "SHEET.BACKGROUND",
        510: "INSERT.MAP.OBJECT",
        511: "OPTIONS.MENONO",
        517: "MSOCHECKS",
        518: "NORMAL",
        519: "LAYOUT",
        520: "RM.PRINT.AREA",
        521: "CLEAR.PRINT.AREA",
        522: "ADD.PRINT.AREA",
        523: "MOVE.BRK",
        545: "HIDECURR.NOTE",
        546: "HIDEALL.NOTES",
        547: "DELETE.NOTE",
        548: "TRAVERSE.NOTES",
        549: "ACTIVATE.NOTES",
        620: "PROTECT.REVISIONS",
        621: "UNPROTECT.REVISIONS",
        647: "OPTIONS.ME",
        653: "WEB.PUBLISH",
        667: "NEWWEBQUERY",
        673: "PIVOT.TABLE.CHART",
        753: "OPTIONS.SAVE",
        755: "OPTIONS.SPELL",
        808: "HIDEALL.INKANNOTS"
    };
    var iC = {
        0: "COUNT",
        1: "IF",
        2: "ISNA",
        3: "ISERROR",
        4: "SUM",
        5: "AVERAGE",
        6: "MIN",
        7: "MAX",
        8: "ROW",
        9: "COLUMN",
        10: "NA",
        11: "NPV",
        12: "STDEV",
        13: "DOLLAR",
        14: "FIXED",
        15: "SIN",
        16: "COS",
        17: "TAN",
        18: "ATAN",
        19: "PI",
        20: "SQRT",
        21: "EXP",
        22: "LN",
        23: "LOG10",
        24: "ABS",
        25: "INT",
        26: "SIGN",
        27: "ROUND",
        28: "LOOKUP",
        29: "INDEX",
        30: "REPT",
        31: "MID",
        32: "LEN",
        33: "VALUE",
        34: "TRUE",
        35: "FALSE",
        36: "AND",
        37: "OR",
        38: "NOT",
        39: "MOD",
        40: "DCOUNT",
        41: "DSUM",
        42: "DAVERAGE",
        43: "DMIN",
        44: "DMAX",
        45: "DSTDEV",
        46: "VAR",
        47: "DVAR",
        48: "TEXT",
        49: "LINEST",
        50: "TREND",
        51: "LOGEST",
        52: "GROWTH",
        53: "GOTO",
        54: "HALT",
        55: "RETURN",
        56: "PV",
        57: "FV",
        58: "NPER",
        59: "PMT",
        60: "RATE",
        61: "MIRR",
        62: "IRR",
        63: "RAND",
        64: "MATCH",
        65: "DATE",
        66: "TIME",
        67: "DAY",
        68: "MONTH",
        69: "YEAR",
        70: "WEEKDAY",
        71: "HOUR",
        72: "MINUTE",
        73: "SECOND",
        74: "NOW",
        75: "AREAS",
        76: "ROWS",
        77: "COLUMNS",
        78: "OFFSET",
        79: "ABSREF",
        80: "RELREF",
        81: "ARGUMENT",
        82: "SEARCH",
        83: "TRANSPOSE",
        84: "ERROR",
        85: "STEP",
        86: "TYPE",
        87: "ECHO",
        88: "SET.NAME",
        89: "CALLER",
        90: "DEREF",
        91: "WINDOWS",
        92: "SERIES",
        93: "DOCUMENTS",
        94: "ACTIVE.CELL",
        95: "SELECTION",
        96: "RESULT",
        97: "ATAN2",
        98: "ASIN",
        99: "ACOS",
        100: "CHOOSE",
        101: "HLOOKUP",
        102: "VLOOKUP",
        103: "LINKS",
        104: "INPUT",
        105: "ISREF",
        106: "GET.FORMULA",
        107: "GET.NAME",
        108: "SET.VALUE",
        109: "LOG",
        110: "EXEC",
        111: "CHAR",
        112: "LOWER",
        113: "UPPER",
        114: "PROPER",
        115: "LEFT",
        116: "RIGHT",
        117: "EXACT",
        118: "TRIM",
        119: "REPLACE",
        120: "SUBSTITUTE",
        121: "CODE",
        122: "NAMES",
        123: "DIRECTORY",
        124: "FIND",
        125: "CELL",
        126: "ISERR",
        127: "ISTEXT",
        128: "ISNUMBER",
        129: "ISBLANK",
        130: "T",
        131: "N",
        132: "FOPEN",
        133: "FCLOSE",
        134: "FSIZE",
        135: "FREADLN",
        136: "FREAD",
        137: "FWRITELN",
        138: "FWRITE",
        139: "FPOS",
        140: "DATEVALUE",
        141: "TIMEVALUE",
        142: "SLN",
        143: "SYD",
        144: "DDB",
        145: "GET.DEF",
        146: "REFTEXT",
        147: "TEXTREF",
        148: "INDIRECT",
        149: "REGISTER",
        150: "CALL",
        151: "ADD.BAR",
        152: "ADD.MENU",
        153: "ADD.COMMAND",
        154: "ENABLE.COMMAND",
        155: "CHECK.COMMAND",
        156: "RENAME.COMMAND",
        157: "SHOW.BAR",
        158: "DELETE.MENU",
        159: "DELETE.COMMAND",
        160: "GET.CHART.ITEM",
        161: "DIALOG.BOX",
        162: "CLEAN",
        163: "MDETERM",
        164: "MINVERSE",
        165: "MMULT",
        166: "FILES",
        167: "IPMT",
        168: "PPMT",
        169: "COUNTA",
        170: "CANCEL.KEY",
        171: "FOR",
        172: "WHILE",
        173: "BREAK",
        174: "NEXT",
        175: "INITIATE",
        176: "REQUEST",
        177: "POKE",
        178: "EXECUTE",
        179: "TERMINATE",
        180: "RESTART",
        181: "HELP",
        182: "GET.BAR",
        183: "PRODUCT",
        184: "FACT",
        185: "GET.CELL",
        186: "GET.WORKSPACE",
        187: "GET.WINDOW",
        188: "GET.DOCUMENT",
        189: "DPRODUCT",
        190: "ISNONTEXT",
        191: "GET.NOTE",
        192: "NOTE",
        193: "STDEVP",
        194: "VARP",
        195: "DSTDEVP",
        196: "DVARP",
        197: "TRUNC",
        198: "ISLOGICAL",
        199: "DCOUNTA",
        200: "DELETE.BAR",
        201: "UNREGISTER",
        204: "USDOLLAR",
        205: "FINDB",
        206: "SEARCHB",
        207: "REPLACEB",
        208: "LEFTB",
        209: "RIGHTB",
        210: "MIDB",
        211: "LENB",
        212: "ROUNDUP",
        213: "ROUNDDOWN",
        214: "ASC",
        215: "DBCS",
        216: "RANK",
        219: "ADDRESS",
        220: "DAYS360",
        221: "TODAY",
        222: "VDB",
        223: "ELSE",
        224: "ELSE.IF",
        225: "END.IF",
        226: "FOR.CELL",
        227: "MEDIAN",
        228: "SUMPRODUCT",
        229: "SINH",
        230: "COSH",
        231: "TANH",
        232: "ASINH",
        233: "ACOSH",
        234: "ATANH",
        235: "DGET",
        236: "CREATE.OBJECT",
        237: "VOLATILE",
        238: "LAST.ERROR",
        239: "CUSTOM.UNDO",
        240: "CUSTOM.REPEAT",
        241: "FORMULA.CONVERT",
        242: "GET.LINK.INFO",
        243: "TEXT.BOX",
        244: "INFO",
        245: "GROUP",
        246: "GET.OBJECT",
        247: "DB",
        248: "PAUSE",
        251: "RESUME",
        252: "FREQUENCY",
        253: "ADD.TOOLBAR",
        254: "DELETE.TOOLBAR",
        255: "User",
        256: "RESET.TOOLBAR",
        257: "EVALUATE",
        258: "GET.TOOLBAR",
        259: "GET.TOOL",
        260: "SPELLING.CHECK",
        261: "ERROR.TYPE",
        262: "APP.TITLE",
        263: "WINDOW.TITLE",
        264: "SAVE.TOOLBAR",
        265: "ENABLE.TOOL",
        266: "PRESS.TOOL",
        267: "REGISTER.ID",
        268: "GET.WORKBOOK",
        269: "AVEDEV",
        270: "BETADIST",
        271: "GAMMALN",
        272: "BETAINV",
        273: "BINOMDIST",
        274: "CHIDIST",
        275: "CHIINV",
        276: "COMBIN",
        277: "CONFIDENCE",
        278: "CRITBINOM",
        279: "EVEN",
        280: "EXPONDIST",
        281: "FDIST",
        282: "FINV",
        283: "FISHER",
        284: "FISHERINV",
        285: "FLOOR",
        286: "GAMMADIST",
        287: "GAMMAINV",
        288: "CEILING",
        289: "HYPGEOMDIST",
        290: "LOGNORMDIST",
        291: "LOGINV",
        292: "NEGBINOMDIST",
        293: "NORMDIST",
        294: "NORMSDIST",
        295: "NORMINV",
        296: "NORMSINV",
        297: "STANDARDIZE",
        298: "ODD",
        299: "PERMUT",
        300: "POISSON",
        301: "TDIST",
        302: "WEIBULL",
        303: "SUMXMY2",
        304: "SUMX2MY2",
        305: "SUMX2PY2",
        306: "CHITEST",
        307: "CORREL",
        308: "COVAR",
        309: "FORECAST",
        310: "FTEST",
        311: "INTERCEPT",
        312: "PEARSON",
        313: "RSQ",
        314: "STEYX",
        315: "SLOPE",
        316: "TTEST",
        317: "PROB",
        318: "DEVSQ",
        319: "GEOMEAN",
        320: "HARMEAN",
        321: "SUMSQ",
        322: "KURT",
        323: "SKEW",
        324: "ZTEST",
        325: "LARGE",
        326: "SMALL",
        327: "QUARTILE",
        328: "PERCENTILE",
        329: "PERCENTRANK",
        330: "MODE",
        331: "TRIMMEAN",
        332: "TINV",
        334: "MOVIE.COMMAND",
        335: "GET.MOVIE",
        336: "CONCATENATE",
        337: "POWER",
        338: "PIVOT.ADD.DATA",
        339: "GET.PIVOT.TABLE",
        340: "GET.PIVOT.FIELD",
        341: "GET.PIVOT.ITEM",
        342: "RADIANS",
        343: "DEGREES",
        344: "SUBTOTAL",
        345: "SUMIF",
        346: "COUNTIF",
        347: "COUNTBLANK",
        348: "SCENARIO.GET",
        349: "OPTIONS.LISTS.GET",
        350: "ISPMT",
        351: "DATEDIF",
        352: "DATESTRING",
        353: "NUMBERSTRING",
        354: "ROMAN",
        355: "OPEN.DIALOG",
        356: "SAVE.DIALOG",
        357: "VIEW.GET",
        358: "GETPIVOTDATA",
        359: "HYPERLINK",
        360: "PHONETIC",
        361: "AVERAGEA",
        362: "MAXA",
        363: "MINA",
        364: "STDEVPA",
        365: "VARPA",
        366: "STDEVA",
        367: "VARA",
        368: "BAHTTEXT",
        369: "THAIDAYOFWEEK",
        370: "THAIDIGIT",
        371: "THAIMONTHOFYEAR",
        372: "THAINUMSOUND",
        373: "THAINUMSTRING",
        374: "THAISTRINGLENGTH",
        375: "ISTHAIDIGIT",
        376: "ROUNDBAHTDOWN",
        377: "ROUNDBAHTUP",
        378: "THAIYEAR",
        379: "RTD"
    };
    var dk = {
        2: 1,
        3: 1,
        15: 1,
        16: 1,
        17: 1,
        18: 1,
        20: 1,
        21: 1,
        22: 1,
        23: 1,
        24: 1,
        25: 1,
        26: 1,
        27: 2,
        30: 2,
        31: 3,
        32: 1,
        33: 1,
        38: 1,
        39: 2,
        40: 3,
        41: 3,
        42: 3,
        43: 3,
        44: 3,
        45: 3,
        47: 3,
        48: 2,
        53: 1,
        61: 3,
        65: 3,
        66: 3,
        67: 1,
        68: 1,
        69: 1,
        71: 1,
        72: 1,
        73: 1,
        75: 1,
        76: 1,
        77: 1,
        79: 2,
        80: 2,
        83: 1,
        86: 1,
        90: 1,
        97: 2,
        98: 1,
        99: 1,
        105: 1,
        111: 1,
        112: 1,
        113: 1,
        114: 1,
        117: 2,
        118: 1,
        119: 4,
        121: 1,
        126: 1,
        127: 1,
        128: 1,
        129: 1,
        130: 1,
        131: 1,
        133: 1,
        134: 1,
        135: 1,
        136: 2,
        137: 2,
        138: 2,
        140: 1,
        141: 1,
        142: 3,
        143: 4,
        162: 1,
        163: 1,
        164: 1,
        165: 2,
        172: 1,
        175: 2,
        176: 2,
        177: 3,
        178: 2,
        179: 1,
        184: 1,
        189: 3,
        190: 1,
        195: 3,
        196: 3,
        198: 1,
        199: 3,
        201: 1,
        207: 4,
        210: 3,
        211: 1,
        212: 2,
        213: 2,
        214: 1,
        215: 1,
        229: 1,
        230: 1,
        231: 1,
        232: 1,
        233: 1,
        234: 1,
        235: 3,
        244: 1,
        252: 2,
        257: 1,
        261: 1,
        271: 1,
        273: 4,
        274: 2,
        275: 2,
        276: 2,
        277: 3,
        278: 3,
        279: 1,
        280: 3,
        281: 3,
        282: 3,
        283: 1,
        284: 1,
        285: 2,
        286: 4,
        287: 3,
        288: 2,
        289: 4,
        290: 3,
        291: 3,
        292: 3,
        293: 4,
        294: 1,
        295: 3,
        296: 1,
        297: 3,
        298: 1,
        299: 2,
        300: 3,
        301: 3,
        302: 4,
        303: 2,
        304: 2,
        305: 2,
        306: 2,
        307: 2,
        308: 2,
        309: 3,
        310: 2,
        311: 2,
        312: 2,
        313: 2,
        314: 2,
        315: 2,
        316: 4,
        325: 2,
        326: 2,
        327: 2,
        328: 2,
        331: 2,
        332: 2,
        337: 2,
        342: 1,
        343: 1,
        346: 2,
        347: 1,
        350: 4,
        351: 3,
        352: 1,
        353: 2,
        360: 1,
        368: 1,
        369: 1,
        370: 1,
        371: 1,
        372: 1,
        373: 1,
        374: 1,
        375: 1,
        376: 1,
        377: 1,
        378: 1,
        65535: 0
    };
    var cu = {
        "_xlfn.ACOT": "ACOT",
        "_xlfn.ACOTH": "ACOTH",
        "_xlfn.AGGREGATE": "AGGREGATE",
        "_xlfn.ARABIC": "ARABIC",
        "_xlfn.AVERAGEIF": "AVERAGEIF",
        "_xlfn.AVERAGEIFS": "AVERAGEIFS",
        "_xlfn.BASE": "BASE",
        "_xlfn.BETA.DIST": "BETA.DIST",
        "_xlfn.BETA.INV": "BETA.INV",
        "_xlfn.BINOM.DIST": "BINOM.DIST",
        "_xlfn.BINOM.DIST.RANGE": "BINOM.DIST.RANGE",
        "_xlfn.BINOM.INV": "BINOM.INV",
        "_xlfn.BITAND": "BITAND",
        "_xlfn.BITLSHIFT": "BITLSHIFT",
        "_xlfn.BITOR": "BITOR",
        "_xlfn.BITRSHIFT": "BITRSHIFT",
        "_xlfn.BITXOR": "BITXOR",
        "_xlfn.CEILING.MATH": "CEILING.MATH",
        "_xlfn.CEILING.PRECISE": "CEILING.PRECISE",
        "_xlfn.CHISQ.DIST": "CHISQ.DIST",
        "_xlfn.CHISQ.DIST.RT": "CHISQ.DIST.RT",
        "_xlfn.CHISQ.INV": "CHISQ.INV",
        "_xlfn.CHISQ.INV.RT": "CHISQ.INV.RT",
        "_xlfn.CHISQ.TEST": "CHISQ.TEST",
        "_xlfn.COMBINA": "COMBINA",
        "_xlfn.CONFIDENCE.NORM": "CONFIDENCE.NORM",
        "_xlfn.CONFIDENCE.T": "CONFIDENCE.T",
        "_xlfn.COT": "COT",
        "_xlfn.COTH": "COTH",
        "_xlfn.COUNTIFS": "COUNTIFS",
        "_xlfn.COVARIANCE.P": "COVARIANCE.P",
        "_xlfn.COVARIANCE.S": "COVARIANCE.S",
        "_xlfn.CSC": "CSC",
        "_xlfn.CSCH": "CSCH",
        "_xlfn.DAYS": "DAYS",
        "_xlfn.DECIMAL": "DECIMAL",
        "_xlfn.ECMA.CEILING": "ECMA.CEILING",
        "_xlfn.ERF.PRECISE": "ERF.PRECISE",
        "_xlfn.ERFC.PRECISE": "ERFC.PRECISE",
        "_xlfn.EXPON.DIST": "EXPON.DIST",
        "_xlfn.F.DIST": "F.DIST",
        "_xlfn.F.DIST.RT": "F.DIST.RT",
        "_xlfn.F.INV": "F.INV",
        "_xlfn.F.INV.RT": "F.INV.RT",
        "_xlfn.F.TEST": "F.TEST",
        "_xlfn.FILTERXML": "FILTERXML",
        "_xlfn.FLOOR.MATH": "FLOOR.MATH",
        "_xlfn.FLOOR.PRECISE": "FLOOR.PRECISE",
        "_xlfn.FORMULATEXT": "FORMULATEXT",
        "_xlfn.GAMMA": "GAMMA",
        "_xlfn.GAMMA.DIST": "GAMMA.DIST",
        "_xlfn.GAMMA.INV": "GAMMA.INV",
        "_xlfn.GAMMALN.PRECISE": "GAMMALN.PRECISE",
        "_xlfn.GAUSS": "GAUSS",
        "_xlfn.HYPGEOM.DIST": "HYPGEOM.DIST",
        "_xlfn.IFNA": "IFNA",
        "_xlfn.IFERROR": "IFERROR",
        "_xlfn.IMCOSH": "IMCOSH",
        "_xlfn.IMCOT": "IMCOT",
        "_xlfn.IMCSC": "IMCSC",
        "_xlfn.IMCSCH": "IMCSCH",
        "_xlfn.IMSEC": "IMSEC",
        "_xlfn.IMSECH": "IMSECH",
        "_xlfn.IMSINH": "IMSINH",
        "_xlfn.IMTAN": "IMTAN",
        "_xlfn.ISFORMULA": "ISFORMULA",
        "_xlfn.ISO.CEILING": "ISO.CEILING",
        "_xlfn.ISOWEEKNUM": "ISOWEEKNUM",
        "_xlfn.LOGNORM.DIST": "LOGNORM.DIST",
        "_xlfn.LOGNORM.INV": "LOGNORM.INV",
        "_xlfn.MODE.MULT": "MODE.MULT",
        "_xlfn.MODE.SNGL": "MODE.SNGL",
        "_xlfn.MUNIT": "MUNIT",
        "_xlfn.NEGBINOM.DIST": "NEGBINOM.DIST",
        "_xlfn.NETWORKDAYS.INTL": "NETWORKDAYS.INTL",
        "_xlfn.NIGBINOM": "NIGBINOM",
        "_xlfn.NORM.DIST": "NORM.DIST",
        "_xlfn.NORM.INV": "NORM.INV",
        "_xlfn.NORM.S.DIST": "NORM.S.DIST",
        "_xlfn.NORM.S.INV": "NORM.S.INV",
        "_xlfn.NUMBERVALUE": "NUMBERVALUE",
        "_xlfn.PDURATION": "PDURATION",
        "_xlfn.PERCENTILE.EXC": "PERCENTILE.EXC",
        "_xlfn.PERCENTILE.INC": "PERCENTILE.INC",
        "_xlfn.PERCENTRANK.EXC": "PERCENTRANK.EXC",
        "_xlfn.PERCENTRANK.INC": "PERCENTRANK.INC",
        "_xlfn.PERMUTATIONA": "PERMUTATIONA",
        "_xlfn.PHI": "PHI",
        "_xlfn.POISSON.DIST": "POISSON.DIST",
        "_xlfn.QUARTILE.EXC": "QUARTILE.EXC",
        "_xlfn.QUARTILE.INC": "QUARTILE.INC",
        "_xlfn.QUERYSTRING": "QUERYSTRING",
        "_xlfn.RANK.AVG": "RANK.AVG",
        "_xlfn.RANK.EQ": "RANK.EQ",
        "_xlfn.RRI": "RRI",
        "_xlfn.SEC": "SEC",
        "_xlfn.SECH": "SECH",
        "_xlfn.SHEET": "SHEET",
        "_xlfn.SHEETS": "SHEETS",
        "_xlfn.SKEW.P": "SKEW.P",
        "_xlfn.STDEV.P": "STDEV.P",
        "_xlfn.STDEV.S": "STDEV.S",
        "_xlfn.SUMIFS": "SUMIFS",
        "_xlfn.T.DIST": "T.DIST",
        "_xlfn.T.DIST.2T": "T.DIST.2T",
        "_xlfn.T.DIST.RT": "T.DIST.RT",
        "_xlfn.T.INV": "T.INV",
        "_xlfn.T.INV.2T": "T.INV.2T",
        "_xlfn.T.TEST": "T.TEST",
        "_xlfn.UNICHAR": "UNICHAR",
        "_xlfn.UNICODE": "UNICODE",
        "_xlfn.VAR.P": "VAR.P",
        "_xlfn.VAR.S": "VAR.S",
        "_xlfn.WEBSERVICE": "WEBSERVICE",
        "_xlfn.WEIBULL.DIST": "WEIBULL.DIST",
        "_xlfn.WORKDAY.INTL": "WORKDAY.INTL",
        "_xlfn.XOR": "XOR",
        "_xlfn.Z.TEST": "Z.TEST"
    };

    function go(fs, kR) {
        var kQ = fs.read_shift(4);
        if (kQ === 124226) {
            return
        }
        fs.l += kR - 4
    }

    function kp(fs, kQ) {
        return fs.read_shift(4)
    }

    function aM(fs, kQ) {
        var kR = {};
        kR.xclrType = fs.read_shift(2);
        kR.nTintShade = fs.read_shift(2);
        switch (kR.xclrType) {
        case 0:
            fs.l += 4;
            break;
        case 1:
            kR.xclrValue = parse_IcvXF(fs, 4);
            break;
        case 2:
            kR.xclrValue = a2(fs, 4);
            break;
        case 3:
            kR.xclrValue = kp(fs, 4);
            break;
        case 4:
            fs.l += 4;
            break
        }
        fs.l += 8;
        return kR
    }

    function iA(kQ, kR) {
        var kS = kQ.read_shift(2);
        var fs = kQ.read_shift(2);
        var kT = [kS];
        switch (kS) {
        case 4:
        case 5:
        case 7:
        case 8:
        case 9:
        case 10:
        case 11:
        case 13:
            kT[1] = aM(kQ, fs);
            break;
        case 6:
            kT[1] = parse_XFExtGradient(kQ, fs);
            break;
        case 14:
        case 15:
            kT[1] = kQ.read_shift(fs === 5 ? 1 : 2);
            break;
        default:
            throw new Error("Unrecognized ExtProp type: " + kS + " " + fs)
        }
        return kT
    }

    function hj(kR, kU) {
        var kQ = kR.l + kU;
        kR.l += 2;
        var fs = kR.read_shift(2);
        kR.l += 2;
        var kS = kR.read_shift(2);
        var kT = [];
        while (kS-- > 0) {
            kT.push(iA(kR, kQ - kR.l))
        }
        return {
            ixfe: fs,
            ext: kT
        }
    }

    function cp(kQ, fs) {
        fs.forEach(function (kR) {
            switch (kR[0]) {
            case 4:
                break;
            case 5:
                break;
            case 7:
            case 8:
            case 9:
            case 10:
                break;
            case 13:
                break;
            case 14:
                break;
            default:
                throw "bafuq" + kR[0].toString(16)
            }
        })
    }
    var gv = {
        3: {
            n: "BIFF2NUM",
            f: d
        },
        4: {
            n: "BIFF2STR",
            f: jE
        },
        6: {
            n: "Formula",
            f: fj
        },
        9: {
            n: "BOF",
            f: jl
        },
        10: {
            n: "EOF",
            f: kw
        },
        12: {
            n: "CalcCount",
            f: hD
        },
        13: {
            n: "CalcMode",
            f: ko
        },
        14: {
            n: "CalcPrecision",
            f: aq
        },
        15: {
            n: "CalcRefMode",
            f: hq
        },
        16: {
            n: "CalcDelta",
            f: g8
        },
        17: {
            n: "CalcIter",
            f: ek
        },
        18: {
            n: "Protect",
            f: g0
        },
        19: {
            n: "Password",
            f: cv
        },
        20: {
            n: "Header",
            f: cB
        },
        21: {
            n: "Footer",
            f: ku
        },
        23: {
            n: "ExternSheet",
            f: c0
        },
        24: {
            n: "Lbl",
            f: bd
        },
        25: {
            n: "WinProtect",
            f: aS
        },
        26: {
            n: "VerticalPageBreaks",
            f: j7
        },
        27: {
            n: "HorizontalPageBreaks",
            f: kx
        },
        28: {
            n: "Note",
            f: bJ
        },
        29: {
            n: "Selection",
            f: cm
        },
        34: {
            n: "Date1904",
            f: e4
        },
        35: {
            n: "ExternName",
            f: da
        },
        38: {
            n: "LeftMargin",
            f: i
        },
        39: {
            n: "RightMargin",
            f: cH
        },
        40: {
            n: "TopMargin",
            f: aZ
        },
        41: {
            n: "BottomMargin",
            f: kE
        },
        42: {
            n: "PrintRowCol",
            f: x
        },
        43: {
            n: "PrintGrid",
            f: av
        },
        47: {
            n: "FilePass",
            f: eI
        },
        49: {
            n: "Font",
            f: f8
        },
        51: {
            n: "PrintSize",
            f: e5
        },
        60: {
            n: "Continue",
            f: jm
        },
        61: {
            n: "Window1",
            f: jg
        },
        64: {
            n: "Backup",
            f: iO
        },
        65: {
            n: "Pane",
            f: cF
        },
        66: {
            n: "CodePage",
            f: bX
        },
        77: {
            n: "Pls",
            f: eM
        },
        80: {
            n: "DCon",
            f: ke
        },
        81: {
            n: "DConRef",
            f: fr
        },
        82: {
            n: "DConName",
            f: bc
        },
        85: {
            n: "DefColWidth",
            f: gi
        },
        89: {
            n: "XCT",
            f: cQ
        },
        90: {
            n: "CRN",
            f: a1
        },
        91: {
            n: "FileSharing",
            f: cc
        },
        92: {
            n: "WriteAccess",
            f: jS
        },
        93: {
            n: "Obj",
            f: cM
        },
        94: {
            n: "Uncalced",
            f: f6
        },
        95: {
            n: "CalcSaveRecalc",
            f: ci
        },
        96: {
            n: "Template",
            f: aY
        },
        97: {
            n: "Intl",
            f: jD
        },
        99: {
            n: "ObjProtect",
            f: jW
        },
        125: {
            n: "ColInfo",
            f: aN
        },
        128: {
            n: "Guts",
            f: cS
        },
        129: {
            n: "WsBool",
            f: a4
        },
        130: {
            n: "GridSet",
            f: dm
        },
        131: {
            n: "HCenter",
            f: ik
        },
        132: {
            n: "VCenter",
            f: fO
        },
        133: {
            n: "BoundSheet8",
            f: eT
        },
        134: {
            n: "WriteProtect",
            f: u
        },
        140: {
            n: "Country",
            f: cn
        },
        141: {
            n: "HideObj",
            f: gC
        },
        144: {
            n: "Sort",
            f: bx
        },
        146: {
            n: "Palette",
            f: fo
        },
        151: {
            n: "Sync",
            f: J
        },
        152: {
            n: "LPr",
            f: fx
        },
        153: {
            n: "DxGCol",
            f: iz
        },
        154: {
            n: "FnGroupName",
            f: fX
        },
        155: {
            n: "FilterMode",
            f: iG
        },
        156: {
            n: "BuiltInFnGroupCount",
            f: aA
        },
        157: {
            n: "AutoFilterInfo",
            f: ap
        },
        158: {
            n: "AutoFilter",
            f: bW
        },
        160: {
            n: "Scl",
            f: ib
        },
        161: {
            n: "Setup",
            f: j3
        },
        174: {
            n: "ScenMan",
            f: f5
        },
        175: {
            n: "SCENARIO",
            f: ch
        },
        176: {
            n: "SxView",
            f: dQ
        },
        177: {
            n: "Sxvd",
            f: e8
        },
        178: {
            n: "SXVI",
            f: bv
        },
        180: {
            n: "SxIvd",
            f: ag
        },
        181: {
            n: "SXLI",
            f: d1
        },
        182: {
            n: "SXPI",
            f: c4
        },
        184: {
            n: "DocRoute",
            f: hI
        },
        185: {
            n: "RecipName",
            f: b8
        },
        189: {
            n: "MulRk",
            f: kB
        },
        190: {
            n: "MulBlank",
            f: eF
        },
        193: {
            n: "Mms",
            f: c5
        },
        197: {
            n: "SXDI",
            f: fQ
        },
        198: {
            n: "SXDB",
            f: fT
        },
        199: {
            n: "SXFDB",
            f: N
        },
        200: {
            n: "SXDBB",
            f: dd
        },
        201: {
            n: "SXNum",
            f: v
        },
        202: {
            n: "SxBool",
            f: f9
        },
        203: {
            n: "SxErr",
            f: gL
        },
        204: {
            n: "SXInt",
            f: cE
        },
        205: {
            n: "SXString",
            f: af
        },
        206: {
            n: "SXDtr",
            f: cK
        },
        207: {
            n: "SxNil",
            f: bB
        },
        208: {
            n: "SXTbl",
            f: gE
        },
        209: {
            n: "SXTBRGIITM",
            f: il
        },
        210: {
            n: "SxTbpg",
            f: dG
        },
        211: {
            n: "ObProj",
            f: iv
        },
        213: {
            n: "SXStreamID",
            f: iW
        },
        215: {
            n: "DBCell",
            f: jJ
        },
        216: {
            n: "SXRng",
            f: gK
        },
        217: {
            n: "SxIsxoper",
            f: ah
        },
        218: {
            n: "BookBool",
            f: cG
        },
        220: {
            n: "DbOrParamQry",
            f: hg
        },
        221: {
            n: "ScenarioProtect",
            f: hS
        },
        222: {
            n: "OleObjectSize",
            f: fB
        },
        224: {
            n: "XF",
            f: bw
        },
        225: {
            n: "InterfaceHdr",
            f: gf
        },
        226: {
            n: "InterfaceEnd",
            f: cI
        },
        227: {
            n: "SXVS",
            f: bp
        },
        229: {
            n: "MergeCells",
            f: iN
        },
        233: {
            n: "BkHim",
            f: bn
        },
        235: {
            n: "MsoDrawingGroup",
            f: eV
        },
        236: {
            n: "MsoDrawing",
            f: kF
        },
        237: {
            n: "MsoDrawingSelection",
            f: dq
        },
        239: {
            n: "PhoneticInfo",
            f: jZ
        },
        240: {
            n: "SxRule",
            f: dJ
        },
        241: {
            n: "SXEx",
            f: fk
        },
        242: {
            n: "SxFilt",
            f: ic
        },
        244: {
            n: "SxDXF",
            f: jf
        },
        245: {
            n: "SxItm",
            f: aJ
        },
        246: {
            n: "SxName",
            f: fh
        },
        247: {
            n: "SxSelect",
            f: d9
        },
        248: {
            n: "SXPair",
            f: c3
        },
        249: {
            n: "SxFmla",
            f: cC
        },
        251: {
            n: "SxFormat",
            f: es
        },
        252: {
            n: "SST",
            f: aD
        },
        253: {
            n: "LabelSst",
            f: eU
        },
        255: {
            n: "ExtSST",
            f: am
        },
        256: {
            n: "SXVDEx",
            f: hR
        },
        259: {
            n: "SXFormula",
            f: cJ
        },
        290: {
            n: "SXDBEx",
            f: dl
        },
        311: {
            n: "RRDInsDel",
            f: kj
        },
        312: {
            n: "RRDHead",
            f: j2
        },
        315: {
            n: "RRDChgCell",
            f: A
        },
        317: {
            n: "RRTabId",
            f: iU
        },
        318: {
            n: "RRDRenSheet",
            f: fL
        },
        319: {
            n: "RRSort",
            f: jV
        },
        320: {
            n: "RRDMove",
            f: bt
        },
        330: {
            n: "RRFormat",
            f: fK
        },
        331: {
            n: "RRAutoFmt",
            f: jx
        },
        333: {
            n: "RRInsertSh",
            f: bY
        },
        334: {
            n: "RRDMoveBegin",
            f: aK
        },
        335: {
            n: "RRDMoveEnd",
            f: iw
        },
        336: {
            n: "RRDInsDelBegin",
            f: iq
        },
        337: {
            n: "RRDInsDelEnd",
            f: dT
        },
        338: {
            n: "RRDConflict",
            f: t
        },
        339: {
            n: "RRDDefName",
            f: aH
        },
        340: {
            n: "RRDRstEtxp",
            f: gQ
        },
        351: {
            n: "LRng",
            f: cV
        },
        352: {
            n: "UsesELFs",
            f: km
        },
        353: {
            n: "DSF",
            f: fi
        },
        401: {
            n: "CUsr",
            f: b
        },
        402: {
            n: "CbUsr",
            f: jw
        },
        403: {
            n: "UsrInfo",
            f: fn
        },
        404: {
            n: "UsrExcl",
            f: kq
        },
        405: {
            n: "FileLock",
            f: gI
        },
        406: {
            n: "RRDInfo",
            f: ia
        },
        407: {
            n: "BCUsrs",
            f: jc
        },
        408: {
            n: "UsrChk",
            f: ao
        },
        425: {
            n: "UserBView",
            f: j9
        },
        426: {
            n: "UserSViewBegin",
            f: f7
        },
        427: {
            n: "UserSViewEnd",
            f: bl
        },
        428: {
            n: "RRDUserView",
            f: iu
        },
        429: {
            n: "Qsi",
            f: hd
        },
        430: {
            n: "SupBook",
            f: eQ
        },
        431: {
            n: "Prot4Rev",
            f: Q
        },
        432: {
            n: "CondFmt",
            f: hv
        },
        433: {
            n: "CF",
            f: gx
        },
        434: {
            n: "DVal",
            f: c8
        },
        437: {
            n: "DConBin",
            f: eL
        },
        438: {
            n: "TxO",
            f: hK
        },
        439: {
            n: "RefreshAll",
            f: hN
        },
        440: {
            n: "HLink",
            f: bI
        },
        441: {
            n: "Lel",
            f: aF
        },
        442: {
            n: "CodeName",
            f: jY
        },
        443: {
            n: "SXFDBType",
            f: kI
        },
        444: {
            n: "Prot4RevPass",
            f: eX
        },
        445: {
            n: "ObNoMacros",
            f: hW
        },
        446: {
            n: "Dv",
            f: fY
        },
        448: {
            n: "Excel9File",
            f: f2
        },
        449: {
            n: "RecalcId",
            f: jI,
            r: 2
        },
        450: {
            n: "EntExU2",
            f: bH
        },
        512: {
            n: "Dimensions",
            f: f
        },
        513: {
            n: "Blank",
            f: D
        },
        515: {
            n: "Number",
            f: jK
        },
        516: {
            n: "Label",
            f: gF
        },
        517: {
            n: "BoolErr",
            f: gD
        },
        519: {
            n: "String",
            f: dB
        },
        520: {
            n: "Row",
            f: a0
        },
        523: {
            n: "Index",
            f: r
        },
        545: {
            n: "Array",
            f: ja
        },
        549: {
            n: "DefaultRowHeight",
            f: j0
        },
        566: {
            n: "Table",
            f: dC
        },
        574: {
            n: "Window2",
            f: jd
        },
        638: {
            n: "RK",
            f: c7
        },
        659: {
            n: "Style",
            f: ju
        },
        1048: {
            n: "BigName",
            f: Z
        },
        1054: {
            n: "Format",
            f: bq
        },
        1084: {
            n: "ContinueBigName",
            f: fI
        },
        1212: {
            n: "ShrFmla",
            f: hG
        },
        2048: {
            n: "HLinkTooltip",
            f: df
        },
        2049: {
            n: "WebPub",
            f: dS
        },
        2050: {
            n: "QsiSXTag",
            f: eg
        },
        2051: {
            n: "DBQueryExt",
            f: fP
        },
        2052: {
            n: "ExtString",
            f: i0
        },
        2053: {
            n: "TxtQry",
            f: m
        },
        2054: {
            n: "Qsir",
            f: hb
        },
        2055: {
            n: "Qsif",
            f: hf
        },
        2056: {
            n: "RRDTQSIF",
            f: kz
        },
        2057: {
            n: "BOF",
            f: jl
        },
        2058: {
            n: "OleDbConn",
            f: hp
        },
        2059: {
            n: "WOpt",
            f: gH
        },
        2060: {
            n: "SXViewEx",
            f: hY
        },
        2061: {
            n: "SXTH",
            f: ce
        },
        2062: {
            n: "SXPIEx",
            f: Y
        },
        2063: {
            n: "SXVDTEx",
            f: fS
        },
        2064: {
            n: "SXViewEx9",
            f: bC
        },
        2066: {
            n: "ContinueFrt",
            f: cl
        },
        2067: {
            n: "RealTimeData",
            f: gj
        },
        2128: {
            n: "ChartFrtInfo",
            f: au
        },
        2129: {
            n: "FrtWrapper",
            f: ip
        },
        2130: {
            n: "StartBlock",
            f: gV
        },
        2131: {
            n: "EndBlock",
            f: dU
        },
        2132: {
            n: "StartObject",
            f: jk
        },
        2133: {
            n: "EndObject",
            f: hM
        },
        2134: {
            n: "CatLab",
            f: ii
        },
        2135: {
            n: "YMult",
            f: ea
        },
        2136: {
            n: "SXViewLink",
            f: dD
        },
        2137: {
            n: "PivotChartBits",
            f: i5
        },
        2138: {
            n: "FrtFontList",
            f: aC
        },
        2146: {
            n: "SheetExt",
            f: aT
        },
        2147: {
            n: "BookExt",
            f: j8,
            r: 12
        },
        2148: {
            n: "SXAddl",
            f: gO
        },
        2149: {
            n: "CrErr",
            f: dO
        },
        2150: {
            n: "HFPicture",
            f: iy
        },
        2151: {
            n: "FeatHdr",
            f: C
        },
        2152: {
            n: "Feat",
            f: aE
        },
        2154: {
            n: "DataLabExt",
            f: iM
        },
        2155: {
            n: "DataLabExtContents",
            f: eh
        },
        2156: {
            n: "CellWatch",
            f: dN
        },
        2161: {
            n: "FeatHdr11",
            f: cy
        },
        2162: {
            n: "Feature11",
            f: iJ
        },
        2164: {
            n: "DropDownObjIds",
            f: j5
        },
        2165: {
            n: "ContinueFrt11",
            f: dW
        },
        2166: {
            n: "DConn",
            f: g7
        },
        2167: {
            n: "List12",
            f: iD
        },
        2168: {
            n: "Feature12",
            f: iI
        },
        2169: {
            n: "CondFmt12",
            f: ck
        },
        2170: {
            n: "CF12",
            f: hT
        },
        2171: {
            n: "CFEx",
            f: cU
        },
        2172: {
            n: "XFCRC",
            f: h5,
            r: 12
        },
        2173: {
            n: "XFExt",
            f: hj,
            r: 12
        },
        2174: {
            n: "AutoFilter12",
            f: fd
        },
        2175: {
            n: "ContinueFrt12",
            f: dV
        },
        2180: {
            n: "MDTInfo",
            f: ew
        },
        2181: {
            n: "MDXStr",
            f: j4
        },
        2182: {
            n: "MDXTuple",
            f: d7
        },
        2183: {
            n: "MDXSet",
            f: b1
        },
        2184: {
            n: "MDXProp",
            f: db
        },
        2185: {
            n: "MDXKPI",
            f: gZ
        },
        2186: {
            n: "MDB",
            f: bi
        },
        2187: {
            n: "PLV",
            f: aO
        },
        2188: {
            n: "Compat12",
            f: cd,
            r: 12
        },
        2189: {
            n: "DXF",
            f: eb
        },
        2190: {
            n: "TableStyles",
            f: bG,
            r: 12
        },
        2191: {
            n: "TableStyle",
            f: fU
        },
        2192: {
            n: "TableStyleElement",
            f: fm
        },
        2194: {
            n: "StyleExt",
            f: ho
        },
        2195: {
            n: "NamePublish",
            f: ht
        },
        2196: {
            n: "NameCmt",
            f: aP
        },
        2197: {
            n: "SortData",
            f: a6
        },
        2198: {
            n: "Theme",
            f: go,
            r: 12
        },
        2199: {
            n: "GUIDTypeLib",
            f: hU
        },
        2200: {
            n: "FnGrp12",
            f: dv
        },
        2201: {
            n: "NameFnGrp12",
            f: bo
        },
        2202: {
            n: "MTRSettings",
            f: E,
            r: 12
        },
        2203: {
            n: "CompressPictures",
            f: a9
        },
        2204: {
            n: "HeaderFooter",
            f: fp
        },
        2205: {
            n: "CrtLayout12",
            f: eD
        },
        2206: {
            n: "CrtMlFrt",
            f: fM
        },
        2207: {
            n: "CrtMlFrtContinue",
            f: h7
        },
        2211: {
            n: "ForceFullCalculation",
            f: ft
        },
        2212: {
            n: "ShapePropsStream",
            f: kG
        },
        2213: {
            n: "TextPropsStream",
            f: ep
        },
        2214: {
            n: "RichTextStream",
            f: d8
        },
        2215: {
            n: "CrtLayout12A",
            f: e0
        },
        4097: {
            n: "Units",
            f: jz
        },
        4098: {
            n: "Chart",
            f: ka
        },
        4099: {
            n: "Series",
            f: be
        },
        4102: {
            n: "DataFormat",
            f: bj
        },
        4103: {
            n: "LineFormat",
            f: by
        },
        4105: {
            n: "MarkerFormat",
            f: ey
        },
        4106: {
            n: "AreaFormat",
            f: hl
        },
        4107: {
            n: "PieFormat",
            f: bk
        },
        4108: {
            n: "AttachedLabel",
            f: hV
        },
        4109: {
            n: "SeriesText",
            f: fV
        },
        4116: {
            n: "ChartFormat",
            f: q
        },
        4117: {
            n: "Legend",
            f: dP
        },
        4118: {
            n: "SeriesList",
            f: el
        },
        4119: {
            n: "Bar",
            f: eC
        },
        4120: {
            n: "Line",
            f: jT
        },
        4121: {
            n: "Pie",
            f: fw
        },
        4122: {
            n: "Area",
            f: kk
        },
        4123: {
            n: "Scatter",
            f: bA
        },
        4124: {
            n: "CrtLine",
            f: k
        },
        4125: {
            n: "Axis",
            f: ar
        },
        4126: {
            n: "Tick",
            f: jv
        },
        4127: {
            n: "ValueRange",
            f: eZ
        },
        4128: {
            n: "CatSerRange",
            f: aa
        },
        4129: {
            n: "AxisLine",
            f: eE
        },
        4130: {
            n: "CrtLink",
            f: h
        },
        4132: {
            n: "DefaultText",
            f: a3
        },
        4133: {
            n: "Text",
            f: ki
        },
        4134: {
            n: "FontX",
            f: kP
        },
        4135: {
            n: "ObjectLink",
            f: n
        },
        4146: {
            n: "Frame",
            f: F
        },
        4147: {
            n: "Begin",
            f: bZ
        },
        4148: {
            n: "End",
            f: c1
        },
        4149: {
            n: "PlotArea",
            f: P
        },
        4154: {
            n: "Chart3d",
            f: aW
        },
        4156: {
            n: "PicF",
            f: fl
        },
        4157: {
            n: "DropBar",
            f: S
        },
        4158: {
            n: "Radar",
            f: gn
        },
        4159: {
            n: "Surf",
            f: eK
        },
        4160: {
            n: "RadarArea",
            f: gA
        },
        4161: {
            n: "AxisParent",
            f: bE
        },
        4163: {
            n: "LegendException",
            f: jL
        },
        4164: {
            n: "ShtProps",
            f: gr
        },
        4165: {
            n: "SerToCrt",
            f: di
        },
        4166: {
            n: "AxesUsed",
            f: ds
        },
        4168: {
            n: "SBaseRef",
            f: kr
        },
        4170: {
            n: "SerParent",
            f: fW
        },
        4171: {
            n: "SerAuxTrend",
            f: eO
        },
        4174: {
            n: "IFmtRecord",
            f: ax
        },
        4175: {
            n: "Pos",
            f: d4
        },
        4176: {
            n: "AlRuns",
            f: eN
        },
        4177: {
            n: "BRAI",
            f: cX
        },
        4187: {
            n: "SerAuxErrBar",
            f: iT
        },
        4188: {
            n: "ClrtClient",
            f: eP
        },
        4189: {
            n: "SerFmt",
            f: j6
        },
        4191: {
            n: "Chart3DBarShape",
            f: g2
        },
        4192: {
            n: "Fbi",
            f: kc
        },
        4193: {
            n: "BopPop",
            f: fA
        },
        4194: {
            n: "AxcExt",
            f: gT
        },
        4195: {
            n: "Dat",
            f: bu
        },
        4196: {
            n: "PlotGrowth",
            f: ej
        },
        4197: {
            n: "SIIndex",
            f: iZ
        },
        4198: {
            n: "GelFrame",
            f: dc
        },
        4199: {
            n: "BopPopCustom",
            f: e2
        },
        4200: {
            n: "Fbi2",
            f: jy
        },
        22: {
            n: "ExternCount",
            f: jG
        },
        126: {
            n: "RK",
            f: jG
        },
        127: {
            n: "ImData",
            f: jG
        },
        135: {
            n: "Addin",
            f: jG
        },
        136: {
            n: "Edg",
            f: jG
        },
        137: {
            n: "Pub",
            f: jG
        },
        145: {
            n: "Sub",
            f: jG
        },
        148: {
            n: "LHRecord",
            f: jG
        },
        149: {
            n: "LHNGraph",
            f: jG
        },
        150: {
            n: "Sound",
            f: jG
        },
        169: {
            n: "CoordList",
            f: jG
        },
        171: {
            n: "GCW",
            f: jG
        },
        188: {
            n: "ShrFmla",
            f: jG
        },
        194: {
            n: "AddMenu",
            f: jG
        },
        195: {
            n: "DelMenu",
            f: jG
        },
        214: {
            n: "RString",
            f: jG
        },
        223: {
            n: "UDDesc",
            f: jG
        },
        234: {
            n: "TabIdConf",
            f: jG
        },
        354: {
            n: "XL5Modify",
            f: jG
        },
        421: {
            n: "FileSharing2",
            f: jG
        },
        536: {
            n: "Name",
            f: jG
        },
        547: {
            n: "ExternName",
            f: da
        },
        561: {
            n: "Font",
            f: jG
        },
        1030: {
            n: "Formula",
            f: fj
        },
        2157: {
            n: "FeatInfo",
            f: jG
        },
        2163: {
            n: "FeatInfo11",
            f: jG
        },
        2177: {
            n: "SXAddl12",
            f: jG
        },
        2240: {
            n: "AutoWebPub",
            f: jG
        },
        2241: {
            n: "ListObj",
            f: jG
        },
        2242: {
            n: "ListField",
            f: jG
        },
        2243: {
            n: "ListDV",
            f: jG
        },
        2244: {
            n: "ListCondFmt",
            f: jG
        },
        2245: {
            n: "ListCF",
            f: jG
        },
        2246: {
            n: "FMQry",
            f: jG
        },
        2247: {
            n: "FMSQry",
            f: jG
        },
        2248: {
            n: "PLV",
            f: jG
        },
        2249: {
            n: "LnExt",
            f: jG
        },
        2250: {
            n: "MkrExt",
            f: jG
        },
        2251: {
            n: "CrtCoopt",
            f: jG
        },
        0: {}
    };
    var h9 = {
        1: "US",
        2: "CA",
        3: "",
        7: "RU",
        20: "EG",
        30: "GR",
        31: "NL",
        32: "BE",
        33: "FR",
        34: "ES",
        36: "HU",
        39: "IT",
        41: "CH",
        43: "AT",
        44: "GB",
        45: "DK",
        46: "SE",
        47: "NO",
        48: "PL",
        49: "DE",
        52: "MX",
        55: "BR",
        61: "AU",
        64: "NZ",
        66: "TH",
        81: "JP",
        82: "KR",
        84: "VN",
        86: "CN",
        90: "TR",
        105: "JS",
        213: "DZ",
        216: "MA",
        218: "LY",
        351: "PT",
        354: "IS",
        358: "FI",
        420: "CZ",
        886: "TW",
        961: "LB",
        962: "JO",
        963: "SY",
        964: "IQ",
        965: "KW",
        966: "SA",
        971: "AE",
        972: "IL",
        974: "QA",
        981: "IR",
        65535: "US"
    };

    function dR(kQ) {
        return function fs(kS) {
            for (var kR = 0; kR != kQ.length; ++kR) {
                var kT = kQ[kR];
                if (kS[kT[0]] === undefined) {
                    kS[kT[0]] = kT[1]
                }
                if (kT[2] === "n") {
                    kS[kT[0]] = Number(kS[kT[0]])
                }
            }
        }
    }
    var jQ = dR([
        ["cellNF", false],
        ["cellFormula", true],
        ["cellStyles", false],
        ["sheetRows", 0, "n"],
        ["bookSheets", false],
        ["bookProps", false],
        ["bookFiles", false],
        ["password", ""],
        ["WTF", false]
    ]);

    function hi(kS) {
        var kR = {};
        var kT = kS.content;
        var kQ = 28,
            fs;
        fs = jp(kT, kQ);
        kQ += 4 + a5(kT, kQ);
        kR.UserType = fs;
        fs = a5(kT, kQ);
        kQ += 4;
        switch (fs) {
        case 0:
            break;
        case 4294967295:
        case 4294967294:
            kQ += 4;
            break;
        default:
            if (fs > 400) {
                throw new Error("Unsupported Clipboard: " + fs.toString(16))
            }
            kQ += fs
        }
        fs = jp(kT, kQ);
        kQ += fs.length === 0 ? 0 : 5 + fs.length;
        kR.Reserved1 = fs;
        if ((fs = a5(kT, kQ)) !== 1907550708) {
            return kR
        }
        throw "Unsupported Unicode Extension"
    }

    function eA(kV, kQ, kR, fs) {
        var kS = kR;
        var kU = [];
        var kX = kQ.slice(kQ.l, kQ.l + kS);
        if (fs && fs.enc && fs.enc.insitu_decrypt) {
            switch (kV.n) {
            case "BOF":
            case "FilePass":
            case "FileLock":
            case "InterfaceHdr":
            case "RRDInfo":
            case "RRDHead":
            case "UsrExcl":
                break;
            default:
                if (kX.length === 0) {
                    break
                }
                fs.enc.insitu_decrypt(kX)
            }
        }
        kU.push(kX);
        kQ.l += kS;
        var kW = (gv[eS(kQ, kQ.l)]);
        while (kW != null && kW.n === "Continue") {
            kS = eS(kQ, kQ.l + 2);
            kU.push(kQ.slice(kQ.l + 4, kQ.l + 4 + kS));
            kQ.l += 4 + kS;
            kW = (gv[eS(kQ, kQ.l)])
        }
        var kZ = hP(kU);
        hs(kZ, 0);
        var kY = 0;
        kZ.lens = [];
        for (var kT = 0; kT < kU.length; ++kT) {
            kZ.lens.push(kY);
            kY += kU[kT].length
        }
        return kV.f(kZ, kZ.length, fs)
    }

    function eW(kS, fs) {
        if (!kS.XF) {
            return
        }
        try {
            var kQ = kS.XF.ifmt || 0;
            if (kS.t === "e") {
                kS.w = kS.w || e6[kS.v]
            } else {
                if (kQ === 0) {
                    if (kS.t === "n") {
                        if ((kS.v | 0) === kS.v) {
                            kS.w = cN._general_int(kS.v)
                        } else {
                            kS.w = cN._general_num(kS.v)
                        }
                    } else {
                        kS.w = cN._general(kS.v)
                    }
                } else {
                    kS.w = cN.format(kQ, kS.v)
                }
            } if (fs.cellNF) {
                kS.z = cN._table[kQ]
            }
        } catch (kR) {
            if (fs.WTF) {
                throw kR
            }
        }
    }

    function kg(kR, fs, kQ) {
        return {
            v: kR,
            ixfe: fs,
            t: kQ
        }
    }

    function gt(k9, la) {
        var kY = {
            opts: {}
        };
        var kV = {};
        var lo = {};
        var lv = {};
        var ll = false;
        var k0 = {};
        var lg = null;
        var lE = [];
        var fs = "";
        var k1 = {};
        var lA, k4, lj, lD, lx, lp, li;
        var kR = {};
        var kW = [];
        var lb;
        var lr;
        var kS = true;
        var lq = [];
        var lC = [];
        var ls = function k2(lG) {
            if (lG < 8) {
                return bz[lG]
            }
            if (lG < 64) {
                return lC[lG - 8] || bz[lG]
            }
            return bz[lG]
        };
        var kQ = function lc(lG, lH) {
            var lJ = lH.XF.data;
            if (!lJ || !lJ.patternType) {
                return
            }
            lH.s = {};
            lH.s.patternType = lJ.patternType;
            var lI;
            if ((lI = ae(ls(lJ.icvFore)))) {
                lH.s.fgColor = {
                    rgb: lI
                }
            }
            if ((lI = ae(ls(lJ.icvBack)))) {
                lH.s.bgColor = {
                    rgb: lI
                }
            }
        };
        var kT = function kT(lG, lH, lI) {
            if (!kS) {
                return
            }
            if (lI.cellStyles && lH.XF && lH.XF.data) {
                kQ(lG, lH)
            }
            lA = lG;
            k4 = gW(lG);
            if (k0.s) {
                if (lG.r < k0.s.r) {
                    k0.s.r = lG.r
                }
                if (lG.c < k0.s.c) {
                    k0.s.c = lG.c
                }
            }
            if (k0.e) {
                if (lG.r + 1 > k0.e.r) {
                    k0.e.r = lG.r + 1
                }
                if (lG.c + 1 > k0.e.c) {
                    k0.e.c = lG.c + 1
                }
            }
            if (lI.sheetRows && lA.r >= lI.sheetRows) {
                kS = false
            } else {
                lo[k4] = lH
            }
        };
        var ld = {
            enc: false,
            sbcch: 0,
            snames: [],
            sharedf: kR,
            arrayf: kW,
            rrtabid: [],
            lastuser: "",
            biff: 8,
            codepage: 0,
            winlocked: 0,
            wtf: false
        };
        if (la.password) {
            ld.password = la.password
        }
        var k6 = [];
        var lu = [];
        var lz = [
            []
        ];
        var lk = 0,
            k8 = 0,
            lF = 0;
        lz.SheetNames = ld.snames;
        lz.sharedf = ld.sharedf;
        lz.arrayf = ld.arrayf;
        var lf = "";
        var lw = 0;
        while (k9.l < k9.length - 1) {
            var lt = k9.l;
            var k7 = k9.read_shift(2);
            if (k7 === 0 && lf === "EOF") {
                break
            }
            var k5 = (k9.l === k9.length ? 0 : k9.read_shift(2)),
                ln;
            var kX = gv[k7];
            if (kX && kX.f) {
                if (la.bookSheets) {
                    if (lf === "BoundSheet8" && kX.n !== "BoundSheet8") {
                        break
                    }
                }
                lf = kX.n;
                if (kX.r === 2 || kX.r == 12) {
                    var lh = k9.read_shift(2);
                    k5 -= 2;
                    if (!ld.enc && lh !== k7) {
                        throw "rt mismatch"
                    }
                    if (kX.r == 12) {
                        k9.l += 10;
                        k5 -= 10
                    }
                }
                var le;
                if (kX.n === "EOF") {
                    le = kX.f(k9, k5, ld)
                } else {
                    le = eA(kX, k9, k5, ld)
                }
                var kU = kX.n;
                if (ld.biff === 5 || ld.biff === 2) {
                    switch (kU) {
                    case "Lbl":
                        kU = "Label";
                        break
                    }
                }
                switch (kU) {
                case "Date1904":
                    kY.opts.Date1904 = le;
                    break;
                case "WriteProtect":
                    kY.opts.WriteProtect = true;
                    break;
                case "FilePass":
                    if (!ld.enc) {
                        k9.l = 0
                    }
                    ld.enc = le;
                    if (ld.WTF) {
                        console.error(le)
                    }
                    if (!la.password) {
                        throw new Error("File is password-protected")
                    }
                    if (le.Type !== 0) {
                        throw new Error("Encryption scheme unsupported")
                    }
                    if (!le.valid) {
                        throw new Error("Password is incorrect")
                    }
                    break;
                case "WriteAccess":
                    ld.lastuser = le;
                    break;
                case "FileSharing":
                    break;
                case "CodePage":
                    if (le === 21010) {
                        le = 1200
                    } else {
                        if (le === 32769) {
                            le = 1252
                        }
                    }
                    ld.codepage = le;
                    ev(le);
                    break;
                case "RRTabId":
                    ld.rrtabid = le;
                    break;
                case "WinProtect":
                    ld.winlocked = le;
                    break;
                case "Template":
                    break;
                case "RefreshAll":
                    kY.opts.RefreshAll = le;
                    break;
                case "BookBool":
                    break;
                case "UsesELFs":
                    break;
                case "MTRSettings":
                    if (le[0] && le[1]) {
                        throw "Unsupported threads: " + le
                    }
                    break;
                case "CalcCount":
                    kY.opts.CalcCount = le;
                    break;
                case "CalcDelta":
                    kY.opts.CalcDelta = le;
                    break;
                case "CalcIter":
                    kY.opts.CalcIter = le;
                    break;
                case "CalcMode":
                    kY.opts.CalcMode = le;
                    break;
                case "CalcPrecision":
                    kY.opts.CalcPrecision = le;
                    break;
                case "CalcSaveRecalc":
                    kY.opts.CalcSaveRecalc = le;
                    break;
                case "CalcRefMode":
                    ld.CalcRefMode = le;
                    break;
                case "Uncalced":
                    break;
                case "ForceFullCalculation":
                    kY.opts.FullCalc = le;
                    break;
                case "WsBool":
                    break;
                case "XF":
                    lq.push(le);
                    break;
                case "ExtSST":
                    break;
                case "BookExt":
                    break;
                case "RichTextStream":
                    break;
                case "BkHim":
                    break;
                case "SupBook":
                    lz[++lk] = [le];
                    k8 = 0;
                    break;
                case "ExternName":
                    lz[lk][++k8] = le;
                    break;
                case "Index":
                    break;
                case "Lbl":
                    lz[0][++lF] = le;
                    break;
                case "ExternSheet":
                    lz[lk] = lz[lk].concat(le);
                    k8 += le.length;
                    break;
                case "Protect":
                    lo["!protect"] = le;
                    break;
                case "Password":
                    if (le !== 0 && ld.WTF) {
                        console.error("Password verifier: " + le)
                    }
                    break;
                case "Prot4Rev":
                case "Prot4RevPass":
                    break;
                case "BoundSheet8":
                    lv[le.pos] = le;
                    ld.snames.push(le.name);
                    break;
                case "EOF":
                    if (--lw) {
                        break
                    }
                    if (k0.e) {
                        lo["!range"] = k0;
                        if (k0.e.r > 0 && k0.e.c > 0) {
                            k0.e.r--;
                            k0.e.c--;
                            lo["!ref"] = cR(k0);
                            k0.e.r++;
                            k0.e.c++
                        }
                        if (k6.length > 0) {
                            lo["!merges"] = k6
                        }
                        if (lu.length > 0) {
                            lo["!objects"] = lu
                        }
                    }
                    if (fs === "") {
                        k1 = lo
                    } else {
                        kV[fs] = lo
                    }
                    lo = {};
                    break;
                case "BOF":
                    if (ld.biff !== 8) {} else {
                        if (le.BIFFVer === 1280) {
                            ld.biff = 5
                        } else {
                            if (le.BIFFVer === 2) {
                                ld.biff = 2
                            } else {
                                if (le.BIFFVer === 7) {
                                    ld.biff = 2
                                }
                            }
                        }
                    } if (lw++) {
                        break
                    }
                    kS = true;
                    lo = {};
                    if (ld.biff === 2) {
                        if (fs === "") {
                            fs = "Sheet1"
                        }
                        k0 = {
                            s: {
                                r: 0,
                                c: 0
                            },
                            e: {
                                r: 0,
                                c: 0
                            }
                        }
                    } else {
                        fs = (lv[lt] || {
                            name: ""
                        }).name
                    }
                    k6 = [];
                    lu = [];
                    break;
                case "Number":
                case "BIFF2NUM":
                    lb = {
                        ixfe: le.ixfe,
                        XF: lq[le.ixfe],
                        v: le.val,
                        t: "n"
                    };
                    if (lb.XF) {
                        eW(lb, la)
                    }
                    kT({
                        c: le.c,
                        r: le.r
                    }, lb, la);
                    break;
                case "BoolErr":
                    lb = {
                        ixfe: le.ixfe,
                        XF: lq[le.ixfe],
                        v: le.val,
                        t: le.t
                    };
                    if (lb.XF) {
                        eW(lb, la)
                    }
                    kT({
                        c: le.c,
                        r: le.r
                    }, lb, la);
                    break;
                case "RK":
                    lb = {
                        ixfe: le.ixfe,
                        XF: lq[le.ixfe],
                        v: le.rknum,
                        t: "n"
                    };
                    if (lb.XF) {
                        eW(lb, la)
                    }
                    kT({
                        c: le.c,
                        r: le.r
                    }, lb, la);
                    break;
                case "MulRk":
                    for (var lB = le.c; lB <= le.C; ++lB) {
                        var ly = le.rkrec[lB - le.c][0];
                        lb = {
                            ixfe: ly,
                            XF: lq[ly],
                            v: le.rkrec[lB - le.c][1],
                            t: "n"
                        };
                        if (lb.XF) {
                            eW(lb, la)
                        }
                        kT({
                            c: lB,
                            r: le.r
                        }, lb, la)
                    }
                    break;
                case "Formula":
                    switch (le.val) {
                    case "String":
                        lg = le;
                        break;
                    case "Array Formula":
                        throw "Array Formula unsupported";
                    default:
                        lb = {
                            v: le.val,
                            ixfe: le.cell.ixfe,
                            t: le.tt
                        };
                        lb.XF = lq[lb.ixfe];
                        if (la.cellFormula) {
                            lb.f = "=" + ex(le.formula, k0, le.cell, lz, ld)
                        }
                        if (lb.XF) {
                            eW(lb, la)
                        }
                        kT(le.cell, lb, la);
                        lg = le
                    }
                    break;
                case "String":
                    if (lg) {
                        lg.val = le;
                        lb = {
                            v: lg.val,
                            ixfe: lg.cell.ixfe,
                            t: "s"
                        };
                        lb.XF = lq[lb.ixfe];
                        if (la.cellFormula) {
                            lb.f = "=" + ex(lg.formula, k0, lg.cell, lz, ld)
                        }
                        if (lb.XF) {
                            eW(lb, la)
                        }
                        kT(lg.cell, lb, la);
                        lg = null
                    }
                    break;
                case "Array":
                    kW.push(le);
                    break;
                case "ShrFmla":
                    if (!kS) {
                        break
                    }
                    kR[gW(lg.cell)] = le[0];
                    break;
                case "LabelSst":
                    lb = kg(lE[le.isst].t, le.ixfe, "s");
                    lb.XF = lq[lb.ixfe];
                    if (lb.XF) {
                        eW(lb, la)
                    }
                    kT({
                        c: le.c,
                        r: le.r
                    }, lb, la);
                    break;
                case "Label":
                case "BIFF2STR":
                    lb = kg(le.val, le.ixfe, "s");
                    lb.XF = lq[lb.ixfe];
                    if (lb.XF) {
                        eW(lb, la)
                    }
                    kT({
                        c: le.c,
                        r: le.r
                    }, lb, la);
                    break;
                case "Dimensions":
                    if (lw === 1) {
                        k0 = le
                    }
                    break;
                case "SST":
                    lE = le;
                    break;
                case "Format":
                    cN.load(le[1], le[0]);
                    break;
                case "MergeCells":
                    k6 = k6.concat(le);
                    break;
                case "Obj":
                    lu[le.cmo[0]] = ld.lastobj = le;
                    break;
                case "TxO":
                    ld.lastobj.TxO = le;
                    break;
                case "HLink":
                    for (li = le[0].s.r; li <= le[0].e.r; ++li) {
                        for (lp = le[0].s.c; lp <= le[0].e.c; ++lp) {
                            if (lo[gW({
                                c: lp,
                                r: li
                            })]) {
                                lo[gW({
                                    c: lp,
                                    r: li
                                })].l = le[1]
                            }
                        }
                    }
                    break;
                case "HLinkTooltip":
                    for (li = le[0].s.r; li <= le[0].e.r; ++li) {
                        for (lp = le[0].s.c; lp <= le[0].e.c; ++lp) {
                            if (lo[gW({
                                c: lp,
                                r: li
                            })]) {
                                lo[gW({
                                    c: lp,
                                    r: li
                                })].l.tooltip = le[1]
                            }
                        }
                    }
                    break;
                case "Note":
                    if (ld.biff <= 5 && ld.biff >= 2) {
                        break
                    }
                    lj = lo[gW(le[0])];
                    var k3 = lu[le[2]];
                    if (!lj) {
                        break
                    }
                    if (!lj.c) {
                        lj.c = []
                    }
                    lD = {
                        a: le[1],
                        t: k3.TxO.t
                    };
                    lj.c.push(lD);
                    break;
                default:
                    switch (kX.n) {
                    case "ClrtClient":
                        break;
                    case "XFExt":
                        cp(lq[le.ixfe], le.ext);
                        break;
                    case "NameCmt":
                        break;
                    case "Header":
                        break;
                    case "Footer":
                        break;
                    case "HCenter":
                        break;
                    case "VCenter":
                        break;
                    case "Pls":
                        break;
                    case "Setup":
                        break;
                    case "DefColWidth":
                        break;
                    case "GCW":
                        break;
                    case "LHRecord":
                        break;
                    case "ColInfo":
                        break;
                    case "Row":
                        break;
                    case "DBCell":
                        break;
                    case "MulBlank":
                        break;
                    case "EntExU2":
                        break;
                    case "SxView":
                        break;
                    case "Sxvd":
                        break;
                    case "SXVI":
                        break;
                    case "SXVDEx":
                        break;
                    case "SxIvd":
                        break;
                    case "SXDI":
                        break;
                    case "SXLI":
                        break;
                    case "SXEx":
                        break;
                    case "QsiSXTag":
                        break;
                    case "Selection":
                        break;
                    case "Feat":
                        break;
                    case "FeatHdr":
                    case "FeatHdr11":
                        break;
                    case "Feature11":
                    case "Feature12":
                    case "List12":
                        break;
                    case "Blank":
                        break;
                    case "Country":
                        lr = le;
                        break;
                    case "RecalcId":
                        break;
                    case "DefaultRowHeight":
                    case "DxGCol":
                        break;
                    case "Fbi":
                    case "Fbi2":
                    case "GelFrame":
                        break;
                    case "Font":
                        break;
                    case "XFCRC":
                        break;
                    case "Style":
                        break;
                    case "StyleExt":
                        break;
                    case "Palette":
                        lC = le;
                        break;
                    case "Theme":
                        break;
                    case "ScenarioProtect":
                        break;
                    case "ObjProtect":
                        break;
                    case "CondFmt12":
                        break;
                    case "Table":
                        break;
                    case "TableStyles":
                        break;
                    case "TableStyle":
                        break;
                    case "TableStyleElement":
                        break;
                    case "SXStreamID":
                        break;
                    case "SXVS":
                        break;
                    case "DConRef":
                        break;
                    case "SXAddl":
                        break;
                    case "DConBin":
                        break;
                    case "DConName":
                        break;
                    case "SXPI":
                        break;
                    case "SxFormat":
                        break;
                    case "SxSelect":
                        break;
                    case "SxRule":
                        break;
                    case "SxFilt":
                        break;
                    case "SxItm":
                        break;
                    case "SxDXF":
                        break;
                    case "ScenMan":
                        break;
                    case "DCon":
                        break;
                    case "CellWatch":
                        break;
                    case "PrintRowCol":
                        break;
                    case "PrintGrid":
                        break;
                    case "PrintSize":
                        break;
                    case "XCT":
                        break;
                    case "CRN":
                        break;
                    case "Scl":
                        break;
                    case "SheetExt":
                        break;
                    case "SheetExtOptional":
                        break;
                    case "ObNoMacros":
                        break;
                    case "ObProj":
                        break;
                    case "CodeName":
                        break;
                    case "GUIDTypeLib":
                        break;
                    case "WOpt":
                        break;
                    case "PhoneticInfo":
                        break;
                    case "OleObjectSize":
                        break;
                    case "DXF":
                    case "DXFN":
                    case "DXFN12":
                    case "DXFN12List":
                    case "DXFN12NoCB":
                        break;
                    case "Dv":
                    case "DVal":
                        break;
                    case "BRAI":
                    case "Series":
                    case "SeriesText":
                        break;
                    case "DConn":
                        break;
                    case "DbOrParamQry":
                        break;
                    case "DBQueryExt":
                        break;
                    case "IFmtRecord":
                        break;
                    case "CondFmt":
                    case "CF":
                    case "CF12":
                    case "CFEx":
                        break;
                    case "Excel9File":
                        break;
                    case "Units":
                        break;
                    case "InterfaceHdr":
                    case "Mms":
                    case "InterfaceEnd":
                    case "DSF":
                    case "BuiltInFnGroupCount":
                    case "Window1":
                    case "Window2":
                    case "HideObj":
                    case "GridSet":
                    case "Guts":
                    case "UserBView":
                    case "UserSViewBegin":
                    case "UserSViewEnd":
                    case "Pane":
                        break;
                    default:
                        switch (kX.n) {
                        case "Dat":
                        case "Begin":
                        case "End":
                        case "StartBlock":
                        case "EndBlock":
                        case "Frame":
                        case "Area":
                        case "Axis":
                        case "AxisLine":
                        case "Tick":
                            break;
                        case "AxesUsed":
                        case "CrtLayout12":
                        case "CrtLayout12A":
                        case "CrtLink":
                        case "CrtLine":
                        case "CrtMlFrt":
                        case "CrtMlFrtContinue":
                            break;
                        case "LineFormat":
                        case "AreaFormat":
                        case "Chart":
                        case "Chart3d":
                        case "Chart3DBarShape":
                        case "ChartFormat":
                        case "ChartFrtInfo":
                            break;
                        case "PlotArea":
                        case "PlotGrowth":
                            break;
                        case "SeriesList":
                        case "SerParent":
                        case "SerAuxTrend":
                            break;
                        case "DataFormat":
                        case "SerToCrt":
                        case "FontX":
                            break;
                        case "CatSerRange":
                        case "AxcExt":
                        case "SerFmt":
                            break;
                        case "ShtProps":
                            break;
                        case "DefaultText":
                        case "Text":
                        case "CatLab":
                            break;
                        case "DataLabExtContents":
                            break;
                        case "Legend":
                        case "LegendException":
                            break;
                        case "Pie":
                        case "Scatter":
                            break;
                        case "PieFormat":
                        case "MarkerFormat":
                            break;
                        case "StartObject":
                        case "EndObject":
                            break;
                        case "AlRuns":
                        case "ObjectLink":
                            break;
                        case "SIIndex":
                            break;
                        case "AttachedLabel":
                        case "YMult":
                            break;
                        case "Line":
                        case "Bar":
                            break;
                        case "Surf":
                            break;
                        case "AxisParent":
                            break;
                        case "Pos":
                            break;
                        case "ValueRange":
                            break;
                        case "SXViewEx9":
                            break;
                        case "SXViewLink":
                            break;
                        case "PivotChartBits":
                            break;
                        case "SBaseRef":
                            break;
                        case "TextPropsStream":
                            break;
                        case "LnExt":
                            break;
                        case "MkrExt":
                            break;
                        case "CrtCoopt":
                            break;
                        case "Qsi":
                        case "Qsif":
                        case "Qsir":
                        case "QsiSXTag":
                            break;
                        case "TxtQry":
                            break;
                        case "FilterMode":
                            break;
                        case "AutoFilter":
                        case "AutoFilterInfo":
                            break;
                        case "AutoFilter12":
                            break;
                        case "DropDownObjIds":
                            break;
                        case "Sort":
                            break;
                        case "SortData":
                            break;
                        case "ShapePropsStream":
                            break;
                        case "MsoDrawing":
                        case "MsoDrawingGroup":
                        case "MsoDrawingSelection":
                            break;
                        case "ImData":
                            break;
                        case "WebPub":
                        case "AutoWebPub":
                        case "RightMargin":
                        case "LeftMargin":
                        case "TopMargin":
                        case "BottomMargin":
                        case "HeaderFooter":
                        case "HFPicture":
                        case "PLV":
                        case "HorizontalPageBreaks":
                        case "VerticalPageBreaks":
                        case "Backup":
                        case "CompressPictures":
                        case "Compat12":
                            break;
                        case "Continue":
                        case "ContinueFrt12":
                            break;
                        case "FrtFontList":
                        case "FrtWrapper":
                            break;
                        case "ExternCount":
                            break;
                        case "RString":
                            break;
                        case "TabIdConf":
                        case "Radar":
                        case "RadarArea":
                        case "DropBar":
                        case "Intl":
                        case "CoordList":
                        case "SerAuxErrBar":
                            break;
                        default:
                            switch (kX.n) {
                            case "SCENARIO":
                            case "DConBin":
                            case "PicF":
                            case "DataLabExt":
                            case "Lel":
                            case "BopPop":
                            case "BopPopCustom":
                            case "RealTimeData":
                            case "Name":
                                break;
                            default:
                                if (la.WTF) {
                                    throw "Unrecognized Record " + kX.n
                                }
                            }
                        }
                    }
                }
            } else {
                k9.l += k5
            }
        }
        var lm = ld.biff === 2 ? ["Sheet1"] : Object.keys(lv).sort(function (lH, lG) {
            return Number(lH) - Number(lG)
        }).map(function (lG) {
            return lv[lG].name
        });
        var kZ = lm.slice();
        kY.Directory = lm;
        kY.SheetNames = lm;
        if (!la.bookSheets) {
            kY.Sheets = kV
        }
        kY.Preamble = k1;
        kY.Strings = lE;
        kY.SSF = cN.get_table();
        if (ld.enc) {
            kY.Encryption = ld.enc
        }
        kY.Metadata = {};
        if (lr !== undefined) {
            kY.Metadata.Country = lr
        }
        return kY
    }

    function dX(kX, kY) {
        if (!kY) {
            kY = {}
        }
        jQ(kY);
        fC();
        var kR, fs, kV;
        if (kX.find) {
            kR = kX.find("!CompObj");
            fs = kX.find("!SummaryInformation");
            kV = kX.find("/Workbook")
        } else {
            hs(kX, 0);
            kV = {
                content: kX
            }
        } if (!kV) {
            kV = kX.find("/Book")
        }
        var kQ, kT, kS;
        if (kR) {
            kQ = hi(kR)
        }
        if (kY.bookProps && !kY.bookSheets) {
            kS = {}
        } else {
            if (kV) {
                kS = gt(kV.content, kY, !! kV.find)
            } else {
                throw new Error("Cannot find Workbook stream")
            }
        } if (kX.find) {
            bS(kX)
        }
        var kU = {};
        for (var kW in kX.Summary) {
            kU[kW] = kX.Summary[kW]
        }
        for (kW in kX.DocSummary) {
            kU[kW] = kX.DocSummary[kW]
        }
        kS.Props = kS.Custprops = kU;
        if (kY.bookFiles) {
            kS.cfb = kX
        }
        kS.CompObjP = kQ;
        return kS
    }

    function bS(kQ) {
        var kR = kQ.find("!DocumentSummaryInformation");
        if (kR) {
            try {
                kQ.DocSummary = fa(kR, gd)
            } catch (kS) {}
        }
        var fs = kQ.find("!SummaryInformation");
        if (fs) {
            try {
                kQ.Summary = fa(fs, ed)
            } catch (kS) {}
        }
    }
    var d5 = /&[a-z]*;/g,
        bg = /_x([0-9a-fA-F]+)_/g;

    function ir(fs, kQ) {
        return ak(parseInt(kQ, 16))
    }

    function gJ(fs) {
        return g5[fs]
    }

    function G(fs) {
        if (fs.indexOf("&") > -1) {
            fs = fs.replace(d5, gJ)
        }
        return fs.indexOf("_") === -1 ? fs : fs.replace(bg, ir)
    }

    function iQ(kQ, fs) {
        switch (kQ) {
        case "1":
        case "true":
        case "TRUE":
            return true;
        default:
            return false
        }
    }

    function h3(kQ, fs) {
        return new RegExp("<" + kQ + '(?: xml:space="preserve")?>([^\u2603]*)</' + kQ + ">", (fs || "") + "m")
    }
    var H = /&#(\d+);/g;

    function hA(kQ, fs) {
        return String.fromCharCode(parseInt(fs, 10))
    }

    function cf(fs) {
        return fs.replace(H, hA)
    }
    var eo = {
        "General Number": "General",
        "General Date": cN._table[22],
        "Long Date": "dddd, mmmm dd, yyyy",
        "Medium Date": cN._table[15],
        "Short Date": cN._table[14],
        "Long Time": cN._table[19],
        "Medium Time": cN._table[18],
        "Short Time": cN._table[20],
        Currency: '"$"#,##0.00_);[Red]\\("$"#,##0.00\\)',
        Fixed: cN._table[2],
        Standard: cN._table[4],
        Percent: cN._table[10],
        Scientific: cN._table[11],
        "Yes/No": '"Yes";"Yes";"No";@',
        "True/False": '"True";"True";"False";@',
        "On/Off": '"Yes";"Yes";"No";@'
    };
    var b7 = {
        None: "none",
        Solid: "solid",
        Gray50: "mediumGray",
        Gray75: "darkGray",
        Gray25: "lightGray",
        HorzStripe: "darkHorizontal",
        VertStripe: "darkVertical",
        ReverseDiagStripe: "darkDown",
        DiagStripe: "darkUp",
        DiagCross: "darkGrid",
        ThickDiagCross: "darkTrellis",
        ThinHorzStripe: "lightHorizontal",
        ThinVertStripe: "lightVertical",
        ThinReverseDiagStripe: "lightDown",
        ThinHorzCross: "lightGrid"
    };

    function ad(kQ, fs, kR) {
        switch (fs) {
        case "Description":
            fs = "Comments";
            break
        }
        kQ[fs] = kR
    }

    function fq(kR, kQ) {
        var fs = eo[kR] || G(kR);
        if (fs === "General") {
            return cN._general(kQ)
        }
        return cN.format(fs, kQ)
    }

    function en(fs, kS, kQ, kR) {
        switch ((kQ[0].match(/dt:dt="([\w.]+)"/) || ["", ""])[1]) {
        case "boolean":
            kR = iQ(kR);
            break;
        case "i2":
        case "int":
            kR = parseInt(kR, 10);
            break;
        case "r4":
        case "float":
            kR = parseFloat(kR);
            break;
        case "date":
        case "dateTime.tz":
            kR = new Date(kR);
            break;
        case "i8":
        case "string":
        case "fixed":
        case "uuid":
        case "bin.base64":
            break;
        default:
            throw "bad custprop:" + kQ[0]
        }
        fs[G(kS[3])] = kR
    }

    function dw(fs, kQ, kS) {
        try {
            if (fs.t === "e") {
                fs.w = fs.w || e6[fs.v]
            } else {
                if (kQ === "General") {
                    if (fs.t === "n") {
                        if ((fs.v | 0) === fs.v) {
                            fs.w = cN._general_int(fs.v)
                        } else {
                            fs.w = cN._general_num(fs.v)
                        }
                    } else {
                        fs.w = cN._general(fs.v)
                    }
                } else {
                    fs.w = fq(kQ || "General", fs.v)
                }
            } if (kS.cellNF) {
                fs.z = eo[kQ] || kQ || "General"
            }
        } catch (kR) {
            if (kS.WTF) {
                throw kR
            }
        }
    }

    function hX(kR, kS, kQ) {
        if (kQ.cellStyles) {
            if (kS.Interior) {
                var fs = kS.Interior;
                if (fs.Pattern) {
                    fs.patternType = b7[fs.Pattern] || fs.Pattern
                }
            }
        }
        kR[kS.ID] = kS
    }

    function js(kV, k1, kU, kY, kQ, kZ, kW, k0, kR) {
        var kS = "General",
            fs = kY.StyleID,
            kT = {};
        kR = kR || {};
        var kX = [];
        if (fs === undefined && k0) {
            fs = k0.StyleID
        }
        if (fs === undefined && kW) {
            fs = kW.StyleID
        }
        while (kZ[fs] !== undefined) {
            if (kZ[fs].nf) {
                kS = kZ[fs].nf
            }
            if (kZ[fs].Interior) {
                kX.push(kZ[fs].Interior)
            }
            if (!kZ[fs].Parent) {
                break
            }
            fs = kZ[fs].Parent
        }
        switch (kU.Type) {
        case "Boolean":
            kY.t = "b";
            kY.v = iQ(kV);
            break;
        case "String":
            kY.t = "s";
            kY.r = cf(G(kV));
            kY.v = kV.indexOf("<") > -1 ? k1 : kY.r;
            break;
        case "DateTime":
            kY.v = (Date.parse(kV) - new Date(Date.UTC(1899, 11, 30))) / (24 * 60 * 60 * 1000);
            if (kY.v !== kY.v) {
                kY.v = G(kV)
            } else {
                if (kY.v >= 1 && kY.v < 60) {
                    kY.v = kY.v - 1
                }
            } if (!kS || kS == "General") {
                kS = "yyyy-mm-dd"
            }
        case "Number":
            if (kY.v === undefined) {
                kY.v = +kV
            }
            if (!kY.t) {
                kY.t = "n"
            }
            break;
        case "Error":
            kY.t = "e";
            kY.v = i2[kV];
            kY.w = kV;
            break;
        default:
            kY.t = "s";
            kY.v = cf(k1);
            break
        }
        dw(kY, kS, kR);
        if (kR.cellFormula != null && kY.Formula) {
            kY.f = hm(G(kY.Formula), kQ);
            kY.Formula = undefined
        }
        if (kR.cellStyles) {
            kX.forEach(function (k2) {
                if (!kT.patternType && k2.patternType) {
                    kT.patternType = k2.patternType
                }
            });
            kY.s = kT
        }
        kY.ixfe = kY.StyleID !== undefined ? kY.StyleID : "Default"
    }

    function jB(fs) {
        fs.t = fs.v;
        fs.v = fs.w = fs.ixfe = undefined
    }

    function kK(fs) {
        if (I && Buffer.isBuffer(fs)) {
            return fs.toString("utf8")
        }
        if (typeof fs === "string") {
            return fs
        }
        throw "badf"
    }
    var iB = /<(\/?)([a-z0-9]*:|)(\w+)[^>]*>/mg;

    function hu(lk, k5) {
        var la = kK(lk);
        var kR;
        var lo = [],
            lf;
        var k7 = {}, kT = [],
            kW = {}, kQ = "";
        var k3 = {}, k6 = {}, kZ = {}, lh, ln;
        var ll = 0,
            le = 0;
        var kV = {
            s: {
                r: 1000000,
                c: 1000000
            },
            e: {
                r: 0,
                c: 0
            }
        };
        var k8 = {}, fs = {};
        var kX = "",
            lg = 0;
        var k0 = [];
        var kY = {}, k1 = {}, lj = 0,
            k4 = {};
        var kS = [],
            lb = {};
        var lm = [],
            kU;
        while ((kR = iB.exec(la))) {
            switch (kR[3]) {
            case "Data":
                if (lo[lo.length - 1][1]) {
                    break
                }
                if (kR[1] === "/") {
                    js(la.slice(ln, kR.index), kX, lh, lo[lo.length - 1][0] == "Comment" ? lb : k6, {
                        c: ll,
                        r: le
                    }, k8, lm[ll], kZ, k5)
                } else {
                    kX = "";
                    lh = aB(kR[0]);
                    ln = kR.index + kR[0].length
                }
                break;
            case "Cell":
                if (kR[1] === "/") {
                    if (kS.length > 0) {
                        k6.c = kS
                    }
                    if ((!k5.sheetRows || k5.sheetRows > le) && k6.v !== undefined) {
                        kW[cA(ll) + jH(le)] = k6
                    }
                    if (k6.HRef) {
                        k6.l = {
                            Target: k6.HRef,
                            tooltip: k6.HRefScreenTip
                        };
                        k6.HRef = k6.HRefScreenTip = undefined
                    }
                    if (k6.MergeAcross || k6.MergeDown) {
                        var lc = ll + (parseInt(k6.MergeAcross, 10) | 0);
                        var k9 = le + (parseInt(k6.MergeDown, 10) | 0);
                        k0.push({
                            s: {
                                c: ll,
                                r: le
                            },
                            e: {
                                c: lc,
                                r: k9
                            }
                        })
                    }++ll;
                    if (k6.MergeAcross) {
                        ll += +k6.MergeAcross
                    }
                } else {
                    k6 = b3(kR[0]);
                    if (k6.Index) {
                        ll = +k6.Index - 1
                    }
                    if (ll < kV.s.c) {
                        kV.s.c = ll
                    }
                    if (ll > kV.e.c) {
                        kV.e.c = ll
                    }
                    if (kR[0].substr(-2) === "/>") {
                        ++ll
                    }
                    kS = []
                }
                break;
            case "Row":
                if (kR[1] === "/" || kR[0].substr(-2) === "/>") {
                    if (le < kV.s.r) {
                        kV.s.r = le
                    }
                    if (le > kV.e.r) {
                        kV.e.r = le
                    }
                    if (kR[0].substr(-2) === "/>") {
                        kZ = aB(kR[0]);
                        if (kZ.Index) {
                            le = +kZ.Index - 1
                        }
                    }
                    ll = 0;
                    ++le
                } else {
                    kZ = aB(kR[0]);
                    if (kZ.Index) {
                        le = +kZ.Index - 1
                    }
                }
                break;
            case "Worksheet":
                if (kR[1] === "/") {
                    if ((lf = lo.pop())[0] !== kR[3]) {
                        throw "Bad state: " + lf
                    }
                    kT.push(kQ);
                    if (kV.s.r <= kV.e.r && kV.s.c <= kV.e.c) {
                        kW["!ref"] = cR(kV)
                    }
                    if (k0.length) {
                        kW["!merges"] = k0
                    }
                    k7[kQ] = kW
                } else {
                    kV = {
                        s: {
                            r: 1000000,
                            c: 1000000
                        },
                        e: {
                            r: 0,
                            c: 0
                        }
                    };
                    le = ll = 0;
                    lo.push([kR[3], false]);
                    lf = aB(kR[0]);
                    kQ = lf.Name;
                    kW = {};
                    k0 = []
                }
                break;
            case "Table":
                if (kR[1] === "/") {
                    if ((lf = lo.pop())[0] !== kR[3]) {
                        throw "Bad state: " + lf
                    }
                } else {
                    if (kR[0].slice(-2) == "/>") {
                        break
                    } else {
                        k3 = aB(kR[0]);
                        lo.push([kR[3], false]);
                        lm = []
                    }
                }
                break;
            case "Style":
                if (kR[1] === "/") {
                    hX(k8, fs, k5)
                } else {
                    fs = aB(kR[0])
                }
                break;
            case "NumberFormat":
                fs.nf = aB(kR[0]).Format || "General";
                break;
            case "Column":
                if (lo[lo.length - 1][0] !== "Table") {
                    break
                }
                kU = aB(kR[0]);
                lm[(kU.Index - 1 || lm.length)] = kU;
                for (var li = 0; li < +kU.Span; ++li) {
                    lm[lm.length] = kU
                }
                break;
            case "NamedRange":
                break;
            case "NamedCell":
                break;
            case "B":
                break;
            case "I":
                break;
            case "U":
                break;
            case "S":
                break;
            case "Sub":
                break;
            case "Sup":
                break;
            case "Span":
                break;
            case "Border":
                break;
            case "Alignment":
                break;
            case "Borders":
                break;
            case "Font":
                if (kR[0].substr(-2) === "/>") {
                    break
                } else {
                    if (kR[1] === "/") {
                        kX += la.slice(lg, kR.index)
                    } else {
                        lg = kR.index + kR[0].length
                    }
                }
                break;
            case "Interior":
                if (!k5.cellStyles) {
                    break
                }
                fs.Interior = aB(kR[0]);
                break;
            case "Protection":
                break;
            case "Author":
            case "Title":
            case "Description":
            case "Created":
            case "Keywords":
            case "Subject":
            case "Category":
            case "Company":
            case "LastAuthor":
            case "LastSaved":
            case "LastPrinted":
            case "Version":
            case "Revision":
            case "TotalTime":
            case "HyperlinkBase":
            case "Manager":
                if (kR[0].substr(-2) === "/>") {
                    break
                } else {
                    if (kR[1] === "/") {
                        ad(kY, kR[3], la.slice(lj, kR.index))
                    } else {
                        lj = kR.index + kR[0].length
                    }
                }
                break;
            case "Paragraphs":
                break;
            case "Styles":
            case "Workbook":
                if (kR[1] === "/") {
                    if ((lf = lo.pop())[0] !== kR[3]) {
                        throw "Bad state: " + lf
                    }
                } else {
                    lo.push([kR[3], false])
                }
                break;
            case "Comment":
                if (kR[1] === "/") {
                    if ((lf = lo.pop())[0] !== kR[3]) {
                        throw "Bad state: " + lf
                    }
                    jB(lb);
                    kS.push(lb)
                } else {
                    lo.push([kR[3], false]);
                    lf = aB(kR[0]);
                    lb = {
                        a: lf.Author
                    }
                }
                break;
            case "Name":
                break;
            case "ComponentOptions":
            case "DocumentProperties":
            case "CustomDocumentProperties":
            case "OfficeDocumentSettings":
            case "PivotTable":
            case "PivotCache":
            case "Names":
            case "MapInfo":
            case "PageBreaks":
            case "QueryTable":
            case "DataValidation":
            case "AutoFilter":
            case "Sorting":
            case "Schema":
            case "data":
            case "ConditionalFormatting":
            case "SmartTagType":
            case "SmartTags":
            case "ExcelWorkbook":
            case "WorkbookOptions":
            case "WorksheetOptions":
                if (kR[1] === "/") {
                    if ((lf = lo.pop())[0] !== kR[3]) {
                        throw "Bad state: " + lf
                    }
                } else {
                    if (kR[0].charAt(kR[0].length - 2) !== "/") {
                        lo.push([kR[3], true])
                    }
                }
                break;
            default:
                var k2 = true;
                switch (lo[lo.length - 1][0]) {
                case "OfficeDocumentSettings":
                    switch (kR[3]) {
                    case "AllowPNG":
                        break;
                    case "RemovePersonalInformation":
                        break;
                    case "DownloadComponents":
                        break;
                    case "LocationOfComponents":
                        break;
                    case "Colors":
                        break;
                    case "Color":
                        break;
                    case "Index":
                        break;
                    case "RGB":
                        break;
                    case "PixelsPerInch":
                        break;
                    case "TargetScreenSize":
                        break;
                    case "ReadOnlyRecommended":
                        break;
                    default:
                        k2 = false
                    }
                    break;
                case "ComponentOptions":
                    switch (kR[3]) {
                    case "Toolbar":
                        break;
                    case "HideOfficeLogo":
                        break;
                    case "SpreadsheetAutoFit":
                        break;
                    case "Label":
                        break;
                    case "Caption":
                        break;
                    case "MaxHeight":
                        break;
                    case "MaxWidth":
                        break;
                    case "NextSheetNumber":
                        break;
                    default:
                        k2 = false
                    }
                    break;
                case "ExcelWorkbook":
                    switch (kR[3]) {
                    case "WindowHeight":
                        break;
                    case "WindowWidth":
                        break;
                    case "WindowTopX":
                        break;
                    case "WindowTopY":
                        break;
                    case "TabRatio":
                        break;
                    case "ProtectStructure":
                        break;
                    case "ProtectWindows":
                        break;
                    case "ActiveSheet":
                        break;
                    case "DisplayInkNotes":
                        break;
                    case "FirstVisibleSheet":
                        break;
                    case "SupBook":
                        break;
                    case "SheetName":
                        break;
                    case "SheetIndex":
                        break;
                    case "SheetIndexFirst":
                        break;
                    case "SheetIndexLast":
                        break;
                    case "Dll":
                        break;
                    case "AcceptLabelsInFormulas":
                        break;
                    case "DoNotSaveLinkValues":
                        break;
                    case "Date1904":
                        break;
                    case "Iteration":
                        break;
                    case "MaxIterations":
                        break;
                    case "MaxChange":
                        break;
                    case "Path":
                        break;
                    case "Xct":
                        break;
                    case "Count":
                        break;
                    case "SelectedSheets":
                        break;
                    case "Calculation":
                        break;
                    case "Uncalced":
                        break;
                    case "StartupPrompt":
                        break;
                    case "Crn":
                        break;
                    case "ExternName":
                        break;
                    case "Formula":
                        break;
                    case "ColFirst":
                        break;
                    case "ColLast":
                        break;
                    case "WantAdvise":
                        break;
                    case "Boolean":
                        break;
                    case "Error":
                        break;
                    case "Text":
                        break;
                    case "OLE":
                        break;
                    case "NoAutoRecover":
                        break;
                    case "PublishObjects":
                        break;
                    case "DoNotCalculateBeforeSave":
                        break;
                    case "Number":
                        break;
                    case "RefModeR1C1":
                        break;
                    case "EmbedSaveSmartTags":
                        break;
                    default:
                        k2 = false
                    }
                    break;
                case "WorkbookOptions":
                    switch (kR[3]) {
                    case "OWCVersion":
                        break;
                    case "Height":
                        break;
                    case "Width":
                        break;
                    default:
                        k2 = false
                    }
                    break;
                case "WorksheetOptions":
                    switch (kR[3]) {
                    case "Unsynced":
                        break;
                    case "Visible":
                        break;
                    case "Print":
                        break;
                    case "Panes":
                        break;
                    case "Scale":
                        break;
                    case "Pane":
                        break;
                    case "Number":
                        break;
                    case "Layout":
                        break;
                    case "Header":
                        break;
                    case "Footer":
                        break;
                    case "PageSetup":
                        break;
                    case "PageMargins":
                        break;
                    case "Selected":
                        break;
                    case "ProtectObjects":
                        break;
                    case "EnableSelection":
                        break;
                    case "ProtectScenarios":
                        break;
                    case "ValidPrinterInfo":
                        break;
                    case "HorizontalResolution":
                        break;
                    case "VerticalResolution":
                        break;
                    case "NumberofCopies":
                        break;
                    case "ActiveRow":
                        break;
                    case "ActiveCol":
                        break;
                    case "ActivePane":
                        break;
                    case "TopRowVisible":
                        break;
                    case "TopRowBottomPane":
                        break;
                    case "LeftColumnVisible":
                        break;
                    case "LeftColumnRightPane":
                        break;
                    case "FitToPage":
                        break;
                    case "RangeSelection":
                        break;
                    case "PaperSizeIndex":
                        break;
                    case "PageLayoutZoom":
                        break;
                    case "PageBreakZoom":
                        break;
                    case "FilterOn":
                        break;
                    case "DoNotDisplayGridlines":
                        break;
                    case "SplitHorizontal":
                        break;
                    case "SplitVertical":
                        break;
                    case "FreezePanes":
                        break;
                    case "FrozenNoSplit":
                        break;
                    case "FitWidth":
                        break;
                    case "FitHeight":
                        break;
                    case "CommentsLayout":
                        break;
                    case "Zoom":
                        break;
                    case "LeftToRight":
                        break;
                    case "Gridlines":
                        break;
                    case "AllowSort":
                        break;
                    case "AllowFilter":
                        break;
                    case "AllowInsertRows":
                        break;
                    case "AllowDeleteRows":
                        break;
                    case "AllowInsertCols":
                        break;
                    case "AllowDeleteCols":
                        break;
                    case "AllowInsertHyperlinks":
                        break;
                    case "AllowFormatCells":
                        break;
                    case "AllowSizeCols":
                        break;
                    case "AllowSizeRows":
                        break;
                    case "NoSummaryRowsBelowDetail":
                        break;
                    case "TabColorIndex":
                        break;
                    case "DoNotDisplayHeadings":
                        break;
                    case "ShowPageLayoutZoom":
                        break;
                    case "NoSummaryColumnsRightDetail":
                        break;
                    case "BlackAndWhite":
                        break;
                    case "DoNotDisplayZeros":
                        break;
                    case "DisplayPageBreak":
                        break;
                    case "RowColHeadings":
                        break;
                    case "DoNotDisplayOutline":
                        break;
                    case "NoOrientation":
                        break;
                    case "AllowUsePivotTables":
                        break;
                    case "ZeroHeight":
                        break;
                    case "ViewableRange":
                        break;
                    case "Selection":
                        break;
                    case "ProtectContents":
                        break;
                    default:
                        k2 = false
                    }
                    break;
                case "PivotTable":
                case "PivotCache":
                    switch (kR[3]) {
                    case "ImmediateItemsOnDrop":
                        break;
                    case "ShowPageMultipleItemLabel":
                        break;
                    case "CompactRowIndent":
                        break;
                    case "Location":
                        break;
                    case "PivotField":
                        break;
                    case "Orientation":
                        break;
                    case "LayoutForm":
                        break;
                    case "LayoutSubtotalLocation":
                        break;
                    case "LayoutCompactRow":
                        break;
                    case "Position":
                        break;
                    case "PivotItem":
                        break;
                    case "DataType":
                        break;
                    case "DataField":
                        break;
                    case "SourceName":
                        break;
                    case "ParentField":
                        break;
                    case "PTLineItems":
                        break;
                    case "PTLineItem":
                        break;
                    case "CountOfSameItems":
                        break;
                    case "Item":
                        break;
                    case "ItemType":
                        break;
                    case "PTSource":
                        break;
                    case "CacheIndex":
                        break;
                    case "ConsolidationReference":
                        break;
                    case "FileName":
                        break;
                    case "Reference":
                        break;
                    case "NoColumnGrand":
                        break;
                    case "NoRowGrand":
                        break;
                    case "BlankLineAfterItems":
                        break;
                    case "Hidden":
                        break;
                    case "Subtotal":
                        break;
                    case "BaseField":
                        break;
                    case "MapChildItems":
                        break;
                    case "Function":
                        break;
                    case "RefreshOnFileOpen":
                        break;
                    case "PrintSetTitles":
                        break;
                    case "MergeLabels":
                        break;
                    case "DefaultVersion":
                        break;
                    case "RefreshName":
                        break;
                    case "RefreshDate":
                        break;
                    case "RefreshDateCopy":
                        break;
                    case "VersionLastRefresh":
                        break;
                    case "VersionLastUpdate":
                        break;
                    case "VersionUpdateableMin":
                        break;
                    case "VersionRefreshableMin":
                        break;
                    case "Calculation":
                        break;
                    default:
                        k2 = false
                    }
                    break;
                case "PageBreaks":
                    switch (kR[3]) {
                    case "ColBreaks":
                        break;
                    case "ColBreak":
                        break;
                    case "RowBreaks":
                        break;
                    case "RowBreak":
                        break;
                    case "ColStart":
                        break;
                    case "ColEnd":
                        break;
                    case "RowEnd":
                        break;
                    default:
                        k2 = false
                    }
                    break;
                case "AutoFilter":
                    switch (kR[3]) {
                    case "AutoFilterColumn":
                        break;
                    case "AutoFilterCondition":
                        break;
                    case "AutoFilterAnd":
                        break;
                    case "AutoFilterOr":
                        break;
                    default:
                        k2 = false
                    }
                    break;
                case "QueryTable":
                    switch (kR[3]) {
                    case "Id":
                        break;
                    case "AutoFormatFont":
                        break;
                    case "AutoFormatPattern":
                        break;
                    case "QuerySource":
                        break;
                    case "QueryType":
                        break;
                    case "EnableRedirections":
                        break;
                    case "RefreshedInXl9":
                        break;
                    case "URLString":
                        break;
                    case "HTMLTables":
                        break;
                    case "Connection":
                        break;
                    case "CommandText":
                        break;
                    case "RefreshInfo":
                        break;
                    case "NoTitles":
                        break;
                    case "NextId":
                        break;
                    case "ColumnInfo":
                        break;
                    case "OverwriteCells":
                        break;
                    case "DoNotPromptForFile":
                        break;
                    case "TextWizardSettings":
                        break;
                    case "Source":
                        break;
                    case "Number":
                        break;
                    case "Decimal":
                        break;
                    case "ThousandSeparator":
                        break;
                    case "TrailingMinusNumbers":
                        break;
                    case "FormatSettings":
                        break;
                    case "FieldType":
                        break;
                    case "Delimiters":
                        break;
                    case "Tab":
                        break;
                    case "Comma":
                        break;
                    case "AutoFormatName":
                        break;
                    case "VersionLastEdit":
                        break;
                    case "VersionLastRefresh":
                        break;
                    default:
                        k2 = false
                    }
                    break;
                case "Sorting":
                case "ConditionalFormatting":
                case "DataValidation":
                    switch (kR[3]) {
                    case "Range":
                        break;
                    case "Type":
                        break;
                    case "Min":
                        break;
                    case "Max":
                        break;
                    case "Sort":
                        break;
                    case "Descending":
                        break;
                    case "Order":
                        break;
                    case "CaseSensitive":
                        break;
                    case "Value":
                        break;
                    case "ErrorStyle":
                        break;
                    case "ErrorMessage":
                        break;
                    case "ErrorTitle":
                        break;
                    case "CellRangeList":
                        break;
                    case "InputMessage":
                        break;
                    case "InputTitle":
                        break;
                    case "ComboHide":
                        break;
                    case "InputHide":
                        break;
                    case "Condition":
                        break;
                    case "Qualifier":
                        break;
                    case "UseBlank":
                        break;
                    case "Value1":
                        break;
                    case "Value2":
                        break;
                    case "Format":
                        break;
                    default:
                        k2 = false
                    }
                    break;
                case "MapInfo":
                case "Schema":
                case "data":
                    switch (kR[3]) {
                    case "Map":
                        break;
                    case "Entry":
                        break;
                    case "Range":
                        break;
                    case "XPath":
                        break;
                    case "Field":
                        break;
                    case "XSDType":
                        break;
                    case "FilterOn":
                        break;
                    case "Aggregate":
                        break;
                    case "ElementType":
                        break;
                    case "AttributeType":
                        break;
                    case "schema":
                    case "element":
                    case "complexType":
                    case "datatype":
                    case "all":
                    case "attribute":
                    case "extends":
                        break;
                    case "row":
                        break;
                    default:
                        k2 = false
                    }
                    break;
                case "SmartTags":
                    break;
                default:
                    k2 = false;
                    break
                }
                if (k2) {
                    break
                }
                if (!lo[lo.length - 1][1]) {
                    throw "Unrecognized tag: " + kR[3] + "|" + lo.join("|")
                }
                if (lo[lo.length - 1][0] === "CustomDocumentProperties") {
                    if (kR[0].substr(-2) === "/>") {
                        break
                    } else {
                        if (kR[1] === "/") {
                            en(k1, kR, k4, la.slice(lj, kR.index))
                        } else {
                            k4 = kR;
                            lj = kR.index + kR[0].length
                        }
                    }
                    break
                }
                if (k5.WTF) {
                    throw "Unrecognized tag: " + kR[3] + "|" + lo.join("|")
                }
            }
        }
        var ld = {};
        if (!k5.bookSheets && !k5.bookProps) {
            ld.Sheets = k7
        }
        ld.SheetNames = kT;
        ld.SSF = cN.get_table();
        ld.Props = kY;
        ld.Custprops = k1;
        return ld
    }

    function ay(kQ, fs) {
        jQ(fs = fs || {});
        switch (fs.type || "base64") {
        case "base64":
            return hu(bm.decode(kQ), fs);
        case "binary":
        case "buffer":
        case "file":
            return hu(kQ, fs);
        case "array":
            return hu(kQ.map(ak).join(""), fs)
        }
    }

    function cr(kQ, fs) {}
    var jq;
    if (typeof exports !== "undefined") {
        if (typeof module !== "undefined" && module.exports) {
            jq = require("fs")
        }
    }

    function h4(fs, kQ) {
        switch ((kQ || {}).type || "base64") {
        case "buffer":
            return fs[0];
        case "base64":
            return bm.decode(fs.substr(0, 12)).charCodeAt(0);
        case "binary":
            return fs.charCodeAt(0);
        case "array":
            return fs[0];
        default:
            throw new Error("Unrecognized type " + kQ.type)
        }
    }

    function aI(fs, kQ) {
        if (!kQ) {
            kQ = {}
        }
        if (!kQ.type) {
            kQ.type = (I && Buffer.isBuffer(fs)) ? "buffer" : "base64"
        }
        switch (h4(fs, kQ)) {
        case 208:
            return dX(f3.read(fs, kQ), kQ);
        case 9:
            return dX(jX(kQ.type === "base64" ? bm.decode(fs) : fs), kQ);
        case 60:
            return ay(fs, kQ);
        default:
            throw "Unsupported file"
        }
    }
    var kd = function (fs, kR) {
        var kQ = jq.readFileSync(fs);
        if (!kR) {
            kR = {}
        }
        switch (h4(kQ, {
            type: "buffer"
        })) {
        case 208:
            return dX(f3.read(kQ, {
                type: "buffer"
            }), kR);
        case 9:
            return dX(kQ, kR);
        case 60:
            return ay(kQ, (kR.type = "buffer", kR));
        default:
            throw "Unsupported file"
        }
    };

    function iE(kQ, fs) {
        var kR = fs || {};
        switch (kR.bookType) {
        case "xml":
            return cr(kQ, kR);
        default:
            throw "unsupported output format " + kR.bookType
        }
    }

    function jR(kR, fs, kQ) {
        var kS = kQ | {};
        kS.type = "file";
        kS.file = fs;
        switch (kS.file.substr(-4).toLowerCase()) {
        case ".xls":
            kS.bookType = "xls";
            break;
        case ".xml":
            kS.bookType = "xml";
            break
        }
        return iE(kR, kS)
    }

    function aV(fs, kQ) {
        if (kQ.s) {
            if (fs.cRel) {
                fs.c += kQ.s.c
            }
            if (fs.rRel) {
                fs.r += kQ.s.r
            }
        } else {
            fs.c += kQ.c;
            fs.r += kQ.r
        }
        fs.cRel = fs.rRel = 0;
        while (fs.c >= 256) {
            fs.c -= 256
        }
        while (fs.r >= 65536) {
            fs.r -= 65536
        }
        return fs
    }

    function eY(fs, kQ) {
        fs.s = aV(fs.s, kQ.s);
        fs.e = aV(fs.e, kQ.s);
        return fs
    }

    function dz(fs) {
        return parseInt(cL(fs), 10) - 1
    }

    function jH(fs) {
        return "" + (fs + 1)
    }

    function T(fs) {
        return fs.replace(/([A-Z]|^)(\d+)$/, "$1$$$2")
    }

    function cL(fs) {
        return fs.replace(/\$(\d+)$/, "$1")
    }

    function h1(fs) {
        var kS = g4(fs),
            kR = 0,
            kQ = 0;
        for (; kQ !== kS.length; ++kQ) {
            kR = 26 * kR + kS.charCodeAt(kQ) - 64
        }
        return kR - 1
    }

    function cA(fs) {
        var kQ = "";
        for (++fs; fs; fs = Math.floor((fs - 1) / 26)) {
            kQ = String.fromCharCode(((fs - 1) % 26) + 65) + kQ
        }
        return kQ
    }

    function eG(fs) {
        return fs.replace(/^([A-Z])/, "$$$1")
    }

    function g4(fs) {
        return fs.replace(/^\$([A-Z])/, "$1")
    }

    function kn(fs) {
        return fs.replace(/(\$?[A-Z]*)(\$?\d*)/, "$1,$2").split(",")
    }

    function bh(fs) {
        var kQ = kn(fs);
        return {
            c: h1(kQ[0]),
            r: dz(kQ[1])
        }
    }

    function gW(fs) {
        return cA(fs.c) + jH(fs.r)
    }

    function dF(fs) {
        return eG(T(fs))
    }

    function kb(fs) {
        return g4(cL(fs))
    }

    function cW(kQ) {
        var fs = kQ.split(":").map(bh);
        return {
            s: fs[0],
            e: fs[fs.length - 1]
        }
    }

    function cR(fs, kQ) {
        if (kQ === undefined || typeof kQ === "number") {
            return cR(fs.s, fs.e)
        }
        if (typeof fs !== "string") {
            fs = gW(fs)
        }
        if (typeof kQ !== "string") {
            kQ = gW(kQ)
        }
        return fs == kQ ? fs : fs + ":" + kQ
    }

    function dt(kR) {
        var kT = {
            s: {
                c: 0,
                r: 0
            },
            e: {
                c: 0,
                r: 0
            }
        };
        var kQ = 0,
            kS = 0,
            kU = 0;
        var fs = kR.length;
        for (kQ = 0; kS < fs; ++kS) {
            if ((kU = kR.charCodeAt(kS) - 64) < 1 || kU > 26) {
                break
            }
            kQ = 26 * kQ + kU
        }
        kT.s.c = --kQ;
        for (kQ = 0; kS < fs; ++kS) {
            if ((kU = kR.charCodeAt(kS) - 48) < 0 || kU > 9) {
                break
            }
            kQ = 10 * kQ + kU
        }
        kT.s.r = --kQ;
        if (kS === fs || kR.charCodeAt(++kS) === 58) {
            kT.e.c = kT.s.c;
            kT.e.r = kT.s.r;
            return kT
        }
        for (kQ = 0; kS != fs; ++kS) {
            if ((kU = kR.charCodeAt(kS) - 64) < 1 || kU > 26) {
                break
            }
            kQ = 26 * kQ + kU
        }
        kT.e.c = --kQ;
        for (kQ = 0; kS != fs; ++kS) {
            if ((kU = kR.charCodeAt(kS) - 48) < 0 || kU > 9) {
                break
            }
            kQ = 10 * kQ + kU
        }
        kT.e.r = --kQ;
        return kT
    }

    function c9(fs, kQ) {
        if (fs.z !== undefined) {
            try {
                return (fs.w = cN.format(fs.z, kQ))
            } catch (kR) {}
        }
        if (!fs.XF) {
            return kQ
        }
        try {
            return (fs.w = cN.format(fs.XF.ifmt || 0, kQ))
        } catch (kR) {
            return "" + kQ
        }
    }

    function it(fs, kQ) {
        if (fs == null || fs.t == null) {
            return ""
        }
        if (fs.w !== undefined) {
            return fs.w
        }
        if (kQ === undefined) {
            return c9(fs, fs.v)
        }
        return c9(fs, kQ)
    }

    function i6(kQ, kZ) {
        var k7, kU, k1, k6 = 0,
            kS = 1,
            k3, fs = [],
            kT, kR, k2, kX;
        var k4 = kZ != null ? kZ : {};
        var k0 = k4.raw;
        if (kQ == null || kQ["!ref"] == null) {
            return []
        }
        k1 = k4.range !== undefined ? k4.range : kQ["!ref"];
        if (k4.header === 1) {
            k6 = 1
        } else {
            if (k4.header === "A") {
                k6 = 2
            } else {
                if (Array.isArray(k4.header)) {
                    k6 = 3
                }
            }
        }
        switch (typeof k1) {
        case "string":
            k3 = dt(k1);
            break;
        case "number":
            k3 = dt(kQ["!ref"]);
            k3.s.r = k1;
            break;
        default:
            k3 = k1
        }
        if (k6 > 0) {
            kS = 0
        }
        var kV = jH(k3.s.r);
        var kW = new Array(k3.e.c - k3.s.c + 1);
        var k5 = new Array(k3.e.r - k3.s.r - kS + 1);
        var kY = 0;
        for (k2 = k3.s.c; k2 <= k3.e.c; ++k2) {
            kW[k2] = cA(k2);
            k7 = kQ[kW[k2] + kV];
            switch (k6) {
            case 1:
                fs[k2] = k2;
                break;
            case 2:
                fs[k2] = kW[k2];
                break;
            case 3:
                fs[k2] = k4.header[k2 - k3.s.c];
                break;
            default:
                if (k7 === undefined) {
                    continue
                }
                fs[k2] = it(k7)
            }
        }
        for (kR = k3.s.r + kS; kR <= k3.e.r; ++kR) {
            kV = jH(kR);
            kT = true;
            if (k6 === 1) {
                kU = []
            } else {
                kU = {};
                if (Object.defineProperty) {
                    Object.defineProperty(kU, "__rowNum__", {
                        value: kR,
                        enumerable: false
                    })
                } else {
                    kU.__rowNum__ = kR
                }
            }
            for (k2 = k3.s.c; k2 <= k3.e.c; ++k2) {
                k7 = kQ[kW[k2] + kV];
                if (k7 === undefined || k7.t === undefined) {
                    continue
                }
                kX = k7.v;
                switch (k7.t) {
                case "e":
                    continue;
                case "s":
                    break;
                case "b":
                case "n":
                    break;
                default:
                    throw "unrecognized type " + k7.t
                }
                if (kX !== undefined) {
                    kU[fs[k2]] = k0 ? kX : it(k7, kX);
                    kT = false
                }
            }
            if (kT === false || k6 === 1) {
                k5[kY++] = kU
            }
        }
        k5.length = kY;
        return k5
    }

    function jU(fs, kQ) {
        return i6(fs, kQ != null ? kQ : {})
    }

    function s(kT, k0) {
        var k7 = "",
            k1 = "",
            kR = /"/g;
        var k5 = k0 == null ? {} : k0;
        if (kT == null || kT["!ref"] == null) {
            return ""
        }
        var k3 = dt(kT["!ref"]);
        var kQ = k5.FS !== undefined ? k5.FS : ",",
            kV = kQ.charCodeAt(0);
        var kS = k5.RS !== undefined ? k5.RS : "\n",
            kX = kS.charCodeAt(0);
        var kW = "",
            kY = "",
            kZ = [];
        var k6 = 0,
            k4 = 0,
            k8;
        var kU = 0,
            k2 = 0;
        for (k2 = k3.s.c; k2 <= k3.e.c; ++k2) {
            kZ[k2] = cA(k2)
        }
        for (kU = k3.s.r; kU <= k3.e.r; ++kU) {
            kW = "";
            kY = jH(kU);
            for (k2 = k3.s.c; k2 <= k3.e.c; ++k2) {
                k8 = kT[kZ[k2] + kY];
                k1 = k8 !== undefined ? "" + it(k8) : "";
                for (k6 = 0, k4 = 0; k6 !== k1.length; ++k6) {
                    if ((k4 = k1.charCodeAt(k6)) === kV || k4 === kX || k4 === 34) {
                        k1 = '"' + k1.replace(kR, '""') + '"';
                        break
                    }
                }
                kW += (k2 === k3.s.c ? "" : kQ) + k1
            }
            k7 += kW + kS
        }
        return k7
    }
    var gz = s;

    function fE(kW) {
        var kR, kX = "",
            kZ, kS = "";
        if (kW == null || kW["!ref"] == null) {
            return ""
        }
        var fs = dt(kW["!ref"]),
            kT = "",
            kY = [],
            kQ;
        kR = new Array((fs.e.r - fs.s.r + 1) * (fs.e.c - fs.s.c + 1));
        var kV = 0;
        for (kQ = fs.s.c; kQ <= fs.e.c; ++kQ) {
            kY[kQ] = cA(kQ)
        }
        for (var kU = fs.s.r; kU <= fs.e.r; ++kU) {
            kT = jH(kU);
            for (kQ = fs.s.c; kQ <= fs.e.c; ++kQ) {
                kX = kY[kQ] + kT;
                kZ = kW[kX];
                kS = "";
                if (kZ === undefined) {
                    continue
                }
                if (kZ.f != null) {
                    kS = kZ.f
                } else {
                    if (kZ.w !== undefined) {
                        kS = "'" + kZ.w
                    } else {
                        if (kZ.v === undefined) {
                            continue
                        } else {
                            kS = "" + kZ.v
                        }
                    }
                }
                kR[kV++] = kX + "=" + kS
            }
        }
        kR.length = kV;
        return kR
    }
    var fv = {
        encode_col: cA,
        encode_row: jH,
        encode_cell: gW,
        encode_range: cR,
        decode_col: h1,
        decode_row: dz,
        split_cell: kn,
        decode_cell: bh,
        decode_range: cW,
        format_cell: it,
        get_formulae: fE,
        make_csv: s,
        make_json: i6,
        make_formulae: fE,
        sheet_to_csv: s,
        sheet_to_json: i6,
        sheet_to_formulae: fE,
        sheet_to_row_object_array: jU
    };
    cT.parse_xlscfb = dX;
    cT.read = aI;
    cT.readFile = kd;
    cT.utils = fv;
    cT.CFB = f3;
    cT.SSF = cN
})(typeof exports !== "undefined" ? exports : XLS);
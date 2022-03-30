//好像沒有在使用 
var IfxHost = (function(){
	
var _ifx=null;	// reference to Ifx {}
function IfxHost(aIfx) {
	_ifx = aIfx;
};
function get(name) {
	return _ifx.getValue(name);
}
function putValue(name, value) {
	_ifx.setValue(name,value);
}

var _RIMTASKID = "CR";


IfxHost.prototype.setTaskId = function(tid) {
	_RIMTASKID = tid;
};

var _noCombine = false;
IfxHost.prototype.setNoCombine = function() {
	_noCombine = true;
};

IfxHost.prototype.parseTim = function(fields, t, arr) {
	var commData = new CommData(t),
		value,
		f,
		len,
		arr = arr || [],
		s;
	
	for(var i=0; i < fields.length; i++)
	{
		f = fields[i];
		if(typeof f === 'string'){
			//commData.skip(IfxUtl.strlen(f));
			len = IfxUtl.strlen(f);
			value = commData.get(len);
			s = 'tita filler:[' + value + ']' + ", we defined:[" + f + "] " + len;
			console.log(s);
			
		}else{
			value = commData.get(f.getTomLen());
			s=f.name + ":[" + value + "] " + f.getTomLen();
			console.log(s);
			f.setValueFromTom(value);
		}
		arr.push(s);
	}
	return arr;
};

IfxHost.prototype.parseHeader = function(t) {
	var commData = new CommData(t);
	var header = new Header();
	header.deserialize(commData);
	
	console.log("header:");
	console.log(header.toDebug());
	
//	var basic = new Timbasic();
//	basic.deserialize(commData);
//	console.log("basic:");
//	console.log(basic.toDebug());
	
};

var sim=false;
var simTota = null;
IfxHost.prototype.setSim = function(tota) {
	sim = true;
	simTota = tota;
};


var ar = {};
var jnlHeader ={};

IfxHost.prototype.setTranArchive = function(aar, aHdr) {
	ar = aar;
	jnlHeader = aHdr;
};
function resetTranArchive() {
	ar = {};
	jnlHeader ={};
	
};
var mutext = false;
IfxHost.prototype.send = function(txcd, bRim, text, fnReceive, obj, fnError) {
	
	if(sim) {
		processSim(fnReceive, obj);
		sim  = false;
		return;
	}
	
	
	if(mutext) {
		console.log("bug bug, mutext is on");
		alert("bug");
		return;
	}
	mutex = true;
	var key = get("BRN$") + get("ADD$");
	var tita = buildTita(txcd, bRim, text);
	console.log("tita:"+tita);
	
	console.log("sending");
//	if(!bRim) {
//		ar = _ifx.serialize();
//		jnlHeader = _ifx.jnlHeader();
//	}
	_ifx.block('<h2>傳送電文(' + txcd + ')...<h2>');
	$.ajax({
		type: 'POST',
		url:"send.jsp",
	  cache: false,
		data: {
			"txcd":txcd, 
			"key":key, 
			"tita": tita, 
			"rim":bRim?1:0, 
			"text":text,
			'archive':JSON.stringify(ar),
			'jnlHeader':JSON.stringify(jnlHeader)
		},
		dataType: "json",
		timeout:60*1000,
		cache:false,
		success: function(data) {
			mutext = false;
			console.dir(data);
			if(data.status) {
				console.log(data.tota.join("\n"));
				console.log("oldseq = "+get("SEQNO$"));
				putValue("SEQNO$", data.seqno);
				console.log("data.seqno:"+data.seqno);
				console.log("new seq = "+get("SEQNO$"));
				
				var totaList = refactorTota(data.tota, true);
				if(typeof fnReceive == "function") {
					
					fnReceive(totaList, _noCombine ?  refactorTota(data.tota, false)  : null);
					
				} else {
					obj[fnReceive](totaList);
				}
				
			}else {
				alert("通訊錯誤:"+data.message);
				var tmpObj = makeErrorTota("通訊錯誤:"+data.message);
				alert(tmpObj.getErrmsg());
				if(fnError!=null) {
					fnError(tmpObj);
				}
			}
		},
		error : function(data){
			mutext = false;
			console.log("ajax error");
			console.log(data);
			
			var tmpObj = makeErrorTota("通訊錯誤!" + data.status + "-" + data.statusText);
			alert(tmpObj.getErrmsg());
			if(fnError!=null) {
				fnError(tmpObj);
			}
		},
		complete:function() {
			console.log("complete-host");
			resetTranArchive();
			//_ifx.unblock();  //1015柯
		}
	 });
};

function makeErrorTota(msg){
	var obj = { 
			isError:function() { return true; },
			getErrmsg:function() {
				return msg;
			}
	};
	return obj;
}

function processSim(fnReceive, obj) {
	var totaList = refactorTota(getSimData(), true);
	if(typeof fnReceive == "function") {
		fnReceive(totaList);
	} else {
		obj[fnReceive](totaList);
	}
}
function getSimData() {
//	var s1 = "IU505002612720T5458100000000000093992000000000100010300221489026100000000000000000000000000000000000000000000000000000000000000GM0030                 5050026100093992DU0001000GM0035458001   60�פJ�q��                                                                                                                    �x�_������                                                                                TEL : 86591166                                                                                                                                                                                                                                            RECEIVED FROM:                       DATE: 20110103     OUR REF: 1ABAB7600070                 1                                                                                         2                                                                                                                                                                                                                                                                         20  Transaction Reference Number                                                              233                                                                                   23B Bank Operation code                                                                       CRED                                                                                  32A Value Date, Currency Code, Amount                                                     ----------------------------------------20110103TWD1,                                     50K Ordering Customer                                                                         N/A                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       57D Account With Institution                                                                  1                                                                                         TAIPEI                                                                                    TAIWAN                                                                                                                                                                          59  Beneficiary Customer                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      71A Details of Charges                                                                        BEN                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               THIS COMPUTER GENERATED DOCUMENT DOES NOT REQUIRE SIGNATURE.";                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
//	var s2 = "IU505002612721T0045100000000000093992000000000100010300221509026100000000000000000000000000000000000000000000000000000000000000I10000                 5050026100093992DU0002001I10000045000   00070";
//	return [ s1, s2];
	return simTota;
}
function refactorTota(arrTota, bCheck) {
	console.log(arrTota.length);
	console.dir(arrTota);
	var totaList= new TotaList();
	for(var i=0; i < arrTota.length; i++) {
		totaList.add(new Tota(arrTota[i]), bCheck); // 	//�N�ۦPformid��tota�X�֬��@��
	}
	var arr = totaList.asArray();
	console.log("refactorTota:" + arr.length);
	return arr;
};





// modify from webtsa.hostran.js sendMain()
function buildTita(txcd, bRim, text) {
	if(bRim) {
		switch(txcd) {
		case "000010":  // ��
			//text = buildEC(txcd, rim);
			text = buildRimBasic(txcd) + text;
			console.log("ec text:"+ text);
			break;
		case "000020":  // ���
			// add
			text = buildRimBasic(txcd) + text;
			console.log("approval text:"+ text);
			// end add
			break;
		default:
			text = buildRimBasic(txcd) + text;
		}
	}
	else {
		//text =  buildBasicText(tranObj.getTimDef());
	}


	text =  buidlTitaHeader(txcd, bRim, 150 +  IfxUtl.strlen(text)) + text;
	console.log("total text:"+text);
	return text;
}

function buidlTitaHeader(txcd,bRim, msglen)
{
	
	var header = new Header();
	if(bRim) { // rim has fixed taskid
		if(txcd.substring(0,2)=='HR'){
			header.Td.setValue("HR");
		}else {
			header.Td.setValue(_RIMTASKID);
		}
	}else {
		header.Td.setValue(get("TSKID$"));
	}
	
	header.BrNo.setValue(get("BRN$"));
	header.TdNo.setValue(get("ADD$"));
	header.SeqNo.setValue("000");
	header.fEtr.setValue("1");
	header.MsgType.setValue(" ");
	console.log("msglen:"+msglen);	
	header.MsgLen.setValue(IfxUtl.numericFormatter(msglen,4));
	header.OnOff.setValue(get("ONOFF$"));
	header.TrainMode.setValue(get("TRNMOD$"));
	header.sbtMode.setValue(get("SBTMOD$"));
	header.Dept.setValue(get("DEPT$"));
	header.fBtr.setValue("0");
	header.Mcrr.setValue(get("SUPNO$"));
	

	if(!get("ONSEQ$")) {
		header.TxSeqNew.setValue("00000000");
		header.TxSeqOld.setValue("00000000");
	}else {
		header.TxSeqNew.setValue(get("ONSEQ$"));
		header.TxSeqOld.setValue(get("ONSEQ$"));
	}
	
	
	
	header.TxDate.setValue(get("DATE$"));
	
	putValue("TIME$",IfxUtl.getTimeString()); // current time
	header.TxTime.setValue(get("TIME$"));
	
	header.EcTdNo.setValue(get("RADD$"));
	header.SpWsNo.setValue(get("REMOTESUPER$"));
	header.SpResqNo.setValue(get("RQSP$"));
	header.Filler1.setValue("000");
	header.NewTdNo.setValue("0000");
	header.OldTdNo.setValue(get("OADD$"));
	header.OldBrNo.setValue(get("OBRN$"));
	header.SpResqNo1.setValue(get("RQSP1$"));
	header.SpResqNo2.setValue(get("RQSP2$"));
	header.SpResqNo3.setValue(get("RQSP3$"));
	header.SpResqNo4.setValue(get("RQSP4$"));
	header.SpResqNo5.setValue(get("RQSP5$"));
	header.SpResqNo6.setValue(get("RQSP6$"));
	header.SpResqNo7.setValue(get("RQSP7$"));
	header.SpResqNo8.setValue(get("RQSP8$"));
	header.SpResqNo9.setValue(get("RQSP9$"));
	header.SecSpWsNo.setValue("000");
	header.MsgId.setValue("00000");
	header.NeTxFg.setValue("0");
	header.Filler2.setValue("                 ");
	
	console.log("tita header:"+ header.toDebug());

	return header.serialize();
	
	
//	function getTimeString()
//	{
//		var d = new Date();
//		return IfxUtl.numericFormatter(d. getHours(),2) + 
//					IfxUtl.numericFormatter(d.getMinutes(),2) + 
//					IfxUtl.numericFormatter(d.getSeconds(),2) + 
//					IfxUtl.numericFormatter(d. getMilliseconds(),2);
//		
//	};
}

function buildRimBasic(txcode)
{
	var basic = new Timbasic();
	basic.brNo.setValue(get("RBRN$"));
	basic.tdNo.setValue(get("RADD$")); 
	basic.txSeq.setValue("00000000");
	basic.tskId.setValue(_RIMTASKID);
	basic.trmType.setValue("00");
	
	basic.apType.setValue(txcode.substring(0, 1));
	basic.txNo.setValue(txcode.substring(1));
	basic.dscpt.setValue("   ");
	basic.dscpt1.setValue(" ");
	basic.dscpt2.setValue(" ");
	
	basic.actNo.setValue(get("RBRN$") +  "                ");
	basic.hCode.setValue("0");
	basic.crDb.setValue("0");
	basic.nbCd.setValue("0");		
	basic.tlrNo.setValue(get("TLRNO$"));
	
	basic.spCd.setValue(get("MCRR$"));
	basic.trainMode.setValue("0");
	basic.sbtMode.setValue("0");
	basic.curCd.setValue("00");
	basic.txAmt.setValue("00000000000000");
	basic.telEmpNo.setValue(get("EMPNO$"));
	basic.spEmpNo.setValue("000000");
	basic.totaFg.setValue("0");
	basic.seqFg.setValue("0");
	basic.autoFg.setValue("0");
	
	basic.filler2.setValue("                        ");
	console.log("basic:"+basic.toDebug());
	return basic.serialize();
}


//-----------------------------------------------
// Data structure
//----------------------------------------------------

function HostField(name, len, type, oVar)
{
	this.name = name;
	this.len = len;
	this.value = "";
	
	if(type != null) this.type = type.toUpperCase();
	else this.type = "X";
		
	if(oVar != null) {
		this.linkedVar = oVar;
	}
	else this.linkedVar = null;


	this.getLinkedVar = function() {
		return this.linkedVar;	
	};
	this.setValue = function(value) {
		this.value = value;
	};
	this.serialize = function()
	{
		var v='';
		if(this.type == "X")  {
			v =  IfxUtl.stringFormatter(this.value, this.len);
		}
		else if(this.type == "9") {
			v=  IfxUtl.numericFormatter(this.value, this.len);
		}
		console.log(this.name + "==>" + v);
		return v;
	};
	
	this.deserialize = function(commDataObj)
	{
		var s = commDataObj.get(this.len, this.type);
		this.value = s;
	};
	this.valueOf = function()
	{
		return this.value;
	};
	this.toString = function() {
		return this.value;
		
	};	
	this.toDebug = function() {
		var s="";
		s += "name:" + this.name + ", ";
		s += "type:" + this.type+ ", ";
		s += "length:" + this.len + ", ";
		s += "value:[" + this.value + "]";
		s += this.value.length;
		return s;
	};	
}


// basic class of Header, Basic,...
function TitaTotaType()
{
	
	this.serialize = function()
	{
		var s="";
		for(var i=0; i < this.array.length; i++)
		{
			s += (this.array[i]).serialize();
		}
		return s;
	};
	this.deserialize = function(commDataObj)
	{
		for(var i=0; i < this.array.length; i++)
		{
			(this.array[i]).deserialize(commDataObj);
		};
	};
	this.toDebug = function()
	{
		
		var s="";
		for(var i=0; i < this.array.length; i++)
		{
			s += (this.array[i]).toDebug() + "  ^\n";
		}
		
		return s;
	};
	this.toString = function()
	{
		return this.toDebug();
	};
	this.debug = function (s)
	{
		
		var debugWin = window.open("", "debugTitaTota", "height=300,width=450,resizable=yes,scrollbars=yes"); 
		debugWin.document.writeln("<pre>" + s  + "</pre>");
		debugWin.scrollBy(0,1000);
	};
	this.toSysVar = function()
	{
		console.log("calling toSysVar");
		for(var i=0; i < this.array.length; i++) {
			var f= this.array[i];
			var svar = f.getLinkedVar();
			if(svar != null) {
				putValue(svar, f.valueOf());
				console.log(svar + "=" + get(svar));
			}
		}
		console.log("leave toSysVar");
	};
}

function Header() {
	TitaTotaType.call(this);
	this.length =  150;
	this.Td = new HostField("Td", 2);  //0
	this.BrNo = new HostField("BrNo", 4, null,  "BRN$"); //2
	this.TdNo = new HostField("TdNo", 4, null, "ADD$"); //6
	this.SeqNo = new HostField("SeqNo", 3,"9"); //10
	this.fEtr = new HostField("fEtr", 1); //13
	
	this.MsgType = new HostField("MsgType", 1); //14
	this.MsgLen = new HostField("MsgLen", 4, "9"); //15
	this.OnOff = new HostField("OnOff", 1); //19
	this.TrainMode = new HostField("TrainMode", 1); //20
	this.sbtMode = new HostField("sbtMode", 1); //21
	this.Dept = new HostField("Dept", 1); //22
	this.fBtr = new HostField("fBtr", 1); //23
	this.Mcrr = new HostField("Mcrr", 5); //24
	this.TxSeqNew = new HostField("TxSeqNew", 8, null, "NSEQ$"); //29
	this.TxSeqOld = new HostField("TxSeqOld", 8); //37
	this.TxDate = new HostField("TxDate", 8, "9"); //45
	this.TxTime = new HostField("TxTime", 8, "9", "TIME$"); //53
	this.EcTdNo = new HostField("EcTdNo", 4); //61
	this.SpWsNo = new HostField("SpWsNo", 4); //65
	this.SpResqNo = new HostField("SpResqNo", 4); //69
	this.Filler1 = new HostField("Filler1", 3); //73
	this.NewTdNo = new HostField("NewTdNo", 4); //76
	this.OldTdNo = new HostField("OldTdNo", 4); //80
	this.OldBrNo = new HostField("OldBrNo", 4); //84
	this.SpResqNo1 = new HostField("SpResqNo1", 4); //88
	this.SpResqNo2 = new HostField("SpResqNo2", 4); //92
	this.SpResqNo3 = new HostField("SpResqNo3", 4); //96
	this.SpResqNo4 = new HostField("SpResqNo4", 4); //100
	this.SpResqNo5 = new HostField("SpResqNo5", 4); //104
	this.SpResqNo6 = new HostField("SpResqNo6", 4); //108
	this.SpResqNo7 = new HostField("SpResqNo7", 4); //112
	this.SpResqNo8 = new HostField("SpResqNo8", 4); //116
	this.SpResqNo9 = new HostField("SpResqNo9", 4); //120
	this.SecSpWsNo = new HostField("SecSpWsNo", 3); //124
	this.MsgId = new HostField("MsgId", 5); //127
	this.NeTxFg = new HostField("NeTxFg", 1); //132
	this.Filler2 = new HostField("Filler2", 17); //133
	
	
	this.array  = new Array(
	 this.Td,
	 this.BrNo,
	 this.TdNo,
	 this.SeqNo,
	 this.fEtr,
	 this.MsgType,
	 this.MsgLen,
	 this.OnOff,
	 this.TrainMode,
	 this.sbtMode,
	 this.Dept,
	 this.fBtr,
	 this.Mcrr,
	 this.TxSeqNew,
	 this.TxSeqOld,
	 this.TxDate,
	 this.TxTime,
	 this.EcTdNo,
	 this.SpWsNo,
	 this.SpResqNo,
	 this.Filler1,
	 this.NewTdNo,
	 this.OldTdNo,
	 this.OldBrNo,
	 this.SpResqNo1,
	 this.SpResqNo2,
	 this.SpResqNo3,
	 this.SpResqNo4,
	 this.SpResqNo5,
	 this.SpResqNo6,
	 this.SpResqNo7,
	 this.SpResqNo8,
	 this.SpResqNo9,
	 this.SecSpWsNo,
	 this.MsgId,
	 this.NeTxFg,
	 this.Filler2
	);


	
}


function Timbasic() {
	TitaTotaType.call(this);
	this.length = 120;
	
	this.brNo=new HostField("brNo",4,null,"RBRN$");//0
	this.tdNo=new HostField("tdNo",4, null, "RADD$");//4
	this.txSeq=new HostField("txSeq",8);//8
	this.tskId=new HostField("tskId",2);//16
	this.trmType=new HostField("trmType",2);//18
	this.apType=new HostField("apType",1);//20
	this.txNo=new HostField("txNo",4);//21
	this.dscpt=new HostField("dscpt",3);//25
	this.dscpt1=new HostField("dscpt1",1);//28
	this.dscpt2=new HostField("dscpt2",1);//29
	this.actNo=new HostField("actNo",20);//30
	this.hCode=new HostField("hCode",1);//50
	this.crDb=new HostField("crDb",1);//51
	this.nbCd=new HostField("nbCd",1);//52
	this.tlrNo=new HostField("tlrNo",5);//53
	this.spCd=new HostField("spCd",5);//58
	this.trainMode=new HostField("trainMode",1);//63
	this.sbtMode=new HostField("sbtMode",1);//64
	this.curCd=new HostField("curCd",2);//65
	this.txAmt=new HostField("txAmt",14);//67
	this.telEmpNo=new HostField("telEmpNo",6);//81
	this.spEmpNo=new HostField("spEmpNo",6);//87
	this.totaFg=new HostField("totaFg",1);//93
	this.seqFg=new HostField("seqFg",1);//94
	this.autoFg=new HostField("autoFg",1);//95
	this.filler2=new HostField("filler2",24);
	
	
	this.array = new Array(	    
		this.brNo,
		this.tdNo,
		this.txSeq,
		this.tskId,
		this.trmType,
		this.apType,
		this.txNo,
		this.dscpt,
		this.dscpt1,
		this.dscpt2,
		this.actNo,
		this.hCode,
		this.crDb,
		this.nbCd,
		this.tlrNo,
		this.spCd,
		this.trainMode,
		this.sbtMode,
		this.curCd,
		this.txAmt,
		this.telEmpNo,
		this.spEmpNo,
		this.totaFg,
		this.seqFg,
		this.autoFg,
		this.filler2
	);
}


function Tombasic() {
	TitaTotaType.call(this);
	//this.length = 40;
	
	this.basicText=new HostField("basicText",40);
	this.brNo=new HostField("brNo",4); //0
	this.tdNo=new HostField("tdNo",4, null, "RADD$");  //4
	this.txSeq=new HostField("txSeq",8); //8
	this.tskId=new HostField("tskId",2);  //16
	this.trmType=new HostField("trmType",4);  //18
	this.filler1=new HostField("filler1",2);  //22
	this.msgEnd=new HostField("msgEnd",1);  //24
	this.msgType=new HostField("msgType",1);  //25
	this.msgCode=new HostField("msgCode",4);  //26
	this.msgLen=new HostField("msgLen",4);   //30
	this.cashFg=new HostField("cashFg",1);   //34
	this.filler2=new HostField("filler2",5);  //35
	
	
	this.array = new Array(
		this.brNo,
		this.tdNo,
		this.txSeq,
		this.tskId,
		this.trmType,
		this.filler1,
		this.msgEnd,
		this.msgType,
		this.msgCode,
		this.msgLen,
		this.cashFg,
		this.filler2
	);

}

// �ۦPForm��Tota�X�֬��@��
function TotaList()
{
	this.list = [];
	this.add = function(oTota, bCheck)
	{
		if (bCheck && this.list.length > 0) {
			// combin forms by formname
			var prevTota = this.list[this.list.length-1];
			if(prevTota.getTxForm() == oTota.getTxForm()) // same form
			{
				prevTota.appendText(oTota.text);
				return;
			}
		}
		this.list.push(oTota);
	};
	this.asArray = function() {
		return this.list;
	};
}

function Tota(sTota)
{
	this.header = new Header();
	this.basic = new Tombasic();
	this.text = ""; 
	this.textArray = new Array();
	this.sTota = sTota;
	this.commDataObj = new CommData(this.sTota);
	
	this.deserialize = function()
	{
		(this.header).deserialize(this.commDataObj);
		(this.basic).deserialize(this.commDataObj);
		this.text = this.commDataObj.getToEnd();
		this.textArray.push(this.text);
	};
	if(this.commDataObj != null) {
		this.deserialize(this.commDataObj);
	}
	
	
	this.appendText = function(text)
	{
		//this.text += text;
		this.textArray.push(text);
	};
	
	this.isError = function()
	{
		
		var msgType = this.header.MsgType;
		var errorType = "EX";
		return errorType.indexOf(msgType) >= 0 ;
	};
	
	this.getErrmsg = function()
	{
		var msgType = (this.basic.msgType);
		var msgCode = (this.basic.msgCode);
		var text = this.textArray[0];
		return msgType + msgCode + text;
	};
	
	this.isWarnning = function()
	{
		var msgType = this.header.MsgType;
	
		var errorType = "W";
		return errorType.indexOf(msgType) >= 0 ;
	};
	
	this.getTxForm = function()
	{
		var msgType = (this.basic.msgType);
		var msgCode = (this.basic.msgCode);
		return msgType + msgCode;
	};
	this.isMulti = function()
	{
		return this.header.MsgType == "M";
	};
	this.toSysVar = function()
	{
		// check each hostField's linkedVar, if linkedVar not null, copy value to linkedVar
		this.header.toSysVar();
		this.basic.toSysVar();
		
		// set unmapped var, 
//		TXFORM$.setValue(this.getTxForm());
		putValue("TXFORM$",this.getTxForm());
	};
	
	this.parseTotaForm = function(formFields)
	{
		var commData = new CommData(this.text);
		for(var i=0; i < formFields.length; i++)
		{
			var f = formFields[i];
			var value = commData.get(f.getTomLen());
			f.setValueFromTom(value);
			console.log("setting " + f.name + " to [" + value + "] " + f.getTomLen());
		}
	};
	
	this.getOccurs = function(oSettings, fields)
	{
		if(this.occurs != null) return this.occurs;
		
		this.occurs = [];

		var currTota = 0;
		var tmpCommData = new CommData(this.textArray[currTota]);
		//var hdtext = tmpCommData.get(obj.inqhd);
		
	
		var hdrfields = {};
		var occursfields = {};

		var noHdrFields = [];		
		// first occur of occurs

		for(var i=0, len=0; i < fields.length; i++)
		{
			var f = fields[i];
			console.log("comm get field:"+ f.name);
			var value = tmpCommData.get(f.getTomLen());
			if(len < oSettings.inqhd) {
				hdrfields[f.name] = value;
			}else {
				noHdrFields.push(f);
				occursfields[f.name] = value;

			}
			len += f.getTomLen();
		}
		
		this.occurs.push(this.makeOccurRec(hdrfields, occursfields));
		
		do {
		// the rest of occurs
			while(tmpCommData.hasMore()) {
				var occursfields = {};
				for(var i=0; i <noHdrFields.length; i++)
				{
					var f = noHdrFields[i];
					occursfields[f.name] = tmpCommData.get(f.getTomLen());
				}
				this.occurs.push(this.makeOccurRec(hdrfields, occursfields));
			}
			// next tota
			currTota++;
			if (currTota >= (this.textArray).length) {
				break;
			}
			
			tmpCommData = new CommData(this.textArray[currTota]);
			// skip header of text
			tmpCommData.get(oSettings.inqhd); // get a garbage
		}	while(true);
		
		return this.occurs;
	};
	
	this.debug1 = function(o)
	{
		var s="";
		for(k in o) {
			s +=  k  + "=" + o[k] + "\n";
		}
		alert(s);
	};
	this.makeOccurRec = function(hdrfields, occursfields)
	{
		return new OccursRec(hdrfields, occursfields);
	};
		
	this.toDebug = function()
	{

		var s="";
		s += "[CommData]\n";
		s += this.sTota + "\n";
		s += "[header]\n";
		s += (this.header).toDebug();
	
		s += "\n[basic]\n";
		s += this.basic.toDebug();
		
		s += "\n[text]\n";
		s += "[" + this.text +"]";
		return s;
	};
	this.toString = function()
	{
		return this.toDebug();
	};
	this.debug = function()
	{
		this.header.debug(this.toDebug());
	};

}


function OccursRec(hdrfields, occursfields)
{
	this.hdrFields = hdrfields;
	this.occursFields = occursfields;
	
	this.getHdr = function() {
		return this.hdrFields;
	};
	this.getOccurs = function()
	{
		return this.occursFields;
	};
	this.get = function(name)
	{
		var value="";
		value = this.hdrFields[name];
		if(value != null) return value;
		
		value = this.occursFields[name];
		if(value == null) value = "";
		return value;
	};
	
	this.toDebug = function()
	{
		var s="";
		s += "header fields:\n";
		var bGot = false;
		for(k in this.hdrFields)
		{
			s += (k + "=[" + this.hdrFields[k] + "]; ");
			bGot = true;
		}
		if(!bGot) s += "\t\t*  *  *  *  N O N E  *  *  *  *";
		s += "\n---------------------------------------------------------------------------";
		s += "\noccurs fields:\n";
		var bGot = false;
		for(k in this.occursFields)
		{
			s += (k + "=[" + this.occursFields[k] + "]; ");
			bGot = true;
		}
		if(!bGot) s += "\t\t*  *  *  *  N O N E  *  *  *  *";
		
		return s;
	};
	
}

function CommData(s)
{
	this.buffer = s;
	this.bufLen = s.length;
	
	this.soffset = 0; // javascript string offset, 
	
	this.get = function(len , type)
	{
		var lastOffset = this.soffset;
	
		var t="";
		for(var i=0; i < len;)
		{
			var one = this._getOne();
			if(one != null) {
				t += one;
			}else {
				break;
			}
			this.soffset++;
			
			if(this.isBIG5(one)){ // Taiwan Big5 Character.	
				i+=2; // two byte for Taiwan
//				this.offset+=2; 
				
			}	
			else {
				i++;
//				this.offset++;
			}
		}
//		alert("get lastOffset:" + lastOffset + ", len:" + len + ", current offset:" + this.soffset);
		return t;
			
	};
	this.skip = function(positiveLen , type)
	{
		// �u�O���F����...���κ�return value
		this.get(positiveLen);
	};

	this.getFrom = function(start, length)
	{
		// �S���˰h�a...
		if(start - this.soffset < 0) {
			alert("�ڤ��|�˰h!!!");
			return "";
		}
		
		this.skip(start - this.soffset);
		var t = this.get(length);
		t = this.fillSpace(t, length);
		return t;
	};
	this.getBytesLength = function()
	{
		var bytesLen=0;
		for(var i=0; i < this.buffer.length; i++)
		{
			if(this.isBIG5(this.buffer.charAt(i))) bytesLen += 2;
			else bytesLen++;
		}
		this.bytesLength = bytesLen;
	};
	
	this.fillSpace = function(t, length)
	{
		var byteLen=0;
		for(var i=0; i < t.length; i++)
		{
			if(this.isBIG5(t.charAt(i))) byteLen += 2;
			else byteLen++;
		}
		while(byteLen < length) {
			t += " ";
			byteLen++;
		}
		return t;
	};
	
	this.isBIG5 = function(ch)
	{
		 return IfxUtl.strlen(ch) != 1;
		 
//		if(ch.charCodeAt(0) >= 256)
//		{
//			return true;
//		}
//		return false;
	};
	this.getToEnd = function(bSkip)
	{
		
		var t= this.buffer.substr(this.soffset);
		if(bSkip != null && bSkip == true)
		{
			this.soffset = this.bufLen;
		}
		return t;
	};
	
	this._getOne = function()
	{
		if (this.soffset >= this.bufLen) return null;
		return this.buffer.substr(this.soffset, 1);
		
	};
	this.hasMore = function()
	{
		if (this.soffset >= this.bufLen) return false;
		return true;
	};
	this.sbuggedkipTo = function(toPos)
	{
		if (toPos == this.offset) return;
		if(toPos > this.offset) { // forward
			var distance = toPos;
			for(var i = this.offset; distance > 0 && i < this.bufLen; i++) 
			{
				if(this.isBIG5(this.buffer[i])) distance-= 2;
				else distance--;
			}
			this.offset = i;
		}else { // backward
			var distance = toPos;
			for(var i=this.offset; distance > 0 && i >= 0 ; i--)
			{
				if(this.isBIG5(this.buffer[i])) distance-=2;
				else distance--;
			}
			this.offset = (i < 0) ? 0 : i;
		}
	};
	
	this.buggedskip = function(advPos)
	{
		// TODO:�S�Ҽ{�줤��
		var iPos = parseInt(advPos, 10);
		
		if(iPos < 0) {
			this.offset = Math.max(this.offset, this.offset + this.offset);
		}else {
			this.offset = Math.min(this.bufLen, this.offset + this.offset);
		}
	};
}


return IfxHost;
}());



<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
<!-- environment variables -->
<c:set var="context" value="${pageContext.request.contextPath}" />
<c:set var="script" value="${pageContext.request.contextPath}/script" />
<c:set var="css" value="${pageContext.request.contextPath}/css" />
<c:set var="v" value="?_v=2013082419" />

<script src="<%=request.getContextPath()%>/script/yepnope.1.5.4-min.js"></script>
<script>
var _contextPath = '<%=request.getContextPath()%>';
var _version = '?_v=2013082419';
var resources = [
  		"<%=request.getContextPath() %>/script/stacktrace-min-0.4.js", 			
  		"_IFX_<%=request.getContextPath() %>/script/fixIE.js",
		"<%=request.getContextPath() %>/script/jquery.js",						
		"<%=request.getContextPath() %>/jqgrid/js/grid.locale-tw.js",
		"<%=request.getContextPath() %>/jqgrid/js/jquery.jqGrid.min.js",
		"<%=request.getContextPath() %>/script/jqueryui/js/jquery-ui-1.10.3.custom.min.js",
		"<%=request.getContextPath() %>/script/jqueryui/css/redmond/jquery-ui-1.10.3.custom.css",
		"<%=request.getContextPath() %>/jqgrid/css/ui.jqgrid.css",
		"_IFX_<%=request.getContextPath() %>/css/site.css" ];
function updateVersion() {
	upd(resources);
	function upd(r) {
		var ifxJS = '_IFX_';
		var len = ifxJS.length;
		for ( var i = 0; i < r.length; i++) {
			if (r[i].slice(0, len) == ifxJS) {
				r[i] = r[i].slice(len) + _version;
			}
		}
	}
}
updateVersion();
loadJS();
function loadJS() {
	yepnope({
		load : resources,
		complete : function() {
			fixIE();
			$ = jQuery;
			myExtend($);
			jQuery(document).ready(function() {
				neverComplete();				
				bigTable();
				
			});
		}
	});
}
function myExtend($){
	$.fn.saveText = function(){
		return this.each(function() {
			var $this = $(this);
			$this.attr('__mytext__', $this.text());
		});
	};
	$.fn.restoreText= function(){
		return this.each(function() {
			var $this = $(this);
			$this.text($this.attr('__mytext__'));
		});
	};
	$.ltrim = function( str ) {
        return str.replace( /^\s+/, "" );
    };
    $.rtrim = function( str ) {
        return str.replace( /\s+$/, "" );
    };
    var shade = "#556b2f";
    $.fn.greenify = function() {
        this.css( "color", shade );
        return this;
    };
    
    $.fn.showLinkLocation = function() {
        return this.filter( "a" ).each(function() {
            $( this ).append( " (" + $( this ).attr( "href" ) + ")" );
        });
    };



    $.fn.hilight = function( options ) {
        // Iterate and reformat each matched element.
        return this.each(function() {
            var $this = $( this );
            var markup = $this.html();
            // Call our format function.
            markup = $.fn.hilight.format( markup );
            $this.html( markup );
        });
        
        function debug( $obj ) {
            if ( window.console && window.console.log ) {
                window.console.log( "hilight selection count: " + $obj.size() );
            }
        };

     


    };

    // Define our format function.
    $.fn.hilight.format = function( txt ) {
        return "<strong>" + txt + "</strong>";
    };




}
function neverComplete(){
	$("input#m").autocomplete({
		minLength : 3,
		source : function (request, callback){
			var url = _contextPath + "/mvc/hnd/rim/SWIFT?m=" + $("#m").val();
	
		    $.ajax ({
				cache : true,
		      	url :url,
		      	success : function(data) {
					callback($.map(data.result, function(x) {
						return {
							value : x.key,
							label :  x.key + ' - ' +x.data,
							data:x.data
						};
					}));
		      	}
		    });
		},
		select : function(e, ui) {
			if (ui.item != null) {
				 updateSelected(ui.item);
			}
		}
	});
}

function updateSelected(item){
	$('#txtCode').text(item.value);
	$('#txtName').text(item.data.slice(0,35));
	$('#txtAddr1').text(item.data.slice(37,72));
	$('#txtAddr2').text(item.data.slice(72,107));
	$('#txtAddr3').text(item.data.slice(107));
}

function bigTable(){
	$btn = $('#btnLoad');
	$btn.click(function(){
		$btn.attr("disabled", true);
		//$btn.attr("oldText", $btn.text());
		$btn.saveText();
		$btn.text('please wait...');
		var url = _contextPath + "/mvc/hnd/rim/SWIFT?m=*";
		var $aj = $.ajax ({
				cache : true,
		      	url :url,
		      	dataType :'json'
	   	});
		$aj.done(function(data){
			populate(data.result);
		});
		$aj.fail(function(err){
			alert(err);
		});
		$aj.always(function(){
			$btn.attr("disabled", false);
			//$btn.text($btn.attr("oldText"));
			$btn.restoreText();
		});
	});
}
function populate(mydata){
	var $grid = $("#grid");
	$grid.jqGrid({
		datatype: "local",
	   	colNames:['','SWIFT CODE','BANK NAME','ADDRESS'],
	   	colModel:[
	   		{name:'id',index:'id', width:15, sorttype:"int",hidden:true},
	   		{name:'key',index:'key', width:60},
	   		{name:'name',index:'name', width:150},
	   		{name:'address',index:'address', width:250},
	   	],
		height: '100%',
		width:1000,
		 resize:true,
	   	gridview:true,
    //    rownumbers:true,
        viewrecords: true,
        shrinkToFit: true,
        autoencode: true,
        filter:false,
		rowNum:10,
	   	rowList:[10, 20,50, 100,200],
		pager: $('#pager'),
	   	multiselect: false,
	   	caption: "Big Swift Data",
	    onSelectRow:function(rowid) {
	    	 var row = $(this).getLocalRow(rowid);
	    	 console.log(row);
	    	 updateResult(row);
	    },
	});
	
	for(var i=0;i< mydata.length;i++) {
		$grid.jqGrid('addRowData',i+1,convertData(mydata[i], i));
	}
	$grid.jqGrid('setGridParam',{page:1}).trigger("reloadGrid");
	$grid.jqGrid('filterToolbar', {searchOnEnter: true, stringResult: true, defaultSearch: 'cn'});
}
function convertData(r, i){
	var d = {};
	d['id'] = r.id;
	try {
	d['key'] = r.key;
	}catch(ee){
		alert(ee);
	}
	d['name'] = r.data.slice(0,35);
	d['address']= r.data.slice(37,72) + "\n" 
				+ r.data.slice(72,107)  + "\n"
				+ r.data.slice(107);
	return d;
}

function updateResult(row){
	$('#txtCode').text(row['key']);
	$('#txtName').text(row['name']);
	var addr = row['address'].split('\n');
	$('#txtAddr1').text(addr[0] || '');
	$('#txtAddr2').text(addr[1] || '');
	$('#txtAddr3').text(addr[2] || '');
}
</script>	
<title>Insert title here</title>
</head>
<body>
<input type="text" id="m" size="40" maxlength="30" placeholder="type something"/>
<div style='color:white'>
	<table border='1'>
	<tr><td>SWIFT CODE</td><td><span id="txtCode"></span></td></tr>
	<tr><td>BANK NAME</td><td><span id="txtName"></span></td></tr>
	<tr><td>Address1</td><td><span id="txtAddr1"></span></td></tr>
	<tr><td>Address2</td><td><span id="txtAddr2"></span></td></tr>
	<tr><td>Address3</td><td><span id="txtAddr3"></span></td></tr>
	</table>
	<button id='btnApply'>Apply</button>
</div>
<hr/>
<button id='btnLoad'>Load</button>
<table id="grid"></table>
<div id="pager"></div>
</body>
</html>
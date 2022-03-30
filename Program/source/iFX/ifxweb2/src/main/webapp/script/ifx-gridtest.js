function testGrid() {
	var mydata = [
                  {id:"1", invdate:"2007-10-01",name:"test",  note:"note",  amount:"200.00",tax:"10.00",closed:true, ship_via:"TN",total:"210.00"},
                  {id:"2", invdate:"2007-10-02",name:"test2", note:"note2", amount:"300.00",tax:"20.00",closed:false,ship_via:"FE",total:"320.00"},
                  {id:"3", invdate:"2007-09-01",name:"test3", note:"note3", amount:"400.00",tax:"30.00",closed:false,ship_via:"FE",total:"430.00"},
                  {id:"4", invdate:"2007-10-04",name:"test4", note:"note4", amount:"200.00",tax:"10.00",closed:true ,ship_via:"TN",total:"210.00"},
                  {id:"5", invdate:"2007-10-31",name:"test5", note:"note5", amount:"300.00",tax:"20.00",closed:false,ship_via:"FE",total:"320.00"},
                  {id:"6", invdate:"2007-09-06",name:"test6", note:"note6", amount:"400.00",tax:"30.00",closed:false,ship_via:"FE",total:"430.00"},
                  {id:"7", invdate:"2007-10-04",name:"test7", note:"note7", amount:"200.00",tax:"10.00",closed:true ,ship_via:"TN",total:"210.00"},
                  {id:"8", invdate:"2007-10-03",name:"test8", note:"note8", amount:"300.00",tax:"20.00",closed:false,ship_via:"FE",total:"320.00"},
                  {id:"9", invdate:"2007-09-01",name:"test9", note:"note9", amount:"400.00",tax:"30.00",closed:false,ship_via:"TN",total:"430.00"},
                  {id:"10",invdate:"2007-09-08",name:"test10",note:"note10",amount:"500.00",tax:"30.00",closed:true ,ship_via:"TN",total:"530.00"},
                  {id:"11",invdate:"2007-09-08",name:"test11",note:"note11",amount:"500.00",tax:"30.00",closed:false,ship_via:"FE",total:"530.00"},
                  {id:"12",invdate:"2007-09-10",name:"test12",note:"note12",amount:"500.00",tax:"30.00",closed:false,ship_via:"FE",total:"530.00"}
              ];
	
	var grid = $('#grid');
	grid.jqGrid({
		data: mydata,
		datatype: "local",
		colNames:['Inv No','Date','Client','Amount','Tax','Total','Closed','Shipped via','Notes'],
	    colModel:[
                  {name:'id',index:'id',width:70,align:'center',sorttype: 'int',hidden:false, frozen:true},
                  {name:'invdate',index:'invdate',width:80, align:'center', sorttype:'date',
                   formatter:'date', formatoptions: {newformat:'m/d/Y'}, datefmt: 'm/d/Y', frozen : true },
                  {name:'name',index:'name', width:70},
                  {name:'amount',index:'amount',width:100, formatter:'number', align:'right'},
                  {name:'tax',index:'tax',width:70, formatter:'number', align:'right'},
                  {name:'total',index:'total',width:120, formatter:'number', align:'right'},
                  {name:'closed',index:'closed',width:110,align:'center', formatter: 'checkbox',
                   edittype:'checkbox',editoptions:{value:'Yes:No',defaultValue:'Yes'}},
                  {name:'ship_via',index:'ship_via',width:120,align:'center',formatter:'select',
                   edittype:'select',editoptions:{value:'FE:FedEx;TN:TNT;IN:Intim',defaultValue:'Intime'}},
                  {name:'note',index:'note',width:100,sortable:false}
              ],
	   	       
	    rowNum:3,
        rowList:[3,5,7],
        pager: '#pager',
        gridview:true,
        rownumbers:true,
        sortname: 'invdate',
        viewrecords: true,
        sortorder: 'desc',
        shrinkToFit: false,
		
        caption:'Just simple local grid',
        height: '100%'

	});
	
	grid.jqGrid('navGrid','#pager',{edit:false,add:false,del:false});

    grid.jqGrid('searchGrid',{
    	multipleSearch:true,
    	showOnLoad:true,
    	top:200,
    	left:200,
    	modal:false,
    	loadOnStart:true,
    	resize:true,
        beforeShowSearch: function($form) {
            // because beforeShowSearch will be called on all open Search Dialog,
            // but the old dialog can be only hidden and not destroyed
            // we test whether we already bound the 'keydown' event
            $form.css({top:200,left:200});
            
            var events = $('.vdata',$form).data('events'), event;
            for (event in events) {
                if (events.hasOwnProperty(event) && event === 'keydown') {
                    return;
                }
            }
            $('.vdata',$form).keydown( function( e ) {
                var key = e.charCode ? e.charCode : e.keyCode ? e.keyCode : 0;
                if (e.which == 13) { // save
                    //$(".ui-search", $form).click();
                    grid[0].SearchFilter.search();
                }
            });
        }
    });

    grid.jqGrid('setFrozenColumns');

    grid.jqGrid('gridResize', { minWidth: 450, minHeight: 150 });
}
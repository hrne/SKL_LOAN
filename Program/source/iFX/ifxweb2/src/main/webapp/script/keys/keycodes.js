
	
var __stdKBD__ = true;

if(__stdKBD__)
{
	
var K_F1 = 112;
var K_F2 = 113;
var K_F3 = 114;
var K_F4 = 115;
var K_F5 = 116;
var K_F6 = 117;
var K_F7 = 118;
var K_F8 = 119;
var K_F9 = 120;
var K_F10 = 121;
var K_F11= 122;
var K_F12= 123;

//Navigation: HOME, END, LEFT ARROW, RIGHT ARROW, UP ARROW, DOWN ARROW 
var K_PGUP=33;
var K_PGDN=34;
var K_END=35;
var K_HOME=36;
var K_LEFT=37;
var K_UP=38;
var K_RIGHT=39;
var K_DOWN=40;

//System: ESC, SPACEBAR, SHIFT, TAB 
var K_TAB=9;
var K_ESC=27;
var K_SHIFT=16; // shift tab
var K_BKSP=8;
var K_SPACE=32;
var K_DELETE=46;
var K_ENTER=13;




} // end of __stdKBD__

var navigationKeyMap = new Object;
var navigationKeys = new Array (K_PGUP,K_PGDN,K_END,  K_LEFT,K_UP,K_RIGHT,K_DOWN,
	K_TAB,K_SHIFT,K_ESC,K_BKSP, K_DELETE,K_ENTER, 
	K_F1 ,K_F2,K_F3,K_F4,K_F5,K_F5,K_F6,K_F7,K_F8,K_F9,K_F10,K_F11,K_F12);
	
function setupNavigationMap()
{
	for (key in navigationKeys)  {
		navigationKeyMap[navigationKeys[key]] = true;
	}
}
		


function isNavigationKey(kcode) {
	var o;
	try {
		o = navigationKeyMap[kcode];
	}
	catch(ignoreIt) {	}
	if(o==null) {
		return false;
	}
	else {
		return true;
	}
	
}

function translateKey(oEvent)
{
	var decorator="";
	var kcode = oEvent.keyCode ;
	if(oEvent.altKey || oEvent.ctrlKey || oEvent.shiftKey) {
		if (kcode >= K_F1 && kcode <= K_F12) {
			if(oEvent.altKey) decorator += "A";
			if(oEvent.ctrlKey) decorator += "C";
			if(oEvent.shiftKey) decorator += "S";
			if(decorator != "") {
				   // return "C.F1" --> Ctrl + F1
				   // AC.F2 --> Alt+Ctrl+F2,....
				var t=  decorator + ".F" + new String((oEvent.keyCode - K_F1 + 1));
				return t;
			}
		}
		// ¤£³B²z Alt,Ctrl, Shift  + key
		return 0;
	}
	return kcode;
	
}


// initialization only
var __keyInited__ = false;

if(!__keyInited__){
	setupNavigationMap();
	__keyInited__ = true;
}

exports.expand = expandLoop;

function expandLoop(arr) {
   console.log(arr);
   var buf=[],
      tagLoop = /##(\s){0,}loop\s{0,}/,
      tagEnd = /##\s{0,}end/,
      loopBuf=[],
      objParams={};
      found=false;
   arr.forEach(function(x) {
      if(x.match(tagLoop)) {
         console.log("====>got loop");
         found = true;
         parseParam(x);
      }else {
         if(found) {
            if(x.match(tagEnd)) {
               found = false;
               buf = buf.concat(doExpand());
               loopBuf=[];
            }else {
               loopBuf.push(x);
            }
         }else{
            buf.push(x);
         }
      }
      
   });
   return buf;
   
   function parseParam(x) {
      x = x.replace(tagLoop,"");
      objParam = eval('(' + x + ')');
      console.dir(objParam);
   }
   function doExpand() {
      console.log("-------------------------------------------------");
      console.log("expanding loop");
      console.log("original :\n");
      loopBuf.forEach(function(x) { console.log(x);});
      
      var times = objParam.times;
      var tt=[];
      delete objParam['times'];;
      for(var i=0; i < times; i++) {
         loopBuf.forEach(function(x){
            tt.push(replaceWith(x, objParam));
         });
         advance(objParam);
      }
      console.log("\nexpanded:\n");
      tt.forEach(function(x) { console.log(x);});
      console.log("-------------------------------------------------\n\n");
      return tt;
   }
   function replaceWith(s, o) {
      for(var k in o) {
         var re = new RegExp('\{' + k + '\}','g');
         s = s.replace(re, o[k]);
      }
      return s;
   }
   function advance(o) {
      for(var k in o) {
         o[k] = o[k]+1;
      }
   }
}

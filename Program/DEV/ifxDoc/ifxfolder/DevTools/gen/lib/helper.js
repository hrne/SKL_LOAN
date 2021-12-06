exports.init = init;

function init() {
	//String.prototype.trim = function(){return (this.replace(/^[\s\xA0]+/, "").replace(/[\s\xA0]+$/, ""))}
	String.prototype.trim = function() {
	return this.replace(/^\s+|\s+$/g,"");
}
	String.prototype.ltrim = function() {
		return this.replace(/^\s+/,"");
	}
	String.prototype.rtrim = function() {
		return this.replace(/\s+$/,"");
	}

	//String.prototype.startsWith = function(str) {return (this.match("^"+str)==str)};
	String.prototype.startsWith = function( str ) {
		return this.substring( 0, str.length ) === str;
	}
	
	//String.prototype.endsWith = function(str) {return (this.match(str+"$")==str)};
	String.prototype.endsWith = function( str ) {
		return this.substring( this.length - str.length, this.length ) === str;
	}
	String.prototype.contains = function(txt){
		return (this.indexOf(txt) >= 0);
	}
}	

function DumpObjectIndented(obj, indent) 
{ 
  var result = ""; 
  if (indent == null) indent = ""; 
 
  for (var property in obj) 
  { 
    var value = obj[property]; 
    if (typeof value == 'string') 
      value = "'" + value + "'"; 
    else if (typeof value == 'object') 
    { 
      if (value instanceof Array) 
      { 
        // Just let JS convert the Array to a string! 
        value = "[ " + value + " ]"; 
      } 
      else 
      { 
        // Recursive dump 
        // (replace "  " by "\t" or something else if you prefer) 
        var od = DumpObjectIndented(value, indent + "  "); 
        // If you like { on the same line as the key 
        //value = "{\n" + od + "\n" + indent + "}"; 
        // If you prefer { and } to be aligned 
        value = "\n" + indent + "{\n" + od + "\n" + indent + "}"; 
      } 
    } 
    result += indent + "'" + property + "' : " + value + ",\n"; 
  } 
  return result.replace(/,\n$/, ""); 
} 

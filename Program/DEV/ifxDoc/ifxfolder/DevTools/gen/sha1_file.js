var hash_file = require('hash_file'),
    fs = require('fs'),
    path = require('path');
    
var f1 = 'D:/ifxfolder/Dev/var/tran/I/i1000.var';
var f2 = 'D:/ifxfolder/Dev/var/tran/I/i1001.var';

console.time('sha1');
hash_file(f1, 'sha1', function(err, hash) {
  console.log(hash);
});

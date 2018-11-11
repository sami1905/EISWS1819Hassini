var http = require('http');
var path = require('path');
var express = require('express');
var request = require('request');
var app = express();
var bodyParser = require('body-parser');

var dHost = 'http://localhost';
var dPort = 3000;
var dUrl = dHost + ':' + dPort;


app.get('/analysis', function(req, res){
  var url = dUrl + '/analysis';

  // helper method, only for get requests
  request(url, function(err, response, body){
    body = JSON.parse(body);
    res.json(body);
  });
});


app.listen(3001, function(){
  console.log('Dienstnutzer läuft auf Port 3001.');
});

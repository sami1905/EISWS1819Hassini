var http = require('http');
var path = require('path');
var express = require('express');
var request = require('request');
var app = express();
var bodyParser = require('body-parser');

var dHost = 'http://localhost';
var dPort = 3000;
var dUrl = dHost + ':' + dPort;

//POST /dexcomValues

//GET /dexcomValues

//POST /events
app.post('/events', bodyParser.json(), function(req,res){
    var url = dUrl + '/events';
    var events = {
        "date" : req.body.date,
        "value" : req.body.value,
        "carbohydrates" : req.body.carbohydrates,
        "be" : req.body.be,
        "correction" : req.body.correction,
        "meal_id" : req.body.meal_id,
        "insulin_units" : req.body.insulin_units,
        "insulin_type" : req.body.insulin_type
    }
    var options = {
        uri : url,
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        json: events
    }
    request(options, function(err, response, body){
        res.json(body);        
    });
});

app.listen(3001, function(){
  console.log('Dienstnutzer läuft auf Port 3001.');
});

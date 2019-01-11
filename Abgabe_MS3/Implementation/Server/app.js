var fs = require('fs');
var express = require('express');
var bodyParser = require('body-parser');
var qs = require("querystring");
var http = require("https");
var dexcomHTTP = require("https");

var app = express();
app.use(bodyParser.json());

const settings = {
    port: 3000,
    dexcom_data: './dexcom_data.json',
    user_events: './user_events.json',
    user_Authorization: './user_Authorization.json'
};

app.use(function(err, req, res, next){
  console.log(err.stack);
  res.end(err.status + ' ' + err.messages);
});

app.use(function(req, res, next){
  console.log('Time ' + Date.now() + ' | Request-Pfad: ' + req.path);
  next();
});


//POST /authorization
app.post('/authorization', bodyParser.json(), function(req, res){
    
        //var start = req.body.start;
        //var end = req.body.end;
    
    var newUserAuthorization;
    
    //refresh token
    fs.readFile(settings.user_Authorization, function(err, data){
        var userAuthorization = JSON.parse(data);
        var refreshToken = userAuthorization.refresh_token;
        
        console.log(JSON.stringify(userAuthorization.refresh_token));
        
        var refreshOptions = {
            "method": "POST",
            "hostname": "api.dexcom.com",
            "port": null,
            "path": "/v2/oauth2/token",
            "headers": {
            "content-type": "application/x-www-form-urlencoded",
            "cache-control": "no-cache"
            }
        };

        var req = dexcomHTTP.request(refreshOptions, function (res) {
            var chunks = [];

            res.on("data", function (chunk) {
            chunks.push(chunk);
        });

            res.on("end", function () {
                newUserAuthorization = Buffer.concat(chunks);
                console.log(newUserAuthorization.toString());
                fs.writeFile(settings.user_Authorization, JSON.stringify(JSON.parse(newUserAuthorization.toString()), null, 2));
                
            });
        });
    
        
        
        
        req.write(qs.stringify({ 
            client_secret: 'KkHFiwdepfGE3Pox',
            client_id: 'LiIbbgsBtr7VqpYkNBveXaOs9vzdnGtw',
            refresh_token:  refreshToken,
            grant_type: 'refresh_token',
            redirect_uri: 'https://wba2.herokuapp.com' }));
        req.end();
        });
});

//POST /dexcomValues
app.post('/dexcomValues/:date', bodyParser.json(), function(req, res){
    fs.readFile(settings.user_Authorization, function(err, data){
        var userAuthorization = JSON.parse(data);
        var date = req.params.date;
        console.log(date);
        fs.readFile(settings.dexcom_data, function(err, data){
           

            var dexcomOptions = {
                "method": "GET",
                "hostname": "api.dexcom.com",
                "port": null,
                "path": "/v2/users/self/egvs?startDate=2019-01-10T00:00:00&endDate=" + date,
                "headers": {
                    "authorization": "Bearer " + userAuthorization.access_token,
                    }
            };

            var req = dexcomHTTP.request(dexcomOptions, function (res) {
                var chunks = [];

                res.on("data", function (chunk) {
                    chunks.push(chunk);
                });

                res.on("end", function () {
                    var dexcomValues = Buffer.concat(chunks);
                    console.log(dexcomValues.toString());
                    fs.writeFile(settings.dexcom_data, JSON.stringify(JSON.parse(dexcomValues.toString()), null, 2));
                });
            });

            req.end();
          
        });
    });
});


//GET /dexcomValues
app.get('/dexcomValues', function(req, res){
    fs.readFile(settings.dexcom_data,function(err,data){
        var dexcomValues = JSON.parse(data);
        res.status(200).send(dexcomValues.egvs);
    });
});

//POST /events
app.post('/events', bodyParser.json(), function(req, res){
    fs.readFile(settings.user_events, function(err,data){
        var events = JSON.parse(data);
        var numberOfEvents = events.length;
        var max_index = 0;
        
        //id of the last event is inserted into max_index
        for(var i = 0; i< numberOfEvents; i++){
            if(events[i].id > max_index){
                max_index = events[i].id;
            }
        }
        
        //creat event
        events.push({
            "id" : ++max_index,
            "date" : req.body.date,
            "value" : req.body.value,
            "carbohydrates" : req.body.carbohydrates,
            "be" : req.body.be,
            "correction" : req.body.correction,
            "meal_id" : req.body.meal_id,
            "insulin_units" : req.body.insulin_units,
            "insulin_type" : req.body.insulin_type
        });
        fs.writeFile(settings.user_events, JSON.stringify(events, null, 2));
        res.status(201).send(events[(max_index-1)]);
        
    });
});

//GET /events
app.get('/events', function(req, res){
    fs.readFile(settings.user_events,function(err,data){
        var user_events = JSON.parse(data);
        res.status(200).send(user_events);
    });
});

app.listen(settings.port, function(){
  console.log("Server läuft auf Port " + settings.port + ".");
});
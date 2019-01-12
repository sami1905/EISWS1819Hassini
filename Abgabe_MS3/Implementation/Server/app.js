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
    user_values: './user_values.json',
    user_authorization: './user_Authorization.json'
};

app.use(function(err, req, res, next){
  console.log(err.stack);
  res.end(err.status + ' ' + err.messages);
});

app.use(function(req, res, next){
  console.log('Time ' + Date.now() + ' | Request-Pfad: ' + req.path);
  next();
});

//POST /userValues

app.post('/userValues', bodyParser.json(),function(req, res){
    fs.readFile(settings.user_values, function(err, data){
        var userValues = JSON.parse(data);
        var numberOfValues = userValues.length;
        var lastDate;
        var userValuesIndex = 0;
        
        //id of the last value is inserted into userValuesIndex
        for(var i = 0; i< numberOfValues; i++){
            if(userValues[i].id > userValuesIndex){
                userValuesIndex = userValues[i].id;
            }
        }
        
        for( var i = 0; i < numberOfValues; i++){
            if(userValues[i].type == "dexcom"){
                lastDate = userValues[i].date;
            }
        }
        
        if(lastDate == null)lastDate = "2018-12-15T16:03:06";
        
        console.log(lastDate);
        
        fs.readFile(settings.dexcom_data, function(err, data){
            var dexcomData = JSON.parse(data);
            var numbersOfDexcomData = dexcomData.egvs.length;
        
            for(var i = 0; i < numbersOfDexcomData; i++){
                if(dexcomData.egvs[i].displayTime == lastDate){
                    for(var x = (i-1); x >= 0; x--){
                        userValues.push({
                            "id" : ++userValuesIndex,
                            "type" : "dexcom",
                            "date" : dexcomData.egvs[x].displayTime,
                            "value" : dexcomData.egvs[x].value
                        }); 
                    }
                }
            
               
            }
            fs.writeFile(settings.user_values, JSON.stringify(userValues, null, 2));
        });     
    });
});

//GET /userValues
app.get('/userValues', bodyParser.json(), function(req, res){
    fs.readFile(settings.user_values, function(err, data){
        var userValues = JSON.parse(data);
        res.status(200).send(userValues);
  });
});

//GET /userValues/:date

//POST /authorization
app.post('/authorization', bodyParser.json(), function(req, res){
    
        //var start = req.body.start;
        //var end = req.body.end;
    
    var newUserAuthorization;
    
    //refresh token
    fs.readFile(settings.user_authorization, function(err, data){
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
                fs.writeFile(settings.user_authorization, JSON.stringify(JSON.parse(newUserAuthorization.toString()), null, 2));
                
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

//POST /dexcomValues/:date
app.post('/dexcomValues/:date', bodyParser.json(), function(req, res){
    

    fs.readFile(settings.user_authorization, function(err, data){
        var userAuthorization = JSON.parse(data);
        var date = req.params.date;
        console.log(date);

        fs.readFile(settings.dexcom_data, function(err, data){
            var dexcomData = JSON.parse(data);


            var dexcomOptions = {
                "method": "GET",
                "hostname": "api.dexcom.com",
                "port": null,
                "path": "/v2/users/self/egvs?startDate=2018-12-16T00:00:00&endDate=" + date,
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
    fs.readFile(settings.user_values, function(err, data){
        var values = JSON.parse(data);
        var numberOfValues = values.length;
        var userValuesIndex = 0;
        
        //id of the last value is inserted into userValuesIndex
            for(var i = 0; i< numberOfValues; i++){
                if(values[i].id > userValuesIndex){
                    userValuesIndex = values[i].id;
                }
            }
        
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
            
            values.push({
                "id" : ++userValuesIndex,
                "type" : "manuell",
                "date" : req.body.date,
                "value" : req.body.value
            });
            
            fs.writeFile(settings.user_events, JSON.stringify(events, null, 2));
            fs.writeFile(settings.user_values, JSON.stringify(values, null, 2));
            res.status(201).send(events[(max_index-1)]);
        
        });
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
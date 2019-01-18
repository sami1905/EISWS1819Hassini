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

//DEXCOM-API

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
                    //console.log(dexcomValues.toString());
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


//UserValues

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
                lastDate = userValues[i].date + "T" + userValues[i].time;
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
                            "date" : dexcomData.egvs[x].displayTime.substr(0, 10),
                            "time": dexcomData.egvs[x].displayTime.substring(11,19),
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
app.get('/userValues/:date', bodyParser.json(), function(req, res){
    fs.readFile(settings.user_values, function(err, data){
    var userValues = JSON.parse(data);
    var date = req.params.date;
    var userValuesOfDate = [];
    
    for(var i = 0; i< userValues.length; i++){
        if(userValues[i].date == date){
            userValuesOfDate.push({
                "id": userValues[i].id,
                "type": userValues[i].type,
                "date": userValues[i].date,
                "time": userValues[i].time,
                "value": userValues[i].value
            })
        }
    }

    if(err){
        console.log(err);
        res.status(500).send("database error")
    }
        
    if(userValuesOfDate.length != 0) {
        res.status(200).send(userValuesOfDate);
    }
    else res.status(500).send(null);
    
   }); 
});


//Events

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
            var date = req.body.date.substring(0,10);
            var time = req.body.date.substring(11,19);
        
            //id of the last event is inserted into max_index
            for(var i = 0; i< numberOfEvents; i++){
                if(events[i].id > max_index){
                    max_index = events[i].id;
                }
            }
            
            var eventToAdd = {
                "id" : ++max_index,
                "date" : date,
                "time" : time,
                "value" : req.body.value,
                "carbohydrates" : req.body.carbohydrates,
                "be" : req.body.be,
                "correction" : req.body.correction,
                "meal_id" : req.body.meal_id,
                "insulin_units" : req.body.insulin_units,
                "insulin_type" : req.body.insulin_type
            };
            
            if(eventToAdd.value == 0){
                var key = "value";
                delete eventToAdd[key];
            }
            
            if(eventToAdd.carbohydrates == 0){
                var key = "carbohydrates";
                delete eventToAdd[key];
            }
            
            if(eventToAdd.be == 0){
                var key = "be";
                delete eventToAdd[key];
            }
            
            if(eventToAdd.correction == 0){
                var key = "correction";
                delete eventToAdd[key];
            }
            
            if(eventToAdd.meal_id == ""){
                var key = "meal_id";
                delete eventToAdd[key];
            }
            
            if(eventToAdd.insulin_units == 0){
                var key = "insulin_units";
                delete eventToAdd[key];
            }
            
            if(eventToAdd.insulin_type == ""){
                var key = "insulin_type";
                delete eventToAdd[key];
            }
            

            
            
            var insertIndexOfEvents = 0;
            
            for(insertIndexOfEvents; insertIndexOfEvents < values.length; insertIndexOfEvents++)
            if(values[insertIndexOfEvents].date == date){
                break;
            }
        
            //creat event
            events.splice(insertIndexOfEvents, 0, eventToAdd);
            
            //add value of event to user_values
            if(req.body.value != 0){
                
                var valueToAdd = {
                    "id" : ++userValuesIndex,
                    "type" : "manuell",
                    "date" : date,
                    "time" : time,
                    "value" : req.body.value
                };
            
            
                //search for the index by date of the value to add
                var insertIndexOfValues = 0;
            
                for(insertIndexOfValues; insertIndexOfValues < values.length; insertIndexOfValues++)
                    if(values[insertIndexOfValues].date == date){
                    break;
                }
            
                values.splice(insertIndexOfValues, 0, valueToAdd);
                
            }
            
            if (req.body.date != "" && 
                req.body.value != 0 ||
                req.body.carbohydrates != 0 ||
                req.body.be != 0 || 
                req.body.correction != 0 || 
                req.body.meal != "" || 
                req.body.insulin_units != 0 || 
                req.body.insulin_type != "") {
                
                fs.writeFile(settings.user_events, JSON.stringify(events, null, 2));
                fs.writeFile(settings.user_values, JSON.stringify(values, null, 2));
            
                res.status(201).send(events[(insertIndexOfEvents)]);
                
            }
            
            
            
            else return res.status(406).send("Please stick to the form");
            
        
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

//GET /events/:date
app.get('/events/:date', bodyParser.json(), function(req, res){
    fs.readFile(settings.user_events, function(err, data){
    var userEvents = JSON.parse(data);
    var date = req.params.date;
    var userEventsOfDate = [];
    
    for(var i = 0; i< userEvents.length; i++){
        if(userEvents[i].date == date){
            userEventsOfDate.push({
                "id": userEvents[i].id,
                "date": userEvents[i].date,
                "time": userEvents[i].time,
                "value": userEvents[i].value,
                "carbohydrates": userEvents[i].carbohydrates,
                "be": userEvents[i].be,
                "correction": userEvents[i].correction,
                "meal_id": userEvents[i].meal_id,
                "insulin_units": userEvents[i].insulin_units,
                "insulin_type": userEvents[i].insulin_type
            })
        }
    }
    
    res.status(200).send(userEventsOfDate);
    
   }); 
});


//Statics

//GET /valuesInPercent/:date
app.get('/valuesInPercent/:date', bodyParser.json(), function(req, res){
   fs.readFile(settings.user_values, function(err,data){
       var userValues = JSON.parse(data);
       var date = req.params.date;
       var userValuesSinceDate = [];
       var nValues = 0;
       var nVeryLow = 0;
       var nLow = 0;
       var nWithinRange = 0;
       var nHigh = 0;
       var percentVeryLow = 0;
       var percentLow = 0;
       var percentWithinRange = 0;
       var percentHigh = 0;
       
       //alle Werte ab 'date' mit berücksichtigen
       if(date != "0"){
           for(var i = 0; i < userValues.length; i++){
               if (userValues[i].date == date){
                   for(i; i<userValues.length; i++){
                       nValues++;
                       if(userValues[i].value <= 55){
                           nVeryLow++;
                       }
                       else if(userValues[i].value > 55 && userValues[i].value <= 80){
                           nLow++;
                       }
                       else if(userValues[i].value >=180){
                           nHigh++;
                       }
                       else nWithinRange++;
                   }
               }
           }
       }
       //alle Werte mit berücksichtigen
       else {
           for (var i = 0; i < userValues.length; i++){
               nValues++;
               if(userValues[i].value <= 55){
                   nVeryLow++;
               }
               else if(userValues[i].value > 55 && userValues[i].value <= 80){
                   nLow++;
               }
               else if(userValues[i].value >=180){
                   nHigh++;
               }   
                else nWithinRange++;
           }
       }
       
       percentVeryLow = (nVeryLow/nValues);
       percentLow = (nLow/nValues);
       percentWithinRange = (nWithinRange/nValues);
       percentHigh = (nHigh/nValues);
       
       userValuesSinceDate.push({
           "nValues" : nValues,
           "nVeryLow" : nVeryLow,
           "nLow" : nLow,
           "nWithinRange" : nWithinRange,
           "nHigh" : nHigh,
           "percentVeryLow" : percentVeryLow,
           "percentLow" : percentLow,
           "percentWithinRange" : percentWithinRange,
           "percentHigh" : percentHigh
       });
       if(nValues != 0){
           res.status(200).send(userValuesSinceDate);
       }
       else res.status(500).send("error in database");
   });   
});

//GET/averageValue/:date
app.get('/averageValue/:date', bodyParser.json(), function(req, res){
    fs.readFile(settings.user_values, function(err,data){
        var userValues = JSON.parse(data);
        var date = req.params.date;
        var minValue = -1;
        var maxValue = -1;
        var averageValue = 0;
        var nValues = 0;
        var HbA1c = 0;
        var statics = [];
        
        //alle Werte ab 'date' mit berücksichtigen
        if (date != "0"){
            for (var i = 0; i < userValues.length; i++){
                 if(userValues[i].date == date){
                     for( i; i < userValues.length; i++){
                         nValues++;
                         if (minValue == -1)minValue = userValues[i].value;
                         
                         if(minValue > userValues[i].value)minValue = userValues[i].value;
                         
                         if(maxValue == -1)maxValue = userValues[i].value;
                         
                         if(maxValue < userValues[i].value)maxValue = userValues[i].value;
                         
                         averageValue += userValues[i].value
                     }
                     
                 }
            }
        }
        
        //alle Werte mit berücksichtigen
        else{
            for( var i = 0; i < userValues.length; i++){
                nValues++;
                if (minValue == -1)minValue = userValues[i].value;
                if(minValue > userValues[i].value)minValue = userValues[i].value;
                if(maxValue == -1)maxValue = userValues[i].value;
                if(maxValue < userValues[i].value)maxValue = userValues[i].value;
                averageValue += userValues[i].value
                }
            }
        
        averageValue = (averageValue/nValues);
        
        HbA1c = ((0.031 * averageValue) + 2.393);
        
        statics.push({
           "nValues" : nValues,
           "minValue" : minValue,
           "maxValue" : maxValue,
           "averageValue" : averageValue,
            "HbA1c" : HbA1c
        });
        if(nValues != 0){
            res.status(200).send(statics);      
        }
        else res.status(500).send("error in database");
    });
});

//GET /valuesOutOfRange
app.get('/valuesOutOfRange', bodyParser.json(), function(req, res) {
    fs.readFile(settings.user_values, function(err,data){
        var userValues = JSON.parse(data);
        var valuesOutOfRange = {
            "low" : [],
            "high": []
        };
        
        for(var i = 0; i< userValues.length; i++){
            if(userValues[i].value <= 70){
                valuesOutOfRange.low.push({
                    "date": userValues[i].date,
                    "time": userValues[i].time,
                    "value": userValues[i].value
                });
            }
            if(userValues[i].value >= 180){
                valuesOutOfRange.high.push({
                    "date": userValues[i].date,
                    "time": userValues[i].time,
                    "value": userValues[i].value
                });
            }   
        }   
        res.status(200).send(valuesOutOfRange);   

    });
});

app.listen(settings.port, function(){
  console.log("Server läuft auf Port " + settings.port + ".");
});
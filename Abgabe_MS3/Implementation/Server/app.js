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
    user_events: './user_events.json'
};

app.use(function(err, req, res, next){
  console.log(err.stack);
  res.end(err.status + ' ' + err.messages);
});

app.use(function(req, res, next){
  console.log('Time ' + Date.now() + ' | Request-Pfad: ' + req.path);
  next();
});


//POST /dexcomValues
app.post('/dexcomValues', bodyParser.json(), function(req, res){
    fs.readFile(settings.dexcom_data, function(err, data){
        var decxomValues = JSON.parse(data);
        var start = req.body.start;
        var end = req.body.end;
        
        var dexcomOptions = {
            "method": "GET",
            "hostname": "api.dexcom.com",
            "port": null,
            "path": "/v2/users/self/egvs?startDate=" + start + "&endDate=" + end,
            "headers": {
                "authorization": "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IncyYUVpQmRlMXBfNnNjZmMzXzFpeHFvaDVqTSIsImtpZCI6IncyYUVpQmRlMXBfNnNjZmMzXzFpeHFvaDVqTSJ9.eyJpc3MiOiJodHRwczovL3VhbTEuZGV4Y29tLmNvbS9pZGVudGl0eSIsImF1ZCI6Imh0dHBzOi8vdWFtMS5kZXhjb20uY29tL2lkZW50aXR5L3Jlc291cmNlcyIsImV4cCI6MTU0NjE5OTMxMSwibmJmIjoxNTQ2MTk4NzExLCJjbGllbnRfaWQiOiJMaUliYmdzQnRyN1ZxcFlrTkJ2ZVhhT3M5dnpkbkd0dyIsInNjb3BlIjpbIm9mZmxpbmVfYWNjZXNzIiwiZWd2IiwiY2FsaWJyYXRpb24iLCJkZXZpY2UiLCJzdGF0aXN0aWNzIiwiZXZlbnQiXSwic3ViIjoiZjA3YTkyMGQtNmFjYS00ZjFkLWI4MmUtMzllYjAzOWJjOGNkIiwiYXV0aF90aW1lIjoxNTQ2MTg4NTUwLCJpZHAiOiJpZHNydiIsImp0aSI6ImQ2MzFlNTJkYzQyZDE2ZDk4YjQ1MDU0MTlmYmI2ZWFhIiwiYW1yIjpbInBhc3N3b3JkIl19.gBGbOnxz0mWWTk577qfM1T013vXEaNcuBj192pbq5XUeGeqDaYCN4U2_BFLR2ioTjwkTAl-DEPcuS5x3Gw1545OuDGN_pmpjqRfy40jBNs9W2lcT6gQSUq3WKulIPO1poNxhn9PH4n-k1W6wMGcf45GUYDFL5VchLg9BuFF0H3CKEZkPOkn4G5pbliWI6bWZWupRC8t_TypjDpv2KPv8TuIjNH1dtygv247w6Fnb6qa2QULBn2lLUWJX0Ah35JgOMKGkG9G4iLbOe7yJv8NX-MHtB510DAJhNzzXJFBuOqMcfYSX0NOF16SjOm78fVBNfKEHndLX1Y_vLZHKAfS1aA",
            }
        };
        
        var dexcomReq = dexcomHTTP.request(dexcomOptions, function (res) {
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
        dexcomReq.end();
    });
});

//GET /dexcomValues
app.get('/dexcomValues', function(req, res){
    fs.readFile(settings.dexcom_data,function(err,data){
        var dexcomValues = JSON.parse(data);
        res.status(200).send(dexcomValues);
    });
});

//POST /events
app.post('/events', bodyParser.json(), function(req, res){
    fs.readFile(settings.user_events, function(err,data){
        var events = JSON.parse(data);
        var numberOfEvents = events.event.length;
        var max_index = 0;
        
        //id of the last event is inserted into max_index
        for(var i = 0; i< numberOfEvents; i++){
            if(events.event[i].id > max_index){
                max_index = events.event[i].id;
            }
        }
        
        //creat event
        events.event.push({
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
        res.status(201).send("Ereignis erfolgreich gespeichert\n");
        
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
var fs = require('fs');
var express = require('express');
var bodyParser = require('body-parser');
var qs = require("querystring");
var http = require("https");

var app = express();
app.use(bodyParser.json());

const settings = {
    port: 3000,
    dexcom_data: './dexcom_data.json',
    user_events: './user_events.json'
};

var options = {
  "method": "GET",
  "hostname": "api.dexcom.com",
  "port": null,
  "path": "/v2/users/self/egvs?startDate=2018-12-16T15:30:00&endDate=2018-12-29T15:45:00",
  "headers": {
    "authorization": "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IncyYUVpQmRlMXBfNnNjZmMzXzFpeHFvaDVqTSIsImtpZCI6IncyYUVpQmRlMXBfNnNjZmMzXzFpeHFvaDVqTSJ9.eyJpc3MiOiJodHRwczovL3VhbTEuZGV4Y29tLmNvbS9pZGVudGl0eSIsImF1ZCI6Imh0dHBzOi8vdWFtMS5kZXhjb20uY29tL2lkZW50aXR5L3Jlc291cmNlcyIsImV4cCI6MTU0NjE4OTE4MiwibmJmIjoxNTQ2MTg4NTgyLCJjbGllbnRfaWQiOiJMaUliYmdzQnRyN1ZxcFlrTkJ2ZVhhT3M5dnpkbkd0dyIsInNjb3BlIjpbIm9mZmxpbmVfYWNjZXNzIiwiZWd2IiwiY2FsaWJyYXRpb24iLCJkZXZpY2UiLCJzdGF0aXN0aWNzIiwiZXZlbnQiXSwic3ViIjoiZjA3YTkyMGQtNmFjYS00ZjFkLWI4MmUtMzllYjAzOWJjOGNkIiwiYXV0aF90aW1lIjoxNTQ2MTg4NTUwLCJpZHAiOiJpZHNydiIsImp0aSI6IjQ1Yjc5ZGIxNTgyYjQwNmJhODNmNDgxMjI4ZjNiNGMzIiwiYW1yIjpbInBhc3N3b3JkIl19.mbpcEb0bdDPSSvjkjTJvO65PKHp3SiD6nwgmxxvqAHmoxPZisQDx2mjzEimW5Qq6ohy28hpQNbm9lHmdIfg2ZroCrYTeJp_vh2PbQZMWAgoTX-hQPJ2jFEoG7TFYdsYCK9igEQe0ZoPv010R8VYmAjjocoU0ehOH2nfmQgCalaA5TwDFPrYfA3Hw9sVXv_UGCX5oz-26GtsYg-HddekboE6QRlPJyXWM9f2oXWoLxYlBIeGNbM4tb293YHJpg5BmO8HvjaBKB3yXrk8p3YxEneo4S2jZtbwbeIyIsX8Kg2SSu-UVvTSsyN9oWT5d-qsECsJrR1pZlJxRbPPmPNq_0A",
    }
};


//POST /dexcomValues
app.post('/dexcomValues', function(req, res){
    fs.readFile(settings.dexcom_data, function(err, data){
        var req = http.request(options, function (res) {
            var chunks = [];

            res.on("data", function (chunk) {
                chunks.push(chunk);
            });

            res.on("end", function () {
                var body = Buffer.concat(chunks);
                fs.writeFile(settings.dexcom_data, JSON.stringify(JSON.parse(body.toString()), null, 2));
                console.log(body.toString());
            });
        });
        req.end();
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

app.listen(settings.port, function(){
  console.log("Dienstgeber läuft auf Port " + settings.port + ".");
});
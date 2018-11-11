var fs = require('fs');
var express = require('express');
var bodyParser = require('body-parser');
var app = express();

app.use(bodyParser.json());

const settings = {
    port:3000,
    database: './database.json'
};

//Log mit Pfad und Zeitangabe für jeden Request-Pfad

app.use(function(err, req, res, next){
  console.log(err.stack);
  res.end(err.status + ' ' + err.messages);
});

app.get('/', function(req, res){
  res.status(200).send('Hello World!');
});

app.use(function(req, res, next){
  console.log('Time: %d '+ 'Request-Pfad: ' + req.path, Date.now());
    next();
})

//GET /analysis
app.get('/analysis', function(req, res){
    fs.readFile(settings.database, function(err, data){
        
        var current_data = JSON.parse(data);
        var array0 = {};
        var array1 = {};
        var array2 = {};
        var array3 = {};
        var ausgabe = {"timeID_0": {},"timeID_1":{},"timeID_2":{},"timeID_3":{}};
        var z = 0;
        var y = 0;
        var x = 0;
        var w = 0;
        
        for(i=0; i < current_data.bloodsugar.length; i++){
            var current_bloodsugar = current_data.bloodsugar[i];
            
            if(current_bloodsugar.sugarvalue > 180 && current_bloodsugar.timeID == 0){
                
                //array0[z] = current_bloodsugar;
                ausgabe.timeID_0[z] = current_bloodsugar;
                z++;
            }
            
            else if(current_bloodsugar.sugarvalue > 180 && current_bloodsugar.timeID == 1){
                
                //array0[z] = current_bloodsugar;
                ausgabe.timeID_1[y] = current_bloodsugar;
                y++;
            }
              else if(current_bloodsugar.sugarvalue > 180 && current_bloodsugar.timeID == 2){
                
                //array0[z] = current_bloodsugar;
                ausgabe.timeID_2[x] = current_bloodsugar;
                x++;
            }
              else if(current_bloodsugar.sugarvalue > 180 && current_bloodsugar.timeID == 3){
                
                //array0[z] = current_bloodsugar;
                ausgabe.timeID_3[w] = current_bloodsugar;
                w++;
            }
            
        }
        
        
        
        
        
        console.log(JSON.stringify(ausgabe));
        res.send(ausgabe);
        
		});
});

app.listen(settings.port, function(){
	console.log("Dienstgeber ist nun auf Port "+settings.port+" verfügbar.");
	
});
         
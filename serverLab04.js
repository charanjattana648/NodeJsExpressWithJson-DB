var data = require("./au.json");
var filter = require("./filterData.js");

var mysql = require('mysql');
var fs = require("fs");
var express = require("express");
var bodyParser = require("body-parser");

var app = express();
var port = 8000;
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());
app.use(express.static(__dirname));

var server = app.listen(port, () => {
    console.log("server is listening on port ", server.address().port);
})

app.post('/', function(req, res) {
    console.log("entered for login")
    status(req, res);
});

//check user entered for login or data
//if for login then he needs to enter login and password
// if for data login details will be automatically taken by server.
function status(req, res) {
    var connection = "";
    if (req.body.type == "login") {
        connection = mysql.createPool({
            connectionLimit: 5,
            host: 'localhost',
            user: req.body.name,
            password: req.body.pwd,
            database: 'mytestdb'
        })

    }
    else {
        connection = mysql.createPool({
            connectionLimit: 5,
            host: 'localhost',
            user: "myuser",
            password: "pass123",
            database: 'mytestdb'
        })
    }
    login(req, res, connection);
}


function login(req, res, connection) {

    if (req.body.type == "login") {
        let qString = "SELECT 1 from Country";
        connection.query(qString, function(err, rows, fields) {
            if (!err) {
                console.log("enteredd in server")
                var max = filter.maxPopulation(req, res, data);
                var countriesName = filter.countriesName(req, res, data);
                console.log(countriesName);
                res.send("1" + "\n" + max + "\n" + countriesName);

            }
            else {
                console.log("failed in server")
                res.send("0");
            }
        })
    }
    else if (req.body.type == "data") {
        var fil = filter.filteredData(req, res, data);
        res.send(fil);


    }
    else if (req.body.type == "countryData") {


        var countryData;
        var countryDataQuery = "SELECT * from Country where Name='" + req.body.country + "'";
        connection.query(countryDataQuery, function(err, rows, fields) {
            console.log("entered table !!!!!!")
            if (!err) {
                countryData = JSON.stringify(rows);
                console.log("countryData : " + countryData);
                console.log("rows " + rows)
                res.send(countryData);

            }
            else {
                console.log("Error occured!!" + " " + err)
            }
        });

    }
    else if (req.body.type == "countryLanguages") {

        var countryLanguageData = "";
        var countryLanguageDataQuery = "select Language from CountryLanguage where CountryCode='" + req.body.code + "'";
        connection.query(countryLanguageDataQuery, function(err, rows, fields) {
            if (!err) {
                countryLanguageData = JSON.stringify(rows);
                console.log("countryLanguageData : " + countryLanguageData)
                res.send(countryLanguageData);
            }
            else {
                console.log("Error occured while getting data from CountryLanguage table!!")
            }
        });
    }

}

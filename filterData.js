function searchData(req, res, data) {
    let countryData = data.filter((conData) => {
        return Number(req.body.min) <= Number(conData.population) && Number(req.body.max) >= Number(conData.population) && req.body.country === conData.country
    })
    countryData.sort(function(a, b) {
        return a.admin.localeCompare(b.admin);
    })

    res.send(JSON.stringify(countryData));
    console.log(JSON.stringify(countryData))
}

function maxPop(req, res, data) {
    var max = 0;
    for (var i = 0; i < data.length; i++) {
        max = Math.max(max, data[i].population);
    }
    return max;
}

function countries(req, res, data) {

    var countriesName = [];
    countriesName.push(data[0].country);
    console.log(countriesName);
    var isFound = false;
    for (var i = 1; i < data.length; i++) {
        for (var j = 0; j < countriesName.length; j++) {
            if (data[i].country === countriesName[j]) {
                isFound = true;
            }
        }
        if (!isFound) {
            countriesName.push(data[i].country);
        }
        isFound = false;
    }
    return countriesName;
}

exports.maxPopulation = maxPop;
exports.countriesName = countries;
exports.filteredData = searchData;

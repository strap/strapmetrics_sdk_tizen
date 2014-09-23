/**
 * This file builds the whole project using uglify
 * */

//Let there be colors
require("colors");

//Load deps
var fs = require("fs"),
    uglify = require("uglify-js"),
    async = require("async"),
    rr = require("recursive-readdir"),
    path = require("path");

var tasks = [];

var options = {
    source: path.join(__dirname, "/src/"),
    destination: {
        bundle: path.join(__dirname, "lib/strap.tizen.sdk.js"),
        min: path.join(__dirname, "lib/strap.tizen.sdk.min.js")
    },
    copyTo: {
        bundle: path.join(__dirname, "../strap.tizen.sdk.js"),
        min: path.join(__dirname, "../strap.tizen.sdk.min.js")
    }
};

console.log(options);

//Build the master file
tasks.push(function (callback) {
    rr(options.source, function (err, files) {
        if (err) callback(err);
        else {
            console.log(files);
            try {
                fs.writeFileSync(options.destination.bundle, uglify.minify(files, {
                    beautify: true,
                    indent_level: 4
                }).code);
            } catch (c) {
                return callback(c);
            }
            return callback(null);
        }
    });
});

//Create the min file
tasks.push(function (callback) {
    try {
        fs.writeFileSync(options.destination.min, uglify.minify(options.destination.bundle).code);
    } catch (c) {
        return callback(c);
    }
    return callback(null);
});

//Copy lib files
tasks.push(function (callback) {
    fs.writeFileSync(options.copyTo.bundle, fs.readFileSync(options.destination.bundle));
    fs.writeFileSync(options.copyTo.min, fs.readFileSync(options.destination.min));
    callback();
});

async.series(tasks, function (err) {
    if (err)  throw new Error(err);
    else console.log("Done");
});



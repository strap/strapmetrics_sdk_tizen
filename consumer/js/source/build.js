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
    beautify = require("js-beautify").js_beautify,
    //shell = require("shelljs/global"),
    path = require("path");
var exec = require('child_process').exec;


var tasks = [];

var VERSION = "0.2.6-rc1";

var options = {
    source: path.join(__dirname, "/src/"),
    destination: {
        bundle: path.join(__dirname, "lib/strap.tizen.sdk.js"),
        min: path.join(__dirname, "lib/strap.tizen.sdk.min.js")
    },
    copyTo: {
        bundle: path.join(__dirname, "../strap.tizen.sdk." + VERSION + ".js"),
        min: path.join(__dirname, "../strap.tizen.sdk." + VERSION + ".min.js")
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
                fs.writeFileSync(options.destination.bundle, "");
                files.forEach(function (file) {
                    fs.appendFileSync(options.destination.bundle, fs.readFileSync(file) + "\n");
                });
                fs.writeFileSync(options.destination.bundle, beautify(fs.readFileSync(options.destination.bundle).toString(), {
                    "indent_size": 4,
                    "indent_char": " ",
                    "indent_level": 0,
                    "indent_with_tabs": false,
                    "preserve_newlines": false,
                    "max_preserve_newlines": 1,
                    "jslint_happy": false,
                    "brace_style": "collapse",
                    "keep_array_indentation": false,
                    "keep_function_indentation": false,
                    "space_before_conditional": true,
                    "break_chained_methods": false,
                    "eval_code": false,
                    "unescape_strings": false,
                    "wrap_line_length": 0
                }));
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

//Delete old lib files
tasks.push(function (callback) {
    exec("rm -rfv ../strap.tizen.sdk.*.js");
    callback();
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



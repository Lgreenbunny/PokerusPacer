// ==UserScript==
// @name         FieldHelpter
// @namespace    http://tampermonkey.net/
// @version      0.1
// @description  Trade aids for impatient people~
// @author       Lgreenbunny
// @match        https://pokefarm.com/fields/*
// @match        https://pokefarm.com/trade/setup/pkmn/*
// @grant        GM_listValues
// @grant        GM_getValue
// @grant        GM_getResourceText
// @run-at       document-start
// @grant        none
// ==/UserScript==

(function() {
    'use strict';

    var ready = false;//program ready to run
    var button;// button to start searching document for urls
    var trade = false;//change the trading page

    //setup program
    document.addEventListener("DOMContentLoaded", (event) => {
        setTimeout(preparing, 1000);//makes elements for the program & other thing, once
        /*document.addEventListener("", (event) => {});*/
        //make

    });

    function preparing(){//change different things depending on the page you're on, field v. trading
        //FIELD SECTION
        //make elements

        //place in right spot

        //add button event function



        //TRADE SECTION (pkmn-setup trade)
        //move element-function
        if(trade){
            rearrange();
        }

        ready=true;
    }

    function butHit(){//the function when the button is hit
    //start scanning document for field

        //add valid summary links to array

        var arrr = [];
        /*resultThing(arrr)*/
    }


    function rearrange(){/*
    add option to stop/start rearrange
    on trading page, setup thru pkmn,
    move the setup trade button & gift option on top of the page
    also move pkmn section to the bottom of the page
    order: username, setup button+gift checkbox, item selection, pokemon*/
    }

    function resultThing(arrr){
        //update text of result field with the urls of the field...
    }

    //grab the html element with the info

    /*grab everything that matches the pattern
    .+?/summary/(?!hello)(.{5}) matches the pkmn's summary btw*/

    /*Present the links in a certain manner*/
})();

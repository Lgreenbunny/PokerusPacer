// ==UserScript==
// @name         FieldHelpter
// @namespace    http://tampermonkey.net/
// @version      0.1
// @description  Trade aids for impatient people~
// @author       Lgreenbunny
// @match        https://pokefarm.com/fields
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
    var trade = false;//change the trading page, switch with a checkbox

    var txt;
    var button;// button to start searching document for urls
    var background;
    //setup program
    document.addEventListener("DOMContentLoaded", (event) => {
        setTimeout(preparing, 1000);//makes elements for the program & other thing, once
        /*document.addEventListener("", (event) => {});*/
        //make

    });

    function preparing(){//change different things depending on the page you're on, field v. trading
        //FIELD SECTION
        //make elements
        background=document.createElement("div");
        txt=document.createElement("p");

        button=document.createElement("button");
        button.type = "button";
        var butText = document.createElement("p");
        butText.textContent = "Hi";
        button.appendChild(butText);



        background.appendChild(txt);
        background.appendChild(button);
        //place in right spot, right below the field for now~

        document.getElementById("field_field").insertAdjacentElement('afterend', background);
        //add button event function

        document.addEventListener("click", butHit);

        //TRADE SECTION (pkmn-setup trade)
        //move element-function
        if(trade){
            rearrange();
        }

        ready=true;
    }

    function butHit(){//the function when the button is hit
        //start scanning document for field
        ready=false;
        //https://drafts.csswg.org/selectors/#overview
        //https://stackoverflow.com/questions/14377590/queryselector-and-queryselectorall-vs-getelementsbyclassname-and-getelementbyid
        var arrr = document.querySelectorAll('#fieldmontip a[href^="/summary/"]');//#divId selector
        //add valid summary links to array
        //element.href returns the path of the href thing
        var sArrr = [];
        arrr.forEach(e => {
            sArrr.push(e.href);
        });
        //summary/hello isnt a valid linkk


        resultThing(sArrr);
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
        arrr.forEach(e=>{
            console.log(e + "\n");
        });

        ready=true;
    }

    //grab the html element with the info

    /*grab everything that matches the pattern
    .+?/summary/(?!hello)(.{5}) matches the pkmn's summary btw*/

    /*Present the links in a certain manner*/
})();

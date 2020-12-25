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
    var summary = true;


    var txt;
    var button, sumButton;// button to start searching document for urls
    var background;

    var questionaire;
    //setup program
    document.addEventListener("DOMContentLoaded", (event) => {
        setTimeout(preparing, 1000);//makes elements for the program & other thing, once
        /*document.addEventListener("", (event) => {});*/
        //make

    });

    function preparing(){//change different things depending on the page you're on, field v. trading
        //FIELD SECTION
        //make elements

        /*appendChild puts it on the right of the element as a child
        insertAdjacentElement inserts above or below that element, or 1st in the child list, or last in the child list for that element
        */
        //main txt
        background=document.createElement("div");
        //background.style.margin = "auto";
        background.style.whiteSpace = "pre-wrap";//allows the new lines and spaces from the string to carry over into the paragraph.
        background.style.textAlign = "center";

        txt=document.createElement("p");
        txt.textContent = "Press button to grab urls from this field currently selected (See console)";

        //button to start processing urls
        button=document.createElement("button");
        button.type = "button";
        //button.style.padding = "1px 3px";

        //text for the button above
        var butText = document.createElement("p");
        butText.textContent = "Get dem links";
        button.appendChild(butText);

        //input-type of button for form elements
        sumButton = document.createElement("INPUT");
        sumButton.setAttribute("type", "checkbox");
        sumButton.defaultChecked = false;

        //text beside the summary checkbox
        var sumButText = document.createElement("p");
        sumButText.textContent = "Do you want summary or trade links: ";

        //makes form object
        questionaire = document.createElement("form");
        //questionaire.style.alignItems = "center";

        //add main txt & form to the background/base element that was injected
        questionaire.insertAdjacentElement("afterbegin", txt);
        background.appendChild(questionaire);

        //status message

        /*add status + summary checkbox explanation to the form,
insert the starting button right after the form element
AND place the summary checkbox on the right of the summary checkbox text as a child*/
        questionaire.appendChild(sumButText);
        txt.appendChild(button);
        sumButText.appendChild(sumButton);
        //place in right spot, right below the field for now~

        document.getElementById("field_field").insertAdjacentElement('afterend', background);
        //add button event function

        button.addEventListener("click", butHit);
        sumButton.addEventListener("click", function(){
            summary ? summary=false : summary=true;//if summary's true, make false, vice versa, xor gate with ternary
            sumButton.checked = summary;
        });

        //TRADE SECTION (pkmn-setup trade)
        //move element-function
        if(trade){
            rearrange();
        }

        ready=true;
    }

    function butHit(){//the function when the button is hit
        if(ready){
            //start scanning document for field
            ready=false;//dont wanna start this over and over and over and over and over and over and over and over and over and over

            //https://drafts.csswg.org/selectors/#overview
            //https://stackoverflow.com/questions/14377590/queryselector-and-queryselectorall-vs-getelementsbyclassname-and-getelementbyid
            var field = document.getElementById("field_field").getElementsByClassName("field")[0]; //
            //console.log(field);

            //add valid summary links to array
            //element.href returns the path of the href thing
            var arrr = field.querySelectorAll("div.tooltip_content .fieldmontip h3 a[href*='summary/']");//selects all a's with that path from field class


            //console.log(arrr);
            if(arrr.length > 0 && arrr != null){
                var sArrr = [];
                arrr.forEach(e => {
                    sArrr.push(e.href);
                });
                //summary/hello isnt a valid linkk
                resultThing(sArrr);
            }

            else{
                console.log("No pokemon here");
            }
        }
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

        if(summary){
            for(var i = 0; i < arrr.length; i += 2){
                console.log(arrr[i] + "\n");
            }
        }

        else{
            for(var j = 0; j < arrr.length; j += 2){
                console.log(arrr[j].replace("summary", "trade/setup/pkmn") + "\n");
            }
        }
        ready=true;
    }

    //grab the html element with the info

    /*grab everything that matches the pattern
    .+?/summary/(?!hello)(.{5}) matches the pkmn's summary btw*/

    /*Present the links in a certain manner*/
})();

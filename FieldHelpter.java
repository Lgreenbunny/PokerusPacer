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
    var trade = true; //GM_getValue("tradeStatus", "true");//change the trading page, switch with a checkbox, not like this tho
    var summary = false;


    var txt;
    var button, sumButton, selectButton;// button to start searching document for urls
    var background;
    var resultBox;

    var questionaire;
    //setup program
    document.addEventListener("DOMContentLoaded", (event) => {
        setTimeout(preparing, 1040);//makes elements for the program & other thing, once
        /*document.addEventListener("", (event) => {});*/
        //make

    });

    function preparing(){//change different things depending on the page you're on, field v. trading
        //FIELD SECTION
        //make elements

        if(document.getElementById("field_field") != null){//if you're on the field page, not the trading page
            /*appendChild puts it on the right of the element as a child
        insertAdjacentElement inserts above or below that element, or 1st in the child list, or last in the child list for that element
        */
            //main txt
            background=document.createElement("div");
            //background.style.margin = "auto";
            background.style.whiteSpace = "pre-wrap";//allows the new lines and spaces from the string to carry over into the paragraph.
            background.style.textAlign = "center";

            txt=document.createElement("p");
            txt.textContent = "Press button to grab urls from this field currently selected (See console)~ ";

            //button to start processing urls
            button=document.createElement("button");
            button.type = "button";
            //button.style.padding = "1px 3pxon
            button.style.lineHeight = ".25";


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
            sumButText.textContent = "Check for summary link instead of trade links: ";

            //resultss
            resultBox = document.createElement("p");
            resultBox.style.overflow = "scroll";
            resultBox.style.height = "100px";
            resultBox.textContent = "Text here~";
            resultBox.style.marginLeft = "30%";
            resultBox.style.maxWidth = "40%";

            //makes form object
            questionaire = document.createElement("form");
            //questionaire.style.alignItems = "center";

            //add main txt & form to the background/base element that was injected, also the result box
            questionaire.insertAdjacentElement("afterbegin", txt);
            questionaire.insertAdjacentElement("beforeend", resultBox);

            background.appendChild(questionaire);

            //status message

            /*add status + summary checkbox explanation to the form,
insert the starting button right after the form element
AND place the summary checkbox on the right of the summary checkbox text as a child*/
            resultBox.insertAdjacentElement("beforebegin", sumButText);
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
        }




        else if(trade && document.getElementById("field_field") == null){//you're on the trading page and you gotta rearrange tho
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

        /*
         h3-div repeats btw
        0 h3 recipient, div field
        2 h3 pokemon, div whole thing
        4 items, area
        6 currency, area
        8 message, (child with the gift thing) area
        10 confirmation, area+button

        to
        h3 recipient, div field
        *confirmation, area+button, (child with the gift thing) area
        items, area
        currency, area
        *message,
        *h3 pokemon, div whole thing

        get vars of the things 2 b moved then mov
        */

        //getting the nodes & childrens

        var main = document.getElementById("tradesetup").childNodes;//get all the child
        //console.log(main);

        var sendinH3=main[10];
        var sendinMain=main[11];

        var recipientH3=main[0];
        var recipientMain=main[1];

        var pkmnH3=main[2];
        var pkmnMain=main[3];

        var messageBox=main[9];

        var giftNodeTxt=messageBox.childNodes[2];
        var giftNodeButton=messageBox.childNodes[3];
        console.log(main[8]);

        //console.log(giftNodeTxt + "\n" + giftNodeButton);

        //moving go-button up top near the recipient area
        recipientMain.insertAdjacentElement("afterend", sendinH3);
        sendinH3.insertAdjacentElement("afterend", sendinMain);

        //moving gift + text near the go button
        sendinH3.insertAdjacentElement("afterend", giftNodeTxt);
        giftNodeTxt.insertAdjacentElement("afterend", giftNodeButton);

        //
        messageBox.insertAdjacentElement("afterend", pkmnH3);
        pkmnH3.insertAdjacentElement("afterend", pkmnMain);

        //rearrange switch (on refresh)
        selectButton = document.createElement("INPUT");
        selectButton.setAttribute("type", "checkbox");
        selectButton.checked = true;
        selectButton.addEventListener("click", e=>{
            trade ? trade=false : trade=true;//if summary's true, make false, vice versa, xor gate with ternary
            selectButton.checked = trade;
            //GM_setValue("tradeStatus", trade);
        });
        var txt = document.createElement("p");
        var pompom = document.createElement("form").appendChild(txt);
        txt.textContent = "Rearrange on refresh? (To be added)";
        txt.appendChild(selectButton);
        recipientMain.insertAdjacentElement("afterbegin", pompom);

        //select & de-select paragraph
        //https://developer.mozilla.org/en-US/docs/Web/API/range/selectNodeContents,
        //just refreshes the page 4 some reason, even when event listener is gone?

    }

    function resultThing(arrr){
        //update text of result field with the urls of the field...
        var temp = "";
        if(summary){
            for(var i = 0; i < arrr.length; i += 2){
                temp += arrr[i] + "\n";
            }
        }

        else{
            for(var j = 0; j < arrr.length; j += 2){
                temp += arrr[j].replace("summary", "trade/setup/pkmn") + "\n";
            }
        }
        resultBox.textContent = temp;
        ready=true;
    }

    //grab the html element with the info

    /*grab everything that matches the pattern
    .+?/summary/(?!hello)(.{5}) matches the pkmn's summary btw*/

    /*Present the links in a certain manner*/
})();

// ==UserScript==
// @name         Pokerus Pacer Userscript
// @namespace    http://tampermonkey.net/
// @version      0.1
// @description  Tracks how well you're doing with massclicks
// @author       Lgreenbunny
// @match        https://pokefarm.com/fields/*
// @grant        GM_listValues
// @grant        GM_getValue
// @grant        GM_getResourceText
// ==/UserScript==

(function() {
    'use strict';


    /*
       *e
       Tasks:
          (See if per-page things are possible as well, & restart on refreshes, and...)
       `see if you can add different behaviors per page (like the pages allowed in the matched pages above)
       `the rest is on github btw
       *
       Editing the CSS...
          In div id=content in the public field html sheet, put a new div as the last child of the content div. Then center it (put class="center" in the <> with the ID)
              and put the clicking stats there that update regularly~ to put it as the last child:
          HTML DOM insertAdjacentElement(), using "afterend" will put it right under the element.
          1st make the element, then insert it
              x = Document.createElement(),
              ele.setAttribute("id", "ID_NAME")
              ele.set...("textContent", "UPDATED TEXT HEREREE")
          also edit textContent of the property, not innerHTML.
    */
    //var wholeCSS = document.getElementById("header");

    //cant get sub-elements with getElementById like header.get...(head-right)
    //
    //gets the date, curent population and sent interactions, should ong fetch pop. once tbh, wait until element loaded 1st.
    var /*pop, */pastInt = 0;
    var pokerus = true, total = false;
    var numInt = 0;
    var refreshR = 4;//how fast it refreshes in seconds, dont make it go below 4?
    var announcer;
    window.addEventListener("load", starter);

    //populationcount and clickcount_act_sent arent div, they're SPANS?.
    //check element id
    /*.populationcount, .clickcount_act_sent*/
    function starter(){
        /*dateATM = document.getElementById("servertimeclock").textContent;
        //pop = document.getElementById("field_globalmeta"); //these 2 are in div field_globalmeta
        inter = document.getElementById("field_globalmeta").textContent;*/
        loopy();
    }



    function loopy(){

        setInterval(updaterer, (refreshR*1000));//runs main loop every 10s.
        announcer = document.createElement("p");
        announcer.setAttribute("id", "ID_NAME")
        announcer.textContent = "UPDATED TEXT HEREREE";
        document.getElementById("field_field").insertAdjacentElement('afterend', announcer);
    }



    function updaterer(){//time updates, but the population and click count are undefined atm.
        var dateATM = document.getElementById("servertimeclock").textContent;
        var inter = document.getElementById("field_globalmeta").textContent;

        console.log("~");
        console.log("~ ~ Massclick logs:");
        //console.log(dateATM);
        //console.log(pop);
        //console.log(inter);
        clickCalc(dateATM, inter);
    }

    function clickCalc(date, interac){
        var stringy = "";
        /*date & interaction formats:
        27/Sep/2020 05:52:09 (used for the pokerus timer in the future, just get it to display massclick stuff for now)
        Population: 5,859 PokémonInteractions: 114 sent / 114 received
        */

        //getting the interaction percentage & printing...
        /*get 1st * last index of population number
        get 1st & last index of sent number
        convert to integer
        divide and return percentage of fields clicked.*/

        //1st & last pop
        //get last index of "Population: ", grab last index of number from 5 indexes down from start pt of last index earlier
        var startPop = interac.search("\\d+(,\\d+)*\\sPok");//greedily match manynumbers and the PokemonInteractions (with a comma)
        var lastPop = interac.search("\\d{1}\\sPok");//match 1 number and the PokemonInteractions

        //similar for interactions, start at "eractions: ", get last index 5 indexes from start pt.
        var startInt = interac.search("\\d+(,\\d+)*\\ssent");
        var lastInt = interac.search("\\d{1}\\ssent");

        //get substrings with the appropiate vars
        var realPop = interac.substring(startPop, lastPop+1);
        var realInt = interac.substring(startInt, lastInt+1);

        /*console.log(startPop + "\n" + lastPop + "\n" + startInt + "\n" + lastInt);//testing*/
        //console.log(realPop + "\t" + realInt);//gets the strings correctly <3

        pastInt = numInt;

        //parse strings to integers for calculations
        var numPop = parseInt(realPop.replace(",", ""));//parseInt doesn't like commas and I don't wanna use number format rn
        numInt = parseInt(realInt.replace(",", ""));

        //divide and report da string

        //console.log(numPop + "\t" + numInt + "\n%" + (100*numInt/numPop));//gets the strings correctly in percent form <3





        if(pokerus){
            //do this SECOND.
            //get time with similar string things as above, just condensed ^
            var minStart = 3 + date.search("\\d{2}:\\d{2}:\\d{2}");//starting from min & seconds
            var timeMin = parseInt(
                date.substring(
                    minStart,
                    (minStart+2)
                )
            );

            var timeSec = parseInt(
                date.substring(
                    minStart+3,
                    date.length
                )
            );

            //console.log(timeMin + " = min\t" + timeSec + " = sec");
            // 27/Sep/2020 05:52:09
            //Pokerus speed calc here using ^ information above, put function heer
            //use past int & current int to calculate how fast per 10sec, then div by 10
            var remainTime = (15*60) - ((timeMin % 15)*60) - timeSec;


            if(remainTime < 30){//say that pkrs is almost done
                announcer.textContent = "PKRS almost done...";
            }
            else{//print the clicking status~ this check shouldnt run unless the pkrs option is selected in the future~

                var rateNeed = (numPop-numInt)/remainTime; //per second, based on pokerus time

                //console.log("%.2f clicks/min needed to beat pokerus (%d click/sec)...", (rateNeed*60), rateNeed);
                //calc if it's fast enough for complete pokerus, (population-total int)/TIME LEFT (15 - serverTimeMINUTES mod 15)
                /*var rateHave = ;*/
                var rateHave = (numInt - pastInt)/refreshR;//if it's doing these calculations every 5 seconds, divide by 5s

                //display clik/sec, & click/sec needed 2 fullclick
                /*
            console.log("You're doing %.2f clicks/min right now (%d click/sec)", (rateHave*60), rateHave);
            console.log("You'll finish these fields in: %.2f min (%.2f sec)", ((numPop-numInt)/(rateHave)/60), ((numPop-numInt)/(rateHave)));
            */

                // number.toFixed(digits) rounds number or pads number so it has the right amount of digits after the decimal point
                stringy += `${(rateNeed*60).toFixed(2)} clicks/min needed to beat pokerus (${rateNeed.toFixed(2)} click/sec)...
You're doing ${(rateHave*60).toFixed(2)} clicks/min right now (${rateHave.toFixed(2)} click/sec)...
You'll finish these fields in: ${((numPop-numInt)/rateHave/60).toFixed(2)} min (${((numPop-numInt)/rateHave).toFixed(2)} sec)!`;
                printCSSssS(stringy);
            }

        }
    }

    function printCSSssS(stringy){
        //console.log(stringy);//will be replaced with CSS/HTML manipulationss
        announcer.textContent = stringy;
    }
})();

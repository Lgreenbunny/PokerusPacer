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
    var pokerus = true, main = true;//runs pokerus and basic loop
    var numInt = 0;
    var refreshR = 4;//how fast it refreshes in seconds, dont make it go below 4?
    var announcer;
    var rates = [], rLength = 5;
    var rateHave = 0;
    var lastTime = 0;//last int count + last time in seconds based on site clock
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

        setInterval(updaterer, (refreshR*1000));//runs main loop every #s.
        announcer = document.createElement("p");
        announcer.setAttribute("id", "Pokerus_Pacer");
        announcer.textContent = "Loading...";
        document.getElementById("field_field").insertAdjacentElement('afterend', announcer);

        setTimeout(function d(){
            var e = document.getElementsByName("Pokerus_Pacer").length;
            if(e = 0){
                document.getElementByName("field_field").insertAdjacentElement('afterend', announcer);
            }
        },
                   (5000));//checks if the element actually loaded in the document, if not, try again.
    }



    function updaterer(){//time updates, but the population and click count are undefined atm.
        var dateATM = document.getElementById("servertimeclock").textContent;
        var inter = document.getElementById("field_globalmeta").textContent;

        /*console.log("~");
        console.log("~ ~ Massclick logs:");
        //console.log(dateATM);
        //console.log(pop);
        //console.log(inter);*/
        if(main){clickCalc(dateATM, inter);}
        //if(pokerus){pokerusCalc();}
    }







    function clickCalc(date, interac){
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

        //use past int & current int to calculate remaining sec
        var remainTime = (15*60) - ((timeMin % 15)*60) - timeSec;



        /*used for checking if you're fast enough for complete pokerus, or just the speed you're going for the main loop

        will be replaced with rateCalc function

        (population-total int)/TIME LEFT (15 - serverTimeMINUTES mod 15)*/
        /*var rateHave = ;*/
        rateHave = rateCalc(timeMin*60+timeSec);//if it's doing these calculations every 5 seconds, divide by 5s




        if(pokerus){
            announcie(remainTime,((numPop-numInt)/remainTime),//remaining time + rate needed 2 beat pokerus
                      numPop, numInt);
        }

        else{
            announcie(remainTime, 1, numPop, numInt);
        }
    }



    function rateCalc(thisTime){
        //to add, array system
        //shift() to remove front of array, push() to push to back.
        if(lastTime != 0){//on the 1st loop, don't calculate yet.
            if(rates.length > 0 &&
               !((numInt-pastInt > 0) && (rLength > rates.length))){/*if the array's length is 0, no shift, also dont if there's new interactions and
            the rLength is > the total length*/
                rates.shift();
            }

            if((numInt-pastInt) > 0){//if the interactions didnt change, dont push anything.
               rates.push((numInt-pastInt)/(thisTime-lastTime));
        }
    }

    lastTime = thisTime;

    return (rates[0]+rates[1]+rates[2])/3;
    //
}



 function announcie(remainTime, rateNeed, numPop, numInt){
    var stringy = "";

    if(remainTime < 30 && pokerus){//say that pkrs is almost done
        //plain pokerus, needsrateHave, numPop, numInt

        stringy += `PKRS almost done...
You're doing ${(rateHave*60).toFixed(2)} clicks/min right now (${rateHave.toFixed(2)} click/sec)...
You'll finish these fields in: ${((numPop-numInt)/rateHave/60).toFixed(2)} min (${((numPop-numInt)/rateHave).toFixed(2)} sec)!`;
        announcer.textContent = stringy;
    }

    else if(pokerus){//print the clicking status~ this check shouldnt run unless the pkrs option is selected in the future~
        //plain pokerus, needs rateNeed, rateHave, numPop, numInt

        //display clik/sec, & click/sec needed 2 fullclick
        /*
            console.log("You're doing %.2f clicks/min right now (%d click/sec)", (rateHave*60), rateHave);
            console.log("You'll finish these fields in: %.2f min (%.2f sec)", ((numPop-numInt)/(rateHave)/60), ((numPop-numInt)/(rateHave)));
            */

        // number.toFixed(digits) rounds number or pads number so it has the right amount of digits after the decimal point
        stringy += `${(rateNeed*60).toFixed(2)} clicks/min needed to beat pokerus (${rateNeed.toFixed(2)} click/sec)...
You're doing ${(rateHave*60).toFixed(2)} clicks/min right now (${rateHave.toFixed(2)} click/sec)...
You'll finish these fields in: ${((numPop-numInt)/rateHave/60).toFixed(2)} min (${((numPop-numInt)/rateHave).toFixed(2)} sec)!`;
        announcer.textContent = stringy;
    }

    else{//main, needs rateHave, numPop, numInt
        stringy += `You're doing ${(rateHave*60).toFixed(2)} clicks/min right now (${rateHave.toFixed(2)} click/sec)...
You'll finish these fields in: ${((numPop-numInt)/rateHave/60).toFixed(2)} min (${((numPop-numInt)/rateHave).toFixed(2)} sec)!`;
        announcer.textContent = stringy;

    }

}








})();

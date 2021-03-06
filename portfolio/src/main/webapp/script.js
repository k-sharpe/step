// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const greetings =
    ["I have a California Real Estate Salesperson License!", "My dog is forever a puppy", "I work at Google!", "I played water polo in high school", "I have two middle names", "I really like post it notes", "My favorite home-alone activity is blasting music out loud."];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById("greeting-container");
  greetingContainer.innerText = greeting;
}

const nasaLink = "https://api.nasa.gov/planetary/apod?api_key=dFdwfqC0hgJXnZtm85spG7D1lfp1sIYbAtk5nsbw&date=";
const typewriterText = ["My Passions", "My Projects", "My Portfolio"];
var websiteData = [];
var currentWebsiteDisplayed = 0;

/**
 * To keep track of what is currently being typed out, recursive because of setTimeout.
 * Animates the front page.
 * @param {number} currentCharIndex Keeps track of the next letter to add to the element. 0 by default.
 * @param {number} curTypewriterTextIndex Keeps track of the current word in typewriterText.
 */
function renderHeroText(currentCharIndex = 0, curTypewriterTextIndex = 0) {
  typingDestination = document.getElementById("typewriter-text");
  if (currentCharIndex <= typewriterText[curTypewriterTextIndex].length) {
    typingDestination.innerText = typewriterText[curTypewriterTextIndex].substr(0, currentCharIndex);
    setTimeout(renderHeroText, /* milliseconds= */ 150, ++currentCharIndex, curTypewriterTextIndex);
  } else if (curTypewriterTextIndex < typewriterText.length - 1) {
    reverseHeroText(typingDestination.innerText.length, ++curTypewriterTextIndex);
  }
}

/**
 * A helper function complimentary of the type-forward function to animate the home page.
 * Adjustments to speed are purely stylistic and will not break anything.
 * @param {number} currentCharIndex Keeps track of the next letter to remove from the element.
 * @param {number} curTypewriterTextIndex Keeps track of current word in typewriterText
 */ 
function reverseHeroText(currentCharIndex, curTypewriterTextIndex) { 
  if (typingDestination.innerText.length === 0) {
    setTimeout(renderHeroText, /* milliseconds= */ 300, /* currentCharIndex= */0, curTypewriterTextIndex);
  } else {
    typingDestination.innerText = typingDestination.innerText.substr(0, currentCharIndex);
    setTimeout(reverseHeroText, /* milliseconds= */ 50, --currentCharIndex, curTypewriterTextIndex);
  }
}

/**
 * Easiest way to handle cascading ranges, switch seemed better for specific values.
 */
function guessBikeDistance() {
  const distance = 32;
  let reGuess = true;
  while(reGuess) {
    guess = window.prompt("How far was it? (example: 4)");
    let parsedGuess = parseFloat(guess);
    let difference = Math.abs(parsedGuess - distance);
    if (difference >= 30) {
      reGuess = window.confirm("Yikes, you're off by at least 30 miles.");
    } else if (difference >= 20) {
      reGuess = window.confirm("Not bad! Only off by 20 miles or more. Try again if you'd like");
    } else if (difference >= 10) {
      reGuess = window.confirm("Super close! Only off by 10 miles or more. Try again!");
    } else if (difference >= 5) {
      reGuess = window.confirm("Omg, only off by 5 miles or more. Try again, you're so close.");
    } else if(difference >= 3) {
      reGuess = window.confirm("WOW! Only off by 3 miles or more. Try again if you'd like.");
    } else if (difference >= 1) {
      reGuess = window.confirm("So so SO close, but not quite!");
    } else if (difference === 0.0) {
      reGuess = false;
      window.alert("Hurrah! I went exactly " + distance + " miles that Thursday!");
    } else {
      reGuess = window.confirm("That's not a valid guess, your guesses should look like '4' or '12.6' not " + guess);
    }
  }
}

function revealBikeDistance(){
  window.alert("A whopping 32 miles");
}

function displaySite() {
  let siteDisplayable = websiteData[currentWebsiteDisplayed];
  let url = siteDisplayable[0];
  let description = siteDisplayable[1];
  let votes = siteDisplayable[2];
  let image = siteDisplayable[3];
  let name = siteDisplayable[4];
  document.getElementById("to-site").setAttribute("href", url);
  document.getElementById("site-screenshot").src = image;
  document.getElementById("step-about").innerText = description;
  document.getElementById("display-name").innerText = name;
  // force redraw of image
  document.getElementById("site-screenshot").setAttribute("visibility", "hidden");
  document.getElementById("site-screenshot").setAttribute("visibility", "visible");
}

function getDataFromServlet() {
  fetch("/data").then(response => response.json()).then((sites) => {
    for (let i = 0; i < sites.length; i++) {
      websiteData.push(sites[i]);
    }
    document.getElementById("step-total").innerText = "A whopping " + websiteData.length + " sites added!";
    displaySite();
  });
}

function displayNext() {
  if (currentWebsiteDisplayed >= websiteData.length - 1) {
    currentwebsiteDisplayed = 0;
  } else {
    currentWebsiteDisplayed++;
  }
  displaySite();
}

function displayPrevious() {
  if(currentWebsiteDisplayed <= 0) {
    currentwebsiteDisplayed = websiteData.length - 1;
  } else {
    currentWebsiteDisplayed--;
  }
  displaySite();
}

function displayRandom() {
  currentWebsiteDisplayed = Math.floor(Math.random() * (websiteData.length)); 
  displaySite();
}

function start() {
  renderHeroText();
  getDataFromServlet();
  getCommentsFromServlet();
  loginLoad();
  createMap();
}

function getCommentsFromServlet() {
  fetch("/comment").then(response => response.json()).then((comments) => {
    const commentListElement = document.getElementById("comments");
    commentListElement.innerHTML = "";
    for (let i = 0; i < comments.length; i++) {
      let content = comments[i];
      commentListElement.appendChild(
        createElement(content[0], content[1]));
    }
  });
}


/** Creates an element containing name. */
function createElement(name, text) {
  const nameBox= document.createElement("h3");
  nameBox.innerText = name;
  nameBox.setAttribute("class", "comment-head");
  nameBox.setAttribute("align", "left");
  const message = document.createElement("p");
  message.innerText = text;
  const body = document.createElement("div");
  body.setAttribute("class", "comment")
  body.appendChild(nameBox);
  body.appendChild(message);
  return body;
}

function loginLoad() {
  fetch("/login").then(response => response.json()).then((data) => {
    const submissionForm = document.getElementById("comment-submission-form");
    const loginLogoutParentElement = document.getElementById("login-logout-container");
    const submissionContainer = document.getElementById("comment-submission-holder");
    const loggedIn = data[0] === "1";
    const redirectURL = data[1];
    if (loggedIn) {
      submissionContainer.style.display = "block";
      loginLogoutParentElement.appendChild(createLoginLogoutElement(loggedIn, redirectURL, data[2]));
    } else {
      submissionContainer.style.display = "none";
      loginLogoutParentElement.appendChild(createLoginLogoutElement(loggedIn, redirectURL));
    }
  });
}

/**
 * @return {boolean} False to prevent page refresh when called from birthday form. 
 */
function getBirthPicture() {
  let birthday = document.getElementById("birthday").value;
  let birthdayString = String(birthday);
  let birthdayObject = {
    year: parseInt(birthdayString.substr(0, 4)),
    month: parseInt(birthdayString.substr(5, 2)),
    day: parseInt(birthdayString.substr(8, 2)),
    birthdayString: function () {
      return String(this.year) + "-" + String(this.month) + "-" + String(this.day);
    }
  };
  if (isValidBirthday(birthdayObject)) {
    birthdayAdjust(birthdayObject);
    document.getElementById("birthday-bad-format").style.display = "none";
    fetch(nasaLink + birthdayObject.birthdayString())
    .then(response => response.json()).then((result) => {
      document.getElementById("birthday-photo").src = result.hdurl;
      document.getElementById("nasa-info").innerText = result.explanation;
      document.getElementById("nasa-title").innerText = result.title;
    })
  } else {
    document.getElementById("birthday-bad-format").style.display = "block";
  }
  return false;
}

/**
 * Adjust birthday year due to limitations by nasa's photo of the day (Started Jul 1, 1995).
 * Adjust leap year dates to be valid.
 * TODO: Use a date object
 */
function birthdayAdjust(birthday) {
  const leapYears = [1996, 2000, 2004, 2008, 2012, 2016, 2020];
  if (birthday.year < 1995 || (birthday.year === 1995 && birthday.month < 7)) {
    let randomYear = 1996 + Math.floor(Math.random() * 16);
    birthday.year = randomYear;
  } 
  if (birthday.month === 2 && birthday.day === 29 && !leapYears.includes(birthday.year)) {
    let randomLeapYear = leapYears[Math.floor(Math.random() * 6)];
    birthday.year = randomLeapYear
  }
}

/**
 * Validate String from birthday field and determine it follows a valid "yyyy-mm-dd" structure needed for nasa api call.
 * Additionally validate that the date generated by the string is a real date.
 */
 // TODO: Validate inputs from a date that has not yet occured. 
function isValidBirthday(birthday) {
  let pattern = /([12]\d{3}-([1-9]|0[1-9]|1[0-2])-([1-9]|0[1-9]|[12]\d|3[01]))/;
  if (pattern.test(birthday.birthdayString()) && birthday.birthdayString().length <= 10) {
    // verify 30-day cap for 30 day months, no leap adjustment.
    if (birthday.month === 2 && birthday.day > 29) {
      return false;
    } else if (birthday.month === 4 && birthday.day > 30) {
      return false;
    } else if (birthday.month === 6 && birthday.day > 30) {
      return false;
    } else if (birthday.month === 9 && birthday.day > 30) {
      return false;
    } else if (birthday.month === 11 && birthday.day > 30) {
      return false;
    }
    return true;
  }
  return false;
}

function createMap() {
  const centerBayArea = {lat: 37.7857, lng: -122.4011};
  const bayMap = new google.maps.Map(
    document.getElementById("map-google-headquarters"),
    {center: centerBayArea, zoom: 9, mapTypeId: "satellite"});
  bayMap.setTilt(45);
  const centerBayMarker = new google.maps.Marker({position: centerBayArea, map: bayMap, title: "Bay Area"});
  const infowindow = new google.maps.InfoWindow({content: "This is the bay!"});
  centerBayMarker.addListener("click", function() {
    infowindow.open(bayMap, centerBayMarker);
  });  
}

function createLoginLogoutElement(loggedIn, targetURL, userAddress="") {
  const body = document.createElement("div");
  const message = document.createElement("p");
  const link = document.createElement("a");
  link.setAttribute("href", targetURL);
  if (loggedIn) {
    message.innerText = "Welcome, " + userAddress;
    link.innerText = "Click here to log out";
  } else {
    message.innerText = "Wish to leave a comment? Click the link to log in. ";
    link.innerText = "Click here to log in using Google";
  }
  body.appendChild(message);
  body.appendChild(link);
  return body;
}

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
    ["I have a California Real Estate Salesperson License!", "My dog is forever a puppy", "I work at Google!", "I played water polo in high school"];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}

const typewriterText = ['My Passions', 'My Projects', 'My Portfolio'];

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
  const guess = window.prompt("How far was the bike ride? (Example answer: 12.0)");
  if (guess >= 62 || guess <= 2) {
      window.alert("Yikes, you're off by at least 30 miles.");
  } else if (guess >= 52 || guess <= 12) {
      window.alert("Not bad! Only off by 20 miles or more. Try again if you'd like");
  } else if (guess >= 42 || guess <= 22) {
      window.alert("Super close! Only off by 10 miles or more. Try again!");
  } else if (guess >= 37 || guess <= 27) {
      window.alert("Omg, only off by 5 miles or more. Try again, you're so close.");
  } else if(guess >= 35 || guess <= 29) {
      window.alert("WOW! Only off by 3 miles or more. Try again if you'd like.");
  } else if (guess > 32 || guess < 32) {
      window.alert("So so close, but not quite!");
  } else if (guess === 32) {
      window.alert("Hurrah! I went exactly 32.0 miles that Thursday!");
  }
}

function revealBikeDistance(){
  window.alert("A whopping 32 miles");
}

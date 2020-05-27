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
      ["I have a California Real Estate Salesperson Licence!", "My dog is forever a  puppy", "I work at Google!", "I played water polo in high school"];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}

// Easy way of keeping track of what word to print.
let typewriterTextIndex = 0;

// Easy-access list to make editing the hero text easy. 
let typewriterText = ['My Passions', 'My Projects', 'My Portfolio'];

/**
 * To keep track of what is currently being typed out, recursive because of setTimeout.
 * Animates the front page.
 * @param {number} currentCharIndex Keeps track of the next letter to add to the element. 0 by default.
 */
function renderHeroText(currentCharIndex = 0) {
  typingDestination = document.getElementById("typewriter-text");
  if (currentCharIndex <= typewriterText[typewriterTextIndex].length) {
    typingDestination.innerText = typewriterText[typewriterTextIndex].substr(0, currentCharIndex++);
    setTimeout(renderHeroText, 150, currentCharIndex);
  } else if (typewriterTextIndex < typewriterText.length - 1) {
    typePhrase += 1;
    reverseHeroText();
  }
}

/**
 * A helper function complimentary of the type-forward function to animate the home page.
 * Adjustments to speed are purely stylistic and will not break anything.
 */
function reverseHeroText() {
  if (typingDestination.innerText.length === 0) {
      setTimeout(renderHeroText, 300);
  } else {
      let lenOfDestText = typingDestination.innerText.length;
      typingDestination.innerText = typingDestination.innerText.substr(0, lenOfDestText - 1);
      setTimeout(reverseHeroText, 50);
  }
}

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

/**
 * Effect of typing on the hero.
 */

// Index on a given string to type next.
let typewriterStringIndex = 0;

// Index of the current word
let typePhrase = 0;

// All texts to type
let typewriterText = ['My Passions', 'My Projects', 'My Portfolio'];

// Delay in milliseconds.
let speed = 150;

/**
 * Function that handles the actual typewriter effect.
 */
function typing() {
    cleanTypewriterText();
    var typingDestination = document.getElementById('typewriterText');
    if (typewriterStringIndex < typewriterText[typePhrase].length) {
      charToAdd = typewriterText[typePhrase].charAt(typewriterStringIndex);
      typingDestination.innerText += charToAdd;
      typewriterStringIndex++;
      setTimeout(typing, speed);
  } else if (typePhrase < typewriterText.length - 1) {
      typePhrase += 1;
      typewriterStringIndex = 0;
      reverseTyping();
  }
}

/**
 * Deletes text to maintain a typing animation.
 * Adjustments to speed are purely stylistic and will not break anything.
 */
function reverseTyping() {
    var typingDestination = document.getElementById('typewriterText')
    if (typingDestination.innerText.length === 0) {
        setTimeout(typing, speed * 2);
    } else {
        let lenOfDestText = typingDestination.innerText.length;
        typingDestination.innerText = typingDestination.innerText.substr(0, lenOfDestText - 1);
        setTimeout(reverseTyping, speed / 3);
    }
}

/**
 * Cleans the typewriter text to make it display properly.
 * Why? a normal space ' ' won't be displayed, must use
 * '\xa0'.
 */
 function cleanTypewriterText() {
     for (let i = 0; i < typewriterText.length; i++) {
        typewriterText[i] = typewriterText[i].replace(' ', '\xa0');
     }
 }
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
var typewriterStringIndex = 0;
var txt = 'My Portfolio';

// Delay in milliseconds.
var speed = 150;

/**
 * Function that handles the actual typewriter effect.
 */
function typing() {
  if (typewriterStringIndex < txt.length) {
      typingDestination = document.getElementById('TypewriterText')
      charToAdd = txt.charAt(typewriterStringIndex)
      typingDestination.innerText += charToAdd;
      typewriterStringIndex++;
      setTimeout(typing, speed);
  }
}
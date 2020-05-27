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
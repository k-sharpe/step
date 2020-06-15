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

package com.google.sps;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    Collection<TimeRange> notAvailable = new HashSet<>();
    Collection<TimeRange> available = new HashSet<>();
    Collection<String> attendees = request.getAttendees();
    for (Event event : events) {
      Collection<String> eventAtteendees = event.getAttendees();
      boolean eventContainsRequestedAttendee = false;
      for (String eventAttendee : eventAtteendees) {
        if (attendees.contains(eventAttendee)) {
          eventContainsRequestedAttendee = true;
          break;
        }
      }
      if (eventContainsRequestedAttendee) {
        notAvalable.add(event.getWhen());
      }
   }
    notAvailable = Collections.sort(notAvailable, TimeRange.ORDER_BY_START);
    available = invert(notAvailable);
    return available;
  }

  private Collection<TimeRange> invert(Collection<TimeRange> original) {
    Collection<TimeRange> available = new HashSet<>();
    int start = 0;
    int end = 0;
    while (end < 2401) {
      boolean contained = false;
      for (TimeRange range : original) {
        if (range.contains(start)) {
          contained = true;
          break;
        }
      }
      if (contained) {
        if (start != end) {
          available.add()
        }
      } else {
      
      }
    }
    return available;
  }
}

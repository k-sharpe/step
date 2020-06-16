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

import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    List<TimeRange> notAvailable = new ArrayList<>();
    List<TimeRange> available = new ArrayList<>();
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
        notAvailable.add(event.getWhen());
      }
   }
    Collections.sort(notAvailable, TimeRange.ORDER_BY_START);
    available = invert(notAvailable);
    List<TimeRange> tooShort = new ArrayList<>();
    for (TimeRange open : available) {
      if (open.duration() < request.getDuration()) {
        tooShort.add(open);
      }
    }
    for (TimeRange small : tooShort) {
      available.remove(small);
    }
    return available;
  }

  private List<TimeRange> invert(List<TimeRange> original) {
    List<TimeRange> available = new ArrayList<>();
    int start = 0;
    int end = 0;
    if (original.size() == 0) {
      available.add(TimeRange.fromStartDuration(0, 1440));
      return available;
    }
    while (end < 1440) {
      boolean contained = false;
      for (TimeRange range : original) {
        if (range.contains(end)) {
          contained = true;
          break;
        }
      }
      if (contained) {
        if (start < end) {
          available.add(TimeRange.fromStartDuration(start, end - start));
          }
        end += 15;
        start = end;
      } else {
        end += 15;
      }
    }
    for (TimeRange range : original) {
        if (!range.contains(end) && start != end) {
          available.add(TimeRange.fromStartDuration(start, end - start));
          break;
        }
      }
    return available;
  }
}

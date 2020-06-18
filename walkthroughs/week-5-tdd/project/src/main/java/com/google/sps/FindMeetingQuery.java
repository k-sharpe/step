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
  private static final int SPACE_INCREMENT = 15;
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    List<TimeRange> notAvailable = new ArrayList<>();
    List<TimeRange> available = new ArrayList<>();
    List<TimeRange> notAvailableOptionalIncluded = new ArrayList<>();
    List<TimeRange> availableOptionalIncluded = new ArrayList<>();
    Collection<String> attendees = request.getAttendees();
    Collection<String> optionalAttendees = request.getOptionalAttendees();
    Collection<String> allAttendees = getAllAttendees(attendees, optionalAttendees);

    if (!attendees.isEmpty()) {
      if (optionalAttendees.isEmpty()) {
        findNonAvailable(events, request, notAvailable, attendees);
      } else {
        findNonAvailable(events, request, notAvailable, attendees);
        findNonAvailable(events, request, notAvailableOptionalIncluded, allAttendees);
        available = invert(notAvailable);
        availableOptionalIncluded = invert(notAvailableOptionalIncluded);
        available = removeDurationTooSmall(request, available);
        availableOptionalIncluded = removeDurationTooSmall(request, availableOptionalIncluded);
        if (availableOptionalIncluded.isEmpty()) {
          return available;
        }
        return availableOptionalIncluded;
      }
    } else if (!optionalAttendees.isEmpty()) {
      attendees = optionalAttendees;
      findNonAvailable(events, request, notAvailable, attendees);
    }
    available = invert(notAvailable);
    available = removeDurationTooSmall(request, available);
    return available;
  }

  /**
  * Create a copy to prevent manipulation of attendees or optionalAttendees sets.
  */
  private Collection<String> getAllAttendees(Collection<String> attendees, Collection<String> optionalAttendees) {
    Collection<String> all = new HashSet<>(attendees);
    Collection<String> optional = new HashSet<>(optionalAttendees);
    all.addAll(optional);
    return all;
  }

  /**
  * Helper function to increase readability of parent method.
  */
  private List<TimeRange> removeDurationTooSmall(MeetingRequest request, List<TimeRange> available) {
    List<TimeRange> goodDuration = new ArrayList<>();
    for (TimeRange open : available) {
      if (open.duration() >= request.getDuration()) {
        goodDuration.add(open);
      }
    }
    return goodDuration;
  }

  /**
  * Helper method to place all events that have requested attendees into a list.
  */
  private void findNonAvailable(Collection<Event> events, MeetingRequest request,
                     List<TimeRange> notAvailable, Collection<String> attendees) {
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
  }

  /**
  * Helper method to invert a List of Time Ranges for a day (Used to find availability).
  * TODO: Support seconds, currently limited to meetings that fall on 15 minute intervals.
  */
  private List<TimeRange> invert(List<TimeRange> original) {
    List<TimeRange> available = new ArrayList<>();
    int start = 0;
    int end = 0;
    
    if (original.isEmpty()) {
      available.add(TimeRange.fromStartDuration(0, TimeRange.END_OF_DAY + 1));
      return available;
    }
    while (end <= TimeRange.END_OF_DAY) {
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
        end += SPACE_INCREMENT;
        start = end;
      } else {
        end += SPACE_INCREMENT;
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

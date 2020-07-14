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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    List<TimeRange> resultRange = new ArrayList<>();
    Collection<String> attendees = request.getAttendees();
    long duration = request.getDuration();
    if (duration > TimeRange.END_OF_DAY - TimeRange.START_OF_DAY + 1) return resultRange;

    int[] timeInDay = new int[TimeRange.END_OF_DAY - TimeRange.START_OF_DAY + 2];

    // For each event with any attendees attending the new event, add its start time in
    // timeInDay by 1, subtract its end time (exclusive) in timeInDay by 1.
    for (Event e: events) {
        if(!Collections.disjoint(attendees, e.getAttendees())) {
            TimeRange curEventTime = e.getWhen();
            ++timeInDay[curEventTime.start()];
            --timeInDay[curEventTime.end()];
        }
    }

    int start = TimeRange.START_OF_DAY;
    int end = TimeRange.START_OF_DAY;
    int occupiedPeople = 0;

    // occupiedPeople is the cumulative value for the timeInDay. Only when occupiedPeople is 0, the
    // current time is available for everyone.
    while (start <= TimeRange.END_OF_DAY - (int)duration + 1) {
        occupiedPeople += timeInDay[end];
        while (occupiedPeople == 0 && end <= TimeRange.END_OF_DAY) {
            ++end;
            if (end > TimeRange.END_OF_DAY) break;
            occupiedPeople += timeInDay[end];
        }
        if (end - start >= (int)duration) {
            resultRange.add(TimeRange.fromStartEnd(start, end, false));
        }
        start = end + 1;
        end = start;
    }

    return resultRange;
  }
}

package io.pivotal.pal.tracker;

import java.sql.Time;
import java.util.*;

public class InMemoryTimeEntryRepository implements TimeEntryRepository{
    private Map<Long, TimeEntry> timeEntryMap = new HashMap();
    long myId = 0L;


    public TimeEntry create(TimeEntry timeEntry) {
        myId = myId+1;
        System.out.println("My ID -- > " + myId);
        timeEntry.setId(myId);
        timeEntryMap.put(myId, timeEntry);
        return timeEntry;
    }

    public TimeEntry find(long id) {
        return (TimeEntry) timeEntryMap.get(id);
    }



    public TimeEntry update(long id, TimeEntry timeEntry) {
        if (timeEntryMap.containsKey(id)) {
            System.out.println("In IF");
            timeEntry.setId(id);
            timeEntryMap.replace(id, timeEntryMap.get(id), timeEntry);
            return timeEntry;
        }
        else
            return null;
    }

    public Collection<TimeEntry> list() {
        return timeEntryMap.values();
    }

    public TimeEntry delete(long id) {
        return timeEntryMap.remove(id);
    }
}

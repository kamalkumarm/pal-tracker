package io.pivotal.pal.tracker;

import java.util.Collection;

public interface TimeEntryRepository {

    public TimeEntry find(long nonExistentTimeEntryId);

    public Collection<TimeEntry> list();

    public TimeEntry update(long eq, TimeEntry any);

    public void delete(long timeEntryId);

    public TimeEntry create(TimeEntry timeEntryToCreate);
}

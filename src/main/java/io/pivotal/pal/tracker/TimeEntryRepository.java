package io.pivotal.pal.tracker;

import java.util.Collection;
import java.util.List;

public interface TimeEntryRepository {

    public TimeEntry find(long nonExistentTimeEntryId);

    public List<TimeEntry> list();

    public TimeEntry update(long eq, TimeEntry any);

    public void delete(long timeEntryId);

    public TimeEntry create(TimeEntry timeEntryToCreate);
}

package io.pivotal.pal.tracker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(value = "/time-entries")
public class TimeEntryController {


    private TimeEntryRepository timeEntryRepository;
    private final DistributionSummary timeEntrySummary;
    private final Counter actionCounter;

    public TimeEntryController(TimeEntryRepository timeEntryRepository,
                               MeterRegistry meterRegistry) {
        this.timeEntryRepository = timeEntryRepository;
        timeEntrySummary = meterRegistry.summary("timeEntry.summary");
        actionCounter = meterRegistry.counter("timeEntry.actionCounter");
    }

    @GetMapping("/{ID}")
    public ResponseEntity<TimeEntry> read(@PathVariable("ID") long timeEntryId) {
        TimeEntry localtimeEntry = timeEntryRepository.find(timeEntryId);
        if(localtimeEntry != null) {
            actionCounter.increment();
            return new ResponseEntity<>(localtimeEntry, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{ID}")
    public ResponseEntity update(@PathVariable("ID") long timeEntryId, @RequestBody TimeEntry expected) {
        TimeEntry localtimeEntry = timeEntryRepository.update(timeEntryId, expected);

        if(localtimeEntry != null) {
            actionCounter.increment();
            return new ResponseEntity<>(localtimeEntry, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{ID}")
    public ResponseEntity delete(@PathVariable("ID") long timeEntryId) {
        timeEntryRepository.delete(timeEntryId);
        actionCounter.increment();
        timeEntrySummary.record(timeEntryRepository.list().size());
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @PostMapping("")
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {
        TimeEntry timeEntry = timeEntryRepository.create(timeEntryToCreate);
        actionCounter.increment();
        timeEntrySummary.record(timeEntryRepository.list().size());
        return new ResponseEntity<>(timeEntry, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TimeEntry>> list() {
        List<TimeEntry> listEntries = new ArrayList<>(timeEntryRepository.list());
        actionCounter.increment();
        return new ResponseEntity<>(listEntries, HttpStatus.OK);
    }
}

package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/time-entries")
public class TimeEntryController {


    private TimeEntryRepository timeEntryRepository;

    public TimeEntryController(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }

    @GetMapping("/{ID}")
    public ResponseEntity<TimeEntry> read(@PathVariable("ID") long timeEntryId) {
        System.out.println("In Read method");
        TimeEntry localtimeEntry = timeEntryRepository.find(timeEntryId);
        if(localtimeEntry != null)
            return new ResponseEntity<>(localtimeEntry, HttpStatus.OK);
        else
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{ID}")
    public ResponseEntity update(@PathVariable("ID") long timeEntryId, @RequestBody TimeEntry expected) {
        TimeEntry localtimeEntry = timeEntryRepository.update(timeEntryId, expected);

        if(localtimeEntry != null)
            return new ResponseEntity<>(localtimeEntry, HttpStatus.OK);
        else
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{ID}")
    public ResponseEntity delete(@PathVariable("ID") long timeEntryId) {
        TimeEntry localtimeEntry = timeEntryRepository.delete(timeEntryId);

        if(localtimeEntry != null)
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(null, HttpStatus.NOT_MODIFIED);
    }

    @PostMapping("")
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {
        return new ResponseEntity<>(timeEntryRepository.create(timeEntryToCreate), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<TimeEntry>> list() {
        System.out.println("In List method");
        return new ResponseEntity<>((List)timeEntryRepository.list(), HttpStatus.OK);
    }
}
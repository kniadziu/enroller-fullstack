package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/meetings")
public class MeetingRestController {

    @Autowired
    MeetingService meetingService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetings() {

        Collection<Meeting> meetings = meetingService.getAll();
        return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> addMeeting(@RequestBody Meeting meeting) {
        meetingService.add(meeting);
        return new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{meetingId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable long meetingId) {
        Meeting meeting = meetingService.findById(meetingId);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        meetingService.delete(meeting);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/{meetingId}/participants", method = RequestMethod.POST)
    public ResponseEntity<?> registerMe(@PathVariable long meetingId) {
        Meeting meeting = meetingService.findById(meetingId);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Participant participant = meetingService.addParticipant(meeting, username);
        return new ResponseEntity<>(participant, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{meetingId}/participants/me", method = RequestMethod.DELETE)
    public ResponseEntity<?> unregisterMe(@PathVariable long meetingId) {
        Meeting meeting = meetingService.findById(meetingId);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        meetingService.removeParticipant(meeting, username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

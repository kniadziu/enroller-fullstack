package com.company.enroller.persistence;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component("meetingService")
public class MeetingService {

    DatabaseConnector connector;

    @Autowired
    ParticipantService participantService;

    public MeetingService() {
        connector = DatabaseConnector.getInstance();
    }

    public Collection<Meeting> getAll() {
        String hql = "FROM Meeting";
        Query query = connector.getSession().createQuery(hql);
        return query.list();
    }
//
    public Meeting add(Meeting meeting) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().save(meeting);
        transaction.commit();
        return meeting;
    }

    public void delete(Meeting meeting) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().delete(meeting);
        transaction.commit();
    }

    public Meeting findById(long meetingId) {
        return (Meeting) connector.getSession().get(Meeting.class, meetingId);
    }

    public Participant addParticipant(Meeting meeting, String username) {
        Participant participant = participantService.findByLogin(username);
        meeting.addParticipant(participant);
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().save(meeting);
        transaction.commit();
        return participant;
    }

    public void removeParticipant(Meeting meeting, String username) {
        Participant participant = participantService.findByLogin(username);
        meeting.removeParticipant(participant);
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().save(meeting);
        transaction.commit();
    }
}

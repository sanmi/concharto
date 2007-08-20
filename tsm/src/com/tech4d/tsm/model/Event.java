package com.tech4d.tsm.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="EVENTS")
public class Event {
    private Long id;

    private String title;
    
    @Temporal(TemporalType.TIME)
    private Date date;

    public Event() {}

    @Id 
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    private void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    //TODO template this to Person
    private Set<Person> participants = new HashSet<Person>();

    @ManyToMany(
            targetEntity=com.tech4d.tsm.model.Person.class,
            cascade={CascadeType.PERSIST, CascadeType.MERGE}
    )
    @JoinTable(
            name="PERSON_EVENT",
            joinColumns={@JoinColumn(name="EVENT_ID")},
            inverseJoinColumns={@JoinColumn(name="PERSON_ID")}
    )
    public Set<Person> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<Person> participants) {
        this.participants = participants;
    }

}
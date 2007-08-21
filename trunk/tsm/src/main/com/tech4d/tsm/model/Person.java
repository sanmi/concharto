package com.tech4d.tsm.model;

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

import org.hibernate.annotations.CollectionOfElements;

@Entity
@Table(name="PERSON")
public class Person {

    private Long id;
    private int age;
    private String firstname;
    private String lastname;

    public Person() {}


    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }


    private Set<String> emailAddresses = new HashSet<String>();

    @CollectionOfElements
    public Set<String> getEmailAddresses() {
        return emailAddresses;
    }

    public void setEmailAddresses(Set<String> emailAddresses) {
        this.emailAddresses = emailAddresses;
    }


    private Set<Event> events = new HashSet<Event>();

    // Defensive, convenience methods
    @ManyToMany(
            targetEntity=com.tech4d.tsm.model.Event.class,
            cascade={CascadeType.PERSIST, CascadeType.MERGE}
    )
    @JoinTable(
            name="PERSON_EVENT",
            joinColumns={@JoinColumn(name="PERSON_ID")},
            inverseJoinColumns={@JoinColumn(name="EVENT_ID")}
    )
    protected Set<Event> getEvents() {
        return events;
    }

    protected void setEvents(Set<Event> events) {
        this.events = events;
    }

    public void addToEvent(Event event) {
        this.getEvents().add(event);
        event.getParticipants().add(this);
    }

    public void removeFromEvent(Event event) {
        this.getEvents().remove(event);
        event.getParticipants().remove(this);
    }

}
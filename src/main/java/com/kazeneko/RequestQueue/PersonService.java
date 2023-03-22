package com.kazeneko.RequestQueue;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;
    private Queue<Person> personPostQueue = new ArrayDeque<>();
    public Person addPerson(Person person) {
        customLog(LocalDateTime.now(), "SAVE TO QUEUE " + person.toString()); // saving text description in file
        personPostQueue.offer(person);
        return person;
        //return personRepository.save(person);
    }
    public List<Person> getAllPersons(int limit) {
        List<Person> persons = personRepository.findAll();
        if (limit <= persons.size()) {
            return personRepository.findAll().subList(0,limit);
        } else {
            return persons;
        }
    }
    private void customLog(LocalDateTime dateTime, String message) {
        File logFile = new File("logFile.txt");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true));
            String logEntry = "Log : " + dateTime + ", " + message + "\n";
            writer.append(logEntry);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Scheduled(fixedDelay = 10000)
    public void process() {
        if (!personPostQueue.isEmpty()) {
            customLog(LocalDateTime.now(), "SAVE TO DATABASE " + personPostQueue.peek().toString());
            personRepository.save(personPostQueue.poll());

        }
    }
    @EventListener(ApplicationReadyEvent.class)
    public void loadSaved() {
        ObjectMapper objectMapper = new ObjectMapper();
        File savedJSON = new File("saved.json");
        if (savedJSON.exists()) {
            try {
                List<Person> persons = objectMapper.readValue(savedJSON, new TypeReference<List<Person>>() {});
                for (Person person : persons) {
                    personPostQueue.offer(person);
                }
                customLog(LocalDateTime.now(), "LOAD FROM SAVED");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @EventListener(ContextClosedEvent.class)
    public void save() {
        ObjectMapper objectMapper = new ObjectMapper();
        File savedJSON = new File("saved.json");
        try {
            objectMapper.writeValue(savedJSON, personPostQueue);
            customLog(LocalDateTime.now(), "SAVED");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

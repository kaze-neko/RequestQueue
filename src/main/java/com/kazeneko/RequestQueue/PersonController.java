package com.kazeneko.RequestQueue;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/persons")
public class PersonController {
    @Autowired
    private PersonService personService;

    @GetMapping
    public List<Person> getAllPersons(@RequestParam int limit) {
        return personService.getAllPersons(limit);
    }

    @PostMapping
    public Person addPerson(@RequestBody Person person) {
        if (person.getName() == null || person.getName().isEmpty()) {
            throw new IllegalArgumentException("Name can not be empty!");
        }
        if (person.getSurname() == null || person.getSurname().isEmpty()) {
            throw new IllegalArgumentException("Surname can not be empty!");
        }
        if (person.getAge() == null || person.getAge() < 0) {
            throw new IllegalArgumentException("Age can not be empty or less than 0!");
        }
        if (person.getName().charAt(0) >= 'a' && person.getName().charAt(0) <= 'z') {
            throw new IllegalArgumentException("Name can not start from lowercase!");
        }
        if (person.getSurname().charAt(0) >= 'a' && person.getSurname().charAt(0) <= 'z') {
            throw new IllegalArgumentException("Surname can not start from lowercase!");
        }
        return personService.addPerson(person);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CustomResponseError handleIllegalArgumentException
            (IllegalArgumentException exception, HttpServletRequest request)
    {
        return new CustomResponseError(LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                exception.getMessage(),
                request.getRequestURI());
    }
}

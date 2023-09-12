package com.example.demo.Student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository){
        this.studentRepository=studentRepository;
    }

    public List<Student> getStudents(){
        return studentRepository.findAll();
    }


    public void addNewStudent(Student student) {

        Optional<Student> studentByEmail = studentRepository.findStudentByEmail(student.getEmail());

        if(studentByEmail.isPresent()){
            throw new IllegalStateException("email taken");
        }
        studentRepository.save(student);
        System.out.println(student);
    }

    public void deleteStudent(Long studentId) {
        boolean aux= studentRepository.existsById(studentId);
        if(!aux){
            throw new IllegalStateException("student with id:" + studentId + " does not exists");
        }
        studentRepository.deleteById(studentId);
    }

    @Transactional
    public void updateStudent(Long studentId, String name, String email, Integer age) {
            Student student = studentRepository.findById(studentId).orElseThrow(() -> new IllegalStateException(
                    "student with id " + studentId + " does not exist"
            ));

            //name
            if (name != null && name.length() > 0 && !Objects.equals(student.getName(), name)){
                student.setName(name);
            }

            //email
            if (email != null && email.length() > 0 && !Objects.equals(student.getEmail(), email)){
               Optional<Student> studentOptional = studentRepository.findStudentByEmail(email);
               if (studentOptional.isPresent()){
                   throw new IllegalStateException("email taken");
               }
                student.setEmail(email);
            }

            //age
            if (age != null && age > 0 && !Objects.equals(student.getAge(), age)){
                student.setAge(age);
            }

    }
}

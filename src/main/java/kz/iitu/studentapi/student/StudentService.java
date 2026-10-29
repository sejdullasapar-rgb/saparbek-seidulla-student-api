package kz.iitu.studentapi.student;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public List<Student> findAll() {
        return repository.findAll();
    }

    public Student findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
    }

    @Transactional
    public Student create(Student student) {
        student.setId(null);
        return repository.save(student);
    }

    @Transactional
    public void delete(Long id) {
        Student existing = findById(id);
        repository.delete(existing);
    }
}

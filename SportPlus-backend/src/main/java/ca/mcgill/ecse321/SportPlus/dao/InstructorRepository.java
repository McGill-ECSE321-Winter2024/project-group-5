package ca.mcgill.ecse321.SportPlus.dao;
import org.springframework.data.repository.CrudRepository;


import ca.mcgill.ecse321.SportPlus.model.Instructor;

public interface InstructorRepository extends CrudRepository<Instructor, String>{

}

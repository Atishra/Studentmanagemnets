package net.javaguides.sms.service;

import java.io.IOException;
import java.util.List;


import jakarta.servlet.http.HttpServletResponse;
import net.javaguides.sms.Model.BaseModel;
import net.javaguides.sms.Model.CommonResponse;
import net.javaguides.sms.Model.UserDetail;
import net.javaguides.sms.entity.Student;
import net.javaguides.sms.entity.User;
import net.javaguides.sms.service.impl.StudentServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public interface StudentService {
	    List<Student> getAllStudents();
	
	Student saveStudent(Student student);
	
	Student getStudentById(Long id);
	
	Student updateStudent(Long id, Student student);
	
	void deleteStudentById(Long id);

	public User userExist(String userEmail);

	UserDetail passwordMatch(String userPassword, User userDetail);

	CommonResponse uploadexcel(MultipartFile file);

	CommonResponse imageUpload(BaseModel baseModel);

	CommonResponse readid(Student mangementEntity);

	void generateExcel();

	void downloadexcel(HttpServletResponse response) throws IOException;

//	List<Student> getdataofstudent();
//
//	Student savedata(Student student);
//
//	String deletebyid(Long id);
//
//	String dataupdate(Long id, String firstName, String lastName);

//	void generateExcel(HttpServletResponse response);


//	CommonResponse readDataDsa(MultipartFile file);

//	UserDetail passwordMatch(String userPassword, User userDetail1);

//	UserDetail passwordMatch(String userPassword, User userDetail);

//	CommonResponse uploadexcel(MultipartFile file);

//	void generateExcel(HttpServletResponse response) throws IOException;
}

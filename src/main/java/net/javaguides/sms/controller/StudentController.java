package net.javaguides.sms.controller;
import jakarta.servlet.http.HttpServletResponse;
import net.javaguides.sms.Model.BaseModel;
import net.javaguides.sms.Model.CommonResponse;
import net.javaguides.sms.Model.UserDetail;
import net.javaguides.sms.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import net.javaguides.sms.entity.Student;
import net.javaguides.sms.service.StudentService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

//@RestController
@Controller

//@RequestMapping("/excel")
public class StudentController {

	private StudentService studentService;

	public StudentController(StudentService studentService) {
		super();
		this.studentService = studentService;
	}


//	@GetMapping("/login")
//	public String createStudentLoginForm(Model model) {
//		Student student = new Student();
//		model.addAttribute("student", student);
//		return "student_login";
//
//	}


	// handler method to handle list students and return mode and view
	@GetMapping("/students")
	public String listStudents(Model model) {
		model.addAttribute("students", studentService.getAllStudents());
		return "students";
	}


	@GetMapping("/students/new")
	public String createStudentForm(Model model) {

		// create student object to hold student form data
		Student student = new Student();
		model.addAttribute("student", student);
		return "create_student";

	}

	@PostMapping("/students")
	public String saveStudent(@ModelAttribute("student") Student student) {
		studentService.saveStudent(student);
		return "redirect:/students";
	}

	@GetMapping("/students/edit/{id}")
	public String editStudentForm(@PathVariable Long id, Model model) {
		model.addAttribute("student", studentService.getStudentById(id));
		return "edit_student";
	}

	@PostMapping("/students/{id}")
	public String updateStudent(@PathVariable Long id,
								@ModelAttribute("student") Student student,
								Model model) {

		// get student from database by id
		Student existingStudent = studentService.getStudentById(id);
		existingStudent.setId(id);
		existingStudent.setFirstName(student.getFirstName());
		existingStudent.setLastName(student.getLastName());
		existingStudent.setEmail(student.getEmail());

		// save updated student object
		studentService.updateStudent(id, existingStudent);
		return "redirect:/students";
	}

	// handler method to handle delete student request

	@GetMapping("/students/{id}")
	public String deleteStudent(@PathVariable Long id) {
		studentService.deleteStudentById(id);
		return "redirect:/students";
	}

	@PostMapping("/uploads")
	public CommonResponse uploadexcel(@RequestParam("file") MultipartFile file) {
		return studentService.uploadexcel(file);
	}

	@PostMapping("/post-image-file")
	public CommonResponse readImageFile(@RequestBody BaseModel baseModel) throws IOException {
		CommonResponse responseModel = new CommonResponse();
		responseModel = studentService.imageUpload(baseModel);
		return responseModel;
	}

	@PostMapping("/post-text-file-by-id")
	public CommonResponse readIdForText(@RequestBody Student mangementEntity) {
		CommonResponse responseModel = new CommonResponse();
		responseModel = studentService.readid(mangementEntity);
		return responseModel;
	}

	@GetMapping("/generate")
	public String generateExcel() {
		studentService.generateExcel();
		return "Excel generated successfully!";
	}

	@GetMapping("/download")
	public void downloadExcel(HttpServletResponse response) throws IOException {
		studentService.downloadexcel(response);
	}

	//	@GetMapping("/excel")
//	public void generateExcelReport(HttpServletResponse response) throws Exception{
//
//		response.setContentType("application/octet-stream");
//
//		String headerKey = "Content-Disposition";
//		String headerValue = "attachment;filename=book2.xls";
//
//		response.setHeader(headerKey, headerValue);
//
//		studentService.generateExcel(response);
//
//		response.flushBuffer();
//	}
	@PostMapping("/login")
	public String loginAuthentication(@RequestBody User userRequest, Model model) {
		UserDetail userDetail1 = new UserDetail();
		String userEmail = userRequest.getEmail();
		String userPassword = userRequest.getPassword();

		try {
			if (!userEmail.isEmpty() && userEmail.contains("@gmail") && !userPassword.isEmpty()) {
				User userDetail = studentService.userExist(userEmail);

				if (userDetail != null) {
					String dbuserDetail = userDetail.getPassword();

					if (userPassword.equals(dbuserDetail)) {
						userDetail1.setCode("0000");
						userDetail1.setMsg("Login successfully");
						userDetail1.setUserId(userDetail.getUserid());
						userDetail1.setEmail(userDetail.getEmail());
						userDetail1.setPassword(userDetail.getPassword());
					} else {
						userDetail1.setCode("1111");
						userDetail1.setMsg("Invalid Password");
					}
				} else {
					userDetail1.setCode("1111");
					userDetail1.setMsg("User does not exist");
				}
			} else {
				userDetail1.setCode("1111");
				userDetail1.setMsg("Invalid user email");
			}
		} catch (Exception e) {
			userDetail1.setCode("1111");
			userDetail1.setMsg("Technical issue");
		}
		return userEmail;
	}

//	@GetMapping("/getdata")
//	public List<Student> getalldata() {
//		return studentService.getdataofstudent();
//	}
//
//	@PostMapping("/post")
//	public Student poststudent(@RequestBody Student student) {
//		return studentService.savedata(student);
//
//	}
//
//	@DeleteMapping("/delete")
//	public String delete(@PathVariable Long id) {
//		return studentService.deletebyid(id);
//	}
//	@PutMapping("/update")
//	public String update(@RequestBody Student student){
//		studentService.dataupdate(student.getId(),student.getFirstName(),student.getLastName());
//		return "done";
//	}


}



//	model.addAttribute("userDetail", userDetail1);
//	return "loginResult"; // Return the name of the JSP file
//}










	/*@CrossOrigin
	@PostMapping("/dsaExportUpload")
	public ResponseEntity<CommonResponse> exportFileUpload(@RequestParam("file") MultipartFile file) {
		CommonResponse commonResponse = new CommonResponse();
		commonResponse = studentService.readDataDsa(file);

		return new ResponseEntity<CommonResponse>(commonResponse, HttpStatus.OK);
	}
*/









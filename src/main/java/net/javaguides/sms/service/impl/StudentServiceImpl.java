package net.javaguides.sms.service.impl;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import jakarta.servlet.http.HttpServletResponse;
import net.javaguides.sms.Model.BaseModel;
import net.javaguides.sms.Model.CommonResponse;
import net.javaguides.sms.Model.UserDetail;
import net.javaguides.sms.entity.User;
import net.javaguides.sms.repository.UserRepository;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import net.javaguides.sms.entity.Student;
import net.javaguides.sms.repository.StudentRepository;
import net.javaguides.sms.service.StudentService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	private StudentRepository studentRepository;
	@Autowired
	private UserRepository userRepository;

//	Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);

	public StudentServiceImpl(StudentRepository studentRepository) {
		super();
		this.studentRepository = studentRepository;
	}

	@Override
	public List<Student> getAllStudents() {
		List<Student> students = studentRepository.findAll();
		return students;


	}
//		return studentRepository.findAll();



	@Override
	public Student saveStudent(Student student) {
		return studentRepository.save(student);
	}

	@Override
	public Student getStudentById(Long id) {
		return studentRepository.findById(id).get();
	}

	@Override
	public Student updateStudent(Long id, Student student) {
		return studentRepository.save(student);
	}

	@Override
	public void deleteStudentById(Long id) {
		studentRepository.deleteById(id);
	}
	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Override
	public User userExist(String userEmail) {
		return userRepository.findUser(userEmail);
	}

	@Override
	public UserDetail passwordMatch(String userPassword, User userDetail) {
		UserDetail userDetail1 = new UserDetail();
		if (passwordEncoder.matches(userPassword, userDetail.getPassword())) {
			System.out.println("password correct");
			userDetail1.setCode("0000");
			userDetail1.setMsg("Login successfully");
			userDetail1.setUserId(userDetail.getUserid());
			userDetail1.setEmail(userDetail.getEmail());
			userDetail1.setPassword(userDetail.getPassword());


		}
        return userDetail1;
    }

	@Override
	public CommonResponse uploadexcel(MultipartFile file) {
		//List<Student> studentList = new ArrayList<>();
		List<Student> studentModels = new ArrayList<>();
		CommonResponse commonResponse=new CommonResponse();
		int count = 0;
//        String errorMsg = "";

		try  {
			InputStream inputStream = file.getInputStream();
			Workbook workbook = WorkbookFactory.create(inputStream);
			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			Boolean fileFormat = true;
			Row headerRow = rowIterator.next();

			while (rowIterator.hasNext()) {
				count++;
				Row row = rowIterator.next();
				//StudentModel studentModel = new StudentModel();
				Student student = new Student();
				for (int i = 0; i < 5; i++) {
					Cell cell = row.getCell(i);
//					if (cell == null || cell.getCellType() == CellType.BLANK) {

						switch (i) {
							case 1:
								student.setFirstName(row.getCell(0).toString());
								break;
							case 2:
								student.setLastName(row.getCell(1).toString());
								break;
							case 3:
								student.setEmail(row.getCell(2).toString());
								break;
							case 4:
								student.setPassword(row.getCell(3).toString());
								break;

						}
//					} else {
//						commonResponse.setCode("1111");
//						commonResponse.setMsg("file upload error due to row no " + (row.getRowNum() + 1) + " is empty:");
//					}

				}
				studentModels.add(student);
				studentRepository.saveAll(studentModels);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
		System.out.println(studentModels);
		return commonResponse;
	}

	@Override
	public CommonResponse imageUpload(BaseModel baseModel) {
		CommonResponse responseModel = new CommonResponse();
		byte[] binaryData = Base64.getDecoder().decode(baseModel.getBase64Data());
		String fileName = "src/main/resources/ImageUpload/";
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());

		try (FileOutputStream fileOutputStream = new FileOutputStream(fileName+timeStamp+".jpg")) {
			fileOutputStream.write(binaryData);
			System.out.println("File saved successfully to: " + fileName);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		return responseModel;
	}

	@Override
	public CommonResponse readid(Student mangementEntity) {


			CommonResponse responseModel = new CommonResponse();
			Student mangement = new Student();
			String filePath = "src/main/resources/";

			Optional<Student> mangementEntityOption = studentRepository.findById(mangementEntity.getId());
		mangement = mangementEntityOption.get();
			String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());

			if (mangementEntityOption.isPresent()) {
				try (FileWriter fileWriter = new FileWriter(filePath+timeStamp+ "user_data.txt")) {
					fileWriter.write(" ID: " + mangement.getId() + "\n");
					fileWriter.write("Name: " + mangement.getFirstName() + "\n");
					fileWriter.write("Email: " + mangement.getLastName() + "\n");

					responseModel.setMsg("Data uploaded to user_data.txt");
					responseModel.setCode("0000");
				} catch (IOException e) {
					responseModel.setMsg("Data not uploaded to user_data.txt");
					responseModel.setCode("1111");
				}
			}
			return responseModel;
		}

	@Override
	public void generateExcel() {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Sample Excel");

		Row headerRow = sheet.createRow(0);
		headerRow.createCell(0).setCellValue("firstName");
		headerRow.createCell(1).setCellValue("lastName");

		Row dataRow = sheet.createRow(1);
		dataRow.createCell(0).setCellValue("ashu");
		dataRow.createCell(1).setCellValue("rai");

		try (FileOutputStream outputStream = new FileOutputStream("sample.xlsx")) {
			workbook.write(outputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void downloadexcel(HttpServletResponse response) throws IOException {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Sample Excel");

		Row headerRow = sheet.createRow(0);
		headerRow.createCell(0).setCellValue("FirstName");
		headerRow.createCell(1).setCellValue("LastName");

		Row dataRow = sheet.createRow(1);
		dataRow.createCell(0).setCellValue("ashu");
		dataRow.createCell(1).setCellValue("rai");

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=excel.xlsx");
		workbook.write(response.getOutputStream());
		workbook.close();
	}

//	@Override
//	public List<Student> getdataofstudent() {
//		return studentRepository.findAll();
//	}
//
//	@Override
//	public Student savedata(Student student) {
//		return studentRepository.save(student);
//
//	}
//
//	@Override
//	public String deletebyid(Long id) {
//		studentRepository.deleteById(id);
//		return "done";
//	}
//
//    @Override
//    public String dataupdate(Long id, String firstName, String lastName) {
//        studentRepository.updatebyId(id,firstName,lastName);
//        return "done";
//
//    }


}



//	@Override
//	public void generateExcel(HttpServletResponse response) {
//			List<Student> students = studentRepository.findAll();
//			HSSFWorkbook workbook = new HSSFWorkbook();
//			HSSFSheet sheet = workbook.createSheet("student  Info");
//			HSSFRow row = sheet.createRow(0);
//
//			row.createCell(0).setCellValue("firstName");
//			row.createCell(1).setCellValue("lastName");
//			row.createCell(2).setCellValue("email");
//
//			int dataRowIndex = 1;
//
//			for (Student student : students) {
//				HSSFRow dataRow = sheet.createRow(dataRowIndex);
//				dataRow.createCell(0).setCellValue(student.getFirstName());
//				dataRow.createCell(1).setCellValue(student.getLastName());
//				dataRow.createCell(2).setCellValue(student.getEmail());
//				dataRowIndex++;
//			}
//
//			ServletOutputStream ops = response.getOutputStream();
//			workbook.write(ops);
//			workbook.close();
//			ops.close();
//		}

//	}








/*	@Override
	public CommonResponse readDataDsa(MultipartFile file) {

		List<DsaExport> dsaExports = new ArrayList<>();
		String errorMsg = "";
		CommonResponse commonResponse = new CommonResponse();
		int count = 0;

		try {

			InputStream inputStream = file.getInputStream();
			ZipSecureFile.setMinInflateRatio(0);                //for zip bomb detected
			Workbook workbook = WorkbookFactory.create(inputStream);
			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			Boolean fileFormat = true;
			Row headerRow = rowIterator.next();

				while (rowIterator.hasNext()) {
					count++;
					Row row = rowIterator.next();
					DsaExport dsaExport = new DsaExport();
					String applicationNo = null;

					for (int i = 0; i < 13; i++) {
						Cell cell = row.getCell(i);

						errorMsg = (cell == null || cell.getCellType() == CellType.BLANK) ? "file upload error due to row no " + (row.getRowNum() + 1) + " is empty" : "";

						if (errorMsg.isEmpty()) {
							System.out.println("value=" + row.getRowNum());

							switch (i) {

								case 1:
									dsaExport.setApplicationNo(row.getCell(1).toString());
									break;
								case 2:
									dsaExport.setProduct(row.getCell(2).toString());
									break;
								case 3:
									dsaExport.setDisbursalDate(Date.valueOf(dateFormatUtilty.changeDateFormate((row.getCell(3).toString()))));
									break;
								case 4:
									dsaExport.setProperty_address(row.getCell(4).toString());
									break;
								case 5:
									dsaExport.setPropertyPincode(row.getCell(5).toString().replace(".0", ""));
									break;
								case 6:
									dsaExport.setRegion(row.getCell(6).toString());
									break;
								case 7:
									dsaExport.setZone(row.getCell(7).toString());
									break;
								case 8:
									dsaExport.setLocation(row.getCell(8).toString());
									break;
								case 9:
									dsaExport.setRate_per_sqft(row.getCell(9).toString().replace(".0", ""));

									break;
								case 10:
									dsaExport.setProperty_type(row.getCell(10).toString());
									break;
								case 11:
									dsaExport.setLattitude(row.getCell(11).toString());
									break;
								case 12:
									dsaExport.setLongitude(row.getCell(12).toString());
									break;
							}

							if(errorMsg.isEmpty()) {
								*//*for (DsaExport fileRow : dsaExports) {
									System.out.println(applicationNo);

									if (fileRow.getApplicationNo().equals(applicationNo)) {
										errorMsg = "application number " + applicationNo + " duplicate in uploaded file.";
										System.out.println("error: duplicate application no in uploaded file");
										break;
									}
								}*//*
							}
						}
						if (!errorMsg.isEmpty()) break;
					}
					if (!errorMsg.isEmpty()) break;
					dsaExports.add(dsaExport);

				}


		} catch (Exception e) {
			System.out.println(e);
			errorMsg = "file is empty or technical issue.";
		}

		if (errorMsg.isEmpty() && count > 0) {
			dsaExportRepository.saveAll(dsaExports);
			commonResponse.setCode("0000");
			commonResponse.setMsg("file uploaded successfully " + dsaExports.size() + " row uploaded.");
		} else {
			if (errorMsg.isEmpty()) {
				errorMsg = "file is empty or technical issue";
				System.out.println(errorMsg);
				commonResponse.setCode("1111");
				commonResponse.setMsg(errorMsg);
			} else {
				System.out.println(errorMsg);
				commonResponse.setCode("1111");
				commonResponse.setMsg(errorMsg);
			}
		}

		return commonResponse;
	}*/




//	@Override
//	public CommonResponse uploadexcel(MultipartFile file) {
//		List<Student> studentList = new ArrayList<>();
//		List<StudentModel> studentModels = new ArrayList<>();
//		CommonResponse commonResponse=new CommonResponse();
//		int count = 0;
////        String errorMsg = "";
//
//		try  {
//			InputStream inputStream = file.getInputStream();
//			Workbook workbook = WorkbookFactory.create(inputStream);
//			Sheet sheet = workbook.getSheetAt(0);
//			Iterator<Row> rowIterator = sheet.iterator();
//			Boolean fileFormat = true;
//			Row headerRow = rowIterator.next();
//
//			while (rowIterator.hasNext()) {
//				count++;
//				Row row = rowIterator.next();
//				StudentModel studentModel = new StudentModel();
//
//				for (int i = 0; i < 4; i++) {
//					Cell cell = row.getCell(i);
//					if (cell == null || cell.getCellType() == CellType.BLANK) {
//						switch (i) {
//							case 1:
//								studentModel.setFirstName(row.getCell(0).toString());
//								break;
//							case 2:
//								studentModel.setLastName(row.getCell(1).toString());
//								break;
//							case 3:
//								studentModel.setEmail(row.getCell(2).toString());
//								break;
//
//						}
//					} else {
//						commonResponse.setCode("1111");
//						commonResponse.setMsg("file upload error due to row no " + (row.getRowNum() + 1) + " is empty:");
//					}
//
//				}
//				studentModels.add(studentModel);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println(e);
//		}
//		System.out.println(studentModels);
//		return commonResponse;
//	}






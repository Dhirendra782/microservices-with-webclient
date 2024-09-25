package com.employeeservice.service.impl;

import com.employeeservice.dto.ApiResponseDto;
import com.employeeservice.dto.DepartmentDto;
import com.employeeservice.dto.EmployeeDto;
import com.employeeservice.mapper.EmployeeMapper;
import com.employeeservice.model.Employee;
import com.employeeservice.repository.EmployeeRepository;
import com.employeeservice.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

   // private RestTemplate restTemplate;

    private WebClient webClient;

    //create employee
    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        Employee employee = EmployeeMapper.mapToEntity(employeeDto);
        Employee saveEmployee = employeeRepository.save(employee);

        return EmployeeMapper.mapToEmployeeDto(saveEmployee);
    }

    //get employee by id
    @Override
    //public EmployeeDto getEmployeeById(Long employeeId) {
    public ApiResponseDto getEmployeeById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).get();

        /*
       ResponseEntity<DepartmentDto> responseEntity =
               restTemplate.getForEntity("http://localhost:8082/api/departments/"
                +employee.getDepartmentCode(), DepartmentDto.class);

       DepartmentDto departmentDto = responseEntity.getBody();
        */

        //Using webclient
       DepartmentDto departmentDto = webClient.get()
                .uri("http://localhost:8082/api/departments/"+employee.getDepartmentCode())
                .retrieve()
                .bodyToMono(DepartmentDto.class)
                .block();


        EmployeeDto employeeDto = new EmployeeDto(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getDepartmentCode()
        );

        ApiResponseDto apiResponseDto = new ApiResponseDto();
        apiResponseDto.setEmployee(employeeDto);
        apiResponseDto.setDepartment(departmentDto);

        return apiResponseDto;
    }

}













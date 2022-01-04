package com.example.user.service.serviceImpl;

import com.example.user.VO.Department;
import com.example.user.VO.ResponseTemplateVO;
import com.example.user.entity.Users;
import com.example.user.repository.UsersRepository;
import com.example.user.service.UsersService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final RestTemplate restTemplate;
    private final WebClient.Builder webClient;

//    private final WebClient webClient = WebClient.builder().baseUrl("http://localhost:9001/departments").build();


    private static final String DEPARTMENT_SERVICE = "DEPARTMENT-SERVICE";

    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository, RestTemplate restTemplate, WebClient.Builder webClient) {
        this.usersRepository = usersRepository;
        this.restTemplate = restTemplate;
//        this.webClient = webClient;
        this.webClient = webClient;
    }

    @Override
    public Users saveUser(Users user) {
        log.info("Inside Save user Service");
        return usersRepository.save(user);
    }

    @Override
    @CircuitBreaker(name = DEPARTMENT_SERVICE, fallbackMethod = "userFallback")
    public ResponseEntity<?> getUserWithDepartment(Long userId) {
        log.info("Inside ResponseTemplateVO users Service");
        ResponseTemplateVO vo = new ResponseTemplateVO();
        Users user = usersRepository.findByUserId(userId);
//        Object response = restTemplate.getForObject("http://API-GATEWAY/departments/" +
//                user.getDepartmentId(), Object.class);

        Department department = restTemplate.getForObject("http://DEPARTMENT-SERVICE/departments/" +
                user.getDepartmentId(), Department.class);

        vo.setUsers(user);
        vo.setDepartment(department);
        return new ResponseEntity<>(vo, HttpStatus.OK);
    }

    public ResponseEntity<?> userFallback(Exception e){
        return new ResponseEntity<>("Department Service is down", HttpStatus.OK);
    }

    @CircuitBreaker(name = DEPARTMENT_SERVICE, fallbackMethod = "userFallback")
    public ResponseEntity<?> getUserDepartmentWithWebClient(Long userId){
        log.info("Inside webclient in ResponseTemplateVO users Service");
        ResponseTemplateVO voClient = new ResponseTemplateVO();
        Users user = usersRepository.findByUserId(userId);
        Mono<Object> departmentMono = webClient.build().get()
                .uri("http://DEPARTMENT-SERVICE/departments/{userId}", userId)
                .retrieve().bodyToMono(Object.class);

        log.info("Finish Query from Department");
        voClient.setUsers(user);
        voClient.setDepartmentWebclient(departmentMono.block());
        return new ResponseEntity<>(voClient, HttpStatus.OK);
    }


}

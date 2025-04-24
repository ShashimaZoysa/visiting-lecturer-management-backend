package com.visitinglecturer.vlms_backend;

import com.visitinglecturer.vlms_backend.entity.Role;
import com.visitinglecturer.vlms_backend.repository.RoleRepository;
import com.visitinglecturer.vlms_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VlmsBackendApplication {



	public static void main(String[] args) {
		SpringApplication.run(VlmsBackendApplication.class, args);
	}


}

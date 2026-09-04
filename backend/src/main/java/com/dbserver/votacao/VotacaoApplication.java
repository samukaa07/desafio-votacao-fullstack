package com.dbserver.votacao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Ponto de entrada da aplicacao. Nada de especial aqui, so sobe o Spring mesmo.
 */
@SpringBootApplication
@EnableScheduling
public class VotacaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(VotacaoApplication.class, args);
    }

}


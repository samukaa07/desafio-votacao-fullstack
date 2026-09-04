package com.dbserver.votacao.repository;

import com.dbserver.votacao.enums.OpcaoVoto;
import com.dbserver.votacao.model.Voto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VotoRepository extends JpaRepository<Voto, Long> {

    boolean existsBySessaoIdAndCpfAssociado(Long sessaoId, String cpfAssociado);

    long countBySessaoIdAndOpcao(Long sessaoId, OpcaoVoto opcao);

    List<Voto> findBySessaoId(Long sessaoId);

}


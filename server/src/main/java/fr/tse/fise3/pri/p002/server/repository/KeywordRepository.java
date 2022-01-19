package fr.tse.fise3.pri.p002.server.repository;

import fr.tse.fise3.pri.p002.server.model.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.Optional;

public interface KeywordRepository extends JpaRepository<Keyword, BigInteger> {

	Optional<Keyword> findByKeywordName(String name);

}

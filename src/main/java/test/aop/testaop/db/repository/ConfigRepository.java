package test.aop.testaop.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import test.aop.testaop.db.entity.Config;

import java.util.Optional;

@Repository
public interface ConfigRepository extends JpaRepository<Config, Long>, JpaSpecificationExecutor<Config> {
    //
}

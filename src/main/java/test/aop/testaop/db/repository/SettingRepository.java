package test.aop.testaop.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import test.aop.testaop.db.entity.Setting;

@Repository
public interface SettingRepository extends JpaRepository<Setting, Long> {
    //
}
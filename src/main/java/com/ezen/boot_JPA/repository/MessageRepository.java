package com.ezen.boot_JPA.repository;

import com.ezen.boot_JPA.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByReceiver(String name);
}

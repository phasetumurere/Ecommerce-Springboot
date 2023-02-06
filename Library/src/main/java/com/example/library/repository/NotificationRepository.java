package com.example.library.repository;

import com.example.library.model.Notification;
import com.example.library.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query(value = "insert into notification (name) values('new Order')", nativeQuery = true)
//    @Query(value = "select * from products p where p.is_activated = true and p.is_deleted = false order by random() asc limit 4", nativeQuery = true)

    Notification orderNotification();
}

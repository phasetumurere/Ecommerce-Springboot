package com.example.library.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id", nullable = false)
    private Long id;
    private String name;
    private String clientEmail;
    //    @OneToMany(cascade = CascadeType.ALL, mappedBy = "notification")
//    private List<Order> productInformation;
    private String comment;
}

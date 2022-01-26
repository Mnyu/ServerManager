package com.abhimanyutech.servermanager.model;

import com.abhimanyutech.servermanager.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Server {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    @NotEmpty(message = "IP Address cannot be empty or null")
    private String ipAddress;

    private String name;

    private String memory;

    private String type;

    private String imageUrl;

    private Status status;
}

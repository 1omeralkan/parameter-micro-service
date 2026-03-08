package com.omeralkan.parameter.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "error_messages")
public class ErrorMessageEntity extends BaseEntity {

    @Column(name = "error_code", nullable = false, length = 50)
    private String errorCode;

    @Column(name = "language", nullable = false, length = 10)
    private String language;

    @Column(name = "message", nullable = false, length = 500)
    private String message;
}
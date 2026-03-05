package com.omeralkan.parameter.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.time.LocalDateTime;

@MappedSuperclass // JPA'ya "Bu bir tablo değil, bu bir miras şablonudur" deriz.
@Getter
@Setter
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version // Optimistic Locking'in Java'daki karşılığı. Çakışmaları engeller.
    private Long version;

    @JdbcTypeCode(Types.TINYINT)
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", nullable = false, updatable = false)
    private String createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private String updatedBy;

    // JPA Callback Metodları: Veri kaydedilmeden/güncellenmeden hemen önce otomatik çalışır.
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.createdBy = "system"; // İleride buraya login olan kullanıcının ID'si gelecek.
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = "system";
    }
}
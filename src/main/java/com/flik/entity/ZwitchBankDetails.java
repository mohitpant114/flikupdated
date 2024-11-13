package com.flik.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "zwitch_bank_details")
@Data
public class ZwitchBankDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 10, nullable = false)
    private Long id;
    @Column(name = "customerId", length = 10)
    private Long customerId;
    private String object;//
    private String bank_account_number;//
    private String bank_ifsc_code;//
    private String name_as_per_bank;//
    private Boolean force_penny_drop;
    private Double force_penny_drop_amount;//
    private String status;//
    private String message;//
    private String last_verified_at;
    private String merchant_reference_id;
    private Double created_at;//
    private Boolean is_sandbox;//
}

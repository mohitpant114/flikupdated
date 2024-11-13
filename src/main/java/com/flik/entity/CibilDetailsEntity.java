package com.flik.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
@Entity
public class CibilDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 10, nullable = false)
    private Long id;
    private String totalNumberOfEnquiry;
    private String openDisbursedLoan;
    private String totalOverDueAmount;
    private String totalOverDueAccount;
    private String writtenOfSettled;
    private String writtenOfTotal;
    private String writtenOfPrincipal;
    private String currentAddress;
    private String permanentAddress;





}

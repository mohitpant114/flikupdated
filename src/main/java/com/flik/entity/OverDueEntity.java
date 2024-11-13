package com.flik.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "overdue")
@Data
public class OverDueEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 10, nullable = false)

    private Long id;
    private Long loanId;
    private Long repaymentId;
    private String partnerLoanId;
    private String borrowerName;
    private String mobileNumber;
    private LocalDate disbursedDate;
    private LocalDate dueDate;
    private String dueEmiCount;
    private String overdueAmount;
    private String bounceCharge;
    private String penalCharge;
    private Double totalPendingAmount;


    private String status; //paid & pending

}

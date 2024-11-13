package com.flik.respons;

import com.flik.entity.RepaymentEntity;
import lombok.*;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class DueEmiResponse {
    private long count;
    private List<RepaymentEntity> dueEmis;
}

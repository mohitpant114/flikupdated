package com.flik.respons;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private boolean authenticated;
    private String message;
}

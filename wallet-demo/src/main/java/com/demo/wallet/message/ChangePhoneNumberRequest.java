package com.demo.wallet.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChangePhoneNumberRequest {
    private Long version;
    private String phoneNumber;
}

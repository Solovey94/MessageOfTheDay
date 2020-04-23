package com.solovey.messageoftheday.dto;

import lombok.*;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CustomerRequestDto {

        private String username;

        private String password;

}

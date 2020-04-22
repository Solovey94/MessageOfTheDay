package com.solovey.messageoftheday.dto;

import com.solovey.messageoftheday.model.Role;
import lombok.*;

import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CustomerResponseDto {
    private String username;

    private List<Role> roles;
}

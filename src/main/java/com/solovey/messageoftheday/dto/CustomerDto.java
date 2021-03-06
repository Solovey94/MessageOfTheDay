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
public class CustomerDto {

    private Long id;

    private String username;

    private String password;

    private List<Role> roles;

}

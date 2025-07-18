package com.tugra.dto;

import com.tugra.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtoUsers {

    private String username;

    private String name;

    private String password;

    private Role role;

}

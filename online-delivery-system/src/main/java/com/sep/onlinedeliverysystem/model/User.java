package com.sep.onlinedeliverysystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log
public class User {
    private Long id;
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private String role;
}

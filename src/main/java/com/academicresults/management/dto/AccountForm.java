package com.academicresults.management.dto;

import com.academicresults.management.Entity.enums.RoleCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountForm {

    private Long id;

    private String username;

    private String password;

    private RoleCode roleCode;

    private Boolean enabled = true;

    private Long studentId;

    private Long employeeId;

    private String employeeCode;

    private String fullName;

    private String email;

    private String phone;

    private String position;
}

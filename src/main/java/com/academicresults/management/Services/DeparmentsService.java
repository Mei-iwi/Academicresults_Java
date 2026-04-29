package com.academicresults.management.Services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.academicresults.management.Entity.Department;
import com.academicresults.management.Repository.DeparmantsRepository;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@AllArgsConstructor
public class DeparmentsService {
    @Autowired
    private DeparmantsRepository deparmantsRepository;

    public List<Department> getAllDepartments() {
        List<Department> departments = deparmantsRepository.findAll();
        return departments;
    }

}

package com.academicresults.management.Services;

import com.academicresults.management.Entity.StudentClass;
import com.academicresults.management.Repository.ClassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClassServices
{
    private final ClassRepository classRepository;

    public List<StudentClass> getAllClass()
    {
        return classRepository.findAll();
    }
}

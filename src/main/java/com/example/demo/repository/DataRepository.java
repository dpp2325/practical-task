package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;
import com.example.demo.dataobject.FileData;

public interface DataRepository extends CrudRepository<FileData, String>  {

}

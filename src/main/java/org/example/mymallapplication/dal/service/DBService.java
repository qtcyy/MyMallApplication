package org.example.mymallapplication.dal.service;

import java.util.List;

public interface DBService {
    List<String> getRoles(Long userId);

    List<String> getPermissions(Long userId);
}

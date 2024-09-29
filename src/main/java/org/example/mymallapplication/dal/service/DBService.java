package org.example.mymallapplication.dal.service;

import java.util.List;

public interface DBService {
    List<String> getRoles(String userId);

    List<String> getPermissions(String userId);
}

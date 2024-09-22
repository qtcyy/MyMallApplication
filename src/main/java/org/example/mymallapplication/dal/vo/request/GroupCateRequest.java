package org.example.mymallapplication.dal.vo.request;

import lombok.Data;

import java.util.List;

@Data
public class GroupCateRequest {
    Long cateId;
    List<Long> productIds;
}

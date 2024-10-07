package org.example.mymallapplication.dal.vo.response;

import lombok.Getter;
import lombok.Setter;
import org.example.mymallapplication.dal.dao.entity.person.FrontendUsers;

/**
 * @author chengyiyang
 */
@Getter
@Setter
public class GetUserInfoResponse extends FrontendUsers {
    private double balance;
}

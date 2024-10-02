package org.example.mymallapplication.dal.vo.response;

import lombok.Getter;
import lombok.Setter;
import org.example.mymallapplication.dal.dao.entity.product.Products;

/**
 * @author chengyiyang
 */
@Getter
@Setter
public class GetCartResponse extends Products {
    private Integer quantity;
}

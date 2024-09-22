package org.example.mymallapplication.dal.vo.response;

import lombok.Data;
import org.example.mymallapplication.dal.dao.entity.product.Category;
import org.example.mymallapplication.dal.dao.entity.product.Products;

import java.util.List;

@Data
public class GetCateResponse {
    List<Category> categories;
    List<Products> products;

    public GetCateResponse(List<Category> categories, List<Products> products) {
        this.categories = categories;
        this.products = products;
    }
}

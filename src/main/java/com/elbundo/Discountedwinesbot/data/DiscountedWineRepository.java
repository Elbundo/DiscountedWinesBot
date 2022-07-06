package com.elbundo.Discountedwinesbot.data;

import com.elbundo.Discountedwinesbot.data.Wine;

import java.util.List;

public interface DiscountedWineRepository {
    List<Wine> findAll();
    List<Wine> findAllBySite(String site);
    boolean contains(Wine wine);
    boolean delete(Wine wine);
    Wine save(Wine wine);
}

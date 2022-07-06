package com.elbundo.Discountedwinesbot.data;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcDiscountedWineRepository implements DiscountedWineRepository{
    private JdbcTemplate jdbcTemplate;

    public JdbcDiscountedWineRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Wine> findAll() {
        return jdbcTemplate.query("select " +
                "site, name, alias, price, priceWithDiscount, discount, image " +
                "from Discounted_Wines", this::mapRowToWine);
    }

    @Override
    public List<Wine> findAllBySite(String site) {
        return jdbcTemplate.query("select " +
                "site, name, alias, price, priceWithDiscount, discount, image " +
                "from Discounted_Wines " +
                "where site = ?", this::mapRowToWine, site);
    }

    @Override
    public boolean contains(Wine wine) {
        return findAll().contains(wine);
    }

    @Override
    public boolean delete(Wine wine) {
        return jdbcTemplate.update("delete from Discounted_Wines where " +
                "site = ? and " +
                "name = ? and " +
                "alias = ? and " +
                "price = ? and " +
                "priceWithDiscount = ? and " +
                "discount = ? and " +
                "image = ?",
                wine.getSite(),
                wine.getName(),
                wine.getAlias(),
                wine.getPrice(),
                wine.getPriceWithDiscount(),
                wine.getDiscount(),
                wine.getImage()) == 1;
    }

    @Override
    public Wine save(Wine wine) {
        jdbcTemplate.update(
                "insert into Discounted_Wines " +
                    "(site, name, alias, price, priceWithDiscount, discount, image) " +
                    "values (?, ?, ?, ?, ?, ?, ?)",
                wine.getSite(),
                wine.getName(),
                wine.getAlias(),
                wine.getPrice(),
                wine.getPriceWithDiscount(),
                wine.getDiscount(),
                wine.getImage()
        );
        return wine;
    }

    private Wine mapRowToWine(ResultSet row, int rowNum) throws SQLException {
        return new Wine(
                row.getString("site"),
                row.getString("name"),
                row.getString("alias"),
                row.getDouble("price"),
                row.getDouble("priceWithDiscount"),
                row.getDouble("discount"),
                row.getString("image")
        );
    }
}

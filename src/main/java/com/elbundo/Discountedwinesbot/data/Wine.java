package com.elbundo.Discountedwinesbot.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Wine {
    private String site;
    private String name;
    private String alias;
    private double price;
    private double priceWithDiscount;
    private double discount;
    private String image;
}

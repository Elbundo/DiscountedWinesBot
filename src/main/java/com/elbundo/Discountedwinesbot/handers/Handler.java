package com.elbundo.Discountedwinesbot.handers;

import com.elbundo.Discountedwinesbot.data.Wine;
import org.springframework.stereotype.Component;

import java.util.List;

public interface Handler {
    List<Wine> handle();
    String getSite();
}

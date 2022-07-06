package com.elbundo.Discountedwinesbot.controllers;

import com.elbundo.Discountedwinesbot.data.Wine;
import com.elbundo.Discountedwinesbot.handers.Handler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wines")
public class DiscountedWinesController {
    private final List<Handler> handlers = new ArrayList<>();

    public DiscountedWinesController(Handler... list) {
        handlers.addAll(Arrays.asList(list));
    }

    @GetMapping("/discounted")
    public List<Wine> discountedWines(){
        List<Wine> result = new ArrayList<>();
        for(Handler handler : handlers){
            List<Wine> allDiscountWines = handler.handle();
            //List<Wine> check = checked.get(entry.getKey());
            //Удалить из списка проверенных вина, скидка на которые закончилась
            //check.removeIf(w -> !allDiscountWines.contains(w));
            //Удаляем из списка скидочных вин все вина, которые есть в списке checked для этого сайта
            //allDiscountWines.removeIf(check::contains);
            //Добавляем все скидочные вина в список проверенных для этого сайта
            //check.addAll(allDiscountWines);
            result.addAll(handler.handle());
        }
        return result;
    }
}
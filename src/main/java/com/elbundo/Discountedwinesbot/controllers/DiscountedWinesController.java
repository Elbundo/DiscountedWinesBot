package com.elbundo.Discountedwinesbot.controllers;

import com.elbundo.Discountedwinesbot.data.JdbcDiscountedWineRepository;
import com.elbundo.Discountedwinesbot.data.Wine;
import com.elbundo.Discountedwinesbot.handers.Handler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api/wines")
public class DiscountedWinesController {
    private final List<Handler> handlers = new ArrayList<>();
    private final JdbcDiscountedWineRepository repository;

    public DiscountedWinesController(JdbcDiscountedWineRepository repository, Handler... list) {
        this.repository = repository;
        handlers.addAll(Arrays.asList(list));
    }

    @GetMapping("/discounted")
    public List<Wine> discountedWines(){
        List<Wine> result = new ArrayList<>();
        for(Handler handler : handlers){
            List<Wine> allDiscountWines = handler.handle();

            //Удалить из списка проверенных вина, скидка на которые закончилась
            List<Wine> storedWines = repository.findAllBySite(handler.getSite());
            List<Wine> storedWinesCopy = new ArrayList<>(storedWines);
            storedWines.removeIf(allDiscountWines::contains);
            storedWines.forEach(repository::delete);
            //Удаляем из списка скидочных вин все вина, которые есть в списке checked для этого сайта
            allDiscountWines.removeIf(storedWinesCopy::contains);
            //Добавляем все скидочные вина в список проверенных для этого сайта
            allDiscountWines.forEach(repository::save);

            result.addAll(allDiscountWines);
        }
        return result;
    }
}
package org.redmath.news;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@Slf4j
@RequestMapping("/api/news")
public class NewsController {
    private final NewsService service;

    public NewsController(NewsService service){
        this.service=service;
    }

    @GetMapping
    public List<News> GetAllNews(){
        return service.findAll();

    }
    @GetMapping("/{id}")
    public News FindById(@PathVariable Long id){
        return service.findNewsById(id);
    }
    @DeleteMapping("/{id}")
    public Boolean DeleteNews(@PathVariable Long id){
        return service.DeleteNewsById(id);
    }

    @GetMapping("/search")
    public Page<News> Find(@RequestParam String title, @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "2") int size){
        Pageable pageable= PageRequest.of(page,size);
        return service.find(title,pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public News addNews(@RequestBody News news){
        return service.create(news);
    }



    @PatchMapping("/{id}")
    public News updateNews(@PathVariable Long id, @RequestBody News news) {
        return service.updateNews(id,news);

    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String,String> handleNoSuchElementException(NoSuchElementException ex){
        Map <String,String> errorResponse=new HashMap<>();
        errorResponse.put("error", "Resource not found");
        errorResponse.put("message", ex.getMessage());
        return errorResponse;
    }


}

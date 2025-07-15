package org.redmath.news;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
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
    public News create(@RequestBody News news){
        System.out.println(news);


        News fetched = null;
        if (news.getId()!=null){
            fetched= service.findNewsById(news.getId());
        }


      if(fetched==null){

          return service.create(news);
      }
      else{
          fetched.setAuthor(news.getAuthor());
          fetched.setContent(news.getContent());
          fetched.setTitle(news.getTitle());
          return service.create(fetched);
      }



    }
}

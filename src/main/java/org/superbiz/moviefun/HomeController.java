package org.superbiz.moviefun;


import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
//@RequestMapping("/")
public class HomeController {

    private MoviesBean moviesBean;

    public HomeController(MoviesBean moviesBean) {
        this.moviesBean = moviesBean;
    }


    @GetMapping("/")
    public String getIndex() {
        return "index";
    }

    @RequestMapping("/setup")
//    @Transactional
    public ModelAndView getSetup(ModelAndView modelAndView) {

        moviesBean.addMovie(new Movie("Wedding Crashers", "David Dobkin", "Comedy", 7, 2005));
        moviesBean.addMovie(new Movie("Starsky & Hutch", "Todd Phillips", "Action", 6, 2004));
        moviesBean.addMovie(new Movie("Shanghai Knights", "David Dobkin", "Action", 6, 2003));
        moviesBean.addMovie(new Movie("I-Spy", "Betty Thomas", "Adventure", 5, 2002));
        moviesBean.addMovie(new Movie("The Royal Tenenbaums", "Wes Anderson", "Comedy", 8, 2001));
        moviesBean.addMovie(new Movie("Zoolander", "Ben Stiller", "Comedy", 6, 2001));
        moviesBean.addMovie(new Movie("Shanghai Noon", "Tom Dey", "Comedy", 7, 2000));

        modelAndView.addObject("movies",this.moviesBean.getMovies());
        modelAndView.setViewName("setup");
        return modelAndView;
    }
}

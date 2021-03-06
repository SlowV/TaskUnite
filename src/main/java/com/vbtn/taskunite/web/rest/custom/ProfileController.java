package com.vbtn.taskunite.web.rest.custom;

import com.vbtn.taskunite.domain.Authority;
import com.vbtn.taskunite.domain.User;
import com.vbtn.taskunite.repository.AuthorityRepository;
import com.vbtn.taskunite.security.AuthoritiesConstants;
import com.vbtn.taskunite.service.PaymentInformationService;
import com.vbtn.taskunite.service.StatisticService;
import com.vbtn.taskunite.service.UserInformationService;
import com.vbtn.taskunite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    UserService userService;
    @Autowired
    UserInformationService userInformationService;
    @Autowired
    PaymentInformationService paymentInformationService;
    @Autowired
    StatisticService statisticService;
    @Autowired
    AuthorityRepository authorityRepository;

    @GetMapping
    public String profile(Model model) {
        final Optional<User> user = userService.getUserWithAuthorities();
        if (user.isPresent()) {
            model.addAttribute("authorities", user.get().getAuthorities());
            model.addAttribute("user", user.get());
            model.addAttribute("userInformation", userInformationService.findOne(user.get().getId()).get());
            model.addAttribute("payment", paymentInformationService.findOne(user.get().getId()).get());
            model.addAttribute("statistic", statisticService.findOne(user.get().getId()).get());
            return "profile/profile";
        }
        return "redirect:/";
    }
}

package com.example.md4_minitest1.controller;

import com.example.md4_minitest1.model.Car;
import com.example.md4_minitest1.model.CarForm;
import com.example.md4_minitest1.service.HibernateCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/car")
public class CarController {

    @Value("${upload}")
    private String upload;

    @Autowired
    private HibernateCarService hibernateCarService;

    @GetMapping()
    public String showData(Model model) {
        List<Car> cars = hibernateCarService.findAll();
        model.addAttribute("car",cars);
        return "index";
    }
    @GetMapping("/create")
    public String showFormCreate(Model model) {
        model.addAttribute("car",new CarForm());
        return "create";
    }
    @PostMapping("/save")
    public String createNewCar(CarForm carForm) throws IOException {
        MultipartFile file = carForm.getAvatar();
        String nameImg = file.getOriginalFilename();
        FileCopyUtils.copy(file.getBytes(),new File(upload+nameImg));
        Car car = new Car();
        car.setCode(carForm.getCode());
        car.setName(carForm.getName());
        car.setProducer(carForm.getProducer());
        car.setPrice(carForm.getPrice());
        car.setAvatar(nameImg);
        hibernateCarService.save(car);
        return "redirect:/car";
    }
    @GetMapping("{id}/edit")
    public String showEditForm(@PathVariable int id, Model model) {
        model.addAttribute("car",hibernateCarService.findByID(id));
        return "update";
    }

    @PostMapping("/update")
    public String editCar(Car car) {
        hibernateCarService.save(car);
        return "redirect:/car";
    }
    @GetMapping("{id}/delete")
    public String deleteCar(@PathVariable int id) {
        hibernateCarService.remove(id);
        return "redirect:/car";
    }
}
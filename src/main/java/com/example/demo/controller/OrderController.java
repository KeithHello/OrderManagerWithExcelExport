package com.example.demo.controller;

import com.example.demo.model.Order;
import com.example.demo.repository.OrderRepository;
import com.example.demo.util.ExcelGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("orders", orderRepository.findAll());
        return "index";
    }

    @GetMapping("/order/register")
    public String register(Model model) {
        return "register";
    }

    @GetMapping("/upload")
    public String upload(Model model) {
        model.addAttribute("orders", orderRepository.findAll());
        return "upload/upload";
    }

    @GetMapping("/order/detail/{id}")
    public String detail(Model model, @PathVariable Long id) {
        model.addAttribute("order", orderRepository.findById(id).get());
        return "detail";
    }

    @GetMapping("/order/edit/{id}")
    public String edit(Model model, @PathVariable Long id) {
        model.addAttribute("order", orderRepository.findById(id).get());
        return "edit";
    }

    @PostMapping("/order")
    public String addOrder(@RequestParam String customerName, @RequestParam String productName, @RequestParam Integer price) {
        Order order = new Order();
        order.setCustomerName(customerName);
        order.setProductName(productName);
        order.setPrice(price);
        orderRepository.save(order);
        return "redirect:/";
    }

    @PostMapping("/order/update/{id}")
    public String editOrder(@PathVariable Long id, @RequestParam String customerName, @RequestParam String productName, @RequestParam Integer price) {
        Order order = orderRepository.findById(id).get();
        order.setCustomerName(customerName);
        order.setProductName(productName);
        order.setPrice(price);
        orderRepository.save(order);
        return "redirect:/";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        List<Order> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Order order = new Order();
                order.setCustomerName(values[0]);
                order.setProductName(values[1]);
                order.setPrice(Integer.parseInt(values[2]));
                records.add(order);
            }

            orderRepository.saveAll(records);

            model.addAttribute("message", "File uploaded successfully!");
            model.addAttribute("records", records);
        } catch (Exception e) {
            model.addAttribute("message", "An error occurred while uploading the file.");
        }

        return "upload/result";
    }

    @GetMapping("/order/generate-excel")
    public ResponseEntity<ByteArrayResource> generateExcel() {
        Iterable<Order> orders = orderRepository.findAll();
        ByteArrayResource result = ExcelGenerator.createExcel(orders);

        // HTTPヘッダーを設定
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "data_export.xlsx");

        // excelファイルを返す
        return ResponseEntity.ok()
                .headers(headers)
                .body(result);
    }
}
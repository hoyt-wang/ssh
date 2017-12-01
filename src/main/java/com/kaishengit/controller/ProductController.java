package com.kaishengit.controller;

import com.kaishengit.pojo.Product;
import com.kaishengit.service.ProductService;
import com.kaishengit.util.Page;
import com.kaishengit.util.RequestQuery;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by hoyt on 2017/11/29.
 */

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * 商品清单
     * @param model
     * @return
     */
    @GetMapping
    public String home(Model model, HttpServletRequest request
                        , @RequestParam(required = false,name = "p",defaultValue= "1") Integer pageNo) {
        List<RequestQuery> requestQueryList = RequestQuery.requestQueryBuilder(request);
        Page<Product> productList = productService.findByRequestQuery(requestQueryList,pageNo);
        model.addAttribute("page",productList);
        return "list";
    }

    /**
     * 添加商品
     * @return
     */
    @GetMapping("/new")
    public String saveProduct() {
        return "new";
    }

    @PostMapping("/new")
    public String saveProduct(Product product) {
        productService.saveProduct(product);
        return "redirect:/product";
    }

    @GetMapping("/{id:\\d+}")
    public String showProduct(@PathVariable Integer id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product",product);
        return "show";
    }

    @GetMapping("/{id:\\d+}/edit")
    public String editProduct(@PathVariable Integer id, Model model) {
        model.addAttribute("product",productService.findById(id));
        return "edit";
    }

    @PostMapping("/{id:\\d+}/edit")
    public String editProduct(Product product, RedirectAttributes redirectAttributes) {
        productService.editProduct(product);
        redirectAttributes.addFlashAttribute("message","编辑成功");
        return "redirect:/product/" + product.getId();
    }

    @GetMapping("/{id:\\d+}/delete")
    public String delProduct(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        productService.delete(id);
        redirectAttributes.addFlashAttribute("message","删除成功");
        return "redirect:/product";
    }

}

package br.com.springboot.controller;

import br.com.springboot.model.Fornecedor;
import br.com.springboot.services.FornecedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
@RequestMapping("/fornecedores")
public class FornecedorController {

    private final FornecedorService fornecedorService;
    @RequestMapping(value = "/novo",method= RequestMethod.GET)
    public ModelAndView novo(ModelMap model) {
        model.addAttribute("fornecedor", new Fornecedor());
        return new ModelAndView("/fornecedor/formulario", model);
    }

    @RequestMapping(value = "",method= RequestMethod.POST)
    public String salva(@Valid @ModelAttribute Fornecedor fornecedor, BindingResult result, RedirectAttributes attr) {
        if (result.hasErrors()) {
            return "/fornecedor/formulario";
        }
        if (fornecedor.getId() == null) {
                fornecedorService.insere(fornecedor);
                attr.addFlashAttribute("feedback", "Fornecedor Cadastrado com Sucesso");
        }
        else {
            fornecedorService.atualiza(fornecedor);
            attr.addFlashAttribute("feedback", "Fornecedor Atualizado com Sucesso");
        }
        return "redirect:/fornecedores";

    }

    @RequestMapping(value = "",method= RequestMethod.GET)
    public ModelAndView lista(ModelMap model) {
        model.addAttribute("fornecedores", fornecedorService.lista());
        return new ModelAndView("/fornecedor/lista", model);
    }
    @RequestMapping(value = "/edita/{id}", method = RequestMethod.GET)
    public ModelAndView edita(@PathVariable("id") Long id, ModelMap model) {
        try {
            model.addAttribute("fornecedor", fornecedorService.pesquisaPeloId(id));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("/fornecedor/formulario", model);
    }

    @RequestMapping(value = "/inativa/{id}", method = RequestMethod.GET)
    public String inativa(@PathVariable("id") Long id, RedirectAttributes attr) {
        System.out.println(id);
        try{
            Fornecedor fornecedor = fornecedorService.pesquisaPeloId(id);
            fornecedorService.inativa(fornecedor);
            attr.addFlashAttribute("feedback", "Fornecedor inativado com sucesso");
        }catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/fornecedors";
    }
    @RequestMapping(value = "/ativa/{id}", method = RequestMethod.GET)
    public String ativa(@PathVariable("id") Long id, RedirectAttributes attr) {
        System.out.println(id);
        try{
            Fornecedor fornecedor = fornecedorService.pesquisaPeloId(id);
            fornecedorService.ativa(fornecedor);
            attr.addFlashAttribute("feedback", "Fornecedor ativado com sucesso");
        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/fornecedors";
    }
}

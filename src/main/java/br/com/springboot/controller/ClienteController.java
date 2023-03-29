package br.com.springboot.controller;

import br.com.springboot.model.Cliente;
import br.com.springboot.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.net.http.HttpClient;

@Controller
@RequestMapping("/clientes")
public class ClienteController {
    @Autowired
    private ClienteService clienteService;
    @RequestMapping(value = "/novo",method= RequestMethod.GET)
    public ModelAndView novo(ModelMap model) {
        model.addAttribute("cliente", new Cliente());
        return new ModelAndView("/cliente/formulario", model);
    }

    @RequestMapping(value = "",method= RequestMethod.POST)
    public String salva(@Valid @ModelAttribute Cliente cliente, BindingResult result, RedirectAttributes attr) {
        if (result.hasErrors()) {
            return "/cliente/formulario";
        }
        if (cliente.getId() == null) {
                clienteService.insere(cliente);
                attr.addFlashAttribute("feedback", "Cliente Cadastrado com Sucesso");
        }
        else {
            clienteService.atualiza(cliente);
            attr.addFlashAttribute("feedback", "Cliente Atualizado com Sucesso");
        }
        return "redirect:/clientes";

    }

    @RequestMapping(value = "",method= RequestMethod.GET)
    public ModelAndView lista(ModelMap model) {
        model.addAttribute("clientes", clienteService.lista());
        return new ModelAndView("/cliente/lista", model);
    }
    @RequestMapping(value = "/edita/{id}", method = RequestMethod.GET)
    public ModelAndView edita(@PathVariable("id") Long id, ModelMap model) {
        try {
            model.addAttribute("cliente", clienteService.pesquisaPeloId(id));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("/cliente/formulario", model);
    }

    @RequestMapping(value = "/inativa/{id}", method = RequestMethod.GET)
    public String inativa(@PathVariable("id") Long id, RedirectAttributes attr) {
        System.out.println(id);
        try{
            Cliente cliente = clienteService.pesquisaPeloId(id);
            clienteService.inativa(cliente);
            attr.addFlashAttribute("feedback", "Cliente inativado com sucesso");
        }catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/clientes";
    }
    @RequestMapping(value = "/ativa/{id}", method = RequestMethod.GET)
    public String ativa(@PathVariable("id") Long id, RedirectAttributes attr) {
        System.out.println(id);
        try{
            Cliente cliente = clienteService.pesquisaPeloId(id);
            clienteService.ativa(cliente);
            attr.addFlashAttribute("feedback", "Cliente ativado com sucesso");
        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/clientes";
    }
}

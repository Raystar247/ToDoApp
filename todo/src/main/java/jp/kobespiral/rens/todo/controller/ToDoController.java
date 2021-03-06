package jp.kobespiral.rens.todo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.kobespiral.rens.todo.dto.LoginForm;
import jp.kobespiral.rens.todo.dto.ToDoForm;
import jp.kobespiral.rens.todo.entity.ToDo;
import jp.kobespiral.rens.todo.service.ToDoService;

@Controller
public class ToDoController {
    @Autowired
    ToDoService tdService;

    /**
     * ログインページ HTTP-GET /
     * 
     * @param model
     * @return
     */
    @GetMapping("/")
    String showLoginForm(Model model) {
        LoginForm form = new LoginForm();
        model.addAttribute("LoginForm", form);
        return "login";
    }

    /**
     * リダイレクトする
     * 
     * @param mid
     * @param model
     * @return
     */
    @GetMapping("/login")
    String redirectTodos(@RequestParam String mid, Model model) {
        return "redirect:/" + mid + "/todos";
    }

    /**
     * 自分のToDoを見るページ HTTP-GET
     * 
     * @param mid
     * @param model
     * @return
     */
    @GetMapping("/{mid}/todos")
    String showMembersTodos(@PathVariable String mid, Model model) {
        List<ToDo> todos = tdService.getToDoList(mid);
        model.addAttribute("Todos", todos);
        List<ToDo> dones = tdService.getDoneList(mid);
        model.addAttribute("Dones", dones);
        ToDoForm form = new ToDoForm();
        model.addAttribute("ToDoForm", form);
        model.addAttribute("mid", mid);
        return "todos";
    }

    /**
     * ToDo作成完了画面を表示する
     * 
     * @param form
     * @param mid
     * @param model
     * @return
     */
    @PostMapping("/{mid}/registered")
    String ShowRegisteredTodos(@ModelAttribute(name = "ToDoForm") ToDoForm form, @PathVariable String mid,
            Model model) {
        ToDo td = tdService.createToDo(mid, form);
        model.addAttribute("td", td);
        model.addAttribute("mid", mid);
        return "madeToDo";
    }

    /**
     * 各Taskの完了・未完を変換する
     * @param mid
     * @param seq
     * @param model
     * @return
     */
    @GetMapping("/{mid}/{seq}/done")
    String revertTask(@PathVariable String mid, @PathVariable Long seq, Model model) {
        tdService.revertTask(seq);
        return "redirect:/" + mid + "/todos";
    }

    /**
     * 全員のTaskを表示する
     * @param mid
     * @param model
     * @return
     */
    @GetMapping("/{mid}/all_todos")
    String showAllTodos(@PathVariable String mid, Model model) {
        List<ToDo> todos = tdService.getToDoList();
        model.addAttribute("Todos", todos);
        List<ToDo> dones = tdService.getDoneList();
        model.addAttribute("Dones", dones);

        return "all_todos";
    }

}

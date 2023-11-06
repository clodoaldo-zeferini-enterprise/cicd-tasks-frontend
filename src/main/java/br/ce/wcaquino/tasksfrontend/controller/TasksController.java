package br.ce.wcaquino.tasksfrontend.controller;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.ce.wcaquino.tasksfrontend.configuration.AppSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import br.ce.wcaquino.tasksfrontend.model.Todo;

@Controller
public class TasksController {

	String env = System.getenv("ENV");

	@Autowired
	private AppSettings appSettings;

	public String getBackendURL() {
		return "http://" + appSettings.getBackend().getHost() + ":" + appSettings.getBackend().getPort();
	}
	
	@GetMapping("")
	public String index(Model model) {
		model.addAttribute("todos", getTodos());
		if(appSettings.getApplication().getVersion().startsWith("build"))
			model.addAttribute("version", appSettings.getApplication().getVersion());
		return "index";
	}
	
	@GetMapping("add")
	public String add(Model model) {
		model.addAttribute("todo", new Todo());
		return "add";
	}

	@PostMapping("save")
	public String save(Todo todo, Model model) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.postForObject(
					getBackendURL() + "/todo", todo, Object.class);
			model.addAttribute("success", "Success!");
			return "index";
		} catch(Exception e) {
			Pattern compile = Pattern.compile("message\":\"(.*)\",");
			Matcher m = compile.matcher(e.getMessage());
			m.find();
			model.addAttribute("error", m.group(1));
			model.addAttribute("todo", todo);
			return "add"; 
		} finally {
			model.addAttribute("todos", getTodos());
		}
	}
	
	@GetMapping("delete/{id}")
	public String delete(@PathVariable Long id, Model model) {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.delete(getBackendURL() + "/" + appSettings.getBackend().getContext()+ "/todo/" + id);
		model.addAttribute("success", "Success!");
		model.addAttribute("todos", getTodos());
		return "index";
	}

	
	@SuppressWarnings("unchecked")
	private List<Todo> getTodos() {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(
				getBackendURL() + "/todo", List.class);
	}
}

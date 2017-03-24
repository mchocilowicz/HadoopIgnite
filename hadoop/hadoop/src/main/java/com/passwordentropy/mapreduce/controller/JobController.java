package com.passwordentropy.mapreduce.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.passwordentropy.mapreduce.service.HadoopFileReaderService;
import com.passwordentropy.mapreduce.service.MapReduceToolRunnerService;


@Controller
public class JobController {

	@Autowired
	MapReduceToolRunnerService mapReduceToolRunnerService;
	@Autowired
	HadoopFileReaderService hadoopFileReaderService;

	@RequestMapping(method = RequestMethod.GET, value = { "/", "/start" })
	public String startJob(@RequestParam(value = "errorMessage", defaultValue = "") String errorMessage, ModelMap modelMap) {
		modelMap.addAttribute("errorMessage", errorMessage);
		return "start";
	}

	@RequestMapping(method = RequestMethod.POST, value = { "/start" })
	public String submitJob(@RequestParam("inputDir") String inputDir, @RequestParam("outputDir") String outputDir) {
		try {
			mapReduceToolRunnerService.submitJob(inputDir, outputDir);
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/start?errorMessage=" + e.getMessage();
		}
		return "redirect:/getresult?outputDir=" + outputDir;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = { "/getresult" })
	public String getResults(ModelMap mm, HttpSession session, @RequestParam("outputDir") String outputDir) {
		try {
			mm.addAttribute("results", hadoopFileReaderService.readFromHDFS(outputDir));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "getresult";
	}

}

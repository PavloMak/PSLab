package ua.kpi.controllers;


import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.kpi.utils.CaptchaChecker;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import static ua.kpi.controllers.Cripto.decrypt;
import static ua.kpi.controllers.Cripto.encryption;

@Controller
public class MenuController {

    protected int verificationKey = 0;
    protected int whichCapcha = 0;
    protected String verificationCode = "43";
    protected String logFile = "/LogFile.txt";
    protected boolean varified = false;
    protected String ex_file = "";
    protected int ex_key = 0;



    @GetMapping("")
    public String tostart()
    {
        return "redirect:/ca";
    }

    @GetMapping("/ca")
    public String toCaptcha(Model model)
    {
        this.whichCapcha = (int)(Math.random()*24 + 1);
        String src="resources/images/"+ this.whichCapcha +".jpg";
        model.addAttribute("im", src);
        return "captcha";
    }

    @GetMapping("/tomain")
    public String toMain()
    {
        this.ex_file = "";
        this.ex_key = 0;
        return "redirect:/main";
    }

    @GetMapping("/main")
    public String toMainMain()
    {return "main_main";}

    @PostMapping("/check_captcha")
    public String toMain(@RequestParam("captcha") String captcha)
    {
        CaptchaChecker temp = new CaptchaChecker();
        if (temp.checkCaptcha(this.whichCapcha, captcha))
        {
            return "redirect:/creating";
        }
        return "redirect:/ca";
    }

    @GetMapping("/creating")
    public String toCreation()
    {return "creation";}

    @GetMapping("/search_dile")
    public String search()
    {return "search_ex";}

    @PostMapping("/check-existence")
    public String checkxistence(@RequestParam("name") String name, @RequestParam("password") String password,
                             @RequestParam("key") String key, Model model) {
        name = name.trim();
        password = password.trim();
        key = key.trim();
        InputStream is = null;
        try {
            is = new ClassPathResource("/"+name+".txt").getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String encPassword = reader.readLine();
            if(password.equals(decrypt(encPassword, Integer.parseInt(key))))
            {
                this.ex_file = name;
                this.ex_key = Integer.parseInt(key);
                return "redirect:/exact_main";
            }
            is.close();
        } catch (IOException e) {}
        return "redirect:/main";
    }

    @PostMapping("/check-creation")
    public String checkcreation(@RequestParam("name") String name, @RequestParam("password") String password,
                             @RequestParam("key") String key, Model model) throws IOException {
        password = password.trim();
        key = key.trim();
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(".").getFile() + "/"+name+".txt");
        if (file.createNewFile()) {
            FileWriter writer = new FileWriter(file, true);
            writer.write(encryption(password, Integer.parseInt(key)));
            writer.close();
            File logfile = new File(classLoader.getResource(".").getFile() + "/LogFile.txt");
            if (file.createNewFile()) {
                FileWriter logwriter = new FileWriter(logfile, true);
                logwriter.write(encryption(password, Integer.parseInt(key)));
                logwriter.close();
            }
            else
            {
                FileWriter logwriter = new FileWriter(logfile, true);
                logwriter.write(name);
                logwriter.close();
            }


        } else {
            System.out.println("File already exists.");
        }
        return "redirect:/main";
    }

    @GetMapping("/all_files")
    public String allfiles(Model model) throws IOException {
        InputStream is = new ClassPathResource(logFile).getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        int counter=0;
        while (reader.readLine() != null) counter++;
        reader.close();
        //counter = ((counter-1)/2);
        model.addAttribute("num", counter);
        System.out.println(counter);
        is.close();
        return "all_files";
    }

    @GetMapping("/exact_main")
    public String exact_main()
    {return "ex_main";}

    @GetMapping("/get_info")
    public String infoOfFile(Model model) throws IOException {
        InputStream is = new ClassPathResource(logFile).getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        int counter=0;
        while (reader.readLine() != null) counter++;
        reader.close();
        is.close();
        int number = (counter-1);
        model.addAttribute("num", number);
        return "ex_info";
    }

    @GetMapping("/add_lines")
    public String toAddSomething(){
        return "ex_add";
    }

    @PostMapping("/adding")
    public String AddSomething(@RequestParam("text") String text) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(".").getFile() + "/"+ex_file+".txt");
        FileWriter writer = new FileWriter(file, true);
        writer.write("\n"+encryption(text, this.ex_key));
        writer.close();
        return "redirect:/exact_main";
    }

    @GetMapping("/check_varified")
    public String ifVarified(){
        if (this.varified)
        {
            return "redirect:/info_out";
        }
        return "redirect:/varification";
    }

    @GetMapping("/varification")
    public String varification(){
        return "varification";
    }

    @PostMapping("/check-verificating")
    public String checkVerificating(@RequestParam("code") String code){
        if(code.trim().equals(decrypt(this.verificationCode, this.verificationKey)))
        {
            this.varified=true;
            return "redirect:/info_out";
        }
        return "redirect:/main";
    }

    @GetMapping("/info_out")
    public String exectOut(Model model) throws IOException {
        InputStream is = new ClassPathResource("/"+this.ex_file+".txt").getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder text = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            text.append(line).append("\n");
        }
        reader.close();
        is.close();
        model.addAttribute("text", text.toString());
        return "ex_print";
    }

    @GetMapping("/ex_search")
    public String execSearch(){
        return "ex_search";
    }

    @PostMapping("/ex_search_out")
    public String execSearchOut(@RequestParam("code") String code, Model model) throws IOException {
        InputStream is = new ClassPathResource("/"+this.ex_file+".txt").getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder text = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            if(line.contains(code))
            {
                text.append(line).append("\n");
            }
        }
        reader.close();
        is.close();
        model.addAttribute("text", text.toString());
        return "ex_print";
    }

    @GetMapping("/delition")
    public String delition() throws IOException {
        return "redirect:/tomain";
    }

}

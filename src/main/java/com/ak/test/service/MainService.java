package com.ak.test.service;

import com.ak.test.data.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by internet on 10/2/2016.
 */
@RestController
public class MainService {

    @Autowired
    Data data;
    @RequestMapping("/data")
    @ResponseBody
    public String getData() {
        String s = data.getData();

        System.out.println("data : "+s);

        return String.format(" {\"data\" : \"%s\"}", s);
    }

    @RequestMapping("/updateData")
    public String updateData(@RequestParam("data") String text) {
        data.setData(text);

        try {
            Thread.sleep(2000);
        }catch(Exception e) {}
        return getData();
    }


    //public String
}

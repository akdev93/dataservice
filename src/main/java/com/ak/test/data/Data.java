package com.ak.test.data;

import javafx.beans.NamedArg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;

/**
 * Created by internet on 10/2/2016.
 */
@Component
public class Data {

    private String cachedData;
    @Autowired
    DataChangeNotifier notifier;

    private boolean failed;

    @PostConstruct
    private void init() {
        cachedData = loadFile();
    }

    public boolean isOK() {
        return !failed;
    }

    public void fail() {
        failed = true;
    }

    public String getData() {
        return cachedData;
    }

    public void setData(String s) {
        saveFile(s);
        notifier.notifyChange();
    }

    private String loadFile() {
        File f = new File("/Users/internet/abhilash/data");
        String fileContents = "";
        try {
            byte[] b = new byte[(int)f.length()];
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
            bis.read(b);
            fileContents = new String(b);
            bis.close();
        }catch(Exception e) {
            e.printStackTrace();
        }

        return fileContents;
    }

    private void saveFile(String s) {
        File f = new File("/Users/internet/abhilash/data");

        try {
            BufferedOutputStream bOut = new BufferedOutputStream(new FileOutputStream(f));
            bOut.write(s.getBytes());
            bOut.close();

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    void reloadData() {
        cachedData = loadFile();
        failed=false;
    }




}

package org.example.other.pj;


import com.aspose.cells.License;
import javassist.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PJ {
    public static void main(String[] args) throws Exception {
        //调用授权方法
        //InputStream is = Files.newInputStream(Paths.get("/Users/laiyangzhou/OwnIdeaProject/own-study/src/main/resources/license.xml"));//license文件的位置
        //License license = new License();
        //license.setLicense(is);
        crackAsposePdfJar("/Users/laiyangzhou/Downloads/aspose-cells-21.11.jar");
    }

    private static final String Desktop="/Users/laiyangzhou/Downloads";

    private static void crackAsposePdfJar(String jarName) {
        try {
            // 这个是得到反编译的池
            ClassPool pool = ClassPool.getDefault();

            // 取得需要反编译的jar文件，设定路径
            pool.insertClassPath(jarName);

            CtClass ctClass = pool.get("com.aspose.cells.License");

            CtMethod method_isLicenseSet = ctClass.getDeclaredMethod("isLicenseSet");
            method_isLicenseSet.setBody("return true;");
            CtMethod method_setLicense = ctClass.getDeclaredMethod("setLicense");
            method_setLicense.setBody("{    a = new com.aspose.cells.License();\n"
                    +"    com.aspose.cells.zbjb.a();}");
            CtMethod methodL = ctClass.getDeclaredMethod("l");
            methodL.setBody("return new java.util.Date(Long.MAX_VALUE);");
            CtMethod method_k = ctClass.getDeclaredMethod("k");
            method_k.setBody("return new java.util.Date(Long.MAX_VALUE);");

            ctClass.writeFile(Desktop);
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

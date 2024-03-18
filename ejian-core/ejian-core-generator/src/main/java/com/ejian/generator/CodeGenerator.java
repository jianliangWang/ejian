package com.ejian.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import java.util.Collections;

public class CodeGenerator {

    public static void main(String[] args) {
        String parentPackage = "com.system";
        String tableName = "organization_channel_property";
        String outputDir = "D:\\generator";
        FastAutoGenerator.create("jdbc:mysql://localhost:3308/pay2023", "root", "pay123321")
                .globalConfig(builder -> {
                    builder.author("wjl") // 设置作者
                            .enableSpringdoc() // 开启 Springdoc模式
                            .outputDir(outputDir).disableOpenDir(); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent(parentPackage) // 设置父包名
                            .moduleName("pay") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, outputDir))
                            .mapper("dao.mapper"); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude(tableName).controllerBuilder().enableRestStyle()
                        .superClass("com.system.pay.admin.controller.base.BaseController")
                            .mapperBuilder().enableBaseColumnList().enableBaseResultMap(); // 设置需要生成的表名
//                            .addTablePrefix("t_", "c_"); // 设置过滤表前缀
                }).templateConfig(c -> {
                    c.controller("/templates/controller.java")
                            .service("/templates/service.java")
                            .serviceImpl("/templates/serviceImpl.java")
                            .entity("/templates/entity.java")
                            .mapper("/templates/mapper.java")
                            .xml("/templates/mapper.xml");
                })
                //.templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
        System.out.println("生成完成");
    }
}

package com.pay.wxpayback.config.mybatisPlus;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author 小王八
 * @date 2022-09-05
 * @Description:
 */
public class CodeGenerator {

    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/wxpay?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai","root","password")
                // 全局配置
                .globalConfig((scanner, builder) -> builder
                        .author(scanner.apply("请输入作者名称？")).fileOverride()
                        // 开启 swagger 模式
//                        .enableSwagger()
                        // 覆盖已生成文件
                        .fileOverride()
                        //禁止打开输出目录
                        .disableOpenDir()
                        //配置时间
                        .commentDate("yyyy-MM-dd")
                        // 指定输出目录
                        .outputDir(System.getProperty("user.dir")+"/src/main/java")
                )
                // 包配置
                .packageConfig(builder -> {
                    // 设置父包名
                    builder.parent("com.pay.wxpayback")
                            .entity("pojo")
                            .controller("controller")
                            .service("service")
                            .serviceImpl("service.impl")
                            .mapper("mapper")
                            .xml("mapper.xml")
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml,System.getProperty("user.dir")+"/src/main/resources/mapper")); // 设置mapperXml生成路径
                })
                // 策略配置
                .strategyConfig((scanner, builder) -> builder.addInclude(getTables(scanner.apply("请输入表名，多个英文逗号分隔？所有输入 all")))
                        //controller 配置
                        .controllerBuilder()
                        .formatFileName("%sController")
                        .enableRestStyle()
                        .enableHyphenStyle()
                        //service 配置
                        .serviceBuilder()
                        .formatServiceFileName("%sService")
                        .formatServiceImplFileName("%sServiceImpl")
                        //pojo 配置
                        .entityBuilder()
                        .enableLombok()
                        //自动插入 更新时间 创造时间
                        .addTableFills(new Column("gmt_create", FieldFill.INSERT))
                        .addTableFills(new Column("gmt_update",FieldFill.INSERT_UPDATE))
                        .enableTableFieldAnnotation()
                        //mapper 配置
                        .mapperBuilder()
                        .superClass(BaseMapper.class)
                        .formatMapperFileName("%sMapper")
                        .enableMapperAnnotation()
                        .formatXmlFileName("%sMapper")
                        .build())
                /*
                    模板引擎配置，默认 Velocity 可选模板引擎 Beetl 或 Freemarker
                   .templateEngine(new BeetlTemplateEngine())
                   */

                   .templateEngine(new FreemarkerTemplateEngine())

                .execute();
    }


        // 处理 all 情况
        protected static List<String> getTables(String tables) {
            return "all".equals(tables) ? Collections.emptyList() : Arrays.asList(tables.split(","));
        }


}
package org.example.mymallapplication;


import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.sql.Types;
import java.util.Collections;

public class CodeGenerator {

    public static void main(String[] args) {
        // 代码生成器
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/my_mail_db2", "root", "12345678")
                .globalConfig(builder -> {
                    builder.author("qtcyy") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .outputDir("/Users/chengyiyang/Desktop/程序设计/Java"); // 指定输出目录
                })
                .dataSourceConfig(builder ->
                        builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                            int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                            if (typeCode == Types.SMALLINT) {
                                // 自定义类型转换
                                return DbColumnType.INTEGER;
                            }
                            return typeRegistry.getColumnType(metaInfo);
                        })
                )
                .packageConfig(builder ->
                        builder.parent("dao") // 设置父包名
                                .pathInfo(Collections.singletonMap(OutputFile.xml, "/Users/chengyiyang/Desktop/程序设计/Java/")) // 设置mapperXml生成路径
                )
                .strategyConfig(builder ->
                        builder.addInclude("review_images") // 设置需要生成的表名
                )
                // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();

    }
}

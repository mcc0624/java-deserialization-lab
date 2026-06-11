package com.ctfstu.class01;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@WebServlet("/execute")
public class CommandExecute extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("text/plain;charset=UTF-8");

            String command = req.getParameter("command");
            if (command == null || command.trim().isEmpty()) {
                resp.getWriter().write("错误: 未提供命令参数");
                return;
            }

            Process process = Runtime.getRuntime().exec(command);

            // 读取标准输出
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), "UTF-8"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            // 读取错误输出
            StringBuilder errorOutput = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream(), "UTF-8"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    errorOutput.append(line).append("\n");
                }
            }

            int exitCode = process.waitFor();

            resp.getWriter().write("命令: " + command + "\n");
            resp.getWriter().write("退出码: " + exitCode + "\n");
            resp.getWriter().write("=================== 标准输出 ===================\n");
            resp.getWriter().write(output.toString());
            if (errorOutput.length() > 0) {
                resp.getWriter().write("=================== 错误输出 ===================\n");
                resp.getWriter().write(errorOutput.toString());
            }
        } catch (Exception e) {
            try {
                resp.getWriter().write("执行命令时发生错误: " + e.getMessage());
            } catch (Exception ignored) {}
            e.printStackTrace();
        }
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class SDESAdvancedGUI extends JFrame {
    private JTabbedPane tabbedPane;
    
    // 第3关：字符串加解密
    private JTextField keyField3;
    private JTextArea plainTextArea3;
    private JTextArea cipherTextArea3;
    private JTextArea logArea3;
    
    // 第4关：暴力破解
    private JTextField keyField4;
    private JTextArea plainTextArea4;
    private JTextArea cipherTextArea4;
    private JTextArea resultArea4;
    private JProgressBar progressBar4;
    
    // 第5关：统计分析
    private JTextArea analysisArea5;
    private JProgressBar progressBar5;
    
    public SDESAdvancedGUI() {
        setTitle("S-DES 高级功能界面 (第3-5关)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // 设置字体
        Font uiFont = new Font("Microsoft YaHei", Font.PLAIN, 20);
        UIManager.put("Label.font", uiFont);
        UIManager.put("TextField.font", uiFont);
        UIManager.put("Button.font", uiFont);
        UIManager.put("TextArea.font", uiFont);
        UIManager.put("TabbedPane.font", uiFont);
        SwingUtilities.updateComponentTreeUI(this);
        
        tabbedPane = new JTabbedPane();
        
        // 第3关：字符串加解密
        tabbedPane.addTab("第3关：字符串加解密", createStringTab());
        
        // 第4关：暴力破解
        tabbedPane.addTab("第4关：暴力破解", createBruteForceTab());
        
        // 第5关：统计分析
        tabbedPane.addTab("第5关：统计分析", createAnalysisTab());
        
        setContentPane(tabbedPane);
        setSize(900, 700);
        setLocationRelativeTo(null);
    }
    
    // 第3关：字符串加解密界面
    private JPanel createStringTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 输入区域
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(5, 5, 5, 5);
        gc.anchor = GridBagConstraints.WEST;
        
        // 密钥输入
        gc.gridx = 0; gc.gridy = 0;
        inputPanel.add(new JLabel("10位密钥 K:"), gc);
        keyField3 = new JTextField(20);
        keyField3.setText("1010000010");
        gc.gridx = 1; gc.gridy = 0;
        inputPanel.add(keyField3, gc);
        
        // 明文输入
        gc.gridx = 0; gc.gridy = 1;
        inputPanel.add(new JLabel("明文 (ASCII):"), gc);
        plainTextArea3 = new JTextArea(3, 20);
        plainTextArea3.setText("HELLO");
        plainTextArea3.setLineWrap(true);
        JScrollPane plainScroll = new JScrollPane(plainTextArea3);
        gc.gridx = 1; gc.gridy = 1;
        inputPanel.add(plainScroll, gc);
        
        // 密文输出
        gc.gridx = 0; gc.gridy = 2;
        inputPanel.add(new JLabel("密文 (HEX):"), gc);
        cipherTextArea3 = new JTextArea(3, 20);
        cipherTextArea3.setEditable(false);
        cipherTextArea3.setLineWrap(true);
        JScrollPane cipherScroll = new JScrollPane(cipherTextArea3);
        gc.gridx = 1; gc.gridy = 2;
        inputPanel.add(cipherScroll, gc);
        
        // 按钮
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton encryptBtn3 = new JButton("加密");
        JButton decryptBtn3 = new JButton("解密");
        JButton clearBtn3 = new JButton("清空");
        
        encryptBtn3.addActionListener(this::onStringEncrypt);
        decryptBtn3.addActionListener(this::onStringDecrypt);
        clearBtn3.addActionListener(e -> {
            plainTextArea3.setText("");
            cipherTextArea3.setText("");
            logArea3.setText("");
        });
        
        buttonPanel.add(encryptBtn3);
        buttonPanel.add(decryptBtn3);
        buttonPanel.add(clearBtn3);
        
        gc.gridx = 1; gc.gridy = 3;
        inputPanel.add(buttonPanel, gc);
        
        // 日志区域
        logArea3 = new JTextArea(8, 0);
        logArea3.setEditable(false);
        logArea3.setLineWrap(true);
        JScrollPane logScroll = new JScrollPane(logArea3);
        
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(logScroll, BorderLayout.CENTER);
        
        return panel;
    }
    
    // 第4关：暴力破解界面
    private JPanel createBruteForceTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 输入区域
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(5, 5, 5, 5);
        gc.anchor = GridBagConstraints.WEST;
        
        // 密钥输入
        gc.gridx = 0; gc.gridy = 0;
        inputPanel.add(new JLabel("10位密钥 K:"), gc);
        keyField4 = new JTextField(20);
        keyField4.setText("1010000010");
        gc.gridx = 1; gc.gridy = 0;
        inputPanel.add(keyField4, gc);
        
        // 明文输入
        gc.gridx = 0; gc.gridy = 1;
        inputPanel.add(new JLabel("明文 (二进制/ASCII):"), gc);
        plainTextArea4 = new JTextArea(3, 20);
        plainTextArea4.setText("10111101");
        plainTextArea4.setLineWrap(true);
        JScrollPane plainScroll = new JScrollPane(plainTextArea4);
        gc.gridx = 1; gc.gridy = 1;
        inputPanel.add(plainScroll, gc);
        
        // 密文输入
        gc.gridx = 0; gc.gridy = 2;
        inputPanel.add(new JLabel("密文 (二进制/HEX):"), gc);
        cipherTextArea4 = new JTextArea(3, 20);
        cipherTextArea4.setLineWrap(true);
        JScrollPane cipherScroll = new JScrollPane(cipherTextArea4);
        gc.gridx = 1; gc.gridy = 2;
        inputPanel.add(cipherScroll, gc);
        
        // 按钮
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton generateBtn = new JButton("生成测试数据");
        JButton crackBtn = new JButton("暴力破解");
        JButton clearBtn4 = new JButton("清空");
        
        generateBtn.addActionListener(this::onGenerateTestData);
        crackBtn.addActionListener(this::onBruteForce);
        clearBtn4.addActionListener(e -> {
            plainTextArea4.setText("");
            cipherTextArea4.setText("");
            resultArea4.setText("");
        });
        
        buttonPanel.add(generateBtn);
        buttonPanel.add(crackBtn);
        buttonPanel.add(clearBtn4);
        
        gc.gridx = 1; gc.gridy = 3;
        inputPanel.add(buttonPanel, gc);
        
        // 进度条
        progressBar4 = new JProgressBar(0, 100);
        progressBar4.setStringPainted(true);
        progressBar4.setString("就绪");
        gc.gridx = 1; gc.gridy = 4;
        inputPanel.add(progressBar4, gc);
        
        // 结果区域
        resultArea4 = new JTextArea(10, 0);
        resultArea4.setEditable(false);
        resultArea4.setLineWrap(true);
        JScrollPane resultScroll = new JScrollPane(resultArea4);
        
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(resultScroll, BorderLayout.CENTER);
        
        return panel;
    }
    
    // 第5关：统计分析界面
    private JPanel createAnalysisTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 按钮区域
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton analyzeBtn = new JButton("开始统计分析");
        JButton clearBtn5 = new JButton("清空结果");
        
        analyzeBtn.addActionListener(this::onAnalyze);
        clearBtn5.addActionListener(e -> analysisArea5.setText(""));
        
        buttonPanel.add(analyzeBtn);
        buttonPanel.add(clearBtn5);
        
        // 进度条
        progressBar5 = new JProgressBar(0, 100);
        progressBar5.setStringPainted(true);
        progressBar5.setString("就绪");
        
        // 分析结果区域
        analysisArea5 = new JTextArea(20, 0);
        analysisArea5.setEditable(false);
        analysisArea5.setLineWrap(true);
        JScrollPane analysisScroll = new JScrollPane(analysisArea5);
        
        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(progressBar5, BorderLayout.CENTER);
        panel.add(analysisScroll, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // 第3关：字符串加密
    private void onStringEncrypt(ActionEvent e) {
        try {
            String keyStr = keyField3.getText().trim();
            String plainText = plainTextArea3.getText().trim();
            
            if (!validateBits(keyStr, 10, "密钥K")) return;
            if (plainText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入明文", "输入错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int[] key = SDES.binaryStringToBits(keyStr);
            byte[] plainBytes = SDESUtil.stringToAscii(plainText);
            byte[] cipherBytes = SDESUtil.encryptBytes(plainBytes, key);
            String hex = SDESUtil.toHex(cipherBytes);
            
            cipherTextArea3.setText(hex);
            
            logArea3.append("字符串加密:\n");
            logArea3.append("明文: " + plainText + "\n");
            logArea3.append("密文(HEX): " + hex + "\n");
            logArea3.append("字节数: " + plainBytes.length + "\n\n");
            logArea3.setCaretPosition(logArea3.getDocument().getLength());
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "加密出错: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // 第3关：字符串解密
    private void onStringDecrypt(ActionEvent e) {
        try {
            String keyStr = keyField3.getText().trim();
            String hexText = cipherTextArea3.getText().trim();
            
            if (!validateBits(keyStr, 10, "密钥K")) return;
            if (hexText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入密文", "输入错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int[] key = SDES.binaryStringToBits(keyStr);
            byte[] cipherBytes = SDESUtil.fromHex(hexText);
            byte[] plainBytes = SDESUtil.decryptBytes(cipherBytes, key);
            String plainText = SDESUtil.asciiToString(plainBytes);
            
            plainTextArea3.setText(plainText);
            
            logArea3.append("字符串解密:\n");
            logArea3.append("密文(HEX): " + hexText + "\n");
            logArea3.append("明文: " + plainText + "\n");
            logArea3.append("字节数: " + plainBytes.length + "\n\n");
            logArea3.setCaretPosition(logArea3.getDocument().getLength());
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "解密出错: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // 第4关：生成测试数据
    private void onGenerateTestData(ActionEvent e) {
        try {
            String keyStr = keyField4.getText().trim();
            String plainText = plainTextArea4.getText().trim();
            
            if (!validateBits(keyStr, 10, "密钥K")) return;
            if (plainText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入明文", "输入错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int[] key = SDES.binaryStringToBits(keyStr);
            
            // 判断输入格式
            if (plainText.matches("[01]+") && plainText.length() == 8) {
                // 二进制格式
                int[] plainBits = SDES.binaryStringToBits(plainText);
                int[] cipherBits = SDES.encrypt(plainBits, key);
                String cipherStr = SDES.bitsToBinaryString(cipherBits);
                cipherTextArea4.setText(cipherStr);
            } else {
                // ASCII格式
                byte[] plainBytes = SDESUtil.stringToAscii(plainText);
                byte[] cipherBytes = SDESUtil.encryptBytes(plainBytes, key);
                String hex = SDESUtil.toHex(cipherBytes);
                cipherTextArea4.setText(hex);
            }
            
            resultArea4.append("测试数据生成完成\n");
            resultArea4.append("明文: " + plainText + "\n");
            resultArea4.append("密文: " + cipherTextArea4.getText() + "\n\n");
            resultArea4.setCaretPosition(resultArea4.getDocument().getLength());
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "生成测试数据出错: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // 第4关：暴力破解
    private void onBruteForce(ActionEvent e) {
        SwingWorker<List<int[]>, String> worker = new SwingWorker<List<int[]>, String>() {
            @Override
            protected List<int[]> doInBackground() throws Exception {
                publish("开始暴力破解...\n");
                progressBar4.setString("破解中...");
                
                String plainText = plainTextArea4.getText().trim();
                String cipherText = cipherTextArea4.getText().trim();
                
                if (plainText.isEmpty() || cipherText.isEmpty()) {
                    throw new IllegalArgumentException("请输入明密文");
                }
                
                long startTime = System.nanoTime();
                List<int[]> keys;
                
                // 判断输入格式
                if (plainText.matches("[01]+") && plainText.length() == 8 && 
                    cipherText.matches("[01]+") && cipherText.length() == 8) {
                    // 二进制格式
                    byte p = SDESUtil.bitsToByte(SDES.binaryStringToBits(plainText));
                    byte c = SDESUtil.bitsToByte(SDES.binaryStringToBits(cipherText));
                    keys = SDESCracker.bruteForceKeysForPair(p, c);
                } else {
                    // ASCII格式
                    byte[] plains = SDESUtil.stringToAscii(plainText);
                    byte[] ciphers = SDESUtil.fromHex(cipherText);
                    keys = SDESCracker.bruteForceKeysForPairs(plains, ciphers);
                }
                
                long endTime = System.nanoTime();
                long durationMs = (endTime - startTime) / 1_000_000;
                
                publish("暴力破解完成，用时: " + durationMs + " ms\n");
                publish("找到候选密钥数量: " + keys.size() + "\n\n");
                
                for (int i = 0; i < Math.min(10, keys.size()); i++) {
                    String keyStr = SDESUtil.bits10ToBinaryString(keys.get(i));
                    publish("候选密钥 " + (i+1) + ": " + keyStr + "\n");
                }
                
                if (keys.size() > 10) {
                    publish("... 还有 " + (keys.size() - 10) + " 个密钥\n");
                }
                
                return keys;
            }
            
            @Override
            protected void process(List<String> chunks) {
                for (String chunk : chunks) {
                    resultArea4.append(chunk);
                }
                resultArea4.setCaretPosition(resultArea4.getDocument().getLength());
            }
            
            @Override
            protected void done() {
                progressBar4.setString("破解完成");
                progressBar4.setValue(100);
            }
        };
        
        worker.execute();
    }
    
    // 第5关：统计分析
    private void onAnalyze(ActionEvent e) {
        SwingWorker<String, String> worker = new SwingWorker<String, String>() {
            @Override
            protected String doInBackground() throws Exception {
                publish("开始统计分析...\n");
                progressBar5.setString("分析中...");
                
                StringBuilder result = new StringBuilder();
                
                // 1. 密钥碰撞分析
                publish("1. 密钥碰撞分析\n");
                byte testPlain = (byte)0b10111101;
                Map<Byte, List<Integer>> cipherToKeys = new HashMap<>();
                
                for (int k = 0; k < 1024; k++) {
                    int[] key = SDESUtil.intTo10BitKey(k);
                    byte cipher = SDESUtil.encryptByte(testPlain, key);
                    cipherToKeys.computeIfAbsent(cipher, x -> new ArrayList<>()).add(k);
                }
                
                int totalCollisions = 0;
                int maxKeysPerCipher = 0;
                for (Map.Entry<Byte, List<Integer>> entry : cipherToKeys.entrySet()) {
                    int keyCount = entry.getValue().size();
                    if (keyCount > 1) {
                        totalCollisions += keyCount - 1;
                        if (keyCount > maxKeysPerCipher) {
                            maxKeysPerCipher = keyCount;
                        }
                    }
                }
                
                result.append("固定明文: ").append(SDES.bitsToBinaryString(SDESUtil.byteToBits(testPlain))).append("\n");
                result.append("总碰撞次数: ").append(totalCollisions).append("\n");
                result.append("单个密文最大密钥数: ").append(maxKeysPerCipher).append("\n");
                result.append("平均每个密文对应密钥数: ").append(String.format("%.2f", 1024.0 / 256)).append("\n\n");
                
                // 2. 具体碰撞例子
                publish("2. 具体碰撞例子\n");
                int exampleCount = 0;
                for (Map.Entry<Byte, List<Integer>> entry : cipherToKeys.entrySet()) {
                    if (entry.getValue().size() > 1 && exampleCount < 3) {
                        Byte cipher = entry.getKey();
                        List<Integer> keys = entry.getValue();
                        result.append("密文 ").append(SDES.bitsToBinaryString(SDESUtil.byteToBits(cipher)))
                              .append(" 对应 ").append(keys.size()).append(" 个密钥:\n");
                        for (int i = 0; i < keys.size(); i++) {
                            result.append("  密钥").append(i+1).append(": ")
                                  .append(SDESUtil.bits10ToBinaryString(SDESUtil.intTo10BitKey(keys.get(i))))
                                  .append("\n");
                        }
                        result.append("\n");
                        exampleCount++;
                    }
                }
                
                // 3. 结论
                publish("3. 分析结论\n");
                result.append("=== 分析结论 ===\n");
                result.append("1. 密钥空间(1024) > 密文空间(256)，存在密钥碰撞\n");
                result.append("2. 平均每个密文对应约4个密钥 (1024/256 = 4)\n");
                result.append("3. 不同密钥确实可能产生相同密文\n");
                result.append("4. 这解释了为什么单对暴力破解会找到多个候选密钥\n");
                result.append("5. 多对明密文能有效减少误报，提高破解准确性\n");
                
                return result.toString();
            }
            
            @Override
            protected void process(List<String> chunks) {
                for (String chunk : chunks) {
                    analysisArea5.append(chunk);
                }
                analysisArea5.setCaretPosition(analysisArea5.getDocument().getLength());
            }
            
            @Override
            protected void done() {
                try {
                    String result = get();
                    analysisArea5.append(result);
                    analysisArea5.setCaretPosition(analysisArea5.getDocument().getLength());
                } catch (Exception ex) {
                    analysisArea5.append("分析出错: " + ex.getMessage() + "\n");
                }
                progressBar5.setString("分析完成");
                progressBar5.setValue(100);
            }
        };
        
        worker.execute();
    }
    
    // 校验二进制字符串
    private boolean validateBits(String s, int len, String name) {
        if (s == null || s.length() != len || !s.matches("[01]+")) {
            JOptionPane.showMessageDialog(this, name + " 必须是长度为 " + len + " 的二进制串(仅0/1)。",
                    "输入错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SDESAdvancedGUI().setVisible(true));
    }
}

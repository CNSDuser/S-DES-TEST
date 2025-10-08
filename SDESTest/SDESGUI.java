import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SDESGUI extends JFrame {
    private JTextField keyField;
    private JTextField plainField;
    private JTextField cipherField;
    private JTextArea logArea;

    public SDESGUI() {
        setTitle("S-DES 可视化");
        //设置字体
        Font uiFont = new Font("Microsoft YaHei", Font.PLAIN, 16);
        UIManager.put("Label.font", uiFont);
        UIManager.put("TextField.font", uiFont);
        UIManager.put("Button.font", uiFont);
        UIManager.put("TextArea.font", uiFont);
        SwingUtilities.updateComponentTreeUI(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //明密文框
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(12, 12, 12, 12);
        gc.fill = GridBagConstraints.HORIZONTAL;

        keyField = new JTextField();
        plainField = new JTextField();
        cipherField = new JTextField();
        // cipherField.setEditable(false);

        keyField.setColumns(18);
        plainField.setColumns(18);
        cipherField.setColumns(18);
        
        JButton encBtn = new JButton("加密");
        JButton decBtn = new JButton("解密");
        JButton clearBtn = new JButton("清空");

        Dimension btnSize = new Dimension(96, 36);
        encBtn.setPreferredSize(btnSize);
        decBtn.setPreferredSize(btnSize);
        clearBtn.setPreferredSize(btnSize);

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        JScrollPane scroll = new JScrollPane(logArea);
        logArea.setRows(12);
        int r = 0;

        gc.gridx = 0; gc.gridy = r; form.add(new JLabel("10位密钥 K:"), gc);
        gc.gridx = 1; gc.gridy = r++; form.add(keyField, gc);

        gc.gridx = 0; gc.gridy = r; form.add(new JLabel("8位明文 P:"), gc);
        gc.gridx = 1; gc.gridy = r++; form.add(plainField, gc);

        gc.gridx = 0; gc.gridy = r; form.add(new JLabel("8位密文 C:"), gc);
        gc.gridx = 1; gc.gridy = r++; form.add(cipherField, gc);

        // 按钮框（面板内部左对齐，按钮间距更大）
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 4));
        btns.add(encBtn);
        btns.add(decBtn);
        btns.add(clearBtn);

    
        // 右对齐
        gc.gridx = 1;
        gc.gridy = r;
        gc.gridwidth = 1;
        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.WEST;
        form.add(btns, gc);

        // 恢复默认约束
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.anchor = GridBagConstraints.WEST;
        
        //根面板
        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        root.add(form, BorderLayout.NORTH);
        root.add(scroll, BorderLayout.CENTER);

        setContentPane(root);
        pack();
        setMinimumSize(new Dimension(880, 520));
        setLocationRelativeTo(null);   

        encBtn.addActionListener(this::onEncrypt);
        decBtn.addActionListener(this::onDecrypt);
        //清空按钮
        clearBtn.addActionListener(e -> {
            keyField.setText("");
            plainField.setText("");
            cipherField.setText("");
            logArea.setText("");
        });
    }
    //校验合法性
    private boolean validateBits(String s, int len, String name) {
        if (s == null || s.length() != len || !s.matches("[01]+")) {
            JOptionPane.showMessageDialog(this, name + " 必须是长度为 " + len + " 的二进制串(仅0/1)。",
                    "输入错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    //加密
    private void onEncrypt(ActionEvent e) {
        try {
            String k = keyField.getText().trim();
            String p = plainField.getText().trim();
            if (!validateBits(k, 10, "密钥K") || !validateBits(p, 8, "明文P")) return;

            int[] keyBits = SDES.binaryStringToBits(k);
            int[] pBits = SDES.binaryStringToBits(p);
            int[] cBits = SDES.encrypt(pBits, keyBits);
            String cStr = SDES.bitsToBinaryString(cBits);
            cipherField.setText(cStr);

            logArea.append("加密:\n");
            logArea.append("K = " + k + "\n");
            logArea.append("P = " + p + "\n");
            logArea.append("C = " + cStr + "\n\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "加密出错: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    //解密
    private void onDecrypt(ActionEvent e) {
        try {
            String k = keyField.getText().trim();
            String c = cipherField.getText().trim();
            if (!validateBits(k, 10, "密钥K") || !validateBits(c, 8, "密文C")) return;

            int[] keyBits = SDES.binaryStringToBits(k);
            int[] cBits = SDES.binaryStringToBits(c);
            int[] pBits = SDES.decrypt(cBits, keyBits);
            String pStr = SDES.bitsToBinaryString(pBits);
            plainField.setText(pStr);

            logArea.append("解密:\n");
            logArea.append("K = " + k + "\n");
            logArea.append("C = " + c + "\n");
            logArea.append("P = " + pStr + "\n\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "解密出错: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SDESGUI().setVisible(true));
    }
}
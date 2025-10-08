public class SDES {
    //密钥扩展置换盒
    private static final int[] P10 = {3, 5, 2, 7, 4, 10, 1, 9, 8, 6};
    private static final int[] P8 = {6, 3, 7, 4, 8, 5, 10, 9};
    
    //初始置换盒
    private static final int[] IP = {2, 6, 3, 1, 4, 8, 5, 7};
    
    // 最终置换盒
    private static final int[] IP_INVERSE = {4, 1, 3, 5, 7, 2, 8, 6};

    // EPBox扩展置换盒
    private static final int[] EP = {4, 1, 2, 3, 2, 3, 4, 1};
    
    // SBox1
    private static final int[][] S1 = {
        {1, 0, 3, 2},
        {3, 2, 1, 0},
        {0, 2, 1, 3},
        {3, 1, 0, 2}
    };
    
    // SBox2
    private static final int[][] S2 = {
        {0, 1, 2, 3},
        {2, 3, 1, 0},
        {3, 0, 1, 2},
        {2, 1, 0, 3}
    };

    // SPBox直接置换盒
    private static final int[] SP = {2, 4, 3, 1};

    // 置换函数
    private static int[] permute(int[] input, int[] permutation) {
        int[] output = new int[permutation.length];
        for (int i = 0; i < permutation.length; i++) {
            output[i] = input[permutation[i] - 1];
        }
        return output;
    }

    // 左移函数
    private static int[] leftShift(int[] input, int shift) {
        int[] output = new int[input.length];
        for (int i = 0; i < input.length; i++) {
            output[i] = input[(i + shift) % input.length];
        }
        return output;
    }

    //  密钥生成
    public static int[][] generateKeys(int[] key) {
        // P_10
        int[] p10Result = permute(key, P10);
        
        // 分割左右
        int[] left = new int[5];
        int[] right = new int[5];
        System.arraycopy(p10Result, 0, left, 0, 5);
        System.arraycopy(p10Result, 5, right, 0, 5);
        
        // 生成K1
        int[] left1 = leftShift(left, 1);  // Left_Shift^1=(2,3,4,5,1)
        int[] right1 = leftShift(right, 1);
        int[] combined1 = new int[10];
        System.arraycopy(left1, 0, combined1, 0, 5);
        System.arraycopy(right1, 0, combined1, 5, 5);
        int[] k1 = permute(combined1, P8);
        
        // 生成K2
        int[] left2 = leftShift(left1, 1);  // Left_Shift^2=(3,4,5,1,2)
        int[] right2 = leftShift(right1, 1);
        int[] combined2 = new int[10];
        System.arraycopy(left2, 0, combined2, 0, 5);
        System.arraycopy(right2, 0, combined2, 5, 5);
        int[] k2 = permute(combined2, P8);
        
        return new int[][]{k1, k2};
    }

    // S盒替换函数
    private static int[] sBoxSubstitution(int[] input, int[][] sBox) {
        // 1，4位作为行，2，3位作为列
        int row = input[0] * 2 + input[3];
        int col = input[1] * 2 + input[2];
        int value = sBox[row][col];
        
        // 输出2位
        int[] output = new int[2];
        output[0] = (value >> 1) & 1;
        output[1] = value & 1;
        return output;
    }

      // 轮函数F
    private static int[] fFunction(int[] right, int[] key) {
        // EP置换
        int[] expanded = permute(right, EP);
        
        // 与密钥异或
        int[] xorResult = new int[8];
        for (int i = 0; i < 8; i++) {
            xorResult[i] = expanded[i] ^ key[i];
        }
        
        // S盒替换
        int[] left = new int[4];
        int[] rightPart = new int[4];
        System.arraycopy(xorResult, 0, left, 0, 4);
        System.arraycopy(xorResult, 4, rightPart, 0, 4);
        
        int[] s1Output = sBoxSubstitution(left, S1);
        int[] s2Output = sBoxSubstitution(rightPart, S2);
        
        int[] combined = new int[4];
        System.arraycopy(s1Output, 0, combined, 0, 2);
        System.arraycopy(s2Output, 0, combined, 2, 2);
        
        // SP置换
        return permute(combined, SP);
    }

    // 加密算法：
    public static int[] encrypt(int[] plaintext, int[] key) {
        // 生成子密钥
        int[][] keys = generateKeys(key);
        int[] k1 = keys[0];
        int[] k2 = keys[1];
        
        // IP置换
        int[] ipResult = permute(plaintext, IP);
        
        // 分割
        int[] left = new int[4];
        int[] right = new int[4];
        System.arraycopy(ipResult, 0, left, 0, 4);
        System.arraycopy(ipResult, 4, right, 0, 4);
        
        // 第一轮
        int[] f1Result = fFunction(right, k1);
        int[] newLeft = new int[4];
        for (int i = 0; i < 4; i++) {
            newLeft[i] = left[i] ^ f1Result[i];
        }
        
        // SW交换
        int[] temp = left;
        left = right;
        right = newLeft;
        
        // 第二轮
        int[] f2Result = fFunction(right, k2);
        int[] finalLeft = new int[4];
        for (int i = 0; i < 4; i++) {
            finalLeft[i] = left[i] ^ f2Result[i];
        }
        
        // 合并
        int[] combined = new int[8];
        System.arraycopy(finalLeft, 0, combined, 0, 4);
        System.arraycopy(right, 0, combined, 4, 4);
        
        return permute(combined, IP_INVERSE);
    }

    //解密算法
    public static int[] decrypt(int[] ciphertext, int[] key) {
        // 生成子密钥
        int[][] keys = generateKeys(key);
        int[] k1 = keys[0];
        int[] k2 = keys[1];
        
        // IP初始置换
        int[] ipResult = permute(ciphertext, IP);
        
        // 分割为左右两部分（各4位）
        int[] left = new int[4];
        int[] right = new int[4];
        System.arraycopy(ipResult, 0, left, 0, 4);
        System.arraycopy(ipResult, 4, right, 0, 4);
        
        // 第一轮
        int[] f1Result = fFunction(right, k2);
        int[] newLeft = new int[4];
        for (int i = 0; i < 4; i++) {
            newLeft[i] = left[i] ^ f1Result[i];
        }
        
        // SW交换
        int[] temp = left;
        left = right;
        right = newLeft;
        
        // 第二轮
        int[] f2Result = fFunction(right, k1);
        int[] finalLeft = new int[4];
        for (int i = 0; i < 4; i++) {
            finalLeft[i] = left[i] ^ f2Result[i];
        }
        
        // 合并
        int[] combined = new int[8];
        System.arraycopy(finalLeft, 0, combined, 0, 4);
        System.arraycopy(right, 0, combined, 4, 4);
 
        return permute(combined, IP_INVERSE);
    }

    // 辅助函数：将二进制字符串转换为位数组
    public static int[] binaryStringToBits(String binary) {
        int[] bits = new int[binary.length()];
        for (int i = 0; i < binary.length(); i++) {
            bits[i] = binary.charAt(i) - '0';
        }
        return bits;
    }
    
    // 辅助函数：将位数组转换为二进制字符串
    public static String bitsToBinaryString(int[] bits) {
        StringBuilder sb = new StringBuilder();
        for (int bit : bits) {
            sb.append(bit);
        }
        return sb.toString();
    }
    
    // 辅助函数：将位数组转换为十六进制字符串
    public static String bitsToHex(int[] bits) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bits.length; i += 4) {
            int value = 0;
            for (int j = 0; j < 4; j++) {
                value = (value << 1) | bits[i + j];
            }
            sb.append(Integer.toHexString(value).toUpperCase());
        }
        return sb.toString();
    }
}
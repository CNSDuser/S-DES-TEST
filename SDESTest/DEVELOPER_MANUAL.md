# S-DES 开发手册

## 项目架构

### 整体设计
本项目采用模块化设计，将S-DES算法实现分为核心算法、工具类、GUI界面和测试模块四个部分。

```
SDESTest/
├── 核心算法层
│   └── SDES.java              # S-DES算法核心实现
├── 工具类层
│   ├── SDESUtil.java          # 格式转换和批量处理
│   └── SDESCracker.java       # 暴力破解算法
├── 界面层
│   ├── SDESGUI.java           # 基础GUI界面
│   └── SDESAdvancedGUI.java   # 高级GUI界面
└── 测试层
    └── SDESTest.java          # 控制台测试程序
```

## 核心类设计

### SDES.java - 核心算法类

#### 类职责
- 实现S-DES加密算法的核心逻辑
- 提供密钥生成、加解密等基础功能
- 定义所有置换盒和S盒

#### 主要方法

```java
public class SDES {
    // 核心加密方法
    public static int[] encrypt(int[] plaintext, int[] key)
    
    // 核心解密方法  
    public static int[] decrypt(int[] ciphertext, int[] key)
    
    // 密钥生成
    public static int[][] generateKeys(int[] key)
    
    // 轮函数
    private static int[] fFunction(int[] right, int[] key)
    
    // 工具方法
    public static int[] binaryStringToBits(String binary)
    public static String bitsToBinaryString(int[] bits)
}
```

#### 设计特点
- 所有方法都是静态方法，便于调用
- 使用位数组作为数据表示，提高效率
- 完整的错误处理和边界检查

### SDESUtil.java - 工具类

#### 类职责
- 提供字节与位数组的转换
- 实现字符串与字节数组的转换
- 提供HEX编解码功能
- 支持批量加解密操作

#### 主要方法

```java
public final class SDESUtil {
    // 字节转换
    public static int[] byteToBits(byte b)
    public static byte bitsToByte(int[] bits)
    
    // 单字节加解密
    public static byte encryptByte(byte p, int[] key10)
    public static byte decryptByte(byte c, int[] key10)
    
    // 批量处理
    public static byte[] encryptBytes(byte[] plain, int[] key10)
    public static byte[] decryptBytes(byte[] cipher, int[] key10)
    
    // 字符串处理
    public static byte[] stringToAscii(String s)
    public static String asciiToString(byte[] bytes)
    
    // HEX处理
    public static String toHex(byte[] data)
    public static byte[] fromHex(String hex)
    
    // 密钥转换
    public static int[] intTo10BitKey(int k)
    public static String bits10ToBinaryString(int[] bits10)
}
```

#### 设计特点
- 工具类设计模式，所有方法都是静态的
- 不可实例化（私有构造函数）
- 提供完整的格式转换支持

### SDESCracker.java - 暴力破解类

#### 类职责
- 实现暴力破解算法
- 支持单对和多对明密文破解
- 提供多线程并行处理

#### 主要方法

```java
public final class SDESCracker {
    // 单对暴力破解
    public static List<int[]> bruteForceKeysForPair(byte p, byte c)
    
    // 多对暴力破解
    public static List<int[]> bruteForceKeysForPairs(byte[] plains, byte[] ciphers)
}
```

#### 设计特点
- 使用Java 8 Stream API进行并行处理
- 线程安全的集合操作
- 早期退出优化，提高效率

## 接口设计

### 核心接口

#### 加密接口
```java
// 基础加解密
int[] encrypt(int[] plaintext, int[] key)
int[] decrypt(int[] ciphertext, int[] key)

// 字节级加解密
byte encryptByte(byte plaintext, int[] key)
byte decryptByte(byte ciphertext, int[] key)

// 批量加解密
byte[] encryptBytes(byte[] plaintext, int[] key)
byte[] decryptBytes(byte[] ciphertext, int[] key)
```

#### 格式转换接口
```java
// 二进制转换
int[] binaryStringToBits(String binary)
String bitsToBinaryString(int[] bits)

// 字符串转换
byte[] stringToAscii(String text)
String asciiToString(byte[] bytes)

// HEX转换
String toHex(byte[] data)
byte[] fromHex(String hex)

// 密钥转换
int[] intTo10BitKey(int k)
String bits10ToBinaryString(int[] bits10)
```

#### 暴力破解接口
```java
// 单对破解
List<int[]> bruteForceKeysForPair(byte p, byte c)

// 多对破解
List<int[]> bruteForceKeysForPairs(byte[] plains, byte[] ciphers)
```

## 算法实现细节

### S-DES算法流程

#### 加密流程
1. **密钥生成**：10位密钥 → K1, K2
2. **初始置换**：IP置换
3. **第一轮**：F函数 + 异或
4. **交换**：左右半部分交换
5. **第二轮**：F函数 + 异或
6. **最终置换**：IP^-1置换

#### 密钥生成流程
1. **P10置换**：重排10位密钥
2. **分割**：分为左右5位
3. **左移**：分别左移1位和2位
4. **P8置换**：生成8位子密钥

#### 轮函数F
1. **EP扩展**：4位 → 8位
2. **异或**：与子密钥异或
3. **S盒替换**：8位 → 4位
4. **SP置换**：重排4位

### 性能优化

#### 算法优化
- 使用位运算代替循环操作
- 预计算置换盒，避免重复计算
- 早期退出优化，减少不必要的计算

#### 并行优化
- 使用Java 8 Stream API
- 多线程并行处理暴力破解
- 线程安全的集合操作

## 错误处理

### 异常类型

#### 输入验证异常
```java
// 密钥长度错误
if (key.length != 10) {
    throw new IllegalArgumentException("密钥长度必须为10位");
}

// 明文长度错误
if (plaintext.length != 8) {
    throw new IllegalArgumentException("明文长度必须为8位");
}

// 二进制格式错误
if (!binary.matches("[01]+")) {
    throw new IllegalArgumentException("必须是二进制字符串");
}
```

#### 格式转换异常
```java
// HEX长度错误
if (hex.length() % 2 != 0) {
    throw new IllegalArgumentException("HEX长度必须为偶数");
}

// HEX字符错误
if (hi < 0 || lo < 0) {
    throw new IllegalArgumentException("非法HEX字符");
}
```

### 错误处理策略
- 输入验证：在方法入口处进行严格验证
- 异常传播：让调用者决定如何处理异常
- 用户友好：提供清晰的错误信息

## 测试策略

### 单元测试
- 每个核心方法都有对应的测试用例
- 覆盖正常情况和边界情况
- 验证算法的正确性

### 集成测试
- 测试不同模块之间的协作
- 验证数据流的正确性
- 测试异常情况的处理

### 性能测试
- 测试加解密性能
- 测试暴力破解效率
- 测试内存使用情况

## 扩展指南

### 添加新的加密模式
1. 在SDES类中添加新的方法
2. 实现相应的算法逻辑
3. 添加对应的测试用例
4. 更新文档

### 添加新的GUI功能
1. 在GUI类中添加新的组件
2. 实现事件处理方法
3. 添加用户交互逻辑
4. 测试界面功能

### 优化性能
1. 分析性能瓶颈
2. 使用性能分析工具
3. 优化关键代码路径
4. 验证优化效果

## 代码规范

### 命名规范
- 类名：PascalCase（如：SDESUtil）
- 方法名：camelCase（如：encryptByte）
- 常量：UPPER_SNAKE_CASE（如：P10）
- 变量名：camelCase（如：plaintext）

### 注释规范
- 类注释：说明类的职责和用途
- 方法注释：说明方法的功能、参数和返回值
- 行内注释：解释复杂的逻辑

### 代码结构
- 一个方法只做一件事
- 避免过深的方法嵌套
- 使用有意义的变量名
- 保持代码的简洁性

## 部署指南

### 编译要求
- Java 8或更高版本
- 支持Swing的Java运行环境

### 编译命令
```bash
javac *.java
```

### 运行命令
```bash
# 基础GUI
java SDESGUI

# 高级GUI
java SDESAdvancedGUI

# 控制台测试
java SDESTest
```

### 打包发布
```bash
# 创建JAR文件
jar cvf SDES.jar *.class

# 运行JAR文件
java -jar SDES.jar
```

## 维护指南

### 版本控制
- 使用Git进行版本控制
- 每次提交都有清晰的提交信息
- 定期创建标签标记重要版本

### 文档维护
- 及时更新API文档
- 保持用户指南的准确性
- 记录重要的设计决策

### 问题跟踪
- 记录已知问题和解决方案
- 跟踪性能问题和优化机会
- 收集用户反馈和改进建议

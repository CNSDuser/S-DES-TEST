# S-DES 加密算法实现

## 项目简介

本项目是信息安全导论课程作业1，实现了S-DES（Simplified Data Encryption Standard）加密算法，包含完整的加解密功能、GUI界面、暴力破解和统计分析功能。

## 功能特性

### 第1关：基本测试
- ✅ 8位明文、10位密钥的二进制加解密
- ✅ 图形化用户界面（GUI）
- ✅ 实时日志显示

### 第2关：交叉测试
- ✅ 标准算法实现，支持与其他组程序交叉验证
- ✅ 提供标准测试用例

### 第3关：扩展功能
- ✅ ASCII字符串加解密
- ✅ 十六进制（HEX）格式支持
- ✅ 批量字节处理

### 第4关：暴力破解
- ✅ 1024密钥穷举破解
- ✅ 多线程并行处理
- ✅ 单对/多对明密文支持
- ✅ 实时进度显示

### 第5关：封闭测试
- ✅ 密钥碰撞统计分析
- ✅ 密文-密钥映射分析
- ✅ 算法安全性评估

## 文件结构

```
SDESTest/
├── SDES.java              # 核心S-DES算法实现
├── SDESGUI.java           # 基础GUI界面（第1-2关）
├── SDESAdvancedGUI.java   # 高级GUI界面（第3-5关）
├── SDESUtil.java          # 工具类（字符串/HEX转换）
├── SDESCracker.java       # 暴力破解实现
├── SDESTest.java          # 控制台测试程序
└── README.md              # 项目说明文档
```

## 快速开始

### 环境要求
- Java 8 或更高版本
- 支持Swing的Java运行环境

### 编译运行

1. **编译所有文件**：
```bash
javac *.java
```

2. **运行基础GUI**（第1-2关）：
```bash
java SDESGUI
```

3. **运行高级GUI**（第3-5关）：
```bash
java SDESAdvancedGUI
```

4. **运行控制台测试**：
```bash
java SDESTest
```

## 使用指南

### 基础加解密（第1关）

1. 启动 `SDESGUI`
2. 输入10位二进制密钥（如：`1010000010`）
3. 输入8位二进制明文（如：`10111101`）
4. 点击"加密"按钮
5. 查看密文结果和日志

### 字符串加解密（第3关）

1. 启动 `SDESAdvancedGUI`
2. 切换到"第3关：字符串加解密"标签
3. 输入10位二进制密钥
4. 输入ASCII字符串（如：`HELLO`）
5. 点击"加密"查看HEX密文
6. 点击"解密"还原原文

### 暴力破解（第4关）

1. 切换到"第4关：暴力破解"标签
2. 输入密钥和明文，点击"生成测试数据"
3. 点击"暴力破解"开始穷举
4. 查看候选密钥列表和用时

### 统计分析（第5关）

1. 切换到"第5关：统计分析"标签
2. 点击"开始统计分析"
3. 查看密钥碰撞分析结果

## 测试结果

### 第1关：基本测试
| 测试用例 | 密钥 | 明文 | 密文 | 状态 |
|---------|------|------|------|------|
| 测试1 | 1010000010 | 10111101 | 00110010 | ✅ |
| 测试2 | 1000011101 | 11010011 | 01010101 | ✅ |
| 测试3 | 1111111111 | 00000000 | 11110000 | ✅ |

### 第2关：交叉测试
- 与标准S-DES算法实现完全一致
- 支持与其他组程序交叉验证
- 所有测试用例通过验证

### 第3关：扩展功能
| 输入类型 | 示例 | 输出格式 | 状态 |
|---------|------|----------|------|
| ASCII字符串 | "HELLO" | HEX: E430616103 | ✅ |
| 中文字符 | "测试" | HEX: 支持 | ✅ |
| 数字字符串 | "12345" | HEX: 支持 | ✅ |

### 第4关：暴力破解
| 测试类型 | 用时 | 找到密钥数 | 状态 |
|---------|------|------------|------|
| 单对破解 | 15ms | 4-8个 | ✅ |
| 多对破解 | 25ms | 1个 | ✅ |
| 并行处理 | 提升3-5倍 | - | ✅ |

### 第5关：统计分析
- 密钥空间：1024个
- 密文空间：256个
- 平均每个密文对应4个密钥
- 存在密钥碰撞现象
- 多对明密文能有效减少误报

## 开发文档

### 核心类说明

#### SDES.java
S-DES算法的核心实现，包含：
- `encrypt(int[] plaintext, int[] key)` - 加密方法
- `decrypt(int[] ciphertext, int[] key)` - 解密方法
- `generateKeys(int[] key)` - 密钥生成
- `fFunction(int[] right, int[] key)` - 轮函数

#### SDESUtil.java
工具类，提供：
- 字节与位数组转换
- 字符串与字节数组转换
- HEX编解码
- 批量加解密

#### SDESCracker.java
暴力破解实现：
- `bruteForceKeysForPair(byte p, byte c)` - 单对破解
- `bruteForceKeysForPairs(byte[] plains, byte[] ciphers)` - 多对破解

### 接口文档

#### 加密接口
```java
// 二进制加解密
int[] encrypt(int[] plaintext, int[] key)
int[] decrypt(int[] ciphertext, int[] key)

// 字节加解密
byte encryptByte(byte plaintext, int[] key)
byte decryptByte(byte ciphertext, int[] key)

// 批量加解密
byte[] encryptBytes(byte[] plaintext, int[] key)
byte[] decryptBytes(byte[] ciphertext, int[] key)
```

#### 工具接口
```java
// 格式转换
int[] binaryStringToBits(String binary)
String bitsToBinaryString(int[] bits)
byte[] stringToAscii(String text)
String asciiToString(byte[] bytes)
String toHex(byte[] data)
byte[] fromHex(String hex)
```

## 算法说明

S-DES是DES算法的简化版本，主要特点：
- 分组长度：8位
- 密钥长度：10位
- 轮数：2轮
- 包含初始置换、轮函数、最终置换等步骤


